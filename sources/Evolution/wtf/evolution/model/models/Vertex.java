package wtf.evolution.model.models;

import javax.vecmath.*;

public class Vertex {

    private Vector3f pos;
    private Vector2f texCoords;
    private Vector3f normal;
    private Vector3f tangent;

    public Vertex(Vector3f pos, Vector2f texCoords, Vector3f normal, Vector3f tangent) {
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

