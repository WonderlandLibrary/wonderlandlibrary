package me.aquavit.liquidsense.value;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockValue extends IntegerValue {
    public BlockValue(String name, int value) {
        super(name, value, 1, 197);
    }
}
