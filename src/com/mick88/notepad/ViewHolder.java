package com.mick88.notepad;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

public class ViewHolder
{
	final View root;
	final SparseArray<View> views;
	
	public ViewHolder(View root, int cacheSize)
	{
		this.root = root;
		this.views = new SparseArray<View>(cacheSize);
		populateMapping(root);
	}
	
	public ViewHolder(View root)
	{
		this(root, 5);
	}
	
	private void populateMapping(View root)
	{
		views.put(root.getId(), root);
		if (root instanceof ViewGroup)
		{
			
			for (int i=0; i < ((ViewGroup) root).getChildCount(); i++)
				populateMapping(((ViewGroup) root).getChildAt(i));
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends View> T getViewById(int id)
	{
		T view = (T) views.get(id);
		if (view == null)
			throw new NullPointerException("Cannot find requested view id "+String.valueOf(id));
		return view;
	}
	
	public boolean hasView(int id)
	{
		return (views.get(id) != null);
	}
}
