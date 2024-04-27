/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.relauncher.IFMLLoadingPlugin
 */
package net.ccbluex.liquidbounce.injection.forge;

import java.util.Map;
import net.ccbluex.liquidbounce.injection.transformers.ForgeNetworkTransformer;
import net.ccbluex.liquidbounce.script.remapper.injection.transformers.AbstractJavaLinkerTransformer;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

public class TransformerLoader
implements IFMLLoadingPlugin {
    public String[] getASMTransformerClass() {
        return new String[]{ForgeNetworkTransformer.class.getName(), AbstractJavaLinkerTransformer.class.getName()};
    }

    public String getAccessTransformerClass() {
        return null;
    }

    public String getSetupClass() {
        return null;
    }

    public void injectData(Map map) {
    }

    public String getModContainerClass() {
        return null;
    }
}

