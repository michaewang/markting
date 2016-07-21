package com.mars.markting.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

import com.mars.markting.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.LogRecord;

/**
 * Created by SPREADTRUM\michael.wang on 5/10/16.
 */
public class MyImageLoader {


        //图片缓存,传见cashe
        private LruCache<String,Bitmap> mCaches;

        private ImageView mImageView;
        private String mIconUrl;

        public MyImageLoader(){//ListView listView
        //ListView滑动优化
        //mListView = listView;
        //mTask = new HashSet<News2AyncTask>();

        //内存转换缓存空间
        //获取最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory/4;

        mCaches = new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //在每次存入缓存的时候，调用
                Log.d("michael","LruCache -> sizeOf = " + value.getByteCount());
                return value.getByteCount();
            }
        };
    }

        //增加到缓存
    public void addBitmapToCache(String url,Bitmap bitmap){
        Log.d("michael"," addBitmapToCache url = " + url + " // getBitmapFromCache(url) = " + getBitmapFromCache(url));
        if(getBitmapFromCache(url) == null){
            if(url != null && bitmap != null){
                Log.d("michael","addBitmapToCache -> bitmap ~~~= " + mCaches.put(url, bitmap));

                mCaches.put(url, bitmap);
            }

        }
    }

    //从缓存中获取数据
    public Bitmap getBitmapFromCache(String url){
        Log.d("michael"," getBitmapFromCache url = " + url);
        return mCaches.get(url);
    }


    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mImageView.getTag().equals(mIconUrl)){ //用Tag标识，解决某一个图片被加载多次
                mImageView.setImageBitmap((Bitmap) msg.obj);
            }
        }
    };

    //方式一，使用多线程的方式加载IMG + Handler，需要UI主线成更新界面
    public void showImageByThread(ImageView imageView, final String url){

        mImageView = imageView;
        mIconUrl = url;
        new Thread(){

            @Override
            public void run() {
                super.run();
                Bitmap bitmap = getBitmapFromUrl(url);
                Message message = Message.obtain();
                message.obj = bitmap;
                mHandler.sendMessage(message);
            }
        }.start();
    }

    public Bitmap getBitmapFromUrl(String urlString){
        Bitmap bitmap;

        InputStream is = null;
        try {
            URL url =  new URL(urlString);
            try {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                is = new BufferedInputStream(connection.getInputStream());
                bitmap = BitmapFactory.decodeStream(is);
                connection.disconnect();
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    //方式二，使用AysnTask实现加载IMG，加入缓存管理
    public void showImageAsycTask(ImageView imageView,String url){

        //从缓存中获取图片

        Bitmap bitmap = getBitmapFromCache(url);
        Log.d("michael","showImageAsycTask url = " + url + " bitmap = " + bitmap);
        if(bitmap == null){ //没有，去下载
            new NewsAyncTask(imageView,url).execute(url);
        }else{
            imageView.setImageBitmap(bitmap);
        }
    }

    private class NewsAyncTask extends AsyncTask<String,Void,Bitmap>{

        private  ImageView mImageView;
        private String mUrl;


        public NewsAyncTask(ImageView imageView, String url){
            mImageView = imageView;
            mUrl = url;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Log.d("michael"," Image Loader doInBackground ");
            String url = strings[0];
            //从网络获取到图片后，加入缓存
            Bitmap bitmap = getBitmapFromUrl(url);
            if(bitmap != null){
                addBitmapToCache(url,bitmap);
            }
            //这个bitmap给onPostExecute
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(mImageView.getTag().equals(mUrl)){
                mImageView.setImageBitmap(bitmap);
            }
        }
    }



    //ListView滑动优化，加载指定的start -> end间的图片
    //private ListView mListView;
    //private Set<News2AyncTask> mTask;


/*
    public void loadImages(int start,int end){
        for(int i = start; i < end; i++){
            String url = NewsAdapter.URLS[i];

            //从缓存中获取图片
            Bitmap bitmap = getBitmapFromCache(url);
            if(bitmap == null){ //没有，去下载
                Log.d("michael","loadImages  -> no pic ,downloading start = " + start + " end " + end);
                News2AyncTask task = new News2AyncTask(url);
                task.execute(url);
                mTask.add(task);
            }else{//有，直接显示
                Log.d("michael","loadImages  -> have,dispaly directly");
                ImageView imageView = (ImageView) mListView.findViewWithTag(url);
                imageView.setImageBitmap(bitmap);
            }

        }
    }

    public void cancelAllTasks(){
        if(mTask != null){
            for(News2AyncTask task:mTask){
                task.cancel(false);
            }
        }
    }
*/
/*

    //ListView优化新建aync task,用于一并显示所见项
    private class News2AyncTask extends AsyncTask<String,Void,Bitmap>{

        private  ImageView mImageView;
        private String mUrl;


        public News2AyncTask(String url){

            mUrl = url;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Log.d("michael","News2AyncTask  -> doInBackground");
            String url = strings[0];
            //从网络获取到图片后，加入缓存
            Bitmap bitmap = getBitmapFromUrl(url);
            if(bitmap != null){
                addBitmapToCache(url,bitmap);
            }
            //这个bitmap给onPostExecute
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Log.d("michael","News2AyncTask  -> onPostExecute");
            super.onPostExecute(bitmap);
            ImageView imageView = (ImageView) mListView.findViewWithTag(mUrl);
            if(imageView != null && bitmap != null){
                imageView.setImageBitmap(bitmap);
            }
            //显示后移除该任务
            mTask.remove(this);
        }
    }
    //与之前的不同在于，现在使用的是ListView滑动是调用loadImages()去异步加载数据，而并非直接开启线程
    public void showImageAsycTask2(ImageView imageView,String url){

        //从缓存中获取图片
        Bitmap bitmap = getBitmapFromCache(url);
        if(bitmap == null){ //没有，设置默认图片
            Log.d("michael","showImageAsycTask2 set ic_launcher");
            imageView.setImageResource(R.mipmap.ic_launcher);//注意这行于showImageAsycTask的变化
        }else{
            Log.d("michael","showImageAsycTask2 set network pic");
            imageView.setImageBitmap(bitmap);
        }
    }
*/

}
