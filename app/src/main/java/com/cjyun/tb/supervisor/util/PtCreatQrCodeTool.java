package com.cjyun.tb.supervisor.util;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

/**
 * Created by jianghw on 2016/3/25 0025.
 * Description 生成二维码图片
 */
public class PtCreatQrCodeTool {

    public static Bitmap createQrCode(String string, int size) {
        Hashtable<EncodeHintType, String> hintTypeStringHashtable = new Hashtable<>();
        hintTypeStringHashtable.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new QRCodeWriter().encode(
                    string, BarcodeFormat.QR_CODE, size, size, hintTypeStringHashtable);
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
}
