package com.example.dell.rhythmmusic.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dell.rhythmmusic.MusicActivity;
import com.example.dell.rhythmmusic.R;
import com.example.dell.rhythmmusic.RequestContent;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RequestSong.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RequestSong#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestSong extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private EditText esongname, esingername;
    private Button rbtn;
    private String songName, singerName;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public RequestSong() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RequestSong.
     */
    // TODO: Rename and change types and number of parameters
    public static RequestSong newInstance(String param1, String param2) {
        RequestSong fragment = new RequestSong();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_request_song, container, false);
        ((MusicActivity)getActivity()).toolbar.setTitle("Request Song");

        esongname = view.findViewById(R.id.esongname);
        esingername = view.findViewById(R.id.esingername);
        rbtn = view.findViewById(R.id.rbtn);






        rbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songName = esongname.getText().toString();
                singerName = esingername.getText().toString();
            if(songName.isEmpty()) {
                esongname.setError("required");
            }
            else {
                final DatabaseReference dr1 = database.getReference("Requests/").child(songName);

                if (dr1 != null) {

                    final DatabaseReference dsong = database.getReference("Requests");

                    RequestContent requestContent = new RequestContent();
                    requestContent.setSong(songName);
                    requestContent.setSinger(singerName);
                    requestContent.setId(dsong.push().getKey());
                    final ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.show();
                    dr1.setValue(requestContent).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Request Sent Succesfully", Toast.LENGTH_SHORT).show();

                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }

            }
        });
    return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
