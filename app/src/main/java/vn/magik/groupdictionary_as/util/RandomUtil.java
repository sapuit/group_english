package vn.magik.groupdictionary_as.util;

import java.util.Random;

/**
 * Created by sapui on 1/8/2016.
 */
public class RandomUtil {

    public static int run(int start, int end){
        Random r = new Random();
        return r.nextInt(end - start +1 ) + start;
    }
}
