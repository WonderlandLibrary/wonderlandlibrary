package me.dev.legacy.features.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.dev.legacy.Legacy;
import me.dev.legacy.features.command.Command;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import me.dev.legacy.util.*;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutoTrap extends Module {
    public static boolean isPlacing = false;
    private final Setting<Integer> delay = register(new Setting("Delay", 50, 0, 250));
    private final Setting<Integer> blocksPerPlace = register(new Setting("BlocksPerTick", 8, 1, 30));
    private final Setting<Boolean> rotate = register(new Setting("Rotate", false));
    private final Setting<Boolean> raytrace = register(new Setting("Raytrace", false));
    private final Setting<Boolean> antiScaffold = register(new Setting("AntiScaffold", false));
    private final Setting<Boolean> antiStep = register(new Setting("AntiStep", false));
    private final Setting<Boolean> noGhost = register(new Setting("Packet", true));
    private final Timer timer = new Timer();
    private final Map<BlockPos, Integer> retries = new HashMap<BlockPos, Integer>();
    private final Timer retryTimer = new Timer();
    public EntityPlayer target;
    private boolean didPlace = false;
    private boolean switchedItem;
    private boolean isSneaking;
    private int lastHotbarSlot;
    private int placements = 0;
    private BlockPos startPos = null;
    private boolean offHand = false;

    public AutoTrap() {
        super("AutoTrap", "Traps other players", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onEnable() {
        if (AutoTrap.fullNullCheck()) {
            return;
        }
        startPos = EntityUtil.getRoundedBlockPos(AutoTrap.mc.player);
        lastHotbarSlot = AutoTrap.mc.player.inventory.currentItem;
        retries.clear();
    }

    @Override
    public void onTick() {
        if (AutoTrap.fullNullCheck()) {
            return;
        }
        doTrap();
    }

    @Override
    public String getDisplayInfo() {
        if (target != null) {
            return target.getName();
        }
        return null;
    }

    @Override
    public void onDisable() {
        isPlacing = false;
        isSneaking = EntityUtil.stopSneaking(isSneaking);
    }

    private void doTrap() {
        if (check()) {
            return;
        }
        doStaticTrap();
        if (didPlace) {
            timer.reset();
        }
    }

    private void doStaticTrap() {
        List<Vec3d> placeTargets = EntityUtil.targets(target.getPositionVector(), antiScaffold.getValue(), antiStep.getValue(), false, false, false, raytrace.getValue());
        placeList(placeTargets);
    }

    private void placeList(List<Vec3d> list) {
        list.sort((vec3d, vec3d2) -> Double.compare(AutoTrap.mc.player.getDistanceSq(vec3d2.x, vec3d2.y, vec3d2.z), AutoTrap.mc.player.getDistanceSq(vec3d.x, vec3d.y, vec3d.z)));
        list.sort(Comparator.comparingDouble(vec3d -> vec3d.y));
        for (Vec3d vec3d3 : list) {
            BlockPos position = new BlockPos(vec3d3);
            int placeability = BlockUtil.isPositionPlaceable(position, raytrace.getValue());
            if (placeability == 1 && (retries.get(position) == null || retries.get(position) < 4)) {
                placeBlock(position);
                retries.put(position, retries.get(position) == null ? 1 : retries.get(position) + 1);
                retryTimer.reset();
                continue;
            }
            if (placeability != 3) continue;
            placeBlock(position);
        }
    }

    private boolean check() {
        isPlacing = false;
        didPlace = false;
        placements = 0;
        int obbySlot2 = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        if (obbySlot2 == -1) {
            toggle();
        }
        int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        if (isOff()) {
            return true;
        }
        if (!startPos.equals(EntityUtil.getRoundedBlockPos(AutoTrap.mc.player))) {
            disable();
            return true;
        }
        if (retryTimer.passedMs(2000L)) {
            retries.clear();
            retryTimer.reset();
        }
        if (obbySlot == -1) {
            Command.sendMessage("<" + getDisplayName() + "> " + ChatFormatting.RED + "No Obsidian in hotbar disabling...");
            disable();
            return true;
        }
        if (AutoTrap.mc.player.inventory.currentItem != lastHotbarSlot && AutoTrap.mc.player.inventory.currentItem != obbySlot) {
            lastHotbarSlot = AutoTrap.mc.player.inventory.currentItem;
        }
        isSneaking = EntityUtil.stopSneaking(isSneaking);
        target = getTarget(10.0, true);
        return target == null || !timer.passedMs(delay.getValue().intValue());
    }

    private EntityPlayer getTarget(double range, boolean trapped) {
        EntityPlayer target = null;
        double distance = Math.pow(range, 2.0) + 1.0;
        for (EntityPlayer player : AutoTrap.mc.world.playerEntities) {
            if (EntityUtil.isntValid(player, range) || trapped && EntityUtil.isTrapped(player, antiScaffold.getValue(), antiStep.getValue(), false, false, false) || Legacy.speedManager.getPlayerSpeed(player) > 10.0)
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
        if (placements < blocksPerPlace.getValue() && AutoTrap.mc.player.getDistanceSq(pos) <= MathUtil.square(5.0)) {
            isPlacing = true;
            int originalSlot = AutoTrap.mc.player.inventory.currentItem;
            int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            int eChestSot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
            if (obbySlot == -1 && eChestSot == -1) {
                toggle();
            }
            if (rotate.getValue()) {
                AutoTrap.mc.player.inventory.currentItem = obbySlot == -1 ? eChestSot : obbySlot;
                AutoTrap.mc.playerController.updateController();
                isSneaking = BlockUtil.placeBlock(pos, offHand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, rotate.getValue(), noGhost.getValue(), isSneaking);
                AutoTrap.mc.player.inventory.currentItem = originalSlot;
                AutoTrap.mc.playerController.updateController();
            } else {
                AutoTrap.mc.player.inventory.currentItem = obbySlot == -1 ? eChestSot : obbySlot;
                AutoTrap.mc.playerController.updateController();
                isSneaking = BlockUtil.placeBlock(pos, offHand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, rotate.getValue(), noGhost.getValue(), isSneaking);
                AutoTrap.mc.player.inventory.currentItem = originalSlot;
                AutoTrap.mc.playerController.updateController();
            }
            didPlace = true;
            ++placements;
        }
    }
}

