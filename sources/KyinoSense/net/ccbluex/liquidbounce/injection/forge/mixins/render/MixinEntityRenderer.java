/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.base.Predicates
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.ActiveRenderInfo
 *  net.minecraft.client.renderer.EntityRenderer
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.item.EntityItemFrame
 *  net.minecraft.entity.passive.EntityAnimal
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EntitySelectors
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.MovingObjectPosition
 *  net.minecraft.util.MovingObjectPosition$MovingObjectType
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.Vec3
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 *  net.minecraftforge.client.ForgeHooksClient
 *  net.minecraftforge.client.event.EntityViewRenderEvent$CameraSetup
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.List;
import me.report.liquidware.modules.render.Camera;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.modules.player.Reach;
import net.ccbluex.liquidbounce.features.module.modules.render.Tracers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
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
    @Shadow
    private Minecraft field_78531_r;
    @Shadow
    private float field_78491_C;
    @Shadow
    private float field_78490_B;
    @Shadow
    private boolean field_78500_U;

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
        Camera camera = (Camera)LiquidBounce.moduleManager.getModule(Camera.class);
        if (camera.getState() && ((Boolean)camera.getNoHurt().get()).booleanValue()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"orientCamera"}, at={@At(value="INVOKE", target="Lnet/minecraft/util/Vec3;distanceTo(Lnet/minecraft/util/Vec3;)D")}, cancellable=true)
    private void cameraClip(float partialTicks, CallbackInfo callbackInfo) {
        Camera camera = (Camera)LiquidBounce.moduleManager.getModule(Camera.class);
        if (camera.getState() && ((Boolean)camera.getCameraClipValue().get()).booleanValue()) {
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
                Block block = ActiveRenderInfo.func_180786_a((World)this.field_78531_r.field_71441_e, (Entity)entity, (float)partialTicks);
                EntityViewRenderEvent.CameraSetup event = new EntityViewRenderEvent.CameraSetup((EntityRenderer)this, entity, block, (double)partialTicks, yaw, pitch, roll);
                MinecraftForge.EVENT_BUS.post((Event)event);
                GlStateManager.func_179114_b((float)event.roll, (float)0.0f, (float)0.0f, (float)1.0f);
                GlStateManager.func_179114_b((float)event.pitch, (float)1.0f, (float)0.0f, (float)0.0f);
                GlStateManager.func_179114_b((float)event.yaw, (float)0.0f, (float)1.0f, (float)0.0f);
            }
            GlStateManager.func_179109_b((float)0.0f, (float)(-f), (float)0.0f);
            double d0 = entity.field_70169_q + (entity.field_70165_t - entity.field_70169_q) * (double)partialTicks;
            double d1 = entity.field_70167_r + (entity.field_70163_u - entity.field_70167_r) * (double)partialTicks + (double)f;
            double d2 = entity.field_70166_s + (entity.field_70161_v - entity.field_70166_s) * (double)partialTicks;
            this.field_78500_U = this.field_78531_r.field_71438_f.func_72721_a(d0, d1, d2, partialTicks);
        }
    }

    @Inject(method={"setupCameraTransform"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/renderer/EntityRenderer;setupViewBobbing(F)V", shift=At.Shift.BEFORE)})
    private void setupCameraViewBobbingBefore(CallbackInfo callbackInfo) {
        if (LiquidBounce.moduleManager.getModule(Tracers.class).getState()) {
            GL11.glPushMatrix();
        }
    }

    @Inject(method={"setupCameraTransform"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/renderer/EntityRenderer;setupViewBobbing(F)V", shift=At.Shift.AFTER)})
    private void setupCameraViewBobbingAfter(CallbackInfo callbackInfo) {
        if (LiquidBounce.moduleManager.getModule(Tracers.class).getState()) {
            GL11.glPopMatrix();
        }
    }

    @Overwrite
    public void func_78473_a(float p_getMouseOver_1_) {
        Entity entity = this.field_78531_r.func_175606_aa();
        if (entity != null && this.field_78531_r.field_71441_e != null) {
            MovingObjectPosition movingObjectPosition;
            this.field_78531_r.field_71424_I.func_76320_a("pick");
            this.field_78531_r.field_147125_j = null;
            Reach reach = (Reach)LiquidBounce.moduleManager.getModule(Reach.class);
            double d0 = reach.getState() ? (double)reach.getMaxRange() : (double)this.field_78531_r.field_71442_b.func_78757_d();
            this.field_78531_r.field_71476_x = entity.func_174822_a(reach.getState() ? (double)((Float)reach.getBuildReachValue().get()).floatValue() : d0, p_getMouseOver_1_);
            double d1 = d0;
            Vec3 vec3 = entity.func_174824_e(p_getMouseOver_1_);
            boolean flag = false;
            if (this.field_78531_r.field_71442_b.func_78749_i()) {
                d0 = 6.0;
                d1 = 6.0;
            } else if (d0 > 3.0) {
                flag = true;
            }
            if (this.field_78531_r.field_71476_x != null) {
                d1 = this.field_78531_r.field_71476_x.field_72307_f.func_72438_d(vec3);
            }
            if (reach.getState() && (movingObjectPosition = entity.func_174822_a(d1 = (double)((Float)reach.getCombatReachValue().get()).floatValue(), p_getMouseOver_1_)) != null) {
                d1 = movingObjectPosition.field_72307_f.func_72438_d(vec3);
            }
            Vec3 vec31 = entity.func_70676_i(p_getMouseOver_1_);
            Vec3 vec32 = vec3.func_72441_c(vec31.field_72450_a * d0, vec31.field_72448_b * d0, vec31.field_72449_c * d0);
            this.field_78528_u = null;
            Vec3 vec33 = null;
            float f = 1.0f;
            List list = this.field_78531_r.field_71441_e.func_175674_a(entity, entity.func_174813_aQ().func_72321_a(vec31.field_72450_a * d0, vec31.field_72448_b * d0, vec31.field_72449_c * d0).func_72314_b((double)f, (double)f, (double)f), Predicates.and((Predicate)EntitySelectors.field_180132_d, p_apply_1_ -> p_apply_1_.func_70067_L()));
            double d2 = d1;
            for (int j = 0; j < list.size(); ++j) {
                double d3;
                Entity entity1 = (Entity)list.get(j);
                float f1 = entity1.func_70111_Y();
                AxisAlignedBB axisalignedbb = entity1.func_174813_aQ().func_72314_b((double)f1, (double)f1, (double)f1);
                MovingObjectPosition movingobjectposition = axisalignedbb.func_72327_a(vec3, vec32);
                if (axisalignedbb.func_72318_a(vec3)) {
                    if (!(d2 >= 0.0)) continue;
                    this.field_78528_u = entity1;
                    vec33 = movingobjectposition == null ? vec3 : movingobjectposition.field_72307_f;
                    d2 = 0.0;
                    continue;
                }
                if (movingobjectposition == null || !((d3 = vec3.func_72438_d(movingobjectposition.field_72307_f)) < d2) && d2 != 0.0) continue;
                if (entity1 == entity.field_70154_o && !entity.canRiderInteract()) {
                    if (d2 != 0.0) continue;
                    this.field_78528_u = entity1;
                    vec33 = movingobjectposition.field_72307_f;
                    continue;
                }
                this.field_78528_u = entity1;
                vec33 = movingobjectposition.field_72307_f;
                d2 = d3;
            }
            if (this.field_78528_u != null && flag) {
                double d = vec3.func_72438_d(vec33);
                double d3 = reach.getState() ? (double)((Float)reach.getCombatReachValue().get()).floatValue() : 3.0;
                if (d > d3) {
                    this.field_78528_u = null;
                    this.field_78531_r.field_71476_x = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec33, (EnumFacing)null, new BlockPos(vec33));
                }
            }
            if (this.field_78528_u != null && (d2 < d1 || this.field_78531_r.field_71476_x == null)) {
                this.field_78531_r.field_71476_x = new MovingObjectPosition(this.field_78528_u, vec33);
                if (this.field_78528_u instanceof EntityLivingBase || this.field_78528_u instanceof EntityItemFrame) {
                    this.field_78531_r.field_147125_j = this.field_78528_u;
                }
            }
            this.field_78531_r.field_71424_I.func_76319_b();
        }
    }
}

