// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.cosmetics.obj;

import java.util.Arrays;
import org.lwjgl.opengl.GL11;
import java.util.ArrayList;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import java.util.List;

public class OBJCosmetic
{
    private final List<Vector3f> vertices;
    private final List<Vector2f> textureCoords;
    private final List<Vector3f> normals;
    private final List<Face> faces;
    private boolean enableSmoothShading;
    
    public OBJCosmetic(final List<Vector3f> vertices, final List<Vector2f> textureCoords, final List<Vector3f> normals, final List<Face> faces, final boolean enableSmoothShading) {
        this.vertices = vertices;
        this.textureCoords = textureCoords;
        this.normals = normals;
        this.faces = faces;
        this.enableSmoothShading = enableSmoothShading;
    }
    
    public OBJCosmetic() {
        this(new ArrayList<Vector3f>(), new ArrayList<Vector2f>(), new ArrayList<Vector3f>(), new ArrayList<Face>(), true);
    }
    
    public void enableStates() {
        if (this.isSmoothShadingEnabled()) {
            GL11.glShadeModel(7425);
        }
        else {
            GL11.glShadeModel(7424);
        }
    }
    
    public boolean hasTextureCoordinates() {
        return this.getTextureCoordinates().size() > 0;
    }
    
    public boolean hasNormals() {
        return this.getNormals().size() > 0;
    }
    
    public List<Vector3f> getVertices() {
        return this.vertices;
    }
    
    public List<Vector2f> getTextureCoordinates() {
        return this.textureCoords;
    }
    
    public List<Vector3f> getNormals() {
        return this.normals;
    }
    
    public List<Face> getFaces() {
        return this.faces;
    }
    
    public boolean isSmoothShadingEnabled() {
        return this.enableSmoothShading;
    }
    
    public void setSmoothShadingEnabled(final boolean isSmoothShadingEnabled) {
        this.enableSmoothShading = isSmoothShadingEnabled;
    }
    
    public static class Face
    {
        private final int[] vertexIndices;
        private final int[] normalIndices;
        private final int[] textureCoordinateIndices;
        
        public boolean hasNormals() {
            return this.normalIndices != null;
        }
        
        public boolean hasTextureCoords() {
            return this.textureCoordinateIndices != null;
        }
        
        public int[] getVertices() {
            return this.vertexIndices;
        }
        
        public int[] getTextureCoords() {
            return this.textureCoordinateIndices;
        }
        
        public int[] getNormals() {
            return this.normalIndices;
        }
        
        public Face(final int[] vertexIndices, final int[] textureCoordinateIndices, final int[] normalIndices) {
            this.vertexIndices = vertexIndices;
            this.normalIndices = normalIndices;
            this.textureCoordinateIndices = textureCoordinateIndices;
        }
        
        @Override
        public String toString() {
            return String.format("Face[vertexIndices%s normalIndices%s textureCoordinateIndices%s]", Arrays.toString(this.vertexIndices), Arrays.toString(this.normalIndices), Arrays.toString(this.textureCoordinateIndices));
        }
    }
}
