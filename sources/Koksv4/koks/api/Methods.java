package koks.api;

import god.buddy.aot.BCompiler;
import koks.Koks;
import koks.api.registry.command.Command;
import koks.api.registry.command.CommandRegistry;
import koks.api.utils.InventoryUtil;
import koks.api.utils.TimeHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Timer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;

/**
 * @author kroko
 * @created on 21.01.2021 : 09:52
 */
public interface Methods {

    Minecraft mc = Minecraft.getMinecraft();
    FontRenderer fr = mc.fontRendererObj;

    default EntityPlayerSP getPlayer() {
        return mc.thePlayer;
    }

    default PlayerControllerMP getPlayerController() {
        return mc.playerController;
    }

    default GameSettings getGameSettings() {
        return mc.gameSettings;
    }

    default WorldClient getWorld() {
        return mc.theWorld;
    }

    default Timer getTimer() {
        return mc.timer;
    }

    default String getName(EntityPlayer player) {
        return player.getGameProfile().getName();
    }

    default float getYaw() {
        return PlayerHandler.yaw;
    }

    default float getPitch() {
        return PlayerHandler.pitch;
    }

    default double getX() {
        return getPlayer().posX;
    }

    default double getY() {
        return getPlayer().posY;
    }

    default double getZ() {
        return getPlayer().posZ;
    }

    default int getHurtTime() {
        return getPlayer().hurtTime;
    }

    default RenderManager getRenderManager() {
        return mc.renderManager;
    }

    default int getVelocityHurtTime() {
        return getPlayer().velocityHurtTime;
    }

    default void setMotion(double motion) {
        getPlayer().motionX = getPlayer().motionZ = motion;
    }

    default Block getBlockUnderPlayer(float offsetY) {
        return getBlockUnderPlayer(getPlayer(), offsetY);
    }

    default Block getBlockUnderPlayer(EntityPlayer player, float offsetY) {
        return getWorld().getBlockState(new BlockPos(player.posX, player.posY - offsetY, player.posZ)).getBlock();
    }

    default void push(double speed) {
        float f1 = (getPlayer().rotationYaw) * 0.017453292F;
        getPlayer().motionX -= MathHelper.sin(f1) * speed;
        getPlayer().motionZ += MathHelper.cos(f1) * speed;
    }

    default void teleportTo(Entity entity) {
        getPlayer().setPosition(entity.posX, entity.posY, entity.posZ);
    }

    default void resumeWalk() {
        getGameSettings().keyBindForward.pressed = isKeyDown(getGameSettings().keyBindForward.getKeyCode());
        getGameSettings().keyBindBack.pressed = isKeyDown(getGameSettings().keyBindBack.getKeyCode());
        getGameSettings().keyBindLeft.pressed = isKeyDown(getGameSettings().keyBindLeft.getKeyCode());
        getGameSettings().keyBindRight.pressed = isKeyDown(getGameSettings().keyBindRight.getKeyCode());
    }

    default void stopWalk() {
        getGameSettings().keyBindForward.pressed = false;
        getGameSettings().keyBindBack.pressed = false;
        getGameSettings().keyBindLeft.pressed = false;
        getGameSettings().keyBindRight.pressed = false;
    }

    default void setPosition(double x, double y, double z) {
        getPlayer().setPosition(x, y, z);
    }

    default void addPosition(double x, double y, double z) {
        setPosition(getX() + x, getY() + y, getZ() + z);
    }

    default void sendPacket(Packet<? extends INetHandler> packet) {
        getPlayer().sendQueue.addToSendQueue(packet);
    }

    default void sendPacketUnlogged(Packet<? extends INetHandler> packet) {
        mc.getNetHandler().getNetworkManager().sendPacket(packet);
    }

    default void sendPacket(Packet<?> packet, long delay) {
        final TimeHelper timeHelper = new TimeHelper();
        timeHelper.reset();
        while (!timeHelper.hasReached(delay + 1)) {
            if (timeHelper.hasReached(delay))
                mc.getNetHandler().getNetworkManager().sendPacket(packet);
        }
    }

