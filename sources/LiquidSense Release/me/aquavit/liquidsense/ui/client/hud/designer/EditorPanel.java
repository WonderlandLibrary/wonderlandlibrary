package me.aquavit.liquidsense.ui.client.hud.designer;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.utils.mc.MinecraftInstance;
import me.aquavit.liquidsense.ui.client.hud.HUD;
import me.aquavit.liquidsense.ui.client.hud.element.Element;
import me.aquavit.liquidsense.ui.client.hud.element.ElementInfo;
import me.aquavit.liquidsense.ui.client.hud.element.Side;
import me.aquavit.liquidsense.ui.font.Fonts;
import me.aquavit.liquidsense.ui.font.GameFontRenderer;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.value.*;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

import static net.minecraftforge.fml.relauncher.Side.CLIENT;

@SideOnly(CLIENT)
public class EditorPanel extends MinecraftInstance {
    public int width;
    public int height;
    public int realHeight;
    public boolean drag;
    public int dragX;
    public int dragY;
    public boolean mouseDown;
    public int scroll;
    public boolean create;
    public Element currentElement;
    public final GuiHudDesigner hudDesigner;
    public int x;
    public int y;

    public EditorPanel(final GuiHudDesigner hudDesigner, final int x, final int y) {
        this.width = 80;
        this.height = 20;
        this.realHeight = 20;
        this.hudDesigner = hudDesigner;
        this.x = x;
        this.y = y;
    }

    public final void drawPanel(int mouseX, int mouseY, int wheel) {

        drag(mouseX, mouseY);

        if (currentElement != hudDesigner.getSelectedElement()) scroll = 0;
        currentElement = hudDesigner.getSelectedElement();

        int currMouseY = mouseY;
        boolean shouldScroll = realHeight > 200;

        if (shouldScroll) {
            GL11.glPushMatrix();
            RenderUtils.makeScissorBox(x, y + 1F, x + width, y + 200F);
            GL11.glEnable(GL11.GL_SCISSOR_TEST);

            if (y + 200 < currMouseY) currMouseY = -1;

            if (mouseX >= x && mouseX <= x + width && currMouseY >= y && currMouseY <= y + 200 && Mouse.hasWheel()) {
                if (wheel < 0 && -scroll + 205 <= realHeight) {
                    scroll -= 12;
                } else if (wheel > 0) {
                    scroll += 12;
                    if (scroll > 0) scroll = 0;
                }
            }
        }

        Gui.drawRect(x, y + 12, x + width, y + realHeight, new Color(27, 34, 40).getRGB());

        if (create) {
            drawCreate(mouseX, currMouseY);
        } else if (this.currentElement != null) {
            drawEditor(mouseX, currMouseY);
        } else {
            drawSelection(mouseX, currMouseY);
        }

        if (shouldScroll) {
            Gui.drawRect(x + width - 5, y + 15, x + width - 2, y + 197,
                    new Color(41, 41, 41).getRGB());

            float v = 197 * (-scroll / (realHeight - 170F));
            RenderUtils.drawRect(x + width - 5F, y + 15 + v, x + width - 2F, y + 20 + v,
                    new Color(37, 126, 255).getRGB());

            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GL11.glPopMatrix();
        }

        // Save mouse states
        mouseDown = Mouse.isButtonDown(0);
    }

    private void drawSelection(int mouseX, int mouseY) {
        height = 15 + scroll;
        realHeight = 15;
        width = 120;

        Fonts.font18.drawString("§lCreate element", x + 2, y + height, Color.WHITE.getRGB());
        if (Mouse.isButtonDown(0) && !mouseDown && mouseX >= x && mouseX <= x + width && mouseY >= y + height && mouseY <= y + height + 10)
            create = true;

        height += 10;
        realHeight += 10;

        Fonts.font18.drawString("§lReset", x + 2, y + height, Color.WHITE.getRGB());
        if (Mouse.isButtonDown(0) && !mouseDown && mouseX >= x && mouseX <= x + width && mouseY >= y + height && mouseY <= y + height + 10)
            LiquidSense.hud = HUD.Companion.createDefault();

        height += 15;
        realHeight += 15;

        Fonts.font18.drawString("§lAvailable Elements", x + 2, y + height, Color.WHITE.getRGB());
        height += 10;
        realHeight += 10;

        for (Element element : LiquidSense.hud.elements) {
            Fonts.font18.drawString(element.getName(), x + 2, y + height, Color.WHITE.getRGB());

            int stringWidth = Fonts.font18.getStringWidth(element.getName());
            if (width < stringWidth + 8)
                width = stringWidth + 8;

            if (Mouse.isButtonDown(0) && !mouseDown && mouseX >= x && mouseX <= x + width && mouseY >= y + height && mouseY <= y + height + 10)
                hudDesigner.setSelectedElement(element);

            height += 10;
            realHeight += 10;
        }

        Gui.drawRect(x, y, x + width, y + 12, me.aquavit.liquidsense.module.modules.client.HUD.generateColor().getRGB());
        Fonts.font18.drawString("§lEditor", x + 2F, y + 3.5f, Color.WHITE.getRGB());
    }

