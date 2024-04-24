package Method.Client.utils;

import Method.Client.utils.system.Wrapper;
import Method.Client.utils.visual.ChatUtils;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.lwjgl.opengl.Display;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Utils {
    public static double[] directionSpeed(double speed) {
        float forward = Wrapper.INSTANCE.mc().player.movementInput.moveForward;
        float side = Wrapper.INSTANCE.mc().player.movementInput.moveStrafe;
        float yaw = Wrapper.INSTANCE.mc().player.prevRotationYaw + (Wrapper.INSTANCE.mc().player.rotationYaw - Wrapper.INSTANCE.mc().player.prevRotationYaw) * Wrapper.INSTANCE.mc().getRenderPartialTicks();

        if (forward != 0) {
            if (side > 0)
                yaw += (forward > 0 ? -45 : 45);
            else if (side < 0)
                yaw += (forward > 0 ? 45 : -45);
            side = 0;
            if (forward > 0)
                forward = 1;
            else if (forward < 0)
                forward = -1;
        }

        final double sin = sin(Math.toRadians(yaw + 90));
        final double cos = cos(Math.toRadians(yaw + 90));
        final double posX = (forward * speed * cos + side * speed * sin);
        final double posZ = (forward * speed * sin - side * speed * cos);
        return new double[]{posX, posZ};
    }

    public static float[] getNeededRotations(Vec3d vec, float yaw, float pitch) {
        final Vec3d eyesPos = getEyesPos();
        final double diffX = vec.x - eyesPos.x;
        final double diffY = vec.y - eyesPos.y;
        final double diffZ = vec.z - eyesPos.z;

        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        final float rotationYaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float rotationPitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ)));

        return new float[]{
                updateRotation(Wrapper.INSTANCE.player().rotationYaw, rotationYaw, yaw / 4),
                updateRotation(Wrapper.INSTANCE.player().rotationPitch, rotationPitch, pitch / 4)
        };
    }

    public static float updateRotation(float PlayerRotation, float Modified, float MaxValueAccepted) {
        float degrees = MathHelper.wrapDegrees(Modified - PlayerRotation);
        if (MaxValueAccepted != 0) {
            if (degrees > MaxValueAccepted) {
                degrees = MaxValueAccepted;
            }
            if (degrees < -MaxValueAccepted) {
                degrees = -MaxValueAccepted;
            }
        }
        return PlayerRotation + degrees;
    }

    static final String PNG16 = "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAMAAAAoLQ9TAAAAWlBMVEUAAAAcTX4Ao8EhL2EDjsEEW5ABVYsgLV8Co8MXSXsNUYQJcJ4HlL0DiLIBj78LUIQDjbwPU4IBjL0cM2QFbqIEeKkCpcIGcqUDrMQAir4eLF4BUYcEcKIVPG+aW6eOAAAAGXRSTlMACf1+ff7JulQVZC0b+/KwmvfQ0H9EzsqNOi43JAAAAJlJREFUGNNNztkSgyAMBdCQGkQWcbcR+P/fbCja6X1KzkwWcNZaD994Y4yDjRJNDSbEYKHLiUZTezMizi/oOBMfXgYWxnxV4Mz4hlqE0iAwo9lHDvPV4JwYjzNTXG+IbkGkpF1skHslB2jelW47qFfyAm3wQBHwix4EUoV8CcDuoEIRoAqSB7byD+kFdl1/ELW2oIZhgDtSqg8YGgqZZV00CQAAAABJRU5ErkJggg==";
    static final String PNG32 = "iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAMAAABEpIrGAAAARVBMVEUAAAAibpsDjsAhL2AAocAGV4ojMWMKapsJjrYJVYgFmsMBqsQCVYoEjL4Mg7MHU4gAi78eK14CU4gAqsMDYJMDdqoWOm1sXDZmAAAAEHRSTlMADsXI/KKSaj3IfI+OrCjjAYTALgAAASZJREFUOMuF0NGugyAQRdGhBVHb6oDA/3/qPTNSVJJez5OBFXeU7uffuvF6Or50nsg8UsTSw1CbnG4yOxk85ijj1/l+ZgXpKYCTvIHteAqwAi47yEkAt4icsUUghh1wVrDNXzAzswTCF3ASYBGpAcwicIAsgLc9Yj4KEGgAQgEiGpAh0IAKfatEFtblcoC2DZE9kPMarsDJuUXE4zKl+HZXkIelfjsuscm4ayIN5LVh9X4dqQNlIELEVuCJXDyDHAAQqcAZAeUEogCJAGigBwVAf5EFQEBBbAAFBXgGQKCC0kBUgI1s07pQBbGBUoFEkqceoNCA+Tg6QKkgHoAW0wBWQVFwrAcp/ARFQfwNooJyA6bwD5gAhqfM98BPMv2rRtcDU49v9wetJCCntwu3NQAAAABJRU5ErkJggg==";

    public static void loadIconsOnFrames() {
        try {
            byte[] imageBytes = java.util.Base64.getDecoder().decode(PNG16);
            byte[] imageBytes2 = java.util.Base64.getDecoder().decode(PNG32);
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
            BufferedImage img2 = ImageIO.read(new ByteArrayInputStream(imageBytes2));
            System.out.println("Loading current icons for Method");
            Display.setIcon(new ByteBuffer[]{IconLoad(img), IconLoad(img2)});
            Frame[] frames = Frame.getFrames();
            if (frames != null) {
                final List<Image> icons = Arrays.asList(img, img2);
                Arrays.stream(frames).forEach(frame -> {
                    try {
                        frame.setIconImages(icons);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static ByteBuffer IconLoad(final BufferedImage icon) {
        final int[] rgb = icon.getRGB(0, 0, icon.getWidth(), icon.getHeight(), null, 0, icon.getWidth());
        final ByteBuffer buffer = ByteBuffer.allocate(4 * rgb.length);
        Arrays.stream(rgb).map(color -> color << 8 | ((color >> 24) & 0xFF)).forEach(buffer::putInt);
        buffer.flip();
        return buffer;
    }

    public static void addEffect(int id, int duration, int amplifier) {
        Wrapper.mc.player.addPotionEffect(new PotionEffect(Objects.requireNonNull(Potion.getPotionById(id)), duration, amplifier));
    }

    public static void removeEffect(int id) {
        Wrapper.mc.player.removePotionEffect(Objects.requireNonNull(Potion.getPotionById(id)));
    }

    public static void clearEffects() {
        Wrapper.mc.player.getActivePotionEffects().forEach(effect -> Wrapper.mc.player.removePotionEffect(effect.getPotion()));
    }

    public static Vec3d interpolateEntity(Entity entity, float time) {
        return new Vec3d(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * time,
                entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * time,
                entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * time);
    }

    public static int random(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    public static void updateFirstEmptySlot(ItemStack stack) {
        int slot = 0;
        boolean slotFound = false;
        for (int i = 0; i < 36; i++) {
            if (Wrapper.mc.player.inventory.getStackInSlot(i).isEmpty()) {
                slot = i;
                slotFound = true;
                break;
            }
        }
        if (!slotFound) {
            ChatUtils.warning("Could not find empty slot. Operation has been aborted.");
            return;
        }

        int convertedSlot = slot;
        if (slot < 9)
            convertedSlot += 36;

        if (stack.getCount() > 64) {
            ItemStack passStack = stack.copy();
            stack.setCount(64);
            passStack.setCount(passStack.getCount() - 64);
            Wrapper.mc.player.inventory.setInventorySlotContents(slot, stack);
            Objects.requireNonNull(Wrapper.mc.getConnection()).sendPacket(new CPacketCreativeInventoryAction(convertedSlot, stack));
            updateFirstEmptySlot(passStack);
            return;
        }

        (Objects.requireNonNull(Wrapper.mc.getConnection())).sendPacket(new CPacketCreativeInventoryAction(convertedSlot, stack));
    }


    public static boolean isMoving(Entity e) {
        return e.motionX != 0.0 && e.motionZ != 0.0 && e.motionY != 0.0;
    }

    public static boolean isLiving(Entity e) {
        return e instanceof EntityLivingBase;
    }

    public static boolean isPassive(Entity e) {
        if (e instanceof EntityWolf && ((EntityWolf) e).isAngry()) return false;
        if (e instanceof EntityAgeable || e instanceof EntityAmbientCreature || e instanceof EntitySquid)
            return true;
        return e instanceof EntityIronGolem && ((EntityIronGolem) e).getRevengeTarget() == null;
    }

    public static boolean ScaledMouseCheck(double mouseX, double mouseY, double minX, double minY, double maxX, double maxY) {
        double minXf = minX * (Minecraft.getMinecraft().displayWidth / 1920.0);
        double minYf = minY * (Minecraft.getMinecraft().displayHeight / 1027.0);
        double maxXf = (maxX + minX) * (Minecraft.getMinecraft().displayWidth / 1920.0);
        double maxYf = (maxY + minY) * (Minecraft.getMinecraft().displayHeight / 1027.0);
        return mouseX > minXf && mouseX < maxXf && mouseY > minYf && mouseY < maxYf;
    }

    public static Vec3d getEyesPos() {
        return new Vec3d(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY + Wrapper.INSTANCE.player().getEyeHeight(), Wrapper.INSTANCE.player().posZ);
    }

    public static void teleportToPosition(Vec3d endPosition) {
        boolean wasSneaking = Wrapper.INSTANCE.player().isSneaking();
        if (wasSneaking)
            Wrapper.INSTANCE.sendPacket(new CPacketEntityAction(Wrapper.INSTANCE.player(), CPacketEntityAction.Action.STOP_SNEAKING));
        double startX = Minecraft.getMinecraft().player.posX;
        double startY = Minecraft.getMinecraft().player.posY;
        double startZ = Minecraft.getMinecraft().player.posZ;

        double endX = endPosition.x + 0.5F;
        double endY = endPosition.y + getOffset(new BlockPos(endPosition.x, endPosition.y, endPosition.z)) + 1.0F;
        double endZ = endPosition.z + 0.5F;

        double distance = Math.abs(startX - startY) + Math.abs(startY - endY) + Math.abs(startZ - endZ);

        int count = 0;

        while (distance > 0) {
            distance = Math.abs(startX - endX) + Math.abs(startY - endY) + Math.abs(startZ - endZ);

            if (count > 120) {
                break;
            }

            double offset = (count & 0x1) == 0 ? .25 + 0.15D : .25;

            double diffX = startX - endX;
            double diffY = startY - endY;
            double diffZ = startZ - endZ;

            final double min = Math.min(Math.abs(diffX), offset);
            if (diffX < 0.0D) startX += min;
            if (diffX > 0.0D) startX -= min;
            final double min2 = Math.min(Math.abs(diffY), offset);
            if (diffY < 0.0D) startY += min2;
            if (diffY > 0.0D) startY -= min2;
            double min1 = Math.min(Math.abs(diffZ), offset);
            if (diffZ < 0.0D) startZ += min1;
            if (diffZ > 0.0D) startZ -= min1;

            Objects.requireNonNull(Wrapper.INSTANCE.mc().getConnection()).getNetworkManager().sendPacket(new CPacketPlayer.Position(startX, startY, startZ, true));
            count++;
        }

        if (wasSneaking) {
            Wrapper.INSTANCE.sendPacket(new CPacketEntityAction(Wrapper.INSTANCE.player(), CPacketEntityAction.Action.START_SNEAKING));
        }
    }

    public static double getOffset(BlockPos pos) {
        IBlockState state = Minecraft.getMinecraft().world.getBlockState(pos);
        Block block = state.getBlock();

        double offset = 0;

        double offsety = 1 - block.getSelectedBoundingBox(state, Minecraft.getMinecraft().world, pos).maxY;

        if (block.getSelectedBoundingBox(state, Minecraft.getMinecraft().world, pos).maxY == 1.0 && !block.isFullCube(state)) {
            offsety = 1;
        }
        offset -= offsety;

        return offset;
    }


    public static boolean isBlockMaterial(BlockPos blockPos, Block block) {
        return Wrapper.INSTANCE.world().getBlockState(blockPos).getBlock() == block;
    }

    public static String getPlayerName(EntityPlayer player) {
        player.getGameProfile();
        return player.getGameProfile().getName();
    }

    public static String getEntityNameColor(EntityLivingBase entity) {
        String name = entity.getDisplayName().getFormattedText();
        if (name.contains("\u00a7")) {
            if (name.contains("\u00a71")) {
                return "\u00a71";
            } else if (name.contains("\u00a72")) {
                return "\u00a72";
            } else if (name.contains("\u00a73")) {
                return "\u00a73";
            } else if (name.contains("\u00a74")) {
                return "\u00a74";
            } else if (name.contains("\u00a75")) {
                return "\u00a75";
            } else if (name.contains("\u00a76")) {
                return "\u00a76";
            } else if (name.contains("\u00a77")) {
                return "\u00a77";
            } else if (name.contains("\u00a78")) {
                return "\u00a78";
            } else if (name.contains("\u00a79")) {
                return "\u00a79";
            } else if (name.contains("\u00a70")) {
                return "\u00a70";
            } else if (name.contains("\u00a7e")) {
                return "\u00a7e";
            } else if (name.contains("\u00a7d")) {
                return "\u00a7d";
            } else if (name.contains("\u00a7a")) {
                return "\u00a7a";
            } else if (name.contains("\u00a7b")) {
                return "\u00a7b";
            } else if (name.contains("\u00a7c")) {
                return "\u00a7c";
            } else if (name.contains("\u00a7f")) {
                return "\u00a7f";
            }
        }
        return "null";
    }


    public static boolean checkScreen() {
        return !(Wrapper.INSTANCE.mc().currentScreen instanceof GuiContainer)
                && !(Wrapper.INSTANCE.mc().currentScreen instanceof GuiChat)
                && Wrapper.INSTANCE.mc().currentScreen == null;
    }

    public static void Quit() {
        boolean flag = Wrapper.INSTANCE.mc().isIntegratedServerRunning();
        Minecraft.getMinecraft().world.sendQuittingDisconnectingPacket();
        Wrapper.INSTANCE.mc().loadWorld(null);
        if (flag) {
            Wrapper.INSTANCE.mc().displayGuiScreen(new GuiMainMenu());
        } else {
            Wrapper.INSTANCE.mc().displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
        }
    }


    public static EntityPlayer createPlayer(EntityPlayer entityPlayer, String name, boolean spawnWorld) {
        GameProfile profile;
        if (!name.equals(entityPlayer.getName()))
            profile = new GameProfile(UUID.randomUUID(), name);
        else
            profile = entityPlayer.getGameProfile();
        EntityOtherPlayerMP otherPlayerMP = new EntityOtherPlayerMP(Wrapper.mc.world, profile);

        otherPlayerMP.setPosition(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ);
        otherPlayerMP.inventory = entityPlayer.inventory;
        otherPlayerMP.rotationPitch = entityPlayer.rotationPitch;
        otherPlayerMP.rotationYaw = entityPlayer.rotationYaw;
        otherPlayerMP.rotationYawHead = entityPlayer.rotationYawHead;
        if (spawnWorld)
            Wrapper.mc.world.spawnEntity(otherPlayerMP);
        else otherPlayerMP.setInvisible(true);
        return otherPlayerMP;
    }

    public static int fovDistanceFromMouse(final EntityLivingBase entity) {
        final float[] neededRotations = getNeededRotations(entity.getPositionVector(), 0, 0);
        final float neededYaw = Wrapper.INSTANCE.player().rotationYaw - neededRotations[0];
        final float neededPitch = Wrapper.INSTANCE.player().rotationPitch - neededRotations[1];
        final float distanceFromMouse = MathHelper.sqrt(neededYaw * neededYaw + neededPitch * neededPitch * 2.0f);
        return (int) distanceFromMouse;
    }


    public static class PlaceLocation extends Vec3i {
        public double damage;
        public double Timeset;

        public PlaceLocation(double xIn, double yIn, double zIn, double v) {
            super(xIn, yIn, zIn);
            this.damage = v;
            this.Timeset = 0;
        }
    }
}
