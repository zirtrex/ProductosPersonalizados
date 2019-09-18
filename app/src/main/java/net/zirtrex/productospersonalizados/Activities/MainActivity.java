package net.zirtrex.productospersonalizados.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.zirtrex.productospersonalizados.Fragments.LoginFragment;


public class MainActivity extends AppCompatActivity {

    public static final String TAG ="MainActivity";

    FirebaseAuth.AuthStateListener mAuthListener;

    Button btnClientes, btnProveedores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    String user_id = user.getUid();
                    DatabaseReference current_user_db_cliente = FirebaseDatabase.getInstance().getReference().child("users").child("cliente").child(user_id);
                    DatabaseReference current_user_db_proveedor = FirebaseDatabase.getInstance().getReference().child("users").child("proveedor").child(user_id);

                    if(current_user_db_cliente != null){
                        Log.w(TAG , "Cliente Logueado");
                        Intent intent = new Intent(getApplicationContext(), ClienteActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }else if(current_user_db_proveedor != null){
                        Log.w(TAG , "Proveedor Logueado");
                        Intent intent = new Intent(getApplicationContext(), ProveedorActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }else {
                    Log.w(TAG , "Sin usuario activo");
                }
            }
        };

        btnClientes = (Button) findViewById(R.id.btnClientes);
        btnProveedores = (Button) findViewById(R.id.btnProveedores);

        btnClientes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.w(TAG , "Cliente Logueado");
                Intent intent = new Intent(getApplicationContext(), ClienteActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btnProveedores.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.w(TAG , "Proveedor Logueado");
                Intent intent = new Intent(getApplicationContext(), ProveedorActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null){
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }
}
