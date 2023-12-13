// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.com.viamcp.platform;

import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.api.platform.ViaInjector;

public class VRInjector implements ViaInjector
{
    @Override
    public void inject() {
    }
    
    @Override
    public void uninject() {
    }
    
    @Override
    public int getServerProtocolVersion() {
        return 47;
    }
    
    @Override
    public String getEncoderName() {
        return "via-encoder";
    }
    
    @Override
    public String getDecoderName() {
        return "via-decoder";
    }
    
    @Override
    public JsonObject getDump() {
        final JsonObject obj = new JsonObject();
        return obj;
    }
}
