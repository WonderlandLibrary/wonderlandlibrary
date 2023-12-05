package rip.helium.cheat.impl.visual;

import me.hippo.systems.lwjeb.annotation.Collect;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.event.minecraft.KeyPressEvent;
import rip.helium.ui.main.tab.TabGui;
import rip.helium.utils.property.impl.*;

import java.awt.*;

public class Hud extends Cheat {

    public static BooleanProperty radar = new BooleanProperty("Radar", "Radar", null, true);
    public static BooleanProperty prop_arraylist = new BooleanProperty("Arraylist", "Show Arraylist", null, true);
    public static BooleanProperty scoreboard_enabled = new BooleanProperty("Scoreboard", "Scoreboard Status.", null, true);
    public static BooleanProperty shadowfont_enabled = new BooleanProperty("Shadow", "Shadow Font", null, false);
    public static StringsProperty prop_theme = new StringsProperty("Mode", "dank", null,
            false, true, new String[]{"Helium", "Virtue", "Memestick", "Memeware", "Exhi", "Helium2", "NSFWImage", "NSFWImage2"}, new Boolean[]{true, false, false, false, false, false, false, false});
    public static TextProperty clientName = new TextProperty("Client Name", "Changes the client name", null, "§4§lH§felium");
    public static BooleanProperty hud_tnt = new BooleanProperty("Tnt Alert", "Alerts you if tnt is close to you", null, true);
    public static BooleanProperty tabui = new BooleanProperty("Tab GUI", "Renders the tab gui ", null, false);
    public static BooleanProperty lowercase = new BooleanProperty("Lowercase", "Makes the arraylist lowercase.", null, true);
    public static DoubleProperty down = new DoubleProperty("Scoreboard Down", "Scoreboard Down.", null, 10, -200, 200.0, 1, null);
    public static BooleanProperty scoreboard_background = new BooleanProperty("Scoreboard Background", "Background status.", null, true);
    public static StringsProperty prop_colormode = new StringsProperty("ArrayList Color Mode", "Color mode of the arraylist", () -> prop_arraylist.getValue(), false, true, new String[]{"Wave", "Custom", "Rainbow", "Rainbow2", "Categories", "Pulsing"}, new Boolean[]{true, false, false, false, false, false});
    //public BooleanProperty   prop_rainbow    = new BooleanProperty("Rainbow Arraylist", "",() -> prop_arraylist.getValue(), true);
    public static BooleanProperty targethud = new BooleanProperty("Target Hud", "Displays the target's ping and health.", null, true);
    public static DoubleProperty backgroundcolor = new DoubleProperty("Background Opacity", "Changes the opacity.", null, 0.3, 0, 1, 0.1, null);
    public static ColorProperty prop_color = new ColorProperty("Arraylist Color", "", () -> prop_arraylist.getValue(), 1f, 0f, 1f, 255);
    public static Color colorr;
    private static final int brightness = 130;
    private static boolean ascending;
    private static final float[] hsb = new float[3];
    public BooleanProperty prop_logo = new BooleanProperty("Watermark", "Show Watermark", null, true);
    public StringsProperty prop_logomode = new StringsProperty("Watermark Mode", "Changes watermark theme.", () -> prop_logo.getValue(), false, true, new String[]{"Helium"}, new Boolean[]{false, true, false});
    public BooleanProperty prop_customfont = new BooleanProperty("Custom Font", "Use verdana as the client font.", () -> prop_arraylist.getValue(), true);
    public StringsProperty prop_arraylistmode = new StringsProperty("Arraylist Mode", "", null,
            false, true, new String[]{"Helium", "Plain"}, new Boolean[]{true, false});
    private float hue;
    private Hud hud;

    public Hud() {
        super("Hud", "", CheatCategory.VISUAL);
        registerProperties(targethud, radar, lowercase, tabui, prop_theme, clientName, hud_tnt, backgroundcolor, scoreboard_background, down, shadowfont_enabled, prop_arraylist, prop_colormode/*/, prop_customfont/*/, prop_color);
    }

    @Collect
    public void keyEvent(KeyPressEvent event) {
        if (tabui.getValue()) {
            TabGui.onKey(event.getKeyCode());
        }
    }

}
