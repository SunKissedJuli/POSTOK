package com.coolgirl.poctok.Adapters;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.coolgirl.poctok.APIclasses.Notes;
import com.coolgirl.poctok.APIclasses.Plant;
import com.coolgirl.poctok.OnFragmentInteractionListener;
import com.coolgirl.poctok.R;

import java.util.List;

public class NoteAdapter extends BaseAdapter{
    private Context mContext;
    private List<Notes> notesList;
    private List<Plant> plantsList;
    private OnFragmentInteractionListener mListener;


    public NoteAdapter(Context mContext, List<Notes> notesList, List<Plant> plantList) {
        this.mContext = mContext;
        this.notesList = notesList;
        this.plantsList = plantList;
        mListener =  (OnFragmentInteractionListener)mContext;
    }

    //количество элементов в списке
    @Override
    public int getCount() {
        return notesList.size();
    }

    // объект Note
    @Override
    public Object getItem(int i) {
        return notesList.get(i);
    }

    // NoteId
    @Override
    public long getItemId(int i) {
        return notesList.get(i).userid;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") View v = View.inflate(mContext, R.layout.note_item, null);
        TextView noteText = v.findViewById(R.id.NoteItemText);
        TextView noteData = v.findViewById(R.id.NoteItemData);
        TextView plantName = v.findViewById(R.id.NoteItemPlantName);
        Notes note = notesList.get(i);
        noteText.setText(note.notetext);
        noteData.setText(note.notedata);
            for (Plant plant: plantsList) {
                if(plant.plantid== note.plantid)
                    plantName.setText(plant.plantname);
            }
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("NoteAdapter", "В NoteAdapter Нажали на button");
                mListener.onOpenNoteFragment(note.userid, note.noteid, note.notetext, note.notedata, note.plantid);
            }
        });
        return v;
    }
}
