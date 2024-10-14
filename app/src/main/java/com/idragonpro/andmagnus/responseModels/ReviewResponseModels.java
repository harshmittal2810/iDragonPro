package com.idragonpro.andmagnus.responseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.idragonpro.andmagnus.models.Review;

import java.util.List;

public class ReviewResponseModels {

    @SerializedName("comments")
    @Expose
    private List<Review> review = null;

    public List<Review> getReview() {
        return review;
    }

    public void setReview(List<Review> review) {
        this.review = review;
    }
}
