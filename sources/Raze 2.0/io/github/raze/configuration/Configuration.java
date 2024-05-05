package io.github.raze.configuration;

public class Configuration {

    private final String name, description;
    private String error = "None";

    public Configuration(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() { return this.name; }

    public String getDescription() { return this.description; }

    public String getError() { return this.error; }

    public void setError(String input) { this.error = input; }

}
