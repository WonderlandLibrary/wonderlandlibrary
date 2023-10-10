package rosegoldclient.features;

import net.minecraft.block.Block;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rosegoldclient.Main;
import rosegoldclient.events.PacketSentEvent;
import rosegoldclient.events.TickEndEvent;
import rosegoldclient.utils.RenderUtils;
import rosegoldclient.utils.Utils;
import rosegoldclient.utils.VecUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

public class CursorTP {
    private static BlockPos blockToTeleport;
    private static ArrayList<BlockPos> blocks = new ArrayList<>();
    private static Block b;
    private static boolean onto = false;
    public static boolean disable = false;

    private static final KeyBinding sneak = Main.mc.gameSettings.keyBindSneak;

    @SubscribeEvent
    public void onPacketSent(PacketSentEvent event) {
        if(event.packet instanceof CPacketPlayerTryUseItem ||
                event.packet instanceof CPacketPlayerTryUseItemOnBlock) {
            if (!Main.configFile.cursorTeleport) return;
            if (cantUseItem()) return;
            if (disable) return;
            if (blockToTeleport != null) {
                if(Main.configFile.cursorTeleportCancelClick) {
                    ItemStack itemStack = Main.mc.player.getHeldItemMainhand();
                    if (itemStack.getTagCompound() != null && !(itemStack.getTagCompound().hasNoTags())) {
                        String nbt = itemStack.getTagCompound().toString();
                        if (nbt.contains("§a✔§7 Class Req:")) {
                            event.setCanceled(true);
                        }
                    }
                }
                if(onto) {
                    tpToBlock(blockToTeleport);
                } else {
                    tpToBlock(blockToTeleport.add(0, -1, 0));
                }
                blockToTeleport = null;
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event) {
        if (!Main.configFile.cursorTeleport) return;
        if (cantUseItem()) return;
        if (disable) return;
        if (event instanceof PlayerInteractEvent.RightClickEmpty) {
            if (blockToTeleport != null) {
                if(onto) {
                    tpToBlock(blockToTeleport);
                } else {
                    tpToBlock(blockToTeleport.add(0, -1, 0));
                }
                blockToTeleport = null;
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (Main.mc.player == null || Main.mc.world == null) return;
        if (!Main.configFile.cursorTeleport) return;
        blockToTeleport = null;
        b = null;
        blocks = rayTrace(Main.configFile.cursorTeleportRange, 1);
        Collections.reverse(blocks);
        for(BlockPos blockPos : blocks) {
            BlockPos toTp = getBlockToTp(blockPos);
            if(toTp != null) {
                blockToTeleport = toTp;
                b = Main.mc.world.getBlockState(blockToTeleport).getBlock();
                break;
            }
            onto = false;
        }

    }

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        if (Main.mc.player == null || Main.mc.world == null) return;
        if (!Main.configFile.cursorTeleport) return;
        if (blockToTeleport != null) {
            RenderUtils.drawBlockESP(blockToTeleport, Color.RED, event.getPartialTicks());
            if (b != null && Main.configFile.cursorTeleportShowDistance) {
                RenderUtils.renderWaypointText(b.getLocalizedName(), blockToTeleport.getX() + 0.5, blockToTeleport.getY() + 0.5, blockToTeleport.getZ() + 0.5, event.getPartialTicks());
            }
        }
    }

    private static void tpToBlock(BlockPos blockPos) {
        onto = false;
        Main.mc.player.setPosition(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5);
        switch (Main.configFile.cursorTeleportResetVelocity) {
            case 1:
                Main.mc.player.setVelocity(0, Main.mc.player.motionY, 0);
            case 2:
                Main.mc.player.setVelocity(Main.mc.player.motionX, 0, Main.mc.player.motionZ);
            case 3:
                Main.mc.player.setVelocity(Main.mc.player.motionX, Main.mc.player.motionY, Main.mc.player.motionZ);
        }
    }

    private static BlockPos getBlockToTp(BlockPos blockPos) {
        BlockPos above = blockPos.add(0, 1, 0);
        BlockPos below = blockPos.add(0, -1, 0);
        if(isAir(above)) return blockPos;
        if(isAir(below)) return below;
        return null;
    }

    private static boolean cantUseItem() {
        switch (Main.configFile.cursorTeleportEmptyHand) {
            case 0:
                ItemStack itemStack = Main.mc.player.getHeldItemMainhand();
                if (itemStack.getTagCompound() == null || itemStack.getTagCompound().hasNoTags()) return false;
                String nbt = itemStack.getTagCompound().toString();
                return nbt.contains("§a✔§7 Class Req:");
            case 1:
                return !Main.mc.player.getHeldItemMainhand().isEmpty();
            default:
                return false;
        }
    }

    private static boolean isAir(BlockPos blockPos) {
        return Main.mc.world.getBlockState(blockPos).getCollisionBoundingBox(
                Main.mc.world,
                blockPos
        ) == Block.NULL_AABB;
    }

    public ArrayList<BlockPos> rayTrace(double blockReachDistance, float partialTicks) {
        ArrayList<BlockPos> blockPositions = new ArrayList<>();
        Vec3d pos = Main.mc.player.getPositionEyes(partialTicks);
        Vec3d direction = Main.mc.player.getLook(partialTicks);
        direction = VecUtils.scaleVec(direction, 0.1f);
        boolean flag = false;
        blocks.clear();
        for (int i = 0; i < blockReachDistance * 10; i++) {
            BlockPos blockPos = new BlockPos(pos);
            if(!isAir(blockPos)) {
                if(i == 0) flag = true;
                if(sneak.isKeyDown()) {
                    blockPositions.add(blockPos);
                    onto = true;
                }
                if(!flag) break;
            }
            blockPositions.add(blockPos);
            pos = pos.add(direction);
        }

        LinkedHashSet<BlockPos> set = new LinkedHashSet<>(blockPositions);
        blockPositions.clear();
        blockPositions.addAll(set);

        return blockPositions;
    }
}
