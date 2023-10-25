package javax.vecmath;

import java.io.Serializable;

public class Matrix3f implements Serializable, Cloneable {
  static final long serialVersionUID = 329697160112089834L;
  
  public float m00;
  
  public float m01;
  
  public float m02;
  
  public float m10;
  
  public float m11;
  
  public float m12;
  
  public float m20;
  
  public float m21;
  
  public float m22;
  
  private static final double EPS = 1.0E-8D;
  
  public Matrix3f(float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21, float m22) {
    this.m00 = m00;
    this.m01 = m01;
    this.m02 = m02;
    this.m10 = m10;
    this.m11 = m11;
    this.m12 = m12;
    this.m20 = m20;
    this.m21 = m21;
    this.m22 = m22;
  }
  
  public Matrix3f(float[] v) {
    this.m00 = v[0];
    this.m01 = v[1];
    this.m02 = v[2];
    this.m10 = v[3];
    this.m11 = v[4];
    this.m12 = v[5];
    this.m20 = v[6];
    this.m21 = v[7];
    this.m22 = v[8];
  }
  
  public Matrix3f(Matrix3d m1) {
    this.m00 = (float)m1.m00;
    this.m01 = (float)m1.m01;
    this.m02 = (float)m1.m02;
    this.m10 = (float)m1.m10;
    this.m11 = (float)m1.m11;
    this.m12 = (float)m1.m12;
    this.m20 = (float)m1.m20;
    this.m21 = (float)m1.m21;
    this.m22 = (float)m1.m22;
  }
  
  public Matrix3f(Matrix3f m1) {
    this.m00 = m1.m00;
    this.m01 = m1.m01;
    this.m02 = m1.m02;
    this.m10 = m1.m10;
    this.m11 = m1.m11;
    this.m12 = m1.m12;
    this.m20 = m1.m20;
    this.m21 = m1.m21;
    this.m22 = m1.m22;
  }
  
  public Matrix3f() {
    this.m00 = 0.0F;
    this.m01 = 0.0F;
    this.m02 = 0.0F;
    this.m10 = 0.0F;
    this.m11 = 0.0F;
    this.m12 = 0.0F;
    this.m20 = 0.0F;
    this.m21 = 0.0F;
    this.m22 = 0.0F;
  }
  
  public String toString() {
    return this.m00 + ", " + this.m01 + ", " + this.m02 + "\n" + this.m10 + ", " + this.m11 + ", " + this.m12 + "\n" + this.m20 + ", " + this.m21 + ", " + this.m22 + "\n";
  }
  
  public final void setIdentity() {
    this.m00 = 1.0F;
    this.m01 = 0.0F;
    this.m02 = 0.0F;
    this.m10 = 0.0F;
    this.m11 = 1.0F;
    this.m12 = 0.0F;
    this.m20 = 0.0F;
    this.m21 = 0.0F;
    this.m22 = 1.0F;
  }
  
  public final void setScale(float scale) {
    double[] tmp_rot = new double[9];
    double[] tmp_scale = new double[3];
    getScaleRotate(tmp_scale, tmp_rot);
    this.m00 = (float)(tmp_rot[0] * scale);
    this.m01 = (float)(tmp_rot[1] * scale);
    this.m02 = (float)(tmp_rot[2] * scale);
    this.m10 = (float)(tmp_rot[3] * scale);
    this.m11 = (float)(tmp_rot[4] * scale);
    this.m12 = (float)(tmp_rot[5] * scale);
    this.m20 = (float)(tmp_rot[6] * scale);
    this.m21 = (float)(tmp_rot[7] * scale);
    this.m22 = (float)(tmp_rot[8] * scale);
  }
  
  public final void setElement(int row, int column, float value) {
    switch (row) {
      case 0:
        switch (column) {
          case 0:
            this.m00 = value;
            return;
          case 1:
            this.m01 = value;
            return;
          case 2:
            this.m02 = value;
            return;
        } 
        throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f0"));
      case 1:
        switch (column) {
          case 0:
            this.m10 = value;
            return;
          case 1:
            this.m11 = value;
            return;
          case 2:
            this.m12 = value;
            return;
        } 
        throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f0"));
      case 2:
        switch (column) {
          case 0:
            this.m20 = value;
            return;
          case 1:
            this.m21 = value;
            return;
          case 2:
            this.m22 = value;
            return;
        } 
        throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f0"));
    } 
    throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f0"));
  }
  
  public final void getRow(int row, Vector3f v) {
    if (row == 0) {
      v.x = this.m00;
      v.y = this.m01;
      v.z = this.m02;
    } else if (row == 1) {
      v.x = this.m10;
      v.y = this.m11;
      v.z = this.m12;
    } else if (row == 2) {
      v.x = this.m20;
      v.y = this.m21;
      v.z = this.m22;
    } else {
      throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f1"));
    } 
  }
  
  public final void getRow(int row, float[] v) {
    if (row == 0) {
      v[0] = this.m00;
      v[1] = this.m01;
      v[2] = this.m02;
    } else if (row == 1) {
      v[0] = this.m10;
      v[1] = this.m11;
      v[2] = this.m12;
    } else if (row == 2) {
      v[0] = this.m20;
      v[1] = this.m21;
      v[2] = this.m22;
    } else {
      throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f1"));
    } 
  }
  
  public final void getColumn(int column, Vector3f v) {
    if (column == 0) {
      v.x = this.m00;
      v.y = this.m10;
      v.z = this.m20;
    } else if (column == 1) {
      v.x = this.m01;
      v.y = this.m11;
      v.z = this.m21;
    } else if (column == 2) {
      v.x = this.m02;
      v.y = this.m12;
      v.z = this.m22;
    } else {
      throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f3"));
    } 
  }
  
  public final void getColumn(int column, float[] v) {
    if (column == 0) {
      v[0] = this.m00;
      v[1] = this.m10;
      v[2] = this.m20;
    } else if (column == 1) {
      v[0] = this.m01;
      v[1] = this.m11;
      v[2] = this.m21;
    } else if (column == 2) {
      v[0] = this.m02;
      v[1] = this.m12;
      v[2] = this.m22;
    } else {
      throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f3"));
    } 
  }
  
