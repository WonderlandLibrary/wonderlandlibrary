package com.canon.majik.impl.ui.clickgui.item.items;

import com.canon.majik.api.core.Initializer;
import com.canon.majik.api.utils.client.TColor;
import com.canon.majik.api.utils.render.RenderUtils;
import com.canon.majik.impl.modules.impl.client.ClickGui;
import com.canon.majik.impl.setting.settings.ColorSetting;
import com.canon.majik.impl.ui.clickgui.item.Item;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

import java.awt.Color;

public class ItemColor extends Item<ColorSetting> {
    private float settingHue, settingSaturation, settingBrightness;
    private boolean movingPicker;
    private boolean isSlideHue;
    private boolean open;

    public ItemColor(ColorSetting colorSetting, int x, int y, int width, int height) {
        super(colorSetting, x, y, width, height);
        float[] hsb = Color.RGBtoHSB(getObject().getValue().getRed(), getObject().getValue().getGreen(), getObject().getValue().getBlue(), new float[3]);
        this.settingHue = hsb[0];
        this.settingSaturation = hsb[1];
        this.settingBrightness = hsb[2];
        this.open = false;
    }

    @Override
    public int drawScreen(int mouseX, int mouseY, float partialTicks, int offset) {
        this.offset = offset;
        int y = this.y + offset;

        RenderUtils.rect(x, y, width, open ? height+width:height, 0x80000000);
        RenderUtils.rect(x, y,width - 129,open ? height+width:height, ClickGui.instance.color.getValue().getRGB());
        RenderUtils.rect(x + width - 15, y + 2, height - 4, height - 4, getObject().getValue().hashCode());
        if(ClickGui.instance.cfont.getValue()){
            Initializer.CFont.drawStringWithShadow(getObject().getName(), x + height - 10, y + height / 2F - Initializer.CFont.getHeight() / 2F, -1);
        }else {
            mc.fontRenderer.drawStringWithShadow(getObject().getName(), x + height - 10, y + height / 2F - mc.fontRenderer.FONT_HEIGHT / 2F, -1);
        }
        if(open){
            drawColorPicker(x + 3, y + height + 3, width - 6, width - 18, Color.HSBtoRGB(settingHue, settingSaturation, settingBrightness));

            //saturation brightness
            RenderUtils.rect(x + 3 + (width - 6) * settingSaturation, y + height + 3 + (width - 18) * (1 - settingBrightness), 1, 1, 0x80000000);
            for (int hueX = 0; hueX < width - 6; hueX++) {
                RenderUtils.rect(x + 3 + hueX, y + height + 3 + width - 15, 1, 12, Color.HSBtoRGB((float) hueX / (width - 6), 1F, 1F));
            }
            //hue
            RenderUtils.rect(x + 3 + (width - 6) * settingHue, y + height + 3 + width - 15, 1, 12, 0x80000000);
    
            if (movingPicker) {
                settingSaturation = MathHelper.clamp(((float) mouseX - (x + 3)) / (width - 6), 0F, 1F);
                settingBrightness = 1 - MathHelper.clamp(((float) mouseY - (y + height + 3)) / (width - 18), 0F, 1F);
                getObject().setValue(TColor.getHSBColor(settingHue, settingSaturation, settingBrightness));
            } else if (isSlideHue) {
                settingHue = MathHelper.clamp(((float) mouseX - (x + 3)) / (width - 6), 0F, 1F);
                getObject().setValue(TColor.getHSBColor(settingHue, settingSaturation, settingBrightness));
            }
        }
        return open ? width + height : height;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (bounding(mouseX, mouseY, x + 3, y + height + 3, width - 6, width - 18))
            movingPicker = true;
        else if (bounding(mouseX, mouseY, x + 3, y + height + 3 + width - 15, width - 6, 12))
            isSlideHue = true;
        if(bounding(mouseX,mouseY) && mouseButton == 1){
            open = !open;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        movingPicker = false;
        isSlideHue = false;
    }

    public void drawColorPicker(int x, int y, int width, int height, int color) {
        int red = color >> 16 & 0xFF;
        int green = color >> 8 & 0xFF;
        int blue = color & 0xFF;
        float hue = Color.RGBtoHSB(red, green, blue, new float[3])[0];
        for (int colorX = 0; colorX < width; colorX++) {
            for (int colorY = 0; colorY < height; colorY++) {
                float saturation = (float) colorX / width;
                float brightness = 1F - (float) colorY / height;
                RenderUtils.rect(x + colorX, y + colorY, 1, 1, Color.HSBtoRGB(hue, saturation, brightness));
            }
        }
    }

}
