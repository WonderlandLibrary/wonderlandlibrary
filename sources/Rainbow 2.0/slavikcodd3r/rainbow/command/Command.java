package slavikcodd3r.rainbow.command;

public class Command
{
    private String[] names;
    
    public void runCommand(final String[] args) {
    }
    
    public String getHelp() {
        return null;
    }
    
    public String[] getNames() {
        return this.names;
    }
    
    public void setNames(final String[] names) {
        this.names = names;
    }
}
