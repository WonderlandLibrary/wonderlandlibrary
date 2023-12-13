package me.dev.legacy.features.modules.movement;

import me.dev.legacy.features.Feature;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import me.dev.legacy.util.Util;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;

public class Rubberband extends Module
{
    private final Setting<RubbeMode> mode;
    private final Setting<Integer> Ym;

    public Rubberband() {
        super("Rubberband", "does rubberband", Category.MOVEMENT, true, false, false);
        this.mode = (Setting<RubbeMode>)this.register(new Setting("Mode", RubbeMode.Motion));
        this.Ym = (Setting<Integer>)this.register(new Setting("Motion", 5, 1, 15, v -> this.mode.getValue() == RubbeMode.Motion));
    }

    @Override
    public void onEnable() {
        if (Feature.fullNullCheck()) {
            return;
        }
    }

    @Override
    public void onUpdate() {
        switch (this.mode.getValue()) {
            case Motion: {
                Util.mc.player.motionY = this.Ym.getValue();
                break;
            }
            case Packet: {
                Rubberband.mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(Rubberband.mc.player.posX, Rubberband.mc.player.posY + this.Ym.getValue(), Rubberband.mc.player.posZ, true));
                break;
            }
            case Teleport: {
                Rubberband.mc.player.setPositionAndUpdate(Rubberband.mc.player.posX, Rubberband.mc.player.posY + this.Ym.getValue(), Rubberband.mc.player.posZ);
                break;
            }
        }
        this.toggle();
    }

    public enum RubbeMode
    {
        Motion,
        Teleport,
        Packet;
    }
}
