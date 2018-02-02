package com.stx.xhb.customwaterview.model;

/**
 * @author: xiaohaibin.
 * @time: 2018/1/8
 * @mail:xhb_199409@163.com
 * @github:https://github.com/xiaohaibin
 * @describe: 水滴位置实体类
 */

public class Point {

    public float x;
    public float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // get方法用于获取坐标
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
