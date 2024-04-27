package me.aquavit.liquidsense.module.modules.render;

import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.Render2DEvent;
import me.aquavit.liquidsense.event.events.Render3DEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.utils.client.ClientUtils;
import me.aquavit.liquidsense.utils.render.ColorUtils;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.utils.render.shader.shaders.OutlineShader;
import me.aquavit.liquidsense.value.BoolValue;
import me.aquavit.liquidsense.value.IntegerValue;
import me.aquavit.liquidsense.value.ListValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityArrow;

import java.awt.*;

@ModuleInfo(name = "ItemESP", description = "Allows you to see items through walls.", category = ModuleCategory.RENDER)
public class ItemESP extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[] {"Box", "ShaderOutline"}, "Box");
    private final IntegerValue colorRedValue = new IntegerValue("R", 0, 0, 255);
    private final IntegerValue colorGreenValue = new IntegerValue("G", 255, 0, 255);
    private final IntegerValue colorBlueValue = new IntegerValue("B", 0, 0, 255);
    private final BoolValue colorRainbow = new BoolValue("Rainbow", true);

    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        if(modeValue.get().equalsIgnoreCase("Box")) {
            final Color color = colorRainbow.get() ? ColorUtils.rainbow() : new Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get());

            for(final Entity entity : mc.theWorld.loadedEntityList) {
                if(!(entity instanceof EntityItem || entity instanceof EntityArrow))
                    continue;

                RenderUtils.drawEntityBox(entity, color, true);
            }
        }
    }

    @EventTarget
    public void onRender2D(final Render2DEvent event) {
        if(modeValue.get().equalsIgnoreCase("ShaderOutline")) {
            OutlineShader.OUTLINE_SHADER.startDraw(event.getPartialTicks());

            try{
                for(final Entity entity : mc.theWorld.loadedEntityList) {
                    if(!(entity instanceof EntityItem || entity instanceof EntityArrow))
                        continue;

                    mc.getRenderManager().renderEntityStatic(entity, event.getPartialTicks(), true);
                }
            }catch (final Exception ex) {
                ClientUtils.getLogger().error("An error occurred while rendering all item entities for shader esp", ex);
            }

            OutlineShader.OUTLINE_SHADER.stopDraw(colorRainbow.get() ? ColorUtils.rainbow() : new Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get()), 1F, 1F);
        }
    }
}
