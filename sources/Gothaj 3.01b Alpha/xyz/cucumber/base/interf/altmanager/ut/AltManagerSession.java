package xyz.cucumber.base.interf.altmanager.ut;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import xyz.cucumber.base.interf.altmanager.AltManager;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.ColorUtils;
import xyz.cucumber.base.utils.render.Fonts;

public class AltManagerSession {

	private Session session;
	private AltManager manager;
	
	private PositionUtils position;
	
	public AltManagerSession(AltManager manager,Session session) {
		this.session = session;
		this.manager = manager;
		this.position = new PositionUtils(0, 0, 100, 15, 1);
	}
	public void draw(int mouseX, int mouseY) {
		RenderUtils.drawRoundedRect(this.position.getX(),this.position.getY(), this.position.getX2(), this.position.getY2(), 0x771c2657, 1.2);
		
		String[] s = session.getUsername().split("");
        double w = 0;
        
        for(String t : s) {
        	Fonts.getFont("rb-b").drawStringWithShadow(t, this.position.getX()+3+w,this.position.getY()+6,  ColorUtils.mix(0xff674cd4, 0xff3d6cb8, Math.sin(Math.toRadians(System.nanoTime()/1000000 + w*10)/3)+1, 2),0x45000000);
        	w += Fonts.getFont("rb-b").getWidth(t);
        }
        Fonts.getFont("rb-r-13").drawString((session.getToken().equals("0") ? "Cracked" : "Online"), this.position.getX()+this.position.getWidth()/2-Fonts.getFont("rb-r").getWidth((session.getToken().equals("0") ? "Cracked" : "Online"))/2, this.position.getY()+6, 0xff777777);
        
        if(this == manager.active) {
        	RenderUtils.drawOutlinedRoundedRect(this.position.getX(),this.position.getY(), this.position.getX2(), this.position.getY2(), 0xff1c2657, 1.2, 0.5);
        }
        
        if(session == Minecraft.getMinecraft().session) {
        	RenderUtils.drawImage(this.position.getX2()-9, this.position.getY()+4.5, 6, 6, new ResourceLocation("client/images/check.png"), 0xff00ff00);
        }
	}
	public void onClick(int mouseX, int mouseY, int b) {
		if(manager.active == this) {
			Minecraft.getMinecraft().session = session;
		}else manager.active = this;
			
	}
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}
	public AltManager getManager() {
		return manager;
	}
	public void setManager(AltManager manager) {
		this.manager = manager;
	}
	public PositionUtils getPosition() {
		return position;
	}
	public void setPosition(PositionUtils position) {
		this.position = position;
	}
	
	
}
