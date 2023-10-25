package org.dreamcore.client.ui.components.altmanager.alt;

public class Alt {

    private final String username;
    private String mask;
    private String password;
    private Status status;

    public Alt(String username, String password) {
        this(username, password, Status.Unchecked);
    }

    public Alt(String username, String password, Status status) {
        this(username, password, "", status);
    }

    public Alt(String username, String password, String mask, Status status) {
        this.username = username;
        this.password = password;
        this.mask = mask;
        this.status = status;
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

    public String getUsername() {
        return this.username;
    }

    public enum Status {
        Working("§aWorking"),
        Banned("§cBanned"),
        Unchecked("§eUnchecked"),
        NotWorking("§4Not Working");

        private final String formatted;

        Status(String string) {
            this.formatted = string;
        }

        public String toFormatted() {
            return this.formatted;
        }
    }
}