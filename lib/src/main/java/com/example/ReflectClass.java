package com.example;

/**
 * Created by Administrator on 2016/5/20 0020</br>
 * description:</br>
 */
public class ReflectClass {
    private int ageA;
    protected int ageB;
    public int ageC;
    private static int ageD;
    private static final int ageE = 100;

    private short mShort;
    private long mLong;
    private boolean mBoolean;
    private float mFloat;
    private double mDouble;
    private char mChar;
    private Character mCharacter;

    private String name;
    private Object unit;
    private Number mNumber;
    private Enum mEnum;

    enum Color {RED, GREEN, BLANK, YELLOW}

    public float getFloat() {
        return mFloat;
    }

    public void setFloat(float aFloat) {
        mFloat = aFloat;
    }

    public int getAgeA() {
        return ageA;
    }

    public void setAgeA(int ageA) {
        this.ageA = ageA;
    }
}
