package com.yanhongbin.workutil.excel.cell;

import com.yanhongbin.workutil.excel.cell.factoryinterface.CellStyleFactory;
import org.apache.poi.ss.usermodel.*;

/**
 * Created with IDEA
 * description: 默认单元格样式,不使用自定义样式的时候使用
 *
 * @author :YanHongBin
 * @date :Created in 2019/12/26 15:46
 */
public class DefaultCellStyleFactory extends CellStyleFactory {
    

    public DefaultCellStyleFactory(Workbook workbook) {
        this.workbook = workbook;
    }

    public DefaultCellStyleFactory() {
    }

    @Override
    public synchronized CellStyle getCellStyle() {
        if (defaultCellStyle == null) {
            // 默认不加粗
            defaultCellStyle = initCellStyle(workbook,false);
        }
        return defaultCellStyle;
    }

    @Override
    public synchronized CellStyle getHeaderCellStyle() {
        if (headerCellStyle == null) {
            headerCellStyle = initCellStyle(workbook,true);
        }
        return headerCellStyle;
    }

    /**
     * 初始化样式
     * @param workbook 生成样式的单元格文件
     * @param bold 是否加粗
     * @return CellStyle
     */
    private CellStyle initCellStyle(Workbook workbook, boolean bold) {
        CellStyle cellStyle = workbook.createCellStyle();
        //设置水平居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        //设置垂直居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置下边框
        cellStyle.setBorderBottom(BorderStyle.THIN);
        //设置上边框
        cellStyle.setBorderTop(BorderStyle.THIN);
        //设置左边框
        cellStyle.setBorderLeft(BorderStyle.THIN);
        //设置右边框
        cellStyle.setBorderRight(BorderStyle.THIN);
        //设置字体
        Font font = workbook.createFont();
        //设置字体名称
        font.setFontName("Arial");
        //设置字号
        font.setFontHeightInPoints((short) 10);
        //设置是否为斜体
        font.setItalic(false);
        //设置是否加粗
        font.setBold(bold);
        //设置字体颜色
        font.setColor(IndexedColors.BLACK.index);
        cellStyle.setFont(font);
        //设置背景
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE1.index);
        return cellStyle;
    }

}
