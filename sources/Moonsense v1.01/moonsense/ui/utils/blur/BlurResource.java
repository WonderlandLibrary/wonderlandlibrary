// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.utils.blur;

import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.data.IMetadataSection;
import java.io.ByteArrayInputStream;
import java.util.Scanner;
import java.io.InputStream;
import net.minecraft.client.resources.IResource;

public class BlurResource implements IResource
{
    private final float BLUR_RADIUS;
    
    @Override
    public InputStream getInputStream() {
        final StringBuilder data = new StringBuilder();
        final Scanner scan = new Scanner(BlurResource.class.getResourceAsStream("/assets/minecraft/streamlined/shaders/post/fade_in_blur.json"));
        try {
            while (scan.hasNextLine()) {
                data.append(scan.nextLine().replaceAll("@radius@", Integer.toString((int)this.BLUR_RADIUS))).append("\n");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ByteArrayInputStream(data.toString().getBytes());
        }
        finally {
            scan.close();
        }
        scan.close();
        return new ByteArrayInputStream(data.toString().getBytes());
    }
    
    @Override
    public boolean hasMetadata() {
        return false;
    }
    
    @Override
    public IMetadataSection getMetadata(final String p_110526_1_) {
        return null;
    }
    
    @Override
    public String func_177240_d() {
        return null;
    }
    
    @Override
    public ResourceLocation func_177241_a() {
        return null;
    }
    
    public BlurResource(final float BLUR_RADIUS) {
        this.BLUR_RADIUS = BLUR_RADIUS;
    }
}
