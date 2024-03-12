package xyz.cucumber.base.interf.altmanager.ut;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.litarvan.openauth.microsoft.model.response.MinecraftProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.Session;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.interf.altmanager.AltManager;
import xyz.cucumber.base.interf.altmanager.impl.AltManagerButton;
import xyz.cucumber.base.interf.altmanager.impl.AltManagerClickable;
import xyz.cucumber.base.interf.altmanager.impl.AltManagerClose;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.StringUtils;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.BlurUtils;
import xyz.cucumber.base.utils.render.ColorUtils;
import xyz.cucumber.base.utils.render.Fonts;
import xyz.cucumber.base.utils.render.StencilUtils;

public class AltManagerPanel {
	
	public GuiTextField username, password;
	
	private PositionUtils position;
	
	private List<AltManagerButton> buttons = new ArrayList();
	
	public boolean open;
	
	private double animation;
	
	private double fieldAnimation1,fieldAnimation2;
	
	
	
	AltManager altManager;
	public AltManagerPanel(AltManager altManager) {
		this.altManager = altManager;
		open = false;
		this.position = new PositionUtils(altManager.width/2-75, altManager.height,150, 160,1);
		
		username = new GuiTextField(0, Minecraft.getMinecraft().fontRendererObj, (int) (position.getX()+2), (int) (position.getY()+position.getHeight()/2-5-25), 146, 25);
		password = new GuiTextField(0, Minecraft.getMinecraft().fontRendererObj, (int) (position.getX()+2), (int) (position.getY()+position.getHeight()/2+5), 146, 25);
		
		double centerLX = position.getX()+position.getWidth()/2;
		double centerLY = position.getY()+position.getHeight()/2;
		buttons.clear();
		buttons.add(new AltManagerClickable(0, "Add", position.getX()+position.getWidth()/2-72.5, position.getY()+position.getHeight()/2-17.5, 70, 15));
		buttons.add(new AltManagerClickable(1, "Login", position.getX()+position.getWidth()/2+2.5, position.getY()+position.getHeight()/2-17.5, 70, 15));
		buttons.add(new AltManagerClose(2, position.getX()+position.getWidth()/2-17, position.getY()+2, 15, 15));
	}
	public void draw(int mouseX, int mouseY) {
		updatePositions();

		RenderUtils.drawRoundedRect(position.getX(),position.getY(), position.getX2(), position.getY2(), 0x20aaaaaa, 3);
		RenderUtils.drawOutlinedRoundedRect(position.getX(),position.getY(), position.getX2(), position.getY2(), 0x50777777, 3, 0.5);
		String[] s = "Add account".split("");
        double w = 0;
        
        for(String t : s) {
        	Fonts.getFont("rb-b-h").drawStringWithShadow(t, position.getX()+position.getWidth()/2-Fonts.getFont("rb-b-h").getWidth("Add account")/2+w,position.getY()+7,  ColorUtils.mix(0xff674cd4, 0xff3d6cb8, Math.sin(Math.toRadians(System.nanoTime()/1000000 + w*10)/3)+1, 2),0x45000000);
        	w += Fonts.getFont("rb-b-h").getWidth(t);
        }
		renderTextField(username);
		renderTextField(password);
		
		for(AltManagerButton b : buttons) {
			b.draw(mouseX, mouseY);
		}
	}
	public void key(char typedChar, int keyCode) {
		if (typedChar == '\t')
        {
            if (!this.username.isFocused()) {
                this.username.setFocused(true);
            }
            if (!this.password.isFocused()) {
            	this.password.setFocused(true);
            }
        }
		
		this.username.textboxKeyTyped(typedChar, keyCode);
        this.password.textboxKeyTyped(typedChar, keyCode);
	}
	public void click(int mouseX, int mouseY, int b) {
		if(open) {
			if(!position.isInside(mouseX, mouseY)) {
				open = false;
			}
		}
		for(AltManagerButton butt : getButtons()) {
			if(butt.getPosition().isInside(mouseX, mouseY)) {
				if(b == 0) {
					switch(butt.getId()) {
					case 1:
						if(username.getText().equals("")) {
							return;
						}
						if(password.getText().equals("")) {
							Minecraft.getMinecraft().session = new Session(username.getText(), "0", "0", "mojang");
							return;
						}
						Session session = loginToAccount(username.getText(), password.getText());
						if(session == null) return;
						Minecraft.getMinecraft().session = session;
						break;
					case 0:
						if(username.getText().equals("")) {
							return;
						}
						if(password.getText().equals("")) {
							this.altManager.sessions.add(new AltManagerSession(altManager, new Session(username.getText(), "0", "0", "mojang")));
							return;
						}
						session = loginToAccount(username.getText(), password.getText());
						if(session == null) return;
						this.altManager.sessions.add(new AltManagerSession(altManager, session));
						break;
					case 2:
						open = false;
					}
				}
			}
		}
		this.username.mouseClicked(mouseX, mouseY, b);
	    this.password.mouseClicked(mouseX, mouseY, b);
	}
	public Session loginToAccount(String email, String password) {
        try {
            MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
            MicrosoftAuthResult result = authenticator.loginWithCredentials(email, password);
            MinecraftProfile profile = result.getProfile();
            Session session = new Session(profile.getName(), profile.getId(), result.getAccessToken(), "mojang");
            return session;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	
	public void updatePositions() {
		double centerLX = position.getX()+position.getWidth()/2;
		double centerLY = position.getY()+position.getHeight()/2;
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		
		
		if(!open) {
		    position.setY((position.getY()* 17+(sr.getScaledHeight()))/18);
		}else {
			position.setY((position.getY()* 17+(sr.getScaledHeight()/2-75))/18);
		}
		for(AltManagerButton b : buttons) {
			switch(b.getId()) {
			case 0:
				b.getPosition().setX(position.getX()+position.getWidth()/2-72.5);
				b.getPosition().setY(position.getY()+position.getHeight()-17.5);
				break;
			case 1:
				b.getPosition().setX(position.getX()+position.getWidth()/2+2.5);
				b.getPosition().setY(position.getY()+position.getHeight()-17.5);
				break;
			case 2:
				b.getPosition().setX(position.getX()+position.getWidth()-17);
				b.getPosition().setY(position.getY()+2);
				break;
			}
		}
		
		username.xPosition = (int) (position.getX()+2);
		username.yPosition = (int) (position.getY()+position.getHeight()/2-5-22);
		
		password.xPosition = (int) (position.getX()+2);
		password.yPosition = (int) (position.getY()+position.getHeight()/2+5);
	}
	
	private double spacing(GuiTextField field) {
		String[] text = field.getText().split("");
		
		double d = 0;
		int i = 0;
		for(String t : text) {
			if(field.getCursorPosition() == i) {
				return d;
			}
			i++;
			d+=(field == password ? Fonts.getFont("rb-r").getWidth("*") : Fonts.getFont("rb-r").getWidth(t));
		}
		return d;
	}
	
	public void renderTextField(GuiTextField field) {
		RenderUtils.drawRoundedRect(field.xPosition, field.yPosition, field.xPosition+field.width, field.yPosition+field.height, 0x35aaaaaa, 3);
		if(field.isFocused()) {
			
			if(field == username) {
				fieldAnimation2 = (fieldAnimation2 * 10 + 10)/11;
				RenderUtils.drawOutlinedRoundedRect(field.xPosition, field.yPosition, field.xPosition+field.width, field.yPosition+field.height, ColorUtils.getAlphaColor(ColorUtils.mix(0x35aaaaaa, 0xffffffff, fieldAnimation2, 10), 10), 3, 0.1);
			}else {
				fieldAnimation1 = (fieldAnimation1 * 10 + 10)/11;
				RenderUtils.drawOutlinedRoundedRect(field.xPosition, field.yPosition, field.xPosition+field.width, field.yPosition+field.height, ColorUtils.getAlphaColor(ColorUtils.mix(0x35aaaaaa, 0xffffffff, fieldAnimation1, 10), 10), 3,0.1);
			}
			Fonts.getFont("rb-r").drawString("|", field.xPosition+4+spacing(field), field.yPosition+11, -1);
				
		}else {
			if(field == username) {
				fieldAnimation2 = (fieldAnimation2 * 6)/7;
				RenderUtils.drawOutlinedRoundedRect(field.xPosition, field.yPosition, field.xPosition+field.width, field.yPosition+field.height, ColorUtils.getAlphaColor(ColorUtils.mix(0x35aaaaaa, 0xffffffff, fieldAnimation2, 10), 10),3,0.1);
			}else {
				fieldAnimation1 = (fieldAnimation1 * 6)/7;
				RenderUtils.drawOutlinedRoundedRect(field.xPosition, field.yPosition, field.xPosition+field.width, field.yPosition+field.height, ColorUtils.getAlphaColor(ColorUtils.mix(0x35aaaaaa, 0xffffffff, fieldAnimation1, 10), 10), 3,0.1);
			}
		}
		if(field.getText().equals("")) {
			if(field == username) {
				Fonts.getFont("rb-r").drawString("Name / E-mail", field.xPosition+4,  field.yPosition+11, 0xffaaaaaa);
			}else {
				Fonts.getFont("rb-r").drawString("Password", field.xPosition+4,  field.yPosition+11, 0xffaaaaaa);
			}
		}else {
			int color = 0xffaaaaaa;
			if(field.isFocused()) {
				color = -1;
			}
			if(field == password) {
				String ps = "";
				for(String s : field.getText().split("")) {
					ps += "*";
				}
				Fonts.getFont("rb-r").drawString(ps, field.xPosition+4,  field.yPosition+12.5, color);
				return;
			}
			Fonts.getFont("rb-r").drawString(field.getText(), field.xPosition+4,  field.yPosition+11, color);
		}
	}
	public List<AltManagerButton> getButtons() {
		return buttons;
	}
	public void setButtons(List<AltManagerButton> buttons) {
		this.buttons = buttons;
	}
	
	
}