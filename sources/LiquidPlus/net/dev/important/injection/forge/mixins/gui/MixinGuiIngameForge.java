/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.scoreboard.ScoreObjective
 *  net.minecraftforge.client.GuiIngameForge
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$ElementType
 */
package net.dev.important.injection.forge.mixins.gui;

import net.dev.important.injection.forge.mixins.gui.MixinGuiInGame;
import net.dev.important.utils.AnimationUtils;
import net.dev.important.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiIngameForge.class})
public abstract class MixinGuiIngameForge
extends MixinGuiInGame {
    public float xScale = 0.0f;

    @Shadow(remap=false)
    abstract boolean pre(RenderGameOverlayEvent.ElementType var1);

    @Shadow(remap=false)
    abstract void post(RenderGameOverlayEvent.ElementType var1);

    @Inject(method={"renderHealth"}, at={@At(value="HEAD")}, remap=false)
    private void renderHealthBegin(int width, int height, CallbackInfo callbackInfo) {
        GlStateManager.func_179109_b((float)0.0f, (float)(-RenderUtils.yPosOffset), (float)0.0f);
    }

    @Inject(method={"renderHealth"}, at={@At(value="RETURN")}, remap=false)
    private void renderHealthEnd(int width, int height, CallbackInfo callbackInfo) {
        GlStateManager.func_179109_b((float)0.0f, (float)RenderUtils.yPosOffset, (float)0.0f);
    }

    @Inject(method={"renderFood"}, at={@At(value="HEAD")}, remap=false)
    private void renderFoodBegin(int width, int height, CallbackInfo callbackInfo) {
        GlStateManager.func_179109_b((float)0.0f, (float)(-RenderUtils.yPosOffset), (float)0.0f);
    }

    @Inject(method={"renderFood"}, at={@At(value="RETURN")}, remap=false)
    private void renderFoodEnd(int width, int height, CallbackInfo callbackInfo) {
        GlStateManager.func_179109_b((float)0.0f, (float)RenderUtils.yPosOffset, (float)0.0f);
    }

    @Inject(method={"renderExperience"}, at={@At(value="HEAD")}, remap=false)
    private void renderExpBegin(int width, int height, CallbackInfo callbackInfo) {
        GlStateManager.func_179109_b((float)0.0f, (float)(-RenderUtils.yPosOffset), (float)0.0f);
    }

    @Inject(method={"renderExperience"}, at={@At(value="RETURN")}, remap=false)
    private void renderExpEnd(int width, int height, CallbackInfo callbackInfo) {
        GlStateManager.func_179109_b((float)0.0f, (float)RenderUtils.yPosOffset, (float)0.0f);
    }

    @Inject(method={"renderArmor"}, at={@At(value="HEAD")}, remap=false)
    private void renderArmorBegin(int width, int height, CallbackInfo callbackInfo) {
        GlStateManager.func_179109_b((float)0.0f, (float)(-RenderUtils.yPosOffset), (float)0.0f);
    }

    @Inject(method={"renderArmor"}, at={@At(value="RETURN")}, remap=false)
    private void renderArmorEnd(int width, int height, CallbackInfo callbackInfo) {
        GlStateManager.func_179109_b((float)0.0f, (float)RenderUtils.yPosOffset, (float)0.0f);
    }

    @Inject(method={"renderHealthMount"}, at={@At(value="HEAD")}, remap=false)
    private void renderHealthMountBegin(int width, int height, CallbackInfo callbackInfo) {
        GlStateManager.func_179109_b((float)0.0f, (float)(-RenderUtils.yPosOffset), (float)0.0f);
    }

    @Inject(method={"renderHealthMount"}, at={@At(value="RETURN")}, remap=false)
    private void renderHealthMountEnd(int width, int height, CallbackInfo callbackInfo) {
        GlStateManager.func_179109_b((float)0.0f, (float)RenderUtils.yPosOffset, (float)0.0f);
    }

    @Inject(method={"renderAir"}, at={@At(value="HEAD")}, remap=false)
    private void renderAirBegin(int width, int height, CallbackInfo callbackInfo) {
        GlStateManager.func_179109_b((float)0.0f, (float)(-RenderUtils.yPosOffset), (float)0.0f);
    }

    @Inject(method={"renderAir"}, at={@At(value="RETURN")}, remap=false)
    private void renderAirEnd(int width, int height, CallbackInfo callbackInfo) {
        GlStateManager.func_179109_b((float)0.0f, (float)RenderUtils.yPosOffset, (float)0.0f);
    }

    @Inject(method={"renderJumpBar"}, at={@At(value="HEAD")}, remap=false)
    private void renderJumpBarBegin(int width, int height, CallbackInfo callbackInfo) {
        GlStateManager.func_179109_b((float)0.0f, (float)(-RenderUtils.yPosOffset), (float)0.0f);
    }

    @Inject(method={"renderJumpBar"}, at={@At(value="RETURN")}, remap=false)
    private void renderJumpBarEnd(int width, int height, CallbackInfo callbackInfo) {
        GlStateManager.func_179109_b((float)0.0f, (float)RenderUtils.yPosOffset, (float)0.0f);
    }

    @Inject(method={"renderRecordOverlay"}, at={@At(value="HEAD")}, remap=false)
    private void renderRecordOverlayBegin(int width, int height, float partialTicks, CallbackInfo callbackInfo) {
        GlStateManager.func_179109_b((float)0.0f, (float)(-RenderUtils.yPosOffset), (float)0.0f);
    }

    @Inject(method={"renderRecordOverlay"}, at={@At(value="RETURN")}, remap=false)
    private void renderRecordOverlayEnd(int width, int height, float partialTicks, CallbackInfo callbackInfo) {
        GlStateManager.func_179109_b((float)0.0f, (float)RenderUtils.yPosOffset, (float)0.0f);
    }

    @Inject(method={"renderToolHightlight"}, at={@At(value="HEAD")}, remap=false)
    private void renderToolHightlightBegin(ScaledResolution sc, CallbackInfo callbackInfo) {
        GlStateManager.func_179109_b((float)0.0f, (float)(-RenderUtils.yPosOffset), (float)0.0f);
    }

    @Inject(method={"renderToolHightlight"}, at={@At(value="RETURN")}, remap=false)
    private void renderToolHightlightEnd(ScaledResolution sc, CallbackInfo callbackInfo) {
        GlStateManager.func_179109_b((float)0.0f, (float)RenderUtils.yPosOffset, (float)0.0f);
    }

    @Overwrite(remap=false)
    protected void renderPlayerList(int width, int height) {
        Minecraft mc = Minecraft.func_71410_x();
        ScoreObjective scoreobjective = mc.field_71441_e.func_96441_U().func_96539_a(0);
        NetHandlerPlayClient handler = mc.field_71439_g.field_71174_a;
        if (!mc.func_71387_A() || handler.func_175106_d().size() > 1 || scoreobjective != null) {
            this.xScale = AnimationUtils.animate(mc.field_71474_y.field_74321_H.func_151470_d() ? 100.0f : 0.0f, this.xScale, 0.0125f * (float)RenderUtils.deltaTime);
            float rescaled = this.xScale / 100.0f;
            boolean displayable = rescaled > 0.0f;
            this.field_175196_v.func_175246_a(displayable);
            if (!displayable || this.pre(RenderGameOverlayEvent.ElementType.PLAYER_LIST)) {
                return;
            }
            GlStateManager.func_179094_E();
            GlStateManager.func_179109_b((float)((float)width / 2.0f * (1.0f - rescaled)), (float)0.0f, (float)0.0f);
            GlStateManager.func_179152_a((float)rescaled, (float)rescaled, (float)rescaled);
            this.field_175196_v.func_175249_a(width, mc.field_71441_e.func_96441_U(), scoreobjective);
            GlStateManager.func_179121_F();
            this.post(RenderGameOverlayEvent.ElementType.PLAYER_LIST);
        } else {
            this.field_175196_v.func_175246_a(false);
        }
    }
}

