package my.NewSnake.Tank.module.modules.PLAYER;

import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.option.Option;
import my.NewSnake.utils.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.util.ResourceLocation;

@Module.Mod
public class Capes extends Module {
   @Option.Op
   private boolean of;
   @Option.Op
   private boolean portal;
   @Option.Op
   private boolean Wifi;
   @Option.Op
   private boolean maça;
   @Option.Op
   private boolean esmeralda;
   @Option.Op
   private boolean Totem;
   @Option.Op
   private boolean campeao;
   @Option.Op
   private boolean Rainbow;
   @Option.Op
   private boolean Lunar;
   @Option.Op
   private boolean space;
   @Option.Op
   private boolean wing;
   @Option.Op
   private boolean AnimPortal;
   @Option.Op
   private boolean Chuva;
   @Option.Op
   private boolean Sherek;
   @Option.Op
   private boolean CapitaoAmerica;
   @Option.Op
   private boolean hard;
   @Option.Op
   private boolean AINBB;
   @Option.Op
   private boolean Batman;
   @Option.Op
   private boolean sword;
   private int cr;
   private Timer timer = new Timer();
   @Option.Op(
      min = 0.0D,
      max = 200.0D,
      increment = 10.0D
   )
   private float speed;
   @Option.Op
   private boolean afk;
   @Option.Op
   private boolean yt;
   @Option.Op
   private boolean Venom;
   @Option.Op
   private boolean TNT;
   @Option.Op
   private boolean pot;
   @Option.Op
   private boolean sky;
   @Option.Op
   private boolean batata;
   @Option.Op
   private boolean Brazil;
   @Option.Op
   private boolean natal;
   @Option.Op
   private boolean microsoft;

   private void otaku() {
      ++this.cr;
      if (this.cr >= 31) {
         this.cr = 0;
         this.timer.reset();
      }

   }

   private void Wifi() {
      ++this.cr;
      if (this.cr >= 17) {
         this.cr = 1;
         this.timer.reset();
      }

   }

   private void Chuva() {
      ++this.cr;
      if (this.cr >= 4) {
         this.cr = 0;
         this.timer.reset();
      }

   }

   private void Lunar() {
      ++this.cr;
      if (this.cr >= 13) {
         this.cr = 0;
         this.timer.reset();
      }

   }

   private void rainbow() {
      ++this.cr;
      if (this.cr >= 14) {
         this.cr = 0;
         this.timer.reset();
      }

   }

   private void tntz() {
      ++this.cr;
      if (this.cr >= 47) {
         this.cr = 1;
         this.timer.reset();
      }

   }

   private void Totem() {
      ++this.cr;
      if (this.cr >= 15) {
         this.cr = 0;
         this.timer.reset();
      }

   }

