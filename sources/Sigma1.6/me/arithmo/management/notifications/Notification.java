/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.management.notifications;

import me.arithmo.Client;
import me.arithmo.management.FontManager;
import me.arithmo.management.animate.Translate;
import me.arithmo.management.notifications.INotification;
import me.arithmo.management.notifications.Notifications;
import me.arithmo.util.render.TTFFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class Notification
implements INotification {
    private String header;
    private String subtext;
    private long start;
    private long displayTime;
    private Notifications.Type type;
    private float x;
    private float tarX;
    private float y;
    public Translate translate;
    private long last;

    @Override
    public long getLast() {
        return this.last;
    }

    @Override
    public void setLast(long last) {
        this.last = last;
    }

    protected Notification(String header, String subtext, long displayTime, Notifications.Type type) {
        this.header = header;
        this.subtext = subtext;
        this.start = System.currentTimeMillis();
        this.displayTime = displayTime;
        this.type = type;
        this.last = System.currentTimeMillis();
        ScaledResolution XD = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        this.y = XD.getScaledHeight() + 20;
        this.x = XD.getScaledWidth();
        float subHeaderWidth = Client.fm.getFont("SFM 8").getWidth(subtext);
        float headerWidth = Client.fm.getFont("SFB 11").getWidth(header);
        this.tarX = (float)(XD.getScaledWidth() - 20) - (headerWidth > subHeaderWidth ? headerWidth : subHeaderWidth);
        this.translate = new Translate(XD.getScaledWidth(), this.y);
    }

    @Override
    public long checkTime() {
        return System.currentTimeMillis() - this.getDisplayTime();
    }

    @Override
    public String getHeader() {
        return this.header;
    }

    @Override
    public String getSubtext() {
        return this.subtext;
    }

    @Override
    public long getStart() {
        return this.start;
    }

    @Override
    public long getDisplayTime() {
        return this.displayTime;
    }

    @Override
    public Notifications.Type getType() {
        return this.type;
    }

    @Override
    public float getX() {
        return this.x;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public float getTarX() {
        return this.tarX;
    }

    @Override
    public float getTarY() {
        return 0.0f;
    }

    @Override
    public void setTarX(int x) {
        this.tarX = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public float getY() {
        return this.y;
    }
}

