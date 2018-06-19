package com.haier.wifi.base;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用RecyclerView的Adapter <br />
 * Created by qibin on 2016/5/17.
 */
public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerAdapter.CommHolder> {

    public static final int TYPE_DEFAULT = -1; // 默认item
    public static final int TYPE_FOOTER = -2;  // 添加footerView
    public static final int TYPE_HEADER = -3;  // 添加header

    protected List<T> mDatas; // datas
    private View mFooterView; // footer
    private View mHeaderView; // header

    private TypeSupport<T> mTypeSupport; // 多类型支持

    private OnItemClickListener<T> mItemClickListener;

    public RecyclerAdapter(final int layoutId) {
        this(new TypeSupport<T>() {
            @Override
            public int getItemType(int position, T data) {
                return TYPE_DEFAULT;
            }

            @Override
            public int getLayoutId(int itemType) {
                return layoutId;
            }
        });
    }

    public RecyclerAdapter(TypeSupport<T> typeSupport) {
        mTypeSupport = typeSupport;
    }

    public void setData(List<T> data) {
        avoidNull();
        mDatas.clear();
        if(data != null) { mDatas.addAll(data);}
        notifyDataSetChanged();
//        notifyItemInserted(mDatas.size() - 1);
    }

    public void addData(List<T> data) {
        avoidNull();
        if(data != null) { mDatas.addAll(data);}
//        notifyDataSetChanged();
        notifyItemInserted(mDatas.size());
    }

    public T getItem(int position) {
        avoidNull();
        if (position < 0 || position > mDatas.size() - 1) { return null;}
        return mDatas.get(position);
    }

    public void remove(int position) {
        avoidNull();
        if (position < 0 || position > mDatas.size() - 1) { return;}
        mDatas.remove(position);
//        notifyDataSetChanged();
        position = mHeaderView == null ? position : position + 1;
        notifyItemRemoved(position);
        if (position != mDatas.size()) {
            notifyItemRangeChanged(position, mDatas.size() - position);
        }
    }

    public void clear() {
        avoidNull();
        int start = mHeaderView == null ? 0 : 1;
        int size = mDatas.size();
        if(size == 0) { return;}
        mDatas.clear();
        notifyItemRangeRemoved(start, size);
    }

    public List<T> getAll() {
        avoidNull();
        return mDatas;
    }

    public void header(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public void footer(View footerView) {
        mFooterView = footerView;
        int pos = mDatas == null ? 0 : mDatas.size();
        notifyItemInserted(pos);
    }

    public View footer() {
        return mFooterView;
    }

    private void avoidNull() {
        if (mDatas == null) { mDatas = new ArrayList<>();}
    }

    @Override
    public int getItemCount() {
        int count = mDatas == null ? 0 : mDatas.size();
        if (mHeaderView != null) { count++;}
        if (hasView(mFooterView)) { count++;}
        return count;
    }

    private boolean hasView(View view) {
        return view != null && view.getVisibility() == View.VISIBLE;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mHeaderView != null) { return TYPE_HEADER;}
        int topSize = mHeaderView == null ? 0 : 1;
        if (mDatas != null) { topSize += mDatas.size();}
        if (mDatas == null || position == topSize) { return TYPE_FOOTER;}
        // 这里的position是在数据集中的位置
        position = mHeaderView == null ? position : position - 1;
        return mTypeSupport.getItemType(position, mDatas.get(position));
    }

    @Override
    public CommHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) { return new CommHolder(mHeaderView);}
        if (viewType == TYPE_FOOTER) { return new CommHolder(mFooterView);}
        return CommHolder.getHolder(parent, mTypeSupport.getLayoutId(viewType));
    }

    @Override
    public void onBindViewHolder(CommHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == TYPE_HEADER || viewType == TYPE_FOOTER) { return;}

        final int pos = getRealPosition(holder);
        final T item = mDatas.get(pos);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(pos, item);
                }
            }
        });
        onBind(holder, pos, viewType, item);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (isHeaderOrFooter(position)) { return gridManager.getSpanCount();}
                    return 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(CommHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(isHeaderOrFooter(holder.getLayoutPosition()));
        }
    }

    private boolean isHeaderOrFooter(int position) {
        int type = getItemViewType(position);
        return type == TYPE_FOOTER || type == TYPE_HEADER;
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int pos = holder.getLayoutPosition();
        if (mHeaderView != null) { pos -= 1;}
        return pos;
    }

    public static class CommHolder extends RecyclerView.ViewHolder {
        private View mItemView;
        private SparseArray<View> mViews;

        private CommHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mViews = new SparseArray<>();
        }

        public <T extends View> T getView(int id) {
            View view = mViews.get(id);
            if (view  == null) {
                view = mItemView.findViewById(id);
                mViews.put(id, view);
            }
            return (T) view;
        }

        public static CommHolder getHolder(ViewGroup parent, int layoutId) {
            View layout = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            return new CommHolder(layout);
        }
    }

    public abstract void onBind(CommHolder holder, int position, int viewType, T data);

    public void setOnItemClickListener(OnItemClickListener<T> li) {
        mItemClickListener = li;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(int pos, T data);
    }

    public interface TypeSupport<T> {
        int getItemType(int position, T data);
        int getLayoutId(int itemType);
    }
}
