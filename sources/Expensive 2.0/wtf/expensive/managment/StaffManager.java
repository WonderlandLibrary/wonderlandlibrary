package wtf.expensive.managment;

import net.minecraft.client.Minecraft;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dedinside
 * @since 06.07.2023
 */
public class StaffManager {
    public static List<String> staffNames = new ArrayList<>();

    public static final File file = new File(Minecraft.getInstance().gameDir, "\\expensive\\staff.exp");

    public void init() throws Exception {
        if (!file.exists()) {
            file.createNewFile();
        } else {
            readFriends();
        }
    }

    public void addStaff(String name) {
        staffNames.add(name);
        updateFile();
    }

    public String getStaffs(String staffs) {
        return staffNames.stream().filter(staff -> staff.equals(staffs)).findFirst().get();
    }

    public boolean isStaff(String staffName) {
        return staffNames.stream().anyMatch(staff -> staff.equals(staffName));
    }

    public void removeStaff(String name) {
        staffNames.removeIf(friend -> friend.equalsIgnoreCase(name));
    }

    public void clearStaffs() {
        staffNames.clear();
        updateFile();
    }

    public List<String> getStaffNames() {
        return staffNames;
    }

    public void updateFile() {
        try {
            StringBuilder builder = new StringBuilder();
            staffNames.forEach(friend -> builder.append(friend).append("\n"));
            Files.write(file.toPath(), builder.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readFriends() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(file.getAbsolutePath()))));
            String line;
            while ((line = reader.readLine()) != null) {
                staffNames.add(line);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}