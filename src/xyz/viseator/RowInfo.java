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


}
