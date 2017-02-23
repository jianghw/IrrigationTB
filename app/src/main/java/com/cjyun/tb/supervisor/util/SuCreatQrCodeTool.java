package com.cjyun.tb.patient.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.cjyun.tb.TbApp;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * Created by jianghw on 2016/3/25 0025.
 * Description 生成二维码图片
 */
public class SuCreatQrCodeTool {

    public static Bitmap prevQrcode(String string, int size) {

        try {
            Key desKey = getDESKey(hexStringToByteArray(md5("1314520")));
            String bitmap = encrypt(string, desKey, "DES");
            return createQrCode(bitmap, size);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeResource(TbApp.getAppResource(), android.R.drawable.stat_notify_error);
    }

    public static Bitmap createQrCode(String string, int size) {
        Hashtable<EncodeHintType, String> hintTypeStringHashtable = new Hashtable<>();
        hintTypeStringHashtable.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new QRCodeWriter().encode(string, BarcodeFormat.QR_CODE, size, size, hintTypeStringHashtable);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        int[] pixels = new int[size * size];
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (bitMatrix.get(x, y)) {
                    pixels[y * size + x] = 0xff000000;
                } else {
                    pixels[y * size + x] = 0xffffffff;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, size, 0, 0, size, size);
        return bitmap;
    }

    /**
     * MD5加密
     *
     * @param string 源字符串
     * @return 加密后的字符串
     */
    public static String md5(String string) {
        byte[] hash = null;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * 密钥
     */
    public static Key getDESKey(byte[] key) throws Exception {
        DESKeySpec des = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        return keyFactory.generateSecret(des);
    }

    /**
     * 进行解密
     */
    public static String decrypt(String data)
            throws Exception {
        Key desKey = getDESKey(hexStringToByteArray(md5("1314520")));
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, desKey);
        String result = new String(cipher.doFinal(hexStringToByteArray(data)), "utf8");
        return result;
    }


    /**
     * 加密
     */
    public static String encrypt(String data, Key key, String algorithm)
            throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return byteArrayToHexString(cipher.doFinal(data.getBytes("utf8")));
    }

    /**
     * 16进制表示的字符串转换为字节数组
     *
     * @param s 16进制表示的字符串
     * @return byte[] 字节数组
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] d = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
            d[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16));
        }
        return d;
    }

    /**
     * byte[]数组转换为16进制的字符串
     *
     * @param data 要转换的字节数组
     * @return 转换后的结果
     */
    public static String byteArrayToHexString(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte b : data) {
            int v = b & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.getDefault());
    }

}
