package me.offeex.bloomware.mixin.render;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.offeex.bloomware.api.manager.managers.HoleManager;
import me.offeex.bloomware.client.module.visuals.NoRender;
import me.offeex.bloomware.event.events.EventDraw;
import me.offeex.bloomware.event.events.EventRaycast;
import me.offeex.bloomware.event.events.EventRender;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.EntityHitResult;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {

    @Inject(method = "render", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/client/render/WorldRenderer;drawEntityOutlinesFramebuffer()V",
        shift = At.Shift.AFTER
    ))
    void drawCustomFramebuffer(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        new EventDraw.EntityOutline(tickDelta).post(ci);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;renderWithTooltip(Lnet/minecraft/client/util/math/MatrixStack;IIF)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    void afterRenderScreen(float tickDelta, long startTime, boolean tick, CallbackInfo ci, int mouseX, int mouseY, MatrixStack worldStack, MatrixStack screenStack) {
        new EventRender.Screen(screenStack, mouseX, mouseY, tickDelta).shift().post();
    }

    @Inject(method = "renderWorld", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z", opcode = Opcodes.GETFIELD, ordinal = 0))
    void renderWorldPost(float tickDelta, long limitTime, MatrixStack matrix, CallbackInfo ci) {
        new EventRender.World(tickDelta, limitTime, matrix).shift().post();
    }

    @Inject(method = "tiltViewWhenHurt", at = @At("HEAD"), cancellable = true)
    public void bobViewWhenHurt(MatrixStack matrixStack, float f, CallbackInfo ci) {
        HoleManager.INSTANCE.protection();
        NoRender module = NoRender.INSTANCE;
        if (module.getEnabled() && NoRender.INSTANCE.getHurtCamera().getToggled()) ci.cancel();
    }

    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    public void bobView(MatrixStack matrixStack, float f, CallbackInfo ci) {
        if (NoRender.INSTANCE.getEnabled() && NoRender.INSTANCE.getBob().getToggled()) ci.cancel();
    }

    @ModifyExpressionValue(method = "updateTargetedEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileUtil;raycast(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;D)Lnet/minecraft/util/hit/EntityHitResult;"))
    private EntityHitResult updateTargetedEntity(EntityHitResult hitResult) {
        EventRaycast.TargetedEntity event = new EventRaycast.TargetedEntity(hitResult).post();
        return event.getEntityHitResult();
    }

    @Inject(method = "showFloatingItem", at = @At("HEAD"), cancellable = true)
    private void onShowFloatingItem(ItemStack floatingItem, CallbackInfo info) {
        if (floatingItem.isOf(Items.TOTEM_OF_UNDYING) && NoRender.INSTANCE.getTotem().getToggled())
            info.cancel();
    }
}