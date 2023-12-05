package rip.helium.ui.main.tab.cheat;

import rip.helium.Helium;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.ui.main.Interface;
import rip.helium.ui.main.components.tab.cheat.ContainerCheats;
import rip.helium.ui.main.components.tab.cheat.ContainerProperties;
import rip.helium.ui.main.tab.Tab;

/**
 * @author antja03
 */
public class TabDefaultCheat extends Tab {

    private final CheatCategory cheatCategory;

    private Cheat selectedCheat;

    public TabDefaultCheat(Interface theInterface, CheatCategory cheatCategory) {
        super(theInterface);
        this.cheatCategory = cheatCategory;
        components.add(new ContainerCheats(theInterface, this, cheatCategory, 25, 2, 150, theInterface.getHeight()));
        for (Cheat module : Helium.instance.cheatManager.getCheatRegistry().values()) {
            if (module.getCategory() == cheatCategory) {
                this.components.add(new ContainerProperties(theInterface, this, module, 175, 0, 150, theInterface.getHeight()));
            }
        }
    }

    @Override
    public void onTick() {
        getInterface().setWidth(300);
        super.onTick();
    }

    public Cheat getSelectedCheat() {
        return selectedCheat;
    }

    public void setSelectedCheat(Cheat selectedCheat) {
        this.selectedCheat = selectedCheat;
    }
}
