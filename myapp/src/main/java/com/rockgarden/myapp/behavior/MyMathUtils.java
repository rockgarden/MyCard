package com.rockgarden.myapp.behavior;

/**
 * Created by rockgarden on 16/6/7.
 */
class MyMathUtils {

    static int constrain(int amount, int low, int high) {
        return amount < low ? low : (amount > high ? high : amount);
    }

    static float constrain(float amount, float low, float high) {
        return amount < low ? low : (amount > high ? high : amount);
    }

}