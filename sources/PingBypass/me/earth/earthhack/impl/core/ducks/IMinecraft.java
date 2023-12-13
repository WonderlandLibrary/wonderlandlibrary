package me.earth.earthhack.impl.core.ducks;

import com.google.common.util.concurrent.ListenableFuture;

public interface IMinecraft
{
    /**
     * Schedules a task for the next gameLoop regardless
     * of the thread it is on, as tasks scheduled on the main
     * thread will be run immediately.
     *
     * @param runnable the Runnable to schedule.
     * @return a listenable future.
     */
    ListenableFuture<Object> scheduleNext(Runnable runnable);

}
