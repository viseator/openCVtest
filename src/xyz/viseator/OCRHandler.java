package xyz.viseator;

import com.sun.jna.Pointer;
import net.sourceforge.lept4j.util.LeptUtils;
import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.TessAPI;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Set;

/**
 * Created by Lily on 2016/12/1.
 * Email: yifengtang@unique.com
 */
public class OCRHandler {

    public static final int FILTER_NUM = 1;
    public static final int FILTER_CHI = 2;
    public static final int FILTER_NO_SPECIAL = 3;

    private ITessAPI.TessBaseAPI handler;
    private TessAPI apiManager;
    public static String specialCha = "`~!@#$%^&*()_-+={}[]|\\:;\"'<>/?丨﹔.︰﹕˙·～‥‧′〃〝〞‵‘’『』「」“”…❞❝﹁﹂﹃﹄″〔〕【】﹝﹞〈〉﹙﹚《》｛｝﹛﹜︵︶︷︸︹︺︻︼︽︾︿﹀＜＞∩∪ˇ丶";
    public static String specialCha1 = "`~!川@#$%^&*_-+={}[]|\\:;\"'<>?丨﹔.︰﹕˙·～‥‧′〃〝〞‵‘’『』「」“”…❞❝﹁﹂﹃﹄″〔〕【】﹝﹞〈〉﹙﹚《》｛｝﹛﹜︵︶︷︸︹︺︻︼︽︾︿﹀＜＞∩∪ˇ丶";
    private static CharacterFixer fixer;

    public void init(String dataPath, String dicPath, String language, int filter) {
        apiManager = TessAPI.INSTANCE;
        handler = apiManager.TessBaseAPICreate();
        apiManager.TessBaseAPIInit3(handler, dataPath, language);
        apiManager.TessBaseAPISetVariable(handler,"enable_new_segsearch","0");
        fixer = new CharacterFixer(dicPath);
        switch (filter){
            case FILTER_NUM:
                apiManager.TessBaseAPISetVariable(handler, "tessedit_char_whitelist","0123456789.");
                break;
            case FILTER_CHI:
                apiManager.TessBaseAPISetVariable(handler, "tessedit_char_blacklist", specialCha);
                break;
            case FILTER_NO_SPECIAL:
                apiManager.TessBaseAPISetVariable(handler, "tessedit_char_blacklist", specialCha1);
        }
    }

    public String getTextFromPic(BufferedImage image, int mode){
        try {
            apiManager.TessBaseAPISetImage2(handler, LeptUtils.convertImageToPix(image));
        }catch (IOException e){
            e.printStackTrace();
        }
        apiManager.TessBaseAPISetPageSegMode(handler, mode);
        Pointer pointer = apiManager.TessBaseAPIGetUTF8Text(handler);
        String result = pointer.getString(0);
        return result != null ? result.trim() : null;
    }

    public static String handleDetail(String output, boolean isNum){
        return (isNum) ? handleNum(output) : handleChi(output);
    }

    private static String handleNum(String output){
        StringBuffer resultBuffer = new StringBuffer(output);
        boolean findDot = false;
        if(!output.contains(".")) {
            for (int i = 0; i < output.length(); i++) {
                if(output.charAt(i) == ' ' && output.charAt(i + 1) == ' '){
                    resultBuffer.setCharAt(i, '.');
                    break;
                }
            }
        }
        for(int i = 0, count = 0; i < resultBuffer.length() && count < 2; i++){
            if(findDot)
                count++;
            if(resultBuffer.charAt(i) == '.')
                if(!findDot && (i != resultBuffer.length() - 1)) {
                    findDot = true;
                }else{
                    resultBuffer.deleteCharAt(i);
                    i--;
                    count--;
                }
            if(resultBuffer.charAt(i) == ' ') {
                resultBuffer.deleteCharAt(i);
                i--;
                count--;
            }
        }
        return resultBuffer.toString();
    }

    private static String handleChi(String output){
        return fixer.fixChars(output);
    }

    public static String handleIndex(Set<String> indexes, String index){
        return CharacterFixer.getRightIndex(indexes, index);
    }

}
