package net.zirtrex.productospersonalizados.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.zirtrex.productospersonalizados.Fragments.FinanciamientoFragment;
import net.zirtrex.productospersonalizados.Fragments.FormasPagoFragment;
import net.zirtrex.productospersonalizados.Fragments.LoginFragment;
import net.zirtrex.productospersonalizados.Fragments.ProductsFragment;
import net.zirtrex.productospersonalizados.Fragments.ProveedorProductsFragment;
import net.zirtrex.productospersonalizados.Interfaces.OnFragmentInteractionListener;
import net.zirtrex.productospersonalizados.Interfaces.OnProveedorFragmentInteractionListener;
import net.zirtrex.productospersonalizados.Models.Usuarios;
import net.zirtrex.productospersonalizados.Util.Utils;

public class ProveedorActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnProveedorFragmentInteractionListener {

    public static final String TAG ="ProveedorActivity";

    FirebaseAuth.AuthStateListener mAuthListener;

    TextView tvUserEmail;
    Button btnLogin, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proveedor);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        View header = navigationView.getHeaderView(0);

        tvUserEmail = (TextView) header.findViewById(R.id.tvUserEmail);
        btnLogin = (Button) header.findViewById(R.id.btnLogin);
        btnLogout = (Button) header.findViewById(R.id.btnLogout);

        getFirebaseAuthSession();

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_proveedor, new LoginFragment(), LoginFragment.TAG)
                        .commit();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                btnLogin.setVisibility(View.VISIBLE);
                btnLogout.setVisibility(View.GONE);
                tvUserEmail.setVisibility(View.GONE);
            }
        });

        if(Utils.validateScreen){
            Fragment proveedorProductsFragment = new ProveedorProductsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_proveedor, proveedorProductsFragment, ProductsFragment.TAG)
                    .addToBackStack(null)
                    .commit();

            Utils.validateScreen = false;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        Fragment miFragment = null;
        boolean fragmentSeleccionado = false;

        if (id == R.id.nav_productos) {

            miFragment = new ProductsFragment();
            fragmentSeleccionado = true;

        }else if (id == R.id.nav_financiamientos) {

            miFragment = new FinanciamientoFragment();
            fragmentSeleccionado = true;

        }else if (id == R.id.nav_formas_de_pago) {

            miFragment = new FormasPagoFragment();
            fragmentSeleccionado = true;

        }else{
            miFragment = new LoginFragment();
            fragmentSeleccionado = true;
        }

        if (fragmentSeleccionado){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_proveedor, miFragment, miFragment.getTag())
                    .addToBackStack(null)
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                            Log.w(TAG , usuario.toString());
                            if(usuario != null){
                                if(usuario.getRol().equals("cliente")){
                                    Log.w(TAG , "Cliente Logueado");
                                }else if(usuario.getRol().equals("proveedor")){
                                    Log.w(TAG , "Proveedor Logueado");
                                    tvUserEmail.setText(usuario.getEmail());
                                    tvUserEmail.setVisibility(View.VISIBLE);
                                    btnLogin.setVisibility(View.GONE);
                                    btnLogout.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }else {
                    Log.w(TAG , "Sin usuario activo");
                    btnLogin.setVisibility(View.VISIBLE);
                }
            }
        };
    }
}
