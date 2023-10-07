package net.minecraft.client.entity;

import cc.novoline.Novoline;
import cc.novoline.events.EventManager;
import cc.novoline.events.events.*;
import cc.novoline.modules.combat.Criticals;
import cc.novoline.modules.combat.KillAura;
import cc.novoline.modules.misc.GuiMove;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSoundMinecartRiding;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.*;
import net.minecraft.potion.Potion;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.*;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.optifine.RenderEnv;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.lang.Math.toRadians;
import static net.minecraft.potion.Potion.*;
import static net.minecraft.util.MathHelper.ceiling_double_int;

public class EntityPlayerSP extends AbstractClientPlayer {

    private List<Integer> list = new CopyOnWriteArrayList<>();
    int maxY;

    public final NetHandlerPlayClient connection;
    private final StatFileWriter statWriter;
    private MovementInput movementInput;
    public float renderArmYaw;
    public float renderArmPitch;
    public float prevRenderArmYaw;
    public float prevRenderArmPitch;
    /**
     * The amount of time an entity has been in a Portal
     */
    public float timeInPortal;
    /**
     * The amount of time an entity has been in a Portal the previous tick
     */
    public float prevTimeInPortal;
    protected Minecraft mc;
    /**
     * Used to tell if the player pressed forward twice. If this is at 0 and it's pressed (And they are allowed to
     * sprint, aka enough food on the ground etc) it sets this to 7. If it's pressed and it's greater than 0 enable
     * sprinting.
     */
    protected int sprintToggleTimer;
    /**
     * Ticks left before sprinting is disabled.
     */
    private int sprintingTicksLeft;
    /**
     * The last X position which was transmitted to the server, used to determine when the X position changes and needs
     * to be re-trasmitted
     */
    private double lastReportedPosX;
    /**
     * The last Y position which was transmitted to the server, used to determine when the Y position changes and needs
     * to be re-transmitted
     */
    private double lastReportedPosY;
    /**
     * The last Z position which was transmitted to the server, used to determine when the Z position changes and needs
     * to be re-transmitted
     */
    private double lastReportedPosZ;
    /**
     * The last yaw value which was transmitted to the server, used to determine when the yaw changes and needs to be
     * re-transmitted
     */
    private float lastReportedYaw;
    /**
     * The last pitch value which was transmitted to the server, used to determine when the pitch changes and needs to
     * be re-transmitted
     */
    private float lastReportedPitch;
    /**
     * the last sneaking state sent to the server
     */
    private boolean serverSneakState;
    /**
     * the last sprinting state sent to the server
     */
    private boolean serverSprintState;
    /**
     * Reset to 0 every time position is sent to the server, used to send periodic updates every 20 ticks even when the
     * player is not moving.
     */
    private int positionUpdateTicks;
    private boolean hasValidHealth;
    private String clientBrand;
    private int horseJumpPowerCounter;
    private float horseJumpPower;

    private String[] coomands = new String[]{".msg", ".binding", ".dm", ".conf", ".bind", ".friend", ".reply", ".tar", ".vc", ".users", ".panic", ".name",
            ".status", ".binds", ".waypoint", ".Hide", ".configs", ".sults", ".configuration", ".ping", ".killsults", ".setbind", ".modulerename",
            ".waypoints", ".onlinelist", ".wp", ".irc", ".b", ".c", ".cfg", ".f", ".h", ".ks", ".i", ".toggle", ".configure", ".vclip", ".message",
            ".m", ".target", ".p", ".hide", ".r", ".names", ".t", ".rename", ".chat", ".keybind", ".Online", ".config", ".tp", ".teleport"};

