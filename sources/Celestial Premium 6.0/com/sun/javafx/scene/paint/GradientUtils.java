/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.paint.Color
 *  javafx.scene.paint.Stop
 */
package com.sun.javafx.scene.paint;

import java.util.LinkedList;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;

public class GradientUtils {
    public static String lengthToString(double d, boolean bl) {
        if (bl) {
            return d * 100.0 + "%";
        }
        return d + "px";
    }

    public static class Parser {
        private int index;
        private String[] tokens;
        private boolean proportional;
        private boolean proportionalSet = false;

        private String[] splitString(String string, Delimiter delimiter, boolean bl) {
            LinkedList<String> linkedList = new LinkedList<String>();
            StringBuilder stringBuilder = new StringBuilder();
            char[] arrc = string.toCharArray();
            block0: for (int i = 0; i < arrc.length; ++i) {
                char c = arrc[i];
                if (delimiter.isDelimiter(c)) {
                    if (!bl || stringBuilder.length() > 0) {
                        linkedList.add(stringBuilder.toString());
                    }
                    stringBuilder.setLength(0);
                    continue;
                }
                if (c == '(') {
                    while (i < arrc.length) {
                        stringBuilder.append(arrc[i]);
                        if (arrc[i] == ')') continue block0;
                        ++i;
                    }
                    continue;
                }
                stringBuilder.append(arrc[i]);
            }
            if (!bl || stringBuilder.length() > 0) {
                linkedList.add(stringBuilder.toString());
            }
            return linkedList.toArray(new String[linkedList.size()]);
        }

        public Parser(String string) {
            this.tokens = this.splitString(string, c -> c == ',', false);
            this.index = 0;
        }

        public int getSize() {
            return this.tokens.length;
        }

        public void shift() {
            ++this.index;
        }

        public String getCurrentToken() {
            String string = this.tokens[this.index].trim();
            if (string.isEmpty()) {
                throw new IllegalArgumentException("Invalid gradient specification: found empty token.");
            }
            return string;
        }

        public String[] splitCurrentToken() {
            return this.getCurrentToken().split("\\s");
        }

        public static void checkNumberOfArguments(String[] arrstring, int n) {
            if (arrstring.length < n + 1) {
                throw new IllegalArgumentException("Invalid gradient specification: parameter '" + arrstring[0] + "' needs " + n + " argument(s).");
            }
        }

        public static double parseAngle(String string) {
            double d = 0.0;
            if (string.endsWith("deg")) {
                string = string.substring(0, string.length() - 3);
                d = Double.parseDouble(string);
            } else if (string.endsWith("grad")) {
                string = string.substring(0, string.length() - 4);
                d = Double.parseDouble(string);
                d = d * 9.0 / 10.0;
            } else if (string.endsWith("rad")) {
                string = string.substring(0, string.length() - 3);
                d = Double.parseDouble(string);
                d = d * 180.0 / Math.PI;
            } else if (string.endsWith("turn")) {
                string = string.substring(0, string.length() - 4);
                d = Double.parseDouble(string);
                d *= 360.0;
            } else {
                throw new IllegalArgumentException("Invalid gradient specification:angle must end in deg, rad, grad, or turn");
            }
            return d;
        }

        public static double parsePercentage(String string) {
            if (!string.endsWith("%")) {
                throw new IllegalArgumentException("Invalid gradient specification: focus-distance must be specified as percentage");
            }
            string = string.substring(0, string.length() - 1);
            double d = Double.parseDouble(string) / 100.0;
            return d;
        }

        public Point parsePoint(String string) {
            Point point = new Point();
            if (string.endsWith("%")) {
                point.proportional = true;
                string = string.substring(0, string.length() - 1);
            } else if (string.endsWith("px")) {
                string = string.substring(0, string.length() - 2);
            }
            point.value = Double.parseDouble(string);
            if (point.proportional) {
                point.value /= 100.0;
            }
            if (this.proportionalSet && this.proportional != point.proportional) {
                throw new IllegalArgumentException("Invalid gradient specification:cannot mix proportional and non-proportional values");
            }
            this.proportionalSet = true;
            this.proportional = point.proportional;
            return point;
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public Stop[] parseStops(boolean bl, double d) {
            int n;
            int n2;
            double d2;
            int n3 = this.tokens.length - this.index;
            Color[] arrcolor = new Color[n3];
            double[] arrd = new double[n3];
            Stop[] arrstop = new Stop[n3];
            for (int i = 0; i < n3; ++i) {
                String string = this.tokens[i + this.index].trim();
                String[] arrstring = this.splitString(string, c -> Character.isWhitespace(c), true);
                if (arrstring.length == 0) {
                    throw new IllegalArgumentException("Invalid gradient specification, empty stop found");
                }
                String string2 = arrstring[0];
                d2 = -1.0;
                Color color = Color.web((String)string2);
                if (arrstring.length == 2) {
                    String string3 = arrstring[1];
                    if (string3.endsWith("%")) {
                        string3 = string3.substring(0, string3.length() - 1);
                        d2 = Double.parseDouble(string3) / 100.0;
                    } else {
                        if (bl) throw new IllegalArgumentException("Invalid gradient specification, non-proportional stops not permited in proportional gradient: " + string3);
                        if (string3.endsWith("px")) {
                            string3 = string3.substring(0, string3.length() - 2);
                        }
                        d2 = Double.parseDouble(string3) / d;
                    }
                } else if (arrstring.length > 2) {
                    throw new IllegalArgumentException("Invalid gradient specification, unexpected content in stop specification: " + arrstring[2]);
                }
                arrcolor[i] = color;
                arrd[i] = d2;
            }
            if (arrd[0] < 0.0) {
                arrd[0] = 0.0;
            }
            if (arrd[arrd.length - 1] < 0.0) {
                arrd[arrd.length - 1] = 1.0;
            }
            double d3 = arrd[0];
            for (n2 = 1; n2 < arrd.length; ++n2) {
                if (arrd[n2] < d3 && arrd[n2] > 0.0) {
                    arrd[n2] = d3;
                    continue;
                }
                d3 = arrd[n2];
            }
            n2 = -1;
            for (n = 1; n < arrd.length; ++n) {
                d2 = arrd[n];
                if (d2 < 0.0 && n2 < 0) {
                    n2 = n;
                    continue;
                }
                if (!(d2 >= 0.0) || n2 <= 0) continue;
                int n4 = n - n2 + 1;
                double d4 = (arrd[n] - arrd[n2 - 1]) / (double)n4;
                for (int i = 0; i < n4 - 1; ++i) {
                    arrd[n2 + i] = arrd[n2 - 1] + d4 * (double)(i + 1);
                }
            }
            for (n = 0; n < arrstop.length; ++n) {
                Stop stop;
                arrstop[n] = stop = new Stop(arrd[n], arrcolor[n]);
            }
            return arrstop;
        }

        private static interface Delimiter {
            public boolean isDelimiter(char var1);
        }
    }

    public static class Point {
        public static final Point MIN = new Point(0.0, true);
        public static final Point MAX = new Point(1.0, true);
        public double value;
        public boolean proportional;

        public String toString() {
            return "value = " + this.value + ", proportional = " + this.proportional;
        }

        public Point(double d, boolean bl) {
            this.value = d;
            this.proportional = bl;
        }

        public Point() {
        }
    }
}

