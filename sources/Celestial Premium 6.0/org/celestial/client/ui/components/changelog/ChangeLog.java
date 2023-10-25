/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.components.changelog;

import org.celestial.client.ui.components.changelog.ChangeLogType;

public class ChangeLog {
    private final ChangeLogType type;
    private String changeName;

    public ChangeLog(String name, ChangeLogType type) {
        this.changeName = name;
        this.type = type;
        switch (type) {
            case CHANGED: {
                this.changeName = "    \u00a77[\u00a76!\u00a77] Changed \u00a7f" + this.changeName;
                break;
            }
            case ADD: {
                this.changeName = "    \u00a77[\u00a7a+\u00a77] Added \u00a7f" + this.changeName;
                break;
            }
            case RECODE: {
                this.changeName = "    \u00a77[\u00a79*\u00a77] Recoded \u00a7f" + this.changeName;
                break;
            }
            case IMPROVED: {
                this.changeName = "    \u00a77[\u00a7d/\u00a77] Improved \u00a7f" + this.changeName;
                break;
            }
            case DELETE: {
                this.changeName = "    \u00a77[\u00a7c-\u00a77] Deleted \u00a7f" + this.changeName;
                break;
            }
            case FIXED: {
                this.changeName = "    \u00a77[\u00a7b/\u00a77] Fixed \u00a7f" + this.changeName;
                break;
            }
            case MOVED: {
                this.changeName = "    \u00a77[\u00a79->\u00a77] Moved \u00a7f" + this.changeName;
                break;
            }
            case NONE: {
                this.changeName = " " + this.changeName;
            }
        }
    }

    public String getLogName() {
        return this.changeName;
    }

    public ChangeLogType getType() {
        return this.type;
    }
}

