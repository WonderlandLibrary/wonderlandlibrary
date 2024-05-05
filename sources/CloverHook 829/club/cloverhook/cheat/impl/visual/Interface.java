package club.cloverhook.cheat.impl.visual;

import org.lwjgl.input.Keyboard;

import club.cloverhook.Cloverhook;
import club.cloverhook.cheat.Cheat;
import club.cloverhook.cheat.CheatCategory;

/**
 * @author antja03
 */
public class Interface extends Cheat {

    public Interface() {
        super("Interface", "Opens the cloverhook interface when toggled.", CheatCategory.MISC);
        setBind(Keyboard.KEY_RSHIFT);
    }

    public void onEnable() {
        getMc().displayGuiScreen(Cloverhook.instance.userInterface);
        setState(false, false);
    }

}
