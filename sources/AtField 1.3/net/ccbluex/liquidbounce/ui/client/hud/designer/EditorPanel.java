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
    private int dragY;
    private int scroll;
    private boolean create;
    private int dragX;
    private int y;
    private int height;
    private final GuiHudDesigner hudDesigner;
    private boolean mouseDown;
    private boolean drag;
    private int width;
    private Element currentElement;
    private int realHeight;
    private int x;

    public final void drawPanel(int n, int n2, int n3) {
        boolean bl;
        this.drag(n, n2);
        if (this.currentElement.equals(this.hudDesigner.getSelectedElement()) ^ true) {
            this.scroll = 0;
        }
        this.currentElement = this.hudDesigner.getSelectedElement();
        int n4 = n2;
        boolean bl2 = bl = this.realHeight > 200;
        if (bl) {
            GL11.glPushMatrix();
            RenderUtils.makeScissorBox(this.x, (float)this.y + 1.0f, (float)this.x + (float)this.width, (float)this.y + 200.0f);
            GL11.glEnable((int)3089);
            if (this.y + 200 < n4) {
                n4 = -1;
            }
            if (n >= this.x && n <= this.x + this.width && n4 >= this.y && n4 <= this.y + 200 && Mouse.hasWheel()) {
                if (n3 < 0 && -this.scroll + 205 <= this.realHeight) {
                    this.scroll -= 12;
                } else if (n3 > 0) {
                    this.scroll += 12;
                    if (this.scroll > 0) {
                        this.scroll = 0;
                    }
                }
            }
        }
        RenderUtils.drawRect(this.x, this.y + 12, this.x + this.width, this.y + this.realHeight, new Color(27, 34, 40).getRGB());
        if (this.create) {
            this.drawCreate(n, n4);
        } else if (this.currentElement != null) {
            this.drawEditor(n, n4);
        } else {
            this.drawSelection(n, n4);
        }
        if (bl) {
            RenderUtils.drawRect(this.x + this.width - 5, this.y + 15, this.x + this.width - 2, this.y + 197, new Color(41, 41, 41).getRGB());
            float f = (float)197 * ((float)(-this.scroll) / ((float)this.realHeight - 170.0f));
            RenderUtils.drawRect((float)(this.x + this.width) - 5.0f, (float)(this.y + 15) + f, (float)(this.x + this.width) - 2.0f, (float)(this.y + 20) + f, new Color(37, 126, 255).getRGB());
            GL11.glDisable((int)3089);
            GL11.glPopMatrix();
        }
        this.mouseDown = Mouse.isButtonDown((int)0);
    }

    public final int getY() {
        return this.y;
    }

    private final void drawSelection(int n, int n2) {
        this.height = 15 + this.scroll;
        this.realHeight = 15;
        this.width = 120;
        Fonts.roboto35.drawString("\u00a7lCreate element", (float)this.x + 2.0f, (float)this.y + (float)this.height, Color.WHITE.getRGB());
        if (Mouse.isButtonDown((int)0) && !this.mouseDown && n >= this.x && n <= this.x + this.width && n2 >= this.y + this.height && n2 <= this.y + this.height + 10) {
            this.create = true;
        }
        this.height += 10;
        this.realHeight += 10;
        Fonts.roboto35.drawString("\u00a7lReset", (float)this.x + (float)2, (float)this.y + (float)this.height, Color.WHITE.getRGB());
        if (Mouse.isButtonDown((int)0) && !this.mouseDown && n >= this.x && n <= this.x + this.width && n2 >= this.y + this.height && n2 <= this.y + this.height + 10) {
            LiquidBounce.INSTANCE.setHud(HUD.Companion.createDefault());
        }
        this.height += 15;
        this.realHeight += 15;
        Fonts.roboto35.drawString("\u00a7lAvailable Elements", (float)this.x + 2.0f, (float)this.y + (float)this.height, Color.WHITE.getRGB());
        this.height += 10;
        this.realHeight += 10;
        for (Element element : LiquidBounce.INSTANCE.getHud().getElements()) {
            Fonts.roboto35.drawString(element.getName(), this.x + 2, this.y + this.height, Color.WHITE.getRGB());
            int n3 = Fonts.roboto35.getStringWidth(element.getName());
            if (this.width < n3 + 8) {
                this.width = n3 + 8;
            }
            if (Mouse.isButtonDown((int)0) && !this.mouseDown && n >= this.x && n <= this.x + this.width && n2 >= this.y + this.height && n2 <= this.y + this.height + 10) {
                this.hudDesigner.setSelectedElement(element);
            }
            this.height += 10;
            this.realHeight += 10;
        }
        RenderUtils.drawRect(this.x, this.y, this.x + this.width, this.y + 12, ClickGUI.generateColor().getRGB());
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Fonts.roboto35.drawString("\u00a7lEditor", (float)this.x + 2.0f, (float)this.y + 3.5f, Color.WHITE.getRGB());
    }

    public final int getHeight() {
        return this.height;
    }

    private final void drawEditor(int n, int n2) {
        this.height = this.scroll + 15;
        this.realHeight = 15;
        int n3 = this.width;
        this.width = 100;
        Element element = this.currentElement;
        if (element == null) {
            return;
        }
        Element element2 = element;
        Object object3 = "%.2f";
        Object[] objectArray = new Object[]{element2.getRenderX()};
        CharSequence charSequence = new StringBuilder().append("X: ");
        Object object2 = Fonts.roboto35;
        boolean bl = false;
        String string = String.format((String)object3, Arrays.copyOf(objectArray, objectArray.length));
        object3 = "%.2f";
        objectArray = new Object[]{element2.getX()};
        charSequence = charSequence.append(string).append(" (");
        bl = false;
        string = String.format((String)object3, Arrays.copyOf(objectArray, objectArray.length));
        object2.drawString(charSequence.append(string).append(')').toString(), this.x + 2, this.y + this.height, Color.WHITE.getRGB());
        this.height += 10;
        this.realHeight += 10;
        object3 = "%.2f";
        objectArray = new Object[]{element2.getRenderY()};
        charSequence = new StringBuilder().append("Y: ");
        object2 = Fonts.roboto35;
        bl = false;
        string = String.format((String)object3, Arrays.copyOf(objectArray, objectArray.length));
        object3 = "%.2f";
        objectArray = new Object[]{element2.getY()};
        charSequence = charSequence.append(string).append(" (");
        bl = false;
        string = String.format((String)object3, Arrays.copyOf(objectArray, objectArray.length));
        object2.drawString(charSequence.append(string).append(')').toString(), this.x + 2, this.y + this.height, Color.WHITE.getRGB());
        this.height += 10;
        this.realHeight += 10;
        object3 = "%.2f";
        objectArray = new Object[]{Float.valueOf(element2.getScale())};
        charSequence = new StringBuilder().append("Scale: ");
        object2 = Fonts.roboto35;
        bl = false;
        string = String.format((String)object3, Arrays.copyOf(objectArray, objectArray.length));
        object2.drawString(charSequence.append(string).toString(), this.x + 2, this.y + this.height, Color.WHITE.getRGB());
        this.height += 10;
        this.realHeight += 10;
        Fonts.roboto35.drawString("H:", this.x + 2, this.y + this.height, Color.WHITE.getRGB());
        Fonts.roboto35.drawString(element2.getSide().getHorizontal().getSideName(), this.x + 12, this.y + this.height, Color.GRAY.getRGB());
        if (Mouse.isButtonDown((int)0) && !this.mouseDown && n >= this.x && n <= this.x + this.width && n2 >= this.y + this.height && n2 <= this.y + this.height + 10) {
            double d;
            object3 = Side.Horizontal.values();
            int n4 = ArraysKt.indexOf((Object[])object3, (Object)((Object)element2.getSide().getHorizontal()));
            double d2 = element2.getRenderX();
            element2.getSide().setHorizontal(object3[n4 + 1 >= ((Side.Horizontal[])object3).length ? 0 : n4 + 1]);
            switch (EditorPanel$WhenMappings.$EnumSwitchMapping$0[element2.getSide().getHorizontal().ordinal()]) {
                case 1: {
                    d = d2;
                    break;
                }
                case 2: {
                    d = (double)(MinecraftInstance.classProvider.createScaledResolution(MinecraftInstance.mc).getScaledWidth() / 2) - d2;
                    break;
                }
                case 3: {
                    d = (double)MinecraftInstance.classProvider.createScaledResolution(MinecraftInstance.mc).getScaledWidth() - d2;
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
        Fonts.roboto35.drawString("V:", this.x + 2, this.y + this.height, Color.WHITE.getRGB());
        Fonts.roboto35.drawString(element2.getSide().getVertical().getSideName(), this.x + 12, this.y + this.height, Color.GRAY.getRGB());
        if (Mouse.isButtonDown((int)0) && !this.mouseDown && n >= this.x && n <= this.x + this.width && n2 >= this.y + this.height && n2 <= this.y + this.height + 10) {
            double d;
            object3 = Side.Vertical.values();
            int n5 = ArraysKt.indexOf((Object[])object3, (Object)((Object)element2.getSide().getVertical()));
            double d3 = element2.getRenderY();
            element2.getSide().setVertical((Side.Vertical)((Object)object3[n5 + 1 >= ((Object)object3).length ? 0 : n5 + 1]));
            switch (EditorPanel$WhenMappings.$EnumSwitchMapping$1[element2.getSide().getVertical().ordinal()]) {
                case 1: {
                    d = d3;
                    break;
                }
                case 2: {
                    d = (double)(MinecraftInstance.classProvider.createScaledResolution(MinecraftInstance.mc).getScaledHeight() / 2) - d3;
                    break;
                }
                case 3: {
                    d = (double)MinecraftInstance.classProvider.createScaledResolution(MinecraftInstance.mc).getScaledHeight() - d3;
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
        for (Object object3 : element2.getValues()) {
            boolean bl2;
            float f;
            float f2;
            int n6;
            Object object4 = object3;
            if (object4 instanceof BoolValue) {
                Fonts.roboto35.drawString(((Value)object3).getName(), this.x + 2, this.y + this.height, (Boolean)((BoolValue)object3).get() != false ? Color.WHITE.getRGB() : Color.GRAY.getRGB());
                int n7 = Fonts.roboto35.getStringWidth(((Value)object3).getName());
                if (this.width < n7 + 8) {
                    this.width = n7 + 8;
                }
                if (Mouse.isButtonDown((int)0) && !this.mouseDown && n >= this.x && n <= this.x + this.width && n2 >= this.y + this.height && n2 <= this.y + this.height + 10) {
                    ((BoolValue)object3).set((Boolean)((BoolValue)object3).get() == false);
                }
                this.height += 10;
                this.realHeight += 10;
                continue;
            }
            if (object4 instanceof FloatValue) {
                float f3 = ((Number)((FloatValue)object3).get()).floatValue();
                float f4 = ((FloatValue)object3).getMinimum();
                float f5 = ((FloatValue)object3).getMaximum();
                String string2 = "%.2f";
                Object[] objectArray2 = new Object[]{Float.valueOf(f3)};
                object2 = new StringBuilder().append(((Value)object3).getName()).append(": \u00a7c");
                n6 = 0;
                charSequence = String.format(string2, Arrays.copyOf(objectArray2, objectArray2.length));
                String string3 = ((StringBuilder)object2).append((String)charSequence).toString();
                Fonts.roboto35.drawString(string3, this.x + 2, this.y + this.height, Color.WHITE.getRGB());
                int n8 = Fonts.roboto35.getStringWidth(string3);
                if (this.width < n8 + 8) {
                    this.width = n8 + 8;
                }
                RenderUtils.drawRect((float)this.x + 8.0f, (float)(this.y + this.height) + 12.0f, (float)(this.x + n3) - 8.0f, (float)(this.y + this.height) + 13.0f, Color.WHITE);
                float f6 = (float)this.x + ((float)n3 - 18.0f) * (f3 - f4) / (f5 - f4);
                RenderUtils.drawRect(8.0f + f6, (float)(this.y + this.height) + 9.0f, f6 + 11.0f, (float)(this.y + this.height) + 15.0f, new Color(37, 126, 255).getRGB());
                if (n >= this.x + 8 && n <= this.x + n3 && n2 >= this.y + this.height + 9 && n2 <= this.y + this.height + 15 && Mouse.isButtonDown((int)0)) {
                    float f7 = ((float)(n - this.x) - 8.0f) / ((float)n3 - 18.0f);
                    f2 = 0.0f;
                    f = 1.0f;
                    bl2 = false;
                    float f8 = f7 < f2 ? f2 : (f7 > f ? f : f7);
                    ((FloatValue)object3).set((Object)Float.valueOf(f4 + (f5 - f4) * f8));
                }
                this.height += 20;
                this.realHeight += 20;
                continue;
            }
            if (object4 instanceof IntegerValue) {
                int n9 = ((Number)((IntegerValue)object3).get()).intValue();
                int n10 = ((IntegerValue)object3).getMinimum();
                int n11 = ((IntegerValue)object3).getMaximum();
                String string4 = ((Value)object3).getName() + ": \u00a7c" + n9;
                Fonts.roboto35.drawString(string4, this.x + 2, this.y + this.height, Color.WHITE.getRGB());
                int n12 = Fonts.roboto35.getStringWidth(string4);
                if (this.width < n12 + 8) {
                    this.width = n12 + 8;
                }
                RenderUtils.drawRect((float)this.x + 8.0f, (float)(this.y + this.height) + 12.0f, (float)(this.x + n3) - 8.0f, (float)(this.y + this.height) + 13.0f, Color.WHITE);
                float f9 = (float)this.x + ((float)n3 - 18.0f) * (float)(n9 - n10) / (float)(n11 - n10);
                RenderUtils.drawRect(8.0f + f9, (float)(this.y + this.height) + 9.0f, f9 + 11.0f, (float)(this.y + this.height) + 15.0f, new Color(37, 126, 255).getRGB());
                if (n >= this.x + 8 && n <= this.x + n3 && n2 >= this.y + this.height + 9 && n2 <= this.y + this.height + 15 && Mouse.isButtonDown((int)0)) {
                    float f10 = ((float)(n - this.x) - 8.0f) / ((float)n3 - 18.0f);
                    f2 = 0.0f;
                    f = 1.0f;
                    bl2 = false;
                    float f11 = f10 < f2 ? f2 : (f10 > f ? f : f10);
                    ((IntegerValue)object3).set((Object)((int)((float)n10 + (float)(n11 - n10) * f11)));
                }
                this.height += 20;
                this.realHeight += 20;
                continue;
            }
            if (object4 instanceof ListValue) {
                Fonts.roboto35.drawString(((Value)object3).getName(), this.x + 2, this.y + this.height, Color.WHITE.getRGB());
                this.height += 10;
                this.realHeight += 10;
                for (String string5 : ((ListValue)object3).getValues()) {
                    String string6 = "\u00a7c> \u00a7r" + string5;
                    Fonts.roboto35.drawString(string6, this.x + 2, this.y + this.height, string5.equals((String)((ListValue)object3).get()) ? Color.WHITE.getRGB() : Color.GRAY.getRGB());
                    int n13 = Fonts.roboto35.getStringWidth(string6);
                    if (this.width < n13 + 8) {
                        this.width = n13 + 8;
                    }
                    if (Mouse.isButtonDown((int)0) && !this.mouseDown && n >= this.x && n <= this.x + this.width && n2 >= this.y + this.height && n2 <= this.y + this.height + 10) {
                        ((ListValue)object3).set(string5);
                    }
                    this.height += 10;
                    this.realHeight += 10;
                }
                continue;
            }
            if (!(object4 instanceof FontValue)) continue;
            IFontRenderer iFontRenderer = (IFontRenderer)((FontValue)object3).get();
            String string7 = iFontRenderer.isGameFontRenderer() ? "Font: " + iFontRenderer.getGameFontRenderer().getDefaultFont().getFont().getName() + " - " + iFontRenderer.getGameFontRenderer().getDefaultFont().getFont().getSize() : (iFontRenderer.equals(Fonts.minecraftFont) ? "Font: Minecraft" : "Font: Unknown");
            Fonts.roboto35.drawString(string7, this.x + 2, this.y + this.height, Color.WHITE.getRGB());
            int n14 = Fonts.roboto35.getStringWidth(string7);
            if (this.width < n14 + 8) {
                this.width = n14 + 8;
            }
            if (Mouse.isButtonDown((int)0) && !this.mouseDown && n >= this.x && n <= this.x + this.width && n2 >= this.y + this.height && n2 <= this.y + this.height + 10) {
                List list = Fonts.getFonts();
                Iterable iterable = list;
                boolean bl3 = false;
                n6 = 0;
                for (Object t : iterable) {
                    int n15 = n6++;
                    bl2 = false;
                    if (n15 < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    int n16 = n15;
                    IFontRenderer iFontRenderer2 = (IFontRenderer)t;
                    int n17 = n16;
                    boolean bl4 = false;
                    if (!iFontRenderer2.equals(iFontRenderer)) continue;
                    ((FontValue)object3).set(list.get(n17 + 1 >= list.size() ? 0 : n17 + 1));
                }
            }
            this.height += 10;
            this.realHeight += 10;
        }
        RenderUtils.drawRect(this.x, this.y, this.x + this.width, this.y + 12, ClickGUI.generateColor().getRGB());
        Fonts.roboto35.drawString("\u00a7l" + element2.getName(), (float)this.x + 2.0f, (float)this.y + 3.5f, Color.WHITE.getRGB());
        if (!element2.getInfo().force()) {
            float f = (float)(this.x + this.width - Fonts.roboto35.getStringWidth("\u00a7lDelete")) - 2.0f;
            Fonts.roboto35.drawString("\u00a7lDelete", f, (float)this.y + 3.5f, Color.WHITE.getRGB());
            if (Mouse.isButtonDown((int)0) && !this.mouseDown && (float)n >= f && n <= this.x + this.width && n2 >= this.y && n2 <= this.y + 10) {
                LiquidBounce.INSTANCE.getHud().removeElement(element2);
            }
        }
    }

    public final void setY(int n) {
        this.y = n;
    }

    public final int getWidth() {
        return this.width;
    }

    public EditorPanel(GuiHudDesigner guiHudDesigner, int n, int n2) {
        this.hudDesigner = guiHudDesigner;
        this.x = n;
        this.y = n2;
        this.width = 80;
        this.height = 20;
        this.realHeight = 20;
    }

    public final void setX(int n) {
        this.x = n;
    }

    private final void drag(int n, int n2) {
        if (n >= this.x && n <= this.x + this.width && n2 >= this.y && n2 <= this.y + 12 && Mouse.isButtonDown((int)0) && !this.mouseDown) {
            this.drag = true;
            this.dragX = n - this.x;
            this.dragY = n2 - this.y;
        }
        if (Mouse.isButtonDown((int)0) && this.drag) {
            this.x = n - this.dragX;
            this.y = n2 - this.dragY;
        } else {
            this.drag = false;
        }
    }

    public final int getX() {
        return this.x;
    }

    private final void drawCreate(int n, int n2) {
        this.height = 15 + this.scroll;
        this.realHeight = 15;
        this.width = 90;
        for (Class clazz : HUD.Companion.getElements()) {
            Object object;
            int n3;
            Object object2;
            ElementInfo elementInfo;
            if (clazz.getAnnotation(ElementInfo.class) == null) {
                continue;
            }
            if (elementInfo.single()) {
                boolean bl;
                block12: {
                    object2 = LiquidBounce.INSTANCE.getHud().getElements();
                    n3 = 0;
                    if (object2 instanceof Collection && ((Collection)object2).isEmpty()) {
                        bl = false;
                    } else {
                        object = object2.iterator();
                        while (object.hasNext()) {
                            Object t = object.next();
                            Element element = (Element)t;
                            boolean bl2 = false;
                            if (!element.getClass().equals(clazz)) continue;
                            bl = true;
                            break block12;
                        }
                        bl = false;
                    }
                }
                if (bl) continue;
            }
            object2 = elementInfo.name();
            Fonts.roboto35.drawString((String)object2, (float)this.x + 2.0f, (float)this.y + (float)this.height, Color.WHITE.getRGB());
            n3 = Fonts.roboto35.getStringWidth((String)object2);
            if (this.width < n3 + 8) {
                this.width = n3 + 8;
            }
            if (Mouse.isButtonDown((int)0) && !this.mouseDown && n >= this.x && n <= this.x + this.width && n2 >= this.y + this.height && n2 <= this.y + this.height + 10) {
                try {
                    object = (Element)clazz.newInstance();
                    if (((Element)object).createElement()) {
                        LiquidBounce.INSTANCE.getHud().addElement((Element)object);
                    }
                }
                catch (InstantiationException instantiationException) {
                    instantiationException.printStackTrace();
                }
                catch (IllegalAccessException illegalAccessException) {
                    illegalAccessException.printStackTrace();
                }
                this.create = false;
            }
            this.height += 10;
            this.realHeight += 10;
        }
        RenderUtils.drawRect(this.x, this.y, this.x + this.width, this.y + 12, ClickGUI.generateColor().getRGB());
        Fonts.roboto35.drawString("\u00a7lCreate element", (float)this.x + 2.0f, (float)this.y + 3.5f, Color.WHITE.getRGB());
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
}