   public ResourceLocation getCapes() {
      if (this.pot) {
         return new ResourceLocation("textures/Capas/pot.png");
      } else if (this.yt) {
         return new ResourceLocation("textures/Capas/yt.png");
      } else if (this.maça) {
         return new ResourceLocation("textures/Capas/maÃ§a.png");
      } else if (this.hard) {
         return new ResourceLocation("textures/Capas/hard.png");
      } else if (this.sky) {
         return new ResourceLocation("textures/Capas/sky.png");
      } else if (this.microsoft) {
         return new ResourceLocation("textures/Capas/microsoft.png");
      } else if (this.of) {
         return new ResourceLocation("textures/Capas/of.png");
      } else if (this.sword) {
         return new ResourceLocation("textures/Capas/sword.png");
      } else if (this.natal) {
         return new ResourceLocation("textures/Capas/natal.png");
      } else if (this.space) {
         return new ResourceLocation("textures/Capas/space.png");
      } else if (this.wing) {
         return new ResourceLocation("textures/Capas/wing.png");
      } else if (this.esmeralda) {
         return new ResourceLocation("textures/Capas/esmeralda.png");
      } else if (this.portal) {
         return new ResourceLocation("textures/Capas/portal.png");
      } else if (this.afk) {
         return new ResourceLocation("textures/Capas/afk.png");
      } else if (this.batata) {
         return new ResourceLocation("textures/Capas/batata.png");
      } else if (this.campeao) {
         return new ResourceLocation("textures/Capas/campeao.png");
      } else if (this.Brazil) {
         return new ResourceLocation("textures/Capas/Brazil.png");
      } else if (this.CapitaoAmerica) {
         return new ResourceLocation("textures/Capas/CapitaoAmerica.png");
      } else if (this.Venom) {
         return new ResourceLocation("textures/Capas/Venom.png");
      } else if (this.Batman) {
         return new ResourceLocation("textures/Capas/Batman.png");
      } else {
         if (this.Lunar) {
            if (this.timer.delay(this.speed)) {
               this.Lunar();
               this.timer.reset();
            }

            if (this.cr == 0) {
               return new ResourceLocation("lunar/cape1.png");
            }

            if (this.cr == 1) {
               return new ResourceLocation("lunar/cape2.png");
            }

            if (this.cr == 2) {
               return new ResourceLocation("lunar/cape3.png");
            }

            if (this.cr == 3) {
               return new ResourceLocation("lunar/cape4.png");
            }

            if (this.cr == 4) {
               return new ResourceLocation("lunar/cape5.png");
            }

            if (this.cr == 5) {
               return new ResourceLocation("lunar/cape6.png");
            }

            if (this.cr == 6) {
               return new ResourceLocation("lunar/cape7.png");
            }

            if (this.cr == 7) {
               return new ResourceLocation("lunar/cape8.png");
            }

            if (this.cr == 8) {
               return new ResourceLocation("lunar/cape9.png");
            }

            if (this.cr == 9) {
               return new ResourceLocation("lunar/cape10.png");
            }

            if (this.cr == 10) {
               return new ResourceLocation("lunar/cape11.png");
            }

            if (this.cr == 11) {
               return new ResourceLocation("lunar/cape12.png");
            }

            if (this.cr == 12) {
               return new ResourceLocation("lunar/cape13.png");
            }

            if (this.cr == 13) {
               return new ResourceLocation("lunar/cape14.png");
            }
         }

         if (this.Chuva) {
            if (this.timer.delay(this.speed)) {
               this.Chuva();
               this.timer.reset();
            }

            if (this.cr == 0) {
               return new ResourceLocation("chuva/cape1.png");
            }

            if (this.cr == 1) {
               return new ResourceLocation("chuva/cape2.png");
            }

            if (this.cr == 2) {
               return new ResourceLocation("chuva/cape3.png");
            }

            if (this.cr == 3) {
               return new ResourceLocation("chuva/cape4.png");
            }
         }

         if (this.Totem) {
            if (this.timer.delay(this.speed)) {
               this.Totem();
               this.timer.reset();
            }

            if (this.cr == 0) {
               return new ResourceLocation("totem/cape1.png");
            }

            if (this.cr == 1) {
               return new ResourceLocation("totem/cape2.png");
            }

            if (this.cr == 2) {
               return new ResourceLocation("totem/cape3.png");
            }

            if (this.cr == 3) {
               return new ResourceLocation("totem/cape4.png");
            }

            if (this.cr == 4) {
               return new ResourceLocation("totem/cape5.png");
            }

            if (this.cr == 5) {
               return new ResourceLocation("totem/cape6.png");
            }

            if (this.cr == 6) {
               return new ResourceLocation("totem/cape7.png");
            }

            if (this.cr == 7) {
               return new ResourceLocation("totem/cape8.png");
            }

            if (this.cr == 8) {
               return new ResourceLocation("totem/cape9.png");
            }

            if (this.cr == 9) {
               return new ResourceLocation("totem/cape10.png");
            }

            if (this.cr == 10) {
               return new ResourceLocation("totem/cape11.png");
            }

            if (this.cr == 11) {
               return new ResourceLocation("totem/cape12.png");
            }

            if (this.cr == 12) {
               return new ResourceLocation("totem/cape13.png");
            }

            if (this.cr == 13) {
               return new ResourceLocation("totem/cape14.png");
            }

            if (this.cr == 14) {
               return new ResourceLocation("totem/cape15.png");
            }
         }

         if (this.TNT) {
            if (this.timer.delay(this.speed)) {
               this.tntz();
               this.timer.reset();
            }

            if (this.cr == 1) {
               return new ResourceLocation("TNT/cape1.png");
            }

            if (this.cr == 2) {
               return new ResourceLocation("TNT/cape2.png");
            }

            if (this.cr == 3) {
               return new ResourceLocation("TNT/cape3.png");
            }

            if (this.cr == 4) {
               return new ResourceLocation("TNT/cape4.png");
            }

            if (this.cr == 5) {
               return new ResourceLocation("TNT/cape5.png");
            }

            if (this.cr == 6) {
               return new ResourceLocation("TNT/cape6.png");
            }

            if (this.cr == 7) {
               return new ResourceLocation("TNT/cape7.png");
            }

            if (this.cr == 8) {
               return new ResourceLocation("TNT/cape8.png");
            }

            if (this.cr == 9) {
               return new ResourceLocation("TNT/cape9.png");
            }

            if (this.cr == 10) {
               return new ResourceLocation("TNT/cape10.png");
            }

            if (this.cr == 11) {
               return new ResourceLocation("TNT/cape11.png");
            }

            if (this.cr == 12) {
               return new ResourceLocation("TNT/cape12.png");
            }

            if (this.cr == 13) {
               return new ResourceLocation("TNT/cape13.png");
            }

            if (this.cr == 14) {
               return new ResourceLocation("TNT/cape14.png");
            }

            if (this.cr == 15) {
               return new ResourceLocation("TNT/cape15.png");
            }

            if (this.cr == 16) {
               return new ResourceLocation("TNT/cape16.png");
            }

            if (this.cr == 17) {
               return new ResourceLocation("TNT/cape17.png");
            }

            if (this.cr == 18) {
               return new ResourceLocation("TNT/cape18.png");
            }

            if (this.cr == 19) {
               return new ResourceLocation("TNT/cape19.png");
            }

            if (this.cr == 20) {
               return new ResourceLocation("TNT/cape20.png");
            }

            if (this.cr == 21) {
               return new ResourceLocation("TNT/cape21.png");
            }

            if (this.cr == 22) {
               return new ResourceLocation("TNT/cape22.png");
            }

            if (this.cr == 23) {
               return new ResourceLocation("TNT/cape23.png");
            }

            if (this.cr == 24) {
               return new ResourceLocation("TNT/cape24.png");
            }

            if (this.cr == 25) {
               return new ResourceLocation("TNT/cape25.png");
            }

            if (this.cr == 26) {
               return new ResourceLocation("TNT/cape26.png");
            }

            if (this.cr == 27) {
               return new ResourceLocation("TNT/cape27.png");
            }

            if (this.cr == 28) {
               return new ResourceLocation("TNT/cape28.png");
            }

            if (this.cr == 29) {
               return new ResourceLocation("TNT/cape29.png");
            }

            if (this.cr == 30) {
               return new ResourceLocation("TNT/cape30.png");
            }

            if (this.cr == 31) {
               return new ResourceLocation("TNT/cape31.png");
            }

            if (this.cr == 32) {
               return new ResourceLocation("TNT/cape32.png");
            }

            if (this.cr == 33) {
               return new ResourceLocation("TNT/cape33.png");
            }

            if (this.cr == 34) {
               return new ResourceLocation("TNT/cape34.png");
            }

            if (this.cr == 35) {
               return new ResourceLocation("TNT/cape35.png");
            }

            if (this.cr == 36) {
               return new ResourceLocation("TNT/cape36.png");
            }

            if (this.cr == 37) {
               return new ResourceLocation("TNT/cape37.png");
            }

            if (this.cr == 38) {
               return new ResourceLocation("TNT/cape38.png");
            }

            if (this.cr == 39) {
               return new ResourceLocation("TNT/cape39.png");
            }

            if (this.cr == 40) {
               return new ResourceLocation("TNT/cape40.png");
            }

            if (this.cr == 41) {
               return new ResourceLocation("TNT/cape41.png");
            }

            if (this.cr == 42) {
               return new ResourceLocation("TNT/cape42.png");
            }

            if (this.cr == 43) {
               return new ResourceLocation("TNT/cape43.png");
            }

            if (this.cr == 44) {
               return new ResourceLocation("TNT/cape44.png");
            }

            if (this.cr == 45) {
               return new ResourceLocation("TNT/cape45.png");
            }

            if (this.cr == 46) {
               return new ResourceLocation("TNT/cape46.png");
            }

            if (this.cr == 47) {
               return new ResourceLocation("TNT/cape47.png");
            }
         }

         if (this.Wifi) {
            if (this.timer.delay(this.speed)) {
               this.Wifi();
               this.timer.reset();
            }

            if (this.cr == 1) {
               return new ResourceLocation("speed/cape1.png");
            }

            if (this.cr == 2) {
               return new ResourceLocation("speed/cape2.png");
            }

            if (this.cr == 3) {
               return new ResourceLocation("speed/cape3.png");
            }

            if (this.cr == 4) {
               return new ResourceLocation("speed/cape4.png");
            }

            if (this.cr == 5) {
               return new ResourceLocation("speed/cape5.png");
            }

            if (this.cr == 6) {
               return new ResourceLocation("speed/cape6.png");
            }

            if (this.cr == 7) {
               return new ResourceLocation("speed/cape7.png");
            }

            if (this.cr == 8) {
               return new ResourceLocation("speed/cape8.png");
            }

            if (this.cr == 9) {
               return new ResourceLocation("speed/cape9.png");
            }

            if (this.cr == 10) {
               return new ResourceLocation("speed/cape10.png");
            }

            if (this.cr == 11) {
               return new ResourceLocation("speed/cape11.png");
            }

            if (this.cr == 12) {
               return new ResourceLocation("speed/cape12.png");
            }

            if (this.cr == 13) {
               return new ResourceLocation("speed/cape13.png");
            }

            if (this.cr == 14) {
               return new ResourceLocation("speed/cape14.png");
            }

            if (this.cr == 15) {
               return new ResourceLocation("speed/cape15.png");
            }

            if (this.cr == 16) {
               return new ResourceLocation("speed/cape16.png");
            }

            if (this.cr == 17) {
               return new ResourceLocation("speed/cape17.png");
            }
         }

         if (this.AINBB) {
            if (this.timer.delay(this.speed)) {
               this.otaku();
               this.timer.reset();
            }

            if (this.cr == 0) {
               return new ResourceLocation("AIN/cape1.png");
            }

            if (this.cr == 1) {
               return new ResourceLocation("AIN/cape2.png");
            }

            if (this.cr == 2) {
               return new ResourceLocation("AIN/cape3.png");
            }

            if (this.cr == 3) {
               return new ResourceLocation("AIN/cape4.png");
            }

            if (this.cr == 4) {
               return new ResourceLocation("AIN/cape5.png");
            }

            if (this.cr == 5) {
               return new ResourceLocation("AIN/cape6.png");
            }

            if (this.cr == 6) {
               return new ResourceLocation("AIN/cape7.png");
            }

            if (this.cr == 7) {
               return new ResourceLocation("AIN/cape8.png");
            }

            if (this.cr == 8) {
               return new ResourceLocation("AIN/cape9.png");
            }

            if (this.cr == 9) {
               return new ResourceLocation("AIN/cape10.png");
            }

            if (this.cr == 10) {
               return new ResourceLocation("AIN/cape11.png");
            }

            if (this.cr == 11) {
               return new ResourceLocation("AIN/cape12.png");
            }

            if (this.cr == 12) {
               return new ResourceLocation("AIN/cape13.png");
            }

            if (this.cr == 13) {
               return new ResourceLocation("AIN/cape14.png");
            }

            if (this.cr == 14) {
               return new ResourceLocation("AIN/cape15.png");
            }

            if (this.cr == 15) {
               return new ResourceLocation("AIN/cape16.png");
            }

            if (this.cr == 16) {
               return new ResourceLocation("AIN/cape17.png");
            }

            if (this.cr == 17) {
               return new ResourceLocation("AIN/cape18.png");
            }

            if (this.cr == 18) {
               return new ResourceLocation("AIN/cape19.png");
            }

            if (this.cr == 19) {
               return new ResourceLocation("AIN/cape20.png");
            }

            if (this.cr == 20) {
               return new ResourceLocation("AIN/cape21.png");
            }

            if (this.cr == 21) {
               return new ResourceLocation("AIN/cape22.png");
            }

            if (this.cr == 22) {
               return new ResourceLocation("AIN/cape23.png");
            }

            if (this.cr == 23) {
               return new ResourceLocation("AIN/cape24.png");
            }

            if (this.cr == 24) {
               return new ResourceLocation("AIN/cape25.png");
            }

            if (this.cr == 25) {
               return new ResourceLocation("AIN/cape26.png");
            }

            if (this.cr == 26) {
               return new ResourceLocation("AIN/cape27.png");
            }

            if (this.cr == 27) {
               return new ResourceLocation("AIN/cape28.png");
            }

            if (this.cr == 28) {
               return new ResourceLocation("AIN/cape29.png");
            }

            if (this.cr == 29) {
               return new ResourceLocation("AIN/cape30.png");
            }

            if (this.cr == 30) {
               return new ResourceLocation("AIN/cape31.png");
            }

            if (this.cr == 31) {
               return new ResourceLocation("AIN/cape32.png");
            }
         }

         if (this.AnimPortal) {
            if (this.timer.delay(this.speed)) {
               this.portal();
               this.timer.reset();
            }

            if (this.cr == 0) {
               return new ResourceLocation("PortalAnim/cape1.png");
            }

            if (this.cr == 1) {
               return new ResourceLocation("PortalAnim/cape2.png");
            }

            if (this.cr == 2) {
               return new ResourceLocation("PortalAnim/cape3.png");
            }

            if (this.cr == 3) {
               return new ResourceLocation("PortalAnim/cape4.png");
            }

            if (this.cr == 4) {
               return new ResourceLocation("PortalAnim/cape5.png");
            }

            if (this.cr == 5) {
               return new ResourceLocation("PortalAnim/cape6.png");
            }

            if (this.cr == 6) {
               return new ResourceLocation("PortalAnim/cape7.png");
            }

            if (this.cr == 7) {
               return new ResourceLocation("PortalAnim/cape8.png");
            }

            if (this.cr == 8) {
               return new ResourceLocation("PortalAnim/cape9.png");
            }

            if (this.cr == 9) {
               return new ResourceLocation("PortalAnim/cape10.png");
            }

            if (this.cr == 10) {
               return new ResourceLocation("PortalAnim/cape11.png");
            }

            if (this.cr == 11) {
               return new ResourceLocation("PortalAnim/cape12.png");
            }

            if (this.cr == 12) {
               return new ResourceLocation("PortalAnim/cape13.png");
            }

            if (this.cr == 13) {
               return new ResourceLocation("PortalAnim/cape14.png");
            }

            if (this.cr == 14) {
               return new ResourceLocation("PortalAnim/cape15.png");
            }

            if (this.cr == 15) {
               return new ResourceLocation("PortalAnim/cape16.png");
            }
         }

         if (this.Rainbow) {
            if (this.timer.delay(this.speed)) {
               this.rainbow();
               this.timer.reset();
            }

            if (this.cr == 0) {
               return new ResourceLocation("Rainbow/97-0.png");
            }

            if (this.cr == 1) {
               return new ResourceLocation("Rainbow/97-1.png");
            }

            if (this.cr == 2) {
               return new ResourceLocation("Rainbow/97-2.png");
            }

            if (this.cr == 3) {
               return new ResourceLocation("Rainbow/97-3.png");
            }

            if (this.cr == 4) {
               return new ResourceLocation("Rainbow/97-4.png");
            }

            if (this.cr == 5) {
               return new ResourceLocation("Rainbow/97-5.png");
            }

            if (this.cr == 6) {
               return new ResourceLocation("Rainbow/97-6.png");
            }

            if (this.cr == 7) {
               return new ResourceLocation("Rainbow/97-7.png");
            }

            if (this.cr == 8) {
               return new ResourceLocation("Rainbow/97-8.png");
            }

            if (this.cr == 9) {
               return new ResourceLocation("Rainbow/97-9.png");
            }

            if (this.cr == 10) {
               return new ResourceLocation("Rainbow/97-10.png");
            }

            if (this.cr == 11) {
               return new ResourceLocation("Rainbow/97-11.png");
            }

            if (this.cr == 12) {
               return new ResourceLocation("Rainbow/97-12.png");
            }

            if (this.cr == 13) {
               return new ResourceLocation("Rainbow/97-13.png");
            }

            if (this.cr == 14) {
               return new ResourceLocation("Rainbow/97-14.png");
            }
         }

         if (this.Sherek) {
            if (this.timer.delay(this.speed)) {
               this.Sherek();
               this.timer.reset();
            }

            if (this.cr == 0) {
               return new ResourceLocation("sh/cape1.png");
            }

            if (this.cr == 1) {
               return new ResourceLocation("sh/cape2.png");
            }

            if (this.cr == 2) {
               return new ResourceLocation("sh/cape3.png");
            }

            if (this.cr == 3) {
               return new ResourceLocation("sh/cape4.png");
            }

            if (this.cr == 4) {
               return new ResourceLocation("sh/cape5.png");
            }

            if (this.cr == 5) {
               return new ResourceLocation("sh/cape6.png");
            }

            if (this.cr == 6) {
               return new ResourceLocation("sh/cape7.png");
            }

            if (this.cr == 7) {
               return new ResourceLocation("sh/cape8.png");
            }

            if (this.cr == 8) {
               return new ResourceLocation("sh/cape9.png");
            }

            if (this.cr == 9) {
               return new ResourceLocation("sh/cape10.png");
            }

            if (this.cr == 10) {
               return new ResourceLocation("sh/cape11.png");
            }

            if (this.cr == 11) {
               return new ResourceLocation("sh/cape12.png");
            }

            if (this.cr == 12) {
               return new ResourceLocation("sh/cape13.png");
            }

            if (this.cr == 13) {
               return new ResourceLocation("sh/cape14.png");
            }

            if (this.cr == 14) {
               return new ResourceLocation("sh/cape15.png");
            }

            if (this.cr == 15) {
               return new ResourceLocation("sh/cape16.png");
            }

            if (this.cr == 16) {
               return new ResourceLocation("sh/cape17.png");
            }

            if (this.cr == 17) {
               return new ResourceLocation("sh/cape18.png");
            }

            if (this.cr == 18) {
               return new ResourceLocation("sh/cape19.png");
            }

            if (this.cr == 19) {
               return new ResourceLocation("sh/cape20.png");
            }
         }

         return null;
      }
   }

   private void Sherek() {
      ++this.cr;
      if (this.cr >= 19) {
         this.cr = 0;
         this.timer.reset();
      }

   }

   private void portal() {
      ++this.cr;
      if (this.cr >= 15) {
         this.cr = 0;
         this.timer.reset();
      }

   }

   public boolean canRender(AbstractClientPlayer var1) {
      Minecraft.getMinecraft();
      return var1 == Minecraft.thePlayer;
   }
}
