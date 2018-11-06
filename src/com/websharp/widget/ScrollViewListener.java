package com.websharp.widget;

import android.widget.ScrollView;

public interface ScrollViewListener {
		  
	    void onScrollChanged(PullRefreshListView listView, int x, int y, int oldx, int oldy); 
	    
	    void onScrollChangedForScrollView(ScrollView scrollView, int x, int y, int oldx, int oldy); 
	  
	}  