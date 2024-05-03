package com.example.condominiox;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MoradorActivity extends AppCompatActivity {

    //Criando Objetos globais
    private TextView ola_Morador, apartamento_Morador, aviso_Morador, contador_Morador, tel1, tel2;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Objetos do recyclerView do morador
    RecyclerView recyclerView;
    ArrayList<Encomendas> userArrayList;
    MyAdapterMorador myAdapter;
    ProgressDialog progressDialog;


    String usuarioID;
    String avisoID;

    Integer cont;

    String numContato1;

    String numContato2;


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

        db = FirebaseFirestore.getInstance();
        userArrayList = new ArrayList<Encomendas>();

        tel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numContato1 = "01100000000";
                Intent ligarnum1 = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + Uri.encode(numContato1)));
                startActivity(ligarnum1);
            }
        });

        tel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numContato2 = "01199999999";
                Intent ligarnum2 = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + Uri.encode(numContato2)));
                startActivity(ligarnum2);
            }
        });




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
        Query query = db.collection("Encomendas").whereEqualTo("retirado", "Não");

        documentoReferencia.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                //recuperando a chave "nome" do método Salvar Dados Usuário do Activity CadastroUsuárioActivity.java no objeto ola_morador
                ola_Morador.setText("  Olá, " + (documentSnapshot.getString("nome")+ "!"));
                //recuperando a chave "nome" do método Salvar Dados Usuário do Activity CadastroUsuárioActivity.java no objeto ola_morador
                apartamento_Morador.setText("  Apartamento " + (documentSnapshot.getString("apto")));
            }
        });

        documentoReferencia1.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                //recuperando a chave "nome" do método Salvar Dados Usuário do Activity CadastroUsuárioActivity.java no objeto ola_morador
                aviso_Morador.setText((documentSnapshot.getString("data")) + ": " + (documentSnapshot.getString("aviso")));
            }
        });

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                cont = value.size();
                contador_Morador.setText(cont.toString());
            }
        });



    }

    //Criando método sem retorno (Void) para inicializar objetos globais
    private void iniciarComponentes(){
        ola_Morador = findViewById(R.id.textViewOla_Morador);
        apartamento_Morador = findViewById(R.id.textViewApartamento_Morador);
        aviso_Morador = findViewById(R.id.textViewAviso_Morador);
        contador_Morador = findViewById(R.id.contador_Encomendas);
        tel1 = findViewById(R.id.PhoneNumber1);
        tel2 = findViewById(R.id.PhoneNumber2);



    }

    public void encomenda(View v) {
        //Criação de intent para chamada de outra tela
        Intent iEncomenda = new Intent(this, EncomendaEntregueActivity.class);
        //Envio de solicitação
        startActivity(iEncomenda);

    }




}