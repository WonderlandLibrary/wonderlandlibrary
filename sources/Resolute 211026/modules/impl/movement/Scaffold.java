// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.movement;

import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockSlab;
import java.util.HashMap;
import net.minecraft.util.MovingObjectPosition;
import vip.Resolute.util.world.RandomUtil;
import vip.Resolute.util.player.RayCastUtil;
import java.util.ArrayList;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockContainer;
import org.apache.commons.lang3.RandomUtils;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import vip.Resolute.Resolute;
import net.minecraft.util.Vec3;
import vip.Resolute.util.misc.MathUtils;
import net.minecraft.util.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.block.BlockAir;
import java.util.concurrent.ThreadLocalRandom;
import vip.Resolute.util.player.Rotation;
import vip.Resolute.util.world.SetBlockAndFacingUtils;
import net.minecraft.network.play.client.C03PacketPlayer;
import vip.Resolute.util.world.Vec3d;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.MovementInput;
import vip.Resolute.util.movement.MovementUtils;
import vip.Resolute.util.player.PlayerUtil;
import vip.Resolute.util.player.InventoryUtils;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.potion.Potion;
import vip.Resolute.events.impl.EventMotion;
import vip.Resolute.util.render.RenderUtils;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import vip.Resolute.events.impl.EventRender2D;
import vip.Resolute.events.impl.EventSafeWalk;
import vip.Resolute.events.Event;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import vip.Resolute.settings.Setting;
import java.util.Arrays;
import net.minecraft.init.Blocks;
import java.util.Random;
import net.minecraft.item.ItemStack;
import vip.Resolute.util.misc.TimerUtil;
import vip.Resolute.util.misc.TimerUtils;
import net.minecraft.block.Block;
import java.util.List;
import java.util.Map;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.settings.impl.BooleanSetting;
import vip.Resolute.settings.impl.ModeSetting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import vip.Resolute.modules.Module;

public class Scaffold extends Module
{
    private static final BlockPos[] BLOCK_POSITIONS;
    private static final EnumFacing[] FACINGS;
    private ModeSetting scaffoldmode;
    private ModeSetting towerMode;
    private BooleanSetting keeprots;
    private BooleanSetting downwards;
    private NumberSetting delay;
    public NumberSetting sneakAfter;
    public BooleanSetting sneak;
    public BooleanSetting altRots;
    public NumberSetting slowdownMod;
    public ModeSetting rayCastMode;
    private NumberSetting timerSpeed;
    private NumberSetting watchdogStep;
    private NumberSetting watchdog2Step;
    private BooleanSetting cancelSpeed;
    private NumberSetting modifier;
    private NumberSetting expandDistance;
    private NumberSetting blockOverride;
    public BooleanSetting watchdogBoost;
    public NumberSetting watchdogValue;
    public ModeSetting sprintMode;
    public BooleanSetting ncpStep;
    public NumberSetting ncpStepAngle;
    public BooleanSetting cancelTowerSpeed;
    public BooleanSetting bypass;
    public static BooleanSetting sprint;
    private BooleanSetting timerboost;
    private BooleanSetting safewalk;
    private BooleanSetting tower;
    private BooleanSetting towermove;
    private BooleanSetting swing;
    private BooleanSetting boolkeepY;
    private BooleanSetting rayCast;
    private BooleanSetting spoofSprint;
    private BooleanSetting esp;
    private float[] rotations;
    private static final Map<Integer, Boolean> glCapMap;
    private List<Block> badBlocks;
    private BlockData blockData;
    public static boolean isPlaceTick;
    public static boolean stopWalk;
    private double startY;
    public TimerUtils towerTimer;
    private TimerUtils timer;
    private TimerUtils slotTimer;
    private TimerUtils boostTimer;
    private TimerUtils reduceTimer;
    private BlockData lastBlockData;
    float yaw;
    float pitch;
    int ticks;
    float x;
    float y;
    float percentage;
    float width;
    float half;
    private int count;
    public static int heldItem;
    int hotBarSlot;
    private static List<Block> invalidBlocks;
    private final BlockPos[] blockPositions;
    private final EnumFacing[] facings;
    private int bestBlockStack;
    private BlockData data;
    private int blockCount;
    private double keepY;
    public boolean isSprinting;
    int usedTicks;
    int placeCounter;
    BlockPos blockUnder;
    boolean override;
    private final TimerUtil clickTimer;
    private float[] angles;
    private static List<Block> blacklistedBlocks;
    private final TimerUtil sigmaTimer;
    double oldY;
    private int sigmaY;
    int i;
    ItemStack stack;
    int blockSlot;
    public BlockPos finalPos;
    public int sneakCount;
    public float aacyaw;
    public float aacpitch;
    public float speed;
    public static boolean enabled;
    TimerUtil timeUtil;
    static Random rng;
    int slotIndex;
    
