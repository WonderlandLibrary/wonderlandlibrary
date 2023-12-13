package me.robbanrobbin.jigsaw.gui;

import java.awt.Color;

import me.robbanrobbin.jigsaw.module.Module;

public class ModuleListEntry {
	
	public int time;
	public Module module;
	public Color color;
	public int type = 0;
	
	public ModuleListEntry(Module module, int type) {
		this(module, type, null);
	}
	
	public ModuleListEntry(Module module, int type, Color color) {
		this.module = module;
		this.type = type;
		this.color = color;
	}
	
	public void update() {
		
	}
	
}
