/*
 * Decompiled with CFR 0.152.
 */
package me.report.liquidware.modules.player;

import java.util.Random;
import me.report.liquidware.variables.Bypass;
import me.report.liquidware.variables.LMessages;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.KeyEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.ListValue;

@ModuleInfo(name="LTap", description="LTap", category=ModuleCategory.PLAYER)
public class LTap
extends Module {
    private final String[] bypassList = new String[]{"Off", "HuaYuTing"};
    private final ListValue bypass = new ListValue("ChatBypass", this.bypassList, this.bypassList[0]);

    @EventTarget
    public void onKey(KeyEvent event) {
        if (event.getKey() != 38) {
            return;
        }
        Random random = new Random();
        String content = LMessages.LMessages[random.nextInt(LMessages.LMessages.length)];
        if (((String)this.bypass.get()).equals("HuaYuTing")) {
            for (String tmp : Bypass.HuaYuTing.keySet().toArray(new String[0])) {
                content = content.replace(tmp, Bypass.HuaYuTing.get(tmp));
            }
        }
        LTap.mc.field_71439_g.func_71165_d(content);
    }
}

