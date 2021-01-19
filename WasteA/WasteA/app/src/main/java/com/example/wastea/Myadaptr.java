package com.example.wastea;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

class Myadaptr extends BaseAdapter {
    List<ModelF> mlist;
    Context mcontext;

    public Myadaptr(Context context, List<ModelF> modelFList) {
        this.mcontext=context;
        this.mlist=modelFList;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int i) {
        return mlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater=(LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view1=layoutInflater.inflate(R.layout.listd,viewGroup,false);
        TextView textView=view1.findViewById(R.id.complnts);
        TextView textView1=view1.findViewById(R.id.nam);
        textView.setText(mlist.get(i).getKey());
        textView1.setText(mlist.get(i).getName());
        return view1;
    }
}
