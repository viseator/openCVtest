package xyz.viseator;

import com.sun.istack.internal.Nullable;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import net.sourceforge.lept4j.Pix;
import net.sourceforge.lept4j.Pixa;
import net.sourceforge.lept4j.util.LeptUtils;
import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.TessAPI;
import net.sourceforge.tess4j.util.ImageIOHelper;

import javax.imageio.IIOException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by Lily on 2016/12/1.
 * Email: yifengtang@unique.com
 */
public class OCRHandler {

    public static final int FILTER_NONE = 0;
    public static final int FILTER_NUM = 1;
    public static final int FILTER_CHI = 2;
    public static final int FILTER_YIN_YANG = 3;

    private static final String dataPath = "C:\\Program Files (x86)\\Tesseract-OCR";
    private ITessAPI.TessBaseAPI handler;
    private TessAPI apiManager;
    private String specialCha = "`~!@#$%^&*()_-+={}[]|\\:;\"',<.>/?﹔︰﹕丶ˇ一﹐．﹒˙·～‥‧′〃〝〞‵‘’『』「」“”…❞❝﹁﹂﹃﹄″〔〕【】﹝﹞〈〉﹙﹚《》｛｝﹛﹜︵︶︷︸︹︺︻︼︽︾︿﹀＜＞∩∪";

    public String getTextFromPic(BufferedImage image, int mode, int filter){
        apiManager = TessAPI.INSTANCE;
        handler = apiManager.TessBaseAPICreate();
        apiManager.TessBaseAPIInit3(handler, dataPath, "chi_sim");

        try {
            apiManager.TessBaseAPISetImage2(handler, LeptUtils.convertImageToPix(image));
        }catch (IOException e){
            e.printStackTrace();
        }
        apiManager.TessBaseAPISetPageSegMode(handler, mode);
        switch (filter){
            case FILTER_NONE:
                break;
            case FILTER_NUM:
                apiManager.TessBaseAPISetVariable(handler, "tessedit_char_whitelist","0123456789.");
                break;
            case FILTER_CHI:
                apiManager.TessBaseAPISetVariable(handler, "tessedit_char_blacklist",specialCha);
                break;
            case FILTER_YIN_YANG:
                apiManager.TessBaseAPISetVariable(handler,"tessedit_char_whitelist","阴阳性");
                break;
        }
        Pointer pointer = apiManager.TessBaseAPIGetUTF8Text(handler);
        String result = pointer.getString(0);
        return result != null ? result.trim() : null;
    }

//    private String handleDetail(String output){
//        StringBuffer resultBuffer = new StringBuffer();
//
//
//    }

}
