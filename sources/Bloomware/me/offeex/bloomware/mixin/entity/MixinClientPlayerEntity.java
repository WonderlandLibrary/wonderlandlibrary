package me.offeex.bloomware.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.mojang.authlib.GameProfile;
import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.manager.managers.HoleManager;
import me.offeex.bloomware.client.module.motion.NoSlow;
import me.offeex.bloomware.client.module.tweaks.PortalGUI;
import me.offeex.bloomware.event.events.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity extends AbstractClientPlayerEntity {
    @Shadow private double lastX;
    @Shadow private double lastBaseY;
    @Shadow private double lastZ;

    @Shadow public Input input;

    public MixinClientPlayerEntity(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @ModifyExpressionValue(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/input/Input;hasForwardMovement()Z"))
    private boolean modifyForwardMovement(boolean original) {
        EventSprint.ForwardMovement event = new EventSprint.ForwardMovement(original).post();
        return event.getHas();
    }

    @Inject(method = "sendMovementPackets", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getX()D", ordinal = 0))
    void onSendMovementPackets(CallbackInfo ci) {
        new EventMovement.Packets(getX() - lastX, getY() - lastBaseY, getZ() - lastZ).post(ci);
    }

    @ModifyArg(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V"), index = 1)
    Vec3d onMove(Vec3d vec3d) {
        EventMovement.Move event = new EventMovement.Move(vec3d).post();
        return event.getVelocity();
    }

    @WrapWithCondition(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/input/Input;tick(ZF)V"))
    boolean tickInput(Input instance, boolean shouldSlowDown, float f) {
        return !new EventInput.Movement((KeyboardInput) instance).post().getCanceled();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo info) {
        new EventTick().post();
    }

    @Inject(method = "pushOutOfBlocks", at = @At("HEAD"), cancellable = true)
    void pushOutOfBlocks(double x, double z, CallbackInfo ci) {
        new EventVelocity.Push.Blocks().post(ci);
    }

    @Redirect(method = "updateNausea", at = @At(value = "INVOKE", target = "net/minecraft/client/network/ClientPlayerEntity.closeHandledScreen()V"))
    public void closeContainerOverride(ClientPlayerEntity player) {
        PortalGUI module = PortalGUI.INSTANCE;
        if (!module.getEnabled()) player.closeHandledScreen();
    }

    @Redirect(method = "updateNausea", at = @At(value = "INVOKE", target = "net/minecraft/client/MinecraftClient.setScreen(Lnet/minecraft/client/gui/screen/Screen;)V"))
    private void nullScreenOverride(MinecraftClient client, Screen screen) {
        PortalGUI module = PortalGUI.INSTANCE;
        if (!module.getEnabled()) client.setScreen(screen);
    }

    @Inject(method = "shouldSlowDown", at = @At(value = "HEAD"), cancellable = true)
    public void shouldSlowDown(CallbackInfoReturnable<Boolean> cir) {
        new EventInput.Slowdown().post(cir);
    }

    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"))
    private boolean onUsingItemCheck(ClientPlayerEntity player) {
        NoSlow module = NoSlow.INSTANCE;
        if (module.getEnabled() && NoSlow.INSTANCE.getItems().getToggled()) return false;
        return player.isUsingItem();
    }

    @Inject(method = "updateHealth", at = @At("HEAD"))
    private void updateHealth(float f, CallbackInfo ci) {
        HoleManager.INSTANCE.protection();
        new EventUpdate.Health(f).post();
    }

    @Redirect(method = "sendMovementPackets", at = @At(value = "FIELD", target = "Lnet/minecraft/client/network/ClientPlayerEntity;ticksSinceLastPositionPacketSent:I", ordinal = 0, opcode = Opcodes.GETFIELD))
    private int removeSendingPacketPerSecond(ClientPlayerEntity instance) {
        return 0;
    }
}