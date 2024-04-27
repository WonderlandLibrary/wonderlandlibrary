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
extends Value {
    @Override
    public JsonElement toJson() {
        Fonts.FontInfo fontInfo = Fonts.getFontDetails((IFontRenderer)this.getValue());
        if (fontInfo == null) {
            return null;
        }
        Fonts.FontInfo fontInfo2 = fontInfo;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("fontName", fontInfo2.getName());
        jsonObject.addProperty("fontSize", (Number)fontInfo2.getFontSize());
        return (JsonElement)jsonObject;
    }

    @Override
    public void fromJson(JsonElement jsonElement) {
        if (!jsonElement.isJsonObject()) {
            return;
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        this.setValue(Fonts.getFontRenderer(jsonObject.get("fontName").getAsString(), jsonObject.get("fontSize").getAsInt()));
    }

    public FontValue(String string, IFontRenderer iFontRenderer) {
        super(string, iFontRenderer);
    }
}

