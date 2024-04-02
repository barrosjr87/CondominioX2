package com.example.condominiox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Firebase;

public class MainActivity extends AppCompatActivity {

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

    public void morador(View v) {
        //Criação de intent para chamada de outra tela
        Intent iMorador = new Intent(this, MoradorActivity.class);
        //Envio de solicitação
        startActivity(iMorador);

    }
}


