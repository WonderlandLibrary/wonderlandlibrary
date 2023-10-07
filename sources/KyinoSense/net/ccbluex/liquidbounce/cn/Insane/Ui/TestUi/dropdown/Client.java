/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.cn.Insane.Ui.TestUi.dropdown;

import net.ccbluex.liquidbounce.cn.Insane.Ui.TestUi.dropdown.DropdownGUI;
import net.ccbluex.liquidbounce.utils.ClientUtils;

public class Client {
    private static Client INSTANCE;
    public DropdownGUI dropDownGUI;

    public DropdownGUI getDropDownGUI() {
        return this.dropDownGUI;
    }

    public static Client getInstance() {
        try {
            if (INSTANCE == null) {
                INSTANCE = new Client();
            }
            return INSTANCE;
        }
        catch (Throwable t) {
            ClientUtils.getLogger().warn((Object)t);
            throw t;
        }
    }
}

