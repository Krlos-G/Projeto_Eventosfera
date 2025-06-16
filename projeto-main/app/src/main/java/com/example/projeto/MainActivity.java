package com.example.projeto;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int SPINNER_ITEM_TODOS = 0;

    private LinearLayout layoutFilters;
    private Button btnToggleFilters, btnLogin, btnClearFilters;
    private ListView lvEvents;
    private EditText etFilterDate, etSearchEventName;
    private Spinner spinnerFilterState, spinnerFilterCity, spinnerFilterEventType;

    private List<Evento> todosOsEventos;
    private List<Evento> eventosFiltrados;
    private EventoAdapter adapterPrincipal;

    private Calendar calendarioFiltroData;
    private String dataSelecionadaFiltro = "";
    private String nomePesquisadoFiltro = "";
    private String estadoSelecionadoFiltro = "";
    private String cidadeSelecionadaFiltro = "";
    private String tipoEventoSelecionadoFiltro = "";

    Map<String, List<String>> estadoCidadeMap = new HashMap<>();
    List<String> estadosList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicialização das Views
        layoutFilters = findViewById(R.id.layoutFilters);
        btnToggleFilters = findViewById(R.id.btnToggleFilters);
        btnLogin = findViewById(R.id.btnLogin);
        lvEvents = findViewById(R.id.lvEvents);
        etFilterDate = findViewById(R.id.etFilterDate);
        etSearchEventName = findViewById(R.id.etSearchEventName);
        spinnerFilterState = findViewById(R.id.spinnerFilterState);
        spinnerFilterCity = findViewById(R.id.spinnerFilterCity);
        spinnerFilterEventType = findViewById(R.id.spinnerFilterEventType);
        btnClearFilters = findViewById(R.id.btnClearFilters);

        calendarioFiltroData = Calendar.getInstance();

        // Configurar Adapter da ListView
        todosOsEventos = new ArrayList<>(RepositorioEventos.getListaEventos());
        eventosFiltrados = new ArrayList<>(todosOsEventos);
        adapterPrincipal = new EventoAdapter(this, R.layout.item_event, eventosFiltrados);
        lvEvents.setAdapter(adapterPrincipal);

        inicializarDadosEstadosCidades();
        configurarSpinnersEstadoCidade();
        configurarSpinnerTipoEvento();
        configurarListenersDeFiltro();

        Log.d(TAG, "MainActivity onCreate: Lista inicial com " + todosOsEventos.size() + " eventos.");
    }

    private void inicializarDadosEstadosCidades() {
        estadosList = new ArrayList<>();

        // Mapeamento completo de estados e cidades
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

        // Adiciona opção "Todos" no início da lista
        estadosList.add("Todos os estados");
        estadosList.addAll(estadoCidadeMap.keySet());
        Collections.sort(estadosList.subList(1, estadosList.size()));
    }

    private void configurarSpinnersEstadoCidade() {
        // Configurar Spinner de Estado
        ArrayAdapter<String> estadoAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, estadosList);
        estadoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilterState.setAdapter(estadoAdapter);

        // Configurar Spinner de Cidade (inicialmente vazio)
        List<String> cidadesIniciais = new ArrayList<>();
        cidadesIniciais.add("Todas as cidades");
        ArrayAdapter<String> cidadeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, cidadesIniciais);
        cidadeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilterCity.setAdapter(cidadeAdapter);

        // Listener para atualizar cidades quando estado mudar
        spinnerFilterState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == SPINNER_ITEM_TODOS) {
                    estadoSelecionadoFiltro = "";

                    // Atualiza spinner de cidades para mostrar apenas "Todas as cidades"
                    List<String> cidades = new ArrayList<>();
                    cidades.add("Todas as cidades");
                    ArrayAdapter<String> cidadeAdapter = new ArrayAdapter<>(MainActivity.this,
                            android.R.layout.simple_spinner_item, cidades);
                    cidadeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerFilterCity.setAdapter(cidadeAdapter);
                } else {
                    estadoSelecionadoFiltro = parent.getItemAtPosition(position).toString();
                    atualizarCidadesParaEstado(estadoSelecionadoFiltro);
                }
                aplicarFiltros();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Listener para spinner de cidades
        spinnerFilterCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == SPINNER_ITEM_TODOS) {
                    cidadeSelecionadaFiltro = "";
                } else {
                    cidadeSelecionadaFiltro = parent.getItemAtPosition(position).toString();
                }
                aplicarFiltros();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void atualizarCidadesParaEstado(String estado) {
        List<String> cidades = estadoCidadeMap.get(estado);
        List<String> cidadesComOpcaoTodas = new ArrayList<>();
        cidadesComOpcaoTodas.add("Todas as cidades");

        if (cidades != null) {
            cidadesComOpcaoTodas.addAll(cidades);
        }

        ArrayAdapter<String> cidadeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, cidadesComOpcaoTodas);
        cidadeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilterCity.setAdapter(cidadeAdapter);
    }

    private void configurarSpinnerTipoEvento() {
        // Cria array com opção "Todos" no início
        List<String> tiposEvento = new ArrayList<>();
        tiposEvento.add("Todos os tipos");

        // Adiciona os tipos do array de recursos
        String[] tiposArray = getResources().getStringArray(R.array.tipos_evento_array);
        for (int i = 1; i < tiposArray.length; i++) { // Começa em 1 para pular o primeiro item ("Selecione...")
            tiposEvento.add(tiposArray[i]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, tiposEvento);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilterEventType.setAdapter(adapter);

        spinnerFilterEventType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == SPINNER_ITEM_TODOS) {
                    tipoEventoSelecionadoFiltro = "";
                } else {
                    tipoEventoSelecionadoFiltro = parent.getItemAtPosition(position).toString();
                }
                aplicarFiltros();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void configurarListenersDeFiltro() {
        // Botão para mostrar/ocultar filtros
        btnToggleFilters.setOnClickListener(v -> {
            if (layoutFilters.getVisibility() == View.GONE) {
                layoutFilters.setVisibility(View.VISIBLE);
                btnToggleFilters.setText("Ocultar Filtros");
            } else {
                layoutFilters.setVisibility(View.GONE);
                btnToggleFilters.setText("Mostrar Filtros");
            }
        });

        // Botão de login
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AreaOrganizadorLoginActivity.class);
            startActivity(intent);
        });

        // Seletor de data
        etFilterDate.setOnClickListener(v -> mostrarDatePickerDialogFiltro());

        // Filtro por nome do evento
        etSearchEventName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nomePesquisadoFiltro = s.toString().toLowerCase(Locale.getDefault()).trim();
                aplicarFiltros();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Botão para limpar filtros
        btnClearFilters.setOnClickListener(v -> limparFiltros());
    }

    private void mostrarDatePickerDialogFiltro() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            calendarioFiltroData.set(Calendar.YEAR, year);
            calendarioFiltroData.set(Calendar.MONTH, monthOfYear);
            calendarioFiltroData.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            atualizarLabelDataFiltro();
            aplicarFiltros();
        };
        new DatePickerDialog(MainActivity.this, dateSetListener,
                calendarioFiltroData.get(Calendar.YEAR),
                calendarioFiltroData.get(Calendar.MONTH),
                calendarioFiltroData.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void atualizarLabelDataFiltro() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        dataSelecionadaFiltro = sdf.format(calendarioFiltroData.getTime());
        etFilterDate.setText(dataSelecionadaFiltro);
    }

    private void limparFiltros() {
        dataSelecionadaFiltro = "";
        etFilterDate.setText("");
        nomePesquisadoFiltro = "";
        etSearchEventName.setText("");
        estadoSelecionadoFiltro = "";
        spinnerFilterState.setSelection(SPINNER_ITEM_TODOS);
        cidadeSelecionadaFiltro = "";
        tipoEventoSelecionadoFiltro = "";
        spinnerFilterEventType.setSelection(SPINNER_ITEM_TODOS);

        Log.d(TAG, "Filtros limpos");
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        Log.d(TAG, "Aplicando filtros: Data=" + dataSelecionadaFiltro +
                ", Nome=" + nomePesquisadoFiltro +
                ", Estado=" + estadoSelecionadoFiltro +
                ", Cidade=" + cidadeSelecionadaFiltro +
                ", Tipo=" + tipoEventoSelecionadoFiltro);

        List<Evento> eventosParaFiltrar = new ArrayList<>(RepositorioEventos.getListaEventos());
        List<Evento> resultadoFiltrado;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            resultadoFiltrado = eventosParaFiltrar.stream()
                    .filter(evento -> {
                        boolean dataMatch = dataSelecionadaFiltro.isEmpty() ||
                                (evento.getDataInicial() != null && evento.getDataInicial().equals(dataSelecionadaFiltro));
                        boolean nomeMatch = nomePesquisadoFiltro.isEmpty() ||
                                (evento.getNome() != null && evento.getNome().toLowerCase(Locale.getDefault()).contains(nomePesquisadoFiltro));
                        boolean estadoMatch = estadoSelecionadoFiltro.isEmpty() ||
                                (evento.getEstado() != null && evento.getEstado().equalsIgnoreCase(estadoSelecionadoFiltro));
                        boolean cidadeMatch = cidadeSelecionadaFiltro.isEmpty() ||
                                (evento.getCidade() != null && evento.getCidade().equalsIgnoreCase(cidadeSelecionadaFiltro));
                        boolean tipoMatch = tipoEventoSelecionadoFiltro.isEmpty() ||
                                (evento.getTipoEvento() != null && evento.getTipoEvento().equalsIgnoreCase(tipoEventoSelecionadoFiltro));

                        return dataMatch && nomeMatch && estadoMatch && cidadeMatch && tipoMatch;
                    })
                    .collect(Collectors.toList());
        } else {
            resultadoFiltrado = new ArrayList<>();
            for (Evento evento : eventosParaFiltrar) {
                boolean dataMatch = dataSelecionadaFiltro.isEmpty() ||
                        (evento.getDataInicial() != null && evento.getDataInicial().equals(dataSelecionadaFiltro));
                boolean nomeMatch = nomePesquisadoFiltro.isEmpty() ||
                        (evento.getNome() != null && evento.getNome().toLowerCase(Locale.getDefault()).contains(nomePesquisadoFiltro));
                boolean estadoMatch = estadoSelecionadoFiltro.isEmpty() ||
                        (evento.getEstado() != null && evento.getEstado().equalsIgnoreCase(estadoSelecionadoFiltro));
                boolean cidadeMatch = cidadeSelecionadaFiltro.isEmpty() ||
                        (evento.getCidade() != null && evento.getCidade().equalsIgnoreCase(cidadeSelecionadaFiltro));
                boolean tipoMatch = tipoEventoSelecionadoFiltro.isEmpty() ||
                        (evento.getTipoEvento() != null && evento.getTipoEvento().equalsIgnoreCase(tipoEventoSelecionadoFiltro));

                if (dataMatch && nomeMatch && estadoMatch && cidadeMatch && tipoMatch) {
                    resultadoFiltrado.add(evento);
                }
            }
        }

        eventosFiltrados.clear();
        eventosFiltrados.addAll(resultadoFiltrado);
        adapterPrincipal.notifyDataSetChanged();

        Log.d(TAG, "Filtro aplicado. " + eventosFiltrados.size() + " eventos correspondem.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        todosOsEventos = new ArrayList<>(RepositorioEventos.getListaEventos());
        aplicarFiltros();
        Log.d(TAG, "MainActivity onResume: Lista e filtros atualizados.");
    }
}