package info.sigmaclient.sigma.gui.hud.notification;

import info.sigmaclient.sigma.utils.render.anims.PartialTicksAnim;
import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import info.sigmaclient.sigma.utils.render.rendermanagers.ScaledResolution;
import info.sigmaclient.sigma.sigma5.utils.NotiTimer;
import info.sigmaclient.sigma.modules.gui.Shader;
import info.sigmaclient.sigma.utils.TimerUtil;
import info.sigmaclient.sigma.gui.font.JelloFontUtil;
import info.sigmaclient.sigma.utils.music.TextureImage;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

import static info.sigmaclient.sigma.modules.Module.mc;
import static info.sigmaclient.sigma.utils.render.RenderUtils.drawRect;
import static net.minecraft.client.gui.AbstractGui.drawModalRectWithCustomSizedTexture;

public class Notification {

	PartialTicksAnim alpha = new PartialTicksAnim(0);
	private String text;
	private String secondText;
	private int time;
	private TimerUtil timer = new TimerUtil();
	private boolean isDone;
	private boolean peaked;
	public int index = 0;
	public NotiTimer 欫Ꮺ婯挐쇽;
	
	private float progress;
	private float lastProgress;
	
	public TextureImage ti;
	
	public Notification(String text, String secondText, int time){
		this.text = text;
		this.secondText = secondText;
		this.time = time;
		timer.reset();
		isDone = false;
		progress = 0;
		lastProgress = 0;
		peaked = false;
		this.欫Ꮺ婯挐쇽 = new NotiTimer();
		this.欫Ꮺ婯挐쇽.轐ᢻ佉뫤甐䖼();
	}
	
	public Notification(String text, String secondText, int time, TextureImage texture){
		ti = texture;
		this.text = text;
		this.secondText = secondText;
		this.time = time;
		timer.reset();
		isDone = false;
		progress = 0;
		lastProgress = 0;
		peaked = false;
		this.欫Ꮺ婯挐쇽 = new NotiTimer();
		this.欫Ꮺ婯挐쇽.轐ᢻ佉뫤甐䖼();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSecondText() {
		return secondText;
	}

	public void setSecondText(String secondText) {
		this.secondText = secondText;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public TimerUtil getTimer() {
		return timer;
	}

	public void setTimer(TimerUtil timer) {
		this.timer = timer;
	}

	public boolean isDone() {
		return isDone;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}
	
	public void onTick(){
		lastProgress = progress;
		if(progress < 34 && !peaked){
			//progress++;
			progress += (((34)-progress)/(3))+2;
		}else if(!peaked){
			peaked = true;
		}
		
		if(progress > 0 && peaked && timer.hasTimeElapsed(this.getTime() + 500, false)){
			//progress--;
			progress -= (progress/(3))+2;
		}else if(progress <= 1 && peaked){
			timer.reset();
			isDone = true;
		}
	}
	public float smoothTrans(double current, double last){
		return (float) (current * Minecraft.getInstance().timer.renderPartialTicks + (last * (1.0f - Minecraft.getInstance().timer.renderPartialTicks)));
	}
	public void renderNotification(){
		ScaledResolution sr = new ScaledResolution(mc);
		
		float animation = this.smoothTrans(progress, lastProgress);
		
		if(!isDone){
			GlStateManager.enableBlend();
			GlStateManager.disableAlpha();
			GlStateManager.color(1, 1, 1, 1);
			Shader.addBlur(()->{
				final float y = sr.getScaledHeight() - 34 - 10f;
				drawRect(sr.getScaledWidth() - animation*5.264f + 3 + 1f + 4,
						y + 4.5,
						sr.getScaledWidth() - 179 - animation*5.264f + 179 + 3 + .5f + 179 - 4,
						y + 38 - 3 + .5f,
						-1);
			});
			final float y = sr.getScaledHeight() - 34 - 10f;
			drawRect(sr.getScaledWidth() - animation*5.264f + 3 + 1f + 4,
					y + 4.5,
					sr.getScaledWidth() - 179 - animation*5.264f + 179 + 3 + .5f + 179 - 4,
					y + 38 - 3 + .5f,
					new Color(50, 50, 50, 200).getRGB());
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();

		
		Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("sigma/notification.png"));
		drawModalRectWithCustomSizedTexture(sr.getScaledWidth() - 179 - animation*5.264f + 179 + 3 + .5f,
				y, 0, 0, 179, 40, 179, 40);

		GlStateManager.color(1, 1, 1, 1);
		Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("sigma/warning.png"));
		drawModalRectWithCustomSizedTexture(sr.getScaledWidth() - 179 - animation*5.264f + 179 + 26/2f + 3 + .5f - 1 + 3,
				27/2f + y - 1, 0, 0, 29f/2f, 29f/2f, 29f / 2f, 29/2f);
		
		if(ti != null){
			GlStateManager.color(1, 1, 1, 1);
			ti.rectTextureMasked(sr.getScaledWidth() - 179 - animation*5.264f + 179 + 26/2f - 8 + 2.5f + 3,
					27/2f - 9 + y + 2, 27, 27, 1, 0.001f);
		}
		
		JelloFontUtil.jelloFont20.drawString(text,
				sr.getScaledWidth() - 179 - animation*5.264f + 179 + 4.5f + 32.5f + 3 + 4.5f,
				sr.getScaledHeight() - 41 - 3.5f + 7.5f + 1 + 3, -1);
		JelloFontUtil.jelloFont14.drawString(secondText,
				sr.getScaledWidth() - 179 - animation*5.264f + 179 + 4.5f + 32.5f + 3 + 4.5f,
				sr.getScaledHeight() - 41 - 3.5f + 19.5f + 1.5f + 1 + 2, -1);
		}
		
	}

	public float getProgress() {
		return this.smoothTrans(progress, lastProgress);
	}

	public void setProgress(float progress) {
		this.progress = progress;
	}

	@Override
	public boolean equals(final Object o) {
		return o instanceof Notification && ((Notification)o).text.equals(this.text);
	}
	
}
