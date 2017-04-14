package pharmaproject.ahmed.example.packagecom.pharmaproject;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import pharmaproject.ahmed.example.packagecom.pharmaproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowFeedback extends Fragment {

    TextView nameoftask,feedback,rateaboutdoctortxt;
    RatingBar rateaboutdoctor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_show_feedback, container, false);
        nameoftask=(TextView)view.findViewById(R.id.nameoftask);
        feedback=(TextView)view.findViewById(R.id.feedback);
        rateaboutdoctortxt=(TextView)view.findViewById(R.id.rateaboutdoctortxt);
        rateaboutdoctor=(RatingBar)view.findViewById(R.id.ratedoctor);

        nameoftask.setTypeface(utils.getFont(getActivity()));
        feedback.setTypeface(utils.getFont(getActivity()));
        rateaboutdoctortxt.setTypeface(utils.getFont(getActivity()));

        nameoftask.setText(getArguments().getString("nameoftask"));
        feedback.setText(getArguments().getString("feedback"));
        rateaboutdoctor.setProgress(getArguments().getInt("rateforDoctor"));
        return view;
    }
}
