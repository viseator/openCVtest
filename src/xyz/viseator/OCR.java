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
    private String excelPath;
    private String outExcelPath;

    public OCR(String dataPath, String language, String dicPath) {
        handlerNum = new OCRHandler();
        handlerNum.init(dataPath, dicPath, "eng", OCRHandler.FILTER_NUM);
        handlerChi = new OCRHandler();
        handlerChi.init(dataPath, dicPath, language, OCRHandler.FILTER_CHI);
        progressPic = new ProgressPic();
    }

    public OCR(String dataPath, String dicPath) {
        this(dataPath, "chi_sim", dicPath);
    }

    public void execute(String picPath, int numOfPic) {
        tableInfo = progressPic.progress(picPath, numOfPic);
        for (int row = 0; row < tableInfo.getRowsSize(); row++) {
            StringBuffer resultOfRowBuffer = new StringBuffer();
            for (int character = 0; character < tableInfo.getRows(row).getChaSize(); character++) {
                BufferedImage image;
                image = tableInfo.getRows(row).getBufferedImage(character);
                if (tableInfo.getRows(row).getDataType() <= 1) {
                    resultOfRowBuffer.append(handlerNum.getTextFromPic(image,
                            ITessAPI.TessPageSegMode.PSM_SINGLE_LINE));
                } else {
                    resultOfRowBuffer.append(handlerChi.getTextFromPic(image,
                            ITessAPI.TessPageSegMode.PSM_SINGLE_LINE));
                }
            }
            String resultOfRow = OCRHandler.handleDetail(
                    resultOfRowBuffer.toString(), tableInfo.getRows(row).getDataType() <= 1);
            tableInfo.getRows(row).setResult(resultOfRow);
        }

        File file = new File(outExcelPath);
        OutputExcel outputExcel;
        if (file.exists()) {
            outputExcel = new OutputExcel(outExcelPath, outExcelPath);
        } else {
            outputExcel = new OutputExcel(excelPath, outExcelPath);
        }

        outputExcel.storeResultToExcel(tableInfo);

        for (int i = 0; i < tableInfo.getRowsSize(); i++) {
            System.out.println(tableInfo.getRows(i).getResult());
        }
    }

    public void setExcelPath(String excelPath) {
        this.excelPath = excelPath;
    }

    public void setOutExcelPath(String outExcelPath) {
        this.outExcelPath = outExcelPath;
    }
}
