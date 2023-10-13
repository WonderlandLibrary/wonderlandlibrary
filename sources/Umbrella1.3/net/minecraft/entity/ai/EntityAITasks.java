/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.entity.ai;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.profiler.Profiler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityAITasks {
    private static final Logger logger = LogManager.getLogger();
    private List taskEntries = Lists.newArrayList();
    private List executingTaskEntries = Lists.newArrayList();
    private final Profiler theProfiler;
    private int tickCount;
    private int tickRate = 3;
    private static final String __OBFID = "CL_00001588";

    public EntityAITasks(Profiler p_i1628_1_) {
        this.theProfiler = p_i1628_1_;
    }

    public void addTask(int p_75776_1_, EntityAIBase p_75776_2_) {
        this.taskEntries.add(new EntityAITaskEntry(p_75776_1_, p_75776_2_));
    }

    public void removeTask(EntityAIBase p_85156_1_) {
        Iterator var2 = this.taskEntries.iterator();
        while (var2.hasNext()) {
            EntityAITaskEntry var3 = (EntityAITaskEntry)var2.next();
            EntityAIBase var4 = var3.action;
            if (var4 != p_85156_1_) continue;
            if (this.executingTaskEntries.contains(var3)) {
                var4.resetTask();
                this.executingTaskEntries.remove(var3);
            }
            var2.remove();
        }
    }

    public void onUpdateTasks() {
        this.theProfiler.startSection("goalSetup");
        if (this.tickCount++ % this.tickRate == 0) {
            for (EntityAITaskEntry var2 : this.taskEntries) {
                boolean var3 = this.executingTaskEntries.contains(var2);
                if (var3) {
                    if (this.canUse(var2) && this.canContinue(var2)) continue;
                    var2.action.resetTask();
                    this.executingTaskEntries.remove(var2);
                }
                if (!this.canUse(var2) || !var2.action.shouldExecute()) continue;
                var2.action.startExecuting();
                this.executingTaskEntries.add(var2);
            }
        } else {
            Iterator var1 = this.executingTaskEntries.iterator();
            while (var1.hasNext()) {
                EntityAITaskEntry var2;
                var2 = (EntityAITaskEntry)var1.next();
                if (this.canContinue(var2)) continue;
                var2.action.resetTask();
                var1.remove();
            }
        }
        this.theProfiler.endSection();
        this.theProfiler.startSection("goalTick");
        for (EntityAITaskEntry var2 : this.executingTaskEntries) {
            var2.action.updateTask();
        }
        this.theProfiler.endSection();
    }

    private boolean canContinue(EntityAITaskEntry p_75773_1_) {
        boolean var2 = p_75773_1_.action.continueExecuting();
        return var2;
    }

    private boolean canUse(EntityAITaskEntry p_75775_1_) {
        for (EntityAITaskEntry var3 : this.taskEntries) {
            if (var3 == p_75775_1_ || !(p_75775_1_.priority >= var3.priority ? !this.areTasksCompatible(p_75775_1_, var3) && this.executingTaskEntries.contains(var3) : !var3.action.isInterruptible() && this.executingTaskEntries.contains(var3))) continue;
            return false;
        }
        return true;
    }

    private boolean areTasksCompatible(EntityAITaskEntry p_75777_1_, EntityAITaskEntry p_75777_2_) {
        return (p_75777_1_.action.getMutexBits() & p_75777_2_.action.getMutexBits()) == 0;
    }

    class EntityAITaskEntry {
        public EntityAIBase action;
        public int priority;
        private static final String __OBFID = "CL_00001589";

        public EntityAITaskEntry(int p_i1627_2_, EntityAIBase p_i1627_3_) {
            this.priority = p_i1627_2_;
            this.action = p_i1627_3_;
        }
    }
}

