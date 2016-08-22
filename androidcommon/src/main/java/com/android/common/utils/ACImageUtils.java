package com.android.common.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2016/8/22 0022.
 */
public class ACImageUtils {

    /**
     * convert Bitmap to byte array
     *
     * @param b
     * @return
     */
    public static byte[] bitmapToByte(Bitmap b) {
        if (b == null) {
            return null;
        }

        ByteArrayOutputStream o = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, o);
        return o.toByteArray();
    }

    /**
     * convert byte array to Bitmap
     *
     * @param b
     * @return
     */
    public static Bitmap byteToBitmap(byte[] b) {
        return (b == null || b.length == 0) ? null : BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    /**
     * convert Drawable to Bitmap
     *
     * @param d
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable d) {
        return d == null ? null : ((BitmapDrawable)d).getBitmap();
    }

    /**
     * convert Bitmap to Drawable
     *
     * @param b
     * @return
     */
    public static Drawable bitmapToDrawable(Bitmap b) {
        return b == null ? null : new BitmapDrawable(b);
    }

    /**
     * convert Drawable to byte array
     *
     * @param d
     * @return
     */
    public static byte[] drawableToByte(Drawable d) {
        return bitmapToByte(drawableToBitmap(d));
    }

    /**
     * convert byte array to Drawable
     *
     * @param b
     * @return
     */
    public static Drawable byteToDrawable(byte[] b) {
        return bitmapToDrawable(byteToBitmap(b));
    }

    /**
     * scale image
     *
     * @param org
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap scaleImageTo(Bitmap org, int newWidth, int newHeight) {
        return scaleImage(org, (float) newWidth / org.getWidth(), (float) newHeight / org.getHeight());
    }

    /**
     * scale image
     *
     * @param org
     * @param scaleWidth sacle of width
     * @param scaleHeight scale of height
     * @return
     */
    public static Bitmap scaleImage(Bitmap org, float scaleWidth, float scaleHeight) {
        if (org == null) {
            return null;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(org, 0, 0, org.getWidth(), org.getHeight(), matrix, true);
    }

    /**
     * 先质量压缩到90%，再把bitmap保存到sd卡上
     *
     * @param relativePath
     * @param fileName
     * @param bm
     * @return
     * @author com.tiantian
     */
    public static int saveBitmap2SD(String relativePath, String fileName, Bitmap bm) {
        return saveBitmap2SD(relativePath, fileName, bm, 90);
    }

    /**
     * 先质量压缩到指定百分比（0% ~ 90%），再把bitmap保存到sd卡上
     *
     * @param relativePath
     * @param fileName
     * @param bm
     * @param quality
     * @return
     */
    public static int saveBitmap2SD(String relativePath, String fileName, Bitmap bm, int quality) {
        if (!relativePath.endsWith("/")) {
            relativePath = relativePath + "/";
        }
        File file = null;
        FileOutputStream out = null;
        try {
            ACFileUtil.creatSDDir(relativePath);
            file = ACFileUtil.creatSDFile(relativePath + fileName);
            out = new FileOutputStream(file.getPath());
            bm.compress(Bitmap.CompressFormat.JPEG, quality, out);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            ACIOUtil.closeIO(out);
        }
    }

    /**
     * 先质量压缩到指定百分比（0% ~ 90%），再把bitmap保存到sd卡上
     *
     * @param filePath
     * @param bm
     * @param quality
     * @return
     */
    public static int saveBitmap2SDAbsolute(String filePath, Bitmap bm, int quality) {
        File file = null;
        FileOutputStream out = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            out = new FileOutputStream(file.getPath());
            bm.compress(Bitmap.CompressFormat.JPEG, quality, out);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            ACIOUtil.closeIO(out);
        }
    }


    /**
     * 压缩图片直到容量小于200kb，并保存到sdcard
     *
     * @param relativePath
     * @param fileName
     * @param bm
     * @return
     */
    public static int saveBitmap2SDWithCapacity(String relativePath, String fileName, Bitmap bm) {
        return saveBitmap2SDWithCapacity(relativePath, fileName, bm, 200);
    }

    /**
     * 压缩图片直到容量小于指定值(kb)，并保存到sdcard
     *
     * @param relativePath
     * @param fileName
     * @param bm
     * @param capacity
     * @return
     */
    public static int saveBitmap2SDWithCapacity(String relativePath, String fileName, Bitmap bm, int capacity) {
        if (!relativePath.endsWith("/")) {
            relativePath = relativePath + "/";
        }
        File file = null;
        FileOutputStream out = null;
        ByteArrayInputStream bais = null;
        try {
            ACFileUtil.creatSDDir(relativePath);
            file = ACFileUtil.creatSDFile(relativePath + fileName);
            out = new FileOutputStream(file.getPath());
//            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            bais = compressImage(bm, capacity);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = bais.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            ACIOUtil.closeIO(out, bais);
        }

    }

    /**
     * 压缩图片直到容量小于指定值(kb)
     *
     * @param image
     * @param capacity
     * @return
     */
    public static ByteArrayInputStream compressImage(Bitmap image, int capacity) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > capacity) {  //循环判断如果压缩后图片是否大于1大00kb,于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            if (options < 10) {
                break;
            }
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        baos.reset();
        return bais;
    }
}
