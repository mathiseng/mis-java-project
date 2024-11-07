package com.example.mis_java_project.list;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mis_java_project.data.model.MediaItem;
import com.example.mis_java_project.databinding.ListItemBinding;

import java.util.List;

public class MediaItemListAdapter extends RecyclerView.Adapter<MediaItemListAdapter.ViewHolder> {
    public interface OnClickItem {
        void onClickMediaItem(MediaItem item);
    }


    private List<MediaItem> mediaItems;
    private final OnClickItem optionsClickListener;
    private final OnClickItem detailsClickListener;


    public MediaItemListAdapter(List<MediaItem> mediaItems, OnClickItem onClickDetails, OnClickItem onClickOptions) {
        this.mediaItems = mediaItems;
        this.optionsClickListener = onClickOptions;
        this.detailsClickListener = onClickDetails;

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
        holder.binding.mediaItemOption.setOnClickListener(view -> optionsClickListener.onClickMediaItem(item));
        holder.binding.mediaItem.setOnClickListener(view -> detailsClickListener.onClickMediaItem(item));
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
        }

        public void bind(MediaItem item) {
            binding.setMediaItem(item);
            binding.executePendingBindings();
        }
    }
}
