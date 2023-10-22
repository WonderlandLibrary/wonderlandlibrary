package cn.hanabi.injection.mixins;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(
   targets = {"net.minecraft.network.NetworkManager$5"}
)
public abstract class MixinNetworkManagerChInit {
}
