/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.util.MathHelper
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package net.dev.important.gui.client.hud.designer;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.Client;
import net.dev.important.gui.client.hud.HUD;
import net.dev.important.gui.client.hud.designer.GuiHudDesigner;
import net.dev.important.gui.client.hud.element.Element;
import net.dev.important.gui.client.hud.element.ElementInfo;
import net.dev.important.gui.client.hud.element.Side;
import net.dev.important.gui.font.Fonts;
import net.dev.important.gui.font.GameFontRenderer;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.FontValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.dev.important.value.Value;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0010\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0007J\u0018\u0010\u0010\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u00052\u0006\u0010$\u001a\u00020\u0005H\u0002J\u0018\u0010%\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u00052\u0006\u0010$\u001a\u00020\u0005H\u0002J\u0018\u0010&\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u00052\u0006\u0010$\u001a\u00020\u0005H\u0002J\u001e\u0010'\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u00052\u0006\u0010$\u001a\u00020\u00052\u0006\u0010(\u001a\u00020\u0005J\u0018\u0010)\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u00052\u0006\u0010$\u001a\u00020\u0005H\u0002R\u001a\u0010\b\u001a\u00020\tX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0014\u001a\u00020\u00052\u0006\u0010\u0013\u001a\u00020\u0005@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0018\u001a\u00020\u00052\u0006\u0010\u0013\u001a\u00020\u0005@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0016R\u000e\u0010\u001a\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u001b\u001a\u00020\u00052\u0006\u0010\u0013\u001a\u00020\u0005@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0016R\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u0016\"\u0004\b\u001e\u0010\u001fR\u001a\u0010\u0006\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b \u0010\u0016\"\u0004\b!\u0010\u001f\u00a8\u0006*"}, d2={"Lnet/dev/important/gui/client/hud/designer/EditorPanel;", "Lnet/dev/important/utils/MinecraftInstance;", "hudDesigner", "Lnet/dev/important/gui/client/hud/designer/GuiHudDesigner;", "x", "", "y", "(Lnet/dev/important/gui/client/hud/designer/GuiHudDesigner;II)V", "create", "", "getCreate", "()Z", "setCreate", "(Z)V", "currentElement", "Lnet/dev/important/gui/client/hud/element/Element;", "drag", "dragX", "dragY", "<set-?>", "height", "getHeight", "()I", "mouseDown", "realHeight", "getRealHeight", "scroll", "width", "getWidth", "getX", "setX", "(I)V", "getY", "setY", "", "mouseX", "mouseY", "drawCreate", "drawEditor", "drawPanel", "wheel", "drawSelection", "LiquidBounce"})
public final class EditorPanel
extends MinecraftInstance {
    @NotNull
    private final GuiHudDesigner hudDesigner;
    private int x;
    private int y;
    private int width;
    private int height;
    private int realHeight;
    private boolean drag;
    private int dragX;
    private int dragY;
    private boolean mouseDown;
    private int scroll;
    private boolean create;
    @Nullable
    private Element currentElement;

    public EditorPanel(@NotNull GuiHudDesigner hudDesigner, int x, int y) {
        Intrinsics.checkNotNullParameter((Object)hudDesigner, "hudDesigner");
        this.hudDesigner = hudDesigner;
        this.x = x;
        this.y = y;
        this.width = 80;
        this.height = 20;
        this.realHeight = 20;
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
        if (!Intrinsics.areEqual(this.currentElement, this.hudDesigner.getSelectedElement())) {
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
        Gui.func_73734_a((int)this.x, (int)(this.y + 12), (int)(this.x + this.width), (int)(this.y + this.realHeight), (int)new Color(0, 0, 0, 150).getRGB());
        if (this.create) {
            this.drawCreate(mouseX, currMouseY);
        } else if (this.currentElement != null) {
            this.drawEditor(mouseX, currMouseY);
        } else {
            this.drawSelection(mouseX, currMouseY);
        }
        if (shouldScroll) {
            Gui.func_73734_a((int)(this.x + this.width - 5), (int)(this.y + 15), (int)(this.x + this.width - 2), (int)(this.y + 197), (int)new Color(41, 41, 41).getRGB());
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
        Class<? extends Element>[] classArray = HUD.Companion.getElements();
        int n = 0;
        int n2 = classArray.length;
        while (n < n2) {
            ElementInfo info;
            Class<? extends Element> element = classArray[n];
            ++n;
            if (element.getAnnotation(ElementInfo.class) == null) continue;
            if (info.single()) {
                boolean bl;
                block11: {
                    Iterable $this$any$iv = Client.INSTANCE.getHud().getElements();
                    boolean $i$f$any = false;
                    if ($this$any$iv instanceof Collection && ((Collection)$this$any$iv).isEmpty()) {
                        bl = false;
                    } else {
                        for (Object element$iv : $this$any$iv) {
                            Element it = (Element)element$iv;
                            boolean bl2 = false;
                            if (!Intrinsics.areEqual(it.getClass(), element)) continue;
                            bl = true;
                            break block11;
                        }
                        bl = false;
                    }
                }
                if (bl) continue;
            }
            String name = info.name();
            Fonts.font35.func_78276_b(name, this.x + 2, this.y + this.height, Color.WHITE.getRGB());
            int stringWidth = Fonts.font35.func_78256_a(name);
            if (this.width < stringWidth + 8) {
                this.width = stringWidth + 8;
            }
            if (Mouse.isButtonDown((int)0) && !this.mouseDown && mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y + this.height && mouseY <= this.y + this.height + 10) {
                try {
                    Element newElement = element.newInstance();
                    if (newElement.createElement()) {
                        HUD hUD = Client.INSTANCE.getHud();
                        Intrinsics.checkNotNullExpressionValue(newElement, "newElement");
                        hUD.addElement(newElement);
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
        Gui.func_73734_a((int)this.x, (int)this.y, (int)(this.x + this.width), (int)(this.y + 12), (int)new Color(0, 0, 0, 150).getRGB());
        Fonts.font35.drawString("\u00a7lCreate element", (float)this.x + 2.0f, (float)this.y + 3.5f, Color.WHITE.getRGB());
    }

    private final void drawSelection(int mouseX, int mouseY) {
        this.height = 15 + this.scroll;
        this.realHeight = 15;
        this.width = 120;
        Fonts.font35.func_78276_b("\u00a7lCreate element", this.x + 2, this.y + this.height, Color.WHITE.getRGB());
        if (Mouse.isButtonDown((int)0) && !this.mouseDown && mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y + this.height && mouseY <= this.y + this.height + 10) {
            this.create = true;
        }
        this.height += 10;
        this.realHeight += 10;
        Fonts.font35.func_78276_b("\u00a7lReset", this.x + 2, this.y + this.height, Color.WHITE.getRGB());
        if (Mouse.isButtonDown((int)0) && !this.mouseDown && mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y + this.height && mouseY <= this.y + this.height + 10) {
            Client.INSTANCE.setHud(HUD.Companion.createDefault());
        }
        this.height += 15;
        this.realHeight += 15;
        Fonts.font35.func_78276_b("\u00a7lAvailable Elements", this.x + 2, this.y + this.height, Color.WHITE.getRGB());
        this.height += 10;
        this.realHeight += 10;
        for (Element element : Client.INSTANCE.getHud().getElements()) {
            Fonts.font35.func_78276_b(element.getName(), this.x + 2, this.y + this.height, Color.WHITE.getRGB());
            int stringWidth = Fonts.font35.func_78256_a(element.getName());
            if (this.width < stringWidth + 8) {
                this.width = stringWidth + 8;
            }
            if (Mouse.isButtonDown((int)0) && !this.mouseDown && mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y + this.height && mouseY <= this.y + this.height + 10) {
                this.hudDesigner.setSelectedElement(element);
            }
            this.height += 10;
            this.realHeight += 10;
        }
        Gui.func_73734_a((int)this.x, (int)this.y, (int)(this.x + this.width), (int)(this.y + 12), (int)new Color(0, 0, 0, 150).getRGB());
        Fonts.font35.drawString("\u00a7lEditor", (float)this.x + 2.0f, (float)this.y + 3.5f, Color.WHITE.getRGB());
    }

    /*
     * WARNING - void declaration
     */
    private final void drawEditor(int mouseX, int mouseY) {
        this.height = this.scroll + 15;
        this.realHeight = 15;
        int prevWidth = this.width;
        this.width = 100;
        Element element = this.currentElement;
        if (element == null) {
            return;
        }
        Element element2 = element;
        StringBuilder stringBuilder = new StringBuilder().append("X: ");
        Object[] objectArray = new Object[]{element2.getRenderX()};
        String string = String.format("%.2f", Arrays.copyOf(objectArray, objectArray.length));
        Intrinsics.checkNotNullExpressionValue(string, "format(this, *args)");
        StringBuilder stringBuilder2 = stringBuilder.append(string).append(" (");
        Object[] objectArray2 = new Object[]{element2.getX()};
        string = String.format("%.2f", Arrays.copyOf(objectArray2, objectArray2.length));
        Intrinsics.checkNotNullExpressionValue(string, "format(this, *args)");
        Fonts.font35.func_78276_b(stringBuilder2.append(string).append(')').toString(), this.x + 2, this.y + this.height, Color.WHITE.getRGB());
        this.height += 10;
        this.realHeight += 10;
        StringBuilder stringBuilder3 = new StringBuilder().append("Y: ");
        Object[] objectArray3 = new Object[]{element2.getRenderY()};
        string = String.format("%.2f", Arrays.copyOf(objectArray3, objectArray3.length));
        Intrinsics.checkNotNullExpressionValue(string, "format(this, *args)");
        StringBuilder stringBuilder4 = stringBuilder3.append(string).append(" (");
        Object[] objectArray4 = new Object[]{element2.getY()};
        string = String.format("%.2f", Arrays.copyOf(objectArray4, objectArray4.length));
        Intrinsics.checkNotNullExpressionValue(string, "format(this, *args)");
        Fonts.font35.func_78276_b(stringBuilder4.append(string).append(')').toString(), this.x + 2, this.y + this.height, Color.WHITE.getRGB());
        this.height += 10;
        this.realHeight += 10;
        Object[] objectArray5 = new Object[]{Float.valueOf(element2.getScale())};
        string = String.format("%.2f", Arrays.copyOf(objectArray5, objectArray5.length));
        Intrinsics.checkNotNullExpressionValue(string, "format(this, *args)");
        Fonts.font35.func_78276_b(Intrinsics.stringPlus("Scale: ", string), this.x + 2, this.y + this.height, Color.WHITE.getRGB());
        this.height += 10;
        this.realHeight += 10;
        Fonts.font35.func_78276_b("H:", this.x + 2, this.y + this.height, Color.WHITE.getRGB());
        Fonts.font35.func_78276_b(element2.getSide().getHorizontal().getSideName(), this.x + 12, this.y + this.height, Color.GRAY.getRGB());
        if (Mouse.isButtonDown((int)0) && !this.mouseDown && mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y + this.height && mouseY <= this.y + this.height + 10) {
            double d;
            Side.Horizontal[] values2 = Side.Horizontal.values();
            int n = ArraysKt.indexOf(values2, element2.getSide().getHorizontal());
            double x = element2.getRenderX();
            element2.getSide().setHorizontal(values2[n + 1 >= values2.length ? 0 : n + 1]);
            switch (WhenMappings.$EnumSwitchMapping$0[element2.getSide().getHorizontal().ordinal()]) {
                case 1: {
                    d = x;
                    break;
                }
                case 2: {
                    d = (double)(new ScaledResolution(MinecraftInstance.mc).func_78326_a() / 2) - x;
                    break;
                }
                case 3: {
                    d = (double)new ScaledResolution(MinecraftInstance.mc).func_78326_a() - x;
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
        Fonts.font35.func_78276_b("V:", this.x + 2, this.y + this.height, Color.WHITE.getRGB());
        Fonts.font35.func_78276_b(element2.getSide().getVertical().getSideName(), this.x + 12, this.y + this.height, Color.GRAY.getRGB());
        if (Mouse.isButtonDown((int)0) && !this.mouseDown && mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y + this.height && mouseY <= this.y + this.height + 10) {
            double d;
            Side.Vertical[] values2 = Side.Vertical.values();
            int n = ArraysKt.indexOf(values2, element2.getSide().getVertical());
            double y = element2.getRenderY();
            element2.getSide().setVertical(values2[n + 1 >= values2.length ? 0 : n + 1]);
            switch (WhenMappings.$EnumSwitchMapping$1[element2.getSide().getVertical().ordinal()]) {
                case 1: {
                    d = y;
                    break;
                }
                case 2: {
                    d = (double)(new ScaledResolution(MinecraftInstance.mc).func_78328_b() / 2) - y;
                    break;
                }
                case 3: {
                    d = (double)new ScaledResolution(MinecraftInstance.mc).func_78328_b() - y;
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
            String text;
            if (!value.getCanDisplay().invoke().booleanValue()) continue;
            Value<?> value2 = value;
            if (value2 instanceof BoolValue) {
                Fonts.font35.func_78276_b(value.getName(), this.x + 2, this.y + this.height, (Boolean)((BoolValue)value).get() != false ? Color.WHITE.getRGB() : Color.GRAY.getRGB());
                int stringWidth = Fonts.font35.func_78256_a(value.getName());
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
                StringBuilder stringBuilder5 = new StringBuilder().append(value.getName()).append(": \u00a7c");
                Object[] objectArray6 = new Object[]{Float.valueOf(current)};
                String string2 = String.format("%.2f", Arrays.copyOf(objectArray6, objectArray6.length));
                Intrinsics.checkNotNullExpressionValue(string2, "format(this, *args)");
                text = stringBuilder5.append(string2).toString();
                Fonts.font35.func_78276_b(text, this.x + 2, this.y + this.height, Color.WHITE.getRGB());
                int stringWidth = Fonts.font35.func_78256_a(text);
                if (this.width < stringWidth + 8) {
                    this.width = stringWidth + 8;
                }
                RenderUtils.drawRect((float)this.x + 8.0f, (float)(this.y + this.height) + 12.0f, (float)(this.x + prevWidth) - 8.0f, (float)(this.y + this.height) + 13.0f, Color.WHITE);
                float sliderValue = (float)this.x + ((float)prevWidth - 18.0f) * (current - min) / (max - min);
                RenderUtils.drawRect(8.0f + sliderValue, (float)(this.y + this.height) + 9.0f, sliderValue + 11.0f, (float)(this.y + this.height) + 15.0f, new Color(37, 126, 255).getRGB());
                if (mouseX >= this.x + 8 && mouseX <= this.x + prevWidth && mouseY >= this.y + this.height + 9 && mouseY <= this.y + this.height + 15 && Mouse.isButtonDown((int)0)) {
                    float curr = MathHelper.func_76131_a((float)(((float)(mouseX - this.x) - 8.0f) / ((float)prevWidth - 18.0f)), (float)0.0f, (float)1.0f);
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
                text = value.getName() + ": \u00a7c" + current;
                Fonts.font35.func_78276_b(text, this.x + 2, this.y + this.height, Color.WHITE.getRGB());
                int stringWidth = Fonts.font35.func_78256_a(text);
                if (this.width < stringWidth + 8) {
                    this.width = stringWidth + 8;
                }
                RenderUtils.drawRect((float)this.x + 8.0f, (float)(this.y + this.height) + 12.0f, (float)(this.x + prevWidth) - 8.0f, (float)(this.y + this.height) + 13.0f, Color.WHITE);
                float sliderValue = (float)this.x + ((float)prevWidth - 18.0f) * (float)(current - min) / (float)(max - min);
                RenderUtils.drawRect(8.0f + sliderValue, (float)(this.y + this.height) + 9.0f, sliderValue + 11.0f, (float)(this.y + this.height) + 15.0f, new Color(37, 126, 255).getRGB());
                if (mouseX >= this.x + 8 && mouseX <= this.x + prevWidth && mouseY >= this.y + this.height + 9 && mouseY <= this.y + this.height + 15 && Mouse.isButtonDown((int)0)) {
                    float curr = MathHelper.func_76131_a((float)(((float)(mouseX - this.x) - 8.0f) / ((float)prevWidth - 18.0f)), (float)0.0f, (float)1.0f);
                    ((IntegerValue)value).set((int)((float)min + (float)(max - min) * curr));
                }
                this.height += 20;
                this.realHeight += 20;
                continue;
            }
            if (value2 instanceof ListValue) {
                Fonts.font35.func_78276_b(value.getName(), this.x + 2, this.y + this.height, Color.WHITE.getRGB());
                this.height += 10;
                this.realHeight += 10;
                String[] current = ((ListValue)value).getValues();
                int min = 0;
                int max = current.length;
                while (min < max) {
                    String s = current[min];
                    ++min;
                    String text2 = Intrinsics.stringPlus("\u00a7c> \u00a7r", s);
                    Fonts.font35.func_78276_b(text2, this.x + 2, this.y + this.height, Intrinsics.areEqual(s, ((ListValue)value).get()) ? Color.WHITE.getRGB() : Color.GRAY.getRGB());
                    int stringWidth = Fonts.font35.func_78256_a(text2);
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
            FontRenderer fontRenderer = (FontRenderer)((FontValue)value).get();
            FontRenderer max = fontRenderer;
            String text3 = max instanceof GameFontRenderer ? "Font: " + ((GameFontRenderer)fontRenderer).getDefaultFont().getFont().getName() + " - " + ((GameFontRenderer)fontRenderer).getDefaultFont().getFont().getSize() : (Intrinsics.areEqual(max, Fonts.minecraftFont) ? "Font: Minecraft" : "Font: Unknown");
            Fonts.font35.func_78276_b(text3, this.x + 2, this.y + this.height, Color.WHITE.getRGB());
            int stringWidth = Fonts.font35.func_78256_a(text3);
            if (this.width < stringWidth + 8) {
                this.width = stringWidth + 8;
            }
            if (Mouse.isButtonDown((int)0) && !this.mouseDown && mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y + this.height && mouseY <= this.y + this.height + 10) {
                List<FontRenderer> fonts = Fonts.getFonts();
                Intrinsics.checkNotNullExpressionValue(fonts, "fonts");
                Iterable $this$forEachIndexed$iv = fonts;
                boolean $i$f$forEachIndexed = false;
                int index$iv = 0;
                for (Object item$iv : $this$forEachIndexed$iv) {
                    void font;
                    int n = index$iv;
                    index$iv = n + 1;
                    if (n < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    FontRenderer fontRenderer2 = (FontRenderer)item$iv;
                    int index = n;
                    boolean bl = false;
                    if (!Intrinsics.areEqual(font, fontRenderer)) continue;
                    FontValue fontValue = (FontValue)value;
                    FontRenderer fontRenderer3 = fonts.get(index + 1 >= fonts.size() ? 0 : index + 1);
                    Intrinsics.checkNotNullExpressionValue(fontRenderer3, "fonts[if (index + 1 >= f\u2026s.size) 0 else index + 1]");
                    fontValue.set(fontRenderer3);
                }
            }
            this.height += 10;
            this.realHeight += 10;
        }
        Gui.func_73734_a((int)this.x, (int)this.y, (int)(this.x + this.width), (int)(this.y + 12), (int)new Color(0, 0, 0, 150).getRGB());
        Fonts.font35.drawString(Intrinsics.stringPlus("\u00a7l", element2.getName()), (float)this.x + 2.0f, (float)this.y + 3.5f, Color.WHITE.getRGB());
        if (!element2.getInfo().force()) {
            float deleteWidth = (float)(this.x + this.width - Fonts.font35.func_78256_a("\u00a7lDelete")) - 2.0f;
            Fonts.font35.drawString("\u00a7lDelete", deleteWidth, (float)this.y + 3.5f, Color.WHITE.getRGB());
            if (Mouse.isButtonDown((int)0) && !this.mouseDown && (float)mouseX >= deleteWidth && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + 10) {
                Client.INSTANCE.getHud().removeElement(element2);
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

    @Metadata(mv={1, 6, 0}, k=3, xi=48)
    public final class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;
        public static final /* synthetic */ int[] $EnumSwitchMapping$1;

        static {
            int[] nArray = new int[Side.Horizontal.values().length];
            nArray[Side.Horizontal.LEFT.ordinal()] = 1;
            nArray[Side.Horizontal.MIDDLE.ordinal()] = 2;
            nArray[Side.Horizontal.RIGHT.ordinal()] = 3;
            $EnumSwitchMapping$0 = nArray;
            nArray = new int[Side.Vertical.values().length];
            nArray[Side.Vertical.UP.ordinal()] = 1;
            nArray[Side.Vertical.MIDDLE.ordinal()] = 2;
            nArray[Side.Vertical.DOWN.ordinal()] = 3;
            $EnumSwitchMapping$1 = nArray;
        }
    }
}

