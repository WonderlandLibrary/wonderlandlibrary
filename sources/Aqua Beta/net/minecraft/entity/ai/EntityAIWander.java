package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.Vec3;

public class EntityAIWander
extends EntityAIBase {
    private EntityCreature entity;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private double speed;
    private int executionChance;
    private boolean mustUpdate;

    public EntityAIWander(EntityCreature creatureIn, double speedIn) {
        this(creatureIn, speedIn, 120);
    }

    public EntityAIWander(EntityCreature creatureIn, double speedIn, int chance) {
        this.entity = creatureIn;
        this.speed = speedIn;
        this.executionChance = chance;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        Vec3 vec3;
        if (!this.mustUpdate) {
            if (this.entity.getAge() >= 100) {
                return false;
            }
            if (this.entity.getRNG().nextInt(this.executionChance) != 0) {
                return false;
            }
        }
        if ((vec3 = RandomPositionGenerator.findRandomTarget((EntityCreature)this.entity, (int)10, (int)7)) == null) {
            return false;
        }
        this.xPosition = vec3.xCoord;
        this.yPosition = vec3.yCoord;
        this.zPosition = vec3.zCoord;
        this.mustUpdate = false;
        return true;
    }

    public boolean continueExecuting() {
        return !this.entity.getNavigator().noPath();
    }

    public void startExecuting() {
        this.entity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
    }

    public void makeUpdate() {
        this.mustUpdate = true;
    }

    public void setExecutionChance(int newchance) {
        this.executionChance = newchance;
    }
}
