// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.viamcp.platform;

import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.api.platform.ViaInjector;

public class MCPViaInjector implements ViaInjector
{
    public void inject() {
    }
    
    public void uninject() {
    }
    
    public int getServerProtocolVersion() {
        return 47;
    }
    
    public String getEncoderName() {
        return "via-encoder";
    }
    
    public String getDecoderName() {
        return "via-decoder";
    }
    
    public JsonObject getDump() {
        return new JsonObject();
    }
}
