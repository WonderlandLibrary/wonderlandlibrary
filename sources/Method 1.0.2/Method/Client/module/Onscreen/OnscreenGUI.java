package Method.Client.module.Onscreen;

import Method.Client.clickgui.component.Component;
import Method.Client.clickgui.component.Frame;
import Method.Client.module.Category;
import Method.Client.utils.visual.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

import java.util.ArrayList;

public class OnscreenGUI extends GuiScreen {
    public static ArrayList<PinableFrame> pinableFrames = new ArrayList<>();

    private final Frame Onscreen;

    public OnscreenGUI() {
        Onscreen = new Frame(Category.ONSCREEN);
        Onscreen.setOpen(true);
    }

    protected Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        mouseX = (int) (mouseX / RenderUtils.simpleScale(false));
        mouseY = (int) (mouseY /  RenderUtils.simpleScale(true));
        GlStateManager.scale(RenderUtils.simpleScale(false), RenderUtils.simpleScale(false), 0.5f);
        if (Onscreen.isWithinBounds(mouseX, mouseY))
            Onscreen.handleScrollinput();

        Onscreen.updatePosition(mouseX, mouseY);
        Onscreen.renderFrame();

        if (Onscreen.isOpen())
            for (Component comp : Onscreen.getComponents()) {
                comp.RenderTooltip();
                comp.updateComponent(mouseX, mouseY);
            }
        for (PinableFrame pinableFrame : pinableFrames) {
            if (mc.currentScreen instanceof OnscreenGUI) {
                pinableFrame.renderFrame();
                pinableFrame.Ongui();
            }
            pinableFrame.renderFrameText();
            pinableFrame.updatePosition(mouseX, mouseY);
        }
        GlStateManager.scale(1f, 1f, 0.5f);
    }

    @Override
    protected void mouseClicked( int mouseX,  int mouseY, final int mouseButton) {
        mouseX = (int) (mouseX / RenderUtils.simpleScale(false));
        mouseY = (int) (mouseY / RenderUtils.simpleScale(true));
        if (Onscreen.isWithinHeader(mouseX, mouseY) && mouseButton == 0) {
            Onscreen.setDrag(true);
            Onscreen.dragX = mouseX - Onscreen.getX();
            Onscreen.dragY = mouseY - Onscreen.getY();
        }
        if (Onscreen.isWithinHeader(mouseX, mouseY) && mouseButton == 1) {
            Onscreen.setOpen(!Onscreen.isOpen());
        }
        if (Onscreen.isOpen()) {
            if (!Onscreen.getComponents().isEmpty()) {
                if (Onscreen.isWithinBounds(mouseX, mouseY))
                    for (Component component : Onscreen.getComponents()) {
                        component.mouseClicked(mouseX, mouseY, mouseButton);
                    }
            }
        }

        if (Onscreen.isWithinFooter(mouseX, mouseY) && mouseButton == 0) {
            Onscreen.dragScrollstop = mouseY - Onscreen.getScrollpos();
            Onscreen.setDragBot(true);
        }


        boolean multidrag = false;
        for (PinableFrame pinableFrame : pinableFrames) {
            if (pinableFrame.isWithinHeader(mouseX, mouseY) && mouseButton == 0 && !multidrag && pinableFrame.isPinned()) {
                pinableFrame.setDrag(true);
                pinableFrame.dragX = mouseX - pinableFrame.getX();
                pinableFrame.dragY = mouseY - pinableFrame.getY();
                multidrag = true;
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        mouseX = (int) (mouseX / RenderUtils.simpleScale(false));
        mouseY = (int) (mouseY / RenderUtils.simpleScale(true));
        Onscreen.setDrag(false);
        Onscreen.setDragBot(false);

        if (Onscreen.isOpen()) {
            if (!Onscreen.getComponents().isEmpty()) {
                for (Component component : Onscreen.getComponents()) {
                    component.mouseReleased(mouseX, mouseY, state);
                }
            }

        }
        for (PinableFrame pinableFrame : pinableFrames) {
            pinableFrame.setDrag(false);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (Onscreen.isOpen() && keyCode != 1) {
            if (!Onscreen.getComponents().isEmpty()) {
                for (Component component : Onscreen.getComponents()) {
                    component.keyTyped(typedChar, keyCode);
                }
            }
        }
        if (keyCode == 1) {
            this.mc.displayGuiScreen(null);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}