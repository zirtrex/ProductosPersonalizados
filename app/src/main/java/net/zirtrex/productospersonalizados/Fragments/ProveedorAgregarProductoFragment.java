package net.zirtrex.productospersonalizados.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import net.zirtrex.productospersonalizados.Activities.R;
import net.zirtrex.productospersonalizados.Adapters.MateriaPrimaSpinnerAdapter;
import net.zirtrex.productospersonalizados.Adapters.MaterialIndirectoSpinnerAdapter;
import net.zirtrex.productospersonalizados.Adapters.ProveedorProductoMateriaPrimaRecyclerAdapter;
import net.zirtrex.productospersonalizados.Adapters.ProveedorProductoMaterialesIndirectosRecyclerAdapter;
import net.zirtrex.productospersonalizados.Interfaces.OnProveedorFragmentInteractionListener;
import net.zirtrex.productospersonalizados.Models.MateriaPrimaAutocomplete;
import net.zirtrex.productospersonalizados.Models.MateriaPrimaPojo;
import net.zirtrex.productospersonalizados.Models.MaterialIndirectoAutocomplete;
import net.zirtrex.productospersonalizados.Models.MaterialesIndirectosPojo;
import net.zirtrex.productospersonalizados.Models.Producto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class ProveedorAgregarProductoFragment extends Fragment {

    public static final String TAG = "AgregarProductoFragment";

    private OnProveedorFragmentInteractionListener mListener;

    private FirebaseAuth mAuth;
    private DatabaseReference productosDatabase;
    private String proveedorID = ""; //ID del fabricante

    ArrayList<MateriaPrimaPojo> lProductoMateriaPrima = new ArrayList<>();
    ArrayList<MaterialesIndirectosPojo> lProductoMaterialesIndirectos = new ArrayList<>();

    List<MateriaPrimaAutocomplete> lMateriaPrimaAutocomplete = new ArrayList<>();
    List<MaterialIndirectoAutocomplete> lMaterialIndirectoAutocomplete = new ArrayList<>();

    Map<String, HashMap<Integer, Double>> hmTarjetas = new HashMap<String, HashMap<Integer, Double>>();

    RadioGroup rgTipoPrenda;
    RadioButton radioButton;

    RecyclerView rvProductoMateriaPrima, rvProductoMaterialesIndirectos;
    ProveedorProductoMateriaPrimaRecyclerAdapter ppmpRecyclerAdapter;
    ProveedorProductoMaterialesIndirectosRecyclerAdapter ppmiRecyclerAdapter;

    //Spinners Adapters
    MateriaPrimaSpinnerAdapter materiaPrimaSA;
    MaterialIndirectoSpinnerAdapter materialIndirectoSA;

    Spinner spnrNombreMateriaPrima, spnrMaterialesIndirectos;
    Button btnAgregarMateriaPrima, btnAgregarMaterialesIndirectos, btnGuardarProducto;
    EditText txtGastosFinancieros, txtImgUrl, txtNombreProducto, txtValorMateriaPrima, txtValorMaterialIndirecto;
    TextView tvSeleccionTipoPrenda;
    ImageView ivPrenda;

    private Uri resultUri;

    View view;

    public ProveedorAgregarProductoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnProveedorFragmentInteractionListener) {
            mListener = (OnProveedorFragmentInteractionListener) context;
            //this.montoTotal = mListener.getMonto();
        } else {
            throw new RuntimeException(context.toString()
                    + " debe Implementarse OnProveedorFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.proveedor_fragment_agregar_producto, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
            proveedorID = user.getUid();

        //Radio Group para tipo de prenda
        rgTipoPrenda = (RadioGroup) view.findViewById(R.id.rgTipoPrenda);
        tvSeleccionTipoPrenda = (TextView) view.findViewById(R.id.tvSeleccionTipoPrenda);
        rgTipoPrenda.setOnCheckedChangeListener(elegirTipoPrenda);

        //Spinner para Materia Prima
        populateLMateriaPrimaAutocomplete();
        spnrNombreMateriaPrima = (Spinner) view.findViewById(R.id.spnrNombreMateriaPrima);
        materiaPrimaSA = new MateriaPrimaSpinnerAdapter(getActivity(), R.layout.proveedor_spinner_fila, R.id.tvSpinnerFila, lMateriaPrimaAutocomplete);
        //mpsa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrNombreMateriaPrima.setAdapter(materiaPrimaSA);

        //RecyclerView Materia Prima
        rvProductoMateriaPrima = (RecyclerView) view.findViewById(R.id.rvProductoMateriaPrima);
        ppmpRecyclerAdapter = new ProveedorProductoMateriaPrimaRecyclerAdapter(getContext(), lProductoMateriaPrima, mListener);
        rvProductoMateriaPrima.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvProductoMateriaPrima.setAdapter(ppmpRecyclerAdapter);

        //Evento de deslizar para Materia Prima Recicler View
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rvProductoMateriaPrima);

        //Spinner para Material Indirecto
        populateLMaterialesIndirectosAutocomplete();
        spnrMaterialesIndirectos = (Spinner) view.findViewById(R.id.spnrMaterialesIndirectos);
        materialIndirectoSA = new MaterialIndirectoSpinnerAdapter(getActivity(), R.layout.proveedor_spinner_fila, R.id.tvSpinnerFila, lMaterialIndirectoAutocomplete);
        //materialIndirectoSA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrMaterialesIndirectos.setAdapter(materialIndirectoSA);

        //RecyclerView Materia Prima
        rvProductoMaterialesIndirectos = (RecyclerView) view.findViewById(R.id.rvProductoMaterialesIndirectos);
        ppmiRecyclerAdapter = new ProveedorProductoMaterialesIndirectosRecyclerAdapter(getContext(), lProductoMaterialesIndirectos, mListener);
        rvProductoMaterialesIndirectos.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvProductoMaterialesIndirectos.setAdapter(ppmiRecyclerAdapter);

        //Evento de deslizar para Materia Prima Recicler View
        ItemTouchHelper itemTouchHelper4MI = new ItemTouchHelper(simpleItemTouchCallback4FI);
        itemTouchHelper4MI.attachToRecyclerView(rvProductoMaterialesIndirectos);


        //Botones
        btnAgregarMateriaPrima = (Button) view.findViewById(R.id.btnAgregarMateriaPrima);
        btnAgregarMaterialesIndirectos = (Button) view.findViewById(R.id.btnAgregarMaterialesIndirectos);
        btnGuardarProducto = (Button) view.findViewById(R.id.btnGuardarProducto);

        //Cajas de texto
        txtGastosFinancieros = (EditText) view.findViewById(R.id.txtGastosFinancieros);
        txtImgUrl = (EditText) view.findViewById(R.id.txtImgUrl);
        txtNombreProducto = (EditText) view.findViewById(R.id.txtNombreProducto);
        txtValorMateriaPrima = (EditText) view.findViewById(R.id.txtValorMateriaPrima);
        txtValorMaterialIndirecto = (EditText) view.findViewById(R.id.txtValorMaterialIndirecto);

        //Imágenes
        ivPrenda = (ImageView) view.findViewById(R.id.ivPrenda);

        ivPrenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        //Eventos a botones
        btnAgregarMateriaPrima.setOnClickListener(agregarMateriaPrima);
        btnAgregarMaterialesIndirectos.setOnClickListener(agregarMaterialIndirecto);
        btnGuardarProducto.setOnClickListener(confirmarAgregarProducto);

        /*spnrNombreMateriaPrima.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                MateriaPrimaAutocomplete mpSeleccionada = mpsa.getItem(position);

                Toast.makeText(getActivity(), "ID: " + mpSeleccionada.getNombreMateriaPrima() + "\nName: " + mpSeleccionada.getDescripcionMateriaPrima(),
                        Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });*/

        return view;
    }


    private void populateLMateriaPrimaAutocomplete() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef  = database.getReference();

        final Query materiaPrima;

        materiaPrima = myRef.child("materiaPrima");

        materiaPrima.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lMateriaPrimaAutocomplete.removeAll(lMateriaPrimaAutocomplete);

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    MateriaPrimaAutocomplete mP = new MateriaPrimaAutocomplete();
                    mP.setNombreMateriaPrima(postSnapshot.getKey());
                    mP.setDescripcionMateriaPrima(postSnapshot.getValue(String.class));
                    lMateriaPrimaAutocomplete.add(mP);
                }

                if(materiaPrimaSA != null)
                    materiaPrimaSA.notifyDataSetChanged(); //Esta línea me hizo perder 2 días.
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //count = 0;
            }
        });
    }

    private void populateLMaterialesIndirectosAutocomplete() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef  = database.getReference();

        final Query materialesIndirectos;

        materialesIndirectos = myRef.child("materialesIndirectos");

        materialesIndirectos.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lMaterialIndirectoAutocomplete.removeAll(lMaterialIndirectoAutocomplete);

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    MaterialIndirectoAutocomplete mP = new MaterialIndirectoAutocomplete();
                    mP.setNombreMaterialIndirecto(postSnapshot.getKey());
                    mP.setDescripcionMaterialIndirecto(postSnapshot.getValue(String.class));
                    lMaterialIndirectoAutocomplete.add(mP);
                }

                if(materialIndirectoSA != null)
                    materialIndirectoSA.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //count = 0;
            }
        });
    }

    /*private void  seleccionarSpinner(MateriaPrimaPojo objetoSeleccionado){

        double valorEntrante =  objetoSeleccionado.getValorMateriaPrima();
        int posicion = 0;

        for (int i = 0; i < mp.size(); i++){
            if(valorEntrante == mp.get(i).getValorMateriaPrima()){
                posicion = i;
                break;
            }
        }

        spnrTarjetas.setSelection(posicion);
    }*/

    RadioGroup.OnCheckedChangeListener elegirTipoPrenda = new RadioGroup.OnCheckedChangeListener(){
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch(checkedId) {
                case R.id.rbPolos:
                    tvSeleccionTipoPrenda.setText("Has elegido: Polos" );
                    break;
                case R.id.rbPantalones:
                    tvSeleccionTipoPrenda.setText("Has elegido: Pantalones" );
                    break;
                case R.id.rbZapatos:
                    tvSeleccionTipoPrenda.setText("Has elegido: Zapatos" );
                    break;
            }
        }
    };

    View.OnClickListener agregarMateriaPrima = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MateriaPrimaPojo materiaPrimaItem = new MateriaPrimaPojo();

            String nombreMP = "";

            if(spnrNombreMateriaPrima.getSelectedItem() != null)
                nombreMP = ((MateriaPrimaAutocomplete) spnrNombreMateriaPrima.getSelectedItem()).getNombreMateriaPrima();

            Double valorMP = 0.0;
            if(txtValorMateriaPrima.getText().toString() == ""){
                valorMP = 0.0;
            }else{
                valorMP = Double.parseDouble(txtValorMateriaPrima.getText().toString());
            }

            materiaPrimaItem.setNombreMateriaPrima(nombreMP);
            materiaPrimaItem.setValorMateriaPrima(valorMP);
            lProductoMateriaPrima.add(materiaPrimaItem);
            ppmpRecyclerAdapter.notifyDataSetChanged();

            if(lProductoMateriaPrima.size() > 0){
                rvProductoMateriaPrima.setVisibility(View.VISIBLE);
            }

            txtValorMateriaPrima.setText("");

            Toast.makeText(getActivity(),"Materia Prima agregada correctamente", Toast.LENGTH_LONG).show();

        }
    };

    View.OnClickListener agregarMaterialIndirecto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MaterialesIndirectosPojo materialIndirectoItem = new MaterialesIndirectosPojo();

            String nombreMI = "";

            if(spnrMaterialesIndirectos.getSelectedItem() != null)
                nombreMI = ((MaterialIndirectoAutocomplete) spnrMaterialesIndirectos.getSelectedItem()).getNombreMaterialIndirecto();

            Double valorMI = 0.0;
            if(txtValorMaterialIndirecto.getText().toString() == ""){
                valorMI = 0.0;
            }else{
                valorMI = Double.parseDouble(txtValorMaterialIndirecto.getText().toString());
            }

            materialIndirectoItem.setNombreMaterialIndirecto(nombreMI);
            materialIndirectoItem.setValorMaterialIndirecto(valorMI);
            lProductoMaterialesIndirectos.add(materialIndirectoItem);
            ppmiRecyclerAdapter.notifyDataSetChanged();

            if(lProductoMaterialesIndirectos.size() > 0){
                rvProductoMaterialesIndirectos.setVisibility(View.VISIBLE);
            }

            txtValorMaterialIndirecto.setText("");

            Toast.makeText(getActivity(),"Material Inderecto agregadao correctamente", Toast.LENGTH_LONG).show();
        }
    };

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

            int id = (int) viewHolder.getAdapterPosition();
            lProductoMateriaPrima.remove(id);
            ppmpRecyclerAdapter.notifyDataSetChanged();

            Toast.makeText(viewHolder.itemView.getContext(), "Materia Prima eliminada correctamente", Toast.LENGTH_LONG).show();

        }
    };

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback4FI = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

            int id = (int) viewHolder.getAdapterPosition();
            lProductoMaterialesIndirectos.remove(id);
            ppmiRecyclerAdapter.notifyDataSetChanged();

            Toast.makeText(viewHolder.itemView.getContext(), "Material Indirecto eliminado correctamente", Toast.LENGTH_LONG).show();

        }
    };

    View.OnClickListener confirmarAgregarProducto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            builder.setMessage("¿Desea agregar el producto?");

            builder.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    guardarProducto();
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    };


    private void guardarProducto() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        productosDatabase  = database.getReference("productos");

        Producto producto = new Producto();

        String idProducto = productosDatabase.push().getKey();

        producto.setIdProducto(idProducto);
        producto.setTipo(tvSeleccionTipoPrenda.getText().toString());
        producto.setNombreProducto(txtNombreProducto.getText().toString());
        producto.setImgUrl(guardarImagenPrenda(idProducto));
        producto.setGastosFinancieros(Double.parseDouble(txtGastosFinancieros.getText().toString()));

        Map<String, Double> materiasPrima = new HashMap<>();

        for (int i = 0; i < lProductoMateriaPrima.size(); i++){
            MateriaPrimaPojo materiaPrimaActual = lProductoMateriaPrima.get(i);
            materiasPrima.put(materiaPrimaActual.getNombreMateriaPrima(), materiaPrimaActual.getValorMateriaPrima());
        }

        producto.setMateriaPrima(materiasPrima);

        Map<String, Double> materialesIndirectos = new HashMap<>();

        for (int i = 0; i < lProductoMaterialesIndirectos.size(); i++){
            MaterialesIndirectosPojo materialIndirectoActual = lProductoMaterialesIndirectos.get(i);
            materialesIndirectos.put(materialIndirectoActual.getNombreMaterialIndirecto(), materialIndirectoActual.getValorMaterialIndirecto());
        }

        producto.setMaterialesIndirectos(materialesIndirectos);

        if(proveedorID != null){
            producto.setIdUsuario(proveedorID);
            productosDatabase.child(idProducto).setValue(producto);
            Log.w(TAG , "Usuario Logueado");
            Toast.makeText(getContext(), "Prenda agregada correctamente",
                    Toast.LENGTH_LONG).show();

            Navigation.findNavController(view).navigate(R.id.action_dp_to_p);


        }else {
            Log.w(TAG , "Sin usuario activo");
            Toast.makeText(getContext(), "Necesitas iniciar sesión para guardar los datos.",
                    Toast.LENGTH_LONG).show();
        }
    }

    static String prendaImageUrl = "";
    private String guardarImagenPrenda(final String idProducto){

        if(resultUri != null) {
            StorageReference filePath = FirebaseStorage.getInstance().getReference().child("prendas_images").child(proveedorID);
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = filePath.putBytes(data);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    prendaImageUrl = downloadUrl.toString();
                    txtImgUrl.setText(prendaImageUrl);
                    Map newImage = new HashMap();
                    newImage.put("imgUrl", downloadUrl.toString());
                    productosDatabase.child(idProducto).updateChildren(newImage);

                    Toast.makeText(getContext(), "La imagen se ha guardado correctamente", Toast.LENGTH_LONG).show();
                }
            });

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "La imagen no ha podido guardarse", Toast.LENGTH_LONG).show();
                }
            });
        }else {
            Toast.makeText(getContext(), "La imagen no ha sido guardado correctamente", Toast.LENGTH_LONG).show();
        }

        return prendaImageUrl;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            ivPrenda.setImageURI(resultUri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
