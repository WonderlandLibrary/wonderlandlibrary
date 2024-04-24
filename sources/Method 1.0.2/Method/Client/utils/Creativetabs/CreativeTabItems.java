package Method.Client.utils.Creativetabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;


public class CreativeTabItems extends CreativeTabs {

    ArrayList<Enchantment> Enchants = new ArrayList<>();
    ArrayList<Integer> Levels = new ArrayList<>();

    ItemStack Blankspot = new ItemStack(Items.BRICK);

    public CreativeTabItems() {
        super("Items");
        this.tabLabel = "Items";
    }
    @Override
    public String getTabLabel() {
        return "Items";
    }
    @Override
    public String getTranslationKey() {
        return "Items";
    }
    @Override
    public void displayAllRelevantItems(NonNullList<ItemStack> itemList) {

        Blankspot.setCount(-1);

        Enchants.add(Enchantments.SHARPNESS);
        Levels.add(32767);
        Creativetabhelper.Packsize(Items.DIAMOND_SWORD, Enchants, Levels, itemList);
        clearvar();

        itemList.add(Blankspot);

        Enchants.add(Enchantments.SHARPNESS);
        Levels.add(5);
        Enchants.add(Enchantments.KNOCKBACK);
        Levels.add(2);
        Enchants.add(Enchantments.FIRE_ASPECT);
        Levels.add(2);
        Enchants.add(Enchantments.LOOTING);
        Levels.add(3);
        Enchants.add(Enchantments.SWEEPING);
        Levels.add(3);
        Enchants.add(Enchantments.SMITE);
        Levels.add(5);
        Enchants.add(Enchantments.BANE_OF_ARTHROPODS);
        Levels.add(5);
        Mendundbr();
        Creativetabhelper.Packsize(Items.DIAMOND_SWORD, Enchants, Levels, itemList);
        clearvar();


        PicRepeat();
        Enchants.add(Enchantments.SILK_TOUCH);
        Levels.add(1);
        Creativetabhelper.Packsize(Items.DIAMOND_PICKAXE, Enchants, Levels, itemList);
        clearvar();

        PicRepeat();
        itemList.add(Blankspot);
        Creativetabhelper.Packsize(Items.DIAMOND_PICKAXE, Enchants, Levels, itemList);
        clearvar();

        Enchants.add(Enchantments.BANE_OF_ARTHROPODS);
        Levels.add(5);
        Enchants.add(Enchantments.EFFICIENCY);
        Levels.add(5);
        Enchants.add(Enchantments.FORTUNE);
        Levels.add(3);
        Enchants.add(Enchantments.SHARPNESS);
        Levels.add(5);
        Enchants.add(Enchantments.SMITE);
        Levels.add(5);
        Mendundbr();
        Creativetabhelper.Packsize(Items.DIAMOND_AXE, Enchants, Levels, itemList);
        clearvar();

        PicRepeat();
        itemList.add(Blankspot);
        Creativetabhelper.Packsize(Items.DIAMOND_SHOVEL, Enchants, Levels, itemList);
        clearvar();


        Mendundbr();
        Creativetabhelper.Packsize(Items.DIAMOND_HOE, Enchants, Levels, itemList);
        clearvar();
        itemList.add(Blankspot);

        Enchants.add(Enchantments.EFFICIENCY);
        Levels.add(5);
        Mendundbr();
        Creativetabhelper.Packsize(Items.SHEARS, Enchants, Levels, itemList);
        clearvar();

        Enchants.add(Enchantments.PUNCH);
        Levels.add(2);
        Enchants.add(Enchantments.POWER);
        Levels.add(5);
        Bowrepeat(itemList);
        itemList.add(Blankspot);

        Enchants.add(Enchantments.PUNCH);
        Levels.add(32767);
        Enchants.add(Enchantments.POWER);
        Levels.add(32767);
        Bowrepeat(itemList);

        Enchants.add(Enchantments.MENDING);
        Levels.add(1);
        Enchants.add(Enchantments.UNBREAKING);
        Levels.add(3);
        Creativetabhelper.Packsize(Items.ELYTRA, Enchants, Levels, itemList);
        clearvar();
        itemList.add(Blankspot);

        Enchants.add(Enchantments.MENDING);
        Levels.add(32767);
        Enchants.add(Enchantments.UNBREAKING);
        Levels.add(32767);
        Creativetabhelper.Packsize(Items.ELYTRA, Enchants, Levels, itemList);
        clearvar();

        Enchants.add(Enchantments.LUCK_OF_THE_SEA);
        Levels.add(3);
        Enchants.add(Enchantments.LURE);
        Levels.add(3);
        Mendundbr();
        Creativetabhelper.Packsize(Items.FISHING_ROD, Enchants, Levels, itemList);
        clearvar();
        itemList.add(Blankspot);

        Enchants.add(Enchantments.LUCK_OF_THE_SEA);
        Levels.add(32767);
        Enchants.add(Enchantments.LURE);
        Levels.add(32767);
        Mendundbr();
        Creativetabhelper.Packsize(Items.FISHING_ROD, Enchants, Levels, itemList);
        clearvar();


        super.displayAllRelevantItems(itemList);
    }

    private void PicRepeat() {
        Enchants.add(Enchantments.EFFICIENCY);
        Levels.add(5);
        Enchants.add(Enchantments.FORTUNE);
        Levels.add(3);
        Enchants.add(Enchantments.MENDING);
        Levels.add(1);
        Enchants.add(Enchantments.UNBREAKING);
        Levels.add(3);
    }


    private void Mendundbr() {
        Enchants.add(Enchantments.MENDING);
        Levels.add(1);
        Enchants.add(Enchantments.UNBREAKING);
        Levels.add(3);
    }

    private void Bowrepeat(NonNullList<ItemStack> itemList) {
        Enchants.add(Enchantments.FLAME);
        Levels.add(1);
        Enchants.add(Enchantments.INFINITY);
        Levels.add(1);
        Enchants.add(Enchantments.MENDING);
        Levels.add(1);
        Enchants.add(Enchantments.UNBREAKING);
        Levels.add(3);
        Creativetabhelper.Packsize(Items.BOW, Enchants, Levels, itemList);
        clearvar();
    }


    private void clearvar() {
        Enchants.clear();
        Levels.clear();
    }


    @Override
    public ItemStack createIcon() {
        return new ItemStack(Items.DIAMOND_AXE);
    }
}
