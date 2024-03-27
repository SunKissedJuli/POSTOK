package com.coolgirl.poctok.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.coolgirl.poctok.APIclasses.Notes;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.coolgirl.poctok.APIclasses.Plant;
import com.coolgirl.poctok.OnFragmentInteractionListener;
import com.coolgirl.poctok.R;

import java.util.List;

public class PlantAdapter extends BaseAdapter {
    private Context mContext;
    private List<Plant> plantList;
    private OnFragmentInteractionListener mListener;

    public PlantAdapter(Context mContext, List<Plant> plantList) {
        this.mContext = mContext;
        this.plantList = plantList;
        mListener =  (OnFragmentInteractionListener)mContext;
    }

    //количество элементов в списке
    @Override
    public int getCount() {
        return plantList.size();
    }

    // объект Note
    @Override
    public Object getItem(int i) {
        return plantList.get(i);
    }

    // NoteId
    @Override
    public long getItemId(int i) {
        return plantList.get(i).userid;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") View v = View.inflate(mContext, R.layout.plant_item, null);
        TextView plantName = v.findViewById(R.id.PlantItemName);
        TextView plantDescription = v.findViewById(R.id.PlantItemDiscription);
        Button toPlantProfile = v.findViewById(R.id.ToPlantProfile);
        Plant plant = plantList.get(i);
        plantName.setText(plant.plantname.toString());
        plantDescription.setText(plant.plantdescription.toString());
        toPlantProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("PlantAdapter", "В PlantAdapter Нажали на button");
                mListener.onOpenPlantFragment(plant.userid, plant.plantid);
            }
        });
        return v;
    }

}
