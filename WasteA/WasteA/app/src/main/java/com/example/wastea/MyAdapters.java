package com.example.wastea;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

class MyAdapters extends BaseAdapter {

    List<ModelFile> modelFiles;
    Context context;
    ComplaintSActivity mlistA;
    public MyAdapters(FragmentActivity activity, List<ModelFile> mList, ComplaintSActivity complaintSActivity) {
        this.modelFiles=mList;
        this.mlistA=complaintSActivity;
        this.context=activity;
    }

    @Override
    public int getCount() {
        return modelFiles.size();
    }

    @Override
    public Object getItem(int i) {
        return modelFiles.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view1=layoutInflater.inflate(R.layout.modelres,null);
        TextView textView=(TextView) view1.findViewById(R.id.cmp);
        Button button=view1.findViewById(R.id.prcd);
        Button button1=view1.findViewById(R.id.cancel);

        ModelFile modelFile=modelFiles.get(i);
        textView.setText(modelFile.getName());
        return view1;
    }
}
