package com.example.musicsearchapi;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.example.musicsearchapi.databinding.FragmentCollectBinding;
import com.example.musicsearchapi.databinding.ItemMusicBinding;
import com.example.musicsearchapi.databinding.ItemMusicCollectBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

public class CollectFragment extends Fragment {
    private BindAdapter<ItemMusicCollectBinding, MusicItem> adapter = new BindAdapter<ItemMusicCollectBinding, MusicItem>() {
        @Override
        public ItemMusicCollectBinding createHolder(ViewGroup parent) {
            return ItemMusicCollectBinding.inflate(getLayoutInflater(), parent, false);
        }

        @Override
        public void bind(ItemMusicCollectBinding item, MusicItem data, int position) {
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
                            .hide(CollectFragment.this)
                            .show(fragment)
                            .commit();
                }
            });
            item.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(requireActivity())
                            .setTitle("notice")
                            .setMessage("Are you sure you want to delete this collection")
                            .setNegativeButton("sure", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Database database = new Database(requireActivity());
                                    database.deleteCollect(data);
                                    MusicItem remove = getData().remove(position);
                                    notifyItemRemoved(position);
                                    Snackbar.make(requireView(), "delete success", Snackbar.LENGTH_LONG)
                                            .setAction("Revoked", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    database.addCollect(remove);
                                                    getData().add(position, remove);
                                                    notifyItemInserted(position);
                                                }
                                            }).show();
                                }
                            }).setPositiveButton("cancel", null).show();

                }
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentCollectBinding binding = FragmentCollectBinding.inflate(getLayoutInflater());
        adapter.getData().addAll(new Database(requireContext()).getCollect());
        binding.rvList.setAdapter(adapter);
        return binding.getRoot();
    }


}