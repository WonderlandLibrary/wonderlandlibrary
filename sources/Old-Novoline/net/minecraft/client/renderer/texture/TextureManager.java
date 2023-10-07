package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.optifine.Config;
import net.optifine.RandomMobs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.shadersmod.client.ShadersTex;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TextureManager implements ITickable, IResourceManagerReloadListener {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Map<ResourceLocation, ITextureObject> mapTextureObjects = Maps.newHashMap();
    private final List<ITickable> listTickables = Lists.newArrayList();
    private final Map<String, Integer> mapTextureCounters = Maps.newHashMap();
    private final IResourceManager theResourceManager;

    public TextureManager(IResourceManager resourceManager) {
        this.theResourceManager = resourceManager;
    }

    public void bindTexture(ResourceLocation resource) {
        if (Config.isRandomMobs()) {
            resource = RandomMobs.getTextureLocation(resource);
        }

        ITextureObject object = this.mapTextureObjects.get(resource);

        if (object == null) {
            object = new SimpleTexture(resource);
            this.loadTexture(resource, object);
        }

        if (Config.isShaders()) {
            ShadersTex.bindTexture(object);
        } else {
            TextureUtil.bindTexture(object.getGlTextureId());
        }
    }

    public ITextureObject bindTextureA(ResourceLocation resource) {
        if(Config.isRandomMobs()) {
            resource = RandomMobs.getTextureLocation(resource);
        }

        ITextureObject object = this.mapTextureObjects.get(resource);

        if(object == null) {
            object = new SimpleTexture(resource);
            this.loadTexture(resource, object);
        }

        if(Config.isShaders()) {
            ShadersTex.bindTexture(object);
        } else {
            TextureUtil.bindTexture(object.getGlTextureId());
        }

        return object;
    }

    public boolean loadTickableTexture(ResourceLocation textureLocation, ITickableTextureObject textureObj) {
        if (this.loadTexture(textureLocation, textureObj)) {
            this.listTickables.add(textureObj);
            return true;
        } else {
            return false;
        }
    }

    public boolean loadTexture(ResourceLocation textureLocation, ITextureObject textureObj) {
        boolean flag = true;
        ITextureObject textureObject = textureObj;

        try {
            textureObj.loadTexture(this.theResourceManager);
        } catch (IOException ioexception) {
            LOGGER.warn("Failed to load texture: " + textureLocation, ioexception);
            textureObject = TextureUtil.missingTexture;
            this.mapTextureObjects.put(textureLocation, textureObject);
            flag = false;
        } catch (Throwable throwable) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Registering texture");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Resource location being registered");
            crashreportcategory.addCrashSection("Resource location", textureLocation);
            crashreportcategory.addCrashSectionCallable("Texture object class", () -> textureObj.getClass().getName());
            throw new ReportedException(crashreport);
        }

        this.mapTextureObjects.put(textureLocation, textureObject);
        return flag;
    }

    public ITextureObject getTexture(ResourceLocation textureLocation) {
        return this.mapTextureObjects.get(textureLocation);
    }

    public ResourceLocation getDynamicTextureLocation(String name, DynamicTexture texture) {
        if (name.equals("logo")) {
            texture = Config.getMojangLogoTexture(texture);
        }

        Integer integer = this.mapTextureCounters.get(name);

        if (integer == null) {
            integer = 1;
        } else {
            integer = integer + 1;
        }

        this.mapTextureCounters.put(name, integer);
        final ResourceLocation resourcelocation = new ResourceLocation(String.format("dynamic/%s_%d", name, integer));
        this.loadTexture(resourcelocation, texture);
        return resourcelocation;
    }

    public void tick() {
        for (Object tickable : this.listTickables) {
            ((ITickable) tickable).tick();
        }
    }

    public void deleteTexture(ResourceLocation textureLocation) {
        final ITextureObject itextureobject = this.getTexture(textureLocation);

        if (itextureobject != null) {
            this.mapTextureObjects.remove(textureLocation);
            TextureUtil.deleteTexture(itextureobject.getGlTextureId());
        }
    }

    public void onResourceManagerReload(IResourceManager resourceManager) {
        Config.dbg("*** Reloading textures ***");
        Config.log("Resource packs: " + Config.getResourcePackNames());

        final Iterator<ResourceLocation> iterator = this.mapTextureObjects.keySet().iterator();

        while (iterator.hasNext()) {
            final ResourceLocation resourceLocation = iterator.next();
            final String s = resourceLocation.getResourcePath();

            if (s.startsWith("mcpatcher/") || s.startsWith("optifine/")) {
                final ITextureObject itextureobject = this.mapTextureObjects.get(resourceLocation);

                if (itextureobject instanceof AbstractTexture) {
                    final AbstractTexture abstracttexture = (AbstractTexture) itextureobject;
                    abstracttexture.deleteGlTexture();
                }

                iterator.remove();
            }
        }

        for (Entry<ResourceLocation, ITextureObject> entry : this.mapTextureObjects.entrySet()) {
            loadTexture(entry.getKey(), entry.getValue());
        }
    }

    public void reloadBannerTextures() {
        for (Entry<ResourceLocation, ITextureObject> entry : this.mapTextureObjects.entrySet()) {
            final ITextureObject textureObject = entry.getValue();

            if (textureObject instanceof LayeredColorMaskTexture) {
                this.loadTexture(entry.getKey(), textureObject);
            }
        }
    }

}
