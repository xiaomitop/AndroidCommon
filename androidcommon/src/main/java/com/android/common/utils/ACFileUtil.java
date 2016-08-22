package com.android.common.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.webkit.MimeTypeMap;

import com.android.common.log.Logger;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class ACFileUtil {

    public static final String TAG = ACFileUtil.class.getSimpleName();

    public static final String SD_CARD_PATH = Environment.getExternalStorageDirectory().toString()+"/";

    public static String getSDPATH() {
        return SD_CARD_PATH;
    }

    public static File obtainDirF(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static String obtainDirS(String path) {
        return obtainDirF(path).getAbsolutePath();
    }

    /**
     * 在SD卡上创建文件
     *
     * @throws IOException
     */
    public static File creatSDFile(String fileRelativePath) throws IOException {
        File file = new File(SD_CARD_PATH + fileRelativePath);
        file.createNewFile();
        return file;
    }

    /**
     * 在SD卡上创建目录
     *
     * @param dirRelativePath
     */
    public static File creatSDDir(String dirRelativePath) {
        File dir = new File(SD_CARD_PATH + dirRelativePath);
        dir.mkdirs();
        return dir;
    }

    /**
     * 判断SDCard是否可用
     *
     * @return true 存在 false 不存在
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 判断SD卡上的文件夹是否存在
     */
    public static boolean isFileExist(String fileRelativePath) {
        File file = new File(SD_CARD_PATH + fileRelativePath);
        return file.exists();
    }

    /**
     * 输入流转byte[]
     */
    public static byte[] input2byte(InputStream inStream) {
        if (inStream == null) {
            return null;
        }
        byte[] in2b = null;
        BufferedInputStream in = new BufferedInputStream(inStream);
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        int rc = 0;
        try {
            while ((rc = in.read()) != -1) {
                swapStream.write(rc);
            }
            in2b = swapStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ACIOUtil.closeIO(inStream, in, swapStream);
        }
        return in2b;
    }

    /**
     * 将一个InputStream里面的数据写入到SD卡中
     */
    public static File write2SDFromInput(String relativePath, String fileName, InputStream input) {
        if (!relativePath.endsWith("/")) {
            relativePath = relativePath + "/";
        }
        File file = null;
        OutputStream output = null;
        try {
            creatSDDir(relativePath);
            file = creatSDFile(relativePath + fileName);
            output = new FileOutputStream(file);
            byte buffer[] = new byte[4 * 1024];
            int length = 0;
            while ((length = input.read(buffer)) != -1) {
                output.write(buffer, 0, length);
            }
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ACIOUtil.closeIO(output);
        }
        return file;
    }

    /**
     * 把uri转为File对象
     *
     * @param context
     * @param uri
     * @return
     */
    public static File uri2File(Context context, Uri uri) {
        // 而managedquery在api 11 被弃用，所以要转为使用CursorLoader,并使用loadInBackground来返回
        Cursor cursor = null;
        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            CursorLoader loader = new CursorLoader(context, uri, projection, null, null, null);
            cursor = loader.loadInBackground();
            if (null == cursor) {
                return null;
            }
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return new File(cursor.getString(column_index));
        } catch (Exception ex) {
            Logger.e(TAG, ex.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public static File uri2FileInteral(Context context, Uri uri) {
        if (null == uri) {
            return null;
        }
        File file = uri2File(context, uri);
        return null == file ? new File(uri.getPath()) : file;
    }


    public static void openFile(Context context, String path) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String type = getMimeType(path);
        //参数二type见下面
        intent.setDataAndType(Uri.fromFile(new File(path)), type);
        context.startActivity(intent);
    }

    public static String getMimeType(String uri) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(uri);
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static void deleteFile(File... files) {
        if (!ACStringUtils.isEmpty(files)) {
            for (File file : files) {
                try {
                    file.delete();
                } catch (RuntimeException e) {
                    Logger.e(TAG, e.toString());
                }
            }
        }
    }

    /**
     * 删除文件夹
     *
     * @param file
     */
    public static void deleteFolder(File file) {
        if (file.exists() && file.isDirectory()) {//判断是文件还是目录
            if (file.listFiles().length > 0) {//若目录下没有文件则直接删除
                //若有则把文件放进数组，并判断是否有下级目录
                File delFile[] = file.listFiles();
                int i = file.listFiles().length;
                for (int j = 0; j < i; j++) {
                    if (delFile[j].isDirectory()) {
                        deleteFolder(delFile[j]);//递归调用del方法并取得子目录路径
                    }
                    delFile[j].delete();//删除文件  
                }
            }
            file.delete();
        }
    }

    /**
     * 删除文件夹
     *
     * @param filePath
     */
    public static void deleteFolder(String filePath) {
        File file = new File(filePath);
        deleteFolder(file);
    }


    /**
     * 从文件中读取Byte数组
     * Add by Hubert
     *
     * @param file
     * @return byte[]
     * @throws IOException
     */
    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        // 获取文件大小
        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // 文件太大，无法读取
            throw new IOException("File is to large " + file.getName());
        }
        // 创建一个数据来保存文件数据
        byte[] bytes = new byte[(int) length];
        // 读取数据到byte数组中
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }
        // 确保所有数据均被读取
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        // Close the input stream and return bytes
        is.close();
        return bytes;
    }

    public static byte[] getBytesFromInputStream(InputStream in) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[4096];
        int count;
        while ((count = in.read(data, 0, 4096)) != -1)
            outStream.write(data, 0, count);
        return outStream.toByteArray();
    }

    public static File getFileFromInputStream(File file, InputStream ins) throws IOException {
        OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        ins.close();
        return file;
    }

    /**
     * 获取拍照的图片路径
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.FROYO)
    public static File getDCIMFile() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
    }

    /**
     * 扫描目录（扫描后可以及时在图库中看到）
     *
     * @param context
     * @param path
     */
    @TargetApi(Build.VERSION_CODES.FROYO)
    public static void scanFile(Context context, String path) {
        MediaScannerConnection.scanFile(context, new String[]{path}, null, null);
    }

    /**
     * 获取项目根路径
     *
     * @param context
     * @param applicationDir
     * @return
     */
    public static String getApplicationPath(Context context, @Nullable String applicationDir) {
        // check whether the device has external storage
        if (isSDCardEnable()) {
            String sdcardPath = Environment.getExternalStorageDirectory().toString()
                    + File.separator + (ACStringUtils.isEmpty(applicationDir) ? "" : applicationDir + File.separator);
            Logger.d(TAG, "have sdcard! sdcard path: " + sdcardPath);
            return sdcardPath;
        } else {
            String dirPath = context.getCacheDir().getAbsoluteFile() + File.separator;
            Logger.d(TAG, "have no sdcard! dir path: " + dirPath);
            return dirPath;
        }
    }

    /**
     * 获取一个文件对象，如果不存在，则自动创建
     *
     * @param filePath
     * @return
     */
    public static File getFileAutoCreated(String filePath) {
        File file = new File(filePath);
        if (file.isDirectory()) {
            Logger.e(TAG, "[getFileAutoCreated]file[" + filePath + "] is directory!");
            return file;
        }
        if (file.exists()) {
            return file;
        }
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            Logger.e(TAG, e.toString());
        }
        return file;
    }

    /**
     * 获取一个目录对象，如果不存在，则自动创建
     *
     * @param dirPath
     * @return
     */
    public static File getDirAutoCreated(String dirPath) {
        File dirFile = new File(dirPath);
        if (dirFile.isFile()) {
            Logger.e(TAG, "[getDirAutoCreated]file[" + dirPath + "] is file!");
            return dirFile;
        }
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        return dirFile;
    }

    /**
     * 复制文件
     *
     * @param from
     * @param to
     */
    public static void copyFile(File from, File to) {
        if (null == from || !from.exists()) {
            Logger.e(TAG, "file(from) is null or is not exists!!");
            return;
        }
        if (null == to) {
            Logger.e(TAG, "file(to) is null!!");
            return;
        }

        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(from);
            if (!to.exists()) {
                to.createNewFile();
            }
            os = new FileOutputStream(to);

            byte[] buffer = new byte[1024];
            int len = 0;
            while (-1 != (len = is.read(buffer))) {
                os.write(buffer, 0, len);
            }
            os.flush();
        } catch (Exception e) {
            Logger.e(TAG, e.toString());
        } finally {
            ACIOUtil.closeIO(is, os);
        }
    }
}