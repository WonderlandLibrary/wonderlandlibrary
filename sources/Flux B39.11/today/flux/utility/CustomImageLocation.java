package today.flux.utility;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class CustomImageLocation {
    ResourceLocation resourceLocation;
    BufferedImage bufferedImage;
    int width, height;

    public CustomImageLocation(File file) {
        try {
            bufferedImage = ImageIO.read(file);
            resourceLocation = new ResourceLocation("Flux" + new Random().nextInt(256));
            width = bufferedImage.getWidth();
            height = bufferedImage.getHeight();

            Minecraft.getMinecraft().getTextureManager().loadTexture(resourceLocation, new DynamicTexture(bufferedImage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ResourceLocation getResourceLocation() {
        return resourceLocation;
    }
}
