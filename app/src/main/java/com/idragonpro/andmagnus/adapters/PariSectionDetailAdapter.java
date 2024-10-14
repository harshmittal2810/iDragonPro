package com.idragonpro.andmagnus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.idragonpro.andmagnus.R;
import com.idragonpro.andmagnus.activities.PariSectionDetailActivity;
import com.idragonpro.andmagnus.fragments.VideoDialogFragment;
import com.idragonpro.andmagnus.models.pari_section_details_response.Section;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PariSectionDetailAdapter extends RecyclerView.Adapter<PariSectionDetailAdapter.ViewHolder> {
    private final List<Section> sectionList;
    private final PariSectionDetailActivity pariSectionDetailActivity;
    private Context context;

    public PariSectionDetailAdapter(List<Section> sectionList, PariSectionDetailActivity pariSectionDetailActivity) {
        this.sectionList = sectionList;
        this.pariSectionDetailActivity = pariSectionDetailActivity;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.recycler_item_pari_section_detail, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        setData(position, holder);
    }

    private void setData(int position, ViewHolder holder) {
        holder.imageView.setOnClickListener(v -> {
            pariSectionDetailActivity.stopAudio();
            // Create and show the dialog.
            DialogFragment newFragment = VideoDialogFragment.newInstance(sectionList.get(position).getiVideoUrl(),
                pariSectionDetailActivity);
            newFragment.show(pariSectionDetailActivity.getSupportFragmentManager(), "dialog");
        });

        //set image
        if (sectionList.get(position).getImageUrl() != null && !sectionList.get(position).getImageUrl().isEmpty()) {
            Glide.with(holder.imageView.getContext())
                .load(sectionList.get(position).getImageUrl())
                .into(holder.imageView);
        }
        //

        //set title
        holder.textViewTitle.setText(sectionList.get(position).getiTitle());

        //set description
        holder.textViewDescription.setText(sectionList.get(position).getiDescription());
    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewTitle, textViewDescription;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
        }
    }

}
