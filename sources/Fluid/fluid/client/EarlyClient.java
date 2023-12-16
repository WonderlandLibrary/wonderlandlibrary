// 
// Decompiled by Procyon v0.6.0
// 

package fluid.client;

public class EarlyClient
{
    public static ReleaseType releasetype;
    public static String display;
    
    static {
        EarlyClient.releasetype = ReleaseType.DEVELOPER;
        EarlyClient.display = "Fluid Client [" + EarlyClient.releasetype.name() + "]";
    }
}
