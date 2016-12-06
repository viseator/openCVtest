package xyz.viseator;

import net.sourceforge.tess4j.ITessAPI;
import org.opencv.core.Core;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by viseator on 2016/12/1.
 */
public class MainTest {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String args[]) {
        final int pic = 3;
        final String PATH = "C:/Users/visea/Desktop/test/" +
                String.valueOf(pic) + ".jpg";
        OCR ocr = new OCR("C:\\Program Files (x86)\\Tesseract-OCR");
        ocr.execute(PATH, pic);
    }

}
