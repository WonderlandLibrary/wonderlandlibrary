// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.model.models;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Collection;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.HashMap;

public class OBJLoader
{
    private static final String COMMENT = "#";
    private static final String FACE = "f";
    private static final String POSITION = "v";
    private static final String TEX_COORDS = "vt";
    private static final String NORMAL = "vn";
    private static final String NEW_OBJECT = "o";
    private static final String NEW_GROUP = "g";
    private static final String USE_MATERIAL = "usemtl";
    private static final String NEW_MATERIAL = "mtllib";
    private boolean hasNormals;
    private boolean hasTexCoords;
    
    public OBJLoader() {
        this.hasNormals = false;
        this.hasTexCoords = false;
    }
    
    public HashMap<ObjObject, IndexedModel> loadModel(final String startPath, final String res) throws Exception {
        try {
            this.hasNormals = true;
            this.hasTexCoords = true;
            IndexedModel result = new IndexedModel();
            IndexedModel normalModel = new IndexedModel();
            final String[] lines = res.split("\n|\r");
            final int posOffset = 0;
            final boolean indicesOffset = false;
            final int texOffset = 0;
            final int normOffset = 0;
            final ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
            final ArrayList<Vector2f> texCoords = new ArrayList<Vector2f>();
            final ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
            ArrayList<OBJIndex> indices = new ArrayList<OBJIndex>();
            final ArrayList<Material> materials = new ArrayList<Material>();
            final HashMapWithDefault<OBJIndex, Integer> resultIndexMap = new HashMapWithDefault<OBJIndex, Integer>();
            final HashMapWithDefault<Integer, Integer> normalIndexMap = new HashMapWithDefault<Integer, Integer>();
            final HashMapWithDefault<Integer, Integer> indexMap = new HashMapWithDefault<Integer, Integer>();
            resultIndexMap.setDefault(-1);
            normalIndexMap.setDefault(-1);
            indexMap.setDefault(-1);
            final HashMap<ObjObject, IndexedModel> map = new HashMap<ObjObject, IndexedModel>();
            ObjObject currentObject = null;
            final HashMap<ObjObject, IndexedModel[]> objects = new HashMap<ObjObject, IndexedModel[]>();
            objects.put(currentObject = new ObjObject("main"), new IndexedModel[] { result, normalModel });
            final String[] var21 = lines;
            for (int var22 = lines.length, i = 0; i < var22; ++i) {
                final String line = var21[i];
                if (line != null && !line.trim().equals("")) {
                    final String[] parts = trim(line.split(" "));
                    if (parts.length != 0 && !parts[0].equals("#")) {
                        if (parts[0].equals("v")) {
                            positions.add(new Vector3f(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3])));
                        }
                        else if (parts[0].equals("f")) {
                            for (int i2 = 0; i2 < parts.length - 3; ++i2) {
                                indices.add(this.parseOBJIndex(parts[1], posOffset, texOffset, normOffset));
                                indices.add(this.parseOBJIndex(parts[2 + i2], posOffset, texOffset, normOffset));
                                indices.add(this.parseOBJIndex(parts[3 + i2], posOffset, texOffset, normOffset));
                            }
                        }
                        else if (parts[0].equals("vn")) {
                            normals.add(new Vector3f(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3])));
                        }
                        else if (parts[0].equals("vt")) {
                            texCoords.add(new Vector2f(Float.parseFloat(parts[1]), Float.parseFloat(parts[2])));
                        }
                        else if (parts[0].equals("mtllib")) {
                            final String path = startPath + parts[1];
                            final MtlMaterialLib material = new MtlMaterialLib(path);
                            material.parse(this.read(OBJLoader.class.getResourceAsStream(path)));
                            materials.addAll(material.getMaterials());
                        }
                        else if (parts[0].equals("usemtl")) {
                            currentObject.material = this.getMaterial(materials, parts[1]);
                        }
                        else if (parts[0].equals("o") || parts[0].equals("g")) {
                            result.getObjIndices().addAll(indices);
                            normalModel.getObjIndices().addAll(indices);
                            result = new IndexedModel();
                            normalModel = new IndexedModel();
                            indices.clear();
                            objects.put(currentObject = new ObjObject(parts[1]), new IndexedModel[] { result, normalModel });
                        }
                    }
                }
            }
            result.getObjIndices().addAll(indices);
            normalModel.getObjIndices().addAll(indices);
            for (final ObjObject object : objects.keySet()) {
                result = objects.get(object)[0];
                normalModel = objects.get(object)[1];
                indices = result.getObjIndices();
                map.put(object, result);
                object.center = result.computeCenter();
                for (int i = 0; i < indices.size(); ++i) {
                    final OBJIndex current = indices.get(i);
                    final Vector3f pos = positions.get(current.positionIndex);
                    Vector2f texCoord;
                    if (this.hasTexCoords) {
                        texCoord = texCoords.get(current.texCoordsIndex);
                    }
                    else {
                        texCoord = new Vector2f();
                    }
                    Vector3f normal;
                    if (this.hasNormals) {
                        try {
                            normal = normals.get(current.normalIndex);
                        }
                        catch (Exception var24) {
                            normal = new Vector3f();
                        }
                    }
                    else {
                        normal = new Vector3f();
                    }
                    int modelVertexIndex = resultIndexMap.get(current);
                    if (modelVertexIndex == -1) {
                        resultIndexMap.put(current, result.getPositions().size());
                        modelVertexIndex = result.getPositions().size();
                        result.getPositions().add(pos);
                        result.getTexCoords().add(texCoord);
                        if (this.hasNormals) {
                            result.getNormals().add(normal);
                        }
                        result.getTangents().add(new Vector3f());
                    }
                    int normalModelIndex = normalIndexMap.get(current.positionIndex);
                    if (normalModelIndex == -1) {
                        normalModelIndex = normalModel.getPositions().size();
                        normalIndexMap.put(current.positionIndex, normalModelIndex);
                        normalModel.getPositions().add(pos);
                        normalModel.getTexCoords().add(texCoord);
                        normalModel.getNormals().add(normal);
                        normalModel.getTangents().add(new Vector3f());
                    }
                    result.getIndices().add(modelVertexIndex);
                    normalModel.getIndices().add(normalModelIndex);
                    indexMap.put(modelVertexIndex, normalModelIndex);
                }
                if (!this.hasNormals) {
                    normalModel.computeNormals();
                    for (int i = 0; i < result.getNormals().size(); ++i) {
                        result.getNormals().add(normalModel.getNormals().get(indexMap.get(i)));
                    }
                }
            }
            return map;
        }
        catch (Exception var23) {
            throw new RuntimeException("Error while loading model", var23);
        }
    }
    
    private Material getMaterial(final ArrayList<Material> materials, final String id) {
        for (final Material mat : materials) {
            if (mat.getName().equals(id)) {
                return mat;
            }
        }
        return null;
    }
    
    protected String read(final InputStream resource) throws IOException {
        final byte[] buffer = new byte[65565];
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        int i;
        while ((i = resource.read(buffer, 0, buffer.length)) != -1) {
            out.write(buffer, 0, i);
        }
        out.flush();
        out.close();
        return new String(out.toByteArray(), "UTF-8");
    }
    
    public OBJIndex parseOBJIndex(final String token, final int posOffset, final int texCoordsOffset, final int normalOffset) {
        final OBJIndex index = new OBJIndex();
        final String[] values = token.split("/");
        index.positionIndex = Integer.parseInt(values[0]) - 1 - posOffset;
        if (values.length > 1) {
            if (values[1] != null && !values[1].equals("")) {
                index.texCoordsIndex = Integer.parseInt(values[1]) - 1 - texCoordsOffset;
            }
            this.hasTexCoords = true;
            if (values.length > 2) {
                index.normalIndex = Integer.parseInt(values[2]) - 1 - normalOffset;
                this.hasNormals = true;
            }
        }
        return index;
    }
    
    public static String[] trim(final String[] split) {
        final ArrayList<String> strings = new ArrayList<String>();
        final String[] var2 = split;
        for (int var3 = split.length, var4 = 0; var4 < var3; ++var4) {
            final String s = var2[var4];
            if (s != null && !s.trim().equals("")) {
                strings.add(s);
            }
        }
        return strings.toArray(new String[0]);
    }
    
    public static final class OBJIndex
    {
        int positionIndex;
        int texCoordsIndex;
        int normalIndex;
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof OBJIndex)) {
                return false;
            }
            final OBJIndex index = (OBJIndex)o;
            return index.normalIndex == this.normalIndex && index.positionIndex == this.positionIndex && index.texCoordsIndex == this.texCoordsIndex;
        }
        
        @Override
        public int hashCode() {
            final boolean base = true;
            final boolean multiplier = true;
            int result = 17;
            result = 31 * result + this.positionIndex;
            result = 31 * result + this.texCoordsIndex;
            result = 31 * result + this.normalIndex;
            return result;
        }
    }
}
