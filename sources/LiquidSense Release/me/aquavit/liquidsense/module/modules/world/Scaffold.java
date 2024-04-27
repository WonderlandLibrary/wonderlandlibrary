package me.aquavit.liquidsense.module.modules.world;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.event.EventState;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.utils.client.InventoryUtils;
import me.aquavit.liquidsense.utils.entity.MovementUtils;
import me.aquavit.liquidsense.utils.client.PlaceRotation;
import me.aquavit.liquidsense.utils.client.Rotation;
import me.aquavit.liquidsense.utils.client.RotationUtils;
import me.aquavit.liquidsense.event.events.*;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.module.modules.movement.Speed;
import me.aquavit.liquidsense.ui.font.Fonts;
import me.aquavit.liquidsense.utils.block.BlockUtils;
import me.aquavit.liquidsense.utils.block.PlaceInfo;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.utils.timer.MSTimer;
import me.aquavit.liquidsense.utils.timer.TimeUtils;
import me.aquavit.liquidsense.value.BoolValue;
import me.aquavit.liquidsense.value.FloatValue;
import me.aquavit.liquidsense.value.IntegerValue;
import me.aquavit.liquidsense.value.ListValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.*;
import net.minecraft.util.Timer;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@ModuleInfo(name = "Scaffold", description = "Automatically places blocks beneath your feet.", category = ModuleCategory.WORLD, keyBind = Keyboard.KEY_I)
public class Scaffold extends Module {

    /**
     * OPTIONS
     */

    // Mode
    public final ListValue modeValue = new ListValue("Mode", new String[]{"Normal", "Rewinside", "Expand"}, "Normal");

    // Delay
    private final IntegerValue maxDelayValue = new IntegerValue("MaxDelay", 0, 0, 1000) {
        @Override
        protected void onChanged(final Integer oldValue, final Integer newValue) {
            final int i = minDelayValue.get();

            if (i > newValue)
                set(i);
        }
    };

    private final IntegerValue minDelayValue = new IntegerValue("MinDelay", 0, 0, 1000) {
        @Override
        protected void onChanged(final Integer oldValue, final Integer newValue) {
            final int i = maxDelayValue.get();

            if (i < newValue)
                set(i);
        }
    };
    private final BoolValue placeableDelay = new BoolValue("PlaceableDelay", false);

    // AutoBlock
    private final ListValue autoBlockValue = new ListValue("AutoBlock", new String[]{"Off", "Spoof", "Switch"}, "Spoof");

    // Basic stuff
    public final BoolValue sprintValue = new BoolValue("Sprint", true);
    private final BoolValue swingValue = new BoolValue("Swing", true);
    private final BoolValue searchValue = new BoolValue("Search", true);
    private final BoolValue downValue = new BoolValue("Down", true);
    private final BoolValue picker = new BoolValue("Picker", false);
    private final ListValue placeModeValue = new ListValue("PlaceTiming", new String[]{"Pre", "Post"}, "Post");

    // Eagle
    private final ListValue eagleValue = new ListValue("Eagle", new String[]{"Normal", "EdgeDistance", "Silent", "Off"}, "Off");
    private final IntegerValue blocksToEagleValue = new IntegerValue("BlocksToEagle", 0, 0, 10);
    private final FloatValue edgeDistanceValue = new FloatValue("EagleEdgeDistance", 0.2F, 0F, 0.5F);

    // Expand
    private final ListValue expandmode = new ListValue("ExpandMode",new String[]{"New","Old"},"New");
    private final IntegerValue expandLengthValue = new IntegerValue("ExpandLength", 5, 1, 6);

    // RotationStrafe
    private final BoolValue rotationStrafeValue = new BoolValue("RotationStrafe", false);

    // Rotations
    private final ListValue rotationModeValue = new ListValue("RotationMode", new String[]{"Normal", "Static", "StaticPitch", "StaticYaw", "Off"}, "Normal");
    private final BoolValue silentRotation = new BoolValue("SilentRotation", true);
    private final BoolValue keepRotationValue = new BoolValue("KeepRotation", false);
    private final IntegerValue keepLengthValue = new IntegerValue("KeepRotationLength", 0, 0, 20);
    private final FloatValue staticPitchValue = new FloatValue("StaticPitchOffset", 86F, 70F, 90F);
    private final FloatValue staticYawOffsetValue = new FloatValue("StaticYawOffset", 0F, 0F, 90F);


    // Other
    private final FloatValue xzRangeValue = new FloatValue("xzRange", 0.8F, 0.1F, 1.0F);
    private final FloatValue yRangeValue = new FloatValue("yRange", 0.8F, 0.1F, 1.0F);

    // SearchAccuracy
    private final IntegerValue searchAccuracyValue = new IntegerValue("SearchAccuracy", 8, 1, 24) {
        @Override
        protected void onChanged(final Integer oldValue, final Integer newValue) {
            if (getMaximum() < newValue) {
                set(getMaximum());
            } else if (getMinimum() > newValue) {
                set(getMinimum());
            }
        }
    };

