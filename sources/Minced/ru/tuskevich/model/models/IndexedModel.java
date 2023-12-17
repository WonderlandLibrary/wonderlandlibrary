// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.model.models;

import java.util.Iterator;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import java.util.ArrayList;

public class IndexedModel
{
    private ArrayList<Vector3f> vertices;
    private ArrayList<Vector2f> texCoords;
    private ArrayList<Vector3f> normals;
    private ArrayList<Vector3f> tangents;
    private ArrayList<Integer> indices;
    private ArrayList<OBJLoader.OBJIndex> objindices;
    
    public IndexedModel() {
        this.vertices = new ArrayList<Vector3f>();
        this.texCoords = new ArrayList<Vector2f>();
        this.normals = new ArrayList<Vector3f>();
        this.tangents = new ArrayList<Vector3f>();
        this.indices = new ArrayList<Integer>();
        this.objindices = new ArrayList<OBJLoader.OBJIndex>();
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
    
    public void toMesh(final Mesh mesh) {
        final ArrayList<Vertex> verticesList = new ArrayList<Vertex>();
        for (int n = Math.min(this.vertices.size(), Math.min(this.texCoords.size(), this.normals.size())), i = 0; i < n; ++i) {
            final Vertex vertex = new Vertex(this.vertices.get(i), this.texCoords.get(i), this.normals.get(i), new Vector3f());
            verticesList.add(vertex);
        }
        final Integer[] indicesArray = this.indices.toArray(new Integer[0]);
        final Vertex[] verticesArray = verticesList.toArray(new Vertex[0]);
        final int[] indicesArrayInt = new int[indicesArray.length];
        for (int j = 0; j < indicesArray.length; ++j) {
            indicesArrayInt[j] = indicesArray[j];
        }
        mesh.vertices = verticesArray;
        mesh.indices = indicesArrayInt;
    }
    
    public void computeNormals() {
        for (int i = 0; i < this.indices.size(); i += 3) {
            final int i2 = this.indices.get(i);
            final int i3 = this.indices.get(i + 1);
            final int i4 = this.indices.get(i + 2);
            Vector3f v = (Vector3f)this.vertices.get(i3).clone();
            v.sub(this.vertices.get(i2));
            final Vector3f l0 = v;
            v = (Vector3f)this.vertices.get(i4).clone();
            v.sub(this.vertices.get(i2));
            final Vector3f l2 = v;
            v = (Vector3f)l0.clone();
            v.cross(l0, l2);
            final Vector3f normal = v;
            v = (Vector3f)this.normals.get(i2).clone();
            v.add(normal);
            this.normals.set(i2, v);
            v = (Vector3f)this.normals.get(i3).clone();
            v.add(normal);
            this.normals.set(i3, v);
            v = (Vector3f)this.normals.get(i4).clone();
            v.add(normal);
            this.normals.set(i4, v);
        }
        for (int i = 0; i < this.normals.size(); ++i) {
            this.normals.get(i).normalize();
        }
    }
    
    public void computeTangents() {
        this.tangents.clear();
        for (int i = 0; i < this.vertices.size(); ++i) {
            this.tangents.add(new Vector3f());
        }
        for (int i = 0; i < this.indices.size(); i += 3) {
            final int i2 = this.indices.get(i);
            final int i3 = this.indices.get(i + 1);
            final int i4 = this.indices.get(i + 2);
            Vector3f v = (Vector3f)this.vertices.get(i3).clone();
            v.sub(this.vertices.get(i2));
            final Vector3f edge1 = v;
            v = (Vector3f)this.vertices.get(i4).clone();
            v.sub(this.vertices.get(i2));
            final double deltaU1 = this.texCoords.get(i3).x - this.texCoords.get(i2).x;
            final double deltaU2 = this.texCoords.get(i4).x - this.texCoords.get(i2).x;
            final double deltaV1 = this.texCoords.get(i3).y - this.texCoords.get(i2).y;
            final double deltaV2 = this.texCoords.get(i4).y - this.texCoords.get(i2).y;
            final double dividend = deltaU1 * deltaV2 - deltaU2 * deltaV1;
            final double f = (dividend == 0.0) ? 0.0 : (1.0 / dividend);
            final Vector3f tangent = new Vector3f((float)(f * (deltaV2 * edge1.x - deltaV1 * v.x)), (float)(f * (deltaV2 * edge1.y - deltaV1 * v.y)), (float)(f * (deltaV2 * edge1.z - deltaV1 * v.z)));
            v = (Vector3f)this.tangents.get(i2).clone();
            v.add(tangent);
            this.tangents.set(i2, v);
            v = (Vector3f)this.tangents.get(i3).clone();
            v.add(tangent);
            this.tangents.set(i3, v);
            v = (Vector3f)this.tangents.get(i4).clone();
            v.add(tangent);
            this.tangents.set(i4, v);
        }
        for (int i = 0; i < this.tangents.size(); ++i) {
            this.tangents.get(i).normalize();
        }
    }
    
    public ArrayList<OBJLoader.OBJIndex> getObjIndices() {
        return this.objindices;
    }
    
    public org.lwjgl.util.vector.Vector3f computeCenter() {
        float x = 0.0f;
        float y = 0.0f;
        float z = 0.0f;
        for (final Vector3f position : this.vertices) {
            x += position.x;
            y += position.y;
            z += position.z;
        }
        x /= this.vertices.size();
        y /= this.vertices.size();
        z /= this.vertices.size();
        return new org.lwjgl.util.vector.Vector3f(x, y, z);
    }
}
