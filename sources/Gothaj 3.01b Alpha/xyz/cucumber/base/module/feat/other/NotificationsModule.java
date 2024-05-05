package xyz.cucumber.base.module.feat.other;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.util.ResourceLocation;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.ext.EventBloom;
import xyz.cucumber.base.events.ext.EventRenderGui;
import xyz.cucumber.base.module.ArrayPriority;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.settings.BooleanSettings;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.Fonts;


@ModuleInfo(category = Category.OTHER, description = "Displays otifications on screen", name = "Notifications", priority = ArrayPriority.LOW)
public class NotificationsModule extends Mod {
	
	public ArrayList<Notification> notifications = new ArrayList();
	@EventListener
	public void onBloom(EventBloom e) {
		
		
	}
	@EventListener
	public void onRenderGui(EventRenderGui e) {
		
		Iterator<Notification> itr = notifications.iterator();
		int i = 0;
		while(itr.hasNext()) {
			Notification n = itr.next();

			boolean r = n.update(e.getScaledResolution().getScaledWidth(),e.getScaledResolution().getScaledHeight()-25, i);
			i++;
			if(r) {
				itr.remove();
				i--;
			}
		}
		
	}
	
	public static class Notification {
		
		private String name;
		private String description;
		private Type type;
		
		private long maxTime = 3500;
		
		private ResourceLocation spotifyIcon = new ResourceLocation("client/images/notifications/spotify.png"),
				enabledIcon = new ResourceLocation("client/images/notifications/checked.png"),
				disabledIcon = new ResourceLocation("client/images/notifications/cancel.png"),
				warningIcon = new ResourceLocation("client/images/notifications/warning.png");
		
		private PositionUtils position;
		
		public int time = -1;
		
		public double animation;

		public Notification(String name, String description, Type type, PositionUtils position) {
			this.name = name;
			this.description = description;
			this.type = type;
			this.position = position;
			time = (int) (System.nanoTime()/1000000+maxTime);
		}
		
		public boolean update(double x, double y, int offset) {
			if(time == -1) {
				time = (int) (System.nanoTime()/1000000+maxTime);
			}
			if(System.nanoTime()/1000000 >= time) {
				return true;
			}
			double t = time -System.nanoTime()/1000000;
			
			double w = Fonts.getFont("rb-r").getWidth(description)+40+5;
			double size = 0;
			
			if(t >  maxTime-500)
				size = w-Math.pow(w, 1F-(maxTime-t)/500f);
			else if(t <= maxTime-500 && t >= 500) size = w;
			else
				size = Math.pow(w, (t)/500f);
			
			this.position.setX(x-size);
			
			animation = (animation * 9 + 25*offset)/10;
			
			this.position.setY(y- animation);
			this.position.setWidth(w);
			this.position.setHeight(20);
			
			RenderUtils.drawRoundedRect(position.getX(), position.getY(), position.getX2(),position.getY2(), 0x90000000,1);
			int col = -1;
			ResourceLocation icon = null;
			if(type == Type.ENABLED) {
				
				icon = enabledIcon;
				col = 0x4534eb98;
			}
			else if(type == Type.DISABLED) {
				icon = disabledIcon;
				col = 0x45eb3449;
			}
			else if(type == Type.ALERT) {
				icon = warningIcon;
				col = 0x45ebba34;
			}
			else if(type == Type.SPOTIFY) {
				icon = spotifyIcon;
				col = 0x4534eb7a;
			}
			RenderUtils.drawRoundedRect(position.getX(), position.getY(), position.getX()+20,position.getY2(), col,1);
			
			RenderUtils.drawImage(position.getX()+3, position.getY()+2.5, 15, 15, icon, -1);
			
			Fonts.getFont("rb-m").drawString(name,position.getX()+23, position.getY()+5, -1);
			
			Fonts.getFont("rb-r").drawString(description + " ("+((double)(int)(t/100))/10+"s)",position.getX()+23, position.getY()+5+8, 0xffaaaaaa);
			return false;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public Type getType() {
			return type;
		}

		public void setType(Type type) {
			this.type = type;
		}

		public long getMaxTime() {
			return maxTime;
		}

		public void setMaxTime(long maxTime) {
			this.maxTime = maxTime;
		}

		public ResourceLocation getSpotifyIcon() {
			return spotifyIcon;
		}

		public void setSpotifyIcon(ResourceLocation spotifyIcon) {
			this.spotifyIcon = spotifyIcon;
		}

		public ResourceLocation getEnabledIcon() {
			return enabledIcon;
		}

		public void setEnabledIcon(ResourceLocation enabledIcon) {
			this.enabledIcon = enabledIcon;
		}

		public ResourceLocation getDisabledIcon() {
			return disabledIcon;
		}

		public void setDisabledIcon(ResourceLocation disabledIcon) {
			this.disabledIcon = disabledIcon;
		}

		public ResourceLocation getWarningIcon() {
			return warningIcon;
		}

		public void setWarningIcon(ResourceLocation warningIcon) {
			this.warningIcon = warningIcon;
		}

		public PositionUtils getPosition() {
			return position;
		}

		public void setPosition(PositionUtils position) {
			this.position = position;
		}

		public int getTime() {
			return time;
		}

		public void setTime(int time) {
			this.time = time;
		}
		
		
	}
	
	public enum Type {
		ENABLED,
		DISABLED,
		SPOTIFY,
		ALERT
	}
	
}
