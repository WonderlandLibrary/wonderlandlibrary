// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.ui.altmanager.alt;

import net.minecraft.util.text.TextFormatting;

public class Alt
{
    private final String username;
    private String mask;
    private String password;
    private Status status;
    
    public Alt(final String username, final String password) {
        this(username, password, Status.Unchecked);
    }
    
    public Alt(final String username, final String password, final Status status) {
        this(username, password, "", status);
    }
    
    public Alt(final String username, final String password, final String mask, final Status status) {
        this.username = username;
        this.password = password;
        this.mask = mask;
        this.status = status;
    }
    
    public Status getStatus() {
        return this.status;
    }
    
    public void setStatus(final Status status) {
        this.status = status;
    }
    
    public String getMask() {
        return this.mask;
    }
    
    public void setMask(final String mask) {
        this.mask = mask;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(final String password) {
        this.password = password;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public enum Status
    {
        Working(TextFormatting.GREEN + "Working"), 
        Banned(TextFormatting.RED + "Banned"), 
        Unchecked(TextFormatting.YELLOW + "Unchecked"), 
        NotWorking(TextFormatting.RED + "Not Working");
        
        private final String formatted;
        
        private Status(final String string) {
            this.formatted = string;
        }
        
        public String toFormatted() {
            return this.formatted;
        }
    }
}
