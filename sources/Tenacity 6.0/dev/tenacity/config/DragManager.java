// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.config;

import com.google.gson.GsonBuilder;
import dev.tenacity.Tenacity;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.charset.StandardCharsets;
import com.google.gson.Gson;
import java.io.File;
import dev.tenacity.utils.objects.Dragging;
import java.util.HashMap;

public class DragManager
{
    public static HashMap<String, Dragging> draggables;
    private static final File DRAG_DATA;
    private static final Gson GSON;
    
    public static void saveDragData() {
        if (!DragManager.DRAG_DATA.exists()) {
            DragManager.DRAG_DATA.getParentFile().mkdirs();
        }
        try {
            Files.write(DragManager.DRAG_DATA.toPath(), DragManager.GSON.toJson((Object)DragManager.draggables.values()).getBytes(StandardCharsets.UTF_8), new OpenOption[0]);
        }
        catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Failed to save draggables");
        }
    }
    
    public static void loadDragData() {
        if (!DragManager.DRAG_DATA.exists()) {
            System.out.println("No drag data found");
            return;
        }
        Dragging[] draggings;
        try {
            draggings = (Dragging[])DragManager.GSON.fromJson(new String(Files.readAllBytes(DragManager.DRAG_DATA.toPath()), StandardCharsets.UTF_8), (Class)Dragging[].class);
        }
        catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Failed to load draggables");
            return;
        }
        for (final Dragging dragging : draggings) {
            if (DragManager.draggables.containsKey(dragging.getName())) {
                final Dragging currentDrag = DragManager.draggables.get(dragging.getName());
                currentDrag.setX(dragging.getX());
                currentDrag.setY(dragging.getY());
                DragManager.draggables.put(dragging.getName(), currentDrag);
            }
        }
    }
    
    static {
        DragManager.draggables = new HashMap<String, Dragging>();
        DRAG_DATA = new File(Tenacity.DIRECTORY, "Drag.json");
        GSON = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().setLenient().create();
    }
}
