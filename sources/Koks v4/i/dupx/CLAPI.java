/*
 * Copyright (c) 2020, HAZE. All rights reserved.
 */ 
package i.dupx;

/*
 * Mockup implementation for ClientLauncher API
 * @author idupx
 *
 */
public class CLAPI {
    /*
     * 
     * @return Name of the Clientlauncher User
     */
    public static String getCLUsername() { return "YourMockName"; }
    
    /**
     * 
     * @return The client role (0 = user, 1 = supporter, 2 = developer, 10 = clientlauncher admin)
     */
    public static int getClientRole() { return 10; }
}