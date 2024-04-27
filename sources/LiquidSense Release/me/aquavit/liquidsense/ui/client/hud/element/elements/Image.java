package me.aquavit.liquidsense.ui.client.hud.element.elements;

import com.google.gson.JsonElement;
import me.aquavit.liquidsense.utils.misc.MiscUtils;
import me.aquavit.liquidsense.utils.misc.RandomUtils;
import me.aquavit.liquidsense.ui.client.hud.element.Border;
import me.aquavit.liquidsense.ui.client.hud.element.Element;
import me.aquavit.liquidsense.ui.client.hud.element.ElementInfo;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.value.TextValue;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.Base64;

@ElementInfo(name = "Image")
public class Image extends Element {

    private final TextValue image = new TextValue("Image", "") {
        @Override
        public void fromJson(JsonElement element) {
            super.fromJson(element);

            if (get().isEmpty())
                return;

            setImage(get());
        }

        @Override
        protected void onChanged(final String oldValue, final String newValue) {
            if (image.get().isEmpty()) {
                return;
            }
            setImage(image.get());
        }
    };;
    private final ResourceLocation resourceLocation = new ResourceLocation(RandomUtils.randomNumber(128));
    private int width = 64;
    private int height = 64;

    @Override
    public Border drawElement() {
        RenderUtils.drawImage(resourceLocation, 0, 0, width / 2, height / 2);

        return new Border(0F, 0F, width / 2F, height / 2F);
    }

    @Override
    public boolean createElement() {
        File file = MiscUtils.openFileChooser();
        if (file == null) {
            return false;
        }

        if (!file.exists()) {
            MiscUtils.showErrorPopup("Error", "The file does not exist.");
            return false;
        }

        if (file.isDirectory()) {
            MiscUtils.showErrorPopup("Error", "The file is a directory.");
            return false;
        }

        this.setImage(file);
        return true;
    }

    private void setImage(final String image) {
        try {
            this.image.changeValue(image);
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(image));
            final BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
            byteArrayInputStream.close();
            this.width = bufferedImage.getWidth();
            this.height = bufferedImage.getHeight();
            mc.getTextureManager().loadTexture(resourceLocation, new DynamicTexture(bufferedImage));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setImage(final File image) {
        try {
            this.setImage(Base64.getEncoder().encodeToString(Files.readAllBytes(image.toPath())));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
