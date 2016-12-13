package xyz.viseator;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

/**
 * Created by viseator on 2016/12/1.
 */
public class OutputExcel {
    private String excelPath;
    private String outputPath;

    public OutputExcel(String excelPath, String outputPath) {
        this.excelPath = excelPath;
        this.outputPath = outputPath;
    }

    public void storeResultToExcel(TableInfo tableInfo) {
        Workbook workbook = getWorkBookFromPath();
        Sheet sheet = workbook.getSheetAt(0);

        for (int position = 0; position < tableInfo.getRowsSize(); position++) {
            int[] positionInExcel = tableInfo.getRows(position).getPositionInExcel();
            if (positionInExcel[0] != -1) {
                Row row = sheet.getRow(positionInExcel[0]);
                Cell cell = row.getCell(positionInExcel[1]);
                cell.setCellValue(tableInfo.getRows(position).getResult());
            }
        }
        outPutWorkBookToPath(workbook);
    }

    private Workbook getWorkBookFromPath() {
        InputStream inputStream;
        Workbook workbook = null;
        try {
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
