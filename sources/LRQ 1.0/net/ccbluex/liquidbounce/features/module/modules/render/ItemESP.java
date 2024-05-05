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
import net.ccbluex.liquidbounce.utils.ClientUtils;
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
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Box", "ShaderOutline"}, "Box");
    private final IntegerValue colorRedValue = new IntegerValue("R", 0, 0, 255);
    private final IntegerValue colorGreenValue = new IntegerValue("G", 255, 0, 255);
    private final IntegerValue colorBlueValue = new IntegerValue("B", 0, 0, 255);
    private final BoolValue colorRainbow = new BoolValue("Rainbow", true);

    @EventTarget
    public final void onRender3D(@Nullable Render3DEvent event) {
        if (StringsKt.equals((String)((String)this.modeValue.get()), (String)"Box", (boolean)true)) {
            Color color = (Boolean)this.colorRainbow.get() != false ? ColorUtils.rainbow() : new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue());
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            for (IEntity entity : iWorldClient.getLoadedEntityList()) {
                if (!MinecraftInstance.classProvider.isEntityItem(entity) && !MinecraftInstance.classProvider.isEntityArrow(entity)) continue;
                RenderUtils.drawEntityBox(entity, color, true);
            }
        }
    }

    @EventTarget
    public final void onRender2D(Render2DEvent event) {
        if (StringsKt.equals((String)((String)this.modeValue.get()), (String)"ShaderOutline", (boolean)true)) {
            OutlineShader.OUTLINE_SHADER.startDraw(event.getPartialTicks());
            try {
                IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                if (iWorldClient == null) {
                    Intrinsics.throwNpe();
                }
                for (IEntity entity : iWorldClient.getLoadedEntityList()) {
                    if (!MinecraftInstance.classProvider.isEntityItem(entity) && !MinecraftInstance.classProvider.isEntityArrow(entity)) continue;
                    MinecraftInstance.mc.getRenderManager().renderEntityStatic(entity, event.getPartialTicks(), true);
                }
            }
            catch (Exception ex) {
                ClientUtils.getLogger().error("An error occurred while rendering all item entities for shader esp", (Throwable)ex);
            }
            OutlineShader.OUTLINE_SHADER.stopDraw((Boolean)this.colorRainbow.get() != false ? ColorUtils.rainbow() : new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue()), 1.0f, 1.0f);
        }
    }
}

