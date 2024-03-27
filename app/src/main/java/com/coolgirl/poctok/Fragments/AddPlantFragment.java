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
import android.widget.EditText;
import android.widget.RadioButton;

import com.coolgirl.poctok.APIclasses.ApiClient;
import com.coolgirl.poctok.APIclasses.ApiController;
import com.coolgirl.poctok.APIclasses.Plant;
import com.coolgirl.poctok.APIclasses.UserLoginData;
import com.coolgirl.poctok.APIclasses.WateringSchedule;
import com.coolgirl.poctok.OnFragmentInteractionListener;
import com.coolgirl.poctok.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddPlantFragment extends Fragment {
   private OnFragmentInteractionListener mListener;
   EditText newPlantName, newPlantDiscription,newPlantCountOfWaterinGraphic;
   int id;
   Plant plant = new Plant();
   ApiController apiController;
   RadioButton mon,tue, wed, thu, fri, sut, sun;
   WateringSchedule wateringSchedule = new WateringSchedule();
   private static final String TAG = "AddPlantFragment";

   public AddPlantFragment(){
      super(R.layout.fragment_add_plant);
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
      View view = inflater.inflate(R.layout.fragment_add_plant, container, false);
      newPlantName = view.findViewById(R.id.NewPlantName);
      newPlantDiscription = view.findViewById(R.id.NewPlantDiscription);
      newPlantCountOfWaterinGraphic = view.findViewById(R.id.NewPlantDiscription);
      mon = view.findViewById(R.id.Mon);
      tue = view.findViewById(R.id.Tue);
      wed = view.findViewById(R.id.Wed);
      thu = view.findViewById(R.id.Thu);
      fri = view.findViewById(R.id.Fri);
      sut = view.findViewById(R.id.Sut);
      sun = view.findViewById(R.id.Sun);
      apiController = ApiClient.start().create(ApiController.class);
      id = getArguments().getInt("id");
      return view;
   }
   @SuppressLint("NonConstantResourceId")
   public void onClick(View v){
      switch (v.getId()){
         case R.id.SaveNewPlantButton:{
            GetUser();
         }
      }
   }
   public void GetUser(){
      Log.d(TAG, "В AddPlantFragment запущен GetUser");
      try {
         plant.plantdescription = newPlantDiscription.getText().toString();
         plant.userid = id;
         plant.plantname = newPlantName.getText().toString();
         Plant p = new Plant();
         p.plantdescription = plant.plantdescription;
         p.plantname = plant.plantname;
         p.userid = id;

         wateringSchedule.plant = p;
         Call<UserLoginData> call = apiController.getUserProfileData(id);
         call.enqueue(new Callback<UserLoginData>() {
            @Override
            public void onResponse(Call<UserLoginData> call, Response<UserLoginData> response) {
               Log.d(TAG, "В AddPlantFragment (GetUser) ответ от сервера: " + response.code());
               if (response.code() == 200) {
                  p.user = response.body();
                  plant.user = response.body();
                  plant.user.notes = null;
                  plant.user.plants = null;
                  SavePlant();
               }
            }
            @Override
            public void onFailure(Call<UserLoginData> call, Throwable t) {
               Log.d(TAG, "В AddPlantFragment (GetUser) нет ответа от сервера " + t.getMessage());
            }
         });
      }
      catch(Exception ex){
         Log.d(TAG, "В AddPlantFragment (GetUser) catch "+ ex.getMessage());
         ex.printStackTrace();
      }
   }

   public void SavePlant(){
      try{
         String shedule = CheckRadioButton();

         wateringSchedule.schedule = shedule;
         wateringSchedule.plantid = plant.plantid;
         wateringSchedule.userid = plant.userid;
         plant.wateringSchedule = wateringSchedule;
         plant.wateringHistories = null;

         Log.d(TAG, "В AddPlantFragment (SavePlant) сейчас будет call");
         Call<Plant> call1 = apiController.postPlant(plant);
         call1.enqueue(new Callback<Plant>() {
            @Override
            public void onResponse(Call<Plant> call1, Response<Plant> response) {
               Log.d(TAG, "В AddPlantFragment (SavePlant) ответ от сервера: " + response.code());
               if (response.code() == 200) {
                  mListener.onOpenPlantFragment(id, response.body().plantid);
               }
            }
            @Override
            public void onFailure(Call<Plant> call1, Throwable t) {
               Log.d(TAG, "В AddPlantFragment (SavePlant) нет ответа от сервера " + t.getMessage());
            }
         });
      }
      catch(Exception ex){
         Log.d(TAG, "В AddPlantFragment (SavePlant) catch "+ ex.getMessage());
         ex.printStackTrace();
      }
   }
   private String CheckRadioButton(){
      StringBuilder result = new StringBuilder();
      List<RadioButton> radioButtons = new ArrayList<>();
      radioButtons.add(mon); radioButtons.add(tue); radioButtons.add(wed);radioButtons.add(thu);radioButtons.add(fri);radioButtons.add(sut);radioButtons.add(sun);
      for (RadioButton radioButton : radioButtons) {
         if (radioButton.isChecked()) {
            result.append("1");
         } else {
            result.append("0");
         }
      }
      Log.d(TAG, "В AddPlantFragment (CheckRadioButton) return: " + result.toString());
      return result.toString();
   }


}