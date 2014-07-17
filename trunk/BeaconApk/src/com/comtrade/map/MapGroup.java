package com.comtrade.map;

import java.util.ArrayList;

import com.comtrade.ilserver.tasks.BeaconServer;

import android.content.Context;
import android.view.ViewGroup;

public class MapGroup extends ViewGroup {

	TouchView tView;
	DotView dView;
	ArrayList<BeaconServer> listaBeacona = new ArrayList<>();
	
	
	
	public MapGroup(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		tView = new TouchView(context);
		
		dView = new DotView(context,this);
		
		addView(tView);
//		addView(dView);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
        for(int i = 0 ; i < getChildCount() ; i++){
            getChildAt(i).layout(l, t, r, b);
        }
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
}
