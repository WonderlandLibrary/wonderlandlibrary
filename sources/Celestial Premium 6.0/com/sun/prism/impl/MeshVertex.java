/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl;

import com.sun.javafx.geom.Vec3f;
import com.sun.prism.impl.MeshTempState;
import com.sun.prism.impl.MeshUtil;

class MeshVertex {
    int smGroup;
    int pVert;
    int tVert;
    int fIdx;
    int index;
    Vec3f[] norm = new Vec3f[3];
    MeshVertex next = null;
    static final int IDX_UNDEFINED = -1;
    static final int IDX_SET_SMOOTH = -2;
    static final int IDX_UNITE = -3;

    MeshVertex() {
        for (int i = 0; i < this.norm.length; ++i) {
            this.norm[i] = new Vec3f();
        }
    }

    static void avgSmNormals(MeshVertex meshVertex) {
        Vec3f vec3f = MeshTempState.getInstance().vec3f1;
        while (meshVertex != null) {
            if (meshVertex.index == -1) {
                vec3f.set(meshVertex.norm[0]);
                int n = meshVertex.smGroup;
                MeshVertex meshVertex2 = meshVertex.next;
                while (meshVertex2 != null) {
                    if (meshVertex2.smGroup == n) {
                        assert (meshVertex2.index == -1);
                        meshVertex2.index = -2;
                        vec3f.add(meshVertex2.norm[0]);
                    }
                    meshVertex2 = meshVertex2.next;
                }
                if (MeshUtil.isNormalOkAfterWeld(vec3f)) {
                    vec3f.normalize();
                    meshVertex2 = meshVertex;
                    while (meshVertex2 != null) {
                        if (meshVertex2.smGroup == n) {
                            meshVertex2.norm[0].set(vec3f);
                        }
                        meshVertex2 = meshVertex2.next;
                    }
                }
            }
            meshVertex = meshVertex.next;
        }
    }

    static boolean okToWeldVertsTB(MeshVertex meshVertex, MeshVertex meshVertex2) {
        return meshVertex.tVert == meshVertex2.tVert && MeshUtil.isTangentOk(meshVertex.norm, meshVertex2.norm);
    }

    static int weldWithTB(MeshVertex meshVertex, int n) {
        Vec3f[] arrvec3f = MeshTempState.getInstance().triNormals;
        while (meshVertex != null) {
            if (meshVertex.index < 0) {
                int n2 = 0;
                for (int i = 0; i < 3; ++i) {
                    arrvec3f[i].set(meshVertex.norm[i]);
                }
                MeshVertex meshVertex2 = meshVertex.next;
                while (meshVertex2 != null) {
                    if (meshVertex2.index < 0 && MeshVertex.okToWeldVertsTB(meshVertex, meshVertex2)) {
                        meshVertex2.index = -3;
                        ++n2;
                        for (int i = 0; i < 3; ++i) {
                            arrvec3f[i].add(meshVertex2.norm[i]);
                        }
                    }
                    meshVertex2 = meshVertex2.next;
                }
                if (n2 != 0) {
                    if (MeshUtil.isTangentOK(arrvec3f)) {
                        MeshUtil.fixTSpace(arrvec3f);
                        meshVertex.index = n;
                        for (int i = 0; i < 3; ++i) {
                            meshVertex.norm[i].set(arrvec3f[i]);
                        }
                        meshVertex2 = meshVertex.next;
                        while (meshVertex2 != null) {
                            if (meshVertex2.index == -3) {
                                meshVertex2.index = n;
                                meshVertex2.norm[0].set(0.0f, 0.0f, 0.0f);
                            }
                            meshVertex2 = meshVertex2.next;
                        }
                    } else {
                        n2 = 0;
                    }
                }
                if (n2 == 0) {
                    MeshUtil.fixTSpace(meshVertex.norm);
                    meshVertex.index = n;
                }
                ++n;
            }
            meshVertex = meshVertex.next;
        }
        return n;
    }

    static void mergeSmIndexes(MeshVertex meshVertex) {
        MeshVertex meshVertex2 = meshVertex;
        while (meshVertex2 != null) {
            boolean bl = false;
            MeshVertex meshVertex3 = meshVertex2.next;
            while (meshVertex3 != null) {
                if ((meshVertex2.smGroup & meshVertex3.smGroup) != 0 && meshVertex2.smGroup != meshVertex3.smGroup) {
                    meshVertex3.smGroup = meshVertex2.smGroup = meshVertex3.smGroup | meshVertex2.smGroup;
                    bl = true;
                }
                meshVertex3 = meshVertex3.next;
            }
            if (bl) continue;
            meshVertex2 = meshVertex2.next;
        }
    }

    static void correctSmNormals(MeshVertex meshVertex) {
        MeshVertex meshVertex2 = meshVertex;
        while (meshVertex2 != null) {
            if (meshVertex2.smGroup != 0) {
                MeshVertex meshVertex3 = meshVertex2.next;
                while (meshVertex3 != null) {
                    if ((meshVertex3.smGroup & meshVertex2.smGroup) != 0 && MeshUtil.isOppositeLookingNormals(meshVertex3.norm, meshVertex2.norm)) {
                        meshVertex2.smGroup = 0;
                        meshVertex3.smGroup = 0;
                        break;
                    }
                    meshVertex3 = meshVertex3.next;
                }
            }
            meshVertex2 = meshVertex2.next;
        }
    }

    static int processVertices(MeshVertex[] arrmeshVertex, int n, boolean bl, boolean bl2) {
        int n2 = 0;
        Vec3f vec3f = MeshTempState.getInstance().vec3f1;
        for (int i = 0; i < n; ++i) {
            if (arrmeshVertex[i] == null) continue;
            if (!bl) {
                if (bl2) {
                    vec3f.set(arrmeshVertex[i].norm[0]);
                    MeshVertex meshVertex = arrmeshVertex[i].next;
                    while (meshVertex != null) {
                        vec3f.add(meshVertex.norm[0]);
                        meshVertex = meshVertex.next;
                    }
                    if (MeshUtil.isNormalOkAfterWeld(vec3f)) {
                        vec3f.normalize();
                        meshVertex = arrmeshVertex[i];
                        while (meshVertex != null) {
                            meshVertex.norm[0].set(vec3f);
                            meshVertex = meshVertex.next;
                        }
                    }
                } else {
                    MeshVertex.mergeSmIndexes(arrmeshVertex[i]);
                    MeshVertex.avgSmNormals(arrmeshVertex[i]);
                }
            }
            n2 = MeshVertex.weldWithTB(arrmeshVertex[i], n2);
        }
        return n2;
    }

    public String toString() {
        return "MeshVertex : " + this.getClass().getName() + "@0x" + Integer.toHexString(this.hashCode()) + ":: smGroup = " + this.smGroup + "\n\tnorm[0] = " + this.norm[0] + "\n\tnorm[1] = " + this.norm[1] + "\n\tnorm[2] = " + this.norm[2] + "\n\ttIndex = " + this.tVert + ", fIndex = " + this.fIdx + "\n\tpIdx = " + this.index + "\n\tnext = " + (this.next == null ? this.next : this.next.getClass().getName() + "@0x" + Integer.toHexString(this.next.hashCode())) + "\n";
    }

    static void dumpInfo(MeshVertex meshVertex) {
        System.err.println("** dumpInfo: ");
        MeshVertex meshVertex2 = meshVertex;
        while (meshVertex2 != null) {
            System.err.println(meshVertex2);
            meshVertex2 = meshVertex2.next;
        }
        System.err.println("***********************************");
    }
}

