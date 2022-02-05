package com.project.elapor.ui.pengaduan;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.elapor.databinding.FragmentPengaduanBinding;

public class PengaduanFragment extends Fragment {

    private FragmentPengaduanBinding binding;
    private PengaduanAdapter adapter;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public void onResume() {
        super.onResume();
        checkRole();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentPengaduanBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private void checkRole() {
        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String role = "" + documentSnapshot.get("role");

                        if(role.equals("user")) {
                            binding.title.setText("Pengaduan Terkirim");
                            binding.keterangan.setText("Pengaduan Terkirim\nKe Admin");
                        } else {
                            binding.title.setText("Pengaduan Masuk");
                            binding.keterangan.setText("Pengaduan Masuk\ndari Client");
                        }

                        initRecyclerView(role);
                        initViewModel(role);

                    }
                });
    }

    private void initRecyclerView(String role) {
        binding.rvPengaduan.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new PengaduanAdapter(role, "");
        binding.rvPengaduan.setAdapter(adapter);
    }

    private void initViewModel(String role) {
        PengaduanViewModel viewModel = new ViewModelProvider(this).get(PengaduanViewModel.class);
        binding.progressBar.setVisibility(View.VISIBLE);
        if(role.equals("user")) {
            viewModel.setListPengaduanUserByUid(user.getUid());
        } else {
            viewModel.setListPengaduanAdminByUid(user.getUid());
        }
        viewModel.getPengaduan().observe(this, pengaduanModelArrayList -> {
            if (pengaduanModelArrayList.size() > 0) {
                if(role.equals("user")) {
                    binding.noDataUser.setVisibility(View.GONE);
                } else {
                    binding.noDataAdmin.setVisibility(View.GONE);
                }
                adapter.setData(pengaduanModelArrayList);
            } else {
                if(role.equals("user")) {
                    binding.noDataUser.setVisibility(View.VISIBLE);
                } else {
                    binding.noDataAdmin.setVisibility(View.VISIBLE);
                }
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