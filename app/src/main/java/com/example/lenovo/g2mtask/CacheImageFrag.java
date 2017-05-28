package com.example.lenovo.g2mtask;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Cache;
import com.squareup.picasso.Picasso;


public class CacheImageFrag extends Fragment {

    public CacheImageFrag() {
        // Required empty public constructor
    }


    private LruCache<String, Bitmap> mMemoryCache;
    ImageView mImageView;
    Button load_network_img_btn ,get_chached_img_btn,remove_network_img_btn,chache_img_btn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static final String IMG_URL="http://colorboration.de/wp-content/uploads/photo-gallery/Colory/Dockx-Colory-Krebs.jpg";
    private static  final String BITMAP_KEY="my_bitmap" ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.fragment_cache_image, container, false);
       mImageView=(ImageView)view.findViewById(R.id.imageView);

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };


        load_network_img_btn= (Button) view.findViewById(R.id.load_network_img_btn);
        chache_img_btn=(Button) view.findViewById(R.id.chache_img_btn);
        remove_network_img_btn=(Button) view.findViewById(R.id.remove_network_img_btn);
        get_chached_img_btn=(Button) view.findViewById(R.id.get_chached_img_btn);
        load_network_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso.with(getContext()).load(IMG_URL).error(R.mipmap.ic_launcher).into(mImageView);
            }
        });
        chache_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = ((BitmapDrawable)mImageView.getDrawable()).getBitmap();
              //  Bitmap bitmap=mImageView.getDrawingCache();
                addBitmapToMemoryCache(BITMAP_KEY,bitmap);
            }
        });
        remove_network_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mImageView.setImageResource(R.mipmap.ic_launcher);
            }
        });
              get_chached_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mImageView.setImageBitmap(getBitmapFromMemCache(BITMAP_KEY
                ));
            }
        });
        return view;

    }


    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

}