  public final float getElement(int row, int column) {
    switch (row) {
      case 0:
        switch (column) {
          case 0:
            return this.m00;
          case 1:
            return this.m01;
          case 2:
            return this.m02;
        } 
        break;
      case 1:
        switch (column) {
          case 0:
            return this.m10;
          case 1:
            return this.m11;
          case 2:
            return this.m12;
        } 
        break;
      case 2:
        switch (column) {
          case 0:
            return this.m20;
          case 1:
            return this.m21;
          case 2:
            return this.m22;
        } 
        break;
    } 
    throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f5"));
  }
  
  public final void setRow(int row, float x, float y, float z) {
    switch (row) {
      case 0:
        this.m00 = x;
        this.m01 = y;
        this.m02 = z;
        return;
      case 1:
        this.m10 = x;
        this.m11 = y;
        this.m12 = z;
        return;
      case 2:
        this.m20 = x;
        this.m21 = y;
        this.m22 = z;
        return;
    } 
    throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f6"));
  }
  
  public final void setRow(int row, Vector3f v) {
    switch (row) {
      case 0:
        this.m00 = v.x;
        this.m01 = v.y;
        this.m02 = v.z;
        return;
      case 1:
        this.m10 = v.x;
        this.m11 = v.y;
        this.m12 = v.z;
        return;
      case 2:
        this.m20 = v.x;
        this.m21 = v.y;
        this.m22 = v.z;
        return;
    } 
    throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f6"));
  }
  
  public final void setRow(int row, float[] v) {
    switch (row) {
      case 0:
        this.m00 = v[0];
        this.m01 = v[1];
        this.m02 = v[2];
        return;
      case 1:
        this.m10 = v[0];
        this.m11 = v[1];
        this.m12 = v[2];
        return;
      case 2:
        this.m20 = v[0];
        this.m21 = v[1];
        this.m22 = v[2];
        return;
    } 
    throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f6"));
  }
  
  public final void setColumn(int column, float x, float y, float z) {
    switch (column) {
      case 0:
        this.m00 = x;
        this.m10 = y;
        this.m20 = z;
        return;
      case 1:
        this.m01 = x;
        this.m11 = y;
        this.m21 = z;
        return;
      case 2:
        this.m02 = x;
        this.m12 = y;
        this.m22 = z;
        return;
    } 
    throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f9"));
  }
  
  public final void setColumn(int column, Vector3f v) {
    switch (column) {
      case 0:
        this.m00 = v.x;
        this.m10 = v.y;
        this.m20 = v.z;
        return;
      case 1:
        this.m01 = v.x;
        this.m11 = v.y;
        this.m21 = v.z;
        return;
      case 2:
        this.m02 = v.x;
        this.m12 = v.y;
        this.m22 = v.z;
        return;
    } 
    throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f9"));
  }
  
  public final void setColumn(int column, float[] v) {
    switch (column) {
      case 0:
        this.m00 = v[0];
        this.m10 = v[1];
        this.m20 = v[2];
        return;
      case 1:
        this.m01 = v[0];
        this.m11 = v[1];
        this.m21 = v[2];
        return;
      case 2:
        this.m02 = v[0];
        this.m12 = v[1];
        this.m22 = v[2];
        return;
    } 
    throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f9"));
  }
  
  public final float getScale() {
    double[] tmp_rot = new double[9];
    double[] tmp_scale = new double[3];
    getScaleRotate(tmp_scale, tmp_rot);
    return (float)Matrix3d.max3(tmp_scale);
  }
  
  public final void add(float scalar) {
    this.m00 += scalar;
    this.m01 += scalar;
    this.m02 += scalar;
    this.m10 += scalar;
    this.m11 += scalar;
    this.m12 += scalar;
    this.m20 += scalar;
    this.m21 += scalar;
    this.m22 += scalar;
  }
  
  public final void add(float scalar, Matrix3f m1) {
    m1.m00 += scalar;
    m1.m01 += scalar;
    m1.m02 += scalar;
    m1.m10 += scalar;
    m1.m11 += scalar;
    m1.m12 += scalar;
    m1.m20 += scalar;
    m1.m21 += scalar;
    m1.m22 += scalar;
  }
  
  public final void add(Matrix3f m1, Matrix3f m2) {
    m1.m00 += m2.m00;
    m1.m01 += m2.m01;
    m1.m02 += m2.m02;
    m1.m10 += m2.m10;
    m1.m11 += m2.m11;
    m1.m12 += m2.m12;
    m1.m20 += m2.m20;
    m1.m21 += m2.m21;
    m1.m22 += m2.m22;
  }
  
  public final void add(Matrix3f m1) {
    this.m00 += m1.m00;
    this.m01 += m1.m01;
    this.m02 += m1.m02;
    this.m10 += m1.m10;
    this.m11 += m1.m11;
    this.m12 += m1.m12;
    this.m20 += m1.m20;
    this.m21 += m1.m21;
    this.m22 += m1.m22;
  }
  
  public final void sub(Matrix3f m1, Matrix3f m2) {
    m1.m00 -= m2.m00;
    m1.m01 -= m2.m01;
    m1.m02 -= m2.m02;
    m1.m10 -= m2.m10;
    m1.m11 -= m2.m11;
    m1.m12 -= m2.m12;
    m1.m20 -= m2.m20;
    m1.m21 -= m2.m21;
    m1.m22 -= m2.m22;
  }
  
  public final void sub(Matrix3f m1) {
    this.m00 -= m1.m00;
    this.m01 -= m1.m01;
    this.m02 -= m1.m02;
    this.m10 -= m1.m10;
    this.m11 -= m1.m11;
    this.m12 -= m1.m12;
    this.m20 -= m1.m20;
    this.m21 -= m1.m21;
    this.m22 -= m1.m22;
  }
  
  public final void transpose() {
    float temp = this.m10;
    this.m10 = this.m01;
    this.m01 = temp;
    temp = this.m20;
    this.m20 = this.m02;
    this.m02 = temp;
    temp = this.m21;
    this.m21 = this.m12;
    this.m12 = temp;
  }
  
  public final void transpose(Matrix3f m1) {
    if (this != m1) {
      this.m00 = m1.m00;
      this.m01 = m1.m10;
      this.m02 = m1.m20;
      this.m10 = m1.m01;
      this.m11 = m1.m11;
      this.m12 = m1.m21;
      this.m20 = m1.m02;
      this.m21 = m1.m12;
      this.m22 = m1.m22;
    } else {
      transpose();
    } 
  }
  
