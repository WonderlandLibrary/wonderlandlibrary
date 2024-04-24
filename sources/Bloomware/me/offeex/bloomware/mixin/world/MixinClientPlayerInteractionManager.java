package me.offeex.bloomware.mixin.world;

import me.offeex.bloomware.api.structure.ColorMutable;
import me.offeex.bloomware.event.events.*;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;



@Mixin(ClientPlayerInteractionManager.class)
public abstract class MixinClientPlayerInteractionManager {
    @Shadow
    private int blockBreakingCooldown;

    @Redirect(method = "updateBlockBreakingProgress", at = @At(value = "FIELD", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;blockBreakingCooldown:I", opcode = Opcodes.PUTFIELD))
    private void updateBlockBreakingProgress(ClientPlayerInteractionManager instance, int value) {
        EventCooldown.BlockBreaking event = new EventCooldown.BlockBreaking(value).post();
        this.blockBreakingCooldown = event.getCooldown();
    }

    @Redirect(method = "attackBlock", at = @At(value = "FIELD", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;blockBreakingCooldown:I", opcode = Opcodes.PUTFIELD))
    private void attackBlock(ClientPlayerInteractionManager clientPlayerInteractionManager, int value) {
        EventCooldown.BlockBreaking event = new EventCooldown.BlockBreaking(value).post();
        this.blockBreakingCooldown = event.getCooldown();
    }

    @Inject(method = "attackBlock", at = @At("HEAD"), cancellable = true)
    private void attackBlock(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        new EventAttack.Block(pos, direction).post(cir);
    }

    @Inject(method = "breakBlock", at = @At("HEAD"))
    private void breakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        new EventBreakBlock.Finish(pos).post(cir);
    }

    @Inject(method = "updateBlockBreakingProgress", at = @At("HEAD"), cancellable = true)
    private void updateBlockBreakingProgress(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        new EventBreakBlock.Update(pos, direction).post(cir);
    }

    @Inject(method = "cancelBlockBreaking", at = @At("HEAD"), cancellable = true)
    private void cancelBlockBreaking(CallbackInfo ci) {
        new EventBreakBlock.Cancel().post(ci);
    }

    @Inject(method = "stopUsingItem", at = @At("HEAD"), cancellable = true)
    private void stopUsingItem(PlayerEntity player, CallbackInfo ci) {
        ColorMutable.Companion.protection();
        new EventStopUsingItem(player).post(ci);
    }

    @Inject(method = "interactEntity", at = @At("HEAD"), cancellable = true)
    void onInteractEntity(PlayerEntity player, Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        new EventInteract.Entity(player, hand, entity).post(cir);
    }

    @Inject(method = "interactEntityAtLocation", at = @At("HEAD"), cancellable = true)
    void onInteractEntityAtLocation(PlayerEntity player, Entity entity, EntityHitResult hitResult, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        new EventInteract.EntityAtLocation(player, hand, hitResult, entity).post(cir);
    }

    @Inject(method = "interactBlock", at = @At("HEAD"), cancellable = true)
    void onInteractBlock(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        new EventInteract.Block(player, hand, hitResult).post(cir);
    }

    @Inject(method = "interactItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V"), cancellable = true)
    void interactItem(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        new EventInteract.Item(player, hand).post(cir);
    }
}
