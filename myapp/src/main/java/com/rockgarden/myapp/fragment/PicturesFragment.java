/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rockgarden.myapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rockgarden.myapp.R;
import com.rockgarden.myapp.adpater.RecyclerViewAdapter_Picture;
import com.rockgarden.myapp.model.Pictures;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PicturesFragment extends Fragment {
    private static final String TAG = PicturesFragment.class.getSimpleName();
    RecyclerViewAdapter_Picture adapter;

    public static PicturesFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(TAG, page);
        PicturesFragment fragment = new PicturesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(
                R.layout.fragment_picture, container, false);
        setupRecyclerView(rv);
        return rv;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        adapter = new RecyclerViewAdapter_Picture(getActivity(), getRandomSubList(Pictures.sPictureStrings, 30));
        recyclerView.setAdapter(adapter);
        //TODO:setOnItemClickListener将覆盖Adapter中的setOnClickListener
//        RecyclerViewItemClickSupport.addTo(recyclerView).setOnItemClickListener(
//                new RecyclerViewItemClickSupport.OnItemClickListener() {
//                    @Override
//                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
//                        // do it
//                    }
//                }
//        );
    }

    public static List<String> getRandomSubList(String[] array, int amount) {
        ArrayList<String> list = new ArrayList<>(amount);
        Random random = new Random();
        while (list.size() < amount) {
            list.add(array[random.nextInt(array.length)]);
        }
        return list;
    }

}
