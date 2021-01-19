package com.example.wastea;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ComplaintSActivity extends Fragment {
    private ListView mListv;
    List<ModelFile> mList =new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.complaints_activity,null,false);

        mListv = view.findViewById(R.id.mlist1);
        ModelFile file = new ModelFile();
        file.setName("ssssss");
        ModelFile file1 = new ModelFile();
        file1.setName("pppppp");
        mList.add(file);
        mList.add(file1);

        MyAdapters adapter = new MyAdapters(getActivity(),mList,ComplaintSActivity.this);
        mListv.setAdapter(adapter);
        return view;

    }

}
