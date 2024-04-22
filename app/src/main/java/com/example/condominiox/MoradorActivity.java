package com.example.condominiox;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MoradorActivity extends AppCompatActivity {

    //Criando Objetos globais

    private TextView ola_Morador, apartamento_Morador, aviso_Morador;
    private TextView codigoview_Historico, tipoview_Historico, dataview_Historico;
    private Button sair_Morador;

    //Criando objeto db que receberá a instancia do bancode dados Firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String usuarioID;
    String avisoID;
    String dataViewID;
    String codigoViewID;
    String tipoViewID;


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
        sair_Morador.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                //Criação de intent para chamada de outra tela
                Intent iSair = new Intent(MoradorActivity.this, MainActivity.class);
                //Envio de solicitação
                startActivity(iSair);
                finish();
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
        dataViewID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        codigoViewID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        tipoViewID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Criando documento de referência que receberá a coleção criada no banco de dados que contém os dados do usuário
        DocumentReference documentoReferencia = db.collection("Usuários").document(usuarioID);
        DocumentReference documentoReferencia1 = db.collection("avisos").document(avisoID);
        DocumentReference documentoReferencia2 = db.collection("Encomendas").document(dataViewID);
        DocumentReference documentoReferencia3 = db.collection("Encomendas").document(codigoViewID);
        DocumentReference documentoReferencia4 = db.collection("Encomendas").document(tipoViewID);
        documentoReferencia.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                //recuperando a chave "nome" do método Salvar Dados Usuário do Activity CadastroUsuárioActivity.java no objeto ola_morador
                ola_Morador.setText("Olá, " + (documentSnapshot.getString("nome")+ "!"));
                //recuperando a chave "nome" do método Salvar Dados Usuário do Activity CadastroUsuárioActivity.java no objeto ola_morador
                apartamento_Morador.setText("Apartamento " + (documentSnapshot.getString("apto")+"."));
            }
        });

        documentoReferencia1.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                //recuperando a chave "nome" do método Salvar Dados Usuário do Activity CadastroUsuárioActivity.java no objeto ola_morador
                aviso_Morador.setText((documentSnapshot.getString("data")) + ": " + (documentSnapshot.getString("aviso")));
            }
        });
        documentoReferencia2.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                dataview_Historico.setText((documentSnapshot.getString("data")));
            }
        });
        documentoReferencia3.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                codigoview_Historico.setText((documentSnapshot.getString("Cep")));
            }
        });
        documentoReferencia4.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                tipoview_Historico.setText((documentSnapshot.getString("tipo")));
            }
        });
    }


    //Criando método sem retorno (Void) para inicializar objetos globais
    private void iniciarComponentes(){
        ola_Morador = findViewById(R.id.textViewOla_Morador);
        apartamento_Morador = findViewById(R.id.textViewApartamento_Morador);
        sair_Morador = findViewById(R.id.sair_Morador);
        aviso_Morador = findViewById(R.id.textViewAviso_Morador);
        dataview_Historico = findViewById(R.id.dataView_Historico);
        codigoview_Historico = findViewById(R.id.codigoView_Historico);
        tipoview_Historico = findViewById(R.id.tipoView_Historico);
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