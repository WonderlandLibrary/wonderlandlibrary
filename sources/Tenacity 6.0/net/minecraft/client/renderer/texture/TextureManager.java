// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.texture;

import org.apache.logging.log4j.LogManager;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import java.io.IOException;
import net.optifine.shaders.ShadersTex;
import net.optifine.EmissiveTextures;
import net.optifine.CustomGuis;
import net.optifine.RandomEntities;
import net.minecraft.src.Config;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.resources.IResourceManager;
import java.util.List;
import net.minecraft.util.ResourceLocation;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.resources.IResourceManagerReloadListener;

public class TextureManager implements ITickable, IResourceManagerReloadListener
{
    private static final Logger logger;
    private final Map<ResourceLocation, ITextureObject> mapTextureObjects;
    private final List<ITickable> listTickables;
    private final Map<String, Integer> mapTextureCounters;
    private final IResourceManager theResourceManager;
    private ITextureObject boundTexture;
    private ResourceLocation boundTextureLocation;
    
    public TextureManager(final IResourceManager resourceManager) {
        this.mapTextureObjects = (Map<ResourceLocation, ITextureObject>)Maps.newHashMap();
        this.listTickables = (List<ITickable>)Lists.newArrayList();
        this.mapTextureCounters = (Map<String, Integer>)Maps.newHashMap();
        this.theResourceManager = resourceManager;
    }
    
    public void bindTexture(ResourceLocation resource) {
        if (Config.isRandomEntities()) {
            resource = RandomEntities.getTextureLocation(resource);
        }
        if (Config.isCustomGuis()) {
            resource = CustomGuis.getTextureLocation(resource);
        }
        ITextureObject itextureobject = this.mapTextureObjects.get(resource);
        if (EmissiveTextures.isActive()) {
            itextureobject = EmissiveTextures.getEmissiveTexture(itextureobject, this.mapTextureObjects);
        }
        if (itextureobject == null) {
            itextureobject = new SimpleTexture(resource);
            this.loadTexture(resource, itextureobject);
        }
        if (Config.isShaders()) {
            ShadersTex.bindTexture(itextureobject);
        }
        else {
            TextureUtil.bindTexture(itextureobject.getGlTextureId());
        }
        this.boundTexture = itextureobject;
        this.boundTextureLocation = resource;
    }
    
    public void loadTickableTexture(final ResourceLocation textureLocation, final ITickableTextureObject textureObj) {
        if (this.loadTexture(textureLocation, textureObj)) {
            this.listTickables.add(textureObj);
        }
    }
    
    public boolean loadTexture(final ResourceLocation textureLocation, ITextureObject textureObj) {
        boolean flag = true;
        try {
            textureObj.loadTexture(this.theResourceManager);
        }
        catch (IOException ioexception) {
            TextureManager.logger.warn("Failed to load texture: " + textureLocation, (Throwable)ioexception);
            textureObj = TextureUtil.missingTexture;
            this.mapTextureObjects.put(textureLocation, textureObj);
            flag = false;
        }
        catch (Throwable throwable) {
            final ITextureObject textureObjf = textureObj;
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Registering texture");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Resource location being registered");
            crashreportcategory.addCrashSection("Resource location", textureLocation);
            crashreportcategory.addCrashSectionCallable("Texture object class", () -> textureObjf.getClass().getName());
            throw new ReportedException(crashreport);
        }
        this.mapTextureObjects.put(textureLocation, textureObj);
        return flag;
    }
    
    public ITextureObject getTexture(final ResourceLocation textureLocation) {
        return this.mapTextureObjects.get(textureLocation);
    }
    
    public ResourceLocation getDynamicTextureLocation(final String name, DynamicTexture texture) {
        if (name.equals("logo")) {
            texture = Config.getMojangLogoTexture(texture);
        }
        Integer integer = this.mapTextureCounters.get(name);
        if (integer == null) {
            integer = 1;
        }
        else {
            ++integer;
        }
        this.mapTextureCounters.put(name, integer);
        final ResourceLocation resourcelocation = new ResourceLocation(String.format("dynamic/%s_%d", name, integer));
        this.loadTexture(resourcelocation, texture);
        return resourcelocation;
    }
    
    @Override
    public void tick() {
        for (final ITickable itickable : this.listTickables) {
            itickable.tick();
        }
    }
    
    public void deleteTexture(final ResourceLocation textureLocation) {
        final ITextureObject itextureobject = this.getTexture(textureLocation);
        if (itextureobject != null) {
            this.mapTextureObjects.remove(textureLocation);
            TextureUtil.deleteTexture(itextureobject.getGlTextureId());
        }
    }
    
    @Override
    public void onResourceManagerReload(final IResourceManager resourceManager) {
        Config.dbg("*** Reloading textures ***");
        Config.log("Resource packs: " + Config.getResourcePackNames());
        final Iterator iterator = this.mapTextureObjects.keySet().iterator();
        while (iterator.hasNext()) {
            final ResourceLocation resourcelocation = iterator.next();
            final String s = resourcelocation.getResourcePath();
            if (s.startsWith("mcpatcher/") || s.startsWith("optifine/") || EmissiveTextures.isEmissive(resourcelocation)) {
                final ITextureObject itextureobject = this.mapTextureObjects.get(resourcelocation);
                if (itextureobject instanceof AbstractTexture) {
                    final AbstractTexture abstracttexture = (AbstractTexture)itextureobject;
                    abstracttexture.deleteGlTexture();
                }
                iterator.remove();
            }
        }
        EmissiveTextures.update();
        for (final Object entry0 : new HashSet<Object>(this.mapTextureObjects.entrySet())) {
            final Map.Entry<ResourceLocation, ITextureObject> entry2 = (Map.Entry<ResourceLocation, ITextureObject>)entry0;
            this.loadTexture(entry2.getKey(), entry2.getValue());
        }
    }
    
    public void reloadBannerTextures() {
        for (final Object entry0 : new HashSet<Object>(this.mapTextureObjects.entrySet())) {
            final Map.Entry<ResourceLocation, ITextureObject> entry2 = (Map.Entry<ResourceLocation, ITextureObject>)entry0;
            final ResourceLocation resourcelocation = entry2.getKey();
            final ITextureObject itextureobject = entry2.getValue();
            if (itextureobject instanceof LayeredColorMaskTexture) {
                this.loadTexture(resourcelocation, itextureobject);
            }
        }
    }
    
    public ITextureObject getBoundTexture() {
        return this.boundTexture;
    }
    
    public ResourceLocation getBoundTextureLocation() {
        return this.boundTextureLocation;
    }
    
    static {
        logger = LogManager.getLogger();
    }
}
