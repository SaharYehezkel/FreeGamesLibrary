package com.example.freegameslibrary;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {
    private FirebaseAuth mAuth;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        // Inflate the layout for this fragment
        mAuth = FirebaseAuth.getInstance();

        View view = inflater.inflate(R.layout.fragment_register, container, false);
        Button registerButton = view.findViewById(R.id.buttonRegisterToDB);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FunReg(view);
            }
        });

        return view;
    }

    public void FunReg(View view){
        EditText email = view.findViewById(R.id.registerEmailText);
        EditText password = view.findViewById(R.id.registerPasswordText);
        String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();


        // Register the user in Firebase Authentication
        mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(TaskExecutors.MAIN_THREAD,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(view.getContext(), "Register successfully.", Toast.LENGTH_LONG).show();
                            addData(view);
                            Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment);
                        } else {
                            // Improved error handling
                            Exception exception = task.getException();
                            String errorMessage = exception != null ? exception.getMessage() : "Unknown error occurred";
                            Toast.makeText(getActivity(), "Authentication failed: " + errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    public void addData(View view){
        EditText emailEdittext = view.findViewById(R.id.registerEmailText);
        EditText passwordEditText = view.findViewById(R.id.registerPasswordText);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        String str = currentUser.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(str);

        User user1 = new User(emailEdittext.getText().toString(),passwordEditText.getText().toString());

        myRef.setValue(user1);
    }
}