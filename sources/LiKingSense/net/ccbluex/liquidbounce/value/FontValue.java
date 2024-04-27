/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.value.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0002\u00a2\u0006\u0002\u0010\u0006J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\n\u0010\u000b\u001a\u0004\u0018\u00010\nH\u0016\u00a8\u0006\f"}, d2={"Lnet/ccbluex/liquidbounce/value/FontValue;", "Lnet/ccbluex/liquidbounce/value/Value;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IFontRenderer;", "valueName", "", "value", "(Ljava/lang/String;Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IFontRenderer;)V", "fromJson", "", "element", "Lcom/google/gson/JsonElement;", "toJson", "LiKingSense"})
public final class FontValue
extends Value<IFontRenderer> {
    @Override
    @Nullable
    public JsonElement toJson() {
        Fonts.FontInfo fontInfo = Fonts.getFontDetails((IFontRenderer)this.getValue());
        if (fontInfo == null) {
            return null;
        }
        Fonts.FontInfo fontDetails = fontInfo;
        JsonObject valueObject = new JsonObject();
        valueObject.addProperty("fontName", fontDetails.getName());
        valueObject.addProperty("fontSize", (Number)fontDetails.getFontSize());
        return (JsonElement)valueObject;
    }

    @Override
    public void fromJson(@NotNull JsonElement element) {
        Intrinsics.checkParameterIsNotNull((Object)element, (String)"element");
        if (!element.isJsonObject()) {
            return;
        }
        JsonObject valueObject = element.getAsJsonObject();
        JsonElement jsonElement = valueObject.get("fontName");
        Intrinsics.checkExpressionValueIsNotNull((Object)jsonElement, (String)"valueObject[\"fontName\"]");
        String string = jsonElement.getAsString();
        JsonElement jsonElement2 = valueObject.get("fontSize");
        Intrinsics.checkExpressionValueIsNotNull((Object)jsonElement2, (String)"valueObject[\"fontSize\"]");
        IFontRenderer iFontRenderer = Fonts.getFontRenderer(string, jsonElement2.getAsInt());
        Intrinsics.checkExpressionValueIsNotNull((Object)iFontRenderer, (String)"Fonts.getFontRenderer(va\u2026Object[\"fontSize\"].asInt)");
        this.setValue(iFontRenderer);
    }

    public FontValue(@NotNull String valueName, @NotNull IFontRenderer value) {
        Intrinsics.checkParameterIsNotNull((Object)valueName, (String)"valueName");
        Intrinsics.checkParameterIsNotNull((Object)value, (String)"value");
        super(valueName, value);
    }
}

