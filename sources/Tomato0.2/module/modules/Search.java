/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.Cheese;
import cow.milkgod.cheese.commands.Command;
import cow.milkgod.cheese.commands.CommandManager;
import cow.milkgod.cheese.events.Event3D;
import cow.milkgod.cheese.events.EventBlockRender;
import cow.milkgod.cheese.events.EventChatSend;
import cow.milkgod.cheese.events.EventTick;
import cow.milkgod.cheese.files.FileManager;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.module.ModuleManager;
import cow.milkgod.cheese.module.modules.ESP;
import cow.milkgod.cheese.properties.Property;
import cow.milkgod.cheese.utils.Logger;
import cow.milkgod.cheese.utils.R2DUtils;
import cow.milkgod.cheese.utils.R3DUtils;
import cow.milkgod.cheese.utils.Wrapper;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;

public class Search
extends Module {
    public final ArrayList<Block> blocks = new ArrayList();
    public final List<Integer[]> blockCoordinates = new CopyOnWriteArrayList<Integer[]>();
    private Property<Boolean> storage;

    public Search() {
        super("Search", 0, Category.RENDER, 13434624, true, "Draws a box around blocks.", new String[]{"blockesp", "besp", "serch"});
        this.storage = new Property<Boolean>(this, "StorageESP", false);
    }

    @EventTarget
    private void onBlockRender(EventBlockRender render) {
        if (this.blocks.isEmpty()) {
            return;
        }
        int x2 = render.getX();
        int y2 = render.getY();
        int z2 = render.getZ();
        Block block = Wrapper.getBlockAtPos(new BlockPos(x2, y2, z2));
        if (!this.areCoordsLoaded(x2, y2, z2) && this.blocks.contains(block)) {
            this.blockCoordinates.add(new Integer[]{x2, y2, z2});
        }
    }

    @EventTarget
    private void onRender3D(Event3D e2) {
        if (((Boolean)this.storage.value).booleanValue() && Objects.nonNull(Wrapper.mc.theWorld)) {
            this.drawChestOutlines(Event3D.getPartialTicks());
            Wrapper.mc.getFramebuffer().bindFramebuffer(true);
            Wrapper.mc.getFramebuffer().bindFramebufferTexture();
        }
        for (Integer[] block : this.blockCoordinates) {
            Block block2 = Wrapper.getBlockAtPos(new BlockPos(block[0], block[1], block[2]));
            if (!this.blocks.contains(block2)) {
                this.blockCoordinates.remove(block);
                continue;
            }
            ESP.renderOne();
            GL11.glLineWidth(4.0f);
            this.renderESP(block, block2);
            ESP.renderTwo();
            this.renderESP(block, block2);
            ESP.renderThree();
            this.renderESP(block, block2);
            ESP.renderFive();
        }
    }

    private void renderESP(Integer[] coords, Block block) {
        if (!this.blocks.contains(block)) {
            return;
        }
        double x2 = (double)coords[0].intValue() - RenderManager.renderPosX;
        double y2 = (double)coords[1].intValue() - RenderManager.renderPosY;
        double z2 = (double)coords[2].intValue() - RenderManager.renderPosZ;
        double[] maxBounds = new double[]{block.getBlockBoundsMaxX(), block.getBlockBoundsMaxY(), block.getBlockBoundsMaxZ()};
        R2DUtils.glColor(R3DUtils.getBlockColor(block));
        R3DUtils.drawBoundingBox(new AxisAlignedBB(x2, y2, z2, x2 + maxBounds[0], y2 + maxBounds[1], z2 + maxBounds[2]));
    }

    @Override
    public void onEnable() {
        super.onEnable();
        Wrapper.mc.renderGlobal.loadRenderers();
    }

    private boolean areCoordsLoaded(double x2, double y2, double z2) {
        for (Integer[] block : this.blockCoordinates) {
            if ((double)block[0].intValue() != x2 || (double)block[1].intValue() != y2 || (double)block[2].intValue() != z2) continue;
            return true;
        }
        return false;
    }

    public void drawChestOutlines(float partialTicks) {
        int loopList = GL11.glGenLists(1);
        R3DUtils.Stencil.getInstance().startLayer();
        GL11.glPushMatrix();
        Wrapper.mc.entityRenderer.setupCameraTransform(partialTicks, 0);
        GL11.glMatrixMode(5888);
        GL11.glEnable(2884);
        R3DUtils.Camera playerCam = new R3DUtils.Camera(Minecraft.getMinecraft().thePlayer);
        Frustrum frustrum = new Frustrum();
        frustrum.setPosition(playerCam.getPosX(), playerCam.getPosY(), playerCam.getPosZ());
        GL11.glDisable(2929);
        GL11.glDepthMask(true);
        R3DUtils.Stencil.getInstance().setBuffer(true);
        GL11.glNewList(loopList, 4864);
        Iterator var6 = Minecraft.getMinecraft().theWorld.loadedTileEntityList.iterator();
        Color rainbow = R3DUtils.getRainbow(0, 1.0f);
        while (var6.hasNext()) {
            Object obj = var6.next();
            TileEntity entity = (TileEntity)obj;
            if (!(entity instanceof TileEntityLockable)) continue;
            GL11.glLineWidth(4.0f);
            GL11.glEnable(3042);
            GL11.glEnable(2848);
            GL11.glPushMatrix();
            GL11.glDisable(3553);
            R2DUtils.glColor(-1711292672);
            GlStateManager.disableLighting();
            GL11.glTranslated((double)entity.getPos().getX() - RenderManager.renderPosX, (double)entity.getPos().getY() - RenderManager.renderPosY, (double)entity.getPos().getZ() - RenderManager.renderPosZ);
            TileEntityRendererDispatcher.instance.renderTileEntityAt(entity, 0.0, 0.0, 0.0, partialTicks);
            GlStateManager.enableLighting();
            GL11.glEnable(3553);
            GL11.glPopMatrix();
        }
        GL11.glEndList();
        GL11.glPolygonMode(1032, 6913);
        GL11.glCallList(loopList);
        GL11.glPolygonMode(1032, 6912);
        GL11.glCallList(loopList);
        R3DUtils.Stencil.getInstance().setBuffer(false);
        GL11.glPolygonMode(1032, 6914);
        GL11.glCallList(loopList);
        R3DUtils.Stencil.getInstance().cropInside();
        GL11.glPolygonMode(1032, 6913);
        GL11.glCallList(loopList);
        GL11.glPolygonMode(1032, 6912);
        GL11.glCallList(loopList);
        GL11.glPolygonMode(1032, 6914);
        GL11.glColor3d(1.0, 1.0, 1.0);
        Minecraft.getMinecraft().entityRenderer.func_175072_h();
        RenderHelper.disableStandardItemLighting();
        R3DUtils.Stencil.getInstance().stopLayer();
        GL11.glDisable(2960);
        GL11.glPopMatrix();
        Minecraft.getMinecraft().entityRenderer.func_175072_h();
        RenderHelper.disableStandardItemLighting();
        GL11.glDeleteLists(loopList, 1);
    }

    @Override
    protected void addCommand() {
        Cheese.getInstance();
        CommandManager commandManager = Cheese.commandManager;
        CommandManager.addCommand(new Command("Search", "Unknown Option! ", new String[]{"serch", "xray"}, "<add | remove | clear>", "Manages Search"){

            @EventTarget
            public void onChatSend(EventTick event) {
                block13 : {
                    try {
                        String mode = EventChatSend.getMessage().split(" ")[1];
                        if (mode.equalsIgnoreCase("add")) {
                            try {
                                String blockToAdd = EventChatSend.getMessage().split(" ")[2];
                                Block block = Block.getBlockFromName(blockToAdd);
                                if (block != null) {
                                    if (block instanceof BlockChest) {
                                        Search.access$0((Search)Search.this).value = true;
                                        return;
                                    }
                                    Cheese.getInstance();
                                    ((Search)Cheese.moduleManager.getModbyName((String)"Search")).blocks.add(block);
                                    Logger.logChat("Added block \u00a7e" + block.getLocalizedName() + "\u00a77 to Search.");
                                    Wrapper.mc.renderGlobal.loadRenderers();
                                    break block13;
                                }
                                Logger.logChat("Invalid Block!");
                            }
                            catch (Exception e) {
                                Logger.logChat("Invalid Arguments! Search add <BlockID>");
                            }
                            break block13;
                        }
                        if (mode.equalsIgnoreCase("clear")) {
                            Cheese.getInstance();
                            ((Search)Cheese.moduleManager.getModbyName((String)"Search")).blocks.clear();
                            Cheese.getInstance();
                            ((Search)Cheese.moduleManager.getModbyName((String)"Search")).blockCoordinates.clear();
                            Logger.logChat("Search cleared.");
                        } else if (mode.equalsIgnoreCase("storage")) {
                            Search.access$0((Search)Search.this).value = (Boolean)Search.access$0((Search)Search.this).value == false;
                            if (!((Boolean)Search.access$0((Search)Search.this).value).booleanValue()) {
                                Logger.logChat("\u00a7eStorage\u00a77 has been \u00a7cdisabled\u00a77 for \u00a7eSearch\u00a77.");
                            } else {
                                Logger.logChat("\u00a7eStorage\u00a77 has been \u00a7aenabled\u00a77 for \u00a7eSearch\u00a77.");
                            }
                        } else {
                            Logger.logChat(String.valueOf(String.valueOf(this.getErrorMessage())) + this.getArguments());
                        }
                    }
                    catch (Exception e2) {
                        Logger.logChat(String.valueOf(String.valueOf(this.getErrorMessage())) + this.getArguments());
                    }
                }
                Cheese.getInstance();
                Cheese.fileManager.saveFiles();
                this.Toggle();
            }
        });
    }

    static /* synthetic */ Property access$0(Search search) {
        return search.storage;
    }

}

