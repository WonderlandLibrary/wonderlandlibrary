// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.HUD;

import ru.tuskevich.util.color.ColorUtility;
import ru.tuskevich.util.render.RenderUtility;
import java.awt.Color;
import ru.tuskevich.util.font.Fonts;
import ru.tuskevich.util.animation.Animation;
import java.util.ArrayList;
import java.util.Iterator;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import ru.tuskevich.event.EventTarget;
import ru.tuskevich.event.events.impl.EventTick;
import java.util.List;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "Notifications", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.HUD)
public class Notifications extends Module
{
    private static List<Notify> notifies;
    
    public static void notify(final String title, final String text, final Notify.NotifyType type, final int second) {
        Notifications.notifies.add(new Notify(title, text, type).setMaxTime(second * 50));
    }
    
    @EventTarget
    public void tick(final EventTick eventTick) {
        Notifications.notifies.forEach(notify -> notify.updateAnimation());
        Notifications.notifies.removeIf(Notify::updateAnimation);
    }
    
    public static void render(final ScaledResolution res) {
        float yOffset = -24.0f;
        if (Notifications.mc.currentScreen instanceof GuiChat) {
            final int i = Notifications.mc.gameSettings.guiScale;
            if (i == 0) {
                yOffset -= 26.0f;
            }
            if (i == 2) {
                yOffset -= 14.0f;
            }
        }
        for (final Notify notify : Notifications.notifies) {
            GL11.glPushMatrix();
            yOffset -= notify.draw(res, yOffset);
            GL11.glPopMatrix();
        }
    }
    
    static {
        Notifications.notifies = new ArrayList<Notify>();
    }
    
    public static class Notify
    {
        private Animation animation;
        private String title;
        private String text;
        private float width;
        private int ticks;
        private int maxTime;
        private final NotifyType type;
        
        public Notify(final String title, final String text, final NotifyType type) {
            this.animation = new Animation();
            this.maxTime = 50;
            this.title = title;
            this.text = text;
            this.type = type;
            this.width = (float)Math.max(Fonts.Nunito16.getStringWidth(title), Fonts.Nunito14.getStringWidth(text));
        }
        
        public float draw(final ScaledResolution res, final float yOffset) {
            final int w = res.getScaledWidth();
            final int h = res.getScaledHeight();
            final double anim = this.animation.get();
            GL11.glTranslated(w - (this.width + 35.0f) * anim, h + yOffset * anim, 0.0);
            if (this.type == NotifyType.ERROR) {
                RenderUtility.drawRound(3.0f, -10.0f, this.width + 24.0f, 25.0f, 2.0f, new Color(20, 20, 20, 200));
                Fonts.icon21.drawStringWithShadow("p", 8.0f, -4.5f, new Color(200, 1, 1, (int)(255.0 * anim)).getRGB());
            }
            else if (this.type == NotifyType.INFO) {
                RenderUtility.drawRound(3.0f, -10.0f, this.width + 24.0f, 25.0f, 2.0f, new Color(20, 20, 20, 200));
                Fonts.icon21.drawStringWithShadow("m", 6.0f, -3.0f, new Color(255, 255, 255, (int)(255.0 * anim)).getRGB());
            }
            else if (this.type == NotifyType.SUCCESS) {
                RenderUtility.drawRound(3.0f, -10.0f, this.width + 24.0f, 25.0f, 2.0f, new Color(20, 20, 20, 200));
                Fonts.icon21.drawStringWithShadow("o", 6.0f, -3.0f, new Color(32, 200, 1, (int)(255.0 * anim)).getRGB());
            }
            Fonts.Nunito16.drawStringWithShadow(this.title, 24.0f, -7.0f, ColorUtility.rgba(200.0, 200.0, 200.0, 255.0 * anim));
            Fonts.Nunito14.drawStringWithShadow(this.text, 24.0f, 4.0f, ColorUtility.rgba(170.0, 170.0, 170.0, 255.0 * anim));
            return 35.0f * (float)this.animation.get();
        }
        
        public boolean updateAnimation() {
            this.animation.update(++this.ticks < this.maxTime);
            return this.animation.get() == 0.0;
        }
        
        public Notify setMaxTime(final int maxTime) {
            this.maxTime = maxTime;
            return this;
        }
        
        public enum NotifyType
        {
            SUCCESS, 
            INFO, 
            ERROR;
        }
    }
}
