package me.nyan.flush.target;

import me.nyan.flush.Flush;
import net.minecraft.entity.Entity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TargetManager {
    private final File dataFile;
    private static final List<String> target = new ArrayList<>();

    public boolean addTarget(String e) {
        if (!target.contains(e)) {
            target.add(e);
            return true;
        }
        return false;
    }

    public boolean removeTarget(String e) {
        if (target.contains(e)) {
            target.remove(e);
            return true;
        }

        return false;
    }

    public boolean isTarget(Entity e) {
        return target.contains(e.getName());
    }

    public List<String> getTargets() {
        return target;
    }

    public TargetManager() {
        dataFile = new File(Flush.getClientPath(), "target.txt");

        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void save() {
        try {
            PrintWriter printWriter = new PrintWriter(dataFile);

            for (String str : getTargets()) {
                printWriter.println(str);
            }

            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(dataFile));
            String line = reader.readLine();

            while (line != null) {
                addTarget(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
