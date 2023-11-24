package com.example.musicsearchapi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.musicsearchapi.adapter.BindAdapter;
import com.example.musicsearchapi.databinding.ActivitySearchFragmentBinding;
import com.example.musicsearchapi.databinding.ItemMusicBinding;
import com.google.gson.Gson;

public class SearchFragment extends Fragment {
    private BindAdapter<ItemMusicBinding, MusicItem> adapter = new BindAdapter<ItemMusicBinding, MusicItem>() {
        @Override
        public ItemMusicBinding createHolder(ViewGroup parent) {
            return ItemMusicBinding.inflate(getLayoutInflater(), parent, false);
        }

        @Override
        public void bind(ItemMusicBinding item, MusicItem data, int position) {
            item.tvArt.setText(data.contributors.get(0).name);
            item.tvName.setText(data.title);
            Glide.with(item.tvName).load(data.album.cover).into(item.ivImage);
            item.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DetailFragment fragment = DetailFragment.newInstance(data);
                    FragmentManager supportFragmentManager = requireActivity().getSupportFragmentManager();

                    FragmentTransaction fragmentTransaction = supportFragmentManager
                            .beginTransaction();
                    fragmentTransaction
                            .add(R.id.root, fragment)
                            .addToBackStack("DetailFragment")
                            .hide(SearchFragment.this)
                            .show(fragment)
                            .commit();
                }
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ActivitySearchFragmentBinding binding = ActivitySearchFragmentBinding.inflate(getLayoutInflater());
        binding.rvList.setAdapter(adapter);
        binding.btnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager supportFragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = supportFragmentManager
                        .beginTransaction();
                fragmentTransaction
                        .add(R.id.root, new CollectFragment())
                        .addToBackStack("CollectFragment")
                        .hide(SearchFragment.this)
                        .show(new CollectFragment())
                        .commit();
            }
        });
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String artName = binding.etSearch.getText().toString();
                defaultSharedPreferences.edit().putString("searchKey",artName).apply();
                RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.deezer.com/search/artist/?q=" + artName, response -> {
                    MusicLinkData data = new Gson().fromJson(response, MusicLinkData.class);
                    if (data != null && !data.data.isEmpty()) {
                        getMusicItems(data.data.get(0).tracklist);
                    }
                }, error -> {

                });
                requestQueue.add(stringRequest);
            }
        });
        String searchKey = defaultSharedPreferences.getString("searchKey", "");
        if (!searchKey.isEmpty()) {
            binding.etSearch.setText(searchKey);
            binding.btnSearch.performClick();
        }

        return binding.getRoot();
    }


    private void getMusicItems(String tracklist) {
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, tracklist, response -> {
            MusicData data = new Gson().fromJson(response, MusicData.class);
            if (data != null && !data.data.isEmpty()) {
                adapter.getData().clear();
                adapter.getData().addAll(data.data);
                adapter.notifyDataSetChanged();
            }
        }, error -> {
        });
        requestQueue.add(stringRequest);
    }

}