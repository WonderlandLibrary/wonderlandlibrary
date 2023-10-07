package wtf.monsoon.impl.ui.clickgui.dropdown1.impl.modern;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;
import wtf.monsoon.Monsoon;
import wtf.monsoon.api.module.Category;
import wtf.monsoon.api.module.Module;
import wtf.monsoon.api.setting.Setting;
import wtf.monsoon.api.setting.impl.BooleanSetting;
import wtf.monsoon.api.setting.impl.ModeSetting;
import wtf.monsoon.api.setting.impl.NumberSetting;
import wtf.monsoon.api.setting.impl.PlaceholderSetting;
import wtf.monsoon.api.util.font.MinecraftFontRenderer;
import wtf.monsoon.api.util.render.ColorUtil;
import wtf.monsoon.api.util.render.DrawUtil;
import wtf.monsoon.impl.ui.clickgui.dropdown1.api.ClickHandler;

import java.awt.*;

public class ModernClickGUI extends ClickHandler {


    private Category drag;
    private int dragX;
    private int dragY;

    public boolean isSliding = false;
    public NumberSetting set;

    public int startX, startY;
    public Module binding;

    int categoryColor, fontColor, enabledcolor,modBackground,settingBgColor;

   public void doColoring() {
       categoryColor = new Color(29, 29, 29).getRGB();
       fontColor = new Color(255,255,255).getRGB();
       enabledcolor = ColorUtil.astolfoColors(14, Monsoon.INSTANCE.manager.modules.size() * 10000);
       modBackground =  new Color(31, 31, 31).getRGB();
       settingBgColor = new Color(38, 38, 38).getRGB();
   }

    @Override
    public void drawCategory(int x, int y, int w, int h, int mouseX, int mouseY, Category category)
    {

        MinecraftFontRenderer fr = Monsoon.INSTANCE.getCustomFont();

        if (category.equals(drag))
        {
            category.setX(dragX + mouseX);
            category.setY(dragY + mouseY);
        }

        Gui.drawRect(x - 1, y, x+w + 1, y+h, categoryColor);
        fr.drawStringWithShadow(category.name, x + 3, y + h / 2 - fr.getHeight() / 2, fontColor);
    }

    @Override
    public void drawModule(int x, int y, int w, int h, int mouseX, int mouseY, Module module) {

        MinecraftFontRenderer fr = Monsoon.INSTANCE.getCustomFont();

        doColoring();

        Gui.drawRect(x, y, x + w, y + h, modBackground);
        if (module.isEnabled()) {
           Gui.drawRect(x + 0.5f, y+ 0.5f, x + w - 0.5f, y + h - 0.5f, enabledcolor);
        } else {
            Gui.drawRect(x, y, x + w, y + h - 1, modBackground);
        }
        GlStateManager.pushMatrix();
        fr.drawStringWithShadow(module.name, x + 3, y + h / 2 - fr.getHeight() / 2, fontColor);
        GlStateManager.popMatrix();
        if(module.getKey() != 0) {
            fr.drawStringWithShadow(EnumChatFormatting.GRAY + "[" + EnumChatFormatting.RESET + Keyboard.getKeyName(module.getKey()) + EnumChatFormatting.GRAY + "]", x + 3 +  fr.getStringWidth(module.name) + 4, y + h / 2 - fr.getHeight() / 2, fontColor);
        }

        if(!module.settings.isEmpty()) {
           Minecraft.getMinecraft().fontRendererObj.drawString(EnumChatFormatting.GRAY + "\u2022", x+w -  Minecraft.getMinecraft().fontRendererObj.getStringWidth("\u22EE") - 3, y + 3, fontColor);
           Minecraft.getMinecraft().fontRendererObj.drawString(EnumChatFormatting.GRAY + "\u2022", x+w -  Minecraft.getMinecraft().fontRendererObj.getStringWidth("\u22EE") - 3, y + 6, fontColor);
           Minecraft.getMinecraft().fontRendererObj.drawString(EnumChatFormatting.GRAY + "\u2022", x+w -  Minecraft.getMinecraft().fontRendererObj.getStringWidth("\u22EE") - 3, y + 9, fontColor);
        }
    }

