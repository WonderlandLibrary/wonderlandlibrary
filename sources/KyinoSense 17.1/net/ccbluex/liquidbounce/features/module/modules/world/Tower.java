/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockAir
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 *  net.minecraft.network.play.client.C09PacketHeldItemChange
 *  net.minecraft.network.play.client.C0APacketAnimation
 *  net.minecraft.stats.StatList
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.MovingObjectPosition
 *  net.minecraft.util.MovingObjectPosition$MovingObjectType
 *  net.minecraft.util.Vec3
 *  net.minecraft.util.Vec3i
 */
package net.ccbluex.liquidbounce.features.module.modules.world;

import java.awt.Color;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.JumpEvent;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.render.BlockOverlay;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.InventoryUtils;
import net.ccbluex.liquidbounce.utils.PlaceRotation;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.VecRotation;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.block.PlaceInfo;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.TickTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.block.BlockAir;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

@ModuleInfo(name="Tower", description="Automatically builds a tower beneath you.", category=ModuleCategory.WORLD)
public class Tower
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Jump", "Motion", "ConstantMotion", "MotionTP", "Packet", "Teleport", "AAC3.3.9", "AAC3.6.4"}, "Motion");
    private final BoolValue autoBlockValue = new BoolValue("Auto Block", true);
    private final BoolValue stayAutoBlock = new BoolValue("Stay Auto Block", false);
    private final BoolValue swingValue = new BoolValue("Swing", true);
    private final BoolValue stopWhenBlockAbove = new BoolValue("Stop When Block Above", false);
    private final BoolValue rotationsValue = new BoolValue("Rotations", true);
    private final BoolValue keepRotationValue = new BoolValue("Keep Rotation", false);
    private final BoolValue onJumpValue = new BoolValue("On Jump", false);
    private final ListValue placeModeValue = new ListValue("Place Timing", new String[]{"Pre", "Post"}, "Post");
    private final FloatValue timerValue = new FloatValue("Timer", 1.0f, 0.0f, 10.0f);
    private final FloatValue jumpMotionValue = new FloatValue("Jump Motion", 0.42f, 0.3681289f, 0.79f);
    private final IntegerValue jumpDelayValue = new IntegerValue("Jump Delay", 0, 0, 20);
    private final FloatValue constantMotionValue = new FloatValue("Constant Motion", 0.42f, 0.1f, 1.0f);
    private final FloatValue constantMotionJumpGroundValue = new FloatValue("Constant Motion Jump Ground", 0.79f, 0.76f, 1.0f);
    private final FloatValue teleportHeightValue = new FloatValue("Teleport Height", 1.15f, 0.1f, 5.0f);
    private final IntegerValue teleportDelayValue = new IntegerValue("Teleport Delay", 0, 0, 20);
    private final BoolValue teleportGroundValue = new BoolValue("Teleport Ground", true);
    private final BoolValue teleportNoMotionValue = new BoolValue("Teleport NoMotion", false);
    private final BoolValue counterDisplayValue = new BoolValue("Counter", true);
    private PlaceInfo placeInfo;
    private Rotation lockRotation;
    private final TickTimer timer = new TickTimer();
    private double jumpGround = 0.0;
    private int slot;

    @Override
    public void onDisable() {
        if (Tower.mc.field_71439_g == null) {
            return;
        }
        Tower.mc.field_71428_T.field_74278_d = 1.0f;
        this.lockRotation = null;
        if (this.slot != Tower.mc.field_71439_g.field_71071_by.field_70461_c) {
            mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(Tower.mc.field_71439_g.field_71071_by.field_70461_c));
        }
    }

    @EventTarget
    public void onMotion(MotionEvent event) {
        if (((Boolean)this.onJumpValue.get()).booleanValue() && !Tower.mc.field_71474_y.field_74314_A.func_151470_d()) {
            return;
        }
        if (((Boolean)this.rotationsValue.get()).booleanValue() && ((Boolean)this.keepRotationValue.get()).booleanValue() && this.lockRotation != null) {
            RotationUtils.setTargetRotation(this.lockRotation);
        }
        Tower.mc.field_71428_T.field_74278_d = ((Float)this.timerValue.get()).floatValue();
        EventState eventState = event.getEventState();
        if (((String)this.placeModeValue.get()).equalsIgnoreCase(eventState.getStateName())) {
            this.place();
        }
        if (eventState == EventState.PRE) {
            this.placeInfo = null;
            this.timer.update();
            if ((Boolean)this.autoBlockValue.get() != false ? InventoryUtils.findAutoBlockBlock() != -1 : Tower.mc.field_71439_g.func_70694_bm() != null && Tower.mc.field_71439_g.func_70694_bm().func_77973_b() instanceof ItemBlock) {
                VecRotation vecRotation;
                BlockPos blockPos;
                if (!((Boolean)this.stopWhenBlockAbove.get()).booleanValue() || BlockUtils.getBlock(new BlockPos(Tower.mc.field_71439_g.field_70165_t, Tower.mc.field_71439_g.field_70163_u + 2.0, Tower.mc.field_71439_g.field_70161_v)) instanceof BlockAir) {
                    this.move();
                }
                if (Tower.mc.field_71441_e.func_180495_p(blockPos = new BlockPos(Tower.mc.field_71439_g.field_70165_t, Tower.mc.field_71439_g.field_70163_u - 1.0, Tower.mc.field_71439_g.field_70161_v)).func_177230_c() instanceof BlockAir && this.search(blockPos) && ((Boolean)this.rotationsValue.get()).booleanValue() && (vecRotation = RotationUtils.faceBlock(blockPos)) != null) {
                    RotationUtils.setTargetRotation(vecRotation.getRotation());
                    this.placeInfo.setVec3(vecRotation.getVec());
                }
            }
        }
    }

    private void fakeJump() {
        Tower.mc.field_71439_g.field_70160_al = true;
        Tower.mc.field_71439_g.func_71029_a(StatList.field_75953_u);
    }

    private void move() {
        switch (((String)this.modeValue.get()).toLowerCase()) {
            case "jump": {
                if (!Tower.mc.field_71439_g.field_70122_E || !this.timer.hasTimePassed((Integer)this.jumpDelayValue.get())) break;
                this.fakeJump();
                Tower.mc.field_71439_g.field_70181_x = ((Float)this.jumpMotionValue.get()).floatValue();
                this.timer.reset();
                break;
            }
            case "motion": {
                if (Tower.mc.field_71439_g.field_70122_E) {
                    this.fakeJump();
                    Tower.mc.field_71439_g.field_70181_x = 0.42;
                    break;
                }
                if (!(Tower.mc.field_71439_g.field_70181_x < 0.1)) break;
                Tower.mc.field_71439_g.field_70181_x = -0.3;
                break;
            }
            case "motiontp": {
                if (Tower.mc.field_71439_g.field_70122_E) {
                    this.fakeJump();
                    Tower.mc.field_71439_g.field_70181_x = 0.42;
                    break;
                }
                if (!(Tower.mc.field_71439_g.field_70181_x < 0.23)) break;
                Tower.mc.field_71439_g.func_70107_b(Tower.mc.field_71439_g.field_70165_t, (double)((int)Tower.mc.field_71439_g.field_70163_u), Tower.mc.field_71439_g.field_70161_v);
                break;
            }
            case "packet": {
                if (!Tower.mc.field_71439_g.field_70122_E || !this.timer.hasTimePassed(2)) break;
                this.fakeJump();
                mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Tower.mc.field_71439_g.field_70165_t, Tower.mc.field_71439_g.field_70163_u + 0.42, Tower.mc.field_71439_g.field_70161_v, false));
                mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Tower.mc.field_71439_g.field_70165_t, Tower.mc.field_71439_g.field_70163_u + 0.753, Tower.mc.field_71439_g.field_70161_v, false));
                Tower.mc.field_71439_g.func_70107_b(Tower.mc.field_71439_g.field_70165_t, Tower.mc.field_71439_g.field_70163_u + 1.0, Tower.mc.field_71439_g.field_70161_v);
                this.timer.reset();
                break;
            }
            case "teleport": {
                if (((Boolean)this.teleportNoMotionValue.get()).booleanValue()) {
                    Tower.mc.field_71439_g.field_70181_x = 0.0;
                }
                if (!Tower.mc.field_71439_g.field_70122_E && ((Boolean)this.teleportGroundValue.get()).booleanValue() || !this.timer.hasTimePassed((Integer)this.teleportDelayValue.get())) break;
                this.fakeJump();
                Tower.mc.field_71439_g.func_70634_a(Tower.mc.field_71439_g.field_70165_t, Tower.mc.field_71439_g.field_70163_u + (double)((Float)this.teleportHeightValue.get()).floatValue(), Tower.mc.field_71439_g.field_70161_v);
                this.timer.reset();
                break;
            }
            case "constantmotion": {
                if (Tower.mc.field_71439_g.field_70122_E) {
                    this.fakeJump();
                    this.jumpGround = Tower.mc.field_71439_g.field_70163_u;
                    Tower.mc.field_71439_g.field_70181_x = ((Float)this.constantMotionValue.get()).floatValue();
                }
                if (!(Tower.mc.field_71439_g.field_70163_u > this.jumpGround + (double)((Float)this.constantMotionJumpGroundValue.get()).floatValue())) break;
                this.fakeJump();
                Tower.mc.field_71439_g.func_70107_b(Tower.mc.field_71439_g.field_70165_t, (double)((int)Tower.mc.field_71439_g.field_70163_u), Tower.mc.field_71439_g.field_70161_v);
                Tower.mc.field_71439_g.field_70181_x = ((Float)this.constantMotionValue.get()).floatValue();
                this.jumpGround = Tower.mc.field_71439_g.field_70163_u;
                break;
            }
            case "aac3.3.9": {
                if (Tower.mc.field_71439_g.field_70122_E) {
                    this.fakeJump();
                    Tower.mc.field_71439_g.field_70181_x = 0.4001;
                }
                Tower.mc.field_71428_T.field_74278_d = 1.0f;
                if (!(Tower.mc.field_71439_g.field_70181_x < 0.0)) break;
                Tower.mc.field_71439_g.field_70181_x -= 9.45E-6;
                Tower.mc.field_71428_T.field_74278_d = 1.6f;
                break;
            }
            case "aac3.6.4": {
                if (Tower.mc.field_71439_g.field_70173_aa % 4 == 1) {
                    Tower.mc.field_71439_g.field_70181_x = 0.4195464;
                    Tower.mc.field_71439_g.func_70107_b(Tower.mc.field_71439_g.field_70165_t - 0.035, Tower.mc.field_71439_g.field_70163_u, Tower.mc.field_71439_g.field_70161_v);
                    break;
                }
                if (Tower.mc.field_71439_g.field_70173_aa % 4 != 0) break;
                Tower.mc.field_71439_g.field_70181_x = -0.5;
                Tower.mc.field_71439_g.func_70107_b(Tower.mc.field_71439_g.field_70165_t + 0.035, Tower.mc.field_71439_g.field_70163_u, Tower.mc.field_71439_g.field_70161_v);
            }
        }
    }

    private void place() {
        if (this.placeInfo == null) {
            return;
        }
        int blockSlot = -1;
        ItemStack itemStack = Tower.mc.field_71439_g.func_70694_bm();
        if (Tower.mc.field_71439_g.func_70694_bm() == null || !(Tower.mc.field_71439_g.func_70694_bm().func_77973_b() instanceof ItemBlock)) {
            if (!((Boolean)this.autoBlockValue.get()).booleanValue()) {
                return;
            }
            blockSlot = InventoryUtils.findAutoBlockBlock();
            if (blockSlot == -1) {
                return;
            }
            mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(blockSlot - 36));
            itemStack = Tower.mc.field_71439_g.field_71069_bz.func_75139_a(blockSlot).func_75211_c();
        }
        if (Tower.mc.field_71442_b.func_178890_a(Tower.mc.field_71439_g, Tower.mc.field_71441_e, itemStack, this.placeInfo.getBlockPos(), this.placeInfo.getEnumFacing(), this.placeInfo.getVec3())) {
            if (((Boolean)this.swingValue.get()).booleanValue()) {
                Tower.mc.field_71439_g.func_71038_i();
            } else {
                mc.func_147114_u().func_147297_a((Packet)new C0APacketAnimation());
            }
        }
        this.placeInfo = null;
        if (!((Boolean)this.stayAutoBlock.get()).booleanValue() && blockSlot >= 0) {
            mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(Tower.mc.field_71439_g.field_71071_by.field_70461_c));
        }
    }

    private boolean search(BlockPos blockPosition) {
        if (!BlockUtils.isReplaceable(blockPosition)) {
            return false;
        }
        Vec3 eyesPos = new Vec3(Tower.mc.field_71439_g.field_70165_t, Tower.mc.field_71439_g.func_174813_aQ().field_72338_b + (double)Tower.mc.field_71439_g.func_70047_e(), Tower.mc.field_71439_g.field_70161_v);
        PlaceRotation placeRotation = null;
        for (EnumFacing side : EnumFacing.values()) {
            BlockPos neighbor = blockPosition.func_177972_a(side);
            if (!BlockUtils.canBeClicked(neighbor)) continue;
            Vec3 dirVec = new Vec3(side.func_176730_m());
            for (double xSearch = 0.1; xSearch < 0.9; xSearch += 0.1) {
                for (double ySearch = 0.1; ySearch < 0.9; ySearch += 0.1) {
                    for (double zSearch = 0.1; zSearch < 0.9; zSearch += 0.1) {
                        Vec3 posVec = new Vec3((Vec3i)blockPosition).func_72441_c(xSearch, ySearch, zSearch);
                        double distanceSqPosVec = eyesPos.func_72436_e(posVec);
                        Vec3 hitVec = posVec.func_178787_e(new Vec3(dirVec.field_72450_a * 0.5, dirVec.field_72448_b * 0.5, dirVec.field_72449_c * 0.5));
                        if (eyesPos.func_72436_e(hitVec) > 18.0 || distanceSqPosVec > eyesPos.func_72436_e(posVec.func_178787_e(dirVec)) || Tower.mc.field_71441_e.func_147447_a(eyesPos, hitVec, false, true, false) != null) continue;
                        double diffX = hitVec.field_72450_a - eyesPos.field_72450_a;
                        double diffY = hitVec.field_72448_b - eyesPos.field_72448_b;
                        double diffZ = hitVec.field_72449_c - eyesPos.field_72449_c;
                        double diffXZ = MathHelper.func_76133_a((double)(diffX * diffX + diffZ * diffZ));
                        Rotation rotation = new Rotation(MathHelper.func_76142_g((float)((float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f)), MathHelper.func_76142_g((float)((float)(-Math.toDegrees(Math.atan2(diffY, diffXZ))))));
                        Vec3 rotationVector = RotationUtils.getVectorForRotation(rotation);
                        Vec3 vector = eyesPos.func_72441_c(rotationVector.field_72450_a * 4.0, rotationVector.field_72448_b * 4.0, rotationVector.field_72449_c * 4.0);
                        MovingObjectPosition obj = Tower.mc.field_71441_e.func_147447_a(eyesPos, vector, false, false, true);
                        if (obj.field_72313_a != MovingObjectPosition.MovingObjectType.BLOCK || !obj.func_178782_a().equals((Object)neighbor) || placeRotation != null && !(RotationUtils.getRotationDifference(rotation) < RotationUtils.getRotationDifference(placeRotation.getRotation()))) continue;
                        placeRotation = new PlaceRotation(new PlaceInfo(neighbor, side.func_176734_d(), hitVec), rotation);
                    }
                }
            }
        }
        if (placeRotation == null) {
            return false;
        }
        if (((Boolean)this.rotationsValue.get()).booleanValue()) {
            RotationUtils.setTargetRotation(placeRotation.getRotation(), 0);
            this.lockRotation = placeRotation.getRotation();
        }
        this.placeInfo = placeRotation.getPlaceInfo();
        return true;
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (Tower.mc.field_71439_g == null) {
            return;
        }
        Packet<?> packet = event.getPacket();
        if (packet instanceof C09PacketHeldItemChange) {
            C09PacketHeldItemChange packetHeldItemChange = (C09PacketHeldItemChange)packet;
            this.slot = packetHeldItemChange.func_149614_c();
        }
    }

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        if (((Boolean)this.counterDisplayValue.get()).booleanValue()) {
            GlStateManager.func_179094_E();
            BlockOverlay blockOverlay = (BlockOverlay)LiquidBounce.moduleManager.getModule(BlockOverlay.class);
            if (blockOverlay.getState() && ((Boolean)blockOverlay.getInfoValue().get()).booleanValue() && blockOverlay.getCurrentBlock() != null) {
                GlStateManager.func_179109_b((float)0.0f, (float)15.0f, (float)0.0f);
            }
            String info = "Blocks: \u00a77" + this.getBlocksAmount();
            ScaledResolution scaledResolution = new ScaledResolution(mc);
            RenderUtils.drawBorderedRect(scaledResolution.func_78326_a() / 2 - 2, scaledResolution.func_78328_b() / 2 + 5, scaledResolution.func_78326_a() / 2 + Fonts.font40.func_78256_a(info) + 2, scaledResolution.func_78328_b() / 2 + 16, 3.0f, Color.BLACK.getRGB(), Color.BLACK.getRGB());
            GlStateManager.func_179117_G();
            Fonts.font40.func_78276_b(info, scaledResolution.func_78326_a() / 2, scaledResolution.func_78328_b() / 2 + 7, Color.WHITE.getRGB());
            GlStateManager.func_179121_F();
        }
    }

    @EventTarget
    public void onJump(JumpEvent event) {
        if (((Boolean)this.onJumpValue.get()).booleanValue()) {
            event.cancelEvent();
        }
    }

    private int getBlocksAmount() {
        int amount = 0;
        for (int i = 36; i < 45; ++i) {
            ItemStack itemStack = Tower.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
            if (itemStack == null || !(itemStack.func_77973_b() instanceof ItemBlock)) continue;
            amount += itemStack.field_77994_a;
        }
        return amount;
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }
}

