package com.example.mainactivity;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class FamilieOpprettFragment extends Fragment {

    private TextView navn, passord, passordIgjen;
    private Button opprettFamilie;

    private String name, date;
    Database database;
    SharedPreferences sharedPreferences;

    public FamilieOpprettFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_familie_opprett, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database = new Database(getActivity());
        sharedPreferences = this.requireActivity().getSharedPreferences(User.SESSION, Context.MODE_PRIVATE);

        navn = view.findViewById(R.id.OpprettFamilieOpprettNavn);
        passord = view.findViewById(R.id.OpprettFamilieOpprettPassord);
        passordIgjen = view.findViewById(R.id.OpprettFamilieGjentaPassord);
        opprettFamilie = view.findViewById(R.id.OpprettFamilieBtn);

        name = getArguments().getString("NAVN");
        date = getArguments().getString("DATO");

        opprettFamilie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passord.getText().toString().equals(passordIgjen.getText().toString())) {
                    database.addFamilyToDatabase(navn.getText().toString(), passord.getText().toString(), Integer.parseInt(sharedPreferences.getString(User.ID, null)));

                    Cursor familyIdQuery = database.getFamilyIdByLastRow();

                    int familyID = 1;

                    while(familyIdQuery.moveToNext()) {
                        familyID = Integer.parseInt(familyIdQuery.getString(0));
                    }

                    database.updateUserFamily(Integer.parseInt(sharedPreferences.getString(User.ID, null)), familyID);
                    database.addUserToDatabaseBIRTHDAY(name, date, String.valueOf(familyID));
                    // Setting session family to familyID
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(User.FAMILIE, String.valueOf(familyID));
                    editor.apply();

                    Toast.makeText(getActivity(), "Data successfully inserted", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                } else
                    Toast.makeText(getActivity(), "Passordene må være like", Toast.LENGTH_SHORT).show();
            }
        });
    }
}