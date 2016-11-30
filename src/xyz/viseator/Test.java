package xyz.viseator;

import org.opencv.core.Core;

/**
 * Created by viseator on 2016/11/21.
 */
public class Test {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        final String PATH = "C:/Users/visea/Desktop/test/2.jpg";
        ProgressPic progressPic;
        progressPic = new ProgressPic();
        progressPic.progress(PATH,2);

    }
}
