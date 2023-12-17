// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.datafix.fixes;

import java.util.Iterator;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.StringUtils;
import net.minecraft.nbt.NBTTagCompound;
import com.google.gson.Gson;
import net.minecraft.util.datafix.IFixableData;

public class SignStrictJSON implements IFixableData
{
    public static final Gson GSON_INSTANCE;
    
    @Override
    public int getFixVersion() {
        return 101;
    }
    
    @Override
    public NBTTagCompound fixTagCompound(final NBTTagCompound compound) {
        if ("Sign".equals(compound.getString("id"))) {
            this.updateLine(compound, "Text1");
            this.updateLine(compound, "Text2");
            this.updateLine(compound, "Text3");
            this.updateLine(compound, "Text4");
        }
        return compound;
    }
    
    private void updateLine(final NBTTagCompound compound, final String key) {
        final String s = compound.getString(key);
        ITextComponent itextcomponent = null;
        Label_0185: {
            if (!"null".equals(s) && !StringUtils.isNullOrEmpty(s)) {
                if (s.charAt(0) != '\"' || s.charAt(s.length() - 1) != '\"') {
                    if (s.charAt(0) != '{' || s.charAt(s.length() - 1) != '}') {
                        itextcomponent = new TextComponentString(s);
                        break Label_0185;
                    }
                }
                try {
                    itextcomponent = JsonUtils.gsonDeserialize(SignStrictJSON.GSON_INSTANCE, s, ITextComponent.class, true);
                    if (itextcomponent == null) {
                        itextcomponent = new TextComponentString("");
                    }
                }
                catch (JsonParseException ex) {}
                if (itextcomponent == null) {
                    try {
                        itextcomponent = ITextComponent.Serializer.jsonToComponent(s);
                    }
                    catch (JsonParseException ex2) {}
                }
                if (itextcomponent == null) {
                    try {
                        itextcomponent = ITextComponent.Serializer.fromJsonLenient(s);
                    }
                    catch (JsonParseException ex3) {}
                }
                if (itextcomponent == null) {
                    itextcomponent = new TextComponentString(s);
                }
            }
            else {
                itextcomponent = new TextComponentString("");
            }
        }
        compound.setString(key, ITextComponent.Serializer.componentToJson(itextcomponent));
    }
    
    static {
        GSON_INSTANCE = new GsonBuilder().registerTypeAdapter((Type)ITextComponent.class, (Object)new JsonDeserializer<ITextComponent>() {
            public ITextComponent deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
                if (p_deserialize_1_.isJsonPrimitive()) {
                    return new TextComponentString(p_deserialize_1_.getAsString());
                }
                if (p_deserialize_1_.isJsonArray()) {
                    final JsonArray jsonarray = p_deserialize_1_.getAsJsonArray();
                    ITextComponent itextcomponent = null;
                    for (final JsonElement jsonelement : jsonarray) {
                        final ITextComponent itextcomponent2 = this.deserialize(jsonelement, jsonelement.getClass(), p_deserialize_3_);
                        if (itextcomponent == null) {
                            itextcomponent = itextcomponent2;
                        }
                        else {
                            itextcomponent.appendSibling(itextcomponent2);
                        }
                    }
                    return itextcomponent;
                }
                throw new JsonParseException("Don't know how to turn " + p_deserialize_1_ + " into a Component");
            }
        }).create();
    }
}
