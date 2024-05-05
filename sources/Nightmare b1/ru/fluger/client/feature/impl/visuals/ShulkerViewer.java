// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.visuals;

import ru.fluger.client.event.EventTarget;
import ru.fluger.client.helpers.render.rect.RectHelper;
import ru.fluger.client.event.events.impl.render.EventRenderToolTip;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.feature.Feature;

public class ShulkerViewer extends Feature
{
    public ShulkerViewer() {
        super("ShulkerViewer", "\u041f\u043e\u043a\u0430\u0437\u044b\u0432\u0430\u0435\u0442 \u0441\u043e\u0434\u0435\u0440\u0436\u0438\u043c\u043e\u0435 \u0448\u0430\u043b\u043a\u0435\u0440\u0430", Type.Visuals);
    }
    
    @EventTarget
    public void onRenderToolTip(final EventRenderToolTip event) {
        final fy tagCompound;
        final fy blockEntityTag;
        if (event.getStack().c() instanceof ajo && (tagCompound = event.getStack().p()) != null && tagCompound.b("BlockEntityTag", 10) && (blockEntityTag = tagCompound.p("BlockEntityTag")).b("Items", 9)) {
            event.setCancelled(true);
            final fi<aip> nonnulllist = fi.a(27, aip.a);
            tw.b(blockEntityTag, nonnulllist);
            bus.m();
            bus.E();
            bhz.a();
            bus.g();
            bus.j();
            final int width = Math.max(144, ShulkerViewer.mc.k.a(event.getStack().r()) + 3);
            final int x1 = event.getX() + 12;
            final int y1 = event.getY() - 12;
            final int height = 57;
            ShulkerViewer.mc.ad().a = 300.0f;
            RectHelper.drawMinecraftRect(x1 - 3, y1 - 4, x1 + width + 3, y1 - 3, -267386864, -267386864);
            RectHelper.drawMinecraftRect(x1 - 3, y1 + height + 3, x1 + width + 3, y1 + height + 4, -267386864, -267386864);
            RectHelper.drawMinecraftRect(x1 - 3, y1 - 3, x1 + width + 3, y1 + height + 3, -267386864, -267386864);
            RectHelper.drawMinecraftRect(x1 - 4, y1 - 3, x1 - 3, y1 + height + 3, -267386864, -267386864);
            RectHelper.drawMinecraftRect(x1 + width + 3, y1 - 3, x1 + width + 4, y1 + height + 3, -267386864, -267386864);
            RectHelper.drawMinecraftRect(x1 - 3, y1 - 3 + 1, x1 - 3 + 1, y1 + height + 3 - 1, 1347420415, 1344798847);
            RectHelper.drawMinecraftRect(x1 + width + 2, y1 - 3 + 1, x1 + width + 3, y1 + height + 3 - 1, 1347420415, 1344798847);
            RectHelper.drawMinecraftRect(x1 - 3, y1 - 3, x1 + width + 3, y1 - 3 + 1, 1347420415, 1347420415);
            RectHelper.drawMinecraftRect(x1 - 3, y1 + height + 2, x1 + width + 3, y1 + height + 3, 1344798847, 1344798847);
            ShulkerViewer.mc.k.a(event.getStack().r(), (float)(event.getX() + 12), (float)(event.getY() - 12), 16777215);
            bus.m();
            bus.e();
            bus.y();
            bus.f();
            bus.k();
            bhz.c();
            for (int i = 0; i < nonnulllist.size(); ++i) {
                final int iX = event.getX() + i % 9 * 16 + 11;
                final int iY = event.getY() + i / 9 * 16 - 11 + 8;
                final aip itemStack = nonnulllist.get(i);
                ShulkerViewer.mc.ad().b(itemStack, iX, iY);
                ShulkerViewer.mc.ad().a(ShulkerViewer.mc.k, itemStack, iX, iY, null);
            }
            bhz.a();
            ShulkerViewer.mc.ad().a = 0.0f;
            bus.f();
            bus.k();
            bhz.b();
            bus.D();
        }
    }
}
