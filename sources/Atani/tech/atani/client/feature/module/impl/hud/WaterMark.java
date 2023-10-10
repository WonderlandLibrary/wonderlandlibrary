package tech.atani.client.feature.module.impl.hud;

import tech.atani.client.feature.theme.ThemeObject;
import tech.atani.client.feature.value.Value;
import tech.atani.client.feature.value.interfaces.ValueChangeListener;
import tech.atani.client.feature.theme.data.enums.ElementType;
import tech.atani.client.feature.theme.storage.ThemeStorage;
import tech.atani.client.listener.event.minecraft.render.Render2DEvent;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.module.impl.hud.clientOverlay.IClientOverlayComponent;
import tech.atani.client.utility.interfaces.ColorPalette;
import tech.atani.client.utility.math.atomic.AtomicFloat;
import tech.atani.client.feature.value.impl.StringBoxValue;

@ModuleData(name = "WaterMark", description = "Shows a watermark", category = Category.HUD)
public class WaterMark extends Module implements ColorPalette, IClientOverlayComponent {

    public StringBoxValue watermarkMode = new StringBoxValue("Watermark Mode", "Which watermark will be displayed?", this, new String[]{"None", "Atani Simple", "Atani Modern", "Atani Golden", "Augustus 2.6", "Xave", "Ryu", "Icarus", "Fatality", "Koks", "Tarasande", "OHareWare"}, new ValueChangeListener[]{new ValueChangeListener() {
        @Override
        public void onChange(Stage stage, Value value, Object oldValue, Object newValue) {
            try {
                if(stage == Stage.POST) {
                    if(oldValue != null && !((String) oldValue).equalsIgnoreCase("None"))
                        ThemeStorage.getInstance().getThemeObject(((String) oldValue), ElementType.WATERMARK).onDisable();
                    if(newValue != null && !((String) newValue).equalsIgnoreCase("None"))
                        ThemeStorage.getInstance().getThemeObject(((String) newValue), ElementType.WATERMARK).onEnable();
                }
            } catch (Exception e) {
                // ignored
            }
        }
    }});

    @Override
    public void draw(Render2DEvent render2DEvent, AtomicFloat leftY, AtomicFloat rightY) {
        ThemeObject themeObject = ThemeStorage.getInstance().getThemeObject(watermarkMode.getValue(), ElementType.WATERMARK);
        if(this.isEnabled() && !this.watermarkMode.getValue().equalsIgnoreCase("None") && themeObject != null) {
            themeObject.onDraw(render2DEvent.getScaledResolution(), render2DEvent.getPartialTicks(), leftY, rightY, new Object[0]);
        }
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

}
