package vestige.util.auth;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class HWIDUtil {

    public static String getHWID() {
        String hwid = System.getenv("COMPUTERNAME") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_LEVEL") + System.getenv("PROCESSOR_ARCHITECTURE") + System.getenv("NUMBER_OF_PROCESSORS");

        try {
            String osName = System.getProperty("os.name");
            if(osName.contains("Windows")) {
                String result = "";
                Process p = Runtime.getRuntime().exec("wmic baseboard get serialnumber");
                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = input.readLine()) != null) {
                    result += line;
                }

                result = result.replace(" ", "");

                if(result.length() >= 6) {
                    hwid += result;
                }
                input.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return EncryptionUtil.hashMD5(EncryptionUtil.encryptAES(hwid, hwid));
    }

}
