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
    @Shadow(remap=false)
    protected abstract void setGL();

    @Shadow(remap=false)
    protected abstract void clearGL();

    @Inject(method={"run()V"}, at={@At(value="HEAD")}, remap=false, cancellable=true)
    private void run(CallbackInfo callbackInfo) {
        int tex;
        callbackInfo.cancel();
        this.setGL();
        GL11.glClearColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glEnable((int)3553);
        try {
            tex = RenderUtils.loadGlTexture(ImageIO.read(this.getClass().getResourceAsStream("/assets/minecraft/liquidbounce/splash.png")));
        }
        catch (IOException e) {
            tex = 0;
        }
        GL11.glDisable((int)3553);
        AnimatedValue animatedValue = new AnimatedValue();
        animatedValue.setType(EaseUtils.EnumEasingType.CIRC);
        animatedValue.setDuration(600L);
        while (!SplashProgress.done) {
            GL11.glClear((int)16384);
            int width = Display.getWidth();
            int height = Display.getHeight();
            GL11.glViewport((int)0, (int)0, (int)width, (int)height);
            GL11.glMatrixMode((int)5889);
            GL11.glLoadIdentity();
            GL11.glOrtho((double)0.0, (double)width, (double)height, (double)0.0, (double)-1.0, (double)1.0);
            GL11.glMatrixMode((int)5888);
            GL11.glLoadIdentity();
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GL11.glEnable((int)3553);
            GL11.glBindTexture((int)3553, (int)tex);
            GL11.glBegin((int)7);
            GL11.glTexCoord2f((float)0.0f, (float)0.0f);
            GL11.glVertex2f((float)0.0f, (float)0.0f);
            GL11.glTexCoord2f((float)1.0f, (float)0.0f);
            GL11.glVertex2f((float)width, (float)0.0f);
            GL11.glTexCoord2f((float)1.0f, (float)1.0f);
            GL11.glVertex2f((float)width, (float)height);
            GL11.glTexCoord2f((float)0.0f, (float)1.0f);
            GL11.glVertex2f((float)0.0f, (float)height);
            GL11.glEnd();
            GL11.glDisable((int)3553);
            float rectX = (float)width * 0.2f;
            float rectX2 = (float)width * 0.8f;
            float rectY = (float)height * 0.75f;
            float rectY2 = (float)height * 0.755f;
            float rectRadius = 0.0f;
            float progress = (float)animatedValue.sync(MixinSplashProgressRunnable.getProgress());
            if (progress != 1.0f) {
                GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.3f);
                RenderUtils.drawRoundedCornerRect(rectX, rectY, rectX2, rectY2, rectRadius);
            }
            if (progress != 0.0f) {
                GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                RenderUtils.drawRoundedCornerRect(rectX, rectY, rectX + (float)width * 0.6f * progress, rectY2, rectRadius);
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
        GL11.glDeleteTextures((int)tex);
        this.clearGL();
    }

    private static float getProgress() {
        float progress = 0.0f;
        Iterator it = ProgressManager.barIterator();
        if (it.hasNext()) {
            ProgressManager.ProgressBar bar = (ProgressManager.ProgressBar)it.next();
            progress = (float)bar.getStep() / (float)bar.getSteps();
        }
        return progress;
    }
}

