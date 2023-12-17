// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.status.server;

import com.google.gson.TypeAdapterFactory;
import net.minecraft.util.EnumTypeAdapterFactory;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.ITextComponent;
import java.lang.reflect.Type;
import com.google.gson.GsonBuilder;
import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.util.JsonUtils;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.ServerStatusResponse;
import com.google.gson.Gson;
import net.minecraft.network.status.INetHandlerStatusClient;
import net.minecraft.network.Packet;

public class SPacketServerInfo implements Packet<INetHandlerStatusClient>
{
    private static final Gson GSON;
    private ServerStatusResponse response;
    
    public SPacketServerInfo() {
    }
    
    public SPacketServerInfo(final ServerStatusResponse responseIn) {
        this.response = responseIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.response = JsonUtils.gsonDeserialize(SPacketServerInfo.GSON, buf.readString(32767), ServerStatusResponse.class);
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeString(SPacketServerInfo.GSON.toJson((Object)this.response));
    }
    
    @Override
    public void processPacket(final INetHandlerStatusClient handler) {
        handler.handleServerInfo(this);
    }
    
    public ServerStatusResponse getResponse() {
        return this.response;
    }
    
    static {
        GSON = new GsonBuilder().registerTypeAdapter((Type)ServerStatusResponse.Version.class, (Object)new ServerStatusResponse.Version.Serializer()).registerTypeAdapter((Type)ServerStatusResponse.Players.class, (Object)new ServerStatusResponse.Players.Serializer()).registerTypeAdapter((Type)ServerStatusResponse.class, (Object)new ServerStatusResponse.Serializer()).registerTypeHierarchyAdapter((Class)ITextComponent.class, (Object)new ITextComponent.Serializer()).registerTypeHierarchyAdapter((Class)Style.class, (Object)new Style.Serializer()).registerTypeAdapterFactory((TypeAdapterFactory)new EnumTypeAdapterFactory()).create();
    }
}
