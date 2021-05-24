package com.example.parkingappv2.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.parkingappv2.activities.SetAvailabilityActivity;
import com.example.parkingappv2.Constants;
import com.example.parkingappv2.MyApi;
import com.example.parkingappv2.R;
import com.example.parkingappv2.models.ParkingSpot;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ParkingSpotAdapter extends RecyclerView.Adapter<ParkingSpotAdapter.ParkingSpotViewHolder> {
    private final List<ParkingSpot> mParkingSpotList;
    private OnItemClickListener mListener;
    private static final String TAG = "ShowParkingActivity";
    private final Context context;

    public ParkingSpotAdapter(List<ParkingSpot> parkingSpotList, Context context) {
        mParkingSpotList = parkingSpotList;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void finish();
        void refreshActivity();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public class ParkingSpotViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextViewID;
        public Button delete_parking_button;
        public Button available_button;
        public int position;
        public ParkingSpot parkingspot;

        public ParkingSpotViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);

            mTextView1 = itemView.findViewById(R.id.textView);
            mTextView2 = itemView.findViewById(R.id.textView2);
            mTextViewID = itemView.findViewById(R.id.id_ascuns);

            delete_parking_button = itemView.findViewById(R.id.button_remove);
            available_button = itemView.findViewById(R.id.make_available);

            delete_parking_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "OnClick: " + getPosition());
                    String id = (String) mTextViewID.getText();
                    Log.d(TAG, "ParkingSpot ID= " + id);
                    delete_parking(id);

                    mListener.refreshActivity();
                }
            });

        }
    }//vh

    private void setAvailabilityAction(String id) {
        Intent intent = new Intent(context, SetAvailabilityActivity.class);
        intent.putExtra("id", id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mListener.finish();

        context.startActivity(intent);

    }

    @NotNull
    @Override
    public ParkingSpotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.parking_sample, parent, false);
        return new ParkingSpotViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(ParkingSpotViewHolder holder, int position) {
        ParkingSpot parkingspot = mParkingSpotList.get(position);

        holder.mTextView1.setText(parkingspot.getAddress());
        holder.mTextView2.setText(parkingspot.getParkingNumber());
        holder.mTextViewID.setText(parkingspot.getId());
        holder.position = position;
        holder.parkingspot = parkingspot;

        if (parkingspot.isAvailable()) {
            holder.available_button.setText("Mark Available");
            holder.available_button.setTextColor(Color.WHITE);
            holder.available_button.setBackgroundColor(Color.GREEN);
        }

        else {
            if (parkingspot.isClaimed()) {
                holder.available_button.setText("Markfewjqneif");
                holder.available_button.setTextColor(Color.WHITE);
                holder.available_button.setBackgroundColor(Color.GREEN);
            }
            holder.available_button.setText("Not Available");
            holder.available_button.setTextColor(Color.BLACK);
            holder.available_button.setBackgroundColor(Color.GRAY);
            holder.available_button.setEnabled(false);

            //holder.delete_parking_button.setEnabled(false);
        }

        holder.available_button.setOnClickListener(v -> {
            if (parkingspot.isAvailable()) {

                setAvailabilityAction(parkingspot.getId());
            } else {
                Toast.makeText(context, "Parking spot is already marked as available!"
                        , Toast.LENGTH_SHORT).show();
            }
        });

    }//OBH

    @Override
    public int getItemCount() {
        return mParkingSpotList.size();
    }

    public void delete_parking(String id) {
        Log.d(TAG, " delete_parking function in process");
        //build retrofit request
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Call<String> call = retrofit.create(MyApi.class).destroy(
                Constants.bearerToken,
                id
        );

        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> stringResponse) {
                if (stringResponse.isSuccessful() && stringResponse.body() != null) {
                    Log.d(TAG, "Sters cu success ---> " + stringResponse.body());
                    //mListener.refreshActivity();

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("RETROFIT", "onFailure: " + t.getLocalizedMessage());
                Log.d(TAG, Constants.fail_general);
            }
        });
        Log.d(TAG, " delete_parking function executed");

    }
}