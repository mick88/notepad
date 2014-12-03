package com.mick88.notepad;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.ContextThemeWrapper;

public class ConfirmationDialogFragment extends DialogFragment
{
	Bundle bundle;
	
	public interface ConfirmationDialogListener
	{
		public void onYesClicked(DialogFragment dialog, Bundle bundle);
		public void onNoClicked(DialogFragment dialog, Bundle bundle);
	}
	
	static ConfirmationDialogListener listener;
	
	@Override
	public void setArguments(Bundle bundle)
	{
		this.bundle.putAll(bundle);
		super.setArguments(bundle);
	}
	
	public ConfirmationDialogFragment()
	{
		this.bundle=new Bundle();
	}
	
	public static ConfirmationDialogFragment newInstance(Activity activity, String question, int id)
	{
		
		try
		{
			listener = (ConfirmationDialogListener)activity;
		}
		catch (ClassCastException e)
		{
			throw new ClassCastException(activity.toString()+" must implement ConfirmationDialogListener");
		}
		ConfirmationDialogFragment dialog = new ConfirmationDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("dialogId", id);
		bundle.putString("text", question);
		dialog.setArguments(bundle);
		return dialog;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.dialog));		
		
		builder.setMessage(bundle.getString("text"));
		builder.setTitle(R.string.app_name);
		
		builder.setPositiveButton(getString(android.R.string.yes), new OnClickListener()
		{			
			public void onClick(DialogInterface dialog, int which)
			{
				listener.onYesClicked(ConfirmationDialogFragment.this, bundle);
				
			}
		});
		builder.setNegativeButton(getString(android.R.string.no), new OnClickListener()
		{
			
			public void onClick(DialogInterface dialog, int which)
			{
				listener.onNoClicked(ConfirmationDialogFragment.this, bundle);
				
			}
		});
		
		return builder.create();
	}
}
