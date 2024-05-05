package me.earth.earthhack.impl.util.minecraft;

import net.minecraft.util.EnumHand;

public enum Swing
{
    None
        {
            @Override
            public void swing(EnumHand hand)
            {
                /* Nothing */
            }
        },
    Packet
        {
            @Override
            public void swing(EnumHand hand)
            {
                ArmUtil.swingPacket(hand);
            }
        },
    Full
        {
            @Override
            public void swing(EnumHand hand)
            {
                ArmUtil.swingArm(hand);
            }
        },
    Client
        {
            @Override
            public void swing(EnumHand hand)
            {
                ArmUtil.swingArmNoPacket(hand);
            }
        };

    public abstract void swing(EnumHand hand);

}
