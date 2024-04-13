package com.canon.majik.api.utils.client;

public class Timer {
    private long time;

    public Timer(){
        time = System.currentTimeMillis();
    }

    public boolean over(float ms){
        return System.currentTimeMillis() - time > ms;
    }

    public float get(){
        return System.currentTimeMillis() - time;
    }

    public void sync(){
        time = System.currentTimeMillis();
    }

    public void set(long time) {
        this.time = time;
    }

    public void reset() {
        this.time = System.nanoTime();
    }
}
