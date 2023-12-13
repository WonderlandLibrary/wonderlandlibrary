package koks.gui.mainmenu.application;

import java.util.ArrayList;

/**
 * @author kroko
 * @created on 08.11.2020 : 02:12
 */
public class ApplicationManager {

    public ArrayList<Application> applications = new ArrayList<>();

    public void addApplication(Application application) {
        applications.add(application);
    }

    public void removeApplication(Application application) {
        applications.remove(application);
    }

    public Application getApplication(String name) {
        for(Application application : applications) {
            if(application.getName().equalsIgnoreCase(name)) {
                return application;
            }
        }
        return null;
    }
}
