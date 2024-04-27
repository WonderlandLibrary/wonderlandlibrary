/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.base.Predicates
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.ActiveRenderInfo
 *  net.minecraft.client.renderer.EntityRenderer
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.item.EntityItemFrame
 *  net.minecraft.entity.passive.EntityAnimal
 *  net.minecraft.util.EntitySelectors
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 *  net.minecraftforge.client.ForgeHooksClient
 *  net.minecraftforge.client.event.EntityViewRenderEvent$CameraSetup
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.opengl.GL11
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Overwrite
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.At$Shift
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.List;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.modules.player.Reach;
import net.ccbluex.liquidbounce.features.module.modules.render.CameraClip;
import net.ccbluex.liquidbounce.features.module.modules.render.NoHurtCam;
import net.ccbluex.liquidbounce.features.module.modules.render.Tracers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SideOnly(value=Side.CLIENT)
@Mixin(value={EntityRenderer.class})
public abstract class MixinEntityRenderer {
    @Shadow
    private Entity field_78528_u;
    @Final
    @Shadow
    private Minecraft field_78531_r;
    @Final
    @Shadow
    private float field_78490_B;
    @Shadow
    private boolean field_78500_U;
    @Shadow
    private float field_78491_C;

    private static boolean lambda$getMouseOver$0(Entity entity) {
        return entity != null && entity.func_70067_L();
    }

    @Inject(method={"renderWorldPass"}, at={@At(value="FIELD", target="Lnet/minecraft/client/renderer/EntityRenderer;renderHand:Z", shift=At.Shift.BEFORE)})
    private void renderWorldPass(int n, float f, long l, CallbackInfo callbackInfo) {
        LiquidBounce.eventManager.callEvent(new Render3DEvent(f));
    }

    @Shadow
    public abstract void func_175069_a(ResourceLocation var1);

    @Overwrite
    public void func_78473_a(float f) {
        Entity entity = this.field_78531_r.func_175606_aa();
        if (entity != null && this.field_78531_r.field_71441_e != null) {
            Vec3d vec3d;
            this.field_78531_r.field_71424_I.func_76320_a("pick");
            this.field_78531_r.field_147125_j = null;
            Reach reach = (Reach)LiquidBounce.moduleManager.getModule(Reach.class);
            double d = reach.getState() ? (double)reach.getMaxRange() : (double)this.field_78531_r.field_71442_b.func_78757_d();
            this.field_78531_r.field_71476_x = entity.func_174822_a(reach.getState() ? (double)((Float)reach.getBuildReachValue().get()).floatValue() : d, f);
            Vec3d vec3d2 = entity.func_174824_e(f);
            boolean bl = false;
            int n = 3;
            double d2 = d;
            if (this.field_78531_r.field_71442_b.func_78749_i()) {
                d = d2 = 6.0;
            } else if (d > 3.0) {
                bl = true;
            }
            if (this.field_78531_r.field_71476_x != null) {
                d2 = this.field_78531_r.field_71476_x.field_72307_f.func_72438_d(vec3d2);
            }
            if (reach.getState() && (vec3d = entity.func_174822_a(d2 = (double)((Float)reach.getCombatReachValue().get()).floatValue(), f)) != null) {
                d2 = vec3d.field_72307_f.func_72438_d(vec3d2);
            }
            vec3d = entity.func_70676_i(1.0f);
            Vec3d vec3d3 = vec3d2.func_72441_c(vec3d.field_72450_a * d, vec3d.field_72448_b * d, vec3d.field_72449_c * d);
            this.field_78528_u = null;
            Vec3d vec3d4 = null;
            float f2 = 1.0f;
            List list = this.field_78531_r.field_71441_e.func_175674_a(entity, entity.func_174813_aQ().func_72321_a(vec3d.field_72450_a * d, vec3d.field_72448_b * d, vec3d.field_72449_c * d).func_72314_b(1.0, 1.0, 1.0), Predicates.and((Predicate)EntitySelectors.field_180132_d, MixinEntityRenderer::lambda$getMouseOver$0));
            double d3 = d2;
            for (Entity entity2 : list) {
                double d4;
                AxisAlignedBB axisAlignedBB = entity2.func_174813_aQ().func_186662_g((double)entity2.func_70111_Y());
                RayTraceResult rayTraceResult = axisAlignedBB.func_72327_a(vec3d2, vec3d3);
                if (axisAlignedBB.func_72318_a(vec3d2)) {
                    if (!(d3 >= 0.0)) continue;
                    this.field_78528_u = entity2;
                    vec3d4 = rayTraceResult == null ? vec3d2 : rayTraceResult.field_72307_f;
                    d3 = 0.0;
                    continue;
                }
                if (rayTraceResult == null || !((d4 = vec3d2.func_72438_d(rayTraceResult.field_72307_f)) < d3) && d3 != 0.0) continue;
                if (entity2.func_184208_bv() == entity.func_184208_bv() && !entity2.canRiderInteract()) {
                    if (d3 != 0.0) continue;
                    this.field_78528_u = entity2;
                    vec3d4 = rayTraceResult.field_72307_f;
                    continue;
                }
                this.field_78528_u = entity2;
                vec3d4 = rayTraceResult.field_72307_f;
                d3 = d4;
            }
            if (this.field_78528_u != null && bl) {
                double d5 = vec3d2.func_72438_d(vec3d4);
                double d6 = reach.getState() ? (double)((Float)reach.getCombatReachValue().get()).floatValue() : 3.0;
                if (d5 > d6) {
                    this.field_78528_u = null;
                    this.field_78531_r.field_71476_x = new RayTraceResult(RayTraceResult.Type.MISS, vec3d4, null, new BlockPos(vec3d4));
                }
            }
            if (this.field_78528_u != null && (d3 < d2 || this.field_78531_r.field_71476_x == null)) {
                this.field_78531_r.field_71476_x = new RayTraceResult(this.field_78528_u, vec3d4);
                if (this.field_78528_u instanceof EntityLivingBase || this.field_78528_u instanceof EntityItemFrame) {
                    this.field_78531_r.field_147125_j = this.field_78528_u;
                }
            }
            this.field_78531_r.field_71424_I.func_76319_b();
        }
    }

