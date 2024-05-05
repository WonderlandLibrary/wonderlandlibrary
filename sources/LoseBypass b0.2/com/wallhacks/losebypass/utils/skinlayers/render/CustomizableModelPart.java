/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.utils.skinlayers.render;

import com.wallhacks.losebypass.utils.skinlayers.render.CustomizableCube;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;

public class CustomizableModelPart {
    public float x;
    public float y;
    public float z;
    public boolean visible = true;
    private final List<CustomizableCube> cubes;

    public CustomizableModelPart(List<CustomizableCube> list) {
        this.cubes = list;
    }

    public void copyFrom(ModelBox modelPart) {
        this.x = modelPart.posX1;
        this.y = modelPart.posY1;
        this.z = modelPart.posZ1;
    }

    public void setPos(float f, float g, float h) {
        this.x = f;
        this.y = g;
        this.z = h;
    }

    public void render(boolean redTint) {
        if (!this.visible) {
            return;
        }
        GlStateManager.pushMatrix();
        this.translateAndRotate();
        this.compile(redTint);
        GlStateManager.popMatrix();
    }

    public void translateAndRotate() {
        GlStateManager.translate(this.x / 16.0f, this.y / 16.0f, this.z / 16.0f);
    }

    private void compile(boolean redTint) {
        Iterator<CustomizableCube> iterator = this.cubes.iterator();
        while (iterator.hasNext()) {
            CustomizableCube cube = iterator.next();
            cube.render(Tessellator.getInstance().getWorldRenderer(), redTint);
        }
    }
}

