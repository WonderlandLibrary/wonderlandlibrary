// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.entity;

import net.augustus.events.EventMoveEntity;
import net.minecraft.util.MovingObjectPosition;
import java.util.Iterator;
import java.util.ArrayList;
import net.augustus.utils.MoveUtil;
import net.augustus.events.EventNoSlow;
import net.minecraft.util.MathHelper;
import net.augustus.events.EventSilentMove;
import net.minecraft.potion.Potion;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.EnumParticleTypes;
import net.augustus.Augustus;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.entity.IMerchant;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.world.IWorldNameable;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.client.gui.inventory.GuiDispenser;
import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.world.IInteractionObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.init.Items;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.util.IChatComponent;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.stats.StatBase;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.augustus.utils.custompackets.CAnimateHandPacket;
import net.augustus.utils.custompackets.Hand;
import net.minecraft.network.play.client.C0APacketAnimation;
import viamcp.gui.GuiProtocolSelector;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.augustus.events.EventChat;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.entity.item.EntityItem;
import net.augustus.events.EventPostMotion;
import net.augustus.utils.ViaMCP;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.augustus.events.EventPreMotion;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.lenni0451.eventapi.events.IEvent;
import net.augustus.utils.EventHandler;
import net.augustus.events.EventUpdate;
import net.minecraft.util.BlockPos;
import net.minecraft.client.audio.ISound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.audio.MovingSoundMinecartRiding;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.augustus.utils.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Vec3;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.client.network.NetHandlerPlayClient;

public class EntityPlayerSP extends AbstractClientPlayer
{
    public final NetHandlerPlayClient sendQueue;
    private final StatFileWriter statWriter;
    private GuiScreen serverInv;
    public int ticksSinceLastSwing;
    private Vec3 spawnPos;
    public int rotIncrement;
    private double lastReportedPosX;
    private double lastReportedPosY;
    private double lastReportedPosZ;
    private float lastReportedYaw;
    private float lastReportedPitch;
    private boolean serverSneakState;
    private boolean serverSprintState;
    private int positionUpdateTicks;
    private boolean hasValidHealth;
    private String clientBrand;
    public MovementInput movementInput;
    protected Minecraft mc;
    protected int sprintToggleTimer;
    public int sprintingTicksLeft;
    public float renderArmYaw;
    public float renderArmPitch;
    public float prevRenderArmYaw;
    public float prevRenderArmPitch;
    private int horseJumpPowerCounter;
    private float horseJumpPower;
    public float timeInPortal;
    public float prevTimeInPortal;
    private boolean prevOnGround;
    private boolean shouldGetSpawnPos;
    public int reSprint;
    private boolean startServerSprintState;
    private int counter;
    private TimeHelper moveFixDelayTimeHelper;
    private float forward;
    private float strafe;
    private Vec3 severPosition;
    private Vec3 lastServerPosition;
    
    public EntityPlayerSP(final Minecraft mcIn, final World worldIn, final NetHandlerPlayClient netHandler, final StatFileWriter statFile) {
        super(worldIn, netHandler.getGameProfile());
        this.shouldGetSpawnPos = false;
        this.moveFixDelayTimeHelper = new TimeHelper();
        this.forward = 0.0f;
        this.strafe = 0.0f;
        this.severPosition = new Vec3(0.0, 0.0, 0.0);
        this.lastServerPosition = new Vec3(0.0, 0.0, 0.0);
        this.sendQueue = netHandler;
        this.statWriter = statFile;
        this.mc = mcIn;
        this.dimension = 0;
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        return false;
    }
    
    @Override
    public void heal(final float healAmount) {
    }
    
    @Override
    public void mountEntity(final Entity entityIn) {
        super.mountEntity(entityIn);
        if (entityIn instanceof EntityMinecart) {
            this.mc.getSoundHandler().playSound(new MovingSoundMinecartRiding(this, (EntityMinecart)entityIn));
        }
    }
    
