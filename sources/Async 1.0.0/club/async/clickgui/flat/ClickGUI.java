package club.async.clickgui.flat;

import club.async.Async;
import club.async.clickgui.flat.components.Component;
import club.async.module.Category;
import club.async.module.Module;
import club.async.util.ColorUtil;
import club.async.util.RenderUtil;
import club.async.util.TextFieldUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ClickGUI extends GuiScreen {

    public double x, y, width = 300, height = 200, dragX, dragY;
    public int scrollOffset, scrollOffset2, scrollOffset3, modeIndex;;
    public boolean dragging;
    public Category selectedCategory;
    private MButton mButton;
    public TextFieldUtil searchField;
    public TextFieldUtil configField;
    public boolean expand, home;
    public Module selectedModule;
    public ArrayList<Component> components = new ArrayList<>();
    private String selectedConfig;

    public ClickGUI() {
        mButton = new MButton(width, height, this);
        x = getScaledResolution().getScaledWidth() / 3.4;
        y = getScaledResolution().getScaledHeight() / 4.0;
        width = 400;
        height = 282;
        mButton = new MButton(270, height, this);
        expand = false;
        selectedModule = null;
        components.clear();
        selectedCategory = Category.COMBAT;
        home = true;
        scrollOffset = 0;
        scrollOffset2 = 0;
        scrollOffset3 = 0;
    }

    @Override
    public void initGui() {
        super.initGui();
        width = 400;
        height = 282;
        if (searchField == null)
            this.searchField = new TextFieldUtil(this.eventButton, this.mc.fontRendererObj, (int)(x + width - 54),(int)y + 12, 50, 15);
        this.configField = new TextFieldUtil(this.eventButton, this.mc.fontRendererObj, (int)(x),(int) (y + height + 2), (263), 15);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawGradientRect(0,getScaledResolution().getScaledHeight() / 3F,getScaledResolution().getScaledWidth(),getScaledResolution().getScaledHeight(), new Color(ColorUtil.getMainColor().getRed(),ColorUtil.getMainColor().getGreen(),ColorUtil.getMainColor().getBlue(),0),ColorUtil.getMainColor());
        Gui.drawRect(0, 0, getScaledResolution().getScaledWidth(), getScaledResolution().getScaledHeight(), 0x30000000);
        for (Component component : components) {
            component.updateScreen(mouseX, mouseY);
        }
        // Handling dragging
        if (dragging) {
            x = mouseX - dragX;
            y = mouseY - dragY;
            searchField.xPosition = (int) (x + 400 - 54);
            searchField.yPosition = (int) y + 12;
        }
        configField.xPosition = (int) (x);
        configField.yPosition = (int) (y + height);

        // Handling scrolling
        handleScrolling(mouseX, mouseY);

        // Correcting the width and height for ConfigGUI
        if (!expand && !home && (width != 263 || height != 162)) {
            width = 263;
            height = 162;
        }

        // Rendering background and Menu rect
        Gui.drawRect(x, y,x + width, y + height, new Color(15,15,15).getRGB());
        Gui.drawRect(x,y,x + (expand ? width : 130), y + 40,new Color(20,20,20).getRGB());
        if (expand) {
            Async.INSTANCE.getFontManager().getFont("Arial 35").drawCenteredString(selectedModule.getName(), (float)(x + 200), (float) y + 10, ColorUtil.getMainColor().getRGB());
        }

        double offset = y + 50;
        // Rendering Category Buttons
        if (!expand && home)
            for (Category category : Category.values()) {
                Gui.drawRect(x, offset - 8,x + 130, offset + 20, new Color(20,20,20).getRGB());
                Gui.drawRect(x + 110, offset - 8,x + 130, offset + 20, selectedCategory == category && searchField.getText().isEmpty() ? ColorUtil.getMainColor().getRGB() : new Color(28,28,28).getRGB());
                Async.INSTANCE.getFontManager().getFont("Arial 25").drawString(category.getName(), (float)x + 10,(float) offset, new Color(140,140,140).getRGB());
                offset += 30;
            }
        // Rendering Search field input and selected category name
        if (selectedCategory != null && !expand && home) {
            String text = searchField.getText().isEmpty() ? selectedCategory.getName() : "''" + searchField.getText().toLowerCase() + "''";
            Async.INSTANCE.getFontManager().getFont("Arial 35").drawCenteredString(text, (float) x + 205, (float) y + 10, new Color(100, 100, 100).getRGB());
        }
        // Rendering Module Buttons
        if (!expand && home)
            mButton.drawScreen();

        // Rendering the Search field
        if (!expand && home)
            searchField.drawTextBox();

        // Rendering the Config field
        if (!expand && !home)
            configField.drawTextBox();

        // Rendering settings
        GL11.glPushMatrix();
        RenderUtil.prepareScissorBox(x,y + 42, x + width, y + height);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        for (Component component : components) {
            component.drawScreen(mouseX, mouseY, partialTicks);
        }
        // Rendering scroll visualizer
        if (home && !expand)
            Gui.drawRect(x + 270 + 1, y + 42 + scrollOffset, x + 270 + 3, y + 70 + scrollOffset, new Color(28,28,28).getRGB());
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();

        // Rendering home and settings icons
        if (!expand) {
            GlStateManager.pushMatrix();
            GL11.glColor4d(ColorUtil.getMainColor().getRed() / 255d, ColorUtil.getMainColor().getGreen() / 255d, ColorUtil.getMainColor().getBlue() / 255d, ColorUtil.getMainColor().getAlpha() / 255d);
            if (home)
                GL11.glDisable(GL11.GL_LIGHTING);
            else
                GL11.glEnable(GL11.GL_LIGHTING);

            mc.getTextureManager().bindTexture(new ResourceLocation("async/icons/home.png"));
            drawScaledCustomSizeModalRect((int) (x + 10), (int) (y + 5), 0, 90, 90, 90, 30, 30, 90, 90);
            if (!home)
                GL11.glDisable(GL11.GL_LIGHTING);
            else
                GL11.glEnable(GL11.GL_LIGHTING);

            mc.getTextureManager().bindTexture(new ResourceLocation("async/icons/settings.png"));
            drawScaledCustomSizeModalRect((int) (x + 80), (int) (y + 5), 0, 90, 90, 90, 30, 30, 90, 90);
            GL11.glColor4d(1, 1, 1, 1);
            GL11.glDisable(GL11.GL_LIGHTING);
            GlStateManager.popMatrix();
        }
        // Rendering Configs
        if (!home && !expand) {
            // Action buttons
            offset = y + 50;
            for (int i = 1; i <= 4; i++) {
                String text = i == 1 ? "Save" : i == 2 ? "Load" : i == 3 ? "Create" : "Remove";
                Gui.drawRect(x, offset - 8, x + 130, offset + 20, new Color(20, 20, 20).getRGB());
                Async.INSTANCE.getFontManager().getFont("Arial 25").drawString(text, (float) x + 10, (float) offset, new Color(100, 100, 100).getRGB());
                offset += 30;
            } // 120

            // Config buttons
            GL11.glPushMatrix();
            RenderUtil.prepareScissorBox(x, y, x + width, y + height);
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            File folder = new File(Minecraft.getMinecraft().mcDataDir, "Async/Configs");
            File[] listOfFiles = folder.listFiles();
            offset = y + 8;
            for (int i = 0; i < Objects.requireNonNull(listOfFiles).length; i++) {
                String name = listOfFiles[i].getName();
                name = name.replace(".cfg", "");
                if (selectedConfig == null && i == 0)
                    selectedConfig = name;
                Gui.drawRect(x + 131, offset - scrollOffset3 - 8, x + 263, offset - scrollOffset3 + 20, name.equalsIgnoreCase(selectedConfig) ? ColorUtil.getMainColor().getRGB() : new Color(20, 20, 20).getRGB());
                Async.INSTANCE.getFontManager().getFont("Arial 25").drawString(name, (float) x + 141, (float) offset - scrollOffset3, name.equalsIgnoreCase(selectedConfig) ? -1 : new Color(100, 100, 100).getRGB());
                offset += 30;
            }
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GL11.glPopMatrix();
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (!expand)
            this.searchField.textboxKeyTyped(typedChar, keyCode);
        if (!home && !expand)
            this.configField.textboxKeyTyped(typedChar, keyCode);
        if (!expand)
            if (typedChar == '\t' &&  this.searchField.isFocused()) {
                this.searchField.setFocused(!this.searchField.isFocused());
            }
        if (!home && !expand)
            if (typedChar == '\t' &&  this.configField.isFocused()) {
                this.configField.setFocused(!this.configField.isFocused());
            }
        if (!mButton.binding && keyCode == 1 || keyCode == Async.INSTANCE.getModuleManager().getModule("ClickGui").getKey())
            this.mc.displayGuiScreen(null);
        mButton.keyTyped(keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (home && !expand)
            this.searchField.mouseClicked(mouseX, mouseY, mouseButton);
        if (!home && !expand)
            this.configField.mouseClicked(mouseX, mouseY, mouseButton);
        // Handling Home button clicked
        if (isInside(mouseX, mouseY,x + 11, y + 6, 39 - 11, 29) && mouseButton == 0) {
            width = 400;
            height = 282;
            home = true;
        }

        // Handling Config button clicked
        if (isInside(mouseX, mouseY,x + 80, y + 6, 30, 19) && mouseButton == 0) {
            width = 263;
            height = 162;
            home = false;
        }

        // Handling dragging
        if (isInside(mouseX, mouseY, x, y - 5, width, 10) && mouseButton == 0) {
            dragging = true;
            dragX = mouseX - x;
            dragY = mouseY - y;
        }
        // Sets the selected Config String
        double offset = y + 8;
        if (!home && !expand) {
            File folder = new File(Minecraft.getMinecraft().mcDataDir, "Async/Configs");
            File[] listOfFiles = folder.listFiles();
            for (File listOfFile : Objects.requireNonNull(listOfFiles)) {
                String name = listOfFile.getName();
                name = name.replace(".cfg", "");
                if (isInside(mouseX, mouseY, x + 131, offset - scrollOffset3 - 8, 132, 28) && mouseY > y && mouseY < y + height && (mouseButton == 0 || mouseButton == 1)) {
                    selectedConfig = name;
                }
                offset += 30;
            }
        }

        // Handling Category Buttons clicking
        offset = y + 50;
        if (!expand && home)
            for (Category category : Category.values()) {
                if (isInside(mouseX, mouseY, x, offset - 8,130, 28) && (mouseButton == 0 || mouseButton == 1) && (selectedCategory != category || !searchField.getText().isEmpty())) {
                    searchField.setText("");
                    scrollOffset = 0;
                    selectedCategory = category;
                }
                offset += 30;
            }
        // Handling Module buttons clicking
        if (!expand && home)
            mButton.mouseClicked(mouseX, mouseY, mouseButton);

        // Handling settings clicking
        for (Component component : components) {
            component.mouseClicked(mouseX, mouseY, mouseButton);
        }
        // Exiting settings screen
        if (isInside(mouseX, mouseY, x,y,width, 40) && (mouseButton == 0 || mouseButton == 1) && expand) {
            expand = false;
            selectedModule = null;
            components.clear();
            width = 400;
        }

        // Action buttons for configs
        File folder = new File("Async/Configs");
        File[] listOfFiles = folder.listFiles();

        if (!home && !expand) {
            offset = y + 50;
            for (int i = 1; i <= 4; i++) {
                String text = i == 1 ? "Save" : i == 2 ? "Load" : i == 3 ? "Create" : "Remove";
                Gui.drawRect(x, offset - 8, x + 130, offset + 20, new Color(20, 20, 20).getRGB());

                // TODO NEW CONFIG SYSTEM

               /* if (isInside(mouseX, mouseY, x, offset - 8, 130, 28) && (mouseButton == 0 || mouseButton == 1)) {
                    switch (text)
                    {
                        case "Save":
                            ConfigConfiger.save(selectedConfig);
                            break;
                        case "Load":
                            ConfigConfiger.load(selectedConfig);
                            break;
                        case "Create":
                            if (configField.getText() != null && !configField.getText().isEmpty()) {
                                for (int it = 0; it < Objects.requireNonNull(listOfFiles).length; it++) {
                                    if (listOfFiles[it].isFile()) {
                                        String name2 = listOfFiles[it].getName().replace(".cfg", "");
                                        if (name2.equalsIgnoreCase(configField.getText())) {
                                            return;
                                        }
                                    }
                                }
                                ConfigConfiger.save(configField.getText());
                                configField.setText("");
                            }
                            break;
                        case "Remove":
                            ConfigConfiger.remove(selectedConfig);
                    }
                }*/
                offset += 30;
            }
        }

    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        dragging = false;
        for (Component component : components) {
            component.mouseReleased(mouseX, mouseY, state);
        }
        if (selectedModule != null)
        mButton.reloadSettings(selectedModule);
    }

    private void handleScrolling(int mouseX, int mouseY) {
        // Handling scrolling for home screen
        if (!expand) {
            if (home) {
                if (Mouse.hasWheel() && isInside(mouseX, mouseY, x + 130, y, 270 - 130, height)) {
                    int wheel = Mouse.getDWheel();
                    int offset = 1;
                    if (searchField.getText().isEmpty()) {
                        for (Module ignored : Async.INSTANCE.getModuleManager().getModules(selectedCategory)) {
                            offset += 28;
                        }
                    } else {
                        for (Module ignored : Async.INSTANCE.getModuleManager().getModules(searchField.getText())) {
                            offset += 28;
                        }
                    }
                    if (wheel < 0) {
                        if (scrollOffset < offset - 230)
                            this.scrollOffset += 10;
                        if (this.scrollOffset < 0) {
                            this.scrollOffset = 0;
                        }
                    } else if (wheel > 0) {
                        this.scrollOffset -= 10;
                        if (this.scrollOffset < 0) {
                            this.scrollOffset = 0;
                        }
                    }
                }
            } else {
                // Handling scrolling for Config Screen
                if (Mouse.hasWheel() && isInside(mouseX, mouseY, x + 130, y - 1, 132, height)) {
                    int wheel = Mouse.getDWheel();
                    if (wheel < 0) {
                        this.scrollOffset3 += 10;
                        if (this.scrollOffset3 < 0) {
                            this.scrollOffset3 = 0;
                        }
                    } else if (wheel > 0) {
                        this.scrollOffset3 -= 10;
                        if (this.scrollOffset3 < 0) {
                            this.scrollOffset3 = 0;
                        }
                    }
                }
            }
        }
        // Handling scrolling for settings screen
        if (expand) {
            if (Mouse.hasWheel() && isInside(mouseX,mouseY, x, y + 40,width, height - 40)) {
                int wheel = Mouse.getDWheel();
                if (wheel < 0) {
                    this.scrollOffset2 += 10;
                    if (this.scrollOffset2 < 0) {
                        this.scrollOffset2 = 0;
                    }
                } else if (wheel > 0) {
                    this.scrollOffset2 -= 10;
                    if (this.scrollOffset2 < 0) {
                        this.scrollOffset2 = 0;
                    }
                }
            }
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
      /*  Async.INSTANCE.getManager().saveKeys();
        Async.INSTANCE.getManager().saveMods();
        Async.INSTANCE.getManager().saveSetting();*/

        Async.INSTANCE.getModuleManager().getModule("Clickgui").setToggled(false);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public final ScaledResolution getScaledResolution() {
        return new ScaledResolution(Minecraft.getMinecraft());
    }

}
