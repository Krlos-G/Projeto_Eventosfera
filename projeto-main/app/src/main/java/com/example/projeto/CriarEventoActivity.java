package com.example.projeto;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class CriarEventoActivity extends AppCompatActivity {

    public static final String EXTRA_NOVO_EVENTO = "com.example.projeto.NOVO_EVENTO";
    private static final int REQUEST_CODE_PICK_IMAGE = 101;
    private static final String TAG = "CriarEventoActivity";

    private RecyclerView rvImages;
    private ImageAdapter imageAdapterRecycler;
    private List<String> imagePathList;

    private EditText etEventName, etEventDescription, etStartDate, etEndDate,
            etStartTime, etEndTime,
            etRua, etNumero, etComplemento, etCEP, etBairro,
            etLatitude, etLongitude;
    private Spinner spinnerEventType, spinnerCidade, spinnerEstado;
    private Button btnSalvarEvento, btnAddImagem;

    private Calendar calendarioStartDate, calendarioEndDate,
            calendarioStartTime, calendarioEndTime;

    private int idOrganizadorAtual;
    private String tipoEventoSelecionado = "";
    private Evento eventoParaEditar;
    private boolean isEditMode = false;

    Map<String, List<String>> estadoCidadeMap = new HashMap<>();
    List<String> estadosList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_evento);

        idOrganizadorAtual = getIntent().getIntExtra("ID_ORGANIZADOR_LOGADO", -1);

        calendarioStartDate = Calendar.getInstance();
        calendarioEndDate = Calendar.getInstance();
        calendarioStartTime = Calendar.getInstance();
        calendarioEndTime = Calendar.getInstance();

        // Inicializar Views
        etEventName = findViewById(R.id.etEventName);
        etEventDescription = findViewById(R.id.etEventDescription);
        spinnerEventType = findViewById(R.id.spinnerEventType);
        etStartDate = findViewById(R.id.etStartDate);
        etStartTime = findViewById(R.id.etStartTime);
        etEndDate = findViewById(R.id.etEndDate);
        etEndTime = findViewById(R.id.etEndTime);
        etRua = findViewById(R.id.etRua);
        etNumero = findViewById(R.id.etNumero);
        etComplemento = findViewById(R.id.etComplemento);
        etBairro = findViewById(R.id.etBairro);
        etCEP = findViewById(R.id.etCEP);
        spinnerCidade = findViewById(R.id.etCidade);
        spinnerEstado = findViewById(R.id.etEstado);
        etLatitude = findViewById(R.id.etLatitude);
        etLongitude = findViewById(R.id.etLongitude);
        rvImages = findViewById(R.id.rvImages);
        btnSalvarEvento = findViewById(R.id.btnSalvarEvento);
        btnAddImagem = findViewById(R.id.btnAddImagem);

        configurarSpinnerTipoEvento();
        inicializarDadosEstadosCidades();
        configurarSpinnersEstadoCidade();

        imagePathList = new ArrayList<>();
        imageAdapterRecycler = new ImageAdapter(this, imagePathList, position -> {
            if (position >= 0 && position < imagePathList.size()) {
                Uri fileUri = Uri.parse(imagePathList.get(position));
                if ("file".equals(fileUri.getScheme())) {
                    new File(fileUri.getPath()).delete();
                }
                imagePathList.remove(position);
                imageAdapterRecycler.notifyItemRemoved(position);
                imageAdapterRecycler.notifyItemRangeChanged(position, imagePathList.size());
            }
        });
        rvImages.setLayoutManager(new GridLayoutManager(this, 3));
        rvImages.setAdapter(imageAdapterRecycler);

        // Verificar se está em modo de edição
        if (getIntent().hasExtra("EVENTO_PARA_EDITAR")) {
            eventoParaEditar = (Evento) getIntent().getSerializableExtra("EVENTO_PARA_EDITAR");
            isEditMode = true;
            btnSalvarEvento.setText("Atualizar Evento");
            if (eventoParaEditar != null) {
                preencherCamposParaEdicao();
                idOrganizadorAtual = eventoParaEditar.getIdOrganizador();
            }
        } else {
            btnSalvarEvento.setText("Salvar Evento");
            if (idOrganizadorAtual == -1) {
                Log.e(TAG, "ID do Organizador não foi passado para CriarEventoActivity. Usando ID de fallback 0.");
                idOrganizadorAtual = 0;
            }
        }

        etStartDate.setOnClickListener(v -> showDatePickerDialog(etStartDate, calendarioStartDate));
        etEndDate.setOnClickListener(v -> showDatePickerDialog(etEndDate, calendarioEndDate));
        etStartTime.setOnClickListener(v -> showTimePickerDialog(etStartTime, calendarioStartTime));
        etEndTime.setOnClickListener(v -> showTimePickerDialog(etEndTime, calendarioEndTime));
        btnAddImagem.setOnClickListener(v -> {
            if (imagePathList.size() >= 6) {
                Toast.makeText(this, "Limite de 6 imagens atingido.", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
        });
        btnSalvarEvento.setOnClickListener(v -> salvarOuAtualizarEvento());
    }

    private void inicializarDadosEstadosCidades() {
        estadosList = new ArrayList<>();

        // Mapeamento de estados e cidades (pode ser expandido)
        estadoCidadeMap.put("Acre", Arrays.asList("Rio Branco", "Cruzeiro do Sul", "Sena Madureira"));
        estadoCidadeMap.put("Alagoas", Arrays.asList("Maceió", "Arapiraca", "Palmeira dos Índios"));
        estadoCidadeMap.put("Amapá", Arrays.asList("Macapá", "Santana", "Laranjal do Jari"));
        estadoCidadeMap.put("Amazonas", Arrays.asList("Manaus", "Parintins", "Itacoatiara"));
        estadoCidadeMap.put("Bahia", Arrays.asList("Salvador", "Feira de Santana", "Vitória da Conquista"));
        estadoCidadeMap.put("Ceará", Arrays.asList("Fortaleza", "Caucaia", "Juazeiro do Norte"));
        estadoCidadeMap.put("Distrito Federal", Arrays.asList("Brasília"));
        estadoCidadeMap.put("Espírito Santo", Arrays.asList("Vitória", "Vila Velha", "Cariacica"));
        estadoCidadeMap.put("Goiás", Arrays.asList("Goiânia", "Anápolis", "Aparecida de Goiânia"));
        estadoCidadeMap.put("Maranhão", Arrays.asList("São Luís", "Imperatriz", "Timon"));
        estadoCidadeMap.put("Mato Grosso", Arrays.asList("Cuiabá", "Várzea Grande", "Rondonópolis"));
        estadoCidadeMap.put("Mato Grosso do Sul", Arrays.asList("Campo Grande", "Dourados", "Três Lagoas"));
        estadoCidadeMap.put("Minas Gerais", Arrays.asList("Belo Horizonte", "Uberlândia", "Contagem"));
        estadoCidadeMap.put("Pará", Arrays.asList("Belém", "Ananindeua", "Santarém"));
        estadoCidadeMap.put("Paraíba", Arrays.asList("João Pessoa", "Campina Grande", "Santa Rita"));
        estadoCidadeMap.put("Paraná", Arrays.asList("Curitiba", "Londrina", "Maringá"));
        estadoCidadeMap.put("Pernambuco", Arrays.asList("Recife", "Jaboatão dos Guararapes", "Olinda"));
        estadoCidadeMap.put("Piauí", Arrays.asList("Teresina", "Parnaíba", "Picos"));
        estadoCidadeMap.put("Rio de Janeiro", Arrays.asList("Rio de Janeiro", "São Gonçalo", "Duque de Caxias"));
        estadoCidadeMap.put("Rio Grande do Norte", Arrays.asList("Natal", "Mossoró", "Parnamirim"));
        estadoCidadeMap.put("Rio Grande do Sul", Arrays.asList("Porto Alegre", "Caxias do Sul", "Pelotas"));
        estadoCidadeMap.put("Rondônia", Arrays.asList("Porto Velho", "Ji-Paraná", "Ariquemes"));
        estadoCidadeMap.put("Roraima", Arrays.asList("Boa Vista", "Rorainópolis", "Caracaraí"));
        estadoCidadeMap.put("Santa Catarina", Arrays.asList("Florianópolis", "Joinville", "Blumenau"));
        estadoCidadeMap.put("São Paulo", Arrays.asList("São Paulo", "Campinas", "Santos"));
        estadoCidadeMap.put("Sergipe", Arrays.asList("Aracaju", "Nossa Senhora do Socorro", "Lagarto"));
        estadoCidadeMap.put("Tocantins", Arrays.asList("Palmas", "Araguaína", "Gurupi"));

        estadosList.addAll(estadoCidadeMap.keySet());
        Collections.sort(estadosList);
    }

    private void configurarSpinnerTipoEvento() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tipos_evento_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEventType.setAdapter(adapter);
        spinnerEventType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) tipoEventoSelecionado = parent.getItemAtPosition(position).toString();
                else tipoEventoSelecionado = "";
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { tipoEventoSelecionado = ""; }
        });
    }

    private void configurarSpinnersEstadoCidade() {
        // Configurar Spinner de Estado
        ArrayAdapter<String> estadoAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, estadosList);
        estadoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(estadoAdapter);

        // Configurar Spinner de Cidade (inicialmente vazio)
        ArrayAdapter<String> cidadeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new ArrayList<>());
        cidadeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCidade.setAdapter(cidadeAdapter);

        // Listener para atualizar cidades quando estado mudar
        spinnerEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String estadoSelecionado = parent.getItemAtPosition(position).toString();
                atualizarCidadesParaEstado(estadoSelecionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Limpar cidades quando nenhum estado estiver selecionado
                ArrayAdapter<String> cidadeAdapter = new ArrayAdapter<>(CriarEventoActivity.this,
                        android.R.layout.simple_spinner_item, new ArrayList<>());
                cidadeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCidade.setAdapter(cidadeAdapter);
            }
        });
    }

    private void atualizarCidadesParaEstado(String estado) {
        List<String> cidades = estadoCidadeMap.get(estado);
        if (cidades != null) {
            ArrayAdapter<String> cidadeAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, cidades);
            cidadeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCidade.setAdapter(cidadeAdapter);
        } else {
            // Se não houver cidades para o estado selecionado
            ArrayAdapter<String> cidadeAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, new ArrayList<>());
            cidadeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCidade.setAdapter(cidadeAdapter);
        }
    }

    private void preencherCamposParaEdicao() {
        etEventName.setText(eventoParaEditar.getNome());
        etEventDescription.setText(eventoParaEditar.getDescricao());

        if (eventoParaEditar.getTipoEvento() != null) {
            ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerEventType.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++) {
                if (adapter.getItem(i).toString().equals(eventoParaEditar.getTipoEvento())) {
                    spinnerEventType.setSelection(i);
                    break;
                }
            }
        }

        etStartDate.setText(eventoParaEditar.getDataInicial());
        etStartTime.setText(eventoParaEditar.getHoraInicial());
        etEndDate.setText(eventoParaEditar.getDataFinal());
        etEndTime.setText(eventoParaEditar.getHoraFinal());

        SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
        try {
            if (eventoParaEditar.getDataInicial() != null) calendarioStartDate.setTime(sdfDate.parse(eventoParaEditar.getDataInicial()));
            if (eventoParaEditar.getHoraInicial() != null) calendarioStartTime.setTime(sdfTime.parse(eventoParaEditar.getHoraInicial()));
            if (eventoParaEditar.getDataFinal() != null) calendarioEndDate.setTime(sdfDate.parse(eventoParaEditar.getDataFinal()));
            if (eventoParaEditar.getHoraFinal() != null) calendarioEndTime.setTime(sdfTime.parse(eventoParaEditar.getHoraFinal()));
        } catch (ParseException e) { Log.e(TAG, "Erro ao parse de data/hora para edição", e); }

        etRua.setText(eventoParaEditar.getRua());
        etNumero.setText(eventoParaEditar.getNumero());
        etComplemento.setText(eventoParaEditar.getComplemento());
        etCEP.setText(eventoParaEditar.getCep());

        // Preencher Estado
        if (eventoParaEditar.getEstado() != null) {
            int estadoPosition = ((ArrayAdapter<String>) spinnerEstado.getAdapter()).getPosition(eventoParaEditar.getEstado());
            if (estadoPosition >= 0) {
                spinnerEstado.setSelection(estadoPosition);
            }
        }

        // Preencher Cidade (com pequeno delay para garantir que as cidades foram carregadas)
        new Handler().postDelayed(() -> {
            if (eventoParaEditar.getCidade() != null) {
                int cidadePosition = ((ArrayAdapter<String>) spinnerCidade.getAdapter()).getPosition(eventoParaEditar.getCidade());
                if (cidadePosition >= 0) {
                    spinnerCidade.setSelection(cidadePosition);
                }
            }
        }, 100);

        etLatitude.setText(String.valueOf(eventoParaEditar.getLatitude()));
        etLongitude.setText(String.valueOf(eventoParaEditar.getLongitude()));

        if (eventoParaEditar.getImagens() != null) {
            imagePathList.addAll(eventoParaEditar.getImagens());
            imageAdapterRecycler.notifyDataSetChanged();
        }
    }

    private void salvarOuAtualizarEvento() {
        String nome = etEventName.getText().toString().trim();
        String descricao = etEventDescription.getText().toString().trim();
        String dataInicialStr = etStartDate.getText().toString().trim();
        String horaInicialStr = etStartTime.getText().toString().trim();
        String dataFinalStr = etEndDate.getText().toString().trim();
        String horaFinalStr = etEndTime.getText().toString().trim();
        String rua = etRua.getText().toString().trim();
        String numero = etNumero.getText().toString().trim();
        String complemento = etComplemento.getText().toString().trim();
        String bairro = etBairro.getText().toString().trim();
        String cep = etCEP.getText().toString().trim();

        // Obter valores dos Spinners
        String estado = spinnerEstado.getSelectedItem() != null ? spinnerEstado.getSelectedItem().toString() : "";
        String cidade = spinnerCidade.getSelectedItem() != null ? spinnerCidade.getSelectedItem().toString() : "";

        double latitude = 0.0, longitude = 0.0;
        try {
            if (!etLatitude.getText().toString().isEmpty()) latitude = Double.parseDouble(etLatitude.getText().toString().replace(',', '.'));
            if (!etLongitude.getText().toString().isEmpty()) longitude = Double.parseDouble(etLongitude.getText().toString().replace(',', '.'));
        } catch (NumberFormatException e) { Log.e(TAG, "Latitude/Longitude inválida", e); }

        if (nome.isEmpty() || tipoEventoSelecionado.isEmpty() ||
                dataInicialStr.isEmpty() || horaInicialStr.isEmpty() ||
                dataFinalStr.isEmpty() || horaFinalStr.isEmpty() ||
                rua.isEmpty() || cidade.isEmpty() || estado.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos obrigatórios.", Toast.LENGTH_LONG).show();
            return;
        }

        Evento eventoProcessado;
        if (isEditMode) {
            eventoProcessado = new Evento(eventoParaEditar.getId(), nome, descricao, tipoEventoSelecionado,
                    idOrganizadorAtual, dataInicialStr, dataFinalStr, horaInicialStr, horaFinalStr,
                    rua, numero, complemento, bairro, cep, estado, cidade, latitude, longitude,
                    new ArrayList<>(imagePathList));
        } else {
            int idEvento = new Random().nextInt(100000) + 1;
            eventoProcessado = new Evento(idEvento, nome, descricao, tipoEventoSelecionado,
                    idOrganizadorAtual, dataInicialStr, dataFinalStr, horaInicialStr, horaFinalStr,
                    rua, numero, complemento, bairro, cep, estado, cidade, latitude, longitude,
                    new ArrayList<>(imagePathList));
        }

        Intent resultadoIntent = new Intent();
        resultadoIntent.putExtra(EXTRA_NOVO_EVENTO, eventoProcessado);
        setResult(Activity.RESULT_OK, resultadoIntent);
        finish();
    }

    private void showDatePickerDialog(final EditText editText, final Calendar calendario) {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            calendario.set(Calendar.YEAR, year);
            calendario.set(Calendar.MONTH, monthOfYear);
            calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateLabel(editText, calendario);
        };
        new DatePickerDialog(CriarEventoActivity.this, dateSetListener,
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateDateLabel(EditText editText, Calendar calendario) {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        editText.setText(sdf.format(calendario.getTime()));
    }

    private void showTimePickerDialog(final EditText editText, final Calendar calendario) {
        TimePickerDialog.OnTimeSetListener timeSetListener = (view, hourOfDay, minute) -> {
            calendario.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendario.set(Calendar.MINUTE, minute);
            updateTimeLabel(editText, calendario);
        };
        new TimePickerDialog(CriarEventoActivity.this, timeSetListener,
                calendario.get(Calendar.HOUR_OF_DAY),
                calendario.get(Calendar.MINUTE),
                true).show();
    }

    private void updateTimeLabel(EditText editText, Calendar calendario) {
        String myFormat = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        editText.setText(sdf.format(calendario.getTime()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null && imagePathList.size() < 6) {
                String internalPath = copyImageToInternalStorage(selectedImageUri, "evt_img_" + System.currentTimeMillis());
                if (internalPath != null) {
                    imagePathList.add(internalPath);
                    imageAdapterRecycler.notifyItemInserted(imagePathList.size() - 1);
                } else {
                    Toast.makeText(this, "Falha ao copiar imagem para armazenamento interno.", Toast.LENGTH_SHORT).show();
                }
            } else if (imagePathList.size() >= 6) {
                Toast.makeText(this, "Você já atingiu o limite de 6 imagens.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String copyImageToInternalStorage(Uri uri, String newFileNameBase) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        String extension = getMimeType(this, uri);
        if (extension == null || extension.isEmpty()) {
            extension = "jpg";
        }
        String fileName = newFileNameBase + "." + extension;
        File directory = getApplicationContext().getDir("event_images", Context.MODE_PRIVATE);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                Log.e(TAG, "Falha ao criar diretório de imagens do evento.");
                return null;
            }
        }
        File outputFile = new File(directory, fileName);
        try {
            inputStream = getContentResolver().openInputStream(uri);
            if (inputStream == null) {
                Log.e(TAG, "Não foi possível abrir InputStream para URI: " + uri);
                return null;
            }
            outputStream = new FileOutputStream(outputFile);
            byte[] buffer = new byte[4096];
            int length;
            while ((length = inputStream.read(buffer)) > 0) { outputStream.write(buffer, 0, length); }
            Log.d(TAG, "Imagem copiada para: " + outputFile.getAbsolutePath());
            return Uri.fromFile(outputFile).toString();
        } catch (Exception e) {
            Log.e(TAG, "Erro ao copiar imagem para armazenamento interno", e);
            if (outputFile.exists()) {
                outputFile.delete();
            }
            return null;
        } finally {
            try {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            } catch (Exception e) { Log.e(TAG, "Erro ao fechar streams", e); }
        }
    }

    public static String getMimeType(Context context, Uri uri) {
        String extension = null;
        try {
            if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
                final MimeTypeMap mime = MimeTypeMap.getSingleton();
                extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
            } else {
                String path = uri.getPath();
                if (path != null) {
                    extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(path)).toString());
                }
            }
        } catch (Exception e) {
            Log.e("GetMimeType", "Erro ao obter MimeType para URI: " + uri, e);
        }
        return extension;
    }
}