    @Override
    public void drawSetting(int x, int y, int w, int h, int mouseX, int mouseY, Setting setting)
    {
        MinecraftFontRenderer fr = Monsoon.INSTANCE.getCustomFont();
        doColoring();

        if(setting instanceof BooleanSetting) {
            Gui.drawRect(x+ 1, y, x+ w - 1, y+h, settingBgColor);
            //DrawUtil.drawRoundedRect(x+w - 24, y + 5.5f, x+w - 2, y+h - 5.5f, 8, new Color(158, 158, 158).getRGB());
            if(((BooleanSetting) setting).isEnabled()) {
                DrawUtil.drawRoundedRect(x+w - 26, y + 5.5f, x+w - 4, y+h - 5.5f, 8, enabledcolor);
                DrawUtil.drawRoundedRect(x+w - 12, y + 6, x+w - 5, y+h - 6, 6, Color.DARK_GRAY.getRGB());
            } else {
                DrawUtil.drawRoundedRect(x+w - 26, y + 5.5f, x+w - 4, y+h - 5.5f, 8, Color.GRAY.getRGB());
                DrawUtil.drawRoundedRect(x+w - 25, y + 6, x+w - 18, y+h - 6, 6, Color.DARK_GRAY.getRGB());
            }
           fr.drawStringWithShadow(setting.name, x + 3, y + h / 2 - fr.getHeight() / 2, fontColor);
        } else if(setting instanceof ModeSetting) {
            //Gui.drawRect(x, y, x + 2, y+ h, fontColor);
            //Gui.drawRect(x + w, y, x + w - 2, y+ h, fontColor);
            Gui.drawRect(x+ 1, y, x+w - 1, y+h, settingBgColor);
            fr.drawStringWithShadow(setting.name + ": " + ((ModeSetting) setting).getMode(), x + 3, y + h / 2 - fr.getHeight() / 2, fontColor);
        } else if(setting instanceof NumberSetting) {
           // Gui.drawRect(x, y, x + 2, y+ h, fontColor);
            //Gui.drawRect(x + w, y, x + w - 2, y+ h, fontColor);
            int newWidth = w - 1;
            if(isSliding && set == setting){
                float value = (float)(mouseX - (x)) / (float)((w)/ ((NumberSetting) setting).getMaximum());
                value = clamp_float(value, (float) ((NumberSetting) setting).getMinimum(), (float) ((NumberSetting) setting).getMaximum());
                float f = this.denormalizeValue(value);
                ((NumberSetting) setting).setValue(value);
                value = this.normalizeValue(f);
            }
            Gui.drawRect(x+ 1, y, x+w - 1, y+h, settingBgColor);
            Gui.drawRect(x + 1, y + 15, ((x) + (((NumberSetting) setting).getValue()/ ((NumberSetting) setting).getMaximum()*newWidth)), (y + h) - 3, enabledcolor);

            double newX = ((x) + (((NumberSetting) setting).getValue()/ ((NumberSetting) setting).getMaximum()*newWidth));

            DrawUtil.drawRoundedRect(newX - 2, y + 13, newX + 2, y+h - 1, 3, enabledcolor);


            fr.drawStringWithShadow(setting.name + " " + ((NumberSetting) setting).getValue(), x + 3, y + h / 2 - fr.getHeight() / 2 - 2, fontColor);
        } else if(setting instanceof PlaceholderSetting) {
            //Gui.drawRect(x, y, x + 2, y+ h, fontColor);
            //Gui.drawRect(x + w, y, x + w - 2, y+ h, fontColor);
            Gui.drawRect(x+ 1, y, x+w - 1, y+h, settingBgColor);
            fr.drawStringWithShadow(setting.name, x + w / 2 - fr.getStringWidth(setting.name) / 2, y + h / 2 - fr.getHeight() / 2, fontColor);
        }

    }

    @Override
    public void clickCategory(int mouseX, int mouseY, int mouseButton, Category category)
    {
        if (mouseButton == 0)
        {
            drag = category;
            dragX = category.getX() - mouseX;
            dragY = category.getY() - mouseY;
        }
        else if (mouseButton == 1) category.setOpen(!category.isOpen());
    }

    @Override
    public void clickModule(int mouseX, int mouseY, int mouseButton, Module module)
    {
        if (mouseButton == 0) module.toggle();
        else if (mouseButton == 1) module.setOpen(!module.isOpen());
    }

    @Override
    public void clickSetting(int mouseX, int mouseY, int mouseButton, Setting setting) {
        if(setting instanceof BooleanSetting && mouseButton == 0) {
            ((BooleanSetting) setting).toggle();
        } else if(setting instanceof ModeSetting) {
            if (mouseButton == 0){
                ((ModeSetting) setting).cycle();
            }
        } else if(setting instanceof NumberSetting){
            this.isSliding = true;
            set = (NumberSetting) setting;
        } else if(setting instanceof PlaceholderSetting) {
            //Monsoon.sendMessage("secks");
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state)
    {
        drag = null;
        isSliding = false;
        set = null;
    }

    @Override
    public void onClose()
    {
        Monsoon.INSTANCE.manager.clickGuiMod.setEnabled(false);
    }

    public float normalizeValue(float val)
    {
        NumberSetting ns = (NumberSetting) set;
        return clamp_float((float) ((this.snapToStepClamp(val) - ns.getMinimum()) / (ns.getMaximum() - ns.getMinimum())), 0.0F, 1.0F);
    }

    public float denormalizeValue(float p_148262_1_)
    {
        NumberSetting ns = (NumberSetting) set;
        return this.snapToStepClamp((float) (ns.getMinimum() + (ns.getMaximum() - ns.getMinimum()) * clamp_float(p_148262_1_, 0.0F, 1.0F)));
    }

    public float snapToStepClamp(float p_148268_1_)
    {
        NumberSetting ns = (NumberSetting) set;
        p_148268_1_ = this.snapToStep(p_148268_1_);
        return clamp_float(p_148268_1_, (float) ns.getMinimum(),(float)  ns.getMaximum());
    }

    protected float snapToStep(float p_148264_1_)
    {
        NumberSetting ns = (NumberSetting) set;
        if (ns.getIncrement() > 0.0F)
        {
            p_148264_1_ = (float) (ns.getIncrement() * (float)Math.round(p_148264_1_ / ns.getIncrement()));
        }

        return p_148264_1_;
    }
    public static float clamp_float(float num, float min, float max)
    {
        return num < min ? min : (num > max ? max : num);
    }
}
