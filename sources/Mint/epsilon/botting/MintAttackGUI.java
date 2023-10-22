package epsilon.botting;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import epsilon.Epsilon;
import epsilon.botting.cmds.ConsoleDelay;
import epsilon.botting.cmds.ConsoleIP;
import epsilon.botting.cmds.ConsolePort;
import epsilon.botting.cmds.EventCMD;
import epsilon.modules.exploit.CrackedRaper;
import epsilon.ui.MainMenu;
import epsilon.util.ColorUtil;
import epsilon.util.Timer;
import epsilon.util.Font.Fonts;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class MintAttackGUI extends GuiScreen {

    private int offset, textOffset, hitcount, hitcount2;
    private String ip, port;
    private static List<String> line = new ArrayList<String>();
    private GuiTextField consoleText = new GuiTextField(offset+5555, fontRendererObj, 47, height-66, 5, 15);
    public static boolean startAttack;
    private final Timer timer = new Timer();
    
    public static void print(String message) {
    	line.add(message);
    }
    


    @Override
    protected void keyTyped(char par1, int par2) {
        this.consoleText.textboxKeyTyped(par1, par2);
        switch(par2) {
        case 1:

			this.mc.displayGuiScreen(new MainMenu());
			
        	break;
        	
        case 28:


            EventCMD event = new EventCMD(this.consoleText.getText());
            Epsilon.onEvent(event);
        	if(!event.isCancelled()) 
            line.add(this.consoleText.getText());
            this.consoleText.setText("");
            
        	break;
        }
    }

    @Override
	 public void drawScreen(int par1, int par2, float par3) {
		 
		 int offy=-10;textOffset=-10;
		 int upc = -1;
		 int downc = -1;
	        if (Mouse.hasWheel()) {
	            int wheel = Mouse.getDWheel();
	            if (wheel < 0) {
	                this.offset += 16;
	            	upc = 0xff3a9100;
	            	downc = -1;
	            } else if (wheel > 0) {
	                this.offset -= 16;
	            	downc = 0xff3a9100;
	            	upc = -1;
	            }
	        }
			 int consoleTextOffset = 0;
			 this.consoleText.drawTextBox();
		 this.drawDefaultBackground();
	     Gui.drawRect(46.0f, 30.0f, width - 50, height - 50, -16777216);
	     Gui.drawRect(width-60, 30.0f, width - 50, height - 50, 0xff141414);
	     this.drawString(fontRendererObj, "^", width-58, 30, upc);
	     this.drawString(fontRendererObj, "V", width-58, height - 58, downc);
	     Fonts.Segation23.drawStringWithShadow("Mint 'Stresser'", 46, 13, ColorUtil.getColorAsInt(15, "CottonCandy", 0.5f));
	     Fonts.Segation23.drawStringWithShadow("| IP = " + ConsoleIP.getIP() + " | Port = " + ConsolePort.getPort() + " | JoinDelay = " + ConsoleDelay.msDelay, 4 + ("Mint Server Botter Info :".length()*4.95f), 13, -1);
	     ip = ConsoleIP.getIP();
	     port = ConsolePort.getPort();
	     for(String lines : line) {
	    	 offy+=20;textOffset+=20;
	    	 if(offy-offset>0 && offy-offset<height - 90)  {
	    		 this.drawString(fontRendererObj, lines,  50, 33 - offset + offy, -1);
	    	 }
	     }
	     
	     if(this.consoleText.getText()!=null) {
	    	 int offset = 0;
	    	 LocalDateTime date = LocalDateTime.now();
	    	 int time = date.toLocalTime().getSecond();
		     if(this.consoleText.getText()!="") {
		    	 this.drawString(fontRendererObj, this.consoleText.getText(), 50, height-60, -1);
		    	 offset = fontRendererObj.getStringWidth(this.consoleText.getText())+5;
		     }

		     if(time%2==0) {
		    	 this.drawRect(46 + offset, height-50, 50 + offset, height - 62, -1);
		     }
		     
		     if(startAttack && (timer.hasTimeElapsed(ConsoleDelay.msDelay, true) || ConsoleDelay.msDelay == 0)) {
		    	 MintAttack.createNewBot(CrackedRaper.genCrackedAlt("briish"), true, "blak", port, ip);
		    	 hitcount++;
		    	 if(hitcount>0) {
		    		 line.remove(line.size()-1);
		    	 }
		    	 print("Thread 1, authErrorSpam: (" + hitcount + "x) | Thread 2, packetEncryptionError: (" + hitcount2 + "x)");
		    	 
		    	 
	    	 
		    	 MintAttack.createNewBot(CrackedRaper.genCrackedAlt("briish"), true, "blak", port, ip, true);
		    	 hitcount2++;
		    	 
		    	 
	    	 
		    	 
		     }
	     
	     }
	     
	     

        super.drawScreen(par1, par2, par3);
	 }
	
    @Override
    public void actionPerformed(GuiButton button) throws IOException {
    	switch(button.id) {
    	case -1:
			this.mc.displayGuiScreen(new MainMenu());
    		break;
    	case 1337:
    		line.clear();
    		hitcount = hitcount2 = 0;
            line.add("Mint Corporations [Version B1]");
            line.add("(c) 2022 Mint Corporation. All rights reserved.");
            line.add("Type 'help' to get started!");
            offset = 0;
    		break;
    	case 0:
    		
    		startAttack = true;
    		
    		break;
    		
    	case 1:
    		
    		startAttack = false;
    		
    		break;
    	}
    	super.actionPerformed(button);
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        try {
            super.mouseClicked(par1, par2, par3);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initGui() {
    	hitcount = 0;
    	startAttack = false;
    	line.clear();
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(1, width - 100, 0, 100, 20, "End"));
        this.buttonList.add(new GuiButton(0, width - 200, 0, 100, 20, "Start"));
        this.buttonList.add(new GuiButton(1337, width - 300, 0, 100, 20, "Clear"));

        this.buttonList.add(new GuiButton(-1, width - 400, 0, 100, 20, "Exit"));
        line.add("Mint Corporations [Version B1]");
        line.add("(c) 2022 Mint Corporation. All rights reserved.");
       /* line.add("                                                                                                                                       ,----,                                                ");
        line.add("          ____                                                                                                                       ,/   .`|                                                ");
        line.add("        ,'  , `.                         ___              .--.--.       ___                                                        ,`   .'  :                      ___                       ");
        line.add("     ,-+-,.' _ |  ,--,                 ,--.'|_           /  /    '.   ,--.'|_                                                    ;    ;     /                    ,--.'|_                     ");
        line.add("  ,-+-. ;   , ||,--.'|         ,---,   |  | :,'         |  :  /`. /   |  | :,'   __  ,-.                                       .'___,/    ,'                     |  | :,'   ,---.    __  ,-. ");
        line.add(" ,--.'|'   |  ;||  |,      ,-+-. /  |  :  : ' :         ;  |  |--`    :  : ' : ,' ,'/ /|          .--.--.    .--.--.           |    :     |            .--.--.   :  : ' :  '   ,'\\ ,' ,'/ /| ");
        line.add("|   |  ,', |  ':`--'_     ,--.'|'   |.;__,'  /          |  :  ;_    .;__,'  /  '  | |' | ,---.   /  /    '  /  /    '          ;    |.';  ;   ,---.   /  /    '.;__,'  /  /   /   |'  | |' | ");
        line.add("|   | /  | |  ||,' ,'|   |   |  ,\"' ||  |   |            \\  \\    `. |  |   |   |  |   ,'/     \\ |  :  /`./ |  :  /`./          `----'  |  |  /     \\ |  :  /`./|  |   |  .   ; ,. :|  |   ,' ");
        line.add("'   | :  | :  |,'  | |   |   | /  | |:__,'| :             `----.   \\:__,'| :   '  :  / /    /  ||  :  ;_   |  :  ;_                '   :  ; /    /  ||  :  ;_  :__,'| :  '   | |: :'  :  /   ");
        line.add(";   . |  ; |--' |  | :   |   | |  | |  '  : |__           __ \\  \\  |  '  : |__ |  | ' .    ' / | \\  \\    `. \\  \\    `.             |   |  '.    ' / | \\  \\    `. '  : |__'   | .; :|  | '    ");
        line.add("|   : |  | ,    '  : |__ |   | |  |/   |  | '.'|         /  /`--'  /  |  | '.'|;  : | '   ;   /|  `----.   \\ `----.   \\            '   :  |'   ;   /|  `----.   \\|  | '.'|   :    |;  : |    ");
        line.add("|   : '  |/     |  | '.'||   | |--'    ;  :    ;        '--'.     /   ;  :    ;|  , ; '   |  / | /  /`--'  //  /`--'  /            ;   |.' '   |  / | /  /`--'  /;  :    ;\\   \\  / |  , ;    ");
        line.add(";   | |`-'      ;  :    ;|   |/        |  ,   /           `--'---'    |  ,   /  ---'  |   :    |'--'.     /'--'.     /             '---'   |   :    |'--'.     / |  ,   /  `----'   ---'     ");
        line.add("|   ;/          |  ,   / '---'          ---`-'                         ---`-'          \\   \\  /   `--'---'   `--'---'                       \\   \\  /   `--'---'   ---`-'                     ");
        line.add("'---'            ---`-'                                                                 `----'                                               `----'                                          ");
        line.add("");*/
        line.add("");
        line.add("Type 'help' to get started!");
        this.consoleText = new GuiTextField(this.eventButton, this.mc.fontRendererObj, width / 2 - 100, 60, 200, 20);
        this.consoleText.setCanLoseFocus(false);
        this.consoleText.setFocused(true);
        super.initGui();
    }

}
