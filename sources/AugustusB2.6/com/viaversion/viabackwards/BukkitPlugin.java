// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards;

import org.bukkit.plugin.Plugin;
import com.viaversion.viabackwards.listener.FireDamageListener;
import com.viaversion.viabackwards.listener.LecternInteractListener;
import com.viaversion.viabackwards.listener.FireExtinguishListener;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.bukkit.platform.BukkitViaLoader;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.ViaVersionPlugin;
import com.viaversion.viabackwards.api.ViaBackwardsPlatform;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitPlugin extends JavaPlugin implements ViaBackwardsPlatform
{
    public void onLoad() {
        if (!ViaVersionPlugin.getInstance().isLateBind()) {
            this.init();
        }
    }
    
    public void onEnable() {
        if (ViaVersionPlugin.getInstance().isLateBind()) {
            this.init();
        }
    }
    
    private void init() {
        this.init(this.getDataFolder());
        Via.getPlatform().runSync(this::onServerLoaded);
    }
    
    private void onServerLoaded() {
        final BukkitViaLoader loader = (BukkitViaLoader)Via.getManager().getLoader();
        final int protocolVersion = Via.getAPI().getServerVersion().highestSupportedVersion();
        if (protocolVersion >= ProtocolVersion.v1_16.getVersion()) {
            loader.storeListener(new FireExtinguishListener(this)).register();
        }
        if (protocolVersion >= ProtocolVersion.v1_14.getVersion()) {
            loader.storeListener(new LecternInteractListener(this)).register();
        }
        if (protocolVersion >= ProtocolVersion.v1_12.getVersion()) {
            loader.storeListener(new FireDamageListener(this)).register();
        }
    }
    
    public void disable() {
        this.getPluginLoader().disablePlugin((Plugin)this);
    }
}
