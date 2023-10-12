package wtf.diablo.utils.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

public class Base64ImageLocation {
    ResourceLocation resourceLocation;
    BufferedImage bufferedImage;
    int width, height;
    String base64;

    public Base64ImageLocation(String base64) {
        this.base64 = base64;
    }

    public ResourceLocation getResourceLocation() {
        if (resourceLocation == null) {
            try {
                byte[] bytes1 = Base64.getDecoder().decode(base64);
                ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
                bufferedImage = ImageIO.read(bais);
                resourceLocation = new ResourceLocation("Diablo" + new Random().nextInt(1000));

                width = bufferedImage.getWidth();
                height = bufferedImage.getHeight();

                Minecraft.getMinecraft().getTextureManager().loadTexture(resourceLocation, new DynamicTexture(bufferedImage));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resourceLocation;
    }
}
