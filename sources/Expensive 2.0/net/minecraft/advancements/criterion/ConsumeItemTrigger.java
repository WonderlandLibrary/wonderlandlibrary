package net.minecraft.advancements.criterion;

import com.google.gson.JsonObject;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.potion.Potion;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

public class ConsumeItemTrigger extends AbstractCriterionTrigger<ConsumeItemTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation("consume_item");

    public ResourceLocation getId()
    {
        return ID;
    }

    public Instance deserializeTrigger(JsonObject json, EntityPredicate.AndPredicate entityPredicate, ConditionArrayParser conditionsParser)
    {
        return new Instance(entityPredicate, ItemPredicate.deserialize(json.get("item")));
    }

    public void trigger(ServerPlayerEntity player, ItemStack item)
    {
        this.triggerListeners(player, (instance) ->
        {
            return instance.test(item);
        });
    }

    public static class Instance extends CriterionInstance
    {
        private final ItemPredicate item;

        public Instance(EntityPredicate.AndPredicate player, ItemPredicate item)
        {
            super(ConsumeItemTrigger.ID, player);
            this.item = item;
        }

        public static Instance any()
        {
            return new Instance(EntityPredicate.AndPredicate.ANY_AND, ItemPredicate.ANY);
        }

        public static Instance forItem(IItemProvider item)
        {
            return new Instance(EntityPredicate.AndPredicate.ANY_AND, new ItemPredicate((ITag<Item>)null, item.asItem(), MinMaxBounds.IntBound.UNBOUNDED, MinMaxBounds.IntBound.UNBOUNDED, EnchantmentPredicate.enchantments, EnchantmentPredicate.enchantments, (Potion)null, NBTPredicate.ANY));
        }

        public boolean test(ItemStack item)
        {
            return this.item.test(item);
        }

        public JsonObject serialize(ConditionArraySerializer conditions)
        {
            JsonObject jsonobject = super.serialize(conditions);
            jsonobject.add("item", this.item.serialize());
            return jsonobject;
        }
    }
}
