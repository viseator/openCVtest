package xyz.viseator;


import net.sourceforge.tess4j.ITessAPI;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Lily on 2016/12/13.
 * Email: yifengtang@unique.com
 */
public class HeaderOCR {

    private OCRHandler handler;

    public HeaderOCR(String dataPath, String language, String dicPath) {
        handler = new OCRHandler();
        handler.init(dataPath, dicPath, language, OCRHandler.FILTER_NO_SPECIAL);
    }

    public HeaderOCR(String dataPath, String dicPath){
        this(dataPath, "chi_sim", dicPath);
    }

    public String getText(ArrayList<BufferedImage> images){
        StringBuilder builder = new StringBuilder();
        for(BufferedImage image : images){
            builder.append(handler.getTextFromPic(image, ITessAPI.TessPageSegMode.PSM_SINGLE_CHAR));
        }
        return builder.toString();
    }

}
