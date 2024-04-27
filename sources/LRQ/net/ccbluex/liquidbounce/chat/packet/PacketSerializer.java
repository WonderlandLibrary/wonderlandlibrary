/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonSerializationContext
 *  com.google.gson.JsonSerializer
 */
package net.ccbluex.liquidbounce.chat.packet;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import net.ccbluex.liquidbounce.chat.packet.SerializedPacket;
import net.ccbluex.liquidbounce.chat.packet.packets.Packet;

public final class PacketSerializer
implements JsonSerializer<Packet> {
    private final HashMap<Class<? extends Packet>, String> packetRegistry = new HashMap();

    public final void registerPacket(String packetName, Class<? extends Packet> packetClass) {
        ((Map)this.packetRegistry).put(packetClass, packetName);
    }

    /*
     * WARNING - void declaration
     */
    public JsonElement serialize(Packet src, Type typeOfSrc, JsonSerializationContext context) {
        boolean bl;
        String string;
        block1: {
            void $this$none$iv;
            String packetName = this.packetRegistry.getOrDefault(src.getClass(), "UNKNOWN");
            Constructor<?>[] constructorArray = src.getClass().getConstructors();
            string = packetName;
            boolean $i$f$none = false;
            void var8_8 = $this$none$iv;
            int n = ((void)var8_8).length;
            for (int i = 0; i < n; ++i) {
                void element$iv;
                void it = element$iv = var8_8[i];
                boolean bl2 = false;
                if (!(it.getParameterCount() != 0)) continue;
                bl = false;
                break block1;
            }
            bl = true;
        }
        boolean bl3 = bl;
        Packet packet = bl3 ? null : src;
        String string2 = string;
        SerializedPacket serializedPacket = new SerializedPacket(string2, packet);
        return new Gson().toJsonTree((Object)serializedPacket);
    }
}

