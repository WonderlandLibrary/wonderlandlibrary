/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.events.EventTick;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.utils.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class SpeedyGonzales extends Module
{
    public static boolean isMining;
    
    public SpeedyGonzales() {
        super("Speedy Gonzales", 50, Category.PLAYER, 15120384, true, "Increase mining speed", new String[] { "sg", "speedmine", "SpeedyGonzales", "speedyg", "sgonzales", "gonzales", "sm", "speedm" });
    }
    
    @EventTarget
    public void onTick(final EventTick event) {
        final ItemStack heldItem = Wrapper.mc.thePlayer.getHeldItem();
        BlockPos var1 = null;
        SpeedyGonzales.isMining = false;
        float mineSpeed = 0.0f;
        if (Wrapper.mc.objectMouseOver != null) {
            final MovingObjectPosition objectMouseOver = Wrapper.mc.objectMouseOver;
            if (MovingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && Wrapper.mc.objectMouseOver.func_178782_a() != null) {
                var1 = Wrapper.mc.objectMouseOver.func_178782_a();
            }
        }
        if (Wrapper.mc.playerController.isHittingBlock) {
            SpeedyGonzales.isMining = true;
        }
        final Float getBlock = Wrapper.getBlockAtPos(var1).getBlockHardness(Wrapper.mc.theWorld, var1);
        final float blockDMG = heldItem.getItem().getStrVsBlock(heldItem, Wrapper.getBlockAtPos(var1));
        if (Wrapper.getBlockAtPos(var1).getBlockHardness(Wrapper.mc.theWorld, var1) > 4.0f) {
            mineSpeed = 0.005f;
        }
        else if (getBlock >= 3.0f) {
            mineSpeed = 0.2f;
            if (blockDMG <= 2.0f) {
                mineSpeed = 0.0f;
            }
            else if (blockDMG == 4.0f) {
                mineSpeed = 0.02f;
            }
            else if (blockDMG == 6.0f) {
                mineSpeed = 0.05f;
            }
        }
        else if (getBlock >= 1.5) {
            mineSpeed = 0.1f;
            if (blockDMG <= 2.0f) {
                mineSpeed = 0.0f;
            }
            else if (blockDMG == 4.0f) {
                mineSpeed = 0.02f;
            }
            else if (blockDMG == 6.0f) {
                mineSpeed = 0.05f;
            }
        }
        else if (getBlock >= 1.0f) {
            mineSpeed = 0.5f;
            if (blockDMG <= 2.0f) {
                mineSpeed = 0.0f;
            }
            else if (blockDMG == 4.0f) {
                mineSpeed = 0.4f;
            }
        }
        final PlayerControllerMP playerController2;
        final PlayerControllerMP playerController = playerController2 = Wrapper.mc.playerController;
        playerController2.curBlockDamageMP += mineSpeed;
    }
    
    @Override
    public void toggleModule() {
        super.toggleModule();
        if (!this.getState()) {
            Wrapper.mc.playerController.curBlockDamageMP = 0.0f;
        }
    }
}
