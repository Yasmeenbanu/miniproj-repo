package com.example.wastea;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class DetailsAdapter extends BaseAdapter {
    Context mContxt;
    List<ModelF> modelFList;

    public DetailsAdapter(DetailsActivity detailsActivity, ArrayList<ModelF> modelFArrayList) {
        this.mContxt=detailsActivity;
        this.modelFList=modelFArrayList;
    }

    @Override
    public int getCount() {
        return modelFList.size();
    }

    @Override
    public Object getItem(int i) {
        return modelFList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater=(LayoutInflater) mContxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view1=layoutInflater.inflate(R.layout.detailsdesign,viewGroup,false);
        TextView textView=view1.findViewById(R.id.nameo);
        TextView textView1=view1.findViewById(R.id.mailo);
        TextView textView2=view1.findViewById(R.id.pho);

       textView.setText(modelFList.get(i).getName());
       textView1.setText(modelFList.get(i).getKey());
       textView2.setText(modelFList.get(i).getPhnum());

        return view1;
    }
}
