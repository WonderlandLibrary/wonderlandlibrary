package koks.api.manager.cl;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */
public class User {

    final String name;
    final Role role;

    public User(String name) {
        this.name = name;
        this.role = getRole(name);
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    public Role getRole(String name) {
//        /*switch(name.toLowerCase()) { // this can be handled over our own server in the future
//            case "cokietv":
//            case "felixuwu":
//            case "hasenpfote":
//            case "vcryzeder2te":
//                return Role.FRIEND;
//        }*/
//        for (Role value : Role.values()) {
//            if(value.getId() == CLAPI.getClientRole()){
//                return value;
//            }
//        }
//        return Role.USER;
        switch(name.toLowerCase()) {
            case "haze":
                return Role.ADMIN;
            case "kroko":
            case "deleteboys":
            case "dirt":
            case "phantom":
            case "lca__modz":
                return Role.DEVELOPER;
            case "cokietv":
            case "felixuwu":
            case "hasenpfote":
            case "vcryzeder2te":
                return Role.FRIEND;
            default:
                return Role.USER;
        }
    }
}
