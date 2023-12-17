// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.toasts;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import java.util.Arrays;
import javax.annotation.Nullable;
import java.util.Iterator;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.gui.ScaledResolution;
import com.google.common.collect.Queues;
import java.util.Deque;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class GuiToast extends Gui
{
    private final Minecraft mc;
    private final ToastInstance<?>[] visible;
    private final Deque<IToast> toastsQueue;
    
    public GuiToast(final Minecraft mcIn) {
        this.visible = (ToastInstance<?>[])new ToastInstance[5];
        this.toastsQueue = (Deque<IToast>)Queues.newArrayDeque();
        this.mc = mcIn;
    }
    
    public void drawToast(final ScaledResolution resolution) {
        if (!this.mc.gameSettings.hideGUI) {
            RenderHelper.disableStandardItemLighting();
            for (int i = 0; i < this.visible.length; ++i) {
                final ToastInstance<?> toastinstance = this.visible[i];
                if (toastinstance != null && toastinstance.render(resolution.getScaledWidth(), i)) {
                    this.visible[i] = null;
                }
                if (this.visible[i] == null && !this.toastsQueue.isEmpty()) {
                    this.visible[i] = new ToastInstance<Object>((IToast)this.toastsQueue.removeFirst());
                }
            }
        }
    }
    
    @Nullable
    public <T extends IToast> T getToast(final Class<? extends T> p_192990_1_, final Object p_192990_2_) {
        for (final ToastInstance<?> toastinstance : this.visible) {
            if (toastinstance != null && p_192990_1_.isAssignableFrom(toastinstance.getToast().getClass()) && ((IToast)toastinstance.getToast()).getType().equals(p_192990_2_)) {
                return (T)toastinstance.getToast();
            }
        }
        for (final IToast itoast : this.toastsQueue) {
            if (p_192990_1_.isAssignableFrom(itoast.getClass()) && itoast.getType().equals(p_192990_2_)) {
                return (T)itoast;
            }
        }
        return null;
    }
    
    public void clear() {
        Arrays.fill(this.visible, null);
        this.toastsQueue.clear();
    }
    
    public void add(final IToast toastIn) {
        this.toastsQueue.add(toastIn);
    }
    
    public Minecraft getMinecraft() {
        return this.mc;
    }
    
    class ToastInstance<T extends IToast>
    {
        private final T toast;
        private long animationTime;
        private long visibleTime;
        private IToast.Visibility visibility;
        
        private ToastInstance(final T toastIn) {
            this.animationTime = -1L;
            this.visibleTime = -1L;
            this.visibility = IToast.Visibility.SHOW;
            this.toast = toastIn;
        }
        
        public T getToast() {
            return this.toast;
        }
        
        private float getVisibility(final long p_193686_1_) {
            float f = MathHelper.clamp((p_193686_1_ - this.animationTime) / 600.0f, 0.0f, 1.0f);
            f *= f;
            return (this.visibility == IToast.Visibility.HIDE) ? (1.0f - f) : f;
        }
        
        public boolean render(final int p_193684_1_, final int p_193684_2_) {
            final long i = Minecraft.getSystemTime();
            if (this.animationTime == -1L) {
                this.animationTime = i;
                this.visibility.playSound(GuiToast.this.mc.getSoundHandler());
            }
            if (this.visibility == IToast.Visibility.SHOW && i - this.animationTime <= 600L) {
                this.visibleTime = i;
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate(p_193684_1_ - 160.0f * this.getVisibility(i), (float)(p_193684_2_ * 32), (float)(500 + p_193684_2_));
            final IToast.Visibility itoast$visibility = this.toast.draw(GuiToast.this, i - this.visibleTime);
            GlStateManager.popMatrix();
            if (itoast$visibility != this.visibility) {
                this.animationTime = i - (int)((1.0f - this.getVisibility(i)) * 600.0f);
                (this.visibility = itoast$visibility).playSound(GuiToast.this.mc.getSoundHandler());
            }
            return this.visibility == IToast.Visibility.HIDE && i - this.animationTime > 600L;
        }
    }
}
