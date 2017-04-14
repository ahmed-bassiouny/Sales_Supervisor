package pharmaproject.ahmed.example.packagecom.pharmaproject;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import pharmaproject.ahmed.example.packagecom.pharmaproject.database.Task;
import pharmaproject.ahmed.example.packagecom.pharmaproject.helper.helper;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListOfTasks extends Fragment {
    RecyclerView recyclerView;
    String EMAILTEMP;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Task task;
    RatingBar averageRatingbar;
    pharmaproject.ahmed.example.packagecom.pharmaproject.helper.helper helper=new helper(getActivity());
    TextView nameemployee;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_list_of_tasks, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swip);
        TextView empty = (TextView)view.findViewById(R.id.empty_view);
        nameemployee=(TextView) view.findViewById(R.id.nameemployee);
        empty.setTypeface(utils.getFont(getActivity()));
        EMAILTEMP = getArguments().getString("KEY");
        nameemployee.setTypeface(utils.getFont(getActivity()));
        nameemployee.setText(getArguments().getString("NAME"));
        averageRatingbar= (RatingBar) view.findViewById(R.id.ratingBarTotal);
        averageRatingbar.setEnabled(false);
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
                getData();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getData();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Tasks");
        MainContainerActivity.drawlayoutmain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }
    private void getData(){
        mSwipeRefreshLayout.setRefreshing(true);
        if(task==null)
            task=new Task();
        task.getTasks(recyclerView,getActivity(),EMAILTEMP,mSwipeRefreshLayout,averageRatingbar);
    }
}
