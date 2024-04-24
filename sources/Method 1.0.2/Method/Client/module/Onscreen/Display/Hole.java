package Method.Client.module.Onscreen.Display;

import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.Onscreen.OnscreenGUI;
import Method.Client.module.Onscreen.PinableFrame;
import Method.Client.utils.Utils;
import Method.Client.utils.visual.RenderUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Block;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import org.lwjgl.input.Keyboard;

import static Method.Client.Main.setmgr;


public final class Hole extends Module {
    public Hole() {
        super("Hole", Keyboard.KEY_NONE, Category.ONSCREEN, "Hole");
    }

    static Setting xpos;
    static Setting ypos;
    static PinableFrame pin;

    @Override
    public void setup() {
        setmgr.add(xpos = new Setting("xpos", this, 200, -20, (mc.displayWidth) + 40, true));
        setmgr.add(ypos = new Setting("ypos", this, 90, -20, (mc.displayHeight) + 40, true));
        pin = new HoleRUN();
        OnscreenGUI.pinableFrames.add(pin);
    }

    @Override
    public void onEnable() {
        PinableFrame.Toggle(pin, true);
    }

    @Override
    public void onDisable() {
        PinableFrame.Toggle(pin, false);
    }

    public static class HoleRUN extends PinableFrame {

        public HoleRUN() {
            super("HoleSET", new String[]{}, (int) ypos.getValDouble(), (int) xpos.getValDouble());
        }

        @Override
        public void setup() {
            xpos.setValDouble(this.x);
            ypos.setValDouble(this.y);
        }

        @Subscribe
        public void onRenderGameOverlay(Text event) {
            int posx = (int) (this.x * RenderUtils.simpleScale(false));
            int posy = (int) (this.y * RenderUtils.simpleScale(true));

            float yaw = 0;
            final int dir = (MathHelper.floor((double) (mc.player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3);
            switch (dir) {
                case 1:
                    yaw = 90;
                    break;
                case 2:
                    yaw = -180;
                    break;
                case 3:
                    yaw = -90;
                    break;
            }
            final BlockPos northPos = this.traceToBlock(mc.getRenderPartialTicks(), yaw);

            final Block north = this.getBlock(northPos);

            if (north != null && north != Blocks.AIR) {
                final int damage = this.getBlockDamage(northPos);

                if (damage != 0)
                    Gui.drawRect(posx + 16, posy, posx + 32, posy + 16, 0x60ff0000);

                this.drawBlock(north, posx + 16, posy);
            }

            final BlockPos southPos = this.traceToBlock(mc.getRenderPartialTicks(), yaw - 180.0f);

            final Block south = this.getBlock(southPos);

            if (south != null && south != Blocks.AIR) {
                final int damage = this.getBlockDamage(southPos);

                if (damage != 0)
                    Gui.drawRect(posx + 16, posy + 32, posx + 32, posy + 48, 0x60ff0000);

                this.drawBlock(south, posx + 16, posy + 32);
            }

            final BlockPos eastPos = this.traceToBlock(mc.getRenderPartialTicks(), yaw + 90.0f);

            final Block east = this.getBlock(eastPos);

            if (east != null && east != Blocks.AIR) {
                final int damage = this.getBlockDamage(eastPos);

                if (damage != 0)
                    Gui.drawRect(posx + 32, posy + 16, posx + 48, posy + 32, 0x60ff0000);

                this.drawBlock(east, posx + 32, posy + 16);
            }

            final BlockPos westPos = this.traceToBlock(mc.getRenderPartialTicks(), yaw - 90.0f);

            final Block west = this.getBlock(westPos);
            if (west != null && west != Blocks.AIR) {
                final int damage = this.getBlockDamage(westPos);

                if (damage != 0)
                    Gui.drawRect(posx, posy + 16, posx + 16, posy + 32, 0x60ff0000);
                this.drawBlock(west, posx, posy + 16);
            }
        }


        private BlockPos traceToBlock(float partialTicks, float yaw) {
            final Vec3d pos = Utils.interpolateEntity(mc.player, partialTicks);
            final Vec3d dir = direction(yaw);

            return new BlockPos(pos.x + dir.x, pos.y, pos.z + dir.z);
        }


        public static Vec3d direction(float yaw) {

            return new Vec3d(Math.cos(degToRad(yaw + 90f)), 0, Math.sin(degToRad(yaw + 90f)));
        }

        public static double degToRad(double deg) {
            return deg * Math.PI / 180.0D;
        }

        private int getBlockDamage(BlockPos pos) {
            for (DestroyBlockProgress destBlockProgress : mc.renderGlobal.damagedBlocks.values()) {
                if (destBlockProgress.getPosition().getX() == pos.getX() && destBlockProgress.getPosition().getY() == pos.getY() && destBlockProgress.getPosition().getZ() == pos.getZ()) {
                    return destBlockProgress.getPartialBlockDamage();
                }
            }
            return 0;
        }

        private Block getBlock(BlockPos pos) {
            final Block block = mc.world.getBlockState(pos).getBlock();

            if ((block == Blocks.BEDROCK) || (block == Blocks.OBSIDIAN)) {
                return block;
            }

            return Blocks.AIR;
        }

        private void drawBlock(Block block, float x, float y) {
            final ItemStack stack = new ItemStack(block);

            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.translate(x, y, 0);
            mc.getRenderItem().renderItemAndEffectIntoGUI(stack, 0, 0);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1, 1, 1, 1);
            GlStateManager.popMatrix();
        }
    }
}