    // Turn Speed
    private final FloatValue maxTurnSpeedValue = new FloatValue("MaxTurnSpeed", 180, 1, 180) {
        @Override
        protected void onChanged(final Float oldValue, final Float newValue) {
            float v = minTurnSpeedValue.get();
            if (v > newValue) set(v);
            if (getMaximum() < newValue) {
                set(getMaximum());
            } else if (getMinimum() > newValue) {
                set(getMinimum());
            }
        }
    };
    private final FloatValue minTurnSpeedValue = new FloatValue("MinTurnSpeed", 180, 1, 180) {
        @Override
        protected void onChanged(final Float oldValue, final Float newValue) {
            float v = maxTurnSpeedValue.get();
            if (v < newValue) set(v);
            if (getMaximum() < newValue) {
                set(getMaximum());
            } else if (getMinimum() > newValue) {
                set(getMinimum());
            }
        }
    };

    // Zitter
    private final BoolValue zitterValue = new BoolValue("Zitter", false);
    private final ListValue zitterModeValue = new ListValue("ZitterMode", new String[]{"Teleport", "Smooth"}, "Teleport");
    private final FloatValue zitterSpeed = new FloatValue("ZitterSpeed", 0.13F, 0.1F, 0.3F);
    private final FloatValue zitterStrength = new FloatValue("ZitterStrength", 0.072F, 0.05F, 0.2F);

    // Game
    private final ListValue render = new ListValue("Render", new String[] {"None", "Mark", "DrawCircle"}, "None");
    private final FloatValue timerValue = new FloatValue("Timer", 1F, 0.1F, 10F);
    private final FloatValue speedModifierValue = new FloatValue("SpeedModifier", 1F, 0, 2F);
    private final BoolValue slowValue = new BoolValue("Slow", false) {
        @Override
        protected void onChanged(final Boolean oldValue, final Boolean newValue) {
            if (newValue)
                sprintValue.set(false);
        }
    };
    private final FloatValue slowSpeed = new FloatValue("SlowSpeed", 0.6F, 0.2F, 0.8F);

    // Safety
    private final BoolValue sameYValue = new BoolValue("SameY", false);
    private final BoolValue safeWalkValue = new BoolValue("SafeWalk", true);
    private final BoolValue airSafeValue = new BoolValue("AirSafe", false);

    // Visuals
    private final BoolValue counterDisplayValue = new BoolValue("Counter", true);
    private final ListValue towermode = new ListValue("Tower", new String[] {"None", "Tower", "TowerMove"}, "None");
    private final BoolValue towerboostValue = new BoolValue("TowerBoost", true);

    /**
     * MODULE
     */

    // Target block
    private PlaceInfo targetPlace;

    // Launch position
    private int launchY;

    // Rotation lock
    private Rotation lockRotation;
    private Rotation limitedRotation;
    private boolean facesBlock = false;

    // Auto block slot
    private int slot;

    // Zitter Smooth
    private boolean zitterDirection;

    // Delay
    private final MSTimer delayTimer = new MSTimer();
    private final MSTimer zitterTimer = new MSTimer();
    private long delay;

    // Eagle
    private int placedBlocksWithoutEagle = 0;
    private boolean eagleSneaking;

    // Down
    private boolean shouldGoDown = false;

    /**
     * Enable module
     */
    @Override
    public void onEnable() {
        if (mc.thePlayer == null) return;

        launchY = (int) mc.thePlayer.posY;
    }

    /**
     * Update event
     *
     * @param event
     */
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        getBestBlocks();
        mc.timer.timerSpeed = timerValue.get();
        if(towermode.get().equalsIgnoreCase("Tower") && Keyboard.isKeyDown(Keyboard.KEY_SPACE) && !LiquidSense.moduleManager.getModule(Speed.class).getState()){
            mc.thePlayer.onGround = false;
            LiquidSense.moduleManager.getModule(Tower.class).setState(true);
            //System.out.println("1");
        }else{
            LiquidSense.moduleManager.getModule(Tower.class).setState(false);

        }

        shouldGoDown = downValue.get() && mc.gameSettings.isKeyDown(mc.gameSettings.keyBindSneak) && getBlocksAmount() > 1;
        if (shouldGoDown)
            mc.gameSettings.keyBindSneak.pressed = false;

        if (slowValue.get()) {
            mc.thePlayer.motionX *= slowSpeed.get();
            mc.thePlayer.motionZ *= slowSpeed.get();
        }

