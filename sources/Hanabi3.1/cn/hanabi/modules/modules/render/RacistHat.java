package cn.hanabi.modules.modules.render;

import cn.hanabi.events.EventRender;
import cn.hanabi.injection.interfaces.IRenderManager;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.RenderUtil;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import me.yarukon.palette.ColorValue;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

public class RacistHat extends Mod {
   public Value renderInFirstPerson = new Value("BambooHat", "ShowInFirstPerson", false);
   public Value side = new Value("BambooHat", "Side", 45.0, 30.0, 50.0, 1.0);
   public Value stack = new Value("BambooHat", "Stacks", 50.0, 45.0, 200.0, 5.0);
   public ColorValue hatColor = new ColorValue("BambooHat Color", 0.5F, 1.0F, 1.0F, 1.0F, false, false, 10.0F);

   public RacistHat() {
      super("BambooHat", Category.RENDER);
   }

   @EventTarget
   public void onRender3D(EventRender evt) {
      if (mc.gameSettings.thirdPersonView != 0 || (Boolean)this.renderInFirstPerson.getValueState()) {
         this.drawDoli(mc.thePlayer, evt);
      }
   }

   private void drawDoli(EntityLivingBase entity, EventRender evt) {
      double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)evt.getPartialTicks() - ((IRenderManager)mc.getRenderManager()).getRenderPosX();
      double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)evt.getPartialTicks() - ((IRenderManager)mc.getRenderManager()).getRenderPosY();
      double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)evt.getPartialTicks() - ((IRenderManager)mc.getRenderManager()).getRenderPosZ();
      int side = ((Double)this.side.getValueState()).intValue();
      int stack = ((Double)this.stack.getValueState()).intValue();
      GL11.glPushMatrix();
      GL11.glTranslated(x, y + (mc.thePlayer.isSneaking() ? 2.0 : 2.2), z);
      GL11.glRotatef(-entity.width, 0.0F, 1.0F, 0.0F);
      RenderUtil.color(RenderUtil.reAlpha(this.hatColor.getColor(), 0.4F));
      GL11.glDisable(3008);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3553);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glEnable(2884);
      GL11.glEnable(2848);
      GL11.glHint(3154, 4354);
      GL11.glHint(3155, 4354);
      GL11.glLineWidth(1.0F);
      Cylinder c = new Cylinder();
      GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
      c.setDrawStyle(100011);
      c.draw(0.0F, 0.8F, 0.4F, side, stack);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDisable(3042);
      GL11.glEnable(3008);
      GL11.glDepthMask(true);
      GL11.glCullFace(1029);
      GL11.glDisable(2848);
      GL11.glHint(3154, 4352);
      GL11.glHint(3155, 4352);
      GL11.glPopMatrix();
   }
}
