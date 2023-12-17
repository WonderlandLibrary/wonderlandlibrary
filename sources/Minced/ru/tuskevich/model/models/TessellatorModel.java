// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.model.models;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.Minecraft;
import java.util.Iterator;
import java.util.Set;
import java.util.HashMap;
import java.nio.charset.StandardCharsets;

public class TessellatorModel extends ObjModel
{
    public TessellatorModel(final String string) {
        super(string);
        try {
            final String content = new String(this.read(Model.class.getResourceAsStream(string)), StandardCharsets.UTF_8);
            final String startPath = string.substring(0, string.lastIndexOf(47) + 1);
            final HashMap<ObjObject, IndexedModel> map = new OBJLoader().loadModel(startPath, content);
            this.objObjects.clear();
            final Set<ObjObject> keys = map.keySet();
            for (final ObjObject object : keys) {
                final Mesh mesh = new Mesh();
                object.mesh = mesh;
                this.objObjects.add(object);
                map.get(object).toMesh(mesh);
            }
        }
        catch (Exception var9) {
            var9.printStackTrace();
        }
    }
    
    public void renderImpl() {
        final Vec3d v;
        final double aDist;
        final double bDist;
        this.objObjects.sort((a, b) -> {
            v = Minecraft.getMinecraft().getRenderViewEntity().getPositionVector();
            aDist = v.distanceTo(new Vec3d(a.center.x, a.center.y, a.center.z));
            bDist = v.distanceTo(new Vec3d(b.center.x, b.center.y, b.center.z));
            return Double.compare(aDist, bDist);
        });
        for (final ObjObject object : this.objObjects) {
            this.renderGroup(object);
        }
    }
    
    public void renderGroupsImpl(final String group) {
        for (final ObjObject object : this.objObjects) {
            if (object.getName().equals(group)) {
                this.renderGroup(object);
            }
        }
    }
    
    public void renderGroupImpl(final ObjObject obj) {
        final Tessellator tess = Tessellator.getInstance();
        final BufferBuilder renderer = tess.getBuffer();
        if (obj.mesh != null) {
            if (obj.material != null) {
                GlStateManager.bindTexture(obj.material.diffuseTexture);
            }
            final int[] indices = obj.mesh.indices;
            final Vertex[] vertices = obj.mesh.vertices;
            renderer.begin(4, DefaultVertexFormats.POSITION_TEX_NORMAL);
            for (int i = 0; i < indices.length; i += 3) {
                final int i2 = indices[i];
                final int i3 = indices[i + 1];
                final int i4 = indices[i + 2];
                final Vertex v0 = vertices[i2];
                final Vertex v2 = vertices[i3];
                final Vertex v3 = vertices[i4];
                renderer.pos(v0.pos().x, v0.pos().y, v0.pos().z).tex(v0.texCoords().x, 1.0f - v0.texCoords().y).normal(v0.normal().x, v0.normal().y, v0.normal().z).endVertex();
                renderer.pos(v2.pos().x, v2.pos().y, v2.pos().z).tex(v2.texCoords().x, 1.0f - v2.texCoords().y).normal(v2.normal().x, v2.normal().y, v2.normal().z).endVertex();
                renderer.pos(v3.pos().x, v3.pos().y, v3.pos().z).tex(v3.texCoords().x, 1.0f - v3.texCoords().y).normal(v3.normal().x, v3.normal().y, v3.normal().z).endVertex();
            }
            tess.draw();
        }
    }
    
    @Override
    public boolean fireEvent(final ObjEvent event) {
        return true;
    }
    
    @Deprecated
    public void regenerateNormals() {
    }
}
