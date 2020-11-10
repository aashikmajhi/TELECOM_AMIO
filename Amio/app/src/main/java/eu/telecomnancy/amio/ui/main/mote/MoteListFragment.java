package eu.telecomnancy.amio.ui.main.mote;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import eu.telecomnancy.amio.R;
import eu.telecomnancy.amio.iotlab.entities.Mote;
import eu.telecomnancy.amio.ui.main.Constants;
import eu.telecomnancy.amio.ui.main.MainViewModel;

/**
 * A fragment representing a list of motes
 */
public class MoteListFragment extends Fragment {

    /**
     * Date adapter for the mote list
     */
    private MoteRecyclerViewAdapter _moteRecyclerViewAdapter;

    /**
     * Recycler view used for the mote list re-rendering when the orientation changes
     */
    private RecyclerView _recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MoteListFragment() { }

    /**
     * Give the right way to construct this fragment
     *
     * @return The instantiated fragment
     */
    public static MoteListFragment newInstance() {
        Bundle args = new Bundle();

        args.putInt(
                Constants.MoteList.ARG_COLUMN_COUNT,
                Constants.MoteList.COLUMN_COUNT);

        MoteListFragment fragment = new MoteListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainViewModel viewModel = new ViewModelProvider(requireActivity())
                .get(MainViewModel.class);

        viewModel.getMotes()
                .observe(
                        getViewLifecycleOwner(),
                        motes -> _moteRecyclerViewAdapter.setMotes(motes));
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        updateRecyclerLayer();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mote_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            _recyclerView = (RecyclerView) view;
            updateRecyclerLayer();
            _moteRecyclerViewAdapter = new MoteRecyclerViewAdapter(new ArrayList<Mote>(), getContext());
            _recyclerView.setAdapter(_moteRecyclerViewAdapter);
        }

        return view;
    }

    /**
     * Update the recycler view to mach the current orientation
     */
    private void updateRecyclerLayer() {
        Context context = _recyclerView.getContext();

        boolean isOrientationPortrait = getResources()
                .getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT;

        _recyclerView.setLayoutManager(isOrientationPortrait
                ? new LinearLayoutManager(context)
                : new GridLayoutManager(context, Constants.MoteList.COLUMN_COUNT));
    }

}