    public EntityPlayerSP(Minecraft mcIn, World worldIn, NetHandlerPlayClient netHandler, StatFileWriter statFile) {
        super(worldIn, netHandler.getGameProfile());
        this.connection = netHandler;
        this.statWriter = statFile;
        this.mc = mcIn;
        this.dimension = 0;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    /**
     * Heal living entity (param: amount of half-hearts)
     */
    public void heal(float healAmount) {
    }

    /**
     * Called when a player mounts an entity. e.g. mounts a pig, mounts a boat.
     */
    public void mountEntity(Entity entityIn) {
        super.mountEntity(entityIn);

        if (entityIn instanceof EntityMinecart) {
            this.mc.getSoundHandler().playSound(new MovingSoundMinecartRiding(this, (EntityMinecart) entityIn));
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
        if (this.worldObj.isBlockLoaded(new BlockPos(this.posX, 0.0D, this.posZ))) {
            EventManager.call(new PlayerUpdateEvent());
            super.onUpdate();

            if (this.isRiding()) {
                this.connection.sendPacket(new C03PacketPlayer.C05PacketPlayerLook(this.rotationYaw, this.rotationPitch, this.onGround));
                this.connection.sendPacket(new C0CPacketInput(this.moveStrafing, this.moveForward, this.movementInput().jump(), this.movementInput().sneak()));
            } else {
                this.onUpdateWalkingPlayer();
            }
        }
    }

    /**
     * called every tick when the player is on foot. Performs all the things that normally happen during movement.
     */
    public void onUpdateWalkingPlayer() {
        boolean flag = this.isSprinting();

        MotionUpdateEvent preUpdate = new MotionUpdateEvent(posX, posY, posZ, rotationYaw, rotationPitch, onGround, MotionUpdateEvent.State.PRE);
        MotionUpdateEvent postUpdate = new MotionUpdateEvent(MotionUpdateEvent.State.POST);
        EventManager.call(preUpdate);

        if (preUpdate.isCancelled()) {
            EventManager.call(postUpdate);
        }

        if (flag != this.serverSprintState) {
            this.connection.sendPacket(new C0BPacketEntityAction(this,
                    flag ? C0BPacketEntityAction.Action.START_SPRINTING : C0BPacketEntityAction.Action.STOP_SPRINTING));

            this.serverSprintState = flag;
        }

        boolean flag1 = this.isSneaking();

        if (flag1 != this.serverSneakState) {
            this.connection.sendPacket(new C0BPacketEntityAction(this,
                    flag1 ? C0BPacketEntityAction.Action.START_SNEAKING : C0BPacketEntityAction.Action.STOP_SNEAKING));

            this.serverSneakState = flag1;
        }

        if (this.isCurrentViewEntity()) {
            double d0 = preUpdate.getX() - this.lastReportedPosX;
            double d1 = preUpdate.getY() - this.lastReportedPosY;
            double d2 = preUpdate.getZ() - this.lastReportedPosZ;
            double d3 = preUpdate.getYaw() - this.lastReportedYaw;
            double d4 = preUpdate.getPitch() - this.lastReportedPitch;
            boolean flag2 = d0 * d0 + d1 * d1 + d2 * d2 > 9.0E-4D || this.positionUpdateTicks >= 20 ||
                    Novoline.getInstance().getModuleManager().getModule(Criticals.class).shouldCrit(Novoline.getInstance().getModuleManager().getModule(KillAura.class));
            boolean flag3 = d3 != 0.0D || d4 != 0.0D;

            if (this.ridingEntity == null) {
                if (flag2 && flag3) {
                    this.connection.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(preUpdate.getX(), preUpdate.getY(), preUpdate.getZ(), preUpdate.getYaw(), preUpdate.getPitch(), preUpdate.isOnGround()));
                } else if (flag2) {
                    this.connection.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(preUpdate.getX(), preUpdate.getY(), preUpdate.getZ(), preUpdate.isOnGround()));
                } else if (flag3) {
                    this.connection.sendPacket(new C03PacketPlayer.C05PacketPlayerLook(preUpdate.getYaw(), preUpdate.getPitch(), preUpdate.isOnGround()));
                } else {
                    this.connection.sendPacket(new C03PacketPlayer(preUpdate.isOnGround()));
                }

            } else {
                this.connection.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(this.motionX, -999.0D, this.motionZ, preUpdate.getYaw(), preUpdate.getPitch(), preUpdate.isOnGround()));
                flag2 = false;
            }

            ++this.positionUpdateTicks;

            if (flag2) {
                this.lastReportedPosX = preUpdate.getX();
                this.lastReportedPosY = preUpdate.getY();
                this.lastReportedPosZ = preUpdate.getZ();
                this.positionUpdateTicks = 0;
            }

            if (flag3) {
                this.lastReportedYaw = preUpdate.getYaw();
                this.lastReportedPitch = preUpdate.getPitch();
            }
        }

