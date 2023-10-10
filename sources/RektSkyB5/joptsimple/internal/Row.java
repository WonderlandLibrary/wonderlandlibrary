/*
 * Decompiled with CFR 0.152.
 */
package joptsimple.internal;

class Row {
    final String option;
    final String description;

    Row(String option, String description) {
        this.option = option;
        this.description = description;
    }

    public boolean equals(Object that) {
        if (that == this) {
            return true;
        }
        if (that == null || !this.getClass().equals(that.getClass())) {
            return false;
        }
        Row other = (Row)that;
        return this.option.equals(other.option) && this.description.equals(other.description);
    }

    public int hashCode() {
        return this.option.hashCode() ^ this.description.hashCode();
    }
}

