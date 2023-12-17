// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.model.models;

import org.lwjgl.util.vector.Vector3f;

public class Material
{
    private String name;
    public Vector3f diffuseColor;
    public Vector3f ambientColor;
    public int ambientTexture;
    public int diffuseTexture;
    public float transparency;
    
    public Material(final String name) {
        this.transparency = 1.0f;
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
}
