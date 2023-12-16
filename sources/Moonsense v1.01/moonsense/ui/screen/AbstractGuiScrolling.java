// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.screen;

import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;
import net.minecraft.client.Minecraft;
import java.util.function.Function;
import net.minecraft.client.gui.GuiSlot;

public abstract class AbstractGuiScrolling extends GuiSlot
{
    protected float target;
    protected long start;
    protected long duration;
    protected int extra;
    private static final Function<Double, Double> easingMethod;
    
    static {
        easingMethod = (v -> v);
    }
    
    public AbstractGuiScrolling(final Minecraft mcIn, final int width, final int height, final int topIn, final int bottomIn, final int slotHeightIn, final int extra) {
        super(mcIn, width, height, topIn, bottomIn, slotHeightIn);
        this.extra = extra;
        this.height += extra;
    }
    
    public void drawScroll(final int i, final int j) {
    }
    
    public void handleMouseInput() {
        if (this.isMouseYWithinSlotBounds(this.mouseY)) {
            if (Mouse.getEventButton() == 0 && Mouse.getEventButtonState() && this.mouseY >= this.top && this.mouseY <= this.bottom) {
                final int i2 = (this.width - this.getListWidth()) / 2;
                final int j2 = (this.width + this.getListWidth()) / 2;
                final int k2 = this.mouseY - this.top - this.headerPadding + (int)this.amountScrolled - 4;
                final int l2 = k2 / this.slotHeight;
                if (l2 < this.getSize() && this.mouseX >= i2 && this.mouseX <= j2 && l2 >= 0 && k2 >= 0) {
                    this.elementClicked(l2, false, this.mouseX, this.mouseY);
                    this.selectedElement = l2;
                }
                else if (this.mouseX >= i2 && this.mouseX <= j2 && k2 < 0) {
                    this.func_148132_a(this.mouseX - i2, this.mouseY - this.top + (int)this.amountScrolled - 4);
                }
            }
            if (Mouse.isButtonDown(0) && this.getEnabled()) {
                if (this.initialClickY != -1.0f) {
                    if (this.initialClickY >= 0.0f) {
                        final float scroll = this.mouseY - this.initialClickY;
                        this.amountScrolled -= (this.mouseY - this.initialClickY) * this.scrollMultiplier;
                        this.initialClickY = (float)this.mouseY;
                    }
                }
                else {
                    boolean flag1 = true;
                    if (this.mouseY >= this.top && this.mouseY <= this.bottom) {
                        final int j2 = (this.width - this.getListWidth()) / 2;
                        final int k2 = (this.width + this.getListWidth()) / 2;
                        final int l2 = this.mouseY - this.top - this.headerPadding + (int)this.amountScrolled - 4;
                        final int i3 = l2 / this.slotHeight;
                        if (i3 < this.getSize() && this.mouseX >= j2 && this.mouseX <= k2 && i3 >= 0 && l2 >= 0) {
                            final boolean flag2 = i3 == this.selectedElement && Minecraft.getSystemTime() - this.lastClicked < 250L;
                            this.elementClicked(i3, flag2, this.mouseX, this.mouseY);
                            this.selectedElement = i3;
                            this.lastClicked = Minecraft.getSystemTime();
                        }
                        else if (this.mouseX >= j2 && this.mouseX <= k2 && l2 < 0) {
                            this.func_148132_a(this.mouseX - j2, this.mouseY - this.top + (int)this.amountScrolled - 4);
                            flag1 = false;
                        }
                        final int i4 = this.getScrollBarX();
                        final int j3 = i4 + 6;
                        if (this.mouseX >= i4 && this.mouseX <= j3) {
                            this.scrollMultiplier = -1.0f;
                            int k3 = this.func_148135_f();
                            if (k3 < 1) {
                                k3 = 1;
                            }
                            int l3 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
                            l3 = MathHelper.clamp_int(l3, 32, this.bottom - this.top - 8);
                            this.scrollMultiplier /= (this.bottom - this.top - l3) / k3;
                        }
                        else {
                            this.scrollMultiplier = 1.0f;
                        }
                        if (flag1) {
                            this.initialClickY = (float)this.mouseY;
                        }
                        else {
                            this.initialClickY = -2.0f;
                        }
                    }
                    else {
                        this.initialClickY = -2.0f;
                    }
                }
            }
            else {
                this.initialClickY = -1.0f;
            }
            if (!Mouse.isButtonDown(0)) {
                int wheel = Mouse.getEventDWheel();
                if (wheel != 0) {
                    if (wheel > 0) {
                        wheel = -3;
                    }
                    else if (wheel < 0) {
                        wheel = 3;
                    }
                    this.offset(19.0f * wheel, true);
                }
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseXIn, final int mouseYIn, final float p_148128_3_) {
        final float[] target = { this.target };
        this.amountScrolled = this.handleScrollingPos(target, this.amountScrolled, (float)this.func_148135_f(), 20.0f / Minecraft.func_175610_ah(), (double)this.start, (double)this.duration);
        this.target = target[0];
        this.amountScrolled = MathHelper.clamp_float(this.amountScrolled, -300.0f, (float)(this.func_148135_f() + 300));
        this.target = MathHelper.clamp_float(this.target, -300.0f, (float)(this.func_148135_f() + 300));
        this.bindAmountScrolled();
        this.drawScroll(this.getScrollBarX(), this.getScrollBarX() + 3);
    }
    
    public void offset(final float value, final boolean animated) {
        this.scrollTo(this.target + value, animated);
    }
    
    public void scrollTo(final float value, final boolean animated) {
        this.scrollTo(value, animated, 600L);
    }
    
    public void scrollTo(final float value, final boolean animated, final long duration) {
        this.target = MathHelper.clamp_float(value, -300.0f, (float)(this.func_148135_f() + 300));
        if (animated) {
            this.start = System.currentTimeMillis();
            this.duration = duration;
        }
        else {
            this.amountScrolled = this.target;
        }
    }
    
    private float handleScrollingPos(final float[] target, final float scroll, final float maxScroll, final float delta, final double start, final double duration) {
        final float bounce = 0.44f;
        target[0] = MathHelper.clamp_float(target[0], -300.0f, maxScroll + 300.0f);
        if (target[0] < 0.0f) {
            final int n = 0;
            final int n2 = 0;
            final int n3 = 0;
            target[n3] -= target[0] * 0.56f * delta / 3.0f;
        }
        else if (target[0] > maxScroll) {
            target[0] = (target[0] - maxScroll) * (1.0f - 0.56f * delta / 3.0f) + maxScroll;
        }
        if (!Precision.almostEquals(scroll, target[0], 0.001f)) {
            return expoEase(scroll, target[0], Math.min((System.currentTimeMillis() - start) / duration * delta * 3.0, 1.0));
        }
        return target[0];
    }
    
    public static float expoEase(final float start, final float end, final double amount) {
        return start + (end - start) * AbstractGuiScrolling.easingMethod.apply(amount).floatValue();
    }
    
    private static class Precision
    {
        public static final float FLOAT_EPSILON = 0.001f;
        public static final double DOUBLE_EPSILON = 1.0E-7;
        
        public static boolean almostEquals(final float value1, final float value2, final float acceptableDifference) {
            return Math.abs(value1 - value2) <= acceptableDifference;
        }
        
        public static boolean almostEquals(final double value1, final double value2, final double acceptableDifference) {
            return Math.abs(value1 - value2) <= acceptableDifference;
        }
    }
}
