package net.minecraft.realms;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;

public class RealmsSliderButton extends RealmsButton {
  public float value;
  
  public boolean sliding;
  
  private final float minValue;
  
  private final float maxValue;
  
  private int steps;
  
  public RealmsSliderButton(int p_i1056_1_, int p_i1056_2_, int p_i1056_3_, int p_i1056_4_, int p_i1056_5_, int p_i1056_6_) {
    this(p_i1056_1_, p_i1056_2_, p_i1056_3_, p_i1056_4_, p_i1056_6_, 0, 1.0F, p_i1056_5_);
  }
  
  public RealmsSliderButton(int p_i1057_1_, int p_i1057_2_, int p_i1057_3_, int p_i1057_4_, int p_i1057_5_, int p_i1057_6_, float p_i1057_7_, float p_i1057_8_) {
    super(p_i1057_1_, p_i1057_2_, p_i1057_3_, p_i1057_4_, 20, "");
    this.value = 1.0F;
    this.minValue = p_i1057_7_;
    this.maxValue = p_i1057_8_;
    this.value = toPct(p_i1057_6_);
    (getProxy()).displayString = getMessage();
  }
  
  public String getMessage() {
    return "";
  }
  
  public float toPct(float p_toPct_1_) {
    return MathHelper.clamp_float((clamp(p_toPct_1_) - this.minValue) / (this.maxValue - this.minValue), 0.0F, 1.0F);
  }
  
  public float toValue(float p_toValue_1_) {
    return clamp(this.minValue + (this.maxValue - this.minValue) * MathHelper.clamp_float(p_toValue_1_, 0.0F, 1.0F));
  }
  
  public float clamp(float p_clamp_1_) {
    p_clamp_1_ = clampSteps(p_clamp_1_);
    return MathHelper.clamp_float(p_clamp_1_, this.minValue, this.maxValue);
  }
  
  protected float clampSteps(float p_clampSteps_1_) {
    if (this.steps > 0)
      p_clampSteps_1_ = (this.steps * Math.round(p_clampSteps_1_ / this.steps)); 
    return p_clampSteps_1_;
  }
  
  public int getYImage(boolean p_getYImage_1_) {
    return 0;
  }
  
  public void renderBg(int p_renderBg_1_, int p_renderBg_2_) {
    if ((getProxy()).visible) {
      if (this.sliding) {
        this.value = (p_renderBg_1_ - (getProxy()).xPosition + 4) / (getProxy().getButtonWidth() - 8);
        this.value = MathHelper.clamp_float(this.value, 0.0F, 1.0F);
        float f = toValue(this.value);
        clicked(f);
        this.value = toPct(f);
        (getProxy()).displayString = getMessage();
      } 
      Minecraft.getMinecraft().getTextureManager().bindTexture(WIDGETS_LOCATION);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      blit((getProxy()).xPosition + (int)(this.value * (getProxy().getButtonWidth() - 8)), (getProxy()).yPosition, 0, 66, 4, 20);
      blit((getProxy()).xPosition + (int)(this.value * (getProxy().getButtonWidth() - 8)) + 4, (getProxy()).yPosition, 196, 66, 4, 20);
    } 
  }
  
  public void clicked(int p_clicked_1_, int p_clicked_2_) {
    this.value = (p_clicked_1_ - (getProxy()).xPosition + 4) / (getProxy().getButtonWidth() - 8);
    this.value = MathHelper.clamp_float(this.value, 0.0F, 1.0F);
    clicked(toValue(this.value));
    (getProxy()).displayString = getMessage();
    this.sliding = true;
  }
  
  public void clicked(float p_clicked_1_) {}
  
  public void released(int p_released_1_, int p_released_2_) {
    this.sliding = false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\realms\RealmsSliderButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */