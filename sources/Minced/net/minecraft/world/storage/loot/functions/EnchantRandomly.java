// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage.loot.functions;

import com.google.gson.JsonSyntaxException;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonArray;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.util.math.MathHelper;
import net.minecraft.init.Items;
import com.google.common.collect.Lists;
import net.minecraft.world.storage.loot.LootContext;
import java.util.Random;
import net.minecraft.item.ItemStack;
import java.util.Collections;
import javax.annotation.Nullable;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.enchantment.Enchantment;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class EnchantRandomly extends LootFunction
{
    private static final Logger LOGGER;
    private final List<Enchantment> enchantments;
    
    public EnchantRandomly(final LootCondition[] conditionsIn, @Nullable final List<Enchantment> enchantmentsIn) {
        super(conditionsIn);
        this.enchantments = ((enchantmentsIn == null) ? Collections.emptyList() : enchantmentsIn);
    }
    
    @Override
    public ItemStack apply(ItemStack stack, final Random rand, final LootContext context) {
        Enchantment enchantment2;
        if (this.enchantments.isEmpty()) {
            final List<Enchantment> list = (List<Enchantment>)Lists.newArrayList();
            for (final Enchantment enchantment1 : Enchantment.REGISTRY) {
                if (stack.getItem() == Items.BOOK || enchantment1.canApply(stack)) {
                    list.add(enchantment1);
                }
            }
            if (list.isEmpty()) {
                EnchantRandomly.LOGGER.warn("Couldn't find a compatible enchantment for {}", (Object)stack);
                return stack;
            }
            enchantment2 = list.get(rand.nextInt(list.size()));
        }
        else {
            enchantment2 = this.enchantments.get(rand.nextInt(this.enchantments.size()));
        }
        final int i = MathHelper.getInt(rand, enchantment2.getMinLevel(), enchantment2.getMaxLevel());
        if (stack.getItem() == Items.BOOK) {
            stack = new ItemStack(Items.ENCHANTED_BOOK);
            ItemEnchantedBook.addEnchantment(stack, new EnchantmentData(enchantment2, i));
        }
        else {
            stack.addEnchantment(enchantment2, i);
        }
        return stack;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public static class Serializer extends LootFunction.Serializer<EnchantRandomly>
    {
        public Serializer() {
            super(new ResourceLocation("enchant_randomly"), EnchantRandomly.class);
        }
        
        @Override
        public void serialize(final JsonObject object, final EnchantRandomly functionClazz, final JsonSerializationContext serializationContext) {
            if (!functionClazz.enchantments.isEmpty()) {
                final JsonArray jsonarray = new JsonArray();
                for (final Enchantment enchantment : functionClazz.enchantments) {
                    final ResourceLocation resourcelocation = Enchantment.REGISTRY.getNameForObject(enchantment);
                    if (resourcelocation == null) {
                        throw new IllegalArgumentException("Don't know how to serialize enchantment " + enchantment);
                    }
                    jsonarray.add((JsonElement)new JsonPrimitive(resourcelocation.toString()));
                }
                object.add("enchantments", (JsonElement)jsonarray);
            }
        }
        
        @Override
        public EnchantRandomly deserialize(final JsonObject object, final JsonDeserializationContext deserializationContext, final LootCondition[] conditionsIn) {
            final List<Enchantment> list = (List<Enchantment>)Lists.newArrayList();
            if (object.has("enchantments")) {
                for (final JsonElement jsonelement : JsonUtils.getJsonArray(object, "enchantments")) {
                    final String s = JsonUtils.getString(jsonelement, "enchantment");
                    final Enchantment enchantment = Enchantment.REGISTRY.getObject(new ResourceLocation(s));
                    if (enchantment == null) {
                        throw new JsonSyntaxException("Unknown enchantment '" + s + "'");
                    }
                    list.add(enchantment);
                }
            }
            return new EnchantRandomly(conditionsIn, list);
        }
    }
}
