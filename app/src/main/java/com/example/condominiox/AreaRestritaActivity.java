package com.example.condominiox;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
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
import java.util.List;

public class AreaRestritaActivity extends AppCompatActivity {

    private Button btlistaportaria;

    private EditText pesquisaPorApto;

    private Button buttonBuscar;

    RecyclerView recyclerView;
    ArrayList<Encomendas> userArrayList;
    MyAdapter myAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_area_restrita);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {;
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        userArrayList = new ArrayList<Encomendas>();
        myAdapter = new MyAdapter(AreaRestritaActivity.this,userArrayList);

        recyclerView.setAdapter(myAdapter);

        iniciarComponentes();

        pesquisarEncomenda();


        btlistaportaria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AreaRestritaActivity.this, PortariaListaEncomendas.class);
                startActivity(intent);
            }
        });


    }

    private void EventChangeListener() {
        String numApto = pesquisaPorApto.getText().toString();

        db.collection("Encomendas")
                .whereEqualTo("apto", numApto )
                .orderBy("data", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {

                            Log.e("Firestore error", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {

                            if (dc.getType() == DocumentChange.Type.ADDED) {

                                userArrayList.add(dc.getDocument().toObject(Encomendas.class));

                            }

                            myAdapter.notifyDataSetChanged();

                        }


                    }
                });
    }

    private void iniciarComponentes(){

       btlistaportaria = findViewById(R.id.button_ListaPortaria);
       pesquisaPorApto = findViewById(R.id.EditTextPesquisa_AreaRestrita);
       buttonBuscar = findViewById(R.id.button_Buscar);

    }

    private void pesquisarEncomenda(){
        buttonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userArrayList.isEmpty()){
                    EventChangeListener();
                }else{
                    userArrayList.clear();
                    EventChangeListener();
                }


            }
        });
    }

    //Criando métodos para acessar outra tela
    public void Avisos(View v) {
        //Criação de intent para chamada de outra tela
        Intent iAvisos = new Intent(this, CadastroAvisoActivity.class);
        //Envio de solicitação
        startActivity(iAvisos);
    }

    public void Usuario(View v) {
        //Criação de intent para chamada de outra tela
        Intent iUsuario = new Intent(this, CadastroUsuarioActivity.class);
        //Envio de solicitação
        startActivity(iUsuario);

    }

    public void Recebimento(View v) {
        //Criação de intent para chamada de outra tela
        Intent iRecebimento = new Intent(this, RecebimentoActivity.class);
        //Envio de solicitação
        startActivity(iRecebimento);

    }

    public void sair(View v) {
        //Criação de intent para chamada de outra tela
        Intent iSair = new Intent(this, MainActivity.class);
        //Envio de solicitação
        startActivity(iSair);

    }
}
