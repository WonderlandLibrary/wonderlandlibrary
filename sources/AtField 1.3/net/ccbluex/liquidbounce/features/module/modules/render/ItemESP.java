/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.shader.shaders.OutlineShader;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="ItemESP", description="Allows you to see items through walls.", category=ModuleCategory.RENDER)
public final class ItemESP
extends Module {
    private final IntegerValue colorGreenValue;
    private final IntegerValue colorRedValue;
    private final IntegerValue colorBlueValue;
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Box", "ShaderOutline"}, "Box");
    private final BoolValue colorRainbow;

    public ItemESP() {
        this.colorRedValue = new IntegerValue("R", 0, 0, 255);
        this.colorGreenValue = new IntegerValue("G", 255, 0, 255);
        this.colorBlueValue = new IntegerValue("B", 0, 0, 255);
        this.colorRainbow = new BoolValue("Rainbow", true);
    }

    @EventTarget
    public final void onRender3D(@Nullable Render3DEvent render3DEvent) {
        if (StringsKt.equals((String)((String)this.modeValue.get()), (String)"Box", (boolean)true)) {
            Color color = (Boolean)this.colorRainbow.get() != false ? ColorUtils.rainbow() : new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue());
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            for (IEntity iEntity : iWorldClient.getLoadedEntityList()) {
                if (!MinecraftInstance.classProvider.isEntityItem(iEntity) && !MinecraftInstance.classProvider.isEntityArrow(iEntity)) continue;
                RenderUtils.drawEntityBox(iEntity, color, true);
            }
        }
    }

    @EventTarget
    public final void onRender2D(Render2DEvent render2DEvent) {
        if (StringsKt.equals((String)((String)this.modeValue.get()), (String)"ShaderOutline", (boolean)true)) {
            OutlineShader.OUTLINE_SHADER.startDraw(render2DEvent.getPartialTicks());
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            for (IEntity iEntity : iWorldClient.getLoadedEntityList()) {
                if (!MinecraftInstance.classProvider.isEntityItem(iEntity) && !MinecraftInstance.classProvider.isEntityArrow(iEntity)) continue;
                MinecraftInstance.mc.getRenderManager().renderEntityStatic(iEntity, render2DEvent.getPartialTicks(), true);
            }
            OutlineShader.OUTLINE_SHADER.stopDraw((Boolean)this.colorRainbow.get() != false ? ColorUtils.rainbow() : new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue()), 1.0f, 1.0f);
        }
    }
}

