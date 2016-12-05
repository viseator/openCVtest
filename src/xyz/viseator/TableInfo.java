package xyz.viseator;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by viseator on 2016/12/5.
 */
public class TableInfo {
    private int colsSize;
    public static final int DATA_TYPE_NUMBER_0_1 = 0;
    public static final int DATA_TYPE_NUMBER_1_2 = 1;
    public static final int DATA_TYPE_STRING_0_1 = 2;
    public static final int DATA_TYPE_STRING_1_2 = 3;
    private ArrayList<ColumnInfo> cols;

    public TableInfo(int colsSize) {
        this.colsSize = colsSize;
        cols = new ArrayList<>(colsSize);
    }

    public void initColumn(int position,int dataType) {
        ColumnInfo columnInfo = cols.get(position);
        switch (dataType) {
            case DATA_TYPE_NUMBER_0_1:
                columnInfo.setDataType(dataType);
                columnInfo.setBoundLeft(0);
                columnInfo.setBoundRight(1);
                break;
            case DATA_TYPE_NUMBER_1_2:
                columnInfo.setDataType(dataType);
                columnInfo.setBoundLeft(1);
                columnInfo.setBoundRight(2);
                break;

            case DATA_TYPE_STRING_0_1:
                columnInfo.setDataType(dataType);
                columnInfo.setBoundLeft(0);
                columnInfo.setBoundRight(1);
                break;

            case DATA_TYPE_STRING_1_2:
                columnInfo.setDataType(dataType);
                columnInfo.setBoundLeft(1);
                columnInfo.setBoundRight(2);
                break;
        }
    }

    public int getColsSize() {
        return colsSize;
    }


    public ArrayList<ColumnInfo> getCols() {
        return cols;
    }

    public void setCols(ArrayList<ColumnInfo> cols) {
        this.cols = cols;
    }

    private class ColumnInfo {

        private int dataType;
        private int boundLeft;
        private int boundRight;
        private BufferedImage bufferedImage;
        private String result;

        public int getDataType() {
            return dataType;
        }

        public void setDataType(int dataType) {
            this.dataType = dataType;
        }

        public int getBoundLeft() {
            return boundLeft;
        }

        public void setBoundLeft(int boundLeft) {
            this.boundLeft = boundLeft;
        }

        public int getBoundRight() {
            return boundRight;
        }

        public void setBoundRight(int boundRight) {
            this.boundRight = boundRight;
        }

        public BufferedImage getBufferedImage() {
            return bufferedImage;
        }

        public void setBufferedImage(BufferedImage bufferedImage) {
            this.bufferedImage = bufferedImage;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

    }
}
