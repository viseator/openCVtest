package xyz.viseator;

import net.sourceforge.tess4j.ITessAPI;
import org.opencv.core.Core;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

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
//    private ProgressPic progressPic;
//    private TableInfo tableInfo;
//    private String excelPath;
//    private String outExcelPath;
    private Set<String> indexes;
    private CharacterFixer fixer;

    public OCR(String dataPath, String language, String dicPath, Set<String> indexes) {
        handlerNum = new OCRHandler();
        handlerNum.init(dataPath, dicPath, "eng", OCRHandler.FILTER_NUM);
        handlerChi = new OCRHandler();
        handlerChi.init(dataPath, dicPath, language, OCRHandler.FILTER_CHI);
//        progressPic = new ProgressPic();
        this.indexes = indexes;
    }

    public OCR(String dataPath, String dicPath, Set<String> indexes) {
        this(dataPath, "chi_sim", dicPath, indexes);
    }

    public String execute(ArrayList<BufferedImage> images, int isNum, boolean isIndex){
        StringBuilder builder = new StringBuilder();
        for(BufferedImage image : images){
            builder.append((isNum == RowInfo.IS_STRING) ? handlerChi.getTextFromPic(image, ITessAPI.TessPageSegMode.PSM_SINGLE_LINE)
                : handlerNum.getTextFromPic(image, ITessAPI.TessPageSegMode.PSM_SINGLE_LINE));
        }
        System.out.println(builder.toString());
        return (isIndex) ? OCRHandler.handleIndex(indexes, OCRHandler.handleDetail(builder.toString(), false))
                : OCRHandler.handleDetail(builder.toString(), isNum == RowInfo.IS_NUM);
    }

    public void executeTest(String picPath) {

        try {
            System.out.println(handlerChi.getTextFromPic(ImageIO.read(new File(picPath)), ITessAPI.TessPageSegMode.PSM_SINGLE_LINE));
        }catch (IOException e){
            e.printStackTrace();
        }
//        tableInfo = progressPic.progress(picPath, numOfPic);
//        for (int row = 0; row < tableInfo.getRowsSize(); row++) {
//            StringBuffer resultOfRowBuffer = new StringBuffer();
//            for (int character = 0; character < tableInfo.getRows(row).getChaSize(); character++) {
//                BufferedImage image;
//                image = tableInfo.getRows(row).getBufferedImage(character);
//                if (tableInfo.getRows(row).getDataType() <= 1) {
//                    resultOfRowBuffer.append(handlerNum.getTextFromPic(image,
//                            ITessAPI.TessPageSegMode.PSM_SINGLE_LINE));
//                } else {
//                    resultOfRowBuffer.append(handlerChi.getTextFromPic(image,
//                            ITessAPI.TessPageSegMode.PSM_SINGLE_LINE));
//                }
//            }
//            String resultOfRow = OCRHandler.handleDetail(
//                    resultOfRowBuffer.toString(), tableInfo.getRows(row).getDataType() <= 1);
//            tableInfo.getRows(row).setResult(resultOfRow);
//        }
//
//        File file = new File(outExcelPath);
//        OutputExcel outputExcel;
//        if (file.exists()) {
//            outputExcel = new OutputExcel(outExcelPath, outExcelPath);
//        } else {
//            outputExcel = new OutputExcel(excelPath, outExcelPath);
//        }
//
//        outputExcel.storeResultToExcel(tableInfo);
//
//        for (int i = 0; i < tableInfo.getRowsSize(); i++) {
//            System.out.println(tableInfo.getRows(i).getResult());
//        }
    }

//    public void setExcelPath(String excelPath) {
//        this.excelPath = excelPath;
//    }
//
//    public void setOutExcelPath(String outExcelPath) {
//        this.outExcelPath = outExcelPath;
//    }
}