  public final void set(Quat4f q1) {
    this.m00 = 1.0F - 2.0F * q1.y * q1.y - 2.0F * q1.z * q1.z;
    this.m10 = 2.0F * (q1.x * q1.y + q1.w * q1.z);
    this.m20 = 2.0F * (q1.x * q1.z - q1.w * q1.y);
    this.m01 = 2.0F * (q1.x * q1.y - q1.w * q1.z);
    this.m11 = 1.0F - 2.0F * q1.x * q1.x - 2.0F * q1.z * q1.z;
    this.m21 = 2.0F * (q1.y * q1.z + q1.w * q1.x);
    this.m02 = 2.0F * (q1.x * q1.z + q1.w * q1.y);
    this.m12 = 2.0F * (q1.y * q1.z - q1.w * q1.x);
    this.m22 = 1.0F - 2.0F * q1.x * q1.x - 2.0F * q1.y * q1.y;
  }
  
  public final void set(AxisAngle4f a1) {
    float mag = (float)Math.sqrt((a1.x * a1.x + a1.y * a1.y + a1.z * a1.z));
    if (mag < 1.0E-8D) {
      this.m00 = 1.0F;
      this.m01 = 0.0F;
      this.m02 = 0.0F;
      this.m10 = 0.0F;
      this.m11 = 1.0F;
      this.m12 = 0.0F;
      this.m20 = 0.0F;
      this.m21 = 0.0F;
      this.m22 = 1.0F;
    } else {
      mag = 1.0F / mag;
      float ax = a1.x * mag;
      float ay = a1.y * mag;
      float az = a1.z * mag;
      float sinTheta = (float)Math.sin(a1.angle);
      float cosTheta = (float)Math.cos(a1.angle);
      float t = 1.0F - cosTheta;
      float xz = ax * az;
      float xy = ax * ay;
      float yz = ay * az;
      this.m00 = t * ax * ax + cosTheta;
      this.m01 = t * xy - sinTheta * az;
      this.m02 = t * xz + sinTheta * ay;
      this.m10 = t * xy + sinTheta * az;
      this.m11 = t * ay * ay + cosTheta;
      this.m12 = t * yz - sinTheta * ax;
      this.m20 = t * xz - sinTheta * ay;
      this.m21 = t * yz + sinTheta * ax;
      this.m22 = t * az * az + cosTheta;
    } 
  }
  
  public final void set(AxisAngle4d a1) {
    double mag = Math.sqrt(a1.x * a1.x + a1.y * a1.y + a1.z * a1.z);
    if (mag < 1.0E-8D) {
      this.m00 = 1.0F;
      this.m01 = 0.0F;
      this.m02 = 0.0F;
      this.m10 = 0.0F;
      this.m11 = 1.0F;
      this.m12 = 0.0F;
      this.m20 = 0.0F;
      this.m21 = 0.0F;
      this.m22 = 1.0F;
    } else {
      mag = 1.0D / mag;
      double ax = a1.x * mag;
      double ay = a1.y * mag;
      double az = a1.z * mag;
      double sinTheta = Math.sin(a1.angle);
      double cosTheta = Math.cos(a1.angle);
      double t = 1.0D - cosTheta;
      double xz = ax * az;
      double xy = ax * ay;
      double yz = ay * az;
      this.m00 = (float)(t * ax * ax + cosTheta);
      this.m01 = (float)(t * xy - sinTheta * az);
      this.m02 = (float)(t * xz + sinTheta * ay);
      this.m10 = (float)(t * xy + sinTheta * az);
      this.m11 = (float)(t * ay * ay + cosTheta);
      this.m12 = (float)(t * yz - sinTheta * ax);
      this.m20 = (float)(t * xz - sinTheta * ay);
      this.m21 = (float)(t * yz + sinTheta * ax);
      this.m22 = (float)(t * az * az + cosTheta);
    } 
  }
  
  public final void set(Quat4d q1) {
    this.m00 = (float)(1.0D - 2.0D * q1.y * q1.y - 2.0D * q1.z * q1.z);
    this.m10 = (float)(2.0D * (q1.x * q1.y + q1.w * q1.z));
    this.m20 = (float)(2.0D * (q1.x * q1.z - q1.w * q1.y));
    this.m01 = (float)(2.0D * (q1.x * q1.y - q1.w * q1.z));
    this.m11 = (float)(1.0D - 2.0D * q1.x * q1.x - 2.0D * q1.z * q1.z);
    this.m21 = (float)(2.0D * (q1.y * q1.z + q1.w * q1.x));
    this.m02 = (float)(2.0D * (q1.x * q1.z + q1.w * q1.y));
    this.m12 = (float)(2.0D * (q1.y * q1.z - q1.w * q1.x));
    this.m22 = (float)(1.0D - 2.0D * q1.x * q1.x - 2.0D * q1.y * q1.y);
  }
  
  public final void set(float[] m) {
    this.m00 = m[0];
    this.m01 = m[1];
    this.m02 = m[2];
    this.m10 = m[3];
    this.m11 = m[4];
    this.m12 = m[5];
    this.m20 = m[6];
    this.m21 = m[7];
    this.m22 = m[8];
  }
  
  public final void set(Matrix3f m1) {
    this.m00 = m1.m00;
    this.m01 = m1.m01;
    this.m02 = m1.m02;
    this.m10 = m1.m10;
    this.m11 = m1.m11;
    this.m12 = m1.m12;
    this.m20 = m1.m20;
    this.m21 = m1.m21;
    this.m22 = m1.m22;
  }
  
  public final void set(Matrix3d m1) {
    this.m00 = (float)m1.m00;
    this.m01 = (float)m1.m01;
    this.m02 = (float)m1.m02;
    this.m10 = (float)m1.m10;
    this.m11 = (float)m1.m11;
    this.m12 = (float)m1.m12;
    this.m20 = (float)m1.m20;
    this.m21 = (float)m1.m21;
    this.m22 = (float)m1.m22;
  }
  
  public final void invert(Matrix3f m1) {
    invertGeneral(m1);
  }
  
  public final void invert() {
    invertGeneral(this);
  }
  
