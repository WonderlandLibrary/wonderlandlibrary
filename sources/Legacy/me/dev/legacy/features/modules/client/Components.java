package me.dev.legacy.features.modules.client;

import me.dev.legacy.Legacy;
import me.dev.legacy.event.events.Render2DEvent;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import me.dev.legacy.util.EntityUtil;
import me.dev.legacy.util.MathUtil;
import me.dev.legacy.util.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import static me.dev.legacy.util.RenderUtil.itemRender;

public class Components extends Module {
    private static final ResourceLocation box = new ResourceLocation("textures/gui/container/shulker_box.png");
    private static final double HALF_PI = Math.PI / 2;

    public Setting<Boolean> inventory = register(new Setting("Inventory", false));
    public Setting<Integer> invX = register(new Setting("InvX", 564, 0, 1000, v -> inventory.getValue()));
    public Setting<Integer> invY = register(new Setting("InvY", 467, 0, 1000, v -> inventory.getValue()));
    public Setting<Integer> fineinvX = register(new Setting("InvFineX", 0, v -> inventory.getValue()));
    public Setting<Integer> fineinvY = register(new Setting("InvFineY", 0, v -> inventory.getValue()));
    public Setting<Boolean> renderXCarry = register(new Setting("RenderXCarry", false, v -> inventory.getValue()));
    public Setting<Integer> invH = register(new Setting("InvH", 3, v -> inventory.getValue()));
    public Setting<Boolean> holeHud = register(new Setting("HoleHUD", false));
    public Setting<Integer> holeX = register(new Setting("HoleX", 279, 0, 1000, v -> holeHud.getValue()));
    public Setting<Integer> holeY = register(new Setting("HoleY", 485, 0, 1000, v -> holeHud.getValue()));
    public Setting<Compass> compass = register(new Setting("Compass", Compass.NONE));
    public Setting<Integer> compassX = register(new Setting("CompX", 472, 0, 1000, v -> compass.getValue() != Compass.NONE));
    public Setting<Integer> compassY = register(new Setting("CompY", 424, 0, 1000, v -> compass.getValue() != Compass.NONE));
    public Setting<Integer> scale = register(new Setting("Scale", 3, 0, 10, v -> compass.getValue() != Compass.NONE));
    public Setting<Boolean> playerViewer = register(new Setting("PlayerViewer", false));
    public Setting<Integer> playerViewerX = register(new Setting("PlayerX", 752, 0, 1000, v -> playerViewer.getValue()));
    public Setting<Integer> playerViewerY = register(new Setting("PlayerY", 497, 0, 1000, v -> playerViewer.getValue()));
    public Setting<Float> playerScale = register(new Setting("PlayerScale", 1.0f, 0.1f, 2.0f, v -> playerViewer.getValue()));

    public Components() {
        super("Components", "Hud Components", Category.CLIENT, false, false, true);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        if(fullNullCheck()) {
            return;
        }

        if(playerViewer.getValue()) {
            drawPlayer();
        }

        if(compass.getValue() != Compass.NONE) {
            drawCompass();
        }

        if(holeHud.getValue()) {
            drawOverlay(event.partialTicks);
        }

        if(inventory.getValue()) {
            renderInventory();
        }
        
    }

    public static EntityPlayer getClosestEnemy() {
        EntityPlayer closestPlayer = null;
        for (EntityPlayer player : mc.world.playerEntities) {
            if (player == mc.player) continue;
            if (Legacy.friendManager.isFriend(player)) continue;
            if (closestPlayer == null) {
                closestPlayer = player;
            } else if (mc.player.getDistanceSq(player) < mc.player.getDistanceSq(closestPlayer)) {
                closestPlayer = player;
            }
        }
        return closestPlayer;
    }

