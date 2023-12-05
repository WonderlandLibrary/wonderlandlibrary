package net.minecraft.client.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSoundMinecartRiding;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.GuiScreen;
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
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import optfine.Config;
import org.apache.commons.lang3.RandomStringUtils;
import rip.helium.ChatUtil;
import rip.helium.Helium;
import rip.helium.cheat.commands.Command;
import rip.helium.cheat.impl.visual.KillSults;
import rip.helium.event.minecraft.BlockPushEvent;
import rip.helium.event.minecraft.PlayerMoveEvent;
import rip.helium.event.minecraft.PlayerSlowdownEvent;
import rip.helium.event.minecraft.PlayerUpdateEvent;
import rip.helium.ui.main.Screen;

public class EntityPlayerSP extends AbstractClientPlayer {
    public static double jumpedY;
    public final NetHandlerPlayClient sendQueue;
    private final StatFileWriter statWriter;
    public double longJumpXZ;
    public boolean heliumUser = true;
    
    public boolean isHeliumUser() {
		return heliumUser;
	}

	public void setHeliumUser(boolean heliumUser) {
		this.heliumUser = heliumUser;
	}

	/**
     * The last X position which was transmitted to the server, used to determine when the X position changes and needs
     * to be re-trasmitted
     */
    public double lastReportedPosX;

    /**
     * The last Y position which was transmitted to the server, used to determine when the Y position changes and needs
     * to be re-transmitted
     */
    public double lastReportedPosY;

    /**
     * The last Z position which was transmitted to the server, used to determine when the Z position changes and needs
     * to be re-transmitted
     */
    public double lastReportedPosZ;

    /**
     * The last yaw value which was transmitted to the server, used to determine when the yaw changes and needs to be
     * re-transmitted
     */
    public float lastReportedYaw;

    /**
     * The last pitch value which was transmitted to the server, used to determine when the pitch changes and needs to
     * be re-transmitted
     */
    public float lastReportedPitch;

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
    public MovementInput movementInput;
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
    public int sprintingTicksLeft;
    public float renderArmYaw;
    public float renderArmPitch;
    public float prevRenderArmYaw;
    public float prevRenderArmPitch;
    private int horseJumpPowerCounter;
    private float horseJumpPower;

    /**
     * The amount of time an entity has been in a Portal
     */
    public float timeInPortal;

    /**
     * The amount of time an entity has been in a Portal the previous tick
     */
    public float prevTimeInPortal;

    private float serverSidePitch;
    public int packets;
    public int packets2;
    private long sentpacketms;
    private long sentpacketms2;
    public boolean permittedToSend;
    private boolean packetsprint;

    public EntityPlayerSP(Minecraft mcIn, World worldIn, NetHandlerPlayClient netHandler, StatFileWriter statFile) {
        super(worldIn, netHandler.getGameProfile());
        this.sendQueue = netHandler;
        this.statWriter = statFile;
        this.mc = mcIn;
        this.dimension = 0;
    }
    