  private final void invertGeneral(Matrix3f m1) {
    double[] temp = new double[9];
    double[] result = new double[9];
    int[] row_perm = new int[3];
    temp[0] = m1.m00;
    temp[1] = m1.m01;
    temp[2] = m1.m02;
    temp[3] = m1.m10;
    temp[4] = m1.m11;
    temp[5] = m1.m12;
    temp[6] = m1.m20;
    temp[7] = m1.m21;
    temp[8] = m1.m22;
    if (!luDecomposition(temp, row_perm))
      throw new SingularMatrixException(VecMathI18N.getString("Matrix3f12")); 
    for (int i = 0; i < 9; ) {
      result[i] = 0.0D;
      i++;
    } 
    result[0] = 1.0D;
    result[4] = 1.0D;
    result[8] = 1.0D;
    luBacksubstitution(temp, row_perm, result);
    this.m00 = (float)result[0];
    this.m01 = (float)result[1];
    this.m02 = (float)result[2];
    this.m10 = (float)result[3];
    this.m11 = (float)result[4];
    this.m12 = (float)result[5];
    this.m20 = (float)result[6];
    this.m21 = (float)result[7];
    this.m22 = (float)result[8];
  }
  
  static boolean luDecomposition(double[] matrix0, int[] row_perm) {
    double[] row_scale = new double[3];
    int ptr = 0;
    int rs = 0;
    int i = 3;
    while (i-- != 0) {
      double big = 0.0D;
      int k = 3;
      while (k-- != 0) {
        double temp = matrix0[ptr++];
        temp = Math.abs(temp);
        if (temp > big)
          big = temp; 
      } 
      if (big == 0.0D)
        return false; 
      row_scale[rs++] = 1.0D / big;
    } 
    int mtx = 0;
    for (int j = 0; j < 3; j++) {
      int k;
      for (k = 0; k < j; k++) {
        int target = mtx + 3 * k + j;
        double sum = matrix0[target];
        int m = k;
        int p1 = mtx + 3 * k;
        int p2 = mtx + j;
        while (m-- != 0) {
          sum -= matrix0[p1] * matrix0[p2];
          p1++;
          p2 += 3;
        } 
        matrix0[target] = sum;
      } 
      double big = 0.0D;
      int imax = -1;
      for (k = j; k < 3; k++) {
        int target = mtx + 3 * k + j;
        double sum = matrix0[target];
        int m = j;
        int p1 = mtx + 3 * k;
        int p2 = mtx + j;
        while (m-- != 0) {
          sum -= matrix0[p1] * matrix0[p2];
          p1++;
          p2 += 3;
        } 
        matrix0[target] = sum;
        double temp;
        if ((temp = row_scale[k] * Math.abs(sum)) >= big) {
          big = temp;
          imax = k;
        } 
      } 
      if (imax < 0)
        throw new RuntimeException(VecMathI18N.getString("Matrix3f13")); 
      if (j != imax) {
        int m = 3;
        int p1 = mtx + 3 * imax;
        int p2 = mtx + 3 * j;
        while (m-- != 0) {
          double temp = matrix0[p1];
          matrix0[p1++] = matrix0[p2];
          matrix0[p2++] = temp;
        } 
        row_scale[imax] = row_scale[j];
      } 
      row_perm[j] = imax;
      if (matrix0[mtx + 3 * j + j] == 0.0D)
        return false; 
      if (j != 2) {
        double temp = 1.0D / matrix0[mtx + 3 * j + j];
        int target = mtx + 3 * (j + 1) + j;
        k = 2 - j;
        while (k-- != 0) {
          matrix0[target] = matrix0[target] * temp;
          target += 3;
        } 
      } 
    } 
    return true;
  }
  
  static void luBacksubstitution(double[] matrix1, int[] row_perm, double[] matrix2) {
    int rp = 0;
    for (int k = 0; k < 3; k++) {
      int cv = k;
      int ii = -1;
      for (int i = 0; i < 3; i++) {
        int ip = row_perm[rp + i];
        double sum = matrix2[cv + 3 * ip];
        matrix2[cv + 3 * ip] = matrix2[cv + 3 * i];
        if (ii >= 0) {
          int m = i * 3;
          for (int j = ii; j <= i - 1; j++)
            sum -= matrix1[m + j] * matrix2[cv + 3 * j]; 
        } else if (sum != 0.0D) {
          ii = i;
        } 
        matrix2[cv + 3 * i] = sum;
      } 
      int rv = 6;
      matrix2[cv + 6] = matrix2[cv + 6] / matrix1[rv + 2];
      rv -= 3;
      matrix2[cv + 3] = (matrix2[cv + 3] - matrix1[rv + 2] * matrix2[cv + 6]) / matrix1[rv + 1];
      rv -= 3;
      matrix2[cv + 0] = (matrix2[cv + 0] - matrix1[rv + 1] * matrix2[cv + 3] - matrix1[rv + 2] * matrix2[cv + 6]) / matrix1[rv + 0];
    } 
  }
  
  public final float determinant() {
    float total = this.m00 * (this.m11 * this.m22 - this.m12 * this.m21) + this.m01 * (this.m12 * this.m20 - this.m10 * this.m22) + this.m02 * (this.m10 * this.m21 - this.m11 * this.m20);
    return total;
  }
  
  public final void set(float scale) {
    this.m00 = scale;
    this.m01 = 0.0F;
    this.m02 = 0.0F;
    this.m10 = 0.0F;
    this.m11 = scale;
    this.m12 = 0.0F;
    this.m20 = 0.0F;
    this.m21 = 0.0F;
    this.m22 = scale;
  }
  
  public final void rotX(float angle) {
    float sinAngle = (float)Math.sin(angle);
    float cosAngle = (float)Math.cos(angle);
    this.m00 = 1.0F;
    this.m01 = 0.0F;
    this.m02 = 0.0F;
    this.m10 = 0.0F;
    this.m11 = cosAngle;
    this.m12 = -sinAngle;
    this.m20 = 0.0F;
    this.m21 = sinAngle;
    this.m22 = cosAngle;
  }
  
  public final void rotY(float angle) {
    float sinAngle = (float)Math.sin(angle);
    float cosAngle = (float)Math.cos(angle);
    this.m00 = cosAngle;
    this.m01 = 0.0F;
    this.m02 = sinAngle;
    this.m10 = 0.0F;
    this.m11 = 1.0F;
    this.m12 = 0.0F;
    this.m20 = -sinAngle;
    this.m21 = 0.0F;
    this.m22 = cosAngle;
  }
  
  public final void rotZ(float angle) {
    float sinAngle = (float)Math.sin(angle);
    float cosAngle = (float)Math.cos(angle);
    this.m00 = cosAngle;
    this.m01 = -sinAngle;
    this.m02 = 0.0F;
    this.m10 = sinAngle;
    this.m11 = cosAngle;
    this.m12 = 0.0F;
    this.m20 = 0.0F;
    this.m21 = 0.0F;
    this.m22 = 1.0F;
  }
  
