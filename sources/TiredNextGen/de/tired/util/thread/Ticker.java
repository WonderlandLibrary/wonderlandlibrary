package de.tired.util.thread;

public class Ticker {

    public static Ticker instance;

    public Ticker() {
        instance = this;
    }

    public void createLoopWithDelay(long ms, String functionName, Runnable runnable) {
        final Thread loopThread = new Thread(() -> {
            while (true) {
                sleep(ms);
                runnable.run();
            }
        }, functionName);
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
