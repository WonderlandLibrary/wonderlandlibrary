/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.math.MathKt
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.features.module.modules.world;

import java.awt.Color;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.enums.StatType;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IPlayerControllerMP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.api.minecraft.util.IScaledResolution;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
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
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.block.PlaceInfo;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.TickTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="Tower", description="Automatically builds a tower beneath you.", category=ModuleCategory.WORLD, keyBind=24)
public final class Tower
extends Module {
    private final BoolValue rotationsValue;
    private final BoolValue stayAutoBlock;
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Jump", "Motion", "ConstantMotion", "MotionTP", "Packet", "Teleport", "AAC3.3.9", "AAC3.6.4"}, "Motion");
    private final FloatValue constantMotionValue;
    private final TickTimer timer;
    private final BoolValue counterDisplayValue;
    private final BoolValue autoBlockValue = new BoolValue("AutoBlock", true);
    private final BoolValue stopWhenBlockAbove;
    private double jumpGround;
    private PlaceInfo placeInfo;
    private final FloatValue jumpMotionValue;
    private final IntegerValue jumpDelayValue;
    private final FloatValue teleportHeightValue;
    private final BoolValue onJumpValue;
    private final FloatValue timerValue;
    private final BoolValue swingValue;
    private int slot;
    private final BoolValue keepRotationValue;
    private final ListValue placeModeValue;
    private final BoolValue teleportGroundValue;
    private final IntegerValue teleportDelayValue;
    private final BoolValue teleportNoMotionValue;
    private final FloatValue constantMotionJumpGroundValue;
    private Rotation lockRotation;

    @Override
    public void onDisable() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
        this.lockRotation = null;
        if (this.slot != iEntityPlayerSP2.getInventory().getCurrentItem()) {
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(iEntityPlayerSP2.getInventory().getCurrentItem()));
        }
    }

    @EventTarget
    public final void onJump(JumpEvent jumpEvent) {
        if (((Boolean)this.onJumpValue.get()).booleanValue()) {
            jumpEvent.cancelEvent();
        }
    }

    private final void place() {
        if (this.placeInfo == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        int n = -1;
        IItemStack iItemStack = iEntityPlayerSP2.getHeldItem();
        if (iItemStack == null || !MinecraftInstance.classProvider.isItemBlock(iItemStack.getItem())) {
            if (!((Boolean)this.autoBlockValue.get()).booleanValue()) {
                return;
            }
            n = InventoryUtils.findAutoBlockBlock();
            if (n == -1) {
                return;
            }
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(n - 36));
            iItemStack = iEntityPlayerSP2.getInventoryContainer().getSlot(n).getStack();
        }
        IPlayerControllerMP iPlayerControllerMP = MinecraftInstance.mc.getPlayerController();
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        IItemStack iItemStack2 = iItemStack;
        if (iItemStack2 == null) {
            Intrinsics.throwNpe();
        }
        PlaceInfo placeInfo = this.placeInfo;
        if (placeInfo == null) {
            Intrinsics.throwNpe();
        }
        WBlockPos wBlockPos = placeInfo.getBlockPos();
        PlaceInfo placeInfo2 = this.placeInfo;
        if (placeInfo2 == null) {
            Intrinsics.throwNpe();
        }
        IEnumFacing iEnumFacing = placeInfo2.getEnumFacing();
        PlaceInfo placeInfo3 = this.placeInfo;
        if (placeInfo3 == null) {
            Intrinsics.throwNpe();
        }
        if (iPlayerControllerMP.onPlayerRightClick(iEntityPlayerSP2, iWorldClient, iItemStack2, wBlockPos, iEnumFacing, placeInfo3.getVec3())) {
            if (((Boolean)this.swingValue.get()).booleanValue()) {
                iEntityPlayerSP2.swingItem();
            } else {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketAnimation());
            }
        }
        this.placeInfo = null;
        if (!((Boolean)this.stayAutoBlock.get()).booleanValue() && n >= 0) {
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(iEntityPlayerSP2.getInventory().getCurrentItem()));
        }
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }

    private final void fakeJump() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        iEntityPlayerSP.setAirBorne(true);
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        iEntityPlayerSP2.triggerAchievement(MinecraftInstance.classProvider.getStatEnum(StatType.JUMP_STAT));
    }

    private final int getBlocksAmount() {
        int n = 0;
        int n2 = 44;
        for (int i = 36; i <= n2; ++i) {
            IItemStack iItemStack;
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if ((iItemStack = iEntityPlayerSP.getInventoryContainer().getSlot(i).getStack()) == null || !MinecraftInstance.classProvider.isItemBlock(iItemStack.getItem())) continue;
            IItem iItem = iItemStack.getItem();
            if (iItem == null) {
                Intrinsics.throwNpe();
            }
            IBlock iBlock = iItem.asItemBlock().getBlock();
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            if (!iEntityPlayerSP2.getHeldItem().equals(iItemStack) && InventoryUtils.BLOCK_BLACKLIST.contains(iBlock)) continue;
            n += iItemStack.getStackSize();
        }
        return n;
    }

    @EventTarget
    public final void onRender2D(Render2DEvent render2DEvent) {
        if (((Boolean)this.counterDisplayValue.get()).booleanValue()) {
            GL11.glPushMatrix();
            Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(BlockOverlay.class);
            if (module == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.render.BlockOverlay");
            }
            BlockOverlay blockOverlay = (BlockOverlay)module;
            if (blockOverlay.getState() && ((Boolean)blockOverlay.getInfoValue().get()).booleanValue() && blockOverlay.getCurrentBlock() != null) {
                GL11.glTranslatef((float)0.0f, (float)15.0f, (float)0.0f);
            }
            String string = "Blocks: \u00a77" + this.getBlocksAmount();
            IScaledResolution iScaledResolution = MinecraftInstance.classProvider.createScaledResolution(MinecraftInstance.mc);
            RenderUtils.drawBorderedRect((float)(iScaledResolution.getScaledWidth() / 2) - (float)2, (float)(iScaledResolution.getScaledHeight() / 2) + (float)5, (float)(iScaledResolution.getScaledWidth() / 2 + Fonts.font40.getStringWidth(string)) + (float)2, (float)(iScaledResolution.getScaledHeight() / 2) + (float)16, 3.0f, Color.BLACK.getRGB(), Color.BLACK.getRGB());
            MinecraftInstance.classProvider.getGlStateManager().resetColor();
            Fonts.font40.drawString(string, (float)iScaledResolution.getScaledWidth() / (float)2, (float)(iScaledResolution.getScaledHeight() / 2) + (float)7, Color.WHITE.getRGB());
            GL11.glPopMatrix();
        }
    }

    @EventTarget
    public final void onPacket(PacketEvent packetEvent) {
        if (MinecraftInstance.mc.getThePlayer() == null) {
            return;
        }
        IPacket iPacket = packetEvent.getPacket();
        if (MinecraftInstance.classProvider.isCPacketHeldItemChange(iPacket)) {
            this.slot = iPacket.asCPacketHeldItemChange().getSlotId();
        }
    }

    /*
     * Exception decompiling
     */
    @EventTarget
    public final void onMotion(MotionEvent var1) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl186 : RETURN - null : trying to set 0 previously set to 1
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:207)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public Tower() {
        this.stayAutoBlock = new BoolValue("StayAutoBlock", false);
        this.swingValue = new BoolValue("Swing", true);
        this.stopWhenBlockAbove = new BoolValue("StopWhenBlockAbove", false);
        this.rotationsValue = new BoolValue("Rotations", true);
        this.keepRotationValue = new BoolValue("KeepRotation", false);
        this.onJumpValue = new BoolValue("OnJump", false);
        this.placeModeValue = new ListValue("PlaceTiming", new String[]{"Pre", "Post"}, "Post");
        this.timerValue = new FloatValue("Timer", 1.0f, 0.0f, 10.0f);
        this.jumpMotionValue = new FloatValue("JumpMotion", 0.42f, 0.3681289f, 0.79f);
        this.jumpDelayValue = new IntegerValue("JumpDelay", 0, 0, 20);
        this.constantMotionValue = new FloatValue("ConstantMotion", 0.42f, 0.1f, 1.0f);
        this.constantMotionJumpGroundValue = new FloatValue("ConstantMotionJumpGround", 0.79f, 0.76f, 1.0f);
        this.teleportHeightValue = new FloatValue("TeleportHeight", 1.15f, 0.1f, 5.0f);
        this.teleportDelayValue = new IntegerValue("TeleportDelay", 0, 0, 20);
        this.teleportGroundValue = new BoolValue("TeleportGround", true);
        this.teleportNoMotionValue = new BoolValue("TeleportNoMotion", false);
        this.counterDisplayValue = new BoolValue("Counter", true);
        this.timer = new TickTimer();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private final void move() {
        IEntityPlayerSP iEntityPlayerSP;
        block23: {
            block18: {
                block21: {
                    block20: {
                        block22: {
                            block19: {
                                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                                if (iEntityPlayerSP2 == null) {
                                    return;
                                }
                                iEntityPlayerSP = iEntityPlayerSP2;
                                String string = (String)this.modeValue.get();
                                boolean bl = false;
                                String string2 = string;
                                if (string2 == null) {
                                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                                }
                                string = string2.toLowerCase();
                                switch (string.hashCode()) {
                                    case 325228192: {
                                        if (!string.equals("aac3.3.9")) return;
                                        break block18;
                                    }
                                    case -157173582: {
                                        if (!string.equals("motiontp")) return;
                                        break block19;
                                    }
                                    case -1068318794: {
                                        if (!string.equals("motion")) return;
                                        break;
                                    }
                                    case -1360201941: {
                                        if (!string.equals("teleport")) return;
                                        break block20;
                                    }
                                    case 792877146: {
                                        if (!string.equals("constantmotion")) return;
                                        break block21;
                                    }
                                    case -995865464: {
                                        if (!string.equals("packet")) return;
                                        break block22;
                                    }
                                    case 325231070: {
                                        if (!string.equals("aac3.6.4")) return;
                                        break block23;
                                    }
                                    case 3273774: {
                                        if (!string.equals("jump") || !iEntityPlayerSP.getOnGround() || !this.timer.hasTimePassed(((Number)this.jumpDelayValue.get()).intValue())) return;
                                        this.fakeJump();
                                        iEntityPlayerSP.setMotionY(((Number)this.jumpMotionValue.get()).floatValue());
                                        this.timer.reset();
                                        return;
                                    }
                                }
                                if (iEntityPlayerSP.getOnGround()) {
                                    this.fakeJump();
                                    iEntityPlayerSP.setMotionY(0.42);
                                    return;
                                }
                                if (!(iEntityPlayerSP.getMotionY() < 0.1)) return;
                                iEntityPlayerSP.setMotionY(-0.3);
                                return;
                            }
                            if (iEntityPlayerSP.getOnGround()) {
                                this.fakeJump();
                                iEntityPlayerSP.setMotionY(0.42);
                                return;
                            }
                            if (!(iEntityPlayerSP.getMotionY() < 0.23)) return;
                            iEntityPlayerSP.setPosition(iEntityPlayerSP.getPosX(), MathKt.truncate((double)iEntityPlayerSP.getPosY()), iEntityPlayerSP.getPosZ());
                            return;
                        }
                        if (!iEntityPlayerSP.getOnGround() || !this.timer.hasTimePassed(2)) return;
                        this.fakeJump();
                        MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(iEntityPlayerSP.getPosX(), iEntityPlayerSP.getPosY() + 0.42, iEntityPlayerSP.getPosZ(), false));
                        MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(iEntityPlayerSP.getPosX(), iEntityPlayerSP.getPosY() + 0.753, iEntityPlayerSP.getPosZ(), false));
                        iEntityPlayerSP.setPosition(iEntityPlayerSP.getPosX(), iEntityPlayerSP.getPosY() + 1.0, iEntityPlayerSP.getPosZ());
                        this.timer.reset();
                        return;
                    }
                    if (((Boolean)this.teleportNoMotionValue.get()).booleanValue()) {
                        iEntityPlayerSP.setMotionY(0.0);
                    }
                    if (!iEntityPlayerSP.getOnGround() && ((Boolean)this.teleportGroundValue.get()).booleanValue() || !this.timer.hasTimePassed(((Number)this.teleportDelayValue.get()).intValue())) return;
                    this.fakeJump();
                    iEntityPlayerSP.setPositionAndUpdate(iEntityPlayerSP.getPosX(), iEntityPlayerSP.getPosY() + ((Number)this.teleportHeightValue.get()).doubleValue(), iEntityPlayerSP.getPosZ());
                    this.timer.reset();
                    return;
                }
                if (iEntityPlayerSP.getOnGround()) {
                    this.fakeJump();
                    this.jumpGround = iEntityPlayerSP.getPosY();
                    iEntityPlayerSP.setMotionY(((Number)this.constantMotionValue.get()).floatValue());
                }
                if (!(iEntityPlayerSP.getPosY() > this.jumpGround + ((Number)this.constantMotionJumpGroundValue.get()).doubleValue())) return;
                this.fakeJump();
                iEntityPlayerSP.setPosition(iEntityPlayerSP.getPosX(), MathKt.truncate((double)iEntityPlayerSP.getPosY()), iEntityPlayerSP.getPosZ());
                iEntityPlayerSP.setMotionY(((Number)this.constantMotionValue.get()).floatValue());
                this.jumpGround = iEntityPlayerSP.getPosY();
                return;
            }
            if (iEntityPlayerSP.getOnGround()) {
                this.fakeJump();
                iEntityPlayerSP.setMotionY(0.4001);
            }
            MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
            if (!(iEntityPlayerSP.getMotionY() < 0.0)) return;
            IEntityPlayerSP iEntityPlayerSP3 = iEntityPlayerSP;
            iEntityPlayerSP3.setMotionY(iEntityPlayerSP3.getMotionY() - 9.45E-6);
            MinecraftInstance.mc.getTimer().setTimerSpeed(1.6f);
            return;
        }
        if (iEntityPlayerSP.getTicksExisted() % 4 == 1) {
            iEntityPlayerSP.setMotionY(0.4195464);
            iEntityPlayerSP.setPosition(iEntityPlayerSP.getPosX() - 0.035, iEntityPlayerSP.getPosY(), iEntityPlayerSP.getPosZ());
            return;
        }
        if (iEntityPlayerSP.getTicksExisted() % 4 != 0) return;
        iEntityPlayerSP.setMotionY(-0.5);
        iEntityPlayerSP.setPosition(iEntityPlayerSP.getPosX() + 0.035, iEntityPlayerSP.getPosY(), iEntityPlayerSP.getPosZ());
        return;
    }
}