  public final void mul(float scalar) {
    this.m00 *= scalar;
    this.m01 *= scalar;
    this.m02 *= scalar;
    this.m10 *= scalar;
    this.m11 *= scalar;
    this.m12 *= scalar;
    this.m20 *= scalar;
    this.m21 *= scalar;
    this.m22 *= scalar;
  }
  
  public final void mul(float scalar, Matrix3f m1) {
    this.m00 = scalar * m1.m00;
    this.m01 = scalar * m1.m01;
    this.m02 = scalar * m1.m02;
    this.m10 = scalar * m1.m10;
    this.m11 = scalar * m1.m11;
    this.m12 = scalar * m1.m12;
    this.m20 = scalar * m1.m20;
    this.m21 = scalar * m1.m21;
    this.m22 = scalar * m1.m22;
  }
  
  public final void mul(Matrix3f m1) {
    float m00 = this.m00 * m1.m00 + this.m01 * m1.m10 + this.m02 * m1.m20;
    float m01 = this.m00 * m1.m01 + this.m01 * m1.m11 + this.m02 * m1.m21;
    float m02 = this.m00 * m1.m02 + this.m01 * m1.m12 + this.m02 * m1.m22;
    float m10 = this.m10 * m1.m00 + this.m11 * m1.m10 + this.m12 * m1.m20;
    float m11 = this.m10 * m1.m01 + this.m11 * m1.m11 + this.m12 * m1.m21;
    float m12 = this.m10 * m1.m02 + this.m11 * m1.m12 + this.m12 * m1.m22;
    float m20 = this.m20 * m1.m00 + this.m21 * m1.m10 + this.m22 * m1.m20;
    float m21 = this.m20 * m1.m01 + this.m21 * m1.m11 + this.m22 * m1.m21;
    float m22 = this.m20 * m1.m02 + this.m21 * m1.m12 + this.m22 * m1.m22;
    this.m00 = m00;
    this.m01 = m01;
    this.m02 = m02;
    this.m10 = m10;
    this.m11 = m11;
    this.m12 = m12;
    this.m20 = m20;
    this.m21 = m21;
    this.m22 = m22;
  }
  
  public final void mul(Matrix3f m1, Matrix3f m2) {
    if (this != m1 && this != m2) {
      this.m00 = m1.m00 * m2.m00 + m1.m01 * m2.m10 + m1.m02 * m2.m20;
      this.m01 = m1.m00 * m2.m01 + m1.m01 * m2.m11 + m1.m02 * m2.m21;
      this.m02 = m1.m00 * m2.m02 + m1.m01 * m2.m12 + m1.m02 * m2.m22;
      this.m10 = m1.m10 * m2.m00 + m1.m11 * m2.m10 + m1.m12 * m2.m20;
      this.m11 = m1.m10 * m2.m01 + m1.m11 * m2.m11 + m1.m12 * m2.m21;
      this.m12 = m1.m10 * m2.m02 + m1.m11 * m2.m12 + m1.m12 * m2.m22;
      this.m20 = m1.m20 * m2.m00 + m1.m21 * m2.m10 + m1.m22 * m2.m20;
      this.m21 = m1.m20 * m2.m01 + m1.m21 * m2.m11 + m1.m22 * m2.m21;
      this.m22 = m1.m20 * m2.m02 + m1.m21 * m2.m12 + m1.m22 * m2.m22;
    } else {
      float m00 = m1.m00 * m2.m00 + m1.m01 * m2.m10 + m1.m02 * m2.m20;
      float m01 = m1.m00 * m2.m01 + m1.m01 * m2.m11 + m1.m02 * m2.m21;
      float m02 = m1.m00 * m2.m02 + m1.m01 * m2.m12 + m1.m02 * m2.m22;
      float m10 = m1.m10 * m2.m00 + m1.m11 * m2.m10 + m1.m12 * m2.m20;
      float m11 = m1.m10 * m2.m01 + m1.m11 * m2.m11 + m1.m12 * m2.m21;
      float m12 = m1.m10 * m2.m02 + m1.m11 * m2.m12 + m1.m12 * m2.m22;
      float m20 = m1.m20 * m2.m00 + m1.m21 * m2.m10 + m1.m22 * m2.m20;
      float m21 = m1.m20 * m2.m01 + m1.m21 * m2.m11 + m1.m22 * m2.m21;
      float m22 = m1.m20 * m2.m02 + m1.m21 * m2.m12 + m1.m22 * m2.m22;
      this.m00 = m00;
      this.m01 = m01;
      this.m02 = m02;
      this.m10 = m10;
      this.m11 = m11;
      this.m12 = m12;
      this.m20 = m20;
      this.m21 = m21;
      this.m22 = m22;
    } 
  }
  
  public final void mulNormalize(Matrix3f m1) {
    double[] tmp = new double[9];
    double[] tmp_rot = new double[9];
    double[] tmp_scale = new double[3];
    tmp[0] = (this.m00 * m1.m00 + this.m01 * m1.m10 + this.m02 * m1.m20);
    tmp[1] = (this.m00 * m1.m01 + this.m01 * m1.m11 + this.m02 * m1.m21);
    tmp[2] = (this.m00 * m1.m02 + this.m01 * m1.m12 + this.m02 * m1.m22);
    tmp[3] = (this.m10 * m1.m00 + this.m11 * m1.m10 + this.m12 * m1.m20);
    tmp[4] = (this.m10 * m1.m01 + this.m11 * m1.m11 + this.m12 * m1.m21);
    tmp[5] = (this.m10 * m1.m02 + this.m11 * m1.m12 + this.m12 * m1.m22);
    tmp[6] = (this.m20 * m1.m00 + this.m21 * m1.m10 + this.m22 * m1.m20);
    tmp[7] = (this.m20 * m1.m01 + this.m21 * m1.m11 + this.m22 * m1.m21);
    tmp[8] = (this.m20 * m1.m02 + this.m21 * m1.m12 + this.m22 * m1.m22);
    Matrix3d.compute_svd(tmp, tmp_scale, tmp_rot);
    this.m00 = (float)tmp_rot[0];
    this.m01 = (float)tmp_rot[1];
    this.m02 = (float)tmp_rot[2];
    this.m10 = (float)tmp_rot[3];
    this.m11 = (float)tmp_rot[4];
    this.m12 = (float)tmp_rot[5];
    this.m20 = (float)tmp_rot[6];
    this.m21 = (float)tmp_rot[7];
    this.m22 = (float)tmp_rot[8];
  }
  
