package Method.Client.module.player;

import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

import static Method.Client.Main.setmgr;
import static Method.Client.utils.visual.RenderUtils.RenderBlock;
import static Method.Client.utils.visual.RenderUtils.Standardbb;


public class Nowall extends Module {
    public Nowall() {
        super("Nowall", Keyboard.KEY_NONE, Category.PLAYER, "Click through walls");
    }

    Setting Storage = setmgr.add(new Setting("Storage", this, true));
    Setting Mine = setmgr.add(new Setting("Mine", this, false));
    Setting Trail = setmgr.add(new Setting("Trail", this, false));

    public Setting idcolor = setmgr.add(new Setting("color", this, .22, 1, 1, 1));
    Setting Drawmode = setmgr.add(new Setting("Mode", this, "Outline", BlockEspOptions()));
    Setting LineWidth = setmgr.add(new Setting("LineWidth", this, 1, 0, 3, false));
    private boolean clicked;
    private boolean focus = false;

    @Subscribe
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (Mine.getValBoolean()) {
            process(mc.player);

            RayTraceResult normal_result = mc.objectMouseOver;

            if (normal_result != null) {
                focus = normal_result.typeOfHit == RayTraceResult.Type.ENTITY;
            }
        }
    }

    @Subscribe
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (Trail.getValBoolean()) {
            ArrayList<BlockPos> blocks = getallinLine();
            for (BlockPos block : blocks) {
                RenderBlock(Drawmode.getValString(), Standardbb(block), idcolor.getcolor(), LineWidth.getValDouble());
            }

        }
    }

    public ArrayList<BlockPos> getallinLine() {
        int distance = 6;
        ArrayList<BlockPos> blocks = new ArrayList<>();
        for (int i = 0; i < distance; i++) {
            Vec3d vec = mc.player.getPositionEyes(mc.getRenderPartialTicks());
            vec = vec.add(mc.player.getLook(mc.getRenderPartialTicks()).scale(i));
            BlockPos pos = new BlockPos(vec);
            blocks.add(pos);
        }
        return blocks;
    }

    private void process(EntityLivingBase event) {
        RayTraceResult bypass_entity_result = event.rayTrace(6, mc.getRenderPartialTicks());
        if (bypass_entity_result != null && focus) {
            if (bypass_entity_result.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos block_pos = bypass_entity_result.getBlockPos();
                if (mc.gameSettings.keyBindAttack.isKeyDown()) {
                    mc.playerController.onPlayerDamageBlock(block_pos, EnumFacing.UP);
                }
            }
        }
    }

    @Override
    public boolean onPacket(Object packet, Connection.Side side) {
        if (side == Connection.Side.OUT && Storage.getValBoolean())
            if (packet instanceof CPacketPlayerTryUseItemOnBlock) {
                if (this.clicked) {
                    this.clicked = false;
                    return true;
                }
                final CPacketPlayerTryUseItemOnBlock packet2 = (CPacketPlayerTryUseItemOnBlock) packet;
                if (mc.currentScreen == null) {
                    final Block block = mc.world.getBlockState(packet2.getPos()).getBlock();
                    final BlockPos usable = findUsableBlock(packet2.getHand(), packet2.getDirection(), packet2.getFacingX(), packet2.getFacingY(), packet2.getFacingZ());
                    if (block.onBlockActivated(mc.world, packet2.getPos(), mc.world.getBlockState(packet2.getPos()), mc.player, packet2.getHand(), packet2.getDirection(), packet2.getFacingX(), packet2.getFacingY(), packet2.getFacingZ())) {
                        return true;
                    }
                    if (usable != null) {
                        mc.player.swingArm(packet2.getHand());
                        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(usable, packet2.getDirection(), packet2.getHand(), packet2.getFacingX(), packet2.getFacingY(), packet2.getFacingZ()));
                        this.clicked = true;
                    } else {
                        final Entity usableEntity = findUsableEntity();
                        if (usableEntity != null) {
                            mc.player.connection.sendPacket(new CPacketUseEntity(usableEntity, packet2.getHand()));
                            this.clicked = true;
                        }
                    }
                }
            }
        return true;
    }

    private Entity findUsableEntity() {
        Entity entity = null;
        for (int i = 0; i <= mc.playerController.getBlockReachDistance(); i++) {
            final AxisAlignedBB bb = this.traceToBlock(i, mc.getRenderPartialTicks());
            float maxDist = mc.playerController.getBlockReachDistance();
            for (Entity e : mc.world.getEntitiesWithinAABBExcludingEntity(mc.player, bb)) {
                float currentDist = mc.player.getDistance(e);
                if (currentDist <= maxDist) {
                    entity = e;
                    maxDist = currentDist;
                }
            }
        }
        return entity;
    }

    private BlockPos findUsableBlock(EnumHand hand, EnumFacing dir, float x, float y, float z) {
        for (int i = 0; i <= mc.playerController.getBlockReachDistance(); i++) {
            final AxisAlignedBB bb = this.traceToBlock(i, mc.getRenderPartialTicks());
            final BlockPos pos = new BlockPos(bb.minX, bb.minY, bb.minZ);
            final Block block = mc.world.getBlockState(pos).getBlock();
            if (block.onBlockActivated(mc.world, pos, mc.world.getBlockState(pos), mc.player, hand, dir, x, y, z)) {
                return new BlockPos(pos);
            }
        }

        return null;
    }

    private AxisAlignedBB traceToBlock(double dist, float partialTicks) {
        final Vec3d pos = mc.player.getPositionEyes(partialTicks);
        final Vec3d angles = mc.player.getLook(partialTicks);
        final Vec3d end = pos.add(angles.x * dist, angles.y * dist, angles.z * dist);
        return new AxisAlignedBB(end.x, end.y, end.z, end.x + 1, end.y + 1, end.z + 1);
    }

}
