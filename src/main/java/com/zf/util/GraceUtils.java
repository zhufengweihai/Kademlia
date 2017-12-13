package com.zf.util;

/**
 * @author zhufeng
 * @date 2017-12-12.
 */

public class GraceUtils {
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }
}
