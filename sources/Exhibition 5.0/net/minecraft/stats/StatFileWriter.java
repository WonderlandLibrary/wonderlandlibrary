// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.stats;

import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.TupleIntJsonSerializable;
import net.minecraft.entity.player.EntityPlayer;
import com.google.common.collect.Maps;
import java.util.Map;

public class StatFileWriter
{
    protected final Map field_150875_a;
    private static final String __OBFID = "CL_00001481";
    
    public StatFileWriter() {
        this.field_150875_a = Maps.newConcurrentMap();
    }
    
    public boolean hasAchievementUnlocked(final Achievement p_77443_1_) {
        return this.writeStat(p_77443_1_) > 0;
    }
    
    public boolean canUnlockAchievement(final Achievement p_77442_1_) {
        return p_77442_1_.parentAchievement == null || this.hasAchievementUnlocked(p_77442_1_.parentAchievement);
    }
    
    public int func_150874_c(final Achievement p_150874_1_) {
        if (this.hasAchievementUnlocked(p_150874_1_)) {
            return 0;
        }
        int var2 = 0;
        for (Achievement var3 = p_150874_1_.parentAchievement; var3 != null && !this.hasAchievementUnlocked(var3); var3 = var3.parentAchievement, ++var2) {}
        return var2;
    }
    
    public void func_150871_b(final EntityPlayer p_150871_1_, final StatBase p_150871_2_, final int p_150871_3_) {
        if (!p_150871_2_.isAchievement() || this.canUnlockAchievement((Achievement)p_150871_2_)) {
            this.func_150873_a(p_150871_1_, p_150871_2_, this.writeStat(p_150871_2_) + p_150871_3_);
        }
    }
    
    public void func_150873_a(final EntityPlayer p_150873_1_, final StatBase p_150873_2_, final int p_150873_3_) {
        TupleIntJsonSerializable var4 = this.field_150875_a.get(p_150873_2_);
        if (var4 == null) {
            var4 = new TupleIntJsonSerializable();
            this.field_150875_a.put(p_150873_2_, var4);
        }
        var4.setIntegerValue(p_150873_3_);
    }
    
    public int writeStat(final StatBase p_77444_1_) {
        final TupleIntJsonSerializable var2 = this.field_150875_a.get(p_77444_1_);
        return (var2 == null) ? 0 : var2.getIntegerValue();
    }
    
    public IJsonSerializable func_150870_b(final StatBase p_150870_1_) {
        final TupleIntJsonSerializable var2 = this.field_150875_a.get(p_150870_1_);
        return (var2 != null) ? var2.getJsonSerializableValue() : null;
    }
    
    public IJsonSerializable func_150872_a(final StatBase p_150872_1_, final IJsonSerializable p_150872_2_) {
        TupleIntJsonSerializable var3 = this.field_150875_a.get(p_150872_1_);
        if (var3 == null) {
            var3 = new TupleIntJsonSerializable();
            this.field_150875_a.put(p_150872_1_, var3);
        }
        var3.setJsonSerializableValue(p_150872_2_);
        return p_150872_2_;
    }
}
