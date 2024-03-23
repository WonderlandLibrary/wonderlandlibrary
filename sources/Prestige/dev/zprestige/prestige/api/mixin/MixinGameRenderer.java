package dev.zprestige.prestige.api.mixin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gl.ShaderStage;
import net.minecraft.util.hit.HitResult;
import dev.zprestige.prestige.client.event.impl.TiltEvent;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import dev.zprestige.prestige.client.shader.GlProgram;
import dev.zprestige.prestige.client.event.impl.FloatingItemEvent;
import dev.zprestige.prestige.client.event.impl.Render3DEvent;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.joml.Matrix4fc;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.zprestige.prestige.client.util.impl.RenderHelper;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.entity.projectile.ProjectileUtil;
import dev.zprestige.prestige.client.event.impl.ReachEvent;
import dev.zprestige.prestige.client.Prestige;
import java.util.function.Function;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.resource.ResourceFactory;
import java.util.List;
import org.spongepowered.asm.mixin.Final;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { GameRenderer.class }, priority = 999)
public class MixinGameRenderer
{
    @Shadow
    public int floatingItemTimeLeft;
    @Shadow
    @Final
    public MinecraftClient client;
    public MinecraftClient mc;

    public MixinGameRenderer() {
        mc = MinecraftClient.getInstance();
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void updateTargetedEntity(float tickDelta) {
        Entity entity = client.getCameraEntity();
        if (entity == null || client.world == null) {
            return;
        }
        client.getProfiler().push("pick");
        client.targetedEntity = null;
        double n2 = client.interactionManager.getReachDistance();
        boolean invoke = false;
        if (!Prestige.Companion.getSelfDestructed()) {
            ReachEvent event = new ReachEvent(0);
            if (event.invoke()) {
                n2 += event.getReach();
            }
        }
        client.crosshairTarget = entity.raycast(n2, tickDelta, false);
        Vec3d cameraPosVec = entity.getCameraPosVec(tickDelta);
        boolean b = false;
        double n3 = n2;
        double n4;
        if (!client.interactionManager.hasExtendedReach()) {
            if (n3 > 3.0 && !invoke) {
                b = true;
            }
            n4 = n3;
        } else {
            n3 = (n4 = 6.0);
        }
        double squaredDistanceTo = n3 * n3;
        if (client.crosshairTarget != null) {
            squaredDistanceTo = client.crosshairTarget.getPos().squaredDistanceTo(cameraPosVec);
        }
        Vec3d vec = entity.getRotationVec(1);
        EntityHitResult raycast = ProjectileUtil.raycast(entity, cameraPosVec, cameraPosVec.add(vec.x * n4, vec.y * n4, vec.z * n4), entity.getBoundingBox().stretch(vec.multiply(n4)).expand(1.0, 1.0, 1.0), e -> !e.isSpectator() && e.canHit(), squaredDistanceTo);
        if (raycast != null) {
            double d3 = cameraPosVec.squaredDistanceTo(raycast.getPos());
            if (b && d3 > 9.0) {
                client.crosshairTarget = BlockHitResult.createMissed(raycast.getPos(), Direction.getFacing(vec.x, vec.y, vec.z), BlockPos.ofFloored(raycast.getPos()));
            } else if (d3 < squaredDistanceTo || client.crosshairTarget == null) {
                client.crosshairTarget = raycast;
                if (raycast.getEntity() instanceof LivingEntity || raycast.getEntity() instanceof ItemFrameEntity) {
                    client.targetedEntity = raycast.getEntity();
                }
            }
        }
        client.getProfiler().pop();
    }
    
    @Inject(at = { @At(value = "FIELD", target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z", opcode = 180, ordinal = 0) }, method = { "renderWorld" })
    void render3dHook(float n, long n2, MatrixStack matrixStack, CallbackInfo callbackInfo) {
        if (!Prestige.Companion.getSelfDestructed()) {
            RenderHelper.getProjectionMatrix().set(RenderSystem.getProjectionMatrix());
            RenderHelper.getModelViewMatrix().set(RenderSystem.getModelViewMatrix());
            RenderHelper.getPositionMatrix().set(matrixStack.peek().getPositionMatrix());
        }
    }
    
    @Inject(at = { @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", ordinal = 1) }, method = { "renderWorld" }, cancellable = true)
    void renderWorld(float n, long n2, MatrixStack matrixStack, CallbackInfo callbackInfo) {
        if (!Prestige.Companion.getSelfDestructed()) {
            if (new Render3DEvent(matrixStack, n).invoke()) {
                callbackInfo.cancel();
            }
        }
    }
    
    @Inject(at = { @At(value = "FIELD", target = "Lnet/minecraft/client/render/GameRenderer;floatingItemTimeLeft:I", ordinal = 1) }, method = { "tick" })
    void adjustFloatingTimeLeft(CallbackInfo callbackInfo) {
        if (!Prestige.Companion.getSelfDestructed()) {
            FloatingItemEvent event = new FloatingItemEvent(0);
            event.invoke();
            floatingItemTimeLeft -= event.getSpeed();
        }
    }

    @Inject(method = "loadPrograms", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
    void loadAllTheShaders(ResourceFactory factory, CallbackInfo ci, List<ShaderStage> stages, List<Pair<ShaderProgram, Consumer<ShaderProgram>>> shadersToLoad) {
        GlProgram.forEachProgram(loader -> shadersToLoad.add(new Pair<>(loader.getLeft().apply(factory), loader.getRight())));
    }

    @Inject(at = { @At("HEAD") }, method = { "tiltViewWhenHurt" }, cancellable = true)
    void tiltViewWhenHurt(MatrixStack matrixStack, float n, CallbackInfo callbackInfo) {
        if (!Prestige.Companion.getSelfDestructed()) {
            if (new TiltEvent().invoke()) {
                callbackInfo.cancel();
            }
        }
    }
}