package tech.atani.client.utility.logging;

import net.minecraft.util.EnumChatFormatting;
import tech.atani.client.utility.interfaces.Methods;

public final class LogUtil
implements Methods {
    private static LogUtil INSTANCE;
    
    public static LogUtil getInstance() {
    	if(INSTANCE == null) {
    		INSTANCE = new LogUtil();
    	}
    	return INSTANCE;
    }
    
    public void sendMessage(String string, String ... arrstring) {
        if (arrstring.length == 1 || arrstring.length == 2) {
            switch (arrstring[0]) {
                case "chat": {
                	sendMessage(string);
                    break;
                }
                case "console": {
                    System.out.println(EnumChatFormatting.getTextWithoutFormattingCodes(string));
                    break;
                }
                case "real_chat": {
                    mc.thePlayer.sendChatMessage(string);
                }
            }
        }
    }

}

