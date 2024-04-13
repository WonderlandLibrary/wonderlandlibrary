package com.canon.majik.impl.ui.clickgui.item.items;

import com.canon.majik.api.core.Initializer;
import com.canon.majik.api.utils.render.RenderUtils;
import com.canon.majik.impl.modules.impl.client.ClickGui;
import com.canon.majik.impl.setting.settings.BooleanSetting;
import com.canon.majik.impl.ui.clickgui.item.Item;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class ItemBoolean extends Item<BooleanSetting> {
    public ItemBoolean(BooleanSetting boolSetting, int x, int y, int width, int height) {
        super(boolSetting, x, y, width, height);
    }

    @Override
    public int drawScreen(int mouseX, int mouseY, float partialTicks, int offset) {
        this.offset = offset;
        float y = this.y + offset;

        RenderUtils.rect(x, y, width, height, getObject().getValue() ? ClickGui.instance.color.getValue().getRGB() : 0x80000000);
        RenderUtils.rect(x, y,width - 129,height, ClickGui.instance.color.getValue().getRGB());

        if(ClickGui.instance.cfont.getValue()){
            Initializer.CFont.drawStringWithShadow(getObject().getName(), x + 5, y + height / 2f - Initializer.CFont.getHeight() / 2f, -1);
        }else {
            mc.fontRenderer.drawStringWithShadow(getObject().getName(), x + 5, y + height / 2f - mc.fontRenderer.FONT_HEIGHT / 2f, -1);
        }
        return height;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (bounding(mouseX, mouseY)) {
            if (mouseButton == 0)
                getObject().setValue(!getObject().getValue());
        }
    }
}
