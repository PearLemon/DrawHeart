package com.lemon.drawheart.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author lemon92xy
 */
public class DataBaseUtil {
    /**
     * 数据库文件路径
     */
    private static final String DB_PATH = "data/data/com.lemon.drawheart/databases/";
    /**
     * 数据库文件名
     */
    private static final String DB_NAME = "present.db";

    public static SQLiteDatabase openDatabase(Context context) {
        File jhPath = new File(DB_PATH + DB_NAME);
        if (jhPath.exists()) {
            return SQLiteDatabase.openOrCreateDatabase(jhPath, null);
        } else {
            File path = new File(DB_PATH);
            Log.d("===", "" + path.mkdir());
            try {
                ClassLoader classLoader = context.getClass().getClassLoader();
                if (classLoader == null) {
                    return null;
                }
                InputStream is = classLoader.getResourceAsStream("assets/" + DB_NAME);
                FileOutputStream fos = new FileOutputStream(jhPath);
                byte[] buffer = new byte[10240];
                int count;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.flush();
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return openDatabase(context);
        }
    }
}
