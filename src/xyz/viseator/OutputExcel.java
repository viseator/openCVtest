package xyz.viseator;

import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by viseator on 2016/12/1.
 */
public class OutputExcel {
    private ArrayList<Map<Integer, String>> dataMapList;
    private static final String PATH = "C:/Users/visea/Desktop/test/交付表格.xlsx";

    public OutputExcel(ArrayList<Map<Integer, String>> dataMapList) {
        this.dataMapList = dataMapList;
    }

    public void execute() {
        InputStream inputStream = null;
        Workbook workbook = null;
        try {
            inputStream = new FileInputStream(PATH);
            workbook = WorkbookFactory.create(inputStream);
        } catch (InvalidFormatException | IOException e) {
            e.printStackTrace();
        }
        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(1);
        Cell cell = row.getCell(2   );
        CellReference cellReference = new CellReference(row.getRowNum(), cell.getColumnIndex());
        System.out.print(cell.getRichStringCellValue());


    }

}
