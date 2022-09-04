package com.better.amino.utils;

import android.util.Base64;

/*
 * This Java Class Was Created By SirLez
 * Feel Free To Modify For A Better Code
 */

public class Utils {

    private static final String NdiKey = "02b258c63559d8804321c5d5065af320358d366f";
    private static final String SigKey = "f8e7a61ac3f725941e3ac7cae2d688be97f30b93";

    /* Convert Byte Array To Hex */

    static String byte2hex(byte[] bArr) {
        char[] cArr = new char[(bArr.length * 2)];
        for (int i = 0; i < bArr.length; i++) {
            int i2 = bArr[i] & 255;
            int i3 = i * 2;
            char[] cArr2 = "0123456789ABCDEF".toCharArray();
            cArr[i3] = cArr2[i2 >>> 4];
            cArr[i3 + 1] = cArr2[i2 & 15];
        }
        return new String(cArr);
    }

    /* Convert Hex to Byte Array */

    static byte[] hex2byte(String str) {
        int length = str.length();
        byte[] bArr = new byte[(length / 2)];
        for (int i = 0; i < length; i += 2) {
            bArr[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i + 1), 16));
        }
        return bArr;
    }

    /* Concat Byte Arrays */

    public static byte[] concat(byte[] prefix, byte[] value) {
        try {
            java.io.ByteArrayOutputStream concat = new java.io.ByteArrayOutputStream();
            concat.write(prefix);
            concat.write(value);
            return concat.toByteArray();
        } catch (Exception ignored) {
        }
        return null;
    }

    /* Generate NDC Message Signature */

    public static String signature(String data) {
        try {
            javax.crypto.spec.SecretKeySpec key = new javax.crypto.spec.SecretKeySpec(hex2byte(SigKey), "HmacSHA1");
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA1");
            mac.init(key);
            return Base64.encodeToString(concat(hex2byte("42"), mac.doFinal(data.getBytes(java.nio.charset.StandardCharsets.UTF_8))), Base64.NO_WRAP);
        } catch (Exception e) {
            return "Error";
        }
    }

    /* Generate NDC Device Id */

    public static String deviceId() {
        try {
            String value = java.util.UUID.randomUUID().toString();
            javax.crypto.spec.SecretKeySpec key = new javax.crypto.spec.SecretKeySpec(hex2byte(NdiKey), "HmacSHA1");
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA1");
            mac.init(key);
            return "42" + byte2hex(value.getBytes()) + byte2hex(mac.doFinal(concat(hex2byte("42"), value.getBytes())));
        } catch (Exception e) {
            return "Error";
        }
    }


}