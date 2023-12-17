// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage.loot.conditions;

import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import java.util.Random;

public class KilledByPlayer implements LootCondition
{
    private final boolean inverse;
    
    public KilledByPlayer(final boolean inverseIn) {
        this.inverse = inverseIn;
    }
    
    @Override
    public boolean testCondition(final Random rand, final LootContext context) {
        final boolean flag = context.getKillerPlayer() != null;
        return flag == !this.inverse;
    }
    
    public static class Serializer extends LootCondition.Serializer<KilledByPlayer>
    {
        protected Serializer() {
            super(new ResourceLocation("killed_by_player"), KilledByPlayer.class);
        }
        
        @Override
        public void serialize(final JsonObject json, final KilledByPlayer value, final JsonSerializationContext context) {
            json.addProperty("inverse", Boolean.valueOf(value.inverse));
        }
        
        @Override
        public KilledByPlayer deserialize(final JsonObject json, final JsonDeserializationContext context) {
            return new KilledByPlayer(JsonUtils.getBoolean(json, "inverse", false));
        }
    }
}
