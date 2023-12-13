// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.util;

import com.elementars.eclient.Xulu;
import java.util.LinkedList;
import java.util.Queue;

public class TaskScheduler
{
    private final Queue<Runnable> tasks;
    private final Queue<Runnable> prioritizedTasks;
    int delay;
    
    public TaskScheduler() {
        this.tasks = new LinkedList<Runnable>();
        this.prioritizedTasks = new LinkedList<Runnable>();
    }
    
    public void onUpdate() {
        if (this.delay > 0) {
            --this.delay;
        }
        if (this.prioritizedTasks.peek() != null) {
            this.prioritizedTasks.remove().run();
            this.delay = Xulu.VALUE_MANAGER.getValueByName("Offhand Delay").getValue();
            return;
        }
        if (this.tasks.peek() != null && this.delay == 0) {
            this.tasks.remove().run();
            this.delay = Xulu.VALUE_MANAGER.getValueByName("Offhand Delay").getValue();
        }
    }
    
    public void addTask(final Runnable r) {
        this.tasks.add(r);
    }
    
    public void addPrioritizedTask(final Runnable r) {
        this.prioritizedTasks.add(r);
    }
}
