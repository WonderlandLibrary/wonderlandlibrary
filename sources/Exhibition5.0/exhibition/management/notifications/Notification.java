// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management.notifications;

import exhibition.util.misc.ChatUtil;
import exhibition.Client;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import exhibition.management.animate.Translate;

public class Notification implements INotification
{
    private String header;
    private String subtext;
    private long start;
    private long fade;
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
    public void setLast(final long last) {
        this.last = last;
    }
    
    protected Notification(final String header, final String subtext, final long fade, final long displayTime, final Notifications.Type type) {
        this.header = header;
        this.subtext = subtext;
        this.start = System.currentTimeMillis();
        this.fade = Math.min(displayTime / 2L, fade);
        this.displayTime = displayTime;
        this.type = type;
        this.last = System.currentTimeMillis();
        final ScaledResolution XD = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        this.y = XD.getScaledHeight() + 20;
        this.x = XD.getScaledWidth();
        final float subHeaderWidth = Client.subHeader.getWidth(subtext);
        final float headerWidth = Client.header.getWidth(header);
        ChatUtil.printChat(subHeaderWidth + " " + headerWidth);
        this.tarX = XD.getScaledWidth() - 25 - ((headerWidth > subHeaderWidth) ? headerWidth : subHeaderWidth);
        this.translate = new Translate(this.tarX, this.y);
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
    public void setX(final int x) {
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
    public void setTarX(final int x) {
        this.tarX = x;
    }
    
    @Override
    public void setY(final int y) {
        this.y = y;
    }
    
    @Override
    public float getY() {
        return this.y;
    }
}
