package me.rhys.client.ui.alt.popup;

import me.rhys.base.ui.element.Element;
import me.rhys.base.ui.element.button.Button;
import me.rhys.base.ui.element.input.TextInputField;
import me.rhys.base.ui.element.label.Label;
import me.rhys.base.ui.popup.Popup;
import me.rhys.base.util.LoginThread;
import me.rhys.base.util.render.ColorUtil;
import me.rhys.base.util.render.FontUtil;
import me.rhys.base.util.render.RenderUtil;
import me.rhys.base.util.vec.Vec2f;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class DirectLoginPopup extends Popup {
  private TextInputField emailField;
  
  private TextInputField passwordField;
  
  private Button closeBtn;
  
  private Button loginBtn;
  
  public DirectLoginPopup(int width, int height) {
    super("Direct Login", width, height);
  }
  
  public void onShow() {
    super.onShow();
    addToBody((Element)new Label("Email", new Vec2f(5.0F, 5.0F)));
    addToBody((Element)(this.emailField = new TextInputField(new Vec2f(5.0F, FontUtil.getFontHeight() + 10.0F), this.width - 11, 15)));
    addToBody((Element)new Label("Password", new Vec2f(5.0F, FontUtil.getFontHeight() * 2.0F + 20.0F)));
    addToBody((Element)(this.passwordField = new TextInputField(new Vec2f(5.0F, FontUtil.getFontHeight() * 3.0F + 25.0F), this.width - 11, 15)));
    this.passwordField.setIsPassword(true);
    addToBody((Element)(this.closeBtn = new Button("Close", new Vec2f(5.0F, FontUtil.getFontHeight() * 3.0F + 46.0F), (int)((this.width - 15) / 2.0F), 17)));
    this.closeBtn.background = ColorUtil.darken(BACKGROUND_COLOR, 20).getRGB();
    addToBody((Element)(this.loginBtn = new Button("Login", new Vec2f((this.width - 15) / 2.0F + 10.0F, FontUtil.getFontHeight() * 3.0F + 46.0F), (int)((this.width - 15) / 2.0F), 17)));
    this.loginBtn.background = ColorUtil.darken(BACKGROUND_COLOR, 20).getRGB();
  }
  
  public void clickMouse(Vec2f pos, int button) {
    this.emailField.clickMouse(pos, button);
    this.passwordField.clickMouse(pos, button);
    if (this.loginBtn.isHovered(pos)) {
      LoginThread loginThread = new LoginThread(this.emailField.getText(), this.passwordField.getText());
      loginThread.playSound = true;
      this.loginBtn.playSound();
      loginThread.start();
      getScreen().displayPopup(null);
    } else if (this.closeBtn.isHovered(pos)) {
      getScreen().displayPopup(null);
      this.closeBtn.playSound();
    } 
  }
  
  public void onDraw() {
    GlStateManager.pushMatrix();
    GL11.glLineWidth(2.5F);
    RenderUtil.drawOutlineRect(this.closeBtn.pos, this.closeBtn.pos.clone().add(this.closeBtn.getWidth(), this.closeBtn.getHeight()), SHADOW_COLOR);
    RenderUtil.drawOutlineRect(this.loginBtn.pos, this.loginBtn.pos.clone().add(this.loginBtn.getWidth(), this.loginBtn.getHeight()), SHADOW_COLOR);
    GL11.glLineWidth(1.0F);
    GlStateManager.popMatrix();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\clien\\ui\alt\popup\DirectLoginPopup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */