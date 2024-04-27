/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.util.math.MathHelper
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.features.module.modules.world;

import java.awt.Color;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.enums.BlockType;
import net.ccbluex.liquidbounce.api.enums.StatType;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemBlock;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketHeldItemChange;
import net.ccbluex.liquidbounce.api.minecraft.util.IScaledResolution;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.JumpEvent;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.StrafeEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.InventoryUtils;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.block.PlaceInfo;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.utils.timer.TickTimer;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="Scaffold2", description="Skid by Baron", category=ModuleCategory.WORLD)
public class Scaffold2
extends Module {
    private final IntegerValue zitterDelay;
    private long delay;
    private final BoolValue smartSpeedValue;
    private final IntegerValue blueValue;
    private final BoolValue keepRotationValue;
    private boolean eagleSneaking;
    private final BoolValue placeableDelay;
    private long lastMS = 0L;
    private PlaceInfo towerPlace;
    private final FloatValue eagleEdgeDistanceValue;
    private final IntegerValue maxDelayValue;
    public final ListValue counterDisplayValue;
    private final TickTimer timer;
    private final IntegerValue minDelayValue;
    private final MSTimer zitterTimer;
    private final BoolValue noMoveOnlyValue;
    private final FloatValue customMoveSpeedValue;
    private final BoolValue rotationStrafeValue;
    private final ListValue placeModeValue;
    private final BoolValue teleportGroundValue;
    private final BoolValue smartDelay;
    private final BoolValue downValue;
    private final ListValue placeConditionValue;
    private int verusState = 0;
    private final IntegerValue keepLengthValue;
    private int launchY;
    private final FloatValue zitterSpeed;
    public final FloatValue speedModifierValue;
    private final FloatValue minTurnSpeed;
    private final IntegerValue greenValue;
    private final BoolValue eagleValue;
    private int placedBlocksWithoutEagle = 0;
    private final BoolValue eagleSilentValue;
    private final IntegerValue teleportDelayValue;
    private final IntegerValue searchAccuracyValue;
    private final BoolValue customSpeedValue;
    private final FloatValue staticPitchValue;
    private final BoolValue autoDisableSpeedValue;
    private final BoolValue noHitCheckValue;
    private final FloatValue customPitchValue;
    private final IntegerValue expandLengthValue;
    private final FloatValue zitterStrength;
    public final BoolValue sprintValue;
    private final BoolValue towerEnabled = new BoolValue("EnableTower", false);
    private final BoolValue sameYValue;
    private final BoolValue stayAutoBlock;
    private final FloatValue timerValue;
    private final BoolValue omniDirectionalExpand;
    private final BoolValue markValue;
    private final FloatValue jumpMotionValue;
    private final BoolValue swingValue;
    private final IntegerValue blocksToEagleValue;
    private Rotation lookupRotation;
    private double jumpGround = 0.0;
    private final FloatValue maxTurnSpeed;
    private final ListValue autoBlockMode;
    private PlaceInfo targetPlace;
    private final ListValue zitterModeValue;
    private final IntegerValue alphaValue;
    private final BoolValue safeWalkValue;
    private final BoolValue teleportNoMotionValue;
    private final BoolValue airSafeValue;
    public final ListValue modeValue;
    public final ListValue sprintModeValue;
    private final FloatValue teleportHeightValue;
    private final FloatValue constantMotionValue;
    private final FloatValue yRangeValue;
    private boolean shouldGoDown = false;
    private final BoolValue autoJumpValue;
    private final IntegerValue redValue;
    private final BoolValue rotationsValue;
    private final FloatValue constantMotionJumpGroundValue;
    private final ListValue towerPlaceModeValue;
    private final BoolValue onJumpValue;
    private final IntegerValue HypixelPitchValue;
    private boolean zitterDirection;
    private boolean faceBlock;
    private final FloatValue customYawValue;
    public final ListValue rotationModeValue;
    private final FloatValue towerTimerValue;
    private final ListValue towerModeValue = new ListValue("TowerMode", new String[]{"Jump", "Motion", "ConstantMotion", "MotionTP", "Packet", "Teleport", "AAC3.3.9", "AAC3.6.4", "Verus"}, "Motion");
    private final BoolValue searchValue;
    private final BoolValue zitterValue;
    private boolean verusJumped = false;
    private int lastSlot;
    private int slot;
    public final ListValue rotationLookupValue;
    private final MSTimer delayTimer;
    private final IntegerValue HypixelYawValue;
    private float progress = 0.0f;
    private final IntegerValue jumpDelayValue;
    private final BoolValue keepRotOnJumpValue;
    private Rotation lockRotation;
    private final FloatValue xzRangeValue;
    private final BoolValue stopWhenBlockAbove;

    private void renderItemStack(IItemStack iItemStack, int n, int n2) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179091_B();
        GlStateManager.func_179147_l();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        RenderHelper.func_74520_c();
        mc.getRenderItem().renderItemAndEffectIntoGUI(iItemStack, n, n2);
        mc.getRenderItem().renderItemOverlays(mc.getFontRendererObj(), iItemStack, n, n2);
        RenderHelper.func_74518_a();
        GlStateManager.func_179101_C();
        GlStateManager.func_179084_k();
        GlStateManager.func_179121_F();
    }

    private double calcStepSize(double d) {
        double d2 = ((Integer)this.searchAccuracyValue.get()).intValue();
        if (d / (d2 += d2 % 2.0) < 0.01) {
            return 0.01;
        }
        return d / d2;
    }

    static FloatValue access$300(Scaffold2 scaffold2) {
        return scaffold2.maxTurnSpeed;
    }

    private void move(MotionEvent motionEvent) {
        switch (((String)this.towerModeValue.get()).toLowerCase()) {
            case "jump": {
                if (!mc.getThePlayer().getOnGround() || !this.timer.hasTimePassed((Integer)this.jumpDelayValue.get())) break;
                this.fakeJump();
                mc.getThePlayer().setMotionY(((Float)this.jumpMotionValue.get()).floatValue());
                this.timer.reset();
                break;
            }
            case "motion": {
                if (mc.getThePlayer().getOnGround()) {
                    this.fakeJump();
                    mc.getThePlayer().setMotionY(0.42);
                    break;
                }
                if (!(mc.getThePlayer().getMotionY() < 0.1)) break;
                mc.getThePlayer().setMotionY(0.3);
                break;
            }
            case "motiontp": {
                if (mc.getThePlayer().getOnGround()) {
                    this.fakeJump();
                    mc.getThePlayer().setMotionY(0.42);
                    break;
                }
                if (!(mc.getThePlayer().getMotionY() < 0.23)) break;
                mc.getThePlayer().setPosition(mc.getThePlayer().getPosX(), (int)mc.getThePlayer().getPosY(), mc.getThePlayer().getPosZ());
                break;
            }
            case "packet": {
                if (!mc.getThePlayer().getOnGround() || !this.timer.hasTimePassed(2)) break;
                this.fakeJump();
                mc.getNetHandler().addToSendQueue(classProvider.createCPacketPlayerPosition(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY() + 0.42, mc.getThePlayer().getPosZ(), false));
                mc.getNetHandler().addToSendQueue(classProvider.createCPacketPlayerPosition(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY() + 0.76, mc.getThePlayer().getPosZ(), false));
                mc.getThePlayer().setPosition(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY() + 1.08, mc.getThePlayer().getPosZ());
                this.timer.reset();
                break;
            }
            case "teleport": {
                if (((Boolean)this.teleportNoMotionValue.get()).booleanValue()) {
                    mc.getThePlayer().setMotionY(0.0);
                }
                if (!mc.getThePlayer().getOnGround() && ((Boolean)this.teleportGroundValue.get()).booleanValue() || !this.timer.hasTimePassed((Integer)this.teleportDelayValue.get())) break;
                this.fakeJump();
                mc.getThePlayer().setPositionAndUpdate(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY() + (double)((Float)this.teleportHeightValue.get()).floatValue(), mc.getThePlayer().getPosZ());
                this.timer.reset();
                break;
            }
            case "constantmotion": {
                if (mc.getThePlayer().getOnGround()) {
                    this.fakeJump();
                    this.jumpGround = mc.getThePlayer().getPosY();
                    mc.getThePlayer().setMotionY(((Float)this.constantMotionValue.get()).floatValue());
                }
                if (!(mc.getThePlayer().getPosY() > this.jumpGround + (double)((Float)this.constantMotionJumpGroundValue.get()).floatValue())) break;
                this.fakeJump();
                mc.getThePlayer().setPosition(mc.getThePlayer().getPosX(), (int)mc.getThePlayer().getPosY(), mc.getThePlayer().getPosZ());
                mc.getThePlayer().setMotionY(((Float)this.constantMotionValue.get()).floatValue());
                this.jumpGround = mc.getThePlayer().getPosY();
                break;
            }
            case "aac3.3.9": {
                if (mc.getThePlayer().getOnGround()) {
                    this.fakeJump();
                    mc.getThePlayer().setMotionY(0.4001);
                }
                mc.getTimer().setTimerSpeed(1.0f);
                if (!(mc.getThePlayer().getMotionY() < 0.0)) break;
                mc.getThePlayer().setMotionY(-9.45E-6);
                mc.getTimer().setTimerSpeed(1.6f);
                break;
            }
            case "aac3.6.4": {
                if (mc.getThePlayer().getTicksExisted() % 4 == 1) {
                    mc.getThePlayer().setMotionY(0.4195464);
                    mc.getThePlayer().setPosition(mc.getThePlayer().getPosX() - 0.035, mc.getThePlayer().getPosY(), mc.getThePlayer().getPosZ());
                    break;
                }
                if (mc.getThePlayer().getTicksExisted() % 4 != 0) break;
                mc.getThePlayer().setMotionY(-0.5);
                mc.getThePlayer().setPosition(mc.getThePlayer().getPosX() + 0.035, mc.getThePlayer().getPosY(), mc.getThePlayer().getPosZ());
                break;
            }
            case "verus": {
                if (!mc.getTheWorld().getCollidingBoundingBoxes(mc.getThePlayer(), mc.getThePlayer().getEntityBoundingBox().offset(0.0, -0.01, 0.0)).isEmpty() && mc.getThePlayer().getOnGround() && mc.getThePlayer().isCollidedVertically()) {
                    this.verusState = 0;
                    this.verusJumped = true;
                }
                if (this.verusJumped) {
                    MovementUtils.strafe();
                    switch (this.verusState) {
                        case 0: {
                            this.fakeJump();
                            mc.getThePlayer().setMotionY(0.42f);
                            ++this.verusState;
                            break;
                        }
                        case 1: {
                            ++this.verusState;
                            break;
                        }
                        case 2: {
                            ++this.verusState;
                            break;
                        }
                        case 3: {
                            motionEvent.setOnGround(true);
                            mc.getThePlayer().setMotionY(0.0);
                            ++this.verusState;
                            break;
                        }
                        case 4: {
                            ++this.verusState;
                        }
                    }
                    this.verusJumped = false;
                }
                this.verusJumped = true;
            }
        }
    }

    @EventTarget
    public void onMove(MoveEvent moveEvent) {
        if (!((Boolean)this.safeWalkValue.get()).booleanValue() || this.shouldGoDown) {
            return;
        }
        if (((Boolean)this.airSafeValue.get()).booleanValue() || mc.getThePlayer().getOnGround()) {
            moveEvent.setSafeWalk(true);
        }
    }

    @EventTarget
    public void onStrafe(StrafeEvent strafeEvent) {
        if (this.lookupRotation != null && ((Boolean)this.rotationStrafeValue.get()).booleanValue()) {
            float f;
            int n = (int)((MathHelper.func_76142_g((float)(mc.getThePlayer().getRotationYaw() - this.lookupRotation.getYaw() - 23.5f - 135.0f)) + 180.0f) / 45.0f);
            float f2 = this.lookupRotation.getYaw();
            float f3 = strafeEvent.getStrafe();
            float f4 = strafeEvent.getForward();
            float f5 = strafeEvent.getFriction();
            float f6 = 0.0f;
            float f7 = 0.0f;
            switch (n) {
                case 0: {
                    f6 = f4;
                    f7 = f3;
                    break;
                }
                case 1: {
                    f6 += f4;
                    f7 -= f4;
                    f6 += f3;
                    f7 += f3;
                    break;
                }
                case 2: {
                    f6 = f3;
                    f7 = -f4;
                    break;
                }
                case 3: {
                    f6 -= f4;
                    f7 -= f4;
                    f6 += f3;
                    f7 -= f3;
                    break;
                }
                case 4: {
                    f6 = -f4;
                    f7 = -f3;
                    break;
                }
                case 5: {
                    f6 -= f4;
                    f7 += f4;
                    f6 -= f3;
                    f7 -= f3;
                    break;
                }
                case 6: {
                    f6 = -f3;
                    f7 = f4;
                    break;
                }
                case 7: {
                    f6 += f4;
                    f7 += f4;
                    f6 -= f3;
                    f7 += f3;
                }
            }
            if (f6 > 1.0f || f6 < 0.9f && f6 > 0.3f || f6 < -1.0f || f6 > -0.9f && f6 < -0.3f) {
                f6 *= 0.5f;
            }
            if (f7 > 1.0f || f7 < 0.9f && f7 > 0.3f || f7 < -1.0f || f7 > -0.9f && f7 < -0.3f) {
                f7 *= 0.5f;
            }
            if ((f = f7 * f7 + f6 * f6) >= 1.0E-4f) {
                if ((f = MathHelper.func_76129_c((float)f)) < 1.0f) {
                    f = 1.0f;
                }
                f = f5 / f;
                float f8 = MathHelper.func_76126_a((float)((float)((double)f2 * Math.PI / 180.0)));
                float f9 = MathHelper.func_76134_b((float)((float)((double)f2 * Math.PI / 180.0)));
                mc.getThePlayer().setMotionX((f7 *= f) * f9 - (f6 *= f) * f8);
                mc.getThePlayer().setMotionZ(f6 * f9 + f7 * f8);
            }
            strafeEvent.cancelEvent();
        }
    }

    @EventTarget
    public void onPacket(PacketEvent packetEvent) {
        if (mc.getThePlayer() == null) {
            return;
        }
        IPacket iPacket = packetEvent.getPacket();
        if (classProvider.isCPacketHeldItemChange(iPacket)) {
            ICPacketHeldItemChange iCPacketHeldItemChange = iPacket.asCPacketHeldItemChange();
            this.slot = iCPacketHeldItemChange.getSlotId();
        }
    }

    private void fakeJump() {
        mc.getThePlayer().setAirBorne(true);
        mc.getThePlayer().triggerAchievement(classProvider.getStatEnum(StatType.JUMP_STAT));
    }

    /*
     * Exception decompiling
     */
    private void findBlock(boolean var1) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl147 : RETURN - null : trying to set 0 previously set to 3
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

    private boolean search(WBlockPos wBlockPos, boolean bl) {
        return this.search(wBlockPos, bl, false);
    }

    @Override
    public void onEnable() {
        if (mc.getThePlayer() == null) {
            return;
        }
        this.progress = 0.0f;
        this.launchY = (int)mc.getThePlayer().getPosY();
        this.lastSlot = mc.getThePlayer().getInventory().getCurrentItem();
        this.slot = mc.getThePlayer().getInventory().getCurrentItem();
        if (((Boolean)this.autoDisableSpeedValue.get()).booleanValue() && LiquidBounce.moduleManager.getModule(Speed.class).getState()) {
            LiquidBounce.moduleManager.getModule(Speed.class).setState(false);
            LiquidBounce.hud.addNotification(new Notification("Scaffold", "Speed is disabled to prevent flags/errors.", NotifyType.INFO, 500, 1000));
        }
        this.faceBlock = false;
        this.lastMS = System.currentTimeMillis();
    }

    static FloatValue access$200(Scaffold2 scaffold2) {
        return scaffold2.minTurnSpeed;
    }

    private int getBlocksAmount() {
        int n = 0;
        for (int i = 36; i < 45; ++i) {
            IItemStack iItemStack = mc.getThePlayer().getInventoryContainer().getSlot(i).getStack();
            if (iItemStack == null || !classProvider.isItemBlock(iItemStack.getItem())) continue;
            IBlock iBlock = iItemStack.getItem().asItemBlock().getBlock();
            IItemStack iItemStack2 = mc.getThePlayer().getHeldItem();
            if ((iItemStack2 == null || !iItemStack2.equals(iItemStack)) && (InventoryUtils.BLOCK_BLACKLIST.contains(iBlock) || classProvider.isBlockBush(iBlock))) continue;
            n += iItemStack.getStackSize();
        }
        return n;
    }

    public boolean isTowerOnly() {
        return (Boolean)this.towerEnabled.get() != false && (Boolean)this.onJumpValue.get() == false;
    }

    @Override
    public void onDisable() {
        if (mc.getThePlayer() == null) {
            return;
        }
        if (mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindSneak())) {
            mc.getGameSettings().getKeyBindSneak().setPressed(false);
            if (this.eagleSneaking) {
                mc.getNetHandler().addToSendQueue(classProvider.createCPacketEntityAction(mc.getThePlayer(), ICPacketEntityAction.WAction.STOP_SNEAKING));
            }
        }
        if (mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindRight())) {
            mc.getGameSettings().getKeyBindRight().setPressed(false);
        }
        if (mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindLeft())) {
            mc.getGameSettings().getKeyBindLeft().setPressed(false);
        }
        this.lockRotation = null;
        this.lookupRotation = null;
        mc.getTimer().setTimerSpeed(1.0f);
        this.shouldGoDown = false;
        this.faceBlock = false;
        if (this.lastSlot != mc.getThePlayer().getInventory().getCurrentItem() && ((String)this.autoBlockMode.get()).equalsIgnoreCase("switch")) {
            mc.getThePlayer().getInventory().setCurrentItem(this.lastSlot);
            mc.getPlayerController().updateController();
        }
        if (this.slot != mc.getThePlayer().getInventory().getCurrentItem() && ((String)this.autoBlockMode.get()).equalsIgnoreCase("spoof")) {
            mc.getNetHandler().addToSendQueue(classProvider.createCPacketHeldItemChange(mc.getThePlayer().getInventory().getCurrentItem()));
        }
    }

    /*
     * Exception decompiling
     */
    @EventTarget
    public void onMotion(MotionEvent var1) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl273 : RETURN - null : trying to set 0 previously set to 3
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

    @Override
    public String getTag() {
        return this != false ? "Tower, " + (String)this.towerPlaceModeValue.get() : (String)this.placeModeValue.get();
    }

    static IntegerValue access$000(Scaffold2 scaffold2) {
        return scaffold2.minDelayValue;
    }

    private void place(boolean bl) {
        IItemBlock iItemBlock;
        IBlock iBlock;
        if (((String)this.sprintModeValue.get()).equalsIgnoreCase("PlaceOff")) {
            mc.getThePlayer().setSprinting(false);
            mc.getThePlayer().setMotionX(mc.getThePlayer().getMotionX() * 1.0);
            mc.getThePlayer().setMotionZ(mc.getThePlayer().getMotionZ() * 1.0);
        }
        if ((bl ? this.towerPlace : this.targetPlace) == null) {
            if (((Boolean)this.placeableDelay.get()).booleanValue()) {
                this.delayTimer.reset();
            }
            return;
        }
        if (this != false && (!this.delayTimer.hasTimePassed(this.delay) || ((Boolean)this.smartDelay.get()).booleanValue() && mc.getRightClickDelayTimer() > 0 || (((Boolean)this.sameYValue.get()).booleanValue() || (((Boolean)this.autoJumpValue.get()).booleanValue() || ((Boolean)this.smartSpeedValue.get()).booleanValue() && LiquidBounce.moduleManager.getModule(Speed.class).getState()) && mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindJump())) && this.launchY - 1 != (int)(bl ? this.towerPlace : this.targetPlace).getVec3().getYCoord())) {
            return;
        }
        int n = -1;
        IItemStack iItemStack = mc.getThePlayer().getHeldItem();
        if (mc.getThePlayer().getHeldItem() == null || !(mc.getThePlayer().getHeldItem().getItem() instanceof ItemBlock)) {
            if (((String)this.autoBlockMode.get()).equalsIgnoreCase("Off")) {
                return;
            }
            n = InventoryUtils.findAutoBlockBlock();
            if (n == -1) {
                return;
            }
            if (((String)this.autoBlockMode.get()).equalsIgnoreCase("Matrix") && n - 36 != this.slot) {
                mc.getNetHandler().addToSendQueue(classProvider.createCPacketHeldItemChange(n - 36));
            }
            if (((String)this.autoBlockMode.get()).equalsIgnoreCase("Spoof")) {
                mc.getNetHandler().addToSendQueue(classProvider.createCPacketHeldItemChange(n - 36));
                iItemStack = mc.getThePlayer().getInventoryContainer().getSlot(n).getStack();
            } else {
                mc.getThePlayer().getInventory().setCurrentItem(n - 36);
                mc.getPlayerController().updateController();
            }
        }
        if (iItemStack != null && iItemStack.getItem() != null && iItemStack.getItem() instanceof ItemBlock && (InventoryUtils.BLOCK_BLACKLIST.contains(iBlock = (iItemBlock = iItemStack.getItem().asItemBlock()).getBlock()) || !iBlock.isFullCube(iBlock.getDefaultState()) || iItemStack.getStackSize() <= 0)) {
            return;
        }
        if (mc.getPlayerController().onPlayerRightClick(mc.getThePlayer(), mc.getTheWorld(), iItemStack, (bl ? this.towerPlace : this.targetPlace).getBlockPos(), (bl ? this.towerPlace : this.targetPlace).getEnumFacing(), (bl ? this.towerPlace : this.targetPlace).getVec3())) {
            this.delayTimer.reset();
            long l = this.delay = (Boolean)this.placeableDelay.get() == false ? 0L : TimeUtils.randomDelay((Integer)this.minDelayValue.get(), (Integer)this.maxDelayValue.get());
            if (mc.getThePlayer().getOnGround()) {
                float f = ((Float)this.speedModifierValue.get()).floatValue();
                mc.getThePlayer().setMotionX(mc.getThePlayer().getMotionX() * (double)f);
                mc.getThePlayer().setMotionZ(mc.getThePlayer().getMotionZ() * (double)f);
            }
            if (((Boolean)this.swingValue.get()).booleanValue()) {
                mc.getThePlayer().swingItem();
            } else {
                mc.getNetHandler().addToSendQueue(classProvider.createCPacketAnimation());
            }
        }
        if (bl) {
            this.towerPlace = null;
        } else {
            this.targetPlace = null;
        }
        if (!((Boolean)this.stayAutoBlock.get()).booleanValue() && n >= 0 && !((String)this.autoBlockMode.get()).equalsIgnoreCase("Switch")) {
            mc.getNetHandler().addToSendQueue(classProvider.createCPacketHeldItemChange(mc.getThePlayer().getInventory().getCurrentItem()));
        }
    }

    static IntegerValue access$100(Scaffold2 scaffold2) {
        return scaffold2.maxDelayValue;
    }

    @EventTarget
    public void onRender2D(Render2DEvent render2DEvent) {
        this.progress = (float)(System.currentTimeMillis() - this.lastMS) / 100.0f;
        if (this.progress >= 1.0f) {
            this.progress = 1.0f;
        }
        String string = (String)this.counterDisplayValue.get();
        IScaledResolution iScaledResolution = classProvider.createScaledResolution(mc);
        String string2 = this.getBlocksAmount() + " blocks";
        int n = Fonts.font25.getStringWidth(string2);
        int n2 = Fonts.minecraftFont.getStringWidth(this.getBlocksAmount() + "");
        if (string.equalsIgnoreCase("simple")) {
            Fonts.minecraftFont.drawString(this.getBlocksAmount() + "", iScaledResolution.getScaledWidth() / 2 - n2 / 2 - 1, iScaledResolution.getScaledHeight() / 2 - 36, -16777216, false);
            Fonts.minecraftFont.drawString(this.getBlocksAmount() + "", iScaledResolution.getScaledWidth() / 2 - n2 / 2 + 1, iScaledResolution.getScaledHeight() / 2 - 36, -16777216, false);
            Fonts.minecraftFont.drawString(this.getBlocksAmount() + "", iScaledResolution.getScaledWidth() / 2 - n2 / 2, iScaledResolution.getScaledHeight() / 2 - 35, -16777216, false);
            Fonts.minecraftFont.drawString(this.getBlocksAmount() + "", iScaledResolution.getScaledWidth() / 2 - n2 / 2, iScaledResolution.getScaledHeight() / 2 - 37, -16777216, false);
            Fonts.minecraftFont.drawString(this.getBlocksAmount() + "", iScaledResolution.getScaledWidth() / 2 - n2 / 2, iScaledResolution.getScaledHeight() / 2 - 36, -1, false);
        }
        if (string.equalsIgnoreCase("advanced")) {
            boolean bl = this.slot >= 0 && this.slot < 9 && mc.getThePlayer().getInventory().getMainInventory().get(this.slot) != null && ((IItemStack)mc.getThePlayer().getInventory().getMainInventory().get(this.slot)).getItem() != null && ((IItemStack)mc.getThePlayer().getInventory().getMainInventory().get(this.slot)).getItem() instanceof ItemBlock;
            RenderUtils.drawRect(iScaledResolution.getScaledWidth() / 2 - n / 2 - 4, iScaledResolution.getScaledHeight() / 2 - 40, iScaledResolution.getScaledWidth() / 2 + n / 2 + 4, iScaledResolution.getScaledHeight() / 2 - 39, this.getBlocksAmount() > 1 ? -1 : -61424);
            RenderUtils.drawRect(iScaledResolution.getScaledWidth() / 2 - n / 2 - 4, iScaledResolution.getScaledHeight() / 2 - 39, iScaledResolution.getScaledWidth() / 2 + n / 2 + 4, iScaledResolution.getScaledHeight() / 2 - 26, -1610612736);
            if (bl) {
                RenderUtils.drawRect(iScaledResolution.getScaledWidth() / 2 - n / 2 - 4, iScaledResolution.getScaledHeight() / 2 - 26, iScaledResolution.getScaledWidth() / 2 + n / 2 + 4, iScaledResolution.getScaledHeight() / 2 - 5, -1610612736);
                GlStateManager.func_179094_E();
                GlStateManager.func_179109_b((float)(iScaledResolution.getScaledWidth() / 2 - 8), (float)(iScaledResolution.getScaledHeight() / 2 - 25), (float)(iScaledResolution.getScaledWidth() / 2 - 8));
                this.renderItemStack((IItemStack)mc.getThePlayer().getInventory().getMainInventory().get(this.slot), 0, 0);
                GlStateManager.func_179121_F();
            }
            GlStateManager.func_179117_G();
            Fonts.font25.drawCenteredString(string2, iScaledResolution.getScaledWidth() / 2, iScaledResolution.getScaledHeight() / 2 - 36, -1);
        }
        if (string.equalsIgnoreCase("sigma")) {
            GlStateManager.func_179109_b((float)0.0f, (float)(-14.0f - this.progress * 4.0f), (float)0.0f);
            GL11.glEnable((int)3042);
            GL11.glDisable((int)3553);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glEnable((int)2848);
            GL11.glColor4f((float)0.15f, (float)0.15f, (float)0.15f, (float)this.progress);
            GL11.glBegin((int)6);
            GL11.glVertex2d((double)(iScaledResolution.getScaledWidth() / 2 - 3), (double)(iScaledResolution.getScaledHeight() - 60));
            GL11.glVertex2d((double)(iScaledResolution.getScaledWidth() / 2), (double)(iScaledResolution.getScaledHeight() - 57));
            GL11.glVertex2d((double)(iScaledResolution.getScaledWidth() / 2 + 3), (double)(iScaledResolution.getScaledHeight() - 60));
            GL11.glEnd();
            GL11.glEnable((int)3553);
            GL11.glDisable((int)3042);
            GL11.glDisable((int)2848);
            RenderUtils.drawRoundedRect((float)iScaledResolution.getScaledWidth() / 2.0f - (float)(n / 2) - 4.0f, (float)iScaledResolution.getScaledHeight() - 60.0f, (float)iScaledResolution.getScaledWidth() / 2.0f + (float)(n / 2) + 4.0f, (float)iScaledResolution.getScaledHeight() - 74.0f, 2.0f, new Color(0.15f, 0.15f, 0.15f, this.progress).getRGB());
            GlStateManager.func_179117_G();
            Fonts.font25.drawCenteredString(string2, (float)(iScaledResolution.getScaledWidth() / 2) + 0.1f, iScaledResolution.getScaledHeight() - 70, new Color(1.0f, 1.0f, 1.0f, 0.8f * this.progress).getRGB(), false);
            GlStateManager.func_179109_b((float)0.0f, (float)(14.0f + this.progress * 4.0f), (float)0.0f);
        }
        if (string.equalsIgnoreCase("novoline")) {
            if (this.slot >= 0 && this.slot < 9 && mc.getThePlayer().getInventory().getMainInventory().get(this.slot) != null && ((IItemStack)mc.getThePlayer().getInventory().getMainInventory().get(this.slot)).getItem() != null && ((IItemStack)mc.getThePlayer().getInventory().getMainInventory().get(this.slot)).getItem() instanceof ItemBlock) {
                GlStateManager.func_179094_E();
                GlStateManager.func_179109_b((float)(iScaledResolution.getScaledWidth() / 2 - 22), (float)(iScaledResolution.getScaledHeight() / 2 + 16), (float)(iScaledResolution.getScaledWidth() / 2 - 22));
                this.renderItemStack((IItemStack)mc.getThePlayer().getInventory().getMainInventory().get(this.slot), 0, 0);
                GlStateManager.func_179121_F();
            }
            GlStateManager.func_179117_G();
            Fonts.minecraftFont.drawString(this.getBlocksAmount() + " blocks", iScaledResolution.getScaledWidth() / 2, iScaledResolution.getScaledHeight() / 2 + 20, -1, true);
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent updateEvent) {
        if (this != false) {
            this.shouldGoDown = false;
            mc.getGameSettings().getKeyBindSneak().setPressed(false);
            mc.getThePlayer().setSprinting(false);
            return;
        }
        if (((String)this.sprintModeValue.get()).equalsIgnoreCase("PlaceOff")) {
            if (mc.getThePlayer().getOnGround()) {
                // empty if block
            }
            mc.getThePlayer().setSprinting(true);
            mc.getThePlayer().setMotionX(mc.getThePlayer().getMotionX() * 1.0);
            mc.getThePlayer().setMotionZ(mc.getThePlayer().getMotionZ() * 1.0);
        }
        mc.getTimer().setTimerSpeed(((Float)this.timerValue.get()).floatValue());
        boolean bl = this.shouldGoDown = (Boolean)this.downValue.get() != false && (Boolean)this.sameYValue.get() == false && mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindSneak()) && this.getBlocksAmount() > 1;
        if (this.shouldGoDown) {
            mc.getGameSettings().getKeyBindSneak().setPressed(false);
        }
        if (((Boolean)this.customSpeedValue.get()).booleanValue()) {
            MovementUtils.strafe(((Float)this.customMoveSpeedValue.get()).floatValue());
        }
        if (mc.getThePlayer().getOnGround()) {
            double d;
            String string = (String)this.modeValue.get();
            if (string.equalsIgnoreCase("Rewinside")) {
                MovementUtils.strafe(0.2f);
                mc.getThePlayer().setMotionY(0.0);
            }
            if (((Boolean)this.zitterValue.get()).booleanValue() && ((String)this.zitterModeValue.get()).equalsIgnoreCase("smooth")) {
                if (mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindRight())) {
                    mc.getGameSettings().getKeyBindRight().setPressed(false);
                }
                if (mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindLeft())) {
                    mc.getGameSettings().getKeyBindLeft().setPressed(false);
                }
                if (this.zitterTimer.hasTimePassed(((Integer)this.zitterDelay.get()).intValue())) {
                    this.zitterDirection = !this.zitterDirection;
                    this.zitterTimer.reset();
                }
                if (this.zitterDirection) {
                    mc.getGameSettings().getKeyBindRight().setPressed(true);
                    mc.getGameSettings().getKeyBindLeft().setPressed(false);
                } else {
                    mc.getGameSettings().getKeyBindRight().setPressed(false);
                    mc.getGameSettings().getKeyBindLeft().setPressed(true);
                }
            }
            if (((Boolean)this.eagleValue.get()).booleanValue() && !this.shouldGoDown) {
                int n;
                d = 0.5;
                if (((Float)this.eagleEdgeDistanceValue.get()).floatValue() > 0.0f) {
                    for (n = 0; n < 4; ++n) {
                        WBlockPos wBlockPos = new WBlockPos(mc.getThePlayer().getPosX() + (double)(n == 0 ? -1 : (n == 1 ? 1 : 0)), mc.getThePlayer().getPosY() - (mc.getThePlayer().getPosY() == (double)((int)mc.getThePlayer().getPosY()) + 0.5 ? 0.0 : 1.0), mc.getThePlayer().getPosZ() + (double)(n == 2 ? -1 : (n == 3 ? 1 : 0)));
                        PlaceInfo placeInfo = PlaceInfo.get(wBlockPos);
                        if (!BlockUtils.isReplaceable(wBlockPos) || placeInfo == null) continue;
                        double d2 = n > 1 ? mc.getThePlayer().getPosZ() - (double)wBlockPos.getZ() : mc.getThePlayer().getPosX() - (double)wBlockPos.getX();
                        if ((d2 -= 0.5) < 0.0) {
                            d2 *= -1.0;
                        }
                        if (!((d2 -= 0.5) < d)) continue;
                        d = d2;
                    }
                }
                if (this.placedBlocksWithoutEagle >= (Integer)this.blocksToEagleValue.get()) {
                    int n2 = n = mc.getTheWorld().getBlockState(new WBlockPos(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY() - 1.0, mc.getThePlayer().getPosZ())).getBlock().equals(classProvider.getBlockEnum(BlockType.AIR)) || d < (double)((Float)this.eagleEdgeDistanceValue.get()).floatValue() ? 1 : 0;
                    if (((Boolean)this.eagleSilentValue.get()).booleanValue()) {
                        if (this.eagleSneaking != n) {
                            mc.getNetHandler().addToSendQueue(classProvider.createCPacketEntityAction(mc.getThePlayer(), n != 0 ? ICPacketEntityAction.WAction.START_SNEAKING : ICPacketEntityAction.WAction.STOP_SNEAKING));
                        }
                        this.eagleSneaking = n;
                    } else {
                        mc.getGameSettings().getKeyBindSneak().setPressed(n != 0);
                    }
                    this.placedBlocksWithoutEagle = 0;
                } else {
                    ++this.placedBlocksWithoutEagle;
                }
            }
            if (((Boolean)this.zitterValue.get()).booleanValue() && ((String)this.zitterModeValue.get()).equalsIgnoreCase("teleport")) {
                MovementUtils.strafe(((Float)this.zitterSpeed.get()).floatValue());
                d = Math.toRadians((double)mc.getThePlayer().getRotationYaw() + (this.zitterDirection ? 90.0 : -90.0));
                mc.getThePlayer().setMotionX(-Math.sin(d) * (double)((Float)this.zitterStrength.get()).floatValue());
                mc.getThePlayer().setMotionZ(Math.cos(d) * (double)((Float)this.zitterStrength.get()).floatValue());
                boolean bl2 = this.zitterDirection = !this.zitterDirection;
            }
        }
        if (((String)this.sprintModeValue.get()).equalsIgnoreCase("off") || ((String)this.sprintModeValue.get()).equalsIgnoreCase("ground") && !mc.getThePlayer().getOnGround() || ((String)this.sprintModeValue.get()).equalsIgnoreCase("air") && mc.getThePlayer().getOnGround()) {
            mc.getThePlayer().setSprinting(true);
        }
        if (this.shouldGoDown) {
            this.launchY = (int)mc.getThePlayer().getPosY() - 1;
        } else if (!((Boolean)this.sameYValue.get()).booleanValue()) {
            if (!((Boolean)this.autoJumpValue.get()).booleanValue() && (!((Boolean)this.smartSpeedValue.get()).booleanValue() || !LiquidBounce.moduleManager.getModule(Speed.class).getState()) || mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindJump()) || mc.getThePlayer().getPosY() < (double)this.launchY) {
                this.launchY = (int)mc.getThePlayer().getPosY();
            }
            if (((Boolean)this.autoJumpValue.get()).booleanValue() && !LiquidBounce.moduleManager.getModule(Speed.class).getState() && MovementUtils.isMoving() && mc.getThePlayer().getOnGround()) {
                mc.getThePlayer().jump();
            }
        }
    }

    @EventTarget
    public void onRender3D(Render3DEvent render3DEvent) {
        if (!((Boolean)this.markValue.get()).booleanValue()) {
            return;
        }
        double d = Math.toRadians(mc.getThePlayer().getRotationYaw());
        int n = (Boolean)this.omniDirectionalExpand.get() != false ? (int)Math.round(-Math.sin(d)) : mc.getThePlayer().getHorizontalFacing().getDirectionVec().getX();
        int n2 = (Boolean)this.omniDirectionalExpand.get() != false ? (int)Math.round(Math.cos(d)) : mc.getThePlayer().getHorizontalFacing().getDirectionVec().getZ();
        for (int i = 0; i < (((String)this.modeValue.get()).equalsIgnoreCase("Expand") && this != false ? (Integer)this.expandLengthValue.get() + 1 : 2); ++i) {
            WBlockPos wBlockPos = new WBlockPos(mc.getThePlayer().getPosX() + (double)(n * i), this != false && ((Boolean)this.sameYValue.get() != false || ((Boolean)this.autoJumpValue.get() != false || (Boolean)this.smartSpeedValue.get() != false && LiquidBounce.moduleManager.getModule(Speed.class).getState()) && mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindJump())) && (double)this.launchY <= mc.getThePlayer().getPosY() ? (double)(this.launchY - 1) : mc.getThePlayer().getPosY() - (mc.getThePlayer().getPosY() == (double)((int)mc.getThePlayer().getPosY()) + 0.5 ? 0.0 : 1.0) - (this.shouldGoDown ? 1.0 : 0.0), mc.getThePlayer().getPosZ() + (double)(n2 * i));
            PlaceInfo placeInfo = PlaceInfo.get(wBlockPos);
            if (!BlockUtils.isReplaceable(wBlockPos) || placeInfo == null) continue;
            RenderUtils.drawBlockBox(wBlockPos, new Color((Integer)this.redValue.get(), (Integer)this.greenValue.get(), (Integer)this.blueValue.get(), (Integer)this.alphaValue.get()), false);
            break;
        }
    }

    public Scaffold2() {
        this.towerPlaceModeValue = new ListValue("Tower-PlaceTiming", new String[]{"Pre", "Post"}, "Post");
        this.stopWhenBlockAbove = new BoolValue("StopWhenBlockAbove", false);
        this.onJumpValue = new BoolValue("OnJump", false);
        this.noMoveOnlyValue = new BoolValue("NoMove", true);
        this.towerTimerValue = new FloatValue("TowerTimer", 1.0f, 0.1f, 10.0f);
        this.jumpMotionValue = new FloatValue("JumpMotion", 0.42f, 0.3681289f, 0.79f);
        this.jumpDelayValue = new IntegerValue("JumpDelay", 0, 0, 20);
        this.constantMotionValue = new FloatValue("ConstantMotion", 0.42f, 0.1f, 1.0f);
        this.constantMotionJumpGroundValue = new FloatValue("ConstantMotionJumpGround", 0.79f, 0.76f, 1.0f);
        this.teleportHeightValue = new FloatValue("TeleportHeight", 1.15f, 0.1f, 5.0f);
        this.teleportDelayValue = new IntegerValue("TeleportDelay", 0, 0, 20);
        this.teleportGroundValue = new BoolValue("TeleportGround", true);
        this.teleportNoMotionValue = new BoolValue("TeleportNoMotion", false);
        this.modeValue = new ListValue("Mode", new String[]{"Normal", "Rewinside", "Expand"}, "Normal");
        this.placeableDelay = new BoolValue("PlaceableDelay", false);
        this.maxDelayValue = new IntegerValue(this, "MaxDelay", 0, 0, 1000){
            final Scaffold2 this$0;
            {
                this.this$0 = scaffold2;
                super(string, n, n2, n3);
            }

            @Override
            protected void onChanged(Object object, Object object2) {
                this.onChanged((Integer)object, (Integer)object2);
            }

            protected void onChanged(Integer n, Integer n2) {
                int n3 = (Integer)Scaffold2.access$000(this.this$0).get();
                if (n3 > n2) {
                    this.set((Object)n3);
                }
            }
        };
        this.minDelayValue = new IntegerValue(this, "MinDelay", 0, 0, 1000){
            final Scaffold2 this$0;

            @Override
            protected void onChanged(Object object, Object object2) {
                this.onChanged((Integer)object, (Integer)object2);
            }

            protected void onChanged(Integer n, Integer n2) {
                int n3 = (Integer)Scaffold2.access$100(this.this$0).get();
                if (n3 < n2) {
                    this.set((Object)n3);
                }
            }
            {
                this.this$0 = scaffold2;
                super(string, n, n2, n3);
            }
        };
        this.smartDelay = new BoolValue("SmartDelay", true);
        this.autoBlockMode = new ListValue("AutoBlock", new String[]{"Spoof", "Switch", "Matrix", "Off"}, "Spoof");
        this.stayAutoBlock = new BoolValue("StayAutoBlock", false);
        this.sprintModeValue = new ListValue("SprintMode", new String[]{"Same", "Ground", "Air", "PlaceOff"}, "Air");
        this.sprintValue = new BoolValue("Sprint", true);
        this.swingValue = new BoolValue("Swing", true);
        this.downValue = new BoolValue("Down", false);
        this.searchValue = new BoolValue("Search", true);
        this.placeModeValue = new ListValue("PlaceTiming", new String[]{"Pre", "Post"}, "Post");
        this.eagleValue = new BoolValue("Eagle", false);
        this.eagleSilentValue = new BoolValue("EagleSilent", false);
        this.blocksToEagleValue = new IntegerValue("BlocksToEagle", 0, 0, 10);
        this.eagleEdgeDistanceValue = new FloatValue("EagleEdgeDistance", 0.2f, 0.0f, 0.5f);
        this.omniDirectionalExpand = new BoolValue("OmniDirectionalExpand", true);
        this.expandLengthValue = new IntegerValue("ExpandLength", 5, 1, 6);
        this.searchAccuracyValue = new IntegerValue(this, "SearchAccuracy", 8, 1, 24){
            final Scaffold2 this$0;
            {
                this.this$0 = scaffold2;
                super(string, n, n2, n3);
            }

            protected void onChanged(Integer n, Integer n2) {
                if (this.getMaximum() < n2) {
                    this.set((Object)this.getMaximum());
                } else if (this.getMinimum() > n2) {
                    this.set((Object)this.getMinimum());
                }
            }

            @Override
            protected void onChanged(Object object, Object object2) {
                this.onChanged((Integer)object, (Integer)object2);
            }
        };
        this.xzRangeValue = new FloatValue("xzRange", 0.8f, 0.1f, 1.0f);
        this.yRangeValue = new FloatValue("yRange", 0.8f, 0.1f, 1.0f);
        this.rotationsValue = new BoolValue("Rotations", true);
        this.noHitCheckValue = new BoolValue("NoHitCheck", false);
        this.rotationModeValue = new ListValue("RotationMode", new String[]{"Hypixel", "Normal", "AAC", "Static", "Static2", "Static3", "Custom"}, "Normal");
        this.rotationLookupValue = new ListValue("RotationLookup", new String[]{"Normal", "AAC", "Same"}, "Normal");
        this.maxTurnSpeed = new FloatValue(this, "MaxTurnSpeed", 180.0f, 0.0f, 180.0f){
            final Scaffold2 this$0;
            {
                this.this$0 = scaffold2;
                super(string, f, f2, f3);
            }

            protected void onChanged(Float f, Float f2) {
                float f3 = ((Float)Scaffold2.access$200(this.this$0).get()).floatValue();
                if (f3 > f2.floatValue()) {
                    this.set((Object)Float.valueOf(f3));
                }
            }

            @Override
            protected void onChanged(Object object, Object object2) {
                this.onChanged((Float)object, (Float)object2);
            }
        };
        this.minTurnSpeed = new FloatValue(this, "MinTurnSpeed", 180.0f, 0.0f, 180.0f){
            final Scaffold2 this$0;

            @Override
            protected void onChanged(Object object, Object object2) {
                this.onChanged((Float)object, (Float)object2);
            }

            protected void onChanged(Float f, Float f2) {
                float f3 = ((Float)Scaffold2.access$300(this.this$0).get()).floatValue();
                if (f3 < f2.floatValue()) {
                    this.set((Object)Float.valueOf(f3));
                }
            }
            {
                this.this$0 = scaffold2;
                super(string, f, f2, f3);
            }
        };
        this.HypixelYawValue = new IntegerValue("HypixelYaw", 180, -360, 360);
        this.HypixelPitchValue = new IntegerValue("HypixelPitch", 79, 60, 100);
        this.staticPitchValue = new FloatValue("Static-Pitch", 86.0f, 80.0f, 90.0f);
        this.customYawValue = new FloatValue("Custom-Yaw", 135.0f, -180.0f, 180.0f);
        this.customPitchValue = new FloatValue("Custom-Pitch", 86.0f, -90.0f, 90.0f);
        this.keepRotOnJumpValue = new BoolValue("KeepRotOnJump", true);
        this.keepRotationValue = new BoolValue("KeepRotation", false);
        this.keepLengthValue = new IntegerValue("KeepRotationLength", 0, 0, 20);
        this.placeConditionValue = new ListValue("Place-Condition", new String[]{"Air", "FallDown", "NegativeMotion", "Always"}, "Always");
        this.rotationStrafeValue = new BoolValue("RotationStrafe", false);
        this.zitterValue = new BoolValue("Zitter", false);
        this.zitterModeValue = new ListValue("ZitterMode", new String[]{"Teleport", "Smooth"}, "Teleport");
        this.zitterSpeed = new FloatValue("ZitterSpeed", 0.13f, 0.1f, 0.3f);
        this.zitterStrength = new FloatValue("ZitterStrength", 0.072f, 0.05f, 0.2f);
        this.zitterDelay = new IntegerValue("ZitterDelay", 100, 0, 500);
        this.timerValue = new FloatValue("Timer", 1.0f, 0.1f, 10.0f);
        this.speedModifierValue = new FloatValue("SpeedModifier", 1.0f, 0.0f, 2.0f);
        this.customSpeedValue = new BoolValue("CustomSpeed", false);
        this.customMoveSpeedValue = new FloatValue("CustomMoveSpeed", 0.3f, 0.0f, 5.0f);
        this.sameYValue = new BoolValue("SameY", false);
        this.autoJumpValue = new BoolValue("AutoJump", false);
        this.smartSpeedValue = new BoolValue("SmartSpeed", false);
        this.safeWalkValue = new BoolValue("SafeWalk", true);
        this.airSafeValue = new BoolValue("AirSafe", false);
        this.autoDisableSpeedValue = new BoolValue("AutoDisable-Speed", true);
        this.counterDisplayValue = new ListValue("Counter", new String[]{"Off", "Simple", "Advanced", "Sigma", "Novoline"}, "Simple");
        this.markValue = new BoolValue("Mark", false);
        this.redValue = new IntegerValue("Red", 0, 0, 255);
        this.greenValue = new IntegerValue("Green", 120, 0, 255);
        this.blueValue = new IntegerValue("Blue", 255, 0, 255);
        this.alphaValue = new IntegerValue("Alpha", 120, 0, 255);
        this.delayTimer = new MSTimer();
        this.zitterTimer = new MSTimer();
        this.timer = new TickTimer();
    }

    @EventTarget
    public void onJump(JumpEvent jumpEvent) {
        if (this != false) {
            jumpEvent.cancelEvent();
        }
    }
}

