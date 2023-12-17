// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.advancements;

import javax.annotation.Nullable;
import java.util.Iterator;
import com.google.gson.JsonElement;
import com.google.common.collect.Maps;
import java.util.Map;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.network.PacketBuffer;

public class Criterion
{
    private final ICriterionInstance criterionInstance;
    
    public Criterion(final ICriterionInstance p_i47470_1_) {
        this.criterionInstance = p_i47470_1_;
    }
    
    public Criterion() {
        this.criterionInstance = null;
    }
    
    public void serializeToNetwork(final PacketBuffer p_192140_1_) {
    }
    
    public static Criterion criterionFromJson(final JsonObject json, final JsonDeserializationContext context) {
        final ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(json, "trigger"));
        final ICriterionTrigger<?> icriteriontrigger = CriteriaTriggers.get(resourcelocation);
        if (icriteriontrigger == null) {
            throw new JsonSyntaxException("Invalid criterion trigger: " + resourcelocation);
        }
        final ICriterionInstance icriterioninstance = (ICriterionInstance)icriteriontrigger.deserializeInstance(JsonUtils.getJsonObject(json, "conditions", new JsonObject()), context);
        return new Criterion(icriterioninstance);
    }
    
    public static Criterion criterionFromNetwork(final PacketBuffer p_192146_0_) {
        return new Criterion();
    }
    
    public static Map<String, Criterion> criteriaFromJson(final JsonObject json, final JsonDeserializationContext context) {
        final Map<String, Criterion> map = (Map<String, Criterion>)Maps.newHashMap();
        for (final Map.Entry<String, JsonElement> entry : json.entrySet()) {
            map.put(entry.getKey(), criterionFromJson(JsonUtils.getJsonObject(entry.getValue(), "criterion"), context));
        }
        return map;
    }
    
    public static Map<String, Criterion> criteriaFromNetwork(final PacketBuffer bus) {
        final Map<String, Criterion> map = (Map<String, Criterion>)Maps.newHashMap();
        for (int i = bus.readVarInt(), j = 0; j < i; ++j) {
            map.put(bus.readString(32767), criterionFromNetwork(bus));
        }
        return map;
    }
    
    public static void serializeToNetwork(final Map<String, Criterion> criteria, final PacketBuffer buf) {
        buf.writeVarInt(criteria.size());
        for (final Map.Entry<String, Criterion> entry : criteria.entrySet()) {
            buf.writeString(entry.getKey());
            entry.getValue().serializeToNetwork(buf);
        }
    }
    
    @Nullable
    public ICriterionInstance getCriterionInstance() {
        return this.criterionInstance;
    }
}
