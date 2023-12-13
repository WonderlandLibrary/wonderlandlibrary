// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.entity;

import exhibition.event.impl.EventPushBlock;
import exhibition.module.Module;
import exhibition.module.impl.movement.NoSlowdown;
import exhibition.Client;
import net.minecraft.potion.Potion;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.EnumParticleTypes;
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
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C01PacketChatMessage;
import exhibition.event.impl.EventChat;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import exhibition.event.impl.EventMotion;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.client.audio.ISound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.audio.MovingSoundMinecartRiding;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.Entity;
import exhibition.event.EventSystem;
import exhibition.event.impl.EventMove;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovementInput;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.client.network.NetHandlerPlayClient;

public class EntityPlayerSP extends AbstractClientPlayer
{
    public final NetHandlerPlayClient sendQueue;
    private final StatFileWriter field_146108_bO;
    private double field_175172_bI;
    private double field_175166_bJ;
    private double field_175167_bK;
    private float field_175164_bL;
    private float field_175165_bM;
    private boolean field_175170_bN;
    private boolean field_175171_bO;
    private int field_175168_bP;
    private boolean field_175169_bQ;
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
    private static final String __OBFID = "CL_00000938";
    
    public EntityPlayerSP(final Minecraft mcIn, final World worldIn, final NetHandlerPlayClient p_i46278_3_, final StatFileWriter p_i46278_4_) {
        super(worldIn, p_i46278_3_.func_175105_e());
        this.sendQueue = p_i46278_3_;
        this.field_146108_bO = p_i46278_4_;
        this.mc = mcIn;
        this.dimension = 0;
    }
    
    @Override
    public boolean isEntityInsideOpaqueBlock() {
        return super.isEntityInsideOpaqueBlock();
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        return false;
    }
    
    @Override
    public void heal(final float p_70691_1_) {
    }
    
