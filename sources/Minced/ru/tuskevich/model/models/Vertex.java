// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.model.models;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

public class Vertex
{
    private Vector3f pos;
    private Vector2f texCoords;
    private Vector3f normal;
    private Vector3f tangent;
    
    public Vertex(final Vector3f pos, final Vector2f texCoords, final Vector3f normal, final Vector3f tangent) {
        this.pos = pos;
        this.texCoords = texCoords;
        this.normal = normal;
        this.tangent = tangent;
    }
    
    public Vector3f pos() {
        return this.pos;
    }
    
    public Vector2f texCoords() {
        return this.texCoords;
    }
    
    public Vector3f normal() {
        return this.normal;
    }
    
    public Vector3f tangent() {
        return this.tangent;
    }
}
