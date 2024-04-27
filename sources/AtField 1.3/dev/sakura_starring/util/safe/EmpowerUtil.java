/*
 * Decompiled with CFR 0.152.
 */
package dev.sakura_starring.util.safe;

import dev.sakura_starring.util.web.WebUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EmpowerUtil {
    public static boolean verification = false;

    public static List getServerEmpowerLib() {
        ArrayList<String> arrayList = new ArrayList<String>();
        String string = "";
        String string2 = "";
        try {
            String string3 = "https://gitcode.net/2301_76573194/at-field-sense/-/raw/master/empower.txt";
            string = WebUtils.get(string3);
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
        for (int i = 0; i <= string.getBytes().length - 1; ++i) {
            while (i <= string.getBytes().length - 1 && string.getBytes()[i] != 10) {
                string2 = string2 + (char)string.getBytes()[i];
                ++i;
            }
            arrayList.add(string2);
            string2 = "";
        }
        return arrayList;
    }

    public static boolean verification() {
        if (verification) {
            return true;
        }
        List list = EmpowerUtil.getServerEmpowerLib();
        for (int i = 0; list.size() != 0 && i <= list.size() - 1; ++i) {
            if (!Objects.equals(list.get(i), "AtField")) continue;
            verification = true;
            return true;
        }
        return false;
    }
}

