package net.zirtrex.productospersonalizados.Activities;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.zirtrex.productospersonalizados.Models.Usuarios;


public class MainActivity extends AppCompatActivity {

    public static final String TAG ="MainActivity";

    FirebaseAuth.AuthStateListener mAuthListener;

    Button btnClientes, btnProveedores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getFirebaseAuthSession();

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

    private void getFirebaseAuthSession(){
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    String user_id = user.getUid();
                    DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("users").child(user_id);

                    current_user_db.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Usuarios usuario = dataSnapshot.getValue(Usuarios.class);

                            if(usuario != null){
                                if(usuario.getRol().equals("cliente")){
                                    Log.w(TAG , "Cliente Logueado");
                                    /*Intent intent = new Intent(getApplicationContext(), ClienteActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);*/
                                }else if(usuario.getRol().equals("proveedor")){
                                    Log.w(TAG , "Proveedor Logueado");
                                    /*Intent intent = new Intent(getApplicationContext(), ProveedorActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);*/
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }else {
                    Log.w(TAG , "Sin usuario activo");
                }
            }
        };
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
