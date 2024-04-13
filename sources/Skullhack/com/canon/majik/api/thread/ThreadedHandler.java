package com.canon.majik.api.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadedHandler {
    private final static ExecutorService executorService = Executors.newFixedThreadPool(2);

    public static void queue(Runnable runnable) {
        executorService.execute(runnable);
    }
}
