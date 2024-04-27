/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.scoreboard.ScoreObjective
 *  net.minecraftforge.client.GuiIngameForge
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$ElementType
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Overwrite
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.Slice
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import net.ccbluex.liquidbounce.features.module.modules.render.Animations;
import net.ccbluex.liquidbounce.injection.forge.mixins.gui.MixinGuiInGame;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.AnimationUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
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
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiIngameForge.class})
public abstract class MixinGuiIngameForge
extends MixinGuiInGame {
    public float xScale = 0.0f;

    @Shadow(remap=false)
    abstract boolean pre(RenderGameOverlayEvent.ElementType var1);

    @Shadow(remap=false)
    abstract void post(RenderGameOverlayEvent.ElementType var1);

    @Inject(method={"renderChat"}, slice={@Slice(from=@At(value="INVOKE", target="Lnet/minecraftforge/fml/common/eventhandler/EventBus;post(Lnet/minecraftforge/fml/common/eventhandler/Event;)Z", ordinal=0, remap=false))}, at={@At(value="RETURN", ordinal=0)}, remap=false)
    private void fixProfilerSectionNotEnding(int width, int height, CallbackInfo ci) {
        Minecraft mc = Minecraft.func_71410_x();
        if (mc.field_71424_I.func_76322_c().endsWith("chat")) {
            mc.field_71424_I.func_76319_b();
        }
    }

    @Inject(method={"renderExperience"}, at={@At(value="HEAD")}, remap=false)
    private void enableExperienceAlpha(int filled, int top, CallbackInfo ci) {
        GlStateManager.func_179141_d();
    }

    @Inject(method={"renderExperience"}, at={@At(value="RETURN")}, remap=false)
    private void disableExperienceAlpha(int filled, int top, CallbackInfo ci) {
        GlStateManager.func_179118_c();
    }

    @Overwrite(remap=false)
    protected void renderPlayerList(int width, int height) {
        Minecraft mc = Minecraft.func_71410_x();
        ScoreObjective scoreobjective = mc.field_71441_e.func_96441_U().func_96539_a(0);
        NetHandlerPlayClient handler = MinecraftInstance.mc2.field_71439_g.field_71174_a;
        if (!mc.func_71387_A() || handler.func_175106_d().size() > 1 || scoreobjective != null) {
            this.xScale = AnimationUtils.animate(mc.field_71474_y.field_74321_H.func_151470_d() ? 100.0f : 0.0f, this.xScale, ((String)Animations.tabAnimations.get()).equalsIgnoreCase("none") ? 1.0f : 0.0125f * (float)RenderUtils.deltaTime);
            float rescaled = this.xScale / 100.0f;
            boolean displayable = rescaled > 0.0f;
            this.field_175196_v.func_175246_a(displayable);
            if (!displayable || this.pre(RenderGameOverlayEvent.ElementType.PLAYER_LIST)) {
                return;
            }
            GlStateManager.func_179094_E();
            switch (((String)Animations.tabAnimations.get()).toLowerCase()) {
                case "zoom": {
                    GlStateManager.func_179109_b((float)((float)width / 2.0f * (1.0f - rescaled)), (float)0.0f, (float)0.0f);
                    GlStateManager.func_179152_a((float)rescaled, (float)rescaled, (float)rescaled);
                    break;
                }
                case "slide": {
                    GlStateManager.func_179152_a((float)1.0f, (float)rescaled, (float)1.0f);
                    break;
                }
            }
            this.field_175196_v.func_175249_a(width, mc.field_71441_e.func_96441_U(), scoreobjective);
            GlStateManager.func_179121_F();
            this.post(RenderGameOverlayEvent.ElementType.PLAYER_LIST);
        } else {
            this.field_175196_v.func_175246_a(false);
        }
    }
}

