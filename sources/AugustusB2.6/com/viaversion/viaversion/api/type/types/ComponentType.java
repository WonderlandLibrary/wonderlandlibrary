// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types;

import com.viaversion.viaversion.libs.gson.JsonSyntaxException;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.libs.gson.JsonParser;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.api.type.Type;

public class ComponentType extends Type<JsonElement>
{
    private static final StringType STRING_TAG;
    
    public ComponentType() {
        super(JsonElement.class);
    }
    
    @Override
    public JsonElement read(final ByteBuf buffer) throws Exception {
        final String s = ComponentType.STRING_TAG.read(buffer);
        try {
            return JsonParser.parseString(s);
        }
        catch (JsonSyntaxException e) {
            Via.getPlatform().getLogger().severe("Error when trying to parse json: " + s);
            throw e;
        }
    }
    
    @Override
    public void write(final ByteBuf buffer, final JsonElement object) throws Exception {
        ComponentType.STRING_TAG.write(buffer, object.toString());
    }
    
    static {
        STRING_TAG = new StringType(262144);
    }
}
