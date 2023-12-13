package today.flux.module.implement.Movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldSettings;
import today.flux.event.*;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.ModuleManager;
import today.flux.module.value.BooleanValue;
import today.flux.module.value.ModeValue;
import today.flux.utility.BlockUtils;
import today.flux.utility.DelayTimer;
import today.flux.utility.PlayerUtils;

/**
 * Created by John on 2016/10/21.
 */
public class NoFall extends Module {
    public static ModeValue mode = new ModeValue("NoFall", "Mode", "Hypixel", "Setback", "Hypixel", "Normal");

    public NoFall() {
        super("NoFall", Category.Movement, false);
    }

    @EventTarget
    public void onPacket(PacketSendEvent e) {
        if (mode.isCurrentMode("Hypixel") && e.getPacket() instanceof C03PacketPlayer && !AntiVoid.isPullbacking()) {
            C03PacketPlayer packet = (C03PacketPlayer) e.getPacket();

            if (!mc.thePlayer.capabilities.isFlying && !mc.thePlayer.capabilities.disableDamage && mc.thePlayer.motionY < 0.0d && packet.isMoving() && mc.thePlayer.fallDistance > 2.0f) {
                e.setCancelled(true);
                mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(packet.x, packet.y, packet.z, packet.onGround));
            }
        }
    }

    private DelayTimer timer = new DelayTimer();

    public static BooleanValue mlg = new BooleanValue("NoFall", "MLGWaterBucket", false);

    @EventTarget
    public void onMove(PreUpdateEvent event) {
        if (mode.getValue().equals("Normal")) {
            if (mc.thePlayer.fallDistance > 2 || PlayerUtils.getDistanceToFall() > 2) {
                event.onGround = true;
            }
        } else if (mode.getValue().equals("Setback")) {
            if (mc.thePlayer.fallDistance > 5) {
                event.y += 0.2;
            }
        }
        if (mode.isCurrentMode("Hypixel") && !AntiVoid.isPullbacking()) {
            if (!mc.thePlayer.capabilities.isFlying && !mc.thePlayer.capabilities.disableDamage && mc.thePlayer.motionY < 0.0d && mc.thePlayer.fallDistance > 3.0f) {
                mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer(true));
            }
        }

        if (mlg.getValue() && ((mc.thePlayer.fallDistance > 4 && BlockUtils.getDistanceToFall() < 10 && getSlotWaterBucket() != -1 && isMLGNeeded()) || reFill)) {
            if (!event.isModified()) {
                event.setPitch(90.0f);
            }
        }

    }

    @EventTarget
    public void onMove(MoveEvent event) {
        if (mlg.getValue() && ((mc.thePlayer.fallDistance > 4 && BlockUtils.getDistanceToFall() < 10 && getSlotWaterBucket() != -1 && isMLGNeeded()) || reFill)) {
            event.setX(0);
            event.setZ(0);
        }
    }

    private boolean reFill;
    private DelayTimer refillTimer = new DelayTimer();

    @EventTarget
    public void onPost(PostUpdateEvent post) {
        if (mlg.getValue()) {
            if (BlockUtils.isInLiquid() && mc.thePlayer.inventory.getCurrentItem() != null && mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBucket && !timer.hasPassed(1000) && reFill) {
                if (((ItemBucket) mc.thePlayer.inventory.getCurrentItem().getItem()).getIsFull().getMaterial() == Material.air) {
                    BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
                    placeWater(pos, EnumFacing.UP);
                    reFill = false;
                }
            } else if (refillTimer.hasPassed(1500) && reFill) {
                reFill = false;
            }

            if (mc.thePlayer.fallDistance > 4 && BlockUtils.getDistanceToFall() < 10 && getSlotWaterBucket() != -1 && isMLGNeeded()) {
                if (BlockUtils.getDistanceToFall() < 3.0 && timer.hasPassed(500)) {
                    timer.reset();

                    swapToWaterBucket(getSlotWaterBucket());

                    BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - BlockUtils.getDistanceToFall() - 1, mc.thePlayer.posZ);
                    placeWater(pos, EnumFacing.UP);
                    reFill = true;
                    refillTimer.reset();
                }
            }
        }

    }




    private void placeWater(BlockPos pos, EnumFacing facing) {
        ItemStack heldItem = mc.thePlayer.inventory.getCurrentItem();

        mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), pos, facing, new Vec3((double) pos.getX() + 0.5, (double) pos.getY() + 1, (double) pos.getZ() + 0.5));
        if (heldItem != null) {
            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, heldItem);
            mc.entityRenderer.itemRenderer.resetEquippedProgress2();
        }
    }

    private void swapToWaterBucket(int blockSlot) {
        mc.thePlayer.inventory.currentItem = blockSlot;
        mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C09PacketHeldItemChange(blockSlot));
    }

    private int getSlotWaterBucket() {
        for (int i = 0; i < 8; i++) {
            if (mc.thePlayer.inventory.mainInventory[i] != null && mc.thePlayer.inventory.mainInventory[i].getItem().getUnlocalizedName().contains("bucketWater"))
                return i;
        }
        return -1;
    }

    private boolean isMLGNeeded() {
        if (mc.playerController.getCurrentGameType() == WorldSettings.GameType.CREATIVE || mc.playerController.getCurrentGameType() == WorldSettings.GameType.SPECTATOR || mc.thePlayer.capabilities.isFlying || mc.thePlayer.capabilities.allowFlying)
            return false;

        if (ModuleManager.flyMod.isEnabled())
            return false;

        for (double y = mc.thePlayer.posY; y > 0.0; --y) {
            final Block block = BlockUtils.getBlock(new BlockPos(mc.thePlayer.posX, y, mc.thePlayer.posZ));
            if (block.getMaterial() == Material.water) {
                return false;
            }

            if (block.getMaterial() != Material.air)
                return true;

            if (y < 0.0) {
                break;
            }
        }

        return true;
    }
}
