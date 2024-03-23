package dev.zprestige.prestige.api.mixin;

import dev.zprestige.prestige.api.interfaces.IExplosion;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={Explosion.class})
public class MixinExplosion implements IExplosion {
    @Shadow
    @Final
    @Mutable
    public World world;
    @Shadow
    @Final
    @Mutable
    public Entity entity;
    @Shadow
    @Final
    @Mutable
    public double y;
    @Shadow
    @Final
    @Mutable
    public double x;
    @Shadow
    @Final
    @Mutable
    public double z;
    @Shadow
    @Final
    @Mutable
    public float power;
    @Shadow
    @Final
    @Mutable
    public boolean createFire;
    @Shadow
    @Final
    @Mutable
    public Explosion.DestructionType destructionType;

    @Override
    public void set(Vec3d vec3d, float f, boolean bl) {
        world = MinecraftClient.getInstance().world;
        entity = null;
        x = vec3d.x;
        y = vec3d.y;
        z = vec3d.z;
        power = f;
        createFire = bl;
        destructionType = Explosion.DestructionType.DESTROY;
    }
}