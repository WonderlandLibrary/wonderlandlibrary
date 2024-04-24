package Method.Client.clickgui;

import Method.Client.Main;
import Method.Client.clickgui.component.Component;
import Method.Client.clickgui.component.Frame;
import Method.Client.clickgui.component.components.Button;
import Method.Client.clickgui.component.components.sub.Guibutton;
import Method.Client.clickgui.component.components.sub.Keybind;
import Method.Client.command.Command;
import Method.Client.managers.CommandManager;
import Method.Client.managers.FileManager;
import Method.Client.module.Category;
import Method.Client.module.Onscreen.PinableFrame;
import Method.Client.module.misc.GuiModule;
import Method.Client.utils.Screens.SubGui;
import Method.Client.utils.visual.ColorUtils;
import Method.Client.utils.visual.RenderUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import static Method.Client.clickgui.component.Component.FontRend;
import static Method.Client.module.Onscreen.OnscreenGUI.pinableFrames;
import static org.lwjgl.input.Keyboard.KEY_TAB;

public class ClickGui extends GuiScreen {
    public ArrayList<Frame> frames = new ArrayList<>();
    private GuiTextField textbox;
    boolean loaded;
    boolean Trycommand = false;
    public SubGui subGui;
    public Guibutton runlater = null;

    public ClickGui() {
        int frameX = 5;

        for (Category category : Category.values()) {
            Frame frame = new Frame(category);
            frame.setX(frameX);
            frames.add(frame);
            frameX += frame.getWidth() + 1;
        }

        FileManager.loadData(FileManager.files[8]);

        loaded = true;
        for (PinableFrame me : pinableFrames) {
            me.setup();
        }

    }


