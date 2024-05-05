package ru.smertnix.celestial.files.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import net.minecraft.client.gui.GuiChat;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.files.FileManager;
import ru.smertnix.celestial.friend.Friend;
import ru.smertnix.celestial.ui.mainmenu.MainMenu;
import ru.smertnix.celestial.utils.other.NameUtils;


public class ClientConfig extends FileManager.CustomFile {
    public ClientConfig(String name, boolean loadOnStart) {
        super(name, loadOnStart);
    }

    @Override
    public void loadFile() {
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(this.getFile()));
            while ((line = br.readLine()) != null) {
                String curLine = line.trim();
                if (curLine.contains("Shaders")) {
                    String name = curLine.split(":")[1];
                    MainMenu.s =  Boolean.parseBoolean(name);
                }
                if (curLine.contains("HideInformation")) {
                    String name = curLine.split(":")[1];
                    GuiChat.hide =  Boolean.parseBoolean(name);
                }
                if (curLine.contains("ChangeLogPinned")) {
                    String name = curLine.split(":")[1];
                    MainMenu.pinned =  Boolean.parseBoolean(name);
                }
                if (curLine.contains("Name")) {
                    String name = curLine.split(":")[1];
                    NameUtils.setName(name);
                }
                if (curLine.contains("UID")) {
                    String name = curLine.split(":")[1];
                    NameUtils.setUID(name);
                }
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
            out.write("Shaders:" + Celestial.shaders);
            out.write("\r\n");
            out.write("HideInformation:" + Celestial.hi);
            out.write("\r\n");
            out.write("ChangeLogPinned:" + Celestial.pin);
            out.close();
            out.write("Name:" + Celestial.nick);
            out.close();
            out.write("UID:" + Celestial.uid);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
