package com.masterof13fps.manager.particlemanager;

import com.masterof13fps.manager.particlemanager.handler.FBPEventHandler;
import com.masterof13fps.manager.particlemanager.particle.FBPParticleManager;
import net.minecraft.util.*;
import java.io.*;
import net.minecraft.block.material.*;
import net.minecraft.client.renderer.vertex.*;
import java.util.*;
import java.lang.invoke.*;
import net.minecraft.client.particle.*;
import com.google.common.base.*;
import net.minecraft.client.*;
import net.minecraft.block.*;
import net.minecraft.init.*;
import org.apache.commons.lang3.StringUtils;

public class FBP
{
    public static final String MODID = "fbp";
    public static final ResourceLocation LOCATION_PARTICLE_TEXTURE;
    public static final ResourceLocation FBP_BUG;
    public static final ResourceLocation FBP_FBP;
    public static final ResourceLocation FBP_WIDGETS;
    public static final Vec3[] CUBE;
    public static final Vec3[] CUBE_NORMALS;
    public static FBP INSTANCE;
    public static File particleBlacklistFile;
    public static File floatingMaterialsFile;
    public static File config;
    public static int minAge;
    public static int maxAge;
    public static int particlesPerAxis;
    public static double scaleMult;
    public static double gravityMult;
    public static double rotationMult;
    public static double weatherParticleDensity;
    public static boolean enabled;
    public static boolean showInMillis;
    public static boolean infiniteDuration;
    public static boolean randomRotation;
    public static boolean cartoonMode;
    public static boolean spawnWhileFrozen;
    public static boolean spawnRedstoneBlockParticles;
    public static boolean randomizedScale;
    public static boolean randomFadingSpeed;
    public static boolean entityCollision;
    public static boolean bounceOffWalls;
    public static boolean lowTraction;
    public static boolean smartBreaking;
    public static boolean fancyRain;
    public static boolean fancySnow;
    public static boolean fancyFlame;
    public static boolean fancySmoke;
    public static boolean waterPhysics;
    public static boolean restOnFloor;
    public static boolean frozen;
    public static SplittableRandom random;
    public static VertexFormat POSITION_TEX_COLOR_LMAP_NORMAL;
    public static MethodHandle setSourcePos;
    public static FBPParticleManager fancyEffectRenderer;
    public static EffectRenderer originalEffectRenderer;
    public List<String> blockParticleBlacklist;
    public List<Material> floatingMaterials;
    public FBPEventHandler eventHandler;

    public FBP() {
        this.eventHandler = new FBPEventHandler();
        FBP.INSTANCE = this;
        (FBP.POSITION_TEX_COLOR_LMAP_NORMAL = new VertexFormat()).func_181721_a(DefaultVertexFormats.POSITION_3F);
        FBP.POSITION_TEX_COLOR_LMAP_NORMAL.func_181721_a(DefaultVertexFormats.TEX_2F);
        FBP.POSITION_TEX_COLOR_LMAP_NORMAL.func_181721_a(DefaultVertexFormats.COLOR_4UB);
        FBP.POSITION_TEX_COLOR_LMAP_NORMAL.func_181721_a(DefaultVertexFormats.TEX_2S);
        FBP.POSITION_TEX_COLOR_LMAP_NORMAL.func_181721_a(DefaultVertexFormats.NORMAL_3B);
        this.blockParticleBlacklist = Collections.synchronizedList(new ArrayList<String>());
        this.floatingMaterials = Collections.synchronizedList(new ArrayList<Material>());
    }

    public void onStart() {
        final MethodHandles.Lookup lookup = MethodHandles.publicLookup();
        try {
            FBP.setSourcePos = lookup.unreflectSetter(ReflectionHelper.findField(EntityDiggingFX.class, "sourcePos", "sourcePos"));
        }
        catch (Exception e) {
            throw Throwables.propagate((Throwable)e);
        }
        this.syncWithModule();
    }

    public static boolean isEnabled() {
        if (!FBP.enabled) {
            FBP.frozen = false;
        }
        return FBP.enabled;
    }

    {
        if (FBP.enabled != enabled)
        {
            if (enabled)
            {
                FBP.fancyEffectRenderer.carryOver();

                Minecraft.mc().effectRenderer = FBP.fancyEffectRenderer;
//				Minecraft.getMinecraft().world.provider.setWeatherRenderer(FBP.fancyWeatherRenderer);
            } else
            {
                Minecraft.mc().effectRenderer = FBP.originalEffectRenderer;
//				Minecraft.getMinecraft().world.provider.setWeatherRenderer(FBP.originalWeatherRenderer);
            }
        }
        FBP.enabled = enabled;
    }


