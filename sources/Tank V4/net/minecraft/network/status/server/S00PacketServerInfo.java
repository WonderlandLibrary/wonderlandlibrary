package net.minecraft.network.status.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.network.status.INetHandlerStatusClient;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumTypeAdapterFactory;
import net.minecraft.util.IChatComponent;

public class S00PacketServerInfo implements Packet {
   private ServerStatusResponse response;
   private static final Gson GSON = (new GsonBuilder()).registerTypeAdapter(ServerStatusResponse.MinecraftProtocolVersionIdentifier.class, new ServerStatusResponse.MinecraftProtocolVersionIdentifier.Serializer()).registerTypeAdapter(ServerStatusResponse.PlayerCountData.class, new ServerStatusResponse.PlayerCountData.Serializer()).registerTypeAdapter(ServerStatusResponse.class, new ServerStatusResponse.Serializer()).registerTypeHierarchyAdapter(IChatComponent.class, new IChatComponent.Serializer()).registerTypeHierarchyAdapter(ChatStyle.class, new ChatStyle.Serializer()).registerTypeAdapterFactory(new EnumTypeAdapterFactory()).create();

   public void processPacket(INetHandlerStatusClient var1) {
      var1.handleServerInfo(this);
   }

   public ServerStatusResponse getResponse() {
      return this.response;
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeString(GSON.toJson((Object)this.response));
   }

   public S00PacketServerInfo(ServerStatusResponse var1) {
      this.response = var1;
   }

   public S00PacketServerInfo() {
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerStatusClient)var1);
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.response = (ServerStatusResponse)GSON.fromJson(var1.readStringFromBuffer(32767), ServerStatusResponse.class);
   }
}
