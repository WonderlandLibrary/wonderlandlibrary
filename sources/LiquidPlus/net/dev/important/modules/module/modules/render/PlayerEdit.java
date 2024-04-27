/*
 * Decompiled with CFR 0.152.
 */
package net.dev.important.modules.module.modules.render;

import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;

@Info(name="PlayerEdit", description="Edit the player.", category=Category.PLAYER, cnName="\u73a9\u5bb6\u5927\u5c0f")
public class PlayerEdit
extends Module {
    public static FloatValue playerSizeValue = new FloatValue("PlayerSize", 0.5f, 0.01f, 5.0f);
    public static BoolValue editPlayerSizeValue = new BoolValue("EditPlayerSize", true);
}

