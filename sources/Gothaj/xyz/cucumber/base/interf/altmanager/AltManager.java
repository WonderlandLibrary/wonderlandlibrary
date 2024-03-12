package xyz.cucumber.base.interf.altmanager;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.file.files.AccountsFile;
import xyz.cucumber.base.interf.altmanager.impl.AltManagerButton;
import xyz.cucumber.base.interf.altmanager.impl.AltManagerClickable;
import xyz.cucumber.base.interf.altmanager.impl.AltManagerClose;
import xyz.cucumber.base.interf.altmanager.ut.AltManagerPanel;
import xyz.cucumber.base.interf.altmanager.ut.AltManagerSession;
import xyz.cucumber.base.interf.mainmenu.Menu;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.StringUtils;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.BlurUtils;
import xyz.cucumber.base.utils.render.ColorUtils;
import xyz.cucumber.base.utils.render.Fonts;

public class AltManager extends GuiScreen {
	
	private GuiScreen menu;
	
	private long startTime;
	
	public ArrayList<AltManagerButton> buttons = new ArrayList();
	
	private String[] preffix = new String[] {
			"xX", "_", "X_", "Svk", "Itz", "_X", "CZ_", "CZ", "SK_", "SK"
	};
	private String[] firstNames = new String[] {
			"Dejvo", "Jozo", "Tomas", "Fero", "Dezo", "Roman", "Anaaa", "Steel"
	};
	private String[] separator = new String[] {
			"", "_"
	};
	private String[] lastNames = new String[] {
			"Rasista", "Sista", "RIPak", "Rolex", "Shot", "Hacienda", "Calu"
	};
	private String[] suffix = new String[] {
			"CZ", "_CZ", "_SK", "SK", "__", "_", "SvK", "_SvK", "Omg"
	};
	
	public AltManager(GuiScreen menu) {
		this.menu = menu;
	}
	
	private ResourceLocation head;
	
	private AltManagerPanel panel;
	public AltManagerSession active;
	
	public ArrayList<AltManagerSession> sessions = new ArrayList();
	
	private PositionUtils position;
	private double scrollY,temp;
	
	@Override
	public void initGui() {
		
		buttons.clear();
		sessions.clear();
		position = new PositionUtils(130, 2, this.width-6-130, this.height-4, 1);
		
		((AccountsFile)Client.INSTANCE.getFileManager().getFile(AccountsFile.class)).load(this);
		startTime = System.nanoTime();
		panel = new AltManagerPanel(this);
		
		buttons.add(new AltManagerClose(0 ,55, this.height-32, 20, 20));
		buttons.add(new AltManagerClickable(1, "Add" ,15, this.height-70, 100, 15));
		buttons.add(new AltManagerClickable(2, "Remove" ,15, this.height-53, 100, 15));
		buttons.add(new AltManagerClickable(3, "Random cracked" ,15, this.height-87, 100, 15));
		buttons.add(new AltManagerClickable(4, "USERNAME:ID:TOKEN from Clipboard" ,15, this.height-104, 100, 15));
	}
	
	public double getBigger() {
		double x =0;
		double y = 0;
		for(AltManagerSession session : sessions) {
			x++;
			if(x == 3) {
				x = 0;
				y += 17;
			}
		}
		return y;
	}
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		RenderUtils.drawOtherBackground(0, 0, this.width, this.height,(System.nanoTime()-startTime)/1000000000F);
		RenderUtils.drawRect(0, 0, this.width, this.height, 0x30333333);
		BlurUtils.renderBlur(8);
		
		
		RenderUtils.drawRoundedRect(position.getX(),position.getY(), position.getX2(), position.getY2(), 0x30001233, 3D);
		
		int x = 0;
		double y = 0;
		for(AltManagerSession session : sessions) {
			session.getPosition().setX(position.getX()+2+x*(position.getWidth()- 8 +5)/3);
			session.getPosition().setY(position.getY()+2+y-scrollY);
			session.getPosition().setWidth((position.getWidth() - 8)/3);
			x++;
			if(x == 3) {
				x = 0;
				y += 17;
			}
			session.draw(mouseX, mouseY);
		}
		
