// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.cosmetics.obj;

import java.io.InputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
import java.util.Iterator;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.opengl.GL11;

public class OBJCosmeticLoader
{
    public int createDisplayList(final OBJCosmetic model) {
        final int displayList = GL11.glGenLists(1);
        GL11.glNewList(displayList, 4864);
        this.render(model);
        GL11.glEndList();
        return displayList;
    }
    
    public void render(final OBJCosmetic model) {
        GL11.glMaterialf(1028, 5633, 120.0f);
        GL11.glBegin(4);
        for (final OBJCosmetic.Face face : model.getFaces()) {
            final Vector3f[] normals = { model.getNormals().get(face.getNormals()[0] - 1), model.getNormals().get(face.getNormals()[1] - 1), model.getNormals().get(face.getNormals()[2] - 1) };
            final Vector2f[] texCoords = { model.getTextureCoordinates().get(face.getTextureCoords()[0] - 1), model.getTextureCoordinates().get(face.getTextureCoords()[1] - 1), model.getTextureCoordinates().get(face.getTextureCoords()[2] - 1) };
            final Vector3f[] vertices = { model.getVertices().get(face.getVertices()[0] - 1), model.getVertices().get(face.getVertices()[1] - 1), model.getVertices().get(face.getVertices()[2] - 1) };
            GL11.glNormal3f(normals[0].getX(), normals[0].getY(), normals[0].getZ());
            GL11.glTexCoord2f(texCoords[0].getX(), texCoords[0].getY());
            GL11.glVertex3f(vertices[0].getX(), vertices[0].getY(), vertices[0].getZ());
            GL11.glNormal3f(normals[1].getX(), normals[1].getY(), normals[1].getZ());
            GL11.glTexCoord2f(texCoords[1].getX(), texCoords[1].getY());
            GL11.glVertex3f(vertices[1].getX(), vertices[1].getY(), vertices[1].getZ());
            GL11.glNormal3f(normals[2].getX(), normals[2].getY(), normals[2].getZ());
            GL11.glTexCoord2f(texCoords[2].getX(), texCoords[2].getY());
            GL11.glVertex3f(vertices[2].getX(), vertices[2].getY(), vertices[2].getZ());
        }
        GL11.glEnd();
    }
    
    public OBJCosmetic loadModel(final File file) throws FileNotFoundException {
        return this.loadModel(new Scanner(file));
    }
    
    public OBJCosmetic loadModel(final InputStream stream) {
        return this.loadModel(new Scanner(stream));
    }
    
    public OBJCosmetic loadModel(final Scanner sc) {
        final OBJCosmetic model = new OBJCosmetic();
        while (sc.hasNextLine()) {
            final String ln = sc.nextLine();
            if (ln != null && !ln.equals("") && !ln.startsWith("#")) {
                final String[] split = ln.split(" ");
                final String s;
                switch (s = split[0]) {
                    case "f": {
                        model.getFaces().add(new OBJCosmetic.Face(new int[] { Integer.parseInt(split[1].split("/")[0]), Integer.parseInt(split[2].split("/")[0]), Integer.parseInt(split[3].split("/")[0]) }, new int[] { Integer.parseInt(split[1].split("/")[1]), Integer.parseInt(split[2].split("/")[1]), Integer.parseInt(split[3].split("/")[1]) }, new int[] { Integer.parseInt(split[1].split("/")[2]), Integer.parseInt(split[2].split("/")[2]), Integer.parseInt(split[3].split("/")[2]) }));
                        continue;
                    }
                    case "s": {
                        model.setSmoothShadingEnabled(!ln.contains("off"));
                        continue;
                    }
                    case "v": {
                        model.getVertices().add(new Vector3f(Float.parseFloat(split[1]), Float.parseFloat(split[2]), Float.parseFloat(split[3])));
                        continue;
                    }
                    case "vn": {
                        model.getNormals().add(new Vector3f(Float.parseFloat(split[1]), Float.parseFloat(split[2]), Float.parseFloat(split[3])));
                        continue;
                    }
                    case "vt": {
                        model.getTextureCoordinates().add(new Vector2f(Float.parseFloat(split[1]), Float.parseFloat(split[2])));
                        continue;
                    }
                    default:
                        break;
                }
                System.err.println("[OBJLoader] Unknown Line: " + ln);
            }
        }
        sc.close();
        return model;
    }
}
