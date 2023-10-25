package me.rhys.client.ui.alt.popup;

import me.rhys.base.ui.element.Element;
import me.rhys.base.ui.element.button.Button;
import me.rhys.base.ui.popup.Popup;
import me.rhys.base.util.render.ColorUtil;
import me.rhys.base.util.render.RenderUtil;
import me.rhys.base.util.vec.Vec2f;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class AccountManagementPopup extends Popup {
  private static final int MARGIN = 5;
  
  private Button addBtn;
  
  private Button clearBtn;
  
  private Button closeBtn;
  
  public AccountManagementPopup(int width, int height) {
    super("Manage Accounts", width, height);
  }
  
  public void clickMouse(Vec2f pos, int button) {
    super.clickMouse(pos, button);
    if (button == 0)
      if (this.addBtn.isHovered(pos)) {
        getScreen().displayPopup(new AddAccountPopup(220, 115));
        this.addBtn.playSound();
      } else if (this.clearBtn.isHovered(pos)) {
        this.clearBtn.playSound();
      } else if (this.closeBtn.isHovered(pos)) {
        getScreen().displayPopup(null);
        this.closeBtn.playSound();
      }  
  }
  
  public void onShow() {
    super.onShow();
    addToBody((Element)(this.addBtn = new Button("Add", new Vec2f(5.0F, 5.0F), this.width - 10, 20)));
    this.addBtn.background = ColorUtil.darken(BACKGROUND_COLOR, 20).getRGB();
    addToBody((Element)(this.clearBtn = new Button("Clear", new Vec2f(5.0F, 30.0F), this.width - 10, 20)));
    this.clearBtn.background = ColorUtil.darken(BACKGROUND_COLOR, 20).getRGB();
    addToBody((Element)(this.closeBtn = new Button("Close", new Vec2f(5.0F, 55.0F), this.width - 10, 20)));
    this.closeBtn.background = ColorUtil.darken(BACKGROUND_COLOR, 20).getRGB();
  }
  
  public void onDraw() {
    GlStateManager.pushMatrix();
    GL11.glLineWidth(2.5F);
    RenderUtil.drawOutlineRect(this.addBtn.pos.clone().sub(0.0F, 1.0F), this.addBtn.pos.clone().add(this.addBtn.getWidth(), this.addBtn.getHeight()), SHADOW_COLOR);
    RenderUtil.drawOutlineRect(this.clearBtn.pos.clone().sub(0.0F, 1.0F), this.clearBtn.pos.clone().add(this.clearBtn.getWidth(), this.clearBtn.getHeight()), SHADOW_COLOR);
    RenderUtil.drawOutlineRect(this.closeBtn.pos.clone().sub(0.0F, 1.0F), this.closeBtn.pos.clone().add(this.closeBtn.getWidth(), this.closeBtn.getHeight()), SHADOW_COLOR);
    GL11.glLineWidth(1.0F);
    GlStateManager.popMatrix();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\clien\\ui\alt\popup\AccountManagementPopup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */