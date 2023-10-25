/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C15PacketClientSettings
implements Packet<INetHandlerPlayServer> {
    private int modelPartFlags;
    private boolean enableColors;
    private EntityPlayer.EnumChatVisibility chatVisibility;
    private int view;
    private String lang;

    @Override
    public void processPacket(INetHandlerPlayServer iNetHandlerPlayServer) {
        iNetHandlerPlayServer.processClientSettings(this);
    }

    public EntityPlayer.EnumChatVisibility getChatVisibility() {
        return this.chatVisibility;
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeString(this.lang);
        packetBuffer.writeByte(this.view);
        packetBuffer.writeByte(this.chatVisibility.getChatVisibility());
        packetBuffer.writeBoolean(this.enableColors);
        packetBuffer.writeByte(this.modelPartFlags);
    }

    public C15PacketClientSettings() {
    }

    public C15PacketClientSettings(String string, int n, EntityPlayer.EnumChatVisibility enumChatVisibility, boolean bl, int n2) {
        this.lang = string;
        this.view = n;
        this.chatVisibility = enumChatVisibility;
        this.enableColors = bl;
        this.modelPartFlags = n2;
    }

    public int getModelPartFlags() {
        return this.modelPartFlags;
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.lang = packetBuffer.readStringFromBuffer(7);
        this.view = packetBuffer.readByte();
        this.chatVisibility = EntityPlayer.EnumChatVisibility.getEnumChatVisibility(packetBuffer.readByte());
        this.enableColors = packetBuffer.readBoolean();
        this.modelPartFlags = packetBuffer.readUnsignedByte();
    }

    public boolean isColorsEnabled() {
        return this.enableColors;
    }

    public String getLang() {
        return this.lang;
    }
}