  public final void mulNormalize(Matrix3f m1, Matrix3f m2) {
    double[] tmp = new double[9];
    double[] tmp_rot = new double[9];
    double[] tmp_scale = new double[3];
    tmp[0] = (m1.m00 * m2.m00 + m1.m01 * m2.m10 + m1.m02 * m2.m20);
    tmp[1] = (m1.m00 * m2.m01 + m1.m01 * m2.m11 + m1.m02 * m2.m21);
    tmp[2] = (m1.m00 * m2.m02 + m1.m01 * m2.m12 + m1.m02 * m2.m22);
    tmp[3] = (m1.m10 * m2.m00 + m1.m11 * m2.m10 + m1.m12 * m2.m20);
    tmp[4] = (m1.m10 * m2.m01 + m1.m11 * m2.m11 + m1.m12 * m2.m21);
    tmp[5] = (m1.m10 * m2.m02 + m1.m11 * m2.m12 + m1.m12 * m2.m22);
    tmp[6] = (m1.m20 * m2.m00 + m1.m21 * m2.m10 + m1.m22 * m2.m20);
    tmp[7] = (m1.m20 * m2.m01 + m1.m21 * m2.m11 + m1.m22 * m2.m21);
    tmp[8] = (m1.m20 * m2.m02 + m1.m21 * m2.m12 + m1.m22 * m2.m22);
    Matrix3d.compute_svd(tmp, tmp_scale, tmp_rot);
    this.m00 = (float)tmp_rot[0];
    this.m01 = (float)tmp_rot[1];
    this.m02 = (float)tmp_rot[2];
    this.m10 = (float)tmp_rot[3];
    this.m11 = (float)tmp_rot[4];
    this.m12 = (float)tmp_rot[5];
    this.m20 = (float)tmp_rot[6];
    this.m21 = (float)tmp_rot[7];
    this.m22 = (float)tmp_rot[8];
  }
  
  public final void mulTransposeBoth(Matrix3f m1, Matrix3f m2) {
    if (this != m1 && this != m2) {
      this.m00 = m1.m00 * m2.m00 + m1.m10 * m2.m01 + m1.m20 * m2.m02;
      this.m01 = m1.m00 * m2.m10 + m1.m10 * m2.m11 + m1.m20 * m2.m12;
      this.m02 = m1.m00 * m2.m20 + m1.m10 * m2.m21 + m1.m20 * m2.m22;
      this.m10 = m1.m01 * m2.m00 + m1.m11 * m2.m01 + m1.m21 * m2.m02;
      this.m11 = m1.m01 * m2.m10 + m1.m11 * m2.m11 + m1.m21 * m2.m12;
      this.m12 = m1.m01 * m2.m20 + m1.m11 * m2.m21 + m1.m21 * m2.m22;
      this.m20 = m1.m02 * m2.m00 + m1.m12 * m2.m01 + m1.m22 * m2.m02;
      this.m21 = m1.m02 * m2.m10 + m1.m12 * m2.m11 + m1.m22 * m2.m12;
      this.m22 = m1.m02 * m2.m20 + m1.m12 * m2.m21 + m1.m22 * m2.m22;
    } else {
      float m00 = m1.m00 * m2.m00 + m1.m10 * m2.m01 + m1.m20 * m2.m02;
      float m01 = m1.m00 * m2.m10 + m1.m10 * m2.m11 + m1.m20 * m2.m12;
      float m02 = m1.m00 * m2.m20 + m1.m10 * m2.m21 + m1.m20 * m2.m22;
      float m10 = m1.m01 * m2.m00 + m1.m11 * m2.m01 + m1.m21 * m2.m02;
      float m11 = m1.m01 * m2.m10 + m1.m11 * m2.m11 + m1.m21 * m2.m12;
      float m12 = m1.m01 * m2.m20 + m1.m11 * m2.m21 + m1.m21 * m2.m22;
      float m20 = m1.m02 * m2.m00 + m1.m12 * m2.m01 + m1.m22 * m2.m02;
      float m21 = m1.m02 * m2.m10 + m1.m12 * m2.m11 + m1.m22 * m2.m12;
      float m22 = m1.m02 * m2.m20 + m1.m12 * m2.m21 + m1.m22 * m2.m22;
      this.m00 = m00;
      this.m01 = m01;
      this.m02 = m02;
      this.m10 = m10;
      this.m11 = m11;
      this.m12 = m12;
      this.m20 = m20;
      this.m21 = m21;
      this.m22 = m22;
    } 
  }
  
  public final void mulTransposeRight(Matrix3f m1, Matrix3f m2) {
    if (this != m1 && this != m2) {
      this.m00 = m1.m00 * m2.m00 + m1.m01 * m2.m01 + m1.m02 * m2.m02;
      this.m01 = m1.m00 * m2.m10 + m1.m01 * m2.m11 + m1.m02 * m2.m12;
      this.m02 = m1.m00 * m2.m20 + m1.m01 * m2.m21 + m1.m02 * m2.m22;
      this.m10 = m1.m10 * m2.m00 + m1.m11 * m2.m01 + m1.m12 * m2.m02;
      this.m11 = m1.m10 * m2.m10 + m1.m11 * m2.m11 + m1.m12 * m2.m12;
      this.m12 = m1.m10 * m2.m20 + m1.m11 * m2.m21 + m1.m12 * m2.m22;
      this.m20 = m1.m20 * m2.m00 + m1.m21 * m2.m01 + m1.m22 * m2.m02;
      this.m21 = m1.m20 * m2.m10 + m1.m21 * m2.m11 + m1.m22 * m2.m12;
      this.m22 = m1.m20 * m2.m20 + m1.m21 * m2.m21 + m1.m22 * m2.m22;
    } else {
      float m00 = m1.m00 * m2.m00 + m1.m01 * m2.m01 + m1.m02 * m2.m02;
      float m01 = m1.m00 * m2.m10 + m1.m01 * m2.m11 + m1.m02 * m2.m12;
      float m02 = m1.m00 * m2.m20 + m1.m01 * m2.m21 + m1.m02 * m2.m22;
      float m10 = m1.m10 * m2.m00 + m1.m11 * m2.m01 + m1.m12 * m2.m02;
      float m11 = m1.m10 * m2.m10 + m1.m11 * m2.m11 + m1.m12 * m2.m12;
      float m12 = m1.m10 * m2.m20 + m1.m11 * m2.m21 + m1.m12 * m2.m22;
      float m20 = m1.m20 * m2.m00 + m1.m21 * m2.m01 + m1.m22 * m2.m02;
      float m21 = m1.m20 * m2.m10 + m1.m21 * m2.m11 + m1.m22 * m2.m12;
      float m22 = m1.m20 * m2.m20 + m1.m21 * m2.m21 + m1.m22 * m2.m22;
      this.m00 = m00;
      this.m01 = m01;
      this.m02 = m02;
      this.m10 = m10;
      this.m11 = m11;
      this.m12 = m12;
      this.m20 = m20;
      this.m21 = m21;
      this.m22 = m22;
    } 
  }
  
