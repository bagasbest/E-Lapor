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
import com.project.elapor.ui.pengaduan.PengaduanAdapter;
import com.project.elapor.ui.pengaduan.PengaduanViewModel;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private DashboardAdapter adapter;
    private PengaduanAdapter pengaduanAdapter;
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
        adapter = new DashboardAdapter(user.getUid(), image, name, unit);
        binding.rvAdmin.setAdapter(adapter);
    }

    private void initViewModel() {
        DashboardViewModel viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        binding.progressBar.setVisibility(View.VISIBLE);
        viewModel.setListDashboardAdmin();

        viewModel.getDashboard().observe(this, dashboardModelArrayList -> {
            if (dashboardModelArrayList.size() > 0) {
                binding.noDataAdmin.setVisibility(View.GONE);
                adapter.setData(dashboardModelArrayList);
            } else {
                binding.noDataAdmin.setVisibility(View.VISIBLE);
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

                        if (role.equals("user")) {
                            binding.textView4.setVisibility(View.VISIBLE);
                            initRecyclerView(image, name, unit);
                            initViewModel();
                        } else {
                            binding.textView5.setVisibility(View.VISIBLE);
                            initRecyclerViewAdminSide();
                            initViewModelAdminSide();
                        }

                        Glide.with(requireActivity())
                                .load(image)
                                .into(binding.image);

                        binding.name.setText(name);
                        binding.nip.setText("NIP: " + nip);
                        binding.unit.setText("Unit: " + unit);

                    }
                });
    }

    private void initRecyclerViewAdminSide() {
        binding.rvAdmin.setLayoutManager(new LinearLayoutManager(getActivity()));
        pengaduanAdapter = new PengaduanAdapter("admin", "dashboard");
        binding.rvAdmin.setAdapter(pengaduanAdapter);
    }

    private void initViewModelAdminSide() {
        PengaduanViewModel viewModel = new ViewModelProvider(this).get(PengaduanViewModel.class);
        binding.progressBar.setVisibility(View.VISIBLE);
        viewModel.setListPengaduanAdminByUidComplete(user.getUid());
        viewModel.getPengaduan().observe(this, pengaduanModelArrayList -> {
            if (pengaduanModelArrayList.size() > 0) {
                binding.noDataUser.setVisibility(View.GONE);
                pengaduanAdapter.setData(pengaduanModelArrayList);
            } else {
                binding.noDataUser.setVisibility(View.VISIBLE);
            }
            binding.progressBar.setVisibility(View.GONE);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}