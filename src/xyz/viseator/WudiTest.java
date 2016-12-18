package xyz.viseator;

import org.opencv.core.Core;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Wudi
 * viseator@gmail.com
 * Created by viseator on 2016/12/12.
 */
public class WudiTest {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String args[]) {
        CutPic cutPic = new CutPic();
        HeaderOCR headerOCR = new HeaderOCR("C:\\Program Files (x86)\\Tesseract-OCR", "./dic.txt");
        cutPic.setOcr(new CutPic.RecognizeCharacters() {
            @Override
            public String recognize(ArrayList<BufferedImage> bufferedImages,int dataType) {
                return CharacterFixer.getRightIndex(cutPic.getIndexes(), headerOCR.getText(bufferedImages));
            }
        });
        for (int picId = 1; picId <= 37; picId++) {
            cutPic.progress("./image/1 (" + String.valueOf(picId) + ").jpg", picId);
        }

//        IndexReader indexReader = new IndexReader("./index.txt");
//        System.out.println(indexReader.getRowInfo("舒张压").getLeftBorder() + " " + indexReader.getRowInfo("舒张压").getRightBorder());
    }
}
