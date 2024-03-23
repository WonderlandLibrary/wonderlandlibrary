package dev.zprestige.prestige.api.mixin;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.event.impl.HealtEvent;
import dev.zprestige.prestige.client.event.impl.LightEvent;
import dev.zprestige.prestige.client.event.impl.NametagRenderEvent;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={EntityRenderer.class}, priority=999)
public abstract class MixinEntityRenderer {
    @Overwrite
    public void render(Entity entity, float n, float n2, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int n3) {
        if (hasLabel(entity)) {
            String name = entity.getDisplayName().getString();
            if (entity instanceof PlayerEntity playerEntity) {
                if (entity.isAlive()) {
                    if (Prestige.Companion.getSelfDestructed()) {
                        renderLabelIfPresent(entity, entity.getDisplayName(), matrixStack, vertexConsumerProvider, n3);
                    } else {
                        HealtEvent event = new HealtEvent(playerEntity, "");
                        if (!event.invoke()) {
                            renderLabelIfPresent(entity, entity.getDisplayName(), matrixStack, vertexConsumerProvider, n3);
                        }
                        else {
                            renderLabelIfPresent(entity, Text.of(name + event.getText()), matrixStack, vertexConsumerProvider, n3);
                        }
                    }
                    return;
                }
            }
            renderLabelIfPresent(entity, entity.getDisplayName(), matrixStack, vertexConsumerProvider, n3);
        }
    }

    @Shadow
    abstract boolean hasLabel(Entity var1);

    @Inject(at={@At(value="HEAD")}, method={"renderLabelIfPresent"}, cancellable=true)
    void renderLabelIfPresent(Entity entity, Text text, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int n, CallbackInfo callbackInfo) {
        if (Prestige.Companion.getSelfDestructed()) {
            return;
        }
        if (new NametagRenderEvent(entity, text).invoke()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"getSkyLight"}, at={@At(value="RETURN")}, cancellable=true)
    void onGetSkyLight(Entity entity, BlockPos blockPos, CallbackInfoReturnable callbackInfoReturnable) {
        if (Prestige.Companion.getSelfDestructed()) {
            return;
        }
        if (new LightEvent().invoke()) {
            callbackInfoReturnable.setReturnValue(15);
        }
    }

    @Shadow
    abstract void renderLabelIfPresent(Entity var1, Text var2, MatrixStack var3, VertexConsumerProvider var4, int var5);
}