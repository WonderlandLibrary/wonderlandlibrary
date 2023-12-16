// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.world.motionblur;

import net.minecraft.client.resources.data.IMetadataSection;
import org.apache.commons.io.IOUtils;
import java.nio.charset.Charset;
import java.util.Locale;
import java.io.InputStream;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.IResource;

public class MotionBlurResource implements IResource
{
    private static final String JSON = "{\"targets\":[\"swap\",\"previous\"],\"passes\":[{\"name\":\"phosphor\",\"intarget\":\"minecraft:main\",\"outtarget\":\"swap\",\"auxtargets\":[{\"name\":\"PrevSampler\",\"id\":\"previous\"}],\"uniforms\":[{\"name\":\"Phosphor\",\"values\":[%.2f, %.2f, %.2f]}]},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"previous\"},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"minecraft:main\"}]}";
    
    @Override
    public ResourceLocation func_177241_a() {
        return null;
    }
    
    @Override
    public InputStream getInputStream() {
        final double amount = Math.min(0.7 + (MotionBlurModule.INSTANCE.blurAmount.getFloat() + 6.0) / 100.0 * 3.0 - 0.01, 1.0);
        return IOUtils.toInputStream(String.format(Locale.ENGLISH, "{\"targets\":[\"swap\",\"previous\"],\"passes\":[{\"name\":\"phosphor\",\"intarget\":\"minecraft:main\",\"outtarget\":\"swap\",\"auxtargets\":[{\"name\":\"PrevSampler\",\"id\":\"previous\"}],\"uniforms\":[{\"name\":\"Phosphor\",\"values\":[%.2f, %.2f, %.2f]}]},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"previous\"},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"minecraft:main\"}]}", amount, amount, amount), Charset.defaultCharset());
    }
    
    @Override
    public boolean hasMetadata() {
        return false;
    }
    
    @Override
    public String func_177240_d() {
        return null;
    }
    
    @Override
    public IMetadataSection getMetadata(final String var1) {
        return null;
    }
}
