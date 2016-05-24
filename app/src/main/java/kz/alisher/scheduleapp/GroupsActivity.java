package kz.alisher.scheduleapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Alisher Kozhabay on 21.05.2016.
 */
public class GroupsActivity extends AppCompatActivity {
    private List<String> listDataHeader;
    private HashMap<String, List<Student>> listDataChild;
    private ExpandableListView expListView;
    private ExpandableGroupsAdapter listAdapter;
    private FloatingActionButton addGroupBtn;
    private MaterialStyledDialog addGroupDialog;
    private View orderView;
    private MaterialEditText name;
    private SQLiteHandler db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        orderView = getLayoutInflater().inflate(R.layout.add_group_view, null);

        db = new SQLiteHandler(this);

        getSupportActionBar().setTitle("Список групп");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        prepareGroupList();
        openDialog();
        listAdapter = new ExpandableGroupsAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

    }


    private void prepareGroupList() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<Student>>();

        if (db.getGroups() == null){
            listDataHeader.add("CS-131");
        } else {
            listDataHeader = db.getGroups();
            for (int i = 0; i < listDataHeader.size(); i++) {
                if (db.getStudentsByGroupId(listDataHeader.get(i)) != null){
                    List<Student> studentList = db.getStudentsByGroupId(listDataHeader.get(i));
                    listDataChild.put(listDataHeader.get(i), studentList);
                } else {
                    listDataChild.put(listDataHeader.get(i), new ArrayList<Student>());
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    public void addGroup(View view) {
        addGroupDialog.show();
    }

    private void openDialog() {
        addGroupDialog = new MaterialStyledDialog(this)
                .setIcon(R.drawable.group_man)
                .setDescription("Введите название новой группы.")
                .withDialogAnimation(true)
                .setCustomView(orderView, 20, 20, 20, 0)
                .setPositive("Добавить", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        if (!name.getText().toString().isEmpty()){
                            db.addGroup(name.getText().toString().trim());
                            listDataHeader = db.getGroups();
                            for (int i = 0; i < listDataHeader.size(); i++) {
                                listDataChild.put(listDataHeader.get(i), new ArrayList<Student>());
                            }
                            listAdapter = new ExpandableGroupsAdapter(GroupsActivity.this, listDataHeader, listDataChild);
                            expListView.setAdapter(listAdapter);
                            Toast.makeText(GroupsActivity.this, "Группа успешно создана", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(GroupsActivity.this, "Введите название группы", Toast.LENGTH_SHORT).show();
                            dialog.show();
                        }
                    }
                })
                .setNegative("Отмена", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .build();
        name = (MaterialEditText) orderView.findViewById(R.id.nameOfGroup);
    }
}
