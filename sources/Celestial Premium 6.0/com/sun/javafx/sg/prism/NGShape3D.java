/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.application.ConditionalFeature
 *  javafx.application.Platform
 *  javafx.scene.shape.CullFace
 *  javafx.scene.shape.DrawMode
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.sg.prism.NGAmbientLight;
import com.sun.javafx.sg.prism.NGLightBase;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGPhongMaterial;
import com.sun.javafx.sg.prism.NGPointLight;
import com.sun.javafx.sg.prism.NGTriangleMesh;
import com.sun.javafx.util.Utils;
import com.sun.prism.Graphics;
import com.sun.prism.Material;
import com.sun.prism.MeshView;
import com.sun.prism.PrinterGraphics;
import com.sun.prism.ResourceFactory;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;

public abstract class NGShape3D
extends NGNode {
    private NGPhongMaterial material;
    private DrawMode drawMode;
    private CullFace cullFace;
    private boolean materialDirty = false;
    private boolean drawModeDirty = false;
    NGTriangleMesh mesh;
    private MeshView meshView;

    public void setMaterial(NGPhongMaterial nGPhongMaterial) {
        this.material = nGPhongMaterial;
        this.materialDirty = true;
        this.visualsChanged();
    }

    public void setDrawMode(Object object) {
        this.drawMode = (DrawMode)object;
        this.drawModeDirty = true;
        this.visualsChanged();
    }

    public void setCullFace(Object object) {
        this.cullFace = (CullFace)object;
        this.visualsChanged();
    }

    void invalidate() {
        this.meshView = null;
        this.visualsChanged();
    }

    private void renderMeshView(Graphics graphics) {
        graphics.setup3DRendering();
        ResourceFactory resourceFactory = graphics.getResourceFactory();
        if (resourceFactory == null || resourceFactory.isDisposed()) {
            return;
        }
        if (this.meshView != null && !this.meshView.isValid()) {
            this.meshView.dispose();
            this.meshView = null;
        }
        if (this.meshView == null && this.mesh != null) {
            this.meshView = resourceFactory.createMeshView(this.mesh.createMesh(resourceFactory));
            this.drawModeDirty = true;
            this.materialDirty = true;
        }
        if (this.meshView == null || !this.mesh.validate()) {
            return;
        }
        Material material = this.material.createMaterial(resourceFactory);
        if (this.materialDirty) {
            this.meshView.setMaterial(material);
            this.materialDirty = false;
        }
        int n = this.cullFace.ordinal();
        if (this.cullFace.ordinal() != MeshView.CULL_NONE && graphics.getTransformNoClone().getDeterminant() < 0.0) {
            n = n == MeshView.CULL_BACK ? MeshView.CULL_FRONT : MeshView.CULL_BACK;
        }
        this.meshView.setCullingMode(n);
        if (this.drawModeDirty) {
            this.meshView.setWireframe(this.drawMode == DrawMode.LINE);
            this.drawModeDirty = false;
        }
        int n2 = 0;
        if (graphics.getLights() == null || graphics.getLights()[0] == null) {
            this.meshView.setAmbientLight(0.0f, 0.0f, 0.0f);
            Vec3d vec3d = graphics.getCameraNoClone().getPositionInWorld(null);
            this.meshView.setLight(n2++, (float)vec3d.x, (float)vec3d.y, (float)vec3d.z, 1.0f, 1.0f, 1.0f, 1.0f, NGPointLight.getDefaultCa(), NGPointLight.getDefaultLa(), NGPointLight.getDefaultQa(), NGPointLight.getDefaultMaxRange(), (float)NGPointLight.getSimulatedDirection().getX(), (float)NGPointLight.getSimulatedDirection().getY(), (float)NGPointLight.getSimulatedDirection().getZ(), NGPointLight.getSimulatedInnerAngle(), NGPointLight.getSimulatedOuterAngle(), NGPointLight.getSimulatedFalloff());
        } else {
            float f = 0.0f;
            float f2 = 0.0f;
            float f3 = 0.0f;
            for (NGLightBase nGLightBase : graphics.getLights()) {
                if (nGLightBase == null) break;
                if (!nGLightBase.affects(this)) continue;
                float f4 = nGLightBase.getColor().getRed();
                float f5 = nGLightBase.getColor().getGreen();
                float f6 = nGLightBase.getColor().getBlue();
                if (f4 == 0.0f && f5 == 0.0f && f6 == 0.0f) continue;
                if (nGLightBase instanceof NGPointLight) {
                    NGPointLight nGPointLight = (NGPointLight)nGLightBase;
                    Affine3D affine3D = nGPointLight.getWorldTransform();
                    this.meshView.setLight(n2++, (float)affine3D.getMxt(), (float)affine3D.getMyt(), (float)affine3D.getMzt(), f4, f5, f6, 1.0f, nGPointLight.getCa(), nGPointLight.getLa(), nGPointLight.getQa(), nGPointLight.getMaxRange(), (float)nGPointLight.getDirection().getX(), (float)nGPointLight.getDirection().getY(), (float)nGPointLight.getDirection().getZ(), nGPointLight.getInnerAngle(), nGPointLight.getOuterAngle(), nGPointLight.getFalloff());
                    continue;
                }
                if (!(nGLightBase instanceof NGAmbientLight)) continue;
                f += f4;
                f3 += f5;
                f2 += f6;
            }
            f = Utils.clamp(0.0f, f, 1.0f);
            f3 = Utils.clamp(0.0f, f3, 1.0f);
            f2 = Utils.clamp(0.0f, f2, 1.0f);
            this.meshView.setAmbientLight(f, f3, f2);
        }
        while (n2 < 3) {
            this.meshView.setLight(n2++, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        }
        this.meshView.render(graphics);
    }

    public void setMesh(NGTriangleMesh nGTriangleMesh) {
        this.mesh = nGTriangleMesh;
        this.meshView = null;
        this.visualsChanged();
    }

    @Override
    protected void renderContent(Graphics graphics) {
        if (!Platform.isSupported((ConditionalFeature)ConditionalFeature.SCENE3D) || this.material == null || graphics instanceof PrinterGraphics) {
            return;
        }
        this.renderMeshView(graphics);
    }

    @Override
    boolean isShape3D() {
        return true;
    }

    @Override
    protected boolean hasOverlappingContents() {
        return false;
    }

    @Override
    public void release() {
    }
}

