/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.tileentity.TileEntity
 *  net.minecraft.tileentity.TileEntityChest
 *  net.minecraft.tileentity.TileEntityEnderChest
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import me.report.liquidware.modules.render.HudColors;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.render.ColorUtil;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtilsNoteless;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="StorageESP", description="Allows you to see chests, dispensers, etc. through walls.", category=ModuleCategory.RENDER)
public class StorageESP
extends Module {
    public final BoolValue hueInterpolation = new BoolValue("hueInterpolation", false);
    public static final ListValue clolormode = new ListValue("ColorMode", new String[]{"Rainbow", "Rainbow2", "LightRainbow", "Default", "DoubleColor"}, "Light Rainbow");
    private static final IntegerValue colorRedValueModules = new IntegerValue("Red", 0, 0, 255);
    private static final IntegerValue colorGreenValueModules = new IntegerValue("Green", 160, 0, 255);
    private static final IntegerValue colorBlueValueModules = new IntegerValue("Blue", 255, 0, 255);

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        GL11.glPushAttrib((int)8192);
        GL11.glPushMatrix();
        int amount = 0;
        for (TileEntity tileEntity : StorageESP.mc.field_71441_e.field_147482_g) {
            if (!(tileEntity instanceof TileEntityChest) && !(tileEntity instanceof TileEntityEnderChest)) continue;
            this.render(amount, tileEntity);
            ++amount;
        }
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    private void render(int amount, TileEntity p) {
        GL11.glPushMatrix();
        RenderManager renderManager = mc.func_175598_ae();
        double x = (double)p.func_174877_v().func_177958_n() + 0.5 - renderManager.field_78725_b;
        double y = (double)p.func_174877_v().func_177956_o() - renderManager.field_78726_c;
        double z = (double)p.func_174877_v().func_177952_p() + 0.5 - renderManager.field_78723_d;
        GL11.glTranslated((double)x, (double)y, (double)z);
        GL11.glRotated((double)(-renderManager.field_78735_i), (double)0.0, (double)1.0, (double)0.0);
        GL11.glRotated((double)renderManager.field_78732_j, (double)(StorageESP.mc.field_71474_y.field_74320_O == 2 ? -1.0 : 1.0), (double)0.0, (double)0.0);
        float scale = 0.01f;
        GL11.glScalef((float)-0.01f, (float)-0.01f, (float)0.01f);
        float offset = renderManager.field_78732_j * 0.5f;
        HudColors HudColors2 = (HudColors)LiquidBounce.moduleManager.getModule(HudColors.class);
        switch (((String)clolormode.get()).toLowerCase()) {
            case "rainbow": {
                Color firstColor = ColorUtil.rainbow(15, 1, ((Float)HudColors2.getSaturationValue().get()).floatValue(), ((Float)HudColors2.getBrightnessValue().get()).floatValue(), 1.0f);
                Color secondColor = ColorUtil.rainbow(15, 40, ((Float)HudColors2.getSaturationValue().get()).floatValue(), ((Float)HudColors2.getBrightnessValue().get()).floatValue(), 1.0f);
                RenderUtilsNoteless.lineNoGl(-50.0, offset, 50.0, offset, firstColor, secondColor);
                RenderUtilsNoteless.lineNoGl(-50.0, -95.0f + offset, -50.0, offset, firstColor, secondColor);
                RenderUtilsNoteless.lineNoGl(-50.0, -95.0f + offset, 50.0, -95.0f + offset, firstColor, secondColor);
                RenderUtilsNoteless.lineNoGl(50.0, -95.0f + offset, 50.0, offset, firstColor, secondColor);
                break;
            }
            case "rainbow2": {
                Color firstColor = ColorUtil.rainbow(15, 1, ((Float)HudColors2.getSaturationValue().get()).floatValue(), ((Float)HudColors2.getBrightnessValue().get()).floatValue(), 1.0f);
                Color secondColor = ColorUtil.rainbow(15, 40, ((Float)HudColors2.getSaturationValue().get()).floatValue(), ((Float)HudColors2.getBrightnessValue().get()).floatValue(), 1.0f);
                RenderUtilsNoteless.lineNoGl(-50.0, offset, 50.0, offset, secondColor, firstColor);
                RenderUtilsNoteless.lineNoGl(-50.0, -95.0f + offset, -50.0, offset, secondColor, firstColor);
                RenderUtilsNoteless.lineNoGl(-50.0, -95.0f + offset, 50.0, -95.0f + offset, secondColor, firstColor);
                RenderUtilsNoteless.lineNoGl(50.0, -95.0f + offset, 50.0, offset, secondColor, firstColor);
                break;
            }
            case "doublecolor": {
                Color firstColor = ColorUtil.interpolateColorsBackAndForth(15, 0, Color.PINK, Color.BLUE, (Boolean)this.hueInterpolation.get());
                Color secondColor = ColorUtil.interpolateColorsBackAndForth(15, 90, Color.PINK, Color.BLUE, (Boolean)this.hueInterpolation.get());
                RenderUtilsNoteless.lineNoGl(-50.0, offset, 50.0, offset, firstColor, secondColor);
                RenderUtilsNoteless.lineNoGl(-50.0, -95.0f + offset, -50.0, offset, firstColor, secondColor);
                RenderUtilsNoteless.lineNoGl(-50.0, -95.0f + offset, 50.0, -95.0f + offset, firstColor, secondColor);
                RenderUtilsNoteless.lineNoGl(50.0, -95.0f + offset, 50.0, offset, firstColor, secondColor);
                break;
            }
            case "default": {
                RenderUtils.lineNoGl(-50.0, offset, 50.0, offset, new Color((Integer)colorRedValueModules.get(), (Integer)colorGreenValueModules.get(), (Integer)colorBlueValueModules.get()));
                RenderUtils.lineNoGl(-50.0, -95.0f + offset, -50.0, offset, new Color((Integer)colorRedValueModules.get(), (Integer)colorGreenValueModules.get(), (Integer)colorBlueValueModules.get()));
                RenderUtils.lineNoGl(-50.0, -95.0f + offset, 50.0, -95.0f + offset, new Color((Integer)colorRedValueModules.get(), (Integer)colorGreenValueModules.get(), (Integer)colorBlueValueModules.get()));
                RenderUtils.lineNoGl(50.0, -95.0f + offset, 50.0, offset, new Color((Integer)colorRedValueModules.get(), (Integer)colorGreenValueModules.get(), (Integer)colorBlueValueModules.get()));
            }
        }
        GL11.glPopMatrix();
    }
}

