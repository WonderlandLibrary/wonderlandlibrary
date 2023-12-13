package me.earth.phobos.features.modules.combat;

import me.earth.phobos.Phobos;
import me.earth.phobos.event.events.Render3DEvent;
import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
import me.earth.phobos.features.command.Command;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.modules.client.ClickGui;
import me.earth.phobos.features.modules.client.Colors;
import me.earth.phobos.features.modules.client.ServerModule;
import me.earth.phobos.features.modules.player.BlockTweaks;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.Timer;
import me.earth.phobos.util.*;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.List;
import java.util.*;

public class Surround
        extends Module {
    public static boolean isPlacing = false;
    private final Setting<Boolean> server = this.register(new Setting<Boolean>("Server", false));
    private final Setting<Integer> delay = this.register(new Setting<Integer>("Delay/Place", 50, 0, 250));
    private final Setting<Integer> blocksPerTick = this.register(new Setting<Integer>("Block/Place", 8, 1, 20));
    private final Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", true));
    private final Setting<Boolean> raytrace = this.register(new Setting<Boolean>("Raytrace", false));
    private final Setting<InventoryUtil.Switch> switchMode = this.register(new Setting<InventoryUtil.Switch>("Switch", InventoryUtil.Switch.NORMAL));
    private final Setting<Boolean> center = this.register(new Setting<Boolean>("Center", false));
    private final Setting<Boolean> helpingBlocks = this.register(new Setting<Boolean>("HelpingBlocks", true));
    private final Setting<Boolean> intelligent = this.register(new Setting<Object>("Intelligent", Boolean.valueOf(false), v -> this.helpingBlocks.getValue()));
    private final Setting<Boolean> antiPedo = this.register(new Setting<Boolean>("NoPedo", false));
    private final Setting<Integer> extender = this.register(new Setting<Integer>("Extend", 1, 1, 4));
    private final Setting<Boolean> extendMove = this.register(new Setting<Object>("MoveExtend", Boolean.valueOf(false), v -> this.extender.getValue() > 1));
    private final Setting<MovementMode> movementMode = this.register(new Setting<MovementMode>("Movement", MovementMode.STATIC));
    private final Setting<Double> speed = this.register(new Setting<Object>("Speed", 10.0, 0.0, 30.0, v -> this.movementMode.getValue() == MovementMode.LIMIT || this.movementMode.getValue() == MovementMode.OFF, "Maximum Movement Speed"));
    private final Setting<Integer> eventMode = this.register(new Setting<Integer>("Updates", 3, 1, 3));
    private final Setting<Boolean> floor = this.register(new Setting<Boolean>("Floor", false));
    private final Setting<Boolean> echests = this.register(new Setting<Boolean>("Echests", false));
    private final Setting<Boolean> noGhost = this.register(new Setting<Boolean>("Packet", false));
    private final Setting<Boolean> info = this.register(new Setting<Boolean>("Info", false));
    private final Setting<Integer> retryer = this.register(new Setting<Integer>("Retries", 4, 1, 15));
    private final Setting<Boolean> endPortals = this.register(new Setting<Boolean>("EndPortals", false));
    private final Setting<Boolean> render = this.register(new Setting<Boolean>("Render", true));
    public final Setting<Boolean> colorSync = this.register(new Setting<Object>("Sync", Boolean.valueOf(false), v -> this.render.getValue()));
    public final Setting<Boolean> box = this.register(new Setting<Object>("Box", Boolean.valueOf(false), v -> this.render.getValue()));
    public final Setting<Boolean> outline = this.register(new Setting<Object>("Outline", Boolean.valueOf(true), v -> this.render.getValue()));
    public final Setting<Boolean> customOutline = this.register(new Setting<Object>("CustomLine", Boolean.valueOf(false), v -> this.outline.getValue() != false && this.render.getValue() != false));
    private final Setting<Integer> red = this.register(new Setting<Object>("Red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.render.getValue()));
    private final Setting<Integer> green = this.register(new Setting<Object>("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.render.getValue()));
    private final Setting<Integer> blue = this.register(new Setting<Object>("Blue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.render.getValue()));
    private final Setting<Integer> alpha = this.register(new Setting<Object>("Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.render.getValue()));
    private final Setting<Integer> boxAlpha = this.register(new Setting<Object>("BoxAlpha", Integer.valueOf(125), Integer.valueOf(0), Integer.valueOf(255), v -> this.box.getValue() != false && this.render.getValue() != false));
    private final Setting<Float> lineWidth = this.register(new Setting<Object>("LineWidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f), v -> this.outline.getValue() != false && this.render.getValue() != false));
    private final Setting<Integer> cRed = this.register(new Setting<Object>("OL-Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.customOutline.getValue() != false && this.outline.getValue() != false && this.render.getValue() != false));
    private final Setting<Integer> cGreen = this.register(new Setting<Object>("OL-Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.customOutline.getValue() != false && this.outline.getValue() != false && this.render.getValue() != false));
    private final Setting<Integer> cBlue = this.register(new Setting<Object>("OL-Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.customOutline.getValue() != false && this.outline.getValue() != false && this.render.getValue() != false));
    private final Setting<Integer> cAlpha = this.register(new Setting<Object>("OL-Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.customOutline.getValue() != false && this.outline.getValue() != false && this.render.getValue() != false));
    private final Timer timer = new Timer();
    private final Timer retryTimer = new Timer();
    private final Set<Vec3d> extendingBlocks = new HashSet<Vec3d>();
    private final Map<BlockPos, Integer> retries = new HashMap<BlockPos, Integer>();
    private int isSafe;
    private BlockPos startPos;
    private boolean didPlace = false;
    private boolean switchedItem;
    private int lastHotbarSlot;
    private boolean isSneaking;
    private int placements = 0;
    private int extenders = 1;
    private int obbySlot = -1;
    private boolean offHand = false;
    private List<BlockPos> placeVectors = new ArrayList<BlockPos>();

    public Surround() {
        super("Surround", "Surrounds you with Obsidian", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onEnable() {
        if (Surround.fullNullCheck()) {
            this.disable();
        }
        this.lastHotbarSlot = Surround.mc.player.inventory.currentItem;
        this.startPos = EntityUtil.getRoundedBlockPos(Surround.mc.player);
        if (this.center.getValue().booleanValue() && !Phobos.moduleManager.isModuleEnabled("Freecam")) {
            if (Surround.mc.world.getBlockState(new BlockPos(Surround.mc.player.getPositionVector())).getBlock() == Blocks.WEB) {
                Phobos.positionManager.setPositionPacket(Surround.mc.player.posX, this.startPos.getY(), Surround.mc.player.posZ, true, true, true);
            } else {
                Phobos.positionManager.setPositionPacket((double) this.startPos.getX() + 0.5, this.startPos.getY(), (double) this.startPos.getZ() + 0.5, true, true, true);
            }
        }
        if (this.shouldServer()) {
            Surround.mc.player.connection.sendPacket(new CPacketChatMessage("@Serverprefix" + ClickGui.getInstance().prefix.getValue()));
            Surround.mc.player.connection.sendPacket(new CPacketChatMessage("@Server" + ClickGui.getInstance().prefix.getValue() + "module Surround set Enabled true"));
            return;
        }
        this.retries.clear();
        this.retryTimer.reset();
    }

    @Override
    public void onTick() {
        if (this.eventMode.getValue() == 3) {
            this.doFeetPlace();
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0 && this.eventMode.getValue() == 2) {
            this.doFeetPlace();
        }
    }

    @Override
    public void onUpdate() {
        if (this.eventMode.getValue() == 1) {
            this.doFeetPlace();
        }
        if (this.isSafe == 2) {
            this.placeVectors = new ArrayList<BlockPos>();
        }
    }

    @Override
    public void onDisable() {
        if (Surround.nullCheck()) {
            return;
        }
        if (this.shouldServer()) {
            Surround.mc.player.connection.sendPacket(new CPacketChatMessage("@Serverprefix" + ClickGui.getInstance().prefix.getValue()));
            Surround.mc.player.connection.sendPacket(new CPacketChatMessage("@Server" + ClickGui.getInstance().prefix.getValue() + "module Surround set Enabled false"));
            return;
        }
        isPlacing = false;
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        this.switchItem(true);
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (this.render.getValue().booleanValue() && (this.isSafe == 0 || this.isSafe == 1)) {
            this.placeVectors = this.fuckYou3arthqu4keYourCodeIsGarbage();
            for (BlockPos pos : this.placeVectors) {
                if (!(Surround.mc.world.getBlockState(pos).getBlock() instanceof BlockAir)) continue;
                RenderUtil.drawBoxESP(pos, this.colorSync.getValue() != false ? Colors.INSTANCE.getCurrentColor() : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), this.customOutline.getValue(), new Color(this.cRed.getValue(), this.cGreen.getValue(), this.cBlue.getValue(), this.cAlpha.getValue()), this.lineWidth.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), false);
            }
        }
    }

    @Override
    public String getDisplayInfo() {
        if (!this.info.getValue().booleanValue()) {
            return null;
        }
        switch (this.isSafe) {
            case 0: {
                return "\u00a7cUnsafe";
            }
            case 1: {
                return "\u00a7eSecure";
            }
        }
        return "\u00a7aSecure";
    }

    private boolean shouldServer() {
        return ServerModule.getInstance().isConnected() && this.server.getValue() != false;
    }

    private void doFeetPlace() {
        if (this.check()) {
            return;
        }
        if (!EntityUtil.isSafe(Surround.mc.player, 0, this.floor.getValue(), true)) {
            this.isSafe = 0;
            this.placeBlocks(Surround.mc.player.getPositionVector(), EntityUtil.getUnsafeBlockArray(Surround.mc.player, 0, this.floor.getValue(), true), this.helpingBlocks.getValue(), false, false);
        } else if (!EntityUtil.isSafe(Surround.mc.player, -1, false, true)) {
            this.isSafe = 1;
            if (this.antiPedo.getValue().booleanValue()) {
                this.placeBlocks(Surround.mc.player.getPositionVector(), EntityUtil.getUnsafeBlockArray(Surround.mc.player, -1, false, true), false, false, true);
            }
        } else {
            this.isSafe = 2;
        }
        this.processExtendingBlocks();
        if (this.didPlace) {
            this.timer.reset();
        }
    }

    private void processExtendingBlocks() {
        if (this.extendingBlocks.size() == 2 && this.extenders < this.extender.getValue()) {
            Vec3d[] array = new Vec3d[2];
            int i = 0;
            Iterator<Vec3d> iterator = this.extendingBlocks.iterator();
            while (iterator.hasNext()) {
                Vec3d vec3d;
                array[i] = vec3d = iterator.next();
                ++i;
            }
            int placementsBefore = this.placements;
            if (this.areClose(array) != null) {
                this.placeBlocks(this.areClose(array), EntityUtil.getUnsafeBlockArrayFromVec3d(this.areClose(array), 0, this.floor.getValue(), true), this.helpingBlocks.getValue(), false, true);
            }
            if (placementsBefore < this.placements) {
                this.extendingBlocks.clear();
            }
        } else if (this.extendingBlocks.size() > 2 || this.extenders >= this.extender.getValue()) {
            this.extendingBlocks.clear();
        }
    }

    private Vec3d areClose(Vec3d[] vec3ds) {
        int matches = 0;
        for (Vec3d vec3d : vec3ds) {
            for (Vec3d pos : EntityUtil.getUnsafeBlockArray(Surround.mc.player, 0, this.floor.getValue(), true)) {
                if (!vec3d.equals(pos)) continue;
                ++matches;
            }
        }
        if (matches == 2) {
            return Surround.mc.player.getPositionVector().add(vec3ds[0].add(vec3ds[1]));
        }
        return null;
    }

    private boolean placeBlocks(Vec3d pos, Vec3d[] vec3ds, boolean hasHelpingBlocks, boolean isHelping, boolean isExtending) {
        int helpings = 0;
        boolean gotHelp = true;
        block6:
        for (Vec3d vec3d : vec3ds) {
            gotHelp = true;
            if (isHelping && !this.intelligent.getValue().booleanValue() && ++helpings > 1) {
                return false;
            }
            BlockPos position = new BlockPos(pos).add(vec3d.x, vec3d.y, vec3d.z);
            switch (BlockUtil.isPositionPlaceable(position, this.raytrace.getValue())) {
                case -1: {
                    continue block6;
                }
                case 1: {
                    if ((this.switchMode.getValue() == InventoryUtil.Switch.SILENT || BlockTweaks.getINSTANCE().isOn() && BlockTweaks.getINSTANCE().noBlock.getValue().booleanValue()) && (this.retries.get(position) == null || this.retries.get(position) < this.retryer.getValue())) {
                        this.placeBlock(position);
                        this.retries.put(position, this.retries.get(position) == null ? 1 : this.retries.get(position) + 1);
                        this.retryTimer.reset();
                        continue block6;
                    }
                    if (!this.extendMove.getValue().booleanValue() && Phobos.speedManager.getSpeedKpH() != 0.0 || isExtending || this.extenders >= this.extender.getValue())
                        continue block6;
                    this.placeBlocks(Surround.mc.player.getPositionVector().add(vec3d), EntityUtil.getUnsafeBlockArrayFromVec3d(Surround.mc.player.getPositionVector().add(vec3d), 0, this.floor.getValue(), true), hasHelpingBlocks, false, true);
                    this.extendingBlocks.add(vec3d);
                    ++this.extenders;
                    continue block6;
                }
                case 2: {
                    if (!hasHelpingBlocks) continue block6;
                    gotHelp = this.placeBlocks(pos, BlockUtil.getHelpingBlocks(vec3d), false, true, true);
                }
                case 3: {
                    if (gotHelp) {
                        this.placeBlock(position);
                    }
                    if (!isHelping) continue block6;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean check() {
        if (Surround.fullNullCheck() || this.shouldServer()) {
            return true;
        }
        if (this.endPortals.getValue().booleanValue()) {
            this.offHand = InventoryUtil.isBlock(Surround.mc.player.getHeldItemOffhand().getItem(), BlockEndPortalFrame.class);
            if (!this.offHand) {
                this.offHand = InventoryUtil.isBlock(Surround.mc.player.getHeldItemOffhand().getItem(), BlockObsidian.class);
            }
        } else {
            this.offHand = InventoryUtil.isBlock(Surround.mc.player.getHeldItemOffhand().getItem(), BlockObsidian.class);
        }
        isPlacing = false;
        this.didPlace = false;
        this.extenders = 1;
        this.placements = 0;
        if (this.endPortals.getValue().booleanValue()) {
            this.obbySlot = InventoryUtil.findHotbarBlock(BlockEndPortalFrame.class);
            if (this.obbySlot == -1) {
                this.obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            }
        } else {
            this.obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        }
        int echestSlot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
        if (this.isOff()) {
            return true;
        }
        if (this.retryTimer.passedMs(2500L)) {
            this.retries.clear();
            this.retryTimer.reset();
        }
        this.switchItem(true);
        if (!(this.obbySlot != -1 || this.offHand || this.echests.getValue().booleanValue() && echestSlot != -1)) {
            if (this.info.getValue().booleanValue()) {
                Command.sendMessage("<" + this.getDisplayName() + "> " + "\u00a7c" + "You are out of Obsidian.");
            }
            this.disable();
            return true;
        }
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        if (Surround.mc.player.inventory.currentItem != this.lastHotbarSlot && Surround.mc.player.inventory.currentItem != this.obbySlot && Surround.mc.player.inventory.currentItem != echestSlot) {
            this.lastHotbarSlot = Surround.mc.player.inventory.currentItem;
        }
        switch (this.movementMode.getValue()) {
            case NONE: {
                break;
            }
            case STATIC: {
                if (!this.startPos.equals(EntityUtil.getRoundedBlockPos(Surround.mc.player))) {
                    this.disable();
                    return true;
                }
            }
            case LIMIT: {
                if (!(Phobos.speedManager.getSpeedKpH() > this.speed.getValue())) break;
                return true;
            }
            case OFF: {
                if (!(Phobos.speedManager.getSpeedKpH() > this.speed.getValue())) break;
                this.disable();
                return true;
            }
        }
        return Phobos.moduleManager.isModuleEnabled("Freecam") || !this.timer.passedMs(this.delay.getValue().intValue()) || this.switchMode.getValue() == InventoryUtil.Switch.NONE && Surround.mc.player.inventory.currentItem != InventoryUtil.findHotbarBlock(BlockObsidian.class);
    }

    private void placeBlock(BlockPos pos) {
        if (this.placements < this.blocksPerTick.getValue() && this.switchItem(false)) {
            isPlacing = true;
            this.isSneaking = BlockUtil.placeBlock(pos, this.offHand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, this.rotate.getValue(), this.noGhost.getValue(), this.isSneaking);
            this.didPlace = true;
            ++this.placements;
        }
    }

    private boolean switchItem(boolean back) {
        if (this.offHand) {
            return true;
        }
        boolean[] value = InventoryUtil.switchItem(back, this.lastHotbarSlot, this.switchedItem, this.switchMode.getValue(), this.obbySlot == -1 ? BlockEnderChest.class : (this.endPortals.getValue() != false && InventoryUtil.findHotbarBlock(BlockEndPortalFrame.class) != -1 ? BlockEndPortalFrame.class : BlockObsidian.class));
        this.switchedItem = value[0];
        return value[1];
    }

    private List<BlockPos> fuckYou3arthqu4keYourCodeIsGarbage() {
        if (this.floor.getValue().booleanValue()) {
            return Arrays.asList(new BlockPos(Surround.mc.player.getPositionVector()).add(0, -1, 0), new BlockPos(Surround.mc.player.getPositionVector()).add(1, 0, 0), new BlockPos(Surround.mc.player.getPositionVector()).add(-1, 0, 0), new BlockPos(Surround.mc.player.getPositionVector()).add(0, 0, -1), new BlockPos(Surround.mc.player.getPositionVector()).add(0, 0, 1));
        }
        return Arrays.asList(new BlockPos(Surround.mc.player.getPositionVector()).add(1, 0, 0), new BlockPos(Surround.mc.player.getPositionVector()).add(-1, 0, 0), new BlockPos(Surround.mc.player.getPositionVector()).add(0, 0, -1), new BlockPos(Surround.mc.player.getPositionVector()).add(0, 0, 1));
    }

    public enum MovementMode {
        NONE,
        STATIC,
        LIMIT,
        OFF

    }
}

