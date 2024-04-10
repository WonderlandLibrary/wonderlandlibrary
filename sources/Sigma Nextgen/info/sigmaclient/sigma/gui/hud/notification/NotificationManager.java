package info.sigmaclient.sigma.gui.hud.notification;

import info.sigmaclient.sigma.modules.gui.Shader;
import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import info.sigmaclient.sigma.utils.render.rendermanagers.ScaledResolution;
import info.sigmaclient.sigma.gui.font.JelloFontUtil;
import info.sigmaclient.sigma.utils.font.FontUtil;
import info.sigmaclient.sigma.utils.music.TextureImage;
import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import static info.sigmaclient.sigma.gui.clickgui.JelloClickGui.牰䩜躚㢸錌ꈍ;
import static info.sigmaclient.sigma.sigma5.utils.SigmaRenderUtils.퉧핇樽웨䈔属;
import static info.sigmaclient.sigma.modules.Module.mc;
import static net.minecraft.client.gui.AbstractGui.drawModalRectWithCustomSizedTexture;
import static net.minecraft.util.math.MathHelper.clamp;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

public class NotificationManager {
	static private int 塱揩㢸樽竁 = 200;
	private int 埙玑蚳葫褕 = 10;
	private int 䡸㐖퉧붛韤 = 340 / 2;
	private int 쇽뫤쿨曞值 = 64 / 2;
	private int 睬䬾ใ蓳唟 = 10;
	private int 鶲핇竬躚㐖 = 10;
	public CopyOnWriteArrayList<Notification> notifications = new CopyOnWriteArrayList<>();
	private static int 曞묙蚳骰䬾;
	public void onTick(){
		final Iterator<Notification> iterator = this.notifications.iterator();
		while (iterator.hasNext()) {
			final Notification noti = iterator.next();
			long ms = noti.getTimer().getTime();
			float m = 200;
			boolean b = false;
			if(ms <= m){
				b = false;
			}
			long dd = noti.getTime() - noti.getTimer().getTime();
			if(dd <= m){
				b = true;
			}
			noti.alpha.interpolate(!b ? 10 : 0, 5);
			if (noti.欫Ꮺ婯挐쇽.㱙㕠郝䡸ꦱ펊() <= noti.getTime()) {
				continue;
			}
			this.notifications.remove(noti);
		}
	}
	public static float 콵值敤䴂卒姮(float n, final float n2, final float n3, final float n4) {
		return n3 * (n /= n4) * n + n2;
	}
	public float Ꮤ殢㹔콵㨳(final Notification 콗뎫鷏Ꮺ놣) {
		final float n = (float)Math.min(콗뎫鷏Ꮺ놣.欫Ꮺ婯挐쇽.㱙㕠郝䡸ꦱ펊(), 콗뎫鷏Ꮺ놣.getTime());
		if (n < this.塱揩㢸樽竁 * 1.4f) {
			return 牰䩜躚㢸錌ꈍ(n / (this.塱揩㢸樽竁 * 1.4f), 0.0f, 1.0f, 1.0f);
		}
		if (n <= 콗뎫鷏Ꮺ놣.getTime() - (float)this.塱揩㢸樽竁) {
			return 1.0f;
		}
		return 콵值敤䴂卒姮((콗뎫鷏Ꮺ놣.getTime() - n) / this.塱揩㢸樽竁, 0.0f, 1.0f, 1.0f);
	}

