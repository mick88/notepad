package com.mick88.notepad.notes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.mick88.notepad.NotepadApplication;

import android.content.Context;

/**
 * Contains and manages list of notes
 * @author Michal
 *
 */
public class NoteManager
{
	Context context=null;
	ArrayList<Note> notes = new ArrayList<Note>();
	
	public NoteManager(Context context) 
	{
		this.context = context;
	}
	
	
	public boolean isEmpty()
	{
		return notes.isEmpty();
	}
	
	public int getNumNotes()
	{
		return notes.size();
	}
	
	public boolean getNoteByText(String t)
	{
		for (Note n : notes) if (n.findChanges(t) == false)
		{
			return true;
		}
		return false;
	}
	
	public boolean getNoteByText(CharSequence text)
	{
		return getNoteByText(text.toString());
	}
	
	public List<Note> getAllNotes()
	{
		return notes;
	}
	
	public List<String> getNotes()
	{
		ArrayList<String> strings = new ArrayList<String>(notes.size());
		for (Note note : notes) strings.add(note.toString());
		return strings;
	}
	
	public Note getNoteById(int id)
	{
		return notes.get(id);
	}
	
	public Note getNoteById(long id)
	{
		return getNoteById((int)id);
	}
	
	/**
	 * Deletes note file and removes it from list
	 * @param note
	 */
	public void deleteNote(Note note)
	{
		note.delete(context);
		notes.remove(note);
	}
	
	public void deleteNote(int index)
	{
		notes.get(index).delete(context);
		notes.remove(index);
	}
	
	public void addNote(Note note)
	{
		if (note == null || notes.contains(note)) return;
		note.noteManager = this;
		notes.add(note);	
		try
		{
			note.saveToFile(context);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private String generateFilename(int id)
	{
		String name = String.format(Locale.getDefault(), "Note_%d.txt", id);
		if (context.getFileStreamPath(name).exists() == true) return generateFilename(id+1);
		else return name;
	}
	
	String generateFilename()
	{
		return generateFilename(0);
	}
	
	public Note newFromClipboard(NotepadApplication application)
	{
		Note note = Note.newFromClipboard(this, application);
		if (note == null) return null;
		addNote(note);
		return note;
	}
	
	public void loadNotes()
	{
		String[] files = context.fileList();
		notes.clear();
		for (String fname:files)
		{
			try
			{
				notes.add(Note.newFromFile(this, context, fname));
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
	}
}
