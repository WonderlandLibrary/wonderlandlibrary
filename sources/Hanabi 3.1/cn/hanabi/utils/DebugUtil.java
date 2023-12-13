package cn.hanabi.utils;

import cn.hanabi.Hanabi;
import java.awt.Color;
import net.minecraft.client.Minecraft;

public class DebugUtil {
   public String message;
   public float x;
   public float y;
   public int index;
   public float timer;
   public boolean removing = false;
   public Translate translate = new Translate(0.0F, 0.0F);
   public Translate Position = new Translate(0.0F, 0.0F);
   public float positionX;
   public FadeState fadeState;
   public Type type;

   public DebugUtil(String Message, int Timer, Type Type) {
      this.fadeState = DebugUtil.FadeState.IN;
      this.message = Message;
      this.timer = (float)Timer;
      this.type = Type;
      this.Position.setX((float)(-Minecraft.getMinecraft().fontRendererObj.getStringWidth(this.message)));
   }

   public void onRender() {
      this.Position.translate(this.positionX, 0.0F, 5.0);
      int color = -1;
      switch (this.type) {
         case NONE:
            color = (new Color(250, 249, 249, 255)).getRGB();
            break;
         case ERROR:
            color = (new Color(255, 45, 45)).getRGB();
            break;
         case INFO:
            color = (new Color(111, 150, 255)).getRGB();
            break;
         case WARNING:
            color = (new Color(255, 200, 0)).getRGB();
            break;
         case SUCESS:
            color = (new Color(0, 200, 0)).getRGB();
      }

      Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(this.message, (float)((int)this.Position.getX()), (float)((int)this.y), color);
      switch (this.fadeState) {
         case IN:
            this.positionX = 100.0F;
            this.fadeState = DebugUtil.FadeState.STAY;
            break;
         case STAY:
            this.timer -= RenderUtil.delta;
            if (this.timer <= 0.0F) {
               this.fadeState = DebugUtil.FadeState.OUT;
            }
            break;
         case OUT:
            this.positionX = (float)(-Minecraft.getMinecraft().fontRendererObj.getStringWidth(this.message));
            if (this.Position.getX() <= this.positionX && this.index == 0) {
               this.removing = true;
               this.fadeState = DebugUtil.FadeState.END;
            }
            break;
         case END:
            Hanabi.INSTANCE.debugUtils.remove(this);
      }

   }

   public static enum FadeState {
      IN,
      STAY,
      OUT,
      END;
   }

   public static enum Type {
      ERROR,
      WARNING,
      SUCESS,
      INFO,
      NONE;
   }
}
