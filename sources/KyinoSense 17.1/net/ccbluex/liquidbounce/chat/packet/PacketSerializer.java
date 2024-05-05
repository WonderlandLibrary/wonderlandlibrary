/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonSerializationContext
 *  com.google.gson.JsonSerializer
 *  org.jetbrains.annotations.NotNull
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
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.chat.packet.SerializedPacket;
import net.ccbluex.liquidbounce.chat.packet.packets.Packet;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0003J\u001e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00072\u000e\u0010\f\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00020\u0006J \u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0016R:\u0010\u0004\u001a.\u0012\f\u0012\n\u0012\u0006\b\u0001\u0012\u00020\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005j\u0016\u0012\f\u0012\n\u0012\u0006\b\u0001\u0012\u00020\u00020\u0006\u0012\u0004\u0012\u00020\u0007`\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2={"Lnet/ccbluex/liquidbounce/chat/packet/PacketSerializer;", "Lcom/google/gson/JsonSerializer;", "Lnet/ccbluex/liquidbounce/chat/packet/packets/Packet;", "()V", "packetRegistry", "Ljava/util/HashMap;", "Ljava/lang/Class;", "", "Lkotlin/collections/HashMap;", "registerPacket", "", "packetName", "packetClass", "serialize", "Lcom/google/gson/JsonElement;", "src", "typeOfSrc", "Ljava/lang/reflect/Type;", "context", "Lcom/google/gson/JsonSerializationContext;", "KyinoClient"})
public final class PacketSerializer
implements JsonSerializer<Packet> {
    private final HashMap<Class<? extends Packet>, String> packetRegistry = new HashMap();

    public final void registerPacket(@NotNull String packetName, @NotNull Class<? extends Packet> packetClass) {
        Intrinsics.checkParameterIsNotNull(packetName, "packetName");
        Intrinsics.checkParameterIsNotNull(packetClass, "packetClass");
        ((Map)this.packetRegistry).put(packetClass, packetName);
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public JsonElement serialize(@NotNull Packet src, @NotNull Type typeOfSrc, @NotNull JsonSerializationContext context) {
        boolean bl;
        String string;
        block1: {
            void $this$none$iv;
            Intrinsics.checkParameterIsNotNull(src, "src");
            Intrinsics.checkParameterIsNotNull(typeOfSrc, "typeOfSrc");
            Intrinsics.checkParameterIsNotNull(context, "context");
            String string2 = this.packetRegistry.getOrDefault(src.getClass(), "UNKNOWN");
            Intrinsics.checkExpressionValueIsNotNull(string2, "packetRegistry.getOrDefa\u2026src.javaClass, \"UNKNOWN\")");
            String packetName = string2;
            Constructor<?>[] constructorArray = src.getClass().getConstructors();
            Intrinsics.checkExpressionValueIsNotNull(constructorArray, "src.javaClass.constructors");
            Constructor<?>[] constructorArray2 = constructorArray;
            string = packetName;
            boolean $i$f$none = false;
            void var8_8 = $this$none$iv;
            int n = ((void)var8_8).length;
            for (int i = 0; i < n; ++i) {
                void element$iv;
                void it = element$iv = var8_8[i];
                boolean bl2 = false;
                void v2 = it;
                Intrinsics.checkExpressionValueIsNotNull(v2, "it");
                if (!(v2.getParameterCount() != 0)) continue;
                bl = false;
                break block1;
            }
            bl = true;
        }
        boolean bl3 = bl;
        Packet packet = bl3 ? null : src;
        String string3 = string;
        SerializedPacket serializedPacket = new SerializedPacket(string3, packet);
        JsonElement jsonElement = new Gson().toJsonTree((Object)serializedPacket);
        Intrinsics.checkExpressionValueIsNotNull(jsonElement, "Gson().toJsonTree(serializedPacket)");
        return jsonElement;
    }
}

