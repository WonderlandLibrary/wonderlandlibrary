/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityTNTPrimed
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

@ModuleInfo(name="TNTBlock", description="Automatically blocks with your sword when TNT around you explodes.", category=ModuleCategory.COMBAT)
public class TNTBlock
extends Module {
    private final IntegerValue fuseValue = new IntegerValue("Fuse", 10, 0, 80);
    private final FloatValue rangeValue = new FloatValue("Range", 9.0f, 1.0f, 20.0f);
    private final BoolValue autoSwordValue = new BoolValue("AutoSword", true);
    private boolean blocked;

    @EventTarget
    public void onMotionUpdate(MotionEvent event) {
        for (Entity entity : TNTBlock.mc.field_71441_e.field_72996_f) {
            if (!(entity instanceof EntityTNTPrimed) || !(TNTBlock.mc.field_71439_g.func_70032_d(entity) <= ((Float)this.rangeValue.get()).floatValue())) continue;
            EntityTNTPrimed tntPrimed = (EntityTNTPrimed)entity;
            if (tntPrimed.field_70516_a > (Integer)this.fuseValue.get()) continue;
            if (((Boolean)this.autoSwordValue.get()).booleanValue()) {
                int slot = -1;
                float bestDamage = 1.0f;
                for (int i = 0; i < 9; ++i) {
                    float itemDamage;
                    ItemStack itemStack = TNTBlock.mc.field_71439_g.field_71071_by.func_70301_a(i);
                    if (itemStack == null || !(itemStack.func_77973_b() instanceof ItemSword) || !((itemDamage = ((ItemSword)itemStack.func_77973_b()).func_150931_i() + 4.0f) > bestDamage)) continue;
                    bestDamage = itemDamage;
                    slot = i;
                }
                if (slot != -1 && slot != TNTBlock.mc.field_71439_g.field_71071_by.field_70461_c) {
                    TNTBlock.mc.field_71439_g.field_71071_by.field_70461_c = slot;
                    TNTBlock.mc.field_71442_b.func_78765_e();
                }
            }
            if (TNTBlock.mc.field_71439_g.func_70694_bm() != null && TNTBlock.mc.field_71439_g.func_70694_bm().func_77973_b() instanceof ItemSword) {
                TNTBlock.mc.field_71474_y.field_74313_G.field_74513_e = true;
                this.blocked = true;
            }
            return;
        }
        if (this.blocked && !GameSettings.func_100015_a((KeyBinding)TNTBlock.mc.field_71474_y.field_74313_G)) {
            TNTBlock.mc.field_71474_y.field_74313_G.field_74513_e = false;
            this.blocked = false;
        }
    }
}

