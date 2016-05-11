package com.rockgarden.myapp.activity;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.rockgarden.myapp.R;
import com.rockgarden.myapp.fragment.ItemFragment;
import com.rockgarden.myapp.fragment.StudentsFragment;

import butterknife.BindView;

public class SpinnerActivity extends BaseLayoutDrawerActivity implements ItemFragment.FragmentInteractionListener {
    public static final String TAG = SpinnerActivity.class.getName();

    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner);

        if (toolbar != null) {
            getToolbar().setTitle(R.string.title_activity_spinner);
        }
        // Setup spinner
        spinner.setAdapter(new MyAdapter(
                toolbar.getContext(),
                new String[]{
                        "Section 1",
                        "Section 2",
                        "Section 3",
                }));

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When the given dropdown item is selected, show its contents in the
                // container view.
                if (position == 1) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, ItemFragment.newInstance(position))
                            .commit();
                } else {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, StudentsFragment.newInstance(position + 1))
                            .commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public void onFragmentInteraction(Object item) {

    }

    private static class MyAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
        private final ThemedSpinnerAdapter.Helper mDropDownHelper;

        public MyAdapter(Context context, String[] objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                // Inflate the drop down using the helper's LayoutInflater
                LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
                view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                view = convertView;
            }

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getItem(position));

            return view;
        }

        @Override
        public Theme getDropDownViewTheme() {
            return mDropDownHelper.getDropDownViewTheme();
        }

        @Override
        public void setDropDownViewTheme(Theme theme) {
            mDropDownHelper.setDropDownViewTheme(theme);
        }
    }


}
