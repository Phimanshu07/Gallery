package com.internshala.galleryapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.internshala.galleryapplication.ImageAdapter.myImages;

public class ViewImage extends AppCompatActivity {

    Context context;
    TextView imageTitle;
    ImageView backBtn,mainImage,previousBtn,nextBtn,shareBtn,deleteBtn,editBtn,detailsBtn;
    int position=-1;
    Uri uri;
    ImageAdapter imageAdapter;
    String listimage;
    static ArrayList<ImageModel> listImage=new ArrayList<>(  );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        getSupportActionBar().hide();
        setContentView( R.layout.activity_view_image );
        initView();
        getImage();
        backBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );
        previousBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int currentposition=position=((position -1) < 0 ? (listImage.size()-1):(position-1));
                String listimage=myImages.get( currentposition ).getPath();
                imageTitle.setText( listImage.get( currentposition ).getFilename() );
                if(listImage!=null){
                    Glide.with( getApplicationContext() ).asBitmap()
                            .load( listimage )
                            .into( mainImage );
                }
                currentposition--;
            }
        } );
        nextBtn.setOnClickListener( new View.OnClickListener() {
            int currentposition;
            @Override
            public void onClick(View v) {

                currentposition = position = ((position + 1) % listImage.size());
                String listimage = myImages.get( currentposition ).getPath();
                imageTitle.setText( listImage.get( currentposition ).getFilename() );
                if (listImage != null) {
                    Glide.with( getApplicationContext() ).asBitmap()
                            .load( listimage )
                            .into( mainImage );
                }

            }



        } );
        shareBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position=getIntent().getIntExtra( "position",-1 );
                listImage=myImages;
                listimage=myImages.get( position ).getPath();
                Intent shareintent=new Intent( Intent.ACTION_SEND );
                shareintent.setType( "image/*" );
                File file=new File( listimage );

                if (Build.VERSION.SDK_INT < 24) {
                    uri = Uri.fromFile(file);
                } else {
                    uri = Uri.parse(file.getPath()); // My work-around for new SDKs, doesn't work in Android 10.
                }
                shareintent.putExtra( Intent.EXTRA_STREAM,uri );
                startActivity( Intent.createChooser( shareintent,"Share Image!" ) );
            }
        } );


    }




    private void initView() {
        backBtn=findViewById( R.id.back_btn );
        imageTitle=findViewById( R.id.Image_Title );
        mainImage=findViewById( R.id.main_image );
        nextBtn=findViewById( R.id.next );
        previousBtn=findViewById( R.id.previous );
        shareBtn=findViewById( R.id.share );
       /* editBtn=findViewById( R.id.edit );
        deleteBtn=findViewById( R.id.delete );
        detailsBtn=findViewById( R.id.details );*/
    }

    private void getImage(){

        position=getIntent().getIntExtra( "position",-1 );
        listImage=myImages;
        listimage=myImages.get( position ).getPath();
       imageTitle.setText( listImage.get( position ).getFilename() );
        if(listimage!=null){
            Glide.with( getApplicationContext() )
                    .load( listimage )
                    .into( mainImage );
        }
        else {

        }



    }
    void updatelist(ArrayList<ImageModel> musicFilesArrayList){
        listImage=new ArrayList<>(  );
        listImage.addAll( musicFilesArrayList );
        notifyAll();
    }



}