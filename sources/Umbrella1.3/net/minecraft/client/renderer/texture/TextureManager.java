/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.client.renderer.texture.ITickableTextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import optifine.RandomMobs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shadersmod.client.ShadersTex;

public class TextureManager
implements ITickable,
IResourceManagerReloadListener {
    private static final Logger logger = LogManager.getLogger();
    private final Map mapTextureObjects = Maps.newHashMap();
    private final List listTickables = Lists.newArrayList();
    private final Map mapTextureCounters = Maps.newHashMap();
    private IResourceManager theResourceManager;
    private static final String __OBFID = "CL_00001064";

    public TextureManager(IResourceManager p_i1284_1_) {
        this.theResourceManager = p_i1284_1_;
    }

    public void bindTexture(ResourceLocation resource) {
        ITextureObject var2;
        if (Config.isRandomMobs()) {
            resource = RandomMobs.getTextureLocation(resource);
        }
        if ((var2 = (ITextureObject)this.mapTextureObjects.get(resource)) == null) {
            var2 = new SimpleTexture(resource);
            this.loadTexture(resource, var2);
        }
        if (Config.isShaders()) {
            ShadersTex.bindTexture(var2);
        } else {
            TextureUtil.bindTexture(var2.getGlTextureId());
        }
    }

    public boolean loadTickableTexture(ResourceLocation p_110580_1_, ITickableTextureObject p_110580_2_) {
        if (this.loadTexture(p_110580_1_, p_110580_2_)) {
            this.listTickables.add(p_110580_2_);
            return true;
        }
        return false;
    }

    public boolean loadTexture(ResourceLocation p_110579_1_, final ITextureObject p_110579_2_) {
        boolean var3 = true;
        ITextureObject p_110579_2_2 = p_110579_2_;
        try {
            p_110579_2_.loadTexture(this.theResourceManager);
        }
        catch (IOException var8) {
            logger.warn("Failed to load texture: " + p_110579_1_, (Throwable)var8);
            p_110579_2_2 = TextureUtil.missingTexture;
            this.mapTextureObjects.put(p_110579_1_, p_110579_2_2);
            var3 = false;
        }
        catch (Throwable var9) {
            CrashReport var5 = CrashReport.makeCrashReport(var9, "Registering texture");
            CrashReportCategory var6 = var5.makeCategory("Resource location being registered");
            var6.addCrashSection("Resource location", p_110579_1_);
            var6.addCrashSectionCallable("Texture object class", new Callable(){
                private static final String __OBFID = "CL_00001065";

                public String call() {
                    return p_110579_2_.getClass().getName();
                }
            });
            throw new ReportedException(var5);
        }
        this.mapTextureObjects.put(p_110579_1_, p_110579_2_2);
        return var3;
    }

    public ITextureObject getTexture(ResourceLocation p_110581_1_) {
        return (ITextureObject)this.mapTextureObjects.get(p_110581_1_);
    }

    public ResourceLocation getDynamicTextureLocation(String p_110578_1_, DynamicTexture p_110578_2_) {
        Integer var3;
        if (p_110578_1_.equals("logo")) {
            p_110578_2_ = Config.getMojangLogoTexture(p_110578_2_);
        }
        var3 = (var3 = (Integer)this.mapTextureCounters.get(p_110578_1_)) == null ? Integer.valueOf(1) : Integer.valueOf(var3 + 1);
        this.mapTextureCounters.put(p_110578_1_, var3);
        ResourceLocation var4 = new ResourceLocation(String.format("dynamic/%s_%d", p_110578_1_, var3));
        this.loadTexture(var4, p_110578_2_);
        return var4;
    }

    @Override
    public void tick() {
        for (ITickable var2 : this.listTickables) {
            var2.tick();
        }
    }

    public void deleteTexture(ResourceLocation p_147645_1_) {
        ITextureObject var2 = this.getTexture(p_147645_1_);
        if (var2 != null) {
            TextureUtil.deleteTexture(var2.getGlTextureId());
        }
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        Config.dbg("*** Reloading textures ***");
        Config.log("Resource packs: " + Config.getResourcePackNames());
        Iterator it = this.mapTextureObjects.keySet().iterator();
        while (it.hasNext()) {
            ResourceLocation var2 = (ResourceLocation)it.next();
            if (!var2.getResourcePath().startsWith("mcpatcher/")) continue;
            ITextureObject var3 = (ITextureObject)this.mapTextureObjects.get(var2);
            if (var3 instanceof AbstractTexture) {
                AbstractTexture at = (AbstractTexture)var3;
                at.deleteGlTexture();
            }
            it.remove();
        }
        for (Map.Entry var31 : this.mapTextureObjects.entrySet()) {
            this.loadTexture((ResourceLocation)var31.getKey(), (ITextureObject)var31.getValue());
        }
    }
}