    private void drawCreate(int mouseX, int mouseY) {
        height = 15 + scroll;
        realHeight = 15;
        width = 90;

        for (Class<? extends Element> element : HUD.Companion.getElements()) {
            ElementInfo info = element.getAnnotation(ElementInfo.class);

            if (info == null) continue;


            if (info.single() && LiquidSense.hud.elements.stream().anyMatch(it -> it.getClass() == element)) continue;

            String name = info.name();
            Fonts.font18.drawString(name, x + 2, y + height, Color.WHITE.getRGB());

            int stringWidth = Fonts.font18.getStringWidth(name);
            if (width < stringWidth + 8) {
                width = stringWidth + 8;
            }

            if (Mouse.isButtonDown(0) && !mouseDown && mouseX >= x && mouseX <= x + width && mouseY >= y + height && mouseY <= y + height + 10) {
                try {
                    Element newElement = element.newInstance();

                    if (newElement.createElement()) {
                        LiquidSense.hud.addElement(newElement);
                    }

                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                create = false;
            }

            height += 10;
            realHeight += 10;
        }

        Gui.drawRect(x, y, x + width, y + 12, me.aquavit.liquidsense.module.modules.client.HUD.generateColor().getRGB());
        Fonts.font18.drawString("§lCreate element", x + 2F, y + 3.5F, Color.WHITE.getRGB());
    }

    private void drawEditor(int mouseX, int mouseY) {
        height = scroll + 15;
        realHeight = 15;

        int prevWidth = width;
        width = 100;

        Element element = this.currentElement;
        if (element == null) return;

        // X
        Fonts.font18.drawString("X: " + String.format("%.2f", element.getRenderX()) + " (" + String.format("%.2f", element.getX()) + ")", x + 2, y + height, Color.WHITE.getRGB());

        height += 10;
        realHeight += 10;

        // Y
        Fonts.font18.drawString("Y: " + String.format("%.2f", element.getRenderY()) + " (" + String.format("%.2f", element.getY()) + ")", x + 2, y + height, Color.WHITE.getRGB());

        height += 10;
        realHeight += 10;

        // Scale
        Fonts.font18.drawString("Scale: " + String.format("%.2f", element.getScale()), x + 2, y + height, Color.WHITE.getRGB());
        height += 10;
        realHeight += 10;

        // Horizontal
        Fonts.font18.drawString("H:", x + 2, y + height, Color.WHITE.getRGB());
        Fonts.font18.drawString(element.getSide().getHorizontal().getSideName(),
                x + 12, y + height, Color.GRAY.getRGB());

        if (Mouse.isButtonDown(0) && !mouseDown && mouseX >= x && mouseX <= x + width && mouseY >= y + height && mouseY <= y + height + 10) {
            Side.Horizontal[] values = Side.Horizontal.values();

            int currIndex = ArrayUtils.indexOf(values, element.getSide().getHorizontal());

            double x = element.getRenderX();

            element.getSide().setHorizontal(values[currIndex + 1 >= values.length ? 0 : currIndex + 1]);
            switch (element.getSide().getHorizontal()) {
                case LEFT:
                    element.setX(x);
                    break;
                case MIDDLE:
                    element.setX((double) new ScaledResolution(mc).getScaledWidth() / 2 - x);
                    break;
                case RIGHT:
                    element.setX(new ScaledResolution(mc).getScaledWidth() - x);
                    break;
            }
        }

        height += 10;
        realHeight += 10;

        // Vertical
        Fonts.font18.drawString("V:", x + 2, y + height, Color.WHITE.getRGB());
        Fonts.font18.drawString(element.getSide().getVertical().getSideName(),
                x + 12, y + height, Color.GRAY.getRGB());

        if (Mouse.isButtonDown(0) && !mouseDown && mouseX >= x && mouseX <= x + width && mouseY >= y + height && mouseY <= y + height + 10) {
            Side.Vertical[] values = Side.Vertical.values();
            int currIndex = ArrayUtils.indexOf(values, element.getSide().getVertical());

            double y = element.getRenderY();

            element.getSide().setVertical(values[currIndex + 1 >= values.length ? 0 : currIndex + 1]);

            switch (element.getSide().getVertical()) {
                case MIDDLE:
                    element.setY((double) new ScaledResolution(mc).getScaledHeight() / 2 - y);
                    break;
                case UP:
                    element.setY(y);
                    break;
                case DOWN:
                    element.setY(new ScaledResolution(mc).getScaledHeight() - y);
                    break;
            }

        }

        height += 10;
        realHeight += 10;

        // Values
        for (Value value : element.getValues().stream().filter(Value::isDisplayable).toArray(Value[]::new)) {

            if (value instanceof BoolValue) {

                int tempRGB = -1;

                if (((Boolean) value.get())) {
                    tempRGB = Color.WHITE.getRGB();
                } else {
                    tempRGB = Color.GRAY.getRGB();
                }

                // Title
                Fonts.font18.drawString(value.getName(), x + 2, y + height, tempRGB);

                int stringWidth = Fonts.font18.getStringWidth(value.getName());
                if (width < stringWidth + 8)
                    width = stringWidth + 8;

                // Toggle value
                if (Mouse.isButtonDown(0) && !mouseDown && mouseX >= x && mouseX <= x + width &&
                        mouseY >= y + height && mouseY <= y + height + 10)
                    value.set(!((Boolean) value.get()));

                // Change pos
                height += 10;
                realHeight += 10;
            } else if (value instanceof FloatValue) {
                float current = (float) value.get();
                float min = ((FloatValue) value).getMinimum();
                float max = ((FloatValue) value).getMaximum();

                // Title

                String text = value.getName() + ": §c" + String.format("%.2f", current);

                Fonts.font18.drawString(text, x + 2, y + height, Color.WHITE.getRGB());

                int stringWidth = Fonts.font18.getStringWidth(text);
                if (width < stringWidth + 8)
                    width = stringWidth + 8;

                // Slider
                RenderUtils.drawRect(x + 8F, y + height + 12F, x + prevWidth - 8F, y + height + 13F, Color.WHITE);

                // Slider mark
                float sliderValue = x + ((prevWidth - 18F) * (current - min) / (max - min));
                RenderUtils.drawRect(8F + sliderValue, y + height + 9F, sliderValue + 11F, y + height
                        + 15F, new Color(37, 126, 255).getRGB());

                // Slider changer
                if (mouseX >= x + 8 && mouseX <= x + prevWidth && mouseY >= y + height + 9 && mouseY <= y + height + 15 &&
                        Mouse.isButtonDown(0)) {
                    float curr = MathHelper.clamp_float((mouseX - x - 8F) / (prevWidth - 18F), 0F, 1F);

                    value.set(min + (max - min) * curr);
                }

                // Change pos
                height += 20;
                realHeight += 20;
            } else if (value instanceof IntegerValue) {
                int current = (int) value.get();
                int min = ((IntegerValue) value).getMinimum();
                int max = ((IntegerValue) value).getMaximum();

                // Title
                String text = value.getName() + ": §c" + current;

                Fonts.font18.drawString(text, x + 2, y + height, Color.WHITE.getRGB());

                int stringWidth = Fonts.font18.getStringWidth(text);
                if (width < stringWidth + 8)
                    width = stringWidth + 8;

                // Slider
                RenderUtils.drawRect(x + 8F, y + height + 12F, x + prevWidth - 8F, y + height + 13F, Color.WHITE);

                // Slider mark
                float sliderValue = x + ((prevWidth - 18F) * (current - min) / (max - min));
                RenderUtils.drawRect(8F + sliderValue, y + height + 9F, sliderValue + 11F, y + height
                        + 15F, new Color(37, 126, 255).getRGB());

                // Slider changer
                if (mouseX >= x + 8 && mouseX <= x + prevWidth && mouseY >= y + height + 9 && mouseY <= y + height + 15 &&
                        Mouse.isButtonDown(0)) {
                    float curr = MathHelper.clamp_float((mouseX - x - 8F) / (prevWidth - 18F), 0F, 1F);

                    value.set((int) (min + (max - min) * curr));
                }

                // Change pos
                height += 20;
                realHeight += 20;
            } else if (value instanceof ListValue) {
                // Title
                Fonts.font18.drawString(value.getName(), x + 2, y + height, Color.WHITE.getRGB());

                height += 10;
                realHeight += 10;

                // Selectable values
                for (String s : ((ListValue) value).getValues()) {
                    // Value title
                    String text = "§c> §r" + s;

                    int tempRGB = -1;
                    if (s == value.get()) {
                        tempRGB = Color.WHITE.getRGB();
                    } else {
                        tempRGB = Color.GRAY.getRGB();
                    }

                    Fonts.font18.drawString(text, x + 2, y + height, tempRGB);

                    int stringWidth = Fonts.font18.getStringWidth(text);
                    if (width < stringWidth + 8)
                        width = stringWidth + 8;

                    // Select value
                    if (Mouse.isButtonDown(0) && !mouseDown && mouseX >= x && mouseX <= x + width
                            && mouseY >= y + height && mouseY <= y + height + 10)
                        value.set(s);

                    // Change pos
                    height += 10;
                    realHeight += 10;
                }
            } else if (value instanceof FontValue) {
                FontRenderer fontRenderer = (FontRenderer) value.get();

                // Title

                String text;
                if (fontRenderer instanceof GameFontRenderer) {
                    text = "Font: " + ((GameFontRenderer) fontRenderer).getDefaultFont().getFont().getName()
                            + " - " + ((GameFontRenderer) fontRenderer).getDefaultFont().getFont().getSize();
                } else if (fontRenderer == Fonts.minecraftFont) {
                    text = "Font: Minecraft";
                } else {
                    text = "Font: Unknown";
                }

                Fonts.font18.drawString(text, x + 2, y + height, Color.WHITE.getRGB());

                int stringWidth = Fonts.font18.getStringWidth(text);
                if (width < stringWidth + 8)
                    width = stringWidth + 8;

                if (Mouse.isButtonDown(0) && !mouseDown && mouseX >= x && mouseX <= x + width &&
                        mouseY >= y + height && mouseY <= y + height + 10) {
                    List<FontRenderer> fonts = Fonts.getFonts();

                    for (int index = 0; index < fonts.size(); index++) {
                        FontRenderer font = fonts.get(index);
                        if (font == fontRenderer) {
                            int temp = -1;
                            if (index + 1 >= fonts.size()) {
                                temp = 0;
                            } else {
                                temp = index + 1;
                            }

                            value.set(fonts.get(temp));
                        }
                    }
                }

                height += 10;
                realHeight += 10;
            }
        }

        // Header
        Gui.drawRect(x, y, x + width, y + 12, me.aquavit.liquidsense.module.modules.client.HUD.generateColor().getRGB());
        Fonts.font18.drawString("§l" + element.getName(), x + 2F, y + 3.5F, Color.WHITE.getRGB());

        // Delete button
        if (!element.getInfo().force()) {
            float deleteWidth = x + width - Fonts.font18.getStringWidth("§lDelete") - 2F;
            Fonts.font18.drawString("§lDelete", deleteWidth, y + 3.5F, Color.WHITE.getRGB());
            if (Mouse.isButtonDown(0) && !mouseDown && mouseX >= deleteWidth && mouseX <= x + width && mouseY >= y
                    && mouseY <= y + 10)
                LiquidSense.hud.removeElement(element);
        }
    }

    private void drag(int mouseX, int mouseY) {
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + 12 && Mouse.isButtonDown(0) && !mouseDown) {
            drag = true;
            dragX = mouseX - x;
            dragY = mouseY - y;
        }

        if (Mouse.isButtonDown(0) && drag) {
            x = mouseX - dragX;
            y = mouseY - dragY;
        } else {
            drag = false;
        }
    }
}
