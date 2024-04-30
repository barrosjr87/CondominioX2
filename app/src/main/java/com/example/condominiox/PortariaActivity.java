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

public class PortariaActivity extends AppCompatActivity {

    //Criando objetos globais
    private EditText editTextEmail_Portaria,editTextSenha_Portaria;
    private Button acessarAreaRestrita;

    String[] mensagensLogin = {"Preencha todos os campos!", "Digite um login de acesso válido!", "Digite sua senha!", "Acesso não autorizado!"};
    String[] loginsAdm = {"admin@parqueserafim.com.br"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_portaria);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        iniciarComponentes();

        //Criando métoco de verificação se todos os campos foram preenchidos
        acessarAreaRestrita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Criando Variáveis para receber o conteúdo digitado nos campos
                String email = editTextEmail_Portaria.getText().toString();
                String senha = editTextSenha_Portaria.getText().toString();

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
                }else if (!email.equalsIgnoreCase(loginsAdm[0])) {
                    Snackbar snackbar = Snackbar.make(v, mensagensLogin[3], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }else{
                    autenticarAdmin(v);
                }

            }
        });

    }

    private void autenticarAdmin(View view){

        String email = editTextEmail_Portaria.getText().toString();
        String senha = editTextSenha_Portaria.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(loginsAdm[0],senha).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if (task.isSuccessful()){
                    AreaRestrita();
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

    private void iniciarComponentes(){
        editTextEmail_Portaria = findViewById(R.id.editTextEmail_Portaria);
        editTextSenha_Portaria = findViewById(R.id.editTextSenha_Portaria);
        acessarAreaRestrita = findViewById(R.id.buttonEntrar_Portaria);
    }

    //Criando métodos para acessar outra tela
    public void AreaRestrita() {
        //Criação de intent para chamada de outra tela
        Intent iAreaRestrita = new Intent(this, AreaRestritaActivity.class);
        //Envio de solicitação
        startActivity(iAreaRestrita);
    }

}