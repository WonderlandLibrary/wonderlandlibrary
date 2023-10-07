// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management.notifications;

import java.util.Iterator;
import exhibition.Client;
import net.minecraft.client.renderer.GlStateManager;
import exhibition.util.RenderingUtil;
import exhibition.util.render.Colors;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import java.util.List;

public class NotificationRenderer implements INotificationRenderer
{
    @Override
    public void draw(final List<INotification> notifications) {
        final Minecraft mc = Minecraft.getMinecraft();
        final ScaledResolution scaledRes = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        float y = scaledRes.getScaledHeight() - notifications.size() * (float)(mc.fontRendererObj.FONT_HEIGHT * 2.5);
        final Iterator<INotification> iterator = notifications.iterator();
        while (iterator.hasNext()) {
            final Notification not = iterator.next();
            not.translate.interpolate(not.getTarX(), y, 5);
            RenderingUtil.rectangle(not.translate.getX(), not.translate.getY(), scaledRes.getScaledWidth(), not.translate.getY() + mc.fontRendererObj.FONT_HEIGHT * 2.5 - 2.0, Colors.getColor(0, 0, 0, 200));
            RenderingUtil.rectangle(not.translate.getX(), not.translate.getY(), not.translate.getX() + 20.0f, not.translate.getY() + mc.fontRendererObj.FONT_HEIGHT * 2.5 - 2.0, Colors.getColor(200, 40));
            for (int i = 0; i < 11; ++i) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(not.translate.getX() + 10.0f, not.translate.getY() + 13.0f, 0.0f);
                GlStateManager.rotate(270.0f, 0.0f, 0.0f, 90.0f);
                RenderingUtil.drawCircle(0.0f, 0.0f, 11 - i, 3, this.getColor(not.getType()));
                GlStateManager.popMatrix();
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate(not.translate.getX() + 10.0f, not.translate.getY() + 13.0f, 0.0f);
            GlStateManager.rotate(270.0f, 0.0f, 0.0f, 90.0f);
            RenderingUtil.drawCircle(0.0f, 0.0f, 11.0f, 3, Colors.getColor(0));
            GlStateManager.popMatrix();
            RenderingUtil.rectangle(not.translate.getX() + 9.6, not.translate.getY() + 5.0f, not.translate.getX() + 10.3, not.translate.getY() + 13.0f, Colors.getColor(0));
            RenderingUtil.rectangle(not.translate.getX() + 9.6, not.translate.getY() + 15.0f, not.translate.getX() + 10.3, not.translate.getY() + 17.0f, Colors.getColor(0));
            Client.header.drawStringWithShadow(not.getHeader(), not.translate.getX() + 22.0f, not.translate.getY() + mc.fontRendererObj.FONT_HEIGHT / 2 - 0.5f, -1);
            Client.subHeader.drawStringWithShadow(not.getSubtext(), not.translate.getX() + 22.0f, not.translate.getY() + mc.fontRendererObj.FONT_HEIGHT + 6.0f, -1);
            if (not.checkTime() >= not.getDisplayTime() + not.getStart()) {
                not.setTarX(scaledRes.getScaledWidth() + 500);
                if (not.translate.getX() >= scaledRes.getScaledWidth()) {
                    iterator.remove();
                }
            }
            y += (float)(mc.fontRendererObj.FONT_HEIGHT * 2.5);
        }
    }
    
    private int getColor(final Notifications.Type type) {
        int color = 0;
        switch (type) {
            case INFO: {
                color = Colors.getColor(64, 131, 214);
                break;
            }
            case NOTIFY: {
                color = Colors.getColor(242, 206, 87);
                break;
            }
            case WARNING: {
                color = Colors.getColor(226, 74, 74);
                break;
            }
        }
        return color;
    }
}
