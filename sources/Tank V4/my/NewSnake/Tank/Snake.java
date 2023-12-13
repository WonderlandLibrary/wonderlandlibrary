package my.NewSnake.Tank;

import my.NewSnake.Tank.command.CommandManager;
import my.NewSnake.Tank.friend.FriendManager;
import my.NewSnake.Tank.gui.click.ClickGui;
import my.NewSnake.Tank.me.tireman.hexa.alts.AltManager;
import my.NewSnake.Tank.module.ModuleManager;
import my.NewSnake.Tank.option.OptionManager;
import my.NewSnake.utils.ClientUtils;
import my.NewSnake.utils.FontManager;
import my.NewSnake.utils.Render2D;
import net.minecraft.dispenser.zaadfcx;

public class Snake {
   public static FontManager fontManager;
   public static Snake instance;
   public AltManager altManager;
   public static final Render2D RENDER2D = new Render2D();

   public static void start() {
      ClientUtils.loadClientFont();
      ModuleManager.start();
      CommandManager.start();
      OptionManager.start();
      FriendManager.start();
      ClickGui.start();
      zaadfcx.isAllowedToRun();
      fontManager = new FontManager();
   }

   public static FontManager getFontManager() {
      return fontManager;
   }
}
