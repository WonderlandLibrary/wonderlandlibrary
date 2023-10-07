package sudo.mixins;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.network.ClientPlayerEntity;

import sudo.core.event.Event;
import sudo.events.EventMotionUpdate;
import sudo.module.Mod;
import sudo.module.ModuleManager;
import sudo.module.exploit.NoLevitation;
import sudo.module.exploit.PortalGui;
import sudo.module.movement.NoSlow;
import sudo.module.render.NoRender;
import sudo.utils.player.RotationUtils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.ClientPlayerTickable;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.encryption.PlayerPublicKey;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import com.mojang.authlib.GameProfile;

import net.minecraft.entity.MovementType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;

import org.jetbrains.annotations.Nullable;

@Mixin({ ClientPlayerEntity.class })
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity
{
    @Unique private boolean lastSneaking;
    @Unique private boolean lastSprinting;
    @Unique private double lastX;
    @Unique private double lastBaseY;
    @Unique private double lastZ;
    @Unique private float lastYaw;
    @Unique private float lastPitch;
    @Unique private boolean lastOnGround;
    @Unique private int ticksSinceLastPositionPacketSent;
    @Shadow private boolean autoJumpEnabled;
    @Shadow @Final private List<ClientPlayerTickable> tickables;
    @Shadow protected abstract boolean isCamera();
    
    public ClientPlayerEntityMixin(final ClientWorld world, final GameProfile profile, @Nullable final PlayerPublicKey publicKey) {
        super(world, profile, publicKey);
        this.autoJumpEnabled = true;
    }
    
    @Inject(method = { "move" }, at = { @At("HEAD") }, cancellable = true)
    public void onMotion(final MovementType type, final Vec3d movement, final CallbackInfo ci) {
        for (final Mod mod : ModuleManager.INSTANCE.getEnabledModules()) {
            mod.onMotion();
        }
    }
    
    @Redirect(method = "updateNausea", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;closeHandledScreen()V", ordinal = 0), require = 0)
	private void updateNausea_closeHandledScreen(ClientPlayerEntity player) {
		if (!ModuleManager.INSTANCE.getModule(PortalGui.class).isEnabled()) {
			closeHandledScreen();
		}
	}

	@Redirect(method = "updateNausea", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V", ordinal = 0), require = 0)
	private void updateNausea_setScreen(MinecraftClient client, Screen screen) {
		if (!ModuleManager.INSTANCE.getModule(PortalGui.class).isEnabled()) {
			client.setScreen(screen);
		}
	}

    @SuppressWarnings("resource")
    @Inject(method = { "sendMovementPackets" }, at = { @At("HEAD") }, cancellable = true)
    private void sendMovementPackets(final CallbackInfo ci) {
        this.sendMovementPacketsWithEvent();
		final EventMotionUpdate event = new EventMotionUpdate(MinecraftClient.getInstance().player.getX(), MinecraftClient.getInstance().player.getY(), MinecraftClient.getInstance().player.getZ(), MinecraftClient.getInstance().player.getYaw(), MinecraftClient.getInstance().player.getPitch(), this.lastYaw, this.lastPitch, MinecraftClient.getInstance().player.isOnGround(), this.isSneaking(), Event.State.POST);
        event.call();
        ci.cancel();
    }
    
    @SuppressWarnings({ "rawtypes", "resource" })
	private void sendMovementPacketsWithEvent() {
        final EventMotionUpdate event = new EventMotionUpdate(MinecraftClient.getInstance().player.getX(), MinecraftClient.getInstance().player.getY(), MinecraftClient.getInstance().player.getZ(), MinecraftClient.getInstance().player.getYaw(), MinecraftClient.getInstance().player.getPitch(), this.lastYaw, this.lastPitch, MinecraftClient.getInstance().player.isOnGround(), this.isSneaking(), Event.State.PRE);
        event.call();
        final boolean bl = this.isSprinting();
        if (bl != this.lastSprinting) {
            final ClientCommandC2SPacket.Mode mode = bl ? ClientCommandC2SPacket.Mode.START_SPRINTING : ClientCommandC2SPacket.Mode.STOP_SPRINTING;
            MinecraftClient.getInstance().player.networkHandler.sendPacket((Packet)new ClientCommandC2SPacket((Entity)this, mode));
            this.lastSprinting = bl;
        }
        final boolean bl2 = this.isSneaking();
        if (bl2 != this.lastSneaking) {
            final ClientCommandC2SPacket.Mode mode2 = bl2 ? ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY : ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY;
            MinecraftClient.getInstance().player.networkHandler.sendPacket((Packet)new ClientCommandC2SPacket((Entity)this, mode2));
            this.lastSneaking = bl2;
        }
        if (this.isCamera()) {
            final double d = event.getX() - this.lastX;
            final double e = this.getY() - this.lastBaseY;
            final double f = event.getZ() - this.lastZ;
            final double g = event.getYaw() - this.lastYaw;
            final double h = event.getPitch() - this.lastPitch;
            ++this.ticksSinceLastPositionPacketSent;
            boolean bl3 = d * d + e * e + f * f > 9.0E-4 || this.ticksSinceLastPositionPacketSent >= 20;
            final boolean bl4 = g != 0.0 || h != 0.0;
            if (this.hasVehicle()) {
                final Vec3d vec3d = this.getVelocity();
                MinecraftClient.getInstance().player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.Full(vec3d.x, -999.0, vec3d.z, this.getYaw(), this.getPitch(), this.onGround));
                bl3 = false;
            }
            else if (bl3 && bl4) {
                MinecraftClient.getInstance().player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.Full(event.getX(), this.getY(), event.getZ(), event.getYaw(), event.getPitch(), event.isOnGround()));
            }
            else if (bl3) {
                MinecraftClient.getInstance().player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(event.getX(), this.getY(), event.getZ(), event.isOnGround()));
            }
            else if (bl4) {
                MinecraftClient.getInstance().player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.LookAndOnGround(event.getYaw(), event.getPitch(), event.isOnGround()));
            }
            else if (this.lastOnGround != this.onGround) {
                MinecraftClient.getInstance().player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.OnGroundOnly(event.isOnGround()));
            }
            if (bl3) {
                this.lastX = event.getX();
                this.lastBaseY = this.getY();
                this.lastZ = event.getZ();
                this.ticksSinceLastPositionPacketSent = 0;
            }
            if (bl4) {
                this.lastYaw = event.getYaw();
                this.lastPitch = event.getPitch();
            }
            this.lastOnGround = event.isOnGround();
            this.autoJumpEnabled = (boolean)MinecraftClient.getInstance().options.getAutoJump().getValue();
        }
        
    	RotationUtils.resetPitch();
		RotationUtils.resetYaw();
        
    }
    
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z", ordinal = 0), method = "tickMovement()V")
	private boolean SudoIsUsingItem(ClientPlayerEntity player) {
		if(ModuleManager.INSTANCE.getModule(NoSlow.class).isEnabled()) return false;
		return player.isUsingItem();
	}
	private static NoRender noRenderModule = ModuleManager.INSTANCE.getModule(NoRender.class);

	@Override
	public boolean hasStatusEffect(StatusEffect effect) {
		if(effect == StatusEffects.LEVITATION && ModuleManager.INSTANCE.getModule(NoLevitation.class).isEnabled()) return false;
		if(effect == StatusEffects.NAUSEA && noRenderModule.isEnabled() && noRenderModule.nausea.isEnabled()) return false;
		if(effect == StatusEffects.BLINDNESS && noRenderModule.isEnabled() && noRenderModule.blindness.isEnabled()) return false;
		return super.hasStatusEffect(effect);
	}
}