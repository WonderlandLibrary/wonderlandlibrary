package me.crimp.claudius.mixins;

import me.crimp.claudius.claudius;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.util.Map;

public class CLoader implements IFMLLoadingPlugin {
    private static boolean isObfuscatedEnvironment = false;

    public CLoader() {
        claudius.LOGGER.info("\n\nLoading mixins by Crimp");
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.claudius.json");
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
        claudius.LOGGER.info(MixinEnvironment.getDefaultEnvironment().getObfuscationContext());
    }

    public String[] getASMTransformerClass() {
        return new String[0];
    }

    public String getModContainerClass() {
        return null;
    }

    public String getSetupClass() {
        return null;
    }

    public void injectData(Map<String, Object> data) {
        isObfuscatedEnvironment = (Boolean) data.get("runtimeDeobfuscationEnabled");
    }

    public String getAccessTransformerClass() {
        return null;
    }
}

