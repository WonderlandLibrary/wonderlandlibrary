
package Method.Client.module.movement;

import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Utils;
import Method.Client.utils.system.Connection;
import Method.Client.utils.system.Wrapper;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import static Method.Client.Main.setmgr;
import static Method.Client.utils.visual.RenderUtils.RenderBlock;
import static Method.Client.utils.visual.RenderUtils.Standardbb;

public class Teleport extends Module {

    Setting mode = setmgr.add(new Setting("Tp Mode", this, "Reach", "Reach", "Flight"));
    Setting math = setmgr.add(new Setting("Speed", this, false));
    Setting Path = setmgr.add(new Setting("Path", this, 0, 1, 1, .22));
    Setting Land = setmgr.add(new Setting("Land", this, .22, 1, 1, .22));
    Setting TpMode = setmgr.add(new Setting("Tp Draw", this, "Outline", BlockEspOptions()));
    Setting LineWidth = setmgr.add(new Setting("LineWidth", this, 1, 0, 3, false));

    public boolean passPacket = false;
    private BlockPos teleportPosition = null;
    private boolean canDraw;
    private int delay;
    float reach = 0;

    public Teleport() {
        super("Teleport", Keyboard.KEY_NONE, Category.MOVEMENT, "Teleport around");
    }


    @Override
    public void onEnable() {
        if (mode.getValString().equalsIgnoreCase("Reach")) {
            reach = (float) mc.player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        if (mode.getValString().equalsIgnoreCase("Flight")) {
            mc.player.noClip = false;
            passPacket = false;
            teleportPosition = null;
            return;
        }
        canDraw = false;
        mc.player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).setBaseValue(500);

        super.onDisable();
    }


    @Override
    public boolean onPacket(Object packet, Connection.Side side) {
        if (side == Connection.Side.OUT && mode.getValString().equalsIgnoreCase("Flight")) {
            if (packet instanceof CPacketPlayer) {
                return passPacket;
            }
        }
        return true;
    }


