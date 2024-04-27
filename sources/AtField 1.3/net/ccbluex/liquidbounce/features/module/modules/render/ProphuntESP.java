/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Unit
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="ProphuntESP", description="Allows you to see disguised players in PropHunt.", category=ModuleCategory.RENDER)
public final class ProphuntESP
extends Module {
    private final IntegerValue colorBlueValue;
    private final IntegerValue colorGreenValue;
    private final IntegerValue colorRedValue;
    private final BoolValue colorRainbow;
    private final Map blocks = new HashMap();

    public final Map getBlocks() {
        return this.blocks;
    }

    @EventTarget
    public final void onRender3D(@Nullable Render3DEvent render3DEvent) {
        Object object2;
        Color color = (Boolean)this.colorRainbow.get() != false ? ColorUtils.rainbow() : new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue());
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        for (Object object2 : iWorldClient.getLoadedEntityList()) {
            if (!MinecraftInstance.classProvider.isEntityFallingBlock(MinecraftInstance.classProvider.isEntityFallingBlock(object2))) continue;
            RenderUtils.drawEntityBox((IEntity)object2, color, true);
        }
        object2 = this.blocks;
        boolean bl = false;
        boolean bl2 = false;
        synchronized (object2) {
            boolean bl3 = false;
            Iterator iterator2 = this.blocks.entrySet().iterator();
            while (iterator2.hasNext()) {
                Map.Entry entry = iterator2.next();
                if (System.currentTimeMillis() - ((Number)entry.getValue()).longValue() > 2000L) {
                    iterator2.remove();
                    continue;
                }
                RenderUtils.drawBlockBox((WBlockPos)entry.getKey(), color, true);
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    @Override
    public void onDisable() {
        Map map = this.blocks;
        boolean bl = false;
        boolean bl2 = false;
        synchronized (map) {
            boolean bl3 = false;
            this.blocks.clear();
            Unit unit = Unit.INSTANCE;
        }
    }

    public ProphuntESP() {
        this.colorRedValue = new IntegerValue("R", 0, 0, 255);
        this.colorGreenValue = new IntegerValue("G", 90, 0, 255);
        this.colorBlueValue = new IntegerValue("B", 255, 0, 255);
        this.colorRainbow = new BoolValue("Rainbow", false);
    }
}

