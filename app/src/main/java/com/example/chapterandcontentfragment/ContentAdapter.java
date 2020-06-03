package com.example.chapterandcontentfragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> {
    private Context context;
    private List<ContentItem> contentItems = new ArrayList<>();

    ContentAdapter(Context context, List<ContentItem>contentItems){
        this.contentItems = contentItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contentView = inflater.inflate(R.layout.content_item, parent,false);
        ViewHolder viewHolder = new ViewHolder(contentView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContentItem contentItem = contentItems.get(position);

        TextView contentNumberView = holder.contentNumberView;
        TextView contentInstructionView = holder.contentInstructionView;
        contentNumberView.setText(Integer.toString(contentItem.getNumber()));
        contentInstructionView.setText(contentItem.getInstruction());

        /*holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"You touched this cardview. Well done.",Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return contentItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView contentNumberView;
        TextView contentInstructionView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contentNumberView = itemView.findViewById(R.id.contentNumber);
            contentInstructionView = itemView.findViewById(R.id.contentInstruction);

        }
    }
}
