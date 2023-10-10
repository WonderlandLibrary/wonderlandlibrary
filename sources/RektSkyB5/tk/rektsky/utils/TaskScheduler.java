/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.utils;

import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.Subscribe;
import tk.rektsky.event.impl.ClientTickEvent;

public class TaskScheduler {
    public List<RektTask> tasks = new ArrayList<RektTask>();

    public void addTask(Runnable task, int delay) {
        if (delay < 0) {
            delay = -delay;
        }
        this.tasks.add(new RektTask(delay, task));
    }

    @Subscribe
    public void onTick(ClientTickEvent event) {
        for (RektTask task : new ArrayList<RektTask>(this.tasks)) {
            if (task.lastTime == 0) {
                task.task.run();
                this.tasks.remove(task);
                continue;
            }
            --task.lastTime;
        }
    }

    private static class RektTask {
        int lastTime = 0;
        Runnable task = null;

        public RektTask(int lastTime, Runnable task) {
            this.lastTime = lastTime;
            this.task = task;
        }
    }
}