    @Override
    public void initGui() {
        textbox = new GuiTextField(0, fontRenderer, (int) (mc.displayWidth / 5.4), 1, 240, mc.displayWidth / 100);
        textbox.setFocused(true);
        textbox.setMaxStringLength(99);
        textbox.setEnableBackgroundDrawing(false);
        Frame.updateFont();
        Button.updateFont();
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        if (subGui != null) {
            int mouseX = Mouse.getEventX() * width / mc.displayWidth;
            int mouseY = height - Mouse.getEventY() * height / mc.displayHeight - 1;
            subGui.handleMouseInput(mouseX, mouseY);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        mouseX = (int) (mouseX / RenderUtils.simpleScale(false));
        mouseY = (int) (mouseY / RenderUtils.simpleScale(true));
        GlStateManager.scale(RenderUtils.simpleScale(false), RenderUtils.simpleScale(true), 0.5f);


        textbox.drawTextBox();
        String parse = textbox.getText();
        for (Frame frame : frames) {
            frame.updatePosition(mouseX, mouseY);
            frame.renderFrame();
            if (frame.isOpen())
                for (Component comp : frame.getComponents()) {
                    comp.RenderTooltip();
                    if (!parse.isEmpty() && comp.getName().toLowerCase().contains(parse.toLowerCase()))
                        RenderUtils.drawRectOutline(frame.getX(), comp.gety(), frame.getX() + 88, comp.gety() + 12, 1, ColorUtils.rainbow().getRGB());
                }
        }

        this.drawGradientRect((int) (mc.displayWidth / 5.4), 0, (int) ((mc.displayWidth / 5.4) + 240), 14, 0x33999999, 0x33999999);

        if (!parse.isEmpty()) {
            int add = 0;
            for (Command c : CommandManager.getInstance().commands) {
                if (c.Syntax.toLowerCase().startsWith(parse.toLowerCase())) {
                    FontRend.drawStringWithShadow(c.Syntax, (float) (mc.displayWidth / 5.4), add + 10, -1);
                    add += 10;
                }
            }
        }
        if (subGui != null) {
            subGui.draw(mouseX, mouseY, partialTicks);
            subGui.updatePosition(mouseX, mouseY);
        }
        if (GuiModule.Blur.getValBoolean())
            if (OpenGlHelper.shadersSupported && mc.getRenderViewEntity() instanceof EntityPlayer) {
                if (mc.entityRenderer.getShaderGroup() != null) {
                    mc.entityRenderer.getShaderGroup().deleteShaderGroup();
                }
                mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
            }
        GlStateManager.scale(1f, 1f, 0.5f);
    }

    @Override
    public void onGuiClosed() {
        if (GuiModule.Blur.getValBoolean()) mc.entityRenderer.stopUseShader();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        mouseX = (int) (mouseX / RenderUtils.simpleScale(false));
        mouseY = (int) (mouseY / RenderUtils.simpleScale(true));
        if (subGui != null) {
            subGui.mouseClicked(mouseX, mouseY, mouseButton);
            return;
        }
        boolean nomultidrag = false;
        Frame.updateFont();
        Button.updateFont();

        for (Frame frame : frames) {
            if (frame.isOpen()) {
                if (!frame.getComponents().isEmpty()) {
                    if (frame.isWithinBounds(mouseX, mouseY))
                        for (Component component : frame.getComponents()) {
                            component.mouseClicked(mouseX, mouseY, mouseButton);
                        }
                }
            }
            if (frame.isWithinHeader(mouseX, mouseY)) {
                if (mouseButton == 0 && !nomultidrag) {
                    frame.setDrag(true);
                    frame.dragX = mouseX - frame.getX();
                    frame.dragY = mouseY - frame.getY();
                    nomultidrag = true;
                    Collections.reverse(frames);
                }
                if (mouseButton == 1) {
                    if (frame.getCategory().equals(Category.ONSCREEN))
                        mc.displayGuiScreen(Main.OnscreenGUI);
                    else if (frame.getCategory().equals(Category.HISTORY)) {
                        Main.PlayerActivity.open();
                        subGui = Main.PlayerActivity;

                    } else
                        frame.setOpen(!frame.isOpen());
                }
            }
            if (frame.isWithinFooter(mouseX, mouseY) && mouseButton == 0) {
                frame.dragScrollstop = mouseY - frame.getScrollpos();
                frame.setDragBot(true);
            }
        }
        if (mouseButton == 0 && !(mouseY < 14 && mouseX < mc.displayWidth / 5.4 + 240 && mouseX > mc.displayWidth / 5.4 - 5)) {
            textbox.setText("");
            Trycommand = false;
        }
        if (mouseY < 14 && mouseButton == 0 && mouseX < (mc.displayWidth / 5.4) + 240 && mouseX > (mc.displayWidth / 5.4) - 5) {
            Trycommand = true;
        }
        if (runlater != null) {
            runlater.dupliacteThis();
            runlater = null;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (subGui != null) {
            subGui.keyTyped(typedChar, keyCode);
            if (keyCode == 1) {
                subGui.close();
                subGui = null;
            }
            return;
        }
        if (loaded) {
            FileManager.saveData(FileManager.files[8]);
            FileManager.saveData(FileManager.files[6]);
            FileManager.saveData(FileManager.files[4]);
            FileManager.saveData(FileManager.files[0]);
        }
        if (keyCode == 1)
            this.mc.displayGuiScreen(null);
        if (!Keybind.PublicBinding)
            textbox.textboxKeyTyped(typedChar, keyCode);

        for (Frame frame : frames) {
            if (frame.isOpen() && keyCode != 1) {
                if (!frame.getComponents().isEmpty()) {
                    for (Component component : frame.getComponents()) {
                        component.keyTyped(typedChar, keyCode);
                    }
                }
            }
        }

        if (typedChar == KEY_TAB) {
            for (Command c : CommandManager.getInstance().commands) {
                String parse = textbox.getText();
                if (parse.length() > 0)
                    if (c.Syntax.toLowerCase().startsWith(parse.toLowerCase().substring(0, parse.indexOf(' '))) || parse.substring(0, parse.indexOf(' ')).toLowerCase().startsWith(c.Syntax)) {
                        textbox.setText(c.Syntax);
                        break;
                    }
            }
        }
        if (textbox.isFocused() && keyCode == 28 && Trycommand) {
            CommandManager.getInstance().runCommands(CommandManager.cmdPrefix + textbox.getText());
            mc.displayGuiScreen(null);
        }
    }

    @Override
    public void updateScreen() {
        if (subGui != null) {
            subGui.updateScreen();
            return;
        }
        textbox.updateCursorCounter();
        super.updateScreen();
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        mouseX = (int) (mouseX / RenderUtils.simpleScale(false));
        mouseY = (int) (mouseY / RenderUtils.simpleScale(true));
        if (subGui != null) {
            subGui.mouseReleased();
            return;
        }
        for (Frame frame : frames) {
            frame.setDrag(false);
            frame.setDragBot(false);
            if (frame.isOpen())
                if (!frame.getComponents().isEmpty())
                    for (Component component : frame.getComponents())
                        component.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
