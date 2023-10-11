package com.initial.modules.impl.combat;

import com.initial.settings.impl.*;
import com.initial.modules.*;
import com.initial.settings.*;
import net.minecraft.network.play.server.*;
import com.initial.events.*;
import net.minecraft.entity.player.*;
import com.initial.events.impl.*;
import net.minecraft.entity.*;
import java.util.*;

public class AntiBot extends Module
{
    public static List<EntityLivingBase> bots;
    public ModeSet mode;
    
    public AntiBot() {
        super("AntiBot", 0, Category.COMBAT);
        this.mode = new ModeSet("Mode", "Advanced", new String[] { "Advanced", "Watchdog" });
        this.addSettings(this.mode);
    }
    
    @EventTarget
    public void onReceivePacket(final EventReceivePacket event) {
        final String mode = this.mode.getMode();
        switch (mode) {
            case "Advanced": {
                if (!(event.getPacket() instanceof S0CPacketSpawnPlayer)) {
                    break;
                }
                this.setDisplayName("Anti Bot §7Advanced");
                final S0CPacketSpawnPlayer packet = (S0CPacketSpawnPlayer)event.getPacket();
                final double posX = packet.getX() / 32.0;
                final double posY = packet.getY() / 32.0;
                final double posZ = packet.getZ() / 32.0;
                final double diffX = this.mc.thePlayer.posX - posX;
                final double diffY = this.mc.thePlayer.posY - posY;
                final double diffZ = this.mc.thePlayer.posZ - posZ;
                final double dist = Math.sqrt(diffX * diffX + diffY * diffY + diffZ * diffZ);
                if (dist <= 17.0 && posX != this.mc.thePlayer.posX && posY != this.mc.thePlayer.posY && posZ != this.mc.thePlayer.posZ) {
                    event.setCancelled(true);
                    break;
                }
                break;
            }
        }
    }
    
    public static boolean isBot(final EntityPlayer ep) {
        return AntiBot.bots.contains(ep);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        final String mode = this.mode.getMode();
        switch (mode) {
            case "Watchdog": {
                this.setDisplayName("Anti Bot §7Watchdog");
                for (final Object entity : this.mc.theWorld.loadedEntityList) {
                    if (((Entity)entity).isInvisible() && entity != this.mc.thePlayer) {
                        this.mc.theWorld.removeEntity((Entity)entity);
                    }
                }
                break;
            }
        }
    }
    
    static {
        AntiBot.bots = new ArrayList<EntityLivingBase>();
    }
}