    default void debugPlayer(EntityPlayer player) {
        sendMessage("----Player-Debugger------");
        sendMessage("onGround: " + player.onGround);
        sendMessage("BedLocation: " + player.getBedLocation());
        sendMessage("HurtTime: " + player.hurtTime);
        sendMessage("Name: " + player.getName());
        sendMessage("CreativeMode: " + player.capabilities.isCreativeMode);
        sendMessage("DamageDisabled: " + player.capabilities.disableDamage);
        sendMessage("isFlying: " + player.capabilities.isFlying);
        sendMessage("allowFlying: " + player.capabilities.allowFlying);
        sendMessage("allowEdit: " + player.capabilities.allowEdit);
        sendMessage("Experience: " + player.experience);
        sendMessage("DeathTime: " + player.deathTime);
        sendMessage("hurtResistantTime: " + player.hurtResistantTime);
        sendMessage("AirBone: " + player.isAirBorne);
        sendMessage("isDead: " + player.isDead);
        sendMessage("Health: " + player.getHealth());
        sendMessage("FoodLevel: " + player.getFoodStats().getFoodLevel());
        sendMessage("NeedFood: " + player.getFoodStats().needFood());
        sendMessage("Dimension: " + player.dimension);
        sendMessage("NoClip: " + player.noClip);
        sendMessage("ridingEntity: " + player.ridingEntity);
        sendMessage("StepHeight: " + player.stepHeight);
        sendMessage("fireResistance: " + player.fireResistance);
        sendMessage("IgnoreFrustumCheck: " + player.ignoreFrustumCheck);
        sendMessage("sleeping: " + player.sleeping);
        if (player.getTeam() != null)
            sendMessage("TeamName: " + player.getTeam().getRegisteredName());
        sendMessage("SpeedInAir: " + player.speedInAir);
        sendMessage("ImmuneToFire: " + player.isImmuneToFire());
    }

    default boolean isKeyDown(int keyCode) {
        if (keyCode < 0) {
            int i = Mouse.getEventButton();
            return i - 100 == keyCode;
        } else {
            return Keyboard.isKeyDown(keyCode);
        }
    }

    default EntityOtherPlayerMP createCopy(EntityPlayer player) {
        final EntityOtherPlayerMP fakeEntity = new EntityOtherPlayerMP(getWorld(), player.getGameProfile());
        fakeEntity.copyLocationAndAnglesFrom(player);
        fakeEntity.renderYawOffset = player.renderYawOffset;
        fakeEntity.inventory = player.inventory;
        fakeEntity.ridingEntity = player.ridingEntity;
        fakeEntity.setEating(player.isEating());
        fakeEntity.setHealth(player.getHealth());
        fakeEntity.setSprinting(player.isSprinting());
        fakeEntity.setSneaking(getPlayer().isSneaking());
        return fakeEntity;
    }

    default void copyStats(EntityOtherPlayerMP playerMP) {
        getPlayer().copyLocationAndAnglesFrom(playerMP);
        getPlayer().renderYawOffset = playerMP.renderYawOffset;
        getPlayer().inventory = playerMP.inventory;
        getPlayer().ridingEntity = playerMP.ridingEntity;
        getPlayer().setEating(playerMP.isEating());
        getPlayer().setSprinting(playerMP.isSprinting());
        getPlayer().setSneaking(playerMP.isSneaking());
    }

    default void sendMessage(Object o) {
        sendMessage(o, true);
    }

    default void sendMessage(Object o, boolean prefix) {
        getPlayer().addChatMessage(new ChatComponentText((prefix ? Koks.prefix : "") + o));
    }

    default void sendHelp(Command command, String... usages) {
        for (String usage : usages) {
            String shortestName = command.getName();
            for (String alias : command.getAliases()) {
                if (alias.length() < shortestName.length()) {
                    shortestName = alias;
                }
            }
            sendMessage("§a" + Koks.getKoks().commandPrefix + shortestName + " §7" + usage);
        }
    }

