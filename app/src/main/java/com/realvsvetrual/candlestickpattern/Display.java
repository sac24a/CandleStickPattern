package com.realvsvetrual.candlestickpattern;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Display extends AppCompatActivity  {

    GridView gridView;
    Button back;
    ArrayList<String> urlArray;
    int index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        gridView = findViewById(R.id.gridView);
        back = findViewById(R.id.back);

        Intent intent = getIntent();
        urlArray = intent.getStringArrayListExtra("array");
        index = intent.getIntExtra("index",0);

        ArrayList<String> newurl = new ArrayList();
        switch (index) {
            case 13:
                for (int i = 0; i<13; i++) {
                    newurl.add(urlArray.get(i));
                }
                break;
            case 46:
                for (int i = 13; i<46; i++) {
                    newurl.add(urlArray.get(i));
                }
                break;
            case 78:
                for (int i = 46; i<78; i++) {
                    newurl.add(urlArray.get(i));
                }
                break;
            case 108:
                for (int i = 78; i<108; i++) {
                    newurl.add(urlArray.get(i));
                }
            case 136:
                for (int i = 108; i<136; i++) {
                    newurl.add(urlArray.get(i));
                }
                break;
            case 168:
                for (int i = 136; i<168; i++) {
                    newurl.add(urlArray.get(i));
                }
                break;
                default:
                    break;
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        CustomAdapter customAdapter = new CustomAdapter(Display.this,newurl);
        gridView.setAdapter(customAdapter);





    }



    class CustomAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflter;
        ArrayList<String> urls;

        public CustomAdapter(Context applicationContext,ArrayList<String> urls) {
            this.context = applicationContext;
            this.urls = urls;
            inflter = (LayoutInflater.from(applicationContext));

        }
        @Override
        public int getCount() {
            return urls.size();
        }
        @Override
        public Object getItem(int i) {
            return null;
        }
        @Override
        public long getItemId(int i) {
            return 0;
        }
        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.gridlayout, null); // inflate the layout
            final ImageView img = view.findViewById(R.id.image);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    final AlertDialog dialogBuilder = new AlertDialog.Builder(Display.this).create();
//                    LayoutInflater inflater = Display.this.getLayoutInflater();
//                    View dialogView = inflater.inflate(R.layout.zoom, null);
                    final TouchImageView img = new TouchImageView(Display.this);
                    Picasso.get().load(urls.get(i)).into(img);
                    img.setMaxZoom(4f);
//
//                    dialogBuilder.setView(dialogView);
//
////                    dialogBuilder.setContentView(dialogView);
//                    dialogBuilder.getWindow().setBackgroundDrawable(null);
//                    dialogBuilder.show();




                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
                    addContentView(img, layoutParams);
                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ViewGroup vg = (ViewGroup) (v.getParent());
                            vg.removeView(v);
                        }
                    });
                }
            });
            try
            {
                Picasso.get().load(urls.get(i)).into(img);

            }
            catch (NullPointerException e) {

            }
            return view;

        }

    }

