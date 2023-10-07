package wtf.monsoon.impl.ui.clickgui.dropdown1.api;


import java.awt.*;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import wtf.monsoon.Monsoon;
import wtf.monsoon.api.module.Category;
import wtf.monsoon.api.module.Module;
import wtf.monsoon.api.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import wtf.monsoon.api.util.render.ColorUtil;
import wtf.monsoon.impl.ui.clickgui.dropdown1.impl.ClickGUI;

public class CGUI extends GuiScreen
{

    public static class ModuleComparator implements Comparator<Module> {
        @Override
        public int compare(Module arg0, Module arg1) {
            if(Minecraft.getMinecraft().fontRendererObj.getStringWidth(arg0.getDisplayname()) > Minecraft.getMinecraft().fontRendererObj.getStringWidth(arg1.getName())) {
                return -1;
            }
            if(Minecraft.getMinecraft().fontRendererObj.getStringWidth(arg0.getDisplayname()) < Minecraft.getMinecraft().fontRendererObj.getStringWidth(arg1.getName())) {
                return 1;
            }
            return 0;
        }
    }

    private final int width;
    private final int height;

    public CGUI(int width, int height)
    {
        Collections.sort(Monsoon.INSTANCE.manager.modules, new CGUI.ModuleComparator());
        this.width = width;
        this.height = height;

        int xOffset = 3;
        for (Category category : Category.values())
        {
            category.setOpen(true);
            category.setX(xOffset);
            category.setY(3);
            xOffset += width + 3;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        ScaledResolution sr = new ScaledResolution(mc);
    	Monsoon.INSTANCE.manager.modules.sort(Comparator.comparingDouble(m -> {
                    String modulesText = ((Module) m).getName();

                    return mc.fontRendererObj.getStringWidth(modulesText);

                })
                        .reversed()
        );
    	Monsoon.INSTANCE.getClickGUI().drawScreen(mouseX, mouseY, partialTicks);

        for (Category category : Category.values())
        {
            if (category == Category.HIDDEN) continue;

            int x = category.getX();
            int y = category.getY();

            Monsoon.INSTANCE.getClickGUI().drawCategory(x, y, width, height, mouseX, mouseY, category);

            if (!category.isOpen()) continue;

            for (Module module : Monsoon.INSTANCE.manager.getModulesByCategory(category))
            {
                y += height;

                Monsoon.INSTANCE.getClickGUI().drawModule(x, y, width, height, mouseX, mouseY, module);

                if (!module.isOpen()) continue;

                for (Setting setting : module.settings)
                {
                    if(setting.shouldRender) {
                        y += height;

                        Monsoon.INSTANCE.getClickGUI().drawSetting(x, y, width, height, mouseX, mouseY, setting);
                    }
                }
            }
            Monsoon.INSTANCE.getClickGUI().drawCategory(x, category.getY(), width, height, mouseX, mouseY, category);
        }

        int color = 0;
        if(Monsoon.INSTANCE.manager.clickGuiMod.theme.is("Modern")) {
            color = ColorUtil.astolfoColors(14, Monsoon.INSTANCE.manager.modules.size() * 10000);
            //this.drawGradientRect(0, sr.getScaledHeight() - 220, sr.getScaledWidth(), sr.getScaledHeight(), new Color(0,0,0,0).getRGB(), color);
        } else if(Monsoon.INSTANCE.manager.clickGuiMod.theme.is("Monsoon")) {
            color = new Color(0,140,255).getRGB();
        }else if(Monsoon.INSTANCE.manager.clickGuiMod.theme.is("Discord")) {
            color = new Color(114, 137, 218).getRGB();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        for (Category category : Category.values())
        {
            int x = category.getX();
            int y = category.getY();
            if (isHover(x, y, width, height, mouseX, mouseY)) Monsoon.INSTANCE.getClickGUI().clickCategory(mouseX, mouseY, mouseButton, category);

            if (!category.isOpen()) continue;

            for (Module module : Monsoon.INSTANCE.manager.getModulesByCategory(category))
            {
                y += height;

                if (isHover(x, y, width, height, mouseX, mouseY)) Monsoon.INSTANCE.getClickGUI().clickModule(mouseX, mouseY, mouseButton, module);

                if (!module.isOpen()) continue;

                for (Setting setting : module.settings)
                {
                    if(setting.shouldRender) {
                        y += height;

                        if (isHover(x, y, width, height, mouseX, mouseY))
                        	Monsoon.INSTANCE.getClickGUI().clickSetting(mouseX, mouseY, mouseButton, setting);
                    }
                }
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
    	Monsoon.INSTANCE.getClickGUI().mouseReleased(mouseX, mouseY, state);
    }


    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        super.keyTyped(typedChar, keyCode);

        Monsoon.INSTANCE.getClickGUI().keyTyped(typedChar, keyCode);
    }

    @Override
    public void initGui()
    {
    	Monsoon.INSTANCE.getClickGUI().onOpen();
    }

    @Override
    public void onGuiClosed()
    {
    	Monsoon.INSTANCE.getClickGUI().onClose();
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return Monsoon.INSTANCE.getClickGUI().doesPauseGame();
    }

    public static boolean isHover(int X, int Y, int W, int H, int mX, int mY)
    {
        return mX >= X && mX <= X + W && mY >= Y && mY <= Y + H;
    }
}
