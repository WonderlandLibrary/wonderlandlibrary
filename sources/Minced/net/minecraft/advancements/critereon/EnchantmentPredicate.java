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
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.enchantment.Enchantment;

public class EnchantmentPredicate
{
    public static final EnchantmentPredicate ANY;
    private final Enchantment enchantment;
    private final MinMaxBounds levels;
    
    public EnchantmentPredicate() {
        this.enchantment = null;
        this.levels = MinMaxBounds.UNBOUNDED;
    }
    
    public EnchantmentPredicate(@Nullable final Enchantment enchantment, final MinMaxBounds levels) {
        this.enchantment = enchantment;
        this.levels = levels;
    }
    
    public boolean test(final Map<Enchantment, Integer> enchantmentsIn) {
        if (this.enchantment != null) {
            if (!enchantmentsIn.containsKey(this.enchantment)) {
                return false;
            }
            final int i = enchantmentsIn.get(this.enchantment);
            if (this.levels != null && !this.levels.test((float)i)) {
                return false;
            }
        }
        else if (this.levels != null) {
            for (final Integer integer : enchantmentsIn.values()) {
                if (this.levels.test(integer)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    public static EnchantmentPredicate deserialize(@Nullable final JsonElement element) {
        if (element != null && !element.isJsonNull()) {
            final JsonObject jsonobject = JsonUtils.getJsonObject(element, "enchantment");
            Enchantment enchantment = null;
            if (jsonobject.has("enchantment")) {
                final ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(jsonobject, "enchantment"));
                enchantment = Enchantment.REGISTRY.getObject(resourcelocation);
                if (enchantment == null) {
                    throw new JsonSyntaxException("Unknown enchantment '" + resourcelocation + "'");
                }
            }
            final MinMaxBounds minmaxbounds = MinMaxBounds.deserialize(jsonobject.get("levels"));
            return new EnchantmentPredicate(enchantment, minmaxbounds);
        }
        return EnchantmentPredicate.ANY;
    }
    
    public static EnchantmentPredicate[] deserializeArray(@Nullable final JsonElement element) {
        if (element != null && !element.isJsonNull()) {
            final JsonArray jsonarray = JsonUtils.getJsonArray(element, "enchantments");
            final EnchantmentPredicate[] aenchantmentpredicate = new EnchantmentPredicate[jsonarray.size()];
            for (int i = 0; i < aenchantmentpredicate.length; ++i) {
                aenchantmentpredicate[i] = deserialize(jsonarray.get(i));
            }
            return aenchantmentpredicate;
        }
        return new EnchantmentPredicate[0];
    }
    
    static {
        ANY = new EnchantmentPredicate();
    }
}
