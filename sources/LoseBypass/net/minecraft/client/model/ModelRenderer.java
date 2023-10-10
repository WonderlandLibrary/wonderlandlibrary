/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
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
    public float textureWidth = 64.0f;
    public float textureHeight = 32.0f;
    private int textureOffsetX;
    private int textureOffsetY;
    public float rotationPointX;
    public float rotationPointY;
    public float rotationPointZ;
    public float rotateAngleX;
    public float rotateAngleY;
    public float rotateAngleZ;
    private boolean compiled;
    private int displayList;
    public boolean mirror;
    public boolean showModel = true;
    public boolean isHidden;
    public List<ModelBox> cubeList = Lists.newArrayList();
    public List<ModelRenderer> childModels;
    public final String boxName;
    private ModelBase baseModel;
    public float offsetX;
    public float offsetY;
    public float offsetZ;

    public ModelRenderer(ModelBase model, String boxNameIn) {
        this.baseModel = model;
        model.boxList.add(this);
        this.boxName = boxNameIn;
        this.setTextureSize(model.textureWidth, model.textureHeight);
    }

    public ModelRenderer(ModelBase model) {
        this(model, null);
    }

    public ModelRenderer(ModelBase model, int texOffX, int texOffY) {
        this(model);
        this.setTextureOffset(texOffX, texOffY);
    }

    public void addChild(ModelRenderer renderer) {
        if (this.childModels == null) {
            this.childModels = Lists.newArrayList();
        }
        this.childModels.add(renderer);
    }

    public ModelRenderer setTextureOffset(int x, int y) {
        this.textureOffsetX = x;
        this.textureOffsetY = y;
        return this;
    }

    public ModelRenderer addBox(String partName, float offX, float offY, float offZ, int width, int height, int depth) {
        partName = this.boxName + "." + partName;
        TextureOffset textureoffset = this.baseModel.getTextureOffset(partName);
        this.setTextureOffset(textureoffset.textureOffsetX, textureoffset.textureOffsetY);
        this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, offX, offY, offZ, width, height, depth, 0.0f).setBoxName(partName));
        return this;
    }

    public ModelRenderer addBox(float offX, float offY, float offZ, int width, int height, int depth) {
        this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, offX, offY, offZ, width, height, depth, 0.0f));
        return this;
    }

    public ModelRenderer addBox(float p_178769_1_, float p_178769_2_, float p_178769_3_, int p_178769_4_, int p_178769_5_, int p_178769_6_, boolean p_178769_7_) {
        this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, p_178769_1_, p_178769_2_, p_178769_3_, p_178769_4_, p_178769_5_, p_178769_6_, 0.0f, p_178769_7_));
        return this;
    }

    public void addBox(float p_78790_1_, float p_78790_2_, float p_78790_3_, int width, int height, int depth, float scaleFactor) {
        this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, p_78790_1_, p_78790_2_, p_78790_3_, width, height, depth, scaleFactor));
    }

    public void setRotationPoint(float rotationPointXIn, float rotationPointYIn, float rotationPointZIn) {
        this.rotationPointX = rotationPointXIn;
        this.rotationPointY = rotationPointYIn;
        this.rotationPointZ = rotationPointZIn;
    }

    public void render(float p_78785_1_) {
        if (this.isHidden) return;
        if (!this.showModel) return;
        if (!this.compiled) {
            this.compileDisplayList(p_78785_1_);
        }
        GlStateManager.translate(this.offsetX, this.offsetY, this.offsetZ);
        if (this.rotateAngleX == 0.0f && this.rotateAngleY == 0.0f && this.rotateAngleZ == 0.0f) {
            if (this.rotationPointX == 0.0f && this.rotationPointY == 0.0f && this.rotationPointZ == 0.0f) {
                GlStateManager.callList(this.displayList);
                if (this.childModels != null) {
                    for (int k = 0; k < this.childModels.size(); ++k) {
                        this.childModels.get(k).render(p_78785_1_);
                    }
                }
            } else {
                GlStateManager.translate(this.rotationPointX * p_78785_1_, this.rotationPointY * p_78785_1_, this.rotationPointZ * p_78785_1_);
                GlStateManager.callList(this.displayList);
                if (this.childModels != null) {
                    for (int j = 0; j < this.childModels.size(); ++j) {
                        this.childModels.get(j).render(p_78785_1_);
                    }
                }
                GlStateManager.translate(-this.rotationPointX * p_78785_1_, -this.rotationPointY * p_78785_1_, -this.rotationPointZ * p_78785_1_);
            }
        } else {
            GlStateManager.pushMatrix();
            GlStateManager.translate(this.rotationPointX * p_78785_1_, this.rotationPointY * p_78785_1_, this.rotationPointZ * p_78785_1_);
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
                for (int i = 0; i < this.childModels.size(); ++i) {
                    this.childModels.get(i).render(p_78785_1_);
                }
            }
            GlStateManager.popMatrix();
        }
        GlStateManager.translate(-this.offsetX, -this.offsetY, -this.offsetZ);
    }

    public void renderWithRotation(float p_78791_1_) {
        if (this.isHidden) return;
        if (!this.showModel) return;
        if (!this.compiled) {
            this.compileDisplayList(p_78791_1_);
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.rotationPointX * p_78791_1_, this.rotationPointY * p_78791_1_, this.rotationPointZ * p_78791_1_);
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

    public void postRender(float scale) {
        if (this.isHidden) return;
        if (!this.showModel) return;
        if (!this.compiled) {
            this.compileDisplayList(scale);
        }
        if (this.rotateAngleX == 0.0f && this.rotateAngleY == 0.0f && this.rotateAngleZ == 0.0f) {
            if (this.rotationPointX == 0.0f && this.rotationPointY == 0.0f) {
                if (this.rotationPointZ == 0.0f) return;
            }
            GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
            return;
        }
        GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
        if (this.rotateAngleZ != 0.0f) {
            GlStateManager.rotate(this.rotateAngleZ * 57.295776f, 0.0f, 0.0f, 1.0f);
        }
        if (this.rotateAngleY != 0.0f) {
            GlStateManager.rotate(this.rotateAngleY * 57.295776f, 0.0f, 1.0f, 0.0f);
        }
        if (this.rotateAngleX == 0.0f) return;
        GlStateManager.rotate(this.rotateAngleX * 57.295776f, 1.0f, 0.0f, 0.0f);
    }

    private void compileDisplayList(float scale) {
        this.displayList = GLAllocation.generateDisplayLists(1);
        GL11.glNewList((int)this.displayList, (int)4864);
        WorldRenderer worldrenderer = Tessellator.getInstance().getWorldRenderer();
        int i = 0;
        while (true) {
            if (i >= this.cubeList.size()) {
                GL11.glEndList();
                this.compiled = true;
                return;
            }
            this.cubeList.get(i).render(worldrenderer, scale);
            ++i;
        }
    }

    public ModelRenderer setTextureSize(int textureWidthIn, int textureHeightIn) {
        this.textureWidth = textureWidthIn;
        this.textureHeight = textureHeightIn;
        return this;
    }
}

