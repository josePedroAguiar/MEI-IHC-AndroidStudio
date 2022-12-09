package com.example.ihc.ui.notifications;

import static com.example.ihc.MainActivity.messaged;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ihc.R;
import com.example.ihc.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Log.e("Messaged Size: ", Integer.toString(messaged.size()));


        for (int i = 0; i < messaged.size(); i++) {
            Log.d("TESTE NOME", messaged.get(i).getName());
        }

        ListAdapter adapter = new ListAdapter(getActivity(), messaged);
        ListView listView = root.findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);
        listView.setClickable(true);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(getActivity(), ChatActivity.class);

            Bundle extras = new Bundle();
            extras.putString("name", messaged.get(position).getName());
            if (messaged.get(position).getPhotoUri() != null)
                extras.putString("imageid", messaged.get(position).getPhotoUri());
            else extras.putInt("imageid", R.drawable.ic_baseline_person_24);
            extras.putString("toID", messaged.get(position).getUuid());

            i.putExtras(extras);
            startActivity(i);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}