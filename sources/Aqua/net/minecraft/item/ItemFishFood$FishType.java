package net.minecraft.item;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;

public static enum ItemFishFood.FishType {
    COD(0, "cod", 2, 0.1f, 5, 0.6f),
    SALMON(1, "salmon", 2, 0.1f, 6, 0.8f),
    CLOWNFISH(2, "clownfish", 1, 0.1f),
    PUFFERFISH(3, "pufferfish", 1, 0.1f);

    private static final Map<Integer, ItemFishFood.FishType> META_LOOKUP;
    private final int meta;
    private final String unlocalizedName;
    private final int uncookedHealAmount;
    private final float uncookedSaturationModifier;
    private final int cookedHealAmount;
    private final float cookedSaturationModifier;
    private boolean cookable = false;

    private ItemFishFood.FishType(int meta, String unlocalizedName, int uncookedHeal, float uncookedSaturation, int cookedHeal, float cookedSaturation) {
        this.meta = meta;
        this.unlocalizedName = unlocalizedName;
        this.uncookedHealAmount = uncookedHeal;
        this.uncookedSaturationModifier = uncookedSaturation;
        this.cookedHealAmount = cookedHeal;
        this.cookedSaturationModifier = cookedSaturation;
        this.cookable = true;
    }

    private ItemFishFood.FishType(int meta, String unlocalizedName, int uncookedHeal, float uncookedSaturation) {
        this.meta = meta;
        this.unlocalizedName = unlocalizedName;
        this.uncookedHealAmount = uncookedHeal;
        this.uncookedSaturationModifier = uncookedSaturation;
        this.cookedHealAmount = 0;
        this.cookedSaturationModifier = 0.0f;
        this.cookable = false;
    }

    public int getMetadata() {
        return this.meta;
    }

    public String getUnlocalizedName() {
        return this.unlocalizedName;
    }

    public int getUncookedHealAmount() {
        return this.uncookedHealAmount;
    }

    public float getUncookedSaturationModifier() {
        return this.uncookedSaturationModifier;
    }

    public int getCookedHealAmount() {
        return this.cookedHealAmount;
    }

    public float getCookedSaturationModifier() {
        return this.cookedSaturationModifier;
    }

    public boolean canCook() {
        return this.cookable;
    }

    public static ItemFishFood.FishType byMetadata(int meta) {
        ItemFishFood.FishType itemfishfood$fishtype = (ItemFishFood.FishType)((Object)META_LOOKUP.get((Object)meta));
        return itemfishfood$fishtype == null ? COD : itemfishfood$fishtype;
    }

    public static ItemFishFood.FishType byItemStack(ItemStack stack) {
        return stack.getItem() instanceof ItemFishFood ? ItemFishFood.FishType.byMetadata(stack.getMetadata()) : COD;
    }

    static {
        META_LOOKUP = Maps.newHashMap();
        for (ItemFishFood.FishType itemfishfood$fishtype : ItemFishFood.FishType.values()) {
            META_LOOKUP.put((Object)itemfishfood$fishtype.getMetadata(), (Object)itemfishfood$fishtype);
        }
    }
}
