// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render.killeffects;

import dev.tenacity.event.impl.game.WorldEvent;
import net.minecraft.entity.player.EntityPlayer;
import dev.tenacity.event.impl.render.RendererLivingEntityEvent;
import net.minecraft.util.StringUtils;
import dev.tenacity.event.impl.player.ChatReceivedEvent;
import net.minecraft.entity.EntityLivingBase;
import dev.tenacity.event.impl.player.LivingDeathEvent;
import dev.tenacity.module.settings.Setting;
import java.util.concurrent.ConcurrentHashMap;
import dev.tenacity.module.Category;
import java.util.Map;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.Module;

public class KillEffects extends Module
{
    public static final ModeSetting killEffect;
    private final Map<String, Location> players;
    private final EffectManager effectManager;
    
    public KillEffects() {
        super("KillEffects", "Kill Effects", Category.RENDER, "Plays animation on killing another player");
        this.players = new ConcurrentHashMap<String, Location>();
        this.effectManager = new EffectManager();
        this.addSettings(KillEffects.killEffect);
    }
    
    @Override
    public void onLivingDeathEvent(final LivingDeathEvent event) {
        if (event.getSource().getEntity() != null && event.getSource().getEntity().equals(KillEffects.mc.thePlayer) && event.getEntity() != KillEffects.mc.thePlayer) {
            final String name = event.getEntity().getName();
            final Location location = this.players.remove(name);
            if (location != null) {
                this.effectManager.playKillEffect(location);
            }
            else {
                final EntityLivingBase ent = event.getEntity();
                this.effectManager.playKillEffect(new Location(ent.posX, ent.posY, ent.posZ, ent.getEyeHeight()));
            }
        }
    }
    
    @Override
    public void onChatReceivedEvent(final ChatReceivedEvent event) {
        final String message = StringUtils.stripControlCodes(event.message.getUnformattedText());
        if (KillEffects.mc.thePlayer != null && !message.contains(":") && message.contains("by " + KillEffects.mc.thePlayer.getName())) {
            final String killedPlayer = message.trim().split(" ")[0];
            final Location location = this.players.remove(killedPlayer);
            if (location != null) {
                this.effectManager.playKillEffect(location);
            }
        }
    }
    
    @Override
    public void onRendererLivingEntityEvent(final RendererLivingEntityEvent event) {
        if (event.isPost() && event.getEntity() instanceof EntityPlayer && event.getEntity() != KillEffects.mc.thePlayer) {
            final EntityLivingBase ent = event.getEntity();
            this.players.put(ent.getName(), new Location(ent.posX, ent.posY, ent.posZ, ent.getEyeHeight()));
        }
    }
    
    @Override
    public void onWorldEvent(final WorldEvent event) {
        this.players.clear();
    }
    
    static {
        killEffect = new ModeSetting("Kill Effect", "Blood Explosion", new String[] { "Blood Explosion", "Lightning Bolt" });
    }
    
    public static class Location
    {
        public double x;
        public double y;
        public double z;
        public double eyeHeight;
        
        public Location(final double x, final double y, final double z, final double eyeHeight) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.eyeHeight = eyeHeight;
        }
    }
}
