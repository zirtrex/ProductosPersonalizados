package net.zirtrex.productospersonalizados.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.zirtrex.productospersonalizados.Activities.ClienteActivity;
import net.zirtrex.productospersonalizados.Activities.ProveedorActivity;
import net.zirtrex.productospersonalizados.Activities.R;
import net.zirtrex.productospersonalizados.Models.Usuarios;

import java.util.ArrayList;
import java.util.List;


public class LoginFragment extends Fragment {

    public static final String TAG ="LoginFragment";

    FirebaseAuth.AuthStateListener mAuthListener;

    ArrayAdapter spnrRolAdapter;
    public static List<String> lRol = new ArrayList<>();

    // UI references.
    Spinner spnrRol;
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private View view;

    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_login, container, false);

        getActivity().setTitle(getText(R.string.title_fragment_login));

        spnrRol = (Spinner) view.findViewById(R.id.spnrRol);

        spnrRolAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, lRol);
        spnrRolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrRol.setAdapter(spnrRolAdapter);

        populateLRol();

        mEmailView = (EditText) view.findViewById(R.id.email);

        mPasswordView = (EditText) view.findViewById(R.id.password);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                    attemptLoginOrRegister("login");
                    return true;
                }
                return false;
            }
        });

        Button btnLogin = (Button) view.findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mEmailView.onEditorAction(EditorInfo.IME_ACTION_DONE);
                mPasswordView.onEditorAction(EditorInfo.IME_ACTION_DONE);
                attemptLoginOrRegister("login");
            }
        });

        Button btnRegister = (Button) view.findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mEmailView.onEditorAction(EditorInfo.IME_ACTION_DONE);
                mPasswordView.onEditorAction(EditorInfo.IME_ACTION_DONE);
                attemptLoginOrRegister("register");
            }
        });

        mLoginFormView = view.findViewById(R.id.login_form);
        mProgressView = view.findViewById(R.id.login_progress);

        getFirebaseAuthSession();

        return view;
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
                                if(usuario.getRol() == "cliente"){
                                    Log.w(TAG , "Cliente Logueado");
                                }else if(usuario.getRol() == "proveedor"){
                                    Log.w(TAG , "Proveedor Logueado");
                                    //NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_proveedor);
                                    //navController.navigate(R.id.proveedorPrincipalFragment);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    //getActivity().getSupportFragmentManager().popBackStack();

                }else {
                    Log.w(TAG , "Sin usuario activo");
                }
            }
        };
    }

    private void populateLRol(){
        lRol.clear();
        lRol.add("proveedor");
        lRol.add("cliente");
        spnrRolAdapter.notifyDataSetChanged();
    }

    private void attemptLoginOrRegister(String option) {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String rol = spnrRol.getSelectedItem().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }else if (!isPasswordValid(password)){
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //focusView = null;
            showProgress(true);
            if (option == "login") {
                doLogin(email, password);
            }else{
                createAccount(email, password, rol);
            }
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private void createAccount(final String email, String password, final String rol) {
        Log.d(TAG, "Creando cuenta: " + email);

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "cuenta creada con email: success");
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String user_id = user.getUid();
                    DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("users").child(user_id);
                    current_user_db.child("email").setValue(email);
                    current_user_db.child("rol").setValue(rol);
                    sendEmailVerification();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "cuenta creada con email: failure", task.getException());
                    String msg = "Registro fallido [" + task.getException().getMessage() + "]";
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                }
                showProgress(false);
                }
            });
    }

    protected void doLogin(String email, String password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.w(TAG, "Usuario Válido");
                    Activity activity = getActivity();

                    if(activity instanceof ProveedorActivity){
                        Log.w(TAG, "Es proveedor activity");
                        //NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_proveedor);
                        //navController.navigate(R.id.proveedorPrincipalFragment);
                    }else if(activity instanceof ClienteActivity){
                        //NavController navController = Navigation.findNavController(getActivity(), R.id.content_cliente);
                        //navController.navigate(R.id.);
                    }

                }else{
                    Log.w(TAG, "Error al intentar Ingresar", task.getException());
                    String msg = "Usuario y/o Clave son incorrectos [" + task.getException().getMessage() + "]";
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                }
                showProgress(false);
                }
            });
    }

    private void sendEmailVerification() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification()
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(),"Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "sendEmailVerification", task.getException());
                    Toast.makeText(getActivity(), "Ha fallado la verificación del correo", Toast.LENGTH_SHORT).show();
                }
                }
            });
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAuthListener != null){
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }

}

