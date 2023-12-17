// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.advancements;

import com.google.gson.JsonParseException;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import java.util.Date;
import java.util.List;
import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import net.minecraft.network.PacketBuffer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import com.google.common.collect.Maps;
import java.util.Map;

public class AdvancementProgress implements Comparable<AdvancementProgress>
{
    private final Map<String, CriterionProgress> criteria;
    private String[][] requirements;
    
    public AdvancementProgress() {
        this.criteria = (Map<String, CriterionProgress>)Maps.newHashMap();
        this.requirements = new String[0][];
    }
    
    public void update(final Map<String, Criterion> criteriaIn, final String[][] requirements) {
        final Set<String> set = criteriaIn.keySet();
        final Iterator<Map.Entry<String, CriterionProgress>> iterator = this.criteria.entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<String, CriterionProgress> entry = iterator.next();
            if (!set.contains(entry.getKey())) {
                iterator.remove();
            }
        }
        for (final String s : set) {
            if (!this.criteria.containsKey(s)) {
                this.criteria.put(s, new CriterionProgress(this));
            }
        }
        this.requirements = requirements;
    }
    
    public boolean isDone() {
        if (this.requirements.length == 0) {
            return false;
        }
        for (final String[] astring : this.requirements) {
            boolean flag = false;
            for (final String s : astring) {
                final CriterionProgress criterionprogress = this.getCriterionProgress(s);
                if (criterionprogress != null && criterionprogress.isObtained()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                return false;
            }
        }
        return true;
    }
    
    public boolean hasProgress() {
        for (final CriterionProgress criterionprogress : this.criteria.values()) {
            if (criterionprogress.isObtained()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean grantCriterion(final String criterionIn) {
        final CriterionProgress criterionprogress = this.criteria.get(criterionIn);
        if (criterionprogress != null && !criterionprogress.isObtained()) {
            criterionprogress.obtain();
            return true;
        }
        return false;
    }
    
    public boolean revokeCriterion(final String criterionIn) {
        final CriterionProgress criterionprogress = this.criteria.get(criterionIn);
        if (criterionprogress != null && criterionprogress.isObtained()) {
            criterionprogress.reset();
            return true;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "AdvancementProgress{criteria=" + this.criteria + ", requirements=" + Arrays.deepToString(this.requirements) + '}';
    }
    
    public void serializeToNetwork(final PacketBuffer p_192104_1_) {
        p_192104_1_.writeVarInt(this.criteria.size());
        for (final Map.Entry<String, CriterionProgress> entry : this.criteria.entrySet()) {
            p_192104_1_.writeString(entry.getKey());
            entry.getValue().write(p_192104_1_);
        }
    }
    
    public static AdvancementProgress fromNetwork(final PacketBuffer p_192100_0_) {
        final AdvancementProgress advancementprogress = new AdvancementProgress();
        for (int i = p_192100_0_.readVarInt(), j = 0; j < i; ++j) {
            advancementprogress.criteria.put(p_192100_0_.readString(32767), CriterionProgress.read(p_192100_0_, advancementprogress));
        }
        return advancementprogress;
    }
    
    @Nullable
    public CriterionProgress getCriterionProgress(final String criterionIn) {
        return this.criteria.get(criterionIn);
    }
    
    public float getPercent() {
        if (this.criteria.isEmpty()) {
            return 0.0f;
        }
        final float f = (float)this.requirements.length;
        final float f2 = (float)this.countCompletedRequirements();
        return f2 / f;
    }
    
    @Nullable
    public String getProgressText() {
        if (this.criteria.isEmpty()) {
            return null;
        }
        final int i = this.requirements.length;
        if (i <= 1) {
            return null;
        }
        final int j = this.countCompletedRequirements();
        return j + "/" + i;
    }
    
    private int countCompletedRequirements() {
        int i = 0;
        for (final String[] astring : this.requirements) {
            boolean flag = false;
            for (final String s : astring) {
                final CriterionProgress criterionprogress = this.getCriterionProgress(s);
                if (criterionprogress != null && criterionprogress.isObtained()) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                ++i;
            }
        }
        return i;
    }
    
    public Iterable<String> getRemaningCriteria() {
        final List<String> list = (List<String>)Lists.newArrayList();
        for (final Map.Entry<String, CriterionProgress> entry : this.criteria.entrySet()) {
            if (!entry.getValue().isObtained()) {
                list.add(entry.getKey());
            }
        }
        return list;
    }
    
    public Iterable<String> getCompletedCriteria() {
        final List<String> list = (List<String>)Lists.newArrayList();
        for (final Map.Entry<String, CriterionProgress> entry : this.criteria.entrySet()) {
            if (entry.getValue().isObtained()) {
                list.add(entry.getKey());
            }
        }
        return list;
    }
    
    @Nullable
    public Date getFirstProgressDate() {
        Date date = null;
        for (final CriterionProgress criterionprogress : this.criteria.values()) {
            if (criterionprogress.isObtained() && (date == null || criterionprogress.getObtained().before(date))) {
                date = criterionprogress.getObtained();
            }
        }
        return date;
    }
    
    @Override
    public int compareTo(final AdvancementProgress p_compareTo_1_) {
        final Date date = this.getFirstProgressDate();
        final Date date2 = p_compareTo_1_.getFirstProgressDate();
        if (date == null && date2 != null) {
            return 1;
        }
        if (date != null && date2 == null) {
            return -1;
        }
        return (date == null && date2 == null) ? 0 : date.compareTo(date2);
    }
    
    public static class Serializer implements JsonDeserializer<AdvancementProgress>, JsonSerializer<AdvancementProgress>
    {
        public JsonElement serialize(final AdvancementProgress p_serialize_1_, final Type p_serialize_2_, final JsonSerializationContext p_serialize_3_) {
            final JsonObject jsonobject = new JsonObject();
            final JsonObject jsonobject2 = new JsonObject();
            for (final Map.Entry<String, CriterionProgress> entry : p_serialize_1_.criteria.entrySet()) {
                final CriterionProgress criterionprogress = entry.getValue();
                if (criterionprogress.isObtained()) {
                    jsonobject2.add((String)entry.getKey(), criterionprogress.serialize());
                }
            }
            if (!jsonobject2.entrySet().isEmpty()) {
                jsonobject.add("criteria", (JsonElement)jsonobject2);
            }
            jsonobject.addProperty("done", Boolean.valueOf(p_serialize_1_.isDone()));
            return (JsonElement)jsonobject;
        }
        
        public AdvancementProgress deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            final JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "advancement");
            final JsonObject jsonobject2 = JsonUtils.getJsonObject(jsonobject, "criteria", new JsonObject());
            final AdvancementProgress advancementprogress = new AdvancementProgress();
            for (final Map.Entry<String, JsonElement> entry : jsonobject2.entrySet()) {
                final String s = entry.getKey();
                advancementprogress.criteria.put(s, CriterionProgress.fromDateTime(advancementprogress, JsonUtils.getString(entry.getValue(), s)));
            }
            return advancementprogress;
        }
    }
}
