// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.ai;

import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import com.google.common.collect.Lists;
import net.minecraft.profiler.Profiler;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class EntityAITasks
{
    private static final Logger logger;
    private List taskEntries;
    private List executingTaskEntries;
    private final Profiler theProfiler;
    private int tickCount;
    private int tickRate;
    private static final String __OBFID = "CL_00001588";
    
    public EntityAITasks(final Profiler p_i1628_1_) {
        this.taskEntries = Lists.newArrayList();
        this.executingTaskEntries = Lists.newArrayList();
        this.tickRate = 3;
        this.theProfiler = p_i1628_1_;
    }
    
    public void addTask(final int p_75776_1_, final EntityAIBase p_75776_2_) {
        this.taskEntries.add(new EntityAITaskEntry(p_75776_1_, p_75776_2_));
    }
    
    public void removeTask(final EntityAIBase p_85156_1_) {
        final Iterator var2 = this.taskEntries.iterator();
        while (var2.hasNext()) {
            final EntityAITaskEntry var3 = var2.next();
            final EntityAIBase var4 = var3.action;
            if (var4 == p_85156_1_) {
                if (this.executingTaskEntries.contains(var3)) {
                    var4.resetTask();
                    this.executingTaskEntries.remove(var3);
                }
                var2.remove();
            }
        }
    }
    
    public void onUpdateTasks() {
        this.theProfiler.startSection("goalSetup");
        if (this.tickCount++ % this.tickRate == 0) {
            for (final EntityAITaskEntry var2 : this.taskEntries) {
                final boolean var3 = this.executingTaskEntries.contains(var2);
                if (var3) {
                    if (this.canUse(var2) && this.canContinue(var2)) {
                        continue;
                    }
                    var2.action.resetTask();
                    this.executingTaskEntries.remove(var2);
                }
                if (this.canUse(var2) && var2.action.shouldExecute()) {
                    var2.action.startExecuting();
                    this.executingTaskEntries.add(var2);
                }
            }
        }
        else {
            final Iterator var1 = this.executingTaskEntries.iterator();
            while (var1.hasNext()) {
                final EntityAITaskEntry var2 = var1.next();
                if (!this.canContinue(var2)) {
                    var2.action.resetTask();
                    var1.remove();
                }
            }
        }
        this.theProfiler.endSection();
        this.theProfiler.startSection("goalTick");
        final Iterator var1 = this.executingTaskEntries.iterator();
        while (var1.hasNext()) {
            final EntityAITaskEntry var2 = var1.next();
            var2.action.updateTask();
        }
        this.theProfiler.endSection();
    }
    
    private boolean canContinue(final EntityAITaskEntry p_75773_1_) {
        final boolean var2 = p_75773_1_.action.continueExecuting();
        return var2;
    }
    
    private boolean canUse(final EntityAITaskEntry p_75775_1_) {
        for (final EntityAITaskEntry var3 : this.taskEntries) {
            if (var3 != p_75775_1_) {
                if (p_75775_1_.priority >= var3.priority) {
                    if (!this.areTasksCompatible(p_75775_1_, var3) && this.executingTaskEntries.contains(var3)) {
                        return false;
                    }
                    continue;
                }
                else {
                    if (!var3.action.isInterruptible() && this.executingTaskEntries.contains(var3)) {
                        return false;
                    }
                    continue;
                }
            }
        }
        return true;
    }
    
    private boolean areTasksCompatible(final EntityAITaskEntry p_75777_1_, final EntityAITaskEntry p_75777_2_) {
        return (p_75777_1_.action.getMutexBits() & p_75777_2_.action.getMutexBits()) == 0x0;
    }
    
    static {
        logger = LogManager.getLogger();
    }
    
    class EntityAITaskEntry
    {
        public EntityAIBase action;
        public int priority;
        private static final String __OBFID = "CL_00001589";
        
        public EntityAITaskEntry(final int p_i1627_2_, final EntityAIBase p_i1627_3_) {
            this.priority = p_i1627_2_;
            this.action = p_i1627_3_;
        }
    }
}
