package com.example.condominiox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MoradorActivity extends AppCompatActivity {

    //Criando Objetos globais
    private TextView ola_Morador, apartamento_Morador;
    private Button sair_Morador;
    //Criando objeto db que receberá a instancia do bancode dados Firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //Criando String que armazenará o ID dousuário
    String usuárioID;

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
            }
        });

    }

    //Criando ciclo de vida OnStart
    @Override
    protected void onStart() {
        super.onStart();
        //passando o instancia do ID do usuário corrente do Firebase Auth para a variável usuarioID
        usuárioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //Criando documento de referência que receberá a coleção criada no bancode dados que contém os dados do usuário
        DocumentReference documentoReferencia = db.collection("Usuários").document(usuárioID);
        documentoReferencia.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                //recuperando a chave "nome" do método Salvar Dados Usuário do Activity CadastroUsuárioActivity.java no objeto ola_morador
                ola_Morador.setText("Olá, " + (documentSnapshot.getString("nome")+ "!"));
                //recuperando a chave "nome" do método Salvar Dados Usuário do Activity CadastroUsuárioActivity.java no objeto ola_morador
                apartamento_Morador.setText("Apartamento " + (documentSnapshot.getString("apto")+"."));
            }
        });

    }

    //Criando método sem retorno (Void) para inicializar objetos globais
    private void iniciarComponentes(){
        ola_Morador = findViewById(R.id.textViewOla_Morador);
        apartamento_Morador = findViewById(R.id.textViewApartamento_Morador);
        sair_Morador = findViewById(R.id.sair_Morador);
    }

    public void historico(View v) {
        //Criação de intent para chamada de outra tela
        Intent iHistorico = new Intent(this, MinhasEncomendasActivity.class);
        //Envio de solicitação
        startActivity(iHistorico);

    }

}