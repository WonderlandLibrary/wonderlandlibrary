// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich;

import ru.tuskevich.util.drag.DragManager;
import ru.tuskevich.util.drag.Dragging;
import java.util.Iterator;
import ru.tuskevich.modules.Module;
import ru.tuskevich.util.animation.AnimationThread;
import ru.tuskevich.commands.macro.MacroManager;
import ru.tuskevich.files.FileManager;
import ru.tuskevich.util.config.ConfigManager;
import ru.tuskevich.util.math.ScaleMath;
import ru.tuskevich.commands.CommandManager;
import ru.tuskevich.managers.FriendManager;
import ru.tuskevich.ui.newui.ClickScreen;
import ru.tuskevich.modules.Manager;

public class Minced
{
    public double animationSpeed;
    public Manager manager;
    public ClickScreen menuMain;
    public FriendManager friendManager;
    public CommandManager commandManager;
    public ScaleMath scaleMath;
    public ConfigManager configManager;
    public FileManager fileManager;
    public MacroManager macroManager;
    public static Minced instance;
    
    public Minced() {
        this.animationSpeed = 0.15;
        this.scaleMath = new ScaleMath(2);
        this.configManager = new ConfigManager();
    }
    
    public void init() throws Exception {
        Minced.instance = this;
        new AnimationThread().start();
        this.fileManager = new FileManager();
        try {
            (this.friendManager = new FriendManager()).init();
            (this.macroManager = new MacroManager()).init();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.manager = new Manager();
        this.commandManager = new CommandManager();
        this.menuMain = new ClickScreen();
        this.configManager.loadConfig("default");
    }
    
    public static Minced getInstance() {
        return Minced.instance;
    }
    
    public void keyTyped(final int key) {
        for (final Module m : this.manager.getModules()) {
            if (m.bind == key) {
                m.toggle();
            }
        }
        if (this.macroManager != null) {
            this.macroManager.onKeyPressed(key);
        }
    }
    
    public Dragging createDrag(final Module module, final String name, final float x, final float y) {
        DragManager.draggables.put(name, new Dragging(module, name, x, y));
        return DragManager.draggables.get(name);
    }
    
    public double createAnimation(final double value) {
        return Math.sqrt(1.0 - Math.pow(value - 1.0, 2.0));
    }
    
    public double dropAnimation(final double value) {
        final double c1 = 1.70158;
        final double c2 = c1 + 1.0;
        return 1.0 + c2 * Math.pow(value - 1.0, 3.0) + c1 * Math.pow(value - 1.0, 2.0);
    }
}
