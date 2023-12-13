// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.combat;

import com.elementars.eclient.command.Command;
import net.minecraft.world.World;
import net.minecraft.network.play.server.SPacketEntityStatus;
import com.elementars.eclient.event.events.EventReceivePacket;
import com.elementars.eclient.event.EventTarget;
import com.elementars.eclient.util.TargetPlayers;
import com.elementars.eclient.event.events.EventTotemPop;
import java.util.Iterator;
import com.elementars.eclient.util.ColorTextUtils;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import net.minecraft.entity.player.EntityPlayer;
import java.util.concurrent.ConcurrentHashMap;
import com.elementars.eclient.module.Module;

public class PopCounter extends Module
{
    public ConcurrentHashMap<EntityPlayer, Integer> popMap;
    private final Value<Boolean> onlyTargets;
    private final Value<Boolean> chat;
    private final Value<Boolean> watermark;
    private final Value<String> color;
    private final Value<String> ncolor;
    public static PopCounter INSTANCE;
    
    public PopCounter() {
        super("PopCounter", "Counts how many times your enemy pops", 0, Category.COMBAT, true);
        this.popMap = new ConcurrentHashMap<EntityPlayer, Integer>();
        this.onlyTargets = this.register(new Value<Boolean>("Only Targets", this, true));
        this.chat = this.register(new Value<Boolean>("Send Message", this, true));
        this.watermark = this.register(new Value<Boolean>("Watermark", this, true));
        this.color = this.register(new Value<String>("Color", this, "White", ColorTextUtils.colors));
        this.ncolor = this.register(new Value<String>("Number Color", this, "White", ColorTextUtils.colors));
        PopCounter.INSTANCE = this;
    }
    
    @Override
    public void onUpdate() {
        for (final EntityPlayer player : PopCounter.mc.world.playerEntities) {
            if (player.getHealth() == 0.0f && this.popMap.containsKey(player)) {
                if (this.chat.getValue()) {
                    this.sendChatMessage(player.getName() + " has died!");
                }
                this.popMap.remove(player);
            }
        }
    }
    
    @EventTarget
    public void onPop(final EventTotemPop event) {
        if (TargetPlayers.targettedplayers.containsKey(event.getPlayer().getName()) || !this.onlyTargets.getValue()) {
            final int pops = this.popMap.getOrDefault(event.getPlayer(), 0) + 1;
            if (this.chat.getValue()) {
                this.sendChatMessage(event.getPlayer().getName() + " has popped " + ColorTextUtils.getColor(this.ncolor.getValue()) + pops + ColorTextUtils.getColor(this.color.getValue()) + " times!");
            }
            this.popMap.put(event.getPlayer(), pops);
        }
    }
    
    @EventTarget
    public void onPacket(final EventReceivePacket event) {
        if (event.getPacket() instanceof SPacketEntityStatus) {
            final SPacketEntityStatus packet = (SPacketEntityStatus)event.getPacket();
            if (packet.getOpCode() == 35 && packet.getEntity((World)PopCounter.mc.world) instanceof EntityPlayer) {
                final EntityPlayer entity = (EntityPlayer)packet.getEntity((World)PopCounter.mc.world);
                final EventTotemPop eventTotemPop = new EventTotemPop(entity);
                eventTotemPop.call();
            }
        }
    }
    
    public void sendChatMessage(final String message) {
        if (this.watermark.getValue()) {
            Command.sendChatMessage(ColorTextUtils.getColor(this.color.getValue()) + message);
        }
        else {
            Command.sendRawChatMessage(ColorTextUtils.getColor(this.color.getValue()) + message);
        }
    }
}
