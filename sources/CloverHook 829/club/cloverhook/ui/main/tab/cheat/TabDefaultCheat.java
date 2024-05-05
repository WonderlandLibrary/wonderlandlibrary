package club.cloverhook.ui.main.tab.cheat;

import club.cloverhook.Cloverhook;
import club.cloverhook.cheat.Cheat;
import club.cloverhook.cheat.CheatCategory;
import club.cloverhook.ui.main.Interface;
import club.cloverhook.ui.main.components.tab.cheat.ContainerCheats;
import club.cloverhook.ui.main.components.tab.cheat.ContainerProperties;
import club.cloverhook.ui.main.tab.Tab;

/**
 * @author antja03
 */
public class TabDefaultCheat extends Tab {

    private CheatCategory cheatCategory;

    private Cheat selectedCheat;

    public TabDefaultCheat(Interface theInterface, CheatCategory cheatCategory) {
        super(theInterface);
        this.cheatCategory = cheatCategory;
        components.add(new ContainerCheats(theInterface, this, cheatCategory, 25, 2, 150, theInterface.getHeight()));
        for (Cheat module : Cloverhook.instance.cheatManager.getCheatRegistry().values()) {
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