    default void sendError(String issue, String help) {
        sendMessage("§c§lERROR: §e" + issue.toUpperCase() + "§7: §a" + help);
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    default void resetRotations(float yaw, float pitch, boolean silent) {
        if(silent) {
            getPlayer().rotationYaw = yaw - yaw % 360 + getPlayer().rotationYaw % 360;
        } else {
            getPlayer().rotationYaw = yaw;
            getPlayer().rotationPitch = pitch;
        }
    }

    default boolean isMoving() {
        return getPlayer().moveForward != 0 || getPlayer().moveStrafing != 0;
    }

    default boolean isMoving(Entity entity) {
        return entity.lastTickPosX != entity.posX || entity.lastTickPosZ != entity.posZ || entity.lastTickPosY != entity.posY;
    }

    default ItemStack createItemStack(int id) {
        return createItemStack(id, 1);
    }

    default ItemStack createItemStack(int id, int amount) {
        return createItemStack(id, amount, (byte) 0);
    }

    default ItemStack createItemStack(String name, int amount, byte meta) {
        return createItemStack(Item.getIdFromItem(Item.getByNameOrId(name)), amount, meta, null);
    }

    default ItemStack createItemStack(int id, int amount, byte meta) {
        return createItemStack(id, amount, meta, null);
    }

    default ItemStack createItemStack(String name, int amount, byte meta, String nbt) {
        return createItemStack(Item.getIdFromItem(Item.getByNameOrId(name)), amount, meta, nbt);
    }

    default ItemStack createItemStack(int id, int amount, byte meta, String nbt) {
        final ItemStack itemStack = new ItemStack(Item.getItemById(id), amount, meta);
        if (nbt != null) {
            try {
                itemStack.setTagCompound(JsonToNBT.getTagFromJson(nbt));
            } catch (NBTException e) {
                sendMessage("§cInvalid NBT-Data!");
            }
        }
        return itemStack;
    }

    default void giveItem(ItemStack itemStack) {
        final InventoryUtil inventoryUtil = InventoryUtil.getInstance();
        if (inventoryUtil.hasAir()) {
            for (int i = 0; i < 9; i++) {
                final ItemStack stack = getPlayer().inventory.getStackInSlot(i);
                if (stack == null) {
                    sendPacket(new C10PacketCreativeInventoryAction(i + 36, itemStack));
                    sendMessage("§aYou got an Item!");
                    return;
                }
            }
        } else {
            sendMessage("§cYour Inventory is not empty!");
        }
    }

    default boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    default int randomInRange(int min, int max) {
        if (min > max) {
            System.err.println("The minimal value cannot be higher than the max value");
            return min;
        }
        max -= min;
        return (int) Math.round(Math.random() * (max)) + min;
    }

    default double randomInRange(double min, double max) {
        if (min > max) {
            System.err.println("The minimal value cannot be higher than the max value");
            return min;
        }
        max -= min;
        return (Math.random() * (max)) + min;
    }

    default Object[] getAddressableRainbow(int index, Color currentColor, int speed, Color... colors) {
        float red = currentColor.getRed() / 255F;
        float green = currentColor.getGreen() / 255F;
        float blue = currentColor.getBlue() / 255F;
        int targetIndex = index + 1 >= colors.length ? 0 : index + 1;
        final Color targetColor = colors[targetIndex];

        final float distanceRed = ((targetColor.getRed() / 255F - red) % speed) / (float) speed;
        final float distanceGreen = ((targetColor.getGreen() / 255F - green) % speed) / (float) speed;
        final float distanceBlue = ((targetColor.getBlue() - blue) / 255F % speed) / (float) speed;

        float r = Math.round((red + distanceRed) * 10F) / 10F;
        float g = Math.round((green + distanceGreen) * 10F) / 10F;
        float b = Math.round((blue + distanceBlue) * 10F) / 10F;
        Color newColor = new Color(MathHelper.clamp_float(r, 0, 1), MathHelper.clamp_float(g, 0, 1), MathHelper.clamp_float(b, 0, 1));

        final float differenceRed = MathHelper.clamp_float(targetColor.getRed() / 255F - red, 0, 1);
        final float differenceGreen = MathHelper.clamp_float(targetColor.getGreen() / 255F - green, 0, 1);
        final float differenceBlue = MathHelper.clamp_float(targetColor.getBlue() / 255F - blue, 0, 1);

        if (differenceRed == 0 && differenceGreen == 0 && differenceBlue == 0) {
            newColor = targetColor;
            index = targetIndex;
        }
        return new Object[]{newColor, index};
    }

    default Color getRainbow(int offset, int speed, float saturation, float brightness) {
        float hue = ((System.currentTimeMillis() + offset) % speed) / (float) speed;
        return Color.getHSBColor(hue, saturation, brightness);
    }

    /*For ChinaHat from getter (Traded)*/
    default int fadeBetween(int startColor, int endColor, float progress) {
        if (progress > 1.0F) {
            progress = 1.0F - progress % 1.0F;
        }

        return fadeTo(startColor, endColor, progress);
    }

    /*For ChinaHat from getter (Traded)*/
    default int fadeTo(int startColor, int endColor, float progress) {
        float invert = 1.0F - progress;
        int r = (int)((float)(startColor >> 16 & 255) * invert + (float)(endColor >> 16 & 255) * progress);
        int g = (int)((float)(startColor >> 8 & 255) * invert + (float)(endColor >> 8 & 255) * progress);
        int b = (int)((float)(startColor & 255) * invert + (float)(endColor & 255) * progress);
        int a = (int)((float)(startColor >> 24 & 255) * invert + (float)(endColor >> 24 & 255) * progress);
        return (a & 255) << 24 | (r & 255) << 16 | (g & 255) << 8 | b & 255;
    }

    default boolean isVoid(EntityPlayer player) {
        for (int i = (int) player.posY; i > 0; i--)
            if (getWorld().getBlockState(new BlockPos(player.posX, i, player.posZ)).getBlock() != Blocks.air)
                return false;
        return true;
    }

    default double interpolate(double old, double current) {
        return old + (current - old) * Minecraft.getMinecraft().timer.renderPartialTicks;
    }

    default boolean isValidEntityName(Entity entity) {
        if (!(entity instanceof EntityPlayer))
            return true;
        final String name = getName((EntityPlayer) entity);
        return name.length() <= 16 && name.length() >= 3 && name.matches("[a-zA-Z0-9_]*");
    }

    default Color getTeamColor(EntityPlayer player) {
        ScorePlayerTeam scoreplayerteam = (ScorePlayerTeam) ((EntityPlayer) player).getTeam();
        int i = 16777215;

        if (scoreplayerteam != null && scoreplayerteam.getColorPrefix() != null) {
            String s = FontRenderer.getFormatFromString(scoreplayerteam.getColorPrefix());
            if (s.length() >= 2) {
                if (mc.getRenderManager().getFontRenderer() != null && mc.getRenderManager().getFontRenderer().getColorCode(s.charAt(1)) != 0)
                    i = mc.getRenderManager().getFontRenderer().getColorCode(s.charAt(1));
            }
        }
        final float f1 = (float) (i >> 16 & 255) / 255.0F;
        final float f2 = (float) (i >> 8 & 255) / 255.0F;
        final float f = (float) (i & 255) / 255.0F;

        return new Color(f1, f2, f);
    }

    default boolean isTeam(EntityPlayer player, EntityPlayer player2) {
        return player.getTeam() != null && player2.getTeam() != null && (player.getTeam().isSameTeam(player2.getTeam()) || getTeamColor(player).getRGB() == getTeamColor(player2).getRGB());
    }

}
