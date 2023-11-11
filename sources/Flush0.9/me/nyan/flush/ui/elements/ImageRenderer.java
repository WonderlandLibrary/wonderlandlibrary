package me.nyan.flush.ui.elements;

import com.madgag.gif.fmsware.GifDecoder;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ImageRenderer {
    public static final int STATUS_OK = 0;
    public static final int STATUS_INVALID_FORMAT = 1;
    public static final int STATUS_FAILURE = 2;

    private static int imgRenderers;

    private final ArrayList<Image> images = new ArrayList<>();
    private Image currentImage;
    private boolean animating;
    private final AtomicBoolean running = new AtomicBoolean();

    public int load(InputStream stream) {
        ByteArrayInputStream inputStream = null;
        try {
            byte[] data = IOUtils.toByteArray(stream);
            inputStream = new ByteArrayInputStream(data);
            GifDecoder decoder = new GifDecoder();
            if (decoder.read(inputStream) == GifDecoder.STATUS_OK) {
                imgRenderers++;
                int i = 0;
                for (GifDecoder.GifFrame frame : decoder.getFrames()) {
                    images.add(new Image(frame, new DynamicTexture(frame.image),
                            new ResourceLocation("imgrenderer/gif/" + imgRenderers + "/" + i)));
                    i++;
                }

                animating = true;
                running.set(true);

                Thread thread = new Thread(() -> {
                    try {
                        for (int j = 0; ; ) {
                            if (!running.get()) {
                                break;
                            }

                            currentImage = images.get(j);

                            try {
                                Thread.sleep(images.get(j).frame.delay);
                            } catch (InterruptedException ignored) {
                            }

                            if (animating) {
                                j++;
                            }
                            if (j == images.size()) {
                                j = 0;
                            }
                        }
                    } catch (NullPointerException ignored) {
                    }
                });
                thread.setDaemon(true);
                thread.start();
                IOUtils.closeQuietly(inputStream);
            } else {
                IOUtils.closeQuietly(inputStream);

                inputStream = new ByteArrayInputStream(data);
                BufferedImage img = ImageIO.read(inputStream);
                if (img == null) {
                    inputStream.close();
                    IOUtils.closeQuietly(inputStream);
                    return STATUS_INVALID_FORMAT;
                }
                imgRenderers++;
                currentImage = new Image(null, new DynamicTexture(img),
                        new ResourceLocation("imgrenderer/image/" + imgRenderers));
            }
        } catch (IOException e) {
            IOUtils.closeQuietly(inputStream);
            e.printStackTrace();
            return STATUS_FAILURE;
        }

        return STATUS_OK;
    }

    public void draw(double x, double y, double width, double height) {
        if (currentImage == null) {
            return;
        }
        RenderUtils.glColor(-1);
        RenderUtils.drawImage(currentImage.texture, x, y, width, height, currentImage.textureId);
    }

    public void setAnimating(boolean animating) {
        this.animating = animating;
    }

    public int getWidth() {
        if (currentImage == null) {
            return 0;
        }
        return currentImage.texture.width;
    }

    public int getHeight() {
        if (currentImage == null) {
            return 0;
        }
        return currentImage.texture.height;
    }

    public boolean isGif() {
        return currentImage != null && currentImage.isGif();
    }

    public void dispose() {
        running.set(false);

        if (currentImage != null) {
            currentImage.texture.deleteGlTexture();
        }
        for (Image image : images) {
            image.texture.deleteGlTexture();
        }
    }

    public static class Image {
        private final GifDecoder.GifFrame frame;
        private final DynamicTexture texture;
        private final ResourceLocation textureId;

        public Image(GifDecoder.GifFrame frame, DynamicTexture texture, ResourceLocation textureId) {
            this.frame = frame;
            this.texture = texture;
            this.textureId = textureId;
        }

        public GifDecoder.GifFrame getFrame() {
            return frame;
        }

        public DynamicTexture getTexture() {
            return texture;
        }

        public ResourceLocation getTextureId() {
            return textureId;
        }

        public boolean isGif() {
            return frame != null;
        }
    }
}
