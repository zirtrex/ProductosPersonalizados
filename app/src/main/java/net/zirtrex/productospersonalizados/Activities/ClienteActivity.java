package net.zirtrex.productospersonalizados.Activities;

import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import net.zirtrex.productospersonalizados.Fragments.CartFragment;
import net.zirtrex.productospersonalizados.Fragments.ClienteLoginFragment;
import net.zirtrex.productospersonalizados.Fragments.ClienteProductosFragment;
import net.zirtrex.productospersonalizados.Interfaces.OnFragmentInteractionListener;
import net.zirtrex.productospersonalizados.Models.Cart;
import net.zirtrex.productospersonalizados.Models.Usuarios;
import net.zirtrex.productospersonalizados.Util.Utils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ClienteActivity extends AppCompatActivity
                                implements  OnFragmentInteractionListener {

    public static final String TAG ="ClienteActivity";

    FirebaseAuth.AuthStateListener mAuthListener;

    Toolbar toolbar;
    public DrawerLayout drawerLayout;
    NavigationView navigationView;
    private AppBarConfiguration mAppBarConfiguration;
    public NavController navController;

    List<Cart> lCart = new LinkedList<>();

    TextView tvUserEmail;
    Button btnLogin, btnLogout;

    int mNotificationsCount = 0;
    Double montoTotal = 0.00;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);

        setupNavigation();

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
                navController.navigate(R.id.nav_cliente_login);
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
                navController.navigate(R.id.nav_cliente_login);
            }
        });

        getCart();

    }

    private void setupNavigation() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_cliente);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout_cliente);
        navigationView =  findViewById(R.id.nav_view_cliente);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_cliente);

        Set<Integer> topLevelDestinations = new HashSet<>();
        topLevelDestinations.add(R.id.nav_cliente_login);
        topLevelDestinations.add(R.id.nav_cliente_productos);

        mAppBarConfiguration = new AppBarConfiguration.Builder(topLevelDestinations)
                                                                .setDrawerLayout(drawerLayout)
                                                                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setItemIconTintList(null);
        //navigationView.setCheckedItem(R.id.nav_proveedor_inicio);
    }




    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_cliente);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        // Get the notifications MenuItem and
        // its LayerDrawable (layer-list)
        MenuItem item = menu.findItem(R.id.action_notifications);
        LayerDrawable icon = (LayerDrawable) item.getIcon();

        // Update LayerDrawable's BadgeDrawable
        Utils.setBadgeCount(this, icon, mNotificationsCount);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_notifications:
                //TODO Navigation

                return true;

            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
                                    //Log.w(TAG , "Cliente Logueado");
                                    tvUserEmail.setText(usuario.getEmail());
                                    tvUserEmail.setVisibility(View.VISIBLE);
                                    btnLogin.setVisibility(View.GONE);
                                    btnLogout.setVisibility(View.VISIBLE);

                                }else if(usuario.getRol().equals("proveedor")){
                                    //Log.w(TAG , "Proveedor Logueado");
                                    Intent intent = new Intent(getApplicationContext(), ProveedorActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) { }
                    });

                }else {
                    Log.w(TAG , "Sin usuario activo");
                    btnLogin.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    private void getCart() {

        final int[] count = new int[1];

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef  = database.getReference();

        Query carts;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            carts = myRef.child("cart").child(user.getUid());
        }else{
            carts = myRef.child("cart");
        }

        carts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                count[0] = (int) dataSnapshot.getChildrenCount();
                updateNotificationsBadge(count[0]);

                lCart.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren() ){
                    Cart cart = snapshot.getValue(Cart.class);
                    lCart.add(cart);
                }
                calculateTotal(lCart);
                //Log.w("Datos:", Long.toString(dataSnapshot.getChildrenCount()));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //count = 0;
            }
        });
    }

    public double calculateTotal(List<Cart> lcart){

        montoTotal = 0.00;

        for (int i = 0; i < lcart.size(); i++)
        {
            Double price = lcart.get(i).getCartPrecioTotal();

            montoTotal += price;

        }

        return montoTotal;
    }

    @Override
    public void saveMonto(Double montoTotal) {
        this.montoTotal = montoTotal;
        Toast.makeText(getApplicationContext(), "Se ha pulsado saveMonto " + String.valueOf(montoTotal), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateNotificationsBadge(Integer count) {
        mNotificationsCount = count;

        //Forzamos la destrucción del menu
        invalidateOptionsMenu();
    }

    @Override
    public Double getMonto() {
        return this.montoTotal;
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null){
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }
}
