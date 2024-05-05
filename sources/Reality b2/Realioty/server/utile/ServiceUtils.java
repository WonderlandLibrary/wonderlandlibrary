package Realioty.server.utile;

public class ServiceUtils {
    public static String getRank(String name) {
        if (name.equals("Nyghtfull")) {
            return "\2476" + "[Developer]\247r";
        }
        switch (name) {
            case "Nyghtfull":
                return "\2474" + "[Admin]\247r + \"[Developer]\\247r";
            
        }

        return "\2477[User]\247f";
    }
}
