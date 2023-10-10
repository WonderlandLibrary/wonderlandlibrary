// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.sponge.listeners;

import com.viaversion.viaversion.api.Via;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.Sponge;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.SpongePlugin;
import java.lang.reflect.Field;
import com.viaversion.viaversion.ViaListener;

public class ViaSpongeListener extends ViaListener
{
    private static Field entityIdField;
    private final SpongePlugin plugin;
    
    public ViaSpongeListener(final SpongePlugin plugin, final Class<? extends Protocol> requiredPipeline) {
        super(requiredPipeline);
        this.plugin = plugin;
    }
    
    @Override
    public void register() {
        if (this.isRegistered()) {
            return;
        }
        Sponge.getEventManager().registerListeners((Object)this.plugin, (Object)this);
        this.setRegistered(true);
    }
    
    protected int getEntityId(final Player p) {
        try {
            if (ViaSpongeListener.entityIdField == null) {
                (ViaSpongeListener.entityIdField = p.getClass().getSuperclass().getSuperclass().getSuperclass().getDeclaredField("field_145783_c")).setAccessible(true);
            }
            return ViaSpongeListener.entityIdField.getInt(p);
        }
        catch (Exception e) {
            Via.getPlatform().getLogger().severe("Could not get the entity id, please report this on our Github");
            e.printStackTrace();
            Via.getPlatform().getLogger().severe("Could not get the entity id, please report this on our Github");
            return -1;
        }
    }
    
    public SpongePlugin getPlugin() {
        return this.plugin;
    }
}
