package matic.mladen.chatapplication;

/**
 * Created by Mladen on 6/6/2018.
 */

public class JNIxor {
    static {
        System.loadLibrary("xor_encryption");
    }
    public static native String encryption(String message);
    public static native String decryption(String message);
}
