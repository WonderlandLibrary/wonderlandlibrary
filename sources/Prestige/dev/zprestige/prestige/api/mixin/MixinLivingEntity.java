package dev.zprestige.prestige.api.mixin;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.event.impl.JumpEvent;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={LivingEntity.class}, priority=999)
public abstract class MixinLivingEntity extends Entity implements Attackable {

    public MixinLivingEntity(EntityType entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    abstract float getJumpVelocity();

    @Overwrite
    public void jump() {
        Vec3d vec3d = getVelocity();
        setVelocity(vec3d.x, getJumpVelocity(), vec3d.z);
        if (isSprinting()) {
            float f = this.getYaw();
            JumpEvent event = new JumpEvent(f);
            if (!Prestige.Companion.getSelfDestructed() && event.invoke()) {
                f = event.getYaw();
            }
            float f2 = f * ((float)Math.PI / 180);
            setVelocity(getVelocity().add(-MathHelper.sin(f2) * 0.2f, 0.0, MathHelper.cos(f2) * 0.2f));
        }
        velocityDirty = true;
    }
}
