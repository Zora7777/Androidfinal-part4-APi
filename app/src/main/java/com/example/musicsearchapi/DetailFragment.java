package com.example.musicsearchapi;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.musicsearchapi.databinding.FragmentDetailBinding;

public class DetailFragment extends Fragment {


    private MusicItem musicItem;


    public DetailFragment() {
    }

    public static DetailFragment newInstance(MusicItem musicItem) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("musicItem", musicItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            musicItem = (MusicItem) getArguments().getSerializable("musicItem");
        }
    }

    private FragmentDetailBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        binding = FragmentDetailBinding.bind(view);
        return view;
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Glide.with(binding.tvArt).load(musicItem.album.cover).into(binding.ivImage);
        binding.tvAlbum.setText("Album:" + musicItem.album.title);
        binding.tvArt.setText("Contributor:" + musicItem.contributors.get(0).name);
        binding.tvTitle.setText("Title:" + musicItem.title);
        binding.tvDuration.setText("Duration:" + String.format("%02d:%02d", (musicItem.duration / 60), (musicItem.duration % 60)));
        binding.btnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(requireActivity()).addCollect(musicItem);
                Toast.makeText(requireActivity(), "collect success", Toast.LENGTH_SHORT).show();
            }
        });
    }
}