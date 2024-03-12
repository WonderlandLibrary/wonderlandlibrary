// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.util;

import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.optifine.reflect.Reflector;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.src.Config;
import net.minecraft.world.IWorldNameable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

public class TileEntityUtils
{
    public static String getTileEntityName(final IBlockAccess blockAccess, final BlockPos blockPos) {
        final TileEntity tileentity = blockAccess.getTileEntity(blockPos);
        return getTileEntityName(tileentity);
    }
    
    public static String getTileEntityName(final TileEntity te) {
        if (!(te instanceof IWorldNameable)) {
            return null;
        }
        final IWorldNameable iworldnameable = (IWorldNameable)te;
        updateTileEntityName(te);
        return iworldnameable.hasCustomName() ? iworldnameable.getName() : null;
    }
    
    public static void updateTileEntityName(final TileEntity te) {
        final BlockPos blockpos = te.getPos();
        final String s = getTileEntityRawName(te);
        if (s == null) {
            String s2 = getServerTileEntityRawName(blockpos);
            s2 = Config.normalize(s2);
            setTileEntityRawName(te, s2);
        }
    }
    
    public static String getServerTileEntityRawName(final BlockPos blockPos) {
        final TileEntity tileentity = IntegratedServerUtils.getTileEntity(blockPos);
        return (tileentity == null) ? null : getTileEntityRawName(tileentity);
    }
    
    public static String getTileEntityRawName(final TileEntity te) {
        if (te instanceof TileEntityBeacon) {
            return (String)Reflector.getFieldValue(te, Reflector.TileEntityBeacon_customName);
        }
        if (te instanceof TileEntityBrewingStand) {
            return (String)Reflector.getFieldValue(te, Reflector.TileEntityBrewingStand_customName);
        }
        if (te instanceof TileEntityEnchantmentTable) {
            return (String)Reflector.getFieldValue(te, Reflector.TileEntityEnchantmentTable_customName);
        }
        if (te instanceof TileEntityFurnace) {
            return (String)Reflector.getFieldValue(te, Reflector.TileEntityFurnace_customName);
        }
        if (te instanceof IWorldNameable) {
            final IWorldNameable iworldnameable = (IWorldNameable)te;
            if (iworldnameable.hasCustomName()) {
                return iworldnameable.getName();
            }
        }
        return null;
    }
    
    public static boolean setTileEntityRawName(final TileEntity te, final String name) {
        if (te instanceof TileEntityBeacon) {
            return Reflector.setFieldValue(te, Reflector.TileEntityBeacon_customName, name);
        }
        if (te instanceof TileEntityBrewingStand) {
            return Reflector.setFieldValue(te, Reflector.TileEntityBrewingStand_customName, name);
        }
        if (te instanceof TileEntityEnchantmentTable) {
            return Reflector.setFieldValue(te, Reflector.TileEntityEnchantmentTable_customName, name);
        }
        if (te instanceof TileEntityFurnace) {
            return Reflector.setFieldValue(te, Reflector.TileEntityFurnace_customName, name);
        }
        if (te instanceof TileEntityChest) {
            ((TileEntityChest)te).setCustomName(name);
            return true;
        }
        if (te instanceof TileEntityDispenser) {
            ((TileEntityDispenser)te).setCustomName(name);
            return true;
        }
        if (te instanceof TileEntityHopper) {
            ((TileEntityHopper)te).setCustomName(name);
            return true;
        }
        return false;
    }
}
