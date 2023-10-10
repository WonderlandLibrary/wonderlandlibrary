package tech.atani.client.feature.module.impl.hud;

import com.google.common.base.Supplier;
import org.lwjgl.input.Keyboard;
import tech.atani.client.feature.guis.screens.clickgui.astolfo.AstolfoClickGuiScreen;
import tech.atani.client.feature.guis.screens.clickgui.atani.AtaniClickGuiScreen;
import tech.atani.client.feature.guis.screens.clickgui.augustus.AugustusClickGuiScreen;
import tech.atani.client.feature.guis.screens.clickgui.fatality.FatalityClickGuiScreen;
import tech.atani.client.feature.guis.screens.clickgui.koks.KoksClickGuiScreen;
import tech.atani.client.feature.guis.screens.clickgui.simple.SimpleClickGuiScreen;
import tech.atani.client.feature.guis.screens.clickgui.tarasande.TarasandeClickGuiScreen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.guis.screens.clickgui.oldaugustus.OldAugustusClickGuiScreen;
import tech.atani.client.feature.guis.screens.clickgui.golden.GoldenClickGuiScreen;
import tech.atani.client.feature.guis.screens.clickgui.icarus.IcarusClickGuiScreen;
import tech.atani.client.feature.guis.screens.clickgui.ryu.RyuClickGuiScreen;
import tech.atani.client.feature.guis.screens.clickgui.xave.XaveClickGuiScreen;
import tech.atani.client.feature.value.impl.CheckBoxValue;
import tech.atani.client.feature.value.impl.SliderValue;
import tech.atani.client.feature.value.impl.StringBoxValue;

@ModuleData(name = "ClickGui", description = "A clicky gui", category = Category.HUD, key = Keyboard.KEY_RSHIFT)
public class ClickGui extends Module {

    public final StringBoxValue mode = new StringBoxValue("Mode", "Which mode will the module use?", this, new String[]{"Atani Simple", "Atani CS:GO", "Atani Golden", "Augustus", "Augustus 2.6", "Xave", "Ryu", "Icarus", "Fatality", "Koks", "Tarasande", "Astolfo"});
    public final CheckBoxValue openingAnimation = new CheckBoxValue("Opening Animation", "Animate the opening and closing of the gui?", this, true);
    public final StringBoxValue dropdownAnimation = new StringBoxValue("Animation Mode", "How will the opening animation look like?", this, new String[]{"Scale-In", "Frame Scale-In", "Left to Right", "Right to Left", "Up to Down", "Down to Up"}, new Supplier[]{() ->
            mode.getValue().equalsIgnoreCase("Atani Simple") || // Dropdown guis go here
            mode.getValue().equalsIgnoreCase("Koks") ||
            mode.getValue().equalsIgnoreCase("Augustus 2.6") ||
            mode.getValue().equalsIgnoreCase("Xave") ||
            mode.getValue().equalsIgnoreCase("Tarasande") ||
            mode.getValue().equalsIgnoreCase("Astolfo") ||
            mode.getValue().equalsIgnoreCase("Icarus")}).setIdName("Dropdown Animation Mode");
    public final StringBoxValue nonDropdownAnimation = new StringBoxValue("Animation Mode", "How will the opening animation look like?", this, new String[]{"Left to Right", "Right to Left", "Up to Down", "Down to Up"}, new Supplier[]{() ->
            mode.getValue().equalsIgnoreCase("Atani Golden") || // Non-Dropdown guis go here
                    mode.getValue().equalsIgnoreCase("Atani CS:GO") ||
                    mode.getValue().equalsIgnoreCase("Augustus") ||
                    mode.getValue().equalsIgnoreCase("Ryu") || // Ryu is special cause it's retarded
                    mode.getValue().equalsIgnoreCase("Fatality")}).setIdName("Non-Dropdown Animation Mode");
    public final SliderValue<Integer> red = new SliderValue<>("Red", "What'll be the red of the colored elements?", this, 255, 0, 255, 0, new Supplier[]{() -> mode.getValue().equalsIgnoreCase("Augustus")});
    public final SliderValue<Integer> green = new SliderValue<>("Green", "What'll be the green of the colored elements?", this, 197, 0, 255, 0, new Supplier[]{() -> mode.getValue().equalsIgnoreCase("Augustus")});
    public final SliderValue<Integer> blue = new SliderValue<>("Blue", "What'll be the blue of the colored elements?", this, 188, 0, 255, 0, new Supplier[]{() -> mode.getValue().equalsIgnoreCase("Augustus")});

