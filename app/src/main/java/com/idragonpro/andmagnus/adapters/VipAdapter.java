package com.idragonpro.andmagnus.adapters;

import static com.idragonpro.andmagnus.activities.Subscription.ISDOUBLESUBS;
import static com.idragonpro.andmagnus.activities.Subscription.ISMOVIESBUNDLE;
import static com.idragonpro.andmagnus.activities.Subscription.PACKAGE;
import static com.idragonpro.andmagnus.activities.Subscription.PACKAGE1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.idragonpro.andmagnus.R;
import com.idragonpro.andmagnus.activities.PaymentActivity;
import com.idragonpro.andmagnus.beans.Movies;
import com.idragonpro.andmagnus.beans.PackageModel;
import com.idragonpro.andmagnus.databinding.LayoutVipBinding;
import com.idragonpro.andmagnus.models.vipModel.VipPackage;

import java.util.ArrayList;

public class VipAdapter extends RecyclerView.Adapter<VipAdapter.ViewHolder> {
    Context context;
    ArrayList<PackageModel> packageModel;
    PackageModel packageModel1 = new PackageModel();
    Movies sMovie;

    public VipAdapter(Context context, ArrayList<PackageModel> packageModel) {
        this.context = context;
        this.packageModel = packageModel;

    }

    @NonNull
    @Override
    public VipAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutVipBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.
                getContext()), R.layout.layout_vip, parent, false);
        return new ViewHolder(binding);

    }
    private void paymentAlert(PackageModel PackageModel, boolean isDoubleSubs, boolean isMovieBundle) {
        Intent intent = new Intent(context, PaymentActivity.class);
        intent.putExtra(PACKAGE, PackageModel);
//        intent.putExtra(VipMOVIE, sMovie);
        intent.putExtra(ISDOUBLESUBS, isDoubleSubs);
        intent.putExtra(ISMOVIESBUNDLE, isMovieBundle);
        intent.putExtra(PACKAGE1, packageModel1);
        context.startActivity(intent);
    }

    @Override
    public void onBindViewHolder(@NonNull VipAdapter.ViewHolder holder, int position) {
            holder.binding.tvSingleMovie.setText(packageModel.get(position).getPackage());
            holder.binding.tvSingle2dMovie.setText(packageModel.get(position).getDescription());
            holder.binding.btnSingleMovie.setText(packageModel.get(position).getPrice());
            holder.binding.cvPAyment.setOnClickListener(view -> paymentAlert(packageModel.get(position),false,false));

    }

    @Override
    public int getItemCount() {
        return packageModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutVipBinding binding;
        public ViewHolder(LayoutVipBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
