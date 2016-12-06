package xyz.viseator;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by viseator on 2016/12/5.
 */
public class TableInfo {
    private int rowsSize;
    private static final double DEFAULT_FILTERGAP = 0;
    public static final int DATA_TYPE_NUMBER_0_1 = 0;
    public static final int DATA_TYPE_NUMBER_1_2 = 1;
    public static final int DATA_TYPE_STRING_0_1 = 2;
    public static final int DATA_TYPE_STRING_1_2 = 3;
    private ArrayList<RowInfo> rows;

    public TableInfo(int rowsSize) {
        this.rowsSize = rowsSize;
        rows = new ArrayList<>();
        for (int i = 0; i < rowsSize; i++) {
            rows.add(new RowInfo());
        }
    }

    public void initRow(int position, int dataType) {
        RowInfo rowInfo = rows.get(position);
        rowInfo.setFilterGap(DEFAULT_FILTERGAP);
        switch (dataType) {
            case DATA_TYPE_NUMBER_0_1:
                rowInfo.setDataType(dataType);
                rowInfo.setBoundLeft(0);
                rowInfo.setBorderRight(1);
                break;
            case DATA_TYPE_NUMBER_1_2:
                rowInfo.setDataType(dataType);
                rowInfo.setBoundLeft(1);
                rowInfo.setBorderRight(2);
                break;

            case DATA_TYPE_STRING_0_1:
                rowInfo.setDataType(dataType);
                rowInfo.setBoundLeft(0);
                rowInfo.setBorderRight(1);
                rowInfo.setFilterGap(0.3);
                break;

            case DATA_TYPE_STRING_1_2:
                rowInfo.setDataType(dataType);
                rowInfo.setBoundLeft(1);
                rowInfo.setBorderRight(2);
                break;
        }
    }

    public int getRowsSize() {
        return rowsSize;
    }


    public RowInfo getRows(int position) {
        return rows.get(position);
    }

    public void setRows(ArrayList<RowInfo> rows) {
        this.rows = rows;
    }

    public class RowInfo {

        private double filterGap;
        private int dataType;
        private int boundLeft;
        private int borderRight;
        private ArrayList<BufferedImage> bufferedImages;
        private String result;

        public double getFilterGap() {
            return filterGap;
        }

        public void setFilterGap(double filterGap) {
            this.filterGap = filterGap;
        }

        public BufferedImage getBufferedImage(int position) {
            return bufferedImages.get(position);
        }

        public int getChaSize() {
            return bufferedImages.size();
        }

        public void setBufferedImages(ArrayList<BufferedImage> bufferedImages) {
            this.bufferedImages = bufferedImages;
        }

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

        public int getBorderRight() {
            return borderRight;
        }

        public void setBorderRight(int borderRight) {
            this.borderRight = borderRight;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

    }
}
