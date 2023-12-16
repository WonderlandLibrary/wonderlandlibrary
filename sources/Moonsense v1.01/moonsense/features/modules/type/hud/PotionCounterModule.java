// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.hud;

import moonsense.config.utils.AnchorPoint;
import moonsense.enums.ModuleCategory;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import moonsense.features.SCModule;
import moonsense.settings.Setting;
import moonsense.features.SCDefaultRenderModule;

public class PotionCounterModule extends SCDefaultRenderModule
{
    private final Setting item;
    
    public PotionCounterModule() {
        super("Potion Counter", "Displays the amount of potions or soups you have in your inventory on the HUD.");
        new Setting(this, "Potion Options");
        this.item = new Setting(this, "Item").setDefault(0).setRange("Potion", "Soup");
    }
    
    @Override
    public Object getValue() {
        final String itemType = this.item.getValue().get(this.item.getInt() + 1);
        if (itemType.equalsIgnoreCase("potion")) {
            return String.valueOf(this.getPotionCount()) + " Pots";
        }
        return String.valueOf(this.getSoupCount()) + " Soups";
    }
    
    private int getPotionCount() {
        int potionCount = 0;
        ItemStack[] mainInventory;
        for (int length = (mainInventory = this.mc.thePlayer.inventory.mainInventory).length, i = 0; i < length; ++i) {
            final ItemStack is = mainInventory[i];
            if (is != null) {
                if (is.getItem().getUnlocalizedName().contains("potion") || is.getItem().getUnlocalizedName().contains("Potion")) {
                    ++potionCount;
                }
            }
        }
        return potionCount;
    }
    
    private int getSoupCount() {
        int soupCount = 0;
        ItemStack[] mainInventory;
        for (int length = (mainInventory = this.mc.thePlayer.inventory.mainInventory).length, i = 0; i < length; ++i) {
            final ItemStack is = mainInventory[i];
            if (is != null) {
                if (is.getItem() instanceof ItemSoup) {
                    ++soupCount;
                }
            }
        }
        return soupCount;
    }
    
    @Override
    public Object getDummyValue() {
        final String itemType = this.item.getValue().get(this.item.getInt() + 1);
        if (itemType.equalsIgnoreCase("potion")) {
            return "3 Pots";
        }
        return "3 Soups";
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.HUD;
    }
    
    @Override
    public AnchorPoint getDefaultPosition() {
        return AnchorPoint.CENTER_RIGHT;
    }
}
