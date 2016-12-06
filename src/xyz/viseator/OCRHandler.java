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
import java.util.*;

/**
 * Created by Lily on 2016/12/1.
 * Email: yifengtang@unique.com
 */
public class OCRHandler {

    public static final int FILTER_NUM = 1;
    public static final int FILTER_CHI = 2;

    private ITessAPI.TessBaseAPI handler;
    private TessAPI apiManager;
    private String specialCha = "`~!@#$%^&*()_-+={}[]|\\:;\"'<>/?丨﹔.︰﹕˙·～‥‧′〃〝〞‵‘’『』「」“”…❞❝﹁﹂﹃﹄″〔〕【】﹝﹞〈〉﹙﹚《》｛｝﹛﹜︵︶︷︸︹︺︻︼︽︾︿﹀＜＞∩∪ˇ丶";
    private Map<Character, ArrayList<Character>> wordsList;

    public void init(String dataPath, String language, int filter) {
        apiManager = TessAPI.INSTANCE;
        handler = apiManager.TessBaseAPICreate();
        apiManager.TessBaseAPIInit3(handler, dataPath, language);
        apiManager.TessBaseAPISetVariable(handler,"enable_new_segsearch","0");
        switch (filter){
            case FILTER_NUM:
                apiManager.TessBaseAPISetVariable(handler, "tessedit_char_whitelist","0123456789.");
                break;
            case FILTER_CHI:
                apiManager.TessBaseAPISetVariable(handler, "tessedit_char_blacklist",specialCha);
                break;
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
        if(!resultBuffer.toString().contains(".")) {
            for (int i = 0; i < resultBuffer.length(); i++) {
                if(resultBuffer.charAt(i) == ' ' && resultBuffer.charAt(i + 1) == ' '){
                    resultBuffer.setCharAt(i, '.');
                    break;
                }
            }
        }
        for(int i = 0; i < resultBuffer.length(); i++){
            if(resultBuffer.charAt(i) == ' ')
                resultBuffer.deleteCharAt(i);
        }
        return resultBuffer.toString();
    }

    private static String handleChi(String output){
//        StringBuffer resultBuffer = new StringBuffer(output);
//
//        return resultBuffer.toString();
        return output;
    }

    private void initialWordsList(){
        addWordList('壳', '亮', new Character[]{'度'});
        addWordList('菅', '管', new Character[]{'道','导'});
    }

    private void addWordList(Character aim, Character result, Character[] conditions){
        if(wordsList == null)
            wordsList = new HashMap<>();
        ArrayList<Character> lists = new ArrayList<>();
        lists.add(result);
        lists.addAll(Arrays.asList(conditions));
        wordsList.put(aim, lists);
    }

}
