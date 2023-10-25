/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 *  javafx.scene.shape.CullFace
 *  javafx.scene.shape.Mesh
 */
package com.sun.javafx.scene.shape;

import com.sun.javafx.geom.PickRay;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.util.Utils;
import javafx.scene.Node;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Mesh;

public abstract class MeshHelper {
    private static MeshAccessor meshAccessor;

    protected MeshHelper() {
    }

    private static MeshHelper getHelper(Mesh mesh) {
        return meshAccessor.getHelper(mesh);
    }

    protected static void setHelper(Mesh mesh, MeshHelper meshHelper) {
        meshAccessor.setHelper(mesh, meshHelper);
    }

    public static boolean computeIntersects(Mesh mesh, PickRay pickRay, PickResultChooser pickResultChooser, Node node, CullFace cullFace, boolean bl) {
        return MeshHelper.getHelper(mesh).computeIntersectsImpl(mesh, pickRay, pickResultChooser, node, cullFace, bl);
    }

    protected abstract boolean computeIntersectsImpl(Mesh var1, PickRay var2, PickResultChooser var3, Node var4, CullFace var5, boolean var6);

    public static void setMeshAccessor(MeshAccessor meshAccessor) {
        if (MeshHelper.meshAccessor != null) {
            throw new IllegalStateException();
        }
        MeshHelper.meshAccessor = meshAccessor;
    }

    static {
        Utils.forceInit(Mesh.class);
    }

    public static interface MeshAccessor {
        public MeshHelper getHelper(Mesh var1);

        public void setHelper(Mesh var1, MeshHelper var2);
    }
}

