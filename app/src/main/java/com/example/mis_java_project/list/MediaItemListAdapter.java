package com.example.mis_java_project.list;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mis_java_project.databinding.ListItemBinding;
import com.example.mis_java_project.data.model.MediaItem;

import java.util.List;

public class MediaItemListAdapter extends RecyclerView.Adapter<MediaItemListAdapter.ViewHolder> {


    List<MediaItem> mediaItems;

    public MediaItemListAdapter(List<MediaItem> mediaItems) {
        this.mediaItems = mediaItems;
    }


    @NonNull
    @Override
    public MediaItemListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        ListItemBinding binding = ListItemBinding.inflate(inflater, viewGroup, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaItemListAdapter.ViewHolder holder, int position) {
        MediaItem item = mediaItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return mediaItems.size();
    }

    public void setMediaItems(List<MediaItem> newMediaItems) {
        mediaItems = newMediaItems;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ListItemBinding binding;

        public ViewHolder(ListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            // Define click listener for the ViewHolder's View
        }

        public void bind(MediaItem item) {
            binding.setMediaItem(item);
            binding.executePendingBindings();
        }

    }
}
