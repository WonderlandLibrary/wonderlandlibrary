package pw.cinque.motionblur.resource;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Locale;

import org.apache.commons.io.IOUtils;

import appu26j.mods.visuals.MotionBlur;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.util.ResourceLocation;

public class MotionBlurResource implements IResource {
    
    private static final String author = "https://2pi.pw/";
    
    private static final String JSON =
            "{\"targets\":[\"swap\",\"previous\"],\"passes\":[{\"name\":\"motion_blur\",\"intarget\":\"minecraft:main\",\"outtarget\":\"swap\",\"auxtargets\":[{\"name\":\"PrevSampler\",\"id\":\"previous\"}],\"uniforms\":[{\"name\":\"Phosphor\",\"values\":[%.2f]}]},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"previous\"},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"minecraft:main\"}]}";

    @Override
    public ResourceLocation getResourceLocation() {
        return null;
    }

    @Override
    public InputStream getInputStream() {
        double amount = 0.2 + MotionBlur.get().blurAmount() / 20;
        return IOUtils.toInputStream(String.format(Locale.ENGLISH, JSON, amount), Charset.defaultCharset());
    }

    @Override
    public boolean hasMetadata() {
        return false;
    }

    @Override
    public <T extends IMetadataSection> T getMetadata(String p_110526_1_) {
        return null;
    }

    @Override
    public String getResourcePackName() {
        return null;
    }
}