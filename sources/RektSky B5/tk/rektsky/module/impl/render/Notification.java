/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.render;

import net.minecraft.client.Minecraft;
import tk.rektsky.Client;
import tk.rektsky.event.Event;
import tk.rektsky.event.impl.WorldTickEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.module.ModulesManager;
import tk.rektsky.module.settings.ListSetting;
import tk.rektsky.utils.display.ColorUtil;

public class Notification
extends Module {
    public static PopupMessage currentNotification = null;
    public static PopupMessage waitingNotification = null;
    public ListSetting animationSetting = new ListSetting("Animation", new String[]{"Sine", "Cubic", "Quint", "Circ", "Elastic", "Quad", "Quart", "Expo", "Back", "Bounce"}, "Elastic");

    public Notification() {
        super("Notification", "Shows Notification instead of Chat Message", 0, Category.RENDER);
        this.toggle();
    }

    public static void displayNotification(PopupMessage message) {
        if (!ModulesManager.getModuleByClass(Notification.class).isToggled()) {
            Client.addClientChat("[" + message.getTitle() + "] " + message.getMessage());
            return;
        }
        if (currentNotification != null) {
            currentNotification = message;
            return;
        }
        currentNotification = message;
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof WorldTickEvent && currentNotification != null) {
            if (currentNotification.getShowTicks() > 0) {
                currentNotification.setShowTicks(currentNotification.getShowTicks() - 1);
            } else {
                currentNotification = null;
            }
        }
    }

    public static class PopupMessage {
        private String title;
        private String message;
        private int color;
        private int titleColor;
        private long firstRenderTime;
        private int showTicks;

        public PopupMessage(String title, String message, int color, int titleColor, int showTicks) {
            this.title = title;
            this.message = message;
            this.color = color;
            this.showTicks = showTicks;
            this.titleColor = titleColor;
            this.firstRenderTime = Minecraft.getSystemTime();
        }

        public PopupMessage(String title, String message, ColorUtil.NotificationColors color, int showTicks) {
            this.title = title;
            this.message = message;
            this.color = color.getColor();
            this.showTicks = showTicks;
            this.titleColor = color.getTitleColor();
            this.firstRenderTime = Minecraft.getSystemTime();
        }

        public String getTitle() {
            return this.title;
        }

        public long getFirstRenderTime() {
            return this.firstRenderTime;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMessage() {
            return this.message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getColor() {
            return this.color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public int getShowTicks() {
            return this.showTicks;
        }

        public void setShowTicks(int showTicks) {
            this.showTicks = showTicks;
        }

        public int getTitleColor() {
            return this.titleColor;
        }

        public void setTitleColor(int titleColor) {
            this.titleColor = titleColor;
        }
    }
}