        if (mc.thePlayer.onGround) {
            final String mode = modeValue.get();

            // Rewinside scaffold mode
            if (mode.equalsIgnoreCase("Rewinside")) {
                MovementUtils.strafe(0.2F);
                mc.thePlayer.motionY = 0D;
            }

            // Smooth Zitter
            if (zitterValue.get() && zitterModeValue.get().equalsIgnoreCase("smooth")) {
                if (!GameSettings.isKeyDown(mc.gameSettings.keyBindRight))
                    mc.gameSettings.keyBindRight.pressed = false;

                if (!GameSettings.isKeyDown(mc.gameSettings.keyBindLeft))
                    mc.gameSettings.keyBindLeft.pressed= false;

                if (zitterTimer.hasTimePassed(100)) {
                    zitterDirection = !zitterDirection;
                    zitterTimer.reset();
                }

                if (zitterDirection) {
                    mc.gameSettings.keyBindRight.pressed = true;
                    mc.gameSettings.keyBindLeft.pressed = false;
                } else {
                    mc.gameSettings.keyBindRight.pressed= false;
                    mc.gameSettings.keyBindLeft.pressed = true;
                }
            }

            // Eagle
            if (!eagleValue.get().equalsIgnoreCase("Off") && !shouldGoDown) {
                double dif = 0.5D;
                if (eagleValue.get().equalsIgnoreCase("EdgeDistance") && !shouldGoDown) {
                    for (int i = 0; i < 4; i++) {
                        switch (i) {
                            case 0: {
                                final BlockPos blockPos = new BlockPos(mc.thePlayer.posX - 1, mc.thePlayer.posY - (mc.thePlayer.posY == (int) mc.thePlayer.posY + 0.5D ? 0D : 1.0D), mc.thePlayer.posZ);
                                final PlaceInfo placeInfo = PlaceInfo.get(blockPos);

                                if (BlockUtils.isReplaceable(blockPos) && placeInfo != null) {
                                    double calcDif = mc.thePlayer.posX - blockPos.getX();
                                    calcDif -= 0.5D;

                                    if (calcDif < 0)
                                        calcDif *= -1;
                                    calcDif -= 0.5;

                                    if (calcDif < dif)
                                        dif = calcDif;
                                }

                            }
                            case 1: {
                                final BlockPos blockPos = new BlockPos(mc.thePlayer.posX + 1, mc.thePlayer.posY - (mc.thePlayer.posY == (int) mc.thePlayer.posY + 0.5D ? 0D : 1.0D), mc.thePlayer.posZ);
                                final PlaceInfo placeInfo = PlaceInfo.get(blockPos);

                                if (BlockUtils.isReplaceable(blockPos) && placeInfo != null) {
                                    double calcDif = mc.thePlayer.posX - blockPos.getX();
                                    calcDif -= 0.5D;

                                    if (calcDif < 0)
                                        calcDif *= -1;
                                    calcDif -= 0.5;

                                    if (calcDif < dif)
                                        dif = calcDif;
                                }

                            }
                            case 2: {
                                final BlockPos blockPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - (mc.thePlayer.posY == (int) mc.thePlayer.posY + 0.5D ? 0D : 1.0D), mc.thePlayer.posZ - 1);
                                final PlaceInfo placeInfo = PlaceInfo.get(blockPos);

                                if (BlockUtils.isReplaceable(blockPos) && placeInfo != null) {
                                    double calcDif = mc.thePlayer.posZ - blockPos.getZ();
                                    calcDif -= 0.5D;

                                    if (calcDif < 0)
                                        calcDif *= -1;
                                    calcDif -= 0.5;

                                    if (calcDif < dif)
                                        dif = calcDif;
                                }

                            }
                            case 3: {
                                final BlockPos blockPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - (mc.thePlayer.posY == (int) mc.thePlayer.posY + 0.5D ? 0D : 1.0D), mc.thePlayer.posZ + 1);
                                final PlaceInfo placeInfo = PlaceInfo.get(blockPos);

                                if (BlockUtils.isReplaceable(blockPos) && placeInfo != null) {
                                    double calcDif = mc.thePlayer.posZ - blockPos.getZ();
                                    calcDif -= 0.5D;

                                    if (calcDif < 0)
                                        calcDif *= -1;
                                    calcDif -= 0.5;

                                    if (calcDif < dif)
                                        dif = calcDif;
                                }

                            }
                        }
                    }
                }

