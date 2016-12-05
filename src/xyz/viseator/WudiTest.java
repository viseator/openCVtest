package xyz.viseator;

import net.sourceforge.tess4j.ITessAPI;
import org.opencv.core.Core;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by viseator on 2016/11/21.
 */
public class WudiTest {
    public static void main(String[] args) {
        ArrayList<ArrayList<BufferedImage>> bufferedImages;
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        final int pic = 4;
        final String PATH = "C:/Users/visea/Desktop/test/" +
                String.valueOf(pic) + ".jpg";
        ProgressPic progressPic;
        progressPic = new ProgressPic();
        bufferedImages = progressPic.progress(PATH, pic,true);
        BufferedImage image;
        for (int cols = 0; cols < bufferedImages.size(); cols++) {
            for (int character = 0;character<bufferedImages.get(cols).size();character++) {
                try {
                    image = bufferedImages.get(cols).get(character);
                    File file = new File("C:/Users/visea/Desktop/test/java/cut5/" +
                            String.valueOf(cols) + "_" + String.valueOf(character) + ".jpg");
                    ImageIO.write(image,"jpg",file);
                    String result = new OCRHandler().getTextFromPic(image, ITessAPI.TessPageSegMode.PSM_SINGLE_LINE,
                            OCRHandler.FILTER_CHI);
                    System.out.print(result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println();
        }
    }
}
