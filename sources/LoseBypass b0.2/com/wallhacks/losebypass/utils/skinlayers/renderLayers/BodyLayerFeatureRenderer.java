/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.utils.skinlayers.renderLayers;

import com.wallhacks.losebypass.systems.module.modules.render.ThreeDSkins;
import com.wallhacks.losebypass.utils.skinlayers.SkinUtil;
import com.wallhacks.losebypass.utils.skinlayers.render.CustomizableModelPart;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EnumPlayerModelParts;

public class BodyLayerFeatureRenderer
implements LayerRenderer<AbstractClientPlayer> {
    private RenderPlayer playerRenderer;
    private final boolean thinArms;
    private static final Minecraft mc = Minecraft.getMinecraft();
    private final List<Layer> bodyLayers = new ArrayList<Layer>();

    public BodyLayerFeatureRenderer(RenderPlayer playerRenderer) {
        this.playerRenderer = playerRenderer;
        this.thinArms = playerRenderer.smallArms;
        this.bodyLayers.add(new Layer(0, false, EnumPlayerModelParts.LEFT_PANTS_LEG, Shape.LEGS, () -> playerRenderer.getMainModel().bipedLeftLeg, () -> true));
        this.bodyLayers.add(new Layer(1, false, EnumPlayerModelParts.RIGHT_PANTS_LEG, Shape.LEGS, () -> playerRenderer.getMainModel().bipedRightLeg, () -> true));
        this.bodyLayers.add(new Layer(2, false, EnumPlayerModelParts.LEFT_SLEEVE, this.thinArms ? Shape.ARMS_SLIM : Shape.ARMS, () -> playerRenderer.getMainModel().bipedLeftArm, () -> true));
        this.bodyLayers.add(new Layer(3, true, EnumPlayerModelParts.RIGHT_SLEEVE, this.thinArms ? Shape.ARMS_SLIM : Shape.ARMS, () -> playerRenderer.getMainModel().bipedRightArm, () -> true));
        this.bodyLayers.add(new Layer(4, false, EnumPlayerModelParts.JACKET, Shape.BODY, () -> playerRenderer.getMainModel().bipedBody, () -> true));
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer player, float paramFloat1, float paramFloat2, float paramFloat3, float deltaTick, float paramFloat5, float paramFloat6, float paramFloat7) {
        if (!player.hasSkin()) return;
        if (player.isInvisible()) {
            return;
        }
        if (BodyLayerFeatureRenderer.mc.theWorld == null) {
            return;
        }
        if (BodyLayerFeatureRenderer.mc.thePlayer.getPositionVector().squareDistanceTo(player.getPositionVector()) > (double)ThreeDSkins.range()) {
            return;
        }
        if (player.getSkinLayers() == null && !this.setupModel(player)) {
            return;
        }
        this.renderLayers(player, player.getSkinLayers(), deltaTick);
    }

    private boolean setupModel(AbstractClientPlayer abstractClientPlayerEntity) {
        if (!SkinUtil.hasCustomSkin(abstractClientPlayerEntity)) {
            return false;
        }
        SkinUtil.setup3dLayers(abstractClientPlayerEntity, this.thinArms, null);
        return true;
    }

    public void renderLayers(AbstractClientPlayer abstractClientPlayer, CustomizableModelPart[] layers, float deltaTick) {
        if (layers == null) {
            return;
        }
        float pixelScaling = 1.15f;
        float heightScaling = 1.035f;
        boolean redTint = abstractClientPlayer.hurtTime > 0 || abstractClientPlayer.deathTime > 0;
        Iterator<Layer> iterator = this.bodyLayers.iterator();
        while (iterator.hasNext()) {
            Layer layer = iterator.next();
            if (!abstractClientPlayer.isWearing(layer.modelPart) || layer.vanillaGetter.get().isHidden || !layer.configGetter.get().booleanValue()) continue;
            GlStateManager.pushMatrix();
            if (abstractClientPlayer.isSneaking()) {
                GlStateManager.translate(0.0f, 0.2f, 0.0f);
            }
            layer.vanillaGetter.get().postRender(0.0625f);
            if (layer.shape == Shape.ARMS) {
                layers[layer.layersId].x = 15.968f;
            } else if (layer.shape == Shape.ARMS_SLIM) {
                layers[layer.layersId].x = 7.984f;
            }
            float widthScaling = layer.shape == Shape.BODY ? 1.05f : 1.15f;
            if (layer.mirrored) {
                layers[layer.layersId].x *= -1.0f;
            }
            GlStateManager.scale(0.0625, 0.0625, 0.0625);
            GlStateManager.scale(widthScaling, heightScaling, pixelScaling);
            layers[layer.layersId].y = layer.shape.yOffsetMagicValue;
            layers[layer.layersId].render(redTint);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    private static enum Shape {
        HEAD(0.0f),
        BODY(0.6f),
        LEGS(-0.2f),
        ARMS(0.4f),
        ARMS_SLIM(0.4f);

        private final float yOffsetMagicValue;

        private Shape(float yOffsetMagicValue) {
            this.yOffsetMagicValue = yOffsetMagicValue;
        }
    }

    class Layer {
        int layersId;
        boolean mirrored;
        EnumPlayerModelParts modelPart;
        Shape shape;
        Supplier<ModelRenderer> vanillaGetter;
        Supplier<Boolean> configGetter;

        public Layer(int layersId, boolean mirrored, EnumPlayerModelParts modelPart, Shape shape, Supplier<ModelRenderer> vanillaGetter, Supplier<Boolean> configGetter) {
            this.layersId = layersId;
            this.mirrored = mirrored;
            this.modelPart = modelPart;
            this.shape = shape;
            this.vanillaGetter = vanillaGetter;
            this.configGetter = configGetter;
        }
    }
}

