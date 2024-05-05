package slavikcodd3r.rainbow.utils;

import net.minecraft.init.Items;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemEmptyMap;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemBow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.Minecraft;

public class MurderMysteryUtils
{
    public Minecraft mc;
    
    public MurderMysteryUtils() {
        super();
        this.mc = Minecraft.getMinecraft();
    }
    
    public static boolean isMurderer(final EntityPlayer player) {
        for (int i = 0; i < 9; ++i) {
            if (player.inventory.getStackInSlot(i) != null && (player.inventory.getStackInSlot(i).getItem() instanceof ItemSword) && !(player.inventory.getStackInSlot(i).getItem() instanceof ItemBow) && !(player.inventory.getStackInSlot(i).getItem() instanceof ItemMap) && !(player.inventory.getStackInSlot(i).getItem() instanceof ItemPotion) && !(player.inventory.getStackInSlot(i).getItem() instanceof ItemEmptyMap) && !(player.inventory.getStackInSlot(i).getItem() instanceof ItemSnowball) && player.inventory.getStackInSlot(i).getItem() != Items.gold_ingot && player.inventory.getStackInSlot(i).getItem() != Items.compass && player.inventory.getStackInSlot(i).getItem() != Items.arrow) {
                return true;
            }
        }
        return false;
    }
}