    public void sendChatMessage(final String m) {
        if (m.startsWith(".")) {
            for (final Command comm : Helium.instance.cmds.cmds) {
                final String[] args = m.replaceFirst("-", "").split(" ");
                if (m.startsWith("-" + comm.name)) {
                    try {
                        comm.run(args);
                    }
                    catch (Exception e) {
                        ChatUtil.chat("Unknown command.");
                        e.printStackTrace();
                    }
                }
            }
            return;
        }

        //todo: remove -

        if (m.startsWith("-")) {
            ChatUtil.chat("§cPrevented you from being an idiot. This is no longer used, Use .<command> or the console instead!");
            return;
        }

        if (Helium.instance.cheatManager.isCheatEnabled("ChatMods")) {
            if (KillSults.filter_bypass.getValue()) {
                String t1 = m;
                if (mc.getCurrentServerData() != null && !mc.getCurrentServerData().serverIP.contains("hypixel")) {
                    //if (mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP.contains("hypixel")) {
                    t1 = t1.replaceAll("(?i)nigger", "ni\u061Cgger");
                    //}
                    t1 = t1.replaceAll("(?i)fuck", "fu\u061Cck");
                    t1 = t1.replaceAll("(?i)shit", "s\u061Chit");
                    t1 = t1.replaceAll("(?i)ass", "as\u061Cs");
                    t1 = t1.replaceAll("(?i)fag", "fa\u061Cg");
                    t1 = t1.replaceAll("(?i)sex", "se\u061Cx");
                    t1 = t1.replaceAll("(?i)ching", "chi\u061Cng");
                    t1 = t1.replaceAll("(?i)kys", "ky\u061Cs");
                    t1 = t1.replaceAll("(?i)commit", "co\u061Cmmit");
                    if (KillSults.antispam.getValue()) {
                        t1 = t1 + " [" + RandomStringUtils.random(15, true, true) + "]";
                    }
                } else {
                    if (KillSults.antispam.getValue()) {
                        if (!t1.contains("/")) {
                            t1 = t1 + " [" + RandomStringUtils.random(15, true, true) + "]";
                        }
                    }
                    t1 = t1.replaceAll("nigger", "??????");

                }
                this.sendQueue.addToSendQueue(new C01PacketChatMessage(t1));

                return;
            }
        }
        this.sendQueue.addToSendQueue(new C01PacketChatMessage(m));
    }


