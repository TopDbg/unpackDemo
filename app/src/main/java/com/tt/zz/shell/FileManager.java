package com.tt.zz.shell;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FileManager {
    public static File releaseAssesFile(Context ctx, String assetFile, String releaseFile, Method decMethod)
    {
        AssetManager manager = ctx.getAssets();
        try{
            InputStream is = manager.open(assetFile);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int iRead;
            while ((iRead = is.read(buf)) != -1)
            {
                os.write(buf,0,iRead);
            }
            byte[] dec = decMethod != null ? (byte[])decMethod.invoke(null,os.toByteArray()):os.toByteArray();
            is.close();
            os.close();

            FileOutputStream of = new FileOutputStream(new File(releaseFile));
            of.write(dec);
            of.close();

            return new File(releaseFile);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
