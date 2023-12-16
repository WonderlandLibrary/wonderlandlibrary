// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.status.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.status.INetHandlerStatusClient;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import com.google.gson.TypeAdapterFactory;
import net.minecraft.util.EnumTypeAdapterFactory;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import java.lang.reflect.Type;
import com.google.gson.GsonBuilder;
import net.minecraft.network.ServerStatusResponse;
import com.google.gson.Gson;
import net.minecraft.network.Packet;

public class S00PacketServerInfo implements Packet
{
    private static final Gson GSON;
    private ServerStatusResponse response;
    private static final String __OBFID = "CL_00001384";
    
    static {
        GSON = new GsonBuilder().registerTypeAdapter(ServerStatusResponse.MinecraftProtocolVersionIdentifier.class, new ServerStatusResponse.MinecraftProtocolVersionIdentifier.Serializer()).registerTypeAdapter(ServerStatusResponse.PlayerCountData.class, new ServerStatusResponse.PlayerCountData.Serializer()).registerTypeAdapter(ServerStatusResponse.class, new ServerStatusResponse.Serializer()).registerTypeHierarchyAdapter(IChatComponent.class, new IChatComponent.Serializer()).registerTypeHierarchyAdapter(ChatStyle.class, new ChatStyle.Serializer()).registerTypeAdapterFactory(new EnumTypeAdapterFactory()).create();
    }
    
    public S00PacketServerInfo() {
    }
    
    public S00PacketServerInfo(final ServerStatusResponse responseIn) {
        this.response = responseIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.response = S00PacketServerInfo.GSON.fromJson(data.readStringFromBuffer(32767), ServerStatusResponse.class);
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeString(S00PacketServerInfo.GSON.toJson(this.response));
    }
    
    public void processPacket(final INetHandlerStatusClient handler) {
        handler.handleServerInfo(this);
    }
    
    public ServerStatusResponse func_149294_c() {
        return this.response;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.processPacket((INetHandlerStatusClient)handler);
    }
}
