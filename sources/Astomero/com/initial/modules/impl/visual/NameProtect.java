package com.initial.modules.impl.visual;

import com.initial.modules.*;

public class NameProtect extends Module
{
    public static String nameprotect;
    
    public NameProtect() {
        super("NameProtect", 0, Category.OTHER);
    }
    
    public static String getNameProtect() {
        return NameProtect.nameprotect;
    }
    
    static {
        NameProtect.nameprotect = "§cUser";
    }
}
