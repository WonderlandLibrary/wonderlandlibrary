package wtf.evolution.editor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

public class DragManager {
    public static HashMap<String, Drag> draggables = new HashMap<>();

    private static final File DRAG_DATA = new File("evolution", "drag.cfg");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().setLenient().create();

    public static void saveDragData() {
        if (!DRAG_DATA.exists()) {
            DRAG_DATA.getParentFile().mkdirs();
        }
        try {
            String ss = "";
            for (Drag drag : draggables.values()) {
                ss += (drag.name + ":" + drag.getX() + ":" + drag.getY()) + "\n";
            }
            Files.write(DRAG_DATA.toPath(), ss.getBytes());

        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Failed to save draggables");
        }
    }

    public static void loadDragData() {
        try {
            if (!DRAG_DATA.exists()) {
                System.out.println("No dragg data found");
                return;
            }

            for (String ss : Files.readAllLines(DRAG_DATA.toPath())) {
                String[] split = ss.split(":");
                Drag currentDrag = draggables.get(split[0]);
                currentDrag.setX(Float.parseFloat(split[1]));
                currentDrag.setY(Float.parseFloat(split[2]));
                draggables.put(split[0], currentDrag);
            }

        }
         catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed to load draggables");
         }
    }

}
