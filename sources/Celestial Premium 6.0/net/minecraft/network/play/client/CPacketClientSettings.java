/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.EnumHandSide;

public class CPacketClientSettings
implements Packet<INetHandlerPlayServer> {
    private String lang;
    private int view;
    private EntityPlayer.EnumChatVisibility chatVisibility;
    private boolean enableColors;
    private int modelPartFlags;
    private EnumHandSide mainHand;

    public CPacketClientSettings() {
    }

    public CPacketClientSettings(String langIn, int renderDistanceIn, EntityPlayer.EnumChatVisibility chatVisibilityIn, boolean chatColorsIn, int modelPartsIn, EnumHandSide mainHandIn) {
        this.lang = langIn;
        this.view = renderDistanceIn;
        this.chatVisibility = chatVisibilityIn;
        this.enableColors = chatColorsIn;
        this.modelPartFlags = modelPartsIn;
        this.mainHand = mainHandIn;
    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.lang = buf.readStringFromBuffer(16);
        this.view = buf.readByte();
        this.chatVisibility = buf.readEnumValue(EntityPlayer.EnumChatVisibility.class);
        this.enableColors = buf.readBoolean();
        this.modelPartFlags = buf.readUnsignedByte();
        this.mainHand = buf.readEnumValue(EnumHandSide.class);
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeString(this.lang);
        buf.writeByte(this.view);
        buf.writeEnumValue(this.chatVisibility);
        buf.writeBoolean(this.enableColors);
        buf.writeByte(this.modelPartFlags);
        buf.writeEnumValue(this.mainHand);
    }

    @Override
    public void processPacket(INetHandlerPlayServer handler) {
        handler.processClientSettings(this);
    }

    public String getLang() {
        return this.lang;
    }

    public EntityPlayer.EnumChatVisibility getChatVisibility() {
        return this.chatVisibility;
    }

    public boolean isColorsEnabled() {
        return this.enableColors;
    }

    public int getModelPartFlags() {
        return this.modelPartFlags;
    }

    public EnumHandSide getMainHand() {
        return this.mainHand;
    }
}

