package com.coolgirl.poctok.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.coolgirl.poctok.APIclasses.ApiClient;
import com.coolgirl.poctok.APIclasses.ApiController;
import com.coolgirl.poctok.APIclasses.UserLoginData;
import com.coolgirl.poctok.OnFragmentInteractionListener;
import com.coolgirl.poctok.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AutorizeFragment extends Fragment {

    private Button toAutorizationButton, registrationButton;
    private EditText userPasswordRepeat, userPassword, userLogin;

    private TextView passwordRepeatView, nameRegistration;
    private LinearLayout mainLayout;
    private SharedPreferences sp;
    ApiController apiController;
    private OnFragmentInteractionListener mListener;
    private static final String TAG = "MainActivity";

    public AutorizeFragment(){
        super(R.layout.fragment_autorize);
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

        apiController = ApiClient.start().create(ApiController.class);
        View v = inflater.inflate(R.layout.fragment_autorize, container, false);
        toAutorizationButton = v.findViewById(R.id.ToAutorizationButton);
        registrationButton = v.findViewById(R.id.RegistrationButton);
        userPasswordRepeat = v.findViewById(R.id.UserPasswordRepeat);
        passwordRepeatView = v.findViewById(R.id.PasswordRepeatView);
        nameRegistration = v.findViewById(R.id.NameRegistration);
        userLogin = v.findViewById(R.id.UserLogin);
        userPassword = v.findViewById(R.id.UserPassword);
        mainLayout = (LinearLayout)  v.findViewById(R.id.MainLayout);
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
                case R.id.ToAutorizationButton:{
                    Log.d(TAG, "В MainActivity нажали сменную кнопку");
                    if(toAutorizationButton.getText()!="Ещё нет аккаунта?"){
                        mainLayout.removeView(passwordRepeatView);
                        mainLayout.removeView(userPasswordRepeat);
                        registrationButton.setText("войти");
                        nameRegistration.setText("АВТОРИЗАЦИЯ");
                        toAutorizationButton.setText("Ещё нет аккаунта?");
                    }else{
                        mainLayout.removeView(registrationButton);
                        mainLayout.addView(passwordRepeatView);
                        mainLayout.addView(userPasswordRepeat);
                        mainLayout.addView(registrationButton);
                        registrationButton.setText("Зарегистрироваться");
                        toAutorizationButton.setText("Уже есть аккаунт?");
                    }
                    break;
                }
                case R.id.RegistrationButton:{
                    Log.d(TAG, "В MainActivity нажали regbutton");
                    if(registrationButton.getText()=="войти"){
                        try {
                            String login = userLogin.getText().toString();
                            String password = userPassword.getText().toString();
                            Call<UserLoginData> call = apiController.autorizeUser(login, password);
                            call.enqueue(new Callback<UserLoginData>() {
                                @Override
                                public void onResponse(Call<UserLoginData> call, Response<UserLoginData> response) {
                                    Log.d(TAG, "В MainActivity ответ от сервера: " + response.code());
                                    if (response.code() == 200) {
                                        int id = response.body().userid;
                                        String newFragment = "UserPageFragment";
                                        mListener.onOpenFragmentRequested(newFragment, id);
                                    }else{
                                        Log.d(TAG, "В MainActivity ответ от сервера: " + response.message());
                                        Log.d(TAG, "В MainActivity ответ от сервера: " + response.body());
                                        Log.d(TAG, "В MainActivity ответ от сервера: " + response.errorBody());
                                    }
                                }

                                @Override
                                public void onFailure(Call<UserLoginData> call, Throwable t) {
                                    Log.d(TAG, "В MainActivity нет ответа от сервера " + t.getMessage());
                                }
                            });

                        }
                        catch(Exception ex){
                            Log.d(TAG, "В MainActivity catch "+ ex.getMessage());
                            ex.printStackTrace();
                        }
                    }
                    else{
                        if(userPasswordRepeat.getText().toString().equals(userPassword.getText().toString())){
                          //  String newFragment = "RegistrationFragment";
                            mListener.onOpenFragmentRegistration(userLogin.getText().toString(), userPassword.getText().toString());
                          //  openAnotherFragment(newFragment, 0, userLogin.getText().toString(), userPassword.getText().toString(), 0, null,null, 0);
                        }else {
                            Toast.makeText(getContext(), "Введите одинаковые пароли!", Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                }
            }
        }


}