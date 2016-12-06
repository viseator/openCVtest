package xyz.viseator;

import net.sourceforge.tess4j.ITessAPI;
import org.opencv.core.Core;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Lily on 2016/12/6.
 * Email: yifengtang@unique.com
 */
public class OCR {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private OCRHandler handlerNum;
    private OCRHandler handlerChi;
    private ProgressPic progressPic;
    private TableInfo tableInfo;

    public OCR(String dataPath, String language, String dicPath){
        handlerNum = new OCRHandler();
        handlerNum.init(dataPath, dicPath, "eng", OCRHandler.FILTER_NUM);
        handlerChi = new OCRHandler();
        handlerChi.init(dataPath, dicPath, language, OCRHandler.FILTER_CHI);
        progressPic = new ProgressPic();
    }

    public OCR(String dataPath, String dicPath){
        this(dataPath, "chi_sim", dicPath);
    }

    public void execute(String picPath, int numOfPic){
        tableInfo = progressPic.progress(picPath, numOfPic);
        for (int cols = 0; cols < tableInfo.getRowsSize(); cols++) {
            StringBuffer resultOfColBuffer = new StringBuffer();
            for (int character = 0; character < tableInfo.getRows(cols).getChaSize(); character++) {
                BufferedImage image;
                image = tableInfo.getRows(cols).getBufferedImage(character);
                if(tableInfo.getRows(cols).getDataType() <= 1){
                    resultOfColBuffer.append(handlerNum.getTextFromPic(image,
                            ITessAPI.TessPageSegMode.PSM_SINGLE_LINE));
                }else{
                    resultOfColBuffer.append(handlerChi.getTextFromPic(image,
                            ITessAPI.TessPageSegMode.PSM_SINGLE_LINE));
                }
            }
            String resultOfCol = OCRHandler.handleDetail(resultOfColBuffer.toString(), tableInfo.getRows(cols).getDataType() <= 1);
            System.out.println(resultOfCol);
        }
    }

}
