/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.javafx.logging.PlatformLogger
 *  com.sun.javafx.logging.PlatformLogger$Level
 *  javafx.scene.shape.VertexFormat
 */
package com.sun.prism.impl;

import com.sun.javafx.geom.Quat4f;
import com.sun.javafx.geom.Vec2f;
import com.sun.javafx.geom.Vec3f;
import com.sun.javafx.logging.PlatformLogger;
import com.sun.prism.Mesh;
import com.sun.prism.impl.BaseGraphicsResource;
import com.sun.prism.impl.Disposer;
import com.sun.prism.impl.MeshTempState;
import com.sun.prism.impl.MeshUtil;
import com.sun.prism.impl.MeshVertex;
import java.util.Arrays;
import java.util.HashMap;
import javafx.scene.shape.VertexFormat;

public abstract class BaseMesh
extends BaseGraphicsResource
implements Mesh {
    private int nVerts;
    private int nTVerts;
    private int nFaces;
    private float[] pos;
    private float[] uv;
    private int[] faces;
    private int[] smoothing;
    private boolean allSameSmoothing;
    private boolean allHardEdges;
    protected static final int POINT_SIZE = 3;
    protected static final int NORMAL_SIZE = 3;
    protected static final int TEXCOORD_SIZE = 2;
    protected static final int POINT_SIZE_VB = 3;
    protected static final int TEXCOORD_SIZE_VB = 2;
    protected static final int NORMAL_SIZE_VB = 4;
    protected static final int VERTEX_SIZE_VB = 9;
    public static final int FACE_MEMBERS_SIZE = 7;
    private boolean[] dirtyVertices;
    private float[] cachedNormals;
    private float[] cachedTangents;
    private float[] cachedBitangents;
    private float[] vertexBuffer;
    private int[] indexBuffer;
    private short[] indexBufferShort;
    private int indexBufferSize;
    private int numberOfVertices;
    private HashMap<Integer, MeshGeomComp2VB> point2vbMap;
    private HashMap<Integer, MeshGeomComp2VB> normal2vbMap;
    private HashMap<Integer, MeshGeomComp2VB> texCoord2vbMap;

    protected BaseMesh(Disposer.Record record) {
        super(record);
    }

    public abstract boolean buildNativeGeometry(float[] var1, int var2, int[] var3, int var4);

    public abstract boolean buildNativeGeometry(float[] var1, int var2, short[] var3, int var4);

    private void convertNormalsToQuats(MeshTempState meshTempState, int n, float[] arrf, float[] arrf2, float[] arrf3, float[] arrf4, boolean[] arrbl) {
        Vec3f vec3f = meshTempState.vec3f1;
        Vec3f vec3f2 = meshTempState.vec3f2;
        Vec3f vec3f3 = meshTempState.vec3f3;
        int n2 = 0;
        int n3 = 0;
        while (n2 < n) {
            if (arrbl == null || arrbl[n2]) {
                int n4 = n2 * 3;
                vec3f.x = arrf[n4];
                vec3f.y = arrf[n4 + 1];
                vec3f.z = arrf[n4 + 2];
                vec3f.normalize();
                vec3f2.x = arrf2[n4];
                vec3f2.y = arrf2[n4 + 1];
                vec3f2.z = arrf2[n4 + 2];
                vec3f3.x = arrf3[n4];
                vec3f3.y = arrf3[n4 + 1];
                vec3f3.z = arrf3[n4 + 2];
                meshTempState.triNormals[0].set(vec3f);
                meshTempState.triNormals[1].set(vec3f2);
                meshTempState.triNormals[2].set(vec3f3);
                MeshUtil.fixTSpace(meshTempState.triNormals);
                this.buildVSQuat(meshTempState.triNormals, meshTempState.quat);
                arrf4[n3 + 5] = meshTempState.quat.x;
                arrf4[n3 + 6] = meshTempState.quat.y;
                arrf4[n3 + 7] = meshTempState.quat.z;
                arrf4[n3 + 8] = meshTempState.quat.w;
            }
            ++n2;
            n3 += 9;
        }
    }

    private boolean doBuildPNTGeometry(float[] arrf, float[] arrf2, float[] arrf3, int[] arrn) {
        int n;
        int n2;
        if (this.point2vbMap == null) {
            this.point2vbMap = new HashMap();
        } else {
            this.point2vbMap.clear();
        }
        if (this.normal2vbMap == null) {
            this.normal2vbMap = new HashMap();
        } else {
            this.normal2vbMap.clear();
        }
        if (this.texCoord2vbMap == null) {
            this.texCoord2vbMap = new HashMap();
        } else {
            this.texCoord2vbMap.clear();
        }
        int n3 = VertexFormat.POINT_NORMAL_TEXCOORD.getVertexIndexSize();
        int n4 = n3 * 3;
        int n5 = VertexFormat.POINT_NORMAL_TEXCOORD.getPointIndexOffset();
        int n6 = VertexFormat.POINT_NORMAL_TEXCOORD.getNormalIndexOffset();
        int n7 = VertexFormat.POINT_NORMAL_TEXCOORD.getTexCoordIndexOffset();
        int n8 = arrf.length / 3;
        int n9 = arrf2.length / 3;
        int n10 = arrf3.length / 2;
        int n11 = arrn.length / n4;
        assert (n8 > 0 && n9 > 0 && n10 > 0 && n11 > 0);
        this.cachedNormals = new float[n8 * 3];
        this.cachedTangents = new float[n8 * 3];
        this.cachedBitangents = new float[n8 * 3];
        this.vertexBuffer = new float[n8 * 9];
        this.indexBuffer = new int[n11 * 3];
        int n12 = 0;
        int n13 = 0;
        MeshTempState meshTempState = MeshTempState.getInstance();
        for (n2 = 0; n2 < 3; ++n2) {
            if (meshTempState.triPoints[n2] == null) {
                meshTempState.triPoints[n2] = new Vec3f();
            }
            if (meshTempState.triTexCoords[n2] != null) continue;
            meshTempState.triTexCoords[n2] = new Vec2f();
        }
        for (n2 = 0; n2 < n11; ++n2) {
            int n14;
            int n15;
            n = n2 * n4;
            for (n15 = 0; n15 < 3; ++n15) {
                int n16;
                int n17;
                n14 = n + n15 * n3;
                int n18 = n14 + n5;
                int n19 = n14 + n6;
                int n20 = n14 + n7;
                Integer n21 = n13 / 9;
                if (this.vertexBuffer.length <= n13) {
                    n17 = n13 / 9;
                    n16 = n17 + Math.max(n17 >> 3, 6);
                    float[] arrf4 = new float[n16 * 9];
                    System.arraycopy(this.vertexBuffer, 0, arrf4, 0, this.vertexBuffer.length);
                    this.vertexBuffer = arrf4;
                    arrf4 = new float[n16 * 3];
                    System.arraycopy(this.cachedNormals, 0, arrf4, 0, this.cachedNormals.length);
                    this.cachedNormals = arrf4;
                    arrf4 = new float[n16 * 3];
                    System.arraycopy(this.cachedTangents, 0, arrf4, 0, this.cachedTangents.length);
                    this.cachedTangents = arrf4;
                    arrf4 = new float[n16 * 3];
                    System.arraycopy(this.cachedBitangents, 0, arrf4, 0, this.cachedBitangents.length);
                    this.cachedBitangents = arrf4;
                }
                n17 = arrn[n18] * 3;
                n16 = arrn[n19] * 3;
                int n22 = arrn[n20] * 2;
                meshTempState.triPointIndex[n15] = n17;
                meshTempState.triTexCoordIndex[n15] = n22;
                meshTempState.triVerts[n15] = n13 / 9;
                this.vertexBuffer[n13] = arrf[n17];
                this.vertexBuffer[n13 + 1] = arrf[n17 + 1];
                this.vertexBuffer[n13 + 2] = arrf[n17 + 2];
                this.vertexBuffer[n13 + 3] = arrf3[n22];
                this.vertexBuffer[n13 + 4] = arrf3[n22 + 1];
                int n23 = meshTempState.triVerts[n15] * 3;
                this.cachedNormals[n23] = arrf2[n16];
                this.cachedNormals[n23 + 1] = arrf2[n16 + 1];
                this.cachedNormals[n23 + 2] = arrf2[n16 + 2];
                n13 += 9;
                MeshGeomComp2VB meshGeomComp2VB = this.point2vbMap.get(n17);
                if (meshGeomComp2VB == null) {
                    meshGeomComp2VB = new MeshGeomComp2VB(n17, n21);
                    this.point2vbMap.put(n17, meshGeomComp2VB);
                } else {
                    meshGeomComp2VB.addLoc(n21);
                }
                MeshGeomComp2VB meshGeomComp2VB2 = this.normal2vbMap.get(n16);
                if (meshGeomComp2VB2 == null) {
                    meshGeomComp2VB2 = new MeshGeomComp2VB(n16, n21);
                    this.normal2vbMap.put(n16, meshGeomComp2VB2);
                } else {
                    meshGeomComp2VB2.addLoc(n21);
                }
                MeshGeomComp2VB meshGeomComp2VB3 = this.texCoord2vbMap.get(n22);
                if (meshGeomComp2VB3 == null) {
                    meshGeomComp2VB3 = new MeshGeomComp2VB(n22, n21);
                    this.texCoord2vbMap.put(n22, meshGeomComp2VB3);
                } else {
                    meshGeomComp2VB3.addLoc(n21);
                }
                this.indexBuffer[n12++] = n21;
            }
            for (n15 = 0; n15 < 3; ++n15) {
                meshTempState.triPoints[n15].x = arrf[meshTempState.triPointIndex[n15]];
                meshTempState.triPoints[n15].y = arrf[meshTempState.triPointIndex[n15] + 1];
                meshTempState.triPoints[n15].z = arrf[meshTempState.triPointIndex[n15] + 2];
                meshTempState.triTexCoords[n15].x = arrf3[meshTempState.triTexCoordIndex[n15]];
                meshTempState.triTexCoords[n15].y = arrf3[meshTempState.triTexCoordIndex[n15] + 1];
            }
            MeshUtil.computeTBNNormalized(meshTempState.triPoints[0], meshTempState.triPoints[1], meshTempState.triPoints[2], meshTempState.triTexCoords[0], meshTempState.triTexCoords[1], meshTempState.triTexCoords[2], meshTempState.triNormals);
            for (n15 = 0; n15 < 3; ++n15) {
                n14 = meshTempState.triVerts[n15] * 3;
                this.cachedTangents[n14] = meshTempState.triNormals[1].x;
                this.cachedTangents[n14 + 1] = meshTempState.triNormals[1].y;
                this.cachedTangents[n14 + 2] = meshTempState.triNormals[1].z;
                this.cachedBitangents[n14] = meshTempState.triNormals[2].x;
                this.cachedBitangents[n14 + 1] = meshTempState.triNormals[2].y;
                this.cachedBitangents[n14 + 2] = meshTempState.triNormals[2].z;
            }
        }
        this.numberOfVertices = n13 / 9;
        this.convertNormalsToQuats(meshTempState, this.numberOfVertices, this.cachedNormals, this.cachedTangents, this.cachedBitangents, this.vertexBuffer, null);
        this.indexBufferSize = n11 * 3;
        if (this.numberOfVertices > 65536) {
            return this.buildNativeGeometry(this.vertexBuffer, this.numberOfVertices * 9, this.indexBuffer, this.indexBufferSize);
        }
        if (this.indexBufferShort == null || this.indexBufferShort.length < this.indexBufferSize) {
            this.indexBufferShort = new short[this.indexBufferSize];
        }
        n2 = 0;
        for (n = 0; n < n11; ++n) {
            this.indexBufferShort[n2] = (short)this.indexBuffer[n2++];
            this.indexBufferShort[n2] = (short)this.indexBuffer[n2++];
            this.indexBufferShort[n2] = (short)this.indexBuffer[n2++];
        }
        this.indexBuffer = null;
        return this.buildNativeGeometry(this.vertexBuffer, this.numberOfVertices * 9, this.indexBufferShort, this.indexBufferSize);
    }

    private boolean updatePNTGeometry(float[] arrf, int[] arrn, float[] arrf2, int[] arrn2, float[] arrf3, int[] arrn3) {
        int n;
        int n2;
        int n3;
        int n4;
        if (this.dirtyVertices == null) {
            this.dirtyVertices = new boolean[this.numberOfVertices];
        }
        Arrays.fill(this.dirtyVertices, false);
        int n5 = arrn[0] / 3;
        int n6 = arrn[1] / 3;
        if (arrn[1] % 3 > 0) {
            ++n6;
        }
        if (n6 > 0) {
            for (n4 = 0; n4 < n6; ++n4) {
                int n7;
                n3 = (n5 + n4) * 3;
                MeshGeomComp2VB meshGeomComp2VB = this.point2vbMap.get(n3);
                assert (meshGeomComp2VB != null);
                if (meshGeomComp2VB == null) continue;
                int[] arrn4 = meshGeomComp2VB.getLocs();
                int n8 = meshGeomComp2VB.getValidLocs();
                if (arrn4 != null) {
                    for (n7 = 0; n7 < n8; ++n7) {
                        n2 = arrn4[n7] * 9;
                        this.vertexBuffer[n2] = arrf[n3];
                        this.vertexBuffer[n2 + 1] = arrf[n3 + 1];
                        this.vertexBuffer[n2 + 2] = arrf[n3 + 2];
                        this.dirtyVertices[arrn4[n7]] = true;
                    }
                    continue;
                }
                n7 = meshGeomComp2VB.getLoc();
                n2 = n7 * 9;
                this.vertexBuffer[n2] = arrf[n3];
                this.vertexBuffer[n2 + 1] = arrf[n3 + 1];
                this.vertexBuffer[n2 + 2] = arrf[n3 + 2];
                this.dirtyVertices[n7] = true;
            }
        }
        n4 = arrn3[0] / 2;
        n3 = arrn3[1] / 2;
        if (arrn3[1] % 2 > 0) {
            ++n3;
        }
        if (n3 > 0) {
            for (int i = 0; i < n3; ++i) {
                int n9;
                int n10;
                int n11 = (n4 + i) * 2;
                MeshGeomComp2VB meshGeomComp2VB = this.texCoord2vbMap.get(n11);
                assert (meshGeomComp2VB != null);
                if (meshGeomComp2VB == null) continue;
                int[] arrn5 = meshGeomComp2VB.getLocs();
                n2 = meshGeomComp2VB.getValidLocs();
                if (arrn5 != null) {
                    for (n10 = 0; n10 < n2; ++n10) {
                        n9 = arrn5[n10] * 9 + 3;
                        this.vertexBuffer[n9] = arrf3[n11];
                        this.vertexBuffer[n9 + 1] = arrf3[n11 + 1];
                        this.dirtyVertices[arrn5[n10]] = true;
                    }
                    continue;
                }
                n10 = meshGeomComp2VB.getLoc();
                n9 = n10 * 9 + 3;
                this.vertexBuffer[n9] = arrf3[n11];
                this.vertexBuffer[n9 + 1] = arrf3[n11 + 1];
                this.dirtyVertices[n10] = true;
            }
        }
        int n12 = arrn2[0] / 3;
        int n13 = arrn2[1] / 3;
        if (arrn2[1] % 3 > 0) {
            ++n13;
        }
        if (n13 > 0) {
            MeshTempState meshTempState = MeshTempState.getInstance();
            for (int i = 0; i < n13; ++i) {
                int n14;
                int n15;
                n2 = (n12 + i) * 3;
                MeshGeomComp2VB meshGeomComp2VB = this.normal2vbMap.get(n2);
                assert (meshGeomComp2VB != null);
                if (meshGeomComp2VB == null) continue;
                int[] arrn6 = meshGeomComp2VB.getLocs();
                int n16 = meshGeomComp2VB.getValidLocs();
                if (arrn6 != null) {
                    for (n15 = 0; n15 < n16; ++n15) {
                        n14 = arrn6[n15] * 3;
                        this.cachedNormals[n14] = arrf2[n2];
                        this.cachedNormals[n14 + 1] = arrf2[n2 + 1];
                        this.cachedNormals[n14 + 2] = arrf2[n2 + 2];
                        this.dirtyVertices[arrn6[n15]] = true;
                    }
                    continue;
                }
                n15 = meshGeomComp2VB.getLoc();
                n14 = n15 * 3;
                this.cachedNormals[n14] = arrf2[n2];
                this.cachedNormals[n14 + 1] = arrf2[n2 + 1];
                this.cachedNormals[n14 + 2] = arrf2[n2 + 2];
                this.dirtyVertices[n15] = true;
            }
        }
        MeshTempState meshTempState = MeshTempState.getInstance();
        for (n = 0; n < 3; ++n) {
            if (meshTempState.triPoints[n] == null) {
                meshTempState.triPoints[n] = new Vec3f();
            }
            if (meshTempState.triTexCoords[n] != null) continue;
            meshTempState.triTexCoords[n] = new Vec2f();
        }
        for (n = 0; n < this.numberOfVertices; n += 3) {
            int n17;
            if (!this.dirtyVertices[n] && !this.dirtyVertices[n + 1] && !this.dirtyVertices[n + 2]) continue;
            n2 = n * 9;
            for (n17 = 0; n17 < 3; ++n17) {
                meshTempState.triPoints[n17].x = this.vertexBuffer[n2];
                meshTempState.triPoints[n17].y = this.vertexBuffer[n2 + 1];
                meshTempState.triPoints[n17].z = this.vertexBuffer[n2 + 2];
                meshTempState.triTexCoords[n17].x = this.vertexBuffer[n2 + 3];
                meshTempState.triTexCoords[n17].y = this.vertexBuffer[n2 + 3 + 1];
                n2 += 9;
            }
            MeshUtil.computeTBNNormalized(meshTempState.triPoints[0], meshTempState.triPoints[1], meshTempState.triPoints[2], meshTempState.triTexCoords[0], meshTempState.triTexCoords[1], meshTempState.triTexCoords[2], meshTempState.triNormals);
            n17 = n * 3;
            for (int i = 0; i < 3; ++i) {
                this.cachedTangents[n17] = meshTempState.triNormals[1].x;
                this.cachedTangents[n17 + 1] = meshTempState.triNormals[1].y;
                this.cachedTangents[n17 + 2] = meshTempState.triNormals[1].z;
                this.cachedBitangents[n17] = meshTempState.triNormals[2].x;
                this.cachedBitangents[n17 + 1] = meshTempState.triNormals[2].y;
                this.cachedBitangents[n17 + 2] = meshTempState.triNormals[2].z;
                n17 += 3;
            }
        }
        this.convertNormalsToQuats(meshTempState, this.numberOfVertices, this.cachedNormals, this.cachedTangents, this.cachedBitangents, this.vertexBuffer, this.dirtyVertices);
        if (this.indexBuffer != null) {
            return this.buildNativeGeometry(this.vertexBuffer, this.numberOfVertices * 9, this.indexBuffer, this.indexBufferSize);
        }
        return this.buildNativeGeometry(this.vertexBuffer, this.numberOfVertices * 9, this.indexBufferShort, this.indexBufferSize);
    }

    @Override
    public boolean buildGeometry(boolean bl, float[] arrf, int[] arrn, float[] arrf2, int[] arrn2, float[] arrf3, int[] arrn3, int[] arrn4, int[] arrn5, int[] arrn6, int[] arrn7) {
        if (bl) {
            return this.buildPNTGeometry(arrf, arrn, arrf2, arrn2, arrf3, arrn3, arrn4, arrn5);
        }
        return this.buildPTGeometry(arrf, arrf3, arrn4, arrn6);
    }

    private boolean buildPNTGeometry(float[] arrf, int[] arrn, float[] arrf2, int[] arrn2, float[] arrf3, int[] arrn3, int[] arrn4, int[] arrn5) {
        boolean bl;
        boolean bl2 = arrn[1] > 0;
        boolean bl3 = arrn2[1] > 0;
        boolean bl4 = arrn3[1] > 0;
        boolean bl5 = arrn5[1] > 0;
        boolean bl6 = bl = !bl2 && !bl3 && !bl4 && !bl5;
        if (bl5) {
            bl = true;
        }
        if (!(bl || this.vertexBuffer == null || this.indexBuffer == null && this.indexBufferShort == null)) {
            return this.updatePNTGeometry(arrf, arrn, arrf2, arrn2, arrf3, arrn3);
        }
        return this.doBuildPNTGeometry(arrf, arrf2, arrf3, arrn4);
    }

    private boolean buildPTGeometry(float[] arrf, float[] arrf2, int[] arrn, int[] arrn2) {
        this.nVerts = arrf.length / 3;
        this.nTVerts = arrf2.length / 2;
        this.nFaces = arrn.length / (VertexFormat.POINT_TEXCOORD.getVertexIndexSize() * 3);
        assert (this.nVerts > 0 && this.nFaces > 0 && this.nTVerts > 0);
        this.pos = arrf;
        this.uv = arrf2;
        this.faces = arrn;
        this.smoothing = (int[])(arrn2.length == this.nFaces ? arrn2 : null);
        MeshTempState meshTempState = MeshTempState.getInstance();
        if (meshTempState.pool == null || meshTempState.pool.length < this.nFaces * 3) {
            meshTempState.pool = new MeshVertex[this.nFaces * 3];
        }
        if (meshTempState.indexBuffer == null || meshTempState.indexBuffer.length < this.nFaces * 3) {
            meshTempState.indexBuffer = new int[this.nFaces * 3];
        }
        if (meshTempState.pVertex == null || meshTempState.pVertex.length < this.nVerts) {
            meshTempState.pVertex = new MeshVertex[this.nVerts];
        } else {
            Arrays.fill(meshTempState.pVertex, 0, meshTempState.pVertex.length, null);
        }
        this.checkSmoothingGroup();
        this.computeTBNormal(meshTempState.pool, meshTempState.pVertex, meshTempState.indexBuffer);
        int n = MeshVertex.processVertices(meshTempState.pVertex, this.nVerts, this.allHardEdges, this.allSameSmoothing);
        if (meshTempState.vertexBuffer == null || meshTempState.vertexBuffer.length < n * 9) {
            meshTempState.vertexBuffer = new float[n * 9];
        }
        this.buildVertexBuffer(meshTempState.pVertex, meshTempState.vertexBuffer);
        if (n > 65536) {
            this.buildIndexBuffer(meshTempState.pool, meshTempState.indexBuffer, null);
            return this.buildNativeGeometry(meshTempState.vertexBuffer, n * 9, meshTempState.indexBuffer, this.nFaces * 3);
        }
        if (meshTempState.indexBufferShort == null || meshTempState.indexBufferShort.length < this.nFaces * 3) {
            meshTempState.indexBufferShort = new short[this.nFaces * 3];
        }
        this.buildIndexBuffer(meshTempState.pool, meshTempState.indexBuffer, meshTempState.indexBufferShort);
        return this.buildNativeGeometry(meshTempState.vertexBuffer, n * 9, meshTempState.indexBufferShort, this.nFaces * 3);
    }

    private void computeTBNormal(MeshVertex[] arrmeshVertex, MeshVertex[] arrmeshVertex2, int[] arrn) {
        MeshTempState meshTempState = MeshTempState.getInstance();
        int[] arrn2 = meshTempState.smFace;
        int[] arrn3 = meshTempState.triVerts;
        Vec3f[] arrvec3f = meshTempState.triPoints;
        Vec2f[] arrvec2f = meshTempState.triTexCoords;
        Vec3f[] arrvec3f2 = meshTempState.triNormals;
        String string = BaseMesh.class.getName();
        int n = 0;
        int n2 = 0;
        for (int i = 0; i < this.nFaces; ++i) {
            int n3;
            int n4 = i * 3;
            arrn2 = this.getFace(i, arrn2);
            arrn3[0] = arrn2[FaceMembers.POINT0.ordinal()];
            arrn3[1] = arrn2[FaceMembers.POINT1.ordinal()];
            arrn3[2] = arrn2[FaceMembers.POINT2.ordinal()];
            if (MeshUtil.isDeadFace(arrn3) && PlatformLogger.getLogger((String)string).isLoggable(PlatformLogger.Level.FINE)) {
                PlatformLogger.getLogger((String)string).fine("Dead face [" + arrn3[0] + ", " + arrn3[1] + ", " + arrn3[2] + "] @ face group " + i + "; nEmptyFaces = " + ++n);
            }
            for (n3 = 0; n3 < 3; ++n3) {
                arrvec3f[n3] = this.getVertex(arrn3[n3], arrvec3f[n3]);
            }
            arrn3[0] = arrn2[FaceMembers.TEXCOORD0.ordinal()];
            arrn3[1] = arrn2[FaceMembers.TEXCOORD1.ordinal()];
            arrn3[2] = arrn2[FaceMembers.TEXCOORD2.ordinal()];
            for (n3 = 0; n3 < 3; ++n3) {
                arrvec2f[n3] = this.getTVertex(arrn3[n3], arrvec2f[n3]);
            }
            MeshUtil.computeTBNNormalized(arrvec3f[0], arrvec3f[1], arrvec3f[2], arrvec2f[0], arrvec2f[1], arrvec2f[2], arrvec3f2);
            for (n3 = 0; n3 < 3; ++n3) {
                int n5;
                int n6;
                arrmeshVertex[n2] = arrmeshVertex[n2] == null ? new MeshVertex() : arrmeshVertex[n2];
                for (n6 = 0; n6 < 3; ++n6) {
                    arrmeshVertex[n2].norm[n6].set(arrvec3f2[n6]);
                }
                arrmeshVertex[n2].smGroup = arrn2[FaceMembers.SMOOTHING_GROUP.ordinal()];
                arrmeshVertex[n2].fIdx = i;
                arrmeshVertex[n2].tVert = arrn3[n3];
                arrmeshVertex[n2].index = -1;
                n6 = n3 == 0 ? FaceMembers.POINT0.ordinal() : (n3 == 1 ? FaceMembers.POINT1.ordinal() : FaceMembers.POINT2.ordinal());
                arrmeshVertex[n2].pVert = n5 = arrn2[n6];
                arrn[n4 + n3] = n5;
                arrmeshVertex[n2].next = arrmeshVertex2[n5];
                arrmeshVertex2[n5] = arrmeshVertex[n2];
                ++n2;
            }
        }
    }

    private void buildVSQuat(Vec3f[] arrvec3f, Quat4f quat4f) {
        Vec3f vec3f = MeshTempState.getInstance().vec3f1;
        vec3f.cross(arrvec3f[1], arrvec3f[2]);
        float f = arrvec3f[0].dot(vec3f);
        if (f < 0.0f) {
            arrvec3f[2].mul(-1.0f);
        }
        MeshUtil.buildQuat(arrvec3f, quat4f);
        if (f < 0.0f) {
            if (quat4f.w == 0.0f) {
                quat4f.w = 1.0E-10f;
            }
            quat4f.scale(-1.0f);
        }
    }

    private void buildVertexBuffer(MeshVertex[] arrmeshVertex, float[] arrf) {
        Quat4f quat4f = MeshTempState.getInstance().quat;
        int n = 0;
        int n2 = 0;
        for (int i = 0; i < this.nVerts; ++i) {
            MeshVertex meshVertex = arrmeshVertex[i];
            while (meshVertex != null) {
                if (meshVertex.index == n) {
                    int n3 = meshVertex.pVert * 3;
                    arrf[n2++] = this.pos[n3];
                    arrf[n2++] = this.pos[n3 + 1];
                    arrf[n2++] = this.pos[n3 + 2];
                    n3 = meshVertex.tVert * 2;
                    arrf[n2++] = this.uv[n3];
                    arrf[n2++] = this.uv[n3 + 1];
                    this.buildVSQuat(meshVertex.norm, quat4f);
                    arrf[n2++] = quat4f.x;
                    arrf[n2++] = quat4f.y;
                    arrf[n2++] = quat4f.z;
                    arrf[n2++] = quat4f.w;
                    ++n;
                }
                meshVertex = meshVertex.next;
            }
        }
    }

    private void buildIndexBuffer(MeshVertex[] arrmeshVertex, int[] arrn, short[] arrs) {
        for (int i = 0; i < this.nFaces; ++i) {
            int n;
            int n2 = i * 3;
            if (arrn[n2] != -1) {
                for (n = 0; n < 3; ++n) {
                    assert (arrmeshVertex[n2].fIdx == i);
                    if (arrs != null) {
                        arrs[n2 + n] = (short)arrmeshVertex[n2 + n].index;
                    } else {
                        arrn[n2 + n] = arrmeshVertex[n2 + n].index;
                    }
                    arrmeshVertex[n2 + n].next = null;
                }
                continue;
            }
            for (n = 0; n < 3; ++n) {
                if (arrs != null) {
                    arrs[n2 + n] = 0;
                    continue;
                }
                arrn[n2 + n] = 0;
            }
        }
    }

    public int getNumVerts() {
        return this.nVerts;
    }

    public int getNumTVerts() {
        return this.nTVerts;
    }

    public int getNumFaces() {
        return this.nFaces;
    }

    public Vec3f getVertex(int n, Vec3f vec3f) {
        if (vec3f == null) {
            vec3f = new Vec3f();
        }
        int n2 = n * 3;
        vec3f.set(this.pos[n2], this.pos[n2 + 1], this.pos[n2 + 2]);
        return vec3f;
    }

    public Vec2f getTVertex(int n, Vec2f vec2f) {
        if (vec2f == null) {
            vec2f = new Vec2f();
        }
        int n2 = n * 2;
        vec2f.set(this.uv[n2], this.uv[n2 + 1]);
        return vec2f;
    }

    private void checkSmoothingGroup() {
        if (this.smoothing == null || this.smoothing.length == 0) {
            this.allSameSmoothing = true;
            this.allHardEdges = false;
            return;
        }
        int n = 0;
        while (n + 1 < this.smoothing.length) {
            if (this.smoothing[n] != this.smoothing[n + 1]) {
                this.allSameSmoothing = false;
                this.allHardEdges = false;
                return;
            }
            ++n;
        }
        if (this.smoothing[0] == 0) {
            this.allSameSmoothing = false;
            this.allHardEdges = true;
        } else {
            this.allSameSmoothing = true;
            this.allHardEdges = false;
        }
    }

    public int[] getFace(int n, int[] arrn) {
        int n2 = n * 6;
        if (arrn == null || arrn.length < 7) {
            arrn = new int[7];
        }
        for (int i = 0; i < 6; ++i) {
            arrn[i] = this.faces[n2 + i];
        }
        arrn[6] = this.smoothing != null ? this.smoothing[n] : 1;
        return arrn;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    boolean test_isVertexBufferNull() {
        return this.vertexBuffer == null;
    }

    int test_getVertexBufferLength() {
        return this.vertexBuffer.length;
    }

    int test_getNumberOfVertices() {
        return this.numberOfVertices;
    }

    class MeshGeomComp2VB {
        private final int key;
        private final int loc;
        private int[] locs;
        private int validLocs;

        MeshGeomComp2VB(int n, int n2) {
            assert (n2 >= 0);
            this.key = n;
            this.loc = n2;
            this.locs = null;
            this.validLocs = 0;
        }

        void addLoc(int n) {
            if (this.locs == null) {
                this.locs = new int[3];
                this.locs[0] = this.loc;
                this.locs[1] = n;
                this.validLocs = 2;
            } else if (this.locs.length > this.validLocs) {
                this.locs[this.validLocs] = n;
                ++this.validLocs;
            } else {
                int[] arrn = new int[this.validLocs * 2];
                System.arraycopy(this.locs, 0, arrn, 0, this.locs.length);
                this.locs = arrn;
                this.locs[this.validLocs] = n;
                ++this.validLocs;
            }
        }

        int getKey() {
            return this.key;
        }

        int getLoc() {
            return this.loc;
        }

        int[] getLocs() {
            return this.locs;
        }

        int getValidLocs() {
            return this.validLocs;
        }
    }

    public static final class FaceMembers
    extends Enum<FaceMembers> {
        public static final /* enum */ FaceMembers POINT0 = new FaceMembers();
        public static final /* enum */ FaceMembers TEXCOORD0 = new FaceMembers();
        public static final /* enum */ FaceMembers POINT1 = new FaceMembers();
        public static final /* enum */ FaceMembers TEXCOORD1 = new FaceMembers();
        public static final /* enum */ FaceMembers POINT2 = new FaceMembers();
        public static final /* enum */ FaceMembers TEXCOORD2 = new FaceMembers();
        public static final /* enum */ FaceMembers SMOOTHING_GROUP = new FaceMembers();
        private static final /* synthetic */ FaceMembers[] $VALUES;

        public static FaceMembers[] values() {
            return (FaceMembers[])$VALUES.clone();
        }

        public static FaceMembers valueOf(String string) {
            return Enum.valueOf(FaceMembers.class, string);
        }

        private static /* synthetic */ FaceMembers[] $values() {
            return new FaceMembers[]{POINT0, TEXCOORD0, POINT1, TEXCOORD1, POINT2, TEXCOORD2, SMOOTHING_GROUP};
        }

        static {
            $VALUES = FaceMembers.$values();
        }
    }
}

