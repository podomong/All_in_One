package com.example.chapterandcontentfragment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final ContentItem contentItem = contentItems.get(position);

        //TextView contentNumberView = holder.contentNumberView;
        TextView contentInstructionView = holder.contentInstructionView;
        //contentNumberView.setText(Integer.toString(contentItem.getNumber()));
        contentInstructionView.setText(contentItem.getInstruction());

        holder.contentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BoardActivity.class);
                intent.putExtra("DETAIL_ID", contentItem.getDetailId());
                context.startActivity(intent);
                //Toast.makeText(context,"You touched this cardview. Well done.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return contentItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView contentNumberView;
        TextView contentInstructionView;
        CardView contentCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            contentCardView = itemView.findViewById(R.id.contentCardView);
            contentNumberView = itemView.findViewById(R.id.contentNumber);
            contentInstructionView = itemView.findViewById(R.id.contentInstruction);

        }
    }


}
