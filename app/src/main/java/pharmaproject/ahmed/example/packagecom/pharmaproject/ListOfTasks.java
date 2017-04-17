package pharmaproject.ahmed.example.packagecom.pharmaproject;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pharmaproject.ahmed.example.packagecom.pharmaproject.database.Task;
import pharmaproject.ahmed.example.packagecom.pharmaproject.database.TaskType;
import pharmaproject.ahmed.example.packagecom.pharmaproject.helper.helper;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListOfTasks extends Fragment {
    RecyclerView recyclerView;
    String ID_TEMP;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Task task;
    RatingBar averageRatingbar;
    helper helper=new helper(getActivity());
    TextView nameemployee;
    Spinner myspinner;
    ArrayAdapter<TaskType> adapter; // create adapter of tasktype
    TaskType[] taskTypes; // create array of tasktype
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_list_of_tasks, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swip);
        TextView empty = (TextView)view.findViewById(R.id.empty_view);
        nameemployee=(TextView) view.findViewById(R.id.nameemployee);
        myspinner=(Spinner)view.findViewById(R.id.myspinner);

        empty.setTypeface(utils.getFont(getActivity()));
        ID_TEMP = getArguments().getString("KEY");
        nameemployee.setTypeface(utils.getFont(getActivity()));
        nameemployee.setText(getArguments().getString("NAME"));
        averageRatingbar= (RatingBar) view.findViewById(R.id.ratingBarTotal);
        averageRatingbar.setEnabled(false);
        taskTypes = TaskType.values(); // task type item to array
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, taskTypes);// set adapter
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(taskTypes[myspinner.getSelectedItemPosition()]); //when user use refresh get data who choose it
            }
        });
        myspinner.setAdapter(adapter);
        myspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getData(taskTypes[position]);//get data which he choose it
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Tasks");
        MainContainerActivity.drawlayoutmain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }
    private void getData(TaskType taskTypeselected){
        mSwipeRefreshLayout.setRefreshing(true);
        if(task==null)
            task=new Task();
        task.getTasks(recyclerView,getActivity(), ID_TEMP,mSwipeRefreshLayout,averageRatingbar,taskTypeselected);
    }

    @Override
    public void onStop() {
        super.onStop();
        task.removeListener();
    }
}
