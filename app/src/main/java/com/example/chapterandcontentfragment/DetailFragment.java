package com.example.chapterandcontentfragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DetailFragment extends Fragment {
    private RecyclerView recyclerView;
    private ContentAdapter contentAdapter;
    private ArrayList<ContentItem> contentItems;
    public final String CURRENT_CHAPTER = "CURRENT_CHAPTER";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.detail_fragment, container, false);
        TextView titleView = rootView.findViewById(R.id.chapterTitleInContentList);

        int currentChapter = getArguments().getInt(CURRENT_CHAPTER);
        titleView.setText("Chapter "+Integer.toString(currentChapter));

        recyclerView = (RecyclerView) rootView.findViewById(R.id.contentList);
        contentItems = new ArrayList<>();
        for(int i=1;i<=currentChapter;i++)
            contentItems.add(new ContentItem(i,"This is instruction"));
        contentAdapter = new ContentAdapter(getActivity(), contentItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(contentAdapter);

        return rootView;
    }
}

