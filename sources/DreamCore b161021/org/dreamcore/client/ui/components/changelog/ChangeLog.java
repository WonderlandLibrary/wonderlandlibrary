package org.dreamcore.client.ui.components.changelog;

public class ChangeLog {

    protected String changeName;
    protected ChangeType type;

    public ChangeLog(String name, ChangeType type) {
        this.changeName = name;
        this.type = type;

        switch (type) {
            case NEW:
                changeName = "    \2477[\2476!\2477] New \247f" + changeName;
                break;
            case ADD:
                changeName = "    \2477[\247a+\2477] Added \247f" + changeName;
                break;
            case RECODE:
                changeName = "    \2477[\2479*\2477] Recoded \247f" + changeName;
                break;
            case IMPROVED:
                changeName = "    \2477[\247d/\2477] Improved \247f" + changeName;
                break;
            case DELETE:
                changeName = "    \2477[\247c-\2477] Deleted \247f" + changeName;
                break;
            case FIXED:
                changeName = "    \2477[\247b/\2477] Fixed \247f" + changeName;
                break;
            case MOVED:
                changeName = "    \2477[\2479->\2477] Moved \247f" + changeName;
                break;
            case RENAMED:
                changeName = "    \2477[\2479!\2477] Renamed \247f" + changeName;
                break;
            case NONE:
                changeName = " " + changeName;
                break;
        }
    }

    public String getLogName() {
        return changeName;
    }

    public ChangeType getType() {
        return type;
    }
}