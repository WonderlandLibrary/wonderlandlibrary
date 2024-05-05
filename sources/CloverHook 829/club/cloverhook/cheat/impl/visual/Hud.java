package club.cloverhook.cheat.impl.visual;

import club.cloverhook.cheat.Cheat;
import club.cloverhook.cheat.CheatCategory;
import club.cloverhook.event.minecraft.PlayerUpdateEvent;
import club.cloverhook.utils.property.impl.BooleanProperty;
import club.cloverhook.utils.property.impl.ColorProperty;
import club.cloverhook.utils.property.impl.DoubleProperty;
import club.cloverhook.utils.property.impl.StringsProperty;
import me.hippo.systems.lwjeb.annotation.Collect;
 
public class Hud extends Cheat {

    public BooleanProperty   prop_logo    = new BooleanProperty("Watermark", "Show Watermark",null, true);
    public StringsProperty   prop_logomode    = new StringsProperty("Watermark Mode", "Changes watermark theme.",() -> prop_logo.getValue(), false, true, new String[]{"Image", "Exhi", "Ayylmao"}, new Boolean[]{false, true, false});
    public BooleanProperty   prop_arraylist    = new BooleanProperty("Arraylist", "Show Arraylist",null, true);
    public StringsProperty prop_colormode = new StringsProperty("ArrayList Color Mode", "Color mode of the arraylist", () -> prop_arraylist.getValue(), false, true, new String[]{"Custom", "Rainbow", "Categories"}, new Boolean[]{false,false,true});
    //public BooleanProperty   prop_rainbow    = new BooleanProperty("Rainbow Arraylist", "",() -> prop_arraylist.getValue(), true);
    public BooleanProperty   prop_customfont    = new BooleanProperty("Custom Font", "Use verdana as the client font.",() -> prop_arraylist.getValue(), true);

    public StringsProperty prop_arraylistmode = new StringsProperty("Arraylist Mode", "", null,
            false, true, new String[] {"Cloverhook", "Plain"}, new Boolean[] {true, false, false});
    public ColorProperty prop_color = new ColorProperty("Arraylist Color", "", () -> prop_arraylist.getValue() && prop_colormode.getValue().get("Custom"), 1f, 0f, 1f, 255);
    
    public Hud() {
        super("Overlay", "", CheatCategory.VISUAL);
        registerProperties(prop_logo, prop_logomode,prop_arraylist,prop_colormode, prop_customfont, prop_arraylistmode, prop_color);
    }

    @Collect
    public void onUpdate(PlayerUpdateEvent e) { 
    }

}
