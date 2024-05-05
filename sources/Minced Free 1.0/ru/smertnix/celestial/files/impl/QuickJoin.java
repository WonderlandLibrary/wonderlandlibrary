package ru.smertnix.celestial.files.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.files.FileManager;
import ru.smertnix.celestial.friend.Friend;
import ru.smertnix.celestial.ui.altmanager.alt.Alt;
import ru.smertnix.celestial.ui.altmanager.alt.AltManager;
import ru.smertnix.celestial.ui.altmanager.alt.Server;
import ru.smertnix.celestial.ui.altmanager.alt.ServerManager;
import ru.smertnix.celestial.ui.altmanager.alt.Alt.Status;


public class QuickJoin extends FileManager.CustomFile {
    public QuickJoin(String name, boolean loadOnStart) {
        super(name, loadOnStart);
    }

    @Override
    public void loadFile() {
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(this.getFile()));
            while ((line = br.readLine()) != null) {
                String[] arguments = line.split(":");
                
                if (!ServerManager.inRegistry(arguments[0]))
                ServerManager.registry.add(new Server(arguments[0], arguments[1]));
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveFile() {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(this.getFile()));
            for (Server s : ServerManager.registry) {
            	if (!(s.getName().contains("MST") || s.getName().contains("Really") ||s.getName().contains("Nexus") ||s.getName().contains("MineBlaze") ||s.getName().contains("SunRise"))) {
            		out.write(s.getName() + ":" + s.getIp());
                    out.write("\r\n");
            	}
            }
            /*
            out.write("MST Network" + ":" + "mc.mstnw.net");
            out.write("\r\n");
            out.write("Really World" + ":" + "mc.reallyworld.ru");
            out.write("\r\n");
            out.write("Nexus Grief" + ":" + "ngrief.su");
            out.write("\r\n");
            out.write("MineBlaze" + ":" + "mineblaze.ru");
            out.write("\r\n");
            out.write("SunRise" + ":" + "play.sunmc.ru");
            out.write("\r\n");*/
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
