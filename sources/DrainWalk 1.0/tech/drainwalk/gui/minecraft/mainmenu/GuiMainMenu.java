package tech.drainwalk.gui.minecraft.mainmenu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import tech.drainwalk.DrainWalk;
import tech.drainwalk.font.FontManager;
import tech.drainwalk.gui.minecraft.Button;
import tech.drainwalk.gui.minecraft.altmanager.GuiAltManager;
import tech.drainwalk.utility.color.ColorUtility;
import tech.drainwalk.utility.render.GLUtility;
import tech.drainwalk.utility.render.RenderUtility;

import java.io.IOException;

public class GuiMainMenu extends GuiScreen {
    @Override
    public void initGui() {
        int width = 143;
        int height = 28 + 6;
        int posX = (int) ((mc.displayWidth / 4f) - (width / 2f));
        int posY = (int) (mc.displayHeight / 4f) + 50;

        this.buttonList.add(new Button(0, posX, posY - (height * 2), "Singleplayer"));
        this.buttonList.add(new Button(1, posX, posY - height, "Multiplayer"));
        this.buttonList.add(new Button(2, posX, posY, "Accounts"));
        this.buttonList.add(new Button(3, posX, posY + height, "Options"));


        this.buttonList.add(new Button(4, posX, posY + (height * 2), "Exit"));

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GLUtility.INSTANCE.rescale(2);
        float posX = mc.displayWidth / 4f;
        float posY = mc.displayHeight / 4f;
        GlStateManager.enableBlend();
        RenderUtility.drawRoundedTexture(new ResourceLocation("drainwalk/images/main_menu.png"), 0, 0, mc.displayWidth / 2f, mc.displayHeight / 2f, 0, 1);
        if (DrainWalk.getInstance().isRoflMode()) {
            RenderUtility.drawRoundedTexture(new ResourceLocation("drainwalk/images/deus_mode.png"), 0, 0, mc.displayWidth / 2f, mc.displayHeight / 2f, 0, 0.1f);
        }
        RenderUtility.drawRoundedTexture(new ResourceLocation("drainwalk/images/main_menu_image.png"), posX - (306 / 1.765f), posY - (74 * 2.31f), 347, 343, 0, 1);
        RenderUtility.drawRoundedTexture(new ResourceLocation("drainwalk/images/logo.png"), posX - (50), posY - (140), 100, 100, 15, 100);
        if (DrainWalk.getInstance().isRoflMode()) {
            RenderUtility.drawRoundedTexture(new ResourceLocation("drainwalk/images/deus_mode.png"), posX - (306 / 1.765f), posY - (74 * 2.31f), 347, 343, 35, 0.1f);
        }
        GlStateManager.disableBlend();

        super.drawScreen((int) (Mouse.getX() / 2f), (int) ((mc.displayHeight - Mouse.getY()) / 2f), partialTicks);
        GLUtility.INSTANCE.rescaleMC();
    }

    boolean typeD;
    boolean typeE;
    boolean typeU;
    boolean typeS;

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {

        if (typeD && !typeE && keyCode != Keyboard.KEY_E && keyCode != Keyboard.KEY_D) {
            reset();
        }
        if (typeE && !typeU && keyCode != Keyboard.KEY_U && keyCode != Keyboard.KEY_E) {
            reset();
        }
        if (typeU && !typeS && keyCode != Keyboard.KEY_S && keyCode != Keyboard.KEY_U) {
            reset();
        }

        if (keyCode == Keyboard.KEY_D) {
            typeD = true;
        }
        if (typeD && keyCode == Keyboard.KEY_E) {
            typeE = true;
        }
        if (typeE && keyCode == Keyboard.KEY_U) {
            typeU = true;
        }
        if (typeU && keyCode == Keyboard.KEY_S) {
            typeS = true;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked((int) (Mouse.getX() / 2f), (int) ((mc.displayHeight - Mouse.getY()) / 2f), mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased((int) (Mouse.getX() / 2f), (int) ((mc.displayHeight - Mouse.getY()) / 2f), state);
    }

    @Override
    public void updateScreen() {
        for (GuiButton guiButton : buttonList) {
            if (guiButton instanceof Button button1) {

                    button1.getAnimation().update(guiButton.isMouseOver());

            }
        }
        if (typeD && typeE && typeU && typeS) {
            DrainWalk.getInstance().setRoflMode(!DrainWalk.getInstance().isRoflMode());
            System.out.println("Deus-mode " + DrainWalk.getInstance().isRoflMode());
            reset();
        }
        super.updateScreen();
    }

    public void reset() {
        System.out.println("reset");
        typeD = false;
        typeE = false;
        typeU = false;
        typeS = false;
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiWorldSelection(this));
        }
        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }
        if (button.id == 2) {
            this.mc.displayGuiScreen(new GuiAltManager());
        }
        if (button.id == 3) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }
        if (button.id == 4) {
            this.mc.shutdown();
        }
    }

}
