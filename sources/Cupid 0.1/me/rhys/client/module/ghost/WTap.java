package me.rhys.client.module.ghost;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.TickEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.base.util.MathUtil;
import me.rhys.base.util.Timer;
import net.minecraft.client.settings.KeyBinding;

public class WTap extends Module {
  private long nextTapDown;
  
  private long nextTapUp;
  
  Timer timer = new Timer();
  
  @Name("Change")
  @Clamp(min = 1.0D, max = 100.0D)
  public float chanceValue;
  
  public WTap(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
    this.chanceValue = 15.0F;
    this.nextTapDown = 0L;
    this.nextTapUp = 0L;
  }
  
  @EventTarget
  public void onHurtAnimation() {
    if (this.mc.thePlayer.isDead)
      return; 
    if (this.mc.theWorld == null || this.mc.currentScreen != null)
      return; 
    if (!this.mc.thePlayer.isSprinting())
      return; 
    if (this.chanceValue >= MathUtil.random.nextInt(100) && this.nextTapUp == 0L && this.nextTapDown == 0L)
      this.nextTapUp = System.currentTimeMillis() + 40L + MathUtil.random.nextInt(325); 
  }
  
  @EventTarget
  public void onRenderTick(TickEvent event) {
    if (this.mc.thePlayer == null || this.mc.theWorld == null || this.mc.currentScreen != null)
      return; 
    if (this.mc.thePlayer.isDead)
      return; 
    if (!this.mc.thePlayer.isSprinting() && this.nextTapUp > 0L) {
      this.nextTapUp = 0L;
      return;
    } 
    if (System.currentTimeMillis() - this.nextTapUp > 0L && this.nextTapUp != 0L) {
      int forwardKey = this.mc.gameSettings.keyBindForward.getKeyCode();
      KeyBinding.setKeyBindState(forwardKey, false);
      KeyBinding.onTick(forwardKey);
      this.nextTapDown = System.currentTimeMillis() + 90L + MathUtil.random.nextInt(50);
      this.nextTapUp = 0L;
    } else if (System.currentTimeMillis() - this.nextTapDown > 0L && this.nextTapDown != 0L) {
      int forwardKey = this.mc.gameSettings.keyBindForward.getKeyCode();
      KeyBinding.setKeyBindState(forwardKey, true);
      KeyBinding.onTick(forwardKey);
      this.nextTapDown = 0L;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\ghost\WTap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */