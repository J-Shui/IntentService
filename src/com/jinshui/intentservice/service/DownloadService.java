package com.jinshui.intentservice.service;
/**
 * IntentService�Ǵ������̵߳ķ�����������ڲ�ʹ���˵��̳߳�ģʽ
 * �����е�����ִ�����֮�󣬻��Զ��رձ�����
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
     * �����еĲ���ֵ�����̵߳�����
     */
    public DownloadService() {
        super(null);
    }

    /**
     * ��������
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
     * �����߳���ִ�еķ���
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
                byte[] bytes = new byte[10*1024];//ÿ�ζ�ȡ�ֽڵ������
                while ((len=is.read(bytes))!=-1){
                    bos.write(bytes,0,len);

                }
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                Intent bitmapIntent = new Intent(Config.ACTION_DOWNLOAD_COMPLETED);
                bitmapIntent.putExtra(Config.EXTRA_URL,path);
                bitmapIntent.putExtra(Config.EXTRA_BITMAP,bitmap);

                sendBroadcast(bitmapIntent);

                //������һ��ͼƬ��������Ժ������߳�˯��һ��ʱ�䣬������IntentService��˳��ִ���߳�
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
