package com.example.chapterandcontentfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChapterFragment extends Fragment {
    private RecyclerView recyclerView;
    private ChapterAdapter chapterAdapter;
    private ArrayList<ChapterItem> chapterItems;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.chapter_fragment, container, false);


        recyclerView = (RecyclerView) rootView.findViewById(R.id.chapterList);
        chapterItems = new ArrayList<>();
        for(int i=1;i<=20;i++)
            chapterItems.add(new ChapterItem("Chapter "+i,R.drawable.ic_launcher_foreground));
        chapterAdapter = new ChapterAdapter(getActivity(), chapterItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(chapterAdapter);

        return rootView;
    }
}
