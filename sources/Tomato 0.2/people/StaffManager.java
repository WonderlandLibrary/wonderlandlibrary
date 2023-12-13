/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.people;

import java.util.ArrayList;

public class StaffManager {
    public static ArrayList<StaffMember> Smembers = new ArrayList();

    public static void addStaffMember(String name) {
        Smembers.add(new StaffMember(name));
    }

    public static void deleteStaffMember(String name) {
        for (StaffMember smember : Smembers) {
            if (!smember.getName().equalsIgnoreCase(name)) continue;
            Smembers.remove(smember);
            break;
        }
    }

    public static boolean isStaff(String name) {
        boolean isStaff = false;
        for (StaffMember smember : Smembers) {
            if (!smember.getName().equalsIgnoreCase(name)) continue;
            isStaff = true;
            break;
        }
        return isStaff;
    }

    public static class StaffMember {
        private String name;

        public StaffMember(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String s) {
            this.name = s;
        }
    }

}

