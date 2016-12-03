package xyz.viseator;

import com.sun.jna.Pointer;
import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.TessAPI;
import net.sourceforge.tess4j.util.ImageIOHelper;
import org.opencv.core.Core;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Created by viseator on 2016/12/1.
 */
public class MainTest {

    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String args[]) {


        BufferedImage image = null;
        try {
            File file = new File("C:/Users/Lily/Desktop/test/1.jpg");
            image = ImageIO.read(new FileInputStream(file));
        }catch (IOException e){
            e.printStackTrace();
        }
        String result = new OCRHandler().getTextFromPic(image, ITessAPI.TessPageSegMode.PSM_SPARSE_TEXT, OCRHandler.FILTER_CHI);
        System.out.println(result);

//        ArrayList<BufferedImage> bufferedImages;
//        bufferedImages = new ProgressPic().progress("C:/Users/Lily/Desktop/test/resource/5.jpg", 5);
//
//        for (BufferedImage image : bufferedImages) {
//            String result = new OCRHandler().getTextFromPic(image, ITessAPI.TessPageSegMode.PSM_SPARSE_TEXT, OCRHandler.FILTER_CHI);
//            System.out.println(result);
//        }
    }

}
