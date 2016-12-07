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
        final int pic = 2;
        final String PATH = "C:/Users/visea/Desktop/test/" +
                String.valueOf(pic) + ".jpg";
        final String EXCEL_PATH = "C:/Users/visea/Desktop/test/交付表格.xlsx";
        final String OUTPUT_PATH = "C:/Users/visea/Desktop/test/交付表格_完成.xlsx";



        OCR ocr = new OCR("C:\\Program Files (x86)\\Tesseract-OCR","./dic.txt");
        ocr.execute(PATH, pic);


    }

}