    @Override
    public void moveEntity(final double x, final double y, final double z) {
        final EventMove em = (EventMove)EventSystem.getInstance(EventMove.class);
        em.fire(x, y, z);
        super.moveEntity(em.getX(), em.getY(), em.getZ());
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
            super.onUpdate();
            if (this.isRiding()) {
                this.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(this.rotationYaw, this.rotationPitch, this.onGround));
                this.sendQueue.addToSendQueue(new C0CPacketInput(this.moveStrafing, this.moveForward, this.movementInput.jump, this.movementInput.sneak));
            }
            else {
                this.motionUpdates();
            }
        }
    }
    
    public void motionUpdates() {
        final EventMotion em = (EventMotion)EventSystem.getInstance(EventMotion.class);
        em.fire(this.posY, this.rotationYaw, this.rotationPitch, this.onGround);
        if (em.isCancelled()) {
            em.fire();
            return;
        }
        final boolean var1 = this.isSprinting();
        if (var1 != this.field_175171_bO) {
            if (var1) {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SPRINTING));
            }
            else {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SPRINTING));
            }
            this.field_175171_bO = var1;
        }
        final boolean var2 = this.isSneaking();
        if (var2 != this.field_175170_bN) {
            if (var2) {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SNEAKING));
            }
            else {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SNEAKING));
            }
            this.field_175170_bN = var2;
        }
        if (this.func_175160_A()) {
            final double var3 = this.posX - this.field_175172_bI;
            final double var4 = em.getY() - this.field_175166_bJ;
            final double var5 = this.posZ - this.field_175167_bK;
            final double var6 = em.getYaw() - this.field_175164_bL;
            final double var7 = em.getPitch() - this.field_175165_bM;
            boolean var8 = var3 * var3 + var4 * var4 + var5 * var5 > 9.0E-4 || this.field_175168_bP >= 20;
            final boolean var9 = var6 != 0.0 || var7 != 0.0;
            if (this.ridingEntity == null) {
                if ((var8 && var9) || em.shouldAlwaysSend()) {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.posX, em.getY(), this.posZ, em.getYaw(), em.getPitch(), em.isOnground()));
                }
                else if (var8) {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.posX, em.getY(), this.posZ, em.isOnground()));
                }
                else if (var9) {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(em.getYaw(), em.getPitch(), em.isOnground()));
                }
                else {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer(em.isOnground()));
                }
            }
            else {
                this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.motionX, -999.0, this.motionZ, this.rotationYaw, this.rotationPitch, this.onGround));
                var8 = false;
            }
            ++this.field_175168_bP;
            if (var8) {
                this.field_175172_bI = this.posX;
                this.field_175166_bJ = this.getEntityBoundingBox().minY;
                this.field_175167_bK = this.posZ;
                this.field_175168_bP = 0;
            }
            if (var9) {
                this.field_175164_bL = em.getYaw();
                this.field_175165_bM = em.getPitch();
            }
        }
        em.fire();
    }
    
    @Override
    public EntityItem dropOneItem(final boolean p_71040_1_) {
        final C07PacketPlayerDigging.Action var2 = p_71040_1_ ? C07PacketPlayerDigging.Action.DROP_ALL_ITEMS : C07PacketPlayerDigging.Action.DROP_ITEM;
        this.sendQueue.addToSendQueue(new C07PacketPlayerDigging(var2, BlockPos.ORIGIN, EnumFacing.DOWN));
        return null;
    }
    
    @Override
    protected void joinEntityItemWithWorld(final EntityItem p_71012_1_) {
    }
    
    public void sendChatMessage(final String text) {
        final EventChat ec = (EventChat)EventSystem.getInstance(EventChat.class);
        ec.fire(text);
        if (ec.isCancelled()) {
            return;
        }
        this.sendQueue.addToSendQueue(new C01PacketChatMessage(ec.getText()));
    }
    
    @Override
    public void swingItem() {
        super.swingItem();
        this.sendQueue.addToSendQueue(new C0APacketAnimation());
    }
    
    @Override
    public void respawnPlayer() {
        this.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
    }
    
    @Override
    protected void damageEntity(final DamageSource p_70665_1_, final float p_70665_2_) {
        if (!this.func_180431_b(p_70665_1_)) {
            this.setHealth(this.getHealth() - p_70665_2_);
        }
    }
    
    public void closeScreen() {
        this.sendQueue.addToSendQueue(new C0DPacketCloseWindow(this.openContainer.windowId));
        this.func_175159_q();
    }
    
    public void func_175159_q() {
        this.inventory.setItemStack(null);
        super.closeScreen();
        this.mc.displayGuiScreen(null);
    }
    
    public void setPlayerSPHealth(final float p_71150_1_) {
        if (this.field_175169_bQ) {
            final float var2 = this.getHealth() - p_71150_1_;
            if (var2 <= 0.0f) {
                this.setHealth(p_71150_1_);
                if (var2 < 0.0f) {
                    this.hurtResistantTime = this.maxHurtResistantTime / 2;
                }
            }
            else {
                this.lastDamage = var2;
                this.setHealth(this.getHealth());
                this.hurtResistantTime = this.maxHurtResistantTime;
                this.damageEntity(DamageSource.generic, var2);
                final int n = 10;
                this.maxHurtTime = n;
                this.hurtTime = n;
            }
        }
        else {
            this.setHealth(p_71150_1_);
            this.field_175169_bQ = true;
        }
    }
    
    @Override
    public void addStat(final StatBase p_71064_1_, final int p_71064_2_) {
        if (p_71064_1_ != null && p_71064_1_.isIndependent) {
            super.addStat(p_71064_1_, p_71064_2_);
        }
    }
    
    @Override
    public void sendPlayerAbilities() {
        this.sendQueue.addToSendQueue(new C13PacketPlayerAbilities(this.capabilities));
    }
    
    @Override
    public boolean func_175144_cb() {
        return true;
    }
    
    protected void sendHorseJump() {
        this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.RIDING_JUMP, (int)(this.getHorseJumpPower() * 100.0f)));
    }
    
    public void func_175163_u() {
        this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.OPEN_INVENTORY));
    }
    
    public void func_175158_f(final String p_175158_1_) {
        this.clientBrand = p_175158_1_;
    }
    
    public String getClientBrand() {
        return this.clientBrand;
    }
    
    public StatFileWriter getStatFileWriter() {
        return this.field_146108_bO;
    }
    
    @Override
    public void addChatComponentMessage(final IChatComponent p_146105_1_) {
        this.mc.ingameGUI.getChatGUI().printChatMessage(p_146105_1_);
    }
    
    @Override
    protected boolean pushOutOfBlocks(final double x, final double y, final double z) {
        if (this.noClip) {
            return false;
        }
        final BlockPos var7 = new BlockPos(x, y, z);
        final double var8 = x - var7.getX();
        final double var9 = z - var7.getZ();
        if (!this.func_175162_d(var7)) {
            byte var10 = -1;
            double var11 = 9999.0;
            if (this.func_175162_d(var7.offsetWest()) && var8 < var11) {
                var11 = var8;
                var10 = 0;
            }
            if (this.func_175162_d(var7.offsetEast()) && 1.0 - var8 < var11) {
                var11 = 1.0 - var8;
                var10 = 1;
            }
            if (this.func_175162_d(var7.offsetNorth()) && var9 < var11) {
                var11 = var9;
                var10 = 4;
            }
            if (this.func_175162_d(var7.offsetSouth()) && 1.0 - var9 < var11) {
                var11 = 1.0 - var9;
                var10 = 5;
            }
            final float var12 = 0.1f;
            if (var10 == 0) {
                this.motionX = -var12;
            }
            if (var10 == 1) {
                this.motionX = var12;
            }
            if (var10 == 4) {
                this.motionZ = -var12;
            }
            if (var10 == 5) {
                this.motionZ = var12;
            }
        }
        return false;
    }
    
    private boolean func_175162_d(final BlockPos p_175162_1_) {
        return !this.worldObj.getBlockState(p_175162_1_).getBlock().isNormalCube() && !this.worldObj.getBlockState(p_175162_1_.offsetUp()).getBlock().isNormalCube();
    }
    
    @Override
    public void setSprinting(final boolean sprinting) {
        super.setSprinting(sprinting);
        this.sprintingTicksLeft = (sprinting ? 600 : 0);
    }
    
    public void setXPStats(final float p_71152_1_, final int p_71152_2_, final int p_71152_3_) {
        this.experience = p_71152_1_;
        this.experienceTotal = p_71152_2_;
        this.experienceLevel = p_71152_3_;
    }
    
    @Override
    public void addChatMessage(final IChatComponent message) {
        this.mc.ingameGUI.getChatGUI().printChatMessage(message);
    }
    
    @Override
    public boolean canCommandSenderUseCommand(final int permissionLevel, final String command) {
        return permissionLevel <= 0;
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
    public void func_175141_a(final TileEntitySign p_175141_1_) {
        this.mc.displayGuiScreen(new GuiEditSign(p_175141_1_));
    }
    
    @Override
    public void func_146095_a(final CommandBlockLogic p_146095_1_) {
        this.mc.displayGuiScreen(new GuiCommandBlock(p_146095_1_));
    }
    
    @Override
    public void displayGUIBook(final ItemStack bookStack) {
        final Item var2 = bookStack.getItem();
        if (var2 == Items.writable_book) {
            this.mc.displayGuiScreen(new GuiScreenBook(this, bookStack, true));
        }
    }
    
    @Override
    public void displayGUIChest(final IInventory chestInventory) {
        final String var2 = (chestInventory instanceof IInteractionObject) ? ((IInteractionObject)chestInventory).getGuiID() : "minecraft:container";
        if ("minecraft:chest".equals(var2)) {
            this.mc.displayGuiScreen(new GuiChest(this.inventory, chestInventory));
        }
        else if ("minecraft:hopper".equals(var2)) {
            this.mc.displayGuiScreen(new GuiHopper(this.inventory, chestInventory));
        }
        else if ("minecraft:furnace".equals(var2)) {
            this.mc.displayGuiScreen(new GuiFurnace(this.inventory, chestInventory));
        }
        else if ("minecraft:brewing_stand".equals(var2)) {
            this.mc.displayGuiScreen(new GuiBrewingStand(this.inventory, chestInventory));
        }
        else if ("minecraft:beacon".equals(var2)) {
            this.mc.displayGuiScreen(new GuiBeacon(this.inventory, chestInventory));
        }
        else if (!"minecraft:dispenser".equals(var2) && !"minecraft:dropper".equals(var2)) {
            this.mc.displayGuiScreen(new GuiChest(this.inventory, chestInventory));
        }
        else {
            this.mc.displayGuiScreen(new GuiDispenser(this.inventory, chestInventory));
        }
    }
    
    @Override
    public void displayGUIHorse(final EntityHorse p_110298_1_, final IInventory p_110298_2_) {
        this.mc.displayGuiScreen(new GuiScreenHorseInventory(this.inventory, p_110298_2_, p_110298_1_));
    }
    
    @Override
    public void displayGui(final IInteractionObject guiOwner) {
        final String var2 = guiOwner.getGuiID();
        if ("minecraft:crafting_table".equals(var2)) {
            this.mc.displayGuiScreen(new GuiCrafting(this.inventory, this.worldObj));
        }
        else if ("minecraft:enchanting_table".equals(var2)) {
            this.mc.displayGuiScreen(new GuiEnchantment(this.inventory, this.worldObj, guiOwner));
        }
        else if ("minecraft:anvil".equals(var2)) {
            this.mc.displayGuiScreen(new GuiRepair(this.inventory, this.worldObj));
        }
    }
    
    @Override
    public void displayVillagerTradeGui(final IMerchant villager) {
        this.mc.displayGuiScreen(new GuiMerchant(this.inventory, villager, this.worldObj));
    }
    
    @Override
    public void onCriticalHit(final Entity p_71009_1_) {
        this.mc.effectRenderer.func_178926_a(p_71009_1_, EnumParticleTypes.CRIT);
    }
    
    @Override
    public void onEnchantmentCritical(final Entity p_71047_1_) {
        this.mc.effectRenderer.func_178926_a(p_71047_1_, EnumParticleTypes.CRIT_MAGIC);
    }
    
    @Override
    public boolean isSneaking() {
        final boolean var1 = this.movementInput != null && this.movementInput.sneak;
        return var1 && !this.sleeping;
    }
    
    public void updateEntityActionState() {
        super.updateEntityActionState();
        if (this.func_175160_A()) {
            this.moveStrafing = this.movementInput.moveStrafe;
            this.moveForward = this.movementInput.moveForward;
            this.isJumping = this.movementInput.jump;
            this.prevRenderArmYaw = this.renderArmYaw;
            this.prevRenderArmPitch = this.renderArmPitch;
            this.renderArmPitch += (float)((this.rotationPitch - this.renderArmPitch) * 0.5);
            this.renderArmYaw += (float)((this.rotationYaw - this.renderArmYaw) * 0.5);
        }
    }
    
    protected boolean func_175160_A() {
        return this.mc.func_175606_aa() == this;
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
                this.mc.getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("portal.trigger"), this.rand.nextFloat() * 0.4f + 0.8f));
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
        final boolean var1 = this.movementInput.jump;
        final boolean var2 = this.movementInput.sneak;
        final float var3 = 0.8f;
        final boolean var4 = this.movementInput.moveForward >= var3;
        this.movementInput.updatePlayerMoveState();
        if (this.isUsingItem() && !this.isRiding() && !Client.getModuleManager().isEnabled(NoSlowdown.class)) {
            final MovementInput movementInput = this.movementInput;
            movementInput.moveStrafe *= 0.2f;
            final MovementInput movementInput2 = this.movementInput;
            movementInput2.moveForward *= 0.2f;
            this.sprintToggleTimer = 0;
        }
        final EventPushBlock epb = (EventPushBlock)EventSystem.getInstance(EventPushBlock.class);
        if (!epb.isCancelled()) {
            this.pushOutOfBlocks(this.posX - this.width * 0.35, this.getEntityBoundingBox().minY + 0.5, this.posZ + this.width * 0.35);
            this.pushOutOfBlocks(this.posX - this.width * 0.35, this.getEntityBoundingBox().minY + 0.5, this.posZ - this.width * 0.35);
            this.pushOutOfBlocks(this.posX + this.width * 0.35, this.getEntityBoundingBox().minY + 0.5, this.posZ - this.width * 0.35);
            this.pushOutOfBlocks(this.posX + this.width * 0.35, this.getEntityBoundingBox().minY + 0.5, this.posZ + this.width * 0.35);
        }
        final boolean var5 = this.getFoodStats().getFoodLevel() > 6.0f || this.capabilities.allowFlying;
        if (this.onGround && !var2 && !var4 && this.movementInput.moveForward >= var3 && !this.isSprinting() && var5 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness)) {
            if (this.sprintToggleTimer <= 0 && !this.mc.gameSettings.keyBindSprint.getIsKeyPressed()) {
                this.sprintToggleTimer = 7;
            }
            else {
                this.setSprinting(true);
            }
        }
        if (!this.isSprinting() && this.movementInput.moveForward >= var3 && var5 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness) && this.mc.gameSettings.keyBindSprint.getIsKeyPressed()) {
            this.setSprinting(true);
        }
        if (this.isSprinting() && (this.movementInput.moveForward < var3 || this.isCollidedHorizontally || !var5)) {
            this.setSprinting(false);
        }
        if (this.capabilities.allowFlying) {
            if (this.mc.playerController.isSpectatorMode()) {
                if (!this.capabilities.isFlying) {
                    this.capabilities.isFlying = true;
                    this.sendPlayerAbilities();
                }
            }
            else if (!var1 && this.movementInput.jump) {
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
        if (this.capabilities.isFlying && this.func_175160_A()) {
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
            if (var1 && !this.movementInput.jump) {
                this.horseJumpPowerCounter = -10;
                this.sendHorseJump();
            }
            else if (!var1 && this.movementInput.jump) {
                this.horseJumpPowerCounter = 0;
                this.horseJumpPower = 0.0f;
            }
            else if (var1) {
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
    
    public float getDirection() {
        float var1 = this.rotationYaw;
        if (this.moveForward < 0.0f) {
            var1 += 180.0f;
        }
        float forward = 1.0f;
        if (this.moveForward < 0.0f) {
            forward = -0.5f;
        }
        else if (this.moveForward > 0.0f) {
            forward = 0.5f;
        }
        else {
            forward = 1.0f;
        }
        if (this.moveStrafing > 0.0f) {
            var1 -= 90.0f * forward;
        }
        if (this.moveStrafing < 0.0f) {
            var1 += 90.0f * forward;
        }
        var1 *= 0.017453292f;
        return var1;
    }
}
