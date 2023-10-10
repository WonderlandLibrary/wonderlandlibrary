// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

public class QuaterniondInterpolator
{
    private final SvdDecomposition3d svdDecomposition3d;
    private final double[] m;
    private final Matrix3d u;
    private final Matrix3d v;
    
    public QuaterniondInterpolator() {
        this.svdDecomposition3d = new SvdDecomposition3d();
        this.m = new double[9];
        this.u = new Matrix3d();
        this.v = new Matrix3d();
    }
    
    public Quaterniond computeWeightedAverage(final Quaterniond[] qs, final double[] weights, final int maxSvdIterations, final Quaterniond dest) {
        double m00 = 0.0;
        double m2 = 0.0;
        double m3 = 0.0;
        double m4 = 0.0;
        double m5 = 0.0;
        double m6 = 0.0;
        double m7 = 0.0;
        double m8 = 0.0;
        double m9 = 0.0;
        for (int i = 0; i < qs.length; ++i) {
            final Quaterniond q = qs[i];
            final double dx = q.x + q.x;
            final double dy = q.y + q.y;
            final double dz = q.z + q.z;
            final double q2 = dx * q.x;
            final double q3 = dy * q.y;
            final double q4 = dz * q.z;
            final double q5 = dx * q.y;
            final double q6 = dx * q.z;
            final double q7 = dx * q.w;
            final double q8 = dy * q.z;
            final double q9 = dy * q.w;
            final double q10 = dz * q.w;
            m00 += weights[i] * (1.0 - q3 - q4);
            m2 += weights[i] * (q5 + q10);
            m3 += weights[i] * (q6 - q9);
            m4 += weights[i] * (q5 - q10);
            m5 += weights[i] * (1.0 - q4 - q2);
            m6 += weights[i] * (q8 + q7);
            m7 += weights[i] * (q6 + q9);
            m8 += weights[i] * (q8 - q7);
            m9 += weights[i] * (1.0 - q3 - q2);
        }
        this.m[0] = m00;
        this.m[1] = m2;
        this.m[2] = m3;
        this.m[3] = m4;
        this.m[4] = m5;
        this.m[5] = m6;
        this.m[6] = m7;
        this.m[7] = m8;
        this.m[8] = m9;
        this.svdDecomposition3d.svd(this.m, maxSvdIterations, this.u, this.v);
        this.u.mul(this.v.transpose());
        return dest.setFromNormalized(this.u).normalize();
    }
    
    private static class SvdDecomposition3d
    {
        private final double[] rv1;
        private final double[] w;
        private final double[] v;
        
        SvdDecomposition3d() {
            this.rv1 = new double[3];
            this.w = new double[3];
            this.v = new double[9];
        }
        
        private double SIGN(final double a, final double b) {
            return (b >= 0.0) ? Math.abs(a) : (-Math.abs(a));
        }
        
