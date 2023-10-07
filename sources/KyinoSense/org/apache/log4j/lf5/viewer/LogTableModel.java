/*
 * Decompiled with CFR 0.152.
 */
package org.apache.log4j.lf5.viewer;

import javax.swing.table.DefaultTableModel;

public class LogTableModel
extends DefaultTableModel {
    private static final long serialVersionUID = 3593300685868700894L;

    public LogTableModel(Object[] colNames, int numRows) {
        super(colNames, numRows);
    }

    public boolean isCellEditable(int row, int column) {
        return false;
    }
}

