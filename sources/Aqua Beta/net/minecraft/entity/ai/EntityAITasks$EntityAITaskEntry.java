package net.minecraft.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;

class EntityAITasks.EntityAITaskEntry {
    public EntityAIBase action;
    public int priority;

    public EntityAITasks.EntityAITaskEntry(int priorityIn, EntityAIBase task) {
        this.priority = priorityIn;
        this.action = task;
    }
}
