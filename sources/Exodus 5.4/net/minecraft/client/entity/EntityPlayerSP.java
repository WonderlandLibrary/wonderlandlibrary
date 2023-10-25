/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.entity;

import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.EventDirection;
import me.Tengoku.Terror.event.EventType;
import me.Tengoku.Terror.event.events.EventChat;
import me.Tengoku.Terror.event.events.EventMotion;
import me.Tengoku.Terror.event.events.EventPostMotionUpdate;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.event.events.MovementEvent;
import me.Tengoku.Terror.module.combat.KillAura;
import me.Tengoku.Terror.module.player.NoSlow;
import me.Tengoku.Terror.util.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSoundMinecartRiding;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiDispenser;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.potion.Potion;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MovementInput;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;

public class EntityPlayerSP
extends AbstractClientPlayer {
    private KillAura aura;
    public float renderArmPitch;
    public MovementInput movementInput;
    public float prevRenderArmYaw;
    protected int sprintToggleTimer;
    protected Minecraft mc;
    public float prevRenderArmPitch;
    private boolean serverSprintState;
    public final NetHandlerPlayClient sendQueue;
    public double serverSidePosZ;
    private String clientBrand;
    private float lastReportedPitch;
    public float timeInPortal;
    private float horseJumpPower;
    private boolean hasValidHealth;
    private int horseJumpPowerCounter;
    public float serverSideYaw;
    public double serverSidePosY;
    private float lastReportedYaw;
    public double serverSidePosX;
    public float prevServerSidePitch;
    private boolean serverSneakState;
    public float prevTimeInPortal;
    public float serverSidePitch;
    private final StatFileWriter statWriter;
    private double lastReportedPosY;
    public float renderArmYaw;
    public float prevServerSideYaw;
    private double lastReportedPosX;
    private int positionUpdateTicks;
    public int sprintingTicksLeft;
    private double lastReportedPosZ;

    @Override
    public void displayGUIBook(ItemStack itemStack) {
        Item item = itemStack.getItem();
        if (item == Items.writable_book) {
            this.mc.displayGuiScreen(new GuiScreenBook(this, itemStack, true));
        }
    }

    @Override
    public void heal(float f) {
    }

    protected boolean isCurrentViewEntity() {
        return this.mc.getRenderViewEntity() == this;
    }

    @Override
    public boolean isUser() {
        return true;
    }

    private boolean isOpenBlockSpace(BlockPos blockPos) {
        return !this.worldObj.getBlockState(blockPos).getBlock().isNormalCube() && !this.worldObj.getBlockState(blockPos.up()).getBlock().isNormalCube();
    }

    @Override
    public void addChatMessage(IChatComponent iChatComponent) {
        this.mc.ingameGUI.getChatGUI().printChatMessage(iChatComponent);
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
                this.damageEntity(DamageSource.generic, f2);
                this.maxHurtTime = 10;
                this.hurtTime = 10;
            }
        } else {
            this.setHealth(f);
            this.hasValidHealth = true;
        }
    }

    @Override
    public void mountEntity(Entity entity) {
        super.mountEntity(entity);
        if (entity instanceof EntityMinecart) {
            this.mc.getSoundHandler().playSound(new MovingSoundMinecartRiding(this, (EntityMinecart)entity));
        }
    }

    @Override
    public void displayGui(IInteractionObject iInteractionObject) {
        String string = iInteractionObject.getGuiID();
        if ("minecraft:crafting_table".equals(string)) {
            this.mc.displayGuiScreen(new GuiCrafting(this.inventory, this.worldObj));
        } else if ("minecraft:enchanting_table".equals(string)) {
            this.mc.displayGuiScreen(new GuiEnchantment(this.inventory, this.worldObj, iInteractionObject));
        } else if ("minecraft:anvil".equals(string)) {
            this.mc.displayGuiScreen(new GuiRepair(this.inventory, this.worldObj));
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float f) {
        return false;
    }

    @Override
    public void onEnchantmentCritical(Entity entity) {
        this.mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT_MAGIC);
    }

    @Override
    public void playSound(String string, float f, float f2) {
        this.worldObj.playSound(this.posX, this.posY, this.posZ, string, f, f2, false);
    }

    public void setSpeedDirection(float f, float f2) {
        this.motionX = -Math.sin(this.getDirection() * f2) * (double)f;
        this.motionZ = Math.cos(this.getDirection() * f2) * (double)f;
    }

    public void sendChatMessage(String string) {
        EventChat eventChat = new EventChat(string);
        Exodus.onEvent(eventChat);
        if (eventChat.isCancelled()) {
            return;
        }
        this.sendQueue.addToSendQueue(new C01PacketChatMessage(eventChat.getMessage()));
    }

    protected void sendHorseJump() {
        this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.RIDING_JUMP, (int)(this.getHorseJumpPower() * 100.0f)));
    }

    @Override
    public void openEditCommandBlock(CommandBlockLogic commandBlockLogic) {
        this.mc.displayGuiScreen(new GuiCommandBlock(commandBlockLogic));
    }

    @Override
    protected void damageEntity(DamageSource damageSource, float f) {
        if (!this.isEntityInvulnerable(damageSource)) {
            this.setHealth(this.getHealth() - f);
        }
    }

    public StatFileWriter getStatFileWriter() {
        return this.statWriter;
    }

    @Override
    public boolean isSneaking() {
        boolean bl;
        boolean bl2 = bl = this.movementInput != null ? this.movementInput.sneak : false;
        return bl && !this.sleeping;
    }

    public boolean isRidingHorse() {
        return this.ridingEntity != null && this.ridingEntity instanceof EntityHorse && ((EntityHorse)this.ridingEntity).isHorseSaddled();
    }

    @Override
    public BlockPos getPosition() {
        return new BlockPos(this.posX + 0.5, this.posY + 0.5, this.posZ + 0.5);
    }

    public float getSpeed() {
        float f = (float)Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        return f;
    }

    @Override
    public void swingItem() {
        super.swingItem();
        this.sendQueue.addToSendQueue(new C0APacketAnimation());
    }

    @Override
    public boolean isServerWorld() {
        return true;
    }

    public void setSpeedStrafeless(float f) {
        Minecraft.thePlayer.motionX = -Math.sin(Math.toRadians(Minecraft.thePlayer.rotationYaw)) * (double)f * (double)1.08f * MathUtils.getBaseMovementSpeed() * 2.2524514211212;
        Minecraft.thePlayer.motionZ = Math.cos(Math.toRadians(Minecraft.thePlayer.rotationYaw)) * (double)f * (double)1.08f * MathUtils.getBaseMovementSpeed() * 2.2524514211212;
    }

    public void setSpeed(float f) {
        this.motionX = -(Math.sin(this.getDirection()) * (double)f);
        this.motionZ = Math.cos(this.getDirection()) * (double)f;
    }

    @Override
    public void addChatComponentMessage(IChatComponent iChatComponent) {
        this.mc.ingameGUI.getChatGUI().printChatMessage(iChatComponent);
    }

    @Override
    public void addStat(StatBase statBase, int n) {
        if (statBase != null && statBase.isIndependent) {
            super.addStat(statBase, n);
        }
    }

    @Override
    public void closeScreen() {
        this.sendQueue.addToSendQueue(new C0DPacketCloseWindow(this.openContainer.windowId));
        this.closeScreenAndDropStack();
    }

    @Override
    public EntityItem dropOneItem(boolean bl) {
        C07PacketPlayerDigging.Action action = bl ? C07PacketPlayerDigging.Action.DROP_ALL_ITEMS : C07PacketPlayerDigging.Action.DROP_ITEM;
        this.sendQueue.addToSendQueue(new C07PacketPlayerDigging(action, BlockPos.ORIGIN, EnumFacing.DOWN));
        return null;
    }

    public void setXPStats(float f, int n, int n2) {
        this.experience = f;
        this.experienceTotal = n;
        this.experienceLevel = n2;
    }

    @Override
    public void onLivingUpdate() {
        boolean bl;
        if (this.sprintingTicksLeft > 0) {
            --this.sprintingTicksLeft;
            if (this.sprintingTicksLeft == 0) {
                this.setSprinting(false);
            }
        }
        if (this.sprintToggleTimer > 0) {
            --this.sprintToggleTimer;
        }
        this.prevTimeInPortal = this.timeInPortal;
        if (this.inPortal) {
            if (this.mc.currentScreen != null && !this.mc.currentScreen.doesGuiPauseGame()) {
                this.mc.displayGuiScreen(null);
            }
            if (this.timeInPortal == 0.0f) {
                this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("portal.trigger"), this.rand.nextFloat() * 0.4f + 0.8f));
            }
            this.timeInPortal += 0.0125f;
            if (this.timeInPortal >= 1.0f) {
                this.timeInPortal = 1.0f;
            }
            this.inPortal = false;
        } else if (this.isPotionActive(Potion.confusion) && this.getActivePotionEffect(Potion.confusion).getDuration() > 60) {
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
        boolean bl4 = MovementInput.moveForward >= f;
        this.movementInput.updatePlayerMoveState();
        if (this.isUsingItem() && !this.isRiding() && !Exodus.INSTANCE.moduleManager.getModuleByClass(NoSlow.class).isToggled()) {
            MovementInput.moveStrafe *= 0.2f;
            MovementInput.moveForward *= 0.2f;
            this.sprintToggleTimer = 0;
        }
        this.pushOutOfBlocks(this.posX - (double)this.width * 0.35, this.getEntityBoundingBox().minY + 0.5, this.posZ + (double)this.width * 0.35);
        this.pushOutOfBlocks(this.posX - (double)this.width * 0.35, this.getEntityBoundingBox().minY + 0.5, this.posZ - (double)this.width * 0.35);
        this.pushOutOfBlocks(this.posX + (double)this.width * 0.35, this.getEntityBoundingBox().minY + 0.5, this.posZ - (double)this.width * 0.35);
        this.pushOutOfBlocks(this.posX + (double)this.width * 0.35, this.getEntityBoundingBox().minY + 0.5, this.posZ + (double)this.width * 0.35);
        boolean bl5 = bl = (float)this.getFoodStats().getFoodLevel() > 6.0f || this.capabilities.allowFlying;
        if (this.onGround && !bl3 && !bl4 && MovementInput.moveForward >= f && !this.isSprinting() && bl && !this.isUsingItem() && !this.isPotionActive(Potion.blindness)) {
            if (this.sprintToggleTimer <= 0 && !Minecraft.gameSettings.keyBindSprint.isKeyDown()) {
                this.sprintToggleTimer = 7;
            } else {
                this.setSprinting(true);
            }
        }
        if (!this.isSprinting() && MovementInput.moveForward >= f && bl && !this.isUsingItem() && !this.isPotionActive(Potion.blindness) && Minecraft.gameSettings.keyBindSprint.isKeyDown()) {
            this.setSprinting(true);
        }
        if (this.isSprinting() && (MovementInput.moveForward < f || this.isCollidedHorizontally || !bl)) {
            this.setSprinting(false);
        }
        if (this.capabilities.allowFlying) {
            if (Minecraft.playerController.isSpectatorMode()) {
                if (!this.capabilities.isFlying) {
                    this.capabilities.isFlying = true;
                    this.sendPlayerAbilities();
                }
            } else if (!bl2 && this.movementInput.jump) {
                if (this.flyToggleTimer == 0) {
                    this.flyToggleTimer = 7;
                } else {
                    this.capabilities.isFlying = !this.capabilities.isFlying;
                    this.sendPlayerAbilities();
                    this.flyToggleTimer = 0;
                }
            }
        }
        if (this.capabilities.isFlying && this.isCurrentViewEntity()) {
            if (this.movementInput.sneak) {
                this.motionY -= (double)(this.capabilities.getFlySpeed() * 3.0f);
            }
            if (this.movementInput.jump) {
                this.motionY += (double)(this.capabilities.getFlySpeed() * 3.0f);
            }
        }
        if (this.isRidingHorse()) {
            if (this.horseJumpPowerCounter < 0) {
                ++this.horseJumpPowerCounter;
                if (this.horseJumpPowerCounter == 0) {
                    this.horseJumpPower = 0.0f;
                }
            }
            if (bl2 && !this.movementInput.jump) {
                this.horseJumpPowerCounter = -10;
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
        if (this.onGround && this.capabilities.isFlying && !Minecraft.playerController.isSpectatorMode()) {
            this.capabilities.isFlying = false;
            this.sendPlayerAbilities();
        }
    }

    public void closeScreenAndDropStack() {
        this.inventory.setItemStack(null);
        super.closeScreen();
        this.mc.displayGuiScreen(null);
    }

    public float getDirection() {
        float f = this.rotationYaw;
        if (this.moveForward < 0.0f) {
            f += 180.0f;
        }
        float f2 = 1.0f;
        f2 = this.moveForward < 0.0f ? -0.5f : (this.moveForward > 0.0f ? 0.5f : 1.0f);
        if (this.moveStrafing > 0.0f) {
            f -= 90.0f * f2;
        }
        if (this.moveStrafing < 0.0f) {
            f += 90.0f * f2;
        }
        return f *= (float)Math.PI / 180;
    }

    @Override
    protected void joinEntityItemWithWorld(EntityItem entityItem) {
    }

    @Override
    public void displayGUIChest(IInventory iInventory) {
        String string;
        String string2 = string = iInventory instanceof IInteractionObject ? ((IInteractionObject)((Object)iInventory)).getGuiID() : "minecraft:container";
        if ("minecraft:chest".equals(string)) {
            this.mc.displayGuiScreen(new GuiChest(this.inventory, iInventory));
        } else if ("minecraft:hopper".equals(string)) {
            this.mc.displayGuiScreen(new GuiHopper(this.inventory, iInventory));
        } else if ("minecraft:furnace".equals(string)) {
            this.mc.displayGuiScreen(new GuiFurnace(this.inventory, iInventory));
        } else if ("minecraft:brewing_stand".equals(string)) {
            this.mc.displayGuiScreen(new GuiBrewingStand(this.inventory, iInventory));
        } else if ("minecraft:beacon".equals(string)) {
            this.mc.displayGuiScreen(new GuiBeacon(this.inventory, iInventory));
        } else if (!"minecraft:dispenser".equals(string) && !"minecraft:dropper".equals(string)) {
            this.mc.displayGuiScreen(new GuiChest(this.inventory, iInventory));
        } else {
            this.mc.displayGuiScreen(new GuiDispenser(this.inventory, iInventory));
        }
    }

    @Override
    public void respawnPlayer() {
        this.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
    }

    @Override
    public void moveEntity(double d, double d2, double d3) {
        MovementEvent movementEvent = new MovementEvent(d, d2, d3);
        movementEvent.call();
        if (movementEvent.isCancelled()) {
            super.moveEntity(0.0, 0.0, 0.0);
        } else {
            super.moveEntity(movementEvent.getMotionX(), movementEvent.getMotionY(), movementEvent.getMotionZ());
        }
    }

    public void setClientBrand(String string) {
        this.clientBrand = string;
    }

    public float getServerDirection() {
        float f = this.serverSideYaw;
        if (this.moveForward < 0.0f) {
            f += 180.0f;
        }
        float f2 = 1.0f;
        f2 = this.moveForward < 0.0f ? -0.5f : (this.moveForward > 0.0f ? 0.5f : 1.0f);
        if (this.moveStrafing > 0.0f) {
            f -= 90.0f * f2;
        }
        if (this.moveStrafing < 0.0f) {
            f += 90.0f * f2;
        }
        return f *= (float)Math.PI / 180;
    }

    @Override
    public void onUpdate() {
        if (this.worldObj.isBlockLoaded(new BlockPos(this.posX, 0.0, this.posZ))) {
            EventUpdate eventUpdate = new EventUpdate();
            eventUpdate.setType(EventType.PRE);
            Exodus.onEvent(eventUpdate);
            eventUpdate.call();
            super.onUpdate();
            if (this.isRiding()) {
                this.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(this.rotationYaw, this.rotationPitch, this.onGround));
                this.sendQueue.addToSendQueue(new C0CPacketInput(this.moveStrafing, this.moveForward, this.movementInput.jump, this.movementInput.sneak));
            } else {
                EventPostMotionUpdate eventPostMotionUpdate = new EventPostMotionUpdate();
                eventPostMotionUpdate.setType(EventType.PRE);
                Exodus.onEvent(eventPostMotionUpdate);
                eventPostMotionUpdate.call();
                this.onUpdateWalkingPlayer();
                eventPostMotionUpdate.setType(EventType.POST);
            }
        }
    }

    public void sendHorseInventory() {
        this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.OPEN_INVENTORY));
    }

    @Override
    public void setSprinting(boolean bl) {
        super.setSprinting(bl);
        this.sprintingTicksLeft = bl ? 600 : 0;
    }

    public void setSpeed(double d) {
        this.motionX = -(Math.sin(this.getDirection()) * d);
        this.motionZ = Math.cos(this.getDirection()) * d;
    }

    public EntityPlayerSP(Minecraft minecraft, World world, NetHandlerPlayClient netHandlerPlayClient, StatFileWriter statFileWriter) {
        super(world, netHandlerPlayClient.getGameProfile());
        this.sendQueue = netHandlerPlayClient;
        this.statWriter = statFileWriter;
        this.mc = minecraft;
        this.dimension = 0;
    }

    public void onUpdateWalkingPlayer() {
        boolean bl;
        EventMotion eventMotion = new EventMotion(this.posX, this.posY, this.posZ, this.onGround, this.rotationYaw, this.rotationPitch, this.lastReportedYaw, this.lastReportedPitch, EventType.PRE);
        eventMotion.call();
        Exodus.onEvent(eventMotion);
        eventMotion.setType(EventType.PRE);
        eventMotion.setDirection(EventDirection.INCOMING);
        boolean bl2 = this.isSprinting();
        this.serverSidePosX = EventMotion.getX();
        this.serverSidePosY = EventMotion.getY();
        this.serverSidePosZ = EventMotion.getZ();
        if (bl2 != this.serverSprintState) {
            if (bl2) {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SPRINTING));
            } else {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SPRINTING));
            }
            this.serverSprintState = bl2;
        }
        if ((bl = this.isSneaking()) != this.serverSneakState) {
            if (bl) {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SNEAKING));
            } else {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SNEAKING));
            }
            this.serverSneakState = bl;
        }
        if (Minecraft.gameSettings.thirdPersonView != 0) {
            this.renderYawOffset = EventMotion.getYaw();
            this.serverSidePitch = EventMotion.getPitch();
            this.rotationYawHead = EventMotion.getYaw();
        }
        if (this.isCurrentViewEntity()) {
            boolean bl3;
            double d = EventMotion.getX() - this.lastReportedPosX;
            double d2 = EventMotion.getY() - this.lastReportedPosY;
            double d3 = EventMotion.getZ() - this.lastReportedPosZ;
            double d4 = EventMotion.getYaw() - this.lastReportedYaw;
            double d5 = EventMotion.getPitch() - this.lastReportedPitch;
            boolean bl4 = !(d * d + d2 * d2 + d3 * d3 <= 9.0E-4) || this.positionUpdateTicks >= 20;
            boolean bl5 = bl3 = d4 != 0.0 || d5 != 0.0;
            if (this.ridingEntity == null) {
                if (bl4 && bl3) {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(EventMotion.getX(), EventMotion.getY(), EventMotion.getZ(), EventMotion.getYaw(), EventMotion.getPitch(), eventMotion.isOnGround()));
                } else if (bl4) {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(EventMotion.getX(), EventMotion.getY(), EventMotion.getZ(), eventMotion.isOnGround()));
                } else if (bl3) {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(EventMotion.getYaw(), EventMotion.getPitch(), eventMotion.isOnGround()));
                } else {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer(eventMotion.isOnGround()));
                }
            } else {
                this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.motionX, -999.0, this.motionZ, this.rotationYaw, this.rotationPitch, eventMotion.isOnGround()));
                bl4 = false;
            }
            ++this.positionUpdateTicks;
            if (bl4) {
                this.lastReportedPosX = EventMotion.getX();
                this.lastReportedPosY = EventMotion.getY();
                this.lastReportedPosZ = EventMotion.getZ();
                this.positionUpdateTicks = 0;
            }
            if (bl3) {
                this.lastReportedYaw = EventMotion.getYaw();
                eventMotion.setLastYaw(this.lastReportedYaw);
                this.lastReportedPitch = EventMotion.getPitch();
                eventMotion.setLastPitch(this.lastReportedPitch);
            }
        }
        eventMotion.setDirection(EventDirection.OUTGOING);
        eventMotion.setType(EventType.POST);
        EventMotion.setLastX(EventMotion.getX());
        EventMotion.setLastY(EventMotion.getY());
        EventMotion.setLastZ(EventMotion.getZ());
    }

    @Override
    public void displayVillagerTradeGui(IMerchant iMerchant) {
        this.mc.displayGuiScreen(new GuiMerchant(this.inventory, iMerchant, this.worldObj));
    }

    @Override
    public void displayGUIHorse(EntityHorse entityHorse, IInventory iInventory) {
        this.mc.displayGuiScreen(new GuiScreenHorseInventory(this.inventory, iInventory, entityHorse));
    }

    public final boolean isMoving() {
        Minecraft.getMinecraft();
        if (Minecraft.thePlayer.moveForward == 0.0f) {
            Minecraft.getMinecraft();
            if (Minecraft.thePlayer.moveStrafing == 0.0f) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void openEditSign(TileEntitySign tileEntitySign) {
        this.mc.displayGuiScreen(new GuiEditSign(tileEntitySign));
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
                this.motionX = -f;
            }
            if (n == 1) {
                this.motionX = f;
            }
            if (n == 4) {
                this.motionZ = -f;
            }
            if (n == 5) {
                this.motionZ = f;
            }
        }
        return false;
    }

    @Override
    public void updateEntityActionState() {
        super.updateEntityActionState();
        if (this.isCurrentViewEntity()) {
            this.moveStrafing = MovementInput.moveStrafe;
            this.moveForward = MovementInput.moveForward;
            this.isJumping = this.movementInput.jump;
            this.prevRenderArmYaw = this.renderArmYaw;
            this.prevRenderArmPitch = this.renderArmPitch;
            this.renderArmPitch = (float)((double)this.renderArmPitch + (double)(this.rotationPitch - this.renderArmPitch) * 0.5);
            this.renderArmYaw = (float)((double)this.renderArmYaw + (double)(this.rotationYaw - this.renderArmYaw) * 0.5);
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(int n, String string) {
        return n <= 0;
    }

    public float getHorseJumpPower() {
        return this.horseJumpPower;
    }

    public String getClientBrand() {
        return this.clientBrand;
    }

    @Override
    public void sendPlayerAbilities() {
        this.sendQueue.addToSendQueue(new C13PacketPlayerAbilities(this.capabilities));
    }

    @Override
    public void onCriticalHit(Entity entity) {
        this.mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT);
    }
}

