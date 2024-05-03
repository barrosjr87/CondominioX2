package com.example.condominiox;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.annotation.SuppressLint;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
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

import java.lang.ref.Cleaner;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RecebimentoActivity extends AppCompatActivity {


    //Criando objetos globais
    private EditText dataRecebimento, aptoRecebimento, cepRecebimento;

    private RadioButton radioCarta, radioPacote;

    private Button cadastrarRecebimento;

    String radioGroup;

    String[] mensagens = {"Preencha todos os campos!", "Selecione um tipo de Encomenda!", "Encomenda cadastrada com SUCESSO! Notificação enviada ao morador."};
    //Cria variável global utilizada no map de usuários

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recebimento);

        //Notificação enviado pelo porteiro tipo PUSH
        // verifica a versão do android se e maior que a versão TIRAMISU
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(RecebimentoActivity.this,
                    android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(RecebimentoActivity.this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        iniciarComponentes();


        //Criando métoco de verificação se todos os campos foram preenchidos
        cadastrarRecebimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Criando Variáveis para receber o conteúdo digitado nos campos
                String data = dataRecebimento.getText().toString();
                String apartamento = aptoRecebimento.getText().toString();
                String Cep = cepRecebimento.getText().toString();
                if(radioCarta.isChecked()){
                    radioGroup = "Carta";
                }else if(radioPacote.isChecked()){
                    radioGroup = "Pacote";
                }else{
                    radioGroup = null;
                }

                //verificação se todos os campos foram preenchidos
                if(data.isEmpty() || apartamento.isEmpty() || Cep.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(v, mensagens[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }else if(radioCarta.isChecked()==false && radioPacote.isChecked()==false){
                    Snackbar snackbar = Snackbar.make(v, mensagens[1], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }else{
                    cadastrarEncomenda(v);
                    Snackbar snackbar = Snackbar.make(v, mensagens[2], Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    //chama a funcao para notificar
                    makeNotification();
                }
            }
        });
    }

    // função para notificar entrega
    public void makeNotification(){

        //Variavel para receber a imagem da logo e colocar na notificação a direita (grande)
        Bitmap logoIcon = BitmapFactory.decodeResource(getResources(),R.drawable.hands);

        String channelID = "Aqui a notificação";
        //construtor da notificação e texto de exibição da notificação
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelID);
        builder.setSmallIcon(R.drawable.ic_notifica)
               .setContentTitle("Encomenda CHEGOU!")
               .setContentText("Retirada liberada")
               .setStyle(new NotificationCompat.BigTextStyle()
                        .setBigContentTitle("Retire de sua nova encomenda")
                        .bigText("Para retirar sua encomenda, apresente-se na portaria do Condominio Parque Serafim das 08:00 as 12:00 e das 13:00 as 21:00 de segunda-feira a sábado, não haverá retirada aos Domingos e feriados."))
               .setLargeIcon(logoIcon)
               .setAutoCancel(true)
               .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        //intent para quando clicar na notificação encaminhar para a MainActivity e exibe um texto se quiser
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("data","");

        //Montando a chamada da notificação
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //Verificando a versão do android e exibindo erro se for abaixo do android oreo
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelID);

            if(notificationChannel == null){
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(channelID, "descrição", importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        //Executando o construtor e notificando
        notificationManager.notify(0,builder.build());

        //Toast.makeText(this,"Notificação Enviada...",Toast.LENGTH_SHORT).show();
    }





    //Criando método para cadastrar encomenda recebida no banco de dados
    private void cadastrarEncomenda(View v){
        //criando variável que recebe o conteúdo digitado no campo nome
        String data = dataRecebimento.getText().toString();
        String apartamento = aptoRecebimento.getText().toString();
        String Cep = cepRecebimento.getText().toString();
        String tipo = radioGroup;
        String id = UUID.randomUUID().toString();
        String retirado = "Não";

        //Inicia Banco de Dados no Firebase com a Instancia do Banco de Dados db
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Cria tabela Map de ENcomendas com a string chave e o objeto
        Map<String,Object> encomendas = new HashMap<>();
        encomendas.put("id",id);
        encomendas.put("data",data);
        encomendas.put("apto",apartamento);
        encomendas.put("Cep",Cep);
        encomendas.put("tipo",tipo);
        encomendas.put("retirado",retirado);


        //Criando documento de referencia que vai receber o banco de dados e cria uma coleção
        DocumentReference documentoReferencia = db.collection("Encomendas").document(id);
        //cada usuário terá umdocumento referencia com seu usuário ID

        //setando objeto map usuário no documento e aplicando interface onSucessListener
        documentoReferencia.set(encomendas).addOnSuccessListener(new OnSuccessListener<Void>() {
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

        dataRecebimento = findViewById(R.id.editTextNome_Recebimento);
        aptoRecebimento = findViewById(R.id.editTextApto_Recebimento);
        cepRecebimento = findViewById(R.id.editTextCep_Recebimento);
        radioCarta = findViewById(R.id.radioButtonCarta_Recebimento);
        radioPacote = findViewById(R.id.radioButtonPacote_Recebimento);
        cadastrarRecebimento = findViewById(R.id.buttonCadastrar_Recebimento);

    }
    public void sair(View v) {
        //Criação de intent para chamada de outra tela
        Intent iSair = new Intent(this, AreaRestritaActivity.class);
        //Envio de solicitação
        startActivity(iSair);

    }
}