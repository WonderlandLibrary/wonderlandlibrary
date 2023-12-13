// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.misc;

import net.lenni0451.eventapi.reflection.EventTarget;
import java.util.Iterator;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.augustus.utils.ChatUtil;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.augustus.events.EventReadPacket;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.minecraft.client.multiplayer.WorldClient;
import net.augustus.settings.BooleanValue;
import java.util.UUID;
import java.util.ArrayList;
import net.augustus.modules.Module;

public class StaffDetector extends Module
{
    private final ArrayList<UUID> playersInRound;
    public BooleanValue isInRound;
    private WorldClient worldClient;
    
    public StaffDetector() {
        super("StaffDetector", new Color(80, 162, 181), Categorys.MISC);
        this.playersInRound = new ArrayList<UUID>();
        this.isInRound = new BooleanValue(1, "IsInRound", this, true);
    }
    
    @Override
    public void onEnable() {
        if (StaffDetector.mc.theWorld != null) {
            this.worldClient = StaffDetector.mc.theWorld;
            this.playersInRound.clear();
        }
    }
    
    @EventTarget
    public void onEventReadPacket(final EventReadPacket eventReadPacket) {
        final Packet packet = eventReadPacket.getPacket();
        if (packet instanceof S38PacketPlayerListItem) {
            final S38PacketPlayerListItem playerListItem = (S38PacketPlayerListItem)packet;
            switch (playerListItem.getAction()) {
                case UPDATE_LATENCY: {
                    if (!this.isInRound.getBoolean()) {
                        this.playersInRound.clear();
                    }
                    for (final S38PacketPlayerListItem.AddPlayerData addPlayerData : playerListItem.getPlayers()) {
                        if (!StaffDetector.mc.getNetHandler().getPlayerMapInfo().containsKey(addPlayerData.getProfile().getId()) && !this.playersInRound.contains(addPlayerData.getProfile().getId())) {
                            ChatUtil.sendChat("§4Staff detectecd:§6 " + addPlayerData.getProfile().getName());
                        }
                    }
                    break;
                }
                case ADD_PLAYER: {
                    for (final S38PacketPlayerListItem.AddPlayerData addPlayerData : playerListItem.getPlayers()) {
                        if (!this.playersInRound.contains(addPlayerData.getProfile().getId()) && addPlayerData.getDisplayName() == null) {
                            this.playersInRound.add(addPlayerData.getProfile().getId());
                        }
                    }
                    break;
                }
            }
        }
        if (packet instanceof S08PacketPlayerPosLook) {
            if (this.worldClient != null && this.worldClient != StaffDetector.mc.theWorld) {
                this.playersInRound.clear();
            }
            this.worldClient = StaffDetector.mc.theWorld;
        }
    }
}
