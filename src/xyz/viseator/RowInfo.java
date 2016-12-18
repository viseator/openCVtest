package xyz.viseator;

import org.opencv.core.Mat;

import java.awt.image.BufferedImage;

/**
 * Wudi
 * viseator@gmail.com
 * Created by viseator on 2016/12/13.
 */
public class RowInfo {

    public static final int IS_STRING = 0;
    public static final int IS_NUM = 1;

    private int dataType;
    private int leftBorder;
    private int rightBorder;
    private Mat contentImage;
    private String nameOfRow;
    private String result;

    public RowInfo(){}

    public RowInfo(int dataType, int position, String name){
        setDataType(dataType, position);
        setNameOfRow(name);
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType, int position) {
        this.dataType = dataType;
        this.leftBorder = position;
        this.rightBorder = position + 1;
    }

    public int getLeftBorder() {
        return leftBorder;
    }

    public int getRightBorder() {
        return rightBorder;
    }

    public Mat getContentImage() {
        return contentImage;
    }

    public void setContentImage(Mat contentImage) {
        this.contentImage = contentImage;
    }

    public String getNameOfRow() {
        return nameOfRow;
    }

    public void setNameOfRow(String nameOfRow) {
        this.nameOfRow = nameOfRow;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
