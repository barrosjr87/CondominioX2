package com.example.condominiox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AreaRestritaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_area_restrita);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    //Criando métodos para acessar outra tela
    public void Avisos(View v) {
        //Criação de intent para chamada de outra tela
        Intent iAvisos = new Intent(this, CadastroAvisoActivity.class);
        //Envio de solicitação
        startActivity(iAvisos);
    }

    public void Usuario(View v) {
        //Criação de intent para chamada de outra tela
        Intent iUsuario = new Intent(this, CadastroUsuarioActivity.class);
        //Envio de solicitação
        startActivity(iUsuario);

    }

    public void Recebimento(View v) {
        //Criação de intent para chamada de outra tela
        Intent iRecebimento = new Intent(this, RecebimentoActivity.class);
        //Envio de solicitação
        startActivity(iRecebimento);

    }

    public void sair(View v) {
        //Criação de intent para chamada de outra tela
        Intent iSair = new Intent(this, MainActivity.class);
        //Envio de solicitação
        startActivity(iSair);

    }
}
