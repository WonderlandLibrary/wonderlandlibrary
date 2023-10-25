package ru.salam4ik.bot.bot.entity;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerAbilities;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketRecipeInfo;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.MovementInput;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IInteractionObject;
import ru.salam4ik.bot.bot.network.BotPlayClient;
import wtf.evolution.Main;
import wtf.evolution.helpers.animation.Counter;
import wtf.evolution.notifications.NotificationType;

public class BotPlayer
        extends AbstractClientPlayer {
    private float lastReportedYaw;
    private float lastReportedPitch;
    private float horseJumpPower;
    public float renderArmPitch;
    private int permissionLevel;
    private String serverBrand;
    public float prevTimeInPortal;
    private int horseJumpPowerCounter;
    private RecipeBook field_192036_cb;
    private boolean serverSneakState;
    private double lastReportedPosY;
    public int sprintingTicksLeft;
    public float renderArmYaw;
    private boolean prevOnGround;
    public float timeInPortal;
    private boolean handActive;
    private EnumHand activeHand;
    private double lastReportedPosZ;
    private StatisticsManager statWriter;
    public Counter c;
    private double lastReportedPosX;
    private boolean rowingBoat;
    private boolean autoJumpEnabled = true;
    public String currentContainerName;
    public float prevRenderArmPitch;
    public final BotPlayClient connection;
    public float prevRenderArmYaw;
    public MovementInput movementInput;
    private int autoJumpTime;
    private boolean hasValidHealth;
    protected int sprintToggleTimer;
    private boolean wasFallFlying;
    private boolean serverSprintState;
    private int positionUpdateTicks;

    @Override
    public void openBook(ItemStack itemStack, EnumHand enumHand) {
    }

    @Override
    public boolean isSneaking() {
        boolean bl = this.movementInput != null && this.movementInput.sneak;
        return bl && !this.sleeping;
    }

    public float getHorseJumpPower() {
        return this.horseJumpPower;
    }

    @Override
    public void displayVillagerTradeGui(IMerchant iMerchant) {
    }

    @Override
    public void playSound(SoundEvent soundEvent, float f, float f2) {
        this.world.playSound(this.posX, this.posY, this.posZ, soundEvent, this.getSoundCategory(), f, f2, false);
    }

    @Override
    public void onEnchantmentCritical(Entity entity) {
    }

    @Override
    public Vec3d getLook(float f) {
        return this.getVectorForRotation(this.rotationPitch, this.rotationYaw);
    }

    public void setXPStats(float f, int n, int n2) {
        this.experience = f;
        this.experienceTotal = n;
        this.experienceLevel = n2;
    }

    public void sendHorseInventory() {
        this.connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.OPEN_INVENTORY));
    }

    @Override
    public void handleStatusUpdate(byte by) {
        if (by >= 24 && by <= 28) {
            this.setPermissionLevel(by - 24);
        } else {
            super.handleStatusUpdate(by);
        }
    }

    public int getPermissionLevel() {
        return this.permissionLevel;
    }

    @Override
    public void displayGuiCommandBlock(TileEntityCommandBlock tileEntityCommandBlock) {
    }

    @Override
    public void swingArm(EnumHand enumHand) {
        super.swingArm(enumHand);
        this.connection.sendPacket(new CPacketAnimation(enumHand));
    }

    public String getServerBrand() {
        return this.serverBrand;
    }

    public void openHorseInventory(AbstractHorse abstractHorse, IInventory iInventory) {
    }

    public StatisticsManager getStatFileWriter() {
        return this.statWriter;
    }

    @Override
    public void move(MoverType moverType, double d, double d2, double d3) {
        double d4 = this.posX;
        double d5 = this.posZ;
        super.move(moverType, d, d2, d3);
        this.updateAutoJump((float)(this.posX - d4), (float)(this.posZ - d5));
    }



    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float f) {
        return false;
    }


    @Override
    public EnumHand getActiveHand() {
        return this.activeHand;
    }




    @Override
    public void closeScreen() {
        this.connection.sendPacket(new CPacketCloseWindow(this.openContainer.windowId));
        this.closeScreenAndDropStack();
    }

    @Override
    public boolean startRiding(Entity entity, boolean bl) {
        if (!super.startRiding(entity, bl)) {
            return false;
        }
        if (entity instanceof EntityMinecart) {
            // empty if block
        }
        if (entity instanceof EntityBoat) {
            this.prevRotationYaw = entity.rotationYaw;
            this.rotationYaw = entity.rotationYaw;
            this.setRotationYawHead(entity.rotationYaw);
        }
        return true;
    }

    @Override
    public void setSprinting(boolean bl) {
        super.setSprinting(bl);
        this.sprintingTicksLeft = 0;
    }

    @Override
    public void displayGUIChest(IInventory iInventory) {
        String string;
        String string2 = string = this.inventory instanceof IInteractionObject ? ((IInteractionObject)((Object)this.inventory)).getGuiID() : "minecraft:container";
        if ("minecraft:chest".equals(string)) {
            this.openContainer = new ContainerChest(this.inventory, this.inventory, this);
        } else if ("minecraft:hopper".equals(string)) {
            this.openContainer = new ContainerHopper(this.inventory, this.inventory, this);
        } else if ("minecraft:furnace".equals(string)) {
            this.openContainer = new ContainerFurnace(this.inventory, this.inventory);
        } else if ("minecraft:brewing_stand".equals(string)) {
            this.openContainer = new ContainerBrewingStand(this.inventory, this.inventory);
        } else if ("minecraft:beacon".equals(string)) {
            this.openContainer = new ContainerBeacon(this.inventory, this.inventory);
        } else if (!"minecraft:dispenser".equals(string) && !"minecraft:dropper".equals(string)) {
            if ("minecraft:shulker_box".equals(string)) {
                this.openContainer = new ContainerShulkerBox(this.inventory, this.inventory, this);
            }
        } else {
            this.openContainer = new ContainerDispenser(this.inventory, this.inventory);
        }
    }

    @Override
    public void respawnPlayer() {
        this.connection.sendPacket(new CPacketClientStatus(CPacketClientStatus.State.PERFORM_RESPAWN));
    }

    @Override
    public void dismountRidingEntity() {
        super.dismountRidingEntity();
        this.rowingBoat = false;
    }

    public void sendChatMessage(String string) {
        this.connection.sendPacket(new CPacketChatMessage(string));
    }

    @Override
    public void openEditStructure(TileEntityStructure tileEntityStructure) {
    }

    protected void updateAutoJump(float f, float f2) {
        if (this.isAutoJumpEnabled() && this.autoJumpTime <= 0 && this.onGround && !this.isSneaking() && !this.isRiding()) {
            Vec2f vec2f = this.movementInput.getMoveVector();
            if (vec2f.x != 0.0f || vec2f.y != 0.0f) {
                IBlockState iBlockState;
                BlockPos blockPos;
                IBlockState iBlockState2;
                float f3;
                float f4;
                Vec3d vec3d = new Vec3d(this.posX, this.getEntityBoundingBox().minY, this.posZ);
                double d = this.posX + (double)f;
                double d2 = this.posZ + (double)f2;
                Vec3d vec3d2 = new Vec3d(d, this.getEntityBoundingBox().minY, d2);
                Vec3d vec3d3 = new Vec3d(f, 0.0, f2);
                float f5 = this.getAIMoveSpeed();
                float f6 = (float)vec3d3.lengthSquared();
                if (f6 <= 0.001f) {
                    f4 = f5 * vec2f.x;
                    float f7 = f5 * vec2f.y;
                    float f8 = MathHelper.sin(this.rotationYaw * ((float)Math.PI / 180));
                    f3 = MathHelper.cos(this.rotationYaw * ((float)Math.PI / 180));
                    vec3d3 = new Vec3d(f4 * f3 - f7 * f8, vec3d3.y, f7 * f3 + f4 * f8);
                    f6 = (float)vec3d3.lengthSquared();
                    if (f6 <= 0.001f) {
                        return;
                    }
                }
                f4 = (float)MathHelper.fastInvSqrt(f6);
                Vec3d vec3d4 = vec3d3.scale(f4);
                Vec3d vec3d5 = this.getForward();
                f3 = (float)(vec3d5.x * vec3d4.x + vec3d5.z * vec3d4.z);
                if (f3 >= -0.15f && (iBlockState2 = this.world.getBlockState(blockPos = new BlockPos(this.posX, this.getEntityBoundingBox().maxY, this.posZ))).getCollisionBoundingBox(this.world, blockPos) == null && (iBlockState = this.world.getBlockState(blockPos = blockPos.up())).getCollisionBoundingBox(this.world, blockPos) == null) {
                    float f9;
                    float f10 = 7.0f;
                    float f11 = 1.2f;
                    if (this.isPotionActive(MobEffects.JUMP_BOOST)) {
                        f11 += (float)(this.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.75f;
                    }
                    float f12 = Math.max(f5 * 7.0f, 1.0f / f4);
                    Vec3d vec3d6 = vec3d2.add(vec3d4.scale(f12));
                    float f13 = this.width;
                    float f14 = this.height;
                    AxisAlignedBB axisAlignedBB = new AxisAlignedBB(vec3d, vec3d6.add(0.0, f14, 0.0)).expand(f13, 0.0, f13);
                    Vec3d vec3d7 = vec3d.add(0.0, 0.51f, 0.0);
                    vec3d6 = vec3d6.add(0.0, 0.51f, 0.0);
                    Vec3d vec3d8 = vec3d4.crossProduct(new Vec3d(0.0, 1.0, 0.0));
                    Vec3d vec3d9 = vec3d8.scale(f13 * 0.5f);
                    Vec3d vec3d10 = vec3d7.subtract(vec3d9);
                    Vec3d vec3d11 = vec3d6.subtract(vec3d9);
                    Vec3d vec3d12 = vec3d7.add(vec3d9);
                    Vec3d vec3d13 = vec3d6.add(vec3d9);
                    List<AxisAlignedBB> list = this.world.getCollisionBoxes(this, axisAlignedBB);
                    if (!list.isEmpty()) {
                        // empty if block
                    }
                    float f15 = Float.MIN_VALUE;
                    for (AxisAlignedBB axisAlignedBB2 : list) {
                        if (!axisAlignedBB2.intersects(vec3d10, vec3d11) && !axisAlignedBB2.intersects(vec3d12, vec3d13)) continue;
                        f15 = (float)axisAlignedBB2.maxY;
                        Vec3d vec3d14 = axisAlignedBB2.getCenter();
                        BlockPos blockPos2 = new BlockPos(vec3d14);
                        int n = 1;
                        while (!((float)n >= f11)) {
                            IBlockState iBlockState3;
                            BlockPos blockPos3 = blockPos2.up(n);
                            IBlockState iBlockState4 = this.world.getBlockState(blockPos3);
                            AxisAlignedBB axisAlignedBB3 = iBlockState4.getCollisionBoundingBox(this.world, blockPos3);
                            if (axisAlignedBB3 != null && (double)(f15 = (float)axisAlignedBB3.maxY + (float)blockPos3.getY()) - this.getEntityBoundingBox().minY > (double)f11) {
                                return;
                            }
                            if (n > 1 && (iBlockState3 = this.world.getBlockState(blockPos = blockPos.up())).getCollisionBoundingBox(this.world, blockPos) != null) {
                                return;
                            }
                            ++n;
                        }
                    }
                    if (f15 != Float.MIN_VALUE && (f9 = (float)((double)f15 - this.getEntityBoundingBox().minY)) > 0.5f && f9 <= f11) {
                        this.autoJumpTime = 1;
                    }
                }
            }
        }
    }

    @Override
    protected void damageEntity(DamageSource damageSource, float f) {
        if (!this.isEntityInvulnerable(damageSource)) {
            this.setHealth(this.getHealth() - f);
        }
    }

    public boolean isRidingHorse() {
        Entity entity = this.getRidingEntity();
        return this.isRiding() && entity instanceof IJumpingMount && ((IJumpingMount)((Object)entity)).canJump();
    }

    @Override
    @Nullable
    public EntityItem dropItem(boolean bl) {
        CPacketPlayerDigging.Action action = bl ? CPacketPlayerDigging.Action.DROP_ALL_ITEMS : CPacketPlayerDigging.Action.DROP_ITEM;
        this.connection.sendPacket(new CPacketPlayerDigging(action, BlockPos.ORIGIN, EnumFacing.DOWN));
        return null;
    }

    @Override
    public boolean isHandActive() {
        return this.handActive;
    }

    public void onUpdateWalkingPlayer() {

        boolean bl;
        boolean bl2;
        boolean bl3 = this.isSprinting();
        if (bl3 != this.serverSprintState) {
            if (bl3) {
                this.connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.START_SPRINTING));
            } else {
                this.connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.STOP_SPRINTING));
            }
            this.serverSprintState = bl3;
        }
        if ((bl2 = this.isSneaking()) != this.serverSneakState) {
            if (bl2) {
                this.connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.START_SNEAKING));
            } else {
                this.connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.STOP_SNEAKING));
            }
            this.serverSneakState = bl2;
        }
        AxisAlignedBB axisAlignedBB = this.getEntityBoundingBox();
        double d = this.posX - this.lastReportedPosX;
        double d2 = axisAlignedBB.minY - this.lastReportedPosY;
        double d3 = this.posZ - this.lastReportedPosZ;
        double d4 = this.rotationYaw - this.lastReportedYaw;
        double d5 = this.rotationPitch - this.lastReportedPitch;
        ++this.positionUpdateTicks;
        boolean bl4 = d * d + d2 * d2 + d3 * d3 > 9.0E-4 || this.positionUpdateTicks >= 20;
        boolean bl5 = bl = d4 != 0.0 || d5 != 0.0;
        if (this.isRiding()) {
            this.connection.sendPacket(new CPacketPlayer.PositionRotation(this.motionX, -999.0, this.motionZ, this.rotationYaw, this.rotationPitch, this.onGround));
            bl4 = false;
        } else if (bl4 && bl) {
            this.connection.sendPacket(new CPacketPlayer.PositionRotation(this.posX, axisAlignedBB.minY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround));
        } else if (bl4) {
            this.connection.sendPacket(new CPacketPlayer.Position(this.posX, axisAlignedBB.minY, this.posZ, this.onGround));
        } else if (bl) {
            this.connection.sendPacket(new CPacketPlayer.Rotation(this.rotationYaw, this.rotationPitch, this.onGround));
        } else if (this.prevOnGround != this.onGround) {
            this.connection.sendPacket(new CPacketPlayer(this.onGround));
        }
        if (bl4) {
            this.lastReportedPosX = this.posX;
            this.lastReportedPosY = axisAlignedBB.minY;
            this.lastReportedPosZ = this.posZ;
            this.positionUpdateTicks = 0;
        }
        if (bl) {
            this.lastReportedYaw = this.rotationYaw;
            this.lastReportedPitch = this.rotationPitch;
        }
        this.prevOnGround = this.onGround;

    }

    @Override
    public void updateEntityActionState() {
        super.updateEntityActionState();
        this.moveStrafing = this.movementInput.moveStrafe;
        this.moveForward = this.movementInput.moveForward;
        this.isJumping = this.movementInput.jump;
        this.prevRenderArmYaw = this.renderArmYaw;
        this.prevRenderArmPitch = this.renderArmPitch;
        this.renderArmPitch = (float)((double)this.renderArmPitch + (double)(this.rotationPitch - this.renderArmPitch) * 0.5);
        this.renderArmYaw = (float)((double)this.renderArmYaw + (double)(this.rotationYaw - this.renderArmYaw) * 0.5);
    }

    @Override
    public boolean isUser() {
        return true;
    }

    @Override
    public void updateRidden() {
        super.updateRidden();
        this.rowingBoat = false;
        if (this.getRidingEntity() instanceof EntityBoat) {
            EntityBoat entityBoat = (EntityBoat)this.getRidingEntity();
            entityBoat.updateInputs(this.movementInput.leftKeyDown, this.movementInput.rightKeyDown, this.movementInput.forwardKeyDown, this.movementInput.backKeyDown);
            this.rowingBoat |= this.movementInput.leftKeyDown || this.movementInput.rightKeyDown || this.movementInput.forwardKeyDown || this.movementInput.backKeyDown;
        }
    }

    public void setPermissionLevel(int n) {
        this.permissionLevel = n;
    }

    @Override
    public void displayGuiEditCommandCart(CommandBlockBaseLogic commandBlockBaseLogic) {
    }

    @Override
    public void sendPlayerAbilities() {
        this.connection.sendPacket(new CPacketPlayerAbilities(this.capabilities));
    }

    @Override
    public void onLivingUpdate() {
        Object object;
        boolean bl;
        ++this.sprintingTicksLeft;
        if (this.sprintToggleTimer > 0) {
            --this.sprintToggleTimer;
        }
        this.prevTimeInPortal = this.timeInPortal;
        if (this.inPortal) {
            if (this.timeInPortal == 0.0f) {
                // empty if block
            }
            this.timeInPortal += 0.0125f;
            if (this.timeInPortal >= 1.0f) {
                this.timeInPortal = 1.0f;
            }
            this.inPortal = false;
        } else if (this.isPotionActive(MobEffects.NAUSEA) && this.getActivePotionEffect(MobEffects.NAUSEA).getDuration() > 60) {
            this.timeInPortal += 0.006666667f;
            if (this.timeInPortal > 1.0f) {
                this.timeInPortal = 1.0f;
            }
        } else {
            if (this.timeInPortal > 0.0f) {
                this.timeInPortal -= 0.05f;
            }
            if (this.timeInPortal < 0.0f) {
                this.timeInPortal = 0.0f;
            }
        }
        if (this.timeUntilPortal > 0) {
            --this.timeUntilPortal;
        }
        boolean bl2 = this.movementInput.jump;
        boolean bl3 = this.movementInput.sneak;
        float f = 0.8f;
        boolean bl4 = this.movementInput.moveForward >= 0.8f;
        this.movementInput.updatePlayerMoveState();
        if (this.isHandActive() && !this.isRiding()) {
            this.movementInput.moveStrafe *= 0.2f;
            this.movementInput.moveForward *= 0.2f;
            this.sprintToggleTimer = 0;
        }
        boolean bl5 = false;
        if (this.autoJumpTime > 0) {
            --this.autoJumpTime;
            bl5 = true;
            this.movementInput.jump = true;
        }
        AxisAlignedBB axisAlignedBB = this.getEntityBoundingBox();
        this.pushOutOfBlocks(this.posX - (double)this.width * 0.35, axisAlignedBB.minY + 0.5, this.posZ + (double)this.width * 0.35);
        this.pushOutOfBlocks(this.posX - (double)this.width * 0.35, axisAlignedBB.minY + 0.5, this.posZ - (double)this.width * 0.35);
        this.pushOutOfBlocks(this.posX + (double)this.width * 0.35, axisAlignedBB.minY + 0.5, this.posZ - (double)this.width * 0.35);
        this.pushOutOfBlocks(this.posX + (double)this.width * 0.35, axisAlignedBB.minY + 0.5, this.posZ + (double)this.width * 0.35);
        boolean bl6 = bl = (float)this.getFoodStats().getFoodLevel() > 6.0f || this.capabilities.allowFlying;
        if (this.onGround && !bl3 && !bl4 && this.movementInput.moveForward >= 0.8f && !this.isSprinting() && bl && !this.isHandActive() && !this.isPotionActive(MobEffects.BLINDNESS)) {
            this.setSprinting(true);
        }
        if (!this.isSprinting() && this.movementInput.moveForward >= 0.8f && bl && !this.isHandActive() && !this.isPotionActive(MobEffects.BLINDNESS)) {
            this.setSprinting(true);
        }
        if (this.isSprinting() && (this.movementInput.moveForward < 0.8f || this.collidedHorizontally || !bl)) {
            this.setSprinting(false);
        }
        if (this.capabilities.allowFlying) {
            if (this.connection.getController().isSpectatorMode()) {
                if (!this.capabilities.isFlying) {
                    this.capabilities.isFlying = true;
                    this.sendPlayerAbilities();
                }
            } else if (!bl2 && this.movementInput.jump && !bl5) {
                if (this.flyToggleTimer == 0) {
                    this.flyToggleTimer = 7;
                } else {
                    this.capabilities.isFlying = !this.capabilities.isFlying;
                    this.sendPlayerAbilities();
                    this.flyToggleTimer = 0;
                }
            }
        }
        if (this.movementInput.jump && !bl2 && !this.onGround && this.motionY < 0.0 && !this.isElytraFlying() && !this.capabilities.isFlying && ((ItemStack)(object = this.getItemStackFromSlot(EntityEquipmentSlot.CHEST))).getItem() == Items.ELYTRA && !ItemElytra.isUsable((ItemStack)object)) {
            this.connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.START_FALL_FLYING));
        }
        this.wasFallFlying = this.isElytraFlying();
        if (this.capabilities.isFlying) {
            if (this.movementInput.sneak) {
                this.movementInput.moveStrafe = (float)((double)this.movementInput.moveStrafe / 0.3);
                this.movementInput.moveForward = (float)((double)this.movementInput.moveForward / 0.3);
                this.motionY -= (double)(this.capabilities.getFlySpeed() * 3.0f);
            }
            if (this.movementInput.jump) {
                this.motionY += (double)(this.capabilities.getFlySpeed() * 3.0f);
            }
        }
        if (this.isRidingHorse()) {
            object = (IJumpingMount)((Object)this.getRidingEntity());
            if (this.horseJumpPowerCounter < 0) {
                ++this.horseJumpPowerCounter;
                if (this.horseJumpPowerCounter == 0) {
                    this.horseJumpPower = 0.0f;
                }
            }
            if (bl2 && !this.movementInput.jump) {
                this.horseJumpPowerCounter = -10;
                ((IJumpingMount) object).setJumpPower(MathHelper.floor(this.getHorseJumpPower() * 100.0f));
                this.sendHorseJump();
            } else if (!bl2 && this.movementInput.jump) {
                this.horseJumpPowerCounter = 0;
                this.horseJumpPower = 0.0f;
            } else if (bl2) {
                ++this.horseJumpPowerCounter;
                this.horseJumpPower = this.horseJumpPowerCounter < 10 ? (float)this.horseJumpPowerCounter * 0.1f : 0.8f + 2.0f / (float)(this.horseJumpPowerCounter - 9) * 0.1f;
            }
        } else {
            this.horseJumpPower = 0.0f;
        }
        super.onLivingUpdate();
        if (this.onGround && this.capabilities.isFlying && !this.connection.getController().isSpectatorMode()) {
            this.capabilities.isFlying = false;
            this.sendPlayerAbilities();
        }
    }

    @Override
    public void displayGui(IInteractionObject iInteractionObject) {
    }

    @Override
    public void setActiveHand(EnumHand enumHand) {
        ItemStack itemStack = this.getHeldItem(enumHand);
        if (!itemStack.isEmpty() && !this.isHandActive()) {
            super.setActiveHand(enumHand);
            this.handActive = true;
            this.activeHand = enumHand;
        }
    }

    public void setServerBrand(String string) {
        this.serverBrand = string;
    }

    public boolean isAutoJumpEnabled() {
        return this.autoJumpEnabled;
    }

    @Override
    public void openEditSign(TileEntitySign tileEntitySign) {
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> dataParameter) {
        super.notifyDataManagerChange(dataParameter);
        if (HAND_STATES.equals(dataParameter)) {
            EnumHand enumHand;
            boolean bl = ((Byte)this.dataManager.get(HAND_STATES) & 1) > 0;
            EnumHand enumHand2 = enumHand = ((Byte)this.dataManager.get(HAND_STATES) & 2) > 0 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
            if (bl && !this.handActive) {
                this.setActiveHand(enumHand);
            } else if (!bl && this.handActive) {
                this.resetActiveHand();
            }
        }
        if (!(FLAGS.equals(dataParameter) && this.isElytraFlying() && this.wasFallFlying)) {
            // empty if block
        }
    }

    protected void sendHorseJump() {
        this.connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.START_RIDING_JUMP, MathHelper.floor(this.getHorseJumpPower() * 100.0f)));
    }



    public void closeScreenAndDropStack() {
        this.inventory.setItemStack(ItemStack.EMPTY);
        super.closeScreen();
    }

    @Override
    public void addStat(StatBase statBase, int n) {
        if (statBase != null && statBase.isIndependent) {
            super.addStat(statBase, n);
        }
    }

    @Override
    public BlockPos getPosition() {
        return new BlockPos(this.posX + 0.5, this.posY + 0.5, this.posZ + 0.5);
    }

    @Override
    protected ItemStack dropItemAndGetStack(EntityItem entityItem) {
        return ItemStack.EMPTY;
    }

    public void setPlayerSPHealth(float f) {
        if (this.hasValidHealth) {
            float f2 = this.getHealth() - f;
            if (f2 <= 0.0f) {
                this.setHealth(f);
                if (f2 < 0.0f) {
                    this.hurtResistantTime = this.maxHurtResistantTime / 2;
                }
            } else {
                this.lastDamage = f2;
                this.setHealth(this.getHealth());
                this.hurtResistantTime = this.maxHurtResistantTime;
                this.damageEntity(DamageSource.GENERIC, f2);
                this.hurtTime = this.maxHurtTime = 10;
            }
        } else {
            this.setHealth(f);
            this.hasValidHealth = true;
        }
    }

    private boolean isOpenBlockSpace(BlockPos blockPos) {
        return !this.world.getBlockState(blockPos).isNormalCube() && !this.world.getBlockState(blockPos.up()).isNormalCube();
    }

    public BotPlayer(BotPlayClient botPlayClient) {
        super(botPlayClient.getWorld(), botPlayClient.getGameProfile());
        this.permissionLevel = 0;
        this.currentContainerName = "";
        this.connection = botPlayClient;
        this.dimension = 0;
        c = new Counter();
    }

    @Override
    protected boolean pushOutOfBlocks(double d, double d2, double d3) {
        if (this.noClip) {
            return false;
        }
        BlockPos blockPos = new BlockPos(d, d2, d3);
        double d4 = d - (double)blockPos.getX();
        double d5 = d3 - (double)blockPos.getZ();
        if (!this.isOpenBlockSpace(blockPos)) {
            int n = -1;
            double d6 = 9999.0;
            if (this.isOpenBlockSpace(blockPos.west()) && d4 < d6) {
                d6 = d4;
                n = 0;
            }
            if (this.isOpenBlockSpace(blockPos.east()) && 1.0 - d4 < d6) {
                d6 = 1.0 - d4;
                n = 1;
            }
            if (this.isOpenBlockSpace(blockPos.north()) && d5 < d6) {
                d6 = d5;
                n = 4;
            }
            if (this.isOpenBlockSpace(blockPos.south()) && 1.0 - d5 < d6) {
                d6 = 1.0 - d5;
                n = 5;
            }
            float f = 0.1f;
            if (n == 0) {
                this.motionX = -0.1f;
            }
            if (n == 1) {
                this.motionX = 0.1f;
            }
            if (n == 4) {
                this.motionZ = -0.1f;
            }
            if (n == 5) {
                this.motionZ = 0.1f;
            }
        }
        return false;
    }

    @Override
    public void onUpdate() {


        if (this.world.isBlockLoaded(new BlockPos(this.posX, 0.0, this.posZ))) {
            try {
                super.onUpdate();
            }
            catch (NullPointerException nullPointerException) {
                this.connection.sendPacket(new SPacketDisconnect());
            }
            if (this.isRiding()) {
                this.connection.sendPacket(new CPacketPlayer.Rotation(this.rotationYaw, this.rotationPitch, this.onGround));
                this.connection.sendPacket(new CPacketInput(this.moveStrafing, this.moveForward, this.movementInput.jump, this.movementInput.sneak));
                Entity entity = this.getLowestRidingEntity();
                if (entity != this && entity.canPassengerSteer()) {
                    this.connection.sendPacket(new CPacketVehicleMove(entity));
                }
            } else {
                this.onUpdateWalkingPlayer();
            }
        }
    }

    @Override
    public boolean isServerWorld() {
        return true;
    }

    @Override
    public void heal(float f) {
    }

    @Override
    public void onCriticalHit(Entity entity) {
    }

    @Override
    @Nullable
    public PotionEffect removeActivePotionEffect(@Nullable Potion potion) {
        if (potion == MobEffects.NAUSEA) {
            this.prevTimeInPortal = 0.0f;
            this.timeInPortal = 0.0f;
        }
        return super.removeActivePotionEffect(potion);
    }

    @Override
    public void resetActiveHand() {
        super.resetActiveHand();
        this.handActive = false;
    }
}