package org.dreamcore.client.feature.impl.misc;

import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.misc.ChatHelper;
import org.dreamcore.client.settings.impl.NumberSetting;

public class ItemScroller extends Feature {

    public static NumberSetting scrollerDelay;

    public ItemScroller() {
        super("ItemScroller", "Позволяет быстро лутать сундуки при нажатии на шифт и ЛКМ", Type.Misc);
        scrollerDelay = new NumberSetting("Scroller Delay", 0, 0, 1000, 50, () -> true);
        addSettings(scrollerDelay);
    }

    @Override
    public void onEnable() {
        for (int i = 0; i < 3; i++) {
            ChatHelper.addChatMessage("Зажмите шифт и левую кнопку мыши, что бы быстро лутать сундуки!");
        }
        super.onEnable();
    }
}
