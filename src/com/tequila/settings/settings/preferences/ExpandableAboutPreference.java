package com.tequila.settings.preferences;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;

/**
 * Custom expandable preference for About ROM card
 * Expands to show full text when clicked with smooth animation
 */
public class ExpandableAboutPreference extends Preference {

    private boolean isExpanded = false;
    private TextView summaryView;
    private static final int COLLAPSED_MAX_LINES = 2;
    private static final int ANIMATION_DURATION = 300; // milliseconds
    
    private int collapsedHeight = 0;
    private int expandedHeight = 0;

    public ExpandableAboutPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public ExpandableAboutPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ExpandableAboutPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ExpandableAboutPreference(Context context) {
        super(context);
        init();
    }

    private void init() {
        setLayoutResource(R.layout.superioros_dashboard_preference_tequila);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        
        summaryView = (TextView) holder.findViewById(android.R.id.summary);
        
        if (summaryView != null) {
            // Disable scrollbars
            summaryView.setVerticalScrollBarEnabled(false);
            summaryView.setHorizontalScrollBarEnabled(false);
            summaryView.setScrollContainer(false);
            
            // Set initial collapsed state
            summaryView.setMaxLines(COLLAPSED_MAX_LINES);
            summaryView.setEllipsize(android.text.TextUtils.TruncateAt.END);
            summaryView.setSingleLine(false);
            
            // Make the entire preference clickable to expand/collapse
            holder.itemView.setOnClickListener(v -> toggleExpanded());
            
            // Measure heights after layout
            summaryView.post(() -> measureHeights());
        }
    }

    private void measureHeights() {
        if (summaryView == null) return;
        
        // Measure collapsed height
        summaryView.setMaxLines(COLLAPSED_MAX_LINES);
        summaryView.measure(
            View.MeasureSpec.makeMeasureSpec(summaryView.getWidth(), View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        );
        collapsedHeight = summaryView.getMeasuredHeight();
        
        // Measure expanded height
        summaryView.setMaxLines(Integer.MAX_VALUE);
        summaryView.measure(
            View.MeasureSpec.makeMeasureSpec(summaryView.getWidth(), View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        );
        expandedHeight = summaryView.getMeasuredHeight();
        
        // Reset to collapsed state
        if (!isExpanded) {
            summaryView.setMaxLines(COLLAPSED_MAX_LINES);
            ViewGroup.LayoutParams params = summaryView.getLayoutParams();
            params.height = collapsedHeight;
            summaryView.setLayoutParams(params);
        }
    }

    private void toggleExpanded() {
        if (summaryView == null) return;
        
        isExpanded = !isExpanded;
        animateExpansion(isExpanded);
    }

    private void animateExpansion(boolean expand) {
        if (collapsedHeight == 0 || expandedHeight == 0) {
            // Fallback to instant expansion if heights not measured
            updateSummaryStateInstant(expand);
            return;
        }
        
        int startHeight = expand ? collapsedHeight : expandedHeight;
        int endHeight = expand ? expandedHeight : collapsedHeight;
        
        ValueAnimator animator = ValueAnimator.ofInt(startHeight, endHeight);
        animator.setDuration(ANIMATION_DURATION);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        
        animator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            ViewGroup.LayoutParams params = summaryView.getLayoutParams();
            params.height = value;
            summaryView.setLayoutParams(params);
        });
        
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (expand) {
                    summaryView.setMaxLines(Integer.MAX_VALUE);
                    summaryView.setEllipsize(null);
                }
            }
            
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!expand) {
                    summaryView.setMaxLines(COLLAPSED_MAX_LINES);
                    summaryView.setEllipsize(android.text.TextUtils.TruncateAt.END);
                }
                ViewGroup.LayoutParams params = summaryView.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                summaryView.setLayoutParams(params);
            }
        });
        
        animator.start();
    }

    private void updateSummaryStateInstant(boolean expand) {
        if (summaryView != null) {
            if (expand) {
                summaryView.setMaxLines(Integer.MAX_VALUE);
                summaryView.setEllipsize(null);
            } else {
                summaryView.setMaxLines(COLLAPSED_MAX_LINES);
                summaryView.setEllipsize(android.text.TextUtils.TruncateAt.END);
            }
        }
    }
}