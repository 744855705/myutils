package com.yanhongbin.workutil.excel.util;

import com.yanhongbin.workutil.excel.annonation.Excel;
import com.yanhongbin.workutil.excel.annonation.ExcelDictionary;
import com.yanhongbin.workutil.excel.exception.AnnotationNotFoundException;
import com.yanhongbin.workutil.excel.exception.ExcelDictionaryMatchException;
import com.yanhongbin.workutil.excel.exception.HeaderNotFindException;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import com.yanhongbin.workutil.excel.enumerate.CellType;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;


/**
 * Created with IDEA
 * description: excel工具类
 *              配合{@link Excel}注解使用
 *              基于org.apache.poi version 4.1.1
 *              JDK8
 * @author :YanHongBin
 * @version 1.0
 * @date :Created in 2019/12/4 13:53
 */
public class ExcelUtil {

    private static Logger log =  LoggerFactory.getLogger(ExcelUtil.class);

    private static Integer SHEET_SIZE = 65536;

    /**
     * 将导入的Excel文件进行处理,转化为对象的List
     *
     * @param clazz 对象类型(该类必须有无参数构造函数)
     * @param file  excel文件
     * @param <T>   声明的类型
     * @return List<T>
     */
    public static <T> List<T> excelToList(Class<T> clazz, MultipartFile file) throws Exception {
        return buildEntityList(
                clazz,
                createWorkbookFile(file)
        );
    }

    /**
     * 将对象列表转换为文件,写入response输出流
     *
     * @param clazz 类型
     * @param queue 实体list
     * @param <T>   声明的类型
     * @see ExcelUtil#createWorkbook(Queue, Class)
     */
    public static <T> void excelOutPut(Class<T> clazz, Queue<T> queue, HttpServletResponse response) throws HeaderNotFindException,IOException {
        createWorkbook(queue, clazz)
                .write(response.getOutputStream());
    }

    /**
     * 根据表头生成导入示例文档(表头需与注解{@link Excel} value值相同)
     *
     * @param clazz      要导入的对象类
     * @param properties 表头对应的字段
     * @param response   HttpServletResponse
     * @param <T>        声明的泛型
     * @throws Exception
     */
    public static <T> void exampleWorkbookOutPut(Class<T> clazz, HttpServletResponse response, String[] properties) throws HeaderNotFindException,IOException {
        createExampleWorkbook(clazz, properties)
                .write(response.getOutputStream());
    }

    /**
     * 生成导入示例文档(所有加了{@link Excel}的字段)
     *
     * @param clazz    要导入的对象类
     * @param response HttpServletResponse
     * @param <T>      声明的泛型
     * @throws Exception
     * @see ExcelUtil#exampleWorkbookOutPut(Class, HttpServletResponse, String[])
     */
    public static <T> void exampleWorkbookOutPut(Class<T> clazz, HttpServletResponse response) throws Exception {
        exampleWorkbookOutPut(
                clazz,
                response,
                new String[0]
        );
    }

