package wtf.monsoon.api.auth.database;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class Database {

    public static ArrayList<String> users = new ArrayList<>();


    public static void establishUsers() {
        try
        {
            URL list = new URL("https://raw.githubusercontent.com/MonsoonDevelopment/Authtest/main/list.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(list.openStream()));

            String userLine;

            while ((userLine = bufferedReader.readLine()) != null)
            {
                users.add(userLine);

            }
        }
        catch(Exception e)
        {
            System.out.println("Monsoon: Error code [13A]");
        }
    }

    public static boolean isUser(String hwid) {
        return users.contains(hwid);
    }
}