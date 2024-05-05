package rosegoldclient.features;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rosegoldclient.Main;
import rosegoldclient.events.PlayerMoveEvent;
import rosegoldclient.events.ScreenClosedEvent;
import rosegoldclient.events.SecondEvent;
import rosegoldclient.events.TickEndEvent;
import rosegoldclient.utils.RenderUtils;
import rosegoldclient.utils.RotationUtils;

import java.awt.*;
import java.util.HashSet;

public class ChestAura {
    private static final HashSet<BlockPos> allChests = new HashSet<>();
    private static final HashSet<BlockPos> chestsInRange = new HashSet<>();
    private static BlockPos selectedBlock = null;
    private static BlockPos lastCheckedPosition = null;
    public static final HashSet<BlockPos> usedBlocks = new HashSet<>();
    private static int waitingForChestClose = 0;

    @SubscribeEvent
    public void chestESP(TickEndEvent event) {
        if (Main.mc.player == null || Main.mc.world == null) return;
        if (!Main.configFile.chestAura) return;
        if(!Main.configFile.chestESP) return;
        BlockPos playerPosition = Main.mc.player.getPosition();
        if ((lastCheckedPosition == null || !lastCheckedPosition.equals(playerPosition))) {
            allChests.clear();
            lastCheckedPosition = playerPosition;
            int espRange = Main.configFile.chestESPRange;
            for (int x = playerPosition.getX() - espRange; x < playerPosition.getX() + espRange; x++) {
                for (int y = playerPosition.getY() - espRange; y < playerPosition.getY() + espRange; y++) {
                    for (int z = playerPosition.getZ() - espRange; z < playerPosition.getZ() + espRange; z++) {
                        BlockPos position = new BlockPos(x, y, z);
                        Block block = Main.mc.world.getBlockState(position).getBlock();

                        if (block instanceof BlockChest) {
                            allChests.add(position);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void chestsInRange(TickEndEvent event) {
        if (Main.mc.player == null || Main.mc.world == null) return;
        if (!Main.configFile.chestAura) return;
        if(Main.mc.player.ticksExisted % 4 != 0) return;
        int range = Main.configFile.chestAuraRange;
        BlockPos playerPosition = Main.mc.player.getPosition();
        chestsInRange.clear();
        for (int x = playerPosition.getX() - range; x < playerPosition.getX() + range; x++) {
            for (int y = playerPosition.getY() - range; y < playerPosition.getY() + range; y++) {
                for (int z = playerPosition.getZ() - range; z < playerPosition.getZ() + range; z++) {
                    BlockPos position = new BlockPos(x, y, z);
                    Block block = Main.mc.world.getBlockState(position).getBlock();

                    if (block instanceof BlockChest) {
                        chestsInRange.add(position);
                    }
                }
            }
        }
        for (BlockPos chest : chestsInRange) {
            if (selectedBlock != null) return;
            if (!canOpenChest(chest)) continue;
            switch (Main.configFile.chestAuraRangeType) {
                case 1:
                    interactWithChest(chest);
                    usedBlocks.add(chest);
                    return;
                case 0:
                    Vec3d playerPos = Main.mc.player.getPositionEyes(1f);
                    double distance = playerPos.distanceTo(new Vec3d(chest.getX() + 0.5, chest.getY() + 0.5, chest.getZ() + 0.5));
                    if (distance < Main.configFile.chestAuraRange) {
                        interactWithChest(chest);
                        usedBlocks.add(chest);
                    }
                    return;
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onUpdatePre(PlayerMoveEvent.Pre pre) {
        if (selectedBlock != null) {
            RotationUtils.smoothLook(RotationUtils.getRotationToBlock(selectedBlock), 0, () -> {});
        }
    }

    @SubscribeEvent
    public void onSecond(SecondEvent event) {
        if (Main.mc.player == null) return;
        if (!Main.configFile.chestAura) return;
        if (Main.mc.currentScreen == null) {
            if(waitingForChestClose > 0) waitingForChestClose--;
            if(waitingForChestClose == 0) selectedBlock = null; CursorTP.disable = false;
            lastCheckedPosition = null;
        }
    }

    @SubscribeEvent
    public void onScreenClosed(ScreenClosedEvent event) {
        selectedBlock = null;
        CursorTP.disable = false;
        waitingForChestClose = 0;
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!Main.configFile.chestAura) return;
        if (Main.configFile.chestESP) {
            for (BlockPos block : allChests) {
                if(usedBlocks.contains(block)) {
                    RenderUtils.drawBlockESP(block, Color.GREEN, event.getPartialTicks());
                } else {
                    RenderUtils.drawBlockESP(block, new Color(255, 128, 0), event.getPartialTicks());
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        chestsInRange.clear();
        allChests.clear();
        usedBlocks.clear();
        selectedBlock = null;
        lastCheckedPosition = null;
        CursorTP.disable = false;
    }

    private static boolean canOpenChest(BlockPos blockPos) {
        if(Main.configFile.onlyConfirmedChests) {
            return Main.mc.player.onGround && Main.mc.currentScreen == null && !usedBlocks.contains(blockPos) && WynncraftChestESP.chests.contains(blockPos);
        } else {
            return Main.mc.player.onGround && Main.mc.currentScreen == null && !usedBlocks.contains(blockPos);
        }
    }

    private static void interactWithChest(BlockPos block) {
        CursorTP.disable = true;
        waitingForChestClose = 1;
        selectedBlock = block;
        Main.mc.playerController.processRightClickBlock(
                Main.mc.player,
                Main.mc.world,
                block,
                EnumFacing.fromAngle(Main.mc.player.rotationYaw),
                new Vec3d(Math.random(), Math.random(), Math.random()),
                EnumHand.MAIN_HAND
        );
        Main.mc.player.swingArm(EnumHand.MAIN_HAND);
    }
}
