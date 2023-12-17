// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import com.google.common.collect.Sets;
import net.minecraft.profiler.Profiler;
import java.util.Set;
import org.apache.logging.log4j.Logger;

public class EntityAITasks
{
    private static final Logger LOGGER;
    private final Set<EntityAITaskEntry> taskEntries;
    private final Set<EntityAITaskEntry> executingTaskEntries;
    private final Profiler profiler;
    private int tickCount;
    private int tickRate;
    private int disabledControlFlags;
    
    public EntityAITasks(final Profiler profilerIn) {
        this.taskEntries = (Set<EntityAITaskEntry>)Sets.newLinkedHashSet();
        this.executingTaskEntries = (Set<EntityAITaskEntry>)Sets.newLinkedHashSet();
        this.tickRate = 3;
        this.profiler = profilerIn;
    }
    
    public void addTask(final int priority, final EntityAIBase task) {
        this.taskEntries.add(new EntityAITaskEntry(priority, task));
    }
    
    public void removeTask(final EntityAIBase task) {
        final Iterator<EntityAITaskEntry> iterator = this.taskEntries.iterator();
        while (iterator.hasNext()) {
            final EntityAITaskEntry entityaitasks$entityaitaskentry = iterator.next();
            final EntityAIBase entityaibase = entityaitasks$entityaitaskentry.action;
            if (entityaibase == task) {
                if (entityaitasks$entityaitaskentry.using) {
                    entityaitasks$entityaitaskentry.using = false;
                    entityaitasks$entityaitaskentry.action.resetTask();
                    this.executingTaskEntries.remove(entityaitasks$entityaitaskentry);
                }
                iterator.remove();
            }
        }
    }
    
    public void onUpdateTasks() {
        this.profiler.startSection("goalSetup");
        if (this.tickCount++ % this.tickRate == 0) {
            for (final EntityAITaskEntry entityaitasks$entityaitaskentry : this.taskEntries) {
                if (entityaitasks$entityaitaskentry.using) {
                    if (this.canUse(entityaitasks$entityaitaskentry) && this.canContinue(entityaitasks$entityaitaskentry)) {
                        continue;
                    }
                    entityaitasks$entityaitaskentry.using = false;
                    entityaitasks$entityaitaskentry.action.resetTask();
                    this.executingTaskEntries.remove(entityaitasks$entityaitaskentry);
                }
                else {
                    if (!this.canUse(entityaitasks$entityaitaskentry) || !entityaitasks$entityaitaskentry.action.shouldExecute()) {
                        continue;
                    }
                    entityaitasks$entityaitaskentry.using = true;
                    entityaitasks$entityaitaskentry.action.startExecuting();
                    this.executingTaskEntries.add(entityaitasks$entityaitaskentry);
                }
            }
        }
        else {
            final Iterator<EntityAITaskEntry> iterator = this.executingTaskEntries.iterator();
            while (iterator.hasNext()) {
                final EntityAITaskEntry entityaitasks$entityaitaskentry2 = iterator.next();
                if (!this.canContinue(entityaitasks$entityaitaskentry2)) {
                    entityaitasks$entityaitaskentry2.using = false;
                    entityaitasks$entityaitaskentry2.action.resetTask();
                    iterator.remove();
                }
            }
        }
        this.profiler.endSection();
        if (!this.executingTaskEntries.isEmpty()) {
            this.profiler.startSection("goalTick");
            for (final EntityAITaskEntry entityaitasks$entityaitaskentry3 : this.executingTaskEntries) {
                entityaitasks$entityaitaskentry3.action.updateTask();
            }
            this.profiler.endSection();
        }
    }
    
    private boolean canContinue(final EntityAITaskEntry taskEntry) {
        return taskEntry.action.shouldContinueExecuting();
    }
    
    private boolean canUse(final EntityAITaskEntry taskEntry) {
        if (this.executingTaskEntries.isEmpty()) {
            return true;
        }
        if (this.isControlFlagDisabled(taskEntry.action.getMutexBits())) {
            return false;
        }
        for (final EntityAITaskEntry entityaitasks$entityaitaskentry : this.executingTaskEntries) {
            if (entityaitasks$entityaitaskentry != taskEntry) {
                if (taskEntry.priority >= entityaitasks$entityaitaskentry.priority) {
                    if (!this.areTasksCompatible(taskEntry, entityaitasks$entityaitaskentry)) {
                        return false;
                    }
                    continue;
                }
                else {
                    if (!entityaitasks$entityaitaskentry.action.isInterruptible()) {
                        return false;
                    }
                    continue;
                }
            }
        }
        return true;
    }
    
    private boolean areTasksCompatible(final EntityAITaskEntry taskEntry1, final EntityAITaskEntry taskEntry2) {
        return (taskEntry1.action.getMutexBits() & taskEntry2.action.getMutexBits()) == 0x0;
    }
    
    public boolean isControlFlagDisabled(final int p_188528_1_) {
        return (this.disabledControlFlags & p_188528_1_) > 0;
    }
    
    public void disableControlFlag(final int p_188526_1_) {
        this.disabledControlFlags |= p_188526_1_;
    }
    
    public void enableControlFlag(final int p_188525_1_) {
        this.disabledControlFlags &= ~p_188525_1_;
    }
    
    public void setControlFlag(final int p_188527_1_, final boolean p_188527_2_) {
        if (p_188527_2_) {
            this.enableControlFlag(p_188527_1_);
        }
        else {
            this.disableControlFlag(p_188527_1_);
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    class EntityAITaskEntry
    {
        public final EntityAIBase action;
        public final int priority;
        public boolean using;
        
        public EntityAITaskEntry(final int priorityIn, final EntityAIBase task) {
            this.priority = priorityIn;
            this.action = task;
        }
        
        @Override
        public boolean equals(@Nullable final Object p_equals_1_) {
            return this == p_equals_1_ || (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass() && this.action.equals(((EntityAITaskEntry)p_equals_1_).action));
        }
        
        @Override
        public int hashCode() {
            return this.action.hashCode();
        }
    }
}
