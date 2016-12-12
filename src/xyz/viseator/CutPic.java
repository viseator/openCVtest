package xyz.viseator;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;

import static org.opencv.imgproc.Imgproc.ADAPTIVE_THRESH_MEAN_C;
import static org.opencv.imgproc.Imgproc.COLOR_GRAY2RGB;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY_INV;

/**
 * Wudi
 * viseator@gmail.com
 * Created by viseator on 2016/12/12.
 */
public class CutPic {
    private static final int BLOCK_SIZE = 27;
    private static final int C_THRESHOLD = 10;

    //the minimum length of line when find the horizontal lines of table
    private static final double Y_MINLINELENGTH_FACTOR = 0.8;
    //the threshold when find the horizontal lines of table
    private static final int Y_THRESHOLD = 150;
    //the max gap of the intermittent line
    private static final double Y_MAXLINEGAP = 20;

    //similar with above,using in find vertical lines
    private static final int X_THRESHOLD = 0;
    //similar with above,using in find vertical lines
    private static final double X_MAXLINEGAP = 11;

    //factor for the minimum length of line,X_MINLINELENGTH = image.height() * X_HEIGHT_FACTOR
    private static final double X_HEIGHT_FACTOR = 0.65;
    //padding for cut table to rows
    private static final double PADDING_TOP_BOTTOM = 0;

    private static final double PADDING_LEFT_RIGHT = 0.09;
    //the scale of a character's width in picture's width
    private static final double CHARACTER_SIZE = 0.019;
    private static final double IMAGE_WIDTH = 2592.0;

    private ArrayList<Mat> blockImages; //Store rows

    private Mat srcPic;
    private Mat dilateMuchPic;
    private int picId;