    /**
     * 导入excel的时候,将excel文件转化为实体类list
     *
     * @param clazz    导入的实体类
     * @param workbook 导入的excel文件
     * @param <T>      声明的类型
     * @return List<T>
     * @throws Exception
     */
    private static <T> List<T> buildEntityList(Class<T> clazz, Workbook workbook) throws HeaderNotFindException, InstantiationException, IllegalAccessException{
        List<T> entityList = new ArrayList<>();
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            // 循环获取sheet
            Sheet sheet = workbook.getSheetAt(i);
            entityList.addAll(processSheet(sheet, clazz));
        }
        return entityList;
    }

    /**
     * 处理导入excel的sheet
     *
     * @param sheet 导入excel的sheet
     * @param clazz 导入的实体类
     * @param <T>   声明的类型
     * @return List<T>
     */
    private static <T> List<T> processSheet(Sheet sheet, Class<T> clazz) throws HeaderNotFindException, InstantiationException, IllegalAccessException {
        List<T> entityList = new ArrayList<>();
        // 获取表头
        Row firstRow = sheet.getRow(0);
        int rowNum = firstRow.getRowNum();
        String[] properties = new String[rowNum];
        for (int i = 0; i < rowNum; i++) {
            Cell cell = firstRow.getCell(i);
            String stringCellValue = cell.getStringCellValue();
            properties[i] = stringCellValue;
        }
        List<Field> header = buildHeader(clazz, properties);
        int lastRowNum = sheet.getLastRowNum();
        // 跳过表头,读取每行的信息
        for (int i = 1; i <= lastRowNum; i++) {
            Row row = sheet.getRow(i);
            entityList.add(processRow(row, clazz, header));
        }
        return entityList;
    }

    /**
     * 对单行Row 进行处理,返回处理后的实体对象
     *
     * @param row    row
     * @param clazz  对象类型
     * @param header 表头
     * @param <T>    声明的类型
     * @return T 类型的对象,对应单行的数据
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private static <T> T processRow(Row row, Class<T> clazz, List<Field> header) throws InstantiationException, IllegalAccessException {
        T t = clazz.newInstance();
        short lastCellNum = row.getLastCellNum();
        for (int i = 0; i <= lastCellNum; i++) {
            int[] index = {0};
            header.forEach(field -> {
                try {
                    Cell cell = row.getCell(index[0]++);
                    // 将获取到的字段值转换为对应的类型并放入实体中
                    field.set(t, field.getType().cast(getCellValue(cell, field)));
                } catch (IllegalAccessException | ExcelDictionaryMatchException e) {
                    e.printStackTrace();
                }
            });
        }
        return t;
    }

    /**
     * 根据Cell和对应的表头中的类型,获取cell的value
     *
     * @param cell  cell
     * @param field 表头字段
     * @return value(Object)
     */
    private static Object getCellValue(Cell cell, Field field) throws ExcelDictionaryMatchException {
        Class<?> fieldType = field.getType();
        // 获取Cell类型(支持字典自动设置为String)
        com.yanhongbin.workutil.excel.enumerate.CellType type = processExcelDictionaryCelltype(field);
        Object value;
        switch (type) {
            case STRING:
                // 对字典类型进行特殊处理
                value = processExcelDictionaryValue(cell.getStringCellValue(),field);
                if (fieldType.equals(Integer.class)) {
                    // 字典字段为int型
                    value = Integer.parseInt(String.valueOf(value));
                }
                break;
            case NUMERIC:
                // 数字取出会转换为Double类型,需进行处理
                Double doubleValue = cell.getNumericCellValue();
                long round = Math.round(doubleValue);
                if (Double.parseDouble(round + ".0") == doubleValue) {
                    // long型
                    value = round;
                    if (fieldType.equals(Integer.class)) {
                        // 字段为int型
                        value = Integer.parseInt(String.valueOf(round));
                    }
                } else {
                    // 浮点型
                    value = doubleValue;
                }
                break;
            case ERROR:
                value = cell.getErrorCellValue();
                break;
            case FORMULA:
                value = cell.getCellFormula();
                break;
            case BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case BLANK:
                value = new Object();
                break;
            default:
                // 默认按文本类型处理
                value = cell.getStringCellValue();
        }
        return value;
    }

    /**
     * 判断是否是字典类型,是则匹配字典,返回字典对应值,不是则直接返回字段值
     * @param value 字段对应值
     * @param field 字段本身
     * @return value
     * @throws ExcelDictionaryMatchException 标识为字典字段,但是字典值匹配失败,抛出异常
     */
    private static Object processExcelDictionaryValue(Object value, Field field) throws ExcelDictionaryMatchException {
        ExcelDictionary excelDictionary = field.getAnnotation(ExcelDictionary.class);
        if (excelDictionary != null) {
            String[] valueArray = excelDictionary.valueArray();
            int index = 0;
            for (String val : valueArray) {
                if (val.equals(value)) {
                    String[] keyArray = excelDictionary.keyArray();
                    return keyArray[index];
                }
                index++;
            }
            throw new ExcelDictionaryMatchException("字典匹配失败,请检查键值 " + value + " 是否存在于valueArray中");
        }else{
            return value;
        }

    }
    /**
     * 将传入的MultipartFile 类型的excel文件转换为poi的Workbook
     *
     * @param file 传入的MultipartFile 类型的excel文件
     * @return Workbook
     * @throws Exception
     */
    private static Workbook createWorkbookFile(MultipartFile file) throws IOException, EncryptedDocumentException {
        return WorkbookFactory.create(file.getInputStream());
    }

    /**
     * 构建导入导出时的表头
     *
     * @param clazz      导入或导出对应的类
     * @param properties 导入或导出对应的字段表头名
     * @param <T>        类型
     * @return List<Field>
     */
    private static <T> List<Field> buildHeader(Class<T> clazz, String[] properties) throws HeaderNotFindException {
        // 获取所有字段,包括父类中的字段,并且字段被@Excel注解修饰
        List<Field> fields = FieldUtil.getFieldsListWithAnnotation(clazz,Excel.class);
        List<Field> fieldList = new ArrayList<>(fields.size());
        if (properties == null || properties.length == 0) {
            // 默认拿出所有被Excel修饰的字段
            fields.forEach((field) -> {
                Excel excel = field.getAnnotation(Excel.class);
                if (excel != null) {
                    // 字段被@Excel修饰
                    field.setAccessible(true);
                    fieldList.add(field);
                }
            });
        } else {
            fields.forEach((field) -> {
                Excel excel = field.getAnnotation(Excel.class);
                if (excel != null) {
                    // 字段被@Excel修饰并且和properties中的表头名字相等
                    for (int i = 0; i < properties.length; i++) {
                        if (properties[i].equals(excel.value())) {
                            field.setAccessible(true);
                            fieldList.add(field);
                        }
                    }
                }
            });
            if (fieldList.size() != properties.length) {
                throw new HeaderNotFindException("传入的表头在实体类中不能一一对应");
            }
        }
        return fieldList;
    }



    /**
     * 构建excel文件
     *
     * @param queue 要导出的queue
     * @param clazz 对应的对象类型
     * @param <T>   声明的类型
     * @return Workbook
     */
    public static <T> Workbook createWorkbook(Queue<T> queue, Class<T> clazz) throws HeaderNotFindException{
        return createWorkbook(queue, clazz, new String[0]);
    }

    /**
     * 构建excel文件
     *
     * @param queue      要导出的queue
     * @param clazz      对应的对象类型
     * @param properties 导出的表头,全部导出传空
     * @param <T>        声明的类型
     * @return Workbook
     */
    public static <T> Workbook createWorkbook(Queue<T> queue, Class<T> clazz, String[] properties) throws HeaderNotFindException{
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 页码数
        double sheetNo = Math.ceil(queue.size() / SHEET_SIZE) + 1;
        for (int i = 0; i < sheetNo; i++) {
            createSheet(queue, workbook, clazz, properties);
            workbook.setSheetName(i, "第" + (i + 1) + "页");
        }
        return workbook;
    }

    /**
     * 创建sheet 最大65536
     *
     * @param queue      传入的对象列表建议使用LinkedList兼容List与队列
     * @param workbook   创建的Workbook对象
     * @param clazz      对应的类型
     * @param properties 表头字段名
     * @param <T>        声明的类型
     */
    private static <T> void createSheet(Queue<T> queue, Workbook workbook, Class<T> clazz, String[] properties) throws HeaderNotFindException {
        final Sheet sheet = workbook.createSheet();
        List<Field> fieldList = buildHeader(clazz, properties);
        // 构建表头
        createRowHeader(sheet, fieldList);
        int queueSize = queue.size();
        int sheetSize;
        if (queueSize < SHEET_SIZE) {
            // 只有一页
            sheetSize = queue.size();
        } else {
            // 有多页
            sheetSize = SHEET_SIZE;
        }
        // 避开表头,循环生成Row
        for (int i = 1; i <= sheetSize; i++) {
            Row iRow = sheet.createRow(i);
            createCell(queue, iRow, fieldList);
        }

    }

    /**
     * 创建Cell
     *
     * @param queue     队列
     * @param row       当前行对象
     * @param fieldList 导出的fieldlist
     * @param <T>       声明的类型
     */
    private static <T> void createCell(Queue<T> queue, Row row, List<Field> fieldList) {
        int[] index = {0};
        T poll = queue.poll();
        fieldList.forEach(field -> {
            Cell cell = row.createCell(index[0]++);
            // 取队列头,按照当前field指定的类型,放入单元格Cell
            try {
                setCellValueByType(field, cell, poll);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 将值放入Cell中
     *
     * @param field  字段
     * @param cell   cell对象
     * @param object 要放入的实体对象
     * @throws Exception
     */
    private static void setCellValueByType(Field field, Cell cell, Object object) throws Exception {
        CellType type = processExcelDictionaryCelltype(field);
        Object value = processExcelDictionaryKey(field, object);
        switch (type) {
            case STRING:
                cell.setCellType(org.apache.poi.ss.usermodel.CellType.STRING);
                cell.setCellValue(String.valueOf(value));
                break;
            case NUMERIC:
                cell.setCellType(org.apache.poi.ss.usermodel.CellType.NUMERIC);
                cell.setCellValue(Double.valueOf(String.valueOf(value)));
                break;
            case ERROR:
                cell.setCellType(org.apache.poi.ss.usermodel.CellType.ERROR);
                // 错误类型设置为空
                cell.setBlank();
                break;
            case FORMULA:
                cell.setCellType(org.apache.poi.ss.usermodel.CellType.FORMULA);
                cell.setCellValue(new HSSFRichTextString(String.valueOf(value)));
                break;
            case BOOLEAN:
                cell.setCellType(org.apache.poi.ss.usermodel.CellType.BOOLEAN);
                cell.setCellValue(Boolean.getBoolean(String.valueOf(value)));
                break;
            case BLANK:
                cell.setCellType(org.apache.poi.ss.usermodel.CellType.BLANK);
                cell.setBlank();
                break;
            default:
                // 默认按文本类型处理
                cell.setCellType(org.apache.poi.ss.usermodel.CellType.STRING);
                cell.setCellValue(String.valueOf(value));
        }
    }

    /**
     * 对字典类型进行特殊处理,单元格类型设置为文本
     * @param field 字段
     * @param
     * @return
     */
    private static CellType processExcelDictionaryCelltype(Field field) {
        ExcelDictionary excelDictionary = field.getAnnotation(ExcelDictionary.class);
        if (excelDictionary != null) {
            return CellType.STRING;
        } else {
            return field.getAnnotation(Excel.class).type();
        }
    }


    /**
     * 导出时处理字典类型,如果字段是字典类型,返回匹配后的值,不是则直接返回value
     *
     * @param field 字段
     * @param obj 要导出的实体
     * @return value
     * @throws IllegalAccessException Field::get方法抛出的异常
     * @throws ExcelDictionaryMatchException 标识为字典字段,但是字典值匹配失败,抛出此异常
     */
    public static Object processExcelDictionaryKey(Field field, Object obj) throws IllegalAccessException, ExcelDictionaryMatchException {
        ExcelDictionary excelDictionary = field.getAnnotation(ExcelDictionary.class);
        if (excelDictionary != null) {
            // 该字段对应字典
            String[] keyArray = excelDictionary.keyArray();
            // 导出时将字典类型与keyArray匹配
            int index = 0;
            for (String key : keyArray) {
                if (key.equals(String.valueOf(field.get(obj)))) {
                    String[] valueArray = excelDictionary.valueArray();
                    return valueArray[index];
                }
                index++;
            }
            // 字典匹配失败,抛出异常
            throw new ExcelDictionaryMatchException("字典匹配失败,请检查键值 " + field.get(obj) + " 是否存在于keyArray中");
        }
        // 不是字典类型,直接返回value
        return field.get(obj);
    }

    /**
     * 构建表头
     *
     * @param sheet     当前页sheet对象
     * @param fieldList 对应字段列表
     */
    private static void createRowHeader(Sheet sheet, List<Field> fieldList) {
        Row row = sheet.createRow(0);
        int size = fieldList.size();
        for (int i = 0; i < size; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(fieldList.get(i).getAnnotation(Excel.class).value());
        }
    }


    /**
     * 生成导入示例文档(class内被{@link Excel} 注解修饰的字段,并且其value等于 properties 中的一个)
     *
     * @param clazz      要导入的对象
     * @param properties 需要的表头(传空生成全部被{@link Excel}修饰的字段)
     * @param <T>        声明的类型
     * @return Workbook
     */
    public static <T> Workbook createExampleWorkbook(Class<T> clazz, String[] properties) throws HeaderNotFindException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        createRowHeader(workbook.createSheet(), buildHeader(clazz, properties));
        workbook.setSheetName(0, "第1页");
        return workbook;
    }

    /**
     * 生成示例文档(class内全部被{@link Excel} 注解修饰的字段)
     *
     * @param clazz class对象
     * @param <T>   声明的类型
     * @return Workbook
     */
    public static <T> Workbook createExampleWorkbook(Class<T> clazz) throws HeaderNotFindException{
        return createExampleWorkbook(clazz, new String[0]);
    }


    /**
     * 获取该类所有被{@link Excel}注解修饰的字段的{@link Excel#value()}
     * @param clazz 要查询的Class对象
     * @return
     * @throws AnnotationNotFoundException
     */
    public static String[] getAllProperties(Class<?> clazz) throws AnnotationNotFoundException {
        List<Field> fieldsListWithAnnotation = FieldUtil.getFieldsListWithAnnotation(clazz, Excel.class);
        if (fieldsListWithAnnotation.size() == 0) {
            throw new AnnotationNotFoundException(clazz,Excel.class);
        }
        final String[] properties = new String[fieldsListWithAnnotation.size()];
        final int[] index = {0};
        fieldsListWithAnnotation.forEach(field -> properties[index[0]++] = field.getAnnotation(Excel.class).value());
        return properties;
    }
}
