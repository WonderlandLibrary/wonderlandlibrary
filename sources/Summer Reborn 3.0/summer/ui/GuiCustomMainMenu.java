package summer.ui;

import net.minecraft.client.gui.*;
import net.minecraft.util.ResourceLocation;
import summer.Summer;
import summer.base.utilities.RenderUtils;
import summer.ui.altmanager.GuiAltManager;
import summer.ui.changelog.GuiChanges;

import java.awt.*;
import java.io.IOException;

public class GuiCustomMainMenu extends GuiScreen implements GuiYesNoCallback {

    @Override
    public void initGui() {
    		 String[] displayNames = new String[]{"Singleplayer", "Multiplayer", "Options", "Alt Manager", "Changelog", "Exit"};
             int offset = height / 2 - 50;
             for (int i = 0; i < displayNames.length; i++) {
                 String name = displayNames[i];
                 buttonList.add(new GuiButton(i, width / 2 - 75, offset, 180, 20, name));
                 offset += 28;
             }

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
//        RenderUtils.drawRect(0, 0, width, height, Color.BLACK.getRGB());

        ResourceLocation background = new ResourceLocation("textures/menu/SummerBackGround.png");
        this.mc.getTextureManager().bindTexture(background);
        RenderUtils.drawImage(0, 0, 0, 0, width, height, width, height);

        	Gui.drawGradientRect(width / 2 - 80, height / 2 - 72, width / 2 + 110, height / 2  + (5 * 23), new Color(10, 10, 10, 255).getRGB(), new Color(10, 10, 10, 255).getRGB());
            Gui.drawGradientRect(width / 2 - 79, height / 2 - 71, width / 2 + 109, height / 2 - 1 + (5 * 23), new Color(59, 59, 59, 255).getRGB(), new Color(59, 59, 59, 255).getRGB());
//            Gui.drawGradientRect(width / 2 - 76, height / 2 - 69, width / 2 + 106, height / 2 - 28 + (5 * 23), new Color(74, 74, 74, 255).getRGB(), new Color(74, 74, 74, 255).getRGB());
            Gui.drawGradientRect(width / 2 - 78, height / 2 - 70, width / 2 + 108, height / 2 - 2 + (5 * 23), new Color(20, 20, 20, 255).getRGB(), new Color(10, 10, 10, 255).getRGB());
        Summer.INSTANCE.getFontManager().arial20.drawCenteredString(Summer.NAME + " " + "v" + Summer.VERSION, width / 2 + 13, height / 2 - 67, -1);
            super.drawScreen(mouseX, mouseY, partialTicks);

    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        }

        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }

        if (button.id == 2) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }

        if (button.id == 3) {
            this.mc.displayGuiScreen(new GuiAltManager());

        }
        if (button.id == 4) {
            this.mc.displayGuiScreen(new GuiChanges());

        }

        if (button.id == 5) {
            this.mc.shutdown();
        }


        super.actionPerformed(button);
    }


    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
