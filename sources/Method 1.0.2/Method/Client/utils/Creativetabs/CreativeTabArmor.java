package Method.Client.utils.Creativetabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;


public class CreativeTabArmor extends CreativeTabs {

    ArrayList<Enchantment> Enchants = new ArrayList<>();
    ArrayList<Integer> Levels = new ArrayList<>();

    ItemStack Blankspot = new ItemStack(Items.BRICK);

    public CreativeTabArmor() {
        super("Armor");
        this.tabLabel = "Armor";
    }

    @Override
    public String getTabLabel() {
        return "Armor";
    }

    @Override
    public String getTranslationKey() {
        return "Armor";
    }

    @Override
    public void displayAllRelevantItems(NonNullList<ItemStack> itemList) {

        Blankspot.setCount(-1);
        // 0 all 1 blast 2 fire 3 Proj 4 Prot
        MaxVanillaArmor(itemList, 0);
        MaxVanillaArmor(itemList, 1);
        MaxVanillaArmor(itemList, 2);
        MaxVanillaArmor(itemList, 3);
        MaxVanillaArmor(itemList, 4);

        super.displayAllRelevantItems(itemList);
    }

    private void MaxVanillaArmor(NonNullList<ItemStack> itemList, int Switch) {
        Enchants.add(Enchantments.RESPIRATION);
        Levels.add(3);
        Enchants.add(Enchantments.AQUA_AFFINITY);
        Levels.add(1);

        Enchantsetup(Switch);
        Creativetabhelper.Packsize(Items.DIAMOND_HELMET, Enchants, Levels, itemList);
        clearvar();
        itemList.add(Blankspot);

        Enchantsetup(Switch);
        Creativetabhelper.Packsize(Items.DIAMOND_CHESTPLATE, Enchants, Levels, itemList);
        clearvar();


        Enchantsetup(Switch);
        Creativetabhelper.Packsize(Items.DIAMOND_LEGGINGS, Enchants, Levels, itemList);
        clearvar();
        itemList.add(Blankspot);

        Enchants.add(Enchantments.DEPTH_STRIDER);
        Levels.add(3);
        Enchants.add(Enchantments.FEATHER_FALLING);
        Levels.add(4);
        Enchants.add(Enchantments.FROST_WALKER);
        Levels.add(2);
        Enchantsetup(Switch);
        Creativetabhelper.Packsize(Items.DIAMOND_BOOTS, Enchants, Levels, itemList);
        clearvar();

    }

    private void Enchantsetup(int Switch) {
        if (Switch == 0 || Switch == 1) {
            Enchants.add(Enchantments.BLAST_PROTECTION);
            Levels.add(4);
        }
        if (Switch == 0 || Switch == 2) {
            Enchants.add(Enchantments.FIRE_PROTECTION);
            Levels.add(4);
        }
        Enchants.add(Enchantments.MENDING);
        Levels.add(1);
        if (Switch == 0 || Switch == 3) {
            Enchants.add(Enchantments.PROJECTILE_PROTECTION);
            Levels.add(4);
        }
        if (Switch == 0 || Switch == 4) {
            Enchants.add(Enchantments.PROTECTION);
            Levels.add(4);
        }
        Enchants.add(Enchantments.UNBREAKING);
        Levels.add(3);
    }


    private void clearvar() {
        Enchants.clear();
        Levels.clear();
    }


    @Override
    public ItemStack createIcon() {
        return new ItemStack(Items.DIAMOND_CHESTPLATE);
    }
}
