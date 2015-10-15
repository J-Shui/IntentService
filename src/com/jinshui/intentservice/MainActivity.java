package com.jinshui.intentservice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.jinshui.intentservice.service.DownloadService;
import com.jinshui.intentservice.utils.Config;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * ʹ��ע���ȡ�ؼ���ID
 */
@ContentView(R.layout.main)
public class MainActivity extends Activity {
    @ViewInject(R.id.img1Id)
    private ImageView imageView1;
    @ViewInject(R.id.img2Id)
    private ImageView imageView2;
    @ViewInject(R.id.img3Id)
    private ImageView imageView3;
    @ViewInject(R.id.mainLayout)
    private RelativeLayout mainLayout;

    private ImageReceiver receiver;
    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewUtils.inject(this);

        //��imageView���ñ�ǩ���Ա������ͼƬ�Ĵ�λ
        imageView1.setTag(Config.URL1);
        imageView2.setTag(Config.URL2);
        imageView3.setTag(Config.URL3);

        //ע��㲥������
        receiver = new ImageReceiver();
        IntentFilter filter = new IntentFilter(Config.ACTION_DOWNLOAD_COMPLETED);
        registerReceiver(receiver,filter);

    }
    public void startDownload(View v){
        Intent intent = new Intent(getApplicationContext(),DownloadService.class);
        //���ص�һ��ͼƬ
        intent.putExtra("path",Config.URL1);
        startService(intent);
        //���صڶ���ͼƬ
        intent.putExtra("path",Config.URL2);
        startService(intent);
        //���ص�����ͼƬ
        intent.putExtra("path",Config.URL3);
        startService(intent);
    }

    /**
     * һ����onDestroy�����У�ȡ��ע��
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);

    }
    //����һ���㲥������
    class ImageReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            //����ͼƬ������ɵĹ㲥
            String url = intent.getStringExtra(Config.EXTRA_URL);
            Bitmap bitmap = intent.getParcelableExtra(Config.EXTRA_BITMAP);
            //������Url��ΪͼƬ�ı�ʶ����ʶ��ͼƬ
            ImageView imageView = (ImageView) mainLayout.findViewWithTag(url);
            imageView.setImageBitmap(bitmap);
        }
    }
}
