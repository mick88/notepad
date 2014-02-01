package com.mick88.notepad.note_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mick88.notepad.R;
import com.mick88.notepad.TwoWayHashMap;
import com.mick88.notepad.ViewHolder;
import com.mick88.notepad.notes.Note;
import com.mick88.notepad.notes.NoteManager;

public class NoteAdapter extends ArrayAdapter<Note>
{
	Note expandedNote=null;
	private final TwoWayHashMap<Note, View> displayedNotes;
	
	public NoteAdapter(Context context, NoteManager noteManager)
	{
		super(context, 0, 0, noteManager.getAllNotes());
		displayedNotes = new TwoWayHashMap<Note, View>();
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
		
		final Note note = getItem(position);
		final View finalView = view;
		
		TextView tvNote = viewHolder.getViewById(R.id.noteTitle);
		View btnExpand = viewHolder.getViewById(R.id.btn_tile_expand);
		
		tvNote.setText(note.getText());
		
		if (isExpanded(note))
		{
			expandNote(note, view);
		}
		else
		{
			collapseNote(note, view);
		}
		
		btnExpand.setOnClickListener(new OnClickListener()
		{
			
			public void onClick(View v)
			{
				if (isExpanded(note))
					collapseNote(note, finalView);
				else 
					expandNote(note, finalView);
			}
		});
		
		displayedNotes.put(note, finalView);
		return view;
	}
	
	private boolean isExpanded(Note note)
	{
		return note != null && note == expandedNote;
	}
	
	void collapseNote(Note note, View view)
	{
		if (note == null || view == null) return;
		expandedNote=null;
		ViewHolder holder = (ViewHolder) view.getTag();
		
		holder.getViewById(R.id.tile_options).setVisibility(View.GONE);
		
		TextView tvTitle = holder.getViewById(R.id.noteTitle);
		tvTitle.setMaxLines(getContext().getResources().getInteger(R.integer.max_tile_lines));
		
		ImageView btnExpand = holder.getViewById(R.id.btn_tile_menu);
		btnExpand.setImageResource(R.drawable.icon_dark_expland);
	}
	
	void expandNote(Note note, View view)
	{
		if (expandedNote != null) collapseNote(expandedNote, displayedNotes.get(note));
		ViewHolder holder = (ViewHolder) view.getTag();
		
		holder.getViewById(R.id.tile_options).setVisibility(View.VISIBLE);
		
		TextView tvTitle = holder.getViewById(R.id.noteTitle);
		tvTitle.setMaxLines(getContext().getResources().getInteger(R.integer.max_tile_lines_expanded));
		
		ImageView btnExpand = holder.getViewById(R.id.btn_tile_menu);
		btnExpand.setImageResource(R.drawable.icon_dark_collapse);
		
		expandedNote = note;
	}
}
