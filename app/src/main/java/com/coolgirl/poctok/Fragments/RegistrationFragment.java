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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.coolgirl.poctok.APIclasses.ApiClient;
import com.coolgirl.poctok.APIclasses.ApiController;
import com.coolgirl.poctok.APIclasses.UserLoginData;
import com.coolgirl.poctok.OnFragmentInteractionListener;
import com.coolgirl.poctok.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationFragment extends Fragment {

    private LinearLayout bottomSheet, blackout;
    private static final String TAG = "RegistrationActivity";
    ApiController apiController;
    String login, password;
    EditText newUserName, newUserDescription;
    private OnFragmentInteractionListener mListener;

    public RegistrationFragment(){
        super(R.layout.fragment_registration);
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
        View v = inflater.inflate(R.layout.fragment_registration, container, false);

        bottomSheet = (LinearLayout) v.findViewById(R.id.BottomSheet);
        blackout = (LinearLayout) v.findViewById(R.id.Blackout);
        newUserName = v.findViewById(R.id.NewUserName);
        newUserDescription = v.findViewById(R.id.NewUserDescription);
        apiController = ApiClient.start().create(ApiController.class);
        login = getArguments().getString("login");
        password = getArguments().getString("password");
        return v;

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

    public void onClick(View v){
        switch (v.getId()){
            case R.id.SetImage:{
                Animation slide_up = AnimationUtils.loadAnimation(getContext(),
                        R.anim.slide_up);
                bottomSheet.setVisibility(LinearLayout.VISIBLE);
                blackout.setVisibility(LinearLayout.VISIBLE);
                bottomSheet.startAnimation(slide_up);
                break;
            }
            case R.id.NextButton:{
                try {
                    UserLoginData newuser = new UserLoginData(login, password);
                    newuser.username = newUserName.getText().toString();
                    newuser.userdescription = newUserDescription.getText().toString();

                    Call<UserLoginData> call = apiController.createUser(newuser);
                    call.enqueue(new Callback<UserLoginData>() {
                        @Override
                        public void onResponse(Call<UserLoginData> call, Response<UserLoginData> response) {
                            Log.d(TAG, "В RegistrationActivity ответ от сервера: " + response.code());
                            if (response.code() == 200) {
                                int id = response.body().userid;
                                String newFragment = "UserPageFragment";
                                mListener.onOpenFragmentRequested(newFragment, id);
                            }else {
                                Log.d(TAG, "В RegistrationActivity ответ от сервера: " + response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<UserLoginData> call, Throwable t) {
                            Log.d(TAG, "В RegistrationActivity нет ответа от сервера " + t.getMessage());
                        }
                    });

                }
                catch(Exception ex){
                    Log.d(TAG, "В RegistrationActivity catch "+ ex.getMessage());
                    ex.printStackTrace();
                }
                break;

            }
            case R.id.SetImageFromBottomSheet:{
                Animation slide_down = AnimationUtils.loadAnimation(getContext(),
                        R.anim.slide_down);
                bottomSheet.startAnimation(slide_down);
                bottomSheet.setVisibility(LinearLayout.GONE);
                blackout.setVisibility(LinearLayout.GONE);
                break;
            }
        }
    }
}