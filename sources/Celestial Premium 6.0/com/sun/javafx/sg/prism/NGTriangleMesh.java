/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.javafx.collections.FloatArraySyncer
 *  com.sun.javafx.collections.IntegerArraySyncer
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.collections.FloatArraySyncer;
import com.sun.javafx.collections.IntegerArraySyncer;
import com.sun.prism.Mesh;
import com.sun.prism.ResourceFactory;

public class NGTriangleMesh {
    private boolean meshDirty = true;
    private Mesh mesh;
    private boolean userDefinedNormals = false;
    private float[] points;
    private int[] pointsFromAndLengthIndices = new int[2];
    private float[] normals;
    private int[] normalsFromAndLengthIndices = new int[2];
    private float[] texCoords;
    private int[] texCoordsFromAndLengthIndices = new int[2];
    private int[] faces;
    private int[] facesFromAndLengthIndices = new int[2];
    private int[] faceSmoothingGroups;
    private int[] faceSmoothingGroupsFromAndLengthIndices = new int[2];

    Mesh createMesh(ResourceFactory resourceFactory) {
        if (this.mesh != null && !this.mesh.isValid()) {
            this.mesh.dispose();
            this.mesh = null;
        }
        if (this.mesh == null) {
            this.mesh = resourceFactory.createMesh();
            this.meshDirty = true;
        }
        return this.mesh;
    }

    boolean validate() {
        if (this.points == null || this.texCoords == null || this.faces == null || this.faceSmoothingGroups == null || this.userDefinedNormals && this.normals == null) {
            return false;
        }
        if (this.meshDirty) {
            if (!this.mesh.buildGeometry(this.userDefinedNormals, this.points, this.pointsFromAndLengthIndices, this.normals, this.normalsFromAndLengthIndices, this.texCoords, this.texCoordsFromAndLengthIndices, this.faces, this.facesFromAndLengthIndices, this.faceSmoothingGroups, this.faceSmoothingGroupsFromAndLengthIndices)) {
                throw new RuntimeException("NGTriangleMesh: buildGeometry failed");
            }
            this.meshDirty = false;
        }
        return true;
    }

    void setPointsByRef(float[] arrf) {
        this.meshDirty = true;
        this.points = arrf;
    }

    void setNormalsByRef(float[] arrf) {
        this.meshDirty = true;
        this.normals = arrf;
    }

    void setTexCoordsByRef(float[] arrf) {
        this.meshDirty = true;
        this.texCoords = arrf;
    }

    void setFacesByRef(int[] arrn) {
        this.meshDirty = true;
        this.faces = arrn;
    }

    void setFaceSmoothingGroupsByRef(int[] arrn) {
        this.meshDirty = true;
        this.faceSmoothingGroups = arrn;
    }

    public void setUserDefinedNormals(boolean bl) {
        this.userDefinedNormals = bl;
    }

    public boolean isUserDefinedNormals() {
        return this.userDefinedNormals;
    }

    public void syncPoints(FloatArraySyncer floatArraySyncer) {
        this.meshDirty = true;
        this.points = floatArraySyncer != null ? floatArraySyncer.syncTo(this.points, this.pointsFromAndLengthIndices) : null;
    }

    public void syncNormals(FloatArraySyncer floatArraySyncer) {
        this.meshDirty = true;
        this.normals = floatArraySyncer != null ? floatArraySyncer.syncTo(this.normals, this.normalsFromAndLengthIndices) : null;
    }

    public void syncTexCoords(FloatArraySyncer floatArraySyncer) {
        this.meshDirty = true;
        this.texCoords = floatArraySyncer != null ? floatArraySyncer.syncTo(this.texCoords, this.texCoordsFromAndLengthIndices) : null;
    }

    public void syncFaces(IntegerArraySyncer integerArraySyncer) {
        this.meshDirty = true;
        this.faces = integerArraySyncer != null ? integerArraySyncer.syncTo(this.faces, this.facesFromAndLengthIndices) : null;
    }

    public void syncFaceSmoothingGroups(IntegerArraySyncer integerArraySyncer) {
        this.meshDirty = true;
        this.faceSmoothingGroups = integerArraySyncer != null ? integerArraySyncer.syncTo(this.faceSmoothingGroups, this.faceSmoothingGroupsFromAndLengthIndices) : null;
    }

    int[] test_getFaceSmoothingGroups() {
        return this.faceSmoothingGroups;
    }

    int[] test_getFaces() {
        return this.faces;
    }

    float[] test_getPoints() {
        return this.points;
    }

    float[] test_getNormals() {
        return this.normals;
    }

    float[] test_getTexCoords() {
        return this.texCoords;
    }

    Mesh test_getMesh() {
        return this.mesh;
    }
}