                if (placedBlocksWithoutEagle >= blocksToEagleValue.get()) {
                    final boolean shouldEagle = mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX,
                            mc.thePlayer.posY - 1D, mc.thePlayer.posZ)).getBlock() == Blocks.air || (dif < edgeDistanceValue.get() && eagleValue.get().equalsIgnoreCase("EdgeDistance"));

                    if (eagleValue.get().equalsIgnoreCase("Silent") && !shouldGoDown) {
                        if (eagleSneaking != shouldEagle) {
                            mc.getNetHandler().addToSendQueue(
                                    new C0BPacketEntityAction(mc.thePlayer, shouldEagle ?
                                            C0BPacketEntityAction.Action.START_SNEAKING :
                                            C0BPacketEntityAction.Action.STOP_SNEAKING)
                            );
                        }

                        eagleSneaking = shouldEagle;
                    } else
                        mc.gameSettings.keyBindSneak.pressed = shouldEagle;

                    placedBlocksWithoutEagle = 0;
                } else
                    placedBlocksWithoutEagle++;
            }

            // Zitter
            if (zitterValue.get() && zitterModeValue.get().equalsIgnoreCase("teleport")) {
                MovementUtils.strafe(zitterSpeed.get());


                final double yaw = Math.toRadians(mc.thePlayer.rotationYaw + (zitterDirection ? 90D : -90D));
                mc.thePlayer.motionX -= Math.sin(yaw) * zitterStrength.get();
                mc.thePlayer.motionZ += Math.cos(yaw) * zitterStrength.get();
                zitterDirection = !zitterDirection;
            }
        }
    }

    @EventTarget
    public void onPacket(final PacketEvent event) {
        if (mc.thePlayer == null)
            return;

        final Packet<?> packet = event.getPacket();

        // AutoBlock
        if(packet instanceof C09PacketHeldItemChange) {
            final C09PacketHeldItemChange packetHeldItemChange = (C09PacketHeldItemChange) packet;

            slot = packetHeldItemChange.getSlotId();
        }
    }

    @EventTarget
    private void onStrafe(StrafeEvent event) {

        if (!rotationStrafeValue.get())
            return;
        RotationUtils.serverRotation.applyStrafeToPlayer(event);
        event.cancelEvent();
    }
    @EventTarget
    public void onMotion(final MotionEvent event) {
        final EventState eventState = event.getEventState();
        final double y = mc.thePlayer.posY - 1.0;
        final double z = mc.thePlayer.posZ;
        final double x = mc.thePlayer.posX;

        if (towermode.get().equalsIgnoreCase("TowerMove")) {
            if (mc.gameSettings.keyBindJump.isKeyDown() && !autoBlockValue.get().equalsIgnoreCase("Off")) {
                mc.thePlayer.motionX = 0.0;
                mc.thePlayer.motionZ = 0.0;
                mc.thePlayer.jumpMovementFactor = 0.0f;
                final BlockPos blockBelow = new BlockPos(x, y, z);
                if (mc.theWorld.getBlockState(blockBelow).getBlock() == Blocks.air && this.targetPlace != null) {
                    mc.thePlayer.motionY = 0.4196;
                    mc.thePlayer.motionX *= 0.75;
                    mc.thePlayer.motionZ *= 0.75;
                    if(towerboostValue.get()) {
                        mc.timer.timerSpeed = 5F;
                    }
                }
            }
        }

        // Lock Rotation
        if (!rotationModeValue.get().equalsIgnoreCase("Off") && keepRotationValue.get() && lockRotation != null)
            setRotation(lockRotation);

        // Place block
        if ((facesBlock || rotationModeValue.get().equalsIgnoreCase("Off")) && placeModeValue.get().equalsIgnoreCase(eventState.getStateName()))
            place();

        // Update and search for new block
        if (eventState == EventState.PRE)
            update();

        // Reset placeable delay
        if (targetPlace == null && placeableDelay.get())
            delayTimer.reset();
    }

    private void update() {
        final boolean isHeldItemBlock = mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock;
        if (!autoBlockValue.get().equalsIgnoreCase("Off") ? InventoryUtils.findAutoBlockBlock() == -1 && !isHeldItemBlock : !isHeldItemBlock)
            return;

        findBlock(modeValue.get().equalsIgnoreCase("expand"));
    }

    private void setRotation(Rotation rotation, int keepRotation) {
        if (silentRotation.get()) {
            RotationUtils.setTargetRotation(rotation, keepRotation);
        } else {
            mc.thePlayer.rotationYaw = rotation.getYaw();
            mc.thePlayer.rotationPitch = rotation.getPitch();
        }
    }

    private void setRotation(Rotation rotation) {
        setRotation(rotation, 0);
    }

    /**
     * Search for new target block
     */
    private void findBlock(final boolean expand) {
        final BlockPos blockPosition = shouldGoDown ? (mc.thePlayer.posY == (int) mc.thePlayer.posY + 0.5D ?
                new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 0.6D, mc.thePlayer.posZ)
                : new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 0.6, mc.thePlayer.posZ).down()) :
                (mc.thePlayer.posY == (int) mc.thePlayer.posY + 0.5D ? new BlockPos(mc.thePlayer)
                        : new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ).down());

        if (!expand && (!BlockUtils.isReplaceable(blockPosition) || search(blockPosition, !shouldGoDown)))
            return;

        if (expand) {
            if(expandmode.get().equalsIgnoreCase("New") && !GameSettings.isKeyDown(mc.gameSettings.keyBindJump)){
                for (int i = 0; i < expandLengthValue.get(); i++) {
                    double[] coords = Expand(mc.thePlayer.posX, mc.thePlayer.posZ, mc.thePlayer.movementInput.moveForward, mc.thePlayer.movementInput.moveStrafe, mc.thePlayer.rotationYaw);
                    if (search(new BlockPos(coords[0], mc.thePlayer.posY -1, coords[1]), false))
                        return;
                }
            }else if (expandmode.get().equalsIgnoreCase("Old")){
                for (int i = 0; i < expandLengthValue.get(); i++) {
                    if (search(blockPosition.add(mc.thePlayer.getHorizontalFacing() == EnumFacing.WEST ? -i : mc.thePlayer.getHorizontalFacing() == EnumFacing.EAST ? i : 0,
                            0, mc.thePlayer.getHorizontalFacing() == EnumFacing.NORTH ? -i : mc.thePlayer.getHorizontalFacing() == EnumFacing.SOUTH ? i : 0), false))

                        return;
                }
            } else if(expandmode.get().equalsIgnoreCase("New") && GameSettings.isKeyDown(mc.gameSettings.keyBindJump)){
                for (int x = -1; x <= 1; x++)
                    for (int z = -1; z <= 1; z++)
                        if (search(blockPosition.add(x, 0, z), !shouldGoDown))
                            return;
            }
        } else if (searchValue.get()) {
            for (int x = -1; x <= 1; x++)
                for (int z = -1; z <= 1; z++)
                    if (search(blockPosition.add(x, 0, z), !shouldGoDown))
                        return;
        }
    }

    public double[] Expand(double x, double z, double forward, double strafe, float YAW) {
        BlockPos underPos = new BlockPos(x, mc.thePlayer.posY - 1, z);
        Block underBlock = mc.theWorld.getBlockState(underPos).getBlock();
        double xCalc = -999, zCalc = -999;
        double dist = 0;
        double expandDist = expandLengthValue.get()*2;
        while (!isAirBlock(underBlock)) {
            xCalc = x;
            zCalc = z;
            dist++;
            if (dist > expandDist) {
                dist = expandDist;
            }
            xCalc += (forward * 0.45 * Math.cos(Math.toRadians(YAW + 90.0f)) + strafe * 0.45 * Math.sin(Math.toRadians(YAW + 90.0f))) * dist;
            zCalc += (forward * 0.45 * Math.sin(Math.toRadians(YAW + 90.0f)) - strafe * 0.45 * Math.cos(Math.toRadians(YAW + 90.0f))) * dist;
            if (dist == expandDist) {
                break;
            }
            underPos = new BlockPos(xCalc, mc.thePlayer.posY - 1, zCalc);
            underBlock = mc.theWorld.getBlockState(underPos).getBlock();
        }
        return new double[]{xCalc, zCalc};
    }

    public boolean isAirBlock(Block block) {
        if (block.getMaterial().isReplaceable()) {
            if (block instanceof BlockSnow && block.getBlockBoundsMaxY() > 0.125) {
                return false;
            }
            return true;
        }

        return false;
    }

    /**
     * Place target block
     */
    private void place() {
        if (targetPlace == null) {
            if (placeableDelay.get())
                delayTimer.reset();
            return;
        }

        if (!delayTimer.hasTimePassed(delay) || (sameYValue.get() && launchY - 1 != (int) targetPlace.getVec3().yCoord))
            return;

        int blockSlot = -1;
        ItemStack itemStack = mc.thePlayer.getHeldItem();

        if (itemStack == null || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock)){
            if (autoBlockValue.get().equalsIgnoreCase("Off"))
                return;

            blockSlot = InventoryUtils.findAutoBlockBlock();

            if (blockSlot == -1)
                return;


            if (autoBlockValue.get().equalsIgnoreCase("Spoof")) {
                if (blockSlot - 36 != slot)
                    mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(blockSlot - 36));
            } else if(autoBlockValue.get().equalsIgnoreCase("Switch")) {
                mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(blockSlot - 36));
            } else {
                mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(blockSlot - 36));
                mc.playerController.updateController();
            }
            itemStack = mc.thePlayer.inventoryContainer.getSlot(blockSlot).getStack();
        }


        if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, itemStack, targetPlace.getBlockPos(), targetPlace.getEnumFacing(), targetPlace.getVec3())) {
            delayTimer.reset();
            delay = TimeUtils.randomDelay(minDelayValue.get(), maxDelayValue.get());

            if (mc.thePlayer.onGround) {
                final float modifier = speedModifierValue.get();
                mc.thePlayer.motionX *= modifier;
                mc.thePlayer.motionZ *= modifier;
            }

            if (swingValue.get())
                mc.thePlayer.swingItem();
            else
                mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
        }

        /*
        if (!stayAutoBlock.get() && blockSlot >= 0)
            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.getCurrentItem()));
         */

        // Reset
        this.targetPlace = null;
    }

    /**
     * Disable scaffold module
     */
    @Override
    public void onDisable() {
        LiquidSense.moduleManager.getModule(Tower.class).setState(false);
        if (mc.thePlayer == null) return;

        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindSneak)) {
            mc.gameSettings.keyBindSneak.pressed = false;

            if (eagleSneaking)
                mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
        }

        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindRight))
            mc.gameSettings.keyBindRight.pressed = false;

        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindLeft))
            mc.gameSettings.keyBindLeft.pressed = false;

        lockRotation = null;
        limitedRotation = null;
        facesBlock = false;
        mc.timer.timerSpeed = 1F;
        shouldGoDown = false;

        if (slot != mc.thePlayer.inventory.currentItem)
            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
    }

    /**
     * Entity movement event
     *
     * @param event
     */
    @EventTarget
    public void onMove(final MoveEvent event) {
        if (!safeWalkValue.get() || shouldGoDown)
            return;

        if (airSafeValue.get() || mc.thePlayer.onGround)
            event.setSafeWalk(true);
    }

    private ItemStack barrier = new ItemStack(Item.getItemById(166), 0, 0);

    @EventTarget
    public void onRender2D(final Render2DEvent event) {
        if(counterDisplayValue.get()) {
            GlStateManager.pushMatrix();
            final String info = "Blocks: " + getBlocksAmount();
            final ScaledResolution scaledResolution = new ScaledResolution(mc);
            float width = scaledResolution.getScaledWidth();
            float height = scaledResolution.getScaledHeight();
            int slot = InventoryUtils.findAutoBlockBlock();
            ItemStack stack = barrier;
            if (slot != -1) {
                if (mc.thePlayer.inventory.getCurrentItem() != null) {
                    Item handItem = mc.thePlayer.inventory.getCurrentItem().getItem();
                    if (handItem instanceof ItemBlock && InventoryUtils.canPlaceBlock(((ItemBlock) handItem).getBlock())) {
                        stack = mc.thePlayer.inventory.getCurrentItem();
                    }
                }
                if (stack == barrier) {
                    stack = mc.thePlayer.inventory.getStackInSlot(InventoryUtils.findAutoBlockBlock() - 36);
                    if (stack == null) {
                        stack = barrier;
                    }
                }
            }
            RenderHelper.enableGUIStandardItemLighting();
            mc.getRenderItem().renderItemIntoGUI(stack, (int) width / 2 - Fonts.font20.getStringWidth(info)
                    , (int) (height * 0.6 - Fonts.font20.FONT_HEIGHT * 0.5));
            RenderHelper.disableStandardItemLighting();
            Fonts.font20.drawCenteredString(info, width / 2f, height * 0.6f, Color.WHITE.getRGB(), false);
            GlStateManager.popMatrix();
        }
    }

    /**
     * Scaffold visuals
     *
     * @param event
     */
    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        final RenderManager renderManager = mc.getRenderManager();
        final Timer timer = mc.timer;
        if(render.get().equalsIgnoreCase("Mark")){
            for(int i = 0; i < (modeValue.get().equalsIgnoreCase("Expand") ? expandLengthValue.get() + 1 : 2); i++) {
                final BlockPos blockPos = new BlockPos(mc.thePlayer.posX + (mc.thePlayer.getHorizontalFacing() == EnumFacing.WEST ? -i : mc.thePlayer.getHorizontalFacing() == EnumFacing.EAST ? i : 0), mc.thePlayer.posY - (mc.thePlayer.posY == (int) mc.thePlayer.posY + 0.5D ? 0D : 1.0D), mc.thePlayer.posZ + (mc.thePlayer.getHorizontalFacing() == EnumFacing.NORTH ? -i : mc.thePlayer.getHorizontalFacing() == EnumFacing.SOUTH ? i : 0));
                final PlaceInfo placeInfo = PlaceInfo.get(blockPos);

                if(BlockUtils.isReplaceable(blockPos) && placeInfo != null) {
                    RenderUtils.drawBlockBox(blockPos, new Color(68, 117, 255, 100), false);
                    break;
                }
            }
        }
        if(render.get().equalsIgnoreCase("DrawCircle")){
            final double posX = mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * timer.renderPartialTicks - renderManager.renderPosX;
            final double posY = mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * timer.renderPartialTicks - renderManager.renderPosY;
            final double posZ = mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * timer.renderPartialTicks - renderManager.renderPosZ;
            RenderUtils.shadow(mc.thePlayer, posX, posY, posZ, 1, 64);
            RenderUtils.cylinder(mc.thePlayer, posX, posY, posZ, 1, 64);

        }


    }

    /**
     * Search for placeable block
     *
     * @param blockPosition pos
     * @param checks        visible
     * @return
     */
    private boolean search(final BlockPos blockPosition, final boolean checks) {
        if (!BlockUtils.isReplaceable(blockPosition))
            return false;
        // StaticModes
        final boolean staticMode = rotationModeValue.get().equalsIgnoreCase("Static");
        final boolean staticPitchMode = staticMode || rotationModeValue.get().equalsIgnoreCase("StaticPitch");
        final boolean staticYawMode = staticMode || rotationModeValue.get().equalsIgnoreCase("StaticYaw");
        final float staticPitch = staticPitchValue.get();
        final float staticYawOffset = staticYawOffsetValue.get();

        // SearchRanges
        final double xzRV = xzRangeValue.get();
        final double xzSSV = calcStepSize(xzRV);
        final double yRV = yRangeValue.get();
        final double ySSV = calcStepSize(yRV);

        double xSearchFace = 0;
        double ySearchFace = 0;
        double zSearchFace = 0;


        final Vec3 eyesPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);

        PlaceRotation placeRotation = null;

        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbor = blockPosition.offset(side);

            if (!BlockUtils.canBeClicked(neighbor))
                continue;

            final Vec3 dirVec = new Vec3(side.getDirectionVec());

            for (double xSearch = 0.5D - (xzRV / 2); xSearch <= 0.5D + (xzRV / 2); xSearch += xzSSV) {
                for (double ySearch = 0.5D - (yRV / 2); ySearch <= 0.5D + (yRV / 2); ySearch += ySSV) {
                    for (double zSearch = 0.5D - (xzRV / 2); zSearch <= 0.5D + (xzRV / 2); zSearch += xzSSV) {
                        final Vec3 posVec = new Vec3(blockPosition).addVector(xSearch, ySearch, zSearch);
                        final double distanceSqPosVec = eyesPos.squareDistanceTo(posVec);
                        final Vec3 hitVec = posVec.add(new Vec3(dirVec.xCoord * 0.5, dirVec.yCoord * 0.5, dirVec.zCoord * 0.5));

                        if (checks && (eyesPos.squareDistanceTo(hitVec) > 18D || distanceSqPosVec > eyesPos.squareDistanceTo(posVec.add(dirVec)) || mc.theWorld.rayTraceBlocks(eyesPos, hitVec, false, true, false) != null))
                            continue;

                        // face block
                        for (int i = 0; i < (staticYawMode ? 2 : 1); i++) {
                            final double diffX = staticYawMode && i == 0 ? 0 : hitVec.xCoord - eyesPos.xCoord;
                            final double diffY = hitVec.yCoord - eyesPos.yCoord;
                            final double diffZ = staticYawMode && i == 1 ? 0 : hitVec.zCoord - eyesPos.zCoord;

                            final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

                            final float pitch = staticPitchMode ? staticPitch : MathHelper.wrapAngleTo180_float((float) -Math.toDegrees(Math.atan2(diffY, diffXZ)));
                            final Rotation rotation = new Rotation(
                                    MathHelper.wrapAngleTo180_float((float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F +
                                            (staticYawMode ? staticYawOffset : 0)), pitch);

                            final Vec3 rotationVector = RotationUtils.getVectorForRotation(rotation);
                            final Vec3 vector = eyesPos.addVector(rotationVector.xCoord * 4, rotationVector.yCoord * 4, rotationVector.zCoord * 4);
                            final MovingObjectPosition obj = mc.theWorld.rayTraceBlocks(eyesPos, vector, false, false, true);

                            if (obj.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK || !obj.getBlockPos().equals(neighbor))
                                continue;

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

        if (placeRotation == null) return false;

        if (!rotationModeValue.get().equalsIgnoreCase("Off")) {
            if (minTurnSpeedValue.get() < 180) {
                limitedRotation = RotationUtils.limitAngleChange(RotationUtils.serverRotation, placeRotation.getRotation(), (float) (Math.random() * (maxTurnSpeedValue.get() - minTurnSpeedValue.get()) + minTurnSpeedValue.get()));
                setRotation(limitedRotation, keepLengthValue.get());
                lockRotation = limitedRotation;

                facesBlock = false;
                for (final EnumFacing side : EnumFacing.values()) {

                    final BlockPos neighbor = blockPosition.offset(side);

                    if (!BlockUtils.canBeClicked(neighbor))
                        continue;

                    final Vec3 dirVec = new Vec3(side.getDirectionVec());

                    final Vec3 posVec = new Vec3(blockPosition).addVector(xSearchFace, ySearchFace, zSearchFace);
                    final double distanceSqPosVec = eyesPos.squareDistanceTo(posVec);
                    final Vec3 hitVec = posVec.add(new Vec3(dirVec.xCoord * 0.5, dirVec.yCoord * 0.5, dirVec.zCoord * 0.5));

                    if (checks && (eyesPos.squareDistanceTo(hitVec) > 18D || distanceSqPosVec > eyesPos.squareDistanceTo(posVec.add(dirVec)) || mc.theWorld.rayTraceBlocks(eyesPos, hitVec, false, true, false) != null))
                        continue;

                    final Vec3 rotationVector = RotationUtils.getVectorForRotation(limitedRotation);
                    final Vec3 vector = eyesPos.addVector(rotationVector.xCoord * 4, rotationVector.yCoord * 4, rotationVector.zCoord * 4);
                    final MovingObjectPosition obj = mc.theWorld.rayTraceBlocks(eyesPos, vector, false, false, true);

                    if (!(obj.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && obj.getBlockPos().equals(neighbor)))
                        continue;
                    facesBlock = true;
                    break;
                }
            } else {
                setRotation(placeRotation.getRotation(), keepLengthValue.get());
                lockRotation = placeRotation.getRotation();
                facesBlock = true;
            }
        }
        targetPlace = placeRotation.getPlaceInfo();
        return true;
    }

    private double calcStepSize(double range) {
        double accuracy = searchAccuracyValue.get();
        accuracy += accuracy % 2; // If it is set to uneven it changes it to even. Fixes a bug
        if (range / accuracy < 0.01D)
            return 0.01D;
        return range / accuracy;
    }

    /**
     * @return hotbar blocks amount
     */
    private int getBlocksAmount() {
        int amount = 0;

        for(int i = 36; i < 45; i++) {
            final ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if(itemStack != null && itemStack.getItem() instanceof ItemBlock && InventoryUtils.canPlaceBlock(((ItemBlock)itemStack.getItem()).block))
                amount += itemStack.stackSize;
        }

        return amount;
    }

    @Override
    public String getTag() {
        return modeValue.get();
    }

    public void getBestBlocks(){

        if(getBlocksAmount() == 0)
            return;
        if(picker.get()){
            ItemStack is = new ItemStack(Item.getItemById(261));
            int bestInvSlot = getBiggestBlockSlotInv();
            int bestHotbarSlot = getBiggestBlockSlotHotbar();
            int bestSlot = getBiggestBlockSlotHotbar() > 0 ? getBiggestBlockSlotHotbar() : getBiggestBlockSlotInv();
            int spoofSlot = 42;
            if(bestHotbarSlot > 0 && bestInvSlot > 0){
                if (mc.thePlayer.inventoryContainer.getSlot(bestInvSlot).getHasStack() && mc.thePlayer.inventoryContainer.getSlot(bestHotbarSlot).getHasStack() ) {
                    if(mc.thePlayer.inventoryContainer.getSlot(bestHotbarSlot).getStack().stackSize < mc.thePlayer.inventoryContainer.getSlot(bestInvSlot).getStack().stackSize){
                        bestSlot = bestInvSlot;
                    }
                }
            }
            if(hotbarContainBlock()){
                for (int a = 36; a < 45; a++) {
                    if (mc.thePlayer.inventoryContainer.getSlot(a).getHasStack()) {
                        Item item = mc.thePlayer.inventoryContainer.getSlot(a).getStack().getItem();
                        if(item instanceof ItemBlock){
                            spoofSlot = a;
                            break;
                        }
                    }
                }
            }else{
                for (int a = 36; a < 45; a++) {
                    if (!mc.thePlayer.inventoryContainer.getSlot(a).getHasStack()) {
                        spoofSlot = a;
                        break;
                    }
                }
            }

            if (mc.thePlayer.inventoryContainer.getSlot(spoofSlot).slotNumber != bestSlot) {

                swap(bestSlot, spoofSlot - 36);
                mc.playerController.updateController();


            }
        }else{
            if (invCheck()) {

                ItemStack is = new ItemStack(Item.getItemById(261));
                for (int i = 9; i < 36; i++) {

                    if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                        Item item = mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
                        int count = 0;
                        if (item instanceof ItemBlock) {
                            for (int a = 36; a < 45; a++) {
                                if (mc.thePlayer.inventoryContainer.canAddItemToSlot(mc.thePlayer.inventoryContainer.getSlot(a), is, true)) {

                                    swap(i, a - 36);
                                    count++;
                                    break;
                                }
                            }

                            if (count == 0) {

                                swap(i, 7);
                            }
                            break;

                        }
                    }
                }
            }
        }
    }

    private boolean hotbarContainBlock() {
        int i = 36;

        while (i < 45) {
            try {
                ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if ((stack == null) || (stack.getItem() == null) || !(stack.getItem() instanceof ItemBlock)) {
                    i++;
                    continue;
                }
                return true;
            } catch (Exception e) {

            }
        }

        return false;

    }

    public int getBiggestBlockSlotHotbar(){
        int slot = -1;
        int size = 0;
        if(getBlocksAmount() == 0)
            return - 1;
        for (int i = 36; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                Item item = mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (item instanceof ItemBlock) {
                    if(is.stackSize > size){
                        size = is.stackSize;
                        slot = i;
                    }
                }
            }
        }
        return slot;
    }

    protected void swap(int slot, int hotbarNum) {

        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum,2, mc.thePlayer);

    }

    private boolean invCheck() {
        for (int i = 36; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                Item item = mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
                if (item instanceof ItemBlock) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getBiggestBlockSlotInv(){
        int slot = -1;
        int size = 0;
        if(getBlocksAmount() == 0)
            return - 1;
        for (int i = 9; i < 36; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                Item item = mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (item instanceof ItemBlock) {
                    if(is.stackSize > size){
                        size = is.stackSize;
                        slot = i;
                    }
                }
            }
        }
        return slot;
    }


}
