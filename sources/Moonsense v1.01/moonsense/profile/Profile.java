// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.profile;

import java.util.Random;

public class Profile
{
    private String name;
    private String folderName;
    public static final Profile DEFAULT_PROFILE;
    
    static {
        DEFAULT_PROFILE = new Profile("Default", "");
    }
    
    public Profile(final String name, final String folderName) {
        this.name = name;
        this.folderName = folderName;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getFolderName() {
        return this.folderName;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setFolderName(final String folderName) {
        this.folderName = folderName;
    }
    
    public static Profile generate() {
        final Random r = new Random();
        final String name = "Profile " + Math.abs(r.nextInt() / 2147);
        return new Profile(name, name);
    }
    
    @Override
    public String toString() {
        return "Profile(name=" + this.name + ", folderName=" + this.folderName + ")";
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Profile)) {
            return false;
        }
        final Profile p1 = this;
        final Profile p2 = (Profile)obj;
        return p1.toString().equals(p2.toString());
    }
}
