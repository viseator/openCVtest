package xyz.viseator;import org.opencv.core.*;import org.opencv.imgcodecs.Imgcodecs;import org.opencv.imgproc.Imgproc;import java.awt.image.BufferedImage;import java.awt.image.DataBufferByte;import java.util.ArrayList;import java.util.Collections;import java.util.List;import static org.opencv.imgproc.Imgproc.*;/** * Created by viseator on 2016/11/13. */public class ProgressPic {    private static final double Y_MINLINELENGTH = 1200;    private static final int Y_THRESHOLD = 150;    private static final double Y_MAXLINEGAP = 20;    private static final int X_THRESHOLD = 0;    private static final double X_MAXLINEGAP = 11;    private static final double X_HEIGHT = 0.65;    private static final double PADDING_TOP_BOTTOM = 0;    private static final double PADDING_LEFT_RIGHT = 0.09;    private Mat srcPic;//Source Picture    private Mat rawSrcPic;//Raw Source Picture    private Mat rgbSrcPic;//Rgb Source Picture    private Mat dilateMuchPic;//Dilate Much for finding lines    private String path;    private double scaleSize = 0.5;    private ArrayList<Mat> blockImages;    private int mark;    private int outNum = 0;    private double filterGap = 0;    private int startLine;    private int endLine;    private double padding;    private boolean skip;    private ArrayList<Double> lineXs;    private ArrayList<Double> lineYs;    private ArrayList<Double> uniqueLineXs;    private ArrayList<Double> uniqueLineYs;    private ArrayList<BufferedImage> bufferedImages;    public ArrayList<BufferedImage> progress(String path, int mark) {        this.mark = mark;        this.path = path;        rawSrcPic = Imgcodecs.imread(path, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);        rgbSrcPic = Imgcodecs.imread(path, Imgcodecs.CV_LOAD_IMAGE_COLOR);        bufferedImages = new ArrayList<>();     /*   srcPic = new Mat();        srcPic.create((int) (rawSrcPic.rows() * scaleSize), (int) (rawSrcPic.cols() * scaleSize), CvType.CV_8UC1);        Imgproc.resize(rawSrcPic,srcPic,srcPic.size());*/        srcPic = rawSrcPic;        toGrayAndBinarization();        deNoise();        findLines();        cutImagesToRows();        cutImageToCols();        return bufferedImages;    }    private void toGrayAndBinarization() {//        Test for finding the best param1 and param2/*        for (int param1 = 5; param1 < 40; param1 += 6) {            for (int param2 = 0; param2 <= 40; param2 += 5) {                Imgproc.adaptiveThreshold(srcPic, newPic, 255, ADAPTIVE_THRESH_GAUSSIAN_C, THRESH_BINARY, param1, param2);                Imgcodecs.imwrite("/storage/sdcard/pic/test/2_" + String.valueOf(param1) +                                "_" + String.valueOf(param2)+                        ".jpg", newPic);            }        }*/        Imgproc.adaptiveThreshold(srcPic, srcPic, 255, ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY_INV, 27, 10);/*        for (testId = 0; testId < 9; testId++) {            Imgcodecs.imwrite("/storage/sdcard/pic/test1/" + String.valueOf(testId) +                    ".jpg", newPic);        }*///        Imgcodecs.imwrite("C:/Users/visea/Desktop/test/java/afterBinarization.jpg", srcPic);    }    private void deNoise() {        Mat kernelErode = Imgproc.getStructuringElement(Imgproc.MORPH_ERODE, new Size(2, 2));        Imgproc.erode(srcPic, srcPic, kernelErode);//        Imgcodecs.imwrite("C:/Users/visea/Desktop/test/java/afterErode.jpg", srcPic);        Mat kernelDilate = Imgproc.getStructuringElement(Imgproc.MORPH_DILATE, new Size(2, 2));        Imgproc.dilate(srcPic, srcPic, kernelDilate);        dilateMuchPic = new Mat();        Mat kernelDilateMuch = Imgproc.getStructuringElement(Imgproc.MORPH_DILATE, new Size(12, 12));        Imgproc.dilate(srcPic, dilateMuchPic, kernelDilateMuch);        Imgcodecs.imwrite("C:/Users/visea/Desktop/test/java/afterDilate.jpg", srcPic);    }    private void findLines() {        lineYs = new ArrayList<>();        uniqueLineYs = new ArrayList<>();        Mat lines = new Mat();        Mat showLines = new Mat();        showLines.create(srcPic.rows(), srcPic.cols(), CvType.CV_32SC3);        Imgproc.HoughLinesP(dilateMuchPic, lines, 1, Math.PI / 180, Y_THRESHOLD,                Y_MINLINELENGTH, Y_MAXLINEGAP);        //Record rows        for (int i = 0; i < lines.rows(); i++) {            double[] points = lines.get(i, 0);            double y1, y2;            y1 = points[1];            y2 = points[3];            if (Math.abs(y1 - y2) < 30) {                lineYs.add((y1 + y2) / 2);            }        }        getUniqueLines(lineYs, uniqueLineYs,10,0);        //Draw rows        for (double y : uniqueLineYs) {            Point pt1 = new Point(0, y);            Point pt2 = new Point(srcPic.width(), y);            Imgproc.line(rgbSrcPic, pt1, pt2, new Scalar(0, 0, 255), 3);        }        Imgcodecs.imwrite("C:/Users/visea/Desktop/test/java/findLines.jpg", rgbSrcPic);    }    private void cutImagesToRows() {        blockImages = new ArrayList<>();        for (int i = 0; i < uniqueLineYs.size(); i++) {            Rect rect;            double y = uniqueLineYs.get(i);            if (i != uniqueLineYs.size() - 1) {                rect = new Rect((int) (srcPic.width() * PADDING_LEFT_RIGHT),                        (int) (y + (uniqueLineYs.get(i + 1) - y) * PADDING_TOP_BOTTOM),                        (int) (srcPic.width() * (1 - PADDING_LEFT_RIGHT * 2)),                        (int) ((uniqueLineYs.get(i + 1) - y) * (1 - PADDING_TOP_BOTTOM * 2)));            } else {                rect = new Rect((int) (srcPic.width() * PADDING_LEFT_RIGHT),                        (int) (y + (srcPic.height() - y) * PADDING_TOP_BOTTOM),                        (int) (srcPic.width() * (1 - PADDING_LEFT_RIGHT * 2)),                        (int) ((srcPic.height() - y) * (1 - PADDING_TOP_BOTTOM * 2)));            }            blockImages.add(new Mat(srcPic, rect));        }    }    private void cutImageToCols() {        for (int position = 0; position < blockImages.size(); position++) {            Mat image = blockImages.get(position);            lineXs = new ArrayList<>();            uniqueLineXs = new ArrayList<>();            Mat lines = new Mat();            dilateMuchPic = new Mat();            Mat kernelDilateMuch = Imgproc.getStructuringElement(Imgproc.MORPH_DILATE, new Size(6, 6));            Imgproc.dilate(image, dilateMuchPic, kernelDilateMuch);            Imgproc.HoughLinesP(dilateMuchPic, lines, 0.1, Math.PI / 360, X_THRESHOLD,                    image.height() * X_HEIGHT, X_MAXLINEGAP);            for (int i = 0; i < lines.rows(); i++) {                double[] points = lines.get(i, 0);                double x1, x2;                x1 = points[0];                x2 = points[2];                if (Math.abs(x1 - x2) < 50) {                    lineXs.add((x1 + x2) / 2);                }            }            getUniqueLines(lineXs, uniqueLineXs,10,0);            setParams(position, image);//            padding = 0.05;            ArrayList<Double> betterLineXs = new ArrayList<>();            filterLines(uniqueLineXs, betterLineXs, image.width());            if (betterLineXs.size() < 2 | betterLineXs.size() > 5) {                continue;            }            System.out.println(String.valueOf(position) + ":" + String.valueOf(startLine) +                    " " + String.valueOf(endLine));//            showMarkedLines(image, betterLineXs, position);            cutImages(betterLineXs, image);        }    }    private void getUniqueLines(ArrayList<Double> src, ArrayList<Double> dst,int maxGap,int minNum) {        Collections.sort(src);        for (int i = 0; i < src.size(); i++) {            double sum = src.get(i);            double num = 1;            //When the distance between two lines less than 10,get the average of them            while (i != src.size() - 1 && src.get(i + 1) - src.get(i) < maxGap) {                num++;                sum = sum + src.get(i + 1);                i++;            }            if (num == 1) {                dst.add(src.get(i));            } else {                if (num > minNum) {                    dst.add(sum / num);                }            }        }    }    private void filterLines(ArrayList<Double> src, ArrayList<Double> dst, int width) {        for (int i = 0; i < src.size(); i++) {            int recode = i;            while (i != src.size() - 1 && src.get(i + 1) - src.get(i) < width * filterGap) {                i++;            }            dst.add(src.get(recode));        }    }    private void showMarkedLines(Mat src, ArrayList<Double> lines, int position) {        Mat showLines = new Mat();        Imgproc.cvtColor(src, showLines, COLOR_GRAY2BGR);        for (double x : lines) {            Point pt1 = new Point(x, 0);            Point pt2 = new Point(x, src.height());            Imgproc.line(showLines, pt1, pt2, new Scalar(0, 0, 255), 3);        }        Imgcodecs.imwrite("C:/Users/visea/Desktop/test/java/cut2/" +                String.valueOf(position) +                ".jpg", showLines);    }    private void setParams(int position, Mat image) {        if (image.height() < srcPic.height() * 0.015) {            skip = true;            return;        }        switch (mark) {            case 1:                if (position == 1 | position == 10 | position == 24) {                    skip = true;                } else {                    skip = false;                    filterGap = 0;                    padding = 0;                    if (position >= 2 && position <= 20) {                        startLine = 0;                        endLine = 1;                    } else {                        startLine = 1;                        endLine = 2;                    }                }                break;            case 2:                if (position == 14 | position == 28) {                    skip = true;                } else {                    skip = false;                    filterGap = 0;                    padding = 0;                    startLine = 1;                    endLine = 2;                }                break;            case 3:                skip = false;                filterGap = 0;                padding = 0;                if (position <= 24) {                    startLine = 1;                    endLine = 2;                } else {                    startLine = 0;                    endLine = 1;                }                break;            case 4:                if (position == 18) {                    skip = true;                } else {                    skip = false;                    filterGap = 0.4;                    padding = 0;                    startLine = 0;                    endLine = 1;                }                break;            case 5:                skip = false;                filterGap = 0.15;                padding = 0;                startLine = 0;                endLine = 1;                break;        }    }    private void cutImages(ArrayList<Double> lineXs, Mat image) {        if (skip) return;        Mat cutMat = new Mat(image, new Rect((int) (lineXs.get(startLine) + 5),                (int) (image.height() * padding),                (int) (lineXs.get(endLine) - lineXs.get(startLine) - 10), (int) (image.height() * (1 - 2 * padding))));        Imgcodecs.imwrite("C:/Users/visea/Desktop/test/java/cut2/" +                String.valueOf(++outNum) +                ".jpg", cutMat);        cutSingleLine(cutMat);        convertMatToBufferedImage(cutMat);    }    private ArrayList<Mat> cutSingleLine(Mat srcMat) {        ArrayList<Double> emptyRows = new ArrayList<>();        ArrayList<Double> uniqueEmptyRows = new ArrayList<>();        double[] points;        for (int row = 0; row < srcMat.rows(); row++) {            int count = 0;            for (int col = 0; col < srcMat.cols(); col++) {                points = srcMat.get(row, col);                if (points[0] == 255) {                    count++;                }            }            if (count < 3) emptyRows.add((double) row);        }        Mat showLines = new Mat();        Imgproc.cvtColor(srcMat, showLines, COLOR_GRAY2BGR);        getUniqueLines(emptyRows, uniqueEmptyRows,10,0);        if (uniqueEmptyRows.get(0) > srcMat.height() * 0.3)            uniqueEmptyRows.add(0, 0.0);        if (uniqueEmptyRows.get(uniqueEmptyRows.size() - 1) < srcMat.height() * 0.7)            uniqueEmptyRows.add((double) (srcMat.height() - 1));/*        for (double y : uniqueEmptyRows) {            Point pt1 = new Point(0, y);            Point pt2 = new Point(srcMat.width(), y);            Imgproc.line(showLines, pt1, pt2, new Scalar(0, 0, 255), 1);        }        Imgcodecs.imwrite("C:/Users/visea/Desktop/test/java/cut3/" +                String.valueOf(outNum) + ".jpg", showLines);*/        for (int i = 0; i < uniqueEmptyRows.size(); i++) {            if (i != uniqueEmptyRows.size() - 1) {                Mat cutMat = new Mat(srcMat, new Rect(0,                        (int) (double) uniqueEmptyRows.get(i),                        srcMat.width(),                        (int) (uniqueEmptyRows.get(i + 1) - uniqueEmptyRows.get(i))));  /*              Imgcodecs.imwrite("C:/Users/visea/Desktop/test/java/cut3/" +                        String.valueOf(outNum)+String.valueOf(i) + ".jpg", cutMat);*/                cutSingleCha(cutMat);            }        }        return null;    }    private ArrayList<Mat> cutSingleCha(Mat srcMat) {        ArrayList<Double> emptyCols = new ArrayList<>();        ArrayList<Double> uniqueEmptyCols = new ArrayList<>();        double[] points;        for (int col = 0; col < srcMat.cols(); col++) {            int count = 0;            for (int row = 0; row < srcMat.rows(); row++) {                points = srcMat.get(row, col);                if (points[0] == 255) {                    count++;                }            }            if (count < 3) emptyCols.add((double) col);        }        getUniqueLines(emptyCols,uniqueEmptyCols,2,2);        Mat showLines = new Mat();        Imgproc.cvtColor(srcMat, showLines, COLOR_GRAY2BGR);        for (double x : uniqueEmptyCols) {            Point pt1 = new Point(x, 0);            Point pt2 = new Point(x, srcMat.height());            Imgproc.line(showLines, pt1, pt2, new Scalar(0, 255, 0), 1);        }        Imgcodecs.imwrite("C:/Users/visea/Desktop/test/java/cut3/" +                String.valueOf(outNum) + ".jpg", showLines);        return null;    }    private void convertMatToBufferedImage(Mat mat) {        BufferedImage bufferedImage = new BufferedImage(mat.width(), mat.height(), BufferedImage.TYPE_BYTE_GRAY);        byte[] data = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();        mat.get(0, 0, data);        bufferedImages.add(bufferedImage);    }}