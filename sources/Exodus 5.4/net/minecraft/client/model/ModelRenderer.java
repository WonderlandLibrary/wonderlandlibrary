/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.lwjgl.opengl.GL11
 */
package net.minecraft.client.model;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.TextureOffset;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import org.lwjgl.opengl.GL11;

public class ModelRenderer {
    public boolean mirror;
    private ModelBase baseModel;
    public float rotationPointZ;
    public List<ModelBox> cubeList = Lists.newArrayList();
    public float offsetY;
    public float rotationPointX;
    public float offsetZ;
    public float rotateAngleZ;
    public float rotateAngleY;
    private int textureOffsetX;
    public List<ModelRenderer> childModels;
    private boolean compiled;
    public float rotateAngleX;
    public float textureWidth = 64.0f;
    private int displayList;
    public float textureHeight = 32.0f;
    public boolean isHidden;
    public boolean showModel = true;
    public float rotationPointY;
    public final String boxName;
    public float offsetX;
    private int textureOffsetY;

    public void postRender(float f) {
        if (!this.isHidden && this.showModel) {
            if (!this.compiled) {
                this.compileDisplayList(f);
            }
            if (this.rotateAngleX == 0.0f && this.rotateAngleY == 0.0f && this.rotateAngleZ == 0.0f) {
                if (this.rotationPointX != 0.0f || this.rotationPointY != 0.0f || this.rotationPointZ != 0.0f) {
                    GlStateManager.translate(this.rotationPointX * f, this.rotationPointY * f, this.rotationPointZ * f);
                }
            } else {
                GlStateManager.translate(this.rotationPointX * f, this.rotationPointY * f, this.rotationPointZ * f);
                if (this.rotateAngleZ != 0.0f) {
                    GlStateManager.rotate(this.rotateAngleZ * 57.295776f, 0.0f, 0.0f, 1.0f);
                }
                if (this.rotateAngleY != 0.0f) {
                    GlStateManager.rotate(this.rotateAngleY * 57.295776f, 0.0f, 1.0f, 0.0f);
                }
                if (this.rotateAngleX != 0.0f) {
                    GlStateManager.rotate(this.rotateAngleX * 57.295776f, 1.0f, 0.0f, 0.0f);
                }
            }
        }
    }

    public void addChild(ModelRenderer modelRenderer) {
        if (this.childModels == null) {
            this.childModels = Lists.newArrayList();
        }
        this.childModels.add(modelRenderer);
    }

    private void compileDisplayList(float f) {
        this.displayList = GLAllocation.generateDisplayLists(1);
        GL11.glNewList((int)this.displayList, (int)4864);
        WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
        int n = 0;
        while (n < this.cubeList.size()) {
            this.cubeList.get(n).render(worldRenderer, f);
            ++n;
        }
        GL11.glEndList();
        this.compiled = true;
    }

    public void setRotationPoint(float f, float f2, float f3) {
        this.rotationPointX = f;
        this.rotationPointY = f2;
        this.rotationPointZ = f3;
    }

    public ModelRenderer setTextureOffset(int n, int n2) {
        this.textureOffsetX = n;
        this.textureOffsetY = n2;
        return this;
    }

    public ModelRenderer(ModelBase modelBase, int n, int n2) {
        this(modelBase);
        this.setTextureOffset(n, n2);
    }

    public ModelRenderer(ModelBase modelBase, String string) {
        this.baseModel = modelBase;
        modelBase.boxList.add(this);
        this.boxName = string;
        this.setTextureSize(modelBase.textureWidth, modelBase.textureHeight);
    }

