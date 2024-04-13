package com.canon.majik.impl.ui.clickgui.item.items;

import com.canon.majik.api.core.Initializer;
import com.canon.majik.api.utils.render.RenderUtils;
import com.canon.majik.impl.modules.impl.client.ClickGui;
import com.canon.majik.impl.setting.settings.NumberSetting;
import com.canon.majik.impl.ui.clickgui.item.Item;
import net.minecraft.util.math.MathHelper;

public class ItemNumber extends Item<NumberSetting> {
    private boolean isSlide;
  
    public ItemNumber(NumberSetting intSetting, int x, int y, int width, int height) {
        super(intSetting, x, y, width, height);
    }
  
    @Override
    public int drawScreen(int mouseX, int mouseY, float partialTicks, int offset) {
        this.offset = offset;
        float y = offset + this.y;

        RenderUtils.rect(x, y, width, height, 0x80000000);
        RenderUtils.rect(x,y,width - 129,height,ClickGui.instance.color.getValue().getRGB());
        float drawWidth = (getObject().getValue().floatValue() - getObject().getMin().floatValue()) / (getObject().getMax().floatValue() - getObject().getMin().floatValue());
        RenderUtils.rect(x, y, width * drawWidth, height, ClickGui.instance.color.getValue().getRGB());
        String text = getObject().getName() + " " + getObject().getValue().intValue();
        if(ClickGui.instance.cfont.getValue()){
            Initializer.CFont.drawStringWithShadow(text, x + 5, y + height / 2F - Initializer.CFont.getHeight() / 2F, -1);
        }else {
            mc.fontRenderer.drawStringWithShadow(text, x + 5, y + height / 2F - mc.fontRenderer.FONT_HEIGHT / 2F, -1);
        }
        if (this.isSlide) {
            float value = ((float) mouseX - x) / width;
            value = MathHelper.clamp(value, 0.0F, 1.0F);
            getObject().setValue(getObject().getMin().floatValue() + (getObject().getMax().floatValue() - getObject().getMin().floatValue()) * value);
        }
      
        return height;
    }
  
    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (bounding(mouseX, mouseY)) {
            this.isSlide = true;
        }
    }
  
    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        this.isSlide = false;
    }
}
