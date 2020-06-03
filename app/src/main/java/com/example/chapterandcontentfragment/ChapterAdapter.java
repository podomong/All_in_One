package com.example.chapterandcontentfragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView titleView;

        ViewHolder(View itemview){
            super(itemview);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(listener != null)
                            listener.onItemClick(v, pos);
                    }
                }
            });
            imageView = itemview.findViewById(R.id.chapterImage);
            titleView = itemview.findViewById(R.id.chapterTitle);
        }
    }

    private List<ChapterItem> chapterItems;
    private Context context;
    ChapterAdapter(Context context, List<ChapterItem>courseContents){
        this.chapterItems = courseContents;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View courseContentView = inflater.inflate(R.layout.chapter_item, parent,false);
        ViewHolder viewHolder = new ViewHolder(courseContentView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChapterItem courseContent = chapterItems.get(position);

        TextView titleView = holder.titleView;
        ImageView imageView = holder.imageView;

        titleView.setText(courseContent.getTitle());
        imageView.setImageResource(courseContent.getImageResource());

        /*holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"You touched this cardview. Well done.",Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return chapterItems.size();
    }

    private OnItemClickListener listener = null ;

    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener ;
    }
}