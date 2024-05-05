// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.ui.notification;

import ru.fluger.client.feature.impl.hud.Notifications;
import ru.fluger.client.helpers.misc.TimerHelper;
import ru.fluger.client.helpers.render.ScreenHelper;
import ru.fluger.client.helpers.Helper;

public class Notification implements Helper
{
    private final ScreenHelper screenHelper;
    private final bip fontRenderer;
    private final String title;
    private final String content;
    private final int time;
    private final NotificationType type;
    private final TimerHelper timer;
    
    public Notification(final String title, final String content, final NotificationType type, final int second, final bip fontRenderer) {
        this.title = title;
        this.content = content;
        this.time = second;
        this.type = type;
        this.timer = new TimerHelper();
        this.fontRenderer = fontRenderer;
        final bit sr = new bit(Notification.mc);
        this.screenHelper = new ScreenHelper((float)(sr.a() + 100), (float)(sr.b() - 60));
    }
    
    public int getWidth() {
        return Notification.mc.k.a(this.content) + (Notifications.timePeriod.getCurrentValue() ? 70 : 90);
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public String getContent() {
        return this.content;
    }
    
    public int getTime() {
        return this.time;
    }
    
    public NotificationType getType() {
        return this.type;
    }
    
    public TimerHelper getTimer() {
        return this.timer;
    }
    
    public ScreenHelper getTranslate() {
        return this.screenHelper;
    }
}
