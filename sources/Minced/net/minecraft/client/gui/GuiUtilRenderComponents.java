// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import net.minecraft.util.text.TextComponentString;
import java.util.List;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.client.Minecraft;

public class GuiUtilRenderComponents
{
    public static String removeTextColorsIfConfigured(final String text, final boolean forceColor) {
        return (!forceColor && !Minecraft.getMinecraft().gameSettings.chatColours) ? TextFormatting.getTextWithoutFormattingCodes(text) : text;
    }
    
    public static List<ITextComponent> splitText(final ITextComponent textComponent, final int maxTextLenght, final FontRenderer fontRendererIn, final boolean p_178908_3_, final boolean forceTextColor) {
        int i = 0;
        ITextComponent itextcomponent = new TextComponentString("");
        final List<ITextComponent> list = (List<ITextComponent>)Lists.newArrayList();
        final List<ITextComponent> list2 = (List<ITextComponent>)Lists.newArrayList((Iterable)textComponent);
        for (int j = 0; j < list2.size(); ++j) {
            final ITextComponent itextcomponent2 = list2.get(j);
            String s = itextcomponent2.getUnformattedComponentText();
            boolean flag = false;
            if (s.contains("\n")) {
                final int k = s.indexOf(10);
                final String s2 = s.substring(k + 1);
                s = s.substring(0, k + 1);
                final ITextComponent itextcomponent3 = new TextComponentString(s2);
                itextcomponent3.setStyle(itextcomponent2.getStyle().createShallowCopy());
                list2.add(j + 1, itextcomponent3);
                flag = true;
            }
            final String s3 = removeTextColorsIfConfigured(itextcomponent2.getStyle().getFormattingCode() + s, forceTextColor);
            final String s4 = s3.endsWith("\n") ? s3.substring(0, s3.length() - 1) : s3;
            int i2 = fontRendererIn.getStringWidth(s4);
            TextComponentString textcomponentstring = new TextComponentString(s4);
            textcomponentstring.setStyle(itextcomponent2.getStyle().createShallowCopy());
            if (i + i2 > maxTextLenght) {
                String s5 = fontRendererIn.trimStringToWidth(s3, maxTextLenght - i, false);
                String s6 = (s5.length() < s3.length()) ? s3.substring(s5.length()) : null;
                if (s6 != null && !s6.isEmpty()) {
                    int l = s5.lastIndexOf(32);
                    if (l >= 0 && fontRendererIn.getStringWidth(s3.substring(0, l)) > 0) {
                        s5 = s3.substring(0, l);
                        if (p_178908_3_) {
                            ++l;
                        }
                        s6 = s3.substring(l);
                    }
                    else if (i > 0 && !s3.contains(" ")) {
                        s5 = "";
                        s6 = s3;
                    }
                    final TextComponentString textcomponentstring2 = new TextComponentString(s6);
                    textcomponentstring2.setStyle(itextcomponent2.getStyle().createShallowCopy());
                    list2.add(j + 1, textcomponentstring2);
                }
                i2 = fontRendererIn.getStringWidth(s5);
                textcomponentstring = new TextComponentString(s5);
                textcomponentstring.setStyle(itextcomponent2.getStyle().createShallowCopy());
                flag = true;
            }
            if (i + i2 <= maxTextLenght) {
                i += i2;
                itextcomponent.appendSibling(textcomponentstring);
            }
            else {
                flag = true;
            }
            if (flag) {
                list.add(itextcomponent);
                i = 0;
                itextcomponent = new TextComponentString("");
            }
        }
        list.add(itextcomponent);
        return list;
    }
}