	public float ศใ䡸뵯蓳(final int n) {
		float n2 = 0.0f;
		for (int i = 0; i < n; ++i) {
			n2 += this.Ꮤ殢㹔콵㨳(this.notifications.get(i));
		}
		return n2 / n;
	}
	public void onRender(){
		final int n = 6;
		鶲핇竬躚㐖 = 5;
		睬䬾ใ蓳唟 = 5;
		埙玑蚳葫褕 = 5;
		塱揩㢸樽竁 = SigmaNG.gameMode == SigmaNG.GAME_MODE.SIGMA ? 200 : 50;
		쇽뫤쿨曞值 = SigmaNG.gameMode == SigmaNG.GAME_MODE.SIGMA ? 32 : 32;
		䡸㐖퉧붛韤 = SigmaNG.gameMode == SigmaNG.GAME_MODE.SIGMA ? 340 / 2 : 120;
		ScaledResolution sr = new ScaledResolution(mc);
		曞묙蚳骰䬾 = Math.max(Math.round((float)(n - Minecraft.debugFPS / 10)), 1);
		if(SigmaNG.gameMode == SigmaNG.GAME_MODE.SIGMA){
			for (int i = 0; i < notifications.size(); ++i) {
				final Notification 콗뎫鷏Ꮺ놣 = notifications.get(i);
				final float 殢㹔콵㨳 = Ꮤ殢㹔콵㨳(콗뎫鷏Ꮺ놣);
				final int n2 = sr.getScaledWidth() - 埙玑蚳葫褕 - (int)(䡸㐖퉧붛韤 * 殢㹔콵㨳 * 殢㹔콵㨳);
				final int n3 = sr.getScaledHeight() - 쇽뫤쿨曞值 - 睬䬾ใ蓳唟 - i * (int)(쇽뫤쿨曞值 * ศใ䡸뵯蓳(i) + 鶲핇竬躚㐖 * ศใ䡸뵯蓳(i));
				final float min = Math.min(1.0f, 殢㹔콵㨳);
				final int rgb = new Color(0.14f, 0.14f, 0.14f, min * 0.93f).getRGB();
				final int rgb2 = new Color(0.0f, 0.0f, 0.0f, Math.min(殢㹔콵㨳 * 0.075f, 1.0f)).getRGB();
				final int rgb3 = new Color(1.0f, 1.0f, 1.0f, min).getRGB();
				RenderUtils.sigma_drawShadow((float)n2, (float)n3, (float)䡸㐖퉧붛韤, (float)쇽뫤쿨曞值, 10.0f, min);
				RenderUtils.drawRect((float)n2, (float)n3, (float)(n2 + 䡸㐖퉧붛韤), (float)(n3 + 쇽뫤쿨曞值), rgb);
				RenderUtils.drawRect((float)n2, (float)n3, (float)(n2 + 䡸㐖퉧붛韤), (float)(n3 + 1), rgb2);
				RenderUtils.drawRect((float)n2, (float)(n3 + 쇽뫤쿨曞值 - 1), (float)(n2 + 䡸㐖퉧붛韤), (float)(n3 + 쇽뫤쿨曞值), rgb2);
				RenderUtils.drawRect((float)n2, (float)(n3 + 1), (float)(n2 + 1), (float)(n3 + 쇽뫤쿨曞值 - 1), rgb2);
				RenderUtils.drawRect((float)(n2 + 䡸㐖퉧붛韤 - 1), (float)(n3 + 1), (float)(n2 + 䡸㐖퉧붛韤), (float)(n3 + 쇽뫤쿨曞值 - 1), rgb2);

				GlStateManager.color(1, 1, 1, min);
				Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("sigma/warning.png"));
				float ww = (float)(쇽뫤쿨曞值 - 鶲핇竬躚㐖);
				GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
				GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
				drawModalRectWithCustomSizedTexture((float)(n2 + 鶲핇竬躚㐖 / 2) + ww / 4f, (float)(n3 + 鶲핇竬躚㐖 / 2) + ww / 4f, 0, 0, 29/2f, 29/2f, 29/2f, 29/2f);
//			퉧핇樽웨䈔属(n2, n3, n2 + 䡸㐖퉧붛韤 - 鶲핇竬躚㐖, n3 + 쇽뫤쿨曞值);
				JelloFontUtil.jelloFont20.drawString(콗뎫鷏Ꮺ놣.getText(), (float)(n2 + 쇽뫤쿨曞值 + 鶲핇竬躚㐖 - 2), (float)(n3 + 鶲핇竬躚㐖 + 2), rgb3);
				JelloFontUtil.jelloFont14.drawString(콗뎫鷏Ꮺ놣.getSecondText(), (float)(n2 + 쇽뫤쿨曞值 + 鶲핇竬躚㐖 - 2), (float)(n3 + 鶲핇竬躚㐖 + 2 + JelloFontUtil.jelloFont14.getHeight() + 8), rgb3);
//			롤婯鷏붛浣弻();
				if(콗뎫鷏Ꮺ놣.ti != null)
					콗뎫鷏Ꮺ놣.ti.rectTextureMasked((float)(n2 + 鶲핇竬躚㐖 / 2), (float)(n3 + 鶲핇竬躚㐖 / 2), (float)(쇽뫤쿨曞值 - 鶲핇竬躚㐖), (float)(쇽뫤쿨曞值 - 鶲핇竬躚㐖), 0, 0);
			}
		}else{
			for (int i = 0; i < notifications.size(); ++i) {
				final Notification noti = notifications.get(i);
				float width = FontUtil.sfuiFont18.getStringWidth(noti.getSecondText()) + 35;
				float height = 29;
				double scale = noti.alpha.getValue() / 10f;
				scale *= 1.2f;
				if(scale >= 1.1){
					double a = scale - 1.1; // 0 - 0.05
					scale = 1.1 - a; // 1.05 - 1.0
				} // jello bounce
				final float 殢㹔콵㨳 = (float) scale;
				final float n2 = sr.getScaledWidth() - (int)((10 + width) * 殢㹔콵㨳);
				final float n3 = sr.getScaledHeight() - height - 10 - i * (int)(height * ศใ䡸뵯蓳(i) + 10 * ศใ䡸뵯蓳(i));

				Color icolor3 = new Color(230, 230, 230, (int) clamp((殢㹔콵㨳) * 255, 0, 255));
				Color icolor2 = new Color(200, 200, 200, (int) clamp((殢㹔콵㨳) * 255, 0, 255));


				float x1 = n2;
				float y1 = n3;
				Shader.drawRoundRectWithGlowing(x1, y1, x1 + width,y1 + height, new Color(0.117647059f, 0.117647059f, 0.117647059f, (float) clamp((殢㹔콵㨳), 0, 1)));

				FontUtil.sfuiFontBold18.drawString(noti.getText(), x1 + 25, y1 + 7, icolor3.getRGB());
				FontUtil.sfuiFont16.drawString(noti.getSecondText(), x1 + 25, (int) y1 + 17, icolor2.getRGB());

				FontUtil.icon24.drawString("J", x1 + 5, y1 + 7, icolor2.getRGB());
			}
		}
	}
	void add(Notification notification){
		if(SigmaNG.gameMode == SigmaNG.GAME_MODE.SIGMA) {
			for (final Notification 콗뎫鷏Ꮺ놣2 : notifications) {
				if (!콗뎫鷏Ꮺ놣2.equals(notification)) {
					continue;
				}
				콗뎫鷏Ꮺ놣2.欫Ꮺ婯挐쇽.鞞콗뎫䖼㼜挐(Math.min(콗뎫鷏Ꮺ놣2.欫Ꮺ婯挐쇽.㱙㕠郝䡸ꦱ펊(), 塱揩㢸樽竁 + 1));
				콗뎫鷏Ꮺ놣2.setSecondText(notification.getSecondText());
				final Notification 콗뎫鷏Ꮺ놣3 = 콗뎫鷏Ꮺ놣2;
				++콗뎫鷏Ꮺ놣3.index;
				콗뎫鷏Ꮺ놣2.ti = notification.ti;
				return;
			}
			notifications.add(notification);
		}else {
			if(notifications.size() > 6){
				notifications.remove(0);
			}
			notifications.add(notification);
		}
	}
	public static void notify(String text, String secondText, int time){
		SigmaNG.getSigmaNG().notificationManager.add(new Notification(text, secondText, time));
	}
	
	public static void notify(String text, String secondText, int time, TextureImage t){
		SigmaNG.getSigmaNG().notificationManager.add(new Notification(text, secondText, time, t));
	}

	public static void notify(String text, String secondText){
		SigmaNG.getSigmaNG().notificationManager.add(new Notification(text, secondText, 4000));
	}
}
