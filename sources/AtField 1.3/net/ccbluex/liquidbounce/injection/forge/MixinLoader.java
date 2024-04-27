/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.relauncher.IFMLLoadingPlugin
 *  org.spongepowered.asm.launch.MixinBootstrap
 *  org.spongepowered.asm.mixin.MixinEnvironment
 *  org.spongepowered.asm.mixin.MixinEnvironment$Side
 *  org.spongepowered.asm.mixin.Mixins
 */
package net.ccbluex.liquidbounce.injection.forge;

import java.util.Map;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

public class MixinLoader
implements IFMLLoadingPlugin {
    public void injectData(Map map) {
    }

    public String getSetupClass() {
        return null;
    }

    public String[] getASMTransformerClass() {
        return new String[0];
    }

    public String getModContainerClass() {
        return null;
    }

    public String getAccessTransformerClass() {
        return null;
    }

    public MixinLoader() {
        MixinBootstrap.init();
        Mixins.addConfiguration((String)"liquidbounce.forge.mixins.json");
        MixinEnvironment.getDefaultEnvironment().setSide(MixinEnvironment.Side.CLIENT);
    }
}

