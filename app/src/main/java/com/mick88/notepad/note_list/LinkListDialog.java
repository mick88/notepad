package com.mick88.notepad.note_list;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;

public class LinkListDialog extends DialogFragment implements OnClickListener
{
	public interface LinkListener
	{
		public void onLinkClicked(String url);
	}
	LinkListener listener=null;
	List<String> hyperlinks=new ArrayList<String>();
	
	public void setHyperlinks(List<String> hyperlinks)
	{
		this.hyperlinks = hyperlinks;
	}
	
	public void setLinkListener(LinkListener listener)
	{
		this.listener = listener;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		String [] items = new String[hyperlinks.size()];
		items = hyperlinks.toArray(items);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());		
		builder.setTitle("Open hyperlink:")
		.setItems(items, this)
		.setNegativeButton(android.R.string.cancel, null);

		return builder.create();
	}

	public void onClick(DialogInterface dialog, int which)
	{
		String url =  hyperlinks.get(which);
		if (listener != null)
		{
			listener.onLinkClicked(url);
		}
	}
}
