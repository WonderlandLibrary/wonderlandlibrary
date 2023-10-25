package intent.AquaDev.aqua.modules.world;

import de.Hero.settings.Setting;
import events.Event;
import events.EventType;
import events.listeners.EventPacket;
import events.listeners.EventPostRender2D;
import events.listeners.EventPreMotion;
import events.listeners.EventRender2D;
import events.listeners.EventSycItem;
import events.listeners.EventTick;
import events.listeners.EventUpdate;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;
import intent.AquaDev.aqua.modules.movement.Fly;
import intent.AquaDev.aqua.modules.visual.Arraylist;
import intent.AquaDev.aqua.modules.visual.Blur;
import intent.AquaDev.aqua.modules.visual.Shadow;
import intent.AquaDev.aqua.modules.world.Scaffold;
import intent.AquaDev.aqua.utils.RandomUtil;
import intent.AquaDev.aqua.utils.RenderUtil;
import intent.AquaDev.aqua.utils.RotationUtil;
import intent.AquaDev.aqua.utils.TimeUtil;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;

/*
 * Exception performing whole class analysis ignored.
 */
public class Scaffold
extends Module {
    private final BlackList blackList = new BlackList(this);
    public float[] rots = new float[2];
    public float[] lastRots = new float[2];
    public int slot;
    public MovingObjectPosition objectPosition = null;
    private final ArrayList<Vec3> lastPositions = new ArrayList();
    private double[] xyz = new double[3];
    public static BlockData data;
    TimeUtil timeUtil = new TimeUtil();
    private double posY;
    public boolean down;
    private int offGround = 0;

    public Scaffold() {
        super("Scaffold", "Scaffold", 0, Category.World);
        Aqua.setmgr.register(new Setting("Sprint", (Module)this, false));
        Aqua.setmgr.register(new Setting("BlockCount", (Module)this, true));
        Aqua.setmgr.register(new Setting("Swing", (Module)this, false));
        Aqua.setmgr.register(new Setting("Down", (Module)this, false));
        Aqua.setmgr.register(new Setting("SameY", (Module)this, false));
        Aqua.setmgr.register(new Setting("Expand", (Module)this, false));
        Aqua.setmgr.register(new Setting("LegitPlace", (Module)this, false));
        Aqua.setmgr.register(new Setting("Expandlength", (Module)this, 8.0, 0.0, 25.0, false));
        Aqua.setmgr.register(new Setting("ReverseYaw", (Module)this, false));
        Aqua.setmgr.register(new Setting("Shader", (Module)this, "Glow", new String[]{"Glow", "Shadow"}));
        Aqua.setmgr.register(new Setting("Tower", (Module)this, "None", new String[]{"None", "Watchdog", "VerusFast", "IntaveFast"}));
    }

    public void onEnable() {
        this.offGround = 0;
        if (Aqua.setmgr.getSetting("DisablerModes").getCurrentMode().equalsIgnoreCase("Intave")) {
            Scaffold.mc.gameSettings.keyBindSneak.pressed = true;
        }
        Aqua.moduleManager.getModuleByName("Killaura").setState(false);
        this.posY = Scaffold.mc.thePlayer.posY;
        super.onEnable();
    }

    public void onDisable() {
        Scaffold.mc.gameSettings.keyBindSneak.pressed = false;
        Aqua.moduleManager.getModuleByName("Killaura").setState(true);
        super.onDisable();
    }

    public void onEvent(Event event) {
        ScaledResolution sr;
        if (event instanceof EventTick) {
            this.offGround = Scaffold.mc.thePlayer.onGround ? 0 : ++this.offGround;
        }
        if (event instanceof EventRender2D && Aqua.setmgr.getSetting("ScaffoldBlockCount").isState()) {
            sr = new ScaledResolution(mc);
            if (Aqua.setmgr.getSetting("ScaffoldShader").getCurrentMode().equalsIgnoreCase("Shadow")) {
                Shadow.drawGlow(() -> RenderUtil.drawRoundedRect2Alpha((double)((int)((float)sr.getScaledWidth() / 2.0f - 25.0f)), (double)((int)((float)sr.getScaledHeight() / 2.0f + 20.0f)), (double)(Scaffold.mc.fontRendererObj.getStringWidth("Blocks: " + Scaffold.getBlockCount()) + 5), (double)13.0, (double)5.0, (Color)Color.BLACK), (boolean)false);
            } else {
                Arraylist.drawGlowArray(() -> RenderUtil.drawRoundedRect2Alpha((double)((int)((float)sr.getScaledWidth() / 2.0f - 25.0f)), (double)((int)((float)sr.getScaledHeight() / 2.0f + 20.0f)), (double)(Scaffold.mc.fontRendererObj.getStringWidth("Blocks: " + Scaffold.getBlockCount()) + 5), (double)13.0, (double)5.0, (Color)new Color(Aqua.setmgr.getSetting("HUDColor").getColor())), (boolean)false);
            }
            Blur.drawBlurred(() -> RenderUtil.drawRoundedRect((double)((int)((float)sr.getScaledWidth() / 2.0f - 25.0f)), (double)((int)((float)sr.getScaledHeight() / 2.0f + 20.0f)), (double)(Scaffold.mc.fontRendererObj.getStringWidth("Blocks: " + Scaffold.getBlockCount()) + 5), (double)13.0, (double)5.0, (int)new Color(0, 0, 0, 120).getRGB()), (boolean)false);
        }
        if (event instanceof EventPostRender2D) {
            sr = new ScaledResolution(mc);
            if (Aqua.setmgr.getSetting("ScaffoldBlockCount").isState()) {
                RenderUtil.drawRoundedRect2Alpha((double)((int)((float)sr.getScaledWidth() / 2.0f - 25.0f)), (double)((int)((float)sr.getScaledHeight() / 2.0f + 20.0f)), (double)(Scaffold.mc.fontRendererObj.getStringWidth("Blocks: " + Scaffold.getBlockCount()) + 5), (double)13.0, (double)5.0, (Color)new Color(0, 0, 0, 70));
                Aqua.INSTANCE.comfortaa4.drawString("Blocks : " + Scaffold.getBlockCount(), (float)((int)((float)sr.getScaledWidth() / 2.0f - 20.0f)), (float)((int)((float)sr.getScaledHeight() / 2.0f + 23.0f)), -1);
            }
        }
        if (event instanceof EventSycItem && this.getBlockSlot() != -1) {
            ((EventSycItem)event).slot = this.slot = this.getBlockSlot();
        }
        if (event instanceof EventUpdate && event.type == EventType.PRE) {
            this.objectPosition = null;
            data = this.find(new Vec3(0.0, 0.0, 0.0));
        }
        if (event instanceof EventUpdate) {
            if (Aqua.setmgr.getSetting("ScaffoldTower").getCurrentMode().equalsIgnoreCase("Watchdog")) {
                if (this.timeUtil.hasTimePassed(170L)) {
                    Scaffold.mc.thePlayer.motionY = 0.42;
                    this.timeUtil.reset();
                }
                if (Scaffold.isOnGround(0.1)) {
                    Scaffold.mc.thePlayer.motionY = 0.42;
                }
            }
            if (Aqua.setmgr.getSetting("ScaffoldTower").getCurrentMode().equalsIgnoreCase("IntaveFast") && Scaffold.mc.gameSettings.keyBindJump.pressed && !Scaffold.mc.gameSettings.keyBindForward.pressed) {
                if (Scaffold.mc.thePlayer.onGround) {
                    Scaffold.mc.thePlayer.motionY = 0.405;
                }
                if (this.offGround == 5) {
                    float random1 = RandomUtil.instance.nextFloat(0.9908900590734863, 0.9909900590734863);
                    Scaffold.mc.thePlayer.motionY = (Scaffold.mc.thePlayer.motionY - 0.08) * (double)random1;
                }
            }
            if (Aqua.setmgr.getSetting("ScaffoldTower").getCurrentMode().equalsIgnoreCase("VerusFast")) {
                if (Scaffold.mc.gameSettings.keyBindJump.pressed) {
                    Fly.sendPacketUnlogged((Packet)new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, new ItemStack(Items.water_bucket), 0.0f, 0.5f, 0.0f));
                    Fly.sendPacketUnlogged((Packet)new C08PacketPlayerBlockPlacement(new BlockPos(Fly.getX(), Fly.getY() - 1.5, Fly.getZ()), 1, new ItemStack(Blocks.stone.getItem((World)Scaffold.mc.theWorld, new BlockPos(-1, -1, -1))), 0.0f, 0.94f, 0.0f));
                    if (this.timeUtil.hasReached(5000L)) {
                        this.timeUtil.reset();
                    } else {
                        Scaffold.mc.thePlayer.motionY = 0.70096;
                    }
                } else {
                    Scaffold.mc.timer.timerSpeed = 1.0f;
                }
                this.timeUtil.reset();
            }
            if (Scaffold.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed) != null && Aqua.moduleManager.getModuleByName("Scaffold").isToggled()) {
                float motion = (float)MathHelper.getRandomDoubleInRange((Random)new Random(), (double)0.0, (double)0.06);
                if (Scaffold.mc.thePlayer.ticksExisted % 10 == 0) {
                    Scaffold.mc.thePlayer.motionZ = -motion;
                    Scaffold.mc.thePlayer.motionX = motion;
                }
            }
            if (Aqua.setmgr.getSetting("ScaffoldDown").isState()) {
                if (Scaffold.mc.gameSettings.keyBindSneak.pressed) {
                    this.down = true;
                    this.posY = Scaffold.mc.thePlayer.posY - 1.0;
                } else {
                    this.down = false;
                    this.posY = Scaffold.mc.thePlayer.posY;
                }
            }
            if (!Aqua.setmgr.getSetting("DisablerModes").getCurrentMode().equalsIgnoreCase("Intave")) {
                if (Aqua.setmgr.getSetting("ScaffoldSprint").isState()) {
                    Scaffold.mc.thePlayer.setSprinting(true);
                } else {
                    Scaffold.mc.thePlayer.setSprinting(false);
                }
            } else if (Aqua.setmgr.getSetting("ScaffoldSprint").isState() && Aqua.setmgr.getSetting("DisablerModes").getCurrentMode().equalsIgnoreCase("Intave") && !Scaffold.mc.gameSettings.keyBindJump.pressed) {
                Scaffold.mc.thePlayer.setSprinting(true);
            } else {
                Scaffold.mc.thePlayer.setSprinting(false);
            }
            data = this.find(new Vec3(0.0, 0.0, 0.0));
            if (data != null && this.getBlockSlot() != -1) {
                if (!Aqua.setmgr.getSetting("ScaffoldLegitPlace").isState()) {
                    Scaffold.mc.playerController.updateController();
                    Vec3 hitVec = new Vec3((Vec3i)BlockData.getPos()).addVector(0.5, 0.5, 0.5).add(new Vec3(BlockData.getFacing().getDirectionVec()).multi(0.5));
                    if (this.slot != -1 && Scaffold.mc.playerController.onPlayerRightClick(Scaffold.mc.thePlayer, Scaffold.mc.theWorld, Scaffold.mc.thePlayer.inventory.getStackInSlot(this.slot), BlockData.getPos(), BlockData.getFacing(), hitVec)) {
                        if (Aqua.setmgr.getSetting("ScaffoldSwing").isState()) {
                            Scaffold.mc.thePlayer.swingItem();
                        } else {
                            mc.getNetHandler().addToSendQueue((Packet)new C0APacketAnimation());
                        }
                    }
                } else {
                    this.rightClickMouse(Scaffold.mc.thePlayer.inventory.getStackInSlot(this.slot), this.slot);
                }
            }
        }
        if (event instanceof EventPacket) {
            // empty if block
        }
        if (event instanceof EventPreMotion) {
            BlockPos blockPos;
            RotationUtil.yaw = Scaffold.mc.thePlayer.rotationYawHead;
            RotationUtil.pitch = Scaffold.mc.thePlayer.rotationPitchHead;
            if (data == null) {
                return;
            }
            float[] rotation2 = Scaffold.rotationrecode2(data);
            float yaw_ = Scaffold.updateRotation(Scaffold.mc.thePlayer.rotationYawHead, rotation2[0], 180.0f);
            if (!Aqua.setmgr.getSetting("ScaffoldLegitPlace").isState()) {
                ((EventPreMotion)event).setYaw(rotation2[0]);
            } else {
                blockPos = new BlockPos(Scaffold.mc.thePlayer.posX, Scaffold.mc.thePlayer.posY - 1.0, Scaffold.mc.thePlayer.posZ);
                if (Aqua.setmgr.getSetting("ScaffoldLegitPlace").isState()) {
                    if (Scaffold.mc.theWorld.getBlockState(blockPos).getBlock() == Blocks.air) {
                        Scaffold.mc.gameSettings.keyBindSneak.pressed = true;
                    } else if (!Aqua.setmgr.getSetting("DisablerModes").getCurrentMode().equalsIgnoreCase("Intave")) {
                        Scaffold.mc.gameSettings.keyBindSneak.pressed = false;
                    }
                }
                ((EventPreMotion)event).setYaw(Scaffold.mc.thePlayer.rotationYaw + 180.0f);
            }
            if (!Aqua.setmgr.getSetting("ScaffoldLegitPlace").isState()) {
                ((EventPreMotion)event).setPitch(rotation2[1]);
            } else {
                blockPos = new BlockPos(Scaffold.mc.thePlayer.posX, Scaffold.mc.thePlayer.posY - 1.0, Scaffold.mc.thePlayer.posZ);
                ((EventPreMotion)event).setPitch(82.0f);
            }
        }
    }

    private Vec3 getPositionByFace(BlockPos position, EnumFacing facing) {
        Vec3 offset = new Vec3((double)facing.getDirectionVec().getX() / 2.0, (double)facing.getDirectionVec().getY() / 2.0, (double)facing.getDirectionVec().getZ() / 2.0);
        Vec3 point = new Vec3((double)position.getX() + 0.5, (double)position.getY() + 0.5, (double)position.getZ() + 0.5);
        return point.add(offset);
    }

    private boolean rayTrace(Vec3 origin, Vec3 position) {
        Vec3 difference = position.subtract(origin);
        int steps = 10;
        double x = difference.xCoord / (double)steps;
        double y = difference.yCoord / (double)steps;
        double z = difference.zCoord / (double)steps;
        Vec3 point = origin;
        for (int i = 0; i < steps; ++i) {
            point = point.addVector(x, y, z);
            BlockPos blockPosition = new BlockPos(point);
            IBlockState blockState = Scaffold.mc.getMinecraft().theWorld.getBlockState(blockPosition);
            if (blockState.getBlock() instanceof BlockLiquid || blockState.getBlock() instanceof BlockAir) continue;
            AxisAlignedBB boundingBox = blockState.getBlock().getCollisionBoundingBox((World)Scaffold.mc.getMinecraft().theWorld, blockPosition, blockState);
            if (boundingBox == null) {
                boundingBox = new AxisAlignedBB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
            }
            if (!boundingBox.offset((double)blockPosition.getX(), (double)blockPosition.getY(), (double)blockPosition.getZ()).isVecInside(point)) continue;
            return true;
        }
        return false;
    }

    private BlockData find(Vec3 offset3) {
        BlockPos[] offsets;
        double x;
        double xDiff = Scaffold.mc.thePlayer.posX - Scaffold.mc.thePlayer.prevPosX;
        double zDiff = Scaffold.mc.thePlayer.posZ - Scaffold.mc.thePlayer.prevPosZ;
        float expand = (float)Aqua.setmgr.getSetting("ScaffoldExpandlength").getCurrentNumber();
        double d = x = Aqua.setmgr.getSetting("ScaffoldExpand").isState() ? Scaffold.mc.thePlayer.posX + xDiff * (double)expand : Scaffold.mc.thePlayer.posX;
        double y = Aqua.setmgr.getSetting("ScaffoldSameY").isState() || Aqua.setmgr.getSetting("ScaffoldDown").isState() ? (Scaffold.mc.gameSettings.keyBindForward.pressed ? this.posY : Scaffold.mc.thePlayer.posY) : Scaffold.mc.thePlayer.posY;
        double z = Aqua.setmgr.getSetting("ScaffoldExpand").isState() ? Scaffold.mc.thePlayer.posZ + zDiff * (double)expand : Scaffold.mc.thePlayer.posZ;
        EnumFacing[] invert = new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST};
        BlockPos position = new BlockPos(new Vec3(x, y, z).add(offset3)).offset(EnumFacing.DOWN);
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos offset = position.offset(facing);
            if (Scaffold.mc.theWorld.getBlockState(offset).getBlock() instanceof BlockAir || this.rayTrace(Scaffold.mc.thePlayer.getLook(0.0f), this.getPositionByFace(offset, invert[facing.ordinal()]))) continue;
            return new BlockData(invert[facing.ordinal()], offset);
        }
        for (BlockPos offset : offsets = new BlockPos[]{new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, 0, 1), new BlockPos(0, 0, 2), new BlockPos(0, 0, -2), new BlockPos(2, 0, 0), new BlockPos(-2, 0, 0)}) {
            BlockPos offsetPos = position.add(offset.getX(), 0, offset.getZ());
            if (!(Scaffold.mc.theWorld.getBlockState(offsetPos).getBlock() instanceof BlockAir)) continue;
            for (EnumFacing facing : EnumFacing.values()) {
                BlockPos offset2 = offsetPos.offset(facing);
                if (Scaffold.mc.theWorld.getBlockState(offset2).getBlock() instanceof BlockAir || this.rayTrace(Scaffold.mc.thePlayer.getLook(0.01f), this.getPositionByFace(offset, invert[facing.ordinal()]))) continue;
                return new BlockData(invert[facing.ordinal()], offset2);
            }
        }
        return null;
    }

    public int getBlockSlot() {
        for (int i = 0; i < 9; ++i) {
            ItemStack s = Scaffold.mc.thePlayer.inventory.getStackInSlot(i);
            if (s == null || !(s.getItem() instanceof ItemBlock)) continue;
            s.getItem();
            if (!this.blackList.isNotBlackListed(((ResourceLocation)Item.itemRegistry.getNameForObject((Object)s.getItem())).toString())) continue;
            return i;
        }
        return -1;
    }

    public static float[] mouseSens(float yaw, float pitch) {
        float f2 = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6f + 0.2f;
        float f3 = f2 * f2 * f2 * 1.2f;
        yaw -= yaw % f3;
        pitch -= pitch % (f3 * f2);
        return new float[]{yaw, pitch};
    }

    public static float updateRotation(float current, float needed, float speed) {
        float f = MathHelper.wrapAngleTo180_float((float)(needed - current));
        if (f > speed) {
            f = speed;
        }
        if (f < -speed) {
            f = -speed;
        }
        return current + f;
    }

    public static float[] rotationrecode2(BlockData blockData) {
        double x = (double)blockData.getPos().getX() + 0.5 - Scaffold.mc.thePlayer.posX + (double)blockData.getFacing().getFrontOffsetX() / 2.0;
        double z = (double)blockData.getPos().getZ() + 0.5 - Scaffold.mc.thePlayer.posZ + (double)blockData.getFacing().getFrontOffsetZ() / 2.0;
        double y = (double)blockData.getPos().getY() + 0.6;
        double ymax = Scaffold.mc.thePlayer.posY + (double)Scaffold.mc.thePlayer.getEyeHeight() - y;
        double allmax = MathHelper.sqrt_double((double)(x * x + z * z));
        float yaw = Aqua.setmgr.getSetting("ScaffoldReverseYaw").isState() ? (float)(Math.atan2((double)z, (double)x) * 180.0 / Math.PI) - 270.0f : (float)(Math.atan2((double)z, (double)x) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(Math.atan2((double)ymax, (double)allmax) * 180.0 / Math.PI);
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        float f2 = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6f + 0.2f;
        float f3 = f2 * f2 * f2 * 1.2f;
        yaw -= yaw % f3;
        pitch -= pitch % (f3 * f2);
        return new float[]{yaw, MathHelper.clamp_float((float)pitch, (float)78.0f, (float)80.0f)};
    }

    public void rightClickMouse(ItemStack itemstack, int slot) {
        if (!Scaffold.mc.playerController.getIsHittingBlock()) {
            Scaffold.mc.rightClickDelayTimer = 4;
            try {
                switch (Scaffold.mc.objectMouseOver.typeOfHit) {
                    case MISS: {
                        if (Scaffold.mc.playerController.isPlayerRightClickingOnEntity((EntityPlayer)Scaffold.mc.thePlayer, Scaffold.mc.objectMouseOver.entityHit, Scaffold.mc.objectMouseOver) || !Scaffold.mc.playerController.interactWithEntitySendPacket((EntityPlayer)Scaffold.mc.thePlayer, Scaffold.mc.objectMouseOver.entityHit)) break;
                        break;
                    }
                    case BLOCK: {
                        int i;
                        BlockPos blockpos = Scaffold.mc.objectMouseOver.getBlockPos();
                        if (Scaffold.mc.theWorld.getBlockState(blockpos).getBlock().getMaterial() == Material.air) break;
                        int n = i = itemstack != null ? itemstack.stackSize : 0;
                        if (Scaffold.mc.playerController.onPlayerRightClick(Scaffold.mc.thePlayer, Scaffold.mc.theWorld, itemstack, blockpos, Scaffold.mc.objectMouseOver.sideHit, Scaffold.mc.objectMouseOver.hitVec)) {
                            if (!Aqua.setmgr.getSetting("ScaffoldSwing").isState()) {
                                Scaffold.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C0APacketAnimation());
                            } else {
                                Scaffold.mc.thePlayer.swingItem();
                            }
                        }
                        if (itemstack == null) {
                            return;
                        }
                        if (itemstack.stackSize != 0) break;
                        Scaffold.mc.thePlayer.inventory.mainInventory[slot] = null;
                    }
                }
            }
            catch (NullPointerException nullPointerException) {
                // empty catch block
            }
        }
    }

    public static int getBlockCount() {
        int itemCount = 0;
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = Scaffold.mc.thePlayer.inventory.getStackInSlot(i);
            if (stack == null || stack.getItem() == null || !(stack.getItem() instanceof ItemBlock)) continue;
            itemCount += stack.stackSize;
        }
        return itemCount;
    }

    public static boolean isOnGround(double height) {
        return !Scaffold.mc.theWorld.getCollidingBoundingBoxes((Entity)Scaffold.mc.thePlayer, Scaffold.mc.thePlayer.getEntityBoundingBox().offset(0.0, -height, 0.0)).isEmpty();
    }

    public static class BlockData {
        private static EnumFacing facing;
        private static BlockPos pos;

        public BlockData(EnumFacing facing, BlockPos pos) {
            Scaffold.BlockData.facing = facing;
            Scaffold.BlockData.pos = pos;
        }

        public static EnumFacing getFacing() {
            return facing;
        }

        public static BlockPos getPos() {
            return pos;
        }
    }

    public static class BlackList {
        List<String> stringids = new ArrayList();

        public BlackList() {
            this.addID("minecraft:wooden_slab");
            this.addID("minecraft:stone_slab");
            this.addID("minecraft:banner");
            this.addID("minecraft:beacon");
            this.addID("minecraft:trapped_chest");
            this.addID("minecraft:chest");
            this.addID("minecraft:anvil");
            this.addID("minecraft:enchanting_table");
            this.addID("minecraft:crafting_table");
            this.addID("minecraft:furnace");
            this.addID("minecraft:banner");
            this.addID("minecraft:wall_banner");
            this.addID("minecraft:standing_banner");
            this.addID("minecraft:web");
            this.addID("minecraft:sapling");
        }

        public List<String> getStringids() {
            return this.stringids;
        }

        public boolean isNotBlackListed(String blockID) {
            return !this.stringids.contains((Object)blockID);
        }

        public void addID(String id) {
            this.stringids.add(id);
        }
    }
}
