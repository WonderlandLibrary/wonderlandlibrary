package me.pianopenguin471.mixins;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.weavemc.loader.api.event.EventBus;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import ravenweave.client.event.ext.EventTiming;
import ravenweave.client.event.impl.LivingUpdateEvent;
import ravenweave.client.event.impl.UpdateEvent;
import ravenweave.client.main.Raven;
import ravenweave.client.module.Module;
import ravenweave.client.module.modules.movement.NoSlow;
import ravenweave.client.module.modules.movement.Sprint;

@Mixin(priority = 995, value = EntityPlayerSP.class)
public abstract class EntityPlayerSPMixin extends AbstractClientPlayer {

    @Shadow
    public int sprintingTicksLeft;

    public EntityPlayerSPMixin(World p_i45074_1_, GameProfile p_i45074_2_) {
        super(p_i45074_1_, p_i45074_2_);
    }

    @Override
	@Shadow
    public abstract void setSprinting(boolean p_setSprinting_1_);

    @Shadow
    public int sprintToggleTimer;
    @Shadow
    public float prevTimeInPortal;
    @Shadow
    public float timeInPortal;
    @Shadow
    public Minecraft mc;
    @Shadow
    public MovementInput movementInput;

    @Override
	@Shadow
    public abstract boolean pushOutOfBlocks(double p_pushOutOfBlocks_1_, double p_pushOutOfBlocks_3_,
                                            double p_pushOutOfBlocks_5_);

    @Override
	@Shadow
    public abstract void sendPlayerAbilities();

    @Shadow
    public abstract boolean isCurrentViewEntity();

    @Shadow
    public abstract boolean isRidingHorse();

    @Shadow
    public int horseJumpPowerCounter;
    @Shadow
    public float horseJumpPower;

    @Shadow
    public abstract void sendHorseJump();

    @Shadow
    public boolean serverSprintState;
    @Shadow
    @Final
    public NetHandlerPlayClient sendQueue;

    @Override
	@Shadow
    public abstract boolean isSneaking();

    @Shadow
    public boolean serverSneakState;
    @Shadow
    public double lastReportedPosX;
    @Shadow
    public double lastReportedPosY;
    @Shadow
    public double lastReportedPosZ;
    @Shadow
    public float lastReportedYaw;
    @Shadow
    public float lastReportedPitch;
    @Shadow
    public int positionUpdateTicks;

