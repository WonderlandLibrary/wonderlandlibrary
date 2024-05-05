/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityFallingBlock
 *  net.minecraft.util.BlockPos
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.util.BlockPos;

@ModuleInfo(name="ProphuntESP", description="Allows you to see disguised players in PropHunt.", category=ModuleCategory.RENDER)
public class ProphuntESP
extends Module {
    public final Map<BlockPos, Long> blocks = new HashMap<BlockPos, Long>();
    private final IntegerValue colorRedValue = new IntegerValue("R", 0, 0, 255);
    private final IntegerValue colorGreenValue = new IntegerValue("G", 90, 0, 255);
    private final IntegerValue colorBlueValue = new IntegerValue("B", 255, 0, 255);
    private final BoolValue colorRainbow = new BoolValue("Rainbow", false);

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void onDisable() {
        Map<BlockPos, Long> map = this.blocks;
        synchronized (map) {
            this.blocks.clear();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventTarget
    public void onRender3D(Render3DEvent event) {
        Color color = (Boolean)this.colorRainbow.get() != false ? ColorUtils.rainbow() : new Color((Integer)this.colorRedValue.get(), (Integer)this.colorGreenValue.get(), (Integer)this.colorBlueValue.get());
        for (Entity entity : ProphuntESP.mc.field_71441_e.field_72996_f) {
            if (!(entity instanceof EntityFallingBlock)) continue;
            RenderUtils.drawEntityBox(entity, color, true);
        }
        Map<BlockPos, Long> map = this.blocks;
        synchronized (map) {
            Iterator<Map.Entry<BlockPos, Long>> iterator2 = this.blocks.entrySet().iterator();
            while (iterator2.hasNext()) {
                Map.Entry<BlockPos, Long> entry = iterator2.next();
                if (System.currentTimeMillis() - entry.getValue() > 2000L) {
                    iterator2.remove();
                    continue;
                }
                RenderUtils.drawBlockBox(entry.getKey(), color, true);
            }
        }
    }
}

