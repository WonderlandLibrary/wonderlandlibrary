package org.alphacentauri.management.managers;

import java.util.ArrayList;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventRender2D;
import org.alphacentauri.management.notifications.Notification;
import org.lwjgl.opengl.GL11;

public class NotificationManager implements EventListener {
   public ArrayList notifications = new ArrayList();

   public void onEvent(Event event) {
      if(event instanceof EventRender2D) {
         ArrayList<Notification> toRemove = new ArrayList();

         for(int i = 0; i < this.notifications.size(); ++i) {
            Notification notification = (Notification)this.notifications.get(i);
            if(notification.animation < 20.0F && !notification.getTimer().hasMSPassed((long)notification.getDuration())) {
               notification.animation = (float)notification.getAnimationTimer().getMSPassed() / 333.0F * 20.0F;
               if(notification.animation > 20.0F) {
                  notification.animation = 20.0F;
               }

               notification.getTimer().reset();
            } else if(notification.animation > 0.0F && notification.getTimer().hasMSPassed((long)notification.getDuration())) {
               notification.animation = 20.0F - (float)notification.getAnimationTimer().getMSPassed() / 333.0F * 20.0F;
            } else if(notification.animation <= 0.0F) {
               toRemove.add(notification);
            } else if(notification.animation >= 20.0F) {
               notification.getAnimationTimer().reset();
            }
         }

         for(Notification notification : toRemove) {
            this.notifications.remove(notification);
         }

         ScaledResolution resolution = ((EventRender2D)event).getResolution();
         int width = resolution.getScaledWidth();
         int height = resolution.getScaledHeight();
         int i = 0;
         int nheight = 25;
         double nwidthmult = (double)width * 0.25D / 20.0D;
         FontRenderer fontRenderer = AC.getMC().fontRendererObj;
         ++i;

         for(Notification notification : this.notifications) {
            ++i;
            Gui.drawRect(width - (int)((double)notification.animation * nwidthmult) - 3, height - nheight * i - 2 * (i - 1), width - (int)((double)notification.animation * nwidthmult) + (int)(20.0D * nwidthmult) - 3, height - nheight * i + nheight - 2 * (i - 1), -1207959552);
            fontRenderer.drawStringWithShadow(EnumChatFormatting.AQUA + "§l" + notification.getTitle(), (float)(width - (int)((double)notification.animation * nwidthmult) + 2 - 3), (float)(height - nheight * i - 2 * (i - 1) + 2), 16777215);
            GL11.glPushMatrix();
            float size = (float)(nwidthmult * 19.0D / (double)fontRenderer.getStringWidth(notification.getText()));
            if(size > 1.0F) {
               size = 1.0F;
            }

            GL11.glScalef(size, size, size);
            float posmod = 1.0F / size;
            fontRenderer.drawStringWithShadow(notification.getText(), (float)(width - (int)((double)notification.animation * nwidthmult) + 2 - 3) * posmod, (float)(height - nheight * i - 2 * (i - 1) + 12) * posmod, 16777215);
            GL11.glPopMatrix();
         }
      }

   }

   public void addNotification(Notification not) {
      this.notifications.add(not);
   }
}
