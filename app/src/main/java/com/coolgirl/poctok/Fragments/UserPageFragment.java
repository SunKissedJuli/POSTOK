package com.coolgirl.poctok.Fragments;

import static android.graphics.Paint.UNDERLINE_TEXT_FLAG;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.coolgirl.poctok.APIclasses.ApiClient;
import com.coolgirl.poctok.APIclasses.ApiController;
import com.coolgirl.poctok.APIclasses.Notes;
import com.coolgirl.poctok.APIclasses.Plant;
import com.coolgirl.poctok.APIclasses.UserLoginData;
import com.coolgirl.poctok.Adapters.NoteAdapter;
import com.coolgirl.poctok.OnFragmentInteractionListener;
import com.coolgirl.poctok.Adapters.PlantAdapter;
import com.coolgirl.poctok.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserPageFragment extends Fragment{

     private Button plantButton, photoButton, noteButton, plusButton;
     private TextView userProfileDiscription, userProfileName;
     private SharedPreferences sp;
     private LinearLayout userBottomSheet, userBlackout;
     private static final String TAG = "UserPage";
     ApiController apiController;
     ListView noteListView, plantListView;
     int id =0;
     public NoteAdapter noteAdapter;
     public PlantAdapter plantAdapter;
     private OnFragmentInteractionListener mListener;
     private List<Notes> notesList = new ArrayList<>();
     private List<Plant> plantsList = new ArrayList<>();
     NestedScrollView nestedScrollPlant, nestedScrollNote;

     public UserPageFragment(){
          super(R.layout.fragment_user_page);
     }

     @Override
     public void onCreate(@Nullable Bundle savedInstanceState) {
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
     @Override
     public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
          super.onViewCreated(view, savedInstanceState);
          super.onCreate(savedInstanceState);

     }

     @SuppressLint("MissingInflatedId")
     @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
          View view = inflater.inflate(R.layout.fragment_user_page, container, false);
          notesList.clear();
          plantsList.clear();
          userProfileDiscription = view.findViewById(R.id.UserProfileDiscription);
          userBottomSheet = (LinearLayout) view.findViewById(R.id.UserBottomSheet);
          userBlackout = (LinearLayout) view.findViewById(R.id.UserBlackout);
          userProfileName = view.findViewById(R.id.UserProfileName);
          photoButton = view.findViewById(R.id.PhotoButton);
          plantButton = view.findViewById(R.id.PlantButton);
          noteButton = view.findViewById(R.id.NotesButton);
          plusButton = view.findViewById(R.id.PlusButton);
          noteListView = view.findViewById(R.id.NoteListView);
          plantListView = view.findViewById(R.id.PlantListView);
          apiController = ApiClient.start().create(ApiController.class);
          nestedScrollPlant = view.findViewById(R.id.NestedScrollPlant);
          nestedScrollNote = view.findViewById(R.id.NestedScrollNote);
          id = getArguments().getInt("id");
          checkAccountData(id);
          return view;
     }

     public void onClick(View v){
          Log.d(TAG, "В UserPage Нажали на button");
          switch (v.getId()){
               case R.id.PhotoButton:{
                    photoButton.setPaintFlags(UNDERLINE_TEXT_FLAG);
                    plantButton.setPaintFlags( plantButton.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
                    noteButton.setPaintFlags( noteButton.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
                    nestedScrollNote.setVisibility(ListView.GONE);
                    nestedScrollPlant.setVisibility(ListView.GONE);
                    break;
               }
               case R.id.PlantButton: {
                    plantButton.setPaintFlags(UNDERLINE_TEXT_FLAG);
                    noteButton.setPaintFlags( noteButton.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
                    photoButton.setPaintFlags( photoButton.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
                    nestedScrollNote.setVisibility(ListView.GONE);
                    nestedScrollPlant.setVisibility(ListView.VISIBLE);

                    break;
               }
               case R.id.NotesButton:{
                    noteButton.setPaintFlags(UNDERLINE_TEXT_FLAG);
                    plantButton.setPaintFlags( plantButton.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
                    photoButton.setPaintFlags( photoButton.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
                    nestedScrollPlant.setVisibility(ListView.GONE);
                    nestedScrollNote.setVisibility(ListView.VISIBLE);
                    break;
               }
               case R.id.ChangeProfileButton:{

                    break;
               }
               case R.id.PlusButton:{
                    Animation slide_up = AnimationUtils.loadAnimation(getContext(),
                            R.anim.slide_up);
                    userBottomSheet.setVisibility(LinearLayout.VISIBLE);
                    userBlackout.setVisibility(LinearLayout.VISIBLE);
                    userBottomSheet.startAnimation(slide_up);

                    break;
               }
               case R.id.NewUserNote:{
                    Animation slide_down = AnimationUtils.loadAnimation(getContext(),
                            R.anim.slide_down);
                    userBottomSheet.setVisibility(LinearLayout.GONE);
                    userBlackout.setVisibility(LinearLayout.GONE);
                    userBottomSheet.startAnimation(slide_down);
                    mListener.onOpenNoteFragment(id,0,null,null, 0);
                    break;
               }
               case R.id.NewUserPlant:{
                    Animation slide_down = AnimationUtils.loadAnimation(getContext(),
                            R.anim.slide_down);
                    userBottomSheet.setVisibility(LinearLayout.GONE);
                    userBlackout.setVisibility(LinearLayout.GONE);
                    userBottomSheet.startAnimation(slide_down);
                    String newFragment = "AddPlantFragment";
                    mListener.onOpenFragmentRequested(newFragment, id);
                    break;

               }
               case R.id.NewUserPoliv:{
                    Animation slide_down = AnimationUtils.loadAnimation(getContext(),
                            R.anim.slide_down);
                    userBottomSheet.setVisibility(LinearLayout.GONE);
                    userBlackout.setVisibility(LinearLayout.GONE);
                    userBottomSheet.startAnimation(slide_down);
                    break;
               }

          }
     }

     public void checkAccountData(int UserId) {
          if(UserId!=0){
               try {
                    Call<UserLoginData> call = apiController.getUserProfileData(UserId);
                    call.enqueue(new Callback<UserLoginData>() {
                         @Override
                         public void onResponse(Call<UserLoginData> call, Response<UserLoginData> response) {
                              Log.d(TAG, "В UserPage ответ от сервера: " + response.code());
                              if (response.code() == 200) {
                                   userProfileDiscription.setText(response.body().userdescription);
                                   userProfileName.setText(response.body().username);
                                   id = response.body().userid;
                                   UserLoginData user = response.body();
                                   LoadListView(user.notes, user.plants);
                              }
                         }

                         @Override
                         public void onFailure(Call<UserLoginData> call, Throwable t) {
                              Log.d(TAG, "В UserPage нет ответа от сервера " + t.getMessage());
                         }
                    });

               }
               catch(Exception ex){
                    Log.d(TAG, "В UserPage catch "+ ex.getMessage());
                    ex.printStackTrace();
               }
          }
          else{

          }
     }
     public void LoadListView(List<Notes> notes, List<Plant> plants){
          notesList.clear();
          plantsList.clear();
          for (Notes note: notes) {
               notesList.add(note);
               Log.d(TAG, "В UserPage notelist: " + note.notetext);
          }
          for (Plant plant: plants) {
               plantsList.add(plant);
               Log.d(TAG, "В UserPage plantlist: " + plant.plantname);
          }
          plantAdapter = new PlantAdapter(getContext(),plantsList);
          noteAdapter = new NoteAdapter(getContext(), notesList, plantsList);
          plantListView.setAdapter(plantAdapter);
          noteListView.setAdapter(noteAdapter);
          plantAdapter.notifyDataSetChanged();
          noteAdapter.notifyDataSetChanged();
          plantButton.setPaintFlags(UNDERLINE_TEXT_FLAG);
          nestedScrollPlant.setVisibility(ListView.VISIBLE);
     }
}