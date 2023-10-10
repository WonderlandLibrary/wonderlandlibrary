/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.rektskyapi.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.lang.reflect.Field;
import tk.rektsky.rektskyapi.permission.BranchAccess;
import tk.rektsky.rektskyapi.permission.TestServerAccess;

public class Role {
    public static final Role USER = new Role("User", "Normal user who bought/applied for RektSky", new BranchAccess(true, false, false), new TestServerAccess(false, false), false);
    public static final Role BETA = new Role("Beta", "Early access users", new BranchAccess(true, true, false), new TestServerAccess(false, false), false);
    public static final Role TESTER = new Role("Tester", "Super early access users", new BranchAccess(true, true, true), new TestServerAccess(false, false), false);
    public static final Role FRIEND = new Role("Friend", "Friend of owner/developer", new BranchAccess(true, true, true), new TestServerAccess(false, true), false);
    public static final Role TRUSTED = new Role("Trusted", "Highly trusted friend of owner/developer", new BranchAccess(true, true, true), new TestServerAccess(true, true), false);
    public static final Role OWNER = new Role("Owner", "Also known as Developer", new BranchAccess(true, true, true), new TestServerAccess(true, true), true);
    @Expose
    @SerializedName(value="name")
    public String name;
    @Expose
    @SerializedName(value="description")
    public String description;
    @Expose
    @SerializedName(value="branch-access")
    public BranchAccess branchAccess;
    @Expose
    @SerializedName(value="test-server-access")
    public TestServerAccess testServerAccess;
    @Expose
    @SerializedName(value="admin")
    public Boolean admin = false;

    public Role(String name, String description, BranchAccess branchAccess, TestServerAccess testServerAccess, boolean admin) {
        this.name = name;
        this.description = description;
        this.branchAccess = branchAccess;
        this.testServerAccess = testServerAccess;
        this.admin = admin;
    }

    public Role() {
    }

    public static Role valueOf(String name) {
        for (Field declaredField : Role.class.getDeclaredFields()) {
            if (declaredField.getType() != Role.class) continue;
            Role role = null;
            try {
                role = (Role)declaredField.get(null);
                if (!role.name.equalsIgnoreCase(name)) continue;
                return role;
            }
            catch (IllegalAccessException e2) {
                e2.printStackTrace();
            }
        }
        return null;
    }
}

