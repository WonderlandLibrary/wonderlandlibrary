// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.gui.screen.impl.mainmenu;

import exhibition.management.SubFolder;
import java.io.File;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.Minecraft;
import exhibition.Client;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import exhibition.gui.screen.PanoramaScreen;

public class ClientMainMenu extends PanoramaScreen
{
    private static int key;
    private static final GuiVanillaMainMenu menuVanilla;
    private static final GuiModdedMainMenu menuModded;
    
    @Override
    public void initGui() {
        this.load();
        if (this.getClass().equals(ClientMainMenu.class)) {
            this.display();
        }
    }
    
    private void load() {
        String file = "";
        try {
            file = FileUtils.readFileToString(this.getFile());
        }
        catch (IOException e) {
            return;
        }
        for (final String line : file.split("\n")) {
            if (line.contains("key")) {
                final String[] split = line.split(":");
                if (split.length > 1) {
                    try {
                        ClientMainMenu.key = Integer.parseInt(split[1]);
                    }
                    catch (NumberFormatException ex) {}
                }
            }
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == ClientMainMenu.key) {
            this.toggleVanilla();
            this.display();
        }
    }
    
    private void display() {
        if (Client.isHidden()) {
            Minecraft.getMinecraft().displayGuiScreen(ClientMainMenu.menuVanilla);
        }
        else {
            Minecraft.getMinecraft().displayGuiScreen(new GuiModdedMainMenu());
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public void toggleVanilla() {
        Client.setHidden(!Client.isHidden());
        this.save();
    }
    
    public void save() {
        try {
            FileUtils.write(this.getFile(), (CharSequence)("Swap key (Toggles menus):" + ClientMainMenu.key));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public File getFile() {
        final File file = new File(this.getFolder().getAbsolutePath() + File.separator + "MainMenu.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
    
    public File getFolder() {
        final File folder = new File(Client.getDataDir().getAbsolutePath() + File.separator + SubFolder.Other.getFolderName());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder;
    }
    
    static {
        ClientMainMenu.key = 41;
        menuVanilla = new GuiVanillaMainMenu();
        menuModded = new GuiModdedMainMenu();
    }
}
