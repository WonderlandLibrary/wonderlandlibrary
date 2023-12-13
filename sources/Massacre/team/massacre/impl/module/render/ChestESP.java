package team.massacre.impl.module.render;

import java.util.Iterator;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;
import team.massacre.Massacre;
import team.massacre.api.event.api.annotations.Handler;
import team.massacre.api.module.Category;
import team.massacre.api.module.Module;
import team.massacre.api.property.Property;
import team.massacre.api.property.impl.EnumProperty;
import team.massacre.impl.event.EventRender3D;
import team.massacre.utils.Colors;
import team.massacre.utils.OGLUtils;

public class ChestESP extends Module {
   private static ChestESP instance;
   public final EnumProperty<ChestESP.Mode> mode;
   public final Property<Integer> visibleColorProperty;
   public final Property<Integer> occludedColorProperty;
   public final Property<Boolean> visibleFlatProperty;
   public final Property<Boolean> occludedFlatProperty;
   private final Property<Boolean> outlineProperty;
   private final Property<Integer> colorProperty;

   public ChestESP() {
      super("ChestESP", 0, Category.RENDER);
      this.mode = new EnumProperty("Mode", ChestESP.Mode.BOX);
      this.visibleColorProperty = new Property("Visible", Colors.PURPLE, this::isChams);
      this.occludedColorProperty = new Property("Occluded", -1753449217, this::isChams);
      this.visibleFlatProperty = new Property("Visible Flat", false, this::isChams);
      this.occludedFlatProperty = new Property("Occluded Flat", true, this::isChams);
      this.outlineProperty = new Property("Outline", false, () -> {
         return this.mode.getValue() == ChestESP.Mode.BOX;
      });
      this.colorProperty = new Property("Color", 8158463, () -> {
         return this.mode.getValue() != ChestESP.Mode.CHAMS;
      });
      this.addValues(new Property[]{this.mode, this.visibleColorProperty, this.occludedColorProperty, this.visibleFlatProperty, this.occludedFlatProperty, this.outlineProperty, this.colorProperty});
   }

   @Handler
   public void a(EventRender3D event) {
      if (this.mode.getValue() == ChestESP.Mode.BOX) {
         boolean outline = (Boolean)this.outlineProperty.getValue();
         if (outline) {
            GL11.glEnable(2848);
            GL11.glLineWidth(1.0F);
         }

         Iterator var3 = this.mc.theWorld.loadedTileEntityList.iterator();

         while(var3.hasNext()) {
            TileEntity entity = (TileEntity)var3.next();
            if (entity instanceof TileEntityChest) {
               BlockPos pos = entity.getPos();
               AxisAlignedBB bb = entity.getBlockType().getCollisionBoundingBox(this.mc.theWorld, pos, entity.getBlockType().getStateFromMeta(entity.getBlockMetadata()));
               if (bb != null) {
                  GL11.glDisable(2929);
                  OGLUtils.enableBlending();
                  GL11.glDepthMask(false);
                  GL11.glDisable(3553);
                  OGLUtils.color((Integer)this.colorProperty.getValue());
                  double rX = RenderManager.renderPosX;
                  double rY = RenderManager.renderPosY;
                  double rZ = RenderManager.renderPosZ;
                  GL11.glTranslated(-rX, -rY, -rZ);
                  RenderGlobal.func_181561_a(bb, (Boolean)this.outlineProperty.getValue(), true);
                  GL11.glTranslated(rX, rY, rZ);
                  GL11.glEnable(2929);
                  GL11.glDisable(3042);
                  GL11.glDepthMask(true);
                  GL11.glEnable(3553);
               }
            }
         }

         if (outline) {
            GL11.glDisable(2848);
         }
      }

   }

   public static ChestESP getInstance() {
      return instance != null ? instance : (instance = (ChestESP)Massacre.INSTANCE.getModuleManager().getModule(ChestESP.class));
   }

   public static void preOccludedRender(int occludedColor, boolean occludedFlat) {
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(3042);
      if (occludedFlat) {
         GL11.glDisable(2896);
      }

      GL11.glEnable(32823);
      GL11.glPolygonOffset(0.0F, -1000000.0F);
      OpenGlHelper.setLightmapTextureCoords(1, 240.0F, 240.0F);
      GL11.glDepthMask(false);
      OGLUtils.color(occludedColor);
   }

   public static void preVisibleRender(int visibleColor, boolean visibleFlat, boolean occludedFlat) {
      GL11.glDepthMask(true);
      if (occludedFlat && !visibleFlat) {
         GL11.glEnable(2896);
      } else if (!occludedFlat && visibleFlat) {
         GL11.glDisable(2896);
      }

      OGLUtils.color(visibleColor);
      GL11.glDisable(32823);
   }

   public static void postRender(boolean visibleFlat) {
      if (visibleFlat) {
         GL11.glEnable(2896);
      }

      GL11.glEnable(3553);
      GL11.glDisable(3042);
   }

   public boolean isChams() {
      return this.mode.getValue() == ChestESP.Mode.CHAMS;
   }

   private static enum Mode {
      CHAMS,
      BOX;
   }
}
