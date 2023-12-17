// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage.loot.functions;

import net.minecraft.util.ResourceLocation;
import java.util.Iterator;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import javax.annotation.Nullable;
import net.minecraft.world.storage.loot.RandomValueRange;
import org.apache.logging.log4j.LogManager;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import java.util.UUID;
import net.minecraft.world.storage.loot.LootContext;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import org.apache.logging.log4j.Logger;

public class SetAttributes extends LootFunction
{
    private static final Logger LOGGER;
    private final Modifier[] modifiers;
    
    public SetAttributes(final LootCondition[] conditionsIn, final Modifier[] modifiersIn) {
        super(conditionsIn);
        this.modifiers = modifiersIn;
    }
    
    @Override
    public ItemStack apply(final ItemStack stack, final Random rand, final LootContext context) {
        for (final Modifier setattributes$modifier : this.modifiers) {
            UUID uuid = setattributes$modifier.uuid;
            if (uuid == null) {
                uuid = UUID.randomUUID();
            }
            final EntityEquipmentSlot entityequipmentslot = setattributes$modifier.slots[rand.nextInt(setattributes$modifier.slots.length)];
            stack.addAttributeModifier(setattributes$modifier.attributeName, new AttributeModifier(uuid, setattributes$modifier.modifierName, setattributes$modifier.amount.generateFloat(rand), setattributes$modifier.operation), entityequipmentslot);
        }
        return stack;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    static class Modifier
    {
        private final String modifierName;
        private final String attributeName;
        private final int operation;
        private final RandomValueRange amount;
        @Nullable
        private final UUID uuid;
        private final EntityEquipmentSlot[] slots;
        
        private Modifier(final String modifName, final String attrName, final int operationIn, final RandomValueRange randomAmount, final EntityEquipmentSlot[] slotsIn, @Nullable final UUID uuidIn) {
            this.modifierName = modifName;
            this.attributeName = attrName;
            this.operation = operationIn;
            this.amount = randomAmount;
            this.uuid = uuidIn;
            this.slots = slotsIn;
        }
        
        public JsonObject serialize(final JsonSerializationContext context) {
            final JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("name", this.modifierName);
            jsonobject.addProperty("attribute", this.attributeName);
            jsonobject.addProperty("operation", getOperationFromStr(this.operation));
            jsonobject.add("amount", context.serialize((Object)this.amount));
            if (this.uuid != null) {
                jsonobject.addProperty("id", this.uuid.toString());
            }
            if (this.slots.length == 1) {
                jsonobject.addProperty("slot", this.slots[0].getName());
            }
            else {
                final JsonArray jsonarray = new JsonArray();
                for (final EntityEquipmentSlot entityequipmentslot : this.slots) {
                    jsonarray.add((JsonElement)new JsonPrimitive(entityequipmentslot.getName()));
                }
                jsonobject.add("slot", (JsonElement)jsonarray);
            }
            return jsonobject;
        }
        
        public static Modifier deserialize(final JsonObject jsonObj, final JsonDeserializationContext context) {
            final String s = JsonUtils.getString(jsonObj, "name");
            final String s2 = JsonUtils.getString(jsonObj, "attribute");
            final int i = getOperationFromInt(JsonUtils.getString(jsonObj, "operation"));
            final RandomValueRange randomvaluerange = JsonUtils.deserializeClass(jsonObj, "amount", context, (Class<? extends RandomValueRange>)RandomValueRange.class);
            UUID uuid = null;
            EntityEquipmentSlot[] aentityequipmentslot;
            if (JsonUtils.isString(jsonObj, "slot")) {
                aentityequipmentslot = new EntityEquipmentSlot[] { EntityEquipmentSlot.fromString(JsonUtils.getString(jsonObj, "slot")) };
            }
            else {
                if (!JsonUtils.isJsonArray(jsonObj, "slot")) {
                    throw new JsonSyntaxException("Invalid or missing attribute modifier slot; must be either string or array of strings.");
                }
                final JsonArray jsonarray = JsonUtils.getJsonArray(jsonObj, "slot");
                aentityequipmentslot = new EntityEquipmentSlot[jsonarray.size()];
                int j = 0;
                for (final JsonElement jsonelement : jsonarray) {
                    aentityequipmentslot[j++] = EntityEquipmentSlot.fromString(JsonUtils.getString(jsonelement, "slot"));
                }
                if (aentityequipmentslot.length == 0) {
                    throw new JsonSyntaxException("Invalid attribute modifier slot; must contain at least one entry.");
                }
            }
            if (jsonObj.has("id")) {
                final String s3 = JsonUtils.getString(jsonObj, "id");
                try {
                    uuid = UUID.fromString(s3);
                }
                catch (IllegalArgumentException var12) {
                    throw new JsonSyntaxException("Invalid attribute modifier id '" + s3 + "' (must be UUID format, with dashes)");
                }
            }
            return new Modifier(s, s2, i, randomvaluerange, aentityequipmentslot, uuid);
        }
        
        private static String getOperationFromStr(final int operationIn) {
            switch (operationIn) {
                case 0: {
                    return "addition";
                }
                case 1: {
                    return "multiply_base";
                }
                case 2: {
                    return "multiply_total";
                }
                default: {
                    throw new IllegalArgumentException("Unknown operation " + operationIn);
                }
            }
        }
        
        private static int getOperationFromInt(final String operationIn) {
            if ("addition".equals(operationIn)) {
                return 0;
            }
            if ("multiply_base".equals(operationIn)) {
                return 1;
            }
            if ("multiply_total".equals(operationIn)) {
                return 2;
            }
            throw new JsonSyntaxException("Unknown attribute modifier operation " + operationIn);
        }
    }
    
    public static class Serializer extends LootFunction.Serializer<SetAttributes>
    {
        public Serializer() {
            super(new ResourceLocation("set_attributes"), SetAttributes.class);
        }
        
        @Override
        public void serialize(final JsonObject object, final SetAttributes functionClazz, final JsonSerializationContext serializationContext) {
            final JsonArray jsonarray = new JsonArray();
            for (final Modifier setattributes$modifier : functionClazz.modifiers) {
                jsonarray.add((JsonElement)setattributes$modifier.serialize(serializationContext));
            }
            object.add("modifiers", (JsonElement)jsonarray);
        }
        
        @Override
        public SetAttributes deserialize(final JsonObject object, final JsonDeserializationContext deserializationContext, final LootCondition[] conditionsIn) {
            final JsonArray jsonarray = JsonUtils.getJsonArray(object, "modifiers");
            final Modifier[] asetattributes$modifier = new Modifier[jsonarray.size()];
            int i = 0;
            for (final JsonElement jsonelement : jsonarray) {
                asetattributes$modifier[i++] = Modifier.deserialize(JsonUtils.getJsonObject(jsonelement, "modifier"), deserializationContext);
            }
            if (asetattributes$modifier.length == 0) {
                throw new JsonSyntaxException("Invalid attribute modifiers array; cannot be empty");
            }
            return new SetAttributes(conditionsIn, asetattributes$modifier);
        }
    }
}
