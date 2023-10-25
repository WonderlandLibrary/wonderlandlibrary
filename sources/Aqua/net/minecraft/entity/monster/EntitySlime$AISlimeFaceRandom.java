package net.minecraft.entity.monster;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntitySlime;

static class EntitySlime.AISlimeFaceRandom
extends EntityAIBase {
    private EntitySlime slime;
    private float field_179459_b;
    private int field_179460_c;

    public EntitySlime.AISlimeFaceRandom(EntitySlime slimeIn) {
        this.slime = slimeIn;
        this.setMutexBits(2);
    }

    public boolean shouldExecute() {
        return this.slime.getAttackTarget() == null && (this.slime.onGround || this.slime.isInWater() || this.slime.isInLava());
    }

    public void updateTask() {
        if (--this.field_179460_c <= 0) {
            this.field_179460_c = 40 + this.slime.getRNG().nextInt(60);
            this.field_179459_b = this.slime.getRNG().nextInt(360);
        }
        ((EntitySlime.SlimeMoveHelper)this.slime.getMoveHelper()).func_179920_a(this.field_179459_b, false);
    }
}
