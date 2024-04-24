package Method.Client.module.render;

import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.io.IOUtils;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static Method.Client.Main.setmgr;

public class MotionBlur extends Module {
    boolean Setup = true;
    double old = 0;
    private static Map domainResourceManagers;

    public static Setting blurAmount;

    @Override
    public void setup() {
        setmgr.add(blurAmount = new Setting("blurAmount", this, 1, 0, 10, false));
    }


    public MotionBlur() {
        super("MotionBlur", Keyboard.KEY_NONE, Category.RENDER, "MotionBlur");
    }

    @Override
    public void onEnable() {
        Setup = true;
        domainResourceManagers = null;
    }

    @Override
    public void onDisable() {
        mc.entityRenderer.stopUseShader();
        domainResourceManagers = null;
    }

    @Subscribe
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (old != blurAmount.getValDouble()) {
            old = blurAmount.getValDouble();
            Setup = true;
            domainResourceManagers = null;
            return;
        }
        if (domainResourceManagers == null) {
            try {
                domainResourceManagers = (Map) mc.getResourceManager();
                Field[] var2 = SimpleReloadableResourceManager.class.getDeclaredFields();
                for (Field field : var2) {
                    if (field.getType() == Map.class) {
                        field.setAccessible(true);
                        domainResourceManagers = (Map) field.get(mc.getResourceManager());
                        break;
                    }
                }
            } catch (Exception var6) {
                throw new RuntimeException(var6);
            }
        }
        assert domainResourceManagers != null;
        if (!domainResourceManagers.containsKey("motionblur")) {
            domainResourceManagers.put("motionblur", new MotionBlurResourceManager());
        }
        if (Setup) {
            mc.entityRenderer.loadShader(new ResourceLocation("motionblur", "motionblur"));
            mc.entityRenderer.getShaderGroup().createBindFramebuffers(MC.displayWidth, MC.displayHeight);
            Setup = false;
        }
    }
}

class MotionBlurResourceManager implements IResourceManager {
    @Override
    public Set<String> getResourceDomains() {
        return null;
    }

    @Override
    public IResource getResource(ResourceLocation resourceLocation) {
        return new MotionBlurResource();
    }

    @Override
    public List<IResource> getAllResources(ResourceLocation resourceLocation) {
        return null;
    }
}

class MotionBlurResource implements IResource {

    @Override
    public ResourceLocation getResourceLocation() {
        return null;
    }

    @Override
    public InputStream getInputStream() {
        double amount = 0.7D + MotionBlur.blurAmount.getValDouble() / 100.0D * 3.0D - 0.01D;
        return IOUtils.toInputStream(String.format(Locale.ENGLISH, "{\"targets\":[\"swap\",\"previous\"],\"passes\":[{\"name\":\"phosphor\",\"intarget\":\"minecraft:main\",\"outtarget\":\"swap\",\"auxtargets\":[{\"name\":\"PrevSampler\",\"id\":\"previous\"}],\"uniforms\":[{\"name\":\"Phosphor\",\"values\":[%.2f, %.2f, %.2f]}]},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"previous\"},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"minecraft:main\"}]}", amount, amount, amount));

    }

    @Override
    public boolean hasMetadata() {
        return false;
    }

    @Nullable
    @Override
    public <T extends IMetadataSection> T getMetadata(String s) {
        return null;
    }

    @Override
    public String getResourcePackName() {
        return null;
    }

    @Override
    public void close() {
    }
}



