package com.example.condominiox;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EncomendaEntregueActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Objetos do recyclerView do morador
    RecyclerView recyclerView;
    ArrayList<Encomendas> userArrayList;
    MyAdapterMorador myAdapter;
    ProgressDialog progressDialog;

    Button btVistar;

    String usuarioID;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_encomenda_entregue);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        iniciarComponentes();

        btVistar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDocument();
                Snackbar snackbar = Snackbar.make(v, "Encomendas Atualizadas", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }
        });


        //recyclerView do morador
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Aguarde! Buscando Dados...");
        progressDialog.show();

        recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        userArrayList = new ArrayList<Encomendas>();
        myAdapter = new MyAdapterMorador(EncomendaEntregueActivity.this,userArrayList);

        recyclerView.setAdapter(myAdapter);

        EventChangeListener();

    }

    private void EventChangeListener() {

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference reference = db.collection("Usuários").document(usuarioID);

        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot docSnapshot = task.getResult();

                    if (docSnapshot.exists()) {
                        String info = (String) docSnapshot.getData().get("apto");

                        db.collection("Encomendas")
                                .whereEqualTo("apto", info)
                                .orderBy("data", Query.Direction.DESCENDING)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                        if (error != null) {

                                            if (progressDialog.isShowing())
                                                progressDialog.dismiss();
                                            Log.e("Firestore error", error.getMessage());
                                            return;
                                        }

                                        for (DocumentChange dc : value.getDocumentChanges()) {

                                            if (dc.getType() == DocumentChange.Type.ADDED) {

                                                userArrayList.add(dc.getDocument().toObject(Encomendas.class));

                                            }

                                            myAdapter.notifyDataSetChanged();
                                            if (progressDialog.isShowing())
                                                progressDialog.dismiss();

                                        }


                                    }
                                });

                    }

                }

            }
        });

    }

    public void updateDocument() {

        for (Encomendas encomenda: userArrayList){
            DocumentReference docRef = db.collection("Encomendas")
                    .document(encomenda.id);

            Map<String, Object> docUpdate = new HashMap<>();
            docUpdate.put("retirado", "Sim");

            docRef.update(docUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d("db", "Atualizado com Sucesso!");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("db error", "Falha ao atualizar!", e);
                }
            });
        }



    }


    private void iniciarComponentes(){
        btVistar = findViewById(R.id.buttonAtualizar);

    }

    public void sair(View v) {
        //Criação de intent para chamada de outra tela
        Intent iSair = new Intent(this, MainActivity.class);
        //Envio de solicitação
        startActivity(iSair);

    }

    public void voltar(View v) {
        //Criação de intent para chamada de outra tela
        Intent ivoltar = new Intent(this, MoradorActivity.class);
        //Envio de solicitação
        startActivity(ivoltar);

    }

    public void telacep(View v) {
        //Criação de intent para chamada de outra tela
        Intent iTelacep = new Intent(this, ConsultaCepActivity.class);
        //Envio de solicitação
        startActivity(iTelacep);
    }
}
