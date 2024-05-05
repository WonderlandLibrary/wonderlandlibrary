/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
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
    private static final Logger logger = LogManager.getLogger();
    private final Map<ResourceLocation, ITextureObject> mapTextureObjects = Maps.newHashMap();
    private final List<ITickable> listTickables = Lists.newArrayList();
    private final Map<String, Integer> mapTextureCounters = Maps.newHashMap();
    private IResourceManager theResourceManager;

    public TextureManager(IResourceManager resourceManager) {
        this.theResourceManager = resourceManager;
    }

    public void bindTexture(ResourceLocation resource) {
        ITextureObject itextureobject = this.mapTextureObjects.get(resource);
        if (itextureobject == null) {
            itextureobject = new SimpleTexture(resource);
            this.loadTexture(resource, itextureobject);
        }
        TextureUtil.bindTexture(itextureobject.getGlTextureId());
    }

    public boolean loadTickableTexture(ResourceLocation textureLocation, ITickableTextureObject textureObj) {
        if (!this.loadTexture(textureLocation, textureObj)) return false;
        this.listTickables.add(textureObj);
        return true;
    }

    public boolean loadTexture(ResourceLocation textureLocation, ITextureObject textureObj) {
        boolean flag = true;
        try {
            textureObj.loadTexture(this.theResourceManager);
        }
        catch (IOException ioexception) {
            logger.warn("Failed to load texture: " + textureLocation, (Throwable)ioexception);
            textureObj = TextureUtil.missingTexture;
            this.mapTextureObjects.put(textureLocation, textureObj);
            flag = false;
        }
        catch (Throwable throwable) {
            final ITextureObject textureObjf = textureObj;
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Registering texture");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Resource location being registered");
            crashreportcategory.addCrashSection("Resource location", textureLocation);
            crashreportcategory.addCrashSectionCallable("Texture object class", new Callable<String>(){

                @Override
                public String call() throws Exception {
                    return textureObjf.getClass().getName();
                }
            });
            throw new ReportedException(crashreport);
        }
        this.mapTextureObjects.put(textureLocation, textureObj);
        return flag;
    }

    public ITextureObject getTexture(ResourceLocation textureLocation) {
        return this.mapTextureObjects.get(textureLocation);
    }

    public ResourceLocation getDynamicTextureLocation(String name, DynamicTexture texture) {
        Integer integer = this.mapTextureCounters.get(name);
        integer = integer == null ? Integer.valueOf(1) : Integer.valueOf(integer + 1);
        this.mapTextureCounters.put(name, integer);
        ResourceLocation resourcelocation = new ResourceLocation(String.format("dynamic/%s_%d", name, integer));
        this.loadTexture(resourcelocation, texture);
        return resourcelocation;
    }

    @Override
    public void tick() {
        Iterator<ITickable> iterator = this.listTickables.iterator();
        while (iterator.hasNext()) {
            ITickable itickable = iterator.next();
            itickable.tick();
        }
    }

    public void deleteTexture(ResourceLocation textureLocation) {
        ITextureObject itextureobject = this.getTexture(textureLocation);
        if (itextureobject == null) return;
        TextureUtil.deleteTexture(itextureobject.getGlTextureId());
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        Iterator<Map.Entry<ResourceLocation, ITextureObject>> iterator = this.mapTextureObjects.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<ResourceLocation, ITextureObject> entry = iterator.next();
            this.loadTexture(entry.getKey(), entry.getValue());
        }
    }
}

