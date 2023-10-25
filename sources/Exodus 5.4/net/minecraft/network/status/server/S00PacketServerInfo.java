/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.TypeAdapterFactory
 */
package net.minecraft.network.status.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.network.status.INetHandlerStatusClient;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumTypeAdapterFactory;
import net.minecraft.util.IChatComponent;

public class S00PacketServerInfo
implements Packet<INetHandlerStatusClient> {
    private ServerStatusResponse response;
    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(ServerStatusResponse.MinecraftProtocolVersionIdentifier.class, (Object)new ServerStatusResponse.MinecraftProtocolVersionIdentifier.Serializer()).registerTypeAdapter(ServerStatusResponse.PlayerCountData.class, (Object)new ServerStatusResponse.PlayerCountData.Serializer()).registerTypeAdapter(ServerStatusResponse.class, (Object)new ServerStatusResponse.Serializer()).registerTypeHierarchyAdapter(IChatComponent.class, (Object)new IChatComponent.Serializer()).registerTypeHierarchyAdapter(ChatStyle.class, (Object)new ChatStyle.Serializer()).registerTypeAdapterFactory((TypeAdapterFactory)new EnumTypeAdapterFactory()).create();

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.response = (ServerStatusResponse)GSON.fromJson(packetBuffer.readStringFromBuffer(Short.MAX_VALUE), ServerStatusResponse.class);
    }

    public S00PacketServerInfo() {
    }

    public S00PacketServerInfo(ServerStatusResponse serverStatusResponse) {
        this.response = serverStatusResponse;
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeString(GSON.toJson((Object)this.response));
    }

    @Override
    public void processPacket(INetHandlerStatusClient iNetHandlerStatusClient) {
        iNetHandlerStatusClient.handleServerInfo(this);
    }

    public ServerStatusResponse getResponse() {
        return this.response;
    }
}

