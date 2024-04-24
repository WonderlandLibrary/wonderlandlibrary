package Method.Client.utils.Screens.Override;

import Method.Client.module.Module;
import Method.Client.module.ModuleManager;
import Method.Client.module.player.ChestStealer;
import Method.Client.utils.Screens.Screen;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraftforge.client.event.GuiScreenEvent;

public class ChestGuiInsert extends Screen {


    @Subscribe
    public void GuiScreenEventPost(GuiScreenEvent.ActionPerformedEvent.Post event) {
        if (event.getGui() instanceof GuiChest) {
            if (event.getButton().id == 11209) {
                ChestStealer.Mode.setValString("Steal");
                toggle2();
            }
            if (event.getButton().id == 11210) {
                ChestStealer.Mode.setValString("Store");
                toggle2();
            }
            if (event.getButton().id == 11211) {
                ChestStealer.Mode.setValString("Drop");
                toggle2();
            }
        }
    }

    @Subscribe
    public void GuiScreenEventInit(GuiScreenEvent.InitGuiEvent.Post event) {
        // We use a high button id to avoid any conflicts with other mods
        if (event.getGui() instanceof GuiChest) {
            event.getButtonList().add(new GuiButton(11209, event.getGui().width / 2 + 100, event.getGui().height / 2 - ((GuiChest) event.getGui()).getYSize() + 110, 50, 20, "Steal"));
            event.getButtonList().add(new GuiButton(11210, event.getGui().width / 2 + 100, event.getGui().height / 2 - ((GuiChest) event.getGui()).getYSize() + 130, 50, 20, "Store"));
            event.getButtonList().add(new GuiButton(11211, event.getGui().width / 2 + 100, event.getGui().height / 2 - ((GuiChest) event.getGui()).getYSize() + 150, 50, 20, "Drop"));
        }
    }

    private static void toggle2() {
        Module Chest = ModuleManager.getModuleByName("ChestStealer");
        if (Chest.isToggled())
            Chest.toggle();
        Chest.toggle();
    }


}
