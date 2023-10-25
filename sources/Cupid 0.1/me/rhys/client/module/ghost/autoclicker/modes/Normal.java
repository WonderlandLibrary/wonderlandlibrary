package me.rhys.client.module.ghost.autoclicker.modes;

import java.awt.Robot;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMoveEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.base.util.MathUtil;
import me.rhys.base.util.Timer;
import me.rhys.client.module.ghost.autoclicker.AutoClicker;
import net.minecraft.client.entity.EntityPlayerSP;
import org.lwjgl.input.Mouse;

public class Normal extends ModuleMode<AutoClicker> {
  @Name("CPS")
  @Clamp(min = 1.0D, max = 25.0D)
  public double speed = 12.0D;
  
  public Robot bot;
  
  private Timer timer = new Timer();
  
  public Normal(String name, AutoClicker parent) {
    super(name, (Module)parent);
  }
  
  @EventTarget
  public void playerMove(PlayerMoveEvent event) {
    EntityPlayerSP player = event.getPlayer();
    if (player == null)
      return; 
    try {
      this.bot = new Robot();
    } catch (Exception e) {
      e.printStackTrace();
    } 
    if (Mouse.isButtonDown(0)) {
      this.mc.leftClickCounter = 0;
      if (this.mc.currentScreen == null && 
        this.timer.hasReached(1000.0D / MathUtil.randDouble(this.speed - 3.0D, this.speed + 3.0D))) {
        try {
          this.bot.mouseRelease(16);
          this.bot.mousePress(16);
        } catch (Exception exception) {}
        this.timer.reset();
      } 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\ghost\autoclicker\modes\Normal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */