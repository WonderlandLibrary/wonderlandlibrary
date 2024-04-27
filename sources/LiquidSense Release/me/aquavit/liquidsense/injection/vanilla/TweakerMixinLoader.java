package me.aquavit.liquidsense.injection.vanilla;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.script.remapper.injection.transformers.AbstractJavaLinkerTransformer;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TweakerMixinLoader implements ITweaker {

    private final List<String> list = new ArrayList<>();

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        list.addAll(args);

        if(!args.contains("--version") && profile != null) {
            list.add("--version");
            list.add(profile);
        }

        if(!args.contains("--assetDir") && assetsDir != null) {
            list.add("--assetDir");
            list.add(assetsDir.getAbsolutePath());
        }

        if(!args.contains("--gameDir") && gameDir != null) {
            list.add("--gameDir");
            list.add(gameDir.getAbsolutePath());
        }
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        System.out.println("["+LiquidSense.CLIENT_NAME+"]"+" Injecting with TweakerMixinLoader.");

        MixinBootstrap.init();
        Mixins.addConfiguration("liquidsense.forge.mixins.json");
        MixinEnvironment.getDefaultEnvironment().setSide(MixinEnvironment.Side.CLIENT);

        classLoader.registerTransformer(AbstractJavaLinkerTransformer.class.getName());
    }

    @Override
    public String getLaunchTarget() {
        return "net.minecraft.client.main.Main";
    }

    @Override
    public String[] getLaunchArguments() {
        return this.list.toArray(new String[0]);
    }
}
