/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.client.SplashProgress
 *  net.minecraftforge.fml.common.ProgressManager
 *  net.minecraftforge.fml.common.ProgressManager$ProgressBar
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.GL11
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.splash;

import java.awt.Color;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;
import net.ccbluex.liquidbounce.utils.render.AnimatedValue;
import net.ccbluex.liquidbounce.utils.render.EaseUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraftforge.fml.client.SplashProgress;
import net.minecraftforge.fml.common.ProgressManager;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets={"net.minecraftforge.fml.client.SplashProgress$2"}, remap=false)
public abstract class MixinSplashProgressRunnable {
    @Inject(method={"run()V"}, at={@At(value="HEAD")}, remap=false, cancellable=true)
    private void run(CallbackInfo callbackInfo) {
        int n;
        callbackInfo.cancel();
        this.setGL();
        GL11.glClearColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glEnable((int)3553);
        try {
            n = RenderUtils.loadGlTexture(ImageIO.read(this.getClass().getResourceAsStream("/assets/minecraft/atfield/splash.png")));
        }
        catch (IOException iOException) {
            n = 0;
        }
        GL11.glDisable((int)3553);
        AnimatedValue animatedValue = new AnimatedValue();
        animatedValue.setType(EaseUtils.EnumEasingType.CIRC);
        animatedValue.setDuration(600L);
        while (!SplashProgress.done) {
            GL11.glClear((int)16384);
            int n2 = Display.getWidth();
            int n3 = Display.getHeight();
            GL11.glViewport((int)0, (int)0, (int)n2, (int)n3);
            GL11.glMatrixMode((int)5889);
            GL11.glLoadIdentity();
            GL11.glOrtho((double)0.0, (double)n2, (double)n3, (double)0.0, (double)-1.0, (double)1.0);
            GL11.glMatrixMode((int)5888);
            GL11.glLoadIdentity();
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GL11.glEnable((int)3553);
            GL11.glBindTexture((int)3553, (int)n);
            GL11.glBegin((int)7);
            GL11.glTexCoord2f((float)0.0f, (float)0.0f);
            GL11.glVertex2f((float)0.0f, (float)0.0f);
            GL11.glTexCoord2f((float)1.0f, (float)0.0f);
            GL11.glVertex2f((float)n2, (float)0.0f);
            GL11.glTexCoord2f((float)1.0f, (float)1.0f);
            GL11.glVertex2f((float)n2, (float)n3);
            GL11.glTexCoord2f((float)0.0f, (float)1.0f);
            GL11.glVertex2f((float)0.0f, (float)n3);
            GL11.glEnd();
            GL11.glDisable((int)3553);
            float f = (float)n2 * 0.2f;
            float f2 = (float)n2 * 0.8f;
            float f3 = (float)n3 * 0.85f;
            float f4 = (float)n3 * 0.8f;
            float f5 = (float)n3 * 0.025f;
            float f6 = (float)animatedValue.sync(MixinSplashProgressRunnable.getProgress());
            if (f6 != 1.0f) {
                GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.3f);
                RenderUtils.drawRoundedCornerRect(f, f3, f2, f4, f5, new Color(255, 255, 255, 40).getRGB());
            }
            if (f6 != 0.0f) {
                GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                RenderUtils.drawRoundedCornerRect(f, f3, f + (float)n2 * 0.6f * f6, f4, f5, new Color(255, 255, 255, 170).getRGB());
            }
            SplashProgress.mutex.acquireUninterruptibly();
            Display.update();
            SplashProgress.mutex.release();
            if (SplashProgress.pause) {
                this.clearGL();
                this.setGL();
            }
            Display.sync((int)60);
        }
        GL11.glDeleteTextures((int)n);
        this.clearGL();
    }

    @Shadow(remap=false)
    protected abstract void setGL();

    private static float getProgress() {
        float f = 0.0f;
        Iterator iterator2 = ProgressManager.barIterator();
        if (iterator2.hasNext()) {
            ProgressManager.ProgressBar progressBar = (ProgressManager.ProgressBar)iterator2.next();
            f = (float)progressBar.getStep() / (float)progressBar.getSteps();
        }
        return f;
    }

    @Shadow(remap=false)
    protected abstract void clearGL();
}

