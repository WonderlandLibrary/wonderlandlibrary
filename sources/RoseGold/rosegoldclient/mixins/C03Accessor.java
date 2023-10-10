package rosegoldclient.mixins;

import net.minecraft.network.play.client.CPacketPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CPacketPlayer.class)
public interface C03Accessor {
    @Accessor(value = "x")
    void setX(double var1);

    @Accessor(value = "y")
    void setY(double var1);

    @Accessor(value = "z")
    void setZ(double var1);

    @Accessor
    void setYaw(float var1);

    @Accessor
    void setPitch(float var1);

    @Accessor
    void setOnGround(boolean var1);

}