    public ModelRenderer addBox(float f, float f2, float f3, int n, int n2, int n3, boolean bl) {
        this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, f, f2, f3, n, n2, n3, 0.0f, bl));
        return this;
    }

    public void render(float f) {
        if (!this.isHidden && this.showModel) {
            if (!this.compiled) {
                this.compileDisplayList(f);
            }
            GlStateManager.translate(this.offsetX, this.offsetY, this.offsetZ);
            if (this.rotateAngleX == 0.0f && this.rotateAngleY == 0.0f && this.rotateAngleZ == 0.0f) {
                if (this.rotationPointX == 0.0f && this.rotationPointY == 0.0f && this.rotationPointZ == 0.0f) {
                    GlStateManager.callList(this.displayList);
                    if (this.childModels != null) {
                        int n = 0;
                        while (n < this.childModels.size()) {
                            this.childModels.get(n).render(f);
                            ++n;
                        }
                    }
                } else {
                    GlStateManager.translate(this.rotationPointX * f, this.rotationPointY * f, this.rotationPointZ * f);
                    GlStateManager.callList(this.displayList);
                    if (this.childModels != null) {
                        int n = 0;
                        while (n < this.childModels.size()) {
                            this.childModels.get(n).render(f);
                            ++n;
                        }
                    }
                    GlStateManager.translate(-this.rotationPointX * f, -this.rotationPointY * f, -this.rotationPointZ * f);
                }
            } else {
                GlStateManager.pushMatrix();
                GlStateManager.translate(this.rotationPointX * f, this.rotationPointY * f, this.rotationPointZ * f);
                if (this.rotateAngleZ != 0.0f) {
                    GlStateManager.rotate(this.rotateAngleZ * 57.295776f, 0.0f, 0.0f, 1.0f);
                }
                if (this.rotateAngleY != 0.0f) {
                    GlStateManager.rotate(this.rotateAngleY * 57.295776f, 0.0f, 1.0f, 0.0f);
                }
                if (this.rotateAngleX != 0.0f) {
                    GlStateManager.rotate(this.rotateAngleX * 57.295776f, 1.0f, 0.0f, 0.0f);
                }
                GlStateManager.callList(this.displayList);
                if (this.childModels != null) {
                    int n = 0;
                    while (n < this.childModels.size()) {
                        this.childModels.get(n).render(f);
                        ++n;
                    }
                }
                GlStateManager.popMatrix();
            }
            GlStateManager.translate(-this.offsetX, -this.offsetY, -this.offsetZ);
        }
    }

    public ModelRenderer setTextureSize(int n, int n2) {
        this.textureWidth = n;
        this.textureHeight = n2;
        return this;
    }

    public ModelRenderer addBox(float f, float f2, float f3, int n, int n2, int n3) {
        this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, f, f2, f3, n, n2, n3, 0.0f));
        return this;
    }

    public void renderWithRotation(float f) {
        if (!this.isHidden && this.showModel) {
            if (!this.compiled) {
                this.compileDisplayList(f);
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate(this.rotationPointX * f, this.rotationPointY * f, this.rotationPointZ * f);
            if (this.rotateAngleY != 0.0f) {
                GlStateManager.rotate(this.rotateAngleY * 57.295776f, 0.0f, 1.0f, 0.0f);
            }
            if (this.rotateAngleX != 0.0f) {
                GlStateManager.rotate(this.rotateAngleX * 57.295776f, 1.0f, 0.0f, 0.0f);
            }
            if (this.rotateAngleZ != 0.0f) {
                GlStateManager.rotate(this.rotateAngleZ * 57.295776f, 0.0f, 0.0f, 1.0f);
            }
            GlStateManager.callList(this.displayList);
            GlStateManager.popMatrix();
        }
    }

    public ModelRenderer addBox(String string, float f, float f2, float f3, int n, int n2, int n3) {
        string = String.valueOf(this.boxName) + "." + string;
        TextureOffset textureOffset = this.baseModel.getTextureOffset(string);
        this.setTextureOffset(textureOffset.textureOffsetX, textureOffset.textureOffsetY);
        this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, f, f2, f3, n, n2, n3, 0.0f).setBoxName(string));
        return this;
    }

    public void addBox(float f, float f2, float f3, int n, int n2, int n3, float f4) {
        this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, f, f2, f3, n, n2, n3, f4));
    }

    public ModelRenderer(ModelBase modelBase) {
        this(modelBase, null);
    }
}

