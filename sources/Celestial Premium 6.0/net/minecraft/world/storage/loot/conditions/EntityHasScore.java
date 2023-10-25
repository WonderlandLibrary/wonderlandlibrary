/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.storage.loot.conditions;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class EntityHasScore
implements LootCondition {
    private final Map<String, RandomValueRange> scores;
    private final LootContext.EntityTarget target;

    public EntityHasScore(Map<String, RandomValueRange> scoreIn, LootContext.EntityTarget targetIn) {
        this.scores = scoreIn;
        this.target = targetIn;
    }

    @Override
    public boolean testCondition(Random rand, LootContext context) {
        Entity entity = context.getEntity(this.target);
        if (entity == null) {
            return false;
        }
        Scoreboard scoreboard = entity.world.getScoreboard();
        for (Map.Entry<String, RandomValueRange> entry : this.scores.entrySet()) {
            if (this.entityScoreMatch(entity, scoreboard, entry.getKey(), entry.getValue())) continue;
            return false;
        }
        return true;
    }

    protected boolean entityScoreMatch(Entity entityIn, Scoreboard scoreboardIn, String objectiveStr, RandomValueRange rand) {
        ScoreObjective scoreobjective = scoreboardIn.getObjective(objectiveStr);
        if (scoreobjective == null) {
            return false;
        }
        String s = entityIn instanceof EntityPlayerMP ? entityIn.getName() : entityIn.getCachedUniqueIdString();
        return !scoreboardIn.entityHasObjective(s, scoreobjective) ? false : rand.isInRange(scoreboardIn.getOrCreateScore(s, scoreobjective).getScorePoints());
    }

    public static class Serializer
    extends LootCondition.Serializer<EntityHasScore> {
        protected Serializer() {
            super(new ResourceLocation("entity_scores"), EntityHasScore.class);
        }

        @Override
        public void serialize(JsonObject json, EntityHasScore value, JsonSerializationContext context) {
            JsonObject jsonobject = new JsonObject();
            for (Map.Entry entry : value.scores.entrySet()) {
                jsonobject.add((String)entry.getKey(), context.serialize(entry.getValue()));
            }
            json.add("scores", jsonobject);
            json.add("entity", context.serialize((Object)value.target));
        }

        @Override
        public EntityHasScore deserialize(JsonObject json, JsonDeserializationContext context) {
            Set<Map.Entry<String, JsonElement>> set = JsonUtils.getJsonObject(json, "scores").entrySet();
            LinkedHashMap<String, RandomValueRange> map = Maps.newLinkedHashMap();
            for (Map.Entry<String, JsonElement> entry : set) {
                map.put(entry.getKey(), JsonUtils.deserializeClass(entry.getValue(), "score", context, RandomValueRange.class));
            }
            return new EntityHasScore(map, JsonUtils.deserializeClass(json, "entity", context, LootContext.EntityTarget.class));
        }
    }
}

