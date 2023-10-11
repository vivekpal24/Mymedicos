package com.example.my_medicos;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    List<jobitem> joblist;

    public MyAdapter(Context context, List<jobitem> joblist) {
        this.context = context;
        this.joblist = joblist;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.job_design1, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.pos.setText(joblist.get(position).getPosition());
        holder.hosp.setText(joblist.get(position).getHospital());
        holder.loc.setText(joblist.get(position).getLocation());
        holder.date.setText(joblist.get(position).getDate());


    }

    @Override
    public int getItemCount() {
        return joblist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView pos, hosp, loc,date;
        Button apply;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            pos = itemView.findViewById(R.id.job_pos);
            hosp = itemView.findViewById(R.id.hosp_name);
            loc = itemView.findViewById(R.id.job_loc);
            date=itemView.findViewById(R.id.job_date);
            apply=itemView.findViewById(R.id.Apply);
            apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent j=new Intent(context, JobsApplyActivity.class);
                    j.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(j);
                }


            });

        }
    }
}
