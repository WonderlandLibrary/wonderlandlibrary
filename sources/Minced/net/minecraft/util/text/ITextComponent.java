// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.text;

import com.google.gson.TypeAdapterFactory;
import net.minecraft.util.EnumTypeAdapterFactory;
import com.google.gson.GsonBuilder;
import javax.annotation.Nullable;
import com.google.gson.JsonPrimitive;
import java.util.Map;
import com.google.gson.JsonSerializationContext;
import java.util.Iterator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonParseException;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.Gson;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import java.util.List;

public interface ITextComponent extends Iterable<ITextComponent>
{
    ITextComponent setStyle(final Style p0);
    
    Style getStyle();
    
    ITextComponent appendText(final String p0);
    
    ITextComponent appendSibling(final ITextComponent p0);
    
    String getUnformattedComponentText();
    
    String getUnformattedText();
    
    String getFormattedText();
    
    List<ITextComponent> getSiblings();
    
    ITextComponent createCopy();
    
    public static class Serializer implements JsonDeserializer<ITextComponent>, JsonSerializer<ITextComponent>
    {
        private static final Gson GSON;
        
        public ITextComponent deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            if (p_deserialize_1_.isJsonPrimitive()) {
                return new TextComponentString(p_deserialize_1_.getAsString());
            }
            if (p_deserialize_1_.isJsonObject()) {
                final JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
                ITextComponent itextcomponent;
                if (jsonobject.has("text")) {
                    itextcomponent = new TextComponentString(jsonobject.get("text").getAsString());
                }
                else if (jsonobject.has("translate")) {
                    final String s = jsonobject.get("translate").getAsString();
                    if (jsonobject.has("with")) {
                        final JsonArray jsonarray = jsonobject.getAsJsonArray("with");
                        final Object[] aobject = new Object[jsonarray.size()];
                        for (int i = 0; i < aobject.length; ++i) {
                            aobject[i] = this.deserialize(jsonarray.get(i), p_deserialize_2_, p_deserialize_3_);
                            if (aobject[i] instanceof TextComponentString) {
                                final TextComponentString textcomponentstring = (TextComponentString)aobject[i];
                                if (textcomponentstring.getStyle().isEmpty() && textcomponentstring.getSiblings().isEmpty()) {
                                    aobject[i] = textcomponentstring.getText();
                                }
                            }
                        }
                        itextcomponent = new TextComponentTranslation(s, aobject);
                    }
                    else {
                        itextcomponent = new TextComponentTranslation(s, new Object[0]);
                    }
                }
                else if (jsonobject.has("score")) {
                    final JsonObject jsonobject2 = jsonobject.getAsJsonObject("score");
                    if (!jsonobject2.has("name") || !jsonobject2.has("objective")) {
                        throw new JsonParseException("A score component needs a least a name and an objective");
                    }
                    itextcomponent = new TextComponentScore(JsonUtils.getString(jsonobject2, "name"), JsonUtils.getString(jsonobject2, "objective"));
                    if (jsonobject2.has("value")) {
                        ((TextComponentScore)itextcomponent).setValue(JsonUtils.getString(jsonobject2, "value"));
                    }
                }
                else if (jsonobject.has("selector")) {
                    itextcomponent = new TextComponentSelector(JsonUtils.getString(jsonobject, "selector"));
                }
                else {
                    if (!jsonobject.has("keybind")) {
                        throw new JsonParseException("Don't know how to turn " + p_deserialize_1_ + " into a Component");
                    }
                    itextcomponent = new TextComponentKeybind(JsonUtils.getString(jsonobject, "keybind"));
                }
                if (jsonobject.has("extra")) {
                    final JsonArray jsonarray2 = jsonobject.getAsJsonArray("extra");
                    if (jsonarray2.size() <= 0) {
                        throw new JsonParseException("Unexpected empty array of components");
                    }
                    for (int j = 0; j < jsonarray2.size(); ++j) {
                        itextcomponent.appendSibling(this.deserialize(jsonarray2.get(j), p_deserialize_2_, p_deserialize_3_));
                    }
                }
                itextcomponent.setStyle((Style)p_deserialize_3_.deserialize(p_deserialize_1_, (Type)Style.class));
                return itextcomponent;
            }
            if (p_deserialize_1_.isJsonArray()) {
                final JsonArray jsonarray3 = p_deserialize_1_.getAsJsonArray();
                ITextComponent itextcomponent2 = null;
                for (final JsonElement jsonelement : jsonarray3) {
                    final ITextComponent itextcomponent3 = this.deserialize(jsonelement, jsonelement.getClass(), p_deserialize_3_);
                    if (itextcomponent2 == null) {
                        itextcomponent2 = itextcomponent3;
                    }
                    else {
                        itextcomponent2.appendSibling(itextcomponent3);
                    }
                }
                return itextcomponent2;
            }
            throw new JsonParseException("Don't know how to turn " + p_deserialize_1_ + " into a Component");
        }
        
