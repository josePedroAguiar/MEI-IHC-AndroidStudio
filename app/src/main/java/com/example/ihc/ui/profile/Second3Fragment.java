package com.example.ihc.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ihc.MainActivity;
import com.example.ihc.R;
import com.example.ihc.databinding.FragmentSecond3Binding;

public class Second3Fragment extends Fragment {

    private FragmentSecond3Binding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecond3Binding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MainActivity.class);
                    i.putExtra("frgToLoad", "fragment_profile");
                    startActivity(i);
                /* NavHostFragment.findNavController(Second3Fragment.this)
                        .navigate(R.id.action_Second3Fragment_to_First3Fragment);  */
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}