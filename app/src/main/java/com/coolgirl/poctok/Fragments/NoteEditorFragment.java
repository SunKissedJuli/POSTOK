package com.coolgirl.poctok.Fragments;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import com.coolgirl.poctok.APIclasses.ApiClient;
import com.coolgirl.poctok.APIclasses.ApiController;
import com.coolgirl.poctok.APIclasses.Notes;
import com.coolgirl.poctok.APIclasses.Plant;
import com.coolgirl.poctok.APIclasses.UserLoginData;
import com.coolgirl.poctok.OnFragmentInteractionListener;
import com.coolgirl.poctok.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoteEditorFragment extends Fragment{
    EditText userNoteView;
    ImageView userImageView;
    TextView noteDate;
    Notes note = new Notes();
    String formattedDate;
    List<Plant> userPlantsList = new ArrayList<>();
    private OnFragmentInteractionListener mListener;
    private static final String TAG = "NoteEditorActivity";
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    ArrayAdapter<String> adapter;
    Spinner plantSpinner;
    int id, plantId = 0, noteId = 0;
    ApiController apiController;

    public NoteEditorFragment(){
        super(R.layout.fragment_note_editor);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_editor, container, false);

        userNoteView = view.findViewById(R.id.UserNoteView);
        userImageView = view.findViewById(R.id.UserImageView);
        noteDate = view.findViewById(R.id.NoteDate);
        plantSpinner = view.findViewById(R.id.PlantSpinner);
        apiController = ApiClient.start().create(ApiController.class);

        Date currentDate = new Date();
        formattedDate = sdf.format(currentDate);
        String data = getArguments().getString("noteData", null);
        if(data!=null){
            noteDate.setText(data);
            userNoteView.setText(getArguments().getString("noteText"));
        }else{noteDate.setText(formattedDate);}
        id = getArguments().getInt("id");
        noteId = getArguments().getInt("noteId", 0);
        plantId = getArguments().getInt("plantId", 0);

        GetUser();
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.d(TAG, "В NoteEditorActivity запущен onItemSelected");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        plantSpinner.setOnItemSelectedListener(itemSelectedListener);
        return view;
    }
    @SuppressLint("NonConstantResourceId")
    public void onClick(View v){
        switch (v.getId()){
            case R.id.BackButton:{
                if(userNoteView.getText().toString()==null||userNoteView.getText().toString().equals("")){
                    String newFragment = "UserPageFragment";
                    mListener.onOpenFragmentRequested(newFragment, id);
                } else{
                    SaveNote();
                    String newFragment = "UserPageFragment";
                    mListener.onOpenFragmentRequested(newFragment, id);
                }
            }
        }
    }

    public void GetUser(){
        Log.d(TAG, "В NoteEditorActivity запущен GetUser");
        try {
            Call<UserLoginData> call = apiController.getUserProfileData(id);
            call.enqueue(new Callback<UserLoginData>() {
                @Override
                public void onResponse(Call<UserLoginData> call, Response<UserLoginData> response) {
                    Log.d(TAG, "В NoteEditorActivity (GetUser) ответ от сервера: " + response.code());
                    if (response.code() == 200) {
                        note.user = response.body();
                        LoadSpinner(response.body().plants);
                    }
                }
                @Override
                public void onFailure(Call<UserLoginData> call, Throwable t) {
                    Log.d(TAG, "В NoteEditorActivity (GetUser) нет ответа от сервера " + t.getMessage());
                }
            });
        }
        catch(Exception ex){
            Log.d(TAG, "В NoteEditorActivity (GetUser) catch "+ ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void LoadSpinner(List<Plant> userPlantList){
        note.plantid = plantId;
        userPlantsList = userPlantList;
        List<String> userPlants = new ArrayList<>();
        int position = 1;
        int selectedId = 0;
        userPlants.add("Общая запись");
        for (Plant plant: userPlantList) {
            userPlants.add(plant.plantname);
            if(plant.plantid==note.plantid){
                selectedId = position;}
            else{
                position++;
            }
        }
        adapter= new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, userPlants);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        plantSpinner.setAdapter(adapter);
        plantSpinner.setSelection(selectedId);
    }

    public void SaveNote(){
        try{
            boolean IsPlant = false;
            for (Plant plant: userPlantsList) {
                if(plantSpinner.getSelectedItem().toString().equals(plant.plantname)){
                    note.plantid = plant.plantid;
                    IsPlant = true;
                }
            }
            if(!IsPlant){note.plantid = 0;}
            note.notetext = userNoteView.getText().toString();
            note.userid = id;
            note.notedata = formattedDate;
            if(noteId!=0){note.noteid = noteId;}
            Log.d(TAG, "В NoteEditorActivity (SaveNote) сейчас будет call");
            Call<Notes> call1 = apiController.postNote(note);
            call1.enqueue(new Callback<Notes>() {
                @Override
                public void onResponse(Call<Notes> call1, Response<Notes> response) {
                    Log.d(TAG, "В NoteEditorActivity (SaveNote) ответ от сервера: " + response.code());
                }
                @Override
                public void onFailure(Call<Notes> call1, Throwable t) {
                    Log.d(TAG, "В NoteEditorActivity (SaveNote) нет ответа от сервера " + t.getMessage());
                }
            });
        }
        catch(Exception ex){
            Log.d(TAG, "В NoteEditorActivity (SaveNote) catch "+ ex.getMessage());
            ex.printStackTrace();
        }
    }
}