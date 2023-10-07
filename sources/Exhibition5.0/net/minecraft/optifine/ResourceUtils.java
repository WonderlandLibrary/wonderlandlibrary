// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.optifine;

import java.io.File;
import net.minecraft.client.resources.AbstractResourcePack;

public class ResourceUtils
{
    private static ReflectorClass ForgeAbstractResourcePack;
    private static ReflectorField ForgeAbstractResourcePack_resourcePackFile;
    private static boolean directAccessValid;
    
    public static File getResourcePackFile(final AbstractResourcePack arp) {
        if (ResourceUtils.directAccessValid) {
            try {
                return arp.resourcePackFile;
            }
            catch (IllegalAccessError var2) {
                ResourceUtils.directAccessValid = false;
                if (!ResourceUtils.ForgeAbstractResourcePack_resourcePackFile.exists()) {
                    throw var2;
                }
            }
        }
        return (File)Reflector.getFieldValue(arp, ResourceUtils.ForgeAbstractResourcePack_resourcePackFile);
    }
    
    static {
        ResourceUtils.ForgeAbstractResourcePack = new ReflectorClass(AbstractResourcePack.class);
        ResourceUtils.ForgeAbstractResourcePack_resourcePackFile = new ReflectorField(ResourceUtils.ForgeAbstractResourcePack, "resourcePackFile");
        ResourceUtils.directAccessValid = true;
    }
}
