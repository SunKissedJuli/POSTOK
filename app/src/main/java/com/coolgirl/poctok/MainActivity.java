package com.coolgirl.poctok;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.coolgirl.poctok.Fragments.AddPlantFragment;
import com.coolgirl.poctok.Fragments.AutorizeFragment;
import com.coolgirl.poctok.Fragments.NoteEditorFragment;
import com.coolgirl.poctok.Fragments.PlantPageFragment;
import com.coolgirl.poctok.Fragments.RegistrationFragment;
import com.coolgirl.poctok.Fragments.UserPageFragment;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {


    AutorizeFragment autorizeFragment;
    AddPlantFragment addPlantFragment;
    UserPageFragment userPageFragment;
    RegistrationFragment registrationFragment;
    NoteEditorFragment noteEditorFragment;
    PlantPageFragment plantPageFragment;
    String OpenedFragment = "0";
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IsAutorize();
    }

    public void onClick(View v) {
        try {
            if (OpenedFragment.equals("AutorizeFragment")) {autorizeFragment.onClick(v);}
            else if (OpenedFragment.equals("UserPageFragment")) {userPageFragment.onClick(v);}
            else if (OpenedFragment.equals("RegistrationFragment")) {registrationFragment.onClick(v);}
            else if(OpenedFragment.equals("NoteEditorFragment")){noteEditorFragment.onClick(v);}
            else if(OpenedFragment.equals("AddPlantFragment")){addPlantFragment.onClick(v);}
            else if(OpenedFragment.equals("PlantPageFragment")){plantPageFragment.onClick(v);}}
        catch (Exception ex){
        }
    }

    @Override
    public void onOpenFragmentRequested(String FragmentName, int id) {
        if (FragmentName.equals("UserPageFragment")) {
            if (!userPageFragment.isAdded()) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", id);
                userPageFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, userPageFragment).
                        addToBackStack(null).commit();
                getSharedPreferences("UserId", Context.MODE_PRIVATE).edit().putInt("UserId", id).apply();
                OpenedFragment = "UserPageFragment";
            }
        }else if(FragmentName.equals("AddPlantFragment")){
            if (addPlantFragment == null) {addPlantFragment = new AddPlantFragment();}
            if (!addPlantFragment.isAdded()) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", id);
                addPlantFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, addPlantFragment).
                        addToBackStack(null).commit();
                getSharedPreferences("UserId", Context.MODE_PRIVATE).edit().putInt("UserId", id).apply();
                OpenedFragment = "AddPlantFragment";
            }
        }
    }

    @Override
    public void onOpenFragmentRegistration(String login, String password) {
        if (registrationFragment == null) {registrationFragment = new RegistrationFragment();}
        if (!registrationFragment.isAdded()) {
            Bundle bundle = new Bundle();
            bundle.putString("login", login);
            bundle.putString("password", password);
            registrationFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, registrationFragment).
                    addToBackStack(null).commit();
            OpenedFragment = "RegistrationFragment";
        }
    }

    @Override
    public void onOpenNoteFragment(int id, int noteId, String noteText, String noteData, int plantId) {
        if (noteEditorFragment == null) {noteEditorFragment = new NoteEditorFragment();}
        if (!noteEditorFragment.isAdded()) {
            Bundle bundle = new Bundle();
            bundle.putInt("id", id);
            bundle.putInt("noteId", noteId);
            bundle.putString("noteText", noteText);
            bundle.putString("noteData", noteData);
            bundle.putInt("plantId", plantId);
            noteEditorFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, noteEditorFragment).
                    addToBackStack(null).commit();
            getSharedPreferences("UserId", Context.MODE_PRIVATE).edit().putInt("UserId", id).apply();
            OpenedFragment = "NoteEditorFragment";
        }
    }

    @Override
    public void onOpenPlantFragment(int id, int plantId) {
        if (plantPageFragment == null) {plantPageFragment = new PlantPageFragment();}
        if (!plantPageFragment.isAdded()) {
            Bundle bundle = new Bundle();
            bundle.putInt("id", id);
            bundle.putInt("plantId", plantId);
            plantPageFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, plantPageFragment).
                    addToBackStack(null).commit();
            getSharedPreferences("UserId", Context.MODE_PRIVATE).edit().putInt("UserId", id).apply();
            OpenedFragment = "PlantPageFragment";
        }
    }


    @SuppressLint("SuspiciousIndentation")
    public void IsAutorize() {
        sp = getSharedPreferences("UserId", Context.MODE_PRIVATE);
        int id = sp.getInt("UserId", 0);
        if (id == 0) {
            registrationFragment = new RegistrationFragment();
            autorizeFragment = new AutorizeFragment();
            userPageFragment = new UserPageFragment();
            noteEditorFragment = new NoteEditorFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container_view, autorizeFragment, null)
                    .commit();
            OpenedFragment = "AutorizeFragment";
        } else {
            userPageFragment = new UserPageFragment();
            noteEditorFragment = new NoteEditorFragment();
            onOpenFragmentRequested("UserPageFragment", id);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}