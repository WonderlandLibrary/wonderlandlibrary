package me.earth.earthhack.impl.core.mixins.minecraft.entity.living;

import me.earth.earthhack.api.event.bus.instance.Bus;
import me.earth.earthhack.impl.Earthhack;
import me.earth.earthhack.impl.core.ducks.IEntityLivingBase;
import me.earth.earthhack.impl.core.mixins.minecraft.entity.MixinEntity;
import me.earth.earthhack.impl.event.events.misc.DeathEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.datasync.DataParameter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends MixinEntity implements IEntityLivingBase
{
    @Shadow
    @Final
    private static DataParameter<Float> HEALTH;

    @Override
    @Invoker(value = "getArmSwingAnimationEnd")
    public abstract int armSwingAnimationEnd();

    @Inject(method = "notifyDataManagerChange", at = @At("RETURN"))
    public void notifyDataManagerChangeHook(DataParameter<?> key, CallbackInfo info)
    {
        if (key.equals(HEALTH))
        {
            if (this.dataManager.get(HEALTH) <= 0.0)
            {
                Bus.EVENT_BUS.post(new DeathEvent(EntityLivingBase.class.cast(this)));
            }
        }
    }

}
