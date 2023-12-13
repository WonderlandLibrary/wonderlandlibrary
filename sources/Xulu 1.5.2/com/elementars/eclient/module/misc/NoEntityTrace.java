// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.misc;

import net.minecraft.init.Items;
import java.util.Collection;
import java.util.Arrays;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import java.util.ArrayList;
import com.elementars.eclient.module.Module;

public class NoEntityTrace extends Module
{
    private ArrayList<String> options;
    private final Value<String> mode;
    private final Value<Boolean> pickaxe;
    private static NoEntityTrace INSTANCE;
    
    public NoEntityTrace() {
        super("NoEntityTrace", "Keeps mining through an entity", 0, Category.MISC, true);
        this.mode = this.register(new Value<String>("Mode", this, "Dynamic", new ArrayList<String>(Arrays.asList("Dynamic", "Static"))));
        this.pickaxe = this.register(new Value<Boolean>("Pickaxe Only", this, true));
        NoEntityTrace.INSTANCE = this;
    }
    
    public static boolean shouldBlock() {
        return NoEntityTrace.INSTANCE.isToggled() && (NoEntityTrace.INSTANCE.mode.getValue().equalsIgnoreCase("Static") || NoEntityTrace.mc.playerController.isHittingBlock) && (!NoEntityTrace.INSTANCE.pickaxe.getValue() || NoEntityTrace.mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_PICKAXE);
    }
}
