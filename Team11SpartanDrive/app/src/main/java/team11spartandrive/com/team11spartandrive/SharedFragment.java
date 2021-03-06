package team11spartandrive.com.team11spartandrive;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import team11spartadrive.com.helper.DriveFiles;
import team11spartadrive.com.helper.UsageDataHandler;

public class SharedFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText myFilter;

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types and number of parameters
    public static MyFilesFragment newInstance(String param1, String param2) {
        MyFilesFragment fragment = new MyFilesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    static MyFilesFragment myFilesFragment_instance = null;
    ListView lv;
    //   ArrayAdapter<String> adapter;
    CustomListAdapter ad;
    android.support.v4.app.FragmentTransaction ft ;


    public static MyFilesFragment getFragmentInstance(){

        if(myFilesFragment_instance == null){
            myFilesFragment_instance = new MyFilesFragment();
            return myFilesFragment_instance;
        }
        else{
            return myFilesFragment_instance;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        //Add Menu Options
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_myfiles, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.add_item:
                new AddActionHandler(getContext(), (HomePageActivity)getActivity()).show();
                return true;
            case R.id.userInformation:

                Intent myIntent = new Intent(getActivity(),UsageActivity.class);

                myIntent.putExtra("name", UsageDataHandler.getUserName());
                myIntent.putExtra("mail",UsageDataHandler.getUserEmail());
                myIntent.putExtra("totalUsedSpace",String.valueOf(UsageDataHandler.getUsageInstance().getTotalSpaceUsed()));
                myIntent.putExtra("totalFreeSpace",String.valueOf(UsageDataHandler.getUsageInstance().getTotalFreeSpace()));
                myIntent.putExtra("imageUrl",String.valueOf(UsageDataHandler.getUrl()));
                startActivity(myIntent);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //=======================================

    public void refresh(){
        Log.d("Status", " >>> Going to refresh the fragment files content");
        //getActivity().getSupportFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_shared, container, false);
        lv = (ListView) rootView.findViewById(R.id.listView);
        myFilter=(EditText) rootView.findViewById(R.id.myFilter);

        ad = new CustomListAdapter(getActivity(),DriveFiles.getDriveFileInstance().getSharedFileNameList(),DriveFiles.getDriveFileInstance().getShared_File_desc_list());

        lv.setAdapter(ad);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View rootView, int position,
                                           long id) {

                String temp_ID = DriveFiles.getDriveFileInstance().getIdFromName(lv.getItemAtPosition((int)id).toString());

                new PopupOfAction(getContext(), temp_ID, "SharedFile").show();

                return true;
            }
        });


        myFilter.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                if (ad != null) {
                    ad.getFilter().filter(cs);
                } else {
                    Log.d("filter", "no filter availible");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }
        });

        return rootView;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    //========================================

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}