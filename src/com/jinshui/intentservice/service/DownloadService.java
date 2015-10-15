package com.jinshui.intentservice.service;
/**
 * IntentService是带有子线程的服务组件，其内部使用了单线程池模式
 * 当所有的任务执行完成之后，会自动关闭本服务
 */
import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import com.jinshui.intentservice.utils.Config;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2015/10/15.
 */
public class DownloadService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * 父类中的参数值是子线程的名字
     */
    public DownloadService() {
        super(null);
    }

    /**
     * 开启服务
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 在子线程中执行的方法
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        String path = intent.getStringExtra("path");
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setDoInput(true);
            conn.connect();

            InputStream is = null;
            ByteArrayOutputStream bos = null;
            if (conn.getResponseCode()==200){
                is = conn.getInputStream();
                bos = new ByteArrayOutputStream();

                int len = 0;
                byte[] bytes = new byte[10*1024];//每次读取字节的最大数
                while ((len=is.read(bytes))!=-1){
                    bos.write(bytes,0,len);

                }
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                Intent bitmapIntent = new Intent(Config.ACTION_DOWNLOAD_COMPLETED);
                bitmapIntent.putExtra(Config.EXTRA_URL,path);
                bitmapIntent.putExtra(Config.EXTRA_BITMAP,bitmap);

                sendBroadcast(bitmapIntent);

                //这里让一张图片下载完成以后，让其线程睡眠一段时间，来测试IntentService按顺序执行线程
                Thread.sleep(10000);
           }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
