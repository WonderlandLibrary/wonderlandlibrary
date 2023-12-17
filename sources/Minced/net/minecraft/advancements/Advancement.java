// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.advancements;

import java.io.IOException;
import java.util.Iterator;
import org.apache.commons.lang3.ArrayUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.network.PacketBuffer;
import java.util.function.Function;
import java.util.Arrays;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.util.text.TextComponentString;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import javax.annotation.Nullable;
import net.minecraft.util.text.ITextComponent;
import java.util.Set;
import java.util.Map;
import net.minecraft.util.ResourceLocation;

public class Advancement
{
    private final Advancement parent;
    private final DisplayInfo display;
    private final AdvancementRewards rewards;
    private final ResourceLocation id;
    private final Map<String, Criterion> criteria;
    private final String[][] requirements;
    private final Set<Advancement> children;
    private final ITextComponent displayText;
    
    public Advancement(final ResourceLocation id, @Nullable final Advancement parentIn, @Nullable final DisplayInfo displayIn, final AdvancementRewards rewardsIn, final Map<String, Criterion> criteriaIn, final String[][] requirementsIn) {
        this.children = (Set<Advancement>)Sets.newLinkedHashSet();
        this.id = id;
        this.display = displayIn;
        this.criteria = (Map<String, Criterion>)ImmutableMap.copyOf((Map)criteriaIn);
        this.parent = parentIn;
        this.rewards = rewardsIn;
        this.requirements = requirementsIn;
        if (parentIn != null) {
            parentIn.addChild(this);
        }
        if (displayIn == null) {
            this.displayText = new TextComponentString(id.toString());
        }
        else {
            this.displayText = new TextComponentString("[");
            this.displayText.getStyle().setColor(displayIn.getFrame().getFormat());
            final ITextComponent itextcomponent = displayIn.getTitle().createCopy();
            final ITextComponent itextcomponent2 = new TextComponentString("");
            final ITextComponent itextcomponent3 = itextcomponent.createCopy();
            itextcomponent3.getStyle().setColor(displayIn.getFrame().getFormat());
            itextcomponent2.appendSibling(itextcomponent3);
            itextcomponent2.appendText("\n");
            itextcomponent2.appendSibling(displayIn.getDescription());
            itextcomponent.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, itextcomponent2));
            this.displayText.appendSibling(itextcomponent);
            this.displayText.appendText("]");
        }
    }
    
    public Builder copy() {
        return new Builder((this.parent == null) ? null : this.parent.getId(), this.display, this.rewards, this.criteria, this.requirements);
    }
    
    @Nullable
    public Advancement getParent() {
        return this.parent;
    }
    
    @Nullable
    public DisplayInfo getDisplay() {
        return this.display;
    }
    
    public AdvancementRewards getRewards() {
        return this.rewards;
    }
    
    @Override
    public String toString() {
        return "SimpleAdvancement{id=" + this.getId() + ", parent=" + ((this.parent == null) ? "null" : this.parent.getId()) + ", display=" + this.display + ", rewards=" + this.rewards + ", criteria=" + this.criteria + ", requirements=" + Arrays.deepToString(this.requirements) + '}';
    }
    
    public Iterable<Advancement> getChildren() {
        return this.children;
    }
    
    public Map<String, Criterion> getCriteria() {
        return this.criteria;
    }
    
    public int getRequirementCount() {
        return this.requirements.length;
    }
    
    public void addChild(final Advancement advancementIn) {
        this.children.add(advancementIn);
    }
    
    public ResourceLocation getId() {
        return this.id;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof Advancement)) {
            return false;
        }
        final Advancement advancement = (Advancement)p_equals_1_;
        return this.id.equals(advancement.id);
    }
    
    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
    
    public String[][] getRequirements() {
        return this.requirements;
    }
    
    public ITextComponent getDisplayText() {
        return this.displayText;
    }
    
    public static class Builder
    {
        private final ResourceLocation parentId;
        private Advancement parent;
        private final DisplayInfo display;
        private final AdvancementRewards rewards;
        private final Map<String, Criterion> criteria;
        private final String[][] requirements;
        
        Builder(@Nullable final ResourceLocation parentIdIn, @Nullable final DisplayInfo displayIn, final AdvancementRewards rewardsIn, final Map<String, Criterion> criteriaIn, final String[][] requirementsIn) {
            this.parentId = parentIdIn;
            this.display = displayIn;
            this.rewards = rewardsIn;
            this.criteria = criteriaIn;
            this.requirements = requirementsIn;
        }
        
        public boolean resolveParent(final Function<ResourceLocation, Advancement> lookup) {
            if (this.parentId == null) {
                return true;
            }
            this.parent = lookup.apply(this.parentId);
            return this.parent != null;
        }
        
        public Advancement build(final ResourceLocation id) {
            return new Advancement(id, this.parent, this.display, this.rewards, this.criteria, this.requirements);
        }
        
        public void writeTo(final PacketBuffer buf) {
            if (this.parentId == null) {
                buf.writeBoolean(false);
            }
            else {
                buf.writeBoolean(true);
                buf.writeResourceLocation(this.parentId);
            }
            if (this.display == null) {
                buf.writeBoolean(false);
            }
            else {
                buf.writeBoolean(true);
                this.display.write(buf);
            }
            Criterion.serializeToNetwork(this.criteria, buf);
            buf.writeVarInt(this.requirements.length);
            for (final String[] astring : this.requirements) {
                buf.writeVarInt(astring.length);
                for (final String s : astring) {
                    buf.writeString(s);
                }
            }
        }
        
        @Override
        public String toString() {
            return "Task Advancement{parentId=" + this.parentId + ", display=" + this.display + ", rewards=" + this.rewards + ", criteria=" + this.criteria + ", requirements=" + Arrays.deepToString(this.requirements) + '}';
        }
        
        public static Builder deserialize(final JsonObject json, final JsonDeserializationContext context) {
            final ResourceLocation resourcelocation = json.has("parent") ? new ResourceLocation(JsonUtils.getString(json, "parent")) : null;
            final DisplayInfo displayinfo = json.has("display") ? DisplayInfo.deserialize(JsonUtils.getJsonObject(json, "display"), context) : null;
            final AdvancementRewards advancementrewards = JsonUtils.deserializeClass(json, "rewards", AdvancementRewards.EMPTY, context, AdvancementRewards.class);
            final Map<String, Criterion> map = Criterion.criteriaFromJson(JsonUtils.getJsonObject(json, "criteria"), context);
            if (map.isEmpty()) {
                throw new JsonSyntaxException("Advancement criteria cannot be empty");
            }
            final JsonArray jsonarray = JsonUtils.getJsonArray(json, "requirements", new JsonArray());
            String[][] astring = new String[jsonarray.size()][];
            for (int i = 0; i < jsonarray.size(); ++i) {
                final JsonArray jsonarray2 = JsonUtils.getJsonArray(jsonarray.get(i), "requirements[" + i + "]");
                astring[i] = new String[jsonarray2.size()];
                for (int j = 0; j < jsonarray2.size(); ++j) {
                    astring[i][j] = JsonUtils.getString(jsonarray2.get(j), "requirements[" + i + "][" + j + "]");
                }
            }
            if (astring.length == 0) {
                astring = new String[map.size()][];
                int k = 0;
                for (final String s2 : map.keySet()) {
                    astring[k++] = new String[] { s2 };
                }
            }
            for (final String[] astring2 : astring) {
                if (astring2.length == 0 && map.isEmpty()) {
                    throw new JsonSyntaxException("Requirement entry cannot be empty");
                }
                for (final String s3 : astring2) {
                    if (!map.containsKey(s3)) {
                        throw new JsonSyntaxException("Unknown required criterion '" + s3 + "'");
                    }
                }
            }
            for (final String s4 : map.keySet()) {
                boolean flag = false;
                for (final String[] astring3 : astring) {
                    if (ArrayUtils.contains((Object[])astring3, (Object)s4)) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    throw new JsonSyntaxException("Criterion '" + s4 + "' isn't a requirement for completion. This isn't supported behaviour, all criteria must be required.");
                }
            }
            return new Builder(resourcelocation, displayinfo, advancementrewards, map, astring);
        }
        
        public static Builder readFrom(final PacketBuffer buf) throws IOException {
            final ResourceLocation resourcelocation = buf.readBoolean() ? buf.readResourceLocation() : null;
            final DisplayInfo displayinfo = buf.readBoolean() ? DisplayInfo.read(buf) : null;
            final Map<String, Criterion> map = Criterion.criteriaFromNetwork(buf);
            final String[][] astring = new String[buf.readVarInt()][];
            for (int i = 0; i < astring.length; ++i) {
                astring[i] = new String[buf.readVarInt()];
                for (int j = 0; j < astring[i].length; ++j) {
                    astring[i][j] = buf.readString(32767);
                }
            }
            return new Builder(resourcelocation, displayinfo, AdvancementRewards.EMPTY, map, astring);
        }
    }
}
