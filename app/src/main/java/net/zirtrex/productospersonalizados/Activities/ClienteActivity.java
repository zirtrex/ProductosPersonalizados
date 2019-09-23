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
import net.zirtrex.productospersonalizados.Fragments.ClienteProductsFragment;
import net.zirtrex.productospersonalizados.Fragments.LoginFragment;
import net.zirtrex.productospersonalizados.Interfaces.OnFragmentInteractionListener;
import net.zirtrex.productospersonalizados.Models.Cart;
import net.zirtrex.productospersonalizados.Util.Utils;

import java.util.LinkedList;
import java.util.List;

public class ClienteActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnFragmentInteractionListener {

    public static final String TAG ="ClienteActivity";

    FirebaseAuth.AuthStateListener mAuthListener;

    TextView tvUserEmail;
    Button btnLogin, btnLogout;

    int mNotificationsCount = 0;
    Double montoTotal = 0.00;

    List<Cart> lCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);

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
                    .replace(R.id.content_cliente, new LoginFragment(), LoginFragment.TAG)
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

            Fragment productsFragment = new ClienteProductsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_cliente, productsFragment, ClienteProductsFragment.TAG)
                    .addToBackStack(null)
                    .commit();

            Utils.validateScreen = false;
        }

        lCart = new LinkedList<Cart>();

        getCart();

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

                Fragment fCart = new CartFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_cliente, fCart,"Fragment Cart")
                        .addToBackStack(null)
                        .commit();

                return true;

            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        Fragment miFragment = null;
        String tag = "";
        boolean fragmentSeleccionado = false;

        if (id == R.id.nav_productos) {

            miFragment = new ClienteProductsFragment();
            tag = ClienteProductsFragment.TAG;
            fragmentSeleccionado = true;

        /*}else if (id == R.id.nav_financiamientos) {

            miFragment = new ProveedorPedidosFragment();
            fragmentSeleccionado = true;

        }else if (id == R.id.nav_formas_de_pago) {

            miFragment = new FormasPagoFragment();
            fragmentSeleccionado = true;

        }else if (id == R.id.nav_premios) {

            miFragment = new FormasPagoFragment();
            fragmentSeleccionado = true;

        }else if (id == R.id.nav_repuestos) {

            miFragment = new FormasPagoFragment();
            fragmentSeleccionado = true;

        }else if (id == R.id.nav_comisiones) {

            miFragment = new FormasPagoFragment();
            fragmentSeleccionado = true;*/

        }

        if (fragmentSeleccionado){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_cliente, miFragment, tag)
                    .addToBackStack(null)
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getFirebaseAuthSession(){
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    String user_id = user.getUid();
                    DatabaseReference current_user_db_proveedor = FirebaseDatabase.getInstance().getReference().child("users").child("proveedor").child(user_id);
                    DatabaseReference current_user_db_cliente = FirebaseDatabase.getInstance().getReference().child("users").child("cliente").child(user_id);

                    if(current_user_db_cliente != null){
                        Log.w(TAG , "Usuario Logueado");
                        tvUserEmail.setText(user.getEmail());
                        tvUserEmail.setVisibility(View.VISIBLE);
                        btnLogin.setVisibility(View.GONE);
                        btnLogout.setVisibility(View.VISIBLE);
                    }else if(current_user_db_proveedor != null){
                        Log.w(TAG , "Proveedor");
                        Intent intent = new Intent(getApplicationContext(), ProveedorActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
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
}
