package com.example.condominiox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MinhasEncomendasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_minhas_encomendas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void encomenda(View v) {
        //Criação de intent para chamada de outra tela
        Intent iEncomenda = new Intent(this, EncomendaEntregueActivity.class);
        //Envio de solicitação
        startActivity(iEncomenda);

    }

    public void sair(View v) {
        //Criação de intent para chamada de outra tela
        Intent iSair = new Intent(this, MainActivity.class);
        //Envio de solicitação
        startActivity(iSair);

    }
    public void telacep(View v) {
        //Criação de intent para chamada de outra tela
        Intent itelacep = new Intent(this, ConsultaCepActivity.class);
        //Envio de solicitação
        startActivity(itelacep);
    }
}