		RenderUtils.drawImage(2, 2, 18, 18, new ResourceLocation("client/images/gothaj.png"), 0xffffffff);
		
		Fonts.getFont("rb-b").drawString("Alt Manager", 130/2-Fonts.getFont("rb-b").getWidth("Alt Manager")/2, 25-Fonts.getFont("rb-b").getWidth("GOTHAJ")/2, 0xffcccccc);
		
		
		//session
		RenderUtils.drawRoundedRect(2, 25, 128, 25+6+32, 0x771c2657, 2);
		
		String[] s = "Active Session".split("");
        double w = 0;
        
        for(String t : s) {
        	Fonts.getFont("rb-b").drawStringWithShadow(t, 2+ 126/2-Fonts.getFont("rb-b").getWidth("Active Session")/2+w,25+6,  ColorUtils.mix(0xff674cd4, 0xff3d6cb8, Math.sin(Math.toRadians(System.nanoTime()/1000000 + w*10)/3)+1, 2),0x45000000);
        	w += Fonts.getFont("rb-b").getWidth(t);
        }
        Fonts.getFont("rb-r").drawString("Name: §7"+ Minecraft.getMinecraft().session.getUsername(), 5, 25+6+12, -1);
        Fonts.getFont("rb-r").drawString("Online: " + (mc.session.getToken().equals("0") ? "§cNo" : "§aYes"), 5, 25+6+22, -1);
        //End session
        
        for(AltManagerButton b : buttons) {
        	b.draw(mouseX, mouseY);
        }
        
        panel.draw(mouseX, mouseY);
        
        double save = position.getHeight();
		if (save < getBigger()) {
			float g = Mouse.getEventDWheel();
			double maxScrollY = getBigger() - save;
			double size = Mouse.getDWheel() / 60;
			if (size != 0) {
				temp += size;
			}
			if (Math.round(temp) != 0) {
				temp = ((temp * (10 - 1)) / (10));
				double l = scrollY;
				scrollY = scrollY - temp;
				if (scrollY < 0) {
					scrollY = 0;
				} else if (scrollY > maxScrollY) {
					scrollY = maxScrollY;
				}
			} else {
				temp = 0;
			}
		} else {
			scrollY = 0;
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	
	

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		panel.key(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if(panel.open) {
			panel.click(mouseX, mouseY, mouseButton);
			return;
		}
		
		for(AltManagerSession session : sessions) {
			if(session.getPosition().isInside(mouseX, mouseY) && mouseButton == 0)
				session.onClick(mouseX, mouseY, mouseButton);
		}
		for(AltManagerButton b : buttons) {
        	if(b.getPosition().isInside(mouseX, mouseY) && mouseButton == 0) {
        		switch(b.getId()) {
        		case 0:
        			mc.displayGuiScreen(new Menu());
        			break;
        		case 1:
        			panel.open = true;
        			break;
        		case 2:
        			sessions.remove(active);
        			break;
        		case 3:
        			Minecraft.getMinecraft().session = new Session(StringUtils.generateNamesForMinecraft(preffix, firstNames, separator, lastNames, suffix), "0", "0", "mojang");
        			break;
        		case 4:
        			String text = getClipBoard();
        			
        			if(text.equals("")) return;
        			
        			if(text.contains(":")) {
                        String[] args =  text.split(":");
                        
                        args[1] = args[1].replaceAll("-", "");
                        
                        if(args.length == 3) {
                            Minecraft.getMinecraft().session =  new Session(args[0], args[1], args[2], "mojang");
                            return;
                        }
                    }
        			
        			break;
            	}
        	}
        }
		
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	public String getClipBoard(){
	    try {
	        return (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
	    } catch (HeadlessException e) {
	        e.printStackTrace();            
	    } catch (UnsupportedFlavorException e) {
	        e.printStackTrace();            
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return "";
	}
	@Override
	public void onGuiClosed() {
		((AccountsFile)Client.INSTANCE.getFileManager().getFile(AccountsFile.class)).save(this);
	}
}
