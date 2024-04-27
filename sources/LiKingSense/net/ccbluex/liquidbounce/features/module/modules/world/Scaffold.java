/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.world;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.enums.BlockType;
import net.ccbluex.liquidbounce.api.enums.EnumFacingType;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemBlock;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketHeldItemChange;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
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
import net.ccbluex.liquidbounce.features.module.modules.world.Timer;
import net.ccbluex.liquidbounce.utils.InventoryUtils;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.block.PlaceInfo;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;

@ModuleInfo(name="Scaffold", description="\u4fee\u590d\u7248", category=ModuleCategory.WORLD, keyBind=34)
public class Scaffold
extends Module {
    public final ListValue modeValue;
    public final IntegerValue maxDelayValue;
    public final IntegerValue minDelayValue;
    public final BoolValue placeableDelay;
    public final ListValue autoBlockValue;
    public final BoolValue sprintValue;
    public final BoolValue swingValue;
    public final BoolValue searchValue;
    public final BoolValue downValue;
    public final BoolValue picker;
    public final ListValue placeModeValue;
    public final ListValue eagleValue;
    public final IntegerValue blocksToEagleValue;
    public final FloatValue edgeDistanceValue;
    public final IntegerValue expandLengthValue;
    public final BoolValue rotationStrafeValue;
    public final ListValue rotationModeValue;
    public final BoolValue silentRotation;
    public final BoolValue keepRotationValue;
    public final IntegerValue keepLengthValue;
    public final FloatValue staticPitchValue;
    public final FloatValue staticYawOffsetValue;
    public final FloatValue xzRangeValue;
    public final FloatValue yRangeValue;
    public final IntegerValue searchAccuracyValue;
    public final FloatValue maxTurnSpeedValue;
    public final FloatValue minTurnSpeedValue;
    public final BoolValue zitterValue;
    public final ListValue zitterModeValue;
    public final FloatValue zitterSpeed;
    public final FloatValue zitterStrength;
    public final FloatValue timerValue;
    public final FloatValue speedModifierValue;
    public final BoolValue slowValue;
    public final FloatValue slowSpeed;
    public long lastMS;
    public float progress;
    public final BoolValue sameYValue;
    public final BoolValue smartSpeedValue;
    public final BoolValue autoJumpValue;
    public final BoolValue safeWalkValue;
    public final BoolValue airSafeValue;
    public final BoolValue counterDisplayValue;
    public final BoolValue markValue;
    public final BoolValue autoTimer;
    public PlaceInfo targetPlace;
    public int launchY;
    public Rotation lockRotation;
    public Rotation limitedRotation;
    public boolean facesBlock;
    public int slot;
    public boolean zitterDirection;
    public final MSTimer delayTimer;
    public final MSTimer zitterTimer;
    public long delay;
    public int placedBlocksWithoutEagle;
    public boolean eagleSneaking;
    public boolean shouldGoDown;

    /*
     * Exception decompiling
     */
    public Scaffold() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl33 : PUTFIELD - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
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
    public void onEnable() {
        if (mc.getThePlayer() == null) {
            return;
        }
        if (((Boolean)this.autoTimer.get()).booleanValue()) {
            Timer timer = (Timer)LiquidBounce.INSTANCE.getModuleManager().getModule(Timer.class);
            timer.setState(true);
        }
        this.progress = 0.0f;
        this.launchY = (int)mc.getThePlayer().getPosY();
    }

    /*
     * WARNING - void declaration
     */
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (this.getBlocksAmount() == 0) {
            this.toggle();
        }
        this.getBestBlocks();
        mc.getTimer().setTimerSpeed(((Float)this.timerValue.get()).floatValue());
        this.shouldGoDown = (Boolean)this.downValue.get() != false && (Boolean)this.sameYValue.get() == false && mc.getGameSettings().getKeyBindSneak().isKeyDown() && this.getBlocksAmount() > 1 ? 1 : 0;
        int n = this.shouldGoDown ? 1 : 0;
        if (this.shouldGoDown) {
            mc.getGameSettings().getKeyBindSneak().setPressed(false);
        }
        if (((Boolean)this.slowValue.get()).booleanValue()) {
            mc.getThePlayer().setMotionX(mc.getThePlayer().getMotionX() * (double)((Float)this.slowSpeed.get()).floatValue());
            mc.getThePlayer().setMotionZ(mc.getThePlayer().getMotionZ() * (double)((Float)this.slowSpeed.get()).floatValue());
        }
        if (((Boolean)this.sprintValue.get()).booleanValue()) {
            if (!mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindSprint())) {
                mc.getGameSettings().getKeyBindSprint().setPressed(false);
            }
            if (mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindSprint())) {
                mc.getGameSettings().getKeyBindSprint().setPressed(true);
            }
            if (mc.getGameSettings().getKeyBindSprint().isKeyDown()) {
                mc.getThePlayer().setSprinting(true);
            }
            if (!mc.getGameSettings().getKeyBindSprint().isKeyDown()) {
                mc.getThePlayer().setSprinting(false);
            }
        }
        if (mc.getThePlayer().getOnGround()) {
            String mode = (String)this.modeValue.get();
            if (mode.equalsIgnoreCase("Rewinside")) {
                MovementUtils.strafe(0.2f);
                mc.getThePlayer().setMotionY(0.0);
            }
            if (((Boolean)this.zitterValue.get()).booleanValue() && ((String)this.zitterModeValue.get()).equalsIgnoreCase("smooth")) {
                if (!mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindRight())) {
                    mc.getGameSettings().getKeyBindRight().setPressed(false);
                }
                if (!mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindLeft())) {
                    mc.getGameSettings().getKeyBindLeft().setPressed(false);
                }
                if (this.zitterTimer.hasTimePassed(204L - 347L + 22L - 1L + 222L)) {
                    this.zitterDirection = !this.zitterDirection ? 1 : 0;
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
            if (!((String)this.eagleValue.get()).equalsIgnoreCase("Off") && !this.shouldGoDown) {
                double dif = 0.5;
                if (((String)this.eagleValue.get()).equalsIgnoreCase("EdgeDistance") && !this.shouldGoDown) {
                    void i;
                    while (i < 4) {
                        switch (i) {
                            case 0: {
                                double calcDif;
                                WBlockPos blockPos = new WBlockPos(mc.getThePlayer().getPosX() - 1.0, mc.getThePlayer().getPosY() - (mc.getThePlayer().getPosY() == (double)((int)mc.getThePlayer().getPosY()) + 0.5 ? 0.0 : 1.0), mc.getThePlayer().getPosZ());
                                PlaceInfo placeInfo = PlaceInfo.get(blockPos);
                                if (BlockUtils.isReplaceable(blockPos) && placeInfo != null) {
                                    calcDif = mc.getThePlayer().getPosX() - (double)blockPos.getX();
                                    if ((calcDif -= 0.5) < 0.0) {
                                        calcDif *= -1.0;
                                    }
                                    if ((calcDif -= 0.5) < dif) {
                                        dif = calcDif;
                                    }
                                }
                            }
                            case 1: {
                                double calcDif;
                                WBlockPos blockPos = new WBlockPos(mc.getThePlayer().getPosX() + 1.0, mc.getThePlayer().getPosY() - (mc.getThePlayer().getPosY() == (double)((int)mc.getThePlayer().getPosY()) + 0.5 ? 0.0 : 1.0), mc.getThePlayer().getPosZ());
                                PlaceInfo placeInfo = PlaceInfo.get(blockPos);
                                if (BlockUtils.isReplaceable(blockPos) && placeInfo != null) {
                                    calcDif = mc.getThePlayer().getPosX() - (double)blockPos.getX();
                                    if ((calcDif -= 0.5) < 0.0) {
                                        calcDif *= -1.0;
                                    }
                                    if ((calcDif -= 0.5) < dif) {
                                        dif = calcDif;
                                    }
                                }
                            }
                            case 2: {
                                double calcDif;
                                WBlockPos blockPos = new WBlockPos(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY() - (mc.getThePlayer().getPosY() == (double)((int)mc.getThePlayer().getPosY()) + 0.5 ? 0.0 : 1.0), mc.getThePlayer().getPosZ() - 1.0);
                                PlaceInfo placeInfo = PlaceInfo.get(blockPos);
                                if (BlockUtils.isReplaceable(blockPos) && placeInfo != null) {
                                    calcDif = mc.getThePlayer().getPosZ() - (double)blockPos.getZ();
                                    if ((calcDif -= 0.5) < 0.0) {
                                        calcDif *= -1.0;
                                    }
                                    if ((calcDif -= 0.5) < dif) {
                                        dif = calcDif;
                                    }
                                }
                            }
                            case 3: {
                                WBlockPos blockPos = new WBlockPos(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY() - (mc.getThePlayer().getPosY() == (double)((int)mc.getThePlayer().getPosY()) + 0.5 ? 0.0 : 1.0), mc.getThePlayer().getPosZ() + 1.0);
                                PlaceInfo placeInfo = PlaceInfo.get(blockPos);
                                if (!BlockUtils.isReplaceable(blockPos) || placeInfo == null) break;
                                double calcDif = mc.getThePlayer().getPosZ() - (double)blockPos.getZ();
                                if ((calcDif -= 0.5) < 0.0) {
                                    calcDif *= -1.0;
                                }
                                if (!((calcDif -= 0.5) < dif)) break;
                                dif = calcDif;
                            }
                        }
                        ++i;
                    }
                }
                if (this.placedBlocksWithoutEagle >= (Integer)this.blocksToEagleValue.get()) {
                    boolean shouldEagle;
                    boolean bl = shouldEagle = mc.getTheWorld().getBlockState(new WBlockPos(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY() - 1.0, mc.getThePlayer().getPosZ())).getBlock().equals(classProvider.getBlockEnum(BlockType.AIR)) || dif < (double)((Float)this.edgeDistanceValue.get()).floatValue() && ((String)this.eagleValue.get()).equalsIgnoreCase("EdgeDistance");
                    if (((String)this.eagleValue.get()).equalsIgnoreCase("Silent") && !this.shouldGoDown) {
                        if (this.eagleSneaking != shouldEagle) {
                            mc.getNetHandler().addToSendQueue(classProvider.createCPacketEntityAction(mc.getThePlayer(), shouldEagle ? ICPacketEntityAction.WAction.START_SNEAKING : ICPacketEntityAction.WAction.STOP_SNEAKING));
                        }
                        this.eagleSneaking = shouldEagle;
                    } else {
                        mc.getGameSettings().getKeyBindSneak().setPressed(shouldEagle);
                    }
                    this.placedBlocksWithoutEagle = 0;
                } else {
                    ++this.placedBlocksWithoutEagle;
                }
            }
            if (((Boolean)this.zitterValue.get()).booleanValue() && ((String)this.zitterModeValue.get()).equalsIgnoreCase("teleport")) {
                MovementUtils.strafe(((Float)this.zitterSpeed.get()).floatValue());
                double yaw = Math.toRadians((double)mc.getThePlayer().getRotationYaw() + (this.zitterDirection ? 90.0 : -90.0));
                mc.getThePlayer().setMotionX(mc.getThePlayer().getMotionX() - Math.sin(yaw) * (double)((Float)this.zitterStrength.get()).floatValue());
                mc.getThePlayer().setMotionZ(mc.getThePlayer().getMotionZ() + Math.cos(yaw) * (double)((Float)this.zitterStrength.get()).floatValue());
                this.zitterDirection = !this.zitterDirection ? 1 : 0;
                int n2 = this.zitterDirection ? 1 : 0;
            }
        }
        if (this.shouldGoDown) {
            this.launchY = (int)mc.getThePlayer().getPosY() - 1;
        } else if (!((Boolean)this.sameYValue.get()).booleanValue()) {
            mc.getThePlayer().jump();
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (mc.getThePlayer() == null) {
            return;
        }
        IPacket packet = event.getPacket();
        if (classProvider.isCPacketHeldItemChange(packet)) {
            ICPacketHeldItemChange packetHeldItemChange = packet.asCPacketHeldItemChange();
            this.slot = packetHeldItemChange.getSlotId();
        }
    }

    @EventTarget
    public void onStrafe(StrafeEvent event) {
        if (!((Boolean)this.rotationStrafeValue.get()).booleanValue()) {
            return;
        }
        RotationUtils.serverRotation.applyStrafeToPlayer(event);
        event.cancelEvent();
    }

    @EventTarget
    public void onMotion(MotionEvent event) {
        EventState eventState = event.getEventState();
        if (!((String)this.rotationModeValue.get()).equalsIgnoreCase("Off") && ((Boolean)this.keepRotationValue.get()).booleanValue() && this.lockRotation != null) {
            this.setRotation(this.lockRotation);
        }
        if ((this.facesBlock || ((String)this.rotationModeValue.get()).equalsIgnoreCase("Off")) && ((String)this.placeModeValue.get()).equalsIgnoreCase(eventState.getStateName())) {
            this.place();
        }
        if (eventState == EventState.PRE) {
            this.update();
        }
        if (this.targetPlace == null && ((Boolean)this.placeableDelay.get()).booleanValue()) {
            this.delayTimer.reset();
        }
    }

    public void update() {
        int isHeldItemBlock;
        int n = isHeldItemBlock = mc.getThePlayer().getHeldItem() != null && classProvider.isItemBlock(mc.getThePlayer().getHeldItem().getItem()) ? 1 : 0;
        if (!((String)this.autoBlockValue.get()).equalsIgnoreCase("Off") ? InventoryUtils.findAutoBlockBlock() == -1 && isHeldItemBlock == 0 : isHeldItemBlock == 0) {
            return;
        }
        this.findBlock(((String)this.modeValue.get()).equalsIgnoreCase("expand"));
    }

    public void setRotation(Rotation rotation, int keepRotation) {
        if (((Boolean)this.silentRotation.get()).booleanValue()) {
            RotationUtils.setTargetRotation(rotation, keepRotation);
        } else {
            mc.getThePlayer().setRotationYaw(rotation.getYaw());
            mc.getThePlayer().setRotationPitch(rotation.getPitch());
        }
    }

    public void setRotation(Rotation rotation) {
        this.setRotation(rotation, 0);
    }

    /*
     * WARNING - void declaration
     */
    public void findBlock(boolean expand) {
        block7: {
            void x;
            WBlockPos blockPosition;
            block6: {
                int i;
                WBlockPos wBlockPos = this.shouldGoDown ? (mc.getThePlayer().getPosY() == (double)((int)mc.getThePlayer().getPosY()) + 0.5 ? new WBlockPos(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY() - 0.6, mc.getThePlayer().getPosZ()) : new WBlockPos(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY() - 0.6, mc.getThePlayer().getPosZ()).down()) : ((Boolean)this.sameYValue.get() != false || ((Boolean)this.autoJumpValue.get() != false || (Boolean)this.smartSpeedValue.get() != false && LiquidBounce.moduleManager.getModule(Speed.class).getState()) && !mc.getGameSettings().getKeyBindJump().isKeyDown() && (double)this.launchY <= mc.getThePlayer().getPosY() ? new WBlockPos(mc.getThePlayer().getPosX(), (double)(this.launchY - 1), mc.getThePlayer().getPosZ()) : (blockPosition = mc.getThePlayer().getPosY() == (double)((int)mc.getThePlayer().getPosY()) + 0.5 ? new WBlockPos(mc.getThePlayer()) : new WBlockPos(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY(), mc.getThePlayer().getPosZ()).down()));
                if (!(expand || BlockUtils.isReplaceable(blockPosition) && !this.search(blockPosition, !this.shouldGoDown))) {
                    return;
                }
                if (!expand) break block6;
                while (i < (Integer)this.expandLengthValue.get()) {
                    if (this.search(blockPosition.add(mc.getThePlayer().getHorizontalFacing().equals(classProvider.getEnumFacing(EnumFacingType.WEST)) ? -i : (mc.getThePlayer().getHorizontalFacing().equals(classProvider.getEnumFacing(EnumFacingType.EAST)) ? i : 0), 0, mc.getThePlayer().getHorizontalFacing().equals(classProvider.getEnumFacing(EnumFacingType.NORTH)) ? -i : (mc.getThePlayer().getHorizontalFacing().equals(classProvider.getEnumFacing(EnumFacingType.SOUTH)) ? i : 0)), false)) {
                        return;
                    }
                    ++i;
                }
                break block7;
            }
            if (!((Boolean)this.searchValue.get()).booleanValue()) break block7;
            while (x <= 1) {
                void z;
                while (z <= 1) {
                    if (this.search(blockPosition.add((int)x, 0, (int)z), !this.shouldGoDown)) {
                        return;
                    }
                    ++z;
                }
                ++x;
            }
        }
    }

    public void place() {
        if (this.targetPlace == null) {
            if (((Boolean)this.placeableDelay.get()).booleanValue()) {
                this.delayTimer.reset();
            }
            return;
        }
        if (!this.delayTimer.hasTimePassed(this.delay) || ((Boolean)this.sameYValue.get()).booleanValue() && this.launchY - 1 != (int)this.targetPlace.getVec3().getYCoord()) {
            return;
        }
        IItemStack itemStack = mc.getThePlayer().getHeldItem();
        if (itemStack == null || !classProvider.isItemBlock(itemStack.getItem()) || classProvider.isBlockBush(itemStack.getItem().asItemBlock().getBlock()) || mc.getThePlayer().getHeldItem().getStackSize() <= 0) {
            if (((String)this.autoBlockValue.get()).equalsIgnoreCase("Off")) {
                return;
            }
            int blockSlot = InventoryUtils.findAutoBlockBlock();
            if (blockSlot == -1) {
                return;
            }
            if (((String)this.autoBlockValue.get()).equalsIgnoreCase("Pick")) {
                mc.getNetHandler().addToSendQueue(classProvider.createCPacketHeldItemChange(blockSlot - 36));
            }
            mc.getPlayerController().updateController();
            if (((String)this.autoBlockValue.get()).equalsIgnoreCase("Spoof")) {
                if (blockSlot - 36 != this.slot) {
                    mc.getNetHandler().addToSendQueue(classProvider.createCPacketHeldItemChange(blockSlot - 36));
                }
            } else if (((String)this.autoBlockValue.get()).equalsIgnoreCase("Switch")) {
                mc.getThePlayer().getInventory().setCurrentItem(blockSlot - 36);
                mc.getPlayerController().updateController();
            } else {
                mc.getThePlayer().getInventory().setCurrentItem(blockSlot - 36);
                mc.getPlayerController().updateController();
            }
            itemStack = mc.getThePlayer().getInventoryContainer().getSlot(blockSlot).getStack();
        }
        if (mc.getPlayerController().onPlayerRightClick(mc.getThePlayer(), mc.getTheWorld(), itemStack, this.targetPlace.getBlockPos(), this.targetPlace.getEnumFacing(), this.targetPlace.getVec3())) {
            this.delayTimer.reset();
            this.delay = TimeUtils.randomDelay((Integer)this.minDelayValue.get(), (Integer)this.maxDelayValue.get());
            if (mc.getThePlayer().getOnGround()) {
                float modifier = ((Float)this.speedModifierValue.get()).floatValue();
                mc.getThePlayer().setMotionX(mc.getThePlayer().getMotionX() * (double)modifier);
                mc.getThePlayer().setMotionZ(mc.getThePlayer().getMotionZ() * (double)modifier);
            }
            if (((Boolean)this.swingValue.get()).booleanValue()) {
                mc.getThePlayer().swingItem();
            } else {
                mc.getNetHandler().addToSendQueue(classProvider.createCPacketAnimation());
            }
        }
        this.targetPlace = null;
    }

    @Override
    public void onDisable() {
        if (mc.getThePlayer() == null) {
            return;
        }
        if (!mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindSneak())) {
            mc.getGameSettings().getKeyBindSneak().setPressed(false);
            if (this.eagleSneaking) {
                mc.getNetHandler().addToSendQueue(classProvider.createCPacketEntityAction(mc.getThePlayer(), ICPacketEntityAction.WAction.STOP_SNEAKING));
            }
        }
        if (!mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindRight())) {
            mc.getGameSettings().getKeyBindRight().setPressed(false);
        }
        if (!mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindLeft())) {
            mc.getGameSettings().getKeyBindLeft().setPressed(false);
        }
        if (((Boolean)this.autoTimer.get()).booleanValue()) {
            Timer timer = (Timer)LiquidBounce.INSTANCE.getModuleManager().getModule(Timer.class);
            timer.setState(false);
        }
        this.lockRotation = null;
        this.limitedRotation = null;
        this.facesBlock = 0;
        mc.getTimer().setTimerSpeed(1.0f);
        this.shouldGoDown = 0;
        if (this.slot != mc.getThePlayer().getInventory().getCurrentItem()) {
            mc.getNetHandler().addToSendQueue(classProvider.createCPacketHeldItemChange(mc.getThePlayer().getInventory().getCurrentItem()));
        }
    }

    @EventTarget
    public void onMove(MoveEvent event) {
        if (!((Boolean)this.safeWalkValue.get()).booleanValue() || this.shouldGoDown) {
            return;
        }
        if (((Boolean)this.airSafeValue.get()).booleanValue() || mc.getThePlayer().getOnGround()) {
            event.setSafeWalk(true);
        }
    }

    /*
     * Exception decompiling
     */
    @EventTarget
    public void onRender2D(Render2DEvent event) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl204 : RETURN - null : trying to set 0 previously set to 1
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

    /*
     * Exception decompiling
     */
    @EventTarget
    public void onRender3D(Render3DEvent event) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl125 : RETURN - null : trying to set 0 previously set to 4
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

    /*
     * Exception decompiling
     */
    public boolean search(WBlockPos blockPosition, boolean checks) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl164 : DLOAD - null : trying to set 4 previously set to 0
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

    public double calcStepSize(double range) {
        double accuracy = ((Integer)this.searchAccuracyValue.get()).intValue();
        if (range / (accuracy += accuracy % 2.0) < 0.01) {
            return 0.01;
        }
        return range / accuracy;
    }

    /*
     * WARNING - void declaration
     */
    public int getBlocksAmount() {
        void amount;
        void i;
        while (i < 45) {
            IItemStack itemStack = mc.getThePlayer().getInventoryContainer().getSlot((int)i).getStack();
            if (itemStack != null && classProvider.isItemBlock(itemStack.getItem())) {
                IBlock block = itemStack.getItem().asItemBlock().getBlock();
                IItemStack heldItem = mc.getThePlayer().getHeldItem();
                if (heldItem != null && heldItem.equals(itemStack) || !InventoryUtils.BLOCK_BLACKLIST.contains(block) && !classProvider.isBlockBush(block)) {
                    amount += itemStack.getStackSize();
                }
            }
            ++i;
        }
        return (int)amount;
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }

    /*
     * WARNING - void declaration
     */
    public void getBestBlocks() {
        if (this.getBlocksAmount() == 0) {
            return;
        }
        if (((Boolean)this.picker.get()).booleanValue()) {
            void spoofSlot;
            int bestSlot;
            int bestInvSlot = this.getBiggestBlockSlotInv();
            int bestHotbarSlot = this.getBiggestBlockSlotHotbar();
            int n = bestSlot = this.getBiggestBlockSlotHotbar() > 0 ? this.getBiggestBlockSlotHotbar() : this.getBiggestBlockSlotInv();
            if (bestHotbarSlot > 0 && bestInvSlot > 0 && mc.getThePlayer().getInventoryContainer().getSlot(bestInvSlot).getHasStack() && mc.getThePlayer().getInventoryContainer().getSlot(bestHotbarSlot).getHasStack() && mc.getThePlayer().getInventoryContainer().getSlot(bestHotbarSlot).getStack().getStackSize() < mc.getThePlayer().getInventoryContainer().getSlot(bestInvSlot).getStack().getStackSize()) {
                bestSlot = bestInvSlot;
            }
            if (this.hotbarContainBlock()) {
                void a;
                while (a < 45) {
                    IItem item;
                    if (mc.getThePlayer().getInventoryContainer().getSlot((int)a).getHasStack() && (item = mc.getThePlayer().getInventoryContainer().getSlot((int)a).getStack().getItem()) instanceof IItemBlock) {
                        spoofSlot = a;
                        break;
                    }
                    ++a;
                }
            } else {
                void a;
                while (a < 45) {
                    if (!mc.getThePlayer().getInventoryContainer().getSlot((int)a).getHasStack()) {
                        spoofSlot = a;
                        break;
                    }
                    ++a;
                }
            }
            if (mc.getThePlayer().getInventoryContainer().getSlot((int)spoofSlot).getSlotNumber() != bestSlot) {
                this.swap(bestSlot, (int)(spoofSlot - 36));
                mc.getPlayerController().updateController();
            }
        } else if (this.invCheck()) {
            void i;
            IItemStack is = (IItemStack)((Object)functions.getItemById(261));
            while (i < 36) {
                IItem item;
                if (mc.getThePlayer().getInventoryContainer().getSlot((int)i).getHasStack() && (item = mc.getThePlayer().getInventoryContainer().getSlot((int)i).getStack().getItem()) instanceof IItemBlock) {
                    void count;
                    void a;
                    while (a < 45) {
                        if (functions.canAddItemToSlot(mc.getThePlayer().getInventoryContainer().getSlot((int)a), is, true)) {
                            this.swap((int)i, (int)(a - 36));
                            ++count;
                            break;
                        }
                        ++a;
                    }
                    if (count != false) break;
                    this.swap((int)i, 7);
                    break;
                }
                ++i;
            }
        }
    }

    /*
     * WARNING - void declaration
     */
    public boolean hotbarContainBlock() {
        void i;
        while (i < 45) {
            try {
                IItemStack stack = mc.getThePlayer().getInventoryContainer().getSlot((int)i).getStack();
                if (stack == null || stack.getItem() == null || !(stack.getItem() instanceof IItemBlock)) {
                    ++i;
                    continue;
                }
                return true;
            }
            catch (Exception exception) {
            }
        }
        return false;
    }

    /*
     * WARNING - void declaration
     */
    public int getBiggestBlockSlotHotbar() {
        void slot;
        void i;
        if (this.getBlocksAmount() == 0) {
            return -1;
        }
        while (i < 45) {
            if (mc.getThePlayer().getInventoryContainer().getSlot((int)i).getHasStack()) {
                int size;
                IItem item = mc.getThePlayer().getInventoryContainer().getSlot((int)i).getStack().getItem();
                IItemStack is = mc.getThePlayer().getInventoryContainer().getSlot((int)i).getStack();
                if (item instanceof IItemBlock && is.getStackSize() > size) {
                    size = is.getStackSize();
                    slot = i;
                }
            }
            ++i;
        }
        return (int)slot;
    }

    public protected void swap(int slot, int hotbarNum) {
        mc.getPlayerController().windowClick(mc.getThePlayer().getInventoryContainer().getWindowId(), slot, hotbarNum, 2, mc.getThePlayer());
    }

    /*
     * WARNING - void declaration
     */
    public boolean invCheck() {
        void i;
        while (i < 45) {
            IItem item;
            if (mc.getThePlayer().getInventoryContainer().getSlot((int)i).getHasStack() && (item = mc.getThePlayer().getInventoryContainer().getSlot((int)i).getStack().getItem()) instanceof IItemBlock) {
                return false;
            }
            ++i;
        }
        return true;
    }

    /*
     * WARNING - void declaration
     */
    public int getBiggestBlockSlotInv() {
        void slot;
        void i;
        if (this.getBlocksAmount() == 0) {
            return -1;
        }
        while (i < 36) {
            if (mc.getThePlayer().getInventoryContainer().getSlot((int)i).getHasStack()) {
                int size;
                IItem item = mc.getThePlayer().getInventoryContainer().getSlot((int)i).getStack().getItem();
                IItemStack is = mc.getThePlayer().getInventoryContainer().getSlot((int)i).getStack();
                if (item instanceof IItemBlock && is.getStackSize() > size) {
                    size = is.getStackSize();
                    slot = i;
                }
            }
            ++i;
        }
        return (int)slot;
    }
}

