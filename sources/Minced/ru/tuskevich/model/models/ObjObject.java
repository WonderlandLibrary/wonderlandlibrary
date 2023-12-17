// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.model.models;

import org.lwjgl.util.vector.Vector3f;

public class ObjObject
{
    private String name;
    public Mesh mesh;
    public Material material;
    public Vector3f center;
    
    public ObjObject(final String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
}
