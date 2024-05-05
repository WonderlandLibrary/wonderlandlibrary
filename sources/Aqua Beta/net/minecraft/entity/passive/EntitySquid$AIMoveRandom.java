package net.minecraft.entity.passive;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.util.MathHelper;

/*
 * Exception performing whole class analysis ignored.
 */
static class EntitySquid.AIMoveRandom
extends EntityAIBase {
    private EntitySquid squid;

    public EntitySquid.AIMoveRandom(EntitySquid p_i45859_1_) {
        this.squid = p_i45859_1_;
    }

    public boolean shouldExecute() {
        return true;
    }

    public void updateTask() {
        int i = this.squid.getAge();
        if (i > 100) {
            this.squid.func_175568_b(0.0f, 0.0f, 0.0f);
        } else if (this.squid.getRNG().nextInt(50) == 0 || !EntitySquid.access$000((EntitySquid)this.squid) || !this.squid.func_175567_n()) {
            float f = this.squid.getRNG().nextFloat() * (float)Math.PI * 2.0f;
            float f1 = MathHelper.cos((float)f) * 0.2f;
            float f2 = -0.1f + this.squid.getRNG().nextFloat() * 0.2f;
            float f3 = MathHelper.sin((float)f) * 0.2f;
            this.squid.func_175568_b(f1, f2, f3);
        }
    }
}
