package net.minecraft.block;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockPressurePlate
extends BlockBasePressurePlate {
    public static final PropertyBool POWERED = PropertyBool.create((String)"powered");
    private final Sensitivity sensitivity;

    protected BlockPressurePlate(Material materialIn, Sensitivity sensitivityIn) {
        super(materialIn);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf((boolean)false)));
        this.sensitivity = sensitivityIn;
    }

    protected int getRedstoneStrength(IBlockState state) {
        return (Boolean)state.getValue((IProperty)POWERED) != false ? 15 : 0;
    }

    protected IBlockState setRedstoneStrength(IBlockState state, int strength) {
        return state.withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf((strength > 0 ? 1 : 0) != 0));
    }

    protected int computeRedstoneStrength(World worldIn, BlockPos pos) {
        List list;
        AxisAlignedBB axisalignedbb = this.getSensitiveAABB(pos);
        switch (1.$SwitchMap$net$minecraft$block$BlockPressurePlate$Sensitivity[this.sensitivity.ordinal()]) {
            case 1: {
                list = worldIn.getEntitiesWithinAABBExcludingEntity((Entity)null, axisalignedbb);
                break;
            }
            case 2: {
                list = worldIn.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
                break;
            }
            default: {
                return 0;
            }
        }
        if (!list.isEmpty()) {
            for (Entity entity : list) {
                if (entity.doesEntityNotTriggerPressurePlate()) continue;
                return 15;
            }
        }
        return 0;
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf((meta == 1 ? 1 : 0) != 0));
    }

    public int getMetaFromState(IBlockState state) {
        return (Boolean)state.getValue((IProperty)POWERED) != false ? 1 : 0;
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{POWERED});
    }
}
