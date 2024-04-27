package me.aquavit.liquidsense.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.aquavit.liquidsense.ui.font.Fonts;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FontValue extends Value<FontRenderer> {
    public FontValue(String valueName, FontRenderer value) {
        super(valueName, value);
    }

    @Override
    public JsonElement toJson() {
        Object[] fontDetails = Fonts.getFontDetails(this.getValue());
        if (fontDetails == null) return null;
        JsonObject valueObject = new JsonObject();
        valueObject.addProperty("fontName", (String) fontDetails[0]);
        valueObject.addProperty("fontSize", (int) fontDetails[1]);
        return valueObject;
    }

    @Override
    public void fromJson(JsonElement element) {
        if (!element.isJsonObject()) return;
        JsonObject valueObject = element.getAsJsonObject();
        value = Fonts.getFontRenderer(valueObject.get("fontName").getAsString(), valueObject.get("fontSize").getAsInt());
    }
}
