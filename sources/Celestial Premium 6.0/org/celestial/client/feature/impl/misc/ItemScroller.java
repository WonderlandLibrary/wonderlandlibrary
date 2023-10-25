/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.misc;

import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.misc.ChatHelper;
import org.celestial.client.settings.impl.NumberSetting;

public class ItemScroller
extends Feature {
    public static NumberSetting scrollerDelay;

    public ItemScroller() {
        super("ItemScroller", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0431\u044b\u0441\u0442\u0440\u043e \u043b\u0443\u0442\u0430\u0442\u044c \u0441\u0443\u043d\u0434\u0443\u043a\u0438 \u043f\u0440\u0438 \u043d\u0430\u0436\u0430\u0442\u0438\u0438 \u043d\u0430 \u0448\u0438\u0444\u0442 \u0438 \u041b\u041a\u041c", Type.Misc);
        scrollerDelay = new NumberSetting("Scroller Delay", 0.0f, 0.0f, 1000.0f, 50.0f, () -> true);
        this.addSettings(scrollerDelay);
    }

    @Override
    public void onEnable() {
        for (int i = 0; i < 3; ++i) {
            ChatHelper.addChatMessage("\u0417\u0430\u0436\u043c\u0438\u0442\u0435 \u0448\u0438\u0444\u0442 \u0438 \u043b\u0435\u0432\u0443\u044e \u043a\u043d\u043e\u043f\u043a\u0443 \u043c\u044b\u0448\u0438, \u0447\u0442\u043e \u0431\u044b \u0431\u044b\u0441\u0442\u0440\u043e \u043b\u0443\u0442\u0430\u0442\u044c \u0441\u0443\u043d\u0434\u0443\u043a\u0438!");
        }
        super.onEnable();
    }
}

