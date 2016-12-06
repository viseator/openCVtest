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

    public OCR(String dataPath, String language){
        handlerNum = new OCRHandler();
        handlerNum.init(dataPath, "eng", OCRHandler.FILTER_NUM);
        handlerChi = new OCRHandler();
        handlerChi.init(dataPath, language, OCRHandler.FILTER_CHI);
        progressPic = new ProgressPic();
    }

    public OCR(String dataPath){
        this(dataPath, "chi_sim");
    }

    public void execute(String picPath, int numOfPic){
        tableInfo = progressPic.progress(picPath, numOfPic);
        for (int row = 0; row < tableInfo.getRowsSize(); row++) {
            StringBuffer resultOfColBuffer = new StringBuffer();
            for (int character = 0; character < tableInfo.getRows(row).getChaSize(); character++) {
                BufferedImage image;
                image = tableInfo.getRows(row).getBufferedImage(character);
                if(tableInfo.getRows(row).getDataType() <= 1){
                    resultOfColBuffer.append(handlerNum.getTextFromPic(image,
                            ITessAPI.TessPageSegMode.PSM_SINGLE_LINE));
                }else{
                    resultOfColBuffer.append(handlerChi.getTextFromPic(image,
                            ITessAPI.TessPageSegMode.PSM_SINGLE_LINE));
                }
            }
            String resultOfRow = OCRHandler.handleDetail(resultOfColBuffer.toString(), tableInfo.getRows(row).getDataType() <= 1);
            tableInfo.getRows(row).setResult(resultOfRow);
            System.out.println(resultOfRow);
        }
    }

}
