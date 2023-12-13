package cn.hanabi.modules.modules.render;

import cn.hanabi.Wrapper;
import cn.hanabi.events.EventRender;
import cn.hanabi.events.EventRenderBlock;
import cn.hanabi.injection.interfaces.IEntityRenderer;
import cn.hanabi.injection.interfaces.IRenderManager;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.RenderUtil;
import cn.hanabi.utils.TimeHelper;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;

public class OreTarget extends Mod {
   public static final List toRender = new CopyOnWriteArrayList();
   public static List toRenderPacket = new CopyOnWriteArrayList();
   private final Minecraft mc = Minecraft.getMinecraft();
   private final TimeHelper refresh = new TimeHelper();
   public Value dia = new Value("OreTarget", "Diamond", true);
   public Value gold = new Value("OreTarget", "Gold", true);
   public Value iron = new Value("OreTarget", "Iron", true);
   public Value lapis = new Value("OreTarget", "Lapis", true);
   public Value emerald = new Value("OreTarget", "Emerald", true);
   public Value coal = new Value("OreTarget", "Coal", true);
   public Value redstone = new Value("OreTarget", "Redstone", true);
   public Value tracers = new Value("OreTarget", "Tracers", true);
   public final Value interactiveDelay = new Value("OreTarget", "Interactive Delay", 1000.0, 1000.0, 50000.0, 100.0);
   public final Value blockDis = new Value("OreTarget", "Max Distance", 3.0, 3.0, 9.0, 1.0);
   public Value packet = new Value("OreTarget", "Packet", true);
   public Value bypass = new Value("OreTarget", "Touching Air Or Liquid", true);
   public Value depth = new Value("OreTarget", "TestDepth", 2.0, 1.0, 5.0, 1.0);
   public Value radiusOn = new Value("OreTarget", "Distance Limit Enabled", true);
   public Value radius = new Value("OreTarget", "Distance Limit", 10.0, 5.0, 100.0, 5.0);
   public Value limitEnabled = new Value("OreTarget", "Render Limit Enabled", true);
   public Value limit = new Value("OreTarget", "Render Limit", 10.0, 5.0, 100.0, 5.0);
   public Value refresh_timer = new Value("OreTarget", "Refresh List", 500.0, 0.0, 5000.0, 100.0);
   public Value alpha = new Value("OreTarget", "Alpha", 0.25, 0.0, 1.0, 0.05);
   public Value width = new Value("OreTarget", "Line Width", 2.5, 1.0, 10.0, 0.5);
   TimeHelper timerUtils = new TimeHelper();
   int interlocks = 0;

   public OreTarget() {
      super("OreTarget", Category.RENDER);
   }

   public static void drawOutlinedBlockESP(double x, double y, double z, float red, float green, float blue, float alpha, float lineWidth) {
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glLineWidth(lineWidth);
      GL11.glColor4f(red, green, blue, alpha);
      drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldRenderer = tessellator.getWorldRenderer();
      worldRenderer.begin(3, DefaultVertexFormats.POSITION);
      worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
      tessellator.draw();
      worldRenderer.begin(3, DefaultVertexFormats.POSITION);
      worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
      tessellator.draw();
      worldRenderer.begin(1, DefaultVertexFormats.POSITION);
      worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
      tessellator.draw();
   }

   private Color getColorByBlock(Block block) {
      if (block == Blocks.coal_ore) {
         return new Color(65, 65, 65);
      } else if (block != Blocks.redstone_ore && block != Blocks.lit_redstone_ore) {
         if (block == Blocks.iron_ore) {
            return new Color(255, 185, 115);
         } else if (block == Blocks.gold_ore) {
            return new Color(255, 254, 0);
         } else if (block == Blocks.diamond_ore) {
            return new Color(0, 232, 255);
         } else if (block == Blocks.emerald_ore) {
            return new Color(1, 255, 0);
         } else {
            return block == Blocks.lapis_ore ? new Color(4, 0, 255) : null;
         }
      } else {
         return new Color(255, 65, 67);
      }
   }

   public void onEnable() {
      toRender.clear();
      this.refresh.reset();
      this.mc.renderGlobal.loadRenderers();
   }

   public void onDisable() {
      toRender.clear();
      this.refresh.reset();
      this.mc.renderGlobal.loadRenderers();
   }

   @EventTarget
   public void onTick(EventRender e) {
      if (this.refresh.isDelayComplete(((Double)this.refresh_timer.getValueState()).floatValue())) {
         (new Thread(() -> {
            ArrayList cache = new ArrayList();
            Iterator var2 = toRender.iterator();

            while(var2.hasNext()) {
               BlockPos pos = (BlockPos)var2.next();
               if (this.test(pos)) {
                  cache.add(pos);
               }
            }

            toRender.clear();
            toRender.addAll(cache);
         })).start();
         this.refresh.reset();
      }

   }

   @EventTarget
   public void onRenderBlock(EventRenderBlock event) {
      BlockPos pos = new BlockPos(event.x, event.y, event.z);
      if (!toRender.contains(pos) && this.test(pos) && (!((double)toRender.size() > (Double)this.limit.getValueState()) || !(Boolean)this.limitEnabled.getValueState())) {
         toRender.add(pos);
      }

   }

   public boolean isTarget(BlockPos pos) {
      Block block = this.mc.theWorld.getBlockState(pos).getBlock();
      if (Blocks.diamond_ore.equals(block)) {
         return (Boolean)this.dia.getValueState();
      } else if (Blocks.lapis_ore.equals(block)) {
         return (Boolean)this.lapis.getValueState();
      } else if (Blocks.iron_ore.equals(block)) {
         return (Boolean)this.iron.getValueState();
      } else if (Blocks.gold_ore.equals(block)) {
         return (Boolean)this.gold.getValueState();
      } else if (Blocks.coal_ore.equals(block)) {
         return (Boolean)this.coal.getValueState();
      } else if (Blocks.emerald_ore.equals(block)) {
         return (Boolean)this.emerald.getValueState();
      } else {
         return !Blocks.redstone_ore.equals(block) && !Blocks.lit_redstone_ore.equals(block) ? false : (Boolean)this.redstone.getValueState();
      }
   }

