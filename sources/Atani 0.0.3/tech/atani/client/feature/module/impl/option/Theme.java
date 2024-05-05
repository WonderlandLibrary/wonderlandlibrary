package tech.atani.client.feature.module.impl.option;

import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.module.impl.hud.ClickGui;
import tech.atani.client.feature.module.impl.hud.ModuleList;
import tech.atani.client.feature.module.impl.hud.TargetHUD;
import tech.atani.client.feature.module.impl.hud.WaterMark;
import tech.atani.client.feature.module.storage.ModuleStorage;
import tech.atani.client.feature.value.impl.StringBoxValue;
import tech.atani.client.feature.value.interfaces.ValueChangeListener;
import tech.atani.client.loader.Modification;

@ModuleData(name = "Theme", description = "Load theme presets", category = Category.OPTIONS, frozenState = true, enabled = true)
public class Theme extends Module {

    private WaterMark waterMark;
    private ModuleList moduleList;
    private TargetHUD targetHUD;
    private ClickGui clickGui;
    private FontRenderer fontRenderer;

    public final StringBoxValue preset = new StringBoxValue("Preset", "Which preset to load?", this, new String[]{"None", "Atani Modern", "Atani Simple", "Atani Golden", "Augustus 2.6", "Xave", "Ryu", "Icarus", "Fatality", "Koks", "Tarasande"}, new ValueChangeListener[]{(stage, value, oldValue, newValue) -> {
        if(Modification.INSTANCE.isLoaded()) {
            if(waterMark == null || moduleList == null || clickGui == null || targetHUD == null || fontRenderer == null){
                waterMark = ModuleStorage.getInstance().getByClass(WaterMark.class);
                moduleList = ModuleStorage.getInstance().getByClass(ModuleList.class);
                clickGui = ModuleStorage.getInstance().getByClass(ClickGui.class);
                targetHUD = ModuleStorage.getInstance().getByClass(TargetHUD.class);
                fontRenderer = ModuleStorage.getInstance().getByClass(FontRenderer.class);
            }
            waterMark.watermarkMode.setValue((String) newValue);
            moduleList.moduleListMode.setValue((String) newValue);
            targetHUD.targethudMode.setValue((String) newValue);
            clickGui.mode.setValue((String) newValue);
            switch ((String) newValue) {
                case "Atani Golden":
                case "Atani Simple":
                case "Atani Modern":
                case "Icarus":
                    fontRenderer.mode.setValue("Modern");
                    break;
                case "Koks":
                case "Xave":
                case "Ryu":
                case "Fatality":
                case "Tarasande":
                case "Atani CS:GO":
                case "Augustus 2.6":
                    fontRenderer.mode.setValue("Classic");
                    break;
            }
        }
    }});

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}
}
