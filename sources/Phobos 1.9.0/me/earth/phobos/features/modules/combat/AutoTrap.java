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
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.List;
import java.util.*;

public class AutoTrap
        extends Module {
    public static boolean isPlacing = false;
    private final Setting<Boolean> server = this.register(new Setting<Boolean>("Server", false));
    private final Setting<Integer> delay = this.register(new Setting<Integer>("Delay/Place", 50, 0, 250));
    private final Setting<Integer> blocksPerPlace = this.register(new Setting<Integer>("Block/Place", 8, 1, 30));
    private final Setting<Double> targetRange = this.register(new Setting<Double>("TargetRange", 10.0, 0.0, 20.0));
    private final Setting<Double> range = this.register(new Setting<Double>("PlaceRange", 6.0, 0.0, 10.0));
    private final Setting<TargetMode> targetMode = this.register(new Setting<TargetMode>("Target", TargetMode.CLOSEST));
    private final Setting<InventoryUtil.Switch> switchMode = this.register(new Setting<InventoryUtil.Switch>("Switch", InventoryUtil.Switch.NORMAL));
    private final Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", true));
    private final Setting<Boolean> raytrace = this.register(new Setting<Boolean>("Raytrace", false));
    private final Setting<Pattern> pattern = this.register(new Setting<Pattern>("Pattern", Pattern.STATIC));
    private final Setting<Integer> extend = this.register(new Setting<Object>("Extend", 4, 1, 4, v -> this.pattern.getValue() != Pattern.STATIC, "Extending the Trap."));
    private final Setting<Boolean> antiScaffold = this.register(new Setting<Boolean>("AntiScaffold", false));
    private final Setting<Boolean> antiStep = this.register(new Setting<Boolean>("AntiStep", false));
    private final Setting<Boolean> face = this.register(new Setting<Boolean>("Face", true));
    private final Setting<Boolean> legs = this.register(new Setting<Object>("Legs", Boolean.valueOf(false), v -> this.pattern.getValue() != Pattern.OPEN));
    private final Setting<Boolean> platform = this.register(new Setting<Object>("Platform", Boolean.valueOf(false), v -> this.pattern.getValue() != Pattern.OPEN));
    private final Setting<Boolean> antiDrop = this.register(new Setting<Boolean>("AntiDrop", false));
    private final Setting<Double> speed = this.register(new Setting<Double>("Speed", 10.0, 0.0, 30.0));
    private final Setting<Boolean> antiSelf = this.register(new Setting<Boolean>("AntiSelf", false));
    private final Setting<Integer> eventMode = this.register(new Setting<Integer>("Updates", 3, 1, 3));
    private final Setting<Boolean> freecam = this.register(new Setting<Boolean>("Freecam", false));
    private final Setting<Boolean> info = this.register(new Setting<Boolean>("Info", false));
    private final Setting<Boolean> entityCheck = this.register(new Setting<Boolean>("NoBlock", true));
    private final Setting<Boolean> noScaffoldExtend = this.register(new Setting<Boolean>("NoScaffoldExtend", false));
    private final Setting<Boolean> disable = this.register(new Setting<Boolean>("TSelfMove", false));
    private final Setting<Boolean> packet = this.register(new Setting<Boolean>("Packet", false));
    private final Setting<Boolean> airPacket = this.register(new Setting<Object>("AirPacket", Boolean.valueOf(false), v -> this.packet.getValue()));
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
    private final Map<BlockPos, Integer> retries = new HashMap<BlockPos, Integer>();
    private final Timer retryTimer = new Timer();
    private final Map<BlockPos, IBlockState> toAir = new HashMap<BlockPos, IBlockState>();
    public EntityPlayer target;
    private boolean didPlace = false;
    private boolean switchedItem;
    private boolean isSneaking;
    private int lastHotbarSlot;
    private int placements = 0;
    private boolean smartRotate = false;
    private BlockPos startPos = null;
    private List<Vec3d> currentPlaceList = new ArrayList<Vec3d>();

    public AutoTrap() {
        super("AutoTrap", "Traps other players", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onEnable() {
        if (AutoTrap.fullNullCheck()) {
            this.disable();
            return;
        }
        this.toAir.clear();
        this.startPos = EntityUtil.getRoundedBlockPos(AutoTrap.mc.player);
        this.lastHotbarSlot = AutoTrap.mc.player.inventory.currentItem;
        this.retries.clear();
        if (this.shouldServer()) {
            AutoTrap.mc.player.connection.sendPacket(new CPacketChatMessage("@Serverprefix" + ClickGui.getInstance().prefix.getValue()));
            AutoTrap.mc.player.connection.sendPacket(new CPacketChatMessage("@Server" + ClickGui.getInstance().prefix.getValue() + "module AutoTrap set Enabled true"));
        }
    }

    @Override
    public void onLogout() {
        this.disable();
    }

    @Override
    public void onTick() {
        if (this.eventMode.getValue() == 3) {
            this.smartRotate = false;
            this.doTrap();
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0 && this.eventMode.getValue() == 2) {
            this.smartRotate = this.rotate.getValue() != false && this.blocksPerPlace.getValue() == 1;
            this.doTrap();
        }
    }

    @Override
    public void onUpdate() {
        if (this.eventMode.getValue() == 1) {
            this.smartRotate = false;
            this.doTrap();
        }
    }

    @Override
    public String getDisplayInfo() {
        if (this.info.getValue().booleanValue() && this.target != null) {
            return this.target.getName();
        }
        return null;
    }

    @Override
    public void onDisable() {
        if (AutoTrap.fullNullCheck()) {
            return;
        }
        if (this.shouldServer()) {
            AutoTrap.mc.player.connection.sendPacket(new CPacketChatMessage("@Serverprefix" + ClickGui.getInstance().prefix.getValue()));
            AutoTrap.mc.player.connection.sendPacket(new CPacketChatMessage("@Server" + ClickGui.getInstance().prefix.getValue() + "module AutoTrap set Enabled false"));
            return;
        }
        isPlacing = false;
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        this.switchItem(true);
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (this.render.getValue().booleanValue() && this.currentPlaceList != null) {
            for (Vec3d vec : this.currentPlaceList) {
                BlockPos pos = new BlockPos(vec);
                if (!(AutoTrap.mc.world.getBlockState(pos).getBlock() instanceof BlockAir)) continue;
                RenderUtil.drawBoxESP(pos, this.colorSync.getValue() != false ? Colors.INSTANCE.getCurrentColor() : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), this.customOutline.getValue(), new Color(this.cRed.getValue(), this.cGreen.getValue(), this.cBlue.getValue(), this.cAlpha.getValue()), this.lineWidth.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), false);
            }
        }
    }

    private boolean shouldServer() {
        return ServerModule.getInstance().isConnected() && this.server.getValue() != false;
    }

    private void doTrap() {
        if (this.shouldServer() || this.check()) {
            return;
        }
        switch (this.pattern.getValue()) {
            case STATIC: {
                this.doStaticTrap();
                break;
            }
            case SMART:
            case OPEN: {
                this.doSmartTrap();
                break;
            }
        }
        if (this.packet.getValue().booleanValue() && this.airPacket.getValue().booleanValue()) {
            for (Map.Entry<BlockPos, IBlockState> entry : this.toAir.entrySet()) {
                AutoTrap.mc.world.setBlockState(entry.getKey(), entry.getValue());
            }
            this.toAir.clear();
        }
        if (this.didPlace) {
            this.timer.reset();
        }
    }

    private void doSmartTrap() {
        List<Vec3d> placeTargets = EntityUtil.getUntrappedBlocksExtended(this.extend.getValue(), this.target, this.antiScaffold.getValue(), this.antiStep.getValue(), this.legs.getValue(), this.platform.getValue(), this.antiDrop.getValue(), this.raytrace.getValue(), this.noScaffoldExtend.getValue(), this.face.getValue());
        this.placeList(placeTargets);
        this.currentPlaceList = placeTargets;
    }

    private void doStaticTrap() {
        List<Vec3d> placeTargets = EntityUtil.targets(this.target.getPositionVector(), this.antiScaffold.getValue(), this.antiStep.getValue(), this.legs.getValue(), this.platform.getValue(), this.antiDrop.getValue(), this.raytrace.getValue(), this.face.getValue());
        this.placeList(placeTargets);
        this.currentPlaceList = placeTargets;
    }

    private void placeList(List<Vec3d> list) {
        list.sort((vec3d, vec3d2) -> Double.compare(AutoTrap.mc.player.getDistanceSq(vec3d2.x, vec3d2.y, vec3d2.z), AutoTrap.mc.player.getDistanceSq(vec3d.x, vec3d.y, vec3d.z)));
        list.sort(Comparator.comparingDouble(vec3d -> vec3d.y));
        for (Vec3d vec3d3 : list) {
            BlockPos position = new BlockPos(vec3d3);
            int placeability = BlockUtil.isPositionPlaceable(position, this.raytrace.getValue());
            if (this.entityCheck.getValue().booleanValue() && placeability == 1 && (this.switchMode.getValue() == InventoryUtil.Switch.SILENT || BlockTweaks.getINSTANCE().isOn() && BlockTweaks.getINSTANCE().noBlock.getValue().booleanValue()) && (this.retries.get(position) == null || this.retries.get(position) < this.retryer.getValue())) {
                this.placeBlock(position);
                this.retries.put(position, this.retries.get(position) == null ? 1 : this.retries.get(position) + 1);
                this.retryTimer.reset();
                continue;
            }
            if (placeability != 3 || this.antiSelf.getValue().booleanValue() && MathUtil.areVec3dsAligned(AutoTrap.mc.player.getPositionVector(), vec3d3))
                continue;
            this.placeBlock(position);
        }
    }

    private boolean check() {
        isPlacing = false;
        this.didPlace = false;
        this.placements = 0;
        int obbySlot = -1;
        if (this.endPortals.getValue().booleanValue()) {
            obbySlot = InventoryUtil.findHotbarBlock(BlockEndPortalFrame.class);
            if (obbySlot == -1) {
                obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            }
        } else {
            obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        }
        if (this.isOff()) {
            return true;
        }
        if (this.disable.getValue().booleanValue() && this.startPos != null && !this.startPos.equals(EntityUtil.getRoundedBlockPos(AutoTrap.mc.player))) {
            this.disable();
            return true;
        }
        if (this.retryTimer.passedMs(2000L)) {
            this.retries.clear();
            this.retryTimer.reset();
        }
        if (obbySlot == -1) {
            if (this.switchMode.getValue() != InventoryUtil.Switch.NONE) {
                if (this.info.getValue().booleanValue()) {
                    Command.sendMessage("<" + this.getDisplayName() + "> " + "\u00a7c" + "You are out of Obsidian.");
                }
                this.disable();
            }
            return true;
        }
        if (AutoTrap.mc.player.inventory.currentItem != this.lastHotbarSlot && AutoTrap.mc.player.inventory.currentItem != obbySlot) {
            this.lastHotbarSlot = AutoTrap.mc.player.inventory.currentItem;
        }
        this.switchItem(true);
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        this.target = this.getTarget(this.targetRange.getValue(), this.targetMode.getValue() == TargetMode.UNTRAPPED);
        return this.target == null || Phobos.moduleManager.isModuleEnabled("Freecam") && this.freecam.getValue() == false || !this.timer.passedMs(this.delay.getValue().intValue()) || this.switchMode.getValue() == InventoryUtil.Switch.NONE && AutoTrap.mc.player.inventory.currentItem != InventoryUtil.findHotbarBlock(BlockObsidian.class);
    }

    private EntityPlayer getTarget(double range, boolean trapped) {
        EntityPlayer target = null;
        double distance = Math.pow(range, 2.0) + 1.0;
        for (EntityPlayer player : AutoTrap.mc.world.playerEntities) {
            if (EntityUtil.isntValid(player, range) || this.pattern.getValue() == Pattern.STATIC && trapped && EntityUtil.isTrapped(player, this.antiScaffold.getValue(), this.antiStep.getValue(), this.legs.getValue(), this.platform.getValue(), this.antiDrop.getValue(), this.face.getValue()) || this.pattern.getValue() != Pattern.STATIC && trapped && EntityUtil.isTrappedExtended(this.extend.getValue(), player, this.antiScaffold.getValue(), this.antiStep.getValue(), this.legs.getValue(), this.platform.getValue(), this.antiDrop.getValue(), this.raytrace.getValue(), this.noScaffoldExtend.getValue(), this.face.getValue()) || EntityUtil.getRoundedBlockPos(AutoTrap.mc.player).equals(EntityUtil.getRoundedBlockPos(player)) && this.antiSelf.getValue().booleanValue() || Phobos.speedManager.getPlayerSpeed(player) > this.speed.getValue())
                continue;
            if (target == null) {
                target = player;
                distance = AutoTrap.mc.player.getDistanceSq(player);
                continue;
            }
            if (!(AutoTrap.mc.player.getDistanceSq(player) < distance)) continue;
            target = player;
            distance = AutoTrap.mc.player.getDistanceSq(player);
        }
        return target;
    }

    private void placeBlock(BlockPos pos) {
        if (this.placements < this.blocksPerPlace.getValue() && AutoTrap.mc.player.getDistanceSq(pos) <= MathUtil.square(this.range.getValue()) && this.switchItem(false)) {
            isPlacing = true;
            if (this.airPacket.getValue().booleanValue() && this.packet.getValue().booleanValue()) {
                this.toAir.put(pos, AutoTrap.mc.world.getBlockState(pos));
            }
            this.isSneaking = this.smartRotate ? BlockUtil.placeBlockSmartRotate(pos, EnumHand.MAIN_HAND, true, this.airPacket.getValue() == false && this.packet.getValue() != false, this.isSneaking) : BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, this.rotate.getValue(), this.airPacket.getValue() == false && this.packet.getValue() != false, this.isSneaking);
            this.didPlace = true;
            ++this.placements;
        }
    }

    private boolean switchItem(boolean back) {
        boolean[] value = InventoryUtil.switchItem(back, this.lastHotbarSlot, this.switchedItem, this.switchMode.getValue(), this.endPortals.getValue() != false && InventoryUtil.findHotbarBlock(BlockEndPortalFrame.class) != -1 ? BlockEndPortalFrame.class : BlockObsidian.class);
        this.switchedItem = value[0];
        return value[1];
    }

    public enum TargetMode {
        CLOSEST,
        UNTRAPPED

    }

    public enum Pattern {
        STATIC,
        SMART,
        OPEN

    }
}

