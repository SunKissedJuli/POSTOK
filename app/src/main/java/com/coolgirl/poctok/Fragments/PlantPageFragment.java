package com.coolgirl.poctok.Fragments;

import static android.graphics.Paint.UNDERLINE_TEXT_FLAG;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.coolgirl.poctok.APIclasses.ApiClient;
import com.coolgirl.poctok.APIclasses.ApiController;
import com.coolgirl.poctok.APIclasses.Notes;
import com.coolgirl.poctok.APIclasses.Plant;
import com.coolgirl.poctok.APIclasses.UserLoginData;
import com.coolgirl.poctok.APIclasses.WateringSchedule;
import com.coolgirl.poctok.Adapters.NoteAdapter;
import com.coolgirl.poctok.Adapters.PlantAdapter;
import com.coolgirl.poctok.OnFragmentInteractionListener;
import com.coolgirl.poctok.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PlantPageFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    int id, plantId;
    TextView plantDiscription, plantName;
    Button polivePlantButton, photoPlantButton, notesPlantButton;
    NestedScrollView nestedSctollPlantPolive, nestedScrollPlantNote;
    ListView notePlantListView;
    ApiController apiController;
    LinearLayout plantBottomSheet, plantBlackout;
    RadioButton mon,tue, wed, thu, fri, sut, sun;
    String shedule = "11111111";
    public NoteAdapter noteAdapter;
    private List<Notes> notesList = new ArrayList<>();
    private List<Plant> plantsList = new ArrayList<>();
    private static final String TAG = "PlantPageFragment";

    public PlantPageFragment(){
        super(R.layout.fragment_plant_page);
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
        View view = inflater.inflate(R.layout.fragment_plant_page, container, false);
        polivePlantButton = view.findViewById(R.id.PolivePlantButton);
        photoPlantButton = view.findViewById(R.id.PhotoPlantButton);
        notesPlantButton = view.findViewById(R.id.NotesPlantButton);
        nestedSctollPlantPolive = view.findViewById(R.id.NestedSctollPlantPolive);
        nestedScrollPlantNote = view.findViewById(R.id.NestedScrollPlantNote);
        notePlantListView = view.findViewById(R.id.NotePlantListView);
        apiController = ApiClient.start().create(ApiController.class);
        plantDiscription = view.findViewById(R.id.PlantDiscription);
        plantName = view.findViewById(R.id.PlantName);
        plantBottomSheet = view.findViewById(R.id.UserBottomSheet);
        plantBlackout = view.findViewById(R.id.PlantBlackout);
        mon = view.findViewById(R.id.Mon);
        tue = view.findViewById(R.id.Tue);
        wed = view.findViewById(R.id.Wed);
        thu = view.findViewById(R.id.Thu);
        fri = view.findViewById(R.id.Fri);
        sut = view.findViewById(R.id.Sut);
        sun = view.findViewById(R.id.Sun);

        id = getArguments().getInt("id");
        plantId = getArguments().getInt("plantId");
        LoadPlantPage();
        return view;
    }


    public void onClick(View v){
        Log.d(TAG, "В PlantPage Нажали на button");
        switch (v.getId()) {
            case R.id.PhotoPlantButton: {
                photoPlantButton.setPaintFlags(UNDERLINE_TEXT_FLAG);
                polivePlantButton.setPaintFlags(polivePlantButton.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                notesPlantButton.setPaintFlags(notesPlantButton.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                nestedSctollPlantPolive.setVisibility(NestedScrollView.GONE);
                nestedScrollPlantNote.setVisibility(NestedScrollView.GONE);
                break;
            }
            case R.id.PolivePlantButton: {
                polivePlantButton.setPaintFlags(UNDERLINE_TEXT_FLAG);
                notesPlantButton.setPaintFlags(notesPlantButton.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                photoPlantButton.setPaintFlags(photoPlantButton.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                nestedSctollPlantPolive.setVisibility(NestedScrollView.VISIBLE);
                nestedScrollPlantNote.setVisibility(NestedScrollView.GONE);

                break;
            }
            case R.id.NotesPlantButton: {
                notesPlantButton.setPaintFlags(UNDERLINE_TEXT_FLAG);
                polivePlantButton.setPaintFlags(polivePlantButton.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                photoPlantButton.setPaintFlags(photoPlantButton.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                nestedSctollPlantPolive.setVisibility(NestedScrollView.GONE);
                nestedScrollPlantNote.setVisibility(NestedScrollView.VISIBLE);
                break;
            }
            case R.id.UserProfileButton:{
                String newFragment = "UserPageFragment";
                mListener.onOpenFragmentRequested(newFragment, id);
            }
            case R.id.PlusButton:{
                Animation slide_up = AnimationUtils.loadAnimation(getContext(),
                        R.anim.slide_up);
                plantBottomSheet.setVisibility(LinearLayout.VISIBLE);
                plantBlackout.setVisibility(LinearLayout.VISIBLE);
                plantBottomSheet.startAnimation(slide_up);
                break;
            }
            case R.id.NewUserNote:{
                Animation slide_down = AnimationUtils.loadAnimation(getContext(),
                        R.anim.slide_down);
                plantBottomSheet.setVisibility(LinearLayout.GONE);
                plantBlackout.setVisibility(LinearLayout.GONE);
                plantBottomSheet.startAnimation(slide_down);
                mListener.onOpenNoteFragment(id,0,null,null, 0);
                break;
            }
            case R.id.NewUserPlant:{
                Animation slide_down = AnimationUtils.loadAnimation(getContext(),
                        R.anim.slide_down);
                plantBottomSheet.setVisibility(LinearLayout.GONE);
                plantBlackout.setVisibility(LinearLayout.GONE);
                plantBottomSheet.startAnimation(slide_down);
                String newFragment = "AddPlantFragment";
                mListener.onOpenFragmentRequested(newFragment, id);
                break;

            }
            case R.id.NewUserPoliv:{
                Animation slide_down = AnimationUtils.loadAnimation(getContext(),
                        R.anim.slide_down);
                plantBottomSheet.setVisibility(LinearLayout.GONE);
                plantBlackout.setVisibility(LinearLayout.GONE);
                plantBottomSheet.startAnimation(slide_down);
                break;
            }
        }
    }

    private void LoadPlantPage(){
        try {
            Call<Plant> call = apiController.getPlantProfileData(plantId);
            call.enqueue(new Callback<Plant>() {
                @Override
                public void onResponse(Call<Plant> call, Response<Plant> response) {
                    Log.d(TAG, "В PlantPageFragment (LoadPlantPage) ответ от сервера: " + response.code());
                    if (response.code() == 200) {
                        plantName.setText(response.body().plantname.toString());
                        plantDiscription.setText(response.body().plantdescription.toString());
                        shedule = response.body().wateringSchedule.schedule;
                        List<RadioButton> radioButtons = new ArrayList<>();
                        radioButtons.add(mon); radioButtons.add(tue); radioButtons.add(wed);radioButtons.add(thu);radioButtons.add(fri);radioButtons.add(sut);radioButtons.add(sun);
                        for (int i = 0; i < 7; i++) {
                            char state = shedule.charAt(i);
                            if (state == '0') {radioButtons.get(i).setChecked(false);}
                            else if (state == '1') {radioButtons.get(i).setChecked(true);}
                        }
                        LoadListView(response.body().user.notes, response.body().user.plants);
                    }
                }

                @Override
                public void onFailure(Call<Plant> call, Throwable t) {
                    Log.d(TAG, "В PlantPageFragment (LoadPlantPage) нет ответа от сервера " + t.getMessage());
                }
            });
        }
        catch(Exception ex){
            Log.d(TAG, "В PlantPageFragment (LoadPlantPage) catch "+ ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void LoadListView(List<Notes> notes, List<Plant> plants){
        notesList.clear();
        plantsList.clear();
        for (Notes note: notes) {
            if(note.plantid==plantId)
                notesList.add(note);
            Log.d(TAG, "В UserPage notelist: " + note.notetext);
        }

        for (Plant plant: plants) {
            if(plant!=null){
            if(plantId==plant.plantid){
                plantsList.add(plant);}}
        }
        noteAdapter = new NoteAdapter(getContext(), notesList, plantsList);
        notePlantListView.setAdapter(noteAdapter);
        noteAdapter.notifyDataSetChanged();
        polivePlantButton.setPaintFlags(UNDERLINE_TEXT_FLAG);
        nestedSctollPlantPolive.setVisibility(NestedScrollView.VISIBLE);
    }
}