/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockFence
 *  net.minecraft.block.BlockFenceGate
 *  net.minecraft.block.BlockWall
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.audio.ISound
 *  net.minecraft.client.audio.PositionedSoundRecord
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.crash.CrashReport
 *  net.minecraft.crash.CrashReportCategory
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.IJumpingMount
 *  net.minecraft.entity.MoverType
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.init.MobEffects
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.item.ItemElytra
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.client.CPacketPlayer$PositionRotation
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.util.EnumFacing$Axis
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.MovementInput
 *  net.minecraft.util.ReportedException
 *  net.minecraft.util.SoundEvent
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraftforge.client.ForgeHooksClient
 *  net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Overwrite
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.entity;

import java.util.Arrays;
import java.util.List;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.PushOutEvent;
import net.ccbluex.liquidbounce.event.SlowDownEvent;
import net.ccbluex.liquidbounce.event.StepConfirmEvent;
import net.ccbluex.liquidbounce.event.StepEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.exploit.AntiHunger;
import net.ccbluex.liquidbounce.features.module.modules.exploit.Derp;
import net.ccbluex.liquidbounce.features.module.modules.exploit.PortalMenu;
import net.ccbluex.liquidbounce.features.module.modules.movement.InventoryMove;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoSlow;
import net.ccbluex.liquidbounce.features.module.modules.movement.Sneak;
import net.ccbluex.liquidbounce.features.module.modules.movement.Sprint;
import net.ccbluex.liquidbounce.features.module.modules.render.NoSwing;
import net.ccbluex.liquidbounce.features.module.modules.render.OldHitting;
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold;
import net.ccbluex.liquidbounce.injection.forge.mixins.entity.MixinAbstractClientPlayer;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.MovementInput;
import net.minecraft.util.ReportedException;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SideOnly(value=Side.CLIENT)
@Mixin(value={EntityPlayerSP.class})
public abstract class MixinEntityPlayerSP
extends MixinAbstractClientPlayer {
    @Shadow
    public boolean field_175171_bO;
    @Shadow
    public int field_71157_e;
    @Shadow
    public float field_71086_bY;
    @Shadow
    public float field_71080_cy;
    @Shadow
    public MovementInput field_71158_b;
    @Shadow
    public float field_110321_bQ;
    @Shadow
    public int field_110320_a;
    @Shadow
    @Final
    public NetHandlerPlayClient field_71174_a;
    @Shadow
    protected int field_71156_d;
    @Shadow
    protected Minecraft field_71159_c;
    @Shadow
    private boolean field_175170_bN;
    @Shadow
    private double field_175172_bI;
    @Shadow
    private int field_175168_bP;
    @Shadow
    private double field_175166_bJ;
    @Shadow
    private double field_175167_bK;
    @Shadow
    private float field_175164_bL;
    @Shadow
    private float field_175165_bM;
    @Shadow
    private int field_189812_cs;
    @Shadow
    private boolean field_189813_ct;
    @Shadow
    private boolean field_184841_cd;
    @Shadow
    private boolean field_189811_cr;

    @Override
    @Shadow
    public abstract void func_184185_a(SoundEvent var1, float var2, float var3);

    @Shadow
    public abstract void func_70031_b(boolean var1);

    @Shadow
    protected abstract boolean func_145771_j(double var1, double var3, double var5);

    @Shadow
    public abstract void func_71016_p();

    @Shadow
    protected abstract void func_110318_g();

    @Shadow
    public abstract boolean func_110317_t();

    @Override
    @Shadow
    public abstract boolean func_70093_af();

    @Shadow
    protected abstract boolean func_175160_A();

    @Shadow
    public abstract void func_71053_j();

    @Override
    @Shadow
    public abstract boolean func_184587_cr();

    @Shadow
    public abstract float func_110319_bJ();

    @Shadow
    protected abstract void func_189810_i(float var1, float var2);

    @Overwrite
    public void func_175161_p() {
        try {
            boolean flag1;
            boolean clientSprintState;
            LiquidBounce.eventManager.callEvent(new MotionEvent(EventState.PRE));
            InventoryMove inventoryMove = (InventoryMove)LiquidBounce.moduleManager.getModule(InventoryMove.class);
            Sneak sneak = (Sneak)LiquidBounce.moduleManager.getModule(Sneak.class);
            boolean fakeSprint = inventoryMove.getState() && (Boolean)inventoryMove.getAacAdditionProValue().get() != false || LiquidBounce.moduleManager.getModule(AntiHunger.class).getState() || sneak.getState() && (!MovementUtils.isMoving() || (Boolean)sneak.stopMoveValue.get() == false) && ((String)sneak.modeValue.get()).equalsIgnoreCase("MineSecure");
            boolean bl = clientSprintState = this.func_70051_ag() && !fakeSprint;
            if (clientSprintState != this.field_175171_bO) {
                if (clientSprintState) {
                    this.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)((EntityPlayerSP)this), CPacketEntityAction.Action.START_SPRINTING));
                } else {
                    this.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)((EntityPlayerSP)this), CPacketEntityAction.Action.STOP_SPRINTING));
                }
                this.field_175171_bO = clientSprintState;
            }
            if ((flag1 = this.func_70093_af()) != this.field_175170_bN && (!sneak.getState() || ((String)sneak.modeValue.get()).equalsIgnoreCase("Legit"))) {
                if (flag1) {
                    this.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)((EntityPlayerSP)this), CPacketEntityAction.Action.START_SNEAKING));
                } else {
                    this.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)((EntityPlayerSP)this), CPacketEntityAction.Action.STOP_SNEAKING));
                }
                this.field_175170_bN = flag1;
            }
            if (this.func_175160_A()) {
                boolean flag3;
                float yaw = this.field_70177_z;
                float pitch = this.field_70125_A;
                float lastReportedYaw = RotationUtils.serverRotation.getYaw();
                float lastReportedPitch = RotationUtils.serverRotation.getPitch();
                Derp derp = (Derp)LiquidBounce.moduleManager.getModule(Derp.class);
                if (derp.getState()) {
                    float[] rot = derp.getRotation();
                    yaw = rot[0];
                    pitch = rot[1];
                }
                if (RotationUtils.targetRotation != null) {
                    yaw = RotationUtils.targetRotation.getYaw();
                    pitch = RotationUtils.targetRotation.getPitch();
                }
                AxisAlignedBB axisalignedbb = this.func_174813_aQ();
                double xDiff = this.field_70165_t - this.field_175172_bI;
                double yDiff = axisalignedbb.field_72338_b - this.field_175166_bJ;
                double zDiff = this.field_70161_v - this.field_175167_bK;
                double yawDiff = yaw - lastReportedYaw;
                double pitchDiff = pitch - lastReportedPitch;
                ++this.field_175168_bP;
                boolean flag2 = xDiff * xDiff + yDiff * yDiff + zDiff * zDiff > 9.0E-4 || this.field_175168_bP >= 20;
                boolean bl2 = flag3 = yawDiff != 0.0 || pitchDiff != 0.0;
                if (this.func_184218_aH()) {
                    this.field_71174_a.func_147297_a((Packet)new CPacketPlayer.PositionRotation(this.field_70159_w, -999.0, this.field_70179_y, this.field_70177_z, this.field_70125_A, this.field_70122_E));
                    flag2 = false;
                } else if (flag2 && flag3) {
                    this.field_71174_a.func_147297_a((Packet)new CPacketPlayer.PositionRotation(this.field_70165_t, axisalignedbb.field_72338_b, this.field_70161_v, this.field_70177_z, this.field_70125_A, this.field_70122_E));
                } else if (flag2) {
                    this.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(this.field_70165_t, axisalignedbb.field_72338_b, this.field_70161_v, this.field_70122_E));
                } else if (flag3) {
                    this.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(this.field_70177_z, this.field_70125_A, this.field_70122_E));
                } else if (this.field_184841_cd != this.field_70122_E) {
                    this.field_71174_a.func_147297_a((Packet)new CPacketPlayer(this.field_70122_E));
                }
                if (flag2) {
                    this.field_175172_bI = this.field_70165_t;
                    this.field_175166_bJ = axisalignedbb.field_72338_b;
                    this.field_175167_bK = this.field_70161_v;
                    this.field_175168_bP = 0;
                }
                if (flag3) {
                    this.field_175164_bL = this.field_70177_z;
                    this.field_175165_bM = this.field_70125_A;
                }
                this.field_184841_cd = this.field_70122_E;
                this.field_189811_cr = this.field_71159_c.field_71474_y.field_189989_R;
            }
            LiquidBounce.eventManager.callEvent(new MotionEvent(EventState.POST));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Inject(method={"swingArm"}, at={@At(value="HEAD")}, cancellable=true)
    private void swingItem(EnumHand hand, CallbackInfo callbackInfo) {
        NoSwing noSwing = (NoSwing)LiquidBounce.moduleManager.getModule(NoSwing.class);
        if (noSwing.getState()) {
            callbackInfo.cancel();
            if (!((Boolean)noSwing.getServerSideValue().get()).booleanValue()) {
                this.field_71174_a.func_147297_a((Packet)new CPacketAnimation(hand));
            }
        }
    }

    @Inject(method={"pushOutOfBlocks"}, at={@At(value="HEAD")}, cancellable=true)
    private void onPushOutOfBlocks(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        PushOutEvent event = new PushOutEvent();
        if (this.field_70145_X) {
            event.cancelEvent();
        }
        LiquidBounce.eventManager.callEvent(event);
        if (event.isCancelled()) {
            callbackInfoReturnable.setReturnValue((Object)false);
        }
    }

    @Override
    @Overwrite
    public void func_70636_d() {
        ItemStack itemstack;
        boolean flag4;
        boolean flag3;
        NoSlow noSlow;
        boolean flag;
        block50: {
            block49: {
                Scaffold scaffold;
                AxisAlignedBB axisalignedbb;
                PlayerSPPushOutOfBlocksEvent event;
                try {
                    LiquidBounce.eventManager.callEvent(new UpdateEvent());
                }
                catch (Throwable e) {
                    e.printStackTrace();
                }
                ++this.field_71157_e;
                if (this.field_71156_d > 0) {
                    --this.field_71156_d;
                }
                this.field_71080_cy = this.field_71086_bY;
                if (this.field_71087_bX) {
                    if (this.field_71159_c.field_71462_r != null && !this.field_71159_c.field_71462_r.func_73868_f() && !LiquidBounce.moduleManager.getModule(PortalMenu.class).getState()) {
                        if (this.field_71159_c.field_71462_r instanceof GuiContainer) {
                            this.func_71053_j();
                        }
                        this.field_71159_c.func_147108_a(null);
                    }
                    if (this.field_71086_bY == 0.0f) {
                        this.field_71159_c.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_184371_a((SoundEvent)SoundEvents.field_187814_ei, (float)(this.field_70146_Z.nextFloat() * 0.4f + 0.8f)));
                    }
                    this.field_71086_bY += 0.0125f;
                    if (this.field_71086_bY >= 1.0f) {
                        this.field_71086_bY = 1.0f;
                    }
                    this.field_71087_bX = false;
                } else if (this.func_70644_a(MobEffects.field_76431_k) && this.func_70660_b(MobEffects.field_76431_k).func_76459_b() > 60) {
                    this.field_71086_bY += 0.006666667f;
                    if (this.field_71086_bY > 1.0f) {
                        this.field_71086_bY = 1.0f;
                    }
                } else {
                    if (this.field_71086_bY > 0.0f) {
                        this.field_71086_bY -= 0.05f;
                    }
                    if (this.field_71086_bY < 0.0f) {
                        this.field_71086_bY = 0.0f;
                    }
                }
                if (this.field_71088_bW > 0) {
                    --this.field_71088_bW;
                }
                flag = this.field_71158_b.field_78901_c;
                boolean flag1 = this.field_71158_b.field_78899_d;
                float f = 0.8f;
                boolean flag2 = this.field_71158_b.field_192832_b >= 0.8f;
                this.field_71158_b.func_78898_a();
                noSlow = (NoSlow)LiquidBounce.moduleManager.getModule(NoSlow.class);
                KillAura killAura = (KillAura)LiquidBounce.moduleManager.getModule(KillAura.class);
                ForgeHooksClient.onInputUpdate((EntityPlayer)((EntityPlayerSP)this), (MovementInput)this.field_71158_b);
                this.field_71159_c.func_193032_ao().func_193293_a(this.field_71158_b);
                if (this.func_184587_cr() || this.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() instanceof ItemSword && killAura.getBlockingStatus() && !this.func_184218_aH()) {
                    SlowDownEvent slowDownEvent = new SlowDownEvent(0.2f, 0.2f);
                    LiquidBounce.eventManager.callEvent(slowDownEvent);
                    this.field_71158_b.field_78902_a *= slowDownEvent.getStrafe();
                    this.field_71158_b.field_192832_b *= slowDownEvent.getForward();
                    this.field_71156_d = 0;
                }
                flag3 = false;
                if (this.field_189812_cs > 0) {
                    --this.field_189812_cs;
                    flag3 = true;
                    this.field_71158_b.field_78901_c = true;
                }
                if (!MinecraftForge.EVENT_BUS.post((Event)(event = new PlayerSPPushOutOfBlocksEvent((EntityPlayer)((EntityPlayerSP)this), axisalignedbb = this.func_174813_aQ())))) {
                    axisalignedbb = event.getEntityBoundingBox();
                    this.func_145771_j(this.field_70165_t - (double)this.field_70130_N * 0.35, axisalignedbb.field_72338_b + 0.5, this.field_70161_v + (double)this.field_70130_N * 0.35);
                    this.func_145771_j(this.field_70165_t - (double)this.field_70130_N * 0.35, axisalignedbb.field_72338_b + 0.5, this.field_70161_v - (double)this.field_70130_N * 0.35);
                    this.func_145771_j(this.field_70165_t + (double)this.field_70130_N * 0.35, axisalignedbb.field_72338_b + 0.5, this.field_70161_v - (double)this.field_70130_N * 0.35);
                    this.func_145771_j(this.field_70165_t + (double)this.field_70130_N * 0.35, axisalignedbb.field_72338_b + 0.5, this.field_70161_v + (double)this.field_70130_N * 0.35);
                }
                Sprint sprint = (Sprint)LiquidBounce.moduleManager.getModule(Sprint.class);
                boolean bl = flag4 = (Boolean)sprint.foodValue.get() == false || (float)this.func_71024_bL().func_75116_a() > 6.0f || this.field_71075_bZ.field_75101_c;
                if (this.field_70122_E && !flag1 && !flag2 && this.field_71158_b.field_192832_b >= 0.8f && !this.func_70051_ag() && flag4 && !this.func_184587_cr() && !this.func_70644_a(MobEffects.field_76440_q)) {
                    if (this.field_71156_d <= 0 && !this.field_71159_c.field_71474_y.field_151444_V.func_151470_d()) {
                        this.field_71156_d = 7;
                    } else {
                        this.func_70031_b(true);
                    }
                }
                if (!this.func_70051_ag() && this.field_71158_b.field_192832_b >= 0.8f && flag4 && (noSlow.getState() || !this.func_184587_cr()) && !this.func_70644_a(MobEffects.field_76440_q) && this.field_71159_c.field_71474_y.field_151444_V.func_151470_d()) {
                    this.func_70031_b(true);
                }
                if ((scaffold = (Scaffold)LiquidBounce.moduleManager.getModule(Scaffold.class)).getState() && !((Boolean)scaffold.sprintValue.get()).booleanValue()) break block49;
                if (!sprint.getState() || !((Boolean)sprint.checkServerSide.get()).booleanValue() || !this.field_70122_E && ((Boolean)sprint.checkServerSideGround.get()).booleanValue() || ((Boolean)sprint.allDirectionsValue.get()).booleanValue() || RotationUtils.targetRotation == null) break block50;
                Rotation rotation = new Rotation(this.field_71159_c.field_71439_g.field_70177_z, this.field_71159_c.field_71439_g.field_70125_A);
                if (!(RotationUtils.getRotationDifference(rotation) > 30.0)) break block50;
            }
            this.func_70031_b(false);
        }
        if (this.func_70051_ag() && (this.field_71158_b.field_192832_b < 0.8f || this.field_70123_F || !flag4)) {
            this.func_70031_b(false);
        }
        if (this.field_71075_bZ.field_75101_c) {
            if (this.field_71159_c.field_71442_b.func_178887_k()) {
                if (!this.field_71075_bZ.field_75100_b) {
                    this.field_71075_bZ.field_75100_b = true;
                    this.func_71016_p();
                }
            } else if (!flag && this.field_71158_b.field_78901_c && !flag3) {
                if (this.field_71101_bC == 0) {
                    this.field_71101_bC = 7;
                } else {
                    this.field_71075_bZ.field_75100_b = !this.field_71075_bZ.field_75100_b;
                    this.func_71016_p();
                    this.field_71101_bC = 0;
                }
            }
        }
        if (this.field_71158_b.field_78901_c && !flag && !this.field_70122_E && this.field_70181_x < 0.0 && !this.func_184613_cA() && !this.field_71075_bZ.field_75100_b && (itemstack = this.func_184582_a(EntityEquipmentSlot.CHEST)).func_77973_b() == Items.field_185160_cR && ItemElytra.func_185069_d((ItemStack)itemstack)) {
            this.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)((EntityPlayerSP)this), CPacketEntityAction.Action.START_FALL_FLYING));
        }
        this.field_189813_ct = this.func_184613_cA();
        if (this.field_71075_bZ.field_75100_b && this.func_175160_A()) {
            if (this.field_71158_b.field_78899_d) {
                this.field_71158_b.field_78902_a = (float)((double)this.field_71158_b.field_78902_a / 0.3);
                this.field_71158_b.field_192832_b = (float)((double)this.field_71158_b.field_192832_b / 0.3);
                this.field_70181_x -= (double)(this.field_71075_bZ.func_75093_a() * 3.0f);
            }
            if (this.field_71158_b.field_78901_c) {
                this.field_70181_x += (double)(this.field_71075_bZ.func_75093_a() * 3.0f);
            }
        }
        if (this.func_110317_t()) {
            IJumpingMount ijumpingmount = (IJumpingMount)this.func_184187_bx();
            if (this.field_110320_a < 0) {
                ++this.field_110320_a;
                if (this.field_110320_a == 0) {
                    this.field_110321_bQ = 0.0f;
                }
            }
            if (flag && !this.field_71158_b.field_78901_c) {
                this.field_110320_a = -10;
                ijumpingmount.func_110206_u(MathHelper.func_76141_d((float)(this.func_110319_bJ() * 100.0f)));
                this.func_110318_g();
            } else if (!flag && this.field_71158_b.field_78901_c) {
                this.field_110320_a = 0;
                this.field_110321_bQ = 0.0f;
            } else if (flag) {
                ++this.field_110320_a;
                this.field_110321_bQ = this.field_110320_a < 10 ? (float)this.field_110320_a * 0.1f : 0.8f + 2.0f / (float)(this.field_110320_a - 9) * 0.1f;
            }
        } else {
            this.field_110321_bQ = 0.0f;
        }
        ItemStack shield = new ItemStack(Items.field_185159_cQ);
        OldHitting ot = (OldHitting)LiquidBounce.moduleManager.getModule(OldHitting.class);
        if (!this.field_71159_c.field_71439_g.func_184587_cr() && noSlow.getState() && ((Boolean)noSlow.getValue().get()).booleanValue() && this.field_71159_c.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemSword) {
            this.field_71159_c.field_71439_g.field_71071_by.field_184439_c.set(0, (Object)ItemStack.field_190927_a);
        }
        super.func_70636_d();
        if (this.field_70122_E && this.field_71075_bZ.field_75100_b && !this.field_71159_c.field_71442_b.func_178887_k()) {
            this.field_71075_bZ.field_75100_b = false;
            this.func_71016_p();
        }
    }

    @Override
    @Overwrite
    public void func_70091_d(MoverType type, double x, double y, double z) {
        MoveEvent moveEvent = new MoveEvent(x, y, z);
        LiquidBounce.eventManager.callEvent(moveEvent);
        if (moveEvent.isCancelled()) {
            return;
        }
        x = moveEvent.getX();
        y = moveEvent.getY();
        z = moveEvent.getZ();
        if (this.field_70145_X) {
            this.func_174826_a(this.func_174813_aQ().func_72317_d(x, y, z));
            this.func_174829_m();
        } else {
            BlockPos blockpos1;
            IBlockState iblockstate1;
            Block block1;
            boolean flag;
            if (type == MoverType.PISTON) {
                long i = this.field_70170_p.func_82737_E();
                if (i != this.field_191506_aJ) {
                    Arrays.fill(this.field_191505_aI, 0.0);
                    this.field_191506_aJ = i;
                }
                if (x != 0.0) {
                    int j = EnumFacing.Axis.X.ordinal();
                    double d0 = MathHelper.func_151237_a((double)(x + this.field_191505_aI[j]), (double)-0.51, (double)0.51);
                    x = d0 - this.field_191505_aI[j];
                    this.field_191505_aI[j] = d0;
                    if (Math.abs(x) <= (double)1.0E-5f) {
                        return;
                    }
                } else if (y != 0.0) {
                    int l4 = EnumFacing.Axis.Y.ordinal();
                    double d12 = MathHelper.func_151237_a((double)(y + this.field_191505_aI[l4]), (double)-0.51, (double)0.51);
                    y = d12 - this.field_191505_aI[l4];
                    this.field_191505_aI[l4] = d12;
                    if (Math.abs(y) <= (double)1.0E-5f) {
                        return;
                    }
                } else {
                    if (z == 0.0) {
                        return;
                    }
                    int i5 = EnumFacing.Axis.Z.ordinal();
                    double d13 = MathHelper.func_151237_a((double)(z + this.field_191505_aI[i5]), (double)-0.51, (double)0.51);
                    z = d13 - this.field_191505_aI[i5];
                    this.field_191505_aI[i5] = d13;
                    if (Math.abs(z) <= (double)1.0E-5f) {
                        return;
                    }
                }
            }
            this.field_70170_p.field_72984_F.func_76320_a("move");
            double d10 = this.field_70165_t;
            double d11 = this.field_70163_u;
            double d1 = this.field_70161_v;
            if (this.field_70134_J) {
                this.field_70134_J = false;
                x *= 0.25;
                y *= (double)0.05f;
                z *= 0.25;
                this.field_70159_w = 0.0;
                this.field_70181_x = 0.0;
                this.field_70179_y = 0.0;
            }
            double d2 = x;
            double d3 = y;
            double d4 = z;
            if ((type == MoverType.SELF || type == MoverType.PLAYER) && (this.field_70122_E && this.func_70093_af() || moveEvent.isSafeWalk()) && this instanceof EntityPlayer) {
                double d5 = 0.05;
                while (x != 0.0 && this.field_70170_p.func_184144_a((Entity)((EntityPlayerSP)this), this.func_174813_aQ().func_72317_d(x, (double)(-this.field_70138_W), 0.0)).isEmpty()) {
                    x = x < 0.05 && x >= -0.05 ? 0.0 : (x > 0.0 ? (x -= 0.05) : (x += 0.05));
                    d2 = x;
                }
                while (z != 0.0 && this.field_70170_p.func_184144_a((Entity)((EntityPlayerSP)this), this.func_174813_aQ().func_72317_d(0.0, (double)(-this.field_70138_W), z)).isEmpty()) {
                    z = z < 0.05 && z >= -0.05 ? 0.0 : (z > 0.0 ? (z -= 0.05) : (z += 0.05));
                    d4 = z;
                }
                while (x != 0.0 && z != 0.0 && this.field_70170_p.func_184144_a((Entity)((EntityPlayerSP)this), this.func_174813_aQ().func_72317_d(x, (double)(-this.field_70138_W), z)).isEmpty()) {
                    x = x < 0.05 && x >= -0.05 ? 0.0 : (x > 0.0 ? (x -= 0.05) : (x += 0.05));
                    d2 = x;
                    z = z < 0.05 && z >= -0.05 ? 0.0 : (z > 0.0 ? (z -= 0.05) : (z += 0.05));
                    d4 = z;
                }
            }
            List list1 = this.field_70170_p.func_184144_a((Entity)((EntityPlayerSP)this), this.func_174813_aQ().func_72321_a(x, y, z));
            AxisAlignedBB axisalignedbb = this.func_174813_aQ();
            if (y != 0.0) {
                int l = list1.size();
                for (int k = 0; k < l; ++k) {
                    y = ((AxisAlignedBB)list1.get(k)).func_72323_b(this.func_174813_aQ(), y);
                }
                this.func_174826_a(this.func_174813_aQ().func_72317_d(0.0, y, 0.0));
            }
            if (x != 0.0) {
                int l5 = list1.size();
                for (int j5 = 0; j5 < l5; ++j5) {
                    x = ((AxisAlignedBB)list1.get(j5)).func_72316_a(this.func_174813_aQ(), x);
                }
                if (x != 0.0) {
                    this.func_174826_a(this.func_174813_aQ().func_72317_d(x, 0.0, 0.0));
                }
            }
            if (z != 0.0) {
                int i6 = list1.size();
                for (int k5 = 0; k5 < i6; ++k5) {
                    z = ((AxisAlignedBB)list1.get(k5)).func_72322_c(this.func_174813_aQ(), z);
                }
                if (z != 0.0) {
                    this.func_174826_a(this.func_174813_aQ().func_72317_d(0.0, 0.0, z));
                }
            }
            boolean bl = flag = this.field_70122_E || d3 != y && d3 < 0.0;
            if (this.field_70138_W > 0.0f && flag && (d2 != x || d4 != z)) {
                StepEvent stepEvent = new StepEvent(this.field_70138_W);
                LiquidBounce.eventManager.callEvent(stepEvent);
                double d14 = x;
                double d6 = y;
                double d7 = z;
                AxisAlignedBB axisalignedbb1 = this.func_174813_aQ();
                this.func_174826_a(axisalignedbb);
                y = stepEvent.getStepHeight();
                List list = this.field_70170_p.func_184144_a((Entity)((EntityPlayerSP)this), this.func_174813_aQ().func_72321_a(d2, y, d4));
                AxisAlignedBB axisalignedbb2 = this.func_174813_aQ();
                AxisAlignedBB axisalignedbb3 = axisalignedbb2.func_72321_a(d2, 0.0, d4);
                double d8 = y;
                int k1 = list.size();
                for (int j1 = 0; j1 < k1; ++j1) {
                    d8 = ((AxisAlignedBB)list.get(j1)).func_72323_b(axisalignedbb3, d8);
                }
                axisalignedbb2 = axisalignedbb2.func_72317_d(0.0, d8, 0.0);
                double d18 = d2;
                int i2 = list.size();
                for (int l1 = 0; l1 < i2; ++l1) {
                    d18 = ((AxisAlignedBB)list.get(l1)).func_72316_a(axisalignedbb2, d18);
                }
                axisalignedbb2 = axisalignedbb2.func_72317_d(d18, 0.0, 0.0);
                double d19 = d4;
                int k2 = list.size();
                for (int j2 = 0; j2 < k2; ++j2) {
                    d19 = ((AxisAlignedBB)list.get(j2)).func_72322_c(axisalignedbb2, d19);
                }
                axisalignedbb2 = axisalignedbb2.func_72317_d(0.0, 0.0, d19);
                AxisAlignedBB axisalignedbb4 = this.func_174813_aQ();
                double d20 = y;
                int i3 = list.size();
                for (int l2 = 0; l2 < i3; ++l2) {
                    d20 = ((AxisAlignedBB)list.get(l2)).func_72323_b(axisalignedbb4, d20);
                }
                axisalignedbb4 = axisalignedbb4.func_72317_d(0.0, d20, 0.0);
                double d21 = d2;
                int k3 = list.size();
                for (int j3 = 0; j3 < k3; ++j3) {
                    d21 = ((AxisAlignedBB)list.get(j3)).func_72316_a(axisalignedbb4, d21);
                }
                axisalignedbb4 = axisalignedbb4.func_72317_d(d21, 0.0, 0.0);
                double d22 = d4;
                int i4 = list.size();
                for (int l3 = 0; l3 < i4; ++l3) {
                    d22 = ((AxisAlignedBB)list.get(l3)).func_72322_c(axisalignedbb4, d22);
                }
                axisalignedbb4 = axisalignedbb4.func_72317_d(0.0, 0.0, d22);
                double d23 = d18 * d18 + d19 * d19;
                double d9 = d21 * d21 + d22 * d22;
                if (d23 > d9) {
                    x = d18;
                    z = d19;
                    y = -d8;
                    this.func_174826_a(axisalignedbb2);
                } else {
                    x = d21;
                    z = d22;
                    y = -d20;
                    this.func_174826_a(axisalignedbb4);
                }
                int k4 = list.size();
                for (int j4 = 0; j4 < k4; ++j4) {
                    y = ((AxisAlignedBB)list.get(j4)).func_72323_b(this.func_174813_aQ(), y);
                }
                this.func_174826_a(this.func_174813_aQ().func_72317_d(0.0, y, 0.0));
                if (d14 * d14 + d7 * d7 >= x * x + z * z) {
                    x = d14;
                    y = d6;
                    z = d7;
                    this.func_174826_a(axisalignedbb1);
                } else {
                    LiquidBounce.eventManager.callEvent(new StepConfirmEvent());
                }
            }
            this.field_70170_p.field_72984_F.func_76319_b();
            this.field_70170_p.field_72984_F.func_76320_a("rest");
            this.func_174829_m();
            this.field_70123_F = d2 != x || d4 != z;
            this.field_70124_G = d3 != y;
            this.field_70122_E = this.field_70124_G && d3 < 0.0;
            this.field_70132_H = this.field_70123_F || this.field_70124_G;
            int j6 = MathHelper.func_76128_c((double)this.field_70165_t);
            int i1 = MathHelper.func_76128_c((double)(this.field_70163_u - (double)0.2f));
            int k6 = MathHelper.func_76128_c((double)this.field_70161_v);
            BlockPos blockpos = new BlockPos(j6, i1, k6);
            IBlockState iblockstate = this.field_70170_p.func_180495_p(blockpos);
            if (iblockstate.func_185904_a() == Material.field_151579_a && ((block1 = (iblockstate1 = this.field_70170_p.func_180495_p(blockpos1 = blockpos.func_177977_b())).func_177230_c()) instanceof BlockFence || block1 instanceof BlockWall || block1 instanceof BlockFenceGate)) {
                iblockstate = iblockstate1;
                blockpos = blockpos1;
            }
            this.func_184231_a(y, this.field_70122_E, iblockstate, blockpos);
            if (d2 != x) {
                this.field_70159_w = 0.0;
            }
            if (d4 != z) {
                this.field_70179_y = 0.0;
            }
            Block block = iblockstate.func_177230_c();
            if (d3 != y) {
                block.func_176216_a(this.field_70170_p, (Entity)((EntityPlayerSP)this));
            }
            if (!(!this.func_70041_e_() || this.field_70122_E && this.func_70093_af() && this instanceof EntityPlayer || this.func_184218_aH())) {
                double d15 = this.field_70165_t - d10;
                double d16 = this.field_70163_u - d11;
                double d17 = this.field_70161_v - d1;
                if (block != Blocks.field_150468_ap) {
                    d16 = 0.0;
                }
                if (block != null && this.field_70122_E) {
                    block.func_176199_a(this.field_70170_p, blockpos, (Entity)((EntityPlayerSP)this));
                }
                this.field_70140_Q = (float)((double)this.field_70140_Q + (double)MathHelper.func_76133_a((double)(d15 * d15 + d17 * d17)) * 0.6);
                this.field_82151_R = (float)((double)this.field_82151_R + (double)MathHelper.func_76133_a((double)(d15 * d15 + d16 * d16 + d17 * d17)) * 0.6);
                if (this.field_82151_R > (float)this.field_70150_b && iblockstate.func_185904_a() != Material.field_151579_a) {
                    this.field_70150_b = (int)this.field_82151_R + 1;
                    if (this.func_70090_H()) {
                        EntityPlayerSP entity = this.func_184207_aI() && this.func_184179_bs() != null ? this.func_184179_bs() : (EntityPlayerSP)this;
                        float f = entity == this ? 0.35f : 0.4f;
                        float f1 = MathHelper.func_76133_a((double)(entity.field_70159_w * entity.field_70159_w * (double)0.2f + entity.field_70181_x * entity.field_70181_x + entity.field_70179_y * entity.field_70179_y * (double)0.2f)) * f;
                        if (f1 > 1.0f) {
                            f1 = 1.0f;
                        }
                        this.func_184185_a(this.func_184184_Z(), f1, 1.0f + (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.4f);
                    } else {
                        this.func_180429_a(blockpos, block);
                    }
                } else if (this.field_82151_R > this.field_191959_ay && this.func_191957_ae() && iblockstate.func_185904_a() == Material.field_151579_a) {
                    this.field_191959_ay = this.func_191954_d(this.field_82151_R);
                }
            }
            try {
                this.func_145775_I();
            }
            catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.func_85055_a((Throwable)throwable, (String)"Checking entity block collision");
                CrashReportCategory crashreportcategory = crashreport.func_85058_a("Entity being checked for collision");
                this.func_85029_a(crashreportcategory);
                throw new ReportedException(crashreport);
            }
            boolean flag1 = this.func_70026_G();
            if (this.field_70170_p.func_147470_e(this.func_174813_aQ().func_186664_h(0.001))) {
                this.func_70081_e(1);
                if (!flag1) {
                    ++this.field_190534_ay;
                    if (this.field_190534_ay == 0) {
                        this.func_70015_d(8);
                    }
                }
            } else if (this.field_190534_ay <= 0) {
                this.field_190534_ay = -this.func_190531_bD();
            }
            if (flag1 && this.func_70027_ad()) {
                this.func_184185_a(SoundEvents.field_187541_bC, 0.7f, 1.6f + (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.4f);
                this.field_190534_ay = -this.func_190531_bD();
            }
            this.field_70170_p.field_72984_F.func_76319_b();
        }
        this.func_189810_i((float)(this.field_70165_t - this.field_70165_t), (float)(this.field_70161_v - this.field_70161_v));
    }
}

