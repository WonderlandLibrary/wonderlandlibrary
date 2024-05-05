package net.minecraft.util;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.util.EnumFacing;

public static enum EnumFacing.Plane implements Predicate<EnumFacing>,
Iterable<EnumFacing>
{
    HORIZONTAL,
    VERTICAL;


    public EnumFacing[] facings() {
        switch (EnumFacing.1.$SwitchMap$net$minecraft$util$EnumFacing$Plane[this.ordinal()]) {
            case 1: {
                return new EnumFacing[]{EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST};
            }
            case 2: {
                return new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN};
            }
        }
        throw new Error("Someone's been tampering with the universe!");
    }

    public EnumFacing random(Random rand) {
        EnumFacing[] aenumfacing = this.facings();
        return aenumfacing[rand.nextInt(aenumfacing.length)];
    }

    public boolean apply(EnumFacing p_apply_1_) {
        return p_apply_1_ != null && p_apply_1_.getAxis().getPlane() == this;
    }

    public Iterator<EnumFacing> iterator() {
        return Iterators.forArray((Object[])this.facings());
    }
}
