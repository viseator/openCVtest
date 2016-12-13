package xyz.viseator;

import org.opencv.core.Core;

/**
 * Created by viseator on 2016/12/1.
 */
public class MainTest {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String args[]) {
        for (int pic = 1; pic <= 5; pic++) {
            final String PATH = "./image/" +
                    String.valueOf(pic) + ".jpg";
            final String EXCEL_PATH = "./image/交付表格.xlsx";
            final String OUT_EXCEL_PATH = "./交付表格.xlsx";

            OCR ocr = new OCR("C:\\Program Files (x86)\\Tesseract-OCR", "./dic.txt");
            ocr.setExcelPath(EXCEL_PATH);
            ocr.setOutExcelPath(OUT_EXCEL_PATH);
            ocr.execute(PATH, pic);
        }


    }

}
