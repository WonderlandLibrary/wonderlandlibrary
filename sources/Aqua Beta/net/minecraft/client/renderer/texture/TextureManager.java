package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.client.renderer.texture.ITickableTextureObject;
import net.minecraft.client.renderer.texture.LayeredColorMaskTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.src.Config;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomGuis;
import net.optifine.EmissiveTextures;
import net.optifine.RandomEntities;
import net.optifine.shaders.ShadersTex;
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
    private ITextureObject boundTexture;
    private ResourceLocation boundTextureLocation;

    public TextureManager(IResourceManager resourceManager) {
        this.theResourceManager = resourceManager;
    }

    public void bindTexture(ResourceLocation resource) {
        if (Config.isRandomEntities()) {
            resource = RandomEntities.getTextureLocation((ResourceLocation)resource);
        }
        if (Config.isCustomGuis()) {
            resource = CustomGuis.getTextureLocation((ResourceLocation)resource);
        }
        ITextureObject itextureobject = (ITextureObject)this.mapTextureObjects.get((Object)resource);
        if (EmissiveTextures.isActive()) {
            itextureobject = EmissiveTextures.getEmissiveTexture((ITextureObject)itextureobject, this.mapTextureObjects);
        }
        if (itextureobject == null) {
            itextureobject = new SimpleTexture(resource);
            this.loadTexture(resource, itextureobject);
        }
        if (Config.isShaders()) {
            ShadersTex.bindTexture((ITextureObject)itextureobject);
        } else {
            TextureUtil.bindTexture((int)itextureobject.getGlTextureId());
        }
        this.boundTexture = itextureobject;
        this.boundTextureLocation = resource;
    }

    public boolean loadTickableTexture(ResourceLocation textureLocation, ITickableTextureObject textureObj) {
        if (this.loadTexture(textureLocation, (ITextureObject)textureObj)) {
            this.listTickables.add((Object)textureObj);
            return true;
        }
        return false;
    }

    public boolean loadTexture(ResourceLocation textureLocation, ITextureObject textureObj) {
        boolean flag = true;
        try {
            textureObj.loadTexture(this.theResourceManager);
        }
        catch (IOException ioexception) {
            logger.warn("Failed to load texture: " + textureLocation, (Throwable)ioexception);
            textureObj = TextureUtil.missingTexture;
            this.mapTextureObjects.put((Object)textureLocation, (Object)textureObj);
            flag = false;
        }
        catch (Throwable throwable) {
            ITextureObject textureObjf = textureObj;
            CrashReport crashreport = CrashReport.makeCrashReport((Throwable)throwable, (String)"Registering texture");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Resource location being registered");
            crashreportcategory.addCrashSection("Resource location", (Object)textureLocation);
            crashreportcategory.addCrashSectionCallable("Texture object class", (Callable)new /* Unavailable Anonymous Inner Class!! */);
            throw new ReportedException(crashreport);
        }
        this.mapTextureObjects.put((Object)textureLocation, (Object)textureObj);
        return flag;
    }

    public ITextureObject getTexture(ResourceLocation textureLocation) {
        return (ITextureObject)this.mapTextureObjects.get((Object)textureLocation);
    }

    public ResourceLocation getDynamicTextureLocation(String name, DynamicTexture texture) {
        Integer integer;
        if (name.equals((Object)"logo")) {
            texture = Config.getMojangLogoTexture((DynamicTexture)texture);
        }
        integer = (integer = (Integer)this.mapTextureCounters.get((Object)name)) == null ? Integer.valueOf((int)1) : Integer.valueOf((int)(integer + 1));
        this.mapTextureCounters.put((Object)name, (Object)integer);
        ResourceLocation resourcelocation = new ResourceLocation(String.format((String)"dynamic/%s_%d", (Object[])new Object[]{name, integer}));
        this.loadTexture(resourcelocation, (ITextureObject)texture);
        return resourcelocation;
    }

    public void tick() {
        for (ITickable itickable : this.listTickables) {
            itickable.tick();
        }
    }

    public void deleteTexture(ResourceLocation textureLocation) {
        ITextureObject itextureobject = this.getTexture(textureLocation);
        if (itextureobject != null) {
            this.mapTextureObjects.remove((Object)textureLocation);
            TextureUtil.deleteTexture((int)itextureobject.getGlTextureId());
        }
    }

    public void onResourceManagerReload(IResourceManager resourceManager) {
        Config.dbg((String)"*** Reloading textures ***");
        Config.log((String)("Resource packs: " + Config.getResourcePackNames()));
        Iterator iterator = this.mapTextureObjects.keySet().iterator();
        while (iterator.hasNext()) {
            ResourceLocation resourcelocation = (ResourceLocation)iterator.next();
            String s = resourcelocation.getResourcePath();
            if (!s.startsWith("mcpatcher/") && !s.startsWith("optifine/") && !EmissiveTextures.isEmissive((ResourceLocation)resourcelocation)) continue;
            ITextureObject itextureobject = (ITextureObject)this.mapTextureObjects.get((Object)resourcelocation);
            if (itextureobject instanceof AbstractTexture) {
                AbstractTexture abstracttexture = (AbstractTexture)itextureobject;
                abstracttexture.deleteGlTexture();
            }
            iterator.remove();
        }
        EmissiveTextures.update();
        for (Map.Entry entry : new HashSet((Collection)this.mapTextureObjects.entrySet())) {
            this.loadTexture((ResourceLocation)entry.getKey(), (ITextureObject)entry.getValue());
        }
    }

    public void reloadBannerTextures() {
        for (Map.Entry entry : new HashSet((Collection)this.mapTextureObjects.entrySet())) {
            ResourceLocation resourcelocation = (ResourceLocation)entry.getKey();
            ITextureObject itextureobject = (ITextureObject)entry.getValue();
            if (!(itextureobject instanceof LayeredColorMaskTexture)) continue;
            this.loadTexture(resourcelocation, itextureobject);
        }
    }

    public ITextureObject getBoundTexture() {
        return this.boundTexture;
    }

    public ResourceLocation getBoundTextureLocation() {
        return this.boundTextureLocation;
    }
}
