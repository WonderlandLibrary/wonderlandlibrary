/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.world;

import de.Hero.settings.Setting;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventMotion;
import me.Tengoku.Terror.event.events.EventPacket;
import me.Tengoku.Terror.event.events.EventRenderGUI;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.module.combat.KillAura;
import me.Tengoku.Terror.module.render.HeadRotations;
import me.Tengoku.Terror.util.BlockData;
import me.Tengoku.Terror.util.RayCastUtil;
import me.Tengoku.Terror.util.ScaffoldUtils;
import me.Tengoku.Terror.util.Timer;
import me.Tengoku.Terror.util.TimerUtils;
import me.Tengoku.Terror.util.font.FontUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class Scaffold
extends Module {
    private BlockPos currentPos;
    private float[] rotations;
    boolean hasBlock = false;
    TimerUtils timer2;
    private EnumFacing currentFacing;
    private float serverPitch;
    private float serverYaw;
    private BlockData blockBelowData;
    private static List<Block> blacklistedBlocks;
    private boolean rotated = false;
    FontRenderer fr;
    protected int lastSlot = 0;
    private Timer timer = new Timer();
    BlockData blockData;

    @Override
    public void onDisable() {
        super.onDisable();
        Scaffold.mc.timer.timerSpeed = 1.0f;
        if (Minecraft.thePlayer != null) {
            Minecraft.thePlayer.inventory.currentItem = this.lastSlot;
        }
    }

    private boolean rayTrace(float f, float f2) {
        Vec3 vec3 = Minecraft.thePlayer.getPositionEyes(1.0f);
        Vec3 vec32 = RayCastUtil.getVectorForRotation(f2, f);
        Vec3 vec33 = vec3.addVector(vec32.xCoord * 5.0, vec32.yCoord * 5.0, vec32.zCoord * 5.0);
        MovingObjectPosition movingObjectPosition = Minecraft.theWorld.rayTraceBlocks(vec3, vec33, false);
        return movingObjectPosition != null && movingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.currentPos.equals(movingObjectPosition.getBlockPos());
    }

    protected int findBlock() {
        int n = 0;
        while (n < 9) {
            if (Minecraft.thePlayer.inventoryContainer.getSlot(36 + n).getHasStack()) {
                if (Minecraft.thePlayer.inventoryContainer.getSlot(36 + n).getStack().getItem() instanceof ItemBlock) {
                    return n;
                }
            }
            ++n;
        }
        return -1;
    }

    private void hop(float f) {
        Minecraft.thePlayer.jump();
        Minecraft.thePlayer.motionY -= (double)f;
    }

    private void setBlockAndFacing(BlockPos blockPos) {
        if (Minecraft.theWorld.getBlockState(blockPos.add(0, -1, 0)).getBlock() != Blocks.air) {
            this.currentPos = blockPos.add(0, -1, 0);
            this.currentFacing = EnumFacing.UP;
        } else if (Minecraft.theWorld.getBlockState(blockPos.add(-1, 0, 0)).getBlock() != Blocks.air) {
            this.currentPos = blockPos.add(-1, 0, 0);
            this.currentFacing = EnumFacing.EAST;
        } else if (Minecraft.theWorld.getBlockState(blockPos.add(1, 0, 0)).getBlock() != Blocks.air) {
            this.currentPos = blockPos.add(1, 0, 0);
            this.currentFacing = EnumFacing.WEST;
        } else if (Minecraft.theWorld.getBlockState(blockPos.add(0, 0, -1)).getBlock() != Blocks.air) {
            this.currentPos = blockPos.add(0, 0, -1);
            this.currentFacing = EnumFacing.SOUTH;
        } else if (Minecraft.theWorld.getBlockState(blockPos.add(0, 0, 1)).getBlock() != Blocks.air) {
            this.currentPos = blockPos.add(0, 0, 1);
            this.currentFacing = EnumFacing.NORTH;
        } else if (Minecraft.theWorld.getBlockState(blockPos.add(-1, 0, 1)).getBlock() != Blocks.air) {
            this.currentPos = blockPos.add(-1, 0, 1);
            this.currentFacing = EnumFacing.EAST;
        } else if (Minecraft.theWorld.getBlockState(blockPos.add(1, 0, 1)).getBlock() != Blocks.air) {
            this.currentPos = blockPos.add(1, 0, 1);
            this.currentFacing = EnumFacing.NORTH;
        } else if (Minecraft.theWorld.getBlockState(blockPos.add(-1, 0, -1)).getBlock() != Blocks.air) {
            this.currentPos = blockPos.add(-1, 0, -1);
            this.currentFacing = EnumFacing.SOUTH;
        } else if (Minecraft.theWorld.getBlockState(blockPos.add(1, 0, -1)).getBlock() != Blocks.air) {
            this.currentPos = blockPos.add(1, 0, -1);
            this.currentFacing = EnumFacing.SOUTH;
        } else {
            this.currentPos = null;
            this.currentFacing = null;
        }
    }

    @EventTarget
    public void onPre(EventMotion eventMotion) {
        boolean bl = Exodus.INSTANCE.settingsManager.getSettingByClass("Keep Sprint", Scaffold.class).getValBoolean();
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("Rotation Mode").getValString();
        double d = (float)Exodus.INSTANCE.settingsManager.getSettingByModule("SmoothRotationMaxSpeed", this).getValDouble();
        boolean bl2 = Exodus.INSTANCE.settingsManager.getSettingByName("Smooth").getValBoolean();
        if (eventMotion.isPre()) {
            float[] fArray;
            if (bl) {
                Scaffold.mc.timer.timerSpeed = 1.0f;
                Minecraft.thePlayer.setSprinting(true);
            }
            if (string.equalsIgnoreCase("Watchdog")) {
                if (Minecraft.thePlayer != null) {
                    if (Minecraft.theWorld != null) {
                        fArray = ScaffoldUtils.getRotations(ScaffoldUtils.snowball);
                        if (bl2) {
                            eventMotion.setYaw(this.smoothRotation(this.serverYaw, fArray[0], (float)d));
                            eventMotion.setPitch(87.0f);
                        } else {
                            eventMotion.setYaw(fArray[0]);
                            eventMotion.setPitch(87.0f);
                        }
                        if (Exodus.INSTANCE.moduleManager.getModuleByClass(HeadRotations.class).isToggled()) {
                            Minecraft.thePlayer.rotationYawHead = EventMotion.getYaw();
                            Minecraft.thePlayer.renderYawOffset = EventMotion.getYaw();
                            Minecraft.thePlayer.rotationPitchHead = EventMotion.getPitch();
                        }
                    }
                }
            }
            if (string.equalsIgnoreCase("NCP")) {
                fArray = ScaffoldUtils.getRotations(this.blockBelowData.position, this.blockBelowData.face);
                if (Exodus.INSTANCE.moduleManager.getModuleByName("Head Rotations").isToggled()) {
                    Minecraft.thePlayer.rotationYawHead = fArray[0];
                    Minecraft.thePlayer.renderYawOffset = fArray[0];
                    Minecraft.thePlayer.rotationPitchHead = fArray[1];
                }
                if (bl2) {
                    eventMotion.setYaw(this.smoothRotation(this.serverYaw, fArray[0], (float)d));
                    eventMotion.setPitch(this.smoothRotation(this.serverPitch, fArray[1], (float)d));
                } else {
                    eventMotion.setYaw(fArray[0]);
                    eventMotion.setPitch(fArray[1]);
                }
            }
            if (string.equalsIgnoreCase("Backwards")) {
                fArray = ScaffoldUtils.getRotationsBack(this.blockBelowData.position, this.blockBelowData.face);
                if (Exodus.INSTANCE.moduleManager.getModuleByName("Head Rotations").isToggled()) {
                    Minecraft.thePlayer.rotationYawHead = fArray[0];
                    Minecraft.thePlayer.renderYawOffset = fArray[0];
                }
                eventMotion.setYaw(fArray[0]);
                eventMotion.setPitch(fArray[1]);
            }
        }
    }

    @Override
    public void onEnable() {
        if (Minecraft.thePlayer != null) {
            if (Minecraft.theWorld != null) {
                super.onEnable();
                int n = ScaffoldUtils.grabBlockSlot();
                if (n == -1) {
                    this.toggle();
                }
                this.lastSlot = Minecraft.thePlayer.inventory.currentItem;
                this.blockBelowData = null;
                if (Exodus.INSTANCE.moduleManager.getModuleByClass(KillAura.class).isToggled()) {
                    Exodus.INSTANCE.moduleManager.getModuleByClass(KillAura.class).toggle();
                }
                blacklistedBlocks = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.snow_layer, Blocks.ice, Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore, Blocks.chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.tnt, Blocks.gold_ore, Blocks.iron_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.quartz_ore, Blocks.redstone_ore, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_button, Blocks.wooden_button, Blocks.lever);
            }
        }
    }

    public Scaffold() {
        super("Scaffold", 34, Category.WORLD, "Places blocks under the player.");
        this.rotations = new float[2];
        this.fr = Minecraft.fontRendererObj;
        this.timer2 = new TimerUtils();
        this.blockData = new BlockData(this.currentPos, this.currentFacing);
    }

    private void PlaceBlock(BlockPos blockPos, EnumFacing enumFacing, Vec3 vec3) {
        block2: {
            ItemStack itemStack;
            block4: {
                block5: {
                    int n;
                    block3: {
                        itemStack = Minecraft.thePlayer.inventory.getCurrentItem();
                        if (itemStack == null) break block2;
                        n = itemStack.stackSize--;
                        if (itemStack.stackSize > 0) break block3;
                        Minecraft.thePlayer.inventory.mainInventory[Minecraft.thePlayer.inventory.currentItem] = null;
                        break block4;
                    }
                    if (itemStack.stackSize != n) break block5;
                    if (!Minecraft.playerController.isInCreativeMode()) break block4;
                }
                Scaffold.mc.entityRenderer.itemRenderer.resetEquippedProgress();
            }
            Minecraft.playerController.onPlayerRightClick(Minecraft.thePlayer, Minecraft.theWorld, itemStack, blockPos, enumFacing, vec3);
        }
    }

    @EventTarget
    public void onPacket(EventPacket eventPacket) {
        C03PacketPlayer c03PacketPlayer;
        if (Minecraft.thePlayer == null) {
            return;
        }
        if (eventPacket.getPacket() instanceof C03PacketPlayer.C05PacketPlayerLook) {
            c03PacketPlayer = (C03PacketPlayer)eventPacket.getPacket();
            this.serverYaw = c03PacketPlayer.getYaw();
            this.serverPitch = c03PacketPlayer.getPitch();
        }
        if (eventPacket.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
            c03PacketPlayer = (C03PacketPlayer)eventPacket.getPacket();
            this.serverYaw = c03PacketPlayer.getYaw();
            this.serverPitch = c03PacketPlayer.getPitch();
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("Scaffold Mode").getValString();
        this.setDisplayName("Scaffold \ufffdf" + string);
        Minecraft.thePlayer.setSprinting(false);
        boolean bl = Exodus.INSTANCE.settingsManager.getSettingByName("Tower").getValBoolean();
        if (bl) {
            if (Minecraft.gameSettings.keyBindJump.pressed) {
                Minecraft.thePlayer.motionX = 0.0;
                Minecraft.thePlayer.motionZ = 0.0;
                if (Minecraft.thePlayer.onGround) {
                    Minecraft.thePlayer.jump();
                    Minecraft.thePlayer.motionY += (double)0.2f;
                }
            }
        }
        if (string.equalsIgnoreCase("HypixelHop")) {
            if (Minecraft.thePlayer.onGround) {
                this.hop(0.052147653f);
            }
        }
        if (string.equalsIgnoreCase("HypixelSlow")) {
            Scaffold.mc.timer.timerSpeed = 0.95f;
        }
        if (string.equalsIgnoreCase("Hypixel")) {
            Scaffold.mc.timer.timerSpeed = 1.0f;
        }
        if (string.equalsIgnoreCase("AAC")) {
            Scaffold.mc.timer.timerSpeed = 0.85f;
        }
        int n = ScaffoldUtils.grabBlockSlot();
        this.rotated = false;
        this.currentPos = Minecraft.thePlayer.getPosition();
        this.currentFacing = null;
        int n2 = ScaffoldUtils.grabBlockSlot();
        int n3 = Minecraft.thePlayer.inventory.currentItem;
        BlockPos blockPos = new BlockPos(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY - 1.0, Minecraft.thePlayer.posZ);
        if (Minecraft.theWorld.getBlockState(blockPos).getBlock() instanceof BlockAir) {
            this.setBlockAndFacing(blockPos);
        }
        float f = Minecraft.thePlayer.rotationYaw + (float)(Minecraft.thePlayer.moveForward < 0.0f ? 180 : 0) + (Minecraft.thePlayer.moveStrafing > 0.0f ? -90.0f * (Minecraft.thePlayer.moveForward < 0.0f ? -0.5f : (Minecraft.thePlayer.moveForward > 0.0f ? 0.4f : 1.0f)) : 0.0f);
        boolean bl2 = false;
        ItemStack itemStack = Minecraft.thePlayer.getHeldItem();
        int n4 = ScaffoldUtils.grabBlockSlot();
        int n5 = Minecraft.thePlayer.inventory.currentItem;
        boolean bl3 = Exodus.INSTANCE.settingsManager.getSettingByModule("Silent", this).getValBoolean();
        boolean bl4 = Exodus.INSTANCE.settingsManager.getSettingByModule("NoSwing", this).getValBoolean();
        if (!bl3) {
            if (n != -1) {
                Minecraft.thePlayer.inventory.currentItem = n;
            }
        } else if (n != -1) {
            Minecraft.playerController.updateController();
            if (Minecraft.thePlayer.isMoving()) {
                if (bl4) {
                    Minecraft.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                } else {
                    Minecraft.thePlayer.swingItem();
                }
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(n));
                Minecraft.playerController.updateController();
            }
        }
        if (Minecraft.playerController.onPlayerRightClick(Minecraft.thePlayer, Minecraft.theWorld, Minecraft.thePlayer.getCurrentEquippedItem(), this.currentPos, this.currentFacing, new Vec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ))) {
            if (bl4) {
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
            } else {
                Minecraft.thePlayer.swingItem();
            }
        }
    }

    private float smoothRotation(float f, float f2, float f3) {
        float f4 = MathHelper.wrapAngleTo180_float(f2 - f);
        if (f4 > f3) {
            f4 = f3;
        }
        if (f4 < -f3) {
            f4 = -f3;
        }
        return f + f4;
    }

    @EventTarget
    public void onRender2D(EventRenderGUI eventRenderGUI) {
        int n = ScaffoldUtils.grabBlockSlot();
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("HUD Font").getValString();
        if (n != -1) {
            int n2 = Minecraft.thePlayer.inventory.getStackInSlot((int)n).stackSize;
            ScaledResolution scaledResolution = new ScaledResolution(mc);
            float f = 140.0f;
            float f2 = 40.0f;
            float f3 = 40.0f;
            float f4 = scaledResolution.getScaledWidth();
            float f5 = scaledResolution.getScaledHeight();
            float f6 = f4 / 2.0f + 30.0f;
            float f7 = f5 / 2.0f + 30.0f;
            if (string.equalsIgnoreCase("Arial")) {
                FontUtil.normal.drawString(String.valueOf(n2) + " blocks", scaledResolution.getScaledWidth() / 2 + 10, scaledResolution.getScaledHeight() / 2, -1);
            } else {
                this.fr.drawStringWithShadow(String.valueOf(n2) + " blocks", scaledResolution.getScaledWidth() / 2 + 10, scaledResolution.getScaledHeight() / 2, -1);
            }
        }
    }

    private boolean placeBlock(BlockPos blockPos, EnumFacing enumFacing) {
        Vec3 vec3 = new Vec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight(), Minecraft.thePlayer.posZ);
        return Minecraft.playerController.onPlayerRightClick(Minecraft.thePlayer, Minecraft.theWorld, Minecraft.thePlayer.getHeldItem(), blockPos, enumFacing, new Vec3(this.blockBelowData.position).addVector(0.5, 0.5, 0.5).add(new Vec3(this.blockData.face.getDirectionVec())));
    }

    private BlockData getBlockData(BlockPos blockPos) {
        if (!blacklistedBlocks.contains(Minecraft.theWorld.getBlockState(blockPos.add(0, -1, 0)).getBlock())) {
            return new BlockData(blockPos.add(0, -1, 0), EnumFacing.UP);
        }
        if (!blacklistedBlocks.contains(Minecraft.theWorld.getBlockState(blockPos.add(-1, 0, 0)).getBlock())) {
            return new BlockData(blockPos.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!blacklistedBlocks.contains(Minecraft.theWorld.getBlockState(blockPos.add(1, 0, 0)).getBlock())) {
            return new BlockData(blockPos.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!blacklistedBlocks.contains(Minecraft.theWorld.getBlockState(blockPos.add(0, 0, -1)).getBlock())) {
            return new BlockData(blockPos.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!blacklistedBlocks.contains(Minecraft.theWorld.getBlockState(blockPos.add(0, 0, 1)).getBlock())) {
            return new BlockData(blockPos.add(0, 0, 1), EnumFacing.NORTH);
        }
        BlockPos blockPos2 = blockPos.add(-1, 0, 0);
        if (!blacklistedBlocks.contains(Minecraft.theWorld.getBlockState(blockPos2.add(-1, 0, 0)).getBlock())) {
            return new BlockData(blockPos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!blacklistedBlocks.contains(Minecraft.theWorld.getBlockState(blockPos2.add(1, 0, 0)).getBlock())) {
            return new BlockData(blockPos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!blacklistedBlocks.contains(Minecraft.theWorld.getBlockState(blockPos2.add(0, 0, -1)).getBlock())) {
            return new BlockData(blockPos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!blacklistedBlocks.contains(Minecraft.theWorld.getBlockState(blockPos2.add(0, 0, 1)).getBlock())) {
            return new BlockData(blockPos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        BlockPos blockPos3 = blockPos.add(1, 0, 0);
        if (!blacklistedBlocks.contains(Minecraft.theWorld.getBlockState(blockPos3.add(-1, 0, 0)).getBlock())) {
            return new BlockData(blockPos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!blacklistedBlocks.contains(Minecraft.theWorld.getBlockState(blockPos3.add(1, 0, 0)).getBlock())) {
            return new BlockData(blockPos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!blacklistedBlocks.contains(Minecraft.theWorld.getBlockState(blockPos3.add(0, 0, -1)).getBlock())) {
            return new BlockData(blockPos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!blacklistedBlocks.contains(Minecraft.theWorld.getBlockState(blockPos3.add(0, 0, 1)).getBlock())) {
            return new BlockData(blockPos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        BlockPos blockPos4 = blockPos.add(0, 0, -1);
        if (!blacklistedBlocks.contains(Minecraft.theWorld.getBlockState(blockPos4.add(-1, 0, 0)).getBlock())) {
            return new BlockData(blockPos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!blacklistedBlocks.contains(Minecraft.theWorld.getBlockState(blockPos4.add(1, 0, 0)).getBlock())) {
            return new BlockData(blockPos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!blacklistedBlocks.contains(Minecraft.theWorld.getBlockState(blockPos4.add(0, 0, -1)).getBlock())) {
            return new BlockData(blockPos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!blacklistedBlocks.contains(Minecraft.theWorld.getBlockState(blockPos4.add(0, 0, 1)).getBlock())) {
            return new BlockData(blockPos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        BlockPos blockPos5 = blockPos.add(0, 0, 1);
        if (!blacklistedBlocks.contains(Minecraft.theWorld.getBlockState(blockPos5.add(-1, 0, 0)).getBlock())) {
            return new BlockData(blockPos5.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!blacklistedBlocks.contains(Minecraft.theWorld.getBlockState(blockPos5.add(1, 0, 0)).getBlock())) {
            return new BlockData(blockPos5.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!blacklistedBlocks.contains(Minecraft.theWorld.getBlockState(blockPos5.add(0, 0, -1)).getBlock())) {
            return new BlockData(blockPos5.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!blacklistedBlocks.contains(Minecraft.theWorld.getBlockState(blockPos5.add(0, 0, 1)).getBlock())) {
            return new BlockData(blockPos5.add(0, 0, 1), EnumFacing.NORTH);
        }
        return null;
    }

    public static float[] getRotationsBlock(BlockPos blockPos, EnumFacing enumFacing) {
        double d = (double)blockPos.getX() - Minecraft.thePlayer.posX;
        double d2 = (double)blockPos.getY() - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
        double d3 = (double)blockPos.getZ() - Minecraft.thePlayer.posZ;
        double d4 = MathHelper.sqrt_double(d * d + d3 * d3);
        float f = (float)(Math.atan2(d3, d) * 180.0 / Math.PI) - 90.0f;
        float f2 = (float)(-(Math.atan2(d2, d4) * 180.0 / Math.PI));
        return new float[]{f, f2};
    }

    @Override
    public void setup() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("NCP");
        arrayList.add("Hypixel");
        arrayList.add("HypixelHop");
        arrayList.add("HypixelSlow");
        arrayList.add("AAC");
        ArrayList<String> arrayList2 = new ArrayList<String>();
        arrayList2.add("NCP");
        arrayList2.add("Watchdog");
        arrayList2.add("Backwards");
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Scaffold Mode", (Module)this, "NCP", arrayList));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Rotation Mode", (Module)this, "NCP", arrayList2));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Tower", this, false));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Smooth", this, false));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("SmoothRotationMaxSpeed", this, 1.0, 1.0, 20.0, true));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("NoSwing", this, true));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Silent", this, true));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Keep Sprint", this, true));
    }
}

