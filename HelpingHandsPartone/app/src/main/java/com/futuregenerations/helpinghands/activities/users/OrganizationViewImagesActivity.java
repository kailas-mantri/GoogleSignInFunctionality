package com.futuregenerations.helpinghands.activities.users;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.futuregenerations.helpinghands.models.UserOrganizationImagesAdapter;
import com.futuregenerations.helpinghands.R;
import com.tuyenmonkey.mkloader.MKLoader;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OrganizationViewImagesActivity extends AppCompatActivity {

    GridView gridView;
    SubsamplingScaleImageView imageView;
    int position, currentPosition;
    List<String> extraImages;
    MKLoader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_view_images);
        extraImages = new ArrayList<>();

        gridView = findViewById(R.id.imagesGridView);
        imageView = findViewById(R.id.imageExtraOrganization);
        loader = findViewById(R.id.loader);

        Intent intent = getIntent();
        extraImages = intent.getStringArrayListExtra("extraImages");
        position = intent.getIntExtra("position",0);
        currentPosition = position;

        getImageToView image = new getImageToView(extraImages.get(position));
        image.execute();

        UserOrganizationImagesAdapter adapter = new UserOrganizationImagesAdapter(this,extraImages);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentPosition!=position) {
                    getImageToView image = new getImageToView(extraImages.get(position));
                    image.execute();
                    currentPosition = position;
                }
            }
        });
    }

    public void imageBack(View view) {
        onBackPressed();
    }

    public class getImageToView extends AsyncTask<String,Void,Bitmap> {
        String imageUrl;
        Bitmap bitmap;
        public getImageToView(String imageUrl) {
            this.imageUrl = imageUrl;
        }
        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loader.setVisibility(View.VISIBLE);
                    }
                });
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream stream = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(stream);
                Log.e("IMAGE_PATH",imageUrl);
                bitmap = myBitmap;
            }
            catch (final Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loader.setVisibility(View.GONE);
                    }
                });
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(final Bitmap bitmap) {
            super.onPostExecute(bitmap);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (bitmap!=null) {
                        ImageSource source = ImageSource.bitmap(bitmap);
                        imageView.setImage(source);
                        loader.setVisibility(View.GONE);
                    }
                }
            });
        }
    }
}
