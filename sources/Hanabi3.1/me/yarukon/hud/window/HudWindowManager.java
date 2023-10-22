package me.yarukon.hud.window;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.Hanabi;
import cn.hanabi.events.EventTick;
import cn.hanabi.modules.ModManager;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import me.yarukon.BlurBuffer;
import me.yarukon.hud.window.impl.WindowPlayerInventory;
import me.yarukon.hud.window.impl.WindowRadar;
import me.yarukon.hud.window.impl.WindowScoreboard;
import me.yarukon.hud.window.impl.WindowSessInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Mouse;

@ObfuscationClass
public class HudWindowManager {
   public static Value blur = new Value("HudWindow", "Disable blur", false);
   public static final ArrayList windows = new ArrayList();
   public Minecraft mc = Minecraft.getMinecraft();
   public HudWindow hoveredWindow;
   public HudWindow focusedWindow;
   public cn.hanabi.modules.modules.render.HudWindow hudWindowInstance;
   public static long startTime = 0L;

   public HudWindowManager() {
      this.register(new WindowSessInfo());
      this.register(new WindowPlayerInventory());
      this.register(new WindowScoreboard());
      this.register(new WindowRadar());
      this.hudWindowInstance = (cn.hanabi.modules.modules.render.HudWindow)ModManager.getModule(cn.hanabi.modules.modules.render.HudWindow.class);
      EventManager.register(this);
   }

   public void register(HudWindow window) {
      windows.add(window);
   }

   public void draw() {
      if (OpenGlHelper.shadersSupported && Minecraft.getMinecraft().getRenderViewEntity() instanceof EntityPlayer && !(Boolean)blur.getValueState()) {
         if (Hanabi.INSTANCE.hasOptifine) {
            if (Hanabi.INSTANCE.fastRenderDisabled(this.mc.gameSettings)) {
               BlurBuffer.updateBlurBuffer(true);
            }
         } else {
            BlurBuffer.updateBlurBuffer(true);
         }
      }

      Iterator var1 = windows.iterator();

      while(var1.hasNext()) {
         HudWindow window = (HudWindow)var1.next();
         if (!window.hide) {
            window.draw();
            window.postDraw();
         }
      }

   }

   public void updateScreen() {
      Iterator var1 = windows.iterator();

      while(var1.hasNext()) {
         HudWindow window = (HudWindow)var1.next();
         if (!window.hide) {
            window.updateScreen();
         }
      }

   }

   public void handleMouseInput(int width, int height) {
      int xx = Mouse.getEventX() * width / this.mc.displayWidth;
      int yy = height - Mouse.getEventY() * height / this.mc.displayHeight - 1;
      this.mouseMove(xx, yy);
   }

   public void mouseMove(int mouseX, int mouseY) {
      this.hoveredWindow = null;

      for(int i = windows.size() - 1; i >= 0; --i) {
         HudWindow window = (HudWindow)windows.get(i);
         if (window.isOnFrame(mouseX, mouseY) && !window.hide) {
            this.hoveredWindow = window;
            break;
         }
      }

   }

   public void mouseClick(int mouseX, int mouseY, int mouseButton) {
      this.updateFocusedWindow();
      if (this.hoveredWindow != null) {
         this.hoveredWindow.mouseClick(mouseX, mouseY, mouseButton);
      }

      this.updateWindowOrder();
   }

   public void mouseCoordinateUpdate(int mouseX, int mouseY) {
      Iterator var3 = windows.iterator();

      while(var3.hasNext()) {
         HudWindow window = (HudWindow)var3.next();
         if (!window.hide) {
            window.mouseCoordinateUpdate(mouseX, mouseY);
         }
      }

   }

   public void mouseRelease(int mouseX, int mouseY, int state) {
      if (this.hoveredWindow != null) {
         this.hoveredWindow.mouseReleased(mouseX, mouseY, state);
      }

   }

   public void updateFocusedWindow() {
      if (this.hoveredWindow == null && this.focusedWindow != null) {
         this.focusedWindow.setFocused(false);
         this.focusedWindow = null;
      } else {
         if (this.focusedWindow != this.hoveredWindow) {
            if (this.focusedWindow != null) {
               this.focusedWindow.setFocused(false);
            }

            this.focusedWindow = this.hoveredWindow;
            this.focusedWindow.setFocused(true);
         }

      }
   }

   public static HudWindow getWindowByID(String windowID) {
      Iterator var1 = windows.iterator();

      HudWindow window;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         window = (HudWindow)var1.next();
      } while(!window.windowID.equals(windowID));

      return window;
   }

   public void updateWindowOrder() {
      windows.sort(Comparator.comparingLong((window) -> {
         return window.lastClickTime;
      }));
   }

   @EventTarget
   public void onPre(EventTick e) {
      Iterator var2 = windows.iterator();

      while(true) {
         while(true) {
            while(var2.hasNext()) {
               HudWindow window = (HudWindow)var2.next();
               switch (window.windowID) {
                  case "SessionInfo":
                     if ((Boolean)this.hudWindowInstance.sessionInfo.getValue() && window.hide) {
                        window.show();
                     } else if (!(Boolean)this.hudWindowInstance.sessionInfo.getValue() && !window.hide) {
                        window.hide();
                     }
                     break;
                  case "PlayerInventory":
                     if ((Boolean)this.hudWindowInstance.plyInventory.getValue() && window.hide) {
                        window.show();
                     } else if (!(Boolean)this.hudWindowInstance.plyInventory.getValue() && !window.hide) {
                        window.hide();
                     }
                     break;
                  case "Scoreboard":
                     if ((Boolean)this.hudWindowInstance.scoreboard.getValue() && window.hide) {
                        window.show();
                     } else if (!(Boolean)this.hudWindowInstance.scoreboard.getValue() && !window.hide) {
                        window.hide();
                     }
                     break;
                  case "Radar":
                     if ((Boolean)this.hudWindowInstance.radar.getValue() && window.hide) {
                        window.show();
                     } else if (!(Boolean)this.hudWindowInstance.radar.getValue() && !window.hide) {
                        window.hide();
                     }
               }
            }

            return;
         }
      }
   }
}
