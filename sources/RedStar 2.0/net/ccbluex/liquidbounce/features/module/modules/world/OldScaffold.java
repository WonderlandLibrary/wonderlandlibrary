package net.ccbluex.liquidbounce.features.module.modules.world;

import java.awt.Color;
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
import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.api.minecraft.util.IMovingObjectPosition;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WMathHelper;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
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
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.InventoryUtils;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.PlaceRotation;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.block.PlaceInfo;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="OldScaffold", description="修复版", category=ModuleCategory.WORLD, keyBind=34)
public class OldScaffold
extends Module {
    public final ListValue modeValue = new ListValue("Mode", new String[]{"Normal", "Rewinside", "Expand"}, "Normal");
    private final IntegerValue maxDelayValue = new IntegerValue("MaxDelay", 0, 0, 1000){

        @Override
        protected void onChanged(Integer oldValue, Integer newValue) {
            int i = (Integer)OldScaffold.this.minDelayValue.get();
            if (i > newValue) {
                this.set(i);
            }
        }
    };
    private final IntegerValue minDelayValue = new IntegerValue("MinDelay", 0, 0, 1000){

        @Override
        protected void onChanged(Integer oldValue, Integer newValue) {
            int i = (Integer)OldScaffold.this.maxDelayValue.get();
            if (i < newValue) {
                this.set(i);
            }
        }
    };
    private final BoolValue placeableDelay = new BoolValue("PlaceableDelay", false);
    private final ListValue autoBlockValue = new ListValue("AutoBlock", new String[]{"Off", "Spoof", "Pick", "Switch"}, "Spoof");
    public final BoolValue sprintValue = new BoolValue("Sprint", true);
    private final BoolValue swingValue = new BoolValue("Swing", true);
    private final BoolValue searchValue = new BoolValue("Search", true);
    private final BoolValue downValue = new BoolValue("Down", true);
    private final BoolValue picker = new BoolValue("Picker", false);
    private final ListValue placeModeValue = new ListValue("PlaceTiming", new String[]{"Pre", "Post"}, "Post");
    private final ListValue eagleValue = new ListValue("Eagle", new String[]{"Normal", "EdgeDistance", "Silent", "Off"}, "Off");
    private final IntegerValue blocksToEagleValue = new IntegerValue("BlocksToEagle", 0, 0, 10);
    private final FloatValue edgeDistanceValue = new FloatValue("EagleEdgeDistance", 0.2f, 0.0f, 0.5f);
    private final IntegerValue expandLengthValue = new IntegerValue("ExpandLength", 5, 1, 6);
    private final BoolValue rotationStrafeValue = new BoolValue("RotationStrafe", false);
    private final ListValue rotationModeValue = new ListValue("RotationMode", new String[]{"Normal", "Static", "StaticPitch", "StaticYaw", "Off"}, "Normal");
    private final BoolValue silentRotation = new BoolValue("SilentRotation", true);
    private final BoolValue keepRotationValue = new BoolValue("KeepRotation", false);
    private final IntegerValue keepLengthValue = new IntegerValue("KeepRotationLength", 0, 0, 20);
    private final FloatValue staticPitchValue = new FloatValue("StaticPitchOffset", 86.0f, 70.0f, 90.0f);
    private final FloatValue staticYawOffsetValue = new FloatValue("StaticYawOffset", 0.0f, 0.0f, 90.0f);
    private final FloatValue xzRangeValue = new FloatValue("xzRange", 0.8f, 0.1f, 1.0f);
    private final FloatValue yRangeValue = new FloatValue("yRange", 0.8f, 0.1f, 1.0f);
    private final IntegerValue searchAccuracyValue = new IntegerValue("SearchAccuracy", 8, 1, 24){

        @Override
        protected void onChanged(Integer oldValue, Integer newValue) {
            if (this.getMaximum() < newValue) {
                this.set(this.getMaximum());
            } else if (this.getMinimum() > newValue) {
                this.set(this.getMinimum());
            }
        }
    };
    private final FloatValue maxTurnSpeedValue = new FloatValue("MaxTurnSpeed", 180.0f, 1.0f, 180.0f){

        @Override
        protected void onChanged(Float oldValue, Float newValue) {
            float v = ((Float)OldScaffold.this.minTurnSpeedValue.get()).floatValue();
            if (v > newValue.floatValue()) {
                this.set(Float.valueOf(v));
            }
            if (this.getMaximum() < newValue.floatValue()) {
                this.set(Float.valueOf(this.getMaximum()));
            } else if (this.getMinimum() > newValue.floatValue()) {
                this.set(Float.valueOf(this.getMinimum()));
            }
        }
    };
    private final FloatValue minTurnSpeedValue = new FloatValue("MinTurnSpeed", 180.0f, 1.0f, 180.0f){

        @Override
        protected void onChanged(Float oldValue, Float newValue) {
            float v = ((Float)OldScaffold.this.maxTurnSpeedValue.get()).floatValue();
            if (v < newValue.floatValue()) {
                this.set(Float.valueOf(v));
            }
            if (this.getMaximum() < newValue.floatValue()) {
                this.set(Float.valueOf(this.getMaximum()));
            } else if (this.getMinimum() > newValue.floatValue()) {
                this.set(Float.valueOf(this.getMinimum()));
            }
        }
    };
    private final BoolValue zitterValue = new BoolValue("Zitter", false);
    private final ListValue zitterModeValue = new ListValue("ZitterMode", new String[]{"Teleport", "Smooth"}, "Teleport");
    private final FloatValue zitterSpeed = new FloatValue("ZitterSpeed", 0.13f, 0.1f, 0.3f);
    private final FloatValue zitterStrength = new FloatValue("ZitterStrength", 0.072f, 0.05f, 0.2f);
    private final FloatValue timerValue = new FloatValue("Timer", 1.0f, 0.1f, 10.0f);
    private final FloatValue speedModifierValue = new FloatValue("SpeedModifier", 1.0f, 0.0f, 2.0f);
    private final BoolValue slowValue = new BoolValue("Slow", false){

        @Override
        protected void onChanged(Boolean oldValue, Boolean newValue) {
            if (newValue.booleanValue()) {
                OldScaffold.this.sprintValue.set(false);
            }
        }
    };
    private final FloatValue slowSpeed = new FloatValue("SlowSpeed", 0.6f, 0.2f, 0.8f);
    private long lastMS = 0L;
    private float progress = 0.0f;
    private final BoolValue sameYValue = new BoolValue("SameY", false);
    private final BoolValue smartSpeedValue = new BoolValue("SmartSpeed", false);
    private final BoolValue autoJumpValue = new BoolValue("AutoJump", false);
    private final BoolValue safeWalkValue = new BoolValue("SafeWalk", true);
    private final BoolValue airSafeValue = new BoolValue("AirSafe", false);
    private final BoolValue counterDisplayValue = new BoolValue("Counter", true);
    private final BoolValue markValue = new BoolValue("Mark", false);
    private final BoolValue autoTimer = new BoolValue("AutoTimer", false);
    private PlaceInfo targetPlace;
    private int launchY;
    private Rotation lockRotation;
    private Rotation limitedRotation;
    private boolean facesBlock = false;
    private int slot;
    private boolean zitterDirection;
    private final MSTimer delayTimer = new MSTimer();
    private final MSTimer zitterTimer = new MSTimer();
    private long delay;
    private int placedBlocksWithoutEagle = 0;
    private boolean eagleSneaking;
    private boolean shouldGoDown = false;

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

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (this.getBlocksAmount() == 0) {
            this.toggle();
        }
        this.getBestBlocks();
        mc.getTimer().setTimerSpeed(((Float)this.timerValue.get()).floatValue());
        boolean bl = this.shouldGoDown = (Boolean)this.downValue.get() != false && (Boolean)this.sameYValue.get() == false && mc.getGameSettings().getKeyBindSneak().isKeyDown() && this.getBlocksAmount() > 1;
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
                if (this.zitterTimer.hasTimePassed(100L)) {
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
            if (!((String)this.eagleValue.get()).equalsIgnoreCase("Off") && !this.shouldGoDown) {
                double dif = 0.5;
                if (((String)this.eagleValue.get()).equalsIgnoreCase("EdgeDistance") && !this.shouldGoDown) {
                    block6: for (int i = 0; i < 4; ++i) {
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
                                if (!BlockUtils.isReplaceable(blockPos) || placeInfo == null) continue block6;
                                double calcDif = mc.getThePlayer().getPosZ() - (double)blockPos.getZ();
                                if ((calcDif -= 0.5) < 0.0) {
                                    calcDif *= -1.0;
                                }
                                if (!((calcDif -= 0.5) < dif)) continue block6;
                                dif = calcDif;
                            }
                        }
                    }
                }
                if (this.placedBlocksWithoutEagle >= (Integer)this.blocksToEagleValue.get()) {
                    boolean shouldEagle;
                    boolean bl2 = shouldEagle = mc.getTheWorld().getBlockState(new WBlockPos(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY() - 1.0, mc.getThePlayer().getPosZ())).getBlock().equals(classProvider.getBlockEnum(BlockType.AIR)) || dif < (double)((Float)this.edgeDistanceValue.get()).floatValue() && ((String)this.eagleValue.get()).equalsIgnoreCase("EdgeDistance");
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
                boolean bl3 = this.zitterDirection = !this.zitterDirection;
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
    private void onStrafe(StrafeEvent event) {
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

    private void update() {
        boolean isHeldItemBlock;
        boolean bl = isHeldItemBlock = mc.getThePlayer().getHeldItem() != null && classProvider.isItemBlock(mc.getThePlayer().getHeldItem().getItem());
        if (!((String)this.autoBlockValue.get()).equalsIgnoreCase("Off") ? InventoryUtils.findAutoBlockBlock() == -1 && !isHeldItemBlock : !isHeldItemBlock) {
            return;
        }
        this.findBlock(((String)this.modeValue.get()).equalsIgnoreCase("expand"));
    }

    private void setRotation(Rotation rotation, int keepRotation) {
        if (((Boolean)this.silentRotation.get()).booleanValue()) {
            RotationUtils.setTargetRotation(rotation, keepRotation);
        } else {
            mc.getThePlayer().setRotationYaw(rotation.getYaw());
            mc.getThePlayer().setRotationPitch(rotation.getPitch());
        }
    }

    private void setRotation(Rotation rotation) {
        this.setRotation(rotation, 0);
    }

    private void findBlock(boolean expand) {
        block5: {
            WBlockPos blockPosition;
            block4: {
                WBlockPos wBlockPos = this.shouldGoDown ? (mc.getThePlayer().getPosY() == (double)((int)mc.getThePlayer().getPosY()) + 0.5 ? new WBlockPos(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY() - 0.6, mc.getThePlayer().getPosZ()) : new WBlockPos(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY() - 0.6, mc.getThePlayer().getPosZ()).down()) : ((Boolean)this.sameYValue.get() != false || ((Boolean)this.autoJumpValue.get() != false || (Boolean)this.smartSpeedValue.get() != false && LiquidBounce.moduleManager.getModule(Speed.class).getState()) && !mc.getGameSettings().getKeyBindJump().isKeyDown() && (double)this.launchY <= mc.getThePlayer().getPosY() ? new WBlockPos(mc.getThePlayer().getPosX(), (double)(this.launchY - 1), mc.getThePlayer().getPosZ()) : (blockPosition = mc.getThePlayer().getPosY() == (double)((int)mc.getThePlayer().getPosY()) + 0.5 ? new WBlockPos(mc.getThePlayer()) : new WBlockPos(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY(), mc.getThePlayer().getPosZ()).down()));
                if (!(expand || BlockUtils.isReplaceable(blockPosition) && !this.search(blockPosition, !this.shouldGoDown))) {
                    return;
                }
                if (!expand) break block4;
                for (int i = 0; i < (Integer)this.expandLengthValue.get(); ++i) {
                    if (!this.search(blockPosition.add(mc.getThePlayer().getHorizontalFacing().equals(classProvider.getEnumFacing(EnumFacingType.WEST)) ? -i : (mc.getThePlayer().getHorizontalFacing().equals(classProvider.getEnumFacing(EnumFacingType.EAST)) ? i : 0), 0, mc.getThePlayer().getHorizontalFacing().equals(classProvider.getEnumFacing(EnumFacingType.NORTH)) ? -i : (mc.getThePlayer().getHorizontalFacing().equals(classProvider.getEnumFacing(EnumFacingType.SOUTH)) ? i : 0)), false)) continue;
                    return;
                }
                break block5;
            }
            if (!((Boolean)this.searchValue.get()).booleanValue()) break block5;
            for (int x = -1; x <= 1; ++x) {
                for (int z = -1; z <= 1; ++z) {
                    if (!this.search(blockPosition.add(x, 0, z), !this.shouldGoDown)) continue;
                    return;
                }
            }
        }
    }

    private void place() {
        if (this.targetPlace == null) {
            if (((Boolean)this.placeableDelay.get()).booleanValue()) {
                this.delayTimer.reset();
            }
            return;
        }
        if (!this.delayTimer.hasTimePassed(this.delay) || ((Boolean)this.sameYValue.get()).booleanValue() && this.launchY - 1 != (int)this.targetPlace.getVec3().getYCoord()) {
            return;
        }
        int blockSlot = -1;
        IItemStack itemStack = mc.getThePlayer().getHeldItem();
        if (itemStack == null || !classProvider.isItemBlock(itemStack.getItem()) || classProvider.isBlockBush(itemStack.getItem().asItemBlock().getBlock()) || mc.getThePlayer().getHeldItem().getStackSize() <= 0) {
            if (((String)this.autoBlockValue.get()).equalsIgnoreCase("Off")) {
                return;
            }
            blockSlot = InventoryUtils.findAutoBlockBlock();
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
        this.facesBlock = false;
        mc.getTimer().setTimerSpeed(1.0f);
        this.shouldGoDown = false;
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

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        if (((Boolean)this.counterDisplayValue.get()).booleanValue()) {
            this.progress = (float)(System.currentTimeMillis() - this.lastMS) / 100.0f;
            if (this.progress >= 1.0f) {
                this.progress = 1.0f;
            }
            ScaledResolution scaledResolution = new ScaledResolution(mc2);
            String info = this.getBlocksAmount() + " blocks";
            int infoWidth = Fonts.font40.getStringWidth(info);
            int infoWidth2 = Fonts.minecraftFont.getStringWidth(this.getBlocksAmount() + "");
            GlStateManager.translate((float)0.0f, (float)(-14.0f - this.progress * 4.0f), (float)0.0f);
            GL11.glEnable((int)3042);
            GL11.glDisable((int)3553);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glEnable((int)2848);
            GL11.glColor4f((float)0.15f, (float)0.15f, (float)0.15f, (float)this.progress);
            GL11.glBegin((int)6);
            GL11.glVertex2d((double)(scaledResolution.getScaledWidth() / 2 - 3), (double)(scaledResolution.getScaledHeight() - 60));
            GL11.glVertex2d((double)(scaledResolution.getScaledWidth() / 2), (double)(scaledResolution.getScaledHeight() - 57));
            GL11.glVertex2d((double)(scaledResolution.getScaledWidth() / 2 + 3), (double)(scaledResolution.getScaledHeight() - 60));
            GL11.glEnd();
            GL11.glEnable((int)3553);
            GL11.glDisable((int)3042);
            GL11.glDisable((int)2848);
            RenderUtils.drawRoundedRect(scaledResolution.getScaledWidth() / 2 - infoWidth / 2 - 4, scaledResolution.getScaledHeight() - 60, scaledResolution.getScaledWidth() / 2 + infoWidth / 2 + 4, scaledResolution.getScaledHeight() - 74, 2, new Color(0.15f, 0.15f, 0.15f, this.progress).getRGB());
            GlStateManager.resetColor();
            Fonts.font35.drawCenteredString(info, (float)(scaledResolution.getScaledWidth() / 2) + 0.1f, scaledResolution.getScaledHeight() - 70, new Color(1.0f, 1.0f, 1.0f, 0.8f * this.progress).getRGB(), false);
            GlStateManager.translate((float)0.0f, (float)(14.0f + this.progress * 4.0f), (float)0.0f);
        }
    }

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        if (!((Boolean)this.markValue.get()).booleanValue()) {
            return;
        }
        for (int i = 0; i < (((String)this.modeValue.get()).equalsIgnoreCase("Expand") ? (Integer)this.expandLengthValue.get() + 1 : 2); ++i) {
            WBlockPos blockPos = new WBlockPos(mc.getThePlayer().getPosX() + (double)(mc.getThePlayer().getHorizontalFacing().equals(classProvider.getEnumFacing(EnumFacingType.WEST)) ? -i : (mc.getThePlayer().getHorizontalFacing().equals(classProvider.getEnumFacing(EnumFacingType.EAST)) ? i : 0)), mc.getThePlayer().getPosY() - (mc.getThePlayer().getPosY() == (double)((int)mc.getThePlayer().getPosY()) + 0.5 ? 0.0 : 1.0) - (this.shouldGoDown ? 1.0 : 0.0), mc.getThePlayer().getPosZ() + (double)(mc.getThePlayer().getHorizontalFacing().equals(classProvider.getEnumFacing(EnumFacingType.NORTH)) ? -i : (mc.getThePlayer().getHorizontalFacing().equals(classProvider.getEnumFacing(EnumFacingType.SOUTH)) ? i : 0)));
            PlaceInfo placeInfo = PlaceInfo.get(blockPos);
            if (!BlockUtils.isReplaceable(blockPos) || placeInfo == null) continue;
            RenderUtils.drawBlockBox(blockPos, new Color(68, 117, 255, 100), false);
            break;
        }
    }

    private boolean search(WBlockPos blockPosition, boolean checks) {
        WVec3 dirVec;
        WBlockPos neighbor;
        IEnumFacing side;
        if (!BlockUtils.isReplaceable(blockPosition)) {
            return false;
        }
        boolean staticMode = ((String)this.rotationModeValue.get()).equalsIgnoreCase("Static");
        boolean staticPitchMode = staticMode || ((String)this.rotationModeValue.get()).equalsIgnoreCase("StaticPitch");
        boolean staticYawMode = staticMode || ((String)this.rotationModeValue.get()).equalsIgnoreCase("StaticYaw");
        float staticPitch = ((Float)this.staticPitchValue.get()).floatValue();
        float staticYawOffset = ((Float)this.staticYawOffsetValue.get()).floatValue();
        double xzRV = ((Float)this.xzRangeValue.get()).floatValue();
        double xzSSV = this.calcStepSize(xzRV);
        double yRV = ((Float)this.yRangeValue.get()).floatValue();
        double ySSV = this.calcStepSize(yRV);
        double xSearchFace = 0.0;
        double ySearchFace = 0.0;
        double zSearchFace = 0.0;
        WVec3 eyesPos = new WVec3(mc.getThePlayer().getPosX(), mc.getThePlayer().getEntityBoundingBox().getMinY() + (double)mc.getThePlayer().getEyeHeight(), mc.getThePlayer().getPosZ());
        PlaceRotation placeRotation = null;
        for (EnumFacingType facingType : EnumFacingType.values()) {
            side = classProvider.getEnumFacing(facingType);
            neighbor = blockPosition.offset(side);
            if (!BlockUtils.canBeClicked(neighbor)) continue;
            dirVec = new WVec3(side.getDirectionVec());
            for (double xSearch = 0.5 - xzRV / 2.0; xSearch <= 0.5 + xzRV / 2.0; xSearch += xzSSV) {
                for (double ySearch = 0.5 - yRV / 2.0; ySearch <= 0.5 + yRV / 2.0; ySearch += ySSV) {
                    for (double zSearch = 0.5 - xzRV / 2.0; zSearch <= 0.5 + xzRV / 2.0; zSearch += xzSSV) {
                        WVec3 posVec = new WVec3(blockPosition).addVector(xSearch, ySearch, zSearch);
                        double distanceSqPosVec = eyesPos.squareDistanceTo(posVec);
                        WVec3 hitVec = posVec.add(new WVec3(dirVec.getXCoord() * 0.5, dirVec.getYCoord() * 0.5, dirVec.getZCoord() * 0.5));
                        if (checks && (eyesPos.squareDistanceTo(hitVec) > 18.0 || distanceSqPosVec > eyesPos.squareDistanceTo(posVec.add(dirVec)) || mc.getTheWorld().rayTraceBlocks(eyesPos, hitVec, false, true, false) != null)) continue;
                        for (int i = 0; i < (staticYawMode ? 2 : 1); ++i) {
                            double diffX = staticYawMode && i == 0 ? 0.0 : hitVec.getXCoord() - eyesPos.getXCoord();
                            double diffY = hitVec.getYCoord() - eyesPos.getYCoord();
                            double diffZ = staticYawMode && i == 1 ? 0.0 : hitVec.getZCoord() - eyesPos.getZCoord();
                            double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
                            float pitch = staticPitchMode ? staticPitch : WMathHelper.wrapAngleTo180_float((float)(-Math.toDegrees(Math.atan2(diffY, diffXZ))));
                            Rotation rotation = new Rotation(WMathHelper.wrapAngleTo180_float((float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f + (staticYawMode ? staticYawOffset : 0.0f)), pitch);
                            WVec3 rotationVector = RotationUtils.getVectorForRotation(rotation);
                            WVec3 vector = eyesPos.addVector(rotationVector.getXCoord() * 4.0, rotationVector.getYCoord() * 4.0, rotationVector.getZCoord() * 4.0);
                            IMovingObjectPosition obj = mc.getTheWorld().rayTraceBlocks(eyesPos, vector, false, false, true);
                            if (obj.getTypeOfHit() != IMovingObjectPosition.WMovingObjectType.BLOCK || !obj.getBlockPos().equals(neighbor)) continue;
                            if (placeRotation == null || RotationUtils.getRotationDifference(rotation) < RotationUtils.getRotationDifference(placeRotation.getRotation())) {
                                placeRotation = new PlaceRotation(new PlaceInfo(neighbor, side.getOpposite(), hitVec), rotation);
                            }
                            xSearchFace = xSearch;
                            ySearchFace = ySearch;
                            zSearchFace = zSearch;
                        }
                    }
                }
            }
        }
        if (placeRotation == null) {
            return false;
        }
        if (!((String)this.rotationModeValue.get()).equalsIgnoreCase("Off")) {
            if (((Float)this.minTurnSpeedValue.get()).floatValue() < 180.0f) {
                this.limitedRotation = RotationUtils.limitAngleChange(RotationUtils.serverRotation, placeRotation.getRotation(), (float)(Math.random() * (double)(((Float)this.maxTurnSpeedValue.get()).floatValue() - ((Float)this.minTurnSpeedValue.get()).floatValue()) + (double)((Float)this.minTurnSpeedValue.get()).floatValue()));
                this.setRotation(this.limitedRotation, (Integer)this.keepLengthValue.get());
                this.lockRotation = this.limitedRotation;
                this.facesBlock = false;
                for (EnumFacingType facingType : EnumFacingType.values()) {
                    side = classProvider.getEnumFacing(facingType);
                    neighbor = blockPosition.offset(side);
                    if (!BlockUtils.canBeClicked(neighbor)) continue;
                    dirVec = new WVec3(side.getDirectionVec());
                    WVec3 posVec = new WVec3(blockPosition).addVector(xSearchFace, ySearchFace, zSearchFace);
                    double distanceSqPosVec = eyesPos.squareDistanceTo(posVec);
                    WVec3 hitVec = posVec.add(new WVec3(dirVec.getXCoord() * 0.5, dirVec.getYCoord() * 0.5, dirVec.getZCoord() * 0.5));
                    if (checks && (eyesPos.squareDistanceTo(hitVec) > 18.0 || distanceSqPosVec > eyesPos.squareDistanceTo(posVec.add(dirVec)) || mc.getTheWorld().rayTraceBlocks(eyesPos, hitVec, false, true, false) != null)) continue;
                    WVec3 rotationVector = RotationUtils.getVectorForRotation(this.limitedRotation);
                    WVec3 vector = eyesPos.addVector(rotationVector.getXCoord() * 4.0, rotationVector.getYCoord() * 4.0, rotationVector.getZCoord() * 4.0);
                    IMovingObjectPosition obj = mc.getTheWorld().rayTraceBlocks(eyesPos, vector, false, false, true);
                    if (obj.getTypeOfHit() != IMovingObjectPosition.WMovingObjectType.BLOCK || !obj.getBlockPos().equals(neighbor)) continue;
                    this.facesBlock = true;
                    break;
                }
            } else {
                this.setRotation(placeRotation.getRotation(), (Integer)this.keepLengthValue.get());
                this.lockRotation = placeRotation.getRotation();
                this.facesBlock = true;
            }
        }
        this.targetPlace = placeRotation.getPlaceInfo();
        return true;
    }

    private double calcStepSize(double range) {
        double accuracy = ((Integer)this.searchAccuracyValue.get()).intValue();
        if (range / (accuracy += accuracy % 2.0) < 0.01) {
            return 0.01;
        }
        return range / accuracy;
    }

    private int getBlocksAmount() {
        int amount = 0;
        for (int i = 36; i < 45; ++i) {
            IItemStack itemStack = mc.getThePlayer().getInventoryContainer().getSlot(i).getStack();
            if (itemStack == null || !classProvider.isItemBlock(itemStack.getItem())) continue;
            IBlock block = itemStack.getItem().asItemBlock().getBlock();
            IItemStack heldItem = mc.getThePlayer().getHeldItem();
            if ((heldItem == null || !heldItem.equals(itemStack)) && (InventoryUtils.BLOCK_BLACKLIST.contains(block) || classProvider.isBlockBush(block))) continue;
            amount += itemStack.getStackSize();
        }
        return amount;
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }

    public void getBestBlocks() {
        block11: {
            block10: {
                if (this.getBlocksAmount() == 0) {
                    return;
                }
                if (!((Boolean)this.picker.get()).booleanValue()) break block10;
                int bestInvSlot = this.getBiggestBlockSlotInv();
                int bestHotbarSlot = this.getBiggestBlockSlotHotbar();
                int bestSlot = this.getBiggestBlockSlotHotbar() > 0 ? this.getBiggestBlockSlotHotbar() : this.getBiggestBlockSlotInv();
                int spoofSlot = 42;
                if (bestHotbarSlot > 0 && bestInvSlot > 0 && mc.getThePlayer().getInventoryContainer().getSlot(bestInvSlot).getHasStack() && mc.getThePlayer().getInventoryContainer().getSlot(bestHotbarSlot).getHasStack() && mc.getThePlayer().getInventoryContainer().getSlot(bestHotbarSlot).getStack().getStackSize() < mc.getThePlayer().getInventoryContainer().getSlot(bestInvSlot).getStack().getStackSize()) {
                    bestSlot = bestInvSlot;
                }
                if (this.hotbarContainBlock()) {
                    for (int a = 36; a < 45; ++a) {
                        IItem item;
                        if (!mc.getThePlayer().getInventoryContainer().getSlot(a).getHasStack() || !((item = mc.getThePlayer().getInventoryContainer().getSlot(a).getStack().getItem()) instanceof IItemBlock)) continue;
                        spoofSlot = a;
                        break;
                    }
                } else {
                    for (int a = 36; a < 45; ++a) {
                        if (mc.getThePlayer().getInventoryContainer().getSlot(a).getHasStack()) continue;
                        spoofSlot = a;
                        break;
                    }
                }
                if (mc.getThePlayer().getInventoryContainer().getSlot(spoofSlot).getSlotNumber() == bestSlot) break block11;
                this.swap(bestSlot, spoofSlot - 36);
                mc.getPlayerController().updateController();
                break block11;
            }
            if (this.invCheck()) {
                IItemStack is = (IItemStack)((Object)functions.getItemById(261));
                for (int i = 9; i < 36; ++i) {
                    if (!mc.getThePlayer().getInventoryContainer().getSlot(i).getHasStack()) continue;
                    IItem item = mc.getThePlayer().getInventoryContainer().getSlot(i).getStack().getItem();
                    int count = 0;
                    if (!(item instanceof IItemBlock)) continue;
                    for (int a = 36; a < 45; ++a) {
                        if (!functions.canAddItemToSlot(mc.getThePlayer().getInventoryContainer().getSlot(a), is, true)) continue;
                        this.swap(i, a - 36);
                        ++count;
                        break;
                    }
                    if (count != 0) break;
                    this.swap(i, 7);
                    break;
                }
            }
        }
    }

    private boolean hotbarContainBlock() {
        int i = 36;
        while (i < 45) {
            try {
                IItemStack stack = mc.getThePlayer().getInventoryContainer().getSlot(i).getStack();
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

    public int getBiggestBlockSlotHotbar() {
        int slot = -1;
        int size = 0;
        if (this.getBlocksAmount() == 0) {
            return -1;
        }
        for (int i = 36; i < 45; ++i) {
            if (!mc.getThePlayer().getInventoryContainer().getSlot(i).getHasStack()) continue;
            IItem item = mc.getThePlayer().getInventoryContainer().getSlot(i).getStack().getItem();
            IItemStack is = mc.getThePlayer().getInventoryContainer().getSlot(i).getStack();
            if (!(item instanceof IItemBlock) || is.getStackSize() <= size) continue;
            size = is.getStackSize();
            slot = i;
        }
        return slot;
    }

    protected void swap(int slot, int hotbarNum) {
        mc.getPlayerController().windowClick(mc.getThePlayer().getInventoryContainer().getWindowId(), slot, hotbarNum, 2, mc.getThePlayer());
    }

    private boolean invCheck() {
        for (int i = 36; i < 45; ++i) {
            IItem item;
            if (!mc.getThePlayer().getInventoryContainer().getSlot(i).getHasStack() || !((item = mc.getThePlayer().getInventoryContainer().getSlot(i).getStack().getItem()) instanceof IItemBlock)) continue;
            return false;
        }
        return true;
    }

    public int getBiggestBlockSlotInv() {
        int slot = -1;
        int size = 0;
        if (this.getBlocksAmount() == 0) {
            return -1;
        }
        for (int i = 9; i < 36; ++i) {
            if (!mc.getThePlayer().getInventoryContainer().getSlot(i).getHasStack()) continue;
            IItem item = mc.getThePlayer().getInventoryContainer().getSlot(i).getStack().getItem();
            IItemStack is = mc.getThePlayer().getInventoryContainer().getSlot(i).getStack();
            if (!(item instanceof IItemBlock) || is.getStackSize() <= size) continue;
            size = is.getStackSize();
            slot = i;
        }
        return slot;
    }
}
