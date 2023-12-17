// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.model.models;

import java.util.List;
import org.lwjgl.opengl.GL11;
import org.lwjgl.BufferUtils;
import java.nio.ByteBuffer;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.lwjgl.util.vector.Vector3f;
import java.util.ArrayList;

public class MtlMaterialLib
{
    public static final String COMMENT = "#";
    public static final String NEW_MATERIAL = "newmtl";
    public static final String AMBIENT_COLOR = "Ka";
    public static final String DIFFUSE_COLOR = "Kd";
    public static final String SPECULAR_COLOR = "Ks";
    public static final String TRANSPARENCY_D = "d";
    public static final String TRANSPARENCY_TR = "Tr";
    public static final String ILLUMINATION = "illum";
    public static final String TEXTURE_AMBIENT = "map_Ka";
    public static final String TEXTURE_DIFFUSE = "map_Kd";
    public static final String TEXTURE_SPECULAR = "map_Ks";
    public static final String TEXTURE_TRANSPARENCY = "map_d";
    private ArrayList<Material> materials;
    private String path;
    private String startPath;
    
    public MtlMaterialLib(final String path) {
        this.path = path;
        this.startPath = path.substring(0, path.lastIndexOf(47) + 1);
        this.materials = new ArrayList<Material>();
    }
    
    public void parse(final String content) {
        final String[] lines = content.split("\n");
        Material current = null;
        for (int i = 0; i < lines.length; ++i) {
            final String line = lines[i].trim();
            final String[] parts = line.split(" ");
            if (!parts[0].equals("#")) {
                if (parts[0].equals("newmtl")) {
                    final Material material = new Material(parts[1]);
                    this.materials.add(material);
                    current = material;
                }
                else if (parts[0].equals("Ka")) {
                    current.ambientColor = new Vector3f(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3]));
                }
                else if (parts[0].equals("Kd")) {
                    current.diffuseColor = new Vector3f(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3]));
                }
                else if (parts[0].equals("map_Kd")) {
                    current.diffuseTexture = this.loadTexture(this.startPath + parts[1]);
                }
                else if (parts[0].equals("map_Ka")) {
                    current.ambientTexture = this.loadTexture(this.startPath + parts[1]);
                }
                else if (parts[0].equals("d") || parts[0].equals("Tr")) {
                    current.transparency = (float)Double.parseDouble(parts[1]);
                }
            }
        }
    }
    
    private int loadTexture(final String string) {
        try {
            return loadTexture(ImageIO.read(MtlMaterialLib.class.getResource(string)));
        }
        catch (IOException var3) {
            var3.printStackTrace();
            return 0;
        }
    }
    
    public static ByteBuffer imageToByteBuffer(final BufferedImage img) {
        final int[] pixels = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
        final int bufLen = pixels.length * 4;
        final ByteBuffer oglPixelBuf = BufferUtils.createByteBuffer(bufLen);
        for (int y = 0; y < img.getHeight(); ++y) {
            for (int x = 0; x < img.getWidth(); ++x) {
                final int rgb = pixels[y * img.getWidth() + x];
                final float a = (rgb >> 24 & 0xFF) / 255.0f;
                final float r = (rgb >> 16 & 0xFF) / 255.0f;
                final float g = (rgb >> 8 & 0xFF) / 255.0f;
                final float b = (rgb >> 0 & 0xFF) / 255.0f;
                oglPixelBuf.put((byte)(r * 255.0f));
                oglPixelBuf.put((byte)(g * 255.0f));
                oglPixelBuf.put((byte)(b * 255.0f));
                oglPixelBuf.put((byte)(a * 255.0f));
            }
        }
        oglPixelBuf.flip();
        return oglPixelBuf;
    }
    
    public static int loadTexture(final BufferedImage img) {
        final ByteBuffer oglPixelBuf = imageToByteBuffer(img);
        final int id = GL11.glGenTextures();
        final int target = 3553;
        GL11.glBindTexture(target, id);
        GL11.glTexParameterf(target, 10241, 9728.0f);
        GL11.glTexParameterf(target, 10240, 9728.0f);
        GL11.glTexEnvf(8960, 8704, 8448.0f);
        GL11.glTexParameteri(target, 33084, 0);
        GL11.glTexParameteri(target, 33085, 0);
        GL11.glTexImage2D(target, 0, 32856, img.getWidth(), img.getHeight(), 0, 6408, 5121, oglPixelBuf);
        GL11.glBindTexture(target, 0);
        return id;
    }
    
    public List<Material> getMaterials() {
        return this.materials;
    }
}
