package me.aquavit.liquidsense.script.api;

import jdk.nashorn.api.scripting.ScriptUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import jdk.nashorn.api.scripting.JSObject;

import java.util.List;

@SuppressWarnings({"unchecked", "unused"})
public class ScriptTab extends CreativeTabs {
    private final JSObject tabObject;
    private final ItemStack[] items;

    public ScriptTab(JSObject tabObject) {
        super(tabObject.getMember("name").toString());
        this.tabObject = tabObject;
        this.items = (ItemStack[]) ScriptUtils.convert(tabObject.getMember("items"), ItemStack[].class);
    }

    @Override
    public Item getTabIconItem() {
        try {
            return (Item) Items.class.getField(tabObject.getMember("icon").toString()).get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getTranslatedTabLabel() {
        return tabObject.getMember("name").toString();
    }

    @Override
    public void displayAllReleventItems(List<ItemStack> list) {
        for (ItemStack item : items) {
            if (item != null) {
                list.add(item);
            }
        }
    }
}