  public final void mulTransposeLeft(Matrix3f m1, Matrix3f m2) {
    if (this != m1 && this != m2) {
      this.m00 = m1.m00 * m2.m00 + m1.m10 * m2.m10 + m1.m20 * m2.m20;
      this.m01 = m1.m00 * m2.m01 + m1.m10 * m2.m11 + m1.m20 * m2.m21;
      this.m02 = m1.m00 * m2.m02 + m1.m10 * m2.m12 + m1.m20 * m2.m22;
      this.m10 = m1.m01 * m2.m00 + m1.m11 * m2.m10 + m1.m21 * m2.m20;
      this.m11 = m1.m01 * m2.m01 + m1.m11 * m2.m11 + m1.m21 * m2.m21;
      this.m12 = m1.m01 * m2.m02 + m1.m11 * m2.m12 + m1.m21 * m2.m22;
      this.m20 = m1.m02 * m2.m00 + m1.m12 * m2.m10 + m1.m22 * m2.m20;
      this.m21 = m1.m02 * m2.m01 + m1.m12 * m2.m11 + m1.m22 * m2.m21;
      this.m22 = m1.m02 * m2.m02 + m1.m12 * m2.m12 + m1.m22 * m2.m22;
    } else {
      float m00 = m1.m00 * m2.m00 + m1.m10 * m2.m10 + m1.m20 * m2.m20;
      float m01 = m1.m00 * m2.m01 + m1.m10 * m2.m11 + m1.m20 * m2.m21;
      float m02 = m1.m00 * m2.m02 + m1.m10 * m2.m12 + m1.m20 * m2.m22;
      float m10 = m1.m01 * m2.m00 + m1.m11 * m2.m10 + m1.m21 * m2.m20;
      float m11 = m1.m01 * m2.m01 + m1.m11 * m2.m11 + m1.m21 * m2.m21;
      float m12 = m1.m01 * m2.m02 + m1.m11 * m2.m12 + m1.m21 * m2.m22;
      float m20 = m1.m02 * m2.m00 + m1.m12 * m2.m10 + m1.m22 * m2.m20;
      float m21 = m1.m02 * m2.m01 + m1.m12 * m2.m11 + m1.m22 * m2.m21;
      float m22 = m1.m02 * m2.m02 + m1.m12 * m2.m12 + m1.m22 * m2.m22;
      this.m00 = m00;
      this.m01 = m01;
      this.m02 = m02;
      this.m10 = m10;
      this.m11 = m11;
      this.m12 = m12;
      this.m20 = m20;
      this.m21 = m21;
      this.m22 = m22;
    } 
  }
  
  public final void normalize() {
    double[] tmp_rot = new double[9];
    double[] tmp_scale = new double[3];
    getScaleRotate(tmp_scale, tmp_rot);
    this.m00 = (float)tmp_rot[0];
    this.m01 = (float)tmp_rot[1];
    this.m02 = (float)tmp_rot[2];
    this.m10 = (float)tmp_rot[3];
    this.m11 = (float)tmp_rot[4];
    this.m12 = (float)tmp_rot[5];
    this.m20 = (float)tmp_rot[6];
    this.m21 = (float)tmp_rot[7];
    this.m22 = (float)tmp_rot[8];
  }
  
  public final void normalize(Matrix3f m1) {
    double[] tmp = new double[9];
    double[] tmp_rot = new double[9];
    double[] tmp_scale = new double[3];
    tmp[0] = m1.m00;
    tmp[1] = m1.m01;
    tmp[2] = m1.m02;
    tmp[3] = m1.m10;
    tmp[4] = m1.m11;
    tmp[5] = m1.m12;
    tmp[6] = m1.m20;
    tmp[7] = m1.m21;
    tmp[8] = m1.m22;
    Matrix3d.compute_svd(tmp, tmp_scale, tmp_rot);
    this.m00 = (float)tmp_rot[0];
    this.m01 = (float)tmp_rot[1];
    this.m02 = (float)tmp_rot[2];
    this.m10 = (float)tmp_rot[3];
    this.m11 = (float)tmp_rot[4];
    this.m12 = (float)tmp_rot[5];
    this.m20 = (float)tmp_rot[6];
    this.m21 = (float)tmp_rot[7];
    this.m22 = (float)tmp_rot[8];
  }
  
  public final void normalizeCP() {
    float mag = 1.0F / (float)Math.sqrt((this.m00 * this.m00 + this.m10 * this.m10 + this.m20 * this.m20));
    this.m00 *= mag;
    this.m10 *= mag;
    this.m20 *= mag;
    mag = 1.0F / (float)Math.sqrt((this.m01 * this.m01 + this.m11 * this.m11 + this.m21 * this.m21));
    this.m01 *= mag;
    this.m11 *= mag;
    this.m21 *= mag;
    this.m02 = this.m10 * this.m21 - this.m11 * this.m20;
    this.m12 = this.m01 * this.m20 - this.m00 * this.m21;
    this.m22 = this.m00 * this.m11 - this.m01 * this.m10;
  }
  
  public final void normalizeCP(Matrix3f m1) {
    float mag = 1.0F / (float)Math.sqrt((m1.m00 * m1.m00 + m1.m10 * m1.m10 + m1.m20 * m1.m20));
    m1.m00 *= mag;
    m1.m10 *= mag;
    m1.m20 *= mag;
    mag = 1.0F / (float)Math.sqrt((m1.m01 * m1.m01 + m1.m11 * m1.m11 + m1.m21 * m1.m21));
    m1.m01 *= mag;
    m1.m11 *= mag;
    m1.m21 *= mag;
    this.m02 = this.m10 * this.m21 - this.m11 * this.m20;
    this.m12 = this.m01 * this.m20 - this.m00 * this.m21;
    this.m22 = this.m00 * this.m11 - this.m01 * this.m10;
  }
  
