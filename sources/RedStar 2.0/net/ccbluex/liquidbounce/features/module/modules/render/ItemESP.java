package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.TypeCastException;
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
import net.ccbluex.liquidbounce.utils.render.shader.FramebufferShader;
import net.ccbluex.liquidbounce.utils.render.shader.shaders.GlowShader;
import net.ccbluex.liquidbounce.utils.render.shader.shaders.OutlineShader;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="ItemESP", description="Allows you to see items through walls.", category=ModuleCategory.RENDER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000@\n\n\n\b\n\n\b\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\b\u000020B¢J\b\r0HJ020HJ02\b0HR0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R\b0X¢\n\u0000R\t0\nX¢\n\u0000R0\fX¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/ItemESP;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "colorBlueValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "colorGreenValue", "colorRainbow", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "colorRedValue", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "shaderRadiusValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "getColor", "Ljava/awt/Color;", "onRender2D", "", "event", "Lnet/ccbluex/liquidbounce/event/Render2DEvent;", "onRender3D", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "Pride"})
public final class ItemESP
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Box", "OtherBox", "ShaderOutline", "ShaderGlow"}, "Box");
    private final FloatValue shaderRadiusValue = new FloatValue("ShaderRadius", 2.0f, 0.5f, 5.0f);
    private final IntegerValue colorRedValue = new IntegerValue("R", 0, 0, 255);
    private final IntegerValue colorGreenValue = new IntegerValue("G", 255, 0, 255);
    private final IntegerValue colorBlueValue = new IntegerValue("B", 0, 0, 255);
    private final BoolValue colorRainbow = new BoolValue("Rainbow", true);

    private final Color getColor() {
        return (Boolean)this.colorRainbow.get() != false ? ColorUtils.rainbow() : new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue());
    }

    @EventTarget
    public final void onRender3D(@Nullable Render3DEvent event) {
        Color color = this.getColor();
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        for (IEntity entity : iWorldClient.getLoadedEntityList()) {
            if (!MinecraftInstance.classProvider.isEntityItem(entity) && !MinecraftInstance.classProvider.isEntityArrow(entity)) continue;
            String string = (String)this.modeValue.get();
            boolean bl = false;
            String string2 = string;
            if (string2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string3 = string2.toLowerCase();
            Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
            switch (string3) {
                case "box": {
                    RenderUtils.drawEntityBox(entity, color, true);
                    break;
                }
                case "otherbox": {
                    RenderUtils.drawEntityBox(entity, color, false);
                    break;
                }
            }
        }
    }

    @EventTarget
    public final void onRender2D(@NotNull Render2DEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        FramebufferShader framebufferShader = StringsKt.equals((String)this.modeValue.get(), "shaderoutline", true) ? (FramebufferShader)OutlineShader.OUTLINE_SHADER : (FramebufferShader)(StringsKt.equals((String)this.modeValue.get(), "shaderglow", true) ? GlowShader.GLOW_SHADER : null);
        if (framebufferShader == null) {
            return;
        }
        FramebufferShader shader = framebufferShader;
        float partialTicks = event.getPartialTicks();
        shader.startDraw(partialTicks);
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
        shader.stopDraw(this.getColor(), ((Number)this.shaderRadiusValue.get()).floatValue(), 1.0f);
    }
}
