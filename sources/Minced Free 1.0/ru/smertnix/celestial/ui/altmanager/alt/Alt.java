package ru.smertnix.celestial.ui.altmanager.alt;

import java.util.Date;

import net.minecraft.util.text.TextFormatting;

public class Alt {

    private final String username;
    private String mask;
    private String password;
    private String date;
    private Status status;
    public boolean random;

    public Alt(String username, String password, String date) {
        this(username, password, Status.Unchecked, date, false);
    }
    
    public Alt(String username, String password, boolean random, String date) {
        this(username, password, Status.Unchecked, date, random);
    }
    
    public Alt(String username, String password, Status status, String date, boolean random) {
        this(username, password, "", status, random, date);
    }

    public Alt(String username, String password, Status status, String date) {
        this(username, password, "", status, false, date);
    }

    public Alt(String username, String password, String mask, Status status, boolean random, String date) {
        this.username = username;
        this.password = password;
        this.mask = mask;
        this.status = status;
        this.random = random;
        this.date = date;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMask() {
        return this.mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUsername() {
        return this.username;
    }

    public enum Status {
        Working(TextFormatting.GREEN + "Working"),
        Banned(TextFormatting.RED + "Banned"),
        Unchecked(TextFormatting.YELLOW + "Unchecked"),
        NotWorking(TextFormatting.RED + "Not Working");

        private final String formatted;

        Status(String string) {
            this.formatted = string;
        }

        public String toFormatted() {
            return this.formatted;
        }
    }
}