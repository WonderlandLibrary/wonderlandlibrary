package wtf.evolution.model.models;

import org.lwjgl.util.vector.Vector3f;

public class Material {

    private String name;
    public Vector3f diffuseColor;
    public Vector3f ambientColor;
    public int ambientTexture;
    public int diffuseTexture;
    public float transparency = 1.0F;

    public Material(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}

