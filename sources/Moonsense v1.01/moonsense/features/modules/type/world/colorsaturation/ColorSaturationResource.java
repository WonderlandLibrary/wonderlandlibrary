// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.world.colorsaturation;

import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import java.io.InputStream;
import net.minecraft.client.resources.IResource;

public class ColorSaturationResource implements IResource
{
    @Override
    public InputStream getInputStream() {
        final float saturation = ColorSaturationModule.INSTANCE.saturation.getFloat();
        return IOUtils.toInputStream(String.format("{    \"targets\": [        \"swap\",        \"previous\"    ],    \"passes\": [        {            \"name\": \"color_convolve\",            \"intarget\": \"minecraft:main\",            \"outtarget\": \"swap\",            \"auxtargets\": [                {                    \"name\": \"PrevSampler\",                    \"id\": \"previous\"                }            ],            \"uniforms\": [                {                    \"name\": \"Saturation\",                    \"values\": [ %s ]                }            ]        },        {            \"name\": \"blit\",            \"intarget\": \"swap\",            \"outtarget\": \"previous\"        },        {            \"name\": \"blit\",            \"intarget\": \"swap\",            \"outtarget\": \"minecraft:main\"        }    ]}", new StringBuilder().append(saturation).toString(), new StringBuilder().append(saturation).toString(), new StringBuilder().append(saturation).toString()));
    }
    
    @Override
    public boolean hasMetadata() {
        return false;
    }
    
    @Override
    public ResourceLocation func_177241_a() {
        return null;
    }
    
    @Override
    public IMetadataSection getMetadata(final String var1) {
        return null;
    }
    
    @Override
    public String func_177240_d() {
        return null;
    }
}
