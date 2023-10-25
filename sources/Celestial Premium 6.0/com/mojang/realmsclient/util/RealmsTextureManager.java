/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.xml.bind.DatatypeConverter
 */
package com.mojang.realmsclient.util;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.realmsclient.util.RealmsUtil;
import com.mojang.realmsclient.util.SkinProcessor;
import com.mojang.util.UUIDTypeAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsScreen;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.ARBMultitexture;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GLContext;

public class RealmsTextureManager {
    private static final Map<String, RealmsTexture> textures = new HashMap<String, RealmsTexture>();
    private static final Map<String, Boolean> skinFetchStatus = new HashMap<String, Boolean>();
    private static final Map<String, String> fetchedSkins = new HashMap<String, String>();
    private static Boolean useMultitextureArb;
    public static int GL_TEXTURE0;
    private static final Logger LOGGER;
    private static final String STEVE_LOCATION = "minecraft:textures/entity/steve.png";
    private static final String ALEX_LOCATION = "minecraft:textures/entity/alex.png";

    public static void bindWorldTemplate(String id, String image) {
        if (image == null) {
            RealmsScreen.bind("textures/gui/presets/isles.png");
            return;
        }
        int textureId = RealmsTextureManager.getTextureId(id, image);
        GL11.glBindTexture(3553, textureId);
    }

    public static void bindDefaultFace(UUID uuid) {
        RealmsScreen.bind((uuid.hashCode() & 1) == 1 ? ALEX_LOCATION : STEVE_LOCATION);
    }

    public static void bindFace(final String uuid) {
        UUID actualUuid = UUIDTypeAdapter.fromString(uuid);
        if (textures.containsKey(uuid)) {
            GL11.glBindTexture(3553, RealmsTextureManager.textures.get((Object)uuid).textureId);
            return;
        }
        if (skinFetchStatus.containsKey(uuid)) {
            if (!skinFetchStatus.get(uuid).booleanValue()) {
                RealmsTextureManager.bindDefaultFace(actualUuid);
            } else if (fetchedSkins.containsKey(uuid)) {
                int textureId = RealmsTextureManager.getTextureId(uuid, fetchedSkins.get(uuid));
                GL11.glBindTexture(3553, textureId);
            } else {
                RealmsTextureManager.bindDefaultFace(actualUuid);
            }
            return;
        }
        skinFetchStatus.put(uuid, false);
        RealmsTextureManager.bindDefaultFace(actualUuid);
        Thread thread = new Thread("Realms Texture Downloader"){

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public void run() {
                block17: {
                    block16: {
                        ByteArrayOutputStream output;
                        BufferedImage loadedImage;
                        Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> fetchedTextures = RealmsUtil.getTextures(uuid);
                        if (!fetchedTextures.containsKey((Object)MinecraftProfileTexture.Type.SKIN)) break block16;
                        MinecraftProfileTexture textureInfo = fetchedTextures.get((Object)MinecraftProfileTexture.Type.SKIN);
                        String url = textureInfo.getUrl();
                        HttpURLConnection connection = null;
                        LOGGER.debug("Downloading http texture from {}", url);
                        try {
                            connection = (HttpURLConnection)new URL(url).openConnection(Realms.getProxy());
                            connection.setDoInput(true);
                            connection.setDoOutput(false);
                            connection.connect();
                            if (connection.getResponseCode() / 100 != 2) {
                                skinFetchStatus.remove(uuid);
                                return;
                            }
                            try {
                                loadedImage = ImageIO.read(connection.getInputStream());
                            }
                            catch (Exception ignored) {
                                skinFetchStatus.remove(uuid);
                                if (connection != null) {
                                    connection.disconnect();
                                }
                                return;
                            }
                            finally {
                                IOUtils.closeQuietly(connection.getInputStream());
                            }
                            loadedImage = new SkinProcessor().process(loadedImage);
                            output = new ByteArrayOutputStream();
                        }
                        catch (Exception e) {
                            LOGGER.error("Couldn't download http texture", (Throwable)e);
                            skinFetchStatus.remove(uuid);
                        }
                        finally {
                            if (connection != null) {
                                connection.disconnect();
                            }
                        }
                        ImageIO.write((RenderedImage)loadedImage, "png", output);
                        fetchedSkins.put(uuid, DatatypeConverter.printBase64Binary((byte[])output.toByteArray()));
                        skinFetchStatus.put(uuid, true);
                        break block17;
                    }
                    skinFetchStatus.put(uuid, true);
                    return;
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int getTextureId(String id, String image) {
        int textureId;
        if (textures.containsKey(id)) {
            RealmsTexture texture = textures.get(id);
            if (texture.image.equals(image)) {
                return texture.textureId;
            }
            GL11.glDeleteTextures(texture.textureId);
            textureId = texture.textureId;
        } else {
            textureId = GL11.glGenTextures();
        }
        IntBuffer buf = null;
        int width = 0;
        int height = 0;
        try {
            BufferedImage img;
            ByteArrayInputStream in = new ByteArrayInputStream(new Base64().decode(image));
            try {
                img = ImageIO.read(in);
            }
            finally {
                IOUtils.closeQuietly(in);
            }
            width = img.getWidth();
            height = img.getHeight();
            int[] data = new int[width * height];
            img.getRGB(0, 0, width, height, data, 0, width);
            buf = ByteBuffer.allocateDirect(4 * width * height).order(ByteOrder.nativeOrder()).asIntBuffer();
            buf.put(data);
            buf.flip();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (GL_TEXTURE0 == -1) {
            GL_TEXTURE0 = RealmsTextureManager.getUseMultiTextureArb() ? 33984 : 33984;
        }
        RealmsTextureManager.glActiveTexture(GL_TEXTURE0);
        GL11.glBindTexture(3553, textureId);
        GL11.glTexImage2D(3553, 0, 6408, width, height, 0, 32993, 33639, buf);
        GL11.glTexParameteri(3553, 10242, 10497);
        GL11.glTexParameteri(3553, 10243, 10497);
        GL11.glTexParameteri(3553, 10240, 9728);
        GL11.glTexParameteri(3553, 10241, 9729);
        textures.put(id, new RealmsTexture(image, textureId));
        return textureId;
    }

    public static void glActiveTexture(int texture) {
        if (RealmsTextureManager.getUseMultiTextureArb()) {
            ARBMultitexture.glActiveTextureARB(texture);
        } else {
            GL13.glActiveTexture(texture);
        }
    }

    public static boolean getUseMultiTextureArb() {
        if (useMultitextureArb == null) {
            ContextCapabilities caps = GLContext.getCapabilities();
            useMultitextureArb = caps.GL_ARB_multitexture && !caps.OpenGL13;
        }
        return useMultitextureArb;
    }

    static {
        GL_TEXTURE0 = -1;
        LOGGER = LogManager.getLogger();
    }

    public static class RealmsTexture {
        String image;
        int textureId;

        public RealmsTexture(String image, int textureId) {
            this.image = image;
            this.textureId = textureId;
        }
    }
}

