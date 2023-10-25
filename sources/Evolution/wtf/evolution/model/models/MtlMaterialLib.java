package wtf.evolution.model.models;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

public class MtlMaterialLib {

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

    public MtlMaterialLib(String path) {
        this.path = path;
        this.startPath = path.substring(0, path.lastIndexOf(47) + 1);
        this.materials = new ArrayList();
    }

    public void parse(String content) {
        String[] lines = content.split("\n");
        Material current = null;

        for (int i = 0; i < lines.length; ++i) {
            String line = lines[i].trim();
            String[] parts = line.split(" ");
            if (!parts[0].equals("#")) {
                if (parts[0].equals("newmtl")) {
                    Material material = new Material(parts[1]);
                    this.materials.add(material);
                    current = material;
                } else if (parts[0].equals("Ka")) {
                    current.ambientColor = new Vector3f(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3]));
                } else if (parts[0].equals("Kd")) {
                    current.diffuseColor = new Vector3f(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3]));
                } else if (parts[0].equals("map_Kd")) {
                    current.diffuseTexture = this.loadTexture(this.startPath + parts[1]);
                } else if (parts[0].equals("map_Ka")) {
                    current.ambientTexture = this.loadTexture(this.startPath + parts[1]);
                } else if (parts[0].equals("d") || parts[0].equals("Tr")) {
                    current.transparency = (float) Double.parseDouble(parts[1]);
                }
            }
        }

    }

    private int loadTexture(String string) {
        try {
            return loadTexture(ImageIO.read(MtlMaterialLib.class.getResource(string)));
        } catch (IOException var3) {
            var3.printStackTrace();
            return 0;
        }
    }

    public static ByteBuffer imageToByteBuffer(BufferedImage img) {
        int[] pixels = img.getRGB(0, 0, img.getWidth(), img.getHeight(), (int[]) null, 0, img.getWidth());
        int bufLen = pixels.length * 4;
        ByteBuffer oglPixelBuf = BufferUtils.createByteBuffer(bufLen);

        for (int y = 0; y < img.getHeight(); ++y) {
            for (int x = 0; x < img.getWidth(); ++x) {
                int rgb = pixels[y * img.getWidth() + x];
                float a = (float) (rgb >> 24 & 255) / 255.0F;
                float r = (float) (rgb >> 16 & 255) / 255.0F;
                float g = (float) (rgb >> 8 & 255) / 255.0F;
                float b = (float) (rgb >> 0 & 255) / 255.0F;
                oglPixelBuf.put((byte) ((int) (r * 255.0F)));
                oglPixelBuf.put((byte) ((int) (g * 255.0F)));
                oglPixelBuf.put((byte) ((int) (b * 255.0F)));
                oglPixelBuf.put((byte) ((int) (a * 255.0F)));
            }
        }

        oglPixelBuf.flip();
        return oglPixelBuf;
    }

    public static int loadTexture(BufferedImage img) {
        ByteBuffer oglPixelBuf = imageToByteBuffer(img);
        int id = GL11.glGenTextures();
        int target = 3553;
        GL11.glBindTexture(target, id);
        GL11.glTexParameterf(target, 10241, 9728.0F);
        GL11.glTexParameterf(target, 10240, 9728.0F);
        GL11.glTexEnvf(8960, 8704, 8448.0F);
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
