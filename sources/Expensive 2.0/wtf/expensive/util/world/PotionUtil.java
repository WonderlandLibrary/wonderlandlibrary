package wtf.expensive.util.world;

import net.minecraft.block.AirBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.play.client.CPlayerTryUseItemPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import wtf.expensive.util.IMinecraft;

/**
 * @author dedinside
 * @since 07.06.2023
 */
public class PotionUtil implements IMinecraft {
    public static boolean isChangingItem;
    private boolean isItemChangeRequested;
    private int previousSlot = -1;


    public static boolean isBlockAirBelowPlayer(float distance) {
        if (Minecraft.getInstance().player == null) {
            return false;
        }
        BlockPos blockPos = new BlockPos(
                mc.player.getPosX(),
                mc.player.getPosY() - (double) distance,
                mc.player.getPosZ()
        );
        return mc.world.getBlockState(blockPos).getBlock() instanceof AirBlock;
    }

    public void changeItemSlot(boolean resetAfter) {
        if (this.isItemChangeRequested && this.previousSlot != -1) {
            isChangingItem = true;
            mc.player.inventory.currentItem = this.previousSlot;
            if (resetAfter) {
                this.isItemChangeRequested = false;
                this.previousSlot = -1;
                isChangingItem = false;
            }
        }
    }

    public void setPreviousSlot(int slot) {
        this.previousSlot = slot;
    }


    public static void useItem(Hand hand) {
        mc.player.connection.sendPacket(new CPlayerTryUseItemPacket(hand));
        mc.gameRenderer.itemRenderer.resetEquippedProgress(hand);
    }

    public static int calculateHorizontalDistance(long x1, long z1) {
        float deltaX = (float) (mc.player.getPosX() - (double) x1);
        float deltaZ = (float) (mc.player.getPosZ() - (double) z1);
        return (int) MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ);
    }

    public static int calculateDistance(long x1, long y1, long z1) {
        float deltaX = (float) (mc.player.getPosX() - (double) x1);
        float deltaY = (float) (mc.player.getPosY() - (double) y1);
        float deltaZ = (float) (mc.player.getPosZ() - (double) z1);
        return (int) MathHelper.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
    }

    public static double calculateDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double deltaX = x1 - x2;
        double deltaY = y1 - y2;
        double deltaZ = z1 - z2;
        return MathHelper.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
    }
}