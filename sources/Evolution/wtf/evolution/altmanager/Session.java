package wtf.evolution.altmanager;

import net.minecraft.client.Minecraft;

public class Session {

    public String nick;
    public String password;

    public Session(String nick) {

        this.nick = nick;
        session();
    }

    public void session() {
            Minecraft.getMinecraft().session = new net.minecraft.util.Session(nick, "", "", "mojang");
    }

}
