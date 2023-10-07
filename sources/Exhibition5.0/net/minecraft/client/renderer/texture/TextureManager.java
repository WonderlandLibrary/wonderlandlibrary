// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.texture;

import org.apache.logging.log4j.LogManager;
import org.lwjgl.opengl.GL11;
import java.util.Iterator;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import java.io.IOException;
import net.minecraft.optifine.RandomMobs;
import net.minecraft.optifine.Config;
import net.minecraft.util.ResourceLocation;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.resources.IResourceManager;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.resources.IResourceManagerReloadListener;

public class TextureManager implements ITickable, IResourceManagerReloadListener
{
    private static final Logger logger;
    private final Map mapTextureObjects;
    private final List listTickables;
    private final Map mapTextureCounters;
    private IResourceManager theResourceManager;
    private static final String __OBFID = "CL_00001064";
    
    public TextureManager(final IResourceManager p_i1284_1_) {
        this.mapTextureObjects = Maps.newHashMap();
        this.listTickables = Lists.newArrayList();
        this.mapTextureCounters = Maps.newHashMap();
        this.theResourceManager = p_i1284_1_;
    }
    
    public void bindTexture(ResourceLocation resource) {
        if (Config.isRandomMobs()) {
            resource = RandomMobs.getTextureLocation(resource);
        }
        Object var2 = this.mapTextureObjects.get(resource);
        if (var2 == null) {
            var2 = new SimpleTexture(resource);
            this.loadTexture(resource, (ITextureObject)var2);
        }
        TextureUtil.bindTexture(((ITextureObject)var2).getGlTextureId());
    }
    
    public boolean loadTickableTexture(final ResourceLocation p_110580_1_, final ITickableTextureObject p_110580_2_) {
        if (this.loadTexture(p_110580_1_, p_110580_2_)) {
            this.listTickables.add(p_110580_2_);
            return true;
        }
        return false;
    }
    
    public boolean loadTexture(final ResourceLocation p_110579_1_, final ITextureObject p_110579_2_) {
        boolean var3 = true;
        Object p_110579_2_2 = p_110579_2_;
        try {
            p_110579_2_.loadTexture(this.theResourceManager);
        }
        catch (IOException var4) {
            TextureManager.logger.warn("Failed to load texture: " + p_110579_1_, (Throwable)var4);
            p_110579_2_2 = TextureUtil.missingTexture;
            this.mapTextureObjects.put(p_110579_1_, p_110579_2_2);
            var3 = false;
        }
        catch (Throwable var6) {
            final CrashReport var5 = CrashReport.makeCrashReport(var6, "Registering texture");
            final CrashReportCategory var7 = var5.makeCategory("Resource location being registered");
            var7.addCrashSection("Resource location", p_110579_1_);
            var7.addCrashSectionCallable("Texture object class", new Callable() {
                private static final String __OBFID = "CL_00001065";
                
                @Override
                public String call() {
                    return p_110579_2_.getClass().getName();
                }
            });
            throw new ReportedException(var5);
        }
        this.mapTextureObjects.put(p_110579_1_, p_110579_2_2);
        return var3;
    }
    
    public ITextureObject getTexture(final ResourceLocation p_110581_1_) {
        return this.mapTextureObjects.get(p_110581_1_);
    }
    
    public ResourceLocation getDynamicTextureLocation(final String p_110578_1_, final DynamicTexture p_110578_2_) {
        Integer var3 = this.mapTextureCounters.get(p_110578_1_);
        if (var3 == null) {
            var3 = 1;
        }
        else {
            ++var3;
        }
        this.mapTextureCounters.put(p_110578_1_, var3);
        final ResourceLocation var4 = new ResourceLocation(String.format("dynamic/%s_%d", p_110578_1_, var3));
        this.loadTexture(var4, p_110578_2_);
        return var4;
    }
    
    @Override
    public void tick() {
        for (final ITickable var2 : this.listTickables) {
            var2.tick();
        }
    }
    
    public void deleteTexture(final ResourceLocation p_147645_1_) {
        final ITextureObject var2 = this.getTexture(p_147645_1_);
        if (var2 != null) {
            TextureUtil.deleteTexture(var2.getGlTextureId());
        }
    }
    
    @Override
    public void onResourceManagerReload(final IResourceManager resourceManager) {
        Config.dbg("*** Reloading textures ***");
        Config.log("Resource packs: " + Config.getResourcePackNames());
        final Iterator it = this.mapTextureObjects.keySet().iterator();
        while (it.hasNext()) {
            final ResourceLocation var2 = it.next();
            if (var2.getResourcePath().startsWith("mcpatcher/")) {
                final ITextureObject var3 = this.mapTextureObjects.get(var2);
                final int glTexId = var3.getGlTextureId();
                if (glTexId > 0) {
                    GL11.glDeleteTextures(glTexId);
                }
                it.remove();
            }
        }
        for (final Map.Entry var5 : this.mapTextureObjects.entrySet()) {
            this.loadTexture(var5.getKey(), var5.getValue());
        }
    }
    
    static {
        logger = LogManager.getLogger();
    }
}