    @Inject(method={"setupCameraTransform"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/renderer/EntityRenderer;applyBobbing(F)V", shift=At.Shift.AFTER)})
    private void setupCameraViewBobbingAfter(CallbackInfo callbackInfo) {
        if (LiquidBounce.moduleManager.getModule(Tracers.class).getState()) {
            GL11.glPopMatrix();
        }
    }

    @Shadow
    public abstract void func_78479_a(float var1, int var2);

    @Inject(method={"orientCamera"}, at={@At(value="INVOKE", target="Lnet/minecraft/util/math/Vec3d;distanceTo(Lnet/minecraft/util/math/Vec3d;)D")}, cancellable=true)
    private void cameraClip(float f, CallbackInfo callbackInfo) {
        if (LiquidBounce.moduleManager.getModule(CameraClip.class).getState()) {
            float f2;
            callbackInfo.cancel();
            Entity entity = this.field_78531_r.func_175606_aa();
            float f3 = entity.func_70047_e();
            if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).func_70608_bn()) {
                f3 = (float)((double)f3 + 1.0);
                GlStateManager.func_179109_b((float)0.0f, (float)0.3f, (float)0.0f);
                if (!this.field_78531_r.field_71474_y.field_74325_U) {
                    BlockPos blockPos = new BlockPos(entity);
                    IBlockState iBlockState = this.field_78531_r.field_71441_e.func_180495_p(blockPos);
                    ForgeHooksClient.orientBedCamera((IBlockAccess)this.field_78531_r.field_71441_e, (BlockPos)blockPos, (IBlockState)iBlockState, (Entity)entity);
                    GlStateManager.func_179114_b((float)(entity.field_70126_B + (entity.field_70177_z - entity.field_70126_B) * f + 180.0f), (float)0.0f, (float)-1.0f, (float)0.0f);
                    GlStateManager.func_179114_b((float)(entity.field_70127_C + (entity.field_70125_A - entity.field_70127_C) * f), (float)-1.0f, (float)0.0f, (float)0.0f);
                }
            } else if (this.field_78531_r.field_71474_y.field_74320_O > 0) {
                double d = this.field_78491_C + (this.field_78490_B - this.field_78491_C) * f;
                if (this.field_78531_r.field_71474_y.field_74325_U) {
                    GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)((float)(-d)));
                } else {
                    f2 = entity.field_70177_z;
                    float f4 = entity.field_70125_A;
                    if (this.field_78531_r.field_71474_y.field_74320_O == 2) {
                        f4 += 180.0f;
                    }
                    if (this.field_78531_r.field_71474_y.field_74320_O == 2) {
                        GlStateManager.func_179114_b((float)180.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                    }
                    GlStateManager.func_179114_b((float)(entity.field_70125_A - f4), (float)1.0f, (float)0.0f, (float)0.0f);
                    GlStateManager.func_179114_b((float)(entity.field_70177_z - f2), (float)0.0f, (float)1.0f, (float)0.0f);
                    GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)((float)(-d)));
                    GlStateManager.func_179114_b((float)(f2 - entity.field_70177_z), (float)0.0f, (float)1.0f, (float)0.0f);
                    GlStateManager.func_179114_b((float)(f4 - entity.field_70125_A), (float)1.0f, (float)0.0f, (float)0.0f);
                }
            } else {
                GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)-0.1f);
            }
            if (!this.field_78531_r.field_71474_y.field_74325_U) {
                float f5 = entity.field_70126_B + (entity.field_70177_z - entity.field_70126_B) * f + 180.0f;
                float f6 = entity.field_70127_C + (entity.field_70125_A - entity.field_70127_C) * f;
                f2 = 0.0f;
                if (entity instanceof EntityAnimal) {
                    EntityAnimal entityAnimal = (EntityAnimal)entity;
                    f5 = entityAnimal.field_70758_at + (entityAnimal.field_70759_as - entityAnimal.field_70758_at) * f + 180.0f;
                }
                IBlockState iBlockState = ActiveRenderInfo.func_186703_a((World)this.field_78531_r.field_71441_e, (Entity)entity, (float)f);
                EntityViewRenderEvent.CameraSetup cameraSetup = new EntityViewRenderEvent.CameraSetup((EntityRenderer)this, entity, iBlockState, (double)f, f5, f6, f2);
                MinecraftForge.EVENT_BUS.post((Event)cameraSetup);
                GlStateManager.func_179114_b((float)cameraSetup.getRoll(), (float)0.0f, (float)0.0f, (float)1.0f);
                GlStateManager.func_179114_b((float)cameraSetup.getPitch(), (float)1.0f, (float)0.0f, (float)0.0f);
                GlStateManager.func_179114_b((float)cameraSetup.getYaw(), (float)0.0f, (float)1.0f, (float)0.0f);
            }
            GlStateManager.func_179109_b((float)0.0f, (float)(-f3), (float)0.0f);
            double d = entity.field_70169_q + (entity.field_70165_t - entity.field_70169_q) * (double)f;
            double d2 = entity.field_70167_r + (entity.field_70163_u - entity.field_70167_r) * (double)f + (double)f3;
            double d3 = entity.field_70166_s + (entity.field_70161_v - entity.field_70166_s) * (double)f;
            this.field_78500_U = this.field_78531_r.field_71438_f.func_72721_a(d, d2, d3, f);
        }
    }

    @Inject(method={"hurtCameraEffect"}, at={@At(value="HEAD")}, cancellable=true)
    private void injectHurtCameraEffect(CallbackInfo callbackInfo) {
        if (LiquidBounce.moduleManager.getModule(NoHurtCam.class).getState()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"setupCameraTransform"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/renderer/EntityRenderer;applyBobbing(F)V", shift=At.Shift.BEFORE)})
    private void setupCameraViewBobbingBefore(CallbackInfo callbackInfo) {
        if (LiquidBounce.moduleManager.getModule(Tracers.class).getState()) {
            GL11.glPushMatrix();
        }
    }
}

