package com.yanhongbin.workutil.excel.cell.factoryinterface;

import com.yanhongbin.workutil.excel.cell.DefaultCellStyleFactory;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Created with IDEA
 * description: 实现该接口可以自定义单元格样式,
 *              如果不自定义样式工厂, 则默认使用{@link DefaultCellStyleFactory}
 *
 * @author :YanHongBin
 * @date :Created in 2019/12/26 16:22
 */
public interface CellStyleFactory {
    /**
     * 获取单元格样式
     *
     * @return CellStyle
     */
    CellStyle getCellStyle();

    /**
     * 获取表头样式
     *
     * @return CellStyle
     */
    CellStyle getHeaderCellStyle();

    /**
     * 传入要创建样式的单元格对象
     * @param workbook 要创建样式的单元格对象
     */
    void setWorkbook(Workbook workbook);
}
