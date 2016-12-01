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
    public static void main(String args[]) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        BufferedImage image = null;
        ByteBuffer buf = null;
        String dataPath = "C:\\Program Files (x86)\\Tesseract-OCR";
        ProgressPic progressPic = new ProgressPic();
        ArrayList<BufferedImage> bufferedImages;
        bufferedImages = progressPic.progress("C:/Users/visea/Desktop/test/1.jpg", 1);
        for (int i = 0; i < bufferedImages.size(); i++) {
            image = bufferedImages.get(i);
            buf = ImageIOHelper.convertImageData(image);
            int bpp = image.getColorModel().getPixelSize();
            int bytespp = bpp / 8;
            int bytespl = (int) Math.ceil(image.getWidth() * bpp / 8.0);
            TessAPI api = TessAPI.INSTANCE;
            ITessAPI.TessBaseAPI handle = api.TessBaseAPICreate();
            api.TessBaseAPIInit3(handle, dataPath, "chi_sim");
            api.TessBaseAPISetImage(handle, buf, image.getWidth(), image.getHeight(), bytespp, bytespl);
            api.TessBaseAPISetPageSegMode(handle, ITessAPI.TessPageSegMode.PSM_SPARSE_TEXT);
            Pointer pointer = api.TessBaseAPIGetUTF8Text(handle);
            String result = pointer.getString(0);
            System.out.println(result);
        }
    }

}
