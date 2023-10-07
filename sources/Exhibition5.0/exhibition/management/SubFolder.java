// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management;

public enum SubFolder
{
    ModuleJars("Plugins"), 
    Module("Modules"), 
    Alt("Alts"), 
    Other("Other");
    
    private final String folderName;
    
    private SubFolder(final String folderName) {
        this.folderName = folderName;
    }
    
    public String getFolderName() {
        return this.folderName;
    }
}
