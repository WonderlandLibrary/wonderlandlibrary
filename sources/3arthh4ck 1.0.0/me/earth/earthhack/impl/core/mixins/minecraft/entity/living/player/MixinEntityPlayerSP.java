package me.earth.earthhack.impl.core.mixins.minecraft.entity.living.player;

import me.earth.earthhack.api.event.bus.instance.Bus;
import me.earth.earthhack.api.event.events.Stage;
import me.earth.earthhack.impl.core.ducks.IEntityPlayerSP;
import me.earth.earthhack.impl.event.events.network.MotionUpdateEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends MixinAbstractClientPlayer implements IEntityPlayerSP
{
    private MotionUpdateEvent motionEvent;

    @Override
    @Accessor(value = "lastReportedYaw")
    public abstract float getLastReportedYaw();

    @Override
    @Accessor(value = "lastReportedPitch")
    public abstract float getLastReportedPitch();

    /**
     * Weird injection point, but this keeps compatibility
     * with future while allowing us to spoof our rotation yaw.
     *
     * Functionality is dumbed down to what is needed for PingBypass.
     *
     * @param callbackInfo callbackInfo.
     */
    @Inject(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;onUpdateWalkingPlayer()V", shift = At.Shift.BEFORE))
    private void onUpdateWalkingPlayer_Head(CallbackInfo callbackInfo)
    {
        motionEvent = new MotionUpdateEvent(Stage.PRE, this.rotationYaw, this.rotationPitch);
        Bus.EVENT_BUS.post(motionEvent);

        if (motionEvent.isCancelled())
        {
            callbackInfo.cancel();
        }
        else
        {
            this.rotationYaw   = motionEvent.getYaw();
            this.rotationPitch = motionEvent.getPitch();
        }
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At(value = "RETURN"))
    private void onUpdateWalkingPlayer_Return(CallbackInfo callbackInfo)
    {
        this.rotationYaw   = motionEvent.getPreviousYaw();
        this.rotationPitch = motionEvent.getPreviousPitch();

        MotionUpdateEvent event = new MotionUpdateEvent(Stage.POST, motionEvent);
        Bus.EVENT_BUS.post(event);
    }

}
