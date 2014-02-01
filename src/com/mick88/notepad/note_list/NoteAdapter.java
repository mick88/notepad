package com.mick88.notepad.note_list;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mick88.notepad.R;
import com.mick88.notepad.ViewHolder;
import com.mick88.notepad.notes.Note;

public class NoteAdapter extends ArrayAdapter<Note>
{

	public NoteAdapter(Context context, List<Note> objects)
	{
		super(context, 0, 0, objects);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent)
	{
		final ViewHolder viewHolder;
		if (view == null)
		{
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.note_list_tile, parent, false);
			viewHolder = new ViewHolder(view);
			view.setTag(viewHolder);
		}
		else viewHolder = (ViewHolder) view.getTag();
		
		Note note = getItem(position);
		
		TextView tvNote = viewHolder.getViewById(R.id.noteTitle);
		tvNote.setText(note.getText());
		
		
		return view;
	}
}
