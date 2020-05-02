package gofereatsrestarant.adapters.main.alert;

/**
 * @package com.trioangle.gofereatsrestarant
 * @subpackage view.main
 * @category MainActivity
 * @author Trioangle Product Team
 * @version 1.0
 **/


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.obs.CustomTextView;

import java.util.ArrayList;

import gofereatsrestarant.R;
import gofereatsrestarant.datamodels.main.IssueModel;

/**
 * Created by trioangle on 6/23/18.
 */


/************************************************************
 Alert dialog class used while thumbs up and down for orders
 ****************************************************************/


public class AlertListAdapter extends BaseAdapter {

    private ArrayList<IssueModel> issueModels;
    private Context context;


    private onItemClickListener mItemClickListener;

    /**
     * AlertListAdapter Adapter class to intialize issue models and context of the activity it is used in
     *
     * @param issueModels issueModels array list
     * @param context     context of the activity it is used in
     */

    public AlertListAdapter(ArrayList<IssueModel> issueModels, Context context) {
        this.issueModels = issueModels;
        this.context = context;
    }

    public void setOnItemClickListener(onItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public int getCount() {
        return issueModels.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.item_alert_list, null);

            CustomTextView tvIssues = (CustomTextView) convertView.findViewById(R.id.tvIssues);
            final RelativeLayout rltIssues = (RelativeLayout) convertView.findViewById(R.id.rltIssues);
            final ImageView ivAvailableTick = (ImageView) convertView.findViewById(R.id.ivAvailableTick);
            tvIssues.setText(issueModels.get(position).getIssueName());
            issueModels.get(position).setSelected(true);

            rltIssues.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (issueModels.get(position).isSelected()) {
                        ivAvailableTick.setVisibility(View.VISIBLE);
                        issueModels.get(position).setSelected(false);
                        if (mItemClickListener != null) {
                            mItemClickListener.onItemClickListeners(issueModels.get(position).getIssueId(), position);
                        }
                    } else {
                        ivAvailableTick.setVisibility(View.GONE);
                        issueModels.get(position).setSelected(true);
                        if (mItemClickListener != null) {
                            mItemClickListener.onItemClickListeners(issueModels.get(position).getIssueId(), position);
                        }
                    }
                }
            });
        }
        return convertView;
    }

    public interface onItemClickListener {
        void onItemClickListeners(int id, int position);
    }

}
