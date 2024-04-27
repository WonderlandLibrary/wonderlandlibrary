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

    @Shadow
    public abstract void func_175069_a(ResourceLocation var1);

    @Shadow
    public abstract void func_78479_a(float var1, int var2);

    @Inject(method={"renderWorldPass"}, at={@At(value="FIELD", target="Lnet/minecraft/client/renderer/EntityRenderer;renderHand:Z", shift=At.Shift.BEFORE)})
    private void renderWorldPass(int pass, float partialTicks, long finishTimeNano, CallbackInfo callbackInfo) {
        LiquidBounce.eventManager.callEvent(new Render3DEvent(partialTicks));
    }

    @Inject(method={"hurtCameraEffect"}, at={@At(value="HEAD")}, cancellable=true)
    private void injectHurtCameraEffect(CallbackInfo callbackInfo) {
        if (LiquidBounce.moduleManager.getModule(NoHurtCam.class).getState()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"orientCamera"}, at={@At(value="INVOKE", target="Lnet/minecraft/util/math/Vec3d;distanceTo(Lnet/minecraft/util/math/Vec3d;)D")}, cancellable=true)
    private void cameraClip(float partialTicks, CallbackInfo callbackInfo) {
        if (LiquidBounce.moduleManager.getModule(CameraClip.class).getState()) {
            callbackInfo.cancel();
            Entity entity = this.field_78531_r.func_175606_aa();
            float f = entity.func_70047_e();
            if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).func_70608_bn()) {
                f = (float)((double)f + 1.0);
                GlStateManager.func_179109_b((float)0.0f, (float)0.3f, (float)0.0f);
                if (!this.field_78531_r.field_71474_y.field_74325_U) {
                    BlockPos blockpos = new BlockPos(entity);
                    IBlockState iblockstate = this.field_78531_r.field_71441_e.func_180495_p(blockpos);
                    ForgeHooksClient.orientBedCamera((IBlockAccess)this.field_78531_r.field_71441_e, (BlockPos)blockpos, (IBlockState)iblockstate, (Entity)entity);
                    GlStateManager.func_179114_b((float)(entity.field_70126_B + (entity.field_70177_z - entity.field_70126_B) * partialTicks + 180.0f), (float)0.0f, (float)-1.0f, (float)0.0f);
                    GlStateManager.func_179114_b((float)(entity.field_70127_C + (entity.field_70125_A - entity.field_70127_C) * partialTicks), (float)-1.0f, (float)0.0f, (float)0.0f);
                }
            } else if (this.field_78531_r.field_71474_y.field_74320_O > 0) {
                double d3 = this.field_78491_C + (this.field_78490_B - this.field_78491_C) * partialTicks;
                if (this.field_78531_r.field_71474_y.field_74325_U) {
                    GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)((float)(-d3)));
                } else {
                    float f1 = entity.field_70177_z;
                    float f2 = entity.field_70125_A;
                    if (this.field_78531_r.field_71474_y.field_74320_O == 2) {
                        f2 += 180.0f;
                    }
                    if (this.field_78531_r.field_71474_y.field_74320_O == 2) {
                        GlStateManager.func_179114_b((float)180.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                    }
                    GlStateManager.func_179114_b((float)(entity.field_70125_A - f2), (float)1.0f, (float)0.0f, (float)0.0f);
                    GlStateManager.func_179114_b((float)(entity.field_70177_z - f1), (float)0.0f, (float)1.0f, (float)0.0f);
                    GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)((float)(-d3)));
                    GlStateManager.func_179114_b((float)(f1 - entity.field_70177_z), (float)0.0f, (float)1.0f, (float)0.0f);
                    GlStateManager.func_179114_b((float)(f2 - entity.field_70125_A), (float)1.0f, (float)0.0f, (float)0.0f);
                }
            } else {
                GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)-0.1f);
            }
            if (!this.field_78531_r.field_71474_y.field_74325_U) {
                float yaw = entity.field_70126_B + (entity.field_70177_z - entity.field_70126_B) * partialTicks + 180.0f;
                float pitch = entity.field_70127_C + (entity.field_70125_A - entity.field_70127_C) * partialTicks;
                float roll = 0.0f;
                if (entity instanceof EntityAnimal) {
                    EntityAnimal entityanimal = (EntityAnimal)entity;
                    yaw = entityanimal.field_70758_at + (entityanimal.field_70759_as - entityanimal.field_70758_at) * partialTicks + 180.0f;
                }
                IBlockState block = ActiveRenderInfo.func_186703_a((World)this.field_78531_r.field_71441_e, (Entity)entity, (float)partialTicks);
                EntityViewRenderEvent.CameraSetup event = new EntityViewRenderEvent.CameraSetup((EntityRenderer)this, entity, block, (double)partialTicks, yaw, pitch, roll);
                MinecraftForge.EVENT_BUS.post((Event)event);
                GlStateManager.func_179114_b((float)event.getRoll(), (float)0.0f, (float)0.0f, (float)1.0f);
                GlStateManager.func_179114_b((float)event.getPitch(), (float)1.0f, (float)0.0f, (float)0.0f);
                GlStateManager.func_179114_b((float)event.getYaw(), (float)0.0f, (float)1.0f, (float)0.0f);
            }
            GlStateManager.func_179109_b((float)0.0f, (float)(-f), (float)0.0f);
            double d0 = entity.field_70169_q + (entity.field_70165_t - entity.field_70169_q) * (double)partialTicks;
            double d1 = entity.field_70167_r + (entity.field_70163_u - entity.field_70167_r) * (double)partialTicks + (double)f;
            double d2 = entity.field_70166_s + (entity.field_70161_v - entity.field_70166_s) * (double)partialTicks;
            this.field_78500_U = this.field_78531_r.field_71438_f.func_72721_a(d0, d1, d2, partialTicks);
        }
    }

    @Overwrite
    public void func_78473_a(float partialTicks) {
        Entity entity = this.field_78531_r.func_175606_aa();
        if (entity != null && this.field_78531_r.field_71441_e != null) {
            RayTraceResult movingObjectPosition;
            this.field_78531_r.field_71424_I.func_76320_a("pick");
            this.field_78531_r.field_147125_j = null;
            Reach reach = (Reach)LiquidBounce.moduleManager.getModule(Reach.class);
            double d0 = reach.getState() ? (double)reach.getMaxRange() : (double)this.field_78531_r.field_71442_b.func_78757_d();
            this.field_78531_r.field_71476_x = entity.func_174822_a(reach.getState() ? (double)((Float)reach.getBuildReachValue().get()).floatValue() : d0, partialTicks);
            Vec3d vec3d = entity.func_174824_e(partialTicks);
            boolean flag = false;
            int i = 3;
            double d1 = d0;
            if (this.field_78531_r.field_71442_b.func_78749_i()) {
                d0 = d1 = 6.0;
            } else if (d0 > 3.0) {
                flag = true;
            }
            if (this.field_78531_r.field_71476_x != null) {
                d1 = this.field_78531_r.field_71476_x.field_72307_f.func_72438_d(vec3d);
            }
            if (reach.getState() && (movingObjectPosition = entity.func_174822_a(d1 = (double)((Float)reach.getCombatReachValue().get()).floatValue(), partialTicks)) != null) {
                d1 = movingObjectPosition.field_72307_f.func_72438_d(vec3d);
            }
            Vec3d vec3d1 = entity.func_70676_i(1.0f);
            Vec3d vec3d2 = vec3d.func_72441_c(vec3d1.field_72450_a * d0, vec3d1.field_72448_b * d0, vec3d1.field_72449_c * d0);
            this.field_78528_u = null;
            Vec3d vec3d3 = null;
            float f = 1.0f;
            List list = this.field_78531_r.field_71441_e.func_175674_a(entity, entity.func_174813_aQ().func_72321_a(vec3d1.field_72450_a * d0, vec3d1.field_72448_b * d0, vec3d1.field_72449_c * d0).func_72314_b(1.0, 1.0, 1.0), Predicates.and((Predicate)EntitySelectors.field_180132_d, p_apply_1_ -> p_apply_1_ != null && p_apply_1_.func_70067_L()));
            double d2 = d1;
            for (Entity entity1 : list) {
                double d3;
                AxisAlignedBB axisalignedbb = entity1.func_174813_aQ().func_186662_g((double)entity1.func_70111_Y());
                RayTraceResult raytraceresult = axisalignedbb.func_72327_a(vec3d, vec3d2);
                if (axisalignedbb.func_72318_a(vec3d)) {
                    if (!(d2 >= 0.0)) continue;
                    this.field_78528_u = entity1;
                    vec3d3 = raytraceresult == null ? vec3d : raytraceresult.field_72307_f;
                    d2 = 0.0;
                    continue;
                }
                if (raytraceresult == null || !((d3 = vec3d.func_72438_d(raytraceresult.field_72307_f)) < d2) && d2 != 0.0) continue;
                if (entity1.func_184208_bv() == entity.func_184208_bv() && !entity1.canRiderInteract()) {
                    if (d2 != 0.0) continue;
                    this.field_78528_u = entity1;
                    vec3d3 = raytraceresult.field_72307_f;
                    continue;
                }
                this.field_78528_u = entity1;
                vec3d3 = raytraceresult.field_72307_f;
                d2 = d3;
            }
            if (this.field_78528_u != null && flag) {
                double d = vec3d.func_72438_d(vec3d3);
                double d3 = reach.getState() ? (double)((Float)reach.getCombatReachValue().get()).floatValue() : 3.0;
                if (d > d3) {
                    this.field_78528_u = null;
                    this.field_78531_r.field_71476_x = new RayTraceResult(RayTraceResult.Type.MISS, vec3d3, null, new BlockPos(vec3d3));
                }
            }
            if (this.field_78528_u != null && (d2 < d1 || this.field_78531_r.field_71476_x == null)) {
                this.field_78531_r.field_71476_x = new RayTraceResult(this.field_78528_u, vec3d3);
                if (this.field_78528_u instanceof EntityLivingBase || this.field_78528_u instanceof EntityItemFrame) {
                    this.field_78531_r.field_147125_j = this.field_78528_u;
                }
            }
            this.field_78531_r.field_71424_I.func_76319_b();
        }
    }
}

