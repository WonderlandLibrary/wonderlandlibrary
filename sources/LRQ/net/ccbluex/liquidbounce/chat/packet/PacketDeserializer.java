/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.JsonDeserializationContext
 *  com.google.gson.JsonDeserializer
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.chat.packet;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import net.ccbluex.liquidbounce.chat.packet.packets.Packet;
import org.jetbrains.annotations.Nullable;

public final class PacketDeserializer
implements JsonDeserializer<Packet> {
    private final HashMap<String, Class<? extends Packet>> packetRegistry = new HashMap();

    public final void registerPacket(String packetName, Class<? extends Packet> packetClass) {
        ((Map)this.packetRegistry).put(packetName, packetClass);
    }

    public Packet deserialize(JsonElement json, Type typeOfT, @Nullable JsonDeserializationContext context) {
        JsonObject packetObject = json.getAsJsonObject();
        String packetName = packetObject.get("m").getAsString();
        if (!this.packetRegistry.containsKey(packetName)) {
            return null;
        }
        if (!packetObject.has("c")) {
            packetObject.add("c", (JsonElement)new JsonObject());
        }
        return (Packet)new Gson().fromJson(packetObject.get("c"), this.packetRegistry.get(packetName));
    }
}

