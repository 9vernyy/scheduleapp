package com.example.adilet.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Adilet on 13.04.2016.
 */
public class SubjectActivity extends AppCompatActivity {
    private RecyclerView mList;
    private SimpleAdapter mAdapter;
    private TextView mNoReminderView;
    private FloatingActionButton mAddReminderButton;
    private int mTempPost;
    private LinkedHashMap<Integer, Integer> IDmap = new LinkedHashMap<>();
    private SQLiteHandler db;
    private MultiSelector mMultiSelector = new MultiSelector();
    private int dayId;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        mAddReminderButton = (FloatingActionButton) findViewById(R.id.add_reminder);
        mAddReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SubjectActivity.this, SubjectAddActivity.class);
                i.putExtra("userId", userId);
                i.putExtra("dayId", dayId);
                startActivity(i);
            }
        });

        Intent i = getIntent();
        userId = i.getIntExtra("userId", 0);
        dayId = i.getIntExtra("dayId", 0);
        String date = i.getStringExtra("date");
        getSupportActionBar().setTitle(date);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new SQLiteHandler(getApplicationContext());
//        mAddReminderButton = (FloatingActionButton) findViewById(R.id.add_reminder);
        mList = (RecyclerView) findViewById(R.id.reminder_list);
        mNoReminderView = (TextView) findViewById(R.id.no_reminder_text);
        List<Subject> mTest = db.getSubjectsByDay(userId, dayId);

        if (mTest.isEmpty()) {
            mNoReminderView.setVisibility(View.VISIBLE);
        }

        // Create recycler view
        mList.setLayoutManager(getLayoutManager());
        registerForContextMenu(mList);
        mAdapter = new SimpleAdapter();
        mAdapter.setItemCount(getDefaultItemCount());
        mList.setAdapter(mAdapter);
    }

    // Create context menu for long press actions
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_add_subject, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;
    }

    // Multi select items in recycler view
    private android.support.v7.view.ActionMode.Callback mDeleteMode = new ModalMultiSelectorCallback(mMultiSelector) {

        @Override
        public boolean onCreateActionMode(android.support.v7.view.ActionMode actionMode, Menu menu) {
            getMenuInflater().inflate(R.menu.menu_add_subject, menu);
            return true;
        }

        @Override
        public boolean onActionItemClicked(android.support.v7.view.ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {

                // On clicking discard subject
                case R.id.discard_reminder:
                    // Close the context menu
                    actionMode.finish();

                    // Get the subject id associated with the recycler view item
                    for (int i = IDmap.size(); i >= 0; i--) {
                        if (mMultiSelector.isSelected(i, 0)) {
                            int id = IDmap.get(i);
                            // Get subject from subject database using id
                            Subject temp = db.getSubjectById(id);
                            // Delete subject
                            db.deleteSubject(temp);
                            // Remove subject from recycler view
                            mAdapter.removeItemSelected(i);
                        }
                    }

                    // Clear selected items in recycler view
                    mMultiSelector.clearSelections();
                    // Recreate the recycler items
                    // This is done to remap the item and subject ids
                    mAdapter.onDeleteItem(getDefaultItemCount());

                    // Display toast to confirm delete
                    Toast.makeText(getApplicationContext(),
                            "Удалено",
                            Toast.LENGTH_SHORT).show();

                    // To check is there are saved subjects
                    // If there are no subjects display a message asking the user to create subjects
                    List<Subject> mTest = db.getSubjectsByDay(userId, dayId);

                    if (mTest.isEmpty()) {
                        mNoReminderView.setVisibility(View.VISIBLE);
                    } else {
                        mNoReminderView.setVisibility(View.GONE);
                    }

                    return true;

                // On clicking save subjects
                case R.id.save_reminder:
                    // Close the context menu
                    actionMode.finish();
                    // Clear selected items in recycler view
                    mMultiSelector.clearSelections();
                    return true;

                default:
                    break;
            }
            return false;
        }
    };

    // On clicking a subject item
    private void selectReminder(int mClickID) {
        String mStringClickID = Integer.toString(mClickID);

        // Create intent to edit the subject
        // Put subject id as extra
        Intent i = new Intent(this, SubjectEditActivity.class);
        i.putExtra("subjectId", mStringClickID);
        startActivityForResult(i, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mAdapter.setItemCount(getDefaultItemCount());
    }

    // Recreate recycler view
    // This is done so that newly created subjects are displayed
    @Override
    public void onResume() {
        super.onResume();

        // To check is there are saved subjects
        // If there are no subjects display a message asking the user to create subjects
        List<Subject> mTest = db.getSubjectsByDay(userId, dayId);

        if (mTest.isEmpty()) {
            mNoReminderView.setVisibility(View.VISIBLE);
        } else {
            mNoReminderView.setVisibility(View.GONE);
        }

        mAdapter.setItemCount(getDefaultItemCount());
    }

    // Layout manager for recycler view
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }

    protected int getDefaultItemCount() {
        return 100;
    }

    public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.VerticalItemHolder> {
        private ArrayList<SubjectItem> mItems;

        public SimpleAdapter() {
            mItems = new ArrayList<>();
        }

        public void setItemCount(int count) {
            mItems.clear();
            mItems.addAll(generateData(count));
            notifyDataSetChanged();
        }

        public void onDeleteItem(int count) {
            mItems.clear();
            mItems.addAll(generateData(count));
        }

        public void removeItemSelected(int selected) {
            if (mItems.isEmpty()) return;
            mItems.remove(selected);
            notifyItemRemoved(selected);
        }

        // View holder for recycler view items
        @Override
        public VerticalItemHolder onCreateViewHolder(ViewGroup container, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(container.getContext());
            View root = inflater.inflate(R.layout.recycler_items, container, false);

            return new VerticalItemHolder(root, this);
        }

        @Override
        public void onBindViewHolder(VerticalItemHolder itemHolder, int position) {
            SubjectItem item = mItems.get(position);
            itemHolder.setSubjectName(item.mName);
            itemHolder.setTime(item.mStart + ", " + item.mEnd);
            itemHolder.setCabinet(item.mCabinet);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public class SubjectItem {
            public String mName;
            public String mStart;
            public String mEnd;
            public Integer mCabinet;

            public SubjectItem(String Title, String startTime, String endTime, Integer cabinet) {
                this.mName = Title;
                this.mStart = startTime;
                this.mEnd = endTime;
                this.mCabinet = cabinet;
            }
        }

        // UI and data class for recycler view items
        public class VerticalItemHolder extends SwappingHolder
                implements View.OnClickListener, View.OnLongClickListener {
            private TextView mTitleText, mDateAndTimeText, mRepeatInfoText;
            private ImageView mActiveImage, mThumbnailImage;
            private ColorGenerator mColorGenerator = ColorGenerator.DEFAULT;
            private TextDrawable mDrawableBuilder;
            private SimpleAdapter mAdapter;

            public VerticalItemHolder(View itemView, SimpleAdapter adapter) {
                super(itemView, mMultiSelector);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
                itemView.setLongClickable(true);

                // Initialize adapter for the items
                mAdapter = adapter;

                // Initialize views
                mTitleText = (TextView) itemView.findViewById(R.id.recycle_title);
                mDateAndTimeText = (TextView) itemView.findViewById(R.id.recycle_date_time);
                mRepeatInfoText = (TextView) itemView.findViewById(R.id.recycle_repeat_info);
                mThumbnailImage = (ImageView) itemView.findViewById(R.id.thumbnail_image);
            }

            // On clicking a subject item
            @Override
            public void onClick(View v) {
                if (!mMultiSelector.tapSelection(this)) {
                    mTempPost = mList.getChildAdapterPosition(v);

                    int mReminderClickID = IDmap.get(mTempPost);
                    selectReminder(mReminderClickID);

                } else if (mMultiSelector.getSelectedPositions().isEmpty()) {
                    mAdapter.setItemCount(getDefaultItemCount());
                }
            }

            // On long press enter action mode with context menu
            @Override
            public boolean onLongClick(View v) {
                AppCompatActivity activity = SubjectActivity.this;
                activity.startSupportActionMode(mDeleteMode);
                mMultiSelector.setSelected(this, true);
                return true;
            }

            // Set subject title view
            public void setSubjectName(String title) {
                mTitleText.setText(title);
            }

            // Set date and time views
            public void setTime(String datetime) {
                mDateAndTimeText.setText(datetime);
            }

            // Set repeat views
            public void setCabinet(int cab) {
                mRepeatInfoText.setText(cab + "");
            }
        }

        // Generate real data for each item
        public List<SubjectItem> generateData(int count) {
            ArrayList<SubjectItem> items = new ArrayList<>();

            // Get all subject from the database
            List<Subject> reminders = db.getSubjectsByDay(userId, dayId);

            // Initialize lists
            List<String> Titles = new ArrayList<>();
            List<Integer> Cabinets = new ArrayList<>();
            List<String> StartTIme = new ArrayList<>();
            List<String> EndTIme = new ArrayList<>();
            List<Integer> IDList = new ArrayList<>();
//            List<DateTimeSorter> DateTimeSortList = new ArrayList<>();

            // Add details of all subjects in their respective lists
            for (Subject r : reminders) {
                Titles.add(r.getNameSubject());
                StartTIme.add(r.getStartTime());
                EndTIme.add(r.getEndTime());
                Cabinets.add(r.getCabinet());
                IDList.add(r.getId());
            }

            // Add data to each recycler view item
            for (int i = 0; i < reminders.size(); i++) {
                items.add(new SimpleAdapter.SubjectItem(Titles.get(i), StartTIme.get(i), EndTIme.get(i), Cabinets.get(i)));
                IDmap.put(i, IDList.get(i));
            }

            return items;
        }
    }
}
