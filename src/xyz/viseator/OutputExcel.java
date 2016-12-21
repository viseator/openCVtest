package xyz.viseator;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by viseator on 2016/12/1.
 */
public class OutputExcel {
    private String excelPath;
    private String outputPath;
    private Workbook workbook;
    private Sheet sheet;

    public OutputExcel(String excelPath, String outputPath) {
        this.excelPath = excelPath;
        this.outputPath = outputPath;
        workbook = getWorkBookFromPath();
        sheet = workbook.getSheetAt(0);
    }

    public void storeResultToExcel(ArrayList<RowInfo> rows) {

        for (RowInfo rowInfo : rows) {
            for (int rowNum = 1; rowNum < sheet.getLastRowNum(); rowNum++) {
                Row row = sheet.getRow(rowNum);
                Cell nameOfRowCell = row.getCell(2);
                if (Objects.equals(nameOfRowCell.getStringCellValue(), rowInfo.getNameOfRow())) {
                    Cell cell;
                    if (rowInfo.getDataType() == RowInfo.IS_STRING) cell = row.getCell(5);
                    else cell = row.getCell(4);
                    cell.setCellValue(rowInfo.getResult());
                    outPutWorkBookToPath(workbook);
                }
            }
        }
    }


    private Workbook getWorkBookFromPath() {
        InputStream inputStream;
        Workbook workbook = null;
        try {
            File file = new File(outputPath);
            if (file.exists())
                inputStream = new FileInputStream(outputPath);
            else
                inputStream = new FileInputStream(excelPath);

            workbook = new XSSFWorkbook(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return workbook;
    }

    private void outPutWorkBookToPath(Workbook workbook) {
        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream(outputPath);
            workbook.write(outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
