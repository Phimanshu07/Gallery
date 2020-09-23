package com.internshala.galleryapplication;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {
   public Context context;
   ImageAdapter imageAdapter;
 static    ArrayList<ImageModel> myImages;
    public ImageAdapter(Context applicationContext, ArrayList<ImageModel> images) {
        this.context=applicationContext;
        this.myImages=images;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from( context ).inflate( R.layout.gallery_item,parent,false );
        return new MyViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,final int position) {

       final String galleryimage=myImages.get( position ).getPath();
       if(galleryimage!=null ){
           if(holder.imageView!=null)
            Glide.with( context ).asBitmap()
                    .load( galleryimage )
                    .into( holder.imageView );

       }

       holder.itemView.setOnClickListener( new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent intent=new Intent( context,ViewImage.class );
               intent.putExtra( "position",position );
               intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
               context.startActivity(intent);
           }
       } );
       holder.itemView.setOnLongClickListener( new View.OnLongClickListener() {
           @Override
           public boolean onLongClick(View v) {

               myImages.remove( position );
               notifyItemRemoved( position );

               notifyItemRangeChanged( position,myImages.size() );

               updatelist( myImages );
               return true;
           }
       } );
    }

    public byte[] getImages(String uri) {
        MediaMetadataRetriever retriever=new MediaMetadataRetriever();
        retriever.setDataSource( uri );
        byte[] art=retriever.getEmbeddedPicture();
        return art;
    }

    @Override
    public int getItemCount() {
        return myImages.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super( itemView );
            imageView=itemView.findViewById( R.id.imageitems );
        }
    }
    void updatelist(ArrayList<ImageModel> musicFilesArrayList){

        myImages=new ArrayList<>(  );
        myImages.addAll( musicFilesArrayList );
        notifyDataSetChanged();
    }
}
