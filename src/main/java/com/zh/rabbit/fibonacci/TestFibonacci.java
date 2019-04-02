package com.zh.rabbit.fibonacci;

/**
 * @author zhangqiang
 * @date 2019-04-02
 */

public class TestFibonacci {

    private static int fib(int n) {
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        return fib(n-1) + fib(n -2);
    }

    public static void main(String[] args) {
        int fib = fib(3);
        System.out.println(fib);
    }


}
