package xmicha65.bp_app.controller;

import android.os.Environment;

import java.io.File;

/**
 * source: Android documentation
 */
public class Storages {

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    /* Get external (images) directory */
    public static File getPublicImagesStorageDir(String subdir) {
        File directory = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), subdir);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                System.out.println("### mkdirs " + directory.toString() + " failed!");
            }
        }
        return directory;
    }

    /* Get File into external (images) directory */
    public static File getPublicImagesFile(String dir, String fileName) {
        File path = getPublicImagesStorageDir(dir);

        File file = new File(path, fileName);
        if (file.exists()) {
            System.out.println("### file exists!");
            return null;
        }
        return file;
    }
}
