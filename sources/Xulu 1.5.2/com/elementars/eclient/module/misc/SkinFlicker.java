// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.misc;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import com.elementars.eclient.module.Category;
import java.util.Random;
import net.minecraft.entity.player.EnumPlayerModelParts;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class SkinFlicker extends Module
{
    private final Value<String> mode;
    private final Value<Integer> slowness;
    private static final EnumPlayerModelParts[] PARTS_HORIZONTAL;
    private static final EnumPlayerModelParts[] PARTS_VERTICAL;
    private Random r;
    private int len;
    
    public SkinFlicker() {
        super("SkinFlicker", "Toggles skin layers", 0, Category.MISC, true);
        this.mode = this.register(new Value<String>("Mode", this, "Horizontal", new ArrayList<String>(Arrays.asList("Horizontal", "Vertical", "Random"))));
        this.slowness = this.register(new Value<Integer>("Slowness", this, 2, 1, 5));
        this.r = new Random();
        this.len = EnumPlayerModelParts.values().length;
    }
    
    @Override
    public void onUpdate() {
        if (this.mode.getValue().equalsIgnoreCase("Random")) {
            if (SkinFlicker.mc.player.ticksExisted % this.slowness.getValue() != 0) {
                return;
            }
            SkinFlicker.mc.gameSettings.switchModelPartEnabled(EnumPlayerModelParts.values()[this.r.nextInt(this.len)]);
        }
        else {
            int i = SkinFlicker.mc.player.ticksExisted / this.slowness.getValue() % (SkinFlicker.PARTS_HORIZONTAL.length * 2);
            boolean on = false;
            if (i >= SkinFlicker.PARTS_HORIZONTAL.length) {
                on = true;
                i -= SkinFlicker.PARTS_HORIZONTAL.length;
            }
            SkinFlicker.mc.gameSettings.setModelPartEnabled(this.mode.getValue().equalsIgnoreCase("Vertical") ? SkinFlicker.PARTS_VERTICAL[i] : SkinFlicker.PARTS_HORIZONTAL[i], on);
        }
    }
    
    static {
        PARTS_HORIZONTAL = new EnumPlayerModelParts[] { EnumPlayerModelParts.LEFT_SLEEVE, EnumPlayerModelParts.JACKET, EnumPlayerModelParts.HAT, EnumPlayerModelParts.LEFT_PANTS_LEG, EnumPlayerModelParts.RIGHT_PANTS_LEG, EnumPlayerModelParts.RIGHT_SLEEVE };
        PARTS_VERTICAL = new EnumPlayerModelParts[] { EnumPlayerModelParts.HAT, EnumPlayerModelParts.JACKET, EnumPlayerModelParts.LEFT_SLEEVE, EnumPlayerModelParts.RIGHT_SLEEVE, EnumPlayerModelParts.LEFT_PANTS_LEG, EnumPlayerModelParts.RIGHT_PANTS_LEG };
    }
}
