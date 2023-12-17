// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.advancements.Advancement;
import javax.annotation.Nullable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class CPacketSeenAdvancements implements Packet<INetHandlerPlayServer>
{
    private Action action;
    private ResourceLocation tab;
    
    public CPacketSeenAdvancements() {
    }
    
    public CPacketSeenAdvancements(final Action p_i47595_1_, @Nullable final ResourceLocation p_i47595_2_) {
        this.action = p_i47595_1_;
        this.tab = p_i47595_2_;
    }
    
    public static CPacketSeenAdvancements openedTab(final Advancement p_194163_0_) {
        return new CPacketSeenAdvancements(Action.OPENED_TAB, p_194163_0_.getId());
    }
    
    public static CPacketSeenAdvancements closedScreen() {
        return new CPacketSeenAdvancements(Action.CLOSED_SCREEN, null);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.action = buf.readEnumValue(Action.class);
        if (this.action == Action.OPENED_TAB) {
            this.tab = buf.readResourceLocation();
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeEnumValue(this.action);
        if (this.action == Action.OPENED_TAB) {
            buf.writeResourceLocation(this.tab);
        }
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.handleSeenAdvancements(this);
    }
    
    public Action getAction() {
        return this.action;
    }
    
    public ResourceLocation getTab() {
        return this.tab;
    }
    
    public enum Action
    {
        OPENED_TAB, 
        CLOSED_SCREEN;
    }
}
