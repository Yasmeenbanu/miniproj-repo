package com.example.wastea;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Query mDatabaseRef;
    private DatabaseReference fromPath;
    private DatabaseReference toPath;
    public static String key=null;
   public static int itemPosition;
    private Context mContext;
    private List<Upload> mUploads;
    private RecyclerView mrecycle;

    public Upload infodata ;


    public ImageAdapter(Context context, List<Upload> uploads, RecyclerView mRecyclerView) {
        mContext = context;
        mUploads = uploads;
        mrecycle=mRecyclerView;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
       // v.setOnClickListener(mOnClickListener);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, final int position) {
        Upload uploadCurrent = mUploads.get(position);
        holder.textViewName.setText(uploadCurrent.getName());
        Picasso.get()
                .load(uploadCurrent.getImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.imageView);
        infodata=mUploads.get(position);

        holder.mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int currentPosition = position;
                Toast.makeText(mContext, "OnClick Called at position " + position, Toast.LENGTH_SHORT).show();

            //    Toast.makeText(mContext,"Done!!"+key,Toast.LENGTH_SHORT).show();

                     fromPath= FirebaseDatabase.getInstance().getReference("uploads").child(key);
                     toPath=FirebaseDatabase.getInstance().getReference("DoneC").child(key);
                   moveFirebaseRecord( fromPath,  toPath);
                   removeItem(infodata);
            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           //     itemPosition = mrecycle.getChildLayoutPosition(view);
                Upload item = mUploads.get(position);

                String ss=item.getName();
                mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads").orderByChild("name");

                mDatabaseRef.equalTo(ss).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot foodSnapshot: dataSnapshot.getChildren()) {
                            // result
                            key = foodSnapshot.getKey();
                            Intent intent=new Intent(mContext,ComplainDetail.class);
                            intent.putExtra("key",key);
                            mContext.startActivity(intent);
                            Toast.makeText(mContext,"datss:"+key,Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(mContext,"err:"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }
  //  private final View.OnClickListener mOnClickListener = new MyOnClickListener();

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public ImageView imageView;
        public Button mDone;

        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name);
            imageView = itemView.findViewById(R.id.image_view_upload);
             mDone=itemView.findViewById(R.id.dn);

//             mDone.setOnClickListener(new View.OnClickListener() {
//                 @Override
//                 public void onClick(View view) {
//
//                     Toast.makeText(mContext,"Done!!"+key,Toast.LENGTH_SHORT).show();
//
//                     fromPath=FirebaseDatabase.getInstance().getReference("uploads").child(key);
//                     toPath=FirebaseDatabase.getInstance().getReference("DoneC").child(key);
//                     removeItem(infodata);
//
//                     moveFirebaseRecord( fromPath,  toPath);
//
////                    mrecycle.invalidate();
//                 }
//             });

        }
    }

    public void moveFirebaseRecord(final DatabaseReference fromPath1, final DatabaseReference toPath1)
    {
        fromPath1.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                toPath1.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener()
                {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                 if(databaseError!=null)
                 {
                     Toast.makeText(mContext,"Done!!",Toast.LENGTH_SHORT).show();

                 }

                        fromPath1.removeValue();
                    //   mUploads.remove(itemPosition);

//                        mAdapter.notifyDataSetChanged();
//                        mAdapter.notifyItemRemoved(ImageAdapter.itemPosition);

                    }

                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }


//    public class MyOnClickListener implements View.OnClickListener {
//        public void onClick(View view) {
//         itemPosition = mrecycle.getChildLayoutPosition(view);
//            Upload item = mUploads.get(itemPosition);
//
//            String ss=item.getName();
//            mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads").orderByChild("name");
//
//            mDatabaseRef.equalTo(ss).addListenerForSingleValueEvent(new ValueEventListener() {
//    @Override
//    public void onDataChange(DataSnapshot dataSnapshot) {
//        for (DataSnapshot foodSnapshot: dataSnapshot.getChildren()) {
//            // result
//            key = foodSnapshot.getKey();
//            Intent intent=new Intent(mContext,ComplainDetail.class);
//            intent.putExtra("key",key);
//            mContext.startActivity(intent);
//            Toast.makeText(mContext,"datss:"+key,Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    public void onCancelled(DatabaseError databaseError) {
//        Toast.makeText(mContext,"err:"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
//
//    }
//    });


//        }

//    }

    private void removeItem(Upload infoData) {

        int currPosition = mUploads.indexOf(infoData);
        mUploads.remove(currPosition);
        notifyItemRemoved(currPosition);
        notifyItemRangeChanged(currPosition, mUploads.size());


    }
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return position;
//    }
}