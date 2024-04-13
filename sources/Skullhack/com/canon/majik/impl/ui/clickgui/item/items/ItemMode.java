package com.canon.majik.impl.ui.clickgui.item.items;

import com.canon.majik.api.core.Initializer;
import com.canon.majik.api.utils.render.RenderUtils;
import com.canon.majik.impl.modules.impl.client.ClickGui;
import com.canon.majik.impl.setting.settings.ModeSetting;
import com.canon.majik.impl.ui.clickgui.item.Item;

public class ItemMode extends Item<ModeSetting> {
    public ItemMode(ModeSetting object, int x, int y, int width, int height) {
        super(object, x, y, width, height);
    }

    private int modeIndex;

    @Override
    public int drawScreen(int mouseX, int mouseY, float partialTicks, int offset) {
        this.offset = offset;
        float y = this.y + offset;

        RenderUtils.rect(x, y, width, height, 0x80000000);
        RenderUtils.rect(x,y,width - 129,height, ClickGui.instance.color.getValue().getRGB());
        if(ClickGui.instance.cfont.getValue()){
            Initializer.CFont.drawStringWithShadow(getObject().getName() + ": " + getObject().getValue(), x + 5, y + height / 2f - Initializer.CFont.getHeight() / 2f, -1);
        }else {
            mc.fontRenderer.drawStringWithShadow(getObject().getName() + ": " + getObject().getValue(), x + 5, y + height / 2f - mc.fontRenderer.FONT_HEIGHT / 2f, -1);
        }
        return height;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (bounding(mouseX, mouseY)) {
            int maxIndex = 0;
            if (mouseButton == 0) {
                maxIndex = getObject().getModes().size() - 1;
                ++this.modeIndex;
                if (this.modeIndex > maxIndex) {
                    this.modeIndex = 0;
                }
                this.getObject().setValue(this.getObject().getModes().get(this.modeIndex));
            }

        }
    }
}
