package com.tuling.lock;

/**
 * Created by smlz on 2019/12/12.
 */
public class TestMain {

    public static void main(String[] args) {



        new Thread(new Runnable() {
            @Override
            public void run() {
                GlobalSession globalSession = new GlobalSession();
                globalSession.lock();
                try {
                    System.out.println(Thread.currentThread().getName());
                    Thread.sleep(4000000);
                    System.out.println(Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    globalSession.unlock();
                }

            }
        }).start();


        GlobalSession globalSession = new GlobalSession();





    }
}
