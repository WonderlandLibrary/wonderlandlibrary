package Method.Client.utils.Creativetabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.util.NonNullList;


public class CreativeTabBlocks extends CreativeTabs {


    public CreativeTabBlocks() {
        super("Block");
        this.tabLabel = "Block";
    }

    @Override
    public String getTabLabel() {
        return "Block";
    }

    @Override
     public String getTranslationKey() {
        return "Block";
    }

    @Override
    public void displayAllRelevantItems(NonNullList<ItemStack> itemList) {
        itemList.add(new ItemStack(Items.COMMAND_BLOCK_MINECART));
        itemList.add(new ItemStack(Items.WRITTEN_BOOK));
        itemList.add(new ItemStack(Items.FILLED_MAP));
        itemList.add(new ItemStack(Blocks.COMMAND_BLOCK));
        itemList.add(new ItemStack(Blocks.CHAIN_COMMAND_BLOCK));
        itemList.add(new ItemStack(Blocks.REPEATING_COMMAND_BLOCK));
        itemList.add(new ItemStack(Blocks.STRUCTURE_BLOCK));
        itemList.add(new ItemStack(Blocks.STRUCTURE_VOID));
        itemList.add(new ItemStack(Blocks.DRAGON_EGG));
        itemList.add(new ItemStack(Blocks.BARRIER));
        itemList.add(new ItemStack(Blocks.GRASS_PATH));
        itemList.add(new ItemStack(Blocks.BROWN_MUSHROOM_BLOCK));
        itemList.add(new ItemStack(Blocks.RED_MUSHROOM_BLOCK));
        itemList.add(new ItemStack(Blocks.FARMLAND));
        itemList.add(new ItemStack(Blocks.MOB_SPAWNER));
        itemList.add(new ItemStack(Blocks.TALLGRASS));
        ItemStack Furnace = new ItemStack(Blocks.FURNACE);
        try {
            Furnace.setTagCompound(JsonToNBT.getTagFromJson("{BlockStateTag:{lit:\"true\"}}"));
        } catch (NBTException e) {
            e.printStackTrace();
        }

        itemList.add(Furnace);
        ItemStack Water = new ItemStack(Items.WATER_BUCKET);
        Water.setCount(64);
        itemList.add(Water);
        ItemStack Lava = new ItemStack(Items.LAVA_BUCKET);
        Lava.setCount(64);
        itemList.add(Lava);
        ItemStack Bucket = new ItemStack(Items.BUCKET);
        Bucket.setCount(64);
        itemList.add(Bucket);
        ItemStack Epearl = new ItemStack(Items.ENDER_PEARL);
        Epearl.setCount(64);
        itemList.add(Epearl);
        ItemStack egg = new ItemStack(Items.EGG);
        egg.setCount(64);
        itemList.add(egg);
        ItemStack Sign = new ItemStack(Items.SIGN);
        Sign.setCount(64);
        itemList.add(Sign);
        ItemStack Banner = new ItemStack(Items.BANNER);
        Banner.setCount(64);
        itemList.add(Banner);
        ItemStack Snowball = new ItemStack(Items.SNOWBALL);
        Snowball.setCount(64);
        itemList.add(Snowball);
        ItemStack Bed = new ItemStack(Items.BED);
        Bed.setCount(64);
        itemList.add(Bed);
        ItemStack Boat = new ItemStack(Items.BOAT);
        Boat.setCount(64);
        itemList.add(Boat);
        ItemStack Cake = new ItemStack(Items.CAKE);
        Cake.setCount(64);
        itemList.add(Cake);
        ItemStack Totm = new ItemStack(Items.TOTEM_OF_UNDYING);
        Totm.setCount(64);
        itemList.add(Totm);
        ItemStack Shul = new ItemStack(Item.getItemById(229));
        Shul.setCount(64);
        itemList.add(Shul);
        ItemStack Mush = new ItemStack(Items.MUSHROOM_STEW);
        Mush.setCount(64);
        itemList.add(Mush);
        ItemStack Saddle = new ItemStack(Items.SADDLE);
        Saddle.setCount(64);
        itemList.add(Saddle);
        ItemStack Tntmc = new ItemStack(Items.TNT_MINECART);
        Tntmc.setCount(64);
        itemList.add(Tntmc);
        ItemStack Minecart = new ItemStack(Items.MINECART);
        Minecart.setCount(64);
        itemList.add(Minecart);

    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(Items.GLASS_BOTTLE);
    }
}