    @Override
    public void onUpdate() {
        if (this.worldObj.isBlockLoaded(new BlockPos(this.posX, 0.0, this.posZ))) {
            if (this.ticksExisted < 2) {
                this.shouldGetSpawnPos = true;
            }
            if (this.shouldGetSpawnPos && this.onGround && !this.mc.thePlayer.isRiding()) {
                this.spawnPos = new Vec3(this.posX, this.posY, this.posZ);
                this.shouldGetSpawnPos = false;
            }
            final EventUpdate eventUpdate = new EventUpdate();
            EventHandler.call(eventUpdate);
            super.onUpdate();
            if (this.isRiding()) {
                this.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(this.rotationYaw, this.rotationPitch, this.onGround));
                this.sendQueue.addToSendQueue(new C0CPacketInput(this.moveStrafing, this.moveForward, this.movementInput.jump, this.movementInput.sneak));
            }
            else {
                this.onUpdateWalkingPlayer();
            }
        }
    }
    
    public void onUpdateWalkingPlayer() {
        final EventPreMotion eventPreMotion = new EventPreMotion(this.rotationYaw, this.rotationPitch, this.onGround, this.posX, this.getEntityBoundingBox().minY, this.posZ);
        EventHandler.call(eventPreMotion);
        final boolean flag = this.isSprinting();
        if (flag != this.serverSprintState) {
            if (flag) {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SPRINTING));
            }
            else {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SPRINTING));
            }
            this.reSprint = 1;
            this.serverSprintState = flag;
        }
        final boolean flag2 = this.isSneaking();
        if (flag2 != this.serverSneakState) {
            if (flag2) {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SNEAKING));
            }
            else {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SNEAKING));
            }
            this.serverSneakState = flag2;
        }
        if (this.isCurrentViewEntity()) {
            final double d0 = eventPreMotion.getX() - this.lastReportedPosX;
            final double d2 = eventPreMotion.getY() - this.lastReportedPosY;
            final double d3 = eventPreMotion.getZ() - this.lastReportedPosZ;
            final double d4 = eventPreMotion.getYaw() - this.lastReportedYaw;
            final double d5 = eventPreMotion.getPitch() - this.lastReportedPitch;
            if (ViaMCP.isActive()) {
                ++this.positionUpdateTicks;
            }
            boolean flag3 = d0 * d0 + d2 * d2 + d3 * d3 > 9.0E-4 || this.positionUpdateTicks >= 20;
            final boolean flag4 = d4 != 0.0 || d5 != 0.0;
            if (this.ridingEntity == null) {
                if (flag3 && flag4) {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(eventPreMotion.getX(), eventPreMotion.getY(), eventPreMotion.getZ(), eventPreMotion.getYaw(), eventPreMotion.getPitch(), eventPreMotion.onGround()));
                }
                else if (flag3) {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(eventPreMotion.getX(), eventPreMotion.getY(), eventPreMotion.getZ(), eventPreMotion.onGround()));
                }
                else if (flag4) {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(eventPreMotion.getYaw(), eventPreMotion.getPitch(), eventPreMotion.onGround()));
                }
                else {
                    final int version = viamcp.ViaMCP.getInstance().getVersion();
                    if (ViaMCP.isActive() && version != 47) {
                        if (this.prevOnGround != this.mc.thePlayer.onGround) {
                            this.sendQueue.addToSendQueue(new C03PacketPlayer(eventPreMotion.onGround()));
                        }
                    }
                    else {
                        this.sendQueue.addToSendQueue(new C03PacketPlayer(eventPreMotion.onGround()));
                    }
                }
            }
            else {
                this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.motionX, -999.0, this.motionZ, eventPreMotion.getYaw(), eventPreMotion.getPitch(), eventPreMotion.onGround()));
                flag3 = false;
            }
            if (!ViaMCP.isActive()) {
                ++this.positionUpdateTicks;
            }
            if (flag3) {
                this.lastReportedPosX = eventPreMotion.getX();
                this.lastReportedPosY = eventPreMotion.getY();
                this.lastReportedPosZ = eventPreMotion.getZ();
                this.positionUpdateTicks = 0;
            }
            if (flag4) {
                this.lastReportedYaw = eventPreMotion.getYaw();
                this.lastReportedPitch = eventPreMotion.getPitch();
            }
            final EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
            --thePlayer.rotIncrement;
        }
        final EventPostMotion eventPostMotion = new EventPostMotion();
        EventHandler.call(eventPostMotion);
        this.prevOnGround = this.onGround;
    }
    
    @Override
    public EntityItem dropOneItem(final boolean dropAll) {
        final C07PacketPlayerDigging.Action c07packetplayerdigging$action = dropAll ? C07PacketPlayerDigging.Action.DROP_ALL_ITEMS : C07PacketPlayerDigging.Action.DROP_ITEM;
        this.sendQueue.addToSendQueue(new C07PacketPlayerDigging(c07packetplayerdigging$action, BlockPos.ORIGIN, EnumFacing.DOWN));
        return null;
    }
    
    @Override
    protected void joinEntityItemWithWorld(final EntityItem itemIn) {
    }
    
    public void sendChatMessage(final String message) {
        final EventChat eventChat = new EventChat(message);
        EventHandler.call(eventChat);
        if (eventChat.isCanceled()) {
            return;
        }
        this.sendQueue.addToSendQueue(new C01PacketChatMessage(message));
    }
    
    @Override
    public void swingItem() {
        this.mc.thePlayer.resetCooldown();
        super.swingItem();
        if (viamcp.ViaMCP.getInstance().getVersion() == 47 || !GuiProtocolSelector.active) {
            this.sendQueue.addToSendQueue(new C0APacketAnimation());
        }
        else {
            this.sendQueue.addToSendQueue(new CAnimateHandPacket(Hand.MAIN_HAND));
        }
    }
    
    @Override
    public void respawnPlayer() {
        this.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
    }
    
    @Override
    protected void damageEntity(final DamageSource damageSrc, final float damageAmount) {
        if (!this.isEntityInvulnerable(damageSrc)) {
            this.setHealth(this.getHealth() - damageAmount);
        }
    }
    
    public void closeScreen() {
        this.sendQueue.addToSendQueue(new C0DPacketCloseWindow(this.openContainer.windowId));
        this.closeScreenAndDropStack();
    }
    
    public void closeScreenAndDropStack() {
        this.inventory.setItemStack(null);
        super.closeScreen();
        this.mc.displayGuiScreen(null);
    }
    
    public void setPlayerSPHealth(final float health) {
        if (this.hasValidHealth) {
            final float f = this.getHealth() - health;
            if (f <= 0.0f) {
                this.setHealth(health);
                if (f < 0.0f) {
                    this.hurtResistantTime = this.maxHurtResistantTime / 2;
                }
            }
            else {
                this.lastDamage = f;
                this.setHealth(this.getHealth());
                this.hurtResistantTime = this.maxHurtResistantTime;
                this.damageEntity(DamageSource.generic, f);
                final int n = 10;
                this.maxHurtTime = n;
                this.hurtTime = n;
            }
        }
        else {
            this.setHealth(health);
            this.hasValidHealth = true;
        }
    }
    
    @Override
    public void addStat(final StatBase stat, final int amount) {
        if (stat != null && stat.isIndependent) {
            super.addStat(stat, amount);
        }
    }
    
    @Override
    public void sendPlayerAbilities() {
        this.sendQueue.addToSendQueue(new C13PacketPlayerAbilities(this.capabilities));
    }
    
    @Override
    public boolean isUser() {
        return true;
    }
    
    protected void sendHorseJump() {
        this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.RIDING_JUMP, (int)(this.getHorseJumpPower() * 100.0f)));
    }
    
    public void sendHorseInventory() {
        this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.OPEN_INVENTORY));
    }
    
    public void setClientBrand(final String brand) {
        this.clientBrand = brand;
    }
    
    public String getClientBrand() {
        return this.clientBrand;
    }
    
    public StatFileWriter getStatFileWriter() {
        return this.statWriter;
    }
    
    @Override
    public void addChatComponentMessage(final IChatComponent chatComponent) {
        this.mc.ingameGUI.getChatGUI().printChatMessage(chatComponent);
    }
    
    @Override
    protected boolean pushOutOfBlocks(final double x, final double y, final double z) {
        if (this.noClip) {
            return false;
        }
        final BlockPos blockpos = new BlockPos(x, y, z);
        final double d0 = x - blockpos.getX();
        final double d2 = z - blockpos.getZ();
        if (!this.isOpenBlockSpace(blockpos)) {
            int i = -1;
            double d3 = 9999.0;
            if (this.isOpenBlockSpace(blockpos.west()) && d0 < d3) {
                d3 = d0;
                i = 0;
            }
            if (this.isOpenBlockSpace(blockpos.east()) && 1.0 - d0 < d3) {
                d3 = 1.0 - d0;
                i = 1;
            }
            if (this.isOpenBlockSpace(blockpos.north()) && d2 < d3) {
                d3 = d2;
                i = 4;
            }
            if (this.isOpenBlockSpace(blockpos.south()) && 1.0 - d2 < d3) {
                d3 = 1.0 - d2;
                i = 5;
            }
            final float f = 0.1f;
            if (i == 0) {
                this.motionX = -f;
            }
            if (i == 1) {
                this.motionX = f;
            }
            if (i == 4) {
                this.motionZ = -f;
            }
            if (i == 5) {
                this.motionZ = f;
            }
        }
        return false;
    }
    
    private boolean isOpenBlockSpace(final BlockPos pos) {
        return !this.worldObj.getBlockState(pos).getBlock().isNormalCube() && !this.worldObj.getBlockState(pos.up()).getBlock().isNormalCube();
    }
    
    @Override
    public void setSprinting(final boolean sprinting) {
        super.setSprinting(sprinting);
        this.sprintingTicksLeft = (sprinting ? 600 : 0);
    }
    
    public void setXPStats(final float currentXP, final int maxXP, final int level) {
        this.experience = currentXP;
        this.experienceTotal = maxXP;
        this.experienceLevel = level;
    }
    
    @Override
    public void addChatMessage(final IChatComponent component) {
        this.mc.ingameGUI.getChatGUI().printChatMessage(component);
    }
    
    @Override
    public boolean canCommandSenderUseCommand(final int permLevel, final String commandName) {
        return permLevel <= 0;
    }
    
    @Override
    public BlockPos getPosition() {
        return new BlockPos(this.posX + 0.5, this.posY + 0.5, this.posZ + 0.5);
    }
    
    @Override
    public void playSound(final String name, final float volume, final float pitch) {
        this.worldObj.playSound(this.posX, this.posY, this.posZ, name, volume, pitch, false);
    }
    
    @Override
    public boolean isServerWorld() {
        return true;
    }
    
    public boolean isRidingHorse() {
        return this.ridingEntity != null && this.ridingEntity instanceof EntityHorse && ((EntityHorse)this.ridingEntity).isHorseSaddled();
    }
    
    public float getHorseJumpPower() {
        return this.horseJumpPower;
    }
    
    @Override
    public void openEditSign(final TileEntitySign signTile) {
        this.mc.displayGuiScreen(new GuiEditSign(signTile));
    }
    
    @Override
    public void openEditCommandBlock(final CommandBlockLogic cmdBlockLogic) {
        this.mc.displayGuiScreen(new GuiCommandBlock(cmdBlockLogic));
    }
    
    @Override
    public void displayGUIBook(final ItemStack bookStack) {
        final Item item = bookStack.getItem();
        if (item == Items.writable_book) {
            this.mc.displayGuiScreen(new GuiScreenBook(this, bookStack, true));
        }
    }
    
    @Override
    public void displayGUIChest(final IInventory chestInventory) {
        final String s = (chestInventory instanceof IInteractionObject) ? ((IInteractionObject)chestInventory).getGuiID() : "minecraft:container";
        if ("minecraft:chest".equals(s)) {
            this.mc.displayGuiScreen(new GuiChest(this.inventory, chestInventory));
        }
        else if ("minecraft:hopper".equals(s)) {
            this.mc.displayGuiScreen(new GuiHopper(this.inventory, chestInventory));
        }
        else if ("minecraft:furnace".equals(s)) {
            this.mc.displayGuiScreen(new GuiFurnace(this.inventory, chestInventory));
        }
        else if ("minecraft:brewing_stand".equals(s)) {
            this.mc.displayGuiScreen(new GuiBrewingStand(this.inventory, chestInventory));
        }
        else if ("minecraft:beacon".equals(s)) {
            this.mc.displayGuiScreen(new GuiBeacon(this.inventory, chestInventory));
        }
        else if (!"minecraft:dispenser".equals(s) && !"minecraft:dropper".equals(s)) {
            this.mc.displayGuiScreen(new GuiChest(this.inventory, chestInventory));
        }
        else {
            this.mc.displayGuiScreen(new GuiDispenser(this.inventory, chestInventory));
        }
    }
    
    @Override
    public void displayGUIHorse(final EntityHorse horse, final IInventory horseInventory) {
        this.mc.displayGuiScreen(new GuiScreenHorseInventory(this.inventory, horseInventory, horse));
    }
    
    @Override
    public void displayGui(final IInteractionObject guiOwner) {
        final String s = guiOwner.getGuiID();
        if ("minecraft:crafting_table".equals(s)) {
            this.mc.displayGuiScreen(new GuiCrafting(this.inventory, this.worldObj));
        }
        else if ("minecraft:enchanting_table".equals(s)) {
            this.mc.displayGuiScreen(new GuiEnchantment(this.inventory, this.worldObj, guiOwner));
        }
        else if ("minecraft:anvil".equals(s)) {
            this.mc.displayGuiScreen(new GuiRepair(this.inventory, this.worldObj));
        }
    }
    
    @Override
    public void displayVillagerTradeGui(final IMerchant villager) {
        this.mc.displayGuiScreen(new GuiMerchant(this.inventory, villager, this.worldObj));
    }
    
    @Override
    public void onCriticalHit(final Entity entityHit) {
        if (!Augustus.getInstance().getModuleManager().attackEffects.isToggled()) {
            this.mc.effectRenderer.emitParticleAtEntity(entityHit, EnumParticleTypes.CRIT);
        }
    }
    
    @Override
    public void onEnchantmentCritical(final Entity entityHit) {
        if (!Augustus.getInstance().getModuleManager().attackEffects.isToggled()) {
            this.mc.effectRenderer.emitParticleAtEntity(entityHit, EnumParticleTypes.CRIT_MAGIC);
        }
    }
    
    @Override
    public boolean isSneaking() {
        final boolean flag = this.movementInput != null && this.movementInput.sneak;
        return flag && !this.sleeping;
    }
    
    public void updateEntityActionState() {
        super.updateEntityActionState();
        if (this.isCurrentViewEntity()) {
            this.moveStrafing = this.movementInput.moveStrafe;
            this.moveForward = this.movementInput.moveForward;
            this.isJumping = this.movementInput.jump;
            this.prevRenderArmYaw = this.renderArmYaw;
            this.prevRenderArmPitch = this.renderArmPitch;
            this.renderArmPitch += (float)((Augustus.getInstance().getYawPitchHelper().realPitch - this.renderArmPitch) * 0.5);
            this.renderArmYaw += (float)((Augustus.getInstance().getYawPitchHelper().realYaw - this.renderArmYaw) * 0.5);
        }
    }
    
    protected boolean isCurrentViewEntity() {
        return this.mc.getRenderViewEntity() == this;
    }
    
    @Override
    public void onLivingUpdate() {
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
        }
        else if (this.isPotionActive(Potion.confusion) && this.getActivePotionEffect(Potion.confusion).getDuration() > 60) {
            this.timeInPortal += 0.006666667f;
            if (this.timeInPortal > 1.0f) {
                this.timeInPortal = 1.0f;
            }
        }
        else {
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
        final boolean flag = this.movementInput.jump;
        final boolean flag2 = this.movementInput.sneak;
        final float f = 0.8f;
        final boolean flag3 = this.movementInput.moveForward >= f;
        final float forward = this.movementInput.moveForward;
        final float strafe = this.movementInput.moveStrafe;
        this.movementInput.updatePlayerMoveState();
        final EventSilentMove eventSilentMove = new EventSilentMove(Augustus.getInstance().getYawPitchHelper().realYaw);
        EventHandler.call(eventSilentMove);
        if (eventSilentMove.isSilent()) {
            final float[] floats = this.mySilentStrafe(this.movementInput.moveStrafe, this.movementInput.moveForward, eventSilentMove.getYaw(), eventSilentMove.isAdvanced());
            final float diffForward = forward - floats[1];
            final float diffStrafe = strafe - floats[0];
            if (this.movementInput.sneak) {
                this.movementInput.moveStrafe = MathHelper.clamp_float(floats[0], -0.3f, 0.3f);
                this.movementInput.moveForward = MathHelper.clamp_float(floats[1], -0.3f, 0.3f);
            }
            else {
                if (diffForward >= 2.0f) {
                    floats[1] = 0.0f;
                }
                if (diffForward <= -2.0f) {
                    floats[1] = 0.0f;
                }
                if (diffStrafe >= 2.0f) {
                    floats[0] = 0.0f;
                }
                if (diffStrafe <= -2.0f) {
                    floats[0] = 0.0f;
                }
                this.movementInput.moveStrafe = MathHelper.clamp_float(floats[0], -1.0f, 1.0f);
                this.movementInput.moveForward = MathHelper.clamp_float(floats[1], -1.0f, 1.0f);
            }
        }
        final EventNoSlow eventNoSlow = new EventNoSlow(0.2f, 0.2f);
        EventHandler.call(eventNoSlow);
        if (this.isUsingItem() && !this.isRiding()) {
            final MovementInput movementInput = this.movementInput;
            movementInput.moveStrafe *= eventNoSlow.getMoveStrafe();
            final MovementInput movementInput2 = this.movementInput;
            movementInput2.moveForward *= eventNoSlow.getMoveForward();
            this.sprintToggleTimer = 0;
        }
        this.pushOutOfBlocks(this.posX - this.width * 0.35, this.getEntityBoundingBox().minY + 0.5, this.posZ + this.width * 0.35);
        this.pushOutOfBlocks(this.posX - this.width * 0.35, this.getEntityBoundingBox().minY + 0.5, this.posZ - this.width * 0.35);
        this.pushOutOfBlocks(this.posX + this.width * 0.35, this.getEntityBoundingBox().minY + 0.5, this.posZ - this.width * 0.35);
        this.pushOutOfBlocks(this.posX + this.width * 0.35, this.getEntityBoundingBox().minY + 0.5, this.posZ + this.width * 0.35);
        final boolean flag4 = this.getFoodStats().getFoodLevel() > 6.0f || this.capabilities.allowFlying;
        final float movef = this.movementInput.moveForward;
        if (this.reSprint == 2) {
            this.movementInput.moveForward = 0.0f;
        }
        if (this.onGround && !flag2 && !flag3 && this.movementInput.moveForward >= f && !this.isSprinting() && flag4 && (!this.isUsingItem() || eventNoSlow.isSprint()) && !this.isPotionActive(Potion.blindness)) {
            if (this.sprintToggleTimer <= 0 && !this.mc.gameSettings.keyBindSprint.isKeyDown()) {
                this.sprintToggleTimer = 7;
            }
            else {
                this.setSprinting(true);
            }
        }
        if (!this.isSprinting() && this.movementInput.moveForward >= f && flag4 && (!this.isUsingItem() || eventNoSlow.isSprint()) && !this.isPotionActive(Potion.blindness) && this.mc.gameSettings.keyBindSprint.isKeyDown()) {
            this.setSprinting(true);
        }
        if (this.isSprinting() && (this.movementInput.moveForward < f || this.isCollidedHorizontally || !flag4)) {
            this.setSprinting(false);
        }
        if (!eventNoSlow.isSprint() && this.isUsingItem() && !this.isRiding()) {
            this.setSprinting(false);
        }
        if (this.reSprint == 2) {
            this.movementInput.moveForward = movef;
            this.reSprint = 1;
        }
        if (this.capabilities.allowFlying) {
            if (this.mc.playerController.isSpectatorMode()) {
                if (!this.capabilities.isFlying) {
                    this.capabilities.isFlying = true;
                    this.sendPlayerAbilities();
                }
            }
            else if (!flag && this.movementInput.jump) {
                if (this.flyToggleTimer == 0) {
                    this.flyToggleTimer = 7;
                }
                else {
                    this.capabilities.isFlying = !this.capabilities.isFlying;
                    this.sendPlayerAbilities();
                    this.flyToggleTimer = 0;
                }
            }
        }
        if (this.capabilities.isFlying && this.isCurrentViewEntity()) {
            if (this.movementInput.sneak) {
                this.motionY -= this.capabilities.getFlySpeed() * 3.0f;
            }
            if (this.movementInput.jump) {
                this.motionY += this.capabilities.getFlySpeed() * 3.0f;
            }
        }
        if (this.isRidingHorse()) {
            if (this.horseJumpPowerCounter < 0) {
                ++this.horseJumpPowerCounter;
                if (this.horseJumpPowerCounter == 0) {
                    this.horseJumpPower = 0.0f;
                }
            }
            if (flag && !this.movementInput.jump) {
                this.horseJumpPowerCounter = -10;
                this.sendHorseJump();
            }
            else if (!flag && this.movementInput.jump) {
                this.horseJumpPowerCounter = 0;
                this.horseJumpPower = 0.0f;
            }
            else if (flag) {
                ++this.horseJumpPowerCounter;
                if (this.horseJumpPowerCounter < 10) {
                    this.horseJumpPower = this.horseJumpPowerCounter * 0.1f;
                }
                else {
                    this.horseJumpPower = 0.8f + 2.0f / (this.horseJumpPowerCounter - 9) * 0.1f;
                }
            }
        }
        else {
            this.horseJumpPower = 0.0f;
        }
        super.onLivingUpdate();
        if (this.onGround && this.capabilities.isFlying && !this.mc.playerController.isSpectatorMode()) {
            this.capabilities.isFlying = false;
            this.sendPlayerAbilities();
        }
    }
    
    public float[] mySilentStrafe(final float strafe, final float forward, final float yaw, final boolean advanced) {
        final Minecraft mc = Minecraft.getMinecraft();
        final float diff = MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw);
        float newForward = 0.0f;
        float newStrafe = 0.0f;
        if (!advanced) {
            if (diff >= 22.5 && diff < 67.5) {
                newStrafe += strafe;
                newForward += forward;
                newStrafe -= forward;
                newForward += strafe;
            }
            else if (diff >= 67.5 && diff < 112.5) {
                newStrafe -= forward;
                newForward += strafe;
            }
            else if (diff >= 112.5 && diff < 157.5) {
                newStrafe -= strafe;
                newForward -= forward;
                newStrafe -= forward;
                newForward += strafe;
            }
            else if (diff >= 157.5 || diff <= -157.5) {
                newStrafe -= strafe;
                newForward -= forward;
            }
            else if (diff > -157.5 && diff <= -112.5) {
                newStrafe -= strafe;
                newForward -= forward;
                newStrafe += forward;
                newForward -= strafe;
            }
            else if (diff > -112.5 && diff <= -67.5) {
                newStrafe += forward;
                newForward -= strafe;
            }
            else if (diff > -67.5 && diff <= -22.5) {
                newStrafe += strafe;
                newForward += forward;
                newStrafe += forward;
                newForward -= strafe;
            }
            else {
                newStrafe += strafe;
                newForward += forward;
            }
            return new float[] { newStrafe, newForward };
        }
        final double[] realMotion = MoveUtil.getMotion(0.22, strafe, forward, Augustus.getInstance().getYawPitchHelper().realYaw);
        final double[] array;
        final double[] realPos = array = new double[] { mc.thePlayer.posX, mc.thePlayer.posZ };
        final int n = 0;
        array[n] += realMotion[0];
        final double[] array2 = realPos;
        final int n2 = 1;
        array2[n2] += realMotion[1];
        final ArrayList<float[]> possibleForwardStrafe = new ArrayList<float[]>();
        int i = 0;
        boolean b = false;
        while (!b) {
            newForward = 0.0f;
            newStrafe = 0.0f;
            if (i == 0) {
                newStrafe += strafe;
                newForward += forward;
                newStrafe -= forward;
                newForward += strafe;
                possibleForwardStrafe.add(new float[] { newForward, newStrafe });
            }
            else if (i == 1) {
                newStrafe -= forward;
                newForward += strafe;
                possibleForwardStrafe.add(new float[] { newForward, newStrafe });
            }
            else if (i == 2) {
                newStrafe -= strafe;
                newForward -= forward;
                newStrafe -= forward;
                newForward += strafe;
                possibleForwardStrafe.add(new float[] { newForward, newStrafe });
            }
            else if (i == 3) {
                newStrafe -= strafe;
                newForward -= forward;
                possibleForwardStrafe.add(new float[] { newForward, newStrafe });
            }
            else if (i == 4) {
                newStrafe -= strafe;
                newForward -= forward;
                newStrafe += forward;
                newForward -= strafe;
                possibleForwardStrafe.add(new float[] { newForward, newStrafe });
            }
            else if (i == 5) {
                newStrafe += forward;
                newForward -= strafe;
                possibleForwardStrafe.add(new float[] { newForward, newStrafe });
            }
            else if (i == 6) {
                newStrafe += strafe;
                newForward += forward;
                newStrafe += forward;
                newForward -= strafe;
                possibleForwardStrafe.add(new float[] { newForward, newStrafe });
            }
            else {
                newStrafe += strafe;
                newForward += forward;
                possibleForwardStrafe.add(new float[] { newForward, newStrafe });
                b = true;
            }
            ++i;
        }
        double distance = 5000.0;
        float[] floats = new float[2];
        for (final float[] flo : possibleForwardStrafe) {
            if (flo[0] > 1.0f) {
                flo[0] = 1.0f;
            }
            else if (flo[0] < -1.0f) {
                flo[0] = -1.0f;
            }
            if (flo[1] > 1.0f) {
                flo[1] = 1.0f;
            }
            else if (flo[1] < -1.0f) {
                flo[1] = -1.0f;
            }
            final double[] motion2;
            final double[] motion = motion2 = MoveUtil.getMotion(0.22, flo[1], flo[0], this.rotationYaw);
            final int n3 = 0;
            motion2[n3] += mc.thePlayer.posX;
            final double[] array3 = motion;
            final int n4 = 1;
            array3[n4] += mc.thePlayer.posZ;
            final double diffX = Math.abs(realPos[0] - motion[0]);
            final double diffZ = Math.abs(realPos[1] - motion[1]);
            final double d0 = diffX * diffX + diffZ * diffZ;
            if (d0 < distance) {
                distance = d0;
                floats = flo;
            }
        }
        return new float[] { floats[1], floats[0] };
    }
    
    @Override
    public MovingObjectPosition customRayTrace(final double blockReachDistance, final float partialTicks, final float yaw, final float pitch) {
        final Vec3 vec3 = this.getPositionEyes(partialTicks);
        final Vec3 vec4 = this.customGetLook(partialTicks, yaw, pitch);
        final Vec3 vec5 = vec3.addVector(vec4.xCoord * blockReachDistance, vec4.yCoord * blockReachDistance, vec4.zCoord * blockReachDistance);
        return this.worldObj.rayTraceBlocks(vec3, vec5, false, false, true);
    }
    
    public MovingObjectPosition customRayTrace(final double blockReachDistance, final float partialTicks, final float yaw, final float pitch, final boolean pitchPredict) {
        final Vec3 vec3 = this.getPositionEyes(partialTicks);
        final Vec3 vec4 = this.customGetLook(partialTicks, yaw, pitch, pitchPredict);
        final Vec3 vec5 = vec3.addVector(vec4.xCoord * blockReachDistance, vec4.yCoord * blockReachDistance, vec4.zCoord * blockReachDistance);
        return this.worldObj.rayTraceBlocks(vec3, vec5, false, false, true);
    }
    
    public MovingObjectPosition customRayTrace(final double blockReachDistance, final float partialTicks, final float yaw, final float pitch, final float lastYaw, final float lastPitch) {
        final Vec3 vec3 = this.getPositionEyes(partialTicks);
        final Vec3 vec4 = this.customGetLook(partialTicks, yaw, pitch, lastYaw, lastPitch);
        final Vec3 vec5 = vec3.addVector(vec4.xCoord * blockReachDistance, vec4.yCoord * blockReachDistance, vec4.zCoord * blockReachDistance);
        return this.worldObj.rayTraceBlocks(vec3, vec5, false, false, true);
    }
    
    public MovingObjectPosition customRayTrace(final double blockReachDistance, final float partialTicks, final float yaw, final float pitch, final double[] xyz, final double[] lastXYZ) {
        final Vec3 vec3 = this.getCustomPositionEyes(partialTicks, xyz, lastXYZ);
        final Vec3 vec4 = this.customGetLook(partialTicks, yaw, pitch);
        final Vec3 vec5 = vec3.addVector(vec4.xCoord * blockReachDistance, vec4.yCoord * blockReachDistance, vec4.zCoord * blockReachDistance);
        return this.worldObj.rayTraceBlocks(vec3, vec5, false, false, true);
    }
    
    private Vec3 customGetLook(final float partialTicks, final float yaw, final float pitch) {
        if (partialTicks == 1.0f || partialTicks == 2.0f) {
            return this.getVectorForRotation(pitch, yaw);
        }
        final float f = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * partialTicks;
        final float f2 = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * partialTicks;
        return this.getVectorForRotation(f, f2);
    }
    
    private Vec3 customGetLook(final float partialTicks, final float yaw, final float pitch, final boolean pitchPredict) {
        if (partialTicks == 1.0f) {
            return this.getVectorForRotation(pitch, yaw);
        }
        final float f = pitchPredict ? (this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * partialTicks) : pitch;
        final float f2 = pitchPredict ? (this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * partialTicks) : yaw;
        return this.getVectorForRotation(f, f2);
    }
    
    private Vec3 customGetLook(final float partialTicks, final float yaw, final float pitch, final float lastYaw, final float lastPitch) {
        if (partialTicks == 1.0f) {
            return this.getVectorForRotation(pitch, yaw);
        }
        final float f = lastPitch + (pitch - lastPitch) * partialTicks;
        final float f2 = lastYaw + (yaw - lastYaw) * partialTicks;
        return this.getVectorForRotation(f, f2);
    }
    
    public Vec3 getCustomPositionEyes(final float partialTicks, final double[] xyz, final double[] lastXYZ) {
        if (partialTicks == 1.0f) {
            return new Vec3(xyz[0], xyz[1] + this.getEyeHeight(), xyz[2]);
        }
        final double d0 = lastXYZ[0] + (xyz[0] - lastXYZ[0]) * partialTicks;
        final double d2 = lastXYZ[1] + (xyz[1] - lastXYZ[1]) * partialTicks + this.getEyeHeight();
        final double d3 = lastXYZ[2] + (xyz[2] - lastXYZ[2]) * partialTicks;
        return new Vec3(d0, d2, d3);
    }
    
    public float getXZDistanceToEntity(final Entity entityIn) {
        final float f = (float)(this.posX - entityIn.posX);
        final float f2 = (float)(this.posZ - entityIn.posZ);
        return MathHelper.sqrt_float(f * f + f2 * f2);
    }
    
    public void resetCooldown() {
        this.ticksSinceLastSwing = 0;
    }
    
    public GuiScreen getServerInv() {
        return this.serverInv;
    }
    
    public void setServerInv(final GuiScreen serverInv) {
        this.serverInv = serverInv;
    }
    
    public Vec3 getSpawnPos() {
        return this.spawnPos;
    }
    
    public boolean isServerSprintState() {
        return this.serverSprintState;
    }
    
    public void setServerSprintState(final boolean serverSprintState) {
        this.serverSprintState = serverSprintState;
    }
    
    public Vec3 getLastServerPosition() {
        return this.lastServerPosition;
    }
    
    public void setLastServerPosition(final Vec3 lastServerPosition) {
        this.lastServerPosition = lastServerPosition;
    }
    
    public Vec3 getSeverPosition() {
        return this.severPosition;
    }
    
    public void setSeverPosition(final Vec3 severPosition) {
        this.severPosition = severPosition;
    }
    
    @Override
    public void moveEntity(double x, double y, double z) {
        final EventMoveEntity eventMoveEntity = new EventMoveEntity(x, y, z);
        EventHandler.call(eventMoveEntity);
        x = eventMoveEntity.getX();
        y = eventMoveEntity.getY();
        z = eventMoveEntity.getZ();
        if (eventMoveEntity.isCanceled()) {
            return;
        }
        super.moveEntity(x, y, z);
    }
}
