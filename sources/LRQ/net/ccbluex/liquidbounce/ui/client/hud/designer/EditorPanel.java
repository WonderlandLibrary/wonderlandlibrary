/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.NoWhenBranchMatchedException
 *  kotlin.collections.ArraysKt
 *  kotlin.collections.CollectionsKt
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.designer;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import kotlin.NoWhenBranchMatchedException;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.ui.client.hud.HUD;
import net.ccbluex.liquidbounce.ui.client.hud.designer.EditorPanel$WhenMappings;
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.Side;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.FontValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.ccbluex.liquidbounce.value.Value;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public final class EditorPanel
extends MinecraftInstance {
    private int width;
    private int height;
    private int realHeight;
    private boolean drag;
    private int dragX;
    private int dragY;
    private boolean mouseDown;
    private int scroll;
    private boolean create;
    private Element currentElement;
    private final GuiHudDesigner hudDesigner;
    private int x;
    private int y;

    public final int getWidth() {
        return this.width;
    }

    public final int getHeight() {
        return this.height;
    }

    public final int getRealHeight() {
        return this.realHeight;
    }

    public final boolean getCreate() {
        return this.create;
    }

    public final void setCreate(boolean bl) {
        this.create = bl;
    }

    public final void drawPanel(int mouseX, int mouseY, int wheel) {
        boolean shouldScroll;
        this.drag(mouseX, mouseY);
        if (this.currentElement.equals(this.hudDesigner.getSelectedElement()) ^ true) {
            this.scroll = 0;
        }
        this.currentElement = this.hudDesigner.getSelectedElement();
        int currMouseY = mouseY;
        boolean bl = shouldScroll = this.realHeight > 200;
        if (shouldScroll) {
            GL11.glPushMatrix();
            RenderUtils.makeScissorBox(this.x, (float)this.y + 1.0f, (float)this.x + (float)this.width, (float)this.y + 200.0f);
            GL11.glEnable((int)3089);
            if (this.y + 200 < currMouseY) {
                currMouseY = -1;
            }
            if (mouseX >= this.x && mouseX <= this.x + this.width && currMouseY >= this.y && currMouseY <= this.y + 200 && Mouse.hasWheel()) {
                if (wheel < 0 && -this.scroll + 205 <= this.realHeight) {
                    this.scroll -= 12;
                } else if (wheel > 0) {
                    this.scroll += 12;
                    if (this.scroll > 0) {
                        this.scroll = 0;
                    }
                }
            }
        }
        RenderUtils.drawRect(this.x, this.y + 12, this.x + this.width, this.y + this.realHeight, new Color(27, 34, 40).getRGB());
        if (this.create) {
            this.drawCreate(mouseX, currMouseY);
        } else if (this.currentElement != null) {
            this.drawEditor(mouseX, currMouseY);
        } else {
            this.drawSelection(mouseX, currMouseY);
        }
        if (shouldScroll) {
            RenderUtils.drawRect(this.x + this.width - 5, this.y + 15, this.x + this.width - 2, this.y + 197, new Color(41, 41, 41).getRGB());
            float v = (float)197 * ((float)(-this.scroll) / ((float)this.realHeight - 170.0f));
            RenderUtils.drawRect((float)(this.x + this.width) - 5.0f, (float)(this.y + 15) + v, (float)(this.x + this.width) - 2.0f, (float)(this.y + 20) + v, new Color(37, 126, 255).getRGB());
            GL11.glDisable((int)3089);
            GL11.glPopMatrix();
        }
        this.mouseDown = Mouse.isButtonDown((int)0);
    }

    private final void drawCreate(int mouseX, int mouseY) {
        this.height = 15 + this.scroll;
        this.realHeight = 15;
        this.width = 90;
        for (Class<? extends Element> element : HUD.Companion.getElements()) {
            ElementInfo info;
            if (element.getAnnotation(ElementInfo.class) == null) {
                continue;
            }
            if (info.single()) {
                boolean bl;
                block12: {
                    Iterable $this$any$iv = LiquidBounce.INSTANCE.getHud().getElements();
                    boolean $i$f$any = false;
                    if ($this$any$iv instanceof Collection && ((Collection)$this$any$iv).isEmpty()) {
                        bl = false;
                    } else {
                        for (Object element$iv : $this$any$iv) {
                            Element it = (Element)element$iv;
                            boolean bl2 = false;
                            if (!it.getClass().equals(element)) continue;
                            bl = true;
                            break block12;
                        }
                        bl = false;
                    }
                }
                if (bl) continue;
            }
            String name = info.name();
            Fonts.font35.drawString(name, (float)this.x + 2.0f, (float)this.y + (float)this.height, Color.WHITE.getRGB());
            int stringWidth = Fonts.font35.getStringWidth(name);
            if (this.width < stringWidth + 8) {
                this.width = stringWidth + 8;
            }
            if (Mouse.isButtonDown((int)0) && !this.mouseDown && mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y + this.height && mouseY <= this.y + this.height + 10) {
                try {
                    Element newElement = element.newInstance();
                    if (newElement.createElement()) {
                        LiquidBounce.INSTANCE.getHud().addElement(newElement);
                    }
                }
                catch (InstantiationException e) {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                this.create = false;
            }
            this.height += 10;
            this.realHeight += 10;
        }
        RenderUtils.drawRect(this.x, this.y, this.x + this.width, this.y + 12, ClickGUI.generateColor().getRGB());
        Fonts.font35.drawString("\u00a7lCreate element", (float)this.x + 2.0f, (float)this.y + 3.5f, Color.WHITE.getRGB());
    }

    private final void drawSelection(int mouseX, int mouseY) {
        this.height = 15 + this.scroll;
        this.realHeight = 15;
        this.width = 120;
        Fonts.font35.drawString("\u00a7lCreate element", (float)this.x + 2.0f, (float)this.y + (float)this.height, Color.WHITE.getRGB());
        if (Mouse.isButtonDown((int)0) && !this.mouseDown && mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y + this.height && mouseY <= this.y + this.height + 10) {
            this.create = true;
        }
        this.height += 10;
        this.realHeight += 10;
        Fonts.font35.drawString("\u00a7lReset", (float)this.x + (float)2, (float)this.y + (float)this.height, Color.WHITE.getRGB());
        if (Mouse.isButtonDown((int)0) && !this.mouseDown && mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y + this.height && mouseY <= this.y + this.height + 10) {
            LiquidBounce.INSTANCE.setHud(HUD.Companion.createDefault());
        }
        this.height += 15;
        this.realHeight += 15;
        Fonts.font35.drawString("\u00a7lAvailable Elements", (float)this.x + 2.0f, (float)this.y + (float)this.height, Color.WHITE.getRGB());
        this.height += 10;
        this.realHeight += 10;
        for (Element element : LiquidBounce.INSTANCE.getHud().getElements()) {
            Fonts.font35.drawString(element.getName(), this.x + 2, this.y + this.height, Color.WHITE.getRGB());
            int stringWidth = Fonts.font35.getStringWidth(element.getName());
            if (this.width < stringWidth + 8) {
                this.width = stringWidth + 8;
            }
            if (Mouse.isButtonDown((int)0) && !this.mouseDown && mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y + this.height && mouseY <= this.y + this.height + 10) {
                this.hudDesigner.setSelectedElement(element);
            }
            this.height += 10;
            this.realHeight += 10;
        }
        RenderUtils.drawRect(this.x, this.y, this.x + this.width, this.y + 12, ClickGUI.generateColor().getRGB());
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Fonts.font35.drawString("\u00a7lEditor", (float)this.x + 2.0f, (float)this.y + 3.5f, Color.WHITE.getRGB());
    }

    /*
     * WARNING - void declaration
     */
    private final void drawEditor(int mouseX, int mouseY) {
        Enum[] values;
        this.height = this.scroll + 15;
        this.realHeight = 15;
        int prevWidth = this.width;
        this.width = 100;
        Element element = this.currentElement;
        if (element == null) {
            return;
        }
        Element element2 = element;
        String string = "%.2f";
        Object[] objectArray = new Object[]{element2.getRenderX()};
        CharSequence charSequence = new StringBuilder().append("X: ");
        Object object = Fonts.font35;
        boolean bl = false;
        String string2 = String.format(string, Arrays.copyOf(objectArray, objectArray.length));
        string = "%.2f";
        objectArray = new Object[]{element2.getX()};
        charSequence = charSequence.append(string2).append(" (");
        bl = false;
        string2 = String.format(string, Arrays.copyOf(objectArray, objectArray.length));
        object.drawString(charSequence.append(string2).append(')').toString(), this.x + 2, this.y + this.height, Color.WHITE.getRGB());
        this.height += 10;
        this.realHeight += 10;
        string = "%.2f";
        objectArray = new Object[]{element2.getRenderY()};
        charSequence = new StringBuilder().append("Y: ");
        object = Fonts.font35;
        bl = false;
        string2 = String.format(string, Arrays.copyOf(objectArray, objectArray.length));
        string = "%.2f";
        objectArray = new Object[]{element2.getY()};
        charSequence = charSequence.append(string2).append(" (");
        bl = false;
        string2 = String.format(string, Arrays.copyOf(objectArray, objectArray.length));
        object.drawString(charSequence.append(string2).append(')').toString(), this.x + 2, this.y + this.height, Color.WHITE.getRGB());
        this.height += 10;
        this.realHeight += 10;
        string = "%.2f";
        objectArray = new Object[]{Float.valueOf(element2.getScale())};
        charSequence = new StringBuilder().append("Scale: ");
        object = Fonts.font35;
        bl = false;
        string2 = String.format(string, Arrays.copyOf(objectArray, objectArray.length));
        object.drawString(charSequence.append(string2).toString(), this.x + 2, this.y + this.height, Color.WHITE.getRGB());
        this.height += 10;
        this.realHeight += 10;
        Fonts.font35.drawString("H:", this.x + 2, this.y + this.height, Color.WHITE.getRGB());
        Fonts.font35.drawString(element2.getSide().getHorizontal().getSideName(), this.x + 12, this.y + this.height, Color.GRAY.getRGB());
        if (Mouse.isButtonDown((int)0) && !this.mouseDown && mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y + this.height && mouseY <= this.y + this.height + 10) {
            double d;
            values = Side.Horizontal.values();
            int currIndex = ArraysKt.indexOf((Object[])values, (Object)((Object)element2.getSide().getHorizontal()));
            double x = element2.getRenderX();
            element2.getSide().setHorizontal(values[currIndex + 1 >= values.length ? 0 : currIndex + 1]);
            switch (EditorPanel$WhenMappings.$EnumSwitchMapping$0[element2.getSide().getHorizontal().ordinal()]) {
                case 1: {
                    d = x;
                    break;
                }
                case 2: {
                    d = (double)(MinecraftInstance.classProvider.createScaledResolution(MinecraftInstance.mc).getScaledWidth() / 2) - x;
                    break;
                }
                case 3: {
                    d = (double)MinecraftInstance.classProvider.createScaledResolution(MinecraftInstance.mc).getScaledWidth() - x;
                    break;
                }
                default: {
                    throw new NoWhenBranchMatchedException();
                }
            }
            element2.setX(d);
        }
        this.height += 10;
        this.realHeight += 10;
        Fonts.font35.drawString("V:", this.x + 2, this.y + this.height, Color.WHITE.getRGB());
        Fonts.font35.drawString(element2.getSide().getVertical().getSideName(), this.x + 12, this.y + this.height, Color.GRAY.getRGB());
        if (Mouse.isButtonDown((int)0) && !this.mouseDown && mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y + this.height && mouseY <= this.y + this.height + 10) {
            double d;
            values = Side.Vertical.values();
            int currIndex = ArraysKt.indexOf((Object[])values, (Object)((Object)element2.getSide().getVertical()));
            double y = element2.getRenderY();
            element2.getSide().setVertical((Side.Vertical)values[currIndex + 1 >= values.length ? 0 : currIndex + 1]);
            switch (EditorPanel$WhenMappings.$EnumSwitchMapping$1[element2.getSide().getVertical().ordinal()]) {
                case 1: {
                    d = y;
                    break;
                }
                case 2: {
                    d = (double)(MinecraftInstance.classProvider.createScaledResolution(MinecraftInstance.mc).getScaledHeight() / 2) - y;
                    break;
                }
                case 3: {
                    d = (double)MinecraftInstance.classProvider.createScaledResolution(MinecraftInstance.mc).getScaledHeight() - y;
                    break;
                }
                default: {
                    throw new NoWhenBranchMatchedException();
                }
            }
            element2.setY(d);
        }
        this.height += 10;
        this.realHeight += 10;
        for (Value<?> value : element2.getValues()) {
            float min$iv;
            boolean $i$f$clamp_float;
            float max$iv;
            Value<?> value2 = value;
            if (value2 instanceof BoolValue) {
                Fonts.font35.drawString(value.getName(), this.x + 2, this.y + this.height, (Boolean)((BoolValue)value).get() != false ? Color.WHITE.getRGB() : Color.GRAY.getRGB());
                int stringWidth = Fonts.font35.getStringWidth(value.getName());
                if (this.width < stringWidth + 8) {
                    this.width = stringWidth + 8;
                }
                if (Mouse.isButtonDown((int)0) && !this.mouseDown && mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y + this.height && mouseY <= this.y + this.height + 10) {
                    ((BoolValue)value).set((Boolean)((BoolValue)value).get() == false);
                }
                this.height += 10;
                this.realHeight += 10;
                continue;
            }
            if (value2 instanceof FloatValue) {
                float current = ((Number)((FloatValue)value).get()).floatValue();
                float min = ((FloatValue)value).getMinimum();
                float max = ((FloatValue)value).getMaximum();
                String string3 = "%.2f";
                Object[] objectArray2 = new Object[]{Float.valueOf(current)};
                object = new StringBuilder().append(value.getName()).append(": \u00a7c");
                boolean bl2 = false;
                charSequence = String.format(string3, Arrays.copyOf(objectArray2, objectArray2.length));
                String text = ((StringBuilder)object).append((String)charSequence).toString();
                Fonts.font35.drawString(text, this.x + 2, this.y + this.height, Color.WHITE.getRGB());
                int stringWidth = Fonts.font35.getStringWidth(text);
                if (this.width < stringWidth + 8) {
                    this.width = stringWidth + 8;
                }
                RenderUtils.drawRect((float)this.x + 8.0f, (float)(this.y + this.height) + 12.0f, (float)(this.x + prevWidth) - 8.0f, (float)(this.y + this.height) + 13.0f, Color.WHITE);
                float sliderValue = (float)this.x + ((float)prevWidth - 18.0f) * (current - min) / (max - min);
                RenderUtils.drawRect(8.0f + sliderValue, (float)(this.y + this.height) + 9.0f, sliderValue + 11.0f, (float)(this.y + this.height) + 15.0f, new Color(37, 126, 255).getRGB());
                if (mouseX >= this.x + 8 && mouseX <= this.x + prevWidth && mouseY >= this.y + this.height + 9 && mouseY <= this.y + this.height + 15 && Mouse.isButtonDown((int)0)) {
                    void num$iv;
                    float f = ((float)(mouseX - this.x) - 8.0f) / ((float)prevWidth - 18.0f);
                    float f2 = 0.0f;
                    max$iv = 1.0f;
                    $i$f$clamp_float = false;
                    float curr = num$iv < min$iv ? min$iv : (num$iv > max$iv ? max$iv : num$iv);
                    ((FloatValue)value).set(Float.valueOf(min + (max - min) * curr));
                }
                this.height += 20;
                this.realHeight += 20;
                continue;
            }
            if (value2 instanceof IntegerValue) {
                int current = ((Number)((IntegerValue)value).get()).intValue();
                int min = ((IntegerValue)value).getMinimum();
                int max = ((IntegerValue)value).getMaximum();
                String text = value.getName() + ": \u00a7c" + current;
                Fonts.font35.drawString(text, this.x + 2, this.y + this.height, Color.WHITE.getRGB());
                int stringWidth = Fonts.font35.getStringWidth(text);
                if (this.width < stringWidth + 8) {
                    this.width = stringWidth + 8;
                }
                RenderUtils.drawRect((float)this.x + 8.0f, (float)(this.y + this.height) + 12.0f, (float)(this.x + prevWidth) - 8.0f, (float)(this.y + this.height) + 13.0f, Color.WHITE);
                float sliderValue = (float)this.x + ((float)prevWidth - 18.0f) * (float)(current - min) / (float)(max - min);
                RenderUtils.drawRect(8.0f + sliderValue, (float)(this.y + this.height) + 9.0f, sliderValue + 11.0f, (float)(this.y + this.height) + 15.0f, new Color(37, 126, 255).getRGB());
                if (mouseX >= this.x + 8 && mouseX <= this.x + prevWidth && mouseY >= this.y + this.height + 9 && mouseY <= this.y + this.height + 15 && Mouse.isButtonDown((int)0)) {
                    float num$iv = ((float)(mouseX - this.x) - 8.0f) / ((float)prevWidth - 18.0f);
                    min$iv = 0.0f;
                    max$iv = 1.0f;
                    $i$f$clamp_float = false;
                    float curr = num$iv < min$iv ? min$iv : (num$iv > max$iv ? max$iv : num$iv);
                    ((IntegerValue)value).set((int)((float)min + (float)(max - min) * curr));
                }
                this.height += 20;
                this.realHeight += 20;
                continue;
            }
            if (value2 instanceof ListValue) {
                Fonts.font35.drawString(value.getName(), this.x + 2, this.y + this.height, Color.WHITE.getRGB());
                this.height += 10;
                this.realHeight += 10;
                for (String s : ((ListValue)value).getValues()) {
                    String text = "\u00a7c> \u00a7r" + s;
                    Fonts.font35.drawString(text, this.x + 2, this.y + this.height, s.equals((String)((ListValue)value).get()) ? Color.WHITE.getRGB() : Color.GRAY.getRGB());
                    int stringWidth = Fonts.font35.getStringWidth(text);
                    if (this.width < stringWidth + 8) {
                        this.width = stringWidth + 8;
                    }
                    if (Mouse.isButtonDown((int)0) && !this.mouseDown && mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y + this.height && mouseY <= this.y + this.height + 10) {
                        ((ListValue)value).set(s);
                    }
                    this.height += 10;
                    this.realHeight += 10;
                }
                continue;
            }
            if (!(value2 instanceof FontValue)) continue;
            IFontRenderer fontRenderer = (IFontRenderer)((FontValue)value).get();
            String text = fontRenderer.isGameFontRenderer() ? "Font: " + fontRenderer.getGameFontRenderer().getDefaultFont().getFont().getName() + " - " + fontRenderer.getGameFontRenderer().getDefaultFont().getFont().getSize() : (fontRenderer.equals(Fonts.minecraftFont) ? "Font: Minecraft" : "Font: Unknown");
            Fonts.font35.drawString(text, this.x + 2, this.y + this.height, Color.WHITE.getRGB());
            int stringWidth = Fonts.font35.getStringWidth(text);
            if (this.width < stringWidth + 8) {
                this.width = stringWidth + 8;
            }
            if (Mouse.isButtonDown((int)0) && !this.mouseDown && mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y + this.height && mouseY <= this.y + this.height + 10) {
                List<IFontRenderer> fonts = Fonts.getFonts();
                Iterable $this$forEachIndexed$iv = fonts;
                boolean $i$f$forEachIndexed = false;
                int index$iv = 0;
                for (Object item$iv : $this$forEachIndexed$iv) {
                    void font;
                    int n = index$iv++;
                    boolean bl3 = false;
                    if (n < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    int n2 = n;
                    IFontRenderer iFontRenderer = (IFontRenderer)item$iv;
                    int index = n2;
                    boolean bl4 = false;
                    if (!font.equals(fontRenderer)) continue;
                    ((FontValue)value).set(fonts.get(index + 1 >= fonts.size() ? 0 : index + 1));
                }
            }
            this.height += 10;
            this.realHeight += 10;
        }
        RenderUtils.drawRect(this.x, this.y, this.x + this.width, this.y + 12, ClickGUI.generateColor().getRGB());
        Fonts.font35.drawString("\u00a7l" + element2.getName(), (float)this.x + 2.0f, (float)this.y + 3.5f, Color.WHITE.getRGB());
        if (!element2.getInfo().force()) {
            float deleteWidth = (float)(this.x + this.width - Fonts.font35.getStringWidth("\u00a7lDelete")) - 2.0f;
            Fonts.font35.drawString("\u00a7lDelete", deleteWidth, (float)this.y + 3.5f, Color.WHITE.getRGB());
            if (Mouse.isButtonDown((int)0) && !this.mouseDown && (float)mouseX >= deleteWidth && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + 10) {
                LiquidBounce.INSTANCE.getHud().removeElement(element2);
            }
        }
    }

    private final void drag(int mouseX, int mouseY) {
        if (mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + 12 && Mouse.isButtonDown((int)0) && !this.mouseDown) {
            this.drag = true;
            this.dragX = mouseX - this.x;
            this.dragY = mouseY - this.y;
        }
        if (Mouse.isButtonDown((int)0) && this.drag) {
            this.x = mouseX - this.dragX;
            this.y = mouseY - this.dragY;
        } else {
            this.drag = false;
        }
    }

    public final int getX() {
        return this.x;
    }

    public final void setX(int n) {
        this.x = n;
    }

    public final int getY() {
        return this.y;
    }

    public final void setY(int n) {
        this.y = n;
    }

    public EditorPanel(GuiHudDesigner hudDesigner, int x, int y) {
        this.hudDesigner = hudDesigner;
        this.x = x;
        this.y = y;
        this.width = 80;
        this.height = 20;
        this.realHeight = 20;
    }
}

