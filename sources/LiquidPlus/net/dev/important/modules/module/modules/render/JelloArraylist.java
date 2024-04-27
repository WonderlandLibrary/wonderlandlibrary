/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.ResourceLocation
 */
package net.dev.important.modules.module.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import net.dev.important.Client;
import net.dev.important.event.EventTarget;
import net.dev.important.event.Render2DEvent;
import net.dev.important.gui.font.Fonts;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.value.BoolValue;
import net.dev.important.value.IntegerValue;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

@Info(name="JelloArraylist", description="NO SIGMA HATAR", category=Category.RENDER)
public class JelloArraylist
extends Module {
    ResourceLocation wtf = new ResourceLocation("liquidplus/panorama/shadow.png");
    List<Module> modules = new ArrayList<Module>();
    private final BoolValue useTrueFont = new BoolValue("Use-TrueFont", true);
    private final IntegerValue animateSpeed = new IntegerValue("Animate-Speed", 5, 1, 20);

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        this.updateElements(event.getPartialTicks());
        this.renderArraylist();
    }

    public void updateElements(float partialTicks) {
        this.modules = Client.moduleManager.getModules().stream().filter(mod -> mod.getArray() && !mod.getName().equalsIgnoreCase("JelloArraylist")).sorted(new ModComparator()).collect(Collectors.toCollection(ArrayList::new));
        float tick = 1.0f - partialTicks;
        for (Module module2 : this.modules) {
            module2.setAnimation(module2.getAnimation() + (float)(module2.getState() ? (Integer)this.animateSpeed.get() : -((Integer)this.animateSpeed.get()).intValue()) * tick);
            module2.setAnimation(MathHelper.func_76131_a((float)module2.getAnimation(), (float)0.0f, (float)20.0f));
        }
    }

    public void renderArraylist() {
        float xStart;
        ScaledResolution sr = new ScaledResolution(mc);
        float yStart = 1.0f;
        for (Module module2 : this.modules) {
            if (module2.getAnimation() <= 0.0f) continue;
            xStart = sr.func_78326_a() - Fonts.jello40.func_78256_a(module2.getName()) - 5;
            GlStateManager.func_179094_E();
            GlStateManager.func_179118_c();
            RenderUtils.drawImage3(this.wtf, xStart - 8.0f - 2.0f - 1.0f, yStart + 2.0f - 2.5f - 1.5f - 1.5f - 1.5f - 6.0f - 1.0f, Fonts.jello40.func_78256_a(module2.getName()) + 20 + 10, 38, 1.0f, 1.0f, 1.0f, module2.getAnimation() / 20.0f * 0.7f);
            GlStateManager.func_179141_d();
            GlStateManager.func_179121_F();
            yStart += 12.75f * (module2.getAnimation() / 20.0f);
        }
        yStart = 1.0f;
        for (Module module2 : this.modules) {
            if (module2.getAnimation() <= 0.0f) continue;
            xStart = sr.func_78326_a() - Fonts.jello40.func_78256_a(module2.getName()) - 5;
            GlStateManager.func_179094_E();
            if (((Boolean)this.useTrueFont.get()).booleanValue()) {
                GlStateManager.func_179118_c();
            }
            Fonts.jello40.drawString(module2.getName(), xStart, yStart + 7.5f, new Color(1.0f, 1.0f, 1.0f, module2.getAnimation() / 20.0f * 0.7f).getRGB());
            if (((Boolean)this.useTrueFont.get()).booleanValue()) {
                GlStateManager.func_179141_d();
            }
            GlStateManager.func_179121_F();
            yStart += 12.75f * (module2.getAnimation() / 20.0f);
        }
        GlStateManager.func_179117_G();
    }

    static class ModComparator
    implements Comparator<Module> {
        ModComparator() {
        }

        @Override
        public int compare(Module e1, Module e2) {
            return Fonts.jello40.func_78256_a(e1.getName()) < Fonts.jello40.func_78256_a(e2.getName()) ? 1 : -1;
        }
    }
}

