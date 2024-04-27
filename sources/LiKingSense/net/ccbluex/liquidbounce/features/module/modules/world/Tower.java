/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.math.MathKt
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.features.module.modules.world;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.enums.StatType;
import net.ccbluex.liquidbounce.api.minecraft.client.IMinecraft;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.util.IScaledResolution;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.JumpEvent;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.InventoryUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
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
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="Tower", description="Automatically builds a tower beneath you.", category=ModuleCategory.WORLD, keyBind=24)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u0084\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010+\u001a\u00020,H\u0002J\b\u0010-\u001a\u00020,H\u0002J\b\u0010.\u001a\u00020,H\u0016J\u0010\u0010/\u001a\u00020,2\u0006\u00100\u001a\u000201H\u0007J\u0010\u00102\u001a\u00020,2\u0006\u00100\u001a\u000203H\u0007J\u0010\u00104\u001a\u00020,2\u0006\u00100\u001a\u000205H\u0007J\u0010\u00106\u001a\u00020,2\u0006\u00100\u001a\u000207H\u0007J\b\u00108\u001a\u00020,H\u0002J\u0010\u00109\u001a\u00020:2\u0006\u0010;\u001a\u00020<H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u00020\u00068BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010 \u001a\u00020!8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\"\u0010#R\u000e\u0010$\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020)X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006="}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/world/Tower;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "autoBlockValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "blocksAmount", "", "getBlocksAmount", "()I", "constantMotionJumpGroundValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "constantMotionValue", "counterDisplayValue", "jumpDelayValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "jumpGround", "", "jumpMotionValue", "keepRotationValue", "lockRotation", "Lnet/ccbluex/liquidbounce/utils/Rotation;", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "onJumpValue", "placeInfo", "Lnet/ccbluex/liquidbounce/utils/block/PlaceInfo;", "placeModeValue", "rotationsValue", "slot", "stayAutoBlock", "stopWhenBlockAbove", "swingValue", "tag", "", "getTag", "()Ljava/lang/String;", "teleportDelayValue", "teleportGroundValue", "teleportHeightValue", "teleportNoMotionValue", "timer", "Lnet/ccbluex/liquidbounce/utils/timer/TickTimer;", "timerValue", "fakeJump", "", "move", "onDisable", "onJump", "event", "Lnet/ccbluex/liquidbounce/event/JumpEvent;", "onMotion", "Lnet/ccbluex/liquidbounce/event/MotionEvent;", "onPacket", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onRender2D", "Lnet/ccbluex/liquidbounce/event/Render2DEvent;", "place", "search", "", "blockPosition", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "LiKingSense"})
public final class Tower
extends Module {
    public final ListValue modeValue;
    public final BoolValue autoBlockValue;
    public final BoolValue stayAutoBlock;
    public final BoolValue swingValue;
    public final BoolValue stopWhenBlockAbove;
    public final BoolValue rotationsValue;
    public final BoolValue keepRotationValue;
    public final BoolValue onJumpValue;
    public final ListValue placeModeValue;
    public final FloatValue timerValue;
    public final FloatValue jumpMotionValue;
    public final IntegerValue jumpDelayValue;
    public final FloatValue constantMotionValue;
    public final FloatValue constantMotionJumpGroundValue;
    public final FloatValue teleportHeightValue;
    public final IntegerValue teleportDelayValue;
    public final BoolValue teleportGroundValue;
    public final BoolValue teleportNoMotionValue;
    public final BoolValue counterDisplayValue;
    public PlaceInfo placeInfo;
    public Rotation lockRotation;
    public final TickTimer timer;
    public double jumpGround;
    public int slot;

    @Override
    public void onDisable() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
        this.lockRotation = null;
        if (this.slot != thePlayer.getInventory().getCurrentItem()) {
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(thePlayer.getInventory().getCurrentItem()));
        }
    }

    @EventTarget
    public final void onMotion(@NotNull MotionEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        if (((Boolean)this.onJumpValue.get()).booleanValue() && !MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown()) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (((Boolean)this.rotationsValue.get()).booleanValue() && ((Boolean)this.keepRotationValue.get()).booleanValue() && this.lockRotation != null) {
            RotationUtils.setTargetRotation(this.lockRotation);
        }
        MinecraftInstance.mc.getTimer().setTimerSpeed(((Number)this.timerValue.get()).floatValue());
        EventState eventState = event.getEventState();
        if (StringsKt.equals((String)((String)this.placeModeValue.get()), (String)eventState.getStateName(), (boolean)true)) {
            this.place();
        }
        if (eventState == EventState.PRE) {
            int update;
            this.placeInfo = null;
            this.timer.update();
            int n = ((Boolean)this.autoBlockValue.get()).booleanValue() ? (InventoryUtils.findAutoBlockBlock() != -1 || thePlayer.getHeldItem() != null && MinecraftInstance.classProvider.isItemBlock(thePlayer.getHeldItem().getItem()) ? 1 : 0) : (update = thePlayer.getHeldItem() != null && MinecraftInstance.classProvider.isItemBlock(thePlayer.getHeldItem().getItem()) ? 1 : 0);
            if (update != 0) {
                VecRotation vecRotation;
                if (!((Boolean)this.stopWhenBlockAbove.get()).booleanValue() || MinecraftInstance.classProvider.isBlockAir(BlockUtils.getBlock(new WBlockPos(thePlayer.getPosX(), thePlayer.getPosY() + (double)2, thePlayer.getPosZ())))) {
                    this.move();
                }
                WBlockPos blockPos = new WBlockPos(thePlayer.getPosX(), thePlayer.getPosY() - 1.0, thePlayer.getPosZ());
                if (MinecraftInstance.classProvider.isBlockAir(MinecraftInstance.mc.getTheWorld().getBlockState(blockPos).getBlock()) && this.search(blockPos) && ((Boolean)this.rotationsValue.get()).booleanValue() && (vecRotation = RotationUtils.faceBlock(blockPos)) != null) {
                    RotationUtils.setTargetRotation(vecRotation.getRotation());
                    this.placeInfo.setVec3(vecRotation.getVec());
                }
            }
        }
    }

    public final void fakeJump() {
        MinecraftInstance.mc.getThePlayer().setAirBorne(true);
        MinecraftInstance.mc.getThePlayer().triggerAchievement(MinecraftInstance.classProvider.getStatEnum(StatType.JUMP_STAT));
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public final void move() {
        IEntityPlayerSP thePlayer;
        block23: {
            block18: {
                block21: {
                    block20: {
                        block22: {
                            block19: {
                                String string;
                                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                                if (iEntityPlayerSP == null) {
                                    return;
                                }
                                thePlayer = iEntityPlayerSP;
                                String string2 = string = (String)this.modeValue.get();
                                if (string2 == null) {
                                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                                }
                                String string3 = string2.toLowerCase();
                                Intrinsics.checkExpressionValueIsNotNull((Object)string3, (String)"(this as java.lang.String).toLowerCase()");
                                string = string3;
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
                                        if (!string.equals("jump") || !thePlayer.getOnGround() || !this.timer.hasTimePassed(((Number)this.jumpDelayValue.get()).intValue())) return;
                                        this.fakeJump();
                                        thePlayer.setMotionY(((Number)this.jumpMotionValue.get()).floatValue());
                                        this.timer.reset();
                                        return;
                                    }
                                }
                                if (thePlayer.getOnGround()) {
                                    this.fakeJump();
                                    thePlayer.setMotionY(0.42);
                                    return;
                                }
                                if (!(thePlayer.getMotionY() < 0.1)) return;
                                thePlayer.setMotionY(-0.3);
                                return;
                            }
                            if (thePlayer.getOnGround()) {
                                this.fakeJump();
                                thePlayer.setMotionY(0.42);
                                return;
                            }
                            if (!(thePlayer.getMotionY() < 0.23)) return;
                            thePlayer.setPosition(thePlayer.getPosX(), MathKt.truncate((double)thePlayer.getPosY()), thePlayer.getPosZ());
                            return;
                        }
                        if (!thePlayer.getOnGround() || !this.timer.hasTimePassed(2)) return;
                        this.fakeJump();
                        MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(thePlayer.getPosX(), thePlayer.getPosY() + 0.42, thePlayer.getPosZ(), false));
                        MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(thePlayer.getPosX(), thePlayer.getPosY() + 0.753, thePlayer.getPosZ(), false));
                        thePlayer.setPosition(thePlayer.getPosX(), thePlayer.getPosY() + 1.0, thePlayer.getPosZ());
                        this.timer.reset();
                        return;
                    }
                    if (((Boolean)this.teleportNoMotionValue.get()).booleanValue()) {
                        thePlayer.setMotionY(0.0);
                    }
                    if (!thePlayer.getOnGround() && ((Boolean)this.teleportGroundValue.get()).booleanValue() || !this.timer.hasTimePassed(((Number)this.teleportDelayValue.get()).intValue())) return;
                    this.fakeJump();
                    thePlayer.setPositionAndUpdate(thePlayer.getPosX(), thePlayer.getPosY() + ((Number)this.teleportHeightValue.get()).doubleValue(), thePlayer.getPosZ());
                    this.timer.reset();
                    return;
                }
                if (thePlayer.getOnGround()) {
                    this.fakeJump();
                    this.jumpGround = thePlayer.getPosY();
                    thePlayer.setMotionY(((Number)this.constantMotionValue.get()).floatValue());
                }
                if (!(thePlayer.getPosY() > this.jumpGround + ((Number)this.constantMotionJumpGroundValue.get()).doubleValue())) return;
                this.fakeJump();
                thePlayer.setPosition(thePlayer.getPosX(), MathKt.truncate((double)thePlayer.getPosY()), thePlayer.getPosZ());
                thePlayer.setMotionY(((Number)this.constantMotionValue.get()).floatValue());
                this.jumpGround = thePlayer.getPosY();
                return;
            }
            if (thePlayer.getOnGround()) {
                this.fakeJump();
                thePlayer.setMotionY(0.4001);
            }
            MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
            if (!(thePlayer.getMotionY() < (double)0)) return;
            IEntityPlayerSP iEntityPlayerSP = thePlayer;
            iEntityPlayerSP.setMotionY(iEntityPlayerSP.getMotionY() - 9.45E-6);
            MinecraftInstance.mc.getTimer().setTimerSpeed(1.6f);
            return;
        }
        if (thePlayer.getTicksExisted() % 4 == 1) {
            thePlayer.setMotionY(0.4195464);
            thePlayer.setPosition(thePlayer.getPosX() - 0.035, thePlayer.getPosY(), thePlayer.getPosZ());
            return;
        }
        if (thePlayer.getTicksExisted() % 4 != 0) return;
        thePlayer.setMotionY(-0.5);
        thePlayer.setPosition(thePlayer.getPosX() + 0.035, thePlayer.getPosY(), thePlayer.getPosZ());
        return;
    }

    public final void place() {
        int blockSlot;
        if (this.placeInfo == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        IItemStack itemStack = thePlayer.getHeldItem();
        if (itemStack == null || !MinecraftInstance.classProvider.isItemBlock(itemStack.getItem())) {
            if (!((Boolean)this.autoBlockValue.get()).booleanValue()) {
                return;
            }
            blockSlot = InventoryUtils.findAutoBlockBlock();
            if (blockSlot == -1) {
                return;
            }
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(blockSlot - 36));
            itemStack = thePlayer.getInventoryContainer().getSlot(blockSlot).getStack();
        }
        if (MinecraftInstance.mc.getPlayerController().onPlayerRightClick(thePlayer, MinecraftInstance.mc.getTheWorld(), itemStack, this.placeInfo.getBlockPos(), this.placeInfo.getEnumFacing(), this.placeInfo.getVec3())) {
            if (((Boolean)this.swingValue.get()).booleanValue()) {
                thePlayer.swingItem();
            } else {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketAnimation());
            }
        }
        this.placeInfo = null;
        if (!((Boolean)this.stayAutoBlock.get()).booleanValue() && blockSlot >= 0) {
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(thePlayer.getInventory().getCurrentItem()));
        }
    }

    /*
     * Exception decompiling
     */
    public final boolean search(WBlockPos blockPosition) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl58 : INVOKESTATIC - null : Stack underflow
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

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        if (MinecraftInstance.mc.getThePlayer() == null) {
            return;
        }
        IPacket packet = event.getPacket();
        if (MinecraftInstance.classProvider.isCPacketHeldItemChange(packet)) {
            this.slot = packet.asCPacketHeldItemChange().getSlotId();
        }
    }

    @EventTarget
    public final void onRender2D(@NotNull Render2DEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        if (((Boolean)this.counterDisplayValue.get()).booleanValue()) {
            GL11.glPushMatrix();
            String info = "Blocks: \u00a77" + this.getBlocksAmount();
            IMinecraft iMinecraft = MinecraftInstance.mc;
            Intrinsics.checkExpressionValueIsNotNull((Object)iMinecraft, (String)"mc");
            IScaledResolution scaledResolution = MinecraftInstance.classProvider.createScaledResolution(iMinecraft);
            float f = (float)(scaledResolution.getScaledWidth() / 2) - (float)2;
            float f2 = (float)(scaledResolution.getScaledHeight() / 2) + (float)5;
            float f3 = (float)(scaledResolution.getScaledWidth() / 2 + Fonts.posterama40.getStringWidth(info)) + (float)2;
            float f4 = (float)(scaledResolution.getScaledHeight() / 2) + (float)16;
            Color color = Color.BLACK;
            Intrinsics.checkExpressionValueIsNotNull((Object)color, (String)"Color.BLACK");
            int n = color.getRGB();
            Color color2 = Color.BLACK;
            Intrinsics.checkExpressionValueIsNotNull((Object)color2, (String)"Color.BLACK");
            RenderUtils.drawBorderedRect(f, f2, f3, f4, 3.0f, n, color2.getRGB());
            MinecraftInstance.classProvider.getGlStateManager().resetColor();
            float f5 = (float)scaledResolution.getScaledWidth() / (float)2;
            float f6 = (float)(scaledResolution.getScaledHeight() / 2) + (float)7;
            Color color3 = Color.WHITE;
            Intrinsics.checkExpressionValueIsNotNull((Object)color3, (String)"Color.WHITE");
            Fonts.posterama40.drawString(info, f5, f6, color3.getRGB());
            GL11.glPopMatrix();
        }
    }

    @EventTarget
    public final void onJump(@NotNull JumpEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        if (((Boolean)this.onJumpValue.get()).booleanValue()) {
            event.cancelEvent();
        }
    }

    /*
     * Exception decompiling
     */
    public final int getBlocksAmount() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * java.lang.IllegalStateException: Invisible function parameters on a non-constructor (or reads of uninitialised local variables).
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.assignSSAIdentifiers(Op02WithProcessedDataAndRefs.java:1631)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.discoverStorageLiveness(Op02WithProcessedDataAndRefs.java:1871)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:461)
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
    @NotNull
    public String getTag() {
        return (String)this.modeValue.get();
    }

    /*
     * Exception decompiling
     */
    public Tower() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl134 : PUTFIELD - null : Stack underflow
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
}

