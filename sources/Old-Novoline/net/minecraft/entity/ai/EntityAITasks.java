package net.minecraft.entity.ai;

import com.google.common.collect.Lists;
import net.minecraft.profiler.Profiler;

import java.util.Iterator;
import java.util.List;

public class EntityAITasks {

    private static final int TICK_RATE = 3;

    private final List<EntityAITasks.EntityAITaskEntry> taskEntries = Lists.newArrayList();
    private final List<EntityAITasks.EntityAITaskEntry> executingTaskEntries = Lists.newArrayList();

    /**
     * Instance of Profiler.
     */
    private final Profiler theProfiler;
    private int tickCount;

    public EntityAITasks(Profiler profilerIn) {
        this.theProfiler = profilerIn;
    }

    /**
     * Add a now AITask. Args : priority, task
     */
    public void addTask(int priority, EntityAIBase task) {
        this.taskEntries.add(new EntityAITaskEntry(priority, task));
    }

    /**
     * removes the indicated task from the entity's AI tasks.
     */
    public void removeTask(EntityAIBase task) {
        final Iterator<EntityAITasks.EntityAITaskEntry> iterator = this.taskEntries.iterator();

        while (iterator.hasNext()) {
            final EntityAITasks.EntityAITaskEntry entry = iterator.next();
            final EntityAIBase entityaibase = entry.action;

            if (entityaibase == task) {
                if (this.executingTaskEntries.contains(entry)) {
                    entityaibase.resetTask();
                    this.executingTaskEntries.remove(entry);
                }

                iterator.remove();
            }
        }
    }

    public void onUpdateTasks() {
        this.theProfiler.startSection("goalSetup");

        if (this.tickCount++ % TICK_RATE == 0) {
            final Iterator<EntityAITasks.EntityAITaskEntry> iterator = this.taskEntries.iterator();
            label38:

            while (true) {
                EntityAITasks.EntityAITaskEntry entry;

                while (true) {
                    if (!iterator.hasNext()) {
                        break label38;
                    }

                    entry = iterator.next();
                    final boolean flag = this.executingTaskEntries.contains(entry);

                    if (!flag) {
                        break;
                    }

                    if (!this.canUse(entry) || this.cannotContinue(entry)) {
                        entry.action.resetTask();
                        this.executingTaskEntries.remove(entry);
                        break;
                    }
                }

                if (this.canUse(entry) && entry.action.shouldExecute()) {
                    entry.action.startExecuting();
                    this.executingTaskEntries.add(entry);
                }
            }
        } else {
            final Iterator<EntityAITasks.EntityAITaskEntry> iterator1 = this.executingTaskEntries.iterator();

            while (iterator1.hasNext()) {
                final EntityAITasks.EntityAITaskEntry entry = iterator1.next();

                if (this.cannotContinue(entry)) {
                    entry.action.resetTask();
                    iterator1.remove();
                }
            }
        }

        this.theProfiler.endSection();
        this.theProfiler.startSection("goalTick");

        for (EntityAITasks.EntityAITaskEntry entry : this.executingTaskEntries) {
            entry.action.updateTask();
        }

        this.theProfiler.endSection();
    }

    /**
     * Determine if a specific AI Task should continue being executed.
     */
    private boolean cannotContinue(EntityAITasks.EntityAITaskEntry taskEntry) {
        return !taskEntry.action.continueExecuting();
    }

    /**
     * Determine if a specific AI Task can be executed, which means that all running higher (= lower int value) priority
     * tasks are compatible with it or all lower priority tasks can be interrupted.
     */
    private boolean canUse(EntityAITasks.EntityAITaskEntry taskEntry) {
        for (EntityAITasks.EntityAITaskEntry entry : this.taskEntries) {
            if (entry != taskEntry) {
                if (taskEntry.priority >= entry.priority) {
                    if (!this.areTasksCompatible(taskEntry, entry) && this.executingTaskEntries.contains(entry)) {
                        return false;
                    }
                } else if (!entry.action.isInterruptible() && this.executingTaskEntries.contains(entry)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Returns whether two EntityAITaskEntries can be executed concurrently
     */
    private boolean areTasksCompatible(EntityAITasks.EntityAITaskEntry taskEntry1, EntityAITasks.EntityAITaskEntry taskEntry2) {
        return (taskEntry1.action.getMutexBits() & taskEntry2.action.getMutexBits()) == 0;
    }

    static class EntityAITaskEntry {

        public EntityAIBase action;
        public int priority;

        public EntityAITaskEntry(int priorityIn, EntityAIBase task) {
            this.priority = priorityIn;
            this.action = task;
        }

    }

}
