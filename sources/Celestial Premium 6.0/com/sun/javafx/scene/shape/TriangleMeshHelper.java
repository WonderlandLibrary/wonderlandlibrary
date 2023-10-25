/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 *  javafx.scene.shape.CullFace
 *  javafx.scene.shape.Mesh
 *  javafx.scene.shape.TriangleMesh
 */
package com.sun.javafx.scene.shape;

import com.sun.javafx.geom.PickRay;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.scene.shape.MeshHelper;
import com.sun.javafx.util.Utils;
import javafx.scene.Node;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.TriangleMesh;

public class TriangleMeshHelper
extends MeshHelper {
    private static final TriangleMeshHelper theInstance = new TriangleMeshHelper();
    private static TriangleMeshAccessor triangleMeshAccessor;

    private static TriangleMeshHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(TriangleMesh triangleMesh) {
        TriangleMeshHelper.setHelper((Mesh)triangleMesh, TriangleMeshHelper.getInstance());
    }

    @Override
    protected boolean computeIntersectsImpl(Mesh mesh, PickRay pickRay, PickResultChooser pickResultChooser, Node node, CullFace cullFace, boolean bl) {
        return triangleMeshAccessor.doComputeIntersects(mesh, pickRay, pickResultChooser, node, cullFace, bl);
    }

    public static void setTriangleMeshAccessor(TriangleMeshAccessor triangleMeshAccessor) {
        if (TriangleMeshHelper.triangleMeshAccessor != null) {
            throw new IllegalStateException();
        }
        TriangleMeshHelper.triangleMeshAccessor = triangleMeshAccessor;
    }

    static {
        Utils.forceInit(TriangleMesh.class);
    }

    public static interface TriangleMeshAccessor {
        public boolean doComputeIntersects(Mesh var1, PickRay var2, PickResultChooser var3, Node var4, CullFace var5, boolean var6);
    }
}