    public static FatalityClickGuiScreen clickGuiScreenFatality;
    public static SimpleClickGuiScreen clickGuiScreenSimple;
    public static AtaniClickGuiScreen clickGuiScreenAtani;
    public static GoldenClickGuiScreen clickGuiScreenGolden;
    public static AugustusClickGuiScreen clickGuiScreenAugustus;
    public static OldAugustusClickGuiScreen clickGuiScreenOldAugustus;
    public static XaveClickGuiScreen clickGuiScreenXave;
    public static RyuClickGuiScreen clickGuiScreenRyu;
    public static IcarusClickGuiScreen clickGuiScreenIcarus;
    public static KoksClickGuiScreen clickGuiScreenKoks;
    public static TarasandeClickGuiScreen clickGuiScreenTarasande;
    public static AstolfoClickGuiScreen clickGuiScreenAstolfo;

    @Override
    public void onEnable() {
        switch (this.mode.getValue()) {
            case "Astolfo":
                if(clickGuiScreenAstolfo == null) {
                    clickGuiScreenAstolfo = new AstolfoClickGuiScreen();
                }
                mc.displayGuiScreen(clickGuiScreenAstolfo);
                break;
            case "Tarasande":
                if(clickGuiScreenTarasande == null) {
                    clickGuiScreenTarasande = new TarasandeClickGuiScreen();
                }
                mc.displayGuiScreen(clickGuiScreenTarasande);
                break;
            case "Koks":
                if(clickGuiScreenKoks == null) {
                    clickGuiScreenKoks = new KoksClickGuiScreen();
                }
                mc.displayGuiScreen(clickGuiScreenKoks);
                break;
            case "Augustus":
                if(clickGuiScreenAugustus == null) {
                    clickGuiScreenAugustus = new AugustusClickGuiScreen();
                }
                mc.displayGuiScreen(clickGuiScreenAugustus);
                break;
            case "Atani CS:GO":
                if(clickGuiScreenAtani == null) {
                    clickGuiScreenAtani = new AtaniClickGuiScreen();
                }
                mc.displayGuiScreen(clickGuiScreenAtani);
                break;
            case "Atani Simple":
                if(clickGuiScreenSimple == null) {
                    clickGuiScreenSimple = new SimpleClickGuiScreen();
                }
                mc.displayGuiScreen(clickGuiScreenSimple);
                break;
            case "Atani Golden":
                if(clickGuiScreenGolden == null) {
                    clickGuiScreenGolden = new GoldenClickGuiScreen();
                }
                mc.displayGuiScreen(clickGuiScreenGolden);
                break;
            case "Augustus 2.6":
                if(clickGuiScreenOldAugustus == null) {
                    clickGuiScreenOldAugustus = new OldAugustusClickGuiScreen();
                }
                mc.displayGuiScreen(clickGuiScreenOldAugustus);
                break;
            case "Xave":
                if(clickGuiScreenXave == null) {
                    clickGuiScreenXave = new XaveClickGuiScreen();
                }
                mc.displayGuiScreen(clickGuiScreenXave);
                break;
            case "Ryu":
                if(clickGuiScreenRyu == null) {
                    clickGuiScreenRyu = new RyuClickGuiScreen();
                }
                mc.displayGuiScreen(clickGuiScreenRyu);
                break;
            case "Icarus":
                if(clickGuiScreenIcarus == null) {
                    clickGuiScreenIcarus = new IcarusClickGuiScreen();
                }
                mc.displayGuiScreen(clickGuiScreenIcarus);
                break;
            case "Fatality": {
                if(clickGuiScreenFatality == null) {
                    clickGuiScreenFatality = new FatalityClickGuiScreen();
                }
                mc.displayGuiScreen(clickGuiScreenFatality);
                break;
            }
            default: {
                this.mode.setValue("Atani Simple");
                break;
            }
        }
        this.setEnabled(false);
    }

    @Override
    public void onDisable() {}

}
