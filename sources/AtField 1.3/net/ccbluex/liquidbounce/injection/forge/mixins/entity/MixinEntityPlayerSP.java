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
import net.ccbluex.liquidbounce.features.module.modules.exploit.PortalMenu;
import net.ccbluex.liquidbounce.features.module.modules.movement.InventoryMove;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoSlow;
import net.ccbluex.liquidbounce.features.module.modules.movement.Sneak;
import net.ccbluex.liquidbounce.features.module.modules.movement.Sprint;
import net.ccbluex.liquidbounce.features.module.modules.render.NoSwing;
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
    private boolean field_184841_cd;
    @Shadow
    public MovementInput field_71158_b;
    @Shadow
    public int field_110320_a;
    @Shadow
    public boolean field_175171_bO;
    @Shadow
    private boolean field_175170_bN;
    @Shadow
    public float field_71086_bY;
    @Shadow
    private int field_175168_bP;
    @Shadow
    private float field_175165_bM;
    @Shadow
    @Final
    public NetHandlerPlayClient field_71174_a;
    @Shadow
    private int field_189812_cs;
    @Shadow
    public float field_71080_cy;
    @Shadow
    protected int field_71156_d;
    @Shadow
    private boolean field_189813_ct;
    @Shadow
    private double field_175172_bI;
    @Shadow
    private double field_175167_bK;
    @Shadow
    private double field_175166_bJ;
    @Shadow
    private boolean field_189811_cr;
    @Shadow
    public float field_110321_bQ;
    @Shadow
    private float field_175164_bL;
    @Shadow
    public int field_71157_e;
    @Shadow
    protected Minecraft field_71159_c;

    @Shadow
    public abstract float func_110319_bJ();

    @Shadow
    public abstract void func_71016_p();

    @Override
    @Overwrite
    public void func_70091_d(MoverType moverType, double d, double d2, double d3) {
        MoveEvent moveEvent = new MoveEvent(d, d2, d3);
        LiquidBounce.eventManager.callEvent(moveEvent);
        if (moveEvent.isCancelled()) {
            return;
        }
        d = moveEvent.getX();
        d2 = moveEvent.getY();
        d3 = moveEvent.getZ();
        if (this.field_70145_X) {
            this.func_174826_a(this.func_174813_aQ().func_72317_d(d, d2, d3));
            this.func_174829_m();
        } else {
            BlockPos blockPos;
            IBlockState iBlockState;
            List list;
            AxisAlignedBB axisAlignedBB;
            int n;
            int n2;
            if (moverType == MoverType.PISTON) {
                double d4;
                int n3;
                long l = this.field_70170_p.func_82737_E();
                if (l != this.field_191506_aJ) {
                    Arrays.fill(this.field_191505_aI, 0.0);
                    this.field_191506_aJ = l;
                }
                if (d != 0.0) {
                    n3 = EnumFacing.Axis.X.ordinal();
                    d4 = MathHelper.func_151237_a((double)(d + this.field_191505_aI[n3]), (double)-0.51, (double)0.51);
                    d = d4 - this.field_191505_aI[n3];
                    this.field_191505_aI[n3] = d4;
                    if (Math.abs(d) <= (double)1.0E-5f) {
                        return;
                    }
                } else if (d2 != 0.0) {
                    n3 = EnumFacing.Axis.Y.ordinal();
                    d4 = MathHelper.func_151237_a((double)(d2 + this.field_191505_aI[n3]), (double)-0.51, (double)0.51);
                    d2 = d4 - this.field_191505_aI[n3];
                    this.field_191505_aI[n3] = d4;
                    if (Math.abs(d2) <= (double)1.0E-5f) {
                        return;
                    }
                } else {
                    if (d3 == 0.0) {
                        return;
                    }
                    n3 = EnumFacing.Axis.Z.ordinal();
                    d4 = MathHelper.func_151237_a((double)(d3 + this.field_191505_aI[n3]), (double)-0.51, (double)0.51);
                    d3 = d4 - this.field_191505_aI[n3];
                    this.field_191505_aI[n3] = d4;
                    if (Math.abs(d3) <= (double)1.0E-5f) {
                        return;
                    }
                }
            }
            this.field_70170_p.field_72984_F.func_76320_a("move");
            double d5 = this.field_70165_t;
            double d6 = this.field_70163_u;
            double d7 = this.field_70161_v;
            if (this.field_70134_J) {
                this.field_70134_J = false;
                d *= 0.25;
                d2 *= (double)0.05f;
                d3 *= 0.25;
                this.field_70159_w = 0.0;
                this.field_70181_x = 0.0;
                this.field_70179_y = 0.0;
            }
            double d8 = d;
            double d9 = d2;
            double d10 = d3;
            if ((moverType == MoverType.SELF || moverType == MoverType.PLAYER) && (this.field_70122_E && this.func_70093_af() || moveEvent.isSafeWalk()) && this instanceof EntityPlayer) {
                double d11 = 0.05;
                while (d != 0.0 && this.field_70170_p.func_184144_a((Entity)((EntityPlayerSP)this), this.func_174813_aQ().func_72317_d(d, (double)(-this.field_70138_W), 0.0)).isEmpty()) {
                    d = d < 0.05 && d >= -0.05 ? 0.0 : (d > 0.0 ? (d -= 0.05) : (d += 0.05));
                    d8 = d;
                }
                while (d3 != 0.0 && this.field_70170_p.func_184144_a((Entity)((EntityPlayerSP)this), this.func_174813_aQ().func_72317_d(0.0, (double)(-this.field_70138_W), d3)).isEmpty()) {
                    d3 = d3 < 0.05 && d3 >= -0.05 ? 0.0 : (d3 > 0.0 ? (d3 -= 0.05) : (d3 += 0.05));
                    d10 = d3;
                }
                while (d != 0.0 && d3 != 0.0 && this.field_70170_p.func_184144_a((Entity)((EntityPlayerSP)this), this.func_174813_aQ().func_72317_d(d, (double)(-this.field_70138_W), d3)).isEmpty()) {
                    d = d < 0.05 && d >= -0.05 ? 0.0 : (d > 0.0 ? (d -= 0.05) : (d += 0.05));
                    d8 = d;
                    d3 = d3 < 0.05 && d3 >= -0.05 ? 0.0 : (d3 > 0.0 ? (d3 -= 0.05) : (d3 += 0.05));
                    d10 = d3;
                }
            }
            List list2 = this.field_70170_p.func_184144_a((Entity)((EntityPlayerSP)this), this.func_174813_aQ().func_72321_a(d, d2, d3));
            AxisAlignedBB axisAlignedBB2 = this.func_174813_aQ();
            if (d2 != 0.0) {
                n2 = list2.size();
                for (n = 0; n < n2; ++n) {
                    d2 = ((AxisAlignedBB)list2.get(n)).func_72323_b(this.func_174813_aQ(), d2);
                }
                this.func_174826_a(this.func_174813_aQ().func_72317_d(0.0, d2, 0.0));
            }
            if (d != 0.0) {
                n2 = list2.size();
                for (n = 0; n < n2; ++n) {
                    d = ((AxisAlignedBB)list2.get(n)).func_72316_a(this.func_174813_aQ(), d);
                }
                if (d != 0.0) {
                    this.func_174826_a(this.func_174813_aQ().func_72317_d(d, 0.0, 0.0));
                }
            }
            if (d3 != 0.0) {
                n2 = list2.size();
                for (n = 0; n < n2; ++n) {
                    d3 = ((AxisAlignedBB)list2.get(n)).func_72322_c(this.func_174813_aQ(), d3);
                }
                if (d3 != 0.0) {
                    this.func_174826_a(this.func_174813_aQ().func_72317_d(0.0, 0.0, d3));
                }
            }
            int n4 = n = this.field_70122_E || d9 != d2 && d9 < 0.0 ? 1 : 0;
            if (this.field_70138_W > 0.0f && n != 0 && (d8 != d || d10 != d3)) {
                StepEvent stepEvent = new StepEvent(this.field_70138_W);
                LiquidBounce.eventManager.callEvent(stepEvent);
                double d12 = d;
                double d13 = d2;
                double d14 = d3;
                axisAlignedBB = this.func_174813_aQ();
                this.func_174826_a(axisAlignedBB2);
                d2 = stepEvent.getStepHeight();
                list = this.field_70170_p.func_184144_a((Entity)((EntityPlayerSP)this), this.func_174813_aQ().func_72321_a(d8, d2, d10));
                AxisAlignedBB axisAlignedBB3 = this.func_174813_aQ();
                AxisAlignedBB axisAlignedBB4 = axisAlignedBB3.func_72321_a(d8, 0.0, d10);
                double d15 = d2;
                int n5 = list.size();
                for (int i = 0; i < n5; ++i) {
                    d15 = ((AxisAlignedBB)list.get(i)).func_72323_b(axisAlignedBB4, d15);
                }
                axisAlignedBB3 = axisAlignedBB3.func_72317_d(0.0, d15, 0.0);
                double d16 = d8;
                int n6 = list.size();
                for (int i = 0; i < n6; ++i) {
                    d16 = ((AxisAlignedBB)list.get(i)).func_72316_a(axisAlignedBB3, d16);
                }
                axisAlignedBB3 = axisAlignedBB3.func_72317_d(d16, 0.0, 0.0);
                double d17 = d10;
                int n7 = list.size();
                for (int i = 0; i < n7; ++i) {
                    d17 = ((AxisAlignedBB)list.get(i)).func_72322_c(axisAlignedBB3, d17);
                }
                axisAlignedBB3 = axisAlignedBB3.func_72317_d(0.0, 0.0, d17);
                AxisAlignedBB axisAlignedBB5 = this.func_174813_aQ();
                double d18 = d2;
                int n8 = list.size();
                for (int i = 0; i < n8; ++i) {
                    d18 = ((AxisAlignedBB)list.get(i)).func_72323_b(axisAlignedBB5, d18);
                }
                axisAlignedBB5 = axisAlignedBB5.func_72317_d(0.0, d18, 0.0);
                double d19 = d8;
                int n9 = list.size();
                for (int i = 0; i < n9; ++i) {
                    d19 = ((AxisAlignedBB)list.get(i)).func_72316_a(axisAlignedBB5, d19);
                }
                axisAlignedBB5 = axisAlignedBB5.func_72317_d(d19, 0.0, 0.0);
                double d20 = d10;
                int n10 = list.size();
                for (int i = 0; i < n10; ++i) {
                    d20 = ((AxisAlignedBB)list.get(i)).func_72322_c(axisAlignedBB5, d20);
                }
                axisAlignedBB5 = axisAlignedBB5.func_72317_d(0.0, 0.0, d20);
                double d21 = d16 * d16 + d17 * d17;
                double d22 = d19 * d19 + d20 * d20;
                if (d21 > d22) {
                    d = d16;
                    d3 = d17;
                    d2 = -d15;
                    this.func_174826_a(axisAlignedBB3);
                } else {
                    d = d19;
                    d3 = d20;
                    d2 = -d18;
                    this.func_174826_a(axisAlignedBB5);
                }
                int n11 = list.size();
                for (int i = 0; i < n11; ++i) {
                    d2 = ((AxisAlignedBB)list.get(i)).func_72323_b(this.func_174813_aQ(), d2);
                }
                this.func_174826_a(this.func_174813_aQ().func_72317_d(0.0, d2, 0.0));
                if (d12 * d12 + d14 * d14 >= d * d + d3 * d3) {
                    d = d12;
                    d2 = d13;
                    d3 = d14;
                    this.func_174826_a(axisAlignedBB);
                } else {
                    LiquidBounce.eventManager.callEvent(new StepConfirmEvent());
                }
            }
            this.field_70170_p.field_72984_F.func_76319_b();
            this.field_70170_p.field_72984_F.func_76320_a("rest");
            this.func_174829_m();
            this.field_70123_F = d8 != d || d10 != d3;
            this.field_70124_G = d9 != d2;
            this.field_70122_E = this.field_70124_G && d9 < 0.0;
            this.field_70132_H = this.field_70123_F || this.field_70124_G;
            int n12 = MathHelper.func_76128_c((double)this.field_70165_t);
            int n13 = MathHelper.func_76128_c((double)(this.field_70163_u - (double)0.2f));
            int n14 = MathHelper.func_76128_c((double)this.field_70161_v);
            BlockPos blockPos2 = new BlockPos(n12, n13, n14);
            IBlockState iBlockState2 = this.field_70170_p.func_180495_p(blockPos2);
            if (iBlockState2.func_185904_a() == Material.field_151579_a && ((axisAlignedBB = (iBlockState = this.field_70170_p.func_180495_p(blockPos = blockPos2.func_177977_b())).func_177230_c()) instanceof BlockFence || axisAlignedBB instanceof BlockWall || axisAlignedBB instanceof BlockFenceGate)) {
                iBlockState2 = iBlockState;
                blockPos2 = blockPos;
            }
            this.func_184231_a(d2, this.field_70122_E, iBlockState2, blockPos2);
            if (d8 != d) {
                this.field_70159_w = 0.0;
            }
            if (d10 != d3) {
                this.field_70179_y = 0.0;
            }
            Block block = iBlockState2.func_177230_c();
            if (d9 != d2) {
                block.func_176216_a(this.field_70170_p, (Entity)((EntityPlayerSP)this));
            }
            if (!(!this.func_70041_e_() || this.field_70122_E && this.func_70093_af() && this instanceof EntityPlayer || this.func_184218_aH())) {
                double d23 = this.field_70165_t - d5;
                double d24 = this.field_70163_u - d6;
                double d25 = this.field_70161_v - d7;
                if (block != Blocks.field_150468_ap) {
                    d24 = 0.0;
                }
                if (block != null && this.field_70122_E) {
                    block.func_176199_a(this.field_70170_p, blockPos2, (Entity)((EntityPlayerSP)this));
                }
                this.field_70140_Q = (float)((double)this.field_70140_Q + (double)MathHelper.func_76133_a((double)(d23 * d23 + d25 * d25)) * 0.6);
                this.field_82151_R = (float)((double)this.field_82151_R + (double)MathHelper.func_76133_a((double)(d23 * d23 + d24 * d24 + d25 * d25)) * 0.6);
                if (this.field_82151_R > (float)this.field_70150_b && iBlockState2.func_185904_a() != Material.field_151579_a) {
                    this.field_70150_b = (int)this.field_82151_R + 1;
                    if (this.func_70090_H()) {
                        EntityPlayerSP entityPlayerSP = this.func_184207_aI() && this.func_184179_bs() != null ? this.func_184179_bs() : (EntityPlayerSP)this;
                        float f = entityPlayerSP == this ? 0.35f : 0.4f;
                        float f2 = MathHelper.func_76133_a((double)(entityPlayerSP.field_70159_w * entityPlayerSP.field_70159_w * (double)0.2f + entityPlayerSP.field_70181_x * entityPlayerSP.field_70181_x + entityPlayerSP.field_70179_y * entityPlayerSP.field_70179_y * (double)0.2f)) * f;
                        if (f2 > 1.0f) {
                            f2 = 1.0f;
                        }
                        this.func_184185_a(this.func_184184_Z(), f2, 1.0f + (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.4f);
                    } else {
                        this.func_180429_a(blockPos2, block);
                    }
                } else if (this.field_82151_R > this.field_191959_ay && this.func_191957_ae() && iBlockState2.func_185904_a() == Material.field_151579_a) {
                    this.field_191959_ay = this.func_191954_d(this.field_82151_R);
                }
            }
            try {
                this.func_145775_I();
            }
            catch (Throwable throwable) {
                axisAlignedBB = CrashReport.func_85055_a((Throwable)throwable, (String)"Checking entity block collision");
                list = axisAlignedBB.func_85058_a("Entity being checked for collision");
                this.func_85029_a((CrashReportCategory)list);
                throw new ReportedException((CrashReport)axisAlignedBB);
            }
            boolean bl = this.func_70026_G();
            if (this.field_70170_p.func_147470_e(this.func_174813_aQ().func_186664_h(0.001))) {
                this.func_70081_e(1);
                if (!bl) {
                    ++this.field_190534_ay;
                    if (this.field_190534_ay == 0) {
                        this.func_70015_d(8);
                    }
                }
            } else if (this.field_190534_ay <= 0) {
                this.field_190534_ay = -this.func_190531_bD();
            }
            if (bl && this.func_70027_ad()) {
                this.func_184185_a(SoundEvents.field_187541_bC, 0.7f, 1.6f + (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.4f);
                this.field_190534_ay = -this.func_190531_bD();
            }
            this.field_70170_p.field_72984_F.func_76319_b();
        }
        this.func_189810_i((float)(this.field_70165_t - this.field_70165_t), (float)(this.field_70161_v - this.field_70161_v));
    }

    @Shadow
    public abstract boolean func_110317_t();

    @Shadow
    public abstract void func_70031_b(boolean var1);

    @Override
    @Shadow
    public abstract boolean func_184587_cr();

    @Inject(method={"swingArm"}, at={@At(value="HEAD")}, cancellable=true)
    private void swingItem(EnumHand enumHand, CallbackInfo callbackInfo) {
        NoSwing noSwing = (NoSwing)LiquidBounce.moduleManager.getModule(NoSwing.class);
        if (noSwing.getState()) {
            callbackInfo.cancel();
            if (!((Boolean)noSwing.getServerSideValue().get()).booleanValue()) {
                this.field_71174_a.func_147297_a((Packet)new CPacketAnimation(enumHand));
            }
        }
    }

    @Override
    @Overwrite
    public void func_70636_d() {
        IJumpingMount iJumpingMount;
        boolean bl;
        boolean bl2;
        boolean bl3;
        block49: {
            block48: {
                Scaffold scaffold;
                AxisAlignedBB axisAlignedBB;
                PlayerSPPushOutOfBlocksEvent playerSPPushOutOfBlocksEvent;
                try {
                    LiquidBounce.eventManager.callEvent(new UpdateEvent());
                }
                catch (Throwable throwable) {
                    throwable.printStackTrace();
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
                bl3 = this.field_71158_b.field_78901_c;
                boolean bl4 = this.field_71158_b.field_78899_d;
                float f = 0.8f;
                boolean bl5 = this.field_71158_b.field_192832_b >= 0.8f;
                this.field_71158_b.func_78898_a();
                NoSlow noSlow = (NoSlow)LiquidBounce.moduleManager.getModule(NoSlow.class);
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
                bl2 = false;
                if (this.field_189812_cs > 0) {
                    --this.field_189812_cs;
                    bl2 = true;
                    this.field_71158_b.field_78901_c = true;
                }
                if (!MinecraftForge.EVENT_BUS.post((Event)(playerSPPushOutOfBlocksEvent = new PlayerSPPushOutOfBlocksEvent((EntityPlayer)((EntityPlayerSP)this), axisAlignedBB = this.func_174813_aQ())))) {
                    axisAlignedBB = playerSPPushOutOfBlocksEvent.getEntityBoundingBox();
                    this.func_145771_j(this.field_70165_t - (double)this.field_70130_N * 0.35, axisAlignedBB.field_72338_b + 0.5, this.field_70161_v + (double)this.field_70130_N * 0.35);
                    this.func_145771_j(this.field_70165_t - (double)this.field_70130_N * 0.35, axisAlignedBB.field_72338_b + 0.5, this.field_70161_v - (double)this.field_70130_N * 0.35);
                    this.func_145771_j(this.field_70165_t + (double)this.field_70130_N * 0.35, axisAlignedBB.field_72338_b + 0.5, this.field_70161_v - (double)this.field_70130_N * 0.35);
                    this.func_145771_j(this.field_70165_t + (double)this.field_70130_N * 0.35, axisAlignedBB.field_72338_b + 0.5, this.field_70161_v + (double)this.field_70130_N * 0.35);
                }
                Sprint sprint = (Sprint)LiquidBounce.moduleManager.getModule(Sprint.class);
                boolean bl6 = bl = (Boolean)sprint.foodValue.get() == false || (float)this.func_71024_bL().func_75116_a() > 6.0f || this.field_71075_bZ.field_75101_c;
                if (this.field_70122_E && !bl4 && !bl5 && this.field_71158_b.field_192832_b >= 0.8f && !this.func_70051_ag() && bl && !this.func_184587_cr() && !this.func_70644_a(MobEffects.field_76440_q)) {
                    if (this.field_71156_d <= 0 && !this.field_71159_c.field_71474_y.field_151444_V.func_151470_d()) {
                        this.field_71156_d = 7;
                    } else {
                        this.func_70031_b(true);
                    }
                }
                if (!this.func_70051_ag() && this.field_71158_b.field_192832_b >= 0.8f && bl && (noSlow.getState() || !this.func_184587_cr()) && !this.func_70644_a(MobEffects.field_76440_q) && this.field_71159_c.field_71474_y.field_151444_V.func_151470_d()) {
                    this.func_70031_b(true);
                }
                if ((scaffold = (Scaffold)LiquidBounce.moduleManager.getModule(Scaffold.class)).getState() && !((Boolean)scaffold.sprintValue.get()).booleanValue()) break block48;
                if (!sprint.getState() || !((Boolean)sprint.checkServerSide.get()).booleanValue() || !this.field_70122_E && ((Boolean)sprint.checkServerSideGround.get()).booleanValue() || ((Boolean)sprint.allDirectionsValue.get()).booleanValue() || RotationUtils.targetRotation == null) break block49;
                Rotation rotation = new Rotation(this.field_71159_c.field_71439_g.field_70177_z, this.field_71159_c.field_71439_g.field_70125_A);
                if (!(RotationUtils.getRotationDifference(rotation) > 30.0)) break block49;
            }
            this.func_70031_b(false);
        }
        if (this.func_70051_ag() && (this.field_71158_b.field_192832_b < 0.8f || this.field_70123_F || !bl)) {
            this.func_70031_b(false);
        }
        if (this.field_71075_bZ.field_75101_c) {
            if (this.field_71159_c.field_71442_b.func_178887_k()) {
                if (!this.field_71075_bZ.field_75100_b) {
                    this.field_71075_bZ.field_75100_b = true;
                    this.func_71016_p();
                }
            } else if (!bl3 && this.field_71158_b.field_78901_c && !bl2) {
                if (this.field_71101_bC == 0) {
                    this.field_71101_bC = 7;
                } else {
                    this.field_71075_bZ.field_75100_b = !this.field_71075_bZ.field_75100_b;
                    this.func_71016_p();
                    this.field_71101_bC = 0;
                }
            }
        }
        if (this.field_71158_b.field_78901_c && !bl3 && !this.field_70122_E && this.field_70181_x < 0.0 && !this.func_184613_cA() && !this.field_71075_bZ.field_75100_b && (iJumpingMount = this.func_184582_a(EntityEquipmentSlot.CHEST)).func_77973_b() == Items.field_185160_cR && ItemElytra.func_185069_d((ItemStack)iJumpingMount)) {
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
            iJumpingMount = (IJumpingMount)this.func_184187_bx();
            if (this.field_110320_a < 0) {
                ++this.field_110320_a;
                if (this.field_110320_a == 0) {
                    this.field_110321_bQ = 0.0f;
                }
            }
            if (bl3 && !this.field_71158_b.field_78901_c) {
                this.field_110320_a = -10;
                iJumpingMount.func_110206_u(MathHelper.func_76141_d((float)(this.func_110319_bJ() * 100.0f)));
                this.func_110318_g();
            } else if (!bl3 && this.field_71158_b.field_78901_c) {
                this.field_110320_a = 0;
                this.field_110321_bQ = 0.0f;
            } else if (bl3) {
                ++this.field_110320_a;
                this.field_110321_bQ = this.field_110320_a < 10 ? (float)this.field_110320_a * 0.1f : 0.8f + 2.0f / (float)(this.field_110320_a - 9) * 0.1f;
            }
        } else {
            this.field_110321_bQ = 0.0f;
        }
        super.func_70636_d();
        if (this.field_70122_E && this.field_71075_bZ.field_75100_b && !this.field_71159_c.field_71442_b.func_178887_k()) {
            this.field_71075_bZ.field_75100_b = false;
            this.func_71016_p();
        }
    }

    @Shadow
    protected abstract void func_110318_g();

    @Shadow
    public abstract void func_71053_j();

    @Shadow
    protected abstract void func_189810_i(float var1, float var2);

    @Override
    @Shadow
    public abstract void func_184185_a(SoundEvent var1, float var2, float var3);

    @Inject(method={"pushOutOfBlocks"}, at={@At(value="HEAD")}, cancellable=true)
    private void onPushOutOfBlocks(CallbackInfoReturnable callbackInfoReturnable) {
        PushOutEvent pushOutEvent = new PushOutEvent();
        if (this.field_70145_X) {
            pushOutEvent.cancelEvent();
        }
        LiquidBounce.eventManager.callEvent(pushOutEvent);
        if (pushOutEvent.isCancelled()) {
            callbackInfoReturnable.setReturnValue((Object)false);
        }
    }

    @Shadow
    protected abstract boolean func_145771_j(double var1, double var3, double var5);

    @Overwrite
    public void func_175161_p() {
        boolean bl;
        boolean bl2;
        LiquidBounce.eventManager.callEvent(new MotionEvent(EventState.PRE, true));
        InventoryMove inventoryMove = (InventoryMove)LiquidBounce.moduleManager.getModule(InventoryMove.class);
        Sneak sneak = (Sneak)LiquidBounce.moduleManager.getModule(Sneak.class);
        boolean bl3 = inventoryMove.getState() && (Boolean)inventoryMove.getAacAdditionProValue().get() != false || LiquidBounce.moduleManager.getModule(AntiHunger.class).getState() || sneak.getState() && (!MovementUtils.isMoving() || (Boolean)sneak.stopMoveValue.get() == false) && ((String)sneak.modeValue.get()).equalsIgnoreCase("MineSecure");
        boolean bl4 = bl2 = this.func_70051_ag() && !bl3;
        if (bl2 != this.field_175171_bO) {
            if (bl2) {
                this.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)((EntityPlayerSP)this), CPacketEntityAction.Action.START_SPRINTING));
            } else {
                this.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)((EntityPlayerSP)this), CPacketEntityAction.Action.STOP_SPRINTING));
            }
            this.field_175171_bO = bl2;
        }
        if ((bl = this.func_70093_af()) != this.field_175170_bN && (!sneak.getState() || ((String)sneak.modeValue.get()).equalsIgnoreCase("Legit"))) {
            if (bl) {
                this.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)((EntityPlayerSP)this), CPacketEntityAction.Action.START_SNEAKING));
            } else {
                this.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)((EntityPlayerSP)this), CPacketEntityAction.Action.STOP_SNEAKING));
            }
            this.field_175170_bN = bl;
        }
        if (this.func_175160_A()) {
            boolean bl5;
            float f = this.field_70177_z;
            float f2 = this.field_70125_A;
            float f3 = RotationUtils.serverRotation.getYaw();
            float f4 = RotationUtils.serverRotation.getPitch();
            if (RotationUtils.targetRotation != null) {
                f = RotationUtils.targetRotation.getYaw();
                f2 = RotationUtils.targetRotation.getPitch();
            }
            AxisAlignedBB axisAlignedBB = this.func_174813_aQ();
            double d = this.field_70165_t - this.field_175172_bI;
            double d2 = axisAlignedBB.field_72338_b - this.field_175166_bJ;
            double d3 = this.field_70161_v - this.field_175167_bK;
            double d4 = f - f3;
            double d5 = f2 - f4;
            ++this.field_175168_bP;
            boolean bl6 = d * d + d2 * d2 + d3 * d3 > 9.0E-4 || this.field_175168_bP >= 20;
            boolean bl7 = bl5 = d4 != 0.0 || d5 != 0.0;
            if (this.func_184218_aH()) {
                this.field_71174_a.func_147297_a((Packet)new CPacketPlayer.PositionRotation(this.field_70159_w, -999.0, this.field_70179_y, this.field_70177_z, this.field_70125_A, this.field_70122_E));
                bl6 = false;
            } else if (bl6 && bl5) {
                this.field_71174_a.func_147297_a((Packet)new CPacketPlayer.PositionRotation(this.field_70165_t, axisAlignedBB.field_72338_b, this.field_70161_v, this.field_70177_z, this.field_70125_A, this.field_70122_E));
            } else if (bl6) {
                this.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(this.field_70165_t, axisAlignedBB.field_72338_b, this.field_70161_v, this.field_70122_E));
            } else if (bl5) {
                this.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(this.field_70177_z, this.field_70125_A, this.field_70122_E));
            } else if (this.field_184841_cd != this.field_70122_E) {
                this.field_71174_a.func_147297_a((Packet)new CPacketPlayer(this.field_70122_E));
            }
            if (bl6) {
                this.field_175172_bI = this.field_70165_t;
                this.field_175166_bJ = axisAlignedBB.field_72338_b;
                this.field_175167_bK = this.field_70161_v;
                this.field_175168_bP = 0;
            }
            if (bl5) {
                this.field_175164_bL = this.field_70177_z;
                this.field_175165_bM = this.field_70125_A;
            }
            this.field_184841_cd = this.field_70122_E;
            this.field_189811_cr = this.field_71159_c.field_71474_y.field_189989_R;
        }
        LiquidBounce.eventManager.callEvent(new MotionEvent(EventState.POST, true));
    }

    @Override
    @Shadow
    public abstract boolean func_70093_af();

    @Shadow
    protected abstract boolean func_175160_A();
}

