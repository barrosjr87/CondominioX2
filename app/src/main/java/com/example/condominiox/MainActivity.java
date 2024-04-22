package com.example.condominiox;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    //Criando objetos globais
    private EditText editTextEmail_Inicio,editTextSenha_Inicio;
    private Button buttonEntrar_Inicio;

    String[] mensagensLogin = {"Preencha todos os campos!", "Digite seu E-mail!", "Digite sua senha"};


    @Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        
        IniciarComponentes();

        //Criando métoco de verificação se todos os campos foram preenchidos
        buttonEntrar_Inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Criando Variáveis para receber o conteúdo digitado nos campos
                String email = editTextEmail_Inicio.getText().toString();
                String senha = editTextSenha_Inicio.getText().toString();

                //verificação se todos os campos foram preenchidos
                if (email.isEmpty() && senha.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(v, mensagensLogin[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                } else if (email.isEmpty()) {
                        Snackbar snackbar = Snackbar.make(v, mensagensLogin[1], Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                    }else if (senha.isEmpty()) {
                            Snackbar snackbar = Snackbar.make(v, mensagensLogin[2], Snackbar.LENGTH_SHORT);
                            snackbar.setBackgroundTint(Color.WHITE);
                            snackbar.setTextColor(Color.BLACK);
                            snackbar.show();
                        }else{
                            autenticarUsuario(v);
                        }

            }
        });
}

    private void autenticarUsuario(View view){

        String email = editTextEmail_Inicio.getText().toString();
        String senha = editTextSenha_Inicio.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if (task.isSuccessful()){
                    morador();
                }else {
                    String erro;

                    try {
                        throw task.getException();
                    }catch (Exception e){
                        erro = "Falha ao realizar Login! Verifique os dados.";
                    }
                    Snackbar snackbar = Snackbar.make(view, erro, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            }
        });
    }

    private void IniciarComponentes(){
        editTextEmail_Inicio = findViewById(R.id.editTextEmail_Inicio);
        editTextSenha_Inicio = findViewById(R.id.editTextSenha_Inicio);
        buttonEntrar_Inicio = findViewById(R.id.buttonEntrar_Inicio);
    }

    //Criando método para acessar outra tela
    public void redefinir(View v) {
        //Criação de intent para chamada de outra tela
        Intent iRedefinir = new Intent(this, RedefinirActivity.class);
        //Envio de solicitação
        startActivity(iRedefinir);
    }


    public void portaria(View v) {
        //Criação de intent para chamada de outra tela
        Intent iPortaria = new Intent(this, PortariaActivity.class);
        //Envio de solicitação
        startActivity(iPortaria);
    }

    public void morador() {
        //Criação de intent para chamada de outra tela
        Intent iMorador = new Intent(this, MoradorActivity.class);
        //Envio de solicitação
        startActivity(iMorador);
    }
}


