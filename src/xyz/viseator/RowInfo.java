package xyz.viseator;

import java.awt.image.BufferedImage;

/**
 * Wudi
 * viseator@gmail.com
 * Created by viseator on 2016/12/13.
 */
public class RowInfo {
    public static final int DATA_TYPE_NUMBER_0_1 = 0;
    public static final int DATA_TYPE_NUMBER_1_2 = 1;
    public static final int DATA_TYPE_STRING_0_1 = 2;
    public static final int DATA_TYPE_STRING_1_2 = 3;

    private int dataType;
    private int leftBorder;
    private int rightBorder;
    private BufferedImage contentImage;
    private String nameOfRow;
    private String result;

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
        switch (dataType) {
            case DATA_TYPE_NUMBER_0_1:
                leftBorder = 0;
                rightBorder = 1;
                break;
            case DATA_TYPE_NUMBER_1_2:
                leftBorder = 1;
                rightBorder = 2;
                break;
            case DATA_TYPE_STRING_0_1:
                leftBorder = 0;
                rightBorder = 1;
                break;
            case DATA_TYPE_STRING_1_2:
                leftBorder = 1;
                rightBorder = 2;
                break;
            default:
        }
    }

    public int getLeftBorder() {
        return leftBorder;
    }

    public int getRightBorder() {
        return rightBorder;
    }

    public BufferedImage getContentImage() {
        return contentImage;
    }

    public void setContentImage(BufferedImage contentImage) {
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
