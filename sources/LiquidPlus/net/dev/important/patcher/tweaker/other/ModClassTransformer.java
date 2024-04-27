/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ArrayListMultimap
 *  com.google.common.collect.Multimap
 *  net.minecraft.launchwrapper.IClassTransformer
 *  net.minecraft.launchwrapper.Launch
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.dev.important.patcher.tweaker.other;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.dev.important.patcher.asm.optifine.GuiIngameForgeTransformer;
import net.dev.important.patcher.asm.optifine.OptiFineHookTransformer;
import net.dev.important.patcher.asm.optifine.OptifineFontRendererTransformer;
import net.dev.important.patcher.asm.optifine.reflectionoptimizations.common.BakedQuadReflectionOptimizer;
import net.dev.important.patcher.asm.optifine.reflectionoptimizations.common.EntityRendererReflectionOptimizer;
import net.dev.important.patcher.asm.optifine.reflectionoptimizations.common.ExtendedBlockStorageReflectionOptimizer;
import net.dev.important.patcher.asm.optifine.reflectionoptimizations.common.FaceBakeryReflectionOptimizer;
import net.dev.important.patcher.asm.optifine.reflectionoptimizations.common.ModelRotationReflectionOptimizer;
import net.dev.important.patcher.optifine.OptiFineGenerations;
import net.dev.important.patcher.tweaker.ClassTransformer;
import net.dev.important.patcher.tweaker.transform.PatcherTransformer;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.MixinEnvironment;

public class ModClassTransformer
implements IClassTransformer {
    private final Logger logger = LogManager.getLogger((String)"Patcher - Mod Class Transformer");
    private final Multimap<String, PatcherTransformer> transformerMap = ArrayListMultimap.create();

    public ModClassTransformer() {
        MixinEnvironment.getCurrentEnvironment().addTransformerExclusion(this.getClass().getName());
        String optifineVersion = ClassTransformer.optifineVersion;
        OptiFineGenerations generations = ClassTransformer.generations;
        if (generations.getIGeneration().contains(optifineVersion)) {
            this.registerCommonTransformers();
        } else if (generations.getLGeneration().contains(optifineVersion)) {
            this.registerCommonTransformers();
        } else if (generations.getMGeneration().contains(optifineVersion) || generations.getFutureGeneration().contains(optifineVersion)) {
            this.registerCommonTransformers();
        } else {
            this.logger.info("User has either an old OptiFine version, or no OptiFine present. Aborting reflection optimizations.");
        }
    }

    private void registerTransformer(PatcherTransformer transformer) {
        for (String cls : transformer.getClassName()) {
            this.transformerMap.put((Object)cls, (Object)transformer);
        }
    }

    public byte[] transform(String name, String transformedName, byte[] bytes) {
        return ClassTransformer.createTransformer(transformedName, bytes, this.transformerMap, this.logger);
    }

    private void registerCommonTransformers() {
        this.registerTransformer(new BakedQuadReflectionOptimizer());
        this.registerTransformer(new FaceBakeryReflectionOptimizer());
        this.registerTransformer(new ModelRotationReflectionOptimizer());
        this.registerTransformer(new ExtendedBlockStorageReflectionOptimizer());
        this.registerTransformer(new EntityRendererReflectionOptimizer());
        this.registerTransformer(new GuiIngameForgeTransformer());
        this.registerTransformer(new OptifineFontRendererTransformer());
        this.registerTransformer(new OptiFineHookTransformer());
    }

    public static boolean isDevelopment() {
        Object o = Launch.blackboard.get("fml.deobfuscatedEnvironment");
        return o != null && (Boolean)o != false;
    }
}

