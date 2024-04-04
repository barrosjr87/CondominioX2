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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CadastroUsuarioActivity extends AppCompatActivity {

    //Criando objetos globais
    private EditText editTextNome_Cadastro, editTextApto_Cadastro, editTextSenha_Cadastro,editTextEmail_Cadastro;
    private Button buttonCadastrar_Cadastro;
    //Array global de Mensagens de erro e sucesso
    String[] mensagens = {"Preencha todos os campos!", "Cadastro realizado com sucesso!"};
    //Cria variável global utilizada no map de usuários
    String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro_usuario);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        iniciarComponetes();

        //Criando métoco de verificação se todos os campos foram preenchidos
        buttonCadastrar_Cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Criando Variáveis para receber o conteúdo digitado nos campos
                String nome = editTextNome_Cadastro.getText().toString();
                String apto = editTextApto_Cadastro.getText().toString();
                String email = editTextEmail_Cadastro.getText().toString();
                String senha = editTextSenha_Cadastro.getText().toString();

                //verificação se todos os campos foram preenchidos
                if(nome.isEmpty() || apto.isEmpty() || email.isEmpty() || senha.isEmpty()){
                    Snackbar snackbar = Snackbar.make(v,mensagens[0],Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }else{
                    cadastrarUsuario(v);
                }

            }
        });
    }

    //Criando Método para Cadastrar usuário
    private void cadastrarUsuario(View v){

        String email = editTextEmail_Cadastro.getText().toString();
        String senha = editTextSenha_Cadastro.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    //método para salvar dados no banco
                    salvarDadosUsuario();

                    Snackbar snackbar = Snackbar.make(v,mensagens[1],Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }else{
                    //criando variável que vai armazenar as mensagens de erro
                    String erro;
                    try {
                        throw task.getException();

                    }catch (FirebaseAuthWeakPasswordException e){
                        erro = "A senha deve conter no mínimo 6 caracteres";
                    }catch (FirebaseAuthUserCollisionException e){
                        erro = "E-mail já cadastrado";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erro = "E-mail inválido";
                    }catch (Exception e){
                        erro = "Erro ao cadastrar usuário";
                    }

                    Snackbar snackbar = Snackbar.make(v,erro,Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();

                }
            }

        });

    }
    //Criando método para salvar dados no banco
    private void salvarDadosUsuario(){
        //criando variável que recebe o conteúdo digitado no campo nome
        String nome = editTextNome_Cadastro.getText().toString();
        String apto = editTextApto_Cadastro.getText().toString();
        //Inicia Banco de Dados no Firebase com a Instancia do Banco de Dados db
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Cria tabela Map de usuários com a string chave e o objeto
        Map<String,Object> usuarios = new HashMap<>();
        usuarios.put("nome",nome);
        usuarios.put("apto",apto);

        //variável usuarioID recebe instancia do usuário corrente do FirebaseAuth
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Criando documento de referencia que vai receber o banco de dados e cria uma coleção
        DocumentReference documentoReferencia = db.collection("Usuários").document(usuarioID);
        //cada usuário terá umdocumento referencia com seu usuário ID

        //setando objeto map usuário no documento e aplicando interface onSucessListener
        documentoReferencia.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
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
    private void iniciarComponetes(){

        editTextNome_Cadastro = findViewById(R.id.editTextNome_Cadastro);
        editTextApto_Cadastro = findViewById(R.id.editTextApto_Cadastro);
        editTextEmail_Cadastro = findViewById(R.id.editTextEmail_Cadastro);
        editTextSenha_Cadastro = findViewById(R.id.editTextSenha_Cadastro);
        buttonCadastrar_Cadastro = findViewById(R.id.buttonCadastrar_Cadastro);
    }

    public void sair(View v) {
        //Criação de intent para chamada de outra tela
        Intent iSair = new Intent(this, MainActivity.class);
        //Envio de solicitação
        startActivity(iSair);

    }
}