package com.example.cityzen10.recyclerviewpagination;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.cityzen10.recyclerviewpagination.model.Person;

import java.util.ArrayList;
import java.util.List;


public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<Person> personResults;
    private Context context;

    private boolean isLoadingAdded = false;

    public PaginationAdapter(Context context) {
        this.context = context;
        personResults = new ArrayList<>();
    }

    public List<Person> getPersonResults() {
        return personResults;
    }

    public void setPersonResults(List<Person> personResults) {
        this.personResults = personResults;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.item_list, parent, false);
        viewHolder = new MovieVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Person result = personResults.get(position); // Person

        switch (getItemViewType(position)) {
            case ITEM:
                final MovieVH movieVH = (MovieVH) holder;

                //  movieVH.personid.setText(result.getId());
                movieVH.pfirstname.setText(result.getFirstname());
                movieVH.plastname.setText(result.getLastname());
                Glide
                        .with(context)
                        .load(result.getAvatarpics())
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                                movieVH.mProgress.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                // image ready, hide progress now
                                movieVH.mProgress.setVisibility(View.GONE);
                                return false;   // return false if you want Glide to handle everything else.
                            }
                        })
                        .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                        .centerCrop()
                        .crossFade()
                        .into(movieVH.mPosterImg);

                break;

            case LOADING:
                //Do nothing
                break;
        }
    }

    @Override
    public int getItemCount() {
        return personResults == null ? 0 : personResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == personResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void add(Person r) {
        personResults.add(r);
        notifyItemInserted(personResults.size() - 1);
    }

    public void addAll(List<Person> moveResults) {
        for (Person result : moveResults) {
            add(result);

        }
    }

    public void remove(Person r) {
        int position = personResults.indexOf(r);
        if (position > -1) {
            personResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Person());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = personResults.size() - 1;
        Person result = getItem(position);

        if (result != null) {
            personResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Person getItem(int position) {
        return personResults.get(position);
    }

    /**
     * Main list's content ViewHolder
     */
    protected class MovieVH extends RecyclerView.ViewHolder {
        private TextView personid;
        private TextView pfirstname;
        private TextView plastname;
        private ImageView mPosterImg;
        private ProgressBar mProgress;

        public MovieVH(View itemView) {
            super(itemView);
            personid = itemView.findViewById(R.id.person_id);
            pfirstname = itemView.findViewById(R.id.first_Name);
            plastname = itemView.findViewById(R.id.last_name);
            mPosterImg = itemView.findViewById(R.id.person_poster);
            mProgress = itemView.findViewById(R.id.movie_progress);
        }
    }
    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }
}