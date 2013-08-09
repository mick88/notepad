package com.mick88.notepad;

import notes.NoteManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

public class ApplicationNotepad extends Application
{
	NoteManager noteManager = null;
	
	public ApplicationNotepad() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate()
	{
		this.noteManager = new NoteManager(getApplicationContext());
		super.onCreate();
	}

	public NoteManager getNoteManager()
	{
		return noteManager;
	}
	
	public void openLink(String url)
	{
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
	
	@SuppressWarnings("deprecation")
	public CharSequence getClipboardString()
	{
		android.text.ClipboardManager manager = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		CharSequence string = manager.getText();
		if (TextUtils.isEmpty(string)) return null;
		return string;
	}
	
	@SuppressWarnings("deprecation")
	public void setClipboardString(CharSequence string)
	{
		android.text.ClipboardManager manager = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		manager.setText(string);
	}
}
