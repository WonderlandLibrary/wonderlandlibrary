/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.relauncher.IFMLLoadingPlugin
 */
package net.dev.important.injection.forge;

import java.util.Map;
import net.dev.important.injection.transformers.ForgeNetworkTransformer;
import net.dev.important.injection.transformers.OptimizeTransformer;
import net.dev.important.patcher.tweaker.ClassTransformer;
import net.dev.important.patcher.tweaker.other.ModClassTransformer;
import net.dev.important.script.remapper.injection.transformers.AbstractJavaLinkerTransformer;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

public class TransformerLoader
implements IFMLLoadingPlugin {
    public String[] getASMTransformerClass() {
        return new String[]{ForgeNetworkTransformer.class.getName(), ClassTransformer.class.getName(), ModClassTransformer.class.getName(), OptimizeTransformer.class.getName(), AbstractJavaLinkerTransformer.class.getName()};
    }

    public String getModContainerClass() {
        return null;
    }

    public String getSetupClass() {
        return null;
    }

    public void injectData(Map<String, Object> data) {
    }

    public String getAccessTransformerClass() {
        return null;
    }
}

