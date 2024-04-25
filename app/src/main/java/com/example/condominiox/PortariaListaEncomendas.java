package com.example.condominiox;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class PortariaListaEncomendas extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Encomendas> userArrayList;
    MyAdapter myAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    private Button sairlistaportaria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_portaria_lista_encomendas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Aguarde! Buscando Dados...");
        progressDialog.show();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        userArrayList = new ArrayList<Encomendas>();
        myAdapter = new MyAdapter(PortariaListaEncomendas.this,userArrayList);

        recyclerView.setAdapter(myAdapter);

        EventChangeListener();

        sairlistaportaria = findViewById(R.id.button_sairlistaportaria);
        sairlistaportaria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PortariaListaEncomendas.this, AreaRestritaActivity.class);
                startActivity(intent);
            }
        });
    }

    private void EventChangeListener() {

        db.collection("Encomendas")
                .orderBy("data", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(error != null){

                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                    Log.e("Firestore error", error.getMessage());
                    return;
                }

                for(DocumentChange dc : value.getDocumentChanges()){

                    if(dc.getType() == DocumentChange.Type.ADDED){

                        userArrayList.add(dc.getDocument().toObject(Encomendas.class));

                    }

                    myAdapter.notifyDataSetChanged();
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();

                }


            }
        });
    }
}