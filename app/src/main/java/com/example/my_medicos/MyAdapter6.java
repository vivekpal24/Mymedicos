package com.example.my_medicos;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MyAdapter6 extends RecyclerView.Adapter<MyAdapter6.MyViewHolder6> {

    Context context; // Change to Context
    List<jobitem1> joblist;

    public MyAdapter6(Context context, List<jobitem1> joblist) { // Change to Context
        this.context = context;
        this.joblist = joblist;
    }

    @NonNull
    @Override
    public MyViewHolder6 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.job_design2, parent, false);
        return new MyViewHolder6(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder6 holder, int position) {
        holder.pos.setText(joblist.get(position).getPosition());
        holder.hosp.setText(joblist.get(position).getHospital());
        holder.loc.setText(joblist.get(position).getLocation());
        Log.d("12345678", joblist.get(position).getPosition());
        Log.d("12345678", joblist.get(position).getHospital());
        Log.d("12345678", joblist.get(position).getLocation());
    }

    @Override
    public int getItemCount() {
        return joblist.size();
    }

    public static class MyViewHolder6 extends RecyclerView.ViewHolder {
        TextView pos, hosp, loc;

        public MyViewHolder6(@NonNull View itemView) {
            super(itemView);
            pos = itemView.findViewById(R.id.job_pos1);
            hosp = itemView.findViewById(R.id.hosp_name1);
            loc = itemView.findViewById(R.id.job_loc1);
        }
    }
}