    public void drawCompass() {
        final ScaledResolution sr = new ScaledResolution(mc);
        if(compass.getValue() == Compass.LINE) {
            float playerYaw = mc.player.rotationYaw;
            float rotationYaw = MathUtil.wrap(playerYaw);
            RenderUtil.drawRect(compassX.getValue(), compassY.getValue(), compassX.getValue() + 100, compassY.getValue() + renderer.getFontHeight(), 0x75101010);
            RenderUtil.glScissor(compassX.getValue(), compassY.getValue(), compassX.getValue() + 100, compassY.getValue() + renderer.getFontHeight(), sr);
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            final float zeroZeroYaw = MathUtil.wrap((float) (Math.atan2(0 - mc.player.posZ, 0 - mc.player.posX) * 180.0d / Math.PI) - 90.0f);
            RenderUtil.drawLine(compassX.getValue() - rotationYaw + (100 / 2) + zeroZeroYaw, compassY.getValue() + 2, compassX.getValue() - rotationYaw + (100 / 2) + zeroZeroYaw, compassY.getValue() + renderer.getFontHeight() - 2, 2, 0xFFFF1010);
            RenderUtil.drawLine((compassX.getValue() - rotationYaw + (100 / 2)) + 45, compassY.getValue() + 2, (compassX.getValue() - rotationYaw + (100 / 2)) + 45, compassY.getValue() + renderer.getFontHeight() - 2, 2, 0xFFFFFFFF);
            RenderUtil.drawLine((compassX.getValue() - rotationYaw + (100 / 2)) - 45, compassY.getValue() + 2, (compassX.getValue() - rotationYaw + (100 / 2)) - 45, compassY.getValue() + renderer.getFontHeight() - 2, 2, 0xFFFFFFFF);
            RenderUtil.drawLine((compassX.getValue() - rotationYaw + (100 / 2)) + 135, compassY.getValue() + 2, (compassX.getValue() - rotationYaw + (100 / 2)) + 135, compassY.getValue() + renderer.getFontHeight() - 2, 2, 0xFFFFFFFF);
            RenderUtil.drawLine((compassX.getValue() - rotationYaw + (100 / 2)) - 135, compassY.getValue() + 2, (compassX.getValue() - rotationYaw + (100 / 2)) - 135, compassY.getValue() + renderer.getFontHeight() - 2, 2, 0xFFFFFFFF);
            renderer.drawStringWithShadow("n", (compassX.getValue() - rotationYaw + (100 / 2)) + 180 - renderer.getStringWidth("n") / 2.0f, compassY.getValue(), 0xFFFFFFFF);
            renderer.drawStringWithShadow("n", (compassX.getValue() - rotationYaw + (100 / 2)) - 180 - renderer.getStringWidth("n") / 2.0f, compassY.getValue(), 0xFFFFFFFF);
            renderer.drawStringWithShadow("e", (compassX.getValue() - rotationYaw + (100 / 2)) - 90 - renderer.getStringWidth("e") / 2.0f, compassY.getValue(), 0xFFFFFFFF);
            renderer.drawStringWithShadow("s", (compassX.getValue() - rotationYaw + (100 / 2)) - renderer.getStringWidth("s") / 2.0f, compassY.getValue(), 0xFFFFFFFF);
            renderer.drawStringWithShadow("w", (compassX.getValue() - rotationYaw + (100 / 2)) + 90 - renderer.getStringWidth("w") / 2.0f, compassY.getValue(), 0xFFFFFFFF);
            RenderUtil.drawLine((compassX.getValue() + 100 / 2), compassY.getValue() + 1, (compassX.getValue() + 100 / 2), compassY.getValue() + renderer.getFontHeight() - 1, 2, 0xFF909090);
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        } else {
            final double centerX = compassX.getValue();
            final double centerY = compassY.getValue();
            for (Direction dir : Direction.values()) {
                double rad = getPosOnCompass(dir);
                renderer.drawStringWithShadow(dir.name(), (float) (centerX + getX(rad)), (float) (centerY + getY(rad)), dir == Direction.N ? 0xFFFF0000 : 0xFFFFFFFF);
            }
        }
    }