  public boolean equals(Matrix3f m1) {
    try {
      return (this.m00 == m1.m00 && this.m01 == m1.m01 && this.m02 == m1.m02 && this.m10 == m1.m10 && this.m11 == m1.m11 && this.m12 == m1.m12 && this.m20 == m1.m20 && this.m21 == m1.m21 && this.m22 == m1.m22);
    } catch (NullPointerException e2) {
      return false;
    } 
  }
  
  public boolean equals(Object o1) {
    try {
      Matrix3f m2 = (Matrix3f)o1;
      return (this.m00 == m2.m00 && this.m01 == m2.m01 && this.m02 == m2.m02 && this.m10 == m2.m10 && this.m11 == m2.m11 && this.m12 == m2.m12 && this.m20 == m2.m20 && this.m21 == m2.m21 && this.m22 == m2.m22);
    } catch (ClassCastException e1) {
      return false;
    } catch (NullPointerException e2) {
      return false;
    } 
  }
  
  public boolean epsilonEquals(Matrix3f m1, float epsilon) {
    boolean status = true;
    if (Math.abs(this.m00 - m1.m00) > epsilon)
      status = false; 
    if (Math.abs(this.m01 - m1.m01) > epsilon)
      status = false; 
    if (Math.abs(this.m02 - m1.m02) > epsilon)
      status = false; 
    if (Math.abs(this.m10 - m1.m10) > epsilon)
      status = false; 
    if (Math.abs(this.m11 - m1.m11) > epsilon)
      status = false; 
    if (Math.abs(this.m12 - m1.m12) > epsilon)
      status = false; 
    if (Math.abs(this.m20 - m1.m20) > epsilon)
      status = false; 
    if (Math.abs(this.m21 - m1.m21) > epsilon)
      status = false; 
    if (Math.abs(this.m22 - m1.m22) > epsilon)
      status = false; 
    return status;
  }
  
  public int hashCode() {
    long bits = 1L;
    bits = 31L * bits + VecMathUtil.floatToIntBits(this.m00);
    bits = 31L * bits + VecMathUtil.floatToIntBits(this.m01);
    bits = 31L * bits + VecMathUtil.floatToIntBits(this.m02);
    bits = 31L * bits + VecMathUtil.floatToIntBits(this.m10);
    bits = 31L * bits + VecMathUtil.floatToIntBits(this.m11);
    bits = 31L * bits + VecMathUtil.floatToIntBits(this.m12);
    bits = 31L * bits + VecMathUtil.floatToIntBits(this.m20);
    bits = 31L * bits + VecMathUtil.floatToIntBits(this.m21);
    bits = 31L * bits + VecMathUtil.floatToIntBits(this.m22);
    return (int)(bits ^ bits >> 32L);
  }
  
  public final void setZero() {
    this.m00 = 0.0F;
    this.m01 = 0.0F;
    this.m02 = 0.0F;
    this.m10 = 0.0F;
    this.m11 = 0.0F;
    this.m12 = 0.0F;
    this.m20 = 0.0F;
    this.m21 = 0.0F;
    this.m22 = 0.0F;
  }
  
  public final void negate() {
    this.m00 = -this.m00;
    this.m01 = -this.m01;
    this.m02 = -this.m02;
    this.m10 = -this.m10;
    this.m11 = -this.m11;
    this.m12 = -this.m12;
    this.m20 = -this.m20;
    this.m21 = -this.m21;
    this.m22 = -this.m22;
  }
  
  public final void negate(Matrix3f m1) {
    this.m00 = -m1.m00;
    this.m01 = -m1.m01;
    this.m02 = -m1.m02;
    this.m10 = -m1.m10;
    this.m11 = -m1.m11;
    this.m12 = -m1.m12;
    this.m20 = -m1.m20;
    this.m21 = -m1.m21;
    this.m22 = -m1.m22;
  }
  
  public final void transform(Tuple3f t) {
    float x = this.m00 * t.x + this.m01 * t.y + this.m02 * t.z;
    float y = this.m10 * t.x + this.m11 * t.y + this.m12 * t.z;
    float z = this.m20 * t.x + this.m21 * t.y + this.m22 * t.z;
    t.set(x, y, z);
  }
  
  public final void transform(Tuple3f t, Tuple3f result) {
    float x = this.m00 * t.x + this.m01 * t.y + this.m02 * t.z;
    float y = this.m10 * t.x + this.m11 * t.y + this.m12 * t.z;
    result.z = this.m20 * t.x + this.m21 * t.y + this.m22 * t.z;
    result.x = x;
    result.y = y;
  }
  
  void getScaleRotate(double[] scales, double[] rot) {
    double[] tmp = new double[9];
    tmp[0] = this.m00;
    tmp[1] = this.m01;
    tmp[2] = this.m02;
    tmp[3] = this.m10;
    tmp[4] = this.m11;
    tmp[5] = this.m12;
    tmp[6] = this.m20;
    tmp[7] = this.m21;
    tmp[8] = this.m22;
    Matrix3d.compute_svd(tmp, scales, rot);
  }
  
  public Object clone() {
    Matrix3f m1 = null;
    try {
      m1 = (Matrix3f)super.clone();
    } catch (CloneNotSupportedException e) {
      throw new InternalError();
    } 
    return m1;
  }
  
  public final float getM00() {
    return this.m00;
  }
  
  public final void setM00(float m00) {
    this.m00 = m00;
  }
  
  public final float getM01() {
    return this.m01;
  }
  
  public final void setM01(float m01) {
    this.m01 = m01;
  }
  
  public final float getM02() {
    return this.m02;
  }
  
  public final void setM02(float m02) {
    this.m02 = m02;
  }
  
  public final float getM10() {
    return this.m10;
  }
  
  public final void setM10(float m10) {
    this.m10 = m10;
  }
  
  public final float getM11() {
    return this.m11;
  }
  
  public final void setM11(float m11) {
    this.m11 = m11;
  }
  
  public final float getM12() {
    return this.m12;
  }
  
  public final void setM12(float m12) {
    this.m12 = m12;
  }
  
  public final float getM20() {
    return this.m20;
  }
  
  public final void setM20(float m20) {
    this.m20 = m20;
  }
  
  public final float getM21() {
    return this.m21;
  }
  
  public final void setM21(float m21) {
    this.m21 = m21;
  }
  
  public final float getM22() {
    return this.m22;
  }
  
  public final void setM22(float m22) {
    this.m22 = m22;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\javax\vecmath\Matrix3f.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */