package xyz.viseator;

import net.sourceforge.tess4j.ITessAPI;
import org.opencv.core.Core;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.*;

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
        System.out.println("Picture-cut has been done.");
        ExecutorService executorService = Executors.newCachedThreadPool();
        ArrayList<FutureTask<String>> rowsTasks = new ArrayList<>();
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
                System.out.println("OCR " + (row + 1) + " " + (character + 1) + " has been done.");
            }
            HandleDetailTask task = new HandleDetailTask(resultOfColBuffer.toString(), tableInfo.getRows(row).getDataType() <= 1);
            FutureTask<String> futureTask = new FutureTask<String>(task);
            executorService.submit(futureTask);
            rowsTasks.add(futureTask);
        }
        System.out.println("OCR has been done.");
        for(int i = 0; i < tableInfo.getRowsSize(); i++){
            try {
                System.out.println(rowsTasks.get(i).get());
            }catch (ExecutionException ee){
                ee.printStackTrace();
            }catch (InterruptedException ie){
                ie.printStackTrace();
            }

        }
        executorService.shutdown();
    }

    private class HandleDetailTask implements Callable<String>{

        private String input;
        private boolean isNum;

        public HandleDetailTask(String input, boolean isNum){
            this.input = input;
            this.isNum = isNum;
        }

        @Override
        public String call() throws Exception {
            return OCRHandler.handleDetail(input, isNum);
        }
    }

}
