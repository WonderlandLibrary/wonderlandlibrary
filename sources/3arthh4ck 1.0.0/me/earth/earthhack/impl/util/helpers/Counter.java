package me.earth.earthhack.impl.util.helpers;

import java.util.concurrent.atomic.AtomicInteger;

public class Counter
{
    private final AtomicInteger counter;
    private final int initial;

    public Counter(int initial)
    {
        this.counter = new AtomicInteger(initial);
        this.initial = initial;
    }

    public int next()
    {
        return counter.incrementAndGet();
    }

    public void reset()
    {
        counter.set(initial);
    }

}
