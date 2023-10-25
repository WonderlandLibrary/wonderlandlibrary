package club.cloverhook.utils.property.impl;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

import java.util.ArrayList;
import java.util.Arrays;

import club.cloverhook.Cloverhook;
import club.cloverhook.event.cloverhook.UpdateValueEvent;
import club.cloverhook.utils.property.abs.Property;

/**
 * @author antja03
 */
public class ItemsProperty extends Property<ArrayList<Item>> {

    private boolean onlyBlocks;

    public ItemsProperty(String id, String description, club.cloverhook.utils.Dependency dependency, boolean onlyBlocks, ArrayList<Item> selectedItems) {
        super(id, description, dependency);
        this.onlyBlocks = onlyBlocks;
        this.value = selectedItems;
    }

    @Override
    public void setValue(String input) {
        ArrayList<String> itemIds = new ArrayList<>(Arrays.asList(input.split(":")));
        ArrayList<Item> newValue = new ArrayList<>();
        
        for (Item item : Item.itemRegistry) {
            if (itemIds.contains(item.getUnlocalizedName())) {
            	newValue.add(item);
            }
        }
        
        Cloverhook.eventBus.publish(new UpdateValueEvent(this, getValue(), newValue));
        
        value.clear();
        value.addAll(newValue);
    }

    @Override
    public String getValueAsString() {
        String value = "";
        for (Item item : getValue()) {
            if (value == "") {
                value = item.getUnlocalizedName();
            } else {
                value += "," + item.getUnlocalizedName();
            }
        }
        return value;
    }

    public boolean containsItem(Item item) {
        if (onlyBlocks && !(item instanceof ItemBlock))
            return false;

        return value.contains(item);
    }

    public boolean containsItem(String name) {
        for (Item item : Item.itemRegistry) {
            if (onlyBlocks && !(item instanceof ItemBlock))
                continue;

            if (item.getUnlocalizedName().equalsIgnoreCase(name)) {
                return value.contains(item);
            }
        }
        return false;
    }

    public void addItem(Item item) {
        if (onlyBlocks && !(item instanceof ItemBlock))
            return;

        if (!value.contains(item)) {
            value.add(item);
        }
    }

    public void addItem(String unlocalizedName) {
        for (Item item : Item.itemRegistry) {
            if (onlyBlocks && !(item instanceof ItemBlock))
                continue;

            if (item.getUnlocalizedName().equalsIgnoreCase(unlocalizedName) && !value.contains(item)) {
                value.add(item);
            }
        }
    }

    public void removeItem(Item item) {
        if (onlyBlocks && !(item instanceof ItemBlock))
            return;

        if (value.contains(item)) {
            value.remove(item);
        }
    }

    public void removeItem(String unlocalizedName) {
        for (Item item : Item.itemRegistry) {
            if (onlyBlocks && !(item instanceof ItemBlock))
                continue;

            if (item.getUnlocalizedName().equalsIgnoreCase(unlocalizedName) && value.contains(item)) {
                value.remove(item);
            }
        }
    }


    public boolean onlyBlocks() {
        return onlyBlocks;
    }

}
