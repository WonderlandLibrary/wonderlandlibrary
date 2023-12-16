package net.minecraft.advancements.criterion;

import com.google.gson.JsonObject;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public class PlayerGeneratesContainerLootTrigger extends AbstractCriterionTrigger<PlayerGeneratesContainerLootTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation("player_generates_container_loot");

    public ResourceLocation getId()
    {
        return ID;
    }

    protected Instance deserializeTrigger(JsonObject json, EntityPredicate.AndPredicate entityPredicate, ConditionArrayParser conditionsParser)
    {
        ResourceLocation resourcelocation = new ResourceLocation(JSONUtils.getString(json, "loot_table"));
        return new Instance(entityPredicate, resourcelocation);
    }

    public void test(ServerPlayerEntity player, ResourceLocation generatedLoot)
    {
        this.triggerListeners(player, (instance) ->
        {
            return instance.test(generatedLoot);
        });
    }

    public static class Instance extends CriterionInstance
    {
        private final ResourceLocation generatedLoot;

        public Instance(EntityPredicate.AndPredicate player, ResourceLocation generatedLoot)
        {
            super(PlayerGeneratesContainerLootTrigger.ID, player);
            this.generatedLoot = generatedLoot;
        }

        public static Instance create(ResourceLocation generatedLoot)
        {
            return new Instance(EntityPredicate.AndPredicate.ANY_AND, generatedLoot);
        }

        public boolean test(ResourceLocation generatedLoot)
        {
            return this.generatedLoot.equals(generatedLoot);
        }

        public JsonObject serialize(ConditionArraySerializer conditions)
        {
            JsonObject jsonobject = super.serialize(conditions);
            jsonobject.addProperty("loot_table", this.generatedLoot.toString());
            return jsonobject;
        }
    }
}