    public float getDir(float yaw) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        float theyaw = yaw;
        if (player.movementInput.moveForward < 0) {
            theyaw += 180;
        }
        float forward = 1;
        if (player.movementInput.moveForward < 0) {
            forward = -0.5F;
        } else if (player.movementInput.moveForward > 0) {
            forward = 0.5F;
        }
        if (player.movementInput.moveStrafe > 0) {
            theyaw -= 90 * forward;
        }
        if (player.movementInput.moveStrafe < 0) {
            theyaw += 90 * forward;
        }
        theyaw *= 0.017453292F;//<- this little value can be found in jump() in entity living base
        return theyaw;
    }

    public void setMotion(double speed) {
        double forward = mc.thePlayer.movementInput.moveForward;
        double strafe = mc.thePlayer.movementInput.moveStrafe;
        float yaw = mc.thePlayer.rotationYaw;
        if ((forward == 0.0D) && (strafe == 0.0D)) {
            mc.thePlayer.motionX = 0;
            mc.thePlayer.motionZ = 0;
        } else {
            if (forward != 0.0D) {
                if (strafe > 0.0D) {
                    yaw += (forward > 0.0D ? -45 : 45);
                } else if (strafe < 0.0D) {
                    yaw += (forward > 0.0D ? 45 : -45);
                }
                strafe = 0.0D;
                if (forward > 0.0D) {
                    forward = 1;
                } else if (forward < 0.0D) {
                    forward = -1;
                }
            }
            mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F));
            mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F));
        }
    }

    public int getSpeedEffect() {
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed))
            return mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
        else
            return 0;
    }

    public void setMoveSpeedAris(PlayerMoveEvent event, double speed) {

        /*
         * Okay first off, lemme get one goddamn thing straight, everyone says ``OH U SKID ARIS SPEED CODE`` but yet again, EVERYONE FUCKING USES IT
         *
         * If you can name 1 person with a hypixel damage fly/bhop or a nocheatplus bhop faster than 1.2% vanilla bhopping that isnt using aris speed code, id be genuinly impressed
         * */

        double forward = Minecraft.getMinecraft().thePlayer.movementInput.moveForward,
                strafe = Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe,
                yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {

        } else {
            if (forward != 0) {
                if (strafe > 0) {
                    yaw += forward > 0 ? -45 : 45;
                } else if (strafe < 0) {
                    yaw += forward > 0 ? 45 : -45;
                }
                strafe = 0;
                if (forward > 0) {
                    forward = 1;
                } else if (forward < 0) {
                    forward = -1;
                }
            }
            event.setX(forward * speed * Math.cos(Math.toRadians(yaw + 88.0F))
                    + strafe * speed * Math.sin(Math.toRadians(yaw + 87.9F)));
            event.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 88.0F))
                    - strafe * speed * Math.cos(Math.toRadians(yaw + 87.9F)));

        }
    }
    
    public void setMoveSpeedArisUpdate(PlayerUpdateEvent event, double speed) {

        /*
         * Okay first off, lemme get one goddamn thing straight, everyone says ``OH U SKID ARIS SPEED CODE`` but yet again, EVERYONE FUCKING USES IT
         *
         * If you can name 1 person with a hypixel damage fly/bhop or a nocheatplus bhop faster than 1.2% vanilla bhopping that isnt using aris speed code, id be genuinly impressed
         * */

        double forward = Minecraft.getMinecraft().thePlayer.movementInput.moveForward,
                strafe = Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe,
                yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {

        } else {
            if (forward != 0) {
                if (strafe > 0) {
                    yaw += forward > 0 ? -45 : 45;
                } else if (strafe < 0) {
                    yaw += forward > 0 ? 45 : -45;
                }
                strafe = 0;
                if (forward > 0) {
                    forward = 1;
                } else if (forward < 0) {
                    forward = -1;
                }
            }
            event.setPosX(forward * speed * Math.cos(Math.toRadians(yaw + 88.0F))
                    + strafe * speed * Math.sin(Math.toRadians(yaw + 87.9F)));
            event.setPosZ(forward * speed * Math.sin(Math.toRadians(yaw + 88.0F))
                    - strafe * speed * Math.cos(Math.toRadians(yaw + 87.9F)));

        }
    }

    public int getJumpEffect() {
        if (mc.thePlayer.isPotionActive(Potion.jump))
            return mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1;
        else
            return 0;
    }

    public double getBaseMoveSpeed() {
        double baseSpeed = 0.2875;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            baseSpeed *= 1 + .2 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return baseSpeed;
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
            super.onUpdate();

            if (this.isRiding()) {
                this.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(this.rotationYaw, this.rotationPitch, this.onGround));
                this.sendQueue.addToSendQueue(new C0CPacketInput(this.moveStrafing, this.moveForward, this.movementInput.jump, this.movementInput.sneak));
            } else {
                this.onUpdateWalkingPlayer();
            }
        }
    }

    /**
     * called every tick when the player is on foot. Performs all the things that normally happen during movement.
     */
    public void onUpdateWalkingPlayer() {

        PlayerUpdateEvent e = new PlayerUpdateEvent(onGround, posX, getEntityBoundingBox().minY, posZ,
                rotationYaw, rotationPitch, lastReportedYaw, lastReportedPitch);
        try { //TODO: MIGHT BREAK OTHER IN NETHANDLERPLAYCLIENT
            Helium.instance.eventBus.publish(e);
        } catch (Exception gay) {

        }
        if (!e.isCancellled()) {

            boolean flag = isSprinting();

            if (flag != serverSprintState) {
                if (flag) {
                    sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SPRINTING));
                } else {
                    sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SPRINTING));
                }

                serverSprintState = flag;
            }

            boolean flag1 = isSneaking();

            if (flag1 != serverSneakState) {
                if (flag1) {
                    sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SNEAKING));
                } else {
                    sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SNEAKING));
                }

                serverSneakState = flag1;
            }

            if (mc.gameSettings.thirdPersonView != 0) {
                renderYawOffset = e.getYaw();
                setServerSidePitch(e.getPitch());
                rotationYawHead = e.getYaw();
            }

            if (isCurrentViewEntity()) {

                double d0 = e.getPosX() - lastReportedPosX;
                double d1 = e.getPosY() - lastReportedPosY;
                double d2 = e.getPosZ() - lastReportedPosZ;
                double d3 = (double) (e.getYaw() - lastReportedYaw);
                double d4 = (double) (e.getPitch() - lastReportedPitch);
                boolean flag2 = d0 * d0 + d1 * d1 + d2 * d2 > 9.0E-4D || positionUpdateTicks >= 20;
                boolean flag3 = d3 != 0.0D || d4 != 0.0D;
                if (ridingEntity == null) {
                    if (flag2 && flag3) {
                        sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(e.getPosX(), e.getPosY(), e.getPosZ(), e.getYaw(), e.getPitch(), e.isGrounded()));
                        //	mc.thePlayer.addChatComponentMessage(new ChatComponentText("C06 Compar: " + (e.getPosY() - jumpedY) + " Packets sent: " + packets + " Delay: " + (System.currentTimeMillis() - sentpacketms)));
                        sentpacketms = System.currentTimeMillis();
                        packets = packets + 1;
                    } else if (flag2) {
                        sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(e.getPosX(), e.getPosY(), e.getPosZ(), e.isGrounded()));
                        //	mc.thePlayer.addChatComponentMessage(new ChatComponentText("C04 Compar: " + (e.getPosY() - jumpedY) + " Packets sent: " + packets2 + " Delay: " + (System.currentTimeMillis() - sentpacketms2)));
                        packets2 = packets2 + 1;
                        sentpacketms2 = System.currentTimeMillis();
                    } else if (flag3) {
                        sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(e.getYaw(), e.getPitch(), e.isGrounded()));
                    } else {
                        sendQueue.addToSendQueue(new C03PacketPlayer(e.isGrounded()));
                    }
                } else {
                    sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(motionX, -999.0D, motionZ, rotationYaw, rotationPitch, e.isGrounded()));
                    flag2 = false;
                }
                ++positionUpdateTicks;

                if (flag2) {
                    lastReportedPosX = e.getPosX();
                    lastReportedPosY = e.getPosY();
                    lastReportedPosZ = e.getPosZ();
                    positionUpdateTicks = 0;
                }

                if (flag3) {
                    lastReportedYaw = e.getYaw();
                    e.setLastYaw(lastReportedYaw);
                    lastReportedPitch = e.getPitch();
                    e.setLastPitch(lastReportedPitch);
                }
            }
        }
        PlayerUpdateEvent ev = new PlayerUpdateEvent();
        try {
            Helium.instance.eventBus.publish(ev);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void moveEntity(double x, double y, double z) {
        PlayerMoveEvent playerMoveEvent = new PlayerMoveEvent(x, y, z);
        Helium.eventBus.publish(playerMoveEvent);
        if (!playerMoveEvent.isCancellled())
            super.moveEntity(playerMoveEvent.getX(), playerMoveEvent.getY(), playerMoveEvent.getZ());
    }

    /**
     * Called when player presses the drop item key
     */
    public EntityItem dropOneItem(boolean dropAll) {
        C07PacketPlayerDigging.Action c07packetplayerdigging$action = dropAll ? C07PacketPlayerDigging.Action.DROP_ALL_ITEMS : C07PacketPlayerDigging.Action.DROP_ITEM;
        this.sendQueue.addToSendQueue(new C07PacketPlayerDigging(c07packetplayerdigging$action, BlockPos.ORIGIN, EnumFacing.DOWN));
        return null;
    }

    /**
     * Joins the passed in entity item with the world. Args: entityItem
     */
    protected void joinEntityItemWithWorld(EntityItem itemIn) {
    }

    /**
     * Sends a chat message from the player. Args: chatMessage
     */
    /*/public void sendChatMessage(String message) {
        this.sendQueue.addToSendQueue(new C01PacketChatMessage(message));
    }/*

    /**
     * Swings the item the player is holding.
     */
    public void swingItem() {
        super.swingItem();
        this.sendQueue.addToSendQueue(new C0APacketAnimation());
    }

    public void respawnPlayer() {
        this.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
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
        if (!(mc.currentScreen instanceof Screen)) {
            this.sendQueue.addToSendQueue(new C0DPacketCloseWindow(this.openContainer.windowId));
        }
        //This is to completely disable pandora anticheat - keep it damnit!
        //this.sendQueue.addToSendQueue(new C17PacketCustomPayload("EferealdotRip", (new PacketBuffer(Unpooled.buffer())).writeString(ClientBrandRetriever.getClientModName())));

        this.closeScreenAndDropStack();
    }

    public void closeScreenAndDropStack() {
        this.inventory.setItemStack((ItemStack) null);
        super.closeScreen();
        this.mc.displayGuiScreen((GuiScreen) null);
    }

    /**
     * Updates health locally.
     */
    public void setPlayerSPHealth(float health) {
        if (this.hasValidHealth) {
            float f = this.getHealth() - health;

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

    /**
     * Sends the player's abilities to the server (if there is one).
     */
    public void sendPlayerAbilities() {
        this.sendQueue.addToSendQueue(new C13PacketPlayerAbilities(this.capabilities));
    }

    /**
     * returns true if this is an EntityPlayerSP, or the logged in player.
     */
    public boolean isUser() {
        return true;
    }

    protected void sendHorseJump() {
        this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.RIDING_JUMP, (int) (this.getHorseJumpPower() * 100.0F)));
    }

    public void sendHorseInventory() {
        this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.OPEN_INVENTORY));
    }

    public void setClientBrand(String brand) {
        this.clientBrand = brand;
    }

    public String getClientBrand() {
        return this.clientBrand;
    }

    public StatFileWriter getStatFileWriter() {
        return this.statWriter;
    }

    public void addChatComponentMessage(IChatComponent chatComponent) {
        this.mc.ingameGUI.getChatGUI().printChatMessage(chatComponent);
    }

    protected boolean pushOutOfBlocks(double x, double y, double z) {
        BlockPushEvent blockPushEvent = new BlockPushEvent();
        Helium.eventBus.publish(blockPushEvent);

        if (blockPushEvent.isCancelled())
            return false;

        if (this.noClip) {
            return false;
        } else {
            BlockPos blockpos = new BlockPos(x, y, z);
            double d0 = x - (double) blockpos.getX();
            double d1 = z - (double) blockpos.getZ();

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
                    d2 = 1.0D - d1;
                    i = 5;
                }

                float f = 0.1F;

                if (i == 0) {
                    this.motionX = (double) (-f);
                }

                if (i == 1) {
                    this.motionX = (double) f;
                }

                if (i == 4) {
                    this.motionZ = (double) (-f);
                }

                if (i == 5) {
                    this.motionZ = (double) f;
                }
            }

            return false;
        }
    }

    /**
     * Returns true if the block at the given BlockPos and the block above it are NOT full cubes.
     */
    private boolean isOpenBlockSpace(BlockPos pos) {
        return !this.worldObj.getBlockState(pos).getBlock().isNormalCube() && !this.worldObj.getBlockState(pos.up()).getBlock().isNormalCube();
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
        Item item = bookStack.getItem();

        if (item == Items.writable_book) {
            this.mc.displayGuiScreen(new GuiScreenBook(this, bookStack, true));
        }
    }

    /**
     * Displays the GUI for interacting with a chest inventory. Args: chestInventory
     */
    public void displayGUIChest(IInventory chestInventory) {
        String s = chestInventory instanceof IInteractionObject ? ((IInteractionObject) chestInventory).getGuiID() : "minecraft:container";

        if ("minecraft:chest".equals(s)) {
            this.mc.displayGuiScreen(new GuiChest(this.inventory, chestInventory));
        } else if ("minecraft:hopper".equals(s)) {
            this.mc.displayGuiScreen(new GuiHopper(this.inventory, chestInventory));
        } else if ("minecraft:furnace".equals(s)) {
            this.mc.displayGuiScreen(new GuiFurnace(this.inventory, chestInventory));
        } else if ("minecraft:brewing_stand".equals(s)) {
            this.mc.displayGuiScreen(new GuiBrewingStand(this.inventory, chestInventory));
        } else if ("minecraft:beacon".equals(s)) {
            this.mc.displayGuiScreen(new GuiBeacon(this.inventory, chestInventory));
        } else if (!"minecraft:dispenser".equals(s) && !"minecraft:dropper".equals(s)) {
            this.mc.displayGuiScreen(new GuiChest(this.inventory, chestInventory));
        } else {
            this.mc.displayGuiScreen(new GuiDispenser(this.inventory, chestInventory));
        }
    }

    public void displayGUIHorse(EntityHorse horse, IInventory horseInventory) {
        this.mc.displayGuiScreen(new GuiScreenHorseInventory(this.inventory, horseInventory, horse));
    }

    public void displayGui(IInteractionObject guiOwner) {
        String s = guiOwner.getGuiID();

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
        boolean flag = this.movementInput != null ? this.movementInput.sneak : false;
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
            this.renderArmPitch = (float) ((double) this.renderArmPitch + (double) (this.rotationPitch - this.renderArmPitch) * 0.5D);
            this.renderArmYaw = (float) ((double) this.renderArmYaw + (double) (this.rotationYaw - this.renderArmYaw) * 0.5D);
        }
    }

    protected boolean isCurrentViewEntity() {
        return this.mc.getRenderViewEntity() == this;
    }

    public boolean isMoving() {
        return (moveForward != 0.0f) || (moveStrafing != 0.0f);
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
                this.mc.displayGuiScreen((GuiScreen) null);
            }

            if (this.timeInPortal == 0.0F) {
                this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("portal.trigger"), this.rand.nextFloat() * 0.4F + 0.8F));
            }

            this.timeInPortal += 0.0125F;

            if (this.timeInPortal >= 1.0F) {
                this.timeInPortal = 1.0F;
            }

            this.inPortal = false;
        } else if (this.isPotionActive(Potion.confusion) && this.getActivePotionEffect(Potion.confusion).getDuration() > 60) {
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

        boolean flag = this.movementInput.jump;
        boolean flag1 = this.movementInput.sneak;
        float f = 0.8F;
        boolean flag2 = this.movementInput.moveForward >= f;
        this.movementInput.updatePlayerMoveState();

        PlayerSlowdownEvent playerSlowdownEvent = new PlayerSlowdownEvent();
        Helium.eventBus.publish(playerSlowdownEvent);

        if (this.isUsingItem() && !this.isRiding() && !playerSlowdownEvent.isCancelled()) {
            this.movementInput.moveStrafe *= 0.2F;
            this.movementInput.moveForward *= 0.2F;
            this.sprintToggleTimer = 0;
        }

        this.pushOutOfBlocks(this.posX - (double) this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ + (double) this.width * 0.35D);
        this.pushOutOfBlocks(this.posX - (double) this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ - (double) this.width * 0.35D);
        this.pushOutOfBlocks(this.posX + (double) this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ - (double) this.width * 0.35D);
        this.pushOutOfBlocks(this.posX + (double) this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ + (double) this.width * 0.35D);
        boolean flag3 = (float) this.getFoodStats().getFoodLevel() > 6.0F || this.capabilities.allowFlying;

        if (this.onGround && !flag1 && !flag2 && this.movementInput.moveForward >= f && !this.isSprinting() && flag3 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness)) {
            if (this.sprintToggleTimer <= 0 && !this.mc.gameSettings.keyBindSprint.isKeyDown()) {
                this.sprintToggleTimer = 7;
            } else {
                this.setSprinting(true);
            }
        }

        if (!this.isSprinting() && this.movementInput.moveForward >= f && flag3 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness) && this.mc.gameSettings.keyBindSprint.isKeyDown()) {
            this.setSprinting(true);
        }

        if (this.isSprinting() && (this.movementInput.moveForward < f || this.isCollidedHorizontally || !flag3)) {
            this.setSprinting(false);
        }

        if (this.capabilities.allowFlying) {
            if (this.mc.playerController.isSpectatorMode()) {
                if (!this.capabilities.isFlying) {
                    this.capabilities.isFlying = true;
                    this.sendPlayerAbilities();
                }
            } else if (!flag && this.movementInput.jump) {
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
                this.motionY -= (double) (this.capabilities.getFlySpeed() * 3.0F);
            }

            if (this.movementInput.jump) {
                this.motionY += (double) (this.capabilities.getFlySpeed() * 3.0F);
            }
        }

        if (this.isRidingHorse()) {
            if (this.horseJumpPowerCounter < 0) {
                ++this.horseJumpPowerCounter;

                if (this.horseJumpPowerCounter == 0) {
                    this.horseJumpPower = 0.0F;
                }
            }

            if (flag && !this.movementInput.jump) {
                this.horseJumpPowerCounter = -10;
                this.sendHorseJump();
            } else if (!flag && this.movementInput.jump) {
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

        if (this.onGround && this.capabilities.isFlying && !this.mc.playerController.isSpectatorMode()) {
            this.capabilities.isFlying = false;
            this.sendPlayerAbilities();
        }
    }

    @Override
    public float getFovModifier() {
        return Config.zoomMode ? super.getFovModifier() / 4f : super.getFovModifier();
    }

    public double getSpeed() {
        return Math.sqrt((motionX * motionX) + (motionZ * motionZ));
    }

    public void setSpeed(double speed) {
        boolean isMovingStraight;
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        double yaw = player.rotationYaw;
        boolean isMoving = (player.moveForward != 0.0f) || (player.moveStrafing != 0.0f);
        boolean isMovingForward = player.moveForward > 0.0f;
        boolean isMovingBackward = player.moveForward < 0.0f;
        boolean isMovingRight = player.moveStrafing > 0.0f;
        boolean isMovingLeft = player.moveStrafing < 0.0f;
        boolean isMovingSideways = isMovingLeft || isMovingRight;
        isMovingStraight = isMovingForward || isMovingBackward;
        if (isMoving) {
            if (isMovingForward && !isMovingSideways) {
                yaw += 0.0;
            } else if (isMovingBackward && !isMovingSideways) {
                yaw += 180.0;
            } else if (isMovingForward && isMovingLeft) {
                yaw += 45.0;
            } else if (isMovingForward) {
                yaw -= 45.0;
            } else if (!isMovingStraight && isMovingLeft) {
                yaw += 90.0;
            } else if (!isMovingStraight && isMovingRight) {
                yaw -= 90.0;
            } else if (isMovingBackward && isMovingLeft) {
                yaw += 126.0;
            } else if (isMovingBackward) {
                yaw -= 126.0;
            }
            yaw = Math.toRadians(yaw);
            player.motionX = isMoving ? (-Math.sin(yaw)) * speed : 0;
            player.motionZ = isMoving ? Math.cos(yaw) * speed : 0;
        }
    }

    public float getServerSidePitch() {
        return serverSidePitch;
    }

    public void setServerSidePitch(float serverSidePitch) {
        this.serverSidePitch = serverSidePitch;
    }

    public boolean getPacketSprint() {
        return packetsprint;
    }

    public void setPacketSprint(boolean packetsprint) {
        this.packetsprint = packetsprint;
    }

	  public float getDirection() {
        float yaw = this.rotationYaw, forward = this.moveForward, strafe = this.moveStrafing;
        yaw += (forward < 0 ? 180 : 0);
        if (strafe < 0) {
            yaw += forward == 0 ? 90 : forward < 0 ? -45 : 45;
        }
        if (strafe > 0) {
            yaw -= forward == 0 ? 90 : forward < 0 ? -45 : 45;
        }
        return yaw * MathHelper.deg2Rad;
    }

	  public boolean isMovingOnGround() {
	        return this.isMoving() && this.onGround;
	    }

}
