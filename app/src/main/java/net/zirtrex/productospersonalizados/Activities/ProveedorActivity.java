package net.zirtrex.productospersonalizados.Activities;

import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.Menu;
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

import net.zirtrex.productospersonalizados.Fragments.FormasPagoFragment;
import net.zirtrex.productospersonalizados.Fragments.LoginFragment;
import net.zirtrex.productospersonalizados.Fragments.ProveedorPrincipalFragment;
import net.zirtrex.productospersonalizados.Fragments.ProveedorProductsFragment;
import net.zirtrex.productospersonalizados.Interfaces.OnProveedorFragmentInteractionListener;
import net.zirtrex.productospersonalizados.Models.Usuarios;

public class ProveedorActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnProveedorFragmentInteractionListener {

    public static final String TAG ="ProveedorActivity";

    FirebaseAuth.AuthStateListener mAuthListener;

    Toolbar toolbar;
    public DrawerLayout drawerLayout;
    NavigationView navigationView;
    private AppBarConfiguration mAppBarConfiguration;
    public NavController navController;

    Fragment proveedorPrincipalFragment;

    TextView tvUserEmail;
    Button btnLogin, btnLogout;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proveedor);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout_proveedor);
        navigationView =  findViewById(R.id.nav_view_proveedor);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_proveedor_inicio,
                R.id.nav_proveedor_productos,
                R.id.nav_proveedor_pedidos)
                .setDrawerLayout(drawerLayout)
                .build();


        //navigationView.setNavigationItemSelectedListener(this);
        //navigationView.setItemIconTintList(null);
        //navigationView.setCheckedItem(R.id.nav_proveedor_inicio);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_proveedor);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View header = navigationView.getHeaderView(0);

        tvUserEmail = (TextView) header.findViewById(R.id.tvUserEmail);
        btnLogin = (Button) header.findViewById(R.id.btnLogin);
        btnLogout = (Button) header.findViewById(R.id.btnLogout);

        getFirebaseAuthSession();

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_content_proveedor, new LoginFragment(), LoginFragment.TAG)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(null)
                        .commit();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                btnLogin.setVisibility(View.VISIBLE);
                btnLogout.setVisibility(View.GONE);
                tvUserEmail.setVisibility(View.GONE);
            }
        });

        if (savedInstanceState == null) {
            //initScreen();
        } else {
            //proveedorPrincipalFragment = (ProveedorPrincipalFragment) getSupportFragmentManager().findFragmentByTag(ProveedorPrincipalFragment.TAG);
        }

        /*this.getSupportFragmentManager().addOnBackStackChangedListener(
            new FragmentManager.OnBackStackChangedListener() {
                public void onBackStackChanged() {
                    Fragment current = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_proveedor);;
                    if (current instanceof ProveedorPrincipalFragment) {
                        navigationView.setCheckedItem(R.id.nav_proveedor_inicio);
                    }if (current instanceof ProveedorProductsFragment) {
                        navigationView.setCheckedItem(R.id.nav_proveedor_productos);
                    }if (current instanceof ProveedorProductsFragment) {
                        navigationView.setCheckedItem(R.id.nav_proveedor_productos);
                    } else {
                        navigationView.setCheckedItem(R.id.nav_proveedor_pedidos);
                    }
                }
            });*/
    }

    private void initScreen() {
        proveedorPrincipalFragment = new ProveedorPrincipalFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment_content_proveedor, proveedorPrincipalFragment, ProveedorPrincipalFragment.TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        drawerLayout.closeDrawer(GravityCompat.START);
        int id = item.getItemId();

        Fragment miFragment = null;
        String tag = "";
        boolean fragmentSeleccionado = false;

        if (id == R.id.nav_proveedor_inicio) {
            //navController.navigate(R.id.proveedorPrincipalFragment);
            /*miFragment = new ProveedorPrincipalFragment();
            tag = ProveedorPrincipalFragment.TAG;
            fragmentSeleccionado = true;*/

        }else if (id == R.id.nav_proveedor_productos) {

            /*miFragment = new ProveedorProductsFragment();
            tag = ProveedorProductsFragment.TAG;
            fragmentSeleccionado = true;*/

        }else if (id == R.id.nav_proveedor_pedidos) {

            /*miFragment = new FormasPagoFragment();
            tag = "Hola";
            fragmentSeleccionado = true;*/

        }else{
            /*miFragment = new ProveedorPrincipalFragment();
            tag = ProveedorPrincipalFragment.TAG;
            fragmentSeleccionado = true;*/
        }

        if (fragmentSeleccionado){

            /*Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(tag);

            if(currentFragment == null){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_content_proveedor, miFragment, tag)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(null)
                        .commit();
            }*/
        }


        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_proveedor);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
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

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null){
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }

}
