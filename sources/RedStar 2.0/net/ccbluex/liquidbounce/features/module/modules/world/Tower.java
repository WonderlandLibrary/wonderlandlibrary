package net.ccbluex.liquidbounce.features.module.modules.world;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.IClassProvider;
import net.ccbluex.liquidbounce.api.enums.EnumFacingType;
import net.ccbluex.liquidbounce.api.enums.StatType;
import net.ccbluex.liquidbounce.api.minecraft.block.material.IMaterial;
import net.ccbluex.liquidbounce.api.minecraft.block.state.IIBlockState;
import net.ccbluex.liquidbounce.api.minecraft.client.IMinecraft;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IPlayerControllerMP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.api.minecraft.util.IMovingObjectPosition;
import net.ccbluex.liquidbounce.api.minecraft.util.IScaledResolution;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WMathHelper;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
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
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
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
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="Tower", description="Automatically builds a tower beneath you.", category=ModuleCategory.WORLD, keyBind=24)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\u0000\n\b\n\b\n\n\b\n\n\u0000\n\n\u0000\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\n\n\b\n\n\u0000\n\n\u0000\b\u000020B¢J\b+0,HJ\b-0,HJ\b.0,HJ\b/0,HJ00,2102HJ30,2104HJ50,2106HJ70,2108HJ\b90,HJ:0;2<0=HR0X¢\n\u0000R08BX¢\b\bR\t0\nX¢\n\u0000R0\nX¢\n\u0000R\f0\rX¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0\nX¢\n\u0000R0\rX¢\n\u0000R0X¢\n\u0000R0\rX¢\n\u0000R0X¢\n\u0000R0\rX¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0\rX¢\n\u0000R0X¢\n\u0000R0\rX¢\n\u0000R0\rX¢\n\u0000R 0!8VX¢\b\"#R$0X¢\n\u0000R%0\rX¢\n\u0000R&0\nX¢\n\u0000R'0\rX¢\n\u0000R(0)X¢\n\u0000R*0\nX¢\n\u0000¨>"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/world/Tower;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "autoBlockValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "blocksAmount", "", "getBlocksAmount", "()I", "constantMotionJumpGroundValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "constantMotionValue", "counterDisplayValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "jumpDelayValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "jumpGround", "", "jumpMotionValue", "keepRotationValue", "lockRotation", "Lnet/ccbluex/liquidbounce/utils/Rotation;", "matrixValue", "modeValue", "onJumpValue", "placeInfo", "Lnet/ccbluex/liquidbounce/utils/block/PlaceInfo;", "placeModeValue", "rotationsValue", "slot", "stopWhenBlockAbove", "swingValue", "tag", "", "getTag", "()Ljava/lang/String;", "teleportDelayValue", "teleportGroundValue", "teleportHeightValue", "teleportNoMotionValue", "timer", "Lnet/ccbluex/liquidbounce/utils/timer/TickTimer;", "timerValue", "fakeJump", "", "move", "onDisable", "onEnable", "onJump", "event", "Lnet/ccbluex/liquidbounce/event/JumpEvent;", "onMotion", "Lnet/ccbluex/liquidbounce/event/MotionEvent;", "onPacket", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onRender2D", "Lnet/ccbluex/liquidbounce/event/Render2DEvent;", "place", "search", "", "blockPosition", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "Pride"})
public final class Tower
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Jump", "Motion", "ConstantMotion", "MotionTP", "Packet", "Teleport", "AAC3.3.9", "AAC3.6.4"}, "Motion");
    private final ListValue autoBlockValue = new ListValue("AutoBlock", new String[]{"Off", "Pick", "Spoof", "Switch"}, "Spoof");
    private final BoolValue swingValue = new BoolValue("Swing", true);
    private final BoolValue stopWhenBlockAbove = new BoolValue("StopWhenBlockAbove", false);
    private final BoolValue rotationsValue = new BoolValue("Rotations", true);
    private final BoolValue keepRotationValue = new BoolValue("KeepRotation", false);
    private final BoolValue onJumpValue = new BoolValue("OnJump", false);
    private final BoolValue matrixValue = new BoolValue("Matrix", false);
    private final ListValue placeModeValue = new ListValue("PlaceTiming", new String[]{"Pre", "Post"}, "Post");
    private final FloatValue timerValue = new FloatValue("Timer", 1.0f, 0.01f, 10.0f);
    private final FloatValue jumpMotionValue = new FloatValue("JumpMotion", 0.42f, 0.3681289f, 0.79f);
    private final IntegerValue jumpDelayValue = new IntegerValue("JumpDelay", 0, 0, 20);
    private final FloatValue constantMotionValue = new FloatValue("ConstantMotion", 0.42f, 0.1f, 1.0f);
    private final FloatValue constantMotionJumpGroundValue = new FloatValue("ConstantMotionJumpGround", 0.79f, 0.76f, 1.0f);
    private final FloatValue teleportHeightValue = new FloatValue("TeleportHeight", 1.15f, 0.1f, 5.0f);
    private final IntegerValue teleportDelayValue = new IntegerValue("TeleportDelay", 0, 0, 20);
    private final BoolValue teleportGroundValue = new BoolValue("TeleportGround", true);
    private final BoolValue teleportNoMotionValue = new BoolValue("TeleportNoMotion", false);
    private final BoolValue counterDisplayValue = new BoolValue("Counter", true);
    private PlaceInfo placeInfo;
    private Rotation lockRotation;
    private final TickTimer timer = new TickTimer();
    private double jumpGround;
    private int slot;

    @Override
    public void onEnable() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        this.slot = thePlayer.getInventory().getCurrentItem();
    }

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

    /*
     * WARNING - void declaration
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onMotion(@NotNull MotionEvent event) {
        IEntityPlayerSP thePlayer;
        block14: {
            block13: {
                IBlock iBlock;
                void blockPos$iv;
                boolean bl;
                Intrinsics.checkParameterIsNotNull(event, "event");
                if (((Boolean)this.onJumpValue.get()).booleanValue() && !MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown()) {
                    return;
                }
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) return;
                thePlayer = iEntityPlayerSP;
                if (((Boolean)this.rotationsValue.get()).booleanValue() && ((Boolean)this.keepRotationValue.get()).booleanValue() && this.lockRotation != null) {
                    RotationUtils.setTargetRotation(this.lockRotation);
                }
                MinecraftInstance.mc.getTimer().setTimerSpeed(((Number)this.timerValue.get()).floatValue());
                EventState eventState = event.getEventState();
                if (StringsKt.equals((String)this.placeModeValue.get(), eventState.getStateName(), true)) {
                    this.place();
                }
                if (eventState != EventState.PRE) return;
                this.placeInfo = null;
                this.timer.update();
                if (!StringsKt.equals((String)this.autoBlockValue.get(), "Off", true)) {
                    if (InventoryUtils.findAutoBlockBlock() == -1) {
                        if (thePlayer.getHeldItem() == null) return;
                        IItemStack iItemStack = thePlayer.getHeldItem();
                        if (iItemStack == null) {
                            Intrinsics.throwNpe();
                        }
                        if (!MinecraftInstance.classProvider.isItemBlock(iItemStack.getItem())) return;
                    }
                    bl = true;
                } else {
                    if (thePlayer.getHeldItem() == null) return;
                    IItemStack iItemStack = thePlayer.getHeldItem();
                    if (iItemStack == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!MinecraftInstance.classProvider.isItemBlock(iItemStack.getItem())) return;
                    bl = true;
                }
                boolean update = bl;
                if (!update) return;
                if (!((Boolean)this.stopWhenBlockAbove.get()).booleanValue()) break block13;
                WBlockPos wBlockPos = new WBlockPos(thePlayer.getPosX(), thePlayer.getPosY() + (double)2, thePlayer.getPosZ());
                IClassProvider iClassProvider = MinecraftInstance.classProvider;
                boolean $i$f$getBlock = false;
                Object object = MinecraftInstance.mc.getTheWorld();
                IBlock iBlock2 = object != null && (object = object.getBlockState((WBlockPos)blockPos$iv)) != null ? object.getBlock() : (iBlock = null);
                if (!iClassProvider.isBlockAir(iBlock)) break block14;
            }
            this.move();
        }
        WBlockPos blockPos = new WBlockPos(thePlayer.getPosX(), thePlayer.getPosY() - 1.0, thePlayer.getPosZ());
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        if (!MinecraftInstance.classProvider.isBlockAir(iWorldClient.getBlockState(blockPos).getBlock())) return;
        if (!this.search(blockPos)) return;
        if ((Boolean)this.rotationsValue.get() == false) return;
        VecRotation vecRotation = RotationUtils.faceBlock(blockPos);
        if (vecRotation == null) return;
        RotationUtils.setTargetRotation(vecRotation.getRotation());
        PlaceInfo placeInfo = this.placeInfo;
        if (placeInfo == null) {
            Intrinsics.throwNpe();
        }
        placeInfo.setVec3(vecRotation.getVec());
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

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private final void move() {
        IEntityPlayerSP thePlayer;
        block23: {
            block18: {
                block21: {
                    block20: {
                        block22: {
                            block19: {
                                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                                if (iEntityPlayerSP == null) {
                                    return;
                                }
                                thePlayer = iEntityPlayerSP;
                                String string = (String)this.modeValue.get();
                                boolean bl = false;
                                String string2 = string;
                                if (string2 == null) {
                                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                                }
                                String string3 = string2.toLowerCase();
                                Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
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
                            thePlayer.setPosition(thePlayer.getPosX(), MathKt.truncate(thePlayer.getPosY()), thePlayer.getPosZ());
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
                thePlayer.setPosition(thePlayer.getPosX(), MathKt.truncate(thePlayer.getPosY()), thePlayer.getPosZ());
                thePlayer.setMotionY(((Number)this.constantMotionValue.get()).floatValue());
                this.jumpGround = thePlayer.getPosY();
                return;
            }
            if (thePlayer.getOnGround()) {
                this.fakeJump();
                thePlayer.setMotionY(0.4001);
            }
            MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
            if (!(thePlayer.getMotionY() < 0.0)) return;
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

    private final void place() {
        IItem iItem;
        if (this.placeInfo == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        IItemStack itemStack = thePlayer.getHeldItem();
        if (itemStack == null || !MinecraftInstance.classProvider.isItemBlock(itemStack.getItem()) || MinecraftInstance.classProvider.isBlockBush((iItem = itemStack.getItem()) != null && (iItem = iItem.asItemBlock()) != null ? iItem.getBlock() : null)) {
            int blockSlot = InventoryUtils.findAutoBlockBlock();
            if (blockSlot == -1) {
                return;
            }
            switch ((String)this.autoBlockValue.get()) {
                case "Off": {
                    return;
                }
                case "Pick": {
                    IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP2 == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP2.getInventory().setCurrentItem(blockSlot - 36);
                    MinecraftInstance.mc.getPlayerController().updateController();
                    break;
                }
                case "Spoof": {
                    if (blockSlot - 36 == this.slot) break;
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(blockSlot - 36));
                    break;
                }
                case "Switch": {
                    if (blockSlot - 36 == this.slot) break;
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(blockSlot - 36));
                    break;
                }
            }
            itemStack = thePlayer.getInventoryContainer().getSlot(blockSlot).getStack();
        }
        IPlayerControllerMP iPlayerControllerMP = MinecraftInstance.mc.getPlayerController();
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        IItemStack iItemStack = itemStack;
        if (iItemStack == null) {
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
        if (iPlayerControllerMP.onPlayerRightClick(thePlayer, iWorldClient, iItemStack, wBlockPos, iEnumFacing, placeInfo3.getVec3())) {
            if (((Boolean)this.swingValue.get()).booleanValue()) {
                thePlayer.swingItem();
            } else {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketAnimation());
            }
        }
        if (StringsKt.equals((String)this.autoBlockValue.get(), "Switch", true)) {
            IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP3 == null) {
                Intrinsics.throwNpe();
            }
            if (this.slot != iEntityPlayerSP3.getInventory().getCurrentItem()) {
                IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP4 == null) {
                    Intrinsics.throwNpe();
                }
                iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(iEntityPlayerSP4.getInventory().getCurrentItem()));
            }
        }
        this.placeInfo = null;
    }

    /*
     * WARNING - void declaration
     */
    private final boolean search(WBlockPos blockPosition) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return false;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        boolean $i$f$isReplaceable = false;
        boolean $i$f$getMaterial = false;
        boolean $i$f$getState = false;
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        IIBlockState state$iv$iv = iWorldClient != null ? iWorldClient.getBlockState(blockPosition) : null;
        Object object = state$iv$iv;
        IMaterial iMaterial = object != null && (object = object.getBlock()) != null ? object.getMaterial(state$iv$iv) : null;
        if (!(iMaterial != null ? iMaterial.isReplaceable() : false)) {
            return false;
        }
        WVec3 eyesPos = new WVec3(thePlayer.getPosX(), thePlayer.getEntityBoundingBox().getMinY() + (double)thePlayer.getEyeHeight(), thePlayer.getPosZ());
        PlaceRotation placeRotation = null;
        for (EnumFacingType facingType : EnumFacingType.values()) {
            IEnumFacing side = MinecraftInstance.classProvider.getEnumFacing(facingType);
            WBlockPos neighbor = WBlockPos.offset$default(blockPosition, side, 0, 2, null);
            if (!BlockUtils.canBeClicked(neighbor)) continue;
            WVec3 dirVec = new WVec3(side.getDirectionVec());
            boolean matrix = (Boolean)this.matrixValue.get();
            for (double xSearch = 0.1; xSearch < 0.9; xSearch += 0.1) {
                for (double ySearch = 0.1; ySearch < 0.9; ySearch += 0.1) {
                    double zSearch = 0.1;
                    while (zSearch < 0.9) {
                        IMovingObjectPosition obj;
                        void y$iv;
                        void x$iv;
                        void this_$iv;
                        double d;
                        WVec3 hitVec;
                        boolean $i$f$addVector;
                        double z$iv$iv;
                        double distanceSqPosVec;
                        block17: {
                            block16: {
                                double d2$iv;
                                double d1$iv;
                                WVec3 wVec3;
                                WVec3 this_$iv2;
                                double d2$iv2;
                                double d1$iv2;
                                void y$iv$iv;
                                void x$iv$iv;
                                void this_$iv$iv;
                                WVec3 this_$iv3;
                                void y$iv2;
                                void x$iv2;
                                void this_$iv4;
                                WVec3 wVec32 = new WVec3(blockPosition);
                                double d2 = matrix ? 0.5 : xSearch;
                                double d3 = matrix ? 0.5 : ySearch;
                                double z$iv = matrix ? 0.5 : zSearch;
                                boolean $i$f$addVector22 = false;
                                WVec3 posVec = new WVec3(this_$iv4.getXCoord() + x$iv2, this_$iv4.getYCoord() + y$iv2, this_$iv4.getZCoord() + z$iv);
                                WVec3 this_$iv5 = eyesPos;
                                boolean $i$f$squareDistanceTo22 = false;
                                double d0$iv = posVec.getXCoord() - this_$iv5.getXCoord();
                                double d1$iv222 = posVec.getYCoord() - this_$iv5.getYCoord();
                                double d2$iv3 = posVec.getZCoord() - this_$iv5.getZCoord();
                                distanceSqPosVec = d0$iv * d0$iv + d1$iv222 * d1$iv222 + d2$iv3 * d2$iv3;
                                WVec3 $i$f$squareDistanceTo22 = posVec;
                                WVec3 vec$iv = new WVec3(dirVec.getXCoord() * 0.5, dirVec.getYCoord() * 0.5, dirVec.getZCoord() * 0.5);
                                boolean $i$f$add = false;
                                void d1$iv222 = this_$iv3;
                                double $i$f$addVector22 = vec$iv.getXCoord();
                                double d4 = vec$iv.getYCoord();
                                z$iv$iv = vec$iv.getZCoord();
                                $i$f$addVector = false;
                                hitVec = new WVec3(this_$iv$iv.getXCoord() + x$iv$iv, this_$iv$iv.getYCoord() + y$iv$iv, this_$iv$iv.getZCoord() + z$iv$iv);
                                this_$iv3 = eyesPos;
                                boolean $i$f$squareDistanceTo32 = false;
                                double d0$iv2 = hitVec.getXCoord() - this_$iv3.getXCoord();
                                if (d0$iv2 * d0$iv2 + (d1$iv2 = hitVec.getYCoord() - this_$iv3.getYCoord()) * d1$iv2 + (d2$iv2 = hitVec.getZCoord() - this_$iv3.getZCoord()) * d2$iv2 > 18.0) break block16;
                                this_$iv3 = eyesPos;
                                WVec3 $i$f$squareDistanceTo32 = posVec;
                                double d5 = distanceSqPosVec;
                                $i$f$add = false;
                                this_$iv$iv = this_$iv2;
                                d1$iv2 = dirVec.getXCoord();
                                d2$iv2 = dirVec.getYCoord();
                                z$iv$iv = dirVec.getZCoord();
                                $i$f$addVector = false;
                                this_$iv2 = wVec3 = new WVec3(this_$iv$iv.getXCoord() + x$iv$iv, this_$iv$iv.getYCoord() + y$iv$iv, this_$iv$iv.getZCoord() + z$iv$iv);
                                boolean $i$f$squareDistanceTo = false;
                                double d0$iv3 = vec$iv.getXCoord() - this_$iv3.getXCoord();
                                d = d0$iv3 * d0$iv3 + (d1$iv = vec$iv.getYCoord() - this_$iv3.getYCoord()) * d1$iv + (d2$iv = vec$iv.getZCoord() - this_$iv3.getZCoord()) * d2$iv;
                                if (d5 > d) break block16;
                                IWorldClient iWorldClient2 = MinecraftInstance.mc.getTheWorld();
                                if (iWorldClient2 == null) {
                                    Intrinsics.throwNpe();
                                }
                                if (iWorldClient2.rayTraceBlocks(eyesPos, hitVec, false, true, false) == null) break block17;
                            }
                            zSearch += 0.1;
                            continue;
                        }
                        double diffX = hitVec.getXCoord() - eyesPos.getXCoord();
                        double diffY = hitVec.getYCoord() - eyesPos.getYCoord();
                        double diffZ = hitVec.getZCoord() - eyesPos.getZCoord();
                        z$iv$iv = diffX * diffX + diffZ * diffZ;
                        $i$f$addVector = false;
                        double diffXZ = Math.sqrt(z$iv$iv);
                        boolean bl = false;
                        d = Math.atan2(diffZ, diffX);
                        float f = WMathHelper.wrapAngleTo180_float((float)Math.toDegrees(d) - 90.0f);
                        bl = false;
                        double d6 = Math.atan2(diffY, diffXZ);
                        float f2 = WMathHelper.wrapAngleTo180_float((float)(-Math.toDegrees(d6)));
                        float f3 = f;
                        Rotation rotation = new Rotation(f3, f2);
                        WVec3 rotationVector = RotationUtils.getVectorForRotation(rotation);
                        WVec3 wVec3 = eyesPos;
                        double d7 = rotationVector.getXCoord() * distanceSqPosVec;
                        double d8 = rotationVector.getYCoord() * distanceSqPosVec;
                        double z$iv = rotationVector.getZCoord() * distanceSqPosVec;
                        boolean $i$f$addVector2 = false;
                        WVec3 vector = new WVec3(this_$iv.getXCoord() + x$iv, this_$iv.getYCoord() + y$iv, this_$iv.getZCoord() + z$iv);
                        IWorldClient iWorldClient3 = MinecraftInstance.mc.getTheWorld();
                        if (iWorldClient3 == null) {
                            Intrinsics.throwNpe();
                        }
                        IMovingObjectPosition iMovingObjectPosition = obj = iWorldClient3.rayTraceBlocks(eyesPos, vector, false, false, true);
                        if (iMovingObjectPosition == null) {
                            Intrinsics.throwNpe();
                        }
                        if (iMovingObjectPosition.getTypeOfHit() != IMovingObjectPosition.WMovingObjectType.BLOCK || !Intrinsics.areEqual(obj.getBlockPos(), neighbor)) {
                            zSearch += 0.1;
                            continue;
                        }
                        if (placeRotation == null || RotationUtils.getRotationDifference(rotation) < RotationUtils.getRotationDifference(placeRotation.getRotation())) {
                            placeRotation = new PlaceRotation(new PlaceInfo(neighbor, side.getOpposite(), hitVec), rotation);
                        }
                        zSearch += 0.1;
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
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
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
        Intrinsics.checkParameterIsNotNull(event, "event");
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
            String info = "Blocks: §7" + this.getBlocksAmount();
            IMinecraft iMinecraft = MinecraftInstance.mc;
            Intrinsics.checkExpressionValueIsNotNull(iMinecraft, "mc");
            IScaledResolution scaledResolution = MinecraftInstance.classProvider.createScaledResolution(iMinecraft);
            float f = (float)(scaledResolution.getScaledWidth() / 2) - (float)2;
            float f2 = (float)(scaledResolution.getScaledHeight() / 2) + (float)5;
            float f3 = (float)(scaledResolution.getScaledWidth() / 2 + Fonts.font40.getStringWidth(info)) + (float)2;
            float f4 = (float)(scaledResolution.getScaledHeight() / 2) + (float)16;
            Color color = Color.BLACK;
            Intrinsics.checkExpressionValueIsNotNull(color, "Color.BLACK");
            int n = color.getRGB();
            Color color2 = Color.BLACK;
            Intrinsics.checkExpressionValueIsNotNull(color2, "Color.BLACK");
            RenderUtils.drawBorderedRect(f, f2, f3, f4, 3.0f, n, color2.getRGB());
            MinecraftInstance.classProvider.getGlStateManager().resetColor();
            float f5 = (float)scaledResolution.getScaledWidth() / (float)2;
            float f6 = (float)(scaledResolution.getScaledHeight() / 2) + (float)7;
            Color color3 = Color.WHITE;
            Intrinsics.checkExpressionValueIsNotNull(color3, "Color.WHITE");
            Fonts.font40.drawString(info, f5, f6, color3.getRGB());
            GL11.glPopMatrix();
        }
    }

    @EventTarget
    public final void onJump(@NotNull JumpEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (((Boolean)this.onJumpValue.get()).booleanValue()) {
            event.cancelEvent();
        }
    }

    /*
     * WARNING - void declaration
     */
    private final int getBlocksAmount() {
        int amount = 0;
        int n = 36;
        int n2 = 44;
        while (n <= n2) {
            void i;
            IItemStack itemStack;
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if ((itemStack = iEntityPlayerSP.getInventoryContainer().getSlot((int)i).getStack()) != null && MinecraftInstance.classProvider.isItemBlock(itemStack.getItem())) {
                IItem iItem = itemStack.getItem();
                if (iItem == null) {
                    Intrinsics.throwNpe();
                }
                IBlock block = iItem.asItemBlock().getBlock();
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                if (Intrinsics.areEqual(iEntityPlayerSP2.getHeldItem(), itemStack) || !InventoryUtils.BLOCK_BLACKLIST.contains(block)) {
                    amount += itemStack.getStackSize();
                }
            }
            ++i;
        }
        return amount;
    }

    @Override
    @NotNull
    public String getTag() {
        return (String)this.modeValue.get();
    }
}