        private void serializeChatStyle(final Style style, final JsonObject object, final JsonSerializationContext ctx) {
            final JsonElement jsonelement = ctx.serialize((Object)style);
            if (jsonelement.isJsonObject()) {
                final JsonObject jsonobject = (JsonObject)jsonelement;
                for (final Map.Entry<String, JsonElement> entry : jsonobject.entrySet()) {
                    object.add((String)entry.getKey(), (JsonElement)entry.getValue());
                }
            }
        }
        
        public JsonElement serialize(final ITextComponent p_serialize_1_, final Type p_serialize_2_, final JsonSerializationContext p_serialize_3_) {
            final JsonObject jsonobject = new JsonObject();
            if (!p_serialize_1_.getStyle().isEmpty()) {
                this.serializeChatStyle(p_serialize_1_.getStyle(), jsonobject, p_serialize_3_);
            }
            if (!p_serialize_1_.getSiblings().isEmpty()) {
                final JsonArray jsonarray = new JsonArray();
                for (final ITextComponent itextcomponent : p_serialize_1_.getSiblings()) {
                    jsonarray.add(this.serialize(itextcomponent, itextcomponent.getClass(), p_serialize_3_));
                }
                jsonobject.add("extra", (JsonElement)jsonarray);
            }
            if (p_serialize_1_ instanceof TextComponentString) {
                jsonobject.addProperty("text", ((TextComponentString)p_serialize_1_).getText());
            }
            else if (p_serialize_1_ instanceof TextComponentTranslation) {
                final TextComponentTranslation textcomponenttranslation = (TextComponentTranslation)p_serialize_1_;
                jsonobject.addProperty("translate", textcomponenttranslation.getKey());
                if (textcomponenttranslation.getFormatArgs() != null && textcomponenttranslation.getFormatArgs().length > 0) {
                    final JsonArray jsonarray2 = new JsonArray();
                    for (final Object object : textcomponenttranslation.getFormatArgs()) {
                        if (object instanceof ITextComponent) {
                            jsonarray2.add(this.serialize((ITextComponent)object, object.getClass(), p_serialize_3_));
                        }
                        else {
                            jsonarray2.add((JsonElement)new JsonPrimitive(String.valueOf(object)));
                        }
                    }
                    jsonobject.add("with", (JsonElement)jsonarray2);
                }
            }
            else if (p_serialize_1_ instanceof TextComponentScore) {
                final TextComponentScore textcomponentscore = (TextComponentScore)p_serialize_1_;
                final JsonObject jsonobject2 = new JsonObject();
                jsonobject2.addProperty("name", textcomponentscore.getName());
                jsonobject2.addProperty("objective", textcomponentscore.getObjective());
                jsonobject2.addProperty("value", textcomponentscore.getUnformattedComponentText());
                jsonobject.add("score", (JsonElement)jsonobject2);
            }
            else if (p_serialize_1_ instanceof TextComponentSelector) {
                final TextComponentSelector textcomponentselector = (TextComponentSelector)p_serialize_1_;
                jsonobject.addProperty("selector", textcomponentselector.getSelector());
            }
            else {
                if (!(p_serialize_1_ instanceof TextComponentKeybind)) {
                    throw new IllegalArgumentException("Don't know how to serialize " + p_serialize_1_ + " as a Component");
                }
                final TextComponentKeybind textcomponentkeybind = (TextComponentKeybind)p_serialize_1_;
                jsonobject.addProperty("keybind", textcomponentkeybind.getKeybind());
            }
            return (JsonElement)jsonobject;
        }
        
        public static String componentToJson(final ITextComponent component) {
            return Serializer.GSON.toJson((Object)component);
        }
        
        @Nullable
        public static ITextComponent jsonToComponent(final String json) {
            return JsonUtils.gsonDeserialize(Serializer.GSON, json, ITextComponent.class, false);
        }
        
        @Nullable
        public static ITextComponent fromJsonLenient(final String json) {
            return JsonUtils.gsonDeserialize(Serializer.GSON, json, ITextComponent.class, true);
        }
        
        static {
            final GsonBuilder gsonbuilder = new GsonBuilder();
            gsonbuilder.registerTypeHierarchyAdapter((Class)ITextComponent.class, (Object)new Serializer());
            gsonbuilder.registerTypeHierarchyAdapter((Class)Style.class, (Object)new Style.Serializer());
            gsonbuilder.registerTypeAdapterFactory((TypeAdapterFactory)new EnumTypeAdapterFactory());
            GSON = gsonbuilder.create();
        }
    }
}
