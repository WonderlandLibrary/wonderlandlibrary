package net.ccbluex.liquidbounce.chat.packet;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.chat.packet.packets.Packet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000>\n\n\n\n\b\n\n\n\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\n\n\b\u00002\b00B¢J$\t02\n02\f0\r2\b0HJ0202\n\b00R:.0\f\n\b000j0\f\n\b00`\bX¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/chat/packet/PacketDeserializer;", "Lcom/google/gson/JsonDeserializer;", "Lnet/ccbluex/liquidbounce/chat/packet/packets/Packet;", "()V", "packetRegistry", "Ljava/util/HashMap;", "", "Ljava/lang/Class;", "Lkotlin/collections/HashMap;", "deserialize", "json", "Lcom/google/gson/JsonElement;", "typeOfT", "Ljava/lang/reflect/Type;", "context", "Lcom/google/gson/JsonDeserializationContext;", "registerPacket", "", "packetName", "packetClass", "Pride"})
public final class PacketDeserializer
implements JsonDeserializer<Packet> {
    private final HashMap<String, Class<? extends Packet>> packetRegistry = new HashMap();

    public final void registerPacket(@NotNull String packetName, @NotNull Class<? extends Packet> packetClass) {
        Intrinsics.checkParameterIsNotNull(packetName, "packetName");
        Intrinsics.checkParameterIsNotNull(packetClass, "packetClass");
        ((Map)this.packetRegistry).put(packetName, packetClass);
    }

    @Nullable
    public Packet deserialize(@NotNull JsonElement json, @NotNull Type typeOfT, @Nullable JsonDeserializationContext context) {
        Intrinsics.checkParameterIsNotNull(json, "json");
        Intrinsics.checkParameterIsNotNull(typeOfT, "typeOfT");
        JsonObject packetObject = json.getAsJsonObject();
        JsonElement jsonElement = packetObject.get("m");
        Intrinsics.checkExpressionValueIsNotNull(jsonElement, "packetObject.get(\"m\")");
        String packetName = jsonElement.getAsString();
        if (!this.packetRegistry.containsKey(packetName)) {
            return null;
        }
        if (!packetObject.has("c")) {
            packetObject.add("c", (JsonElement)new JsonObject());
        }
        return (Packet)new Gson().fromJson(packetObject.get("c"), this.packetRegistry.get(packetName));
    }
}
