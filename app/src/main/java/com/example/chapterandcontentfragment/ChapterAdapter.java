package com.example.chapterandcontentfragment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ViewHolder> {
    private int focusPos;

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView numberView;
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
            numberView = itemview.findViewById(R.id.chapterNumber);
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

        View chapterItemView = inflater.inflate(R.layout.chapter_item, parent,false);
        ViewHolder viewHolder = new ViewHolder(chapterItemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {


        ChapterItem chapterItem = chapterItems.get(position);
        TextView numberView = holder.numberView;
        TextView titleView = holder.titleView;

        titleView.setText(chapterItem.getTitle());
        numberView.setText(Integer.toString(position+1)+". ");
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