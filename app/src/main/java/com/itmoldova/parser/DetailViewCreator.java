package com.itmoldova.parser;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itmoldova.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Given as input a {@link List<Block>}, creates the associated views for
 * each {@link Block} and returns as result a {@link List<View>} that is
 * to be rendered in the UI.
 * <p>
 * Author vgrec, on 14.08.16.
 */
public class DetailViewCreator {

    private Context context;

    public DetailViewCreator(Context context) {
        this.context = context;
    }

    public List<View> createViewsFrom(List<Block> blocks) {
        List<View> views = new ArrayList<>();
        for (Block block : blocks) {
            switch (block.getType()) {
                case TEXT:
                    views.add(createTextView(block.getContent()));
                    break;
                case IMAGE:
                    views.add(createImageView(block.getContent()));
                    break;
                case VIDEO:
                    views.add(createVideoView(block.getContent()));
                    break;
            }
        }

        return views;
    }

    private View createTextView(String text) {
        TextView textView = new TextView(context);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setText(Html.fromHtml(text));
        textView.setTextSize(16f);
        textView.setLineSpacing(8, 1);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        return textView;
    }

    private View createImageView(String url) {
        ImageView imageView = new ImageView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.setMargins(0, (int) context.getResources().getDimension(R.dimen.image_margin_top), 0,
                (int) context.getResources().getDimension(R.dimen.image_margin_bottom));
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setAdjustViewBounds(true);
        Picasso.with(context).load(url).into(imageView);
        return imageView;
    }

    private View createVideoView(String url) {
        return null;
    }

}
