/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  net.minecraft.client.gui.FontRenderer
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.value.Value;
import net.minecraft.client.gui.FontRenderer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0017\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0002\u00a2\u0006\u0002\u0010\u0006B#\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0002\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b\u00a2\u0006\u0002\u0010\nJ\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0016J\n\u0010\u000f\u001a\u0004\u0018\u00010\u000eH\u0016\u00a8\u0006\u0010"}, d2={"Lnet/ccbluex/liquidbounce/value/FontValue;", "Lnet/ccbluex/liquidbounce/value/Value;", "Lnet/minecraft/client/gui/FontRenderer;", "valueName", "", "value", "(Ljava/lang/String;Lnet/minecraft/client/gui/FontRenderer;)V", "displayable", "Lkotlin/Function0;", "", "(Ljava/lang/String;Lnet/minecraft/client/gui/FontRenderer;Lkotlin/jvm/functions/Function0;)V", "fromJson", "", "element", "Lcom/google/gson/JsonElement;", "toJson", "KyinoClient"})
public final class FontValue
extends Value<FontRenderer> {
    @Override
    @Nullable
    public JsonElement toJson() {
        Object[] objectArray = Fonts.getFontDetails((FontRenderer)this.getValue());
        if (objectArray == null) {
            return null;
        }
        Object[] fontDetails = objectArray;
        JsonObject valueObject = new JsonObject();
        Object object = fontDetails[0];
        if (object == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
        valueObject.addProperty("fontName", (String)object);
        Object object2 = fontDetails[1];
        if (object2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Int");
        }
        valueObject.addProperty("fontSize", (Number)((Integer)object2));
        return (JsonElement)valueObject;
    }

    @Override
    public void fromJson(@NotNull JsonElement element) {
        Intrinsics.checkParameterIsNotNull(element, "element");
        if (!element.isJsonObject()) {
            return;
        }
        JsonObject valueObject = element.getAsJsonObject();
        JsonElement jsonElement = valueObject.get("fontName");
        Intrinsics.checkExpressionValueIsNotNull(jsonElement, "valueObject[\"fontName\"]");
        String string = jsonElement.getAsString();
        JsonElement jsonElement2 = valueObject.get("fontSize");
        Intrinsics.checkExpressionValueIsNotNull(jsonElement2, "valueObject[\"fontSize\"]");
        FontRenderer fontRenderer = Fonts.getFontRenderer(string, jsonElement2.getAsInt());
        Intrinsics.checkExpressionValueIsNotNull(fontRenderer, "Fonts.getFontRenderer(va\u2026Object[\"fontSize\"].asInt)");
        this.setValue(fontRenderer);
    }

    public FontValue(@NotNull String valueName, @NotNull FontRenderer value, @NotNull Function0<Boolean> displayable) {
        Intrinsics.checkParameterIsNotNull(valueName, "valueName");
        Intrinsics.checkParameterIsNotNull(value, "value");
        Intrinsics.checkParameterIsNotNull(displayable, "displayable");
        super(valueName, value, displayable);
    }

    public FontValue(@NotNull String valueName, @NotNull FontRenderer value) {
        Intrinsics.checkParameterIsNotNull(valueName, "valueName");
        Intrinsics.checkParameterIsNotNull(value, "value");
        this(valueName, value, (Function0<Boolean>)1.INSTANCE);
    }
}

