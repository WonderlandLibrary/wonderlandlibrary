package net.minecraft.entity.ai;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.profiler.Profiler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityAITasks {
    private static final Logger logger = LogManager.getLogger();
    private List<EntityAITaskEntry> taskEntries = Lists.newArrayList();
    private List<EntityAITaskEntry> executingTaskEntries = Lists.newArrayList();
    private final Profiler theProfiler;
    private int tickCount;
    private int tickRate = 3;

    public EntityAITasks(Profiler profilerIn) {
        this.theProfiler = profilerIn;
    }

    public void addTask(int priority, EntityAIBase task) {
        this.taskEntries.add((Object)new EntityAITaskEntry(this, priority, task));
    }

    public void removeTask(EntityAIBase task) {
        Iterator iterator = this.taskEntries.iterator();
        while (iterator.hasNext()) {
            EntityAITaskEntry entityaitasks$entityaitaskentry = (EntityAITaskEntry)iterator.next();
            EntityAIBase entityaibase = entityaitasks$entityaitaskentry.action;
            if (entityaibase != task) continue;
            if (this.executingTaskEntries.contains((Object)entityaitasks$entityaitaskentry)) {
                entityaibase.resetTask();
                this.executingTaskEntries.remove((Object)entityaitasks$entityaitaskentry);
            }
            iterator.remove();
        }
    }

    public void onUpdateTasks() {
        this.theProfiler.startSection("goalSetup");
        if (this.tickCount++ % this.tickRate == 0) {
            for (EntityAITaskEntry entityaitasks$entityaitaskentry : this.taskEntries) {
                boolean flag = this.executingTaskEntries.contains((Object)entityaitasks$entityaitaskentry);
                if (flag) {
                    if (this.canUse(entityaitasks$entityaitaskentry) && this.canContinue(entityaitasks$entityaitaskentry)) continue;
                    entityaitasks$entityaitaskentry.action.resetTask();
                    this.executingTaskEntries.remove((Object)entityaitasks$entityaitaskentry);
                }
                if (!this.canUse(entityaitasks$entityaitaskentry) || !entityaitasks$entityaitaskentry.action.shouldExecute()) continue;
                entityaitasks$entityaitaskentry.action.startExecuting();
                this.executingTaskEntries.add((Object)entityaitasks$entityaitaskentry);
            }
        } else {
            Iterator iterator1 = this.executingTaskEntries.iterator();
            while (iterator1.hasNext()) {
                EntityAITaskEntry entityaitasks$entityaitaskentry1 = (EntityAITaskEntry)iterator1.next();
                if (this.canContinue(entityaitasks$entityaitaskentry1)) continue;
                entityaitasks$entityaitaskentry1.action.resetTask();
                iterator1.remove();
            }
        }
        this.theProfiler.endSection();
        this.theProfiler.startSection("goalTick");
        for (EntityAITaskEntry entityaitasks$entityaitaskentry2 : this.executingTaskEntries) {
            entityaitasks$entityaitaskentry2.action.updateTask();
        }
        this.theProfiler.endSection();
    }

    private boolean canContinue(EntityAITaskEntry taskEntry) {
        boolean flag = taskEntry.action.continueExecuting();
        return flag;
    }

    private boolean canUse(EntityAITaskEntry taskEntry) {
        for (EntityAITaskEntry entityaitasks$entityaitaskentry : this.taskEntries) {
            if (entityaitasks$entityaitaskentry == taskEntry || !(taskEntry.priority >= entityaitasks$entityaitaskentry.priority ? !this.areTasksCompatible(taskEntry, entityaitasks$entityaitaskentry) && this.executingTaskEntries.contains((Object)entityaitasks$entityaitaskentry) : !entityaitasks$entityaitaskentry.action.isInterruptible() && this.executingTaskEntries.contains((Object)entityaitasks$entityaitaskentry))) continue;
            return false;
        }
        return true;
    }

    private boolean areTasksCompatible(EntityAITaskEntry taskEntry1, EntityAITaskEntry taskEntry2) {
        return (taskEntry1.action.getMutexBits() & taskEntry2.action.getMutexBits()) == 0;
    }
}
