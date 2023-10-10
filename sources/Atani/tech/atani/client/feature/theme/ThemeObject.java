package tech.atani.client.feature.theme;

import com.google.gson.JsonObject;
import net.minecraft.client.gui.ScaledResolution;
import tech.atani.client.feature.value.Value;
import tech.atani.client.feature.value.storage.ValueStorage;
import tech.atani.client.feature.theme.data.ThemeObjectInfo;
import tech.atani.client.feature.theme.data.enums.ElementType;
import tech.atani.client.feature.theme.data.enums.ScreenType;
import tech.atani.client.feature.theme.data.enums.ThemeObjectType;
import tech.atani.client.utility.interfaces.ColorPalette;
import tech.atani.client.utility.interfaces.Methods;
import tech.atani.client.utility.math.atomic.AtomicFloat;

import java.util.List;

public abstract class ThemeObject implements Methods, ColorPalette {

    private final String name;
    private final ThemeObjectType themeObjectType;
    private final ElementType elementType;
    private final ScreenType screenType;

    public ThemeObject() {
        ThemeObjectInfo themeObjectInfo = this.getClass().getAnnotation(ThemeObjectInfo.class);
        if(themeObjectInfo == null)
            throw new RuntimeException();
        this.name = themeObjectInfo.name();
        this.themeObjectType = themeObjectInfo.themeObjectType();
        this.elementType = themeObjectInfo.elementType();
        this.screenType = themeObjectInfo.screenType();
    }

    public abstract void onEnable();

    public abstract void onDisable();

    public void onDraw(ScaledResolution scaledResolution, float partialTicks, AtomicFloat leftY, AtomicFloat rightY, Object[] params) {
        if(this.themeObjectType == ThemeObjectType.SCREEN) {
            throw new RuntimeException("Gui Screen objects should not be executed on drawing.");
        }
    }

    public JsonObject save() {
        JsonObject object = new JsonObject();
        List<Value> values = ValueStorage.getInstance().getValues(this);
        if (values != null && !values.isEmpty()) {
            JsonObject propertiesObject = new JsonObject();
            for (Value property : values) {
                propertiesObject.addProperty(property.getIdName(), property.getValueAsString());
            }
            object.add("Values", propertiesObject);
        }
        return object;
    }

    public void load(JsonObject object) {
        List<Value> values = ValueStorage.getInstance().getValues(this);

        if (object.has("Values") && values != null && !values.isEmpty()) {
            JsonObject propertiesObject = object.getAsJsonObject("Values");
            for (Value property : values) {
                if (propertiesObject.has(property.getIdName())) {
                    property.setValue(propertiesObject.get(property.getIdName()).getAsString());
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public ThemeObjectType getThemeObjectType() {
        return themeObjectType;
    }

    public ElementType getElementType() {
        return elementType;
    }

    public ScreenType getScreenType() {
        return screenType;
    }
}
