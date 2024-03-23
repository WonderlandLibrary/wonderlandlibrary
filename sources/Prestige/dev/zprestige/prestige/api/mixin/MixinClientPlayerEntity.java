package dev.zprestige.prestige.api.mixin;

import com.mojang.authlib.GameProfile;
import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.event.Phase;
import dev.zprestige.prestige.client.event.impl.KnockbackEvent;
import dev.zprestige.prestige.client.event.impl.MoveEvent;
import dev.zprestige.prestige.client.event.impl.SwingHandEvent;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ClientPlayerEntity.class})
public abstract class MixinClientPlayerEntity
extends AbstractClientPlayerEntity {
    @Unique
    public float y;
    @Unique
    public float p;

    @Inject(at={@At(value="HEAD")}, method={"swingHand"}, cancellable=true)
    void swingHand(Hand hand, CallbackInfo callbackInfo) {
        if (Prestige.Companion.getSelfDestructed()) {
            return;
        }
        if (new SwingHandEvent().invoke()) {
            callbackInfo.cancel();
        }
    }

    @Inject(at={@At(value="RETURN")}, method={"sendMovementPackets"})
    void sendMovementPacketsPost(CallbackInfo callbackInfo) {
        if (Prestige.Companion.getSelfDestructed()) {
            return;
        }
        if (new MoveEvent(Phase.POST, y, p).invoke()) {
            setYaw(y);
            setPitch(p);
        }
    }

    @Shadow
    public abstract float getPitch(float var1);

    public MixinClientPlayerEntity(ClientWorld clientWorld, GameProfile gameProfile) {
        super(clientWorld, gameProfile);
    }

    public void setVelocityClient(double x, double y, double z) {
        if (Prestige.Companion.getSelfDestructed()) {
            super.setVelocityClient(x, y, z);
            return;
        }
        KnockbackEvent event = new KnockbackEvent(x, y, z);
        event.invoke();
        super.setVelocityClient(event.getX(), event.getY(), event.getZ());
    }

    @Inject(at={@At(value="HEAD")}, method={"sendMovementPackets"})
    void sendMovementPacketsPre(CallbackInfo callbackInfo) {
        if (Prestige.Companion.getSelfDestructed()) {
            return;
        }
        y = getYaw();
        p = getPitch();
        MoveEvent event = new MoveEvent(Phase.PRE, y, p);
        if (event.invoke()) {
            setYaw(event.getYaw());
            setPitch(event.getPitch());
        }
    }

    @Shadow
    public abstract float getYaw(float var1);
}