        void svd(final double[] a, final int maxIterations, final Matrix3d destU, final Matrix3d destV) {
            int l = 0;
            int nm = 0;
            double anorm = 0.0;
            double g = 0.0;
            double scale = 0.0;
            for (int i = 0; i < 3; ++i) {
                l = i + 1;
                this.rv1[i] = scale * g;
                double s = g = (scale = 0.0);
                for (int k = i; k < 3; ++k) {
                    scale += Math.abs(a[k + 3 * i]);
                }
                if (scale != 0.0) {
                    for (int k = i; k < 3; ++k) {
                        a[k + 3 * i] /= scale;
                        s += a[k + 3 * i] * a[k + 3 * i];
                    }
                    double f = a[i + 3 * i];
                    g = -this.SIGN(Math.sqrt(s), f);
                    final double h = f * g - s;
                    a[i + 3 * i] = f - g;
                    if (i != 2) {
                        for (int j = l; j < 3; ++j) {
                            s = 0.0;
                            for (int k = i; k < 3; ++k) {
                                s += a[k + 3 * i] * a[k + 3 * j];
                            }
                            f = s / h;
                            for (int k = i; k < 3; ++k) {
                                final int n = k + 3 * j;
                                a[n] += f * a[k + 3 * i];
                            }
                        }
                    }
                    for (int k = i; k < 3; ++k) {
                        a[k + 3 * i] *= scale;
                    }
                }
                this.w[i] = scale * g;
                s = (g = (scale = 0.0));
                if (i < 3 && i != 2) {
                    for (int k = l; k < 3; ++k) {
                        scale += Math.abs(a[i + 3 * k]);
                    }
                    if (scale != 0.0) {
                        for (int k = l; k < 3; ++k) {
                            a[i + 3 * k] /= scale;
                            s += a[i + 3 * k] * a[i + 3 * k];
                        }
                        final double f = a[i + 3 * l];
                        g = -this.SIGN(Math.sqrt(s), f);
                        final double h = f * g - s;
                        a[i + 3 * l] = f - g;
                        for (int k = l; k < 3; ++k) {
                            this.rv1[k] = a[i + 3 * k] / h;
                        }
                        if (i != 2) {
                            for (int j = l; j < 3; ++j) {
                                s = 0.0;
                                for (int k = l; k < 3; ++k) {
                                    s += a[j + 3 * k] * a[i + 3 * k];
                                }
                                for (int k = l; k < 3; ++k) {
                                    final int n2 = j + 3 * k;
                                    a[n2] += s * this.rv1[k];
                                }
                            }
                        }
                        for (int k = l; k < 3; ++k) {
                            a[i + 3 * k] *= scale;
                        }
                    }
                }
                anorm = Math.max(anorm, Math.abs(this.w[i]) + Math.abs(this.rv1[i]));
            }
            for (int i = 2; i >= 0; --i) {
                if (i < 2) {
                    if (g != 0.0) {
                        for (int j = l; j < 3; ++j) {
                            this.v[j + 3 * i] = a[i + 3 * j] / a[i + 3 * l] / g;
                        }
                        for (int j = l; j < 3; ++j) {
                            double s = 0.0;
                            for (int k = l; k < 3; ++k) {
                                s += a[i + 3 * k] * this.v[k + 3 * j];
                            }
                            for (int k = l; k < 3; ++k) {
                                final double[] v = this.v;
                                final int n3 = k + 3 * j;
                                v[n3] += s * this.v[k + 3 * i];
                            }
                        }
                    }
                    for (int j = l; j < 3; ++j) {
                        this.v[i + 3 * j] = (this.v[j + 3 * i] = 0.0);
                    }
                }
                this.v[i + 3 * i] = 1.0;
                g = this.rv1[i];
                l = i;
            }
            for (int i = 2; i >= 0; --i) {
                l = i + 1;
                g = this.w[i];
                if (i < 2) {
                    for (int j = l; j < 3; ++j) {
                        a[i + 3 * j] = 0.0;
                    }
                }
                if (g != 0.0) {
                    g = 1.0 / g;
                    if (i != 2) {
                        for (int j = l; j < 3; ++j) {
                            double s = 0.0;
                            for (int k = l; k < 3; ++k) {
                                s += a[k + 3 * i] * a[k + 3 * j];
                            }
                            final double f = s / a[i + 3 * i] * g;
                            for (int k = i; k < 3; ++k) {
                                final int n4 = k + 3 * j;
                                a[n4] += f * a[k + 3 * i];
                            }
                        }
                    }
                    for (int j = i; j < 3; ++j) {
                        a[j + 3 * i] *= g;
                    }
                }
                else {
                    for (int j = i; j < 3; ++j) {
                        a[j + 3 * i] = 0.0;
                    }
                }
                final int n5 = i + 3 * i;
                ++a[n5];
            }
            for (int k = 2; k >= 0; --k) {
                int its = 0;
                while (its < maxIterations) {
                    int flag = 1;
                    for (l = k; l >= 0; --l) {
                        nm = l - 1;
                        if (Math.abs(this.rv1[l]) + anorm == anorm) {
                            flag = 0;
                            break;
                        }
                        if (Math.abs(this.w[nm]) + anorm == anorm) {
                            break;
                        }
                    }
                    if (flag != 0) {
                        double c = 0.0;
                        double s = 1.0;
                        for (int i = l; i <= k; ++i) {
                            final double f = s * this.rv1[i];
                            if (Math.abs(f) + anorm != anorm) {
                                g = this.w[i];
                                double h = PYTHAG(f, g);
                                this.w[i] = h;
                                h = 1.0 / h;
                                c = g * h;
                                s = -f * h;
                                for (int j = 0; j < 3; ++j) {
                                    final double y = a[j + 3 * nm];
                                    final double z = a[j + 3 * i];
                                    a[j + 3 * nm] = y * c + z * s;
                                    a[j + 3 * i] = z * c - y * s;
                                }
                            }
                        }
                    }
                    double z = this.w[k];
                    if (l == k) {
                        if (z < 0.0) {
                            this.w[k] = -z;
                            for (int j = 0; j < 3; ++j) {
                                this.v[j + 3 * k] = -this.v[j + 3 * k];
                            }
                            break;
                        }
                        break;
                    }
                    else {
                        if (its == maxIterations - 1) {
                            throw new RuntimeException("No convergence after " + maxIterations + " iterations");
                        }
                        double x = this.w[l];
                        nm = k - 1;
                        double y = this.w[nm];
                        g = this.rv1[nm];
                        double h = this.rv1[k];
                        double f = ((y - z) * (y + z) + (g - h) * (g + h)) / (2.0 * h * y);
                        g = PYTHAG(f, 1.0);
                        f = ((x - z) * (x + z) + h * (y / (f + this.SIGN(g, f)) - h)) / x;
                        double c;
                        double s = c = 1.0;
                        for (int j = l; j <= nm; ++j) {
                            final int i = j + 1;
                            g = this.rv1[i];
                            y = this.w[i];
                            h = s * g;
                            g *= c;
                            z = PYTHAG(f, h);
                            this.rv1[j] = z;
                            c = f / z;
                            s = h / z;
                            f = x * c + g * s;
                            g = g * c - x * s;
                            h = y * s;
                            y *= c;
                            for (int jj = 0; jj < 3; ++jj) {
                                x = this.v[jj + 3 * j];
                                z = this.v[jj + 3 * i];
                                this.v[jj + 3 * j] = x * c + z * s;
                                this.v[jj + 3 * i] = z * c - x * s;
                            }
                            z = PYTHAG(f, h);
                            this.w[j] = z;
                            if (z != 0.0) {
                                z = 1.0 / z;
                                c = f * z;
                                s = h * z;
                            }
                            f = c * g + s * y;
                            x = c * y - s * g;
                            for (int jj = 0; jj < 3; ++jj) {
                                y = a[jj + 3 * j];
                                z = a[jj + 3 * i];
                                a[jj + 3 * j] = y * c + z * s;
                                a[jj + 3 * i] = z * c - y * s;
                            }
                        }
                        this.rv1[l] = 0.0;
                        this.rv1[k] = f;
                        this.w[k] = x;
                        ++its;
                    }
                }
            }
            destU.set(a);
            destV.set(this.v);
        }
        
        private static double PYTHAG(final double a, final double b) {
            final double at = Math.abs(a);
            final double bt = Math.abs(b);
            double result;
            if (at > bt) {
                final double ct = bt / at;
                result = at * Math.sqrt(1.0 + ct * ct);
            }
            else if (bt > 0.0) {
                final double ct = at / bt;
                result = bt * Math.sqrt(1.0 + ct * ct);
            }
            else {
                result = 0.0;
            }
            return result;
        }
    }
}
