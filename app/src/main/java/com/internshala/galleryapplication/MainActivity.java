package com.internshala.galleryapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;

import java.util.ArrayList;
import java.util.Currency;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 123;
    RecyclerView recyclerView;
    Context context;
    private String Sort_Pref_Order="SortOrder";
  static   ArrayList<ImageModel> images;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        permission();
    }

    private void permission() {
        if(ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE )+
                ContextCompat.checkSelfPermission( getApplicationContext(),Manifest.permission.READ_EXTERNAL_STORAGE )
                != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions( MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE );
        }
        else {
            images=getAllImages(this);
            initMethod();
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.main,menu );
        MenuItem menuitem=menu.findItem( R.id.sort_by );

        return super.onCreateOptionsMenu( menu );
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        if(requestCode==REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                images=getAllImages( this );
                initMethod();
            }
        }
    }

    private void initMethod() {
        recyclerView = findViewById( R.id.recyclerview );
        recyclerView.setHasFixedSize( true );
        if (!((images.size()) < 1)) {
            ImageAdapter imageAdapter = new ImageAdapter( getApplicationContext(), images );
            recyclerView.setAdapter( imageAdapter );
            recyclerView.setLayoutManager( new GridLayoutManager(  context,2) );
        }
    }

    private ArrayList<ImageModel> getAllImages(MainActivity mainActivity) {
        SharedPreferences preferences=getSharedPreferences( Sort_Pref_Order,MODE_PRIVATE );
        String sortOrder=preferences.getString( "sorting","sortByName" );
        String order=null;
     ArrayList<ImageModel> temp=new ArrayList<>(  );
        Uri uri= MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        switch (sortOrder){
            case "sortByName":
                order=MediaStore.MediaColumns.DISPLAY_NAME + " ASC";
                break;
            case "sortByDate":
                order=MediaStore.MediaColumns.DATE_ADDED + " DESC";
                break;
            case "sortBySize":
                order=MediaStore.MediaColumns.SIZE + " DESC";
                break;
        }
        String[] prpjection={
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME
        };
        Cursor cursor= mainActivity.getContentResolver().query(uri,prpjection,null,null,order  );
        if(cursor!=null){
            while ((cursor.moveToNext())){
                String id=cursor.getString(  0 );
                String path=cursor.getString( 1 );
                String filename=cursor.getString( 2 );

                ImageModel imageModel=new ImageModel(id,path,filename);
                Log.e("Path:"+path,"Album:"+id);
                temp.add( imageModel );
            }
            cursor.close();
        }
        return temp;
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SharedPreferences.Editor editor=getSharedPreferences( Sort_Pref_Order,MODE_PRIVATE ).edit();
        switch (item.getItemId()){
            case R.id.by_name:
                editor.putString( "sorting","sortByName" );
                editor.apply();
                this.recreate();
                break;
            case R.id.by_date:
                editor.putString( "sorting","sortByDate" );
                editor.apply();
                this.recreate();
                break;
            case R.id.by_size:
                editor.putString( "sorting","sortBySize" );
                editor.apply();
                this.recreate();
                break;
        }
        return super.onOptionsItemSelected( item );
    }

}