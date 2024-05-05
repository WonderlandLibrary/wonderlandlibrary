/*
 * Decompiled with CFR 0.152.
 */
package me.report.liquidware.modules.fun;

import me.report.liquidware.utils.TimerUtils;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import obfuscator.NativeMethod;

@ModuleInfo(name="MemoryFix", description="Clears RAM during the game.", category=ModuleCategory.FUN)
public class MemoryFixer
extends Module {
    private TimerUtils timer = new TimerUtils();

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onTick() {
        if (TimerUtils.hasReached(3000L)) {
            System.gc();
            Runtime.getRuntime().gc();
            MemoryFixer memoryFixer = this;
            memoryFixer.timer.reset();
        }
    }
}

