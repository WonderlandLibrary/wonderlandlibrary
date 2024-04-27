package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.util.IScaledResolution;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.CFont.FontLoaders;
import net.ccbluex.liquidbounce.utils.misc.MathHelper;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

@ModuleInfo(name="JelloArraylist", description="NO SIGMA HATAR", category=ModuleCategory.RENDER)
public class JelloArraylist
extends Module {
    ResourceLocation wtf = new ResourceLocation("wake/arraylistshadow.png");
    List<Module> modules = new ArrayList<Module>();
    private final BoolValue useTrueFont = new BoolValue("Use-TrueFont", true);
    private final IntegerValue animateSpeed = new IntegerValue("Animate-Speed", 5, 1, 20);

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        this.updateElements(event.getPartialTicks());
        this.renderArraylist();
    }

    public void updateElements(float partialTicks) {
        this.modules = LiquidBounce.moduleManager.getModules().stream().filter(mod -> mod.getArray() && !mod.getName().equalsIgnoreCase("JelloArraylist")).sorted(new ModComparator()).collect(Collectors.toCollection(ArrayList::new));
        float tick = 1.0f - partialTicks;
        for (Module module : this.modules) {
            module.setAnimation(module.getAnimation() + (float)(module.getState() ? (Integer)this.animateSpeed.get() : -((Integer)this.animateSpeed.get()).intValue()) * tick);
            module.setAnimation(MathHelper.clamp_float(module.getAnimation(), 0.0f, 20.0f));
        }
    }

    public void renderArraylist() {
        IScaledResolution sr = classProvider.createScaledResolution(mc);
        float yStart = 1.0f;
        float xStart = 0.0f;
        for (Module module : this.modules) {
            if (module.getAnimation() <= 0.0f) continue;
            xStart = sr.getScaledWidth() - FontLoaders.JelloList40.getStringWidth(module.getName()) - 5;
            GlStateManager.pushMatrix();
            GlStateManager.disableAlpha();
            RenderUtils.drawImage3(this.wtf, xStart - 8.0f - 2.0f - 1.0f, yStart + 2.0f - 2.5f - 1.5f - 1.5f - 1.5f - 6.0f - 1.0f, FontLoaders.JelloList40.getStringWidth(module.getName()) * 1 + 20 + 10, 38, 1.0f, 1.0f, 1.0f, module.getAnimation() / 20.0f * 0.7f);
            GlStateManager.enableAlpha();
            GlStateManager.popMatrix();
            yStart += 12.75f * (module.getAnimation() / 20.0f);
        }
        yStart = 1.0f;
        xStart = 0.0f;
        for (Module module : this.modules) {
            if (module.getAnimation() <= 0.0f) continue;
            xStart = sr.getScaledWidth() - FontLoaders.JelloList40.getStringWidth(module.getName()) - 5;
            GlStateManager.pushMatrix();
            if (((Boolean)this.useTrueFont.get()).booleanValue()) {
                GlStateManager.disableAlpha();
            }
            FontLoaders.JelloList40.drawString(module.getName(), xStart, yStart + 7.5f, new Color(1.0f, 1.0f, 1.0f, module.getAnimation() / 20.0f * 0.7f).getRGB());
            if (((Boolean)this.useTrueFont.get()).booleanValue()) {
                GlStateManager.enableAlpha();
            }
            GlStateManager.popMatrix();
            yStart += 12.75f * (module.getAnimation() / 20.0f);
        }
        GlStateManager.resetColor();
    }

    class ModComparator
    implements Comparator<Module> {
        ModComparator() {
        }

        @Override
        public int compare(Module e1, Module e2) {
            return FontLoaders.JelloList40.getStringWidth(e1.getName()) < FontLoaders.JelloList40.getStringWidth(e2.getName()) ? 1 : -1;
        }
    }
}
