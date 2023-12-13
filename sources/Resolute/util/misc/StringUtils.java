// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.util.misc;

import java.io.IOException;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.datatransfer.DataFlavor;
import java.awt.Toolkit;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;

public class StringUtils
{
    private StringUtils() {
    }
    
    public static String upperSnakeCaseToPascal(final String s) {
        return s.charAt(0) + s.substring(1).toLowerCase();
    }
    
    public static boolean isTeamMate(final EntityLivingBase entity) {
        final String entName = entity.getDisplayName().getFormattedText();
        final String playerName = Minecraft.getMinecraft().thePlayer.getDisplayName().getFormattedText();
        return entName.length() >= 2 && playerName.length() >= 2 && entName.startsWith("§") && playerName.startsWith("§") && entName.charAt(1) == playerName.charAt(1);
    }
    
    public static Object castNumber(final String newValueText, final Object currentValue) {
        if (newValueText.contains(".")) {
            if (newValueText.toLowerCase().contains("f")) {
                return Float.parseFloat(newValueText);
            }
            return Double.parseDouble(newValueText);
        }
        else {
            if (isNumeric(newValueText)) {
                return Integer.parseInt(newValueText);
            }
            return newValueText;
        }
    }
    
    public static boolean isNumeric(final String text) {
        try {
            Integer.parseInt(text);
            return true;
        }
        catch (NumberFormatException nfe) {
            return false;
        }
    }
    
    public static String getTrimmedClipboardContents() {
        String data = null;
        try {
            data = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        }
        catch (UnsupportedFlavorException ex) {}
        catch (IOException ex2) {}
        if (data != null) {
            data = data.trim();
            if (data.indexOf(10) != -1) {
                data = data.replace("\n", "");
            }
        }
        return data;
    }
}
