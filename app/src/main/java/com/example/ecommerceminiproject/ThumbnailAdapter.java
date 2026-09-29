package com.example.ecommerceminiproject;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.ecommerceminiproject.databinding.ItemThumbnailBinding;
import java.util.ArrayList;
import java.util.List;

public class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailAdapter.ThumbViewHolder> {
    public interface OnThumbnailClickListener {
        void onThumbnailClick(String imageUrl);
    }
    private final List<String> images = new ArrayList<>();
    private final OnThumbnailClickListener listener;
    private int selectedIndex = 0;
    public ThumbnailAdapter(OnThumbnailClickListener listener) {
        this.listener = listener;
    }
    public void submitList(List<String> newList) {
        images.clear();
        if (newList != null) images.addAll(newList);
        selectedIndex = 0;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ThumbViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ThumbViewHolder(ItemThumbnailBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ThumbViewHolder holder, int position) {
        holder.bind(images.get(position), position);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class ThumbViewHolder extends RecyclerView.ViewHolder {
        private final ItemThumbnailBinding binding;

        ThumbViewHolder(ItemThumbnailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(String url, int position) {

            Glide.with(binding.ivThumb.getContext())
                    .load(url)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(binding.ivThumb);

            binding.getRoot().setSelected(position == selectedIndex);

            binding.getRoot().setOnClickListener(v -> {

                int clicked = getBindingAdapterPosition();

                if (clicked == RecyclerView.NO_POSITION) {
                    return;
                }
                int old = selectedIndex;
                selectedIndex = clicked;
                notifyItemChanged(old);
                notifyItemChanged(selectedIndex);
                listener.onThumbnailClick(url);
            });
        }
    }
}