    /**
     * @author mc code
     * @reason Update event
     */
    @Overwrite
    public void onUpdateWalkingPlayer() {
        boolean flag = this.isSprinting();
        if (flag != this.serverSprintState) {
            if (flag)
				this.sendQueue
                        .addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SPRINTING));
			else
				this.sendQueue
                        .addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SPRINTING));

            this.serverSprintState = flag;
        }

        boolean flag1 = this.isSneaking();
        if (flag1 != this.serverSneakState) {
            if (flag1)
				this.sendQueue
                        .addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SNEAKING));
			else
				this.sendQueue
                        .addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SNEAKING));

            this.serverSneakState = flag1;
        }

        if (this.isCurrentViewEntity()) {

            UpdateEvent e = new UpdateEvent(EventTiming.PRE, this.posX, this.getEntityBoundingBox().minY, this.posZ,
                    this.rotationYaw, this.rotationPitch, this.onGround);
            EventBus.callEvent(e);

            double d0 = e.getX() - this.lastReportedPosX;
            double d1 = e.getY() - this.lastReportedPosY;
            double d2 = e.getZ() - this.lastReportedPosZ;
            double d3 = e.getYaw() - this.lastReportedYaw;
            double d4 = e.getPitch() - this.lastReportedPitch;
            boolean flag2 = (((d0 * d0) + (d1 * d1) + (d2 * d2)) > 9.0E-4D) || (this.positionUpdateTicks >= 20);
            boolean flag3 = (d3 != 0.0D) || (d4 != 0.0D);
            if (this.ridingEntity == null) {
                if (flag2 && flag3)
					this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(e.getX(), e.getY(),
                            e.getZ(), e.getYaw(), e.getPitch(), e.isOnGround()));
				else if (flag2)
					this.sendQueue.addToSendQueue(
                            new C03PacketPlayer.C04PacketPlayerPosition(e.getX(), e.getY(), e.getZ(), e.isOnGround()));
				else if (flag3)
					this.sendQueue.addToSendQueue(
                            new C03PacketPlayer.C05PacketPlayerLook(e.getYaw(), e.getPitch(), e.isOnGround()));
				else
					this.sendQueue.addToSendQueue(new C03PacketPlayer(e.isOnGround()));
            } else {
                this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.motionX, -999.0D,
                        this.motionZ, e.getYaw(), e.getPitch(), e.isOnGround()));
                flag2 = false;
            }

            ++this.positionUpdateTicks;
            if (flag2) {
                this.lastReportedPosX = e.getX();
                this.lastReportedPosY = e.getY();
                this.lastReportedPosZ = e.getZ();
                this.positionUpdateTicks = 0;
            }

            if (flag3) {
                this.lastReportedYaw = e.getYaw();
                this.lastReportedPitch = e.getPitch();
            }

            EventBus.callEvent(e);

        }

    }

    /**
     * @author mc code
     * @reason no slow / sprint
     */
    @Override
	@Overwrite
    public void onLivingUpdate() {
        if (this.sprintingTicksLeft > 0) {
            --this.sprintingTicksLeft;
            if (this.sprintingTicksLeft == 0)
				this.setSprinting(false);
        }

        if (this.sprintToggleTimer > 0)
			--this.sprintToggleTimer;

        this.prevTimeInPortal = this.timeInPortal;
        if (this.inPortal) {
            if ((this.mc.currentScreen != null) && !this.mc.currentScreen.doesGuiPauseGame())
				this.mc.displayGuiScreen(null);

            if (this.timeInPortal == 0.0F)
				this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("portal.trigger"),
                        (this.rand.nextFloat() * 0.4F) + 0.8F));

            this.timeInPortal += 0.0125F;
            if (this.timeInPortal >= 1.0F)
				this.timeInPortal = 1.0F;

            this.inPortal = false;
        } else if (this.isPotionActive(Potion.confusion)
                && (this.getActivePotionEffect(Potion.confusion).getDuration() > 60)) {
            this.timeInPortal += 0.006666667F;
            if (this.timeInPortal > 1.0F)
				this.timeInPortal = 1.0F;
        } else {
            if (this.timeInPortal > 0.0F)
				this.timeInPortal -= 0.05F;

            if (this.timeInPortal < 0.0F)
				this.timeInPortal = 0.0F;
        }

        if (this.timeUntilPortal > 0)
			--this.timeUntilPortal;

        Module noSlow = Raven.moduleManager.getModuleByClazz(NoSlow.class);
        Module sprint = Raven.moduleManager.getModuleByClazz(Sprint.class);

        boolean flag = this.movementInput.jump;
        boolean flag1 = this.movementInput.sneak;
        float f = 0.8F;
        boolean flag2 = this.movementInput.moveForward >= f;
        this.movementInput.updatePlayerMoveState();
        if (this.isUsingItem() && !this.isRiding()) {

            MovementInput var10000 = this.movementInput;

            if (noSlow.isEnabled()) {
                float slowdown = (float) ((100 - NoSlow.speed.getInput()) / 100F);
                var10000.moveStrafe *= slowdown;
                var10000.moveForward *= slowdown;
            } else {
                var10000.moveStrafe *= 0.2F;
                var10000.moveForward *= 0.2F;
                this.sprintToggleTimer = 0;
            }
        }

        this.pushOutOfBlocks(this.posX - ((double) this.width * 0.35D), this.getEntityBoundingBox().minY + 0.5D,
                this.posZ + ((double) this.width * 0.35D));
        this.pushOutOfBlocks(this.posX - ((double) this.width * 0.35D), this.getEntityBoundingBox().minY + 0.5D,
                this.posZ - ((double) this.width * 0.35D));
        this.pushOutOfBlocks(this.posX + ((double) this.width * 0.35D), this.getEntityBoundingBox().minY + 0.5D,
                this.posZ - ((double) this.width * 0.35D));
        this.pushOutOfBlocks(this.posX + ((double) this.width * 0.35D), this.getEntityBoundingBox().minY + 0.5D,
                this.posZ + ((double) this.width * 0.35D));
        boolean flag3 = ((float) this.getFoodStats().getFoodLevel() > 6.0F) || this.capabilities.allowFlying;
        if (this.onGround && !flag1 && !flag2
                && ((this.movementInput.moveForward >= f) || (sprint.isEnabled() && Sprint.multiDir.isToggled()
                        && ((movementInput.moveForward != 0) || (movementInput.moveStrafe != 0))))
                && !this.isSprinting() && flag3 && (!this.isUsingItem() || noSlow.isEnabled())
                && (!this.isPotionActive(Potion.blindness)
                        || (sprint.isEnabled() && Sprint.ignoreBlindness.isToggled())))
			if ((this.sprintToggleTimer <= 0) && !this.mc.gameSettings.keyBindSprint.isKeyDown())
				this.sprintToggleTimer = 7;
			else
				this.setSprinting(true);

        if (!this.isSprinting()
                && ((this.movementInput.moveForward >= f) || (sprint.isEnabled() && Sprint.multiDir.isToggled()
                        && ((movementInput.moveForward != 0) || (movementInput.moveStrafe != 0))))
                && flag3 && (!this.isUsingItem() || noSlow.isEnabled())
                && (!this.isPotionActive(Potion.blindness)
                        || (sprint.isEnabled() && Sprint.ignoreBlindness.isToggled()))
                && this.mc.gameSettings.keyBindSprint.isKeyDown())
			this.setSprinting(true);

        if (this.isSprinting() && (((sprint.isEnabled() && Sprint.multiDir.isToggled())
                ? !((movementInput.moveForward != 0) || (movementInput.moveStrafe != 0))
                : this.movementInput.moveForward < f) || this.isCollidedHorizontally || !flag3))
			this.setSprinting(false);

        if (this.capabilities.allowFlying)
			if (this.mc.playerController.isSpectatorMode()) {
                if (!this.capabilities.isFlying) {
                    this.capabilities.isFlying = true;
                    this.sendPlayerAbilities();
                }
            } else if (!flag && this.movementInput.jump)
				if (this.flyToggleTimer == 0)
					this.flyToggleTimer = 7;
				else {
                    this.capabilities.isFlying = !this.capabilities.isFlying;
                    this.sendPlayerAbilities();
                    this.flyToggleTimer = 0;
                }

        if (this.capabilities.isFlying && this.isCurrentViewEntity()) {
            if (this.movementInput.sneak)
				this.motionY -= this.capabilities.getFlySpeed() * 3.0F;

            if (this.movementInput.jump)
				this.motionY += this.capabilities.getFlySpeed() * 3.0F;
        }

        if (this.isRidingHorse()) {
            if (this.horseJumpPowerCounter < 0) {
                ++this.horseJumpPowerCounter;
                if (this.horseJumpPowerCounter == 0)
					this.horseJumpPower = 0.0F;
            }

            if (flag && !this.movementInput.jump) {
                this.horseJumpPowerCounter = -10;
                this.sendHorseJump();
            } else if (!flag && this.movementInput.jump) {
                this.horseJumpPowerCounter = 0;
                this.horseJumpPower = 0.0F;
            } else if (flag) {
                ++this.horseJumpPowerCounter;
                if (this.horseJumpPowerCounter < 10)
					this.horseJumpPower = (float) this.horseJumpPowerCounter * 0.1F;
				else
					this.horseJumpPower = 0.8F + ((2.0F / (float) (this.horseJumpPowerCounter - 9)) * 0.1F);
            }
        } else
			this.horseJumpPower = 0.0F;

        super.onLivingUpdate();
        if (this.onGround && this.capabilities.isFlying && !this.mc.playerController.isSpectatorMode()) {
            this.capabilities.isFlying = false;
            this.sendPlayerAbilities();
        }

    }

    @Redirect(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;setSprinting(Z)V", ordinal = 2))
    public void onLivingUpdate(EntityPlayerSP entityPlayerSP, boolean sprinting) {
        LivingUpdateEvent event = new LivingUpdateEvent(entityPlayerSP, sprinting);
        EventBus.callEvent(event);

        if (event.isCancelled())
            event.getEntity().setSprinting(true);
        else
            entityPlayerSP.setSprinting(sprinting);
    }
}
