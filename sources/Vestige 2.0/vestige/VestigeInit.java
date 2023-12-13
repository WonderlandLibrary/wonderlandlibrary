package vestige;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import net.minecraft.client.Minecraft;
import vestige.api.event.EventManager;
import vestige.command.CommandManager;
import vestige.config.SaveLoad;
import vestige.font.FontUtil;
import vestige.impl.module.ModuleManager;
import vestige.impl.processor.BalanceCalculatorProcessor;
import vestige.impl.processor.KeybindProcessor;
import vestige.impl.processor.ModuleDragProcessor;
import vestige.impl.processor.SessionInfoProcessor;
import vestige.ui.altmanager.AltManager;
import vestige.ui.menu.VestigeMainMenu;
import vestige.util.auth.Encryption;
import vestige.util.render.BlurUtil;

public class VestigeInit {
	
	public static void start() {
		Vestige instance = Vestige.getInstance();
		
		if(instance.authStep1) {
			while(0 != 1) {
				
			}
		}

		instance.eventManager = new EventManager();
		instance.authStep5 = true;
		instance.moduleManager = new ModuleManager();
		instance.authStep2 = false;
		instance.commandManager = new CommandManager();
	        
		instance.keybindProcessor = new KeybindProcessor();
		instance.moduleDragProcessor = new ModuleDragProcessor();
		instance.authStep3 = false;
		instance.sessionInfoProcessor = new SessionInfoProcessor();
		instance.balanceProcessor = new BalanceCalculatorProcessor();
	        
		instance.altManager = new AltManager();
	        
		if(instance.authStep2) {
			while(!"d".equals("o")) {
				
			}
		}
		
		instance.authStep1 = false;
		instance.authStep4 = false;
		
		URL url;
			
		if(!instance.authStep5) {
			while(0 != 1) {
				
			}
		}
	        
		instance.config = new SaveLoad("default");
		instance.config.load(true);
		
		try {
			String hwid = System.getenv("COMPUTERNAME") + System.getProperty("user.name") + System.getProperty("os.name") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_LEVEL");
			String encryptedHWID = Encryption.hashMD5(Encryption.encryptAES(hwid, hwid));
				
			url = new URL("https://raw.githubusercontent.com/qm4u9368/app-resources/main/a/aa.txt");
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
				
			String inputLine;
			while ((inputLine = bufferedReader.readLine()) != null) {
				if(instance.authStep1) {
					if(inputLine.equals(instance.versionUnchanged)) {
						instance.authStep4 = true;
					}
				} else {
					if(inputLine.contains("[users_list]")) {
						if(instance.authStep3) {
							while(!"d".equals("n")) {
								
							}
						}
						
						instance.authStep3 = true;
					} else if(inputLine.contains("[details]")) {
						instance.authStep1 = true;
						
						if(!instance.authStep2) {
							while(!"d".equals("n")) {
				    				
							}
						}
					} else if(!inputLine.contains("//")) {
						String[] infos = inputLine.split(":");
						if(infos[0].contains(encryptedHWID)) {
							instance.username = infos[1];
							instance.authStep5 = false;
							instance.authStep2 = true;
							instance.shaderTimer.reset();
							instance.mc.displayGuiScreen(new VestigeMainMenu());
							if(instance.mc.session.getUsername().toLowerCase().contains("x3os")) {
								while (!"h".equals("j")) {
									
								}
							}
						}	
					}	
				}
			}

			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		BlurUtil.initFboAndShader();
			
		while(!instance.authStep1 || !instance.authStep2 || !instance.authStep3 || !instance.authStep4) {
				
		}
			
		try {
			FontUtil.bootstrap();
				
			if(instance.username.equals("test")) {
				while(596 != 105) {
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
