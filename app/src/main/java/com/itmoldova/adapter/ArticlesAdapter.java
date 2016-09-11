package com.itmoldova.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.itmoldova.R;
import com.itmoldova.list.ItemClickListener;
import com.itmoldova.model.Item;
import com.itmoldova.parser.ContentParser;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author vgrec, on 10.07.16.
 */
public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {

    private List<Item> items;
    private ItemClickListener itemClickListener;
    private Context context;

    public ArticlesAdapter(Context context, List<Item> items, ItemClickListener itemClickListener) {
        this.context = context;
        this.items = items;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.titleView.setText(item.getTitle());
        holder.dateView.setText(item.getPubDate());
        Picasso.with(context).load(ContentParser.extractFirstImage(item.getContent())).into(holder.previewImage);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView titleView;

        @BindView(R.id.date)
        TextView dateView;

        @BindView(R.id.preview_image)
        ImageView previewImage;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.row)
        public void onItemClicked() {
            itemClickListener.onItemClicked(items.get(getAdapterPosition()));
        }
    }
}
