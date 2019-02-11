package com.squaresdevelopers.latestphonewallpapers.classes;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squaresdevelopers.latestphonewallpapers.R;

import java.lang.reflect.Field;

public class BottomSheetClass extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;
    private static BottomSheetBehavior bottomSheetBehavior;
    private static View bottomSheetInternal;
    private static BottomSheetClass INSTANCE;
    LinearLayout layoutLike,layoutShare,layoutSet,layoutSave;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                CoordinatorLayout coordinatorLayout = (CoordinatorLayout) d.findViewById(R.id.locUXCoordinatorLayout);
                bottomSheetInternal = d.findViewById(R.id.bottomSheet);
                bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetInternal);
                BottomSheetBehavior.from((View)coordinatorLayout.getParent()).setPeekHeight(bottomSheetInternal.getHeight());
                bottomSheetBehavior.setPeekHeight(bottomSheetInternal.getHeight());
                coordinatorLayout.getParent().requestLayout();
                bottomSheetBehavior.setHideable(false);
                d.setCancelable(false);


            }
        });
        INSTANCE = this;
        View view = inflater.inflate(R.layout.bottom_sheet,container,false);
        layoutLike = view.findViewById(R.id.layout_like);
        layoutShare = view.findViewById(R.id.layout_share);
        layoutSet = view.findViewById(R.id.layout_set);
        layoutSave = view.findViewById(R.id.layout_save);

        layoutLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("like");
            }
        });

        layoutSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("set");
            }
        });

        layoutShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("share");
            }
        });

        layoutSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("save");
            }
        });

        return view;
    }

    public interface BottomSheetListener{
         void onButtonClicked(String message);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (BottomSheetListener) context;
        }
        catch (ClassCastException ex){
           throw new ClassCastException(context.toString()+"must implemented listener");
        }
    }
}
