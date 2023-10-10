package today.flux.gui.clickgui.classic;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.src.Config;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import today.flux.Flux;
import today.flux.gui.AbstractGuiScreen;
import today.flux.gui.clickgui.classic.component.Checkbox;
import today.flux.gui.clickgui.classic.component.Component;
import today.flux.gui.clickgui.classic.component.Label;
import today.flux.gui.clickgui.classic.component.*;
import today.flux.gui.clickgui.classic.component.color.ColorPicker;
import today.flux.gui.clickgui.classic.window.Window;
import today.flux.gui.fontRenderer.FontManager;
import today.flux.irc.IRCClient;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.ModuleManager;
import today.flux.module.implement.Render.CGUI;
import today.flux.module.implement.Render.Hud;
import today.flux.module.value.*;

import java.awt.*;
import java.io.IOException;
import java.nio.IntBuffer;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ClickGUI extends AbstractGuiScreen {
    public List<Window> windows = new ArrayList<>();
    private int mouseX = 0;
    private int mouseY = 0;

    public static int defaultWidth = 90;
    public static int settingsWidth = 90;

    public static int defaultHeight = 12;
    public static int buttonHeight = 13;
    public static int scrollbarWidth = 2;
    public static int scrollbarHeight = 100;
    public static int maxWindowHeight = 330;

    public static int backgroundColor = new Color(26, 26, 26, 220).getRGB();
    public static int mainColor = 0xff9B59B6;
    public static int scrollbarColor = new Color(150, 150, 150, 180).getRGB();

    public static int lightBackgroundColor = new Color(255, 255, 255, 175).getRGB();
    public static int lightMainColor = new Color(29, 160, 255).getRGB();

    public NumberFormat nf = new DecimalFormat("0000");

    public ClickGUI() {
        this.allowUserInput = true;
        this.setup();
    }

    @Override
    public void drawScr(int mouseX, int mouseY, float partialTicks) {
        if (!Hud.NoShader.getValueState() && !Config.isFastRender()) {
            BlurUtil.blurAll(Hud.blurIntensity.getValue());
        }

        this.scale = 2.0f;

        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.update();

        this.render();

        int col1 = Hud.isLightMode ? 0xffe9ebed : 0xff292b2f;
        int col2 = Hud.isLightMode ? 0xff000000 : 0xffffffff;
        int col3 = Hud.isLightMode ? 0xff636b6d : 0xffb0b2ba;
        int col4 = Hud.isLightMode ? 0xffb0b2ba : 0xff636b6d;
        float len1 = FontManager.sans18.getStringWidth(IRCClient.loggedPacket.getRealUsername());
        float len2 = FontManager.sans14.getStringWidth("#" + nf.format(IRCClient.loggedPacket.getUid()));
        float textLen = Math.max(len1, len2);
        GuiRenderUtils.drawRoundedRect(5, curHeight - 30, 34 + textLen, 25, 1.5f, col1, .5f, col1);
        RenderUtil.drawImage(Flux.avatarLocation.getResourceLocation(), 7, curHeight - 28, 22, 21, 1f);
        RenderUtil.drawOutlinedRect(7, curHeight - 7, 29, curHeight - 28, .5f, col4);
        FontManager.sans18.drawString(IRCClient.loggedPacket.getRealUsername(), 33, curHeight - 28f, col2);
        FontManager.sans14.drawString("#" + nf.format(IRCClient.loggedPacket.getUid()), 33, curHeight - 17f, col3);

        final float xx = mouseX - 0.5f;
        final float yy = mouseY - 0.5f;

        FontManager.icon18.drawString("p", xx + 0.6f - 1, yy + 0.3f, 0xFF000000);
        FontManager.icon18.drawString("p", xx - 1, yy, CGUI.cursorColours.getColor().getRGB());
    }

    @Override
    public void handleInput() throws IOException {
        if (!Mouse.isCreated())
            return;

        while (Mouse.next())
            this.handleMouseInput();
    }

    public void update() {
        this.windows.forEach(w -> w.update(this.mouseX, this.mouseY));
        this.windows.forEach(w -> w.handleMouseUpdates(this.mouseX, this.mouseY, Mouse.isButtonDown(0) || Mouse.isButtonDown(1)));
    }

    public int rainbow(final int delay) {
        double rainbow = Math.ceil((System.currentTimeMillis() + delay) / 10.0);
        return Color.getHSBColor((float) (rainbow % 360.0 / 360.0), 0.35f, 1.0f).getRGB();
    }

    @Override
    public void onGuiClosed() {
        this.windows.forEach(w -> w.feedTimer.reset());
        
        try {
            Mouse.setNativeCursor(null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initGui() {
    	
        try {
            int min = org.lwjgl.input.Cursor.getMinCursorSize();
            IntBuffer tmp = BufferUtils.createIntBuffer(min * min);
            Cursor emptyCursor = new Cursor(min, min, min / 2, min / 2, 1, tmp, null);
            Mouse.setNativeCursor(emptyCursor);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void handleMouseInput() throws IOException {
        if (Mouse.getEventDWheel() != 0) {
            this.windows.forEach(w -> w.handleWheelUpdates(this.mouseX, this.mouseY, Mouse.isButtonDown(0)));
        } else {
            this.windows.forEach(Window::noWheelUpdates);
        }

        super.handleMouseInput();
    }

    private ColorPicker currentColourPicker;
    public void render() {
        this.windows.stream().filter(w -> w.isEnabled).forEach(w -> w.render(this.mouseX, this.mouseY));
        if (currentColourPicker != null) {
            if (currentColourPicker.isExpanded()) {
                currentColourPicker.renderColourPicker();
            }
        }
    }
    public void setCurrentColourPicker(ColorPicker colourPicker) {
        this.currentColourPicker = colourPicker;
    }
    public ColorPicker getCurrentColourPicker() {
        return currentColourPicker;
    }

    public void mouseClick(int mouseX, int mouseY, int mouseButton) {
        super.mouseClick(mouseX, mouseY, mouseButton);
        if (getCurrentColourPicker() != null && !getCurrentColourPicker().isHoveredPicker(mouseX,mouseY)) {
            this.setCurrentColourPicker(null);
        }
        if (getCurrentColourPicker() != null) {
            getCurrentColourPicker().mouseHeldColour(mouseX, mouseY);
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (getCurrentColourPicker() != null) {
            getCurrentColourPicker().mouseHeldColour(mouseX, mouseY);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (getCurrentColourPicker() != null) {
            getCurrentColourPicker().mouseReleased();
        }
    }

    public void rePositionWindows() {
        //potision
        final int maxHeight = (this.windows.stream().sorted(Comparator.comparing(Window::getHeight).reversed()).toArray(Window[]::new)[0]).height;

        int y = 10;
        int x = 10;

        for (Window window : this.windows) {
            window.x = x;
            window.y = y;

            x += window.width + 5;
        }

        //reposition
        this.windows.forEach(Window::repositionComponents);
    }

    public void setup() {
        this.windows.clear();

        for (Category category : Category.values()) {
        	if (category == Category.Global) continue;
            final Window moduleWindow = new Window(category.name(), 0, 0);

            //add spacer
            moduleWindow.children.add(new Spacer(moduleWindow, 0, 0, 3));

            //lastbox fixer
            Box lastBox = null;

            for (Module module : ModuleManager.getModList()) {
                if (module.getCategory() != category)
                    continue;

                moduleWindow.children.add(new ModuleButton(moduleWindow, 0, 0, module.getName(), "", module));

                //return if it doesnt have value and modes
                if (ValueManager.getValueByModName(module.getName()).isEmpty())
                    continue;

                //add box
                Box box = new Box(moduleWindow, 0, 0, module.getName() + "_box");
                box.group = module.getName() + "_setting";
                lastBox = box;

                //required
                moduleWindow.children.add(box);
                moduleWindow.repositionComponents();

                //add modes
                for(Value v : ValueManager.getValueByModName(module.getName())) {
                    if(v instanceof ModeValue) {

                        ModeLabel l = new ModeLabel(moduleWindow, 0, 0, ((ModeValue) v).getKey());
                        box.addChild(l);

                        ComboBox comboBox = new ComboBox((ModeValue) v, moduleWindow, 0, 0);
                        comboBox.group = module.getName() + "_setting";

                        box.addChild(comboBox);
                    }
                }

                //add values
                for (Value value : ValueManager.getValueByModName(module.getName())) {

                    if (value instanceof FloatValue) {
                        FloatValue floatValue = (FloatValue) value;

                        Slider slider = new Slider(floatValue, moduleWindow, 0, 0, floatValue.getKey());
                        slider.group = module.getName() + "_setting";
                        box.addChild(slider);
                    } else if (value instanceof BooleanValue) {
                        BooleanValue booleanValue = (BooleanValue) value;

                        Checkbox checkbox = new Checkbox(booleanValue, moduleWindow, 0, 0);
                        checkbox.group = module.getName() + "_setting";
                        box.addChild(checkbox);
                    } else if (value instanceof ColorValue) {
                        ColorValue colorProperty = (ColorValue) value;
                        ColorPicker colorPicker = new ColorPicker(colorProperty, moduleWindow, 0,0, colorProperty.getKey());
                        box.addChild(colorPicker);
                    }
                }

                box.recalcHeight();
            }

            //lastbox fixer
            if (lastBox != null) {
                lastBox.setLastBox(true);
                lastBox.recalcHeight();
            }

            moduleWindow.isEnabled = true;
            moduleWindow.repositionComponents();

            this.windows.add(moduleWindow);
        }

        Window presetWindow = new Window("Preset", 5, 5);

        presetWindow.children.add(new PresetList(presetWindow, 0, 0));

        presetWindow.isEnabled = true;
        presetWindow.repositionComponents();

        this.windows.add(presetWindow);

        Window commonSettings = new Window("Common", 5, 5);

        for (Value value : ValueManager.getValueByModName("Global")) {
            if (value instanceof FloatValue) {
                commonSettings.children.add(new Slider((FloatValue) value, commonSettings, -5, 0, value.getKey()));
            }
            if (value instanceof BooleanValue) {
                commonSettings.children.add(new Checkbox((BooleanValue) value, commonSettings, -5, 0, -3));
            }
            if (value instanceof ModeValue) {
                commonSettings.children.add(new Label(commonSettings, 0, 0, value.getKey()));
                commonSettings.children.add(new ComboBox((ModeValue) value, commonSettings, -5, 0, -3));
            }
        }

        commonSettings.isEnabled = true;
        commonSettings.repositionComponents();

        this.windows.add(commonSettings);

        this.rePositionWindows();
    }

    public void toggleBox(String group, boolean value) {
        for (Window window : this.windows) {
            for (today.flux.gui.clickgui.classic.component.Component child : window.children) {
                if (child.group.equals(group) && child instanceof Box) {
                    ((Box) child).setVirtual_visible(value);
                }
            }

            window.repositionComponents();
        }
    }

    public boolean isVisivleComponetsByGroup(String group) {
        for (Window window : this.windows) {
            for (Component child : window.children) {
                if (child.group.equals(group) && child instanceof Box && ((Box) child).isVirtual_visible()) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public void drawGradientRect(float left, float top, float right, float bottom, int startColor, int endColor) {
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(right, top, 0).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos(left, top, 0).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos(left, bottom, 0).color(f5, f6, f7, f4).endVertex();
        worldrenderer.pos(right, bottom, 0).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

}
