// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.player;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import com.elementars.eclient.event.EventTarget;
import com.elementars.eclient.Xulu;
import net.minecraft.network.play.server.SPacketDisconnect;
import com.elementars.eclient.event.events.EventReceivePacket;
import net.minecraftforge.fml.common.FMLCommonHandler;
import com.elementars.eclient.module.Category;
import java.util.concurrent.ConcurrentHashMap;
import com.elementars.eclient.module.Module;

public class SelfLogoutSpot extends Module
{
    public ConcurrentHashMap<String, String> logoutMap;
    public static SelfLogoutSpot INSTANCE;
    
    public SelfLogoutSpot() {
        super("SelfLogoutSpot", "Saves your logout spot in case you forget", 0, Category.PLAYER, true);
        this.logoutMap = new ConcurrentHashMap<String, String>();
        SelfLogoutSpot.INSTANCE = this;
    }
    
    @Override
    public void onEnable() {
        FMLCommonHandler.instance().bus().register((Object)this);
    }
    
    @Override
    public void onDisable() {
        FMLCommonHandler.instance().bus().unregister((Object)this);
    }
    
    @EventTarget
    public void onPacket(final EventReceivePacket event) {
        if (event.getPacket() instanceof SPacketDisconnect && SelfLogoutSpot.mc.getCurrentServerData() != null && SelfLogoutSpot.mc.player != null) {
            this.logoutMap.put(SelfLogoutSpot.mc.getCurrentServerData().serverIP, "X: " + Xulu.df.format(SelfLogoutSpot.mc.player.posX) + ", Y: " + Xulu.df.format(SelfLogoutSpot.mc.player.posY) + ", Z: " + Xulu.df.format(SelfLogoutSpot.mc.player.posZ));
        }
    }
    
    @SubscribeEvent
    public void onDisconnect(final FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        if (SelfLogoutSpot.mc.getCurrentServerData() != null && SelfLogoutSpot.mc.player != null) {
            this.logoutMap.put(SelfLogoutSpot.mc.getCurrentServerData().serverIP, "X: " + Xulu.df.format(SelfLogoutSpot.mc.player.posX) + ", Y: " + Xulu.df.format(SelfLogoutSpot.mc.player.posY) + ", Z: " + Xulu.df.format(SelfLogoutSpot.mc.player.posZ));
        }
    }
}
