// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.util;

public class StringConversions
{
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
}
