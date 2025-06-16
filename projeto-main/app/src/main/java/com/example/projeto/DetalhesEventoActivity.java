package com.example.projeto;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetalhesEventoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "DetalhesEventoActivity";
    private GoogleMap mMap;
    private Evento evento;
    private LatLng coordenadas;
    private Button btnOpenInMaps;
    private SupportMapFragment mapFragment;

    private final String GOOGLE_API_KEY = "AIzaSyDDxhEvABjX9XXmxTkM2xpxMCgUrTObqsA"; // <-- substitua pela sua chave da Geocoding API

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizacao_evento);

        evento = (Evento) getIntent().getSerializableExtra("EVENTO");

        TextView tvEventTitle = findViewById(R.id.tvEventTitle);
        TextView tvEventTypeDetail = findViewById(R.id.tvEventTypeDetail);
        TextView tvEventDescription = findViewById(R.id.tvEventDescription);
        TextView tvEventStartDateTime = findViewById(R.id.tvEventStartDateTime);
        TextView tvEventEndDateTime = findViewById(R.id.tvEventEndDateTime);
        TextView tvEventAddress = findViewById(R.id.tvEventAddress);
        btnOpenInMaps = findViewById(R.id.btnOpenInMaps);
        GridView gridImages = findViewById(R.id.gridImages);
        CardView cardImageGallery = findViewById(R.id.cardImageGallery);

        TextView tvEventTypeLabel = findViewById(R.id.tvEventTypeLabel);
        TextView tvEventStartDateLabel = findViewById(R.id.tvEventStartDateLabel);
        TextView tvEventEndDateLabel = findViewById(R.id.tvEventEndDateLabel);

        if (evento != null) {
            tvEventTitle.setText(evento.getNome());
            tvEventDescription.setText(evento.getDescricao());
            tvEventAddress.setText("Endereço: " + evento.getEnderecoCompleto());

            // Exibição dos campos de data/hora (como no seu código original)
            String startDateTimeText = "";
            if (evento.getDataInicial() != null) startDateTimeText += evento.getDataInicial();
            if (evento.getHoraInicial() != null && !evento.getHoraInicial().isEmpty())
                startDateTimeText += (startDateTimeText.isEmpty() ? "" : " às ") + evento.getHoraInicial();
            tvEventStartDateTime.setText(startDateTimeText.isEmpty() ? "Não informado" : startDateTimeText);

            String endDateTimeText = "";
            if (evento.getDataFinal() != null) endDateTimeText += evento.getDataFinal();
            if (evento.getHoraFinal() != null && !evento.getHoraFinal().isEmpty())
                endDateTimeText += (endDateTimeText.isEmpty() ? "" : " às ") + evento.getHoraFinal();
            tvEventEndDateTime.setText(endDateTimeText.isEmpty() ? "Não informado" : endDateTimeText);

            if (evento.getTipoEvento() != null && !evento.getTipoEvento().isEmpty()) {
                tvEventTypeDetail.setText(evento.getTipoEvento());
                tvEventTypeLabel.setVisibility(View.VISIBLE);
            } else {
                tvEventTypeDetail.setText("Não informado");
                tvEventTypeLabel.setVisibility(View.GONE);
            }

            // Galeria de imagens
            List<String> imagensUris = evento.getImagens();
            if (imagensUris != null && !imagensUris.isEmpty()) {
                cardImageGallery.setVisibility(View.VISIBLE);
                ImagemAdapter adapter = new ImagemAdapter(this, imagensUris);
                gridImages.setAdapter(adapter);
            } else {
                cardImageGallery.setVisibility(View.GONE);
            }

            // Iniciar fragmento de mapa
            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.mapFragment);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }

            // Buscar coordenadas com Geocoding API
            buscarCoordenadasViaAPI(evento.getEnderecoCompleto());

        } else {
            Toast.makeText(this, "Erro ao carregar evento", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void buscarCoordenadasViaAPI(String endereco) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" +
                Uri.encode(endereco) + "&key=" + GOOGLE_API_KEY;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Erro na requisição Geocoding API", e);
                runOnUiThread(() ->
                        Toast.makeText(DetalhesEventoActivity.this, "Erro ao buscar coordenadas", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "Resposta não-sucedida da Geocoding API");
                    return;
                }

                String responseData = response.body().string();
                try {
                    JSONObject json = new JSONObject(responseData);
                    JSONArray results = json.getJSONArray("results");
                    if (results.length() > 0) {
                        JSONObject location = results.getJSONObject(0)
                                .getJSONObject("geometry")
                                .getJSONObject("location");

                        double lat = location.getDouble("lat");
                        double lng = location.getDouble("lng");

                        coordenadas = new LatLng(lat, lng);

                        runOnUiThread(() -> {
                            if (mMap != null) {
                                mMap.addMarker(new MarkerOptions().position(coordenadas).title(evento.getNome()));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenadas, 16f));
                            }

                            btnOpenInMaps.setOnClickListener(v -> {
                                String uriString = String.format(Locale.US, "geo:%f,%f?q=%f,%f(%s)",
                                        coordenadas.latitude,
                                        coordenadas.longitude,
                                        coordenadas.latitude,
                                        coordenadas.longitude,
                                        Uri.encode(evento.getNome()));
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriString));
                                mapIntent.setPackage("com.google.android.apps.maps");
                                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                                    startActivity(mapIntent);
                                } else {
                                    Toast.makeText(DetalhesEventoActivity.this,
                                            "Google Maps não encontrado.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        });
                    } else {
                        Log.e(TAG, "Nenhum resultado da Geocoding API");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Erro ao parsear JSON da Geocoding API", e);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // A exibição no mapa será feita depois que as coordenadas forem buscadas via API
    }
}
