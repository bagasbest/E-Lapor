package com.project.elapor.ui.dashboard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.elapor.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private DashboardAdapter adapter;
    private String role;

    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentDashboardBinding.inflate(inflater, container, false);

        populateUserProfile();


        return binding.getRoot();
    }

    private void initRecyclerView(String image, String name, String unit) {
        binding.rvAdmin.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new DashboardAdapter(user.getUid(), image, name, unit, role);
        binding.rvAdmin.setAdapter(adapter);
    }

    private void initViewModel(String role) {
        DashboardViewModel viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        binding.progressBar.setVisibility(View.VISIBLE);
        if(role.equals("user")) {
            viewModel.setListDashboardAdmin();
        } else {
            viewModel.setListDashboardUser();
        }
        viewModel.getDashboard().observe(this, dashboardModelArrayList -> {
            if (dashboardModelArrayList.size() > 0) {
                if(role.equals("user")) {
                    binding.noDataUser.setVisibility(View.GONE);
                } else {
                    binding.noDataAdmin.setVisibility(View.GONE);
                }
                adapter.setData(dashboardModelArrayList);
            } else {
                if(role.equals("user")) {
                    binding.noDataUser.setVisibility(View.GONE);
                } else {
                    binding.noDataAdmin.setVisibility(View.GONE);
                }
            }
            binding.progressBar.setVisibility(View.GONE);
        });
    }

    private void populateUserProfile() {
        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String image = "" + documentSnapshot.get("image");
                        String name = "" + documentSnapshot.get("name");
                        String nip = "" + documentSnapshot.get("nip");
                        String unit = "" + documentSnapshot.get("unit");
                        role = "" + documentSnapshot.get("role");

                        if(role.equals("user")) {
                            binding.textView4.setVisibility(View.VISIBLE);
                        } else {
                            binding.textView5.setVisibility(View.VISIBLE);
                        }

                        initRecyclerView(image, name, unit);
                        initViewModel(role);

                        Glide.with(requireActivity())
                                .load(image)
                                .into(binding.image);

                        binding.name.setText(name);
                        binding.nip.setText("NIP: " + nip);
                        binding.unit.setText(unit);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}