package me.earth.earthhack.impl.util.thread;

import me.earth.earthhack.api.util.Globals;
import me.earth.earthhack.impl.core.ducks.IMinecraft;
import net.minecraft.network.play.server.SPacketSoundEffect;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ThreadUtil implements Globals
{
    public static void schedule(Runnable runnable)
    {
        mc.addScheduledTask(runnable);
    }

    public static void scheduleNext(Runnable runnable)
    {
        ((IMinecraft) mc).scheduleNext(runnable);
    }

    public static ScheduledExecutorService newSingleThreadDaemonExecutor()
    {
        return Executors.newSingleThreadScheduledExecutor(new DaemonThreadFactory());
    }

    public static void run(Runnable runnable)
    {
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(runnable);
        executor.shutdown();
    }

}