    public void progress(String path, int picId) {
        srcPic = Imgcodecs.imread(path, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
        this.picId = picId;
        binarization();
        deNoise();
        cutImagesToRows();
    }

    /**
     * binarization the srouce picture
     */
    private void binarization() {
        //blockSize and C are the best parameters for table
        Imgproc.adaptiveThreshold(srcPic, srcPic, 255, ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY_INV, BLOCK_SIZE, C_THRESHOLD);
        Imgcodecs.imwrite("C:/Users/visea/Desktop/test/new/binarization/" + String.valueOf(picId) + ".jpg", srcPic);
    }

    /**
     * remove the isolated point in the picture
     * erode the picture to remove
     * then dilate it to recover others
     */
    private void deNoise() {
        //kernel for erode, size:the size of the erosion
        Mat kernelErode = Imgproc.getStructuringElement(Imgproc.MORPH_ERODE, new Size(2, 2));
        Imgproc.erode(srcPic, srcPic, kernelErode);

        //kernel for dilate, size:the size off the dilation
        Mat kernelDilate = Imgproc.getStructuringElement(Imgproc.MORPH_DILATE, new Size(2, 2));
        Imgproc.dilate(srcPic, srcPic, kernelDilate);

        //dilate much, for finding all lines
        dilateMuchPic = new Mat();
        Mat kernelDilateMuch = Imgproc.getStructuringElement(Imgproc.MORPH_DILATE, new Size(12, 12));
        Imgproc.dilate(srcPic, dilateMuchPic, kernelDilateMuch);

        Imgcodecs.imwrite("C:/Users/visea/Desktop/test/new/deNoise/" + String.valueOf(picId) + ".jpg", srcPic);
    }

    /**
     * cut images to rows and store in blockImages
     */
    private void cutImagesToRows() {
        ArrayList<Double> lineYs = new ArrayList<>();
        ArrayList<Double> uniqueLineYs = new ArrayList<>();

        //lines:a special mat for find lines
        Mat lines = new Mat();
        //find lines and store in lines
        Imgproc.HoughLinesP(dilateMuchPic, lines, 1, Math.PI / 180, Y_THRESHOLD,
                Y_MINLINELENGTH_FACTOR*srcPic.width(), Y_MAXLINEGAP);

        //get the lines information from lines and store in lineYs
        for (int i = 0; i < lines.rows(); i++) {
            double[] points = lines.get(i, 0);
            double y1, y2;

            //just need the horizontal lines
            y1 = points[1];
            y2 = points[3];

            // if it slopes, get the average of them, store the y-coordinate
            if (Math.abs(y1 - y2) < 30) {
                lineYs.add((y1 + y2) / 2);
            }
        }

        getUniqueLines(lineYs, uniqueLineYs, 10);

        System.out.println(uniqueLineYs.size());
        showLines(srcPic, uniqueLineYs, false);
       /* blockImages = new ArrayList<>();
        for (int i = 0; i < uniqueLineYs.size(); i++) {
            Rect rect;
            double y = uniqueLineYs.get(i);
            //if not the last line
            if (i != uniqueLineYs.size() - 1) {
                rect = new Rect((int) (srcPic.width() * PADDING_LEFT_RIGHT),
                        (int) (y + (uniqueLineYs.get(i + 1) - y) * PADDING_TOP_BOTTOM),
                        (int) (srcPic.width() * (1 - PADDING_LEFT_RIGHT * 2)),
                        (int) ((uniqueLineYs.get(i + 1) - y) * (1 - PADDING_TOP_BOTTOM * 2)));
            } else {
                //the last line
                rect = new Rect((int) (srcPic.width() * PADDING_LEFT_RIGHT),
                        (int) (y + (srcPic.height() - y) * PADDING_TOP_BOTTOM),
                        (int) (srcPic.width() * (1 - PADDING_LEFT_RIGHT * 2)),
                        (int) ((srcPic.height() - y) * (1 - PADDING_TOP_BOTTOM * 2)));
            }
            //cut the source picture to cutMat
            Mat cutMat = new Mat(srcPic, rect);

            blockImages.add(cutMat);
        }
*/
    }

    private void showLines(Mat mat, ArrayList<Double> lineCoordinates, boolean isX) {
        Mat rgbMat = new Mat();
        Imgproc.cvtColor(mat, rgbMat, COLOR_GRAY2RGB);
        for (double coordinate : lineCoordinates) {
            Point pt1, pt2;
            if (isX) {
                pt1 = new Point(coordinate, 0);
                pt2 = new Point(coordinate, srcPic.height());
            } else {
                pt1 = new Point(0, coordinate);
                pt2 = new Point(srcPic.width(), coordinate);
            }
            Imgproc.line(rgbMat, pt1, pt2, new Scalar(0, 0, 255), 2);
        }
        Imgcodecs.imwrite("C:/Users/visea/Desktop/test/new/showLines/" +
                        String.valueOf(picId) + ".jpg"
                , rgbMat);

    }

    /**
     * filter the source coordinates, if some values are too close ,get the average of them
     *
     * @param src    source coordinates list
     * @param dst    destination coordinate list
     * @param minGap the minimum gap between coordinates
     */
    private void getUniqueLines(ArrayList<Double> src, ArrayList<Double> dst, int minGap) {
        Collections.sort(src); //sort the sourc `e coordinates list
        for (int i = 0; i < src.size(); i++) {
            double sum = src.get(i);
            double num = 1;
            //when the distance between lines less than minGap, get the average of them
            while (i != src.size() - 1 && src.get(i + 1) - src.get(i) < minGap) {
                num++;
                sum = sum + src.get(i + 1);
                i++;
            }
            if (num == 1) {
                dst.add(src.get(i));
            } else {
                dst.add(((sum / num)));
            }
        }
    }

    /*private void testParams() {
        for (int param1 = 17; param1 < 40; param1 += 2) {
            for (int param2 = 2; param2 < 20; param2 += 1) {
                BLOCK_SIZE = param1;
                C_THRESHOLD = param2;
                Mat newPic = new Mat();
                Imgproc.adaptiveThreshold(srcPic, newPic, 255, ADAPTIVE_THRESH_MEAN_C,
                        THRESH_BINARY_INV, BLOCK_SIZE, C_THRESHOLD);
                Imgcodecs.imwrite("C:/Users/visea/Desktop/test/new/binarization/" + String.valueOf(param1) + "_" +
                        String.valueOf(param2) + ".jpg",newPic);
            }
        }
    }*/
}
