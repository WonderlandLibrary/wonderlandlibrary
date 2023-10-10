/*
 * Decompiled with CFR 0.152.
 */
package optfine;

import java.io.File;
import net.minecraft.client.resources.AbstractResourcePack;
import optfine.Reflector;
import optfine.ReflectorClass;
import optfine.ReflectorField;

public class ResourceUtils {
    private static ReflectorClass ForgeAbstractResourcePack = new ReflectorClass(AbstractResourcePack.class);
    private static ReflectorField ForgeAbstractResourcePack_resourcePackFile = new ReflectorField(ForgeAbstractResourcePack, "resourcePackFile");
    private static boolean directAccessValid = true;

    public static File getResourcePackFile(AbstractResourcePack p_getResourcePackFile_0_) {
        block3: {
            if (directAccessValid) {
                try {
                    return p_getResourcePackFile_0_.resourcePackFile;
                }
                catch (IllegalAccessError illegalaccesserror) {
                    directAccessValid = false;
                    if (ForgeAbstractResourcePack_resourcePackFile.exists()) break block3;
                    throw illegalaccesserror;
                }
            }
        }
        return (File)Reflector.getFieldValue(p_getResourcePackFile_0_, ForgeAbstractResourcePack_resourcePackFile);
    }
}