        EventManager.call(postUpdate);
    }

    /**
     * Called when player presses the drop item key
     */
    public void dropOneItem(boolean dropAll) {
        final C07PacketPlayerDigging.Action c07PacketPlayerDigging$Action =
                dropAll ? C07PacketPlayerDigging.Action.DROP_ALL_ITEMS : C07PacketPlayerDigging.Action.DROP_ITEM;
        this.connection.sendPacket(new C07PacketPlayerDigging(c07PacketPlayerDigging$Action, BlockPos.ORIGIN, EnumFacing.DOWN));
    }

    /**
     * Joins the passed in entity item with the world. Args: entityItem
     */
    protected void joinEntityItemWithWorld(EntityItem itemIn) {
    }

    /**
     * Sends a chat message from the player. Args: chatMessage
     */
    public void sendChatMessage(String message) {
        for (String command : this.coomands) {
            if (message.toLowerCase().startsWith(command)) {
                this.mc.getNovoline().getNovoCommandHandler().executeCommand(this, message.toLowerCase());
                return;
            }
        }

        this.connection.sendPacket(new C01PacketChatMessage(message));
    }

    /**
     * Swings the item the player is holding.
     */
    public void swingItem() {
        super.swingItem();
        this.connection.sendPacket(new C0APacketAnimation());
    }

    public void swingItemNoPacket() {
        super.swingItem();
    }

    public void respawnPlayer() {
        this.connection.sendPacket(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
    }

    /**
     * Deals damage to the entity. If its a EntityPlayer then will take damage from the armor first and then health
     * second with the reduced value. Args: damageAmount
     */
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        if (!this.isEntityInvulnerable(damageSrc)) {
            this.setHealth(this.getHealth() - damageAmount);
        }
    }

    /**
     * set current crafting inventory back to the 2x2 square
     */
    public void closeScreen() {
        this.connection.sendPacket(new C0DPacketCloseWindow(this.openContainer.windowId));
        this.closeScreenAndDropStack();
    }

    public void closeScreen(GuiScreen current, int windowsID) {
        this.connection.sendPacketNoEvent(new C0DPacketCloseWindow(windowsID));
        this.closeScreenAndDropStack(current);
    }

    public void closeScreenAndDropStack() {
        this.inventory.setItemStack(null);
        super.closeScreen();
        this.mc.displayGuiScreen(null);
    }

    public void closeScreenAndDropStack(GuiScreen screen) {
        this.inventory.setItemStack(null);
        super.closeScreen();
        this.mc.displayGuiScreen(screen);
    }

    /**
     * Updates health locally.
     */
    public void setPlayerSPHealth(float health) {
        if (this.hasValidHealth) {
            final float f = this.getHealth() - health;

            if (f <= 0.0F) {
                this.setHealth(health);

                if (f < 0.0F) {
                    this.hurtResistantTime = this.maxHurtResistantTime / 2;
                }

            } else {
                this.lastDamage = f;
                this.setHealth(this.getHealth());
                this.hurtResistantTime = this.maxHurtResistantTime;
                this.damageEntity(DamageSource.generic, f);
                this.hurtTime = this.maxHurtTime = 10;
            }

        } else {
            this.setHealth(health);
            this.hasValidHealth = true;
        }
    }

    /**
     * Adds a value to a statistic field.
     */
    public void addStat(StatBase stat, int amount) {
        if (stat != null) {
            if (stat.isIndependent) {
                super.addStat(stat, amount);
            }
        }
    }

    /*
     * Sends the player's abilities to the server (if there is one).
     */
    public void sendPlayerAbilities() {
        connection.sendPacket(new C13PacketPlayerAbilities(abilities));
    }

    public void sendFakeAbilities() {
        if (this != null) {
            connection.sendPacket(new C13PacketPlayerAbilities(abilities.getFlySpeed(), abilities.getWalkSpeed(),
                    true, true, abilities.isCreative(), abilities.isDisabledDamage()));
        }
    }

    /**
     * returns true if this is an EntityPlayerSP, or the logged in player.
     */
    public void sendHorseJump() {
        this.connection.sendPacket(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.RIDING_JUMP, (int) (this.getHorseJumpPower() * 100.0F)));
    }

    public void sendHorseInventory() {
        this.connection.sendPacket(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.OPEN_INVENTORY));
    }

    public String getClientBrand() {
        return this.clientBrand;
    }

    public void setClientBrand(String brand) {
        this.clientBrand = brand;
    }

    public StatFileWriter getStatFileWriter() {
        return this.statWriter;
    }

    public void addChatComponentMessage(IChatComponent chatComponent) {
        this.mc.ingameGUI.getChatGUI().printChatMessage(chatComponent);
    }

    protected boolean pushOutOfBlocks(double x, double y, double z) {
        if (!this.noClip) {
            final BlockPos blockpos = new BlockPos(x, y, z);
            final double d0 = x - (double) blockpos.getX();
            final double d1 = z - (double) blockpos.getZ();

            if (!this.isOpenBlockSpace(blockpos)) {
                int i = -1;
                double d2 = 9999.0D;

                if (this.isOpenBlockSpace(blockpos.west()) && d0 < d2) {
                    d2 = d0;
                    i = 0;
                }

                if (this.isOpenBlockSpace(blockpos.east()) && 1.0D - d0 < d2) {
                    d2 = 1.0D - d0;
                    i = 1;
                }

                if (this.isOpenBlockSpace(blockpos.north()) && d1 < d2) {
                    d2 = d1;
                    i = 4;
                }

                if (this.isOpenBlockSpace(blockpos.south()) && 1.0D - d1 < d2) {
                    i = 5;
                }

                final float f = 0.1F;

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

        }
        return false;
    }

    /**
     * Returns true if the block at the given BlockPos and the block above it are NOT full cubes.
     */
    private boolean isOpenBlockSpace(BlockPos pos) {
        return !this.worldObj.getBlockState(pos).getBlock().isNormalCube() && !this.worldObj.getBlockState(pos.up())
                .getBlock().isNormalCube();
    }

    /**
     * Set sprinting switch for Entity.
     */
    public void setSprinting(boolean sprinting) {
        super.setSprinting(sprinting);
        this.sprintingTicksLeft = sprinting ? 600 : 0;
    }

    /**
     * Sets the current XP, total XP, and level number.
     */
    public void setXPStats(float currentXP, int maxXP, int level) {
        this.experience = currentXP;
        this.experienceTotal = maxXP;
        this.experienceLevel = level;
    }

    /**
     * Send a chat message to the CommandSender
     */
    public void addChatMessage(IChatComponent component) {
        this.mc.ingameGUI.getChatGUI().printChatMessage(component);
    }

    /**
     * Returns {@code true} if the CommandSender is allowed to execute the command, {@code false} if not
     */
    public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
        return permLevel <= 0;
    }

    /**
     * Get the position in the world. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return
     * the coordinates 0, 0, 0
     */
    public BlockPos getPosition() {
        return new BlockPos(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D);
    }

    public void playSound(String name, float volume, float pitch) {
        this.worldObj.playSound(this.posX, this.posY, this.posZ, name, volume, pitch, false);
    }

    /**
     * Returns whether the entity is in a server world
     */
    public boolean isServerWorld() {
        return true;
    }

    public boolean isRidingHorse() {
        return this.ridingEntity != null && this.ridingEntity instanceof EntityHorse && ((EntityHorse) this.ridingEntity).isHorseSaddled();
    }

    public float getHorseJumpPower() {
        return this.horseJumpPower;
    }

    public void openEditSign(TileEntitySign signTile) {
        this.mc.displayGuiScreen(new GuiEditSign(signTile));
    }

    public void openEditCommandBlock(CommandBlockLogic cmdBlockLogic) {
        this.mc.displayGuiScreen(new GuiCommandBlock(cmdBlockLogic));
    }

    /**
     * Displays the GUI for interacting with a book.
     */
    public void displayGUIBook(ItemStack bookStack) {
        final Item item = bookStack.getItem();

        if (item == Items.writable_book) {
            this.mc.displayGuiScreen(new GuiScreenBook(this, bookStack, true));
        }
    }

    /**
     * Displays the GUI for interacting with a chest inventory. Args: chestInventory
     */
    public void displayGUIChest(IInventory chestInventory) {
        final String s = chestInventory instanceof IInteractionObject ?
                ((IInteractionObject) chestInventory).getGuiID() : "minecraft:container";

        if (s.equals("minecraft:chest")) {
            this.mc.displayGuiScreen(new GuiChest(this.inventory, chestInventory));
        } else if (s.equals("minecraft:hopper")) {
            this.mc.displayGuiScreen(new GuiHopper(this.inventory, chestInventory));
        } else if (s.equals("minecraft:furnace")) {
            this.mc.displayGuiScreen(new GuiFurnace(this.inventory, chestInventory));
        } else if (s.equals("minecraft:brewing_stand")) {
            this.mc.displayGuiScreen(new GuiBrewingStand(this.inventory, chestInventory));
        } else if (s.equals("minecraft:beacon")) {
            this.mc.displayGuiScreen(new GuiBeacon(this.inventory, chestInventory));
        } else if (!s.equals("minecraft:dispenser") && !s.equals("minecraft:dropper")) {
            this.mc.displayGuiScreen(new GuiChest(this.inventory, chestInventory));
        } else {
            this.mc.displayGuiScreen(new GuiDispenser(this.inventory, chestInventory));
        }

        EventManager.call(new DisplayChestGuiEvent(s));
    }

    public void displayGUIHorse(EntityHorse horse, IInventory horseInventory) {
        this.mc.displayGuiScreen(new GuiScreenHorseInventory(this.inventory, horseInventory, horse));
    }

    public void displayGui(IInteractionObject guiOwner) {
        final String s = guiOwner.getGuiID();

        if ("minecraft:crafting_table".equals(s)) {
            this.mc.displayGuiScreen(new GuiCrafting(this.inventory, this.worldObj));
        } else if ("minecraft:enchanting_table".equals(s)) {
            this.mc.displayGuiScreen(new GuiEnchantment(this.inventory, this.worldObj, guiOwner));
        } else if ("minecraft:anvil".equals(s)) {
            this.mc.displayGuiScreen(new GuiRepair(this.inventory, this.worldObj));
        }
    }

    public void displayVillagerTradeGui(IMerchant villager) {
        this.mc.displayGuiScreen(new GuiMerchant(this.inventory, villager, this.worldObj));
    }

    /**
     * Called when the player performs a critical hit on the Entity. Args: entity that was hit critically
     */
    public void onCriticalHit(Entity entityHit) {
        this.mc.effectRenderer.emitParticleAtEntity(entityHit, EnumParticleTypes.CRIT);
    }

    public void onEnchantmentCritical(Entity entityHit) {
        this.mc.effectRenderer.emitParticleAtEntity(entityHit, EnumParticleTypes.CRIT_MAGIC);
    }

    /**
     * Returns if this entity is sneaking.
     */
    public boolean isSneaking() {
        final boolean flag = this.movementInput() != null && this.movementInput().sneak();
        return flag && !this.sleeping;
    }

    public void updateEntityActionState() {
        super.updateEntityActionState();
        if (this.isCurrentViewEntity()) {
            this.moveStrafing = this.movementInput().getMoveStrafe();
            this.moveForward = this.movementInput().getMoveForward();
            this.isJumping = this.movementInput().jump();
            this.prevRenderArmYaw = this.renderArmYaw;
            this.prevRenderArmPitch = this.renderArmPitch;
            this.renderArmPitch = (float) ((double) this.renderArmPitch + (double) (this.rotationPitch - this.renderArmPitch) * 0.5D);
            this.renderArmYaw = (float) ((double) this.renderArmYaw + (double) (this.rotationYaw - this.renderArmYaw) * 0.5D);
        }
    }

    protected boolean isCurrentViewEntity() {
        return this.mc.getRenderViewEntity() == this;
    }

    @Override
    public float getToolDigEfficiency(Block p_180471_1_) {
        float f = this.inventory.getStrVsBlock(p_180471_1_);

        if (f > 1.0F) {
            int i = EnchantmentHelper.getEfficiencyModifier(this);
            ItemStack itemstack = this.inventory.getCurrentItem();

            if (i > 0 && itemstack != null) {
                f += (float) (i * i + 1);
            }
        }

        if (this.isPotionActive(Potion.digSpeed)) {
            f *= 1.0F + (float) (this.getActivePotionEffect(Potion.digSpeed).getAmplifier() + 1) * 0.2F;
        }

        if (this.isPotionActive(Potion.digSlowdown)) {
            float f1 = 1.0F;

            switch (this.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) {
                case 0:
                    f1 = 0.3F;
                    break;

                case 1:
                    f1 = 0.09F;
                    break;

                case 2:
                    f1 = 0.0027F;
                    break;

                case 3:
                default:
                    f1 = 8.1E-4F;
            }

            f *= f1;
        }

        if (this.isInsideOfMaterial(Material.water) && !EnchantmentHelper.getAquaAffinityModifier(this)) {
            f /= 5.0F;
        }

        if (!this.onGround) {
            f /= 5.0F;
        }

        return f;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
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

            if (this.timeInPortal == 0.0F) {
                this.mc.getSoundHandler().playSound(PositionedSoundRecord
                        .create(new ResourceLocation("portal.trigger"), this.rand.nextFloat() * 0.4F + 0.8F));
            }

            this.timeInPortal += 0.0125F;

            if (this.timeInPortal >= 1.0F) {
                this.timeInPortal = 1.0F;
            }

            this.inPortal = false;
        } else if (this.isPotionActive(confusion) && this.getActivePotionEffect(confusion).getDuration() > 60) {
            this.timeInPortal += 0.006666667F;

            if (this.timeInPortal > 1.0F) {
                this.timeInPortal = 1.0F;
            }
        } else {
            if (this.timeInPortal > 0.0F) {
                this.timeInPortal -= 0.05F;
            }

            if (this.timeInPortal < 0.0F) {
                this.timeInPortal = 0.0F;
            }
        }

        if (this.timeUntilPortal > 0) {
            --this.timeUntilPortal;
        }

        final boolean flag = this.movementInput().jump();
        final boolean flag1 = this.movementInput().sneak();
        final float f = 0.8F;
        final boolean flag2 = this.movementInput().getMoveForward() >= f;

        if (Novoline.getInstance().getModuleManager().getModule(GuiMove.class).isEnabled() && !(mc.currentScreen instanceof GuiChat)) {
            Novoline.getInstance().getModuleManager().getModule(GuiMove.class).updatePlayerMoveState();
        } else {
            this.movementInput().updatePlayerMoveState();
        }

        if (this.isUsingItem() && !this.isRiding()) {
            SlowdownEvent event = new SlowdownEvent();
            EventManager.call(event);

            if (!event.isCancelled()) {
                this.movementInput().setMoveStrafe(this.movementInput().getMoveStrafe() * 0.2F);
                this.movementInput().setMoveForward(this.movementInput().getMoveForward() * 0.2F);
                this.sprintToggleTimer = 0;
            }
        }

        PushBlockEvent pushBlock = new PushBlockEvent();
        EventManager.call(pushBlock);
        if (!pushBlock.isCancelled()) {
            this.pushOutOfBlocks(this.posX - (double) this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D,
                    this.posZ + (double) this.width * 0.35D);
            this.pushOutOfBlocks(this.posX - (double) this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D,
                    this.posZ - (double) this.width * 0.35D);
            this.pushOutOfBlocks(this.posX + (double) this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D,
                    this.posZ - (double) this.width * 0.35D);
            this.pushOutOfBlocks(this.posX + (double) this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D,
                    this.posZ + (double) this.width * 0.35D);
        }

        final boolean flag3 = (float) this.getFoodStats().getFoodLevel() > 6.0F || this.abilities.isAllowFlying();

        if (this.onGround && !flag1 && !flag2 && this.movementInput().getMoveForward() >= f && !this
                .isSprinting() && flag3 && !this.isUsingItem() && !this.isPotionActive(blindness)) {
            if (this.sprintToggleTimer <= 0 && !this.mc.gameSettings.keyBindSprint.isKeyDown()) {
                this.sprintToggleTimer = 7;
            } else {
                this.setSprinting(true);
            }
        }

        if (!this.isSprinting() && this.movementInput().getMoveForward() >= f && flag3 && !this.isUsingItem() && !this
                .isPotionActive(blindness) && this.mc.gameSettings.keyBindSprint.isKeyDown()) {
            this.setSprinting(true);
        }

        if (this.isSprinting() && (this.movementInput().getMoveForward() < f || this.isCollidedHorizontally || !flag3)) {
            this.setSprinting(false);
        }

        if (this.abilities.isAllowFlying()) {
            if (this.mc.playerController.isSpectatorMode()) {
                if (!this.abilities.isFlying()) {
                    this.abilities.setFlying(true);
                    this.sendPlayerAbilities();
                }
            } else if (!flag && this.movementInput().jump()) {
                if (this.flyToggleTimer == 0) {
                    this.flyToggleTimer = 7;
                } else {
                    this.abilities.setFlying(!this.abilities.isFlying());
                    this.sendPlayerAbilities();
                    this.flyToggleTimer = 0;
                }
            }
        }

        if (this.abilities.isFlying() && this.isCurrentViewEntity()) {
            if (this.movementInput().sneak()) {
                this.motionY -= this.abilities.getFlySpeed() * 3.0F;
            }

            if (this.movementInput().jump()) {
                this.motionY += this.abilities.getFlySpeed() * 3.0F;
            }
        }

        if (this.isRidingHorse()) {
            if (this.horseJumpPowerCounter < 0) {
                ++this.horseJumpPowerCounter;

                if (this.horseJumpPowerCounter == 0) {
                    this.horseJumpPower = 0.0F;
                }
            }

            if (flag && !this.movementInput().jump()) {
                this.horseJumpPowerCounter = -10;
                this.sendHorseJump();
            } else if (!flag && this.movementInput().jump()) {
                this.horseJumpPowerCounter = 0;
                this.horseJumpPower = 0.0F;
            } else if (flag) {
                ++this.horseJumpPowerCounter;

                if (this.horseJumpPowerCounter < 10) {
                    this.horseJumpPower = (float) this.horseJumpPowerCounter * 0.1F;
                } else {
                    this.horseJumpPower = 0.8F + 2.0F / (float) (this.horseJumpPowerCounter - 9) * 0.1F;
                }
            }
        } else {
            this.horseJumpPower = 0.0F;
        }

        super.onLivingUpdate();

        if (this.onGround && this.abilities.isFlying() && !this.mc.playerController.isSpectatorMode()) {
            this.abilities.setFlying(false);
            this.sendPlayerAbilities();
        }
    }

    @Override
    public void moveEntity(double x, double y, double z) {
        final MoveEvent event = new MoveEvent(x, y, z);

        if (RenderEnv.wasInitialized) {
            EventManager.call(event);
        }

        super.moveEntity(event.getX(), event.getY(), event.getZ());
    }

    public boolean isMoving() {
        return this.moveForward != 0 || this.moveStrafing != 0;
    }

    public boolean isRotating() {
        return this.rotationYaw - this.lastReportedYaw != 0 || this.rotationPitch - this.lastReportedPitch != 0;
    }

    public void drop(int slot) {
        this.mc.playerController.windowClick(this.inventoryContainer.windowId, slot, 1, 4, this);
    }

    public void shiftClick(int slot) {
        this.mc.playerController.windowClick(this.inventoryContainer.windowId, slot, 0, 1, this);
    }

    public void swap(int inventorySlot, int hotbarSlot) {
        this.mc.playerController.windowClick(this.inventoryContainer.windowId, inventorySlot, hotbarSlot, 2, this);
    }

    public Slot getSlotFromPlayerContainer(int slot) {
        return this.inventoryContainer.getSlot(slot);
    }

    public void setSpeed(double speed) {
        final double forward = this.moveForward, strafe = this.moveStrafing;
        double yaw = this.rotationYaw;
        final boolean isMovingForward = forward > 0.0f, isMovingBackward = forward < 0.0f, isMovingRight = strafe > 0.0f, isMovingLeft = strafe < 0.0f, isMovingSideways = isMovingLeft || isMovingRight, isMovingStraight = isMovingForward || isMovingBackward;
        if (isMoving()) {
            if (isMovingForward && !isMovingSideways) {
                yaw += 0.0;
            } else if (isMovingBackward && !isMovingSideways) {
                yaw += 180;
            } else if (isMovingForward && isMovingLeft) {
                yaw += 45;
            } else if (isMovingForward) {
                yaw -= 45;
            } else if (!isMovingStraight && isMovingLeft) {
                yaw += 90;
            } else if (!isMovingStraight && isMovingRight) {
                yaw -= 90;
            } else if (isMovingBackward && isMovingLeft) {
                yaw += 126;
            } else if (isMovingBackward) {
                yaw -= 126;
            }

            yaw = toRadians(yaw);
            this.motionX = -MathHelper.sin(yaw) * speed;
            this.motionZ = MathHelper.cos(yaw) * speed;
        } else {
            this.motionX = 0;
            this.motionZ = 0;
        }
    }

    public boolean isInWeb() {
        return this.isInWeb;
    }

    public double getSpeed() {
        return Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
    }


    public double getBaseMoveSpeed() {
        double baseSpeed = getBySprinting();
        if (this.isPotionActive(moveSpeed)) {
            int amplifier = this.getActivePotionEffect(moveSpeed).getAmplifier() +
                    1 - (this.isPotionActive(moveSlowdown) ? this.getActivePotionEffect(moveSlowdown).getAmplifier() + 1 : 0);
            baseSpeed *= 1.0 + 0.2 * amplifier;
        }

        return baseSpeed;
    }

    public double getBaseMoveSpeed(double multiplier) {
        double baseSpeed = getBySprinting();
        if (this.isPotionActive(moveSpeed)) {
            int amplifier = this.getActivePotionEffect(moveSpeed).getAmplifier() +
                    1 - (this.isPotionActive(moveSlowdown) ? this.getActivePotionEffect(moveSlowdown).getAmplifier() + 1 : 0);
            baseSpeed *= 1.0 + multiplier * amplifier;
        }

        return baseSpeed;
    }

    public double getBaseMoveSpeed(double multiplier, int amplifier) {
        double baseSpeed = getBySprinting();
        if (this.isPotionActive(moveSpeed)) {
            baseSpeed *= 1.0 + multiplier * amplifier;
        }

        return baseSpeed;
    }

    public double getBySprinting() {
        return isSprinting() ? 0.28700000047683716 : 0.22300000488758087;
    }

    public double getBySprinting(boolean sprint) {
        return sprint ? 0.28700000047683716 : 0.22300000488758087;
    }

    public double getBaseMotionY() {
        return this.isPotionActive(jump) ? 0.419999986886978 + 0.1 * (this.getActivePotionEffect(jump).getAmplifier() + 1) : 0.419999986886978;
    }

    public double getBaseMotionY(double motionY) {
        return this.isPotionActive(jump) ? motionY + 0.1 * (this.getActivePotionEffect(jump).getAmplifier() + 1) : motionY;
    }

    public boolean isInLiquid() {
        final double y = this.posY + 0.01;
        for (int x = MathHelper.floor_double(this.posX); x < ceiling_double_int(this.posX); ++x) {
            for (int z = MathHelper.floor_double(this.posZ); z < ceiling_double_int(this.posZ); ++z) {
                final BlockPos pos = new BlockPos(x, (int) y, z);
                if (this.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isOnLiquid() {
        final double y = this.posY - 0.1;
        for (int x = MathHelper.floor_double(this.posX); x < ceiling_double_int(this.posX); ++x) {
            for (int z = MathHelper.floor_double(this.posZ); z < ceiling_double_int(this.posZ); ++z) {
                final BlockPos pos = new BlockPos(x, MathHelper.floor_double(y), z);
                if (this.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isOnWater() {
        final double y = this.posY - 0.01;
        for (int x = MathHelper.floor_double(this.posX); x < ceiling_double_int(this.posX); ++x) {
            for (int z = MathHelper.floor_double(this.posZ); z < ceiling_double_int(this.posZ); ++z) {
                final BlockPos pos = new BlockPos(x, MathHelper.floor_double(y), z);
                if (this.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid && this.mc.world
                        .getBlockState(pos).getBlock().getMaterial() == Material.water) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isInsideBlock(Block Block) {
        for (int x = MathHelper.floor_double(this.getEntityBoundingBox().minX); x < MathHelper.floor_double(
                this.getEntityBoundingBox().maxX) + 1; x++) {
            for (int y = MathHelper.floor_double(this.getEntityBoundingBox().minY); y < MathHelper.floor_double(
                    this.getEntityBoundingBox().maxY) + 1; y++) {
                for (int z = MathHelper.floor_double(this.getEntityBoundingBox().minZ); z < MathHelper.floor_double(
                        this.getEntityBoundingBox().maxZ) + 1; z++) {
                    final Block block = this.mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
                    final AxisAlignedBB boundingBox;
                    if (block == Block && block != null && !(block instanceof BlockAir) && (boundingBox = block
                            .getCollisionBoundingBox(this.mc.world, new BlockPos(x, y, z),
                                    this.mc.world.getBlockState(new BlockPos(x, y, z)))) != null) {
                        if (this.getEntityBoundingBox().intersectsWith(boundingBox)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isInsideBlock() {
        for (int x = MathHelper.floor_double(this.getEntityBoundingBox().minX); x < MathHelper.floor_double(
                this.getEntityBoundingBox().maxX) + 1; x++) {
            for (int y = MathHelper.floor_double(this.getEntityBoundingBox().minY); y < MathHelper.floor_double(
                    this.getEntityBoundingBox().maxY) + 1; y++) {
                for (int z = MathHelper.floor_double(this.getEntityBoundingBox().minZ); z < MathHelper.floor_double(
                        this.getEntityBoundingBox().maxZ) + 1; z++) {
                    final Block block = this.mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
                    final AxisAlignedBB boundingBox;
                    if (block != null && !(block instanceof BlockAir) && (boundingBox = block
                            .getCollisionBoundingBox(this.mc.world, new BlockPos(x, y, z),
                                    this.mc.world.getBlockState(new BlockPos(x, y, z)))) != null) {
                        if (this.getEntityBoundingBox().intersectsWith(boundingBox)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void setMotion(double speed) {
        this.motionX *= speed;
        this.motionZ *= speed;
    }

    public boolean isOnGround(double height) {
        return !this.mc.world.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty();
    }

    public void updateTool(BlockPos pos) {
        Block block = mc.world.getBlockState(pos).getBlock();
        float strength = 1.0F;
        int slot = -1;

        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = inventory.getStackInSlot(i);

            if (itemStack != null && itemStack.getStrVsBlock(block) > strength) {
                slot = i;
                strength = itemStack.getStrVsBlock(block);
            }
        }

        if (slot != -1 && mc.player.inventory.getStackInSlot(inventory.currentItem) != inventory.getStackInSlot(slot)) {
            inventory.currentItem = slot;
        }
    }

    public int getSlotByItem(Item item) {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = inventory.getStackInSlot(i);

            if (stack != null && stack.getItem() == item) {
                return i;
            }
        }

        return -1;
    }

    public void movePlayer(double x, double y, double z) {
        double[] dir = moveLooking(0);
        double xDir = dir[0], zDir = dir[1];
        moveEntity(xDir * x, y, zDir * z);
    }

    public double[] moveLooking(float yawOffset) {
        float dir = rotationYaw + yawOffset;

        if (moveForward < 0.0F) {
            dir += 180.0F;
        }

        if (moveStrafing > 0.0F) {
            dir -= 90.0F * (moveForward < 0.0F ? -0.5F : moveForward > 0.0F ? 0.5F : 1.0F);
        }

        if (moveStrafing < 0.0F) {
            dir += 90.0F * (moveForward < 0.0F ? -0.5F : moveForward > 0.0F ? 0.5F : 1.0F);
        }

        float xD = MathHelper.cos((dir + 90.0F) * Math.PI / 180.0D);
        float zD = MathHelper.sin((dir + 90.0F) * Math.PI / 180.0D);
        return new double[]{xD, zD};
    }

    public double blocksInSecond() {
        return getLastTickDistance() * 20;
    }

/*    public Block groundBlock() {
        return !ServerUtils.isHypixel() || Novoline.getInstance().getModuleManager().getModule(Disabler.class).isEnabled() ? worldObj.getBlockState(new BlockPos(MathHelper.floor_double(posX),
                MathHelper.floor_double(getEntityBoundingBox().minY) - 1, MathHelper.floor_double(posZ))).getBlock() : Blocks.stone;
    }*/

    public short getInventoryTransaction() {
        return openContainer.getNextTransactionID(inventory);
    }

    public MovementInput movementInput() {
        return movementInput;
    }

    public void setMovementInput(MovementInput movementInput) {
        this.movementInput = movementInput;
    }

    public double constantFallDistance() {
        for (int i = (int) this.posY; i > 0; i--) {
            BlockPos pos = new BlockPos(this.posX, i, this.posZ);
            Block block = mc.world.getBlockState(pos).getBlock();

            if (block.isSolidFullCube()) {
                if (!list.contains(pos.getY())) {
                    list.add(pos.getY());
                }
            }
        }

        list.sort(Integer::compare);
        maxY = list.get(list.size() - 1) + 1;

        BlockPos pos = new BlockPos(this.posX, maxY, this.posZ);
        Block block = mc.world.getBlockState(pos).getBlock();

        if (block == Blocks.air) {
            list.clear();
        }

        return this.getDistanceY(maxY);
    }
}
