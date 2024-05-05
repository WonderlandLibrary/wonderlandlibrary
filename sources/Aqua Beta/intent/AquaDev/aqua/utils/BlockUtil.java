package intent.AquaDev.aqua.utils;

import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemLeaves;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemSnow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;

public class BlockUtil {
    private final ArrayList<Item> nonValidItems = new ArrayList();

    public BlockUtil() {
        this.nonValidItems.add((Object)Item.getItemById((int)30));
        this.nonValidItems.add((Object)Item.getItemById((int)66));
        this.nonValidItems.add((Object)Item.getItemById((int)58));
        this.nonValidItems.add((Object)Item.getItemById((int)116));
        this.nonValidItems.add((Object)Item.getItemById((int)158));
        this.nonValidItems.add((Object)Item.getItemById((int)23));
        this.nonValidItems.add((Object)Item.getItemById((int)6));
        this.nonValidItems.add((Object)Item.getItemById((int)54));
        this.nonValidItems.add((Object)Item.getItemById((int)146));
        this.nonValidItems.add((Object)Item.getItemById((int)130));
        this.nonValidItems.add((Object)Item.getItemById((int)26));
        this.nonValidItems.add((Object)Item.getItemById((int)50));
        this.nonValidItems.add((Object)Item.getItemById((int)76));
        this.nonValidItems.add((Object)Item.getItemById((int)46));
        this.nonValidItems.add((Object)Item.getItemById((int)37));
        this.nonValidItems.add((Object)Item.getItemById((int)38));
        this.nonValidItems.add((Object)Item.getItemById((int)12));
        this.nonValidItems.add((Object)Item.getItemById((int)13));
        this.nonValidItems.add((Object)Item.getItemById((int)157));
        this.nonValidItems.add((Object)Item.getItemById((int)390));
        this.nonValidItems.add((Object)Item.getItemById((int)65));
        this.nonValidItems.add((Object)Item.getItemById((int)175));
        this.nonValidItems.add((Object)Item.getItemById((int)425));
    }

    public boolean isValidStack(ItemStack itemStack) {
        Item item = itemStack.getItem();
        if (item instanceof ItemSlab || item instanceof ItemLeaves || item instanceof ItemSnow || item instanceof ItemBanner || item instanceof ItemFlintAndSteel) {
            return false;
        }
        for (Item item1 : this.nonValidItems) {
            if (!item.equals((Object)item1)) continue;
            return false;
        }
        return true;
    }

    public static boolean isValidBock(BlockPos blockPos) {
        Block block = Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock();
        return !(block instanceof BlockLiquid) && !(block instanceof BlockAir) && !(block instanceof BlockChest) && !(block instanceof BlockFurnace);
    }

    public static boolean isAirBlock(BlockPos blockPos) {
        Block block = Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock();
        return block instanceof BlockAir;
    }
}