    public boolean isBlacklisted(final Block b) {
        return b == null || this.blockParticleBlacklist.contains(b.getLocalizedName().toString());
    }

    public boolean doesMaterialFloat(final Material mat) {
        return this.floatingMaterials.contains(mat);
    }

    public void addToBlacklist(final String name) {
        if (StringUtils.isEmpty((CharSequence)name)) {
            return;
        }
        final Block b = Block.getBlockFromName(name);
        if (b == null || b == Blocks.redstone_block) {
            return;
        }
        this.addToBlacklist(b);
    }

    public void addToBlacklist(final Block b) {
        if (b == null) {
            return;
        }
        final String name = b.getLocalizedName().toString();
        if (!this.blockParticleBlacklist.contains(name)) {
            this.blockParticleBlacklist.add(name);
        }
    }

    public void removeFromBlacklist(final Block b) {
        if (b == null) {
            return;
        }
        final String name = b.getLocalizedName().toString();
        if (this.blockParticleBlacklist.contains(name)) {
            this.blockParticleBlacklist.remove(name);
        }
    }

    public void syncWithModule() {
        FBP.minAge = 10;
        FBP.maxAge = 50;
        FBP.infiniteDuration = false;
        FBP.particlesPerAxis = 4;
        FBP.scaleMult = 0.7;
        FBP.gravityMult = 1.0;
        FBP.rotationMult = 1.0;
        FBP.randomRotation = true;
        FBP.cartoonMode = false;
        FBP.randomizedScale = true;
        FBP.randomFadingSpeed = true;
        FBP.spawnRedstoneBlockParticles = true;
        FBP.entityCollision = false;
        FBP.bounceOffWalls = true;
        FBP.lowTraction = false;
        FBP.smartBreaking = true;
        FBP.fancyFlame = true;
        FBP.fancySmoke = true;
        FBP.waterPhysics = true;
        FBP.restOnFloor = true;
    }

    public void resetBlacklist() {
        this.blockParticleBlacklist.clear();
    }

    static {
        LOCATION_PARTICLE_TEXTURE = new ResourceLocation("textures/particle/particles.png");
        FBP_BUG = new ResourceLocation("fbp:textures/gui/bug.png");
        FBP_FBP = new ResourceLocation("fbp:textures/gui/fbp.png");
        FBP_WIDGETS = new ResourceLocation("fbp:textures/gui/widgets.png");
        CUBE = new Vec3[] { new Vec3(1.0, 1.0, -1.0), new Vec3(1.0, 1.0, 1.0), new Vec3(-1.0, 1.0, 1.0), new Vec3(-1.0, 1.0, -1.0), new Vec3(-1.0, -1.0, -1.0), new Vec3(-1.0, -1.0, 1.0), new Vec3(1.0, -1.0, 1.0), new Vec3(1.0, -1.0, -1.0), new Vec3(-1.0, -1.0, 1.0), new Vec3(-1.0, 1.0, 1.0), new Vec3(1.0, 1.0, 1.0), new Vec3(1.0, -1.0, 1.0), new Vec3(1.0, -1.0, -1.0), new Vec3(1.0, 1.0, -1.0), new Vec3(-1.0, 1.0, -1.0), new Vec3(-1.0, -1.0, -1.0), new Vec3(-1.0, -1.0, -1.0), new Vec3(-1.0, 1.0, -1.0), new Vec3(-1.0, 1.0, 1.0), new Vec3(-1.0, -1.0, 1.0), new Vec3(1.0, -1.0, 1.0), new Vec3(1.0, 1.0, 1.0), new Vec3(1.0, 1.0, -1.0), new Vec3(1.0, -1.0, -1.0) };
        CUBE_NORMALS = new Vec3[] { new Vec3(0.0, 1.0, 0.0), new Vec3(0.0, -1.0, 0.0), new Vec3(0.0, 0.0, 1.0), new Vec3(0.0, 0.0, -1.0), new Vec3(-1.0, 0.0, 0.0), new Vec3(1.0, 0.0, 0.0) };
        FBP.particleBlacklistFile = null;
        FBP.floatingMaterialsFile = null;
        FBP.config = null;
        FBP.enabled = true;
        FBP.showInMillis = false;
        FBP.infiniteDuration = false;
        FBP.random = new SplittableRandom();
    }
}
