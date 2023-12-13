// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.ai.attributes;

import org.apache.commons.lang3.Validate;
import java.util.Random;
import net.minecraft.util.MathHelper;
import io.netty.util.internal.ThreadLocalRandom;
import java.util.UUID;

public class AttributeModifier
{
    private final double amount;
    private final int operation;
    private final String name;
    private final UUID id;
    private boolean isSaved;
    private static final String __OBFID = "CL_00001564";
    
    public AttributeModifier(final String p_i1605_1_, final double p_i1605_2_, final int p_i1605_4_) {
        this(MathHelper.func_180182_a((Random)ThreadLocalRandom.current()), p_i1605_1_, p_i1605_2_, p_i1605_4_);
    }
    
    public AttributeModifier(final UUID p_i1606_1_, final String p_i1606_2_, final double p_i1606_3_, final int p_i1606_5_) {
        this.isSaved = true;
        this.id = p_i1606_1_;
        this.name = p_i1606_2_;
        this.amount = p_i1606_3_;
        this.operation = p_i1606_5_;
        Validate.notEmpty((CharSequence)p_i1606_2_, "Modifier name cannot be empty", new Object[0]);
        Validate.inclusiveBetween(0L, 2L, (long)p_i1606_5_, "Invalid operation");
    }
    
    public UUID getID() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getOperation() {
        return this.operation;
    }
    
    public double getAmount() {
        return this.amount;
    }
    
    public boolean isSaved() {
        return this.isSaved;
    }
    
    public AttributeModifier setSaved(final boolean p_111168_1_) {
        this.isSaved = p_111168_1_;
        return this;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
            final AttributeModifier var2 = (AttributeModifier)p_equals_1_;
            if (this.id != null) {
                if (!this.id.equals(var2.id)) {
                    return false;
                }
            }
            else if (var2.id != null) {
                return false;
            }
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return (this.id != null) ? this.id.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "AttributeModifier{amount=" + this.amount + ", operation=" + this.operation + ", name='" + this.name + '\'' + ", id=" + this.id + ", serialize=" + this.isSaved + '}';
    }
}
