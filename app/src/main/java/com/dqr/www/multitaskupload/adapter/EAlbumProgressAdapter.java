package com.dqr.www.multitaskupload.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dqr.www.multitaskupload.R;
import com.dqr.www.multitaskupload.bean.ProgressBean;
import com.dqr.www.multitaskupload.recyclerview.RecyclerHolder;

import java.util.List;

/**
 * Description：进度详情adapter
 * Author：LiuYM
 * Date： 2017-05-23 16:01
 */

public class EAlbumProgressAdapter extends RecyclerView.Adapter<RecyclerHolder> {
    private static final String TAG = "EAlbumProgressAdapter";

    private List<ProgressBean> mList;

    public EAlbumProgressAdapter(List<ProgressBean> list) {
        mList = list;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ealbum_upload_progress_item, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        ProgressBean pBean = mList.get(position);
        holder.setImageResource(R.id.iv_image, pBean.getFilePath(), R.mipmap.ic_launcher);
        holder.setText(R.id.tv_title, "上传到《" + pBean.getAlbumName() + "》");

        TextView tvDesc = holder.getView(R.id.tv_desc);
        LinearLayout lltProgress = holder.getView(R.id.llt_progress);
        ProgressBar progressBar = holder.getView(R.id.pb_progressbar);
        TextView tvProgress = holder.getView(R.id.tv_progress_desc);

        if (pBean.isFail()) {//上传失败
            tvDesc.setVisibility(View.VISIBLE);
            lltProgress.setVisibility(View.GONE);
            tvDesc.setText("上传失败");
        } else if (pBean.isSuccess()) {
            tvDesc.setVisibility(View.VISIBLE);
            lltProgress.setVisibility(View.GONE);
            tvDesc.setText("上传成功");

        } else if (pBean.getUploadedSize() > 0) {//正常上传中
            tvDesc.setVisibility(View.GONE);
            lltProgress.setVisibility(View.VISIBLE);

            progressBar.setProgress(pBean.getProgress());
            tvProgress.setText(ProgressBean.convertFileSize(pBean.getUploadedSize())
                    + "/"
                    + ProgressBean.convertFileSize(pBean.getTotalSize()));

        } else {//等待中
            tvDesc.setVisibility(View.VISIBLE);
            lltProgress.setVisibility(View.GONE);
            tvDesc.setText(pBean.getDesc());
        }

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }
}
