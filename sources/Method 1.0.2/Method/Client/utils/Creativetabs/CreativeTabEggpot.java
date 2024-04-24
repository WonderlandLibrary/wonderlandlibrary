package Method.Client.utils.Creativetabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

import java.util.ArrayList;


public class CreativeTabEggpot extends CreativeTabs {

    ArrayList<Enchantment> Enchants = new ArrayList<>();
    ArrayList<Integer> Levels = new ArrayList<>();

    ItemStack Blankspot = new ItemStack(Items.BRICK);

    public CreativeTabEggpot() {
        super("Items");
    }


    @Override
    public void displayAllRelevantItems(NonNullList<ItemStack> itemList) {

        Blankspot.setCount(-1);

        try {

            for (ResourceLocation dan : LootTableList.getAll()) {
                ItemStack Entitydrop = new ItemStack(Items.SPAWN_EGG);
                Entitydrop.setTagCompound(JsonToNBT.getTagFromJson("{EntityTag:{DeathLootTable:\"" + dan.getNamespace() + "\",id:\"minecraft:bat\",ActiveEffects:[{Duration:2147483647,Id:7,Amplifier:0}]}}"));
                itemList.add(Entitydrop);
            }

        } catch (NBTException e) {
            e.printStackTrace();
        }


        super.displayAllRelevantItems(itemList);
    }


    private void clearvar() {
        Enchants.clear();
        Levels.clear();
    }


    @Override
    public ItemStack createIcon() {
        return new ItemStack(Items.EGG);
    }
}
