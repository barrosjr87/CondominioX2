package com.example.condominiox;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CadastroAvisoActivity extends AppCompatActivity {

    //Criando objetos globais;
    private EditText textoAviso, dataAviso;

    private Button salvarAviso;

    //Criando Variáveis globais
    String[] mensagens = {"Preencha todos os campos!", "Aviso cadastrada com sucesso!"};
    //Cria variável global utilizada no map de usuários
    String avisoID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro_aviso);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        iniciarComponentes();

        //Criando métoco de verificação se todos os campos foram preenchidos
        salvarAviso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Criando Variáveis para receber o conteúdo digitado nos campos
                String aviso = textoAviso.getText().toString();
                String data = dataAviso.getText().toString();

                //verificação se todos os campos foram preenchidos
                if(aviso.isEmpty() || data.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(v, mensagens[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }else{
                    cadastrarAviso(v);
                    Snackbar snackbar = Snackbar.make(v, mensagens[1], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();

                }

            }
        });
    }

    //Criando método para cadastrar encomenda recebida no banco de dados
    private void cadastrarAviso(View v){
        //criando variável que recebe o conteúdo digitado no campo aviso
        String aviso = textoAviso.getText().toString();
        String data = dataAviso.getText().toString();
        //Inicia Banco de Dados no Firebase com a Instancia do Banco de Dados db
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Cria tabela Map de ENcomendas com a string chave e o objeto
        Map<String,Object> avisos = new HashMap<>();
        avisos.put("aviso",aviso);
        avisos.put("data",data);


        //Criando documento de referencia que vai receber o banco de dados e cria uma coleção
        DocumentReference documentoReferencia = db.collection("avisos").document(FirebaseAuth.getInstance().getCurrentUser().getProviderId());
        //cada usuário terá umdocumento referencia com seu usuário ID

        //setando objeto map usuário no documento e aplicando interface onSucessListener
        documentoReferencia.set(avisos).addOnSuccessListener(new OnSuccessListener<Void>() {
                    //log debug com mensagem de Sucesso
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("db","Sucesso ao Salvar os Dados!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("db_error","Erro ao Salvar os Dados" + e.toString());
                    }
                });



    }


    //Criando métodos para associar os objetos aos ID's dos campos de referência
    private void iniciarComponentes(){

        textoAviso = findViewById(R.id.cadastro_Aviso);
        dataAviso = findViewById(R.id.data_Aviso);
        salvarAviso = findViewById(R.id.buttonSalvar_Aviso);
    }



    public void sair(View v) {
        //Criação de intent para chamada de outra tela
        Intent iSair = new Intent(this, MainActivity.class);
        //Envio de solicitação
        startActivity(iSair);

    }
}