//    private void zoomImageFromThumb(final View thumbView, String urls) {
//        // If there's an animation in progress, cancel it
//        // immediately and proceed with this one.
//        if (currentAnimator != null) {
//            currentAnimator.cancel();
//        }
//
//        // Load the high-resolution "zoomed-in" image.
//        final ImageView expandedImageView = (ImageView) findViewById(
//                R.id.expanded_image);
////        expandedImageView.setImageResource(imageResId);
//        Picasso.get().load(urls).into(expandedImageView);
//
//        // Calculate the starting and ending bounds for the zoomed-in image.
//        // This step involves lots of math. Yay, math.
//        final Rect startBounds = new Rect();
//        final Rect finalBounds = new Rect();
//        final Point globalOffset = new Point();
//
//        // The start bounds are the global visible rectangle of the thumbnail,
//        // and the final bounds are the global visible rectangle of the container
//        // view. Also set the container view's offset as the origin for the
//        // bounds, since that's the origin for the positioning animation
//        // properties (X, Y).
//        thumbView.getGlobalVisibleRect(startBounds);
//        findViewById(R.id.container)
//                .getGlobalVisibleRect(finalBounds, globalOffset);
//        startBounds.offset(-globalOffset.x, -globalOffset.y);
//        finalBounds.offset(-globalOffset.x, -globalOffset.y);
//
//        // Adjust the start bounds to be the same aspect ratio as the final
//        // bounds using the "center crop" technique. This prevents undesirable
//        // stretching during the animation. Also calculate the start scaling
//        // factor (the end scaling factor is always 1.0).
//        float startScale;
//        if ((float) finalBounds.width() / finalBounds.height()
//                > (float) startBounds.width() / startBounds.height()) {
//            // Extend start bounds horizontally
//            startScale = (float) startBounds.height() / finalBounds.height();
//            float startWidth = startScale * finalBounds.width();
//            float deltaWidth = (startWidth - startBounds.width()) / 2;
//            startBounds.left -= deltaWidth;
//            startBounds.right += deltaWidth;
//        } else {
//            // Extend start bounds vertically
//            startScale = (float) startBounds.width() / finalBounds.width();
//            float startHeight = startScale * finalBounds.height();
//            float deltaHeight = (startHeight - startBounds.height()) / 2;
//            startBounds.top -= deltaHeight;
//            startBounds.bottom += deltaHeight;
//        }
//
//        // Hide the thumbnail and show the zoomed-in view. When the animation
//        // begins, it will position the zoomed-in view in the place of the
//        // thumbnail.
//        thumbView.setAlpha(0f);
//        expandedImageView.setVisibility(View.VISIBLE);
//
//        // Set the pivot point for SCALE_X and SCALE_Y transformations
//        // to the top-left corner of the zoomed-in view (the default
//        // is the center of the view).
//        expandedImageView.setPivotX(0f);
//        expandedImageView.setPivotY(0f);
//
//        // Construct and run the parallel animation of the four translation and
//        // scale properties (X, Y, SCALE_X, and SCALE_Y).
//        AnimatorSet set = new AnimatorSet();
//        set
//                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
//                        startBounds.left, finalBounds.left))
//                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
//                        startBounds.top, finalBounds.top))
//                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
//                        startScale, 2f))
//                .with(ObjectAnimator.ofFloat(expandedImageView,
//                        View.SCALE_Y, startScale, 2f));
//        set.setDuration(shortAnimationDuration);
//        set.setInterpolator(new DecelerateInterpolator());
//        set.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                currentAnimator = null;
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//                currentAnimator = null;
//            }
//        });
//        set.start();
//        currentAnimator = set;
//
//        // Upon clicking the zoomed-in image, it should zoom back down
//        // to the original bounds and show the thumbnail instead of
//        // the expanded image.
//        final float startScaleFinal = startScale;
//        expandedImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (currentAnimator != null) {
//                    currentAnimator.cancel();
//                }
//
//                // Animate the four positioning/sizing properties in parallel,
//                // back to their original values.
//                AnimatorSet set = new AnimatorSet();
//                set.play(ObjectAnimator
//                        .ofFloat(expandedImageView, View.X, startBounds.left))
//                        .with(ObjectAnimator
//                                .ofFloat(expandedImageView,
//                                        View.Y,startBounds.top))
//                        .with(ObjectAnimator
//                                .ofFloat(expandedImageView,
//                                        View.SCALE_X, startScaleFinal))
//                        .with(ObjectAnimator
//                                .ofFloat(expandedImageView,
//                                        View.SCALE_Y, startScaleFinal));
//                set.setDuration(shortAnimationDuration);
//                set.setInterpolator(new DecelerateInterpolator());
//                set.addListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        thumbView.setAlpha(1f);
//                        expandedImageView.setVisibility(View.GONE);
//                        currentAnimator = null;
//                    }
//
//                    @Override
//                    public void onAnimationCancel(Animator animation) {
//                        thumbView.setAlpha(1f);
//                        expandedImageView.setVisibility(View.GONE);
//                        currentAnimator = null;
//                    }
//                });
//                set.start();
//                currentAnimator = set;
//            }
//        });
//    }
}
