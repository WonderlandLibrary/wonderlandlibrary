package net.minecraft.client.gui;

import com.google.common.base.Predicate;
import java.net.IDN;

class GuiScreenAddServer.1
implements Predicate<String> {
    GuiScreenAddServer.1() {
    }

    public boolean apply(String p_apply_1_) {
        if (p_apply_1_.length() == 0) {
            return true;
        }
        String[] astring = p_apply_1_.split(":");
        if (astring.length == 0) {
            return true;
        }
        try {
            String s = IDN.toASCII((String)astring[0]);
            return true;
        }
        catch (IllegalArgumentException var4) {
            return false;
        }
    }
}
