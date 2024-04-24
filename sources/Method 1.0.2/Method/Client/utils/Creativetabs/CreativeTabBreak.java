package Method.Client.utils.Creativetabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;


public class CreativeTabBreak extends CreativeTabs {


    ItemStack Blankspot = new ItemStack(Items.BRICK);

    public CreativeTabBreak() {
        super("Break");
        this.tabLabel = "Break";
    }
    @Override
    public String getTabLabel() {
        return "Break";
    }

    @Override
    public String getTranslationKey() {
        return "Break";
    }
    @Override
    public void displayAllRelevantItems(NonNullList<ItemStack> itemList) {
        ArrayList<Enchantment> AllEnchant = new ArrayList<>();
        ArrayList<Integer> AllLevel32k = new ArrayList<>();
        int Simple30 = 0;
        for (Enchantment e : Enchantment.REGISTRY) {
            if (Simple30 <= 30) {
                AllEnchant.add(e);
                AllLevel32k.add(32767);
            }
            Simple30++;
        }

        Blankspot.setCount(-1);

        Creativetabhelper.Attributeitems(Items.DIAMOND_SWORD, AllEnchant, AllLevel32k, itemList, EntityEquipmentSlot.MAINHAND, true);

        Creativetabhelper.Attributeitems(Items.DIAMOND_PICKAXE, AllEnchant, AllLevel32k, itemList, EntityEquipmentSlot.MAINHAND, true);

        Creativetabhelper.Attributeitems(Items.DIAMOND_AXE, AllEnchant, AllLevel32k, itemList, EntityEquipmentSlot.MAINHAND, true);

        Creativetabhelper.Attributeitems(Items.DIAMOND_SHOVEL, AllEnchant, AllLevel32k, itemList, EntityEquipmentSlot.MAINHAND, true);

        Creativetabhelper.Attributeitems(Items.DIAMOND_HOE, AllEnchant, AllLevel32k, itemList, EntityEquipmentSlot.MAINHAND, true);

        Creativetabhelper.Attributeitems(Items.SHEARS, AllEnchant, AllLevel32k, itemList, EntityEquipmentSlot.MAINHAND, true);

        Creativetabhelper.Attributeitems(Items.BOW, AllEnchant, AllLevel32k, itemList, EntityEquipmentSlot.MAINHAND, true);

        Creativetabhelper.Attributeitems(Items.ELYTRA, AllEnchant, AllLevel32k, itemList, EntityEquipmentSlot.CHEST, true);

        Creativetabhelper.Attributeitems(Items.FISHING_ROD, AllEnchant, AllLevel32k, itemList, EntityEquipmentSlot.MAINHAND, true);

        Creativetabhelper.Attributeitems(Items.DIAMOND_HELMET, AllEnchant, AllLevel32k, itemList, EntityEquipmentSlot.CHEST, true);

        Creativetabhelper.Attributeitems(Items.DIAMOND_CHESTPLATE, AllEnchant, AllLevel32k, itemList, EntityEquipmentSlot.CHEST, true);

        Creativetabhelper.Attributeitems(Items.DIAMOND_LEGGINGS, AllEnchant, AllLevel32k, itemList, EntityEquipmentSlot.CHEST, true);

        Creativetabhelper.Attributeitems(Items.DIAMOND_BOOTS, AllEnchant, AllLevel32k, itemList, EntityEquipmentSlot.CHEST, true);
        itemList.add(Blankspot);
        itemList.add(Blankspot);
        itemList.add(Blankspot);
        itemList.add(Blankspot);
        itemList.add(Blankspot);
        Creativetabhelper.Unbreakpack(Items.DIAMOND_SWORD, AllEnchant, AllLevel32k, itemList);

        Creativetabhelper.Unbreakpack(Items.DIAMOND_PICKAXE, AllEnchant, AllLevel32k, itemList);

        Creativetabhelper.Unbreakpack(Items.DIAMOND_AXE, AllEnchant, AllLevel32k, itemList);

        Creativetabhelper.Unbreakpack(Items.DIAMOND_SHOVEL, AllEnchant, AllLevel32k, itemList);

        Creativetabhelper.Unbreakpack(Items.DIAMOND_HOE, AllEnchant, AllLevel32k, itemList);

        Creativetabhelper.Unbreakpack(Items.SHEARS, AllEnchant, AllLevel32k, itemList);

        Creativetabhelper.Unbreakpack(Items.BOW, AllEnchant, AllLevel32k, itemList);

        Creativetabhelper.Unbreakpack(Items.ELYTRA, AllEnchant, AllLevel32k, itemList);

        Creativetabhelper.Unbreakpack(Items.FISHING_ROD, AllEnchant, AllLevel32k, itemList);

        Creativetabhelper.Unbreakpack(Items.DIAMOND_HELMET, AllEnchant, AllLevel32k, itemList);

        Creativetabhelper.Unbreakpack(Items.DIAMOND_CHESTPLATE, AllEnchant, AllLevel32k, itemList);

        Creativetabhelper.Unbreakpack(Items.DIAMOND_LEGGINGS, AllEnchant, AllLevel32k, itemList);

        Creativetabhelper.Unbreakpack(Items.DIAMOND_BOOTS, AllEnchant, AllLevel32k, itemList);
        itemList.add(Blankspot);
        itemList.add(Blankspot);
        itemList.add(Blankspot);
        itemList.add(Blankspot);
        itemList.add(Blankspot);
        Creativetabhelper.Attributeitems(Items.DIAMOND_SWORD, AllEnchant, AllLevel32k, itemList, EntityEquipmentSlot.MAINHAND, false);

        Creativetabhelper.Attributeitems(Items.DIAMOND_PICKAXE, AllEnchant, AllLevel32k, itemList, EntityEquipmentSlot.MAINHAND, false);

        Creativetabhelper.Attributeitems(Items.DIAMOND_AXE, AllEnchant, AllLevel32k, itemList, EntityEquipmentSlot.MAINHAND, false);

        Creativetabhelper.Attributeitems(Items.DIAMOND_SHOVEL, AllEnchant, AllLevel32k, itemList, EntityEquipmentSlot.MAINHAND, false);

        Creativetabhelper.Attributeitems(Items.DIAMOND_HOE, AllEnchant, AllLevel32k, itemList, EntityEquipmentSlot.MAINHAND, false);

        Creativetabhelper.Attributeitems(Items.SHEARS, AllEnchant, AllLevel32k, itemList, EntityEquipmentSlot.MAINHAND, false);

        Creativetabhelper.Attributeitems(Items.BOW, AllEnchant, AllLevel32k, itemList, EntityEquipmentSlot.MAINHAND, false);

        Creativetabhelper.Attributeitems(Items.ELYTRA, AllEnchant, AllLevel32k, itemList, EntityEquipmentSlot.CHEST, false);

        Creativetabhelper.Attributeitems(Items.FISHING_ROD, AllEnchant, AllLevel32k, itemList, EntityEquipmentSlot.MAINHAND, false);

        Creativetabhelper.Attributeitems(Items.DIAMOND_HELMET, AllEnchant, AllLevel32k, itemList, EntityEquipmentSlot.CHEST, false);

        Creativetabhelper.Attributeitems(Items.DIAMOND_CHESTPLATE, AllEnchant, AllLevel32k, itemList, EntityEquipmentSlot.CHEST, false);

        Creativetabhelper.Attributeitems(Items.DIAMOND_LEGGINGS, AllEnchant, AllLevel32k, itemList, EntityEquipmentSlot.CHEST, false);

        Creativetabhelper.Attributeitems(Items.DIAMOND_BOOTS, AllEnchant, AllLevel32k, itemList, EntityEquipmentSlot.CHEST, false);
        itemList.add(Blankspot);
        itemList.add(Blankspot);
        itemList.add(Blankspot);
        itemList.add(Blankspot);
        itemList.add(Blankspot);
        super.displayAllRelevantItems(itemList);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(Items.ARROW);
    }
}
