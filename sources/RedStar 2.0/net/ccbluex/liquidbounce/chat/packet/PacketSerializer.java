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

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000>\n\n\n\n\b\n\n\n\n\n\u0000\n\n\b\n\n\b\n\n\u0000\n\n\u0000\u00002\b00B¢J\t0\n202\f\n\b00J \r0202020HR:.\f\n\b0000j\f\n\b000`\bX¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/chat/packet/PacketSerializer;", "Lcom/google/gson/JsonSerializer;", "Lnet/ccbluex/liquidbounce/chat/packet/packets/Packet;", "()V", "packetRegistry", "Ljava/util/HashMap;", "Ljava/lang/Class;", "", "Lkotlin/collections/HashMap;", "registerPacket", "", "packetName", "packetClass", "serialize", "Lcom/google/gson/JsonElement;", "src", "typeOfSrc", "Ljava/lang/reflect/Type;", "context", "Lcom/google/gson/JsonSerializationContext;", "Pride"})
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
            Intrinsics.checkExpressionValueIsNotNull(string2, "packetRegistry.getOrDefa…src.javaClass, \"UNKNOWN\")");
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