    public void drawPlayer(EntityPlayer player, int x, int y) {
        final EntityPlayer ent = player;
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.rotate(0.0f, 0.0f, 5.0f, 0.0f);
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(playerViewerX.getValue() + 25, playerViewerY.getValue() + 25, 50.0f);
        GlStateManager.scale(-50.0f * playerScale.getValue(), 50.0f * playerScale.getValue(), 50.0f * playerScale.getValue());
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-(float)Math.atan(playerViewerY.getValue() / 40.0f) * 20.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        final RenderManager rendermanager = mc.getRenderManager();
        rendermanager.setPlayerViewY(180.0f);
        rendermanager.setRenderShadow(false);
        try {
            rendermanager.renderEntity(ent, 0.0, 0.0, 0.0, 0.0f, 1.0f, false);
        } catch(Exception ignored) {}
        rendermanager.setRenderShadow(true);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.depthFunc(515);
        GlStateManager.resetColor();
        GlStateManager.disableDepth();
        GlStateManager.popMatrix();
    }

    public void drawPlayer() {
        final EntityPlayer ent = mc.player;
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.rotate(0.0f, 0.0f, 5.0f, 0.0f);
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(playerViewerX.getValue() + 25, playerViewerY.getValue() + 25, 50.0f);
        GlStateManager.scale(-50.0f * playerScale.getValue(), 50.0f * playerScale.getValue(), 50.0f * playerScale.getValue());
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-(float)Math.atan(playerViewerY.getValue() / 40.0f) * 20.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        final RenderManager rendermanager = mc.getRenderManager();
        rendermanager.setPlayerViewY(180.0f);
        rendermanager.setRenderShadow(false);
        try {
            rendermanager.renderEntity(ent, 0.0, 0.0, 0.0, 0.0f, 1.0f, false);
        } catch(Exception ignored) {}
        rendermanager.setRenderShadow(true);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.depthFunc(515);
        GlStateManager.resetColor();
        GlStateManager.disableDepth();
        GlStateManager.popMatrix();
    }

    private double getX(double rad) {
        return Math.sin(rad) * (scale.getValue() * 10);
    }

    private double getY(double rad) {
        final double epicPitch = MathHelper.clamp(mc.player.rotationPitch + 30f, -90f, 90f);
        final double pitchRadians = Math.toRadians(epicPitch); // player pitch
        return Math.cos(rad) * Math.sin(pitchRadians) * (scale.getValue() * 10);
    }

    private enum Direction {
        N,
        W,
        S,
        E
    }

    private static double getPosOnCompass(Direction dir) {
        double yaw = Math.toRadians(MathHelper.wrapDegrees(mc.player.rotationYaw));
        int index = dir.ordinal();
        return yaw + (index * HALF_PI);
    }

    public enum Compass {
        NONE,
        CIRCLE,
        LINE
    }

    public void drawOverlay(float partialTicks) {
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
            default:
        }

        final BlockPos northPos = traceToBlock(partialTicks, yaw);
        final Block north = getBlock(northPos);
        if (north != null && north != Blocks.AIR) {
            final int damage = getBlockDamage(northPos);
            if (damage != 0) {
                RenderUtil.drawRect(holeX.getValue() + 16, holeY.getValue(), holeX.getValue() + 32, holeY.getValue() + 16, 0x60ff0000);
            }
            drawBlock(north, holeX.getValue() + 16, holeY.getValue());
        }

        final BlockPos southPos = traceToBlock(partialTicks, yaw - 180.0f);
        final Block south = getBlock(southPos);
        if (south != null && south != Blocks.AIR) {
            final int damage = getBlockDamage(southPos);
            if (damage != 0) {
                RenderUtil.drawRect(holeX.getValue() + 16, holeY.getValue() + 32, holeX.getValue() + 32, holeY.getValue() + 48, 0x60ff0000);
            }
            drawBlock(south, holeX.getValue() + 16, holeY.getValue() + 32);
        }