    @Subscribe
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (mode.getValString().equalsIgnoreCase("Flight")) {
            RayTraceResult object = Wrapper.INSTANCE.mc().objectMouseOver;
            if (object == null) {
                return;
            }
            EntityPlayerSP player = mc.player;
            GameSettings settings = Wrapper.INSTANCE.mcSettings();
            if (!passPacket) {
                if (settings.keyBindAttack.isKeyDown() && object.typeOfHit == RayTraceResult.Type.BLOCK) {
                    if (Utils.isBlockMaterial(object.getBlockPos(), Blocks.AIR)) {
                        return;
                    }
                    teleportPosition = object.getBlockPos();
                    passPacket = true;
                }
                return;
            }
            player.noClip = false;
            if (settings.keyBindSneak.isKeyDown() && player.onGround) {
                Mathteleport();

            }
            return;
        }
        if ((!Mouse.isButtonDown(0) && Wrapper.INSTANCE.mc().inGameHasFocus || !Wrapper.INSTANCE.mc().inGameHasFocus) && mc.player.getItemInUseCount() == 0) {
            mc.player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).setBaseValue(100);
            canDraw = true;
        } else {
            canDraw = false;
            mc.player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).setBaseValue(reach);
        }
        if (teleportPosition != null && delay == 0 && Mouse.isButtonDown(1)) {
            Mathteleport();
            delay = 5;
        }

        if (delay > 0) {
            delay--;
        }
    }

    private void Mathteleport() {
        if (math.getValBoolean()) {
            Vec3d blockPosition = new Vec3d(teleportPosition.getX(),teleportPosition.getY(), teleportPosition.getZ());

            Utils.teleportToPosition(blockPosition);
            mc.player.setPosition(blockPosition.x, blockPosition.y, blockPosition.z);

            teleportPosition = null;
        } else {
            double x = teleportPosition.getX();
            double y = teleportPosition.getY() + 1;
            double z = teleportPosition.getZ();

            mc.player.setPosition(x, y, z);
            for (int i = 0; i < 1; i++) {
                Wrapper.INSTANCE.sendPacket(new CPacketPlayer.Position(x, y, z, mc.player.onGround));
            }
        }
    }

    @Subscribe
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (!mode.getValString().equalsIgnoreCase("Flight")) {
            return;
        }

        if (!passPacket) {
            mc.player.noClip = true;
            mc.player.fallDistance = 0;
            mc.player.onGround = true;
            mc.player.capabilities.isFlying = false;
            mc.player.motionX = 0.0F;
            mc.player.motionY = 0.0F;
            mc.player.motionZ = 0.0F;
            float speed = 0.5f;
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.player.motionY += speed;
            }
            if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                mc.player.motionY -= speed;
            }
            double MovSpeed = mc.player.rotationYaw + 90F;
            if (mc.gameSettings.keyBindForward.isKeyDown()) {
                if (mc.gameSettings.keyBindLeft.isKeyDown()) {
                    MovSpeed -= 45D;
                } else if (mc.gameSettings.keyBindRight.isKeyDown()) {
                    MovSpeed += 45D;
                }
            } else if (mc.gameSettings.keyBindBack.isKeyDown()) {
                MovSpeed += 180D;
                if (mc.gameSettings.keyBindLeft.isKeyDown()) {
                    MovSpeed += 45D;
                } else if (mc.gameSettings.keyBindRight.isKeyDown()) {
                    MovSpeed -= 45D;
                }
            } else if (mc.gameSettings.keyBindLeft.isKeyDown()) {
                MovSpeed -= 90D;
            } else if (mc.gameSettings.keyBindRight.isKeyDown()) {
                MovSpeed += 90D;
            }
            if (mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown()) {
                mc.player.motionX = Math.cos(Math.toRadians(MovSpeed));
                mc.player.motionZ = Math.sin(Math.toRadians(MovSpeed));
            }
        }
    }


    @Subscribe
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (mode.getValString().equalsIgnoreCase("Flight")) {
            if (teleportPosition == null) {
                return;
            }
            if (teleportPosition.getY() == new BlockPos(mc.player).down().getY()) {
                RenderBlock(TpMode.getValString(), Standardbb(teleportPosition), Path.getcolor(), LineWidth.getValDouble());
                return;
            }
            RenderBlock(TpMode.getValString(), Standardbb(teleportPosition), Land.getcolor(), LineWidth.getValDouble());
            return;
        }
        RayTraceResult object = Wrapper.INSTANCE.mc().objectMouseOver;
        if (object == null) {
            return;
        }
        object.getBlockPos();
        if (canDraw) {
            for (float offset = -2.0F; offset < 18.0F; offset++) {
                double[] mouseOverPos = new double[]{object.getBlockPos().getX(), object.getBlockPos().getY() + offset, object.getBlockPos().getZ()};

                if (BlockTeleport(mouseOverPos)) break;
            }
        } else if (object.entityHit != null) {
            for (float offset = -2.0F; offset < 18.0F; offset++) {
                double[] mouseOverPos = new double[]{object.entityHit.posX, object.entityHit.posY + offset, object.entityHit.posZ};

                if (BlockTeleport(mouseOverPos)) break;
            }
        } else {
            teleportPosition = null;
        }
    }

    private boolean BlockTeleport(double[] mouseOverPos) {
        BlockPos blockBelowPos = new BlockPos(mouseOverPos[0], mouseOverPos[1], mouseOverPos[2]);

        if (canRenderBox(mouseOverPos)) {
            RenderBlock(TpMode.getValString(), Standardbb(new BlockPos(mouseOverPos[0], mouseOverPos[1], mouseOverPos[2])), Path.getcolor(), LineWidth.getValDouble());
            if (Wrapper.INSTANCE.mc().inGameHasFocus) {
                teleportPosition = blockBelowPos;
                return true;
            } else {
                teleportPosition = null;
            }
        }
        return false;
    }

    public boolean canRenderBox(double[] mouseOverPos) {
        boolean canTeleport = false;

        Block blockBelowPos = mc.world.getBlockState(new BlockPos(mouseOverPos[0], mouseOverPos[1] - 1.0F, mouseOverPos[2])).getBlock();
        Block blockPos = mc.world.getBlockState(new BlockPos(mouseOverPos[0], mouseOverPos[1], mouseOverPos[2])).getBlock();
        Block blockAbovePos = mc.world.getBlockState(new BlockPos(mouseOverPos[0], mouseOverPos[1] + 1.0F, mouseOverPos[2])).getBlock();

        boolean validBlockBelow = blockBelowPos.getCollisionBoundingBox(mc.world.getBlockState(
                        new BlockPos(mouseOverPos[0], mouseOverPos[1] - 1.0F, mouseOverPos[2])),
                mc.world, new BlockPos(mouseOverPos[0], mouseOverPos[1] - 1.0F, mouseOverPos[2])) != null;


        boolean validBlock = blockPos.getCollisionBoundingBox(mc.world.getBlockState(
                        new BlockPos(mouseOverPos[0], mouseOverPos[1], mouseOverPos[2])),
                mc.world, new BlockPos(mouseOverPos[0], mouseOverPos[1], mouseOverPos[2])) == null;
        boolean validBlockAbove = blockAbovePos.getCollisionBoundingBox(mc.world.getBlockState(
                        new BlockPos(mouseOverPos[0], mouseOverPos[1] + 1.0F, mouseOverPos[2])),
                mc.world, new BlockPos(mouseOverPos[0], mouseOverPos[1] + 1.0F, mouseOverPos[2])) == null;

        if ((validBlockBelow && validBlock && validBlockAbove)) {
            canTeleport = true;
        }

        return canTeleport;
    }


}
