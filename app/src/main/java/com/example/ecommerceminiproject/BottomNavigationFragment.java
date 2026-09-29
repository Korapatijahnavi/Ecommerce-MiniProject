package com.example.ecommerceminiproject;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.ecommerceminiproject.databinding.FragmentBottomNavigationBinding;

public class BottomNavigationFragment extends Fragment {
    private FragmentBottomNavigationBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentBottomNavigationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.homebutton.setOnClickListener(v -> {
            if (!(requireActivity() instanceof HomeActivity)) Navigator.openHome(requireActivity());
        });
        binding.productsbutton.setOnClickListener(v -> {
            if (!(requireActivity() instanceof ProductActivity)) {
                Navigator.openProducts(requireActivity(), Constants.CATEGORY_ALL, false);
            }
        });
        binding.cartbutton.setOnClickListener(v -> openCart());
        binding.checkoutbutton.setOnClickListener(v -> openCart());
    }
    private void openCheckout(){
        if(requireActivity() instanceof DeliveryActivity) return;
        if(CartRepository.getInstance().isEmpty()){
            Toast.makeText(requireContext(), R.string.cart_empty_toast,Toast.LENGTH_SHORT).show();
            openCart();
        }
        else{
            Navigator.openDelivery(requireActivity());
        }
    }
    private void openCart() {
        if (!(requireActivity() instanceof CartActivity)) Navigator.openCart(requireActivity());
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}