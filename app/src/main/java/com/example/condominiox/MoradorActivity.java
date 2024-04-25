package com.example.condominiox;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MoradorActivity extends AppCompatActivity {

    //Criando Objetos globais
    private TextView ola_Morador, apartamento_Morador, aviso_Morador;

    //Criando objeto db que receberá a instancia do bancode dados Firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Objetos do recyclerView do morador
    RecyclerView recyclerView;
    ArrayList<Encomendas> userArrayList;
    MyAdapterMorador myAdapter;
    //FirebaseFirestore dbv;
    ProgressDialog progressDialog;


    String usuarioID;
    String avisoID;
    String dataViewID;
    String codigoViewID;
    String tipoViewID;

    String numApto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_morador);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        iniciarComponentes();


        //recyclerView do morador
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Aguarde! Buscando Dados...");
        progressDialog.show();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        userArrayList = new ArrayList<Encomendas>();
        myAdapter = new MyAdapterMorador(MoradorActivity.this,userArrayList);

        recyclerView.setAdapter(myAdapter);

        EventChangeListener();

    }

    //Criando ciclo de vida OnStart
    @Override
    protected void onStart() {
        super.onStart();
        //passando o instancia do ID do usuário corrente do Firebase Auth para a variável usuarioID
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        avisoID = FirebaseAuth.getInstance().getCurrentUser().getProviderId();

        //Criando documento de referência que receberá a coleção criada no banco de dados que contém os dados do usuário
        DocumentReference documentoReferencia = db.collection("Usuários").document(usuarioID);
        DocumentReference documentoReferencia1 = db.collection("avisos").document(avisoID);

        documentoReferencia.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                //recuperando a chave "nome" do método Salvar Dados Usuário do Activity CadastroUsuárioActivity.java no objeto ola_morador
                ola_Morador.setText("Olá, " + (documentSnapshot.getString("nome")+ "!"));
                //recuperando a chave "nome" do método Salvar Dados Usuário do Activity CadastroUsuárioActivity.java no objeto ola_morador
                apartamento_Morador.setText("Apartamento " + (documentSnapshot.getString("apto")));
            }
        });

        documentoReferencia1.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                //recuperando a chave "nome" do método Salvar Dados Usuário do Activity CadastroUsuárioActivity.java no objeto ola_morador
                aviso_Morador.setText((documentSnapshot.getString("data")) + ": " + (documentSnapshot.getString("aviso")));
            }
        });
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

    //Criando método sem retorno (Void) para inicializar objetos globais
    private void iniciarComponentes(){
        ola_Morador = findViewById(R.id.textViewOla_Morador);
        apartamento_Morador = findViewById(R.id.textViewApartamento_Morador);
        aviso_Morador = findViewById(R.id.textViewAviso_Morador);
    }

    public void sair_Morador(View v) {
        //Criação de intent para chamada de outra tela
        Intent iSair = new Intent(this, MainActivity.class);
        //Envio de solicitação
        startActivity(iSair);
    }

    public void telacep(View v) {
        //Criação de intent para chamada de outra tela
        Intent iTelacep = new Intent(this, ConsultaCepActivity.class);
        //Envio de solicitação
        startActivity(iTelacep);

    }
    public void encomenda(View v) {
        //Criação de intent para chamada de outra tela
        Intent iTelaencomenda = new Intent(this, EncomendaEntregueActivity.class);
        //Envio de solicitação
        startActivity(iTelaencomenda);

    }
}