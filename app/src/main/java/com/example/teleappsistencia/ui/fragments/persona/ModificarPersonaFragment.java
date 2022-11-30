package com.example.teleappsistencia.ui.fragments.persona;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.R;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.dialogs.AlertDialogBuilder;
import com.example.teleappsistencia.utilidades.Utilidad;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.modelos.Direccion;
import com.example.teleappsistencia.modelos.Persona;
import com.example.teleappsistencia.utilidades.dialogs.DatePickerFragment;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModificarPersonaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModificarPersonaFragment extends Fragment {

    private Persona persona;

    private Button btn_insertar;
    private Button btn_volver;
    private EditText editText_nombre;
    private EditText editText_apellidos;
    private EditText editText_dni;
    private EditText editText_fechaNacimiento;
    private EditText editText_telefonoFijo;
    private EditText editText_telefonoMovil;
    private EditText editText_localidad;
    private EditText editText_provincia;
    private EditText editText_direccion;
    private EditText editText_codigoPostal;
    private Spinner spinner_sexo;

    private TextView textView_error_nombre;
    private TextView textView_error_apellidos;
    private TextView textView_error_dni;
    private TextView textView_error_fechaNacimiento;
    private TextView textView_error_telefonoFijo;
    private TextView textView_error_telefonoMovil;
    private TextView textView_error_localidad;
    private TextView textView_error_provincia;
    private TextView textView_error_direccion;
    private TextView textView_error_codigoPostal;

    public ModificarPersonaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param persona
     * @return A new instance of fragment InsertarPersonaFragment.
     */
    public static ModificarPersonaFragment newInstance(Persona persona) {
        ModificarPersonaFragment fragment = new ModificarPersonaFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.PERSONA, persona);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            persona = (Persona) getArguments().getSerializable(Constantes.PERSONA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modificar_persona, container, false);

        this.btn_insertar = view.findViewById(R.id.btn_guardar_persona);
        this.btn_volver = view.findViewById(R.id.btn_volver_persona);
        this.editText_nombre = view.findViewById(R.id.editText_nombre_persona);
        this.editText_apellidos = view.findViewById(R.id.editText_apellidos_persona);
        this.editText_dni = view.findViewById(R.id.editText_dni_persona);
        this.editText_fechaNacimiento = view.findViewById(R.id.editText_fechaNacimiento_persona);
        this.editText_telefonoFijo = view.findViewById(R.id.editText_telefonoFijo_persona);
        this.editText_telefonoMovil = view.findViewById(R.id.editText_telefonoMovil_persona);
        this.editText_localidad = (EditText) view.findViewById(R.id.editText_localidad_direccionPersona);
        this.editText_provincia = (EditText) view.findViewById(R.id.editText_provincia_direccionPersona);
        this.editText_direccion = (EditText) view.findViewById(R.id.editText_direccion_direccionPersona);
        this.editText_codigoPostal = (EditText) view.findViewById(R.id.editText_codigoPostal_direccionPersona);
        this.spinner_sexo = view.findViewById(R.id.spinner_sexo_persona);

        this.textView_error_nombre = view.findViewById(R.id.textView_error_nombre_persona);
        this.textView_error_apellidos = view.findViewById(R.id.textView_error_apellidos_persona);
        this.textView_error_dni = view.findViewById(R.id.textView_error_dni_persona);
        this.textView_error_fechaNacimiento = view.findViewById(R.id.textView_error_fechaNacimiento_persona);
        this.textView_error_telefonoFijo = view.findViewById(R.id.textView_error_telefonoFijo_persona);
        this.textView_error_telefonoMovil = view.findViewById(R.id.textView_error_telefonoMovil_persona);
        this.textView_error_localidad = (TextView) view.findViewById(R.id.textView_error_localidad_direccionPersona);
        this.textView_error_provincia = (TextView) view.findViewById(R.id.textView_error_provincia_direccionPersona);
        this.textView_error_direccion = (TextView) view.findViewById(R.id.textView_error_direccion_direccionPersona);
        this.textView_error_codigoPostal = (TextView) view.findViewById(R.id.textView_error_codigoPostal_direccionPersona);


        ArrayAdapter<String> adapter;
        if(persona.getSexo().equalsIgnoreCase(getString(R.string.sexo_masculino))){
            adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new String[]{Constantes.SEXO_MASCULINO, Constantes.SEXO_FEMENINO});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.spinner_sexo.setAdapter(adapter);
        } else {
            adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new String[]{Constantes.SEXO_FEMENINO, Constantes.SEXO_MASCULINO});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.spinner_sexo.setAdapter(adapter);
        }

        rellenarDatos();

        this.btn_insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarPersona()) {
                    modificarPersona();
                }
            }
        });

        this.btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        this.editText_fechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDatePickerDialog();
            }
        });

        rellenarDatos();

        inicializarListeners();

        return view;
    }

    /**
     * Método encargado de rellenar los editText con los datos del modelo.
     */
    private void rellenarDatos() {
        this.editText_nombre.setText(persona.getNombre());
        this.editText_apellidos.setText(persona.getApellidos());
        this.editText_dni.setText(persona.getDni());
        this.editText_fechaNacimiento.setText(persona.getFechaNacimiento());
        this.editText_telefonoFijo.setText(persona.getTelefonoFijo());
        this.editText_telefonoMovil.setText(persona.getTelefonoMovil());

        Direccion direccion = (Direccion) Utilidad.getObjeto(persona.getDireccion(), Constantes.DIRECCION);
        if(direccion != null) {
            this.editText_localidad.setText(direccion.getLocalidad());
            this.editText_provincia.setText(direccion.getProvincia());
            this.editText_direccion.setText(direccion.getDireccion());
            this.editText_codigoPostal.setText(direccion.getCodigoPostal());
        }
    }

    /**
     * Método encargado de inicializar un DatePickerFragment para poder recoger una fecha.
     */
    private void mostrarDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String selectedDate = year + "-" + Utilidad.twoDigitsDate(month + 1) + "-" + Utilidad.twoDigitsDate(day); // Formateo la fecha.
                editText_fechaNacimiento.setText(selectedDate);
            }
        });

        newFragment.show(getActivity().getSupportFragmentManager(), Constantes.TAG_DATEPICKER);
    }

    /**
     * Método para modificar una persona de la base de datos.
     * El método realiza una petición a la API con los datos proporcionados por el usuario.
     */
    private void modificarPersona() {
        String nombre = this.editText_nombre.getText().toString();
        String apellidos = this.editText_apellidos.getText().toString();
        String dni = editText_dni.getText().toString();
        String fechaNacimiento = editText_fechaNacimiento.getText().toString();
        String sexo = spinner_sexo.getSelectedItem().toString();
        String telefonoFijo = editText_telefonoFijo.getText().toString();
        String telefonoMovil = editText_telefonoMovil.getText().toString();

        String localidad = this.editText_localidad.getText().toString();
        String provincia = this.editText_provincia.getText().toString();
        String direc = this.editText_direccion.getText().toString();
        String codigoPostal = this.editText_codigoPostal.getText().toString();

        Direccion direccion = new Direccion();
        direccion.setLocalidad(localidad);
        direccion.setProvincia(provincia);
        direccion.setDireccion(direc);
        direccion.setCodigoPostal(codigoPostal);

        Persona persona = new Persona();
        persona.setNombre(nombre);
        persona.setApellidos(apellidos);
        persona.setDni(dni);
        persona.setFechaNacimiento(fechaNacimiento);
        persona.setSexo(sexo);
        persona.setTelefonoFijo(telefonoFijo);
        persona.setTelefonoMovil(telefonoMovil);
        persona.setDireccion(direccion);

        APIService apiService = ClienteRetrofit.getInstance().getAPIService();

        Call<Object> call = apiService.modifyPersona(this.persona.getId(), persona, Constantes.TOKEN_BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Object persona = response.body();
                    AlertDialogBuilder.crearInfoAlerDialog(getContext(), Constantes.INFO_ALERTDIALOG_MODIFICADO_PERSONA);
                    getActivity().onBackPressed();
                } else {
                    AlertDialogBuilder.crearErrorAlerDialog(getContext(), Integer.toString(response.code()));
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
                System.out.println(t.getMessage());
            }
        });
    }

    /**
     * Método que revisa si los datos de los EditText de persona son válidos.
     *
     * @return Devuelve true si es válido de lo contrario devuelve false.
     */
    private boolean validarPersona() {
        boolean validNombre, validApellidos, validDni, validFechaNacimiento, validTelefonoFijo, validTelefonoMovil, validDireccion;

        validNombre = validarNombre(editText_nombre.getText().toString());
        validApellidos = validarApellidos(editText_apellidos.getText().toString());
        validDni = validarDni(editText_dni.getText().toString());
        validFechaNacimiento = validarFechaNacimiento(editText_fechaNacimiento.getText().toString());
        validTelefonoFijo = validarTelefonoFijo(editText_telefonoFijo.getText().toString());
        validTelefonoMovil = validarTelefonoMovil(editText_telefonoMovil.getText().toString());
        validDireccion = validarDireccion();

        if ((validNombre) && (validApellidos) && (validDni) && (validFechaNacimiento) && (validTelefonoFijo) && (validTelefonoMovil) && (validDireccion)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Método que revisa si los datos de los EditText de dirección son válidos.
     * @return Devuelve true si es válido de lo contrario devuelve false.
     */
    private boolean validarDireccion() {
        boolean validLocalidad, validProvincia, validDireccion, validCodigoPostal;

        validLocalidad = validarLocalidad(editText_localidad.getText().toString());
        validProvincia = validarProvincia(editText_provincia.getText().toString());
        validDireccion = validarDir(editText_direccion.getText().toString());
        validCodigoPostal = validarCodigoPostal(editText_codigoPostal.getText().toString());


        if((validLocalidad) && (validProvincia) && (validDireccion) && (validCodigoPostal)){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Método que inicializa todos los TextWachers de los EditTexts.
     * Los TextWachers se encuentran constantemente revisando si se ha añadido algo a los EditText.
     * Si se ha añadido algo, revisa si lo escrito en el EditText es válido o no.
     */
    private void inicializarListeners() {
        this.editText_nombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                validarNombre(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        this.editText_apellidos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                validarApellidos(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        this.editText_dni.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                validarDni(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        this.editText_fechaNacimiento.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                validarFechaNacimiento(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        this.editText_telefonoFijo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                validarTelefonoFijo(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        this.editText_telefonoMovil.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                validarTelefonoMovil(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        this.editText_localidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                validarLocalidad(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        this.editText_provincia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                validarProvincia(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        this.editText_direccion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                validarDir(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        this.editText_codigoPostal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                validarCodigoPostal(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    /**
     * Método para validar el campo nombre.
     * @param nombre
     * @return
     */
    public boolean validarNombre(String nombre) {
        boolean valid = false;
        if ((nombre.isEmpty()) || (nombre.trim().equals(Constantes.STRING_VACIO))) {         // Reviso si el nombre está vacio.
            textView_error_nombre.setText(R.string.textview_nombre_obligatorio);
            textView_error_nombre.setVisibility(View.VISIBLE);
            valid = false;                                              // Si está vacio entonces le asigno el texto de que es obligatorio y devuelvo false.
        } else {                                                        // De lo contrario devuelvo true y hago que el textView desaparezca.
            textView_error_nombre.setVisibility(View.GONE);
            valid = true;
        }
        return valid;
    }

    /**
     * Método para validar el campo apellidos.
     * @param apellidos
     * @return
     */
    public boolean validarApellidos(String apellidos) {
        boolean valid = false;
        if ((apellidos.isEmpty()) || (apellidos.trim().equals(Constantes.STRING_VACIO))) {    // Reviso si el apellido está vacio.
            textView_error_apellidos.setText(R.string.textview_apellidos_obligatorios);
            textView_error_apellidos.setVisibility(View.VISIBLE);
            valid = false;                                              // Si está vacio entonces le asigno el texto de que es obligatorio y devuelvo false.
        } else {                                                        // De lo contrario devuelvo true y hago que el textView desaparezca.
            textView_error_apellidos.setVisibility(View.GONE);
            valid = true;
        }
        return valid;
    }

    /**
     * Método para validar el DNI.
     * @param dni
     * @return
     */
    public boolean validarDni(String dni) {
        boolean valid = false;
        Pattern regexp = Pattern.compile(Constantes.PATRON_DNI);
        String letras = Constantes.LETRAS_DNI;

        if ((!dni.isEmpty()) || (!dni.trim().equals(Constantes.STRING_VACIO))) { // Reviso si el dni está vacio.
            if (regexp.matcher(dni).matches()) { // Reviso si el DNI cumple con el patrón.
                if (dni.charAt(8) == letras.charAt(Integer.parseInt(dni.substring(0, 8)) % 23)) { // Reviso si la letra coincide.
                    textView_error_dni.setVisibility(View.GONE);  // De lo contrario devuelvo true y hago que el textView desaparezca.
                    valid = true;
                } else{ // Si la letra no coincide, le asigno el texto de que no es válido y devuelvo false.
                    textView_error_dni.setText(R.string.textview_dni_no_valido);
                    textView_error_dni.setVisibility(View.VISIBLE);
                    valid = false;
                }
            } else { // Si no cumple con el patrón, le asigno el texto de que no es válido y devuelvo false.
                textView_error_dni.setText(R.string.textview_dni_no_valido);
                textView_error_dni.setVisibility(View.VISIBLE);
                valid = false;
            }
        } else { // Si está vacio entonces le asigno el texto de que es obligatorio y devuelvo false.
            textView_error_dni.setText(R.string.textview_dni_obligatorio);
            textView_error_dni.setVisibility(View.VISIBLE);
            valid = false;
        }
        return valid;
    }

    /**
     * Método para validar el campo fechaNacimiento.
     * @param fechaNacimiento
     * @return
     */
    public boolean validarFechaNacimiento(String fechaNacimiento) {
        boolean valid = false;
        if ((fechaNacimiento.isEmpty()) || (fechaNacimiento.trim().equals(Constantes.STRING_VACIO))) {     // Reviso si la fecha de nacimiento está vacia.
            textView_error_fechaNacimiento.setText(R.string.textview_fechaNacimiento_obligatorio);
            textView_error_fechaNacimiento.setVisibility(View.VISIBLE);
            valid = false;                                              // Si está vacia entonces le asigno el texto de que es obligatorio y devuelvo false.
        } else {                                                        // De lo contrario devuelvo true y hago que el textView desaparezca.
            textView_error_fechaNacimiento.setVisibility(View.GONE);
            valid = true;
        }
        return valid;
    }

    /**
     * Método para validar el campo telefonoFijo.
     * @param telefonoFijo
     * @return
     */
    public boolean validarTelefonoFijo(String telefonoFijo) {
        boolean valid = false;
        if((telefonoFijo.isEmpty()) || (telefonoFijo.trim().equals(Constantes.STRING_VACIO))) { // Reviso si el teléfono fijo está vacio.
            textView_error_telefonoFijo.setText(R.string.textview_telefonoFijo_obligatorio);    // Si esta vacio, le asigno el texto de que es obligatorio y devuelvo false.
            textView_error_telefonoFijo.setVisibility(View.VISIBLE);
            valid = false;
        } else {
            if(telefonoFijo.length() != 9) { // Reviso si cuenta con la longitud necesaria.
                textView_error_telefonoFijo.setText(R.string.textview_telefonoFijo_error_longitud); // Si no cuenta con la longitud, le asigno el texto de la longitud necesaria y devuelvo false.
                textView_error_telefonoFijo.setVisibility(View.VISIBLE);
                valid = false;
            } else {
                if(!telefonoFijo.matches(Constantes.PATRON_TELEFONO)) { // Reviso si cuenta con un patrón válido.
                    textView_error_telefonoFijo.setText(R.string.textview_telefonoFijo_error_caracter);
                    textView_error_telefonoFijo.setVisibility(View.VISIBLE);  // Si no cuenta con el patrón válido, le asigno el texto de que el patrón no es válido y devuelvo false.
                    valid = false;
                } else { // De lo contrario devuelvo true y hago que el textView desaparezca.
                    textView_error_telefonoFijo.setVisibility(View.GONE);
                    valid = true;
                }
            }
        }
        return valid;
    }

    /**
     * Método para validar el campo telefonoMovil.
     * @param telefonoMovil
     * @return
     */
    public boolean validarTelefonoMovil(String telefonoMovil) {
        boolean valid = false;
        if((telefonoMovil.isEmpty()) || (telefonoMovil.trim().equals(Constantes.STRING_VACIO))) { // Reviso si el teléfono móvil está vacio.
            textView_error_telefonoMovil.setText(R.string.textview_telefonoMovil_obligatorio);    // Si esta vacio, le asigno el texto de que es obligatorio y devuelvo false.
            textView_error_telefonoMovil.setVisibility(View.VISIBLE);
            valid = false;
        } else {
            if(telefonoMovil.length() != 9) { // Reviso si cuenta con la longitud necesaria.
                textView_error_telefonoMovil.setText(R.string.textview_telefonoMovil_error_longitud); // Si no cuenta con la longitud, le asigno el texto de la longitud necesaria y devuelvo false.
                textView_error_telefonoMovil.setVisibility(View.VISIBLE);
                valid = false;
            } else {
                if(!telefonoMovil.matches(Constantes.PATRON_TELEFONO)) { // Reviso si cuenta con un patrón válido.
                    textView_error_telefonoMovil.setText(R.string.textview_telefonoMovil_error_caracter);
                    textView_error_telefonoMovil.setVisibility(View.VISIBLE);  // Si no cuenta con el patrón válido, le asigno el texto de que el patrón no es válido y devuelvo false.
                    valid = false;
                } else { // De lo contrario devuelvo true y hago que el textView desaparezca.
                    textView_error_telefonoMovil.setVisibility(View.GONE);
                    valid = true;
                }
            }
        }
        return valid;
    }

    /**
     * Método para validar el campo localidad.
     * @param localidad
     * @return
     */
    public boolean validarLocalidad(String localidad) {
        boolean valid = false;
        if ((localidad.isEmpty()) || (localidad.trim().equals(Constantes.STRING_VACIO))) {     // Reviso si la localidad está vacia.
            textView_error_localidad.setText(R.string.textview_localidad_obligatoria);
            textView_error_localidad.setVisibility(View.VISIBLE);
            valid = false;                                              // Si está vacia entonces le asigno el texto de que es obligatoria y devuelvo false.
        } else {                                                        // De lo contrario devuelvo true y hago que el textView desaparezca.
            textView_error_localidad.setVisibility(View.GONE);
            valid = true;
        }
        return valid;
    }

    /**
     * Método para validar el campo provincia.
     * @param provincia
     * @return
     */
    public boolean validarProvincia(String provincia) {
        boolean valid = false;
        if ((provincia.isEmpty()) || (provincia.trim().equals(Constantes.STRING_VACIO))) {     // Reviso si la provincia está vacia.
            textView_error_provincia.setText(R.string.textview_provincia_obligatoria);
            textView_error_provincia.setVisibility(View.VISIBLE);
            valid = false;                                              // Si está vacia entonces le asigno el texto de que es obligatoria y devuelvo false.
        } else {                                                        // De lo contrario devuelvo true y hago que el textView desaparezca.
            textView_error_provincia.setVisibility(View.GONE);
            valid = true;
        }
        return valid;
    }

    /**
     * Método para validar el campo direccion.
     * @param direccion
     * @return
     */
    public boolean validarDir(String direccion) {
        boolean valid = false;
        if ((direccion.isEmpty()) || (direccion.trim().equals(Constantes.STRING_VACIO))) {     // Reviso si la dirección está vacia.
            textView_error_direccion.setText(R.string.textview_direccion_obligatoria);
            textView_error_direccion.setVisibility(View.VISIBLE);
            valid = false;                                              // Si está vacia entonces le asigno el texto de que es obligatoria y devuelvo false.
        } else {                                                        // De lo contrario devuelvo true y hago que el textView desaparezca.
            textView_error_direccion.setVisibility(View.GONE);
            valid = true;
        }
        return valid;
    }

    /**
     * Método para validar el campo codigoPostal.
     * @param codigoPostal
     * @return
     */
    public boolean validarCodigoPostal(String codigoPostal) {
        boolean valid = false;
        if ((codigoPostal.isEmpty()) || (codigoPostal.trim().equals(Constantes.STRING_VACIO))) {     // Reviso si la dirección está vacia.
            textView_error_codigoPostal.setText(R.string.textview_codigoPostal_obligatoria);
            textView_error_codigoPostal.setVisibility(View.VISIBLE);
            valid = false;                                              // Si está vacia entonces le asigno el texto de que es obligatoria y devuelvo false.
        } else {                                                        // De lo contrario devuelvo true y hago que el textView desaparezca.
            textView_error_codigoPostal.setVisibility(View.GONE);
            valid = true;
        }
        return valid;
    }
}