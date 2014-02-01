/**
 * 
 */
package com.mick88.notepad.notes;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.mick88.notepad.ApplicationNotepad;
import com.mick88.notepad.note_list.PopupLinkList;
import com.mick88.notepad.note_list.PopupLinkList.LinkListener;
import com.mick88.notepad.R;

/**
 * @author Michal
 * 
 */
public class Note
{
	private String text;
	String fileName = "";
	NoteManager noteManager = null;
	List<String> hyperlinks = new ArrayList<String>();

	public static final int BUFFER_SIZE = 512;

	public Note(NoteManager noteManager) {
		this.noteManager = noteManager;
		this.text = new String();
	}

	public Note(NoteManager noteManager, String content) {
		this(noteManager);
		if (content == null)
			setText(new String());
		else
			setText(new String(content));// text=new String(content);
	}

	public Note(NoteManager noteManager, CharSequence content) {
		this(noteManager, content.toString());
	}

	public String getText()
	{
		return text;
	}

	public boolean isEmpty()
	{
		return text.isEmpty();
	}

	private void findHyperlinks()
	{
		hyperlinks.clear();
		String[] words = this.text.toLowerCase(Locale.getDefault()).split("[\\s]");
		for (String word : words)
		{
			if (word.startsWith("http://") || word.startsWith("https://") || word.startsWith("www."))
//			if (word.matches(Patterns.WEB_URL))
			{
				if (word.startsWith("www.")) word = "http://"+word;
				hyperlinks.add(word.trim());
			}
		}
	}

	/**
	 * Gets the list of hyperlinks found in the text
	 */
	public List<String> getHyperlinks()
	{
		return hyperlinks;
	}

	public void setText(String t)
	{
		this.text = t;
		findHyperlinks();
	}

	public void appendText(String t)
	{
		setText(text.concat(t));
	}

	public String getStart(int numberCharacters, boolean ignoreNewline,
			boolean addEllipsis)
	{
		String s = ignoreNewline ? text : text.trim();
		int end = Math.min(numberCharacters, s.length());
		if (ignoreNewline == false)
		{
			int nlPos = s.indexOf('\n');
			if (nlPos > 0)
			{
				end = Math.min(end, nlPos);
			}
		}
		return addEllipsis ? s.substring(0, end) + "�" : s.substring(0, end);
	}

	public String toString()
	{
		return getStart(100, false, false);
		// text.substring(0, Math.min(100, text.length()));
	}

	public int getID()
	{
		return noteManager.notes.indexOf(this);
	}

	void delete(Context context)
	{
		if (TextUtils.isEmpty(fileName) == false)
		{
			context.deleteFile(fileName);
		}
	}

	public void popupHyperlinks(final SherlockFragmentActivity activity)
	{
		if (hyperlinks.isEmpty()) return;
		
		PopupLinkList dialog = new PopupLinkList();
		dialog.setHyperlinks(this.hyperlinks);
		dialog.setLinkListener(new LinkListener()
		{
			public void onLinkClicked(String url)
			{
				((ApplicationNotepad)activity.getApplication()).openLink(url);
			}
		});
		dialog.show(activity.getSupportFragmentManager(), "Link selector");
	}

	public void copyToClipboard(ApplicationNotepad application)
	{
		/*android.text.ClipboardManager manager = (android.text.ClipboardManager) application
				.getSystemService(Context.CLIPBOARD_SERVICE);
		manager.setText(text);*/
		application.setClipboardString(text);
	}

	public boolean findChanges(String newVersion)
	{
		return (text.equals(newVersion) == false);
	}

	public void share(Context context)
	{
		Intent share = new Intent(android.content.Intent.ACTION_SEND);
		share.setType("text/plain");
		share.putExtra(android.content.Intent.EXTRA_TITLE,
				context.getString(R.string.shareEntityName));
		share.putExtra(android.content.Intent.EXTRA_TEXT, text);
		share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(Intent.createChooser(share,
				context.getString(R.string.sharePromptText)));
	}

	public void saveToFile(Context context) throws IOException
	{
		if (TextUtils.isEmpty(fileName) == true)
		{
			fileName = noteManager.generateFilename();
		}
		FileOutputStream file = context.openFileOutput(fileName,
				Context.MODE_PRIVATE);

		byte[] buffer = text.getBytes();
		file.write(buffer);
		file.flush();
		file.close();

		// backupChanges(context);
	}

	/*
	 * @Deprecated void backupChanges(Context context) { BackupManager man = new
	 * BackupManager(context); man.dataChanged(); }
	 */

	/**
	 * Loads a note from file
	 * 
	 * @return
	 * @throws IOException
	 */
	public static Note newFromFile(NoteManager noteManager, Context context,
			String filename) throws IOException
	{
		FileInputStream inputFileStream = context.openFileInput(filename);
		StringBuffer stringBuffer = new StringBuffer();
		byte[] buffer = new byte[BUFFER_SIZE];
		while (inputFileStream.read(buffer) > 0)
		{
			String line = new String(buffer);
			stringBuffer.append(line);

			buffer = new byte[Note.BUFFER_SIZE];
		}

		Note n = new Note(noteManager, stringBuffer.toString().trim());
		n.fileName = filename;

		inputFileStream.close();

		return n;
	}
	
	public static Note newFromClipboard(NoteManager noteManager, ApplicationNotepad application)
	{
		CharSequence string = application.getClipboardString();
		if (string == null) return null;
		return new Note(noteManager, string);
	}
}
