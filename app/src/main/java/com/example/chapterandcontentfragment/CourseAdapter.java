package com.example.chapterandcontentfragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView illustrationView;
        TextView titleView;
        TextView instructionView;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            illustrationView = itemView.findViewById(R.id.illustrationView);
            titleView = itemView.findViewById(R.id.titleView);
            instructionView = itemView.findViewById(R.id.instructionView);
            cardView = itemView.findViewById(R.id.courseCardView);
        }
    }

    private List<CourseItem> courseItems;

    public CourseAdapter(List<CourseItem> courseItems, Context context) {
        this.context = context;
        this.courseItems = courseItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View courseItemView = inflater.inflate(R.layout.course_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(courseItemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CourseItem courseItem = courseItems.get(position);

        TextView titleView = holder.titleView;
        TextView instructionView = holder.instructionView;
        ImageView illustrationView = holder.illustrationView;

        instructionView.setText(courseItem.getInstruction());
        titleView.setText(courseItem.getTitle());
        illustrationView.setImageBitmap(courseItem.getIllustrationResource());

        final int COURSE_ID = position+1;
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChapterAndContentActivity.class);
                intent.putExtra(AllinOneContract.Course.COURSE_ID, COURSE_ID);
                context.startActivity(intent);
                //Toast.makeText(context,"You touched this cardview. Well done.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseItems.size();
    }
}
