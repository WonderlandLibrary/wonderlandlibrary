package me.aquavit.liquidsense.module.modules.render;

import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.Render3DEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.utils.render.ColorUtils;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.value.BoolValue;
import me.aquavit.liquidsense.value.IntegerValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.util.BlockPos;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@ModuleInfo(name = "ProphuntESP", description = "Allows you to see disguised players in PropHunt.", category = ModuleCategory.RENDER)
public class ProphuntESP extends Module {

    public final Map<BlockPos, Long> blocks = new HashMap<>();

    private final IntegerValue colorRedValue = new IntegerValue("R", 0, 0, 255);
    private final IntegerValue colorGreenValue = new IntegerValue("G", 90, 0, 255);
    private final IntegerValue colorBlueValue = new IntegerValue("B", 255, 0, 255);
    private final BoolValue colorRainbow = new BoolValue("Rainbow", false);

    @Override
    public void onDisable() {
        synchronized(blocks) {
            blocks.clear();
        }
    }

    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        final Color color = colorRainbow.get() ? ColorUtils.rainbow() : new Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get());

        for(final Entity entity : mc.theWorld.loadedEntityList) {
            if(!(entity instanceof EntityFallingBlock))
                continue;

            RenderUtils.drawEntityBox(entity, color, true);
        }

        synchronized(blocks) {
            final Iterator<Map.Entry<BlockPos, Long>> iterator = blocks.entrySet().iterator();

            while(iterator.hasNext()) {
                final Map.Entry<BlockPos, Long> entry = iterator.next();

                if(System.currentTimeMillis() - entry.getValue() > 2000L) {
                    iterator.remove();
                    continue;
                }

                RenderUtils.drawBlockBox(entry.getKey(), color, true);
            }
        }
    }
}