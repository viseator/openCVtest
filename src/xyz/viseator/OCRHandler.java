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

    public static final int FILTER_NONE = 0;
    public static final int FILTER_NUM = 1;
    public static final int FILTER_CHI = 2;
    public static final int FILTER_YIN_YANG = 3;

    private static final String dataPath = "C:\\Program Files (x86)\\Tesseract-OCR";

    private ITessAPI.TessBaseAPI handler;
    private TessAPI apiManager;
    private String specialCha = "`~!@#$%^&*()_-+={}[]|\\:;\"'<>/?﹔︰﹕﹐．﹒˙·～‥‧′〃〝〞‵‘’『』「」“”…❞❝﹁﹂﹃﹄″〔〕【】﹝﹞〈〉﹙﹚《》｛｝﹛﹜︵︶︷︸︹︺︻︼︽︾︿﹀＜＞∩∪ˇ丶";
    private Map<Character, ArrayList<Character>> wordsList;

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
        String result = handleDetail(pointer.getString(0));
        return result != null ? result.trim() : null;
    }

    private String handleDetail(String output){
        initialWordsList();
        StringBuffer resultBuffer = new StringBuffer(output);
        Set<Character> errorList = wordsList.keySet();
        for(int i = 0; i < resultBuffer.length(); i++){
            if(errorList.contains(resultBuffer.charAt(i))){
                if((i != 0 && wordsList.get(resultBuffer.charAt(i)).contains(resultBuffer.charAt(i - 1))) ||
                        (i != resultBuffer.length() && wordsList.get(resultBuffer.charAt(i)).contains(resultBuffer.charAt(i + 1)))){
                    resultBuffer.setCharAt(i, wordsList.get(resultBuffer.charAt(i)).get(0));
                }
            }
        }
        return resultBuffer.toString();
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
