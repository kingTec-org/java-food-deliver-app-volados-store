package gofereatsrestarant.views.customize;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

import javax.inject.Inject;

import gofereatsrestarant.R;
import gofereatsrestarant.configs.SessionManager;

public class ExpandableRecyclerView extends RecyclerView {


    public ExpandableRecyclerView(Context context) {
        super(context, null);
        initRecycler();
    }

    public ExpandableRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initRecycler();
    }

    public ExpandableRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initRecycler();
    }

    private void initRecycler() {
        setClipToPadding(false);
        setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    public Parcelable onSaveInstanceState() {
        //begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);
        //end

        if (getAdapter() != null)
            ss.stateToSave = ((Adapter) this.getAdapter()).getExpandedGroups();

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        //begin boilerplate code so parent classes can restore state
        if (!(state instanceof SavedState))  // if state is not instance of out SaveState just restore in reg way
        {
            super.onRestoreInstanceState(state);
            return;
        }
        // else if cast him to SavedState

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        //end

        if (getAdapter() != null)
            ((Adapter) getAdapter()).setExpandedGroups(ss.stateToSave);
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        if (!(adapter instanceof Adapter))
            throw new IllegalArgumentException("adapter has to be of type ExpandableRecyclerView.Adapter");
        super.setAdapter(adapter);
    }


    public interface OnChildItemClickedListener {
        void onChildItemClicked(int group, int position);
    }

    static class SavedState implements Parcelable {
        public static final SavedState EMPTY_STATE = new SavedState() {
        };
        //required field that makes Parcelables from a Parcel
        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
        private SparseBooleanArray stateToSave;
        private Parcelable superState;

        SavedState() {
            superState = null;
        }

        SavedState(Parcelable superState) {
            this.superState = superState != EMPTY_STATE ? superState : null;
        }

        private SavedState(Parcel in) {
            Parcelable superState = in.readParcelable(ExpandableRecyclerView.class.getClassLoader());
            this.superState = superState != null ? superState : EMPTY_STATE;
            this.stateToSave = in.readSparseBooleanArray();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel out, int flags) {
            out.writeParcelable(superState, flags);
            out.writeSparseBooleanArray(this.stateToSave);
        }

        public Parcelable getSuperState() {
            return superState;
        }
    }

    public static abstract class Adapter<CVH extends ViewHolder, GVH extends ViewHolder, C, G> extends RecyclerView.Adapter<ViewHolder> {
        private static final int TYPE_HEADER = 0;
        public @Inject
        SessionManager sessionManager;
        private SparseBooleanArray expanded = new SparseBooleanArray();
        private OnChildItemClickedListener onChildItemClickedListener;

        /*public Adapter() {
        }*/

        private boolean isExpanded(int group) {
            return expanded.get(group);
        }

        private SparseBooleanArray getExpandedGroups() {
            return expanded;
        }

        public void setExpandedGroups(SparseBooleanArray expanded) {
            this.expanded = expanded;
        }

        public void expand(int group) {
            if (isExpanded(group))
                return;

            // this lines of code calculate number of shown item in recycler view. also group is counting .
            int position = 0;
            for (int i = 0; i < group; i++) {
                position++;
                if (isExpanded(i))
                    position += getChildItemCount(i);
            }
            position++; // this for percent group

            notifyItemRangeInserted(position, getChildItemCount(group)); // notify recycler view for expanding
            expanded.put(group, true); // save expanding in sparce array
        }

        public void collapse(int group) {
            if (!isExpanded(group)) // if is not expanded . so nothing to collapse.
                return;

            int position = 0;
            for (int i = 0; i < group; i++) {
                position++;
                if (isExpanded(i))
                    position += getChildItemCount(i); // item
            }
            position++;
            notifyItemRangeRemoved(position, getChildItemCount(group));
            expanded.put(group, false);
        }

        public abstract int getGroupItemCount();

        public abstract int getChildItemCount(int group);

        @Override
        public int getItemCount() {
            int count = 0;
            for (int i = 0; i <= getGroupItemCount(); i++) {
                count += isExpanded(i) ? getChildItemCount(i) + 1 : 1;
            }
            return count;
        }

        public abstract G getGroupItem(int position);

        public abstract C getChildItem(int group, int position);

        public Object getItem(int item) {
            int group = 0;
            while (group <= getGroupItemCount()) {
                if (item > 0 && !isExpanded(group)) {
                    item--;
                    group++;
                    continue;
                }
                if (item > 0 && isExpanded(group)) {
                    item--;
                    if (item < getChildItemCount(group))
                        return getChildItem(group, item);
                    item -= getChildItemCount(group);
                    group++;
                    continue;
                }
                if (item == 0)
                    return getGroupItem(group);
            }
            throw new IndexOutOfBoundsException();
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int items) {
            int group = 0;
            while (group <= getGroupItemCount()) {
                if (items > 0 && !isExpanded(group)) {
                    items--;
                    group++;
                    continue;
                }
                if (items > 0 && isExpanded(group)) {
                    items--;
                    if (items < getChildItemCount(group)) {
                        onBindChildViewHolder((CVH) holder, group, items);
                        return;
                    }
                    items -= getChildItemCount(group);
                    group++;
                    continue;
                }
                if (items == 0) {
                    onBindGroupViewHolder((GVH) holder, group);
                    return;
                }
            }
            throw new IndexOutOfBoundsException();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return viewType == TYPE_HEADER ? onCreateGroupViewHolder(parent) : onCreateChildViewHolder(parent, viewType);
        }

        protected abstract GVH onCreateGroupViewHolder(ViewGroup parent);

        protected abstract CVH onCreateChildViewHolder(ViewGroup parent, int viewType);

        public abstract int getChildItemViewType(int group, int position);

        @Override
        public int getItemViewType(int j) {
            int group = 0;
            while (group <= getGroupItemCount()) {
                if (j > 0 && !isExpanded(group)) {
                    j--;
                    group++;
                    continue;
                }
                if (j > 0 && isExpanded(group)) {
                    j--;
                    if (j < getChildItemCount(group))
                        return getChildItemViewType(group, j);
                    j -= getChildItemCount(group);
                    group++;
                    continue;
                }
                if (j == 0)
                    return TYPE_HEADER;
            }
            throw new IndexOutOfBoundsException();
        }

        public void setOnChildItemClickedListener(OnChildItemClickedListener onItemClickedListener) {
            this.onChildItemClickedListener = onItemClickedListener;
        }

        public void onBindChildViewHolder(CVH holder, final int group, final int position) {
            holder.itemView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (Adapter.this.onChildItemClickedListener != null) {
                        Adapter.this.onChildItemClickedListener.onChildItemClicked(group, position);


                    }

                }
            });
        }

        public void onBindGroupViewHolder(final GVH holder, final int group) {
            if (holder instanceof GroupViewHolder)
                ((GroupViewHolder) holder).setExpanded(isExpanded(group));

            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isExpanded(group)) {
                        collapse(group);
                        if (holder instanceof GroupViewHolder)
                            ((GroupViewHolder) holder).collapse();
                    } else {
                        expand(group);
                        sessionManager.setClickedMenu(group);
                        if (holder instanceof GroupViewHolder)
                            ((GroupViewHolder) holder).expand();
                    }
                }
            });
        }
    }

    public static abstract class GroupViewHolder extends RecyclerView.ViewHolder {

        public GroupViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void expand();

        public abstract void collapse();

        public abstract boolean isExpanded();

        public abstract void setExpanded(boolean expanded);
    }

    public static class SimpleGroupViewHolder extends GroupViewHolder {
        private ImageView expandedIndicator;
        private TextView text;
        private boolean expanded;

        public SimpleGroupViewHolder(Context context) {
            super(View.inflate(context, R.layout.row_menu_main_list, null));

            itemView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            expandedIndicator = (ImageView) itemView.findViewById(R.id.ivMenuDropDown);
            text = (TextView) itemView.findViewById(R.id.tvMainMenu);
        }

        public void expand() {
            ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.setDuration(200);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    ViewHelper.setRotation(expandedIndicator, 180 * (float) (animation.getAnimatedValue()));
                    expandedIndicator.postInvalidate();
                }
            });
            animator.start();
            expanded = true;
        }

        public void collapse() {
            ValueAnimator animator = ValueAnimator.ofFloat(1, 0);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.setDuration(200);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    ViewHelper.setRotation(expandedIndicator, 180 * (float) (animation.getAnimatedValue()));
                    expandedIndicator.postInvalidate();
                }
            });
            animator.start();
            expanded = false;
        }

        @Override
        public boolean isExpanded() {
            return expanded;
        }

        public void setExpanded(boolean expanded) {
            ViewHelper.setRotation(expandedIndicator, expanded ? 180 : 0);
            this.expanded = expanded;
        }

        public String getText() {
            return text.getText().toString();
        }

        public void setText(String t) {
            text.setText(t);
        }
    }

}