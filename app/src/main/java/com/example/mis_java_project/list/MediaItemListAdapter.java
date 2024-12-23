package com.example.mis_java_project.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mis_java_project.data.model.MediaItem;
import com.example.mis_java_project.data.model.StorageOption;
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
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        ListItemBinding binding = ListItemBinding.inflate(inflater, viewGroup, false);
        return new ViewHolder(binding, context);
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

        private final Context context;

        public ViewHolder(ListItemBinding binding, Context context) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = context;
        }

        public void bind(MediaItem item) {
            binding.setMediaItem(item);
            if (item.getStorageOption() == StorageOption.REMOTE) {
                Glide.with(context.getApplicationContext()).load(item.getImageUri())  // URI from Firebase
                        .into(binding.listItemImage);
            } else {
                binding.listItemImage.setImageURI(item.getImageUri());
            }
            binding.executePendingBindings();
        }
    }
}
