package com.example.mainactivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class FamilieboblaNySamtaleFragment extends Fragment {
    public FamilieboblaNySamtaleFragment() {}

    SharedPreferences sharedPreferences;
    Database database;

    private Spinner spinner;
    private User selectedUser;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_familiebobla_ny_samtale, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        database = new Database(getActivity());
        spinner = view.findViewById(R.id.users);
        Button addSamtale = view.findViewById(R.id.lagSamtale);

        sharedPreferences = requireActivity().getSharedPreferences(User.SESSION, Context.MODE_PRIVATE);

        addUsersToDropdown();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedUser = (User) parent.getItemAtPosition(position);
                System.out.println(selectedUser);
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

        addSamtale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeNewSamtale();
            }
        });
    }

    private void makeNewSamtale() {
        int meID = Integer.parseInt(sharedPreferences.getString(User.ID, null));
        long addToDatabase = database.makeNewConversation(meID, selectedUser);

        if (addToDatabase >= 0) {
            Toast.makeText(getContext(),"Made conversation with: " + selectedUser.getName(), Toast.LENGTH_SHORT).show();
            navController.navigateUp();
        }

        System.out.println("New conversation with: " + selectedUser);
    }

    private void addUsersToDropdown() {
        Cursor data = database.getData();
        System.out.println("ME: " + sharedPreferences.getString(User.NAME, null));
        ArrayList<User> arrayList = new ArrayList<>();

        while(data.moveToNext()) {
            if (!data.getString(0).equals(sharedPreferences.getString(User.ID, null))) {
                int id = data.getInt(0);
                String name = data.getString(1);
                arrayList.add( new User(id,name));
            }
        }
        if (arrayList.size() > 0) {
            ArrayAdapter<User> adapter = new ArrayAdapter<User>(getActivity(), android.R.layout.simple_spinner_item, arrayList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
    }
}