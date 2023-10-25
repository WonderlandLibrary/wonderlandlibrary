package wtf.evolution.model.models;

import javax.vecmath.*;
import java.util.*;

public class IndexedModel {

    private ArrayList<Vector3f> vertices = new ArrayList();
    private ArrayList<Vector2f> texCoords = new ArrayList();
    private ArrayList<Vector3f> normals = new ArrayList();
    private ArrayList<Vector3f> tangents = new ArrayList();
    private ArrayList<Integer> indices = new ArrayList();
    private ArrayList<OBJLoader.OBJIndex> objindices = new ArrayList();

    public IndexedModel() {
    }

    public ArrayList<Vector3f> getPositions() {
        return this.vertices;
    }

    public ArrayList<Vector2f> getTexCoords() {
        return this.texCoords;
    }

    public ArrayList<Vector3f> getNormals() {
        return this.normals;
    }

    public ArrayList<Integer> getIndices() {
        return this.indices;
    }

    public ArrayList<Vector3f> getTangents() {
        return this.tangents;
    }

    public void toMesh(Mesh mesh) {
        ArrayList<Vertex> verticesList = new ArrayList();
        int n = Math.min(this.vertices.size(), Math.min(this.texCoords.size(), this.normals.size()));

        for (int i = 0; i < n; ++i) {
            Vertex vertex = new Vertex((Vector3f) this.vertices.get(i), (Vector2f) this.texCoords.get(i), (Vector3f) this.normals.get(i), new Vector3f());
            verticesList.add(vertex);
        }

        Integer[] indicesArray = (Integer[]) this.indices.toArray(new Integer[0]);
        Vertex[] verticesArray = (Vertex[]) verticesList.toArray(new Vertex[0]);
        int[] indicesArrayInt = new int[indicesArray.length];

        for (int i = 0; i < indicesArray.length; ++i) {
            indicesArrayInt[i] = indicesArray[i];
        }

        mesh.vertices = verticesArray;
        mesh.indices = indicesArrayInt;
    }

    public void computeNormals() {
        int i;
        for (i = 0; i < this.indices.size(); i += 3) {
            int i0 = (Integer) this.indices.get(i);
            int i1 = (Integer) this.indices.get(i + 1);
            int i2 = (Integer) this.indices.get(i + 2);
            Vector3f v = (Vector3f) ((Vector3f) this.vertices.get(i1)).clone();
            v.sub((Tuple3f) this.vertices.get(i0));
            Vector3f l0 = v;
            v = (Vector3f) ((Vector3f) this.vertices.get(i2)).clone();
            v.sub((Tuple3f) this.vertices.get(i0));
            Vector3f l1 = v;
            v = (Vector3f) l0.clone();
            v.cross(l0, l1);
            Vector3f normal = v;
            v = (Vector3f) ((Vector3f) this.normals.get(i0)).clone();
            v.add(normal);
            this.normals.set(i0, v);
            v = (Vector3f) ((Vector3f) this.normals.get(i1)).clone();
            v.add(normal);
            this.normals.set(i1, v);
            v = (Vector3f) ((Vector3f) this.normals.get(i2)).clone();
            v.add(normal);
            this.normals.set(i2, v);
        }

        for (i = 0; i < this.normals.size(); ++i) {
            ((Vector3f) this.normals.get(i)).normalize();
        }

    }

    public void computeTangents() {
        this.tangents.clear();

        int i;
        for (i = 0; i < this.vertices.size(); ++i) {
            this.tangents.add(new Vector3f());
        }

        for (i = 0; i < this.indices.size(); i += 3) {
            int i0 = (Integer) this.indices.get(i);
            int i1 = (Integer) this.indices.get(i + 1);
            int i2 = (Integer) this.indices.get(i + 2);
            Vector3f v = (Vector3f) ((Vector3f) this.vertices.get(i1)).clone();
            v.sub((Tuple3f) this.vertices.get(i0));
            Vector3f edge1 = v;
            v = (Vector3f) ((Vector3f) this.vertices.get(i2)).clone();
            v.sub((Tuple3f) this.vertices.get(i0));
            double deltaU1 = (double) (((Vector2f) this.texCoords.get(i1)).x - ((Vector2f) this.texCoords.get(i0)).x);
            double deltaU2 = (double) (((Vector2f) this.texCoords.get(i2)).x - ((Vector2f) this.texCoords.get(i0)).x);
            double deltaV1 = (double) (((Vector2f) this.texCoords.get(i1)).y - ((Vector2f) this.texCoords.get(i0)).y);
            double deltaV2 = (double) (((Vector2f) this.texCoords.get(i2)).y - ((Vector2f) this.texCoords.get(i0)).y);
            double dividend = deltaU1 * deltaV2 - deltaU2 * deltaV1;
            double f = dividend == 0.0 ? 0.0 : 1.0 / dividend;
            Vector3f tangent = new Vector3f((float) (f * (deltaV2 * (double) edge1.x - deltaV1 * (double) v.x)), (float) (f * (deltaV2 * (double) edge1.y - deltaV1 * (double) v.y)), (float) (f * (deltaV2 * (double) edge1.z - deltaV1 * (double) v.z)));
            v = (Vector3f) ((Vector3f) this.tangents.get(i0)).clone();
            v.add(tangent);
            this.tangents.set(i0, v);
            v = (Vector3f) ((Vector3f) this.tangents.get(i1)).clone();
            v.add(tangent);
            this.tangents.set(i1, v);
            v = (Vector3f) ((Vector3f) this.tangents.get(i2)).clone();
            v.add(tangent);
            this.tangents.set(i2, v);
        }

        for (i = 0; i < this.tangents.size(); ++i) {
            ((Vector3f) this.tangents.get(i)).normalize();
        }

    }

    public ArrayList<OBJLoader.OBJIndex> getObjIndices() {
        return this.objindices;
    }

    public org.lwjgl.util.vector.Vector3f computeCenter() {
        float x = 0.0F;
        float y = 0.0F;
        float z = 0.0F;

        Vector3f position;
        for (Iterator var4 = this.vertices.iterator(); var4.hasNext(); z += position.z) {
            position = (Vector3f) var4.next();
            x += position.x;
            y += position.y;
        }

        x /= (float) this.vertices.size();
        y /= (float) this.vertices.size();
        z /= (float) this.vertices.size();
        return new org.lwjgl.util.vector.Vector3f(x, y, z);
    }
}
