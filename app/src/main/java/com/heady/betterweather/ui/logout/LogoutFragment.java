package com.heady.betterweather.ui.logout;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.heady.betterweather.databinding.FragmentLogoutBinding;
import com.heady.betterweather.view.LoginActivity;

public class LogoutFragment extends Fragment {

    private FragmentLogoutBinding binding;
    private FirebaseAuth auth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LogoutViewModel logoutViewModel =
                new ViewModelProvider(this).get(LogoutViewModel.class);

        binding = FragmentLogoutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        auth = FirebaseAuth.getInstance();

        final TextView textView = binding.textLogout;
        logoutViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        auth.signOut();
        startActivity(new Intent(getActivity(), LoginActivity.class));


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}