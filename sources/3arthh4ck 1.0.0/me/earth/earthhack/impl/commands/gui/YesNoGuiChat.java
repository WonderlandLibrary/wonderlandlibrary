package me.earth.earthhack.impl.commands.gui;

import me.earth.earthhack.api.util.TextColor;
import me.earth.earthhack.impl.services.render.TextRenderer;
import me.earth.earthhack.impl.util.text.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public abstract class YesNoGuiChat extends GuiChat implements Runnable
{
    private static final Set<String> YES = new HashSet<>();
    private static final Set<String> NO = new HashSet<>();

    private final String initial;

    static
    {
        YES.add("y");
        YES.add("yes");
        YES.add("ja");
        YES.add("true");
        YES.add("ye");
        YES.add("yess");
        YES.add("yeess");
        YES.add("oui");

        NO.add("n");
        NO.add("no");
        NO.add("false");
        NO.add("nein");
        NO.add("na");
        NO.add("noo");
        NO.add("non");
    }

    public YesNoGuiChat()
    {
        this("Y/N ?");
    }

    public YesNoGuiChat(String message)
    {
        this.initial = message;
        this.mc = Minecraft.getMinecraft();
    }

    @Override
    public void run()
    {
        this.mc.displayGuiScreen(this);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (keyCode == 15)
        {
            this.inputField.setText(YES.contains(this.inputField.getText().toLowerCase()) ? "No" : "Yes");
        }
        else if (keyCode == 28 || keyCode == 156)
        {
            String s = this.inputField.getText().trim();
            if (YES.contains(s.toLowerCase()))
            {
                onYes();
            }
            else if (NO.contains(s.toLowerCase()))
            {
                onNo();
            }
            else
            {
                onFail();
            }

            this.mc.displayGuiScreen(null);
        }
        else
        {
            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (this.inputField.getText().trim().isEmpty())
        {
            TextRenderer.getInstance().drawString(initial, inputField.x, inputField.y, 0xffffffff, true);
        }
    }

    public abstract void onYes();

    public abstract void onNo();

    public void onFail()
    {
        ChatUtil.sendMessage(TextColor.RED + "Cancelled. Valid inputs: Y, N, basically everything meaning Yes or No, how could you fail this?");
    }

}