   private Boolean oreTest(BlockPos origPos, Double depth) {
      Collection posesNew = new ArrayList();
      Collection posesLast = new ArrayList(Collections.singletonList(origPos));
      Collection finalList = new ArrayList();

      for(int i = 0; (double)i < depth; ++i) {
         Iterator var7 = posesLast.iterator();

         BlockPos pos;
         while(var7.hasNext()) {
            pos = (BlockPos)var7.next();
            posesNew.add(pos.up());
            posesNew.add(pos.down());
            posesNew.add(pos.north());
            posesNew.add(pos.south());
            posesNew.add(pos.west());
            posesNew.add(pos.east());
         }

         var7 = posesNew.iterator();

         while(var7.hasNext()) {
            pos = (BlockPos)var7.next();
            if (posesLast.contains(pos)) {
               posesNew.remove(pos);
            }
         }

         posesLast = posesNew;
         finalList.addAll(posesNew);
         posesNew = new ArrayList();
      }

      List legitBlocks = Arrays.asList(Blocks.water, Blocks.lava, Blocks.flowing_lava, Blocks.air, Blocks.flowing_water, Blocks.fire);
      return finalList.stream().anyMatch((blockPos) -> {
         return legitBlocks.contains(this.mc.theWorld.getBlockState(blockPos).getBlock());
      });
   }

   public void asyncTest(BlockPos pos) {
      (new Thread(() -> {
         if (this.test(pos)) {
            synchronized(toRender) {
               toRender.add(pos);
            }
         }

      })).start();
   }

   public boolean test(BlockPos pos1) {
      if (!this.isTarget(pos1)) {
         return false;
      } else if ((Boolean)this.bypass.getValueState() && !this.oreTest(pos1, (Double)this.depth.getValueState())) {
         return false;
      } else if ((Boolean)this.radiusOn.getValueState()) {
         return !(this.mc.thePlayer.getDistance((double)pos1.getX(), (double)pos1.getY(), (double)pos1.getZ()) >= (Double)this.radius.getValueState());
      } else {
         return true;
      }
   }

   @EventTarget
   public void onRender(EventRender event) {
      Iterator var2 = toRender.iterator();

      BlockPos blockPos;
      while(var2.hasNext()) {
         blockPos = (BlockPos)var2.next();
         this.renderBlock(blockPos);
      }

      var2 = toRenderPacket.iterator();

      while(var2.hasNext()) {
         blockPos = (BlockPos)var2.next();
         this.renderBlock(blockPos);
      }

   }

   private void renderBlock(BlockPos pos) {
      double x = (double)pos.getX() - ((IRenderManager)this.mc.getRenderManager()).getRenderPosX();
      double y = (double)pos.getY() - ((IRenderManager)this.mc.getRenderManager()).getRenderPosY();
      double z = (double)pos.getZ() - ((IRenderManager)this.mc.getRenderManager()).getRenderPosZ();
      Color color = this.getColorByBlock(this.mc.theWorld.getBlockState(pos).getBlock());
      boolean old = this.mc.gameSettings.viewBobbing;
      this.mc.gameSettings.viewBobbing = false;
      ((IEntityRenderer)this.mc.entityRenderer).setupCameraTransform(Wrapper.getTimer().renderPartialTicks, 2);
      this.mc.gameSettings.viewBobbing = old;
      if ((Boolean)this.tracers.getValue()) {
         RenderUtil.drawLine(color.getRGB(), x, y, z);
      }

      RenderUtil.drawBlockESP(x, y, z, (new Color(255, 255, 255, 0)).getRGB(), color.getRGB(), ((Double)this.alpha.getValue()).floatValue(), ((Double)this.width.getValueState()).floatValue());
   }

   @EventTarget
   public void onMoveBlock(EventRender update) {
      if ((Boolean)this.packet.getValueState()) {
         int size = ((Double)this.blockDis.getValueState()).intValue();
         if (this.timerUtils.isDelayComplete(((Double)this.interactiveDelay.getValueState()).longValue())) {
            toRenderPacket.clear();
            this.packet(size);
            this.timerUtils.reset();
         }
      }

   }

   public void packet(int size) {
      for(int x = -size; x < size; ++x) {
         for(int y = -size; y < size; ++y) {
            for(int z = -size; z < size; ++z) {
               if (this.interlocks >= size - 1) {
                  this.interlocks = 0;
               }

               int BlockX = (int)(this.mc.thePlayer.posX + (double)x);
               int BlockY = (int)(this.mc.thePlayer.posY + (double)z);
               int BlockZ = (int)(this.mc.thePlayer.posZ + (double)z);
               BlockPos blockPos = new BlockPos(BlockX, BlockY, BlockZ);
               List legitBlocks = Arrays.asList(Blocks.water, Blocks.lava, Blocks.flowing_lava, Blocks.air, Blocks.flowing_water, Blocks.fire);
               if (!(this.mc.thePlayer.getDistance((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getX()) < (double)this.interlocks) && this.mc.theWorld.getBlockState(blockPos).getBlock() != legitBlocks) {
                  this.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(Action.ABORT_DESTROY_BLOCK, blockPos, EnumFacing.UP));
                  ++this.interlocks;
                  if (this.isTarget(blockPos)) {
                     toRenderPacket.add(blockPos);
                  }
               }
            }
         }
      }

   }
}
