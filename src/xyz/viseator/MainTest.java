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
        final int pic = 4;
        final String PATH = "C:/Users/visea/Desktop/test/" +
                String.valueOf(pic) + ".jpg";
        OCR ocr = new OCR("C:\\Program Files (x86)\\Tesseract-OCR","./dic.txt");
        ocr.execute(PATH, pic);
    }

}
