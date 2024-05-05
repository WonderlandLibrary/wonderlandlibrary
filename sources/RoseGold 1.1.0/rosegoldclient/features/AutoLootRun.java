package rosegoldclient.features;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rosegoldclient.Main;
import rosegoldclient.events.PlayerMoveEvent;
import rosegoldclient.events.ScreenClosedEvent;
import rosegoldclient.events.SecondEvent;
import rosegoldclient.events.TickEndEvent;
import rosegoldclient.utils.RotationUtils;

import java.util.HashSet;

import static rosegoldclient.features.WynncraftChestESP.chests;

public class AutoLootRun {
    public static boolean doing = false;
    private static final HashSet<BlockPos> allChests = new HashSet<>();
    private static final HashSet<BlockPos> chestsInRange = new HashSet<>();
    private static BlockPos selectedBlock = null;
    public static final HashSet<BlockPos> usedBlocks = new HashSet<>();
    private static int waitingForChestClose = 0;

    @SubscribeEvent
    public void chestsInRange(TickEndEvent event) {
        if (Main.mc.player == null || Main.mc.world == null) return;
        if (!doing) return;
        if(Main.mc.player.ticksExisted % 4 != 0) return;
        int range = 5;
        BlockPos playerPosition = Main.mc.player.getPosition();
        chestsInRange.clear();
        if(selectedBlock == null) {
            for(BlockPos blockPos : chests) {
                Main.mc.player.setPosition(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            }
        }
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
            interactWithChest(chest);
            usedBlocks.add(chest);
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
        if (Main.mc.currentScreen == null) {
            if(waitingForChestClose > 0) waitingForChestClose--;
            if(waitingForChestClose == 0) selectedBlock = null; CursorTP.disable = false;
        }
    }

    @SubscribeEvent
    public void onScreenClosed(ScreenClosedEvent event) {
        selectedBlock = null;
        CursorTP.disable = false;
        waitingForChestClose = 0;
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        chestsInRange.clear();
        allChests.clear();
        usedBlocks.clear();
        selectedBlock = null;
        CursorTP.disable = false;
    }

    private static boolean canOpenChest(BlockPos blockPos) {
        return Main.mc.player.onGround && Main.mc.currentScreen == null && !usedBlocks.contains(blockPos);
    }

    private static void interactWithChest(BlockPos block) {
        doing = false;
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