        final BlockPos eastPos = traceToBlock(partialTicks, yaw + 90.0f);
        final Block east = getBlock(eastPos);
        if (east != null && east != Blocks.AIR) {
            final int damage = getBlockDamage(eastPos);
            if (damage != 0) {
                RenderUtil.drawRect(holeX.getValue() + 32, holeY.getValue() + 16, holeX.getValue() + 48, holeY.getValue() + 32, 0x60ff0000);
            }
            drawBlock(east, holeX.getValue() + 32, holeY.getValue() + 16);
        }

        final BlockPos westPos = traceToBlock(partialTicks, yaw - 90.0f);
        final Block west = getBlock(westPos);
        if (west != null && west != Blocks.AIR) {
            final int damage = getBlockDamage(westPos);

            if (damage != 0) {
                RenderUtil.drawRect(holeX.getValue(), holeY.getValue() + 16, holeX.getValue() + 16, holeY.getValue() + 32, 0x60ff0000);
            }
            drawBlock(west, holeX.getValue(), holeY.getValue() + 16);
        }
    }

    public void drawOverlay(float partialTicks, Entity player, int x, int y) {
        float yaw = 0;
        final int dir = (MathHelper.floor((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3);

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
            default:
        }

        final BlockPos northPos = traceToBlock(partialTicks, yaw, player);
        final Block north = getBlock(northPos);
        if (north != null && north != Blocks.AIR) {
            final int damage = getBlockDamage(northPos);
            if (damage != 0) {
                RenderUtil.drawRect(x + 16, y, x + 32, y + 16, 0x60ff0000);
            }
            drawBlock(north, x + 16, y);
        }

        final BlockPos southPos = traceToBlock(partialTicks, yaw - 180.0f, player);
        final Block south = getBlock(southPos);
        if (south != null && south != Blocks.AIR) {
            final int damage = getBlockDamage(southPos);
            if (damage != 0) {
                RenderUtil.drawRect(x + 16, y + 32, x + 32, y + 48, 0x60ff0000);
            }
            drawBlock(south, x + 16, y + 32);
        }

        final BlockPos eastPos = traceToBlock(partialTicks, yaw + 90.0f, player);
        final Block east = getBlock(eastPos);
        if (east != null && east != Blocks.AIR) {
            final int damage = getBlockDamage(eastPos);
            if (damage != 0) {
                RenderUtil.drawRect(x + 32, y + 16, x + 48, y + 32, 0x60ff0000);
            }
            drawBlock(east, x + 32, y + 16);
        }

        final BlockPos westPos = traceToBlock(partialTicks, yaw - 90.0f, player);
        final Block west = getBlock(westPos);
        if (west != null && west != Blocks.AIR) {
            final int damage = getBlockDamage(westPos);

            if (damage != 0) {
                RenderUtil.drawRect(x, y + 16, x + 16, y + 32, 0x60ff0000);
            }
            drawBlock(west, x, y + 16);
        }
    }

    private int getBlockDamage(BlockPos pos) {
        for (DestroyBlockProgress destBlockProgress : mc.renderGlobal.damagedBlocks.values()) {
            if (destBlockProgress.getPosition().getX() == pos.getX() && destBlockProgress.getPosition().getY() == pos.getY() && destBlockProgress.getPosition().getZ() == pos.getZ()) {
                return destBlockProgress.getPartialBlockDamage();
            }
        }
        return 0;
    }

    private BlockPos traceToBlock(float partialTicks, float yaw) {
        final Vec3d pos = EntityUtil.interpolateEntity(mc.player, partialTicks);
        final Vec3d dir = MathUtil.direction(yaw);
        return new BlockPos(pos.x + dir.x, pos.y, pos.z + dir.z);
    }

    private BlockPos traceToBlock(float partialTicks, float yaw, Entity player) {
        final Vec3d pos = EntityUtil.interpolateEntity(player, partialTicks);
        final Vec3d dir = MathUtil.direction(yaw);
        return new BlockPos(pos.x + dir.x, pos.y, pos.z + dir.z);
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
        mc.getRenderItem().zLevel = 501;
        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, 0, 0);
        mc.getRenderItem().zLevel = 0.f;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.popMatrix();
    }

    public void renderInventory() {
        boxrender(invX.getValue() + fineinvX.getValue(), invY.getValue() + fineinvY.getValue());
        itemrender(mc.player.inventory.mainInventory, invX.getValue() + fineinvX.getValue(), invY.getValue() + fineinvY.getValue());
    }

    private static void preboxrender() {
        GL11.glPushMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.clear(256);
        GlStateManager.enableBlend();
        GlStateManager.color(255, 255, 255, 255);
    }

    private static void postboxrender() {
        GlStateManager.disableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
        GL11.glPopMatrix();
    }

    private static void preitemrender() {
        GL11.glPushMatrix();
        GL11.glDepthMask(true);
        GlStateManager.clear(256);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.scale(1.0f, 1.0f, 0.01f);
    }

    private static void postitemrender() {
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GL11.glPopMatrix();
    }

    private void boxrender(final int x, final int y) {
        preboxrender();
        mc.renderEngine.bindTexture(box);
        RenderUtil.drawTexturedRect(x, y, 0, 0, 176, 16, 500);
        RenderUtil.drawTexturedRect(x, y + 16, 0, 16, 176, 54 + invH.getValue(), 500);
        RenderUtil.drawTexturedRect(x, y + 16 + 54, 0, 160, 176, 8, 500);
        postboxrender();
    }

    private void itemrender(final NonNullList<ItemStack> items, final int x, final int y) {
        for (int i = 0; i < items.size() - 9; i++) {
            int iX = x + (i % 9) * (18) + 8;
            int iY = y + (i / 9) * (18) + 18;
            ItemStack itemStack = items.get(i + 9);
            preitemrender();
            mc.getRenderItem().zLevel = 501;
            itemRender.renderItemAndEffectIntoGUI(itemStack, iX, iY);
            itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, itemStack, iX, iY, null);
            mc.getRenderItem().zLevel = 0.f;
            postitemrender();
        }

        if(renderXCarry.getValue()) {
            for (int i = 1; i < 5; i++) {
                int iX = x + ((i + 4) % 9) * (18) + 8;
                ItemStack itemStack = mc.player.inventoryContainer.inventorySlots.get(i).getStack();
                if(itemStack != null && !itemStack.isEmpty) {
                    preitemrender();
                    mc.getRenderItem().zLevel = 501;
                    itemRender.renderItemAndEffectIntoGUI(itemStack, iX, y + 1);
                    itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, itemStack, iX, y + 1, null);
                    mc.getRenderItem().zLevel = 0.f;
                    postitemrender();
                }
            }
        }
        /*
        for(int size = items.size(), item = 9; item < size; ++item) {
            final int slotx = x + 1 + item % 9 * 18;
            final int sloty = y + 1 + (item / 9 - 1) * 18;
            preitemrender();
            mc.getRenderItem().renderItemAndEffectIntoGUI(items.get(item), slotx, sloty);
            mc.getRenderItem().renderItemOverlays(mc.fontRenderer, items.get(item), slotx, sloty);
            postitemrender();
        }*/
    }

    public static void drawCompleteImage(int posX, int posY, int width, int height) {
        GL11.glPushMatrix();
        GL11.glTranslatef(posX, posY, 0.0F);
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0F, 0.0F);
        GL11.glVertex3f(0.0F, 0.0F, 0.0F);
        GL11.glTexCoord2f(0.0F, 1.0F);
        GL11.glVertex3f(0.0F, height, 0.0F);
        GL11.glTexCoord2f(1.0F, 1.0F);
        GL11.glVertex3f(width, height, 0.0F);
        GL11.glTexCoord2f(1.0F, 0.0F);
        GL11.glVertex3f(width, 0.0F, 0.0F);
        GL11.glEnd();
        GL11.glPopMatrix();
    }

}
