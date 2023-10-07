/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.projectile.EntityArrow
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.shader.shaders.OutlineShader;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityArrow;

@ModuleInfo(name="ItemESP", description="Allows you to see items through walls.", category=ModuleCategory.RENDER)
public class ItemESP
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Box", "ShaderOutline"}, "Box");
    private final IntegerValue colorRedValue = new IntegerValue("R", 0, 0, 255);
    private final IntegerValue colorGreenValue = new IntegerValue("G", 255, 0, 255);
    private final IntegerValue colorBlueValue = new IntegerValue("B", 0, 0, 255);
    private final BoolValue colorRainbow = new BoolValue("Rainbow", true);

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        if (((String)this.modeValue.get()).equalsIgnoreCase("Box")) {
            Color color = (Boolean)this.colorRainbow.get() != false ? ColorUtils.rainbow() : new Color((Integer)this.colorRedValue.get(), (Integer)this.colorGreenValue.get(), (Integer)this.colorBlueValue.get());
            for (Entity entity : ItemESP.mc.field_71441_e.field_72996_f) {
                if (!(entity instanceof EntityItem) && !(entity instanceof EntityArrow)) continue;
                RenderUtils.drawEntityBox(entity, color, true);
            }
        }
    }

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        if (((String)this.modeValue.get()).equalsIgnoreCase("ShaderOutline")) {
            OutlineShader.OUTLINE_SHADER.startDraw(event.getPartialTicks());
            try {
                for (Entity entity : ItemESP.mc.field_71441_e.field_72996_f) {
                    if (!(entity instanceof EntityItem) && !(entity instanceof EntityArrow)) continue;
                    mc.func_175598_ae().func_147936_a(entity, event.getPartialTicks(), true);
                }
            }
            catch (Exception ex) {
                ClientUtils.getLogger().error("An error occurred while rendering all item entities for shader esp", (Throwable)ex);
            }
            OutlineShader.OUTLINE_SHADER.stopDraw((Boolean)this.colorRainbow.get() != false ? ColorUtils.rainbow() : new Color((Integer)this.colorRedValue.get(), (Integer)this.colorGreenValue.get(), (Integer)this.colorBlueValue.get()), 1.0f, 1.0f);
        }
        if (((String)this.modeValue.get()).equalsIgnoreCase("Real2D")) {
            Color color = (Boolean)this.colorRainbow.get() != false ? ColorUtils.rainbow() : new Color((Integer)this.colorRedValue.get(), (Integer)this.colorGreenValue.get(), (Integer)this.colorBlueValue.get());
            for (Entity entity : ItemESP.mc.field_71441_e.field_72996_f) {
                if (!(entity instanceof EntityItem) && !(entity instanceof EntityArrow)) continue;
                mc.func_175598_ae().func_147936_a(entity, event.getPartialTicks(), true);
            }
        }
    }
}

