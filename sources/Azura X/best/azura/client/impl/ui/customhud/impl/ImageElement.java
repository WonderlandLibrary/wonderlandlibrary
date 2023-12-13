package best.azura.client.impl.ui.customhud.impl;

import best.azura.client.impl.Client;
import best.azura.client.impl.ui.AnimatedTexture;
import best.azura.client.api.ui.customhud.Element;
import best.azura.client.api.ui.notification.Notification;
import best.azura.client.api.ui.notification.Type;
import best.azura.client.impl.ui.font.Fonts;
import best.azura.client.impl.ui.Texture;
import best.azura.client.util.textures.JordanTextureUtil;
import best.azura.client.util.render.RenderUtil;
import best.azura.client.impl.value.ClickValue;
import best.azura.client.api.value.Value;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.errordev.imagelib.GifConverter;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class ImageElement extends Element {

    private Texture texture;
    private AnimatedTexture animatedTexture;
    private long lastUpdateTime;

    /**
     * Constructor for the Elements
     *
     * Name for the corresponding element
     * Default x position for the element
     * Default y height for the element
     */
    public ImageElement() {
        super("Image", 0, 0, 128, 128);
        setCanSizeBeModified(true);
    }

    @Override
    public List<Value<?>> getValues() {
        return createValuesArray(new ClickValue("Change image", "Change the image of the image element", () -> new Thread(() -> {
            final FileDialog fileDialog = new FileDialog((Frame) null, "Select image");
            fileDialog.setFilenameFilter((dir, name) -> name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".jpeg"));
            fileDialog.setAlwaysOnTop(true);
            fileDialog.setLocationRelativeTo(null);
            fileDialog.requestFocus();
            fileDialog.setVisible(true);
            if (fileDialog.getFiles().length >= 1) {
                try {
                    final File file = fileDialog.getFiles()[0];
                    if (file.getName().endsWith(".gif")) {
                        Client.INSTANCE.getGlTasks().add(() -> {
                            try {
                                this.setAnimatedImage(GifConverter.readGifAsTextures(new FileInputStream(file)));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        return;
                    }
                    if (!(file.getName().toLowerCase().endsWith(".png") || file.getName().toLowerCase().endsWith(".jpg") || file.getName().toLowerCase().endsWith(".jpeg"))) {
                        Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Warning", "File " + file.getName() + " cannot be loaded!", 3000, Type.WARNING));
                        return;
                    }
                    final BufferedImage image = ImageIO.read(file);
                    Client.INSTANCE.getGlTasks().add(() -> this.setImage(image));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }).start()));
    }

    @Override
    public JsonObject buildJson() {
        JsonObject object = super.buildJson();
        try {
            if (animatedTexture != null) {
                final JsonArray jsonArray = new JsonArray();
                int index = 0;
                for (final Texture texture : this.animatedTexture.getTextures()) {
                    final JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("frame", Base64.getEncoder().encodeToString(JordanTextureUtil.getBytesFromImage(texture.getBufferedImage(), "PNG")));
                    jsonObject.addProperty("index", index);
                    jsonObject.addProperty("delay", texture.getDelay());
                    jsonArray.add(jsonObject);
                    index++;
                }
                object.add("image", jsonArray);
            } else if (texture != null)
                object.addProperty("image", Base64.getEncoder().encodeToString(JordanTextureUtil.getBytesFromImage(texture.getBufferedImage(), "PNG")));
            else object.addProperty("image", "");
        } catch (Exception exception) {
            object.addProperty("image", "");
            exception.printStackTrace();
        }
        return object;
    }

    @Override
    public void loadFromJson(JsonObject object) {
        super.loadFromJson(object);
        if (object.has("image") && !object.get("image").isJsonArray() && !object.get("image").getAsString().isEmpty()) {
            try {
                setImage(ImageIO.read(new ByteArrayInputStream(
                        DatatypeConverter.parseBase64Binary(getStringAfter(object.get("image").getAsString())))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (object.has("image") && object.get("image").isJsonArray()) {
            try {
                final JsonArray jsonArray = object.get("image").getAsJsonArray();
                animatedTexture = new AnimatedTexture(new Texture[jsonArray.size()]);
                for (int index = 0; index < jsonArray.size(); index++) {
                    final JsonObject jsonObject = jsonArray.get(index).getAsJsonObject();
                    if (jsonObject.has("frame") && jsonObject.has("index")) {
                        final Texture texture = new Texture(ImageIO.read(new ByteArrayInputStream(
                                DatatypeConverter.parseBase64Binary(getStringAfter(jsonObject.get("frame").getAsString())))), true);
                        texture.setDelay(jsonObject.get("delay").getAsInt());
                        this.animatedTexture.getTextures().set(jsonObject.get("index").getAsInt(), texture);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void render() {
        fitInScreen(mc.displayWidth, mc.displayHeight);
        if (animatedTexture != null && !animatedTexture.getTextures().isEmpty()) {
            if (lastUpdateTime == 0) lastUpdateTime = System.currentTimeMillis() - (500L / animatedTexture.getTextures().size());
            if (this.animatedTexture != null && !this.animatedTexture.getTextures().isEmpty() &&
                    !(mc.isGamePaused() || texture == null) &&
                    (animatedTexture.getCurrentTexture() == null || System.currentTimeMillis() - lastUpdateTime > ((10L * animatedTexture.getCurrentTexture().getDelay())))) {
                this.animatedTexture.updateIndex();
                lastUpdateTime = System.currentTimeMillis();
                texture = animatedTexture.getCurrentTexture();
            }
        }
        if (texture == null) {
            texture = JordanTextureUtil.createTexture(new ResourceLocation("textures/blocks/stone.png"));
            return;
        }
        texture.bind();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.resetColor();
        GlStateManager.color(1, 1, 1, 1);
        RenderUtil.INSTANCE.drawTexture(getX(), getY(), getX() + getWidth(), getY() + getHeight());
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        texture.unbind();
        Fonts.INSTANCE.hudFont.drawString(" ", -1000, -1000, -1);
    }

    private String getStringAfter(String input) {
        if (input.contains(",")) return input.split(",")[1];
        return input;
    }


    @Override
    public Element copy() {
        return new ImageElement();
    }

    public void setImage(BufferedImage image) {
        this.animatedTexture = null;
        this.texture = new Texture(image, true);
    }

    public void setAnimatedImage(final Texture[] textures) {
        if (textures == null || textures.length == 0) return;
        this.texture = null;
        this.animatedTexture = new AnimatedTexture(textures);
    }
}