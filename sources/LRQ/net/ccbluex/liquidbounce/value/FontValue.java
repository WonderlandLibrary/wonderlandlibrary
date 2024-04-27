/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 */
package net.ccbluex.liquidbounce.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.value.Value;

public final class FontValue
extends Value<IFontRenderer> {
    @Override
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
    public void fromJson(JsonElement element) {
        if (!element.isJsonObject()) {
            return;
        }
        JsonObject valueObject = element.getAsJsonObject();
        this.setValue(Fonts.getFontRenderer(valueObject.get("fontName").getAsString(), valueObject.get("fontSize").getAsInt()));
    }

    public FontValue(String valueName, IFontRenderer value) {
        super(valueName, value);
    }
}

