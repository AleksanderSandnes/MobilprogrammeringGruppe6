package com.example.mainactivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

public class ProfilRedigerFragment extends Fragment {

    Database database;
    SharedPreferences sharedPreferences;

    private EditText endreNavn, endreEmail, endreMobilnr;
    private DatePicker endreBursdag;


    public ProfilRedigerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profil_rediger, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Avbryt knapp
        Button avbryt = view.findViewById(R.id.EndreProfilAvbryt);

        // Lagre knapp
        Button send = view.findViewById(R.id.EndreProfilRedigerBtn);

        sharedPreferences = requireActivity().getSharedPreferences(User.SESSION, MODE_PRIVATE);

        endreNavn = view.findViewById(R.id.endreProfilNavn);

        endreBursdag = view.findViewById(R.id.endreProfilBursdag);

        endreEmail = view.findViewById(R.id.endreProfilEmail);

        endreMobilnr = view.findViewById(R.id.profilMobilnr);



        setUserData();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = endreNavn.getText().toString();
                String newBirthday = endreBursdag.getDayOfMonth() + "." + (endreBursdag.getMonth()+1) + "." + endreBursdag.getYear();
                String newEmail = endreEmail.getText().toString();
                String newMobilnr = endreMobilnr.getText().toString();

                if (validUserInfo(newName, newBirthday, newEmail, newMobilnr)) {
                    UpdateUser(newName, newBirthday, newEmail, newMobilnr);
                    setUserData();
                } else {
                    Toast.makeText(getActivity(), "You must put something in the text field", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void setUserData() {
        endreNavn.setText(sharedPreferences.getString(User.NAME, null));
        endreEmail.setText(sharedPreferences.getString(User.EMAIL, null));
        endreMobilnr.setText(sharedPreferences.getString(User.MOBILNR, null));

        String date = sharedPreferences.getString(User.BIRTHDAY, null);
        String[] parts = date.split("\\.");
        Integer dag, maaned, aar;
        dag = Integer.parseInt(parts[0]);
        maaned = Integer.parseInt(parts[1]);
        aar = Integer.parseInt(parts[2]);
        System.out.println(dag + "," + maaned + "," + aar);
        endreBursdag.updateDate(aar, maaned-1, dag);
    }

    public void UpdateUser(String newName, String newBirthday, String newEmail, String newMobilnr) {
        boolean insertData = database.updateUserInDatabase(sharedPreferences.getString(User.ID, null), newName, newBirthday, newEmail, newMobilnr);

        if (insertData)
            Toast.makeText(getActivity(), "Data successfully inserted", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
    }

    private boolean validUserInfo(String name, String email, String birthday, String mobilnr) {
        return name.length() != 0 && email.length() != 0 && birthday.length() != 0 && mobilnr.length() != 0;

        //Kan evt. legge in regEx på email etterhvert.
    }


}