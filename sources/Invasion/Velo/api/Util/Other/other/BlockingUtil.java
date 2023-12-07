package Velo.api.Util.Other.other;

import Velo.api.Util.Other.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings.GameType;

public class BlockingUtil {
	
	static Minecraft mc = Minecraft.getMinecraft();
	
	public static boolean sendUseHypixelBlockingPacket(EntityPlayer playerIn, World worldIn, ItemStack itemStackIn) {
		if (mc.playerController.currentGameType == GameType.SPECTATOR)
        {
            return false;
        }
        else
        {
            
            if (itemStackIn == null) {
                return false;
            }
            
            mc.playerController.syncCurrentPlayItem();
            int i = itemStackIn.stackSize;
            
            ItemStack itemstack = itemStackIn.useItemRightClick(worldIn, playerIn);

            if (itemstack != itemStackIn || itemstack != null && itemstack.stackSize != i)
            {
                playerIn.inventory.mainInventory[playerIn.inventory.currentItem] = itemstack;

                if (itemstack.stackSize == 0)
                {
                    playerIn.inventory.mainInventory[playerIn.inventory.currentItem] = null;
                }

                return true;
            }
            else
            {
                return false;
            }
        }
	}
	
	public static int airSlot() {
		for (int j = 0; j < 8; ++j) {
			if (Minecraft.getMinecraft().thePlayer.inventory.mainInventory[j] == null) {
				return j;
			}
		}
		// ChatUtil.printChat("Clear a hotbar slot.");
		return -10;
	}
}
