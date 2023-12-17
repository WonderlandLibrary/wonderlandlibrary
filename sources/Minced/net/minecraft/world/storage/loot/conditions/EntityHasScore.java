// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage.loot.conditions;

import java.util.Set;
import com.google.common.collect.Maps;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.entity.player.EntityPlayerMP;
import java.util.Iterator;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.entity.Entity;
import java.util.Random;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import java.util.Map;

public class EntityHasScore implements LootCondition
{
    private final Map<String, RandomValueRange> scores;
    private final LootContext.EntityTarget target;
    
    public EntityHasScore(final Map<String, RandomValueRange> scoreIn, final LootContext.EntityTarget targetIn) {
        this.scores = scoreIn;
        this.target = targetIn;
    }
    
    @Override
    public boolean testCondition(final Random rand, final LootContext context) {
        final Entity entity = context.getEntity(this.target);
        if (entity == null) {
            return false;
        }
        final Scoreboard scoreboard = entity.world.getScoreboard();
        for (final Map.Entry<String, RandomValueRange> entry : this.scores.entrySet()) {
            if (!this.entityScoreMatch(entity, scoreboard, entry.getKey(), entry.getValue())) {
                return false;
            }
        }
        return true;
    }
    
    protected boolean entityScoreMatch(final Entity entityIn, final Scoreboard scoreboardIn, final String objectiveStr, final RandomValueRange rand) {
        final ScoreObjective scoreobjective = scoreboardIn.getObjective(objectiveStr);
        if (scoreobjective == null) {
            return false;
        }
        final String s = (entityIn instanceof EntityPlayerMP) ? entityIn.getName() : entityIn.getCachedUniqueIdString();
        return scoreboardIn.entityHasObjective(s, scoreobjective) && rand.isInRange(scoreboardIn.getOrCreateScore(s, scoreobjective).getScorePoints());
    }
    
    public static class Serializer extends LootCondition.Serializer<EntityHasScore>
    {
        protected Serializer() {
            super(new ResourceLocation("entity_scores"), EntityHasScore.class);
        }
        
        @Override
        public void serialize(final JsonObject json, final EntityHasScore value, final JsonSerializationContext context) {
            final JsonObject jsonobject = new JsonObject();
            for (final Map.Entry<String, RandomValueRange> entry : value.scores.entrySet()) {
                jsonobject.add((String)entry.getKey(), context.serialize((Object)entry.getValue()));
            }
            json.add("scores", (JsonElement)jsonobject);
            json.add("entity", context.serialize((Object)value.target));
        }
        
        @Override
        public EntityHasScore deserialize(final JsonObject json, final JsonDeserializationContext context) {
            final Set<Map.Entry<String, JsonElement>> set = (Set<Map.Entry<String, JsonElement>>)JsonUtils.getJsonObject(json, "scores").entrySet();
            final Map<String, RandomValueRange> map = (Map<String, RandomValueRange>)Maps.newLinkedHashMap();
            for (final Map.Entry<String, JsonElement> entry : set) {
                map.put(entry.getKey(), JsonUtils.deserializeClass((JsonElement)entry.getValue(), "score", context, (Class<? extends RandomValueRange>)RandomValueRange.class));
            }
            return new EntityHasScore(map, JsonUtils.deserializeClass(json, "entity", context, (Class<? extends LootContext.EntityTarget>)LootContext.EntityTarget.class));
        }
    }
}
