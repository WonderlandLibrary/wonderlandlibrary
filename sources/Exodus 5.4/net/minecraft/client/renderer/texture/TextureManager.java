/*
 * Decompiled with CFR 0.152.
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TextureManager
implements ITickable,
IResourceManagerReloadListener {
    private final List<ITickable> listTickables;
    private static final Logger logger = LogManager.getLogger();
    private final Map<String, Integer> mapTextureCounters;
    private final Map<ResourceLocation, ITextureObject> mapTextureObjects = Maps.newHashMap();
    private IResourceManager theResourceManager;

    public void deleteTexture(ResourceLocation resourceLocation) {
        ITextureObject iTextureObject = this.getTexture(resourceLocation);
        if (iTextureObject != null) {
            TextureUtil.deleteTexture(iTextureObject.getGlTextureId());
        }
    }

    public boolean loadTickableTexture(ResourceLocation resourceLocation, ITickableTextureObject iTickableTextureObject) {
        if (this.loadTexture(resourceLocation, iTickableTextureObject)) {
            this.listTickables.add(iTickableTextureObject);
            return true;
        }
        return false;
    }

    @Override
    public void onResourceManagerReload(IResourceManager iResourceManager) {
        for (Map.Entry<ResourceLocation, ITextureObject> entry : this.mapTextureObjects.entrySet()) {
            this.loadTexture(entry.getKey(), entry.getValue());
        }
    }

    public ResourceLocation getDynamicTextureLocation(String string, DynamicTexture dynamicTexture) {
        Integer n = this.mapTextureCounters.get(string);
        n = n == null ? Integer.valueOf(1) : Integer.valueOf(n + 1);
        this.mapTextureCounters.put(string, n);
        ResourceLocation resourceLocation = new ResourceLocation(String.format("dynamic/%s_%d", string, n));
        this.loadTexture(resourceLocation, dynamicTexture);
        return resourceLocation;
    }

    @Override
    public void tick() {
        for (ITickable iTickable : this.listTickables) {
            iTickable.tick();
        }
    }

    public ITextureObject getTexture(ResourceLocation resourceLocation) {
        return this.mapTextureObjects.get(resourceLocation);
    }

    public boolean loadTexture(ResourceLocation resourceLocation, ITextureObject iTextureObject) {
        boolean bl = true;
        try {
            iTextureObject.loadTexture(this.theResourceManager);
        }
        catch (IOException iOException) {
            logger.warn("Failed to load texture: " + resourceLocation, (Throwable)iOException);
            iTextureObject = TextureUtil.missingTexture;
            this.mapTextureObjects.put(resourceLocation, iTextureObject);
            bl = false;
        }
        catch (Throwable throwable) {
            final ITextureObject iTextureObject2 = iTextureObject;
            CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Registering texture");
            CrashReportCategory crashReportCategory = crashReport.makeCategory("Resource location being registered");
            crashReportCategory.addCrashSection("Resource location", resourceLocation);
            crashReportCategory.addCrashSectionCallable("Texture object class", new Callable<String>(){

                @Override
                public String call() throws Exception {
                    return iTextureObject2.getClass().getName();
                }
            });
            throw new ReportedException(crashReport);
        }
        this.mapTextureObjects.put(resourceLocation, iTextureObject);
        return bl;
    }

    public void bindTexture(ResourceLocation resourceLocation) {
        ITextureObject iTextureObject = this.mapTextureObjects.get(resourceLocation);
        if (iTextureObject == null) {
            iTextureObject = new SimpleTexture(resourceLocation);
            this.loadTexture(resourceLocation, iTextureObject);
        }
        TextureUtil.bindTexture(iTextureObject.getGlTextureId());
    }

    public TextureManager(IResourceManager iResourceManager) {
        this.listTickables = Lists.newArrayList();
        this.mapTextureCounters = Maps.newHashMap();
        this.theResourceManager = iResourceManager;
    }
}

