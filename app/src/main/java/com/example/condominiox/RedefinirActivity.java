package com.example.condominiox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.graphics.Color;

import com.google.android.material.snackbar.Snackbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RedefinirActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private Button buttonRedefinir;

    String[] mensagensRedefinicao = {"Informe seu e-mail de recuperação!", "Solicitação de redefinição de senha enviada com sucesso!"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_redefinir);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        iniciarComponentes();

        //Criando métoco de verificação se todos os campos foram preenchidos
        buttonRedefinir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Criando Variáveis para receber o conteúdo digitado nos campos
                String email = editTextEmail.getText().toString();

                //verificação se todos os campos foram preenchidos
                if (email.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(v, mensagensRedefinicao[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }else{
                    Snackbar snackbar = Snackbar.make(v, mensagensRedefinicao[1], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }

            }
        });
    }

    private void iniciarComponentes(){
        editTextEmail = findViewById(R.id.editTextEmail_Redefinir);
        buttonRedefinir = findViewById(R.id.buttonEntrar_Redefinir);
    }
    public void sair_redefinir(View v) {
        //Criação de intent para chamada de outra tela
        Intent iSair = new Intent(this, MainActivity.class);
        //Envio de solicitação
        startActivity(iSair);

    }
}