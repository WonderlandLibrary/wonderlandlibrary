package info.sigmaclient.gui.screen.impl.mainmenu;

import info.sigmaclient.Client;
import info.sigmaclient.gui.screen.GuiLogin;
import info.sigmaclient.gui.screen.PanoramaScreen;
import info.sigmaclient.management.SubFolder;
import info.sigmaclient.management.users.UserManager;
import net.minecraft.client.Minecraft;

import org.apache.commons.io.FileUtils;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientMainMenu extends PanoramaScreen {
    private static int key = Keyboard.KEY_GRAVE;
    private static final GuiVanillaMainMenu menuVanilla = new GuiVanillaMainMenu();
    private static final GuiModdedMainMenu menuModded = new GuiModdedMainMenu();

    public void initGui() {
        load();
        if (getClass().equals(ClientMainMenu.class)) {
            display();
        }
    }

    private void load() {
        Client.hasSetup = true;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == key) {
            toggleVanilla();
            display();
        }
    }

    private void display() {
        if (Client.isHidden()) {
            Minecraft.getMinecraft().displayGuiScreen(menuVanilla);
        } else {//menuModded
            Minecraft.getMinecraft().displayGuiScreen(new GuiModdedMainMenu());
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void toggleVanilla() {
        Client.setHidden(!Client.isHidden());
        //save();
    }

    public static void save() {
        //List<String> fileContent = new ArrayList<>();
        //fileContent.add(String.format("%s:%s", "key", key));
        //fileContent.add(String.format("%s:%s", "setup", Client.hasSetup));
        //fileContent.add(String.format("%s:%s", "lowend", Client.isLowEndPC));
        //info.sigmaclient.util.FileUtils.write(getFile(), fileContent, true);
    }

}
