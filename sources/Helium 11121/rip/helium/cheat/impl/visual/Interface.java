package rip.helium.cheat.impl.visual;

import org.lwjgl.input.Keyboard;
import rip.helium.Helium;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.utils.property.impl.BooleanProperty;

/**
 * @author antja03
 */
public class Interface extends Cheat {

    public static BooleanProperty description = new BooleanProperty("Cheat Descriptions", "Cheat Descriptions under the name", null, true);

    public Interface() {
        super("Interface", "Opens the interface.", CheatCategory.MISC);
        setBind(Keyboard.KEY_RSHIFT);
        registerProperties(description);
    }

    public void onEnable() {
        getMc().displayGuiScreen(Helium.instance.userInterface);
        setState(false, false);
    }

}
