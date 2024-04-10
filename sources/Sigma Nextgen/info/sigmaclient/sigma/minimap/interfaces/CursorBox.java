package info.sigmaclient.sigma.minimap.interfaces;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

import java.util.ArrayList;

public class CursorBox
{
    private ArrayList<String> strings;
    private String language;
    private String fullCode;
    private int boxWidth;
    private static final int color = -2147483640;
    
    public CursorBox(final String code) {
        this.fullCode = "";
        this.boxWidth = 150;
        this.fullCode = code;
        final String text = I18n.format(code, new Object[0]);
        this.createLines(text);
    }
    
    public void createLines(final String text) {
//        this.language = Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode();
        this.strings = new ArrayList<String>();
        final String[] words = text.split(" ");
        final int spaceWidth = (int) Minecraft.getInstance().fontRenderer.getStringWidth(" ");
        String line = "";
        int width = 0;
        for (int i = 0; i < words.length; ++i) {
            final int wordWidth = (int) Minecraft.getInstance().fontRenderer.getStringWidth(words[i]);
            if (width == 0 && wordWidth > this.boxWidth - 15) {
                this.boxWidth = wordWidth + 15;
            }
            if (width + wordWidth <= this.boxWidth - 15) {
                width += spaceWidth + wordWidth;
                line = line + words[i] + " ";
            }
            else {
                this.strings.add(line);
                line = new String("");
                width = 0;
                --i;
            }
            if (i == words.length - 1) {
                this.strings.add(line);
            }
        }
    }
    
    public CursorBox(final int size) {
        this.fullCode = "";
        this.boxWidth = 150;
        this.strings = new ArrayList<String>();
        for (int i = 0; i < size; ++i) {
            this.strings.add("");
        }
    }
    
    public String getString(final int line) {
        return this.strings.get(line);
    }
}
