package com.example.wastea;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ComplaintGActivity extends Fragment {
    ImageView mGt;
    private ListView mListv;
    List<ModelFile> mList = new ArrayList<>();
    private static FirebaseUser currentUser;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private StorageReference imagepath;

    public RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;

    private ProgressBar mProgressCircle;

    private DatabaseReference mDatabaseRef;
    private List<Upload> mUploads;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.complaintg_activity, null, false);


        mRecyclerView =view. findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



        mUploads = new ArrayList<>();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");


        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUploads.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    mUploads.add(upload);

                }

            //    mUploads.remove(ImageAdapter.itemPosition);
//
                mAdapter = new ImageAdapter(getContext(), mUploads,mRecyclerView);
               // mAdapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                //mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
              //  mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });



        //    mGt = view.findViewById(R.id.img);

//        currentUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        database = FirebaseDatabase.getInstance();
//        dbRef = database.getReference("complaints").child("cd@gmail,com");

        return view;

}


}
