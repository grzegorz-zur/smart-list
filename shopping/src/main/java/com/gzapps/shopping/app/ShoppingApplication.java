/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.app;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;

import com.gzapps.shopping.R;
import com.gzapps.shopping.core.Shopping;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Semaphore;

import static android.app.AlarmManager.RTC;
import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.app.PendingIntent.getBroadcast;
import static com.gzapps.shopping.core.Time.HOUR;

public class ShoppingApplication extends Application {

    public static final int SIZE = 500;
    public static final float CORRELATION = 0.5F;
    public static final float SHORTAGES_LOWER_BOUND = -2F;
    private static final String SHOPPING = "shopping";
    private static final String BACKUP = "backup";
    private static final long TIME_OFFSET = 3 * HOUR;
    private static final int BUFFER_SIZE = 64 * 1024;
    private final Semaphore semaphore;
    private Shopping shopping;

    public ShoppingApplication() {
        semaphore = new Semaphore(1, true);
    }

    public void acquire() {
        String thread = Thread.currentThread().getName();
        Log.i(Logs.TAG, String.format("acquire %s", thread));
        semaphore.acquireUninterruptibly();
        Log.i(Logs.TAG, String.format("acquired %s", thread));
    }

    public void release() {
        String thread = Thread.currentThread().getName();
        Log.i(Logs.TAG, String.format("release %s", thread));
        semaphore.release();
        Log.i(Logs.TAG, String.format("released %s", thread));
    }

    public boolean shouldRelease() {
        return semaphore.hasQueuedThreads();
    }

    public synchronized Shopping shopping() {
        return shopping;
    }

    public synchronized void create() {
        shopping = new Shopping();
        Resources resources = getResources();
        String[] samples = resources.getStringArray(R.array.sample_products);
        for (String sample : samples) {
            shopping.create(sample);
        }
    }

    public synchronized void load() throws IOException {
        if (!fileExists(SHOPPING)) {
            return;
        }

        FileInputStream fis = openFileInput(SHOPPING);
        BufferedInputStream bis = new BufferedInputStream(fis);
        DataInputStream dis = new DataInputStream(bis);
        try {
            shopping = new Shopping(dis);
        } catch (IOException e) {
            Log.e(Logs.TAG, "load error", e);
            throw e;
        } finally {
            dis.close();
        }
    }

    public synchronized void save() throws IOException {
        if (shopping == null) {
            return;
        }

        if (fileExists(SHOPPING)) {
            backup();
        }

        FileOutputStream fos = openFileOutput(SHOPPING, MODE_PRIVATE);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            shopping.save(dos);
        } catch (IOException e) {
            Log.e(Logs.TAG, "save error", e);
            throw e;
        } finally {
            dos.close();
        }
    }

    private boolean fileExists(String name) {
        String[] files = fileList();
        for (String file : files) {
            if (name.equals(file)) {
                return true;
            }
        }
        return false;
    }

    public synchronized boolean backupAvailable() {
        return fileExists(BACKUP);
    }

    private void backup() throws IOException {
        Log.i(Logs.TAG, "doing backup");
        copy(SHOPPING, BACKUP);
    }

    public synchronized void restore() throws IOException {
        Log.i(Logs.TAG, "restoring backup");
        copy(BACKUP, SHOPPING);
    }

    private void copy(String input, String output) throws IOException {
        FileInputStream fis = openFileInput(input);
        FileOutputStream fos = openFileOutput(output, MODE_PRIVATE);
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            int read;
            while ((read = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, read);
            }
        } catch (IOException e) {
            Log.i(Logs.TAG, "copy error", e);
            throw e;
        } finally {
            fos.close();
            fis.close();
        }
    }

    @SuppressWarnings("CallToNativeMethodWhileLocked")
    public synchronized void schedule() {
        Intent intent = new Intent(this, ShoppingReceiver.class);
        PendingIntent pendingIntent =
                getBroadcast(this, 0, intent, FLAG_UPDATE_CURRENT);

        long time = System.currentTimeMillis();
        long analysisTime = time + TIME_OFFSET;

        AlarmManager alarmManager =
                (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(RTC, analysisTime, pendingIntent);

        Date date = new Date(analysisTime);
        Log.i(Logs.TAG, String.format("schedule %tc", date));
    }
}
