package me.offeex.bloomware.mixin.entity;

import com.mojang.authlib.GameProfile;
import me.offeex.bloomware.api.util.MathUtil;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
abstract class MixinAbstractPlayerEntity extends PlayerEntity {
    private final Identifier DEV_CAPE = new Identifier("bloomware", "/game/devcape.png");
    private final Identifier MAIN_CAPE = new Identifier("bloomware", "/game/maincape.png");

    public MixinAbstractPlayerEntity(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "getCapeTexture", at = @At("HEAD"), cancellable = true)
    public void getCapeTexture(CallbackInfoReturnable<Identifier> cir) {
        // TODO: Dev capes
        MathUtil.INSTANCE.protection();
//        Capes module = Capes.INSTANCE;
//        if (module.getEnabled()) {
//            if (module.containsUuid(this.getUuid()))
//                cir.setReturnValue(MAIN_CAPE);
//        }
    }
}