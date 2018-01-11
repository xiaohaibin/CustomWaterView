package com.stx.xhb.customwaterview;

import android.content.Context;

import java.util.Random;

/**
 * @author: xiaohaibin.
 * @time: 2018/1/5
 * @mail:xhb_199409@163.com
 * @github:https://github.com/xiaohaibin
 * @describe: 工具类
 */

public class Utils {

    private static Random random = new Random();

    public static int dp2px(Context context, int values) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (values * density + 0.5f);
    }

    /**
     * 区间随机
     *
     * @param lower
     * @param upper
     * @return
     */
    public static float getRandom(float lower, float upper) {
        float min = Math.min(lower, upper);
        float max = Math.max(lower, upper);
        return getRandom(max - min) + min;
    }

    /**
     * 上界随机
     *
     * @param upper
     * @return
     */
    public static float getRandom(float upper) {
        return random.nextFloat() * upper;
    }

    /**
     * 上界随机
     *
     * @param upper
     * @return
     */
    public static int getRandom(int upper) {
        return random.nextInt(upper);
    }
}
