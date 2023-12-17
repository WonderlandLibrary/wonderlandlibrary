// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.advancements.critereon;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonElement;
import net.minecraft.enchantment.Enchantment;
import java.util.Map;
import net.minecraft.potion.PotionUtils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import javax.annotation.Nullable;
import net.minecraft.potion.PotionType;
import net.minecraft.item.Item;

public class ItemPredicate
{
    public static final ItemPredicate ANY;
    private final Item item;
    private final Integer data;
    private final MinMaxBounds count;
    private final MinMaxBounds durability;
    private final EnchantmentPredicate[] enchantments;
    private final PotionType potion;
    private final NBTPredicate nbt;
    
    public ItemPredicate() {
        this.item = null;
        this.data = null;
        this.potion = null;
        this.count = MinMaxBounds.UNBOUNDED;
        this.durability = MinMaxBounds.UNBOUNDED;
        this.enchantments = new EnchantmentPredicate[0];
        this.nbt = NBTPredicate.ANY;
    }
    
    public ItemPredicate(@Nullable final Item item, @Nullable final Integer data, final MinMaxBounds count, final MinMaxBounds durability, final EnchantmentPredicate[] enchantments, @Nullable final PotionType potion, final NBTPredicate nbt) {
        this.item = item;
        this.data = data;
        this.count = count;
        this.durability = durability;
        this.enchantments = enchantments;
        this.potion = potion;
        this.nbt = nbt;
    }
    
    public boolean test(final ItemStack item) {
        if (this.item != null && item.getItem() != this.item) {
            return false;
        }
        if (this.data != null && item.getMetadata() != this.data) {
            return false;
        }
        if (!this.count.test((float)item.getCount())) {
            return false;
        }
        if (this.durability != MinMaxBounds.UNBOUNDED && !item.isItemStackDamageable()) {
            return false;
        }
        if (!this.durability.test((float)(item.getMaxDamage() - item.getItemDamage()))) {
            return false;
        }
        if (!this.nbt.test(item)) {
            return false;
        }
        final Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(item);
        for (int i = 0; i < this.enchantments.length; ++i) {
            if (!this.enchantments[i].test(map)) {
                return false;
            }
        }
        final PotionType potiontype = PotionUtils.getPotionFromItem(item);
        return this.potion == null || this.potion == potiontype;
    }
    
    public static ItemPredicate deserialize(@Nullable final JsonElement element) {
        if (element != null && !element.isJsonNull()) {
            final JsonObject jsonobject = JsonUtils.getJsonObject(element, "item");
            final MinMaxBounds minmaxbounds = MinMaxBounds.deserialize(jsonobject.get("count"));
            final MinMaxBounds minmaxbounds2 = MinMaxBounds.deserialize(jsonobject.get("durability"));
            final Integer integer = jsonobject.has("data") ? Integer.valueOf(JsonUtils.getInt(jsonobject, "data")) : null;
            final NBTPredicate nbtpredicate = NBTPredicate.deserialize(jsonobject.get("nbt"));
            Item item = null;
            if (jsonobject.has("item")) {
                final ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(jsonobject, "item"));
                item = Item.REGISTRY.getObject(resourcelocation);
                if (item == null) {
                    throw new JsonSyntaxException("Unknown item id '" + resourcelocation + "'");
                }
            }
            final EnchantmentPredicate[] aenchantmentpredicate = EnchantmentPredicate.deserializeArray(jsonobject.get("enchantments"));
            PotionType potiontype = null;
            if (jsonobject.has("potion")) {
                final ResourceLocation resourcelocation2 = new ResourceLocation(JsonUtils.getString(jsonobject, "potion"));
                if (!PotionType.REGISTRY.containsKey(resourcelocation2)) {
                    throw new JsonSyntaxException("Unknown potion '" + resourcelocation2 + "'");
                }
                potiontype = PotionType.REGISTRY.getObject(resourcelocation2);
            }
            return new ItemPredicate(item, integer, minmaxbounds, minmaxbounds2, aenchantmentpredicate, potiontype, nbtpredicate);
        }
        return ItemPredicate.ANY;
    }
    
    public static ItemPredicate[] deserializeArray(@Nullable final JsonElement element) {
        if (element != null && !element.isJsonNull()) {
            final JsonArray jsonarray = JsonUtils.getJsonArray(element, "items");
            final ItemPredicate[] aitempredicate = new ItemPredicate[jsonarray.size()];
            for (int i = 0; i < aitempredicate.length; ++i) {
                aitempredicate[i] = deserialize(jsonarray.get(i));
            }
            return aitempredicate;
        }
        return new ItemPredicate[0];
    }
    
    static {
        ANY = new ItemPredicate();
    }
}