    public Scaffold() {
        super("Scaffold", 34, "Automatically places blocks under you", Category.MOVEMENT);
        this.scaffoldmode = new ModeSetting("Mode", "NCP", new String[] { "NCP", "Normal", "Hypixel", "Matrix", "Redesky", "Expand" });
        this.towerMode = new ModeSetting("Tower Mode", "NCP", new String[] { "NCP" });
        this.keeprots = new BooleanSetting("Keep Rots", true);
        this.downwards = new BooleanSetting("Downwards", false, () -> this.scaffoldmode.is("Normal"));
        this.delay = new NumberSetting("Delay", 0.0, 0.0, 1000.0, 10.0);
        this.sneakAfter = new NumberSetting("Sneak After", 0.0, () -> this.scaffoldmode.is("Matrix"), 0.0, 10.0, 1.0);
        this.sneak = new BooleanSetting("Sneak", false, () -> this.scaffoldmode.is("Matrix"));
        this.altRots = new BooleanSetting("Alt Rotations", false, () -> this.scaffoldmode.is("Matrix"));
        this.slowdownMod = new NumberSetting("Slowdown Modifier", 0.6, () -> this.scaffoldmode.is("Matrix"), 0.1, 1.0, 0.1);
        this.rayCastMode = new ModeSetting("Raycast Mode", "None", () -> this.scaffoldmode.is("Matrix"), new String[] { "None", "Fix", "Full" });
        this.timerSpeed = new NumberSetting("Timer Speed", 4.0, 1.0, 10.0, 1.0);
        this.watchdogStep = new NumberSetting("Watchdog Step", 1.0, () -> this.scaffoldmode.is("Watchdog"), 1.0, 45.0, 1.0);
        this.watchdog2Step = new NumberSetting("Watchdog2 Step", 1.0, () -> this.scaffoldmode.is("Watchdog2"), 1.0, 45.0, 1.0);
        this.cancelSpeed = new BooleanSetting("Cancel Speed", true);
        this.modifier = new NumberSetting("Modifier", 1.0, () -> this.scaffoldmode.is("Watchdog2"), 0.8, 1.5, 0.1);
        this.expandDistance = new NumberSetting("Expand Value", 0.5, () -> this.scaffoldmode.is("Expand"), 0.1, 5.0, 0.1);
        this.blockOverride = new NumberSetting("Block Slot", 9.0, 1.0, 9.0, 1.0);
        this.watchdogBoost = new BooleanSetting("Watchdog Boost", false, () -> this.scaffoldmode.is("Watchdog2"));
        this.watchdogValue = new NumberSetting("Watchdog Value", 1.6, () -> this.scaffoldmode.is("Watchdog2"), 1.0, 5.0, 0.1);
        this.sprintMode = new ModeSetting("Sprint Mode", "Cancel", () -> this.scaffoldmode.is("Watchdog2"), new String[] { "Cancel", "Full" });
        this.ncpStep = new BooleanSetting("NCP Step", false, () -> this.scaffoldmode.is("NCP"));
        this.ncpStepAngle = new NumberSetting("NCP Angle Step", 45.0, () -> this.ncpStep.isEnabled() && this.ncpStep.isAvailable(), 5.0, 180.0, 5.0);
        this.cancelTowerSpeed = new BooleanSetting("Cancel Tower Speed", true, () -> this.scaffoldmode.is("NCP"));
        this.bypass = new BooleanSetting("Bypass", true, () -> this.scaffoldmode.is("NCP"));
        this.timerboost = new BooleanSetting("TimerBoost", false);
        this.safewalk = new BooleanSetting("Safewalk", true);
        this.tower = new BooleanSetting("Tower", false);
        this.towermove = new BooleanSetting("Tower Move", false);
        this.swing = new BooleanSetting("Swing", false);
        this.boolkeepY = new BooleanSetting("KeepY", false);
        this.rayCast = new BooleanSetting("RayCast", false);
        this.spoofSprint = new BooleanSetting("Disable Sprint Packet", false);
        this.esp = new BooleanSetting("Indicator", true);
        this.rotations = new float[2];
        this.badBlocks = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.snow_layer, Blocks.ice, Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore, Blocks.chest, Blocks.trapped_chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.tnt, Blocks.gold_ore, Blocks.iron_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.quartz_ore, Blocks.redstone_ore, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.tallgrass, Blocks.tripwire, Blocks.tripwire_hook, Blocks.rail, Blocks.waterlily, Blocks.red_flower, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.vine, Blocks.trapdoor, Blocks.yellow_flower, Blocks.ladder, Blocks.furnace, Blocks.sand, Blocks.cactus, Blocks.dispenser, Blocks.noteblock, Blocks.dropper, Blocks.crafting_table, Blocks.web, Blocks.pumpkin, Blocks.sapling, Blocks.cobblestone_wall, Blocks.oak_fence);
        this.towerTimer = new TimerUtils();
        this.timer = new TimerUtils();
        this.slotTimer = new TimerUtils();
        this.boostTimer = new TimerUtils();
        this.reduceTimer = new TimerUtils();
        this.yaw = 0.0f;
        this.pitch = 0.0f;
        this.ticks = 0;
        this.blockPositions = new BlockPos[] { new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, 0, 1) };
        this.facings = new EnumFacing[] { EnumFacing.EAST, EnumFacing.WEST, EnumFacing.SOUTH, EnumFacing.NORTH };
        this.clickTimer = new TimerUtil();
        this.sigmaTimer = new TimerUtil();
        this.oldY = 0.0;
        this.sigmaY = 0;
        this.timeUtil = new TimerUtil();
        this.slotIndex = 0;
        this.addSettings(this.scaffoldmode, this.keeprots, this.downwards, this.delay, this.watchdogStep, this.watchdog2Step, this.cancelSpeed, this.modifier, Scaffold.sprint, this.timerboost, this.expandDistance, this.blockOverride, this.sprintMode, this.ncpStep, this.ncpStepAngle, this.cancelTowerSpeed, this.bypass, this.watchdogBoost, this.watchdogValue, this.sneakAfter, this.sneak, this.altRots, this.slowdownMod, this.rayCastMode, this.timerSpeed, this.safewalk, this.tower, this.towermove, this.swing, this.boolkeepY, this.rayCast, this.spoofSprint, this.esp);
    }
    
    @Override
    public void onEnable() {
        this.timer.reset();
        this.boostTimer.reset();
        this.slotTimer.reset();
        this.reduceTimer.reset();
        this.ticks = 0;
        this.lastBlockData = null;
        this.startY = Scaffold.mc.thePlayer.getEntityBoundingBox().minY - 1.0;
        Scaffold.enabled = true;
        this.blockCount = 0;
        this.data = null;
        this.keepY = Scaffold.mc.thePlayer.posY - 1.0;
        if (this.scaffoldmode.is("Redesky")) {
            this.blockCount = 0;
            this.oldY = Scaffold.mc.thePlayer.posY - 1.0;
            if (this.boolkeepY.isEnabled()) {
                this.sigmaTimer.reset();
            }
        }
        this.usedTicks = 0;
        this.placeCounter = 0;
        if (Scaffold.mc.thePlayer != null && Scaffold.mc.theWorld != null) {
            if (this.sprintMode.is("Cancel") && Scaffold.mc.thePlayer != null && Scaffold.mc.theWorld != null) {
                Scaffold.mc.thePlayer.setSprinting(false);
            }
            Scaffold.enabled = true;
            if (Scaffold.mc.thePlayer != null) {
                Scaffold.heldItem = Scaffold.mc.thePlayer.inventory.currentItem;
            }
        }
        if (Scaffold.mc.thePlayer != null) {
            Scaffold.heldItem = Scaffold.mc.thePlayer.inventory.currentItem;
        }
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        Scaffold.mc.timer.timerSpeed = 1.0f;
        Scaffold.mc.gameSettings.keyBindSneak.pressed = false;
        Scaffold.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(Scaffold.mc.thePlayer.inventory.currentItem));
        Scaffold.mc.getNetHandler().getNetworkManager().sendPacket(new C0BPacketEntityAction(Scaffold.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
        Scaffold.enabled = false;
        this.usedTicks = 0;
        Scaffold.mc.gameSettings.keyBindSneak.pressed = false;
        Scaffold.mc.thePlayer.movementInput.sneak = false;
        Scaffold.mc.thePlayer.inventory.currentItem = Scaffold.heldItem;
        this.setSneaking(false);
    }
    
    @Override
    public void onEvent(final Event e) {
        this.setSuffix("");
        try {
            if (e instanceof EventSafeWalk && this.safewalk.isEnabled()) {
                e.setCancelled(true);
            }
            if (e instanceof EventRender2D && this.esp.isEnabled()) {
                final ScaledResolution resolution = new ScaledResolution(Scaffold.mc);
                final float x = resolution.getScaledWidth() / 2.0f;
                final float y = resolution.getScaledHeight() / 2.0f + 15.0f;
                final float percentage = Math.min(1.0f, this.blockCount / 128.0f);
                final float width = 80.0f;
                final float half = width / 2.0f;
                Gui.drawRect(x - half - 0.5f, y - 2.0f, x + half + 0.5f, y + 2.0f, 2013265920);
                GL11.glEnable(3089);
                RenderUtils.startScissorBox(resolution, (int)(x - half), (int)y - 2, (int)(width * percentage), 4);
                RenderUtils.drawGradientRect(x - half, y - 1.5f, x - half + width, y + 1.5f, true, -1571930, -16711936);
                GL11.glDisable(3089);
            }
            if (e instanceof EventMotion) {
                this.updateBlockCount();
                if (e.isPre() && this.spoofSprint.isEnabled()) {
                    final NetHandlerPlayClient netHandler = Scaffold.mc.getNetHandler();
                    netHandler.sendPacketNoEvent(new C0BPacketEntityAction(Scaffold.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                }
                if (this.scaffoldmode.is("Matrix")) {
                    final EventMotion event = (EventMotion)e;
                    if (e.isPre()) {
                        event.setYaw(this.aacyaw);
                        event.setPitch(this.aacpitch);
                        final BlockPos pos = new BlockPos(Scaffold.mc.thePlayer.posX, Scaffold.mc.thePlayer.getEntityBoundingBox().minY - 1.0, Scaffold.mc.thePlayer.posZ);
                        Scaffold.mc.thePlayer.setSprinting(Scaffold.sprint.isEnabled());
                        this.getBlockPosToPlaceOn(pos);
                        this.aacpitch = this.getPitch();
                        if (this.altRots.isEnabled()) {
                            this.setYawSimple();
                        }
                        else {
                            this.setYaw();
                        }
                        if (Scaffold.mc.thePlayer.isPotionActive(Potion.moveSpeed) && this.cancelSpeed.isEnabled()) {
                            final EntityPlayerSP thePlayer = Scaffold.mc.thePlayer;
                            thePlayer.motionX *= 0.8180000185966492;
                            final EntityPlayerSP thePlayer2 = Scaffold.mc.thePlayer;
                            thePlayer2.motionZ *= 0.8180000185966492;
                        }
                        final EntityPlayerSP thePlayer3 = Scaffold.mc.thePlayer;
                        thePlayer3.motionX *= this.slowdownMod.getValue();
                        final EntityPlayerSP thePlayer4 = Scaffold.mc.thePlayer;
                        thePlayer4.motionZ *= this.slowdownMod.getValue();
                    }
                }
                if (this.scaffoldmode.is("Redesky")) {
                    final EventMotion event = (EventMotion)e;
                    final BlockPos underPos = new BlockPos(Scaffold.mc.thePlayer.posX, Scaffold.mc.thePlayer.posY - 1.0, Scaffold.mc.thePlayer.posZ);
                    final BlockData data = this.getBlockData(underPos);
                    if (this.getBlockSlot() == -1) {
                        return;
                    }
                    if (Scaffold.mc.theWorld.getBlockState(underPos).getBlock().getMaterial().isReplaceable() && data != null) {
                        if (e.isPre()) {
                            final BlockPos pos2 = new BlockPos(Scaffold.mc.thePlayer.posX, Scaffold.mc.thePlayer.posY - 1.0, Scaffold.mc.thePlayer.posZ);
                            final BlockData blockData = this.getBlockData(pos2);
                            this.blockData = blockData;
                            if (data.face == EnumFacing.UP) {
                                Scaffold.mc.timer.timerSpeed = 1.0f;
                                event.setPitch(90.0f);
                            }
                            final float[] facing = this.getRotationsAAC(blockData.position, blockData.face);
                            final float yaw = facing[0];
                            final float pitch = facing[1];
                            event.setYaw(yaw);
                        }
                        else if (this.getBlockSlot() != -1) {
                            Scaffold.mc.thePlayer.inventory.currentItem = this.getBlockSlot();
                            final float[] facing2 = this.getRotationsAAC(this.blockData.position, this.blockData.face);
                            final float yaw2 = facing2[0];
                            Scaffold.mc.playerController.onPlayerRightClick(Scaffold.mc.thePlayer, Scaffold.mc.theWorld, Scaffold.mc.thePlayer.inventory.getCurrentItem(), data.position, data.face, this.getVec3(data));
                            if (this.swing.isEnabled()) {
                                Scaffold.mc.thePlayer.swingItem();
                            }
                            else {
                                Scaffold.mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                            }
                        }
                    }
                }
                if (this.scaffoldmode.is("Hypixel")) {
                    final EventMotion event = (EventMotion)e;
                    if (Scaffold.mc.thePlayer.isPotionActive(Potion.moveSpeed) && this.cancelSpeed.isEnabled()) {
                        final EntityPlayerSP thePlayer5 = Scaffold.mc.thePlayer;
                        thePlayer5.motionX *= 0.8180000185966492;
                        final EntityPlayerSP thePlayer6 = Scaffold.mc.thePlayer;
                        thePlayer6.motionZ *= 0.8180000185966492;
                    }
                    if (e.isPre()) {
                        this.data = this.getBlockData();
                        this.bestBlockStack = this.findBestBlockStack();
                        if (this.bestBlockStack != -1) {
                            if (this.bestBlockStack < 36 && this.clickTimer.hasElapsed((long)this.delay.getValue())) {
                                this.override = true;
                                this.i = 44;
                                while (this.i >= 36) {
                                    this.stack = this.getStackInSlot(this.i);
                                    if (!InventoryUtils.isValid(this.stack)) {
                                        InventoryUtils.windowClick(this.bestBlockStack, this.i - 36, InventoryUtils.ClickType.SWAP_WITH_HOT_BAR_SLOT);
                                        this.bestBlockStack = this.i;
                                        this.override = false;
                                        break;
                                    }
                                    --this.i;
                                }
                                if (this.override) {
                                    this.blockSlot = (int)(this.blockOverride.getValue() - 1.0);
                                    InventoryUtils.windowClick(this.bestBlockStack, this.blockSlot, InventoryUtils.ClickType.SWAP_WITH_HOT_BAR_SLOT);
                                    this.bestBlockStack = this.blockSlot + 36;
                                }
                            }
                            if (this.data != null && this.bestBlockStack >= 36) {
                                Scaffold.mc.getNetHandler().getNetworkManager().sendPacket(new C0BPacketEntityAction(Scaffold.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
                                Scaffold.mc.thePlayer.setPosition(Scaffold.mc.thePlayer.posX, Scaffold.mc.thePlayer.posY, Scaffold.mc.thePlayer.posZ);
                                this.rotations = getScaffoldRotations(this.data);
                                event.setYaw(this.rotations[0]);
                                event.setPitch(this.rotations[1]);
                            }
                        }
                    }
                    else if (this.data != null && this.bestBlockStack != -1 && this.bestBlockStack >= 36) {
                        Scaffold.mc.getNetHandler().getNetworkManager().sendPacket(new C0BPacketEntityAction(Scaffold.mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
                        this.hotBarSlot = this.bestBlockStack - 36;
                        if (Scaffold.mc.thePlayer.inventory.currentItem != this.hotBarSlot) {
                            Scaffold.mc.thePlayer.inventory.currentItem = this.hotBarSlot;
                        }
                        event.setOnGround(false);
                        Scaffold.mc.getNetHandler().getNetworkManager().sendPacket(new C0BPacketEntityAction(Scaffold.mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
                        if (Scaffold.mc.playerController.onPlayerRightClick(Scaffold.mc.thePlayer, Scaffold.mc.theWorld, Scaffold.mc.thePlayer.getHeldItem(), this.data.position, this.data.face, PlayerUtil.getVectorForRotation(this.rotations[0], this.rotations[1]))) {
                            if (!this.swing.isEnabled()) {
                                Scaffold.mc.getNetHandler().getNetworkManager().sendPacket(new C0APacketAnimation());
                            }
                            else {
                                Scaffold.mc.thePlayer.swingItem();
                            }
                            this.data = null;
                        }
                    }
                }
                if (this.scaffoldmode.is("NCP")) {
                    final EventMotion event = (EventMotion)e;
                    if (Scaffold.mc.thePlayer.isPotionActive(Potion.moveSpeed) && this.cancelSpeed.isEnabled()) {
                        final EntityPlayerSP thePlayer7 = Scaffold.mc.thePlayer;
                        thePlayer7.motionX *= 0.8180000185966492;
                        final EntityPlayerSP thePlayer8 = Scaffold.mc.thePlayer;
                        thePlayer8.motionZ *= 0.8180000185966492;
                    }
                    if (e.isPre()) {
                        this.data = null;
                        this.bestBlockStack = this.findBestBlockStack();
                        if (this.bestBlockStack != -1) {
                            if (this.bestBlockStack < 36 && this.clickTimer.hasElapsed((long)this.delay.getValue())) {
                                this.override = true;
                                this.i = 44;
                                while (this.i >= 36) {
                                    this.stack = this.getStackInSlot(this.i);
                                    if (!InventoryUtils.isValid(this.stack)) {
                                        InventoryUtils.windowClick(this.bestBlockStack, this.i - 36, InventoryUtils.ClickType.SWAP_WITH_HOT_BAR_SLOT);
                                        this.bestBlockStack = this.i;
                                        this.override = false;
                                        break;
                                    }
                                    --this.i;
                                }
                                if (this.override) {
                                    this.blockSlot = (int)(this.blockOverride.getValue() - 1.0);
                                    InventoryUtils.windowClick(this.bestBlockStack, this.blockSlot, InventoryUtils.ClickType.SWAP_WITH_HOT_BAR_SLOT);
                                    this.bestBlockStack = this.blockSlot + 36;
                                }
                            }
                            final boolean isKeepY = this.boolkeepY.isEnabled();
                            BlockPos blockUnder = this.getBlockUnder(isKeepY);
                            BlockData data2 = this.getBlockData2(blockUnder);
                            if (isKeepY && (Math.abs(Scaffold.mc.thePlayer.posY - this.keepY) > (MovementUtils.isMoving() ? 4.0 : 1.0) || data2 == null)) {
                                this.keepY = Scaffold.mc.thePlayer.posY - 1.0;
                            }
                            if (data2 == null) {
                                if (isKeepY) {
                                    blockUnder = this.getBlockUnder(true);
                                }
                                data2 = this.getBlockData2(blockUnder.offset(EnumFacing.DOWN));
                            }
                            if (data2 != null && this.bestBlockStack >= 36) {
                                if (this.bypass.isEnabled()) {
                                    Scaffold.mc.getNetHandler().getNetworkManager().sendPacket(new C0BPacketEntityAction(Scaffold.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
                                    Scaffold.mc.thePlayer.setPosition(Scaffold.mc.thePlayer.posX, Scaffold.mc.thePlayer.posY, Scaffold.mc.thePlayer.posZ);
                                }
                                if (validateReplaceable(data2) && data2.hitVec != null) {
                                    if (this.ncpStep.isEnabled()) {
                                        this.angles = this.getRotations(event, data2.hitVec, (float)this.ncpStepAngle.getValue());
                                    }
                                    else {
                                        this.angles = getRotations(data2.hitVec);
                                    }
                                }
                                else {
                                    data2 = null;
                                }
                            }
                            if (this.angles != null) {
                                event.setYaw(this.angles[0]);
                                event.setPitch(this.angles[1]);
                            }
                            this.data = data2;
                        }
                    }
                    else if (this.data != null && this.bestBlockStack != -1 && this.bestBlockStack >= 36) {
                        this.hotBarSlot = this.bestBlockStack - 36;
                        if (Scaffold.mc.thePlayer.inventory.currentItem != this.hotBarSlot) {
                            Scaffold.mc.thePlayer.inventory.currentItem = this.hotBarSlot;
                        }
                        if (this.bypass.isEnabled()) {
                            event.setOnGround(false);
                            Scaffold.mc.getNetHandler().getNetworkManager().sendPacket(new C0BPacketEntityAction(Scaffold.mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
                        }
                        if (Scaffold.mc.playerController.onPlayerRightClick(Scaffold.mc.thePlayer, Scaffold.mc.theWorld, Scaffold.mc.thePlayer.getCurrentEquippedItem(), this.data.position, this.data.face, this.data.hitVec)) {
                            if (this.tower.isEnabled() && Scaffold.mc.gameSettings.keyBindJump.isKeyDown()) {
                                if (!this.towermove.isEnabled() && !MovementUtils.isMoving()) {
                                    if (this.cancelTowerSpeed.isEnabled()) {
                                        Scaffold.mc.thePlayer.motionX = 0.0;
                                        Scaffold.mc.thePlayer.motionZ = 0.0;
                                    }
                                    Scaffold.mc.thePlayer.motionY = MovementUtils.getJumpHeight(0.41999998688697815);
                                    if (this.towerTimer.hasTimeElapsed(1500L, true)) {
                                        Scaffold.mc.thePlayer.motionY = -0.28;
                                        this.towerTimer.reset();
                                    }
                                }
                                else if (this.towermove.isEnabled()) {
                                    if (this.cancelTowerSpeed.isEnabled()) {
                                        Scaffold.mc.thePlayer.motionX = 0.0;
                                        Scaffold.mc.thePlayer.motionZ = 0.0;
                                    }
                                    Scaffold.mc.thePlayer.motionY = MovementUtils.getJumpHeight(0.41999998688697815);
                                    if (this.towerTimer.hasTimeElapsed(1500L, true)) {
                                        Scaffold.mc.thePlayer.motionY = -0.28;
                                        this.towerTimer.reset();
                                    }
                                }
                            }
                            if (this.swing.isEnabled()) {
                                Scaffold.mc.thePlayer.swingItem();
                            }
                            else {
                                Scaffold.mc.getNetHandler().sendPacketNoEvent(new C0APacketAnimation());
                            }
                        }
                    }
                }
                if (this.scaffoldmode.is("Expand")) {
                    final EventMotion event = (EventMotion)e;
                    final double addition = this.expandDistance.getValue();
                    final double x2 = Math.cos(Math.toRadians(Scaffold.mc.thePlayer.rotationYaw + 90.0f));
                    final double z2 = Math.sin(Math.toRadians(Scaffold.mc.thePlayer.rotationYaw + 90.0f));
                    final double xOffset = MovementInput.moveForward * addition * x2 + MovementInput.moveStrafe * addition * z2;
                    final double zOffset = MovementInput.moveForward * addition * z2 - MovementInput.moveStrafe * addition * x2;
                    final BlockPos blockBelow = new BlockPos(Scaffold.mc.thePlayer.posX + xOffset, Scaffold.mc.thePlayer.posY - 1.0, Scaffold.mc.thePlayer.posZ + zOffset);
                    BlockData blockEntry = (Scaffold.mc.theWorld.getBlockState(blockBelow).getBlock() == Blocks.air) ? (blockEntry = this.getBlockData2(blockBelow)) : null;
                    if (blockEntry == null && this.lastBlockData != null && event.isPre()) {
                        final float[] rotations = getRotationsNeeded(this.lastBlockData);
                        event.setPitch(rotations[1]);
                        event.setYaw(rotations[0]);
                    }
                    if (blockEntry == null) {
                        return;
                    }
                    if (event.isPre()) {
                        final float[] rotations = getRotationsNeeded(blockEntry);
                        event.setPitch(rotations[1]);
                        event.setYaw(rotations[0]);
                    }
                    else {
                        if (this.getBlockCount() <= 0) {
                            return;
                        }
                        final int heldItem = Scaffold.mc.thePlayer.inventory.currentItem;
                        boolean hasBlock = false;
                        for (int i = 0; i < 9; ++i) {
                            final ItemStack itemStack = Scaffold.mc.thePlayer.inventory.getStackInSlot(i);
                            if (itemStack != null && itemStack.stackSize != 0 && itemStack.getItem() instanceof ItemBlock && !this.badBlocks.contains(((ItemBlock)Scaffold.mc.thePlayer.inventory.getStackInSlot(i).getItem()).getBlock())) {
                                final NetHandlerPlayClient sendQueue = Scaffold.mc.thePlayer.sendQueue;
                                final InventoryPlayer inventory = Scaffold.mc.thePlayer.inventory;
                                final int n = i;
                                inventory.currentItem = n;
                                sendQueue.sendPacketNoEvent(new C09PacketHeldItemChange(n));
                                hasBlock = true;
                                break;
                            }
                        }
                        if (!hasBlock) {
                            for (int i = 0; i < 45; ++i) {
                                if (Scaffold.mc.thePlayer.inventory.getStackInSlot(i) != null && Scaffold.mc.thePlayer.inventory.getStackInSlot(i).stackSize != 0 && Scaffold.mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemBlock && !this.badBlocks.contains(((ItemBlock)Scaffold.mc.thePlayer.inventory.getStackInSlot(i).getItem()).getBlock())) {
                                    Scaffold.mc.playerController.windowClick(Scaffold.mc.thePlayer.inventoryContainer.windowId, i, 8, 2, Scaffold.mc.thePlayer);
                                    break;
                                }
                            }
                        }
                        if (this.tower.isEnabled() && Scaffold.mc.gameSettings.keyBindJump.isKeyDown() && !Scaffold.mc.thePlayer.isPotionActive(Potion.jump)) {
                            if (!MovementUtils.isMoving()) {
                                Scaffold.mc.thePlayer.motionY = 0.41999998688697815;
                                final EntityPlayerSP thePlayer9 = Scaffold.mc.thePlayer;
                                final EntityPlayerSP thePlayer10 = Scaffold.mc.thePlayer;
                                final double n2 = 0.0;
                                thePlayer10.motionZ = n2;
                                thePlayer9.motionX = n2;
                            }
                            else if (Scaffold.mc.thePlayer.onGround && this.towermove.isEnabled()) {
                                Scaffold.mc.thePlayer.motionY = 0.41999998688697815;
                            }
                            else if (Scaffold.mc.thePlayer.motionY < 0.17 && Scaffold.mc.thePlayer.motionY > 0.16 && this.towermove.isEnabled()) {
                                Scaffold.mc.thePlayer.motionY = -0.009999999776482582;
                            }
                        }
                        Scaffold.mc.playerController.onPlayerRightClick3d(Scaffold.mc.thePlayer, Scaffold.mc.theWorld, Scaffold.mc.thePlayer.getHeldItem(), blockEntry.position.add(0, 0, 0), blockEntry.face, new Vec3d(blockEntry.position.getX(), blockEntry.position.getY(), blockEntry.position.getZ()));
                        this.lastBlockData = blockEntry;
                        if (!this.swing.isEnabled()) {
                            Scaffold.mc.thePlayer.sendQueue.sendPacketNoEvent(new C0APacketAnimation());
                        }
                        else {
                            Scaffold.mc.thePlayer.swingItem();
                        }
                        final NetHandlerPlayClient sendQueue2 = Scaffold.mc.thePlayer.sendQueue;
                        final InventoryPlayer inventory2 = Scaffold.mc.thePlayer.inventory;
                        final int n3 = heldItem;
                        inventory2.currentItem = n3;
                        sendQueue2.addToSendQueue(new C09PacketHeldItemChange(n3));
                    }
                }
                else if (e.isPre()) {
                    final EventMotion event = (EventMotion)e;
                    if (this.scaffoldmode.is("Normal")) {
                        final int slot = this.getSlot();
                        Scaffold.stopWalk = (this.getBlockCount() == 0 || slot == -1);
                        Scaffold.isPlaceTick = (this.keeprots.isEnabled() ? (this.blockData != null && slot != -1) : (this.blockData != null && slot != -1 && Scaffold.mc.theWorld.getBlockState(new BlockPos(Scaffold.mc.thePlayer).add(0, -1, 0)).getBlock() == Blocks.air));
                        if (slot == -1) {
                            this.moveBlocksToHotbar();
                            return;
                        }
                        this.blockData = this.getBlockData();
                        if (this.blockData == null) {
                            return;
                        }
                        if (this.timerboost.isEnabled()) {
                            if (this.boostTimer.hasTimeElapsed(2000L, true)) {
                                Scaffold.mc.timer.timerSpeed = (float)this.timerSpeed.getValue();
                                this.reduceTimer.reset();
                            }
                            else if (this.reduceTimer.hasTimeElapsed(100L, true)) {
                                Scaffold.mc.timer.timerSpeed = 1.0f;
                            }
                        }
                        if (Scaffold.mc.gameSettings.keyBindJump.isKeyDown() && this.tower.isEnabled() && (this.towermove.isEnabled() || !MovementUtils.isMoving()) && !Scaffold.mc.thePlayer.isPotionActive(Potion.jump) && !this.boolkeepY.isEnabled()) {
                            if (this.towerMode.is("Packet") && Scaffold.mc.thePlayer.onGround) {
                                Scaffold.mc.thePlayer.setPosition(Scaffold.mc.thePlayer.posX, Scaffold.mc.thePlayer.posY + 0.99, Scaffold.mc.thePlayer.posZ);
                                Scaffold.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Scaffold.mc.thePlayer.posX, Scaffold.mc.thePlayer.posY + 0.41999998688698, Scaffold.mc.thePlayer.posZ, false));
                                Scaffold.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Scaffold.mc.thePlayer.posX, Scaffold.mc.thePlayer.posY + 0.7531999805212, Scaffold.mc.thePlayer.posZ, false));
                            }
                            if (this.towerMode.is("NCP")) {
                                if (!MovementUtils.isOnGround(0.79) || Scaffold.mc.thePlayer.onGround) {
                                    Scaffold.mc.thePlayer.motionY = 0.41985;
                                }
                                if (this.towerTimer.hasTimeElapsed(1500L, true)) {
                                    Scaffold.mc.thePlayer.motionY = -1.0;
                                }
                            }
                        }
                        else {
                            this.towerTimer.reset();
                        }
                        if (Scaffold.isPlaceTick) {
                            final Rotation targetRotation = new Rotation(SetBlockAndFacingUtils.BlockUtil.getDirectionToBlock(this.blockData.getPosition().getX(), this.blockData.getPosition().getY(), this.blockData.getPosition().getZ(), this.blockData.getFacing())[0], 79.44f);
                            final Rotation limitedRotation = SetBlockAndFacingUtils.BlockUtil.limitAngleChange(new Rotation(this.yaw, event.getPitch()), targetRotation, (float)ThreadLocalRandom.current().nextDouble(20.0, 30.0));
                            this.yaw = limitedRotation.getYaw();
                            this.pitch = limitedRotation.getPitch();
                            event.setYaw(this.yaw);
                            event.setPitch(80.0f);
                        }
                    }
                }
                else if (this.scaffoldmode.is("Normal")) {
                    final int slot2 = this.getSlot();
                    final BlockPos pos = new BlockPos(Scaffold.mc.thePlayer.posX, Scaffold.mc.thePlayer.posY - 1.0, Scaffold.mc.thePlayer.posZ);
                    if (slot2 != -1 && this.blockData != null) {
                        final int currentSlot = Scaffold.mc.thePlayer.inventory.currentItem;
                        if (pos.getBlock() instanceof BlockAir) {
                            Scaffold.mc.thePlayer.inventory.currentItem = slot2;
                            if (this.getPlaceBlock(this.blockData.getPosition(), this.blockData.getFacing())) {
                                Scaffold.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(currentSlot));
                            }
                        }
                        else {
                            MovementUtils.setMotion(MovementUtils.getSpeed() - MovementUtils.getSpeed() / 50.0f);
                        }
                        Scaffold.mc.thePlayer.inventory.currentItem = currentSlot;
                    }
                }
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    public static float[] getRotationsNeeded(final BlockData data) {
        final EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        final Vec3 hitVec = data.hitVec;
        final double xDist = hitVec.xCoord - player.posX;
        final double zDist = hitVec.zCoord - player.posZ;
        final double yDist = hitVec.yCoord - (player.posY + player.getEyeHeight());
        final double fDist = MathHelper.sqrt_double(xDist * xDist + zDist * zDist);
        final float rotationYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
        final float var1 = MovementUtils.getMovementDirection() - 180.0f;
        final float yaw = rotationYaw + MathHelper.wrapAngleTo180_float(var1 - rotationYaw);
        final float rotationPitch = Minecraft.getMinecraft().thePlayer.rotationPitch;
        if (data.face != EnumFacing.DOWN && data.face != EnumFacing.UP) {
            final double yDistFeet = hitVec.yCoord - player.posY;
            final double totalAbsDist = Math.abs(xDist * xDist + yDistFeet * yDistFeet + zDist * zDist);
            if (totalAbsDist < 1.0) {
                return new float[] { yaw, (float)MathUtils.getRandom(80, 90) };
            }
        }
        final float var2 = (float)(-(StrictMath.atan2(yDist, fDist) * 180.0 / 3.141592653589793));
        final float pitch = rotationPitch + MathHelper.wrapAngleTo180_float(var2 - rotationPitch);
        return new float[] { yaw, MathHelper.clamp_float(pitch, -90.0f, 90.0f) };
    }
    
    private static float[] getRotations(final Vec3 hitVec) {
        final EntityPlayerSP player = Scaffold.mc.thePlayer;
        final double xDist = hitVec.xCoord - player.posX;
        final double zDist = hitVec.zCoord - player.posZ;
        final double yDist = hitVec.yCoord - (player.posY + player.getEyeHeight());
        final double fDist = MathHelper.sqrt_double(xDist * xDist + zDist * zDist);
        final float rotationYaw = Scaffold.mc.thePlayer.rotationYaw;
        final float var1 = (float)(StrictMath.atan2(zDist, xDist) * 180.0 / 3.141592653589793) - 90.0f;
        final float yaw = rotationYaw + MathHelper.wrapAngleTo180_float(var1 - rotationYaw);
        final float rotationPitch = Scaffold.mc.thePlayer.rotationPitch;
        final float var2 = (float)(-(StrictMath.atan2(yDist, fDist) * 180.0 / 3.141592653589793));
        final float pitch = rotationPitch + MathHelper.wrapAngleTo180_float(var2 - rotationPitch);
        return new float[] { yaw, MathHelper.clamp_float(pitch, -90.0f, 90.0f) };
    }
    
    private float[] getRotations(final EventMotion ev, final Vec3 hitVec, final float aimSpeed) {
        final EntityPlayerSP entity = Scaffold.mc.thePlayer;
        final double x = hitVec.xCoord - entity.posX;
        final double y = hitVec.yCoord - (entity.posY + entity.getEyeHeight());
        final double z = hitVec.zCoord - entity.posZ;
        final double fDist = MathHelper.sqrt_double(x * x + z * z);
        final float yaw = interpolateRotation(ev.getPrevYaw(), (float)(StrictMath.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f, aimSpeed);
        final float pitch = interpolateRotation(ev.getPrevPitch(), (float)(-(StrictMath.atan2(y, fDist) * 180.0 / 3.141592653589793)), aimSpeed);
        return new float[] { yaw, MathHelper.clamp_float(pitch, -90.0f, 90.0f) };
    }
    
    private void updateBlockCount() {
        this.blockCount = 0;
        for (int i = 9; i < 45; ++i) {
            final ItemStack stack = Resolute.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemBlock && InventoryUtils.isGoodBlockStack(stack)) {
                this.blockCount += stack.stackSize;
            }
        }
    }
    
    public ItemStack getStackInSlot(final int index) {
        return Scaffold.mc.thePlayer.inventoryContainer.getSlot(index).getStack();
    }
    
    private float[] getBlockRotations(final BlockPos blockPos, final EnumFacing enumFacing) {
        if (blockPos == null && enumFacing == null) {
            return null;
        }
        final Vec3d positionEyes = Scaffold.mc.thePlayer.getPositionEyes2(2.0f);
        final Vec3d add = new Vec3d(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5).add(new Vec3d(enumFacing.getDirectionVec()).scale(0.49000000953674316));
        final double n = add.xCoord - positionEyes.xCoord;
        final double n2 = add.yCoord - positionEyes.yCoord;
        final double n3 = add.zCoord - positionEyes.zCoord;
        float yaw = (float)(Math.atan2(n3, n) * 180.0 / 3.141592653589793 - 90.0);
        final float pitch = -(float)(Math.atan2(n2, (float)Math.hypot(n, n3)) * 180.0 / 3.141592653589793);
        final int inc = (int)this.watchdog2Step.getValue();
        yaw = (float)(Math.round(yaw / inc) * inc);
        return new float[] { yaw, pitch };
    }
    
    public int getBlockSlot() {
        for (int i = 36; i < 45; ++i) {
            if (Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack() != null && Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemBlock && !Scaffold.invalidBlocks.contains(((ItemBlock)Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack().getItem()).getBlock())) {
                return i - 36;
            }
        }
        return -1;
    }
    
    public void setYawSimple() {
        final boolean forward = Scaffold.mc.gameSettings.keyBindForward.isKeyDown();
        final boolean left = Scaffold.mc.gameSettings.keyBindLeft.isKeyDown();
        final boolean right = Scaffold.mc.gameSettings.keyBindRight.isKeyDown();
        final boolean back = Scaffold.mc.gameSettings.keyBindBack.isKeyDown();
        float yaw = 0.0f;
        if (forward && !left && !right && !back) {
            yaw = 180.0f;
        }
        if (!forward && left && !right && !back) {
            yaw = 90.0f;
        }
        if (!forward && !left && right && !back) {
            yaw = -90.0f;
        }
        if (!forward && !left && !right && back) {
            yaw = 0.0f;
        }
        if (forward && left && !right && !back) {
            yaw = 135.0f;
        }
        if (forward && !left && right && !back) {
            yaw = -135.0f;
        }
        if (!forward && left && !right && back) {
            yaw = 45.0f;
        }
        if (!forward && !left && right && back) {
            yaw = -45.0f;
        }
        this.aacyaw = Scaffold.mc.thePlayer.rotationYaw + yaw;
    }
    
    public float[] getRotationsAAC(final BlockPos block, final EnumFacing face) {
        final double x = block.getX() + 0.5 - Scaffold.mc.thePlayer.posX + face.getFrontOffsetX() / 2.0;
        final double z = block.getZ() + 0.5 - Scaffold.mc.thePlayer.posZ + face.getFrontOffsetZ() / 2.0;
        double y = block.getY() + 0.5 + face.getFrontOffsetZ() / 2;
        y += 0.5;
        final double d1 = Scaffold.mc.thePlayer.posY + Scaffold.mc.thePlayer.getEyeHeight() - y;
        final double d2 = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(Math.atan2(d1, d2) * 180.0 / 3.141592653589793);
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        return new float[] { yaw, pitch };
    }
    
    private static boolean validateReplaceable(final BlockData data) {
        final BlockPos pos = data.position.offset(data.face);
        final World world = Scaffold.mc.theWorld;
        return world.getBlockState(pos).getBlock().isReplaceable(world, pos);
    }
    
    public float[] look(final Vec3 vector) {
        final double diffX = vector.xCoord - Scaffold.mc.thePlayer.posX;
        final double diffY = vector.yCoord - (Scaffold.mc.thePlayer.posY + Scaffold.mc.thePlayer.getEyeHeight());
        final double diffZ = vector.zCoord - Scaffold.mc.thePlayer.posZ;
        final double distance = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, distance)));
        return new float[] { Scaffold.mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - Scaffold.mc.thePlayer.rotationYaw), Scaffold.mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - Scaffold.mc.thePlayer.rotationPitch) };
    }
    
    private void tower() {
        if (!MovementUtils.isMoving()) {
            Scaffold.mc.thePlayer.motionX = 0.0;
            Scaffold.mc.thePlayer.motionZ = 0.0;
            if (Scaffold.mc.thePlayer.onGround) {
                Scaffold.mc.thePlayer.setPosition(this.down(Scaffold.mc.thePlayer.posX) + 0.5, Scaffold.mc.thePlayer.posY, this.down(Scaffold.mc.thePlayer.posZ) + 0.5);
            }
        }
        if (MovementUtils.isOnGround(0.76) && !MovementUtils.isOnGround(0.75) && Scaffold.mc.thePlayer.motionY > 0.23 && Scaffold.mc.thePlayer.motionY < 0.25) {
            Scaffold.mc.thePlayer.motionY = Math.round(Scaffold.mc.thePlayer.posY) - Scaffold.mc.thePlayer.posY;
        }
        if (MovementUtils.isOnGround(1.0E-4)) {
            Scaffold.mc.thePlayer.motionY = MovementUtils.getJumpBoostModifier(0.41999998688698);
        }
        else if (Scaffold.mc.thePlayer.posY >= Math.round(Scaffold.mc.thePlayer.posY) - 1.0E-4 && Scaffold.mc.thePlayer.posY <= Math.round(Scaffold.mc.thePlayer.posY) + 1.0E-4 && !Scaffold.mc.gameSettings.keyBindSneak.isKeyDown()) {
            Scaffold.mc.thePlayer.motionY = 0.0;
        }
    }
    
    private int down(final double n) {
        final int n2 = (int)n;
        try {
            if (n < n2) {
                return n2 - 1;
            }
        }
        catch (IllegalArgumentException illegalArgumentException) {
            illegalArgumentException.printStackTrace();
        }
        return n2;
    }
    
    private BlockData getBlockData2(final BlockPos pos) {
        final BlockPos[] blockPositions = Scaffold.BLOCK_POSITIONS;
        final EnumFacing[] facings = Scaffold.FACINGS;
        final WorldClient world = Minecraft.getMinecraft().theWorld;
        for (int i = 0; i < blockPositions.length; ++i) {
            final BlockPos blockPos = pos.add(blockPositions[i]);
            if (isValidBlock(world.getBlockState(blockPos).getBlock(), false)) {
                final BlockData data = new BlockData(blockPos, facings[i]);
                if (validateBlockRange(data)) {
                    return data;
                }
            }
        }
        final BlockPos posBelow = pos.add(0, -1, 0);
        if (isValidBlock(world.getBlockState(posBelow).getBlock(), false)) {
            final BlockData data2 = new BlockData(posBelow, EnumFacing.UP);
            if (validateBlockRange(data2)) {
                return data2;
            }
        }
        for (final BlockPos blockPosition : blockPositions) {
            final BlockPos blockPos2 = pos.add(blockPosition);
            for (int j = 0; j < blockPositions.length; ++j) {
                final BlockPos blockPos3 = blockPos2.add(blockPositions[j]);
                if (isValidBlock(world.getBlockState(blockPos3).getBlock(), false)) {
                    final BlockData data3 = new BlockData(blockPos3, facings[j]);
                    if (validateBlockRange(data3)) {
                        return data3;
                    }
                }
            }
        }
        for (final BlockPos blockPosition : blockPositions) {
            final BlockPos blockPos2 = pos.add(blockPosition);
            for (final BlockPos position : blockPositions) {
                final BlockPos blockPos4 = blockPos2.add(position);
                for (int k = 0; k < blockPositions.length; ++k) {
                    final BlockPos blockPos5 = blockPos4.add(blockPositions[k]);
                    if (isValidBlock(world.getBlockState(blockPos5).getBlock(), false)) {
                        final BlockData data4 = new BlockData(blockPos5, facings[k]);
                        if (validateBlockRange(data4)) {
                            return data4;
                        }
                    }
                }
            }
        }
        for (final BlockPos blackPosition : blockPositions) {
            final BlockPos blockPos2 = pos.add(blackPosition);
            for (final BlockPos blockPosition2 : blockPositions) {
                final BlockPos blockPos4 = blockPos2.add(blockPosition2);
                for (final BlockPos position2 : blockPositions) {
                    final BlockPos blockPos6 = blockPos4.add(position2);
                    for (int l = 0; l < blockPositions.length; ++l) {
                        final BlockPos blockPos7 = blockPos6.add(blockPositions[l]);
                        if (isValidBlock(world.getBlockState(blockPos7).getBlock(), false)) {
                            final BlockData data5 = new BlockData(blockPos7, facings[l]);
                            if (validateBlockRange(data5)) {
                                return data5;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public static float[] getScaffoldRotations(final BlockData data) {
        final Vec3 eyes = Scaffold.mc.thePlayer.getPositionEyes(RandomUtils.nextFloat(2.997f, 3.997f));
        final Vec3 position = new Vec3(data.position.getX() + 0.49, data.position.getY() + 0.49, data.position.getZ() + 0.49).add(new Vec3(data.face.getDirectionVec()).scale(0.489996999502182));
        final Vec3 resultPosition = position.subtract(eyes);
        final float yaw = (float)Math.toDegrees(Math.atan2(resultPosition.zCoord, resultPosition.xCoord)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(resultPosition.yCoord, Math.hypot(resultPosition.xCoord, resultPosition.zCoord))));
        return new float[] { yaw, pitch };
    }
    
    private static boolean validateBlockRange(final BlockData data) {
        final Vec3 pos = data.hitVec;
        if (pos == null) {
            return false;
        }
        final EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        final double x = pos.xCoord - player.posX;
        final double y = pos.yCoord - (player.posY + player.getEyeHeight());
        final double z = pos.zCoord - player.posZ;
        return StrictMath.sqrt(x * x + y * y + z * z) <= 5.0;
    }
    
    private float[] getRotationsToBlockData(final BlockData data, final EventMotion ev) {
        final EntityPlayerSP entity = Scaffold.mc.thePlayer;
        final Vec3 hitVec = data.getHitVec();
        final double x = hitVec.xCoord - entity.posX;
        final double y = hitVec.yCoord - (entity.posY + entity.getEyeHeight());
        final double z = hitVec.zCoord - entity.posZ;
        final double fDist = MathHelper.sqrt_double(x * x + z * z);
        float yaw = this.interpolateRotation(ev.getPrevYaw(), (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f);
        final float pitch = this.interpolateRotation(ev.getPrevPitch(), (float)(-(Math.atan2(y, fDist) * 180.0 / 3.141592653589793)));
        final int inc = (int)this.watchdogStep.getValue();
        yaw = (float)(Math.round(yaw / inc) * inc);
        return new float[] { yaw, MathHelper.clamp_float(pitch, -90.0f, 90.0f) };
    }
    
    private float interpolateRotation(final float p_70663_1_, final float p_70663_2_) {
        final float maxTurn = 45.0f;
        float var4 = MathHelper.wrapAngleTo180_float(p_70663_2_ - p_70663_1_);
        if (var4 > maxTurn) {
            var4 = maxTurn;
        }
        if (var4 < -maxTurn) {
            var4 = -maxTurn;
        }
        return p_70663_1_ + var4;
    }
    
    private BlockPos getBlockUnder(final boolean keepY) {
        return new BlockPos(Scaffold.mc.thePlayer.posX, keepY ? this.keepY : (Scaffold.mc.thePlayer.posY - 1.0), Scaffold.mc.thePlayer.posZ);
    }
    
    public static boolean isValidBlock(final Block block, final boolean toPlace) {
        if (block instanceof BlockContainer) {
            return false;
        }
        if (toPlace) {
            return !(block instanceof BlockFalling) && block.isFullBlock() && block.isFullCube();
        }
        final Material material = block.getMaterial();
        return !material.isReplaceable() && !material.isLiquid();
    }
    
    private boolean getPlaceBlock(final BlockPos pos, final EnumFacing facing) {
        if (this.timer.hasTimeElapsed((long)this.delay.getValue(), true) && Scaffold.mc.playerController.onPlayerRightClick(Scaffold.mc.thePlayer, Scaffold.mc.theWorld, Scaffold.mc.thePlayer.getHeldItem(), pos, facing, this.getVec3(new BlockData(pos, facing)))) {
            if (this.swing.isEnabled()) {
                Scaffold.mc.thePlayer.swingItem();
            }
            else {
                Scaffold.mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
            }
            this.timer.reset();
            return true;
        }
        return false;
    }
    
    private BlockData getBlockData(final BlockPos pos) {
        if (this.isPosSolid(Scaffold.mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(Scaffold.mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(Scaffold.mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(Scaffold.mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(Scaffold.mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos add = pos.add(0, 0, 0);
        if (this.isPosSolid(Scaffold.mc.theWorld.getBlockState(add.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(Scaffold.mc.theWorld.getBlockState(add.add(1, 0, 0)).getBlock())) {
            return new BlockData(add.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(Scaffold.mc.theWorld.getBlockState(add.add(0, 0, -1)).getBlock())) {
            return new BlockData(add.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (this.isPosSolid(Scaffold.mc.theWorld.getBlockState(add.add(0, 0, 1)).getBlock())) {
            return new BlockData(add.add(0, 0, 1), EnumFacing.NORTH);
        }
        final BlockPos add2 = pos.add(1, 0, 0);
        if (this.isPosSolid(Scaffold.mc.theWorld.getBlockState(add2.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(Scaffold.mc.theWorld.getBlockState(add2.add(1, 0, 0)).getBlock())) {
            return new BlockData(add2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(Scaffold.mc.theWorld.getBlockState(add2.add(0, 0, -1)).getBlock())) {
            return new BlockData(add2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (this.isPosSolid(Scaffold.mc.theWorld.getBlockState(add2.add(0, 0, 1)).getBlock())) {
            return new BlockData(add2.add(0, 0, 1), EnumFacing.NORTH);
        }
        final BlockPos add3 = pos.add(0, 0, -1);
        if (this.isPosSolid(Scaffold.mc.theWorld.getBlockState(add3.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(Scaffold.mc.theWorld.getBlockState(add3.add(1, 0, 0)).getBlock())) {
            return new BlockData(add3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(Scaffold.mc.theWorld.getBlockState(add3.add(0, 0, -1)).getBlock())) {
            return new BlockData(add3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (this.isPosSolid(Scaffold.mc.theWorld.getBlockState(add3.add(0, 0, 1)).getBlock())) {
            return new BlockData(add3.add(0, 0, 1), EnumFacing.NORTH);
        }
        final BlockPos add4 = pos.add(0, 0, 1);
        if (this.isPosSolid(Scaffold.mc.theWorld.getBlockState(add4.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(Scaffold.mc.theWorld.getBlockState(add4.add(1, 0, 0)).getBlock())) {
            return new BlockData(add4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(Scaffold.mc.theWorld.getBlockState(add4.add(0, 0, -1)).getBlock())) {
            return new BlockData(add4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (this.isPosSolid(Scaffold.mc.theWorld.getBlockState(add4.add(0, 0, 1)).getBlock())) {
            return new BlockData(add4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(Scaffold.mc.theWorld.getBlockState(add.add(1, 1, 0)).getBlock())) {
            return new BlockData(add.add(1, 1, 0), EnumFacing.DOWN);
        }
        if (this.isPosSolid(Scaffold.mc.theWorld.getBlockState(add.add(-1, 2, -1)).getBlock())) {
            return new BlockData(add.add(-1, 2, -1), EnumFacing.DOWN);
        }
        if (this.isPosSolid(Scaffold.mc.theWorld.getBlockState(add2.add(-2, 1, 0)).getBlock())) {
            return new BlockData(add2.add(-2, 1, 0), EnumFacing.DOWN);
        }
        if (this.isPosSolid(Scaffold.mc.theWorld.getBlockState(add2.add(0, 2, 1)).getBlock())) {
            return new BlockData(add2.add(0, 2, 1), EnumFacing.DOWN);
        }
        if (this.isPosSolid(Scaffold.mc.theWorld.getBlockState(add3.add(0, 1, 2)).getBlock())) {
            return new BlockData(add3.add(0, 1, 2), EnumFacing.DOWN);
        }
        if (this.isPosSolid(Scaffold.mc.theWorld.getBlockState(add3.add(1, 2, 0)).getBlock())) {
            return new BlockData(add3.add(1, 2, 0), EnumFacing.DOWN);
        }
        if (this.isPosSolid(Scaffold.mc.theWorld.getBlockState(add4.add(0, 1, -2)).getBlock())) {
            return new BlockData(add4.add(0, 1, -2), EnumFacing.DOWN);
        }
        if (this.isPosSolid(Scaffold.mc.theWorld.getBlockState(add4.add(-1, 2, 0)).getBlock())) {
            return new BlockData(add4.add(-1, 2, 0), EnumFacing.DOWN);
        }
        return null;
    }
    
    private boolean isPosSolid(final Block block) {
        return !Scaffold.blacklistedBlocks.contains(block) && (block.getMaterial().isSolid() || !block.isTranslucent() || block.isVisuallyOpaque() || block instanceof BlockLadder || block instanceof BlockCarpet || block instanceof BlockSnow || block instanceof BlockSkull) && !block.getMaterial().isLiquid() && !(block instanceof BlockContainer);
    }
    
    private static float interpolateRotation(final float prev, final float now, final float maxTurn) {
        float var4 = MathHelper.wrapAngleTo180_float(now - prev);
        if (var4 > maxTurn) {
            var4 = maxTurn;
        }
        if (var4 < -maxTurn) {
            var4 = -maxTurn;
        }
        return prev + var4;
    }
    
    private Vec3 getVec3(final BlockData data) {
        final BlockPos pos = data.getPosition();
        final EnumFacing face = data.getFacing();
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;
        x += face.getFrontOffsetX() / 2.0;
        z += face.getFrontOffsetZ() / 2.0;
        y += face.getFrontOffsetY() / 2.0;
        if (face != EnumFacing.UP && face != EnumFacing.DOWN) {
            y += this.randomNumber(0.49, 0.5);
        }
        else {
            x += this.randomNumber(0.3, -0.3);
            z += this.randomNumber(0.3, -0.3);
        }
        if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
            z += this.randomNumber(0.3, -0.3);
        }
        if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
            x += this.randomNumber(0.3, -0.3);
        }
        return new Vec3(x, y, z);
    }
    
    public Vec3 getVec3(final BlockPos pos, final EnumFacing face) {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;
        x += face.getFrontOffsetX() / 2.0;
        z += face.getFrontOffsetZ() / 2.0;
        y += face.getFrontOffsetY() / 2.0;
        if (face == EnumFacing.UP || face == EnumFacing.DOWN) {
            x += new Random().nextDouble() / 2.0 - 0.25;
            z += new Random().nextDouble() / 2.0 - 0.25;
        }
        else {
            y += new Random().nextDouble() / 2.0 - 0.25;
        }
        if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
            z += new Random().nextDouble() / 2.0 - 0.25;
        }
        if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
            x += new Random().nextDouble() / 2.0 - 0.25;
        }
        return new Vec3(x, y, z);
    }
    
    private double randomNumber(final double max, final double min) {
        return Math.random() * (max - min) + min;
    }
    
    public static int getRandom(final int floor, final int cap) {
        return floor + Scaffold.rng.nextInt(cap - floor + 1);
    }
    
    private void setSneaking(final boolean b) {
        Scaffold.mc.gameSettings.keyBindSneak.pressed = b;
    }
    
    public BlockData getBlockData() {
        final EnumFacing[] invert = { EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST };
        double yValue = 0.0;
        if (Keyboard.isKeyDown(Scaffold.mc.gameSettings.keyBindSneak.getKeyCode()) && !Scaffold.mc.gameSettings.keyBindJump.isKeyDown() && this.downwards.isEnabled()) {
            KeyBinding.setKeyBindState(Scaffold.mc.gameSettings.keyBindSneak.getKeyCode(), false);
            yValue -= 0.6;
        }
        BlockPos playerpos;
        final BlockPos aa = playerpos = new BlockPos(Scaffold.mc.thePlayer.getPositionVector()).offset(EnumFacing.DOWN).add(0.0, yValue, 0.0);
        final boolean tower = !this.towermove.isEnabled() && this.tower.isEnabled() && !MovementUtils.isMoving();
        if (!this.downwards.isEnabled() && this.boolkeepY.isEnabled() && !tower) {
            playerpos = new BlockPos(new Vec3(Scaffold.mc.thePlayer.getPositionVector().xCoord, this.startY, Scaffold.mc.thePlayer.getPositionVector().zCoord)).offset(EnumFacing.DOWN);
        }
        else {
            this.startY = Scaffold.mc.thePlayer.posY;
        }
        for (final EnumFacing facing : EnumFacing.values()) {
            if (playerpos.offset(facing).getBlock().getMaterial() != Material.air) {
                return new BlockData(playerpos.offset(facing), invert[facing.ordinal()]);
            }
        }
        final BlockPos[] addons = { new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, 0, 1) };
        for (int length2 = addons.length, j = 0; j < length2; ++j) {
            final BlockPos offsetPos = playerpos.add(addons[j].getX(), 0, addons[j].getZ());
            if (Scaffold.mc.theWorld.getBlockState(offsetPos).getBlock() instanceof BlockAir) {
                for (int k = 0; k < EnumFacing.values().length; ++k) {
                    if (Scaffold.mc.theWorld.getBlockState(offsetPos.offset(EnumFacing.values()[k])).getBlock().getMaterial() != Material.air) {
                        return new BlockData(offsetPos.offset(EnumFacing.values()[k]), invert[EnumFacing.values()[k].ordinal()]);
                    }
                }
            }
        }
        return null;
    }
    
    private int findBestBlockStack() {
        int bestSlot = -1;
        int blockCount = -1;
        for (int i = 44; i >= 9; --i) {
            final ItemStack stack = this.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemBlock && InventoryUtils.isGoodBlockStack(stack) && stack.stackSize > blockCount) {
                bestSlot = i;
                blockCount = stack.stackSize;
            }
        }
        return bestSlot;
    }
    
    private int getSlot() {
        final ArrayList<Integer> slots = new ArrayList<Integer>();
        for (int k = 0; k < 9; ++k) {
            final ItemStack itemStack = Scaffold.mc.thePlayer.inventory.mainInventory[k];
            if (itemStack != null && this.isValid(itemStack) && itemStack.stackSize >= 1) {
                slots.add(k);
            }
        }
        if (slots.isEmpty()) {
            return -1;
        }
        if (this.slotTimer.hasReached(150.0)) {
            if (this.slotIndex >= slots.size() || this.slotIndex == slots.size() - 1) {
                this.slotIndex = 0;
            }
            else {
                ++this.slotIndex;
            }
            this.slotTimer.reset();
        }
        return slots.get(this.slotIndex);
    }
    
    private boolean isValid(final ItemStack itemStack) {
        if (itemStack.getItem() instanceof ItemBlock) {
            boolean isBad = false;
            final ItemBlock block = (ItemBlock)itemStack.getItem();
            for (int i = 0; i < this.badBlocks.size(); ++i) {
                if (block.getBlock().equals(this.badBlocks.get(i))) {
                    isBad = true;
                }
            }
            return !isBad;
        }
        return false;
    }
    
    private int getBlockCount() {
        int count = 0;
        for (int k = 0; k < Scaffold.mc.thePlayer.inventory.mainInventory.length; ++k) {
            final ItemStack itemStack = Scaffold.mc.thePlayer.inventory.mainInventory[k];
            if (itemStack != null && this.isValid(itemStack) && itemStack.stackSize >= 1) {
                count += itemStack.stackSize;
            }
        }
        return count;
    }
    
    public void setYaw() {
        final float[] rotations = faceBlock(this.finalPos, true, this.aacyaw, this.aacpitch, this.speed);
        this.aacyaw = rotations[0];
    }
    
    public float getPitch() {
        return faceBlock(this.finalPos, true, this.aacyaw, this.aacpitch, this.speed)[1];
    }
    
    public void placeBlock(final BlockPos pos, final EnumFacing face) {
        int silentItem = 0;
        this.finalPos = pos;
        if (Scaffold.mc.thePlayer.getCurrentEquippedItem() != null && Scaffold.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock && !this.badBlocks.contains(Scaffold.mc.thePlayer.getCurrentEquippedItem().getItem())) {
            silentItem = Scaffold.mc.thePlayer.inventory.currentItem;
        }
        else {
            for (int i = 0; i < 9; ++i) {
                if (Scaffold.mc.thePlayer.inventory.getStackInSlot(i) != null && Scaffold.mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemBlock) {
                    final ItemBlock itemBlock = (ItemBlock)Scaffold.mc.thePlayer.inventory.getStackInSlot(i).getItem();
                    if (!this.badBlocks.contains(itemBlock.getBlock())) {
                        silentItem = i;
                        break;
                    }
                }
            }
        }
        final MovingObjectPosition fixRayTrace = RayCastUtil.rayTrace(Scaffold.mc.thePlayer, 4.0, 0.0f, this.aacyaw, this.aacpitch);
        if (Scaffold.mc.theWorld.getBlockState(new BlockPos(Scaffold.mc.thePlayer.posX, Scaffold.mc.thePlayer.posY - 1.0, Scaffold.mc.thePlayer.posZ)).getBlock() instanceof BlockAir) {
            if (!this.rayCastMode.is("Fix") || fixRayTrace.getBlockPos() != null) {
                if (this.sneakCount >= this.sneakAfter.getValue() && this.sneak.isEnabled()) {
                    Scaffold.mc.gameSettings.keyBindSneak.pressed = true;
                }
                if (!this.altRots.isEnabled()) {
                    this.setYaw();
                }
                final int lastItem = Scaffold.mc.thePlayer.inventory.currentItem;
                Scaffold.mc.thePlayer.inventory.currentItem = silentItem;
                if (this.swing.isEnabled()) {
                    Scaffold.mc.thePlayer.swingItem();
                }
                else {
                    Scaffold.mc.getNetHandler().sendPacketNoEvent(new C0APacketAnimation());
                }
                this.speed = 360.0f;
                if (Scaffold.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock && !this.badBlocks.contains(Scaffold.mc.thePlayer.getCurrentEquippedItem().getItem())) {
                    if (this.rayCastMode.is("Full")) {
                        final MovingObjectPosition rayTrace = RayCastUtil.getMouseOver(Scaffold.mc.thePlayer, this.aacyaw, this.aacpitch, 4.0);
                        Scaffold.mc.playerController.onPlayerRightClick(Scaffold.mc.thePlayer, Scaffold.mc.theWorld, Scaffold.mc.thePlayer.getCurrentEquippedItem(), rayTrace.getBlockPos(), rayTrace.sideHit, rayTrace.hitVec);
                        ++this.sneakCount;
                    }
                    else {
                        final double vec = RandomUtil.getDouble(0.2, 0.8);
                        if (this.rayCastMode.is("Fix")) {
                            Scaffold.mc.playerController.onPlayerRightClick(Scaffold.mc.thePlayer, Scaffold.mc.theWorld, Scaffold.mc.thePlayer.getCurrentEquippedItem(), fixRayTrace.getBlockPos(), fixRayTrace.sideHit, fixRayTrace.hitVec);
                        }
                        else {
                            Scaffold.mc.playerController.onPlayerRightClick(Scaffold.mc.thePlayer, Scaffold.mc.theWorld, Scaffold.mc.thePlayer.getCurrentEquippedItem(), pos, face, new Vec3(vec, vec, vec));
                        }
                        ++this.sneakCount;
                    }
                    this.timeUtil.reset();
                }
                Scaffold.mc.thePlayer.inventory.currentItem = lastItem;
                if (this.sneakCount > this.sneakAfter.getValue()) {
                    this.sneakCount = 0;
                }
            }
            else {
                this.timeUtil.reset();
            }
        }
        else {
            this.timeUtil.reset();
            Scaffold.mc.gameSettings.keyBindSneak.pressed = false;
            if (!this.altRots.isEnabled()) {
                this.setYaw();
            }
        }
    }
    
    public static float[] faceBlock(final BlockPos pos, final boolean scaffoldFix, final float currentYaw, final float currentPitch, final float speed) {
        final double x = pos.getX() + (scaffoldFix ? 0.5 : 0.0) - Scaffold.mc.thePlayer.posX;
        final double y = pos.getY() - (scaffoldFix ? 1.75 : 0.0) - (Scaffold.mc.thePlayer.posY + Scaffold.mc.thePlayer.getEyeHeight());
        final double z = pos.getZ() + (scaffoldFix ? 0.5 : 0.0) - Scaffold.mc.thePlayer.posZ;
        final double calculate = MathHelper.sqrt_double(x * x + z * z);
        final float calcYaw = (float)(MathHelper.func_181159_b(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        final float calcPitch = (float)(-(MathHelper.func_181159_b(y, calculate) * 180.0 / 3.141592653589793));
        final float finalPitch = (calcPitch >= 90.0f) ? 90.0f : calcPitch;
        float yaw = updateRotation(currentYaw, calcYaw, speed);
        float pitch = updateRotation(currentPitch, finalPitch, speed);
        final float sense = Scaffold.mc.gameSettings.mouseSensitivity * 0.8f + 0.2f;
        final float fix = (float)(Math.pow(sense, 3.0) * 1.5);
        yaw -= yaw % fix;
        pitch -= pitch % fix;
        return new float[] { yaw, pitch };
    }
    
    public static float updateRotation(final float current, final float intended, final float factor) {
        float f = MathHelper.wrapAngleTo180_float(intended - current);
        if (f > factor) {
            f = factor;
        }
        if (f < -factor) {
            f = -factor;
        }
        return current + f;
    }
    
    public void getBlockPosToPlaceOn(final BlockPos pos) {
        final BlockPos blockPos1 = pos.add(-1, 0, 0);
        final BlockPos blockPos2 = pos.add(1, 0, 0);
        final BlockPos blockPos3 = pos.add(0, 0, -1);
        final BlockPos blockPos4 = pos.add(0, 0, 1);
        if (Scaffold.mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock() != Blocks.air) {
            this.placeBlock(pos.add(0, -1, 0), EnumFacing.UP);
        }
        else if (Scaffold.mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock() != Blocks.air) {
            this.placeBlock(pos.add(-1, 0, 0), EnumFacing.EAST);
        }
        else if (Scaffold.mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock() != Blocks.air) {
            this.placeBlock(pos.add(1, 0, 0), EnumFacing.WEST);
        }
        else if (Scaffold.mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock() != Blocks.air) {
            this.placeBlock(pos.add(0, 0, -1), EnumFacing.SOUTH);
        }
        else if (Scaffold.mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock() != Blocks.air) {
            this.placeBlock(pos.add(0, 0, 1), EnumFacing.NORTH);
        }
        else if (Scaffold.mc.theWorld.getBlockState(blockPos1.add(0, -1, 0)).getBlock() != Blocks.air) {
            this.placeBlock(blockPos1.add(0, -1, 0), EnumFacing.UP);
        }
        else if (Scaffold.mc.theWorld.getBlockState(blockPos1.add(-1, 0, 0)).getBlock() != Blocks.air) {
            this.placeBlock(blockPos1.add(-1, 0, 0), EnumFacing.EAST);
        }
        else if (Scaffold.mc.theWorld.getBlockState(blockPos1.add(1, 0, 0)).getBlock() != Blocks.air) {
            this.placeBlock(blockPos1.add(1, 0, 0), EnumFacing.WEST);
        }
        else if (Scaffold.mc.theWorld.getBlockState(blockPos1.add(0, 0, -1)).getBlock() != Blocks.air) {
            this.placeBlock(blockPos1.add(0, 0, -1), EnumFacing.SOUTH);
        }
        else if (Scaffold.mc.theWorld.getBlockState(blockPos1.add(0, 0, 1)).getBlock() != Blocks.air) {
            this.placeBlock(blockPos1.add(0, 0, 1), EnumFacing.NORTH);
        }
        else if (Scaffold.mc.theWorld.getBlockState(blockPos2.add(0, -1, 0)).getBlock() != Blocks.air) {
            this.placeBlock(blockPos2.add(0, -1, 0), EnumFacing.UP);
        }
        else if (Scaffold.mc.theWorld.getBlockState(blockPos2.add(-1, 0, 0)).getBlock() != Blocks.air) {
            this.placeBlock(blockPos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        else if (Scaffold.mc.theWorld.getBlockState(blockPos2.add(1, 0, 0)).getBlock() != Blocks.air) {
            this.placeBlock(blockPos2.add(1, 0, 0), EnumFacing.WEST);
        }
        else if (Scaffold.mc.theWorld.getBlockState(blockPos2.add(0, 0, -1)).getBlock() != Blocks.air) {
            this.placeBlock(blockPos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        else if (Scaffold.mc.theWorld.getBlockState(blockPos2.add(0, 0, 1)).getBlock() != Blocks.air) {
            this.placeBlock(blockPos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        else if (Scaffold.mc.theWorld.getBlockState(blockPos3.add(0, -1, 0)).getBlock() != Blocks.air) {
            this.placeBlock(blockPos3.add(0, -1, 0), EnumFacing.UP);
        }
        else if (Scaffold.mc.theWorld.getBlockState(blockPos3.add(-1, 0, 0)).getBlock() != Blocks.air) {
            this.placeBlock(blockPos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        else if (Scaffold.mc.theWorld.getBlockState(blockPos3.add(1, 0, 0)).getBlock() != Blocks.air) {
            this.placeBlock(blockPos3.add(1, 0, 0), EnumFacing.WEST);
        }
        else if (Scaffold.mc.theWorld.getBlockState(blockPos3.add(0, 0, -1)).getBlock() != Blocks.air) {
            this.placeBlock(blockPos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        else if (Scaffold.mc.theWorld.getBlockState(blockPos3.add(0, 0, 1)).getBlock() != Blocks.air) {
            this.placeBlock(blockPos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        else if (Scaffold.mc.theWorld.getBlockState(blockPos4.add(0, -1, 0)).getBlock() != Blocks.air) {
            this.placeBlock(blockPos4.add(0, -1, 0), EnumFacing.UP);
        }
        else if (Scaffold.mc.theWorld.getBlockState(blockPos4.add(-1, 0, 0)).getBlock() != Blocks.air) {
            this.placeBlock(blockPos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        else if (Scaffold.mc.theWorld.getBlockState(blockPos4.add(1, 0, 0)).getBlock() != Blocks.air) {
            this.placeBlock(blockPos4.add(1, 0, 0), EnumFacing.WEST);
        }
        else if (Scaffold.mc.theWorld.getBlockState(blockPos4.add(0, 0, -1)).getBlock() != Blocks.air) {
            this.placeBlock(blockPos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        else if (Scaffold.mc.theWorld.getBlockState(blockPos4.add(0, 0, 1)).getBlock() != Blocks.air) {
            this.placeBlock(blockPos4.add(0, 0, 1), EnumFacing.NORTH);
        }
    }
    
    private void moveBlocksToHotbar() {
        boolean added = false;
        if (!this.isHotbarFull()) {
            for (int k = 0; k < Scaffold.mc.thePlayer.inventory.mainInventory.length; ++k) {
                if (k > 8 && !added) {
                    final ItemStack itemStack = Scaffold.mc.thePlayer.inventory.mainInventory[k];
                    if (itemStack != null && this.isValid(itemStack)) {
                        shiftClick(k);
                        added = true;
                    }
                }
            }
        }
    }
    
    public boolean isHotbarFull() {
        int count = 0;
        for (int k = 0; k < 9; ++k) {
            final ItemStack itemStack = Scaffold.mc.thePlayer.inventory.mainInventory[k];
            if (itemStack != null) {
                ++count;
            }
        }
        return count == 8;
    }
    
    public static void shiftClick(final int slot) {
        Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().thePlayer.inventoryContainer.windowId, slot, 0, 1, Minecraft.getMinecraft().thePlayer);
    }
    
    static {
        BLOCK_POSITIONS = new BlockPos[] { new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, 0, 1) };
        FACINGS = new EnumFacing[] { EnumFacing.EAST, EnumFacing.WEST, EnumFacing.SOUTH, EnumFacing.NORTH };
        Scaffold.sprint = new BooleanSetting("Sprint", true);
        glCapMap = new HashMap<Integer, Boolean>();
        Scaffold.isPlaceTick = false;
        Scaffold.stopWalk = false;
        Scaffold.heldItem = 0;
        Scaffold.invalidBlocks = Arrays.asList(Blocks.sand, Blocks.ladder, Blocks.flower_pot, Blocks.red_flower, Blocks.yellow_flower, Blocks.rail, Blocks.golden_rail, Blocks.activator_rail, Blocks.detector_rail, Blocks.beacon, Blocks.web, Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.snow_layer, Blocks.chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.crafting_table, Blocks.furnace, Blocks.stone_slab, Blocks.wooden_slab, Blocks.stone_slab2, Blocks.brown_mushroom, Blocks.red_mushroom, Blocks.flower_pot, Blocks.double_plant);
        Scaffold.blacklistedBlocks = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.ender_chest, Blocks.enchanting_table, Blocks.stone_button, Blocks.wooden_button, Blocks.crafting_table, Blocks.beacon, Blocks.furnace, Blocks.chest, Blocks.trapped_chest, Blocks.iron_bars, Blocks.cactus, Blocks.ladder);
        Scaffold.enabled = false;
        Scaffold.rng = new Random();
    }
    
    public static class BlockData
    {
        public BlockPos position;
        public EnumFacing face;
        public Vec3 hitVec;
        
        public BlockData(final BlockPos position, final EnumFacing face) {
            this.position = position;
            this.face = face;
            this.hitVec = this.getHitVec();
        }
        
        public EnumFacing getFacing() {
            return this.face;
        }
        
        public BlockPos getPosition() {
            return this.position;
        }
        
        private Vec3 getHitVec() {
            final Vec3i directionVec = this.face.getDirectionVec();
            double x = directionVec.getX() * 0.5;
            double z = directionVec.getZ() * 0.5;
            if (this.face.getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE) {
                x = -x;
                z = -z;
            }
            final Vec3 hitVec = new Vec3(this.position).addVector(x + z, directionVec.getY() * 0.5, x + z);
            final Vec3 src = Minecraft.getMinecraft().thePlayer.getPositionEyes(1.0f);
            final MovingObjectPosition obj = Minecraft.getMinecraft().theWorld.rayTraceBlocks(src, hitVec, false, false, true);
            if (obj == null || obj.hitVec == null || obj.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
                return null;
            }
            switch (this.face.getAxis()) {
                case Z: {
                    obj.hitVec = new Vec3(obj.hitVec.xCoord, obj.hitVec.yCoord, (double)Math.round(obj.hitVec.zCoord));
                    break;
                }
                case X: {
                    obj.hitVec = new Vec3((double)Math.round(obj.hitVec.xCoord), obj.hitVec.yCoord, obj.hitVec.zCoord);
                    break;
                }
            }
            if (this.face != EnumFacing.DOWN && this.face != EnumFacing.UP) {
                final IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(obj.getBlockPos());
                final Block blockAtPos = blockState.getBlock();
                double blockFaceOffset;
                if (blockAtPos instanceof BlockSlab && !((BlockSlab)blockAtPos).isDouble()) {
                    final BlockSlab.EnumBlockHalf half = blockState.getValue(BlockSlab.HALF);
                    blockFaceOffset = RandomUtils.nextDouble(0.1, 0.4);
                    if (half == BlockSlab.EnumBlockHalf.TOP) {
                        blockFaceOffset += 0.5;
                    }
                }
                else {
                    blockFaceOffset = RandomUtils.nextDouble(0.1, 0.9);
                }
                obj.hitVec = obj.hitVec.addVector(0.0, -blockFaceOffset, 0.0);
            }
            return obj.hitVec;
        }
    }
}
