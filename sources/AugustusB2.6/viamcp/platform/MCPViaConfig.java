// 
// Decompiled by Procyon v0.5.36
// 

package viamcp.platform;

import java.util.Arrays;
import java.util.Map;
import java.net.URL;
import java.io.File;
import java.util.List;
import com.viaversion.viaversion.configuration.AbstractViaConfig;

public class MCPViaConfig extends AbstractViaConfig
{
    private static final List<String> UNSUPPORTED;
    
    public MCPViaConfig(final File configFile) {
        super(configFile);
        this.reloadConfig();
    }
    
    @Override
    public URL getDefaultConfigURL() {
        return this.getClass().getClassLoader().getResource("assets/viaversion/config.yml");
    }
    
    @Override
    protected void handleConfig(final Map<String, Object> config) {
    }
    
    @Override
    public List<String> getUnsupportedOptions() {
        return MCPViaConfig.UNSUPPORTED;
    }
    
    @Override
    public boolean isAntiXRay() {
        return false;
    }
    
    @Override
    public boolean isNMSPlayerTicking() {
        return false;
    }
    
    @Override
    public boolean is1_12QuickMoveActionFix() {
        return false;
    }
    
    @Override
    public String getBlockConnectionMethod() {
        return "packet";
    }
    
    @Override
    public boolean is1_9HitboxFix() {
        return false;
    }
    
    @Override
    public boolean is1_14HitboxFix() {
        return false;
    }
    
    static {
        UNSUPPORTED = Arrays.asList("anti-xray-patch", "bungee-ping-interval", "bungee-ping-save", "bungee-servers", "quick-move-action-fix", "nms-player-ticking", "velocity-ping-interval", "velocity-ping-save", "velocity-servers", "blockconnection-method", "change-1_9-hitbox", "change-1_14-hitbox");
    }
}
