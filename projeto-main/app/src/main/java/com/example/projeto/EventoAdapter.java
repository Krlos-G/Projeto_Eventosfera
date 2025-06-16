package com.example.projeto;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class EventoAdapter extends ArrayAdapter<Evento> {
    private final LayoutInflater inflater;
    private final int resource;
    private static final String TAG = "EventoAdapter";
    private Context mContext;
    private boolean isEditableContext;

    public EventoAdapter(Context context, int resource, List<Evento> eventos, boolean isEditable) {
        super(context, resource, eventos);
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.resource = resource;
        this.isEditableContext = isEditable;
    }

    public EventoAdapter(Context context, int resource, List<Evento> eventos) {
        this(context, resource, eventos, false);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Evento evento = getItem(position);
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(resource, parent, false);
            holder = new ViewHolder();
            holder.tvEventName = convertView.findViewById(R.id.tvEventName);
            holder.tvEventDateTime = convertView.findViewById(R.id.tvEventDateTime);
            holder.tvEventLocation = convertView.findViewById(R.id.tvEventLocation);
            holder.tvEventType = convertView.findViewById(R.id.tvEventType);
            holder.tvEventOrganizer = convertView.findViewById(R.id.tvEventOrganizer);
            holder.imageView = convertView.findViewById(R.id.imageView);
            holder.btnEditEvent = convertView.findViewById(R.id.btnEditEvent);
            holder.btnDeleteEvent = convertView.findViewById(R.id.btnDeleteEvent);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (evento != null) {
            // Preenchimento dos dados (nome, data, tipo, etc.)
            holder.tvEventName.setText(evento.getNome());
            holder.tvEventDateTime.setText(evento.getDataInicial() + " às " + evento.getHoraInicial());
            holder.tvEventType.setText(evento.getTipoEvento());
            holder.tvEventLocation.setText(evento.getEnderecoCompleto());
            Organizador organizador = RepositorioOrganizadores.getOrganizadorPorId(evento.getIdOrganizador());
            if (organizador != null) {
                holder.tvEventOrganizer.setText("Por: " + organizador.getNome());
            } else {
                holder.tvEventOrganizer.setText("Organizador ID: " + evento.getIdOrganizador());
            }
            List<String> imagensUris = evento.getImagens();
            if (imagensUris != null && !imagensUris.isEmpty() && imagensUris.get(0) != null && !imagensUris.get(0).isEmpty()) {
                Picasso.get().load(imagensUris.get(0)).fit().centerCrop().into(holder.imageView);
            } else {
                holder.imageView.setImageDrawable(null);
            }

            // Lógica dos botões
            if (isEditableContext) {
                holder.btnEditEvent.setVisibility(View.VISIBLE);
                holder.btnDeleteEvent.setVisibility(View.VISIBLE);

                holder.btnEditEvent.setOnClickListener(v -> {
                    Intent intent = new Intent(mContext, CriarEventoActivity.class);
                    intent.putExtra("EVENTO_PARA_EDITAR", evento);
                    if (mContext instanceof Activity) {
                        ((Activity) mContext).startActivityForResult(intent, AreaOrganizadorHomeActivity.REQUEST_CODE_EDITAR_EVENTO);
                    }
                });

                holder.btnDeleteEvent.setOnClickListener(v -> {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Confirmar Exclusão")
                            .setMessage("Tem certeza que deseja excluir o evento '" + evento.getNome() + "'?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("Excluir", (dialog, whichButton) -> {
                                RepositorioEventos.removerEvento(evento);
                                notifyDataSetChanged();
                                Toast.makeText(mContext, "Evento excluído.", Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton("Cancelar", null).show();
                });

            } else {
                holder.btnEditEvent.setVisibility(View.GONE);
                holder.btnDeleteEvent.setVisibility(View.GONE);
            }

        } else {
            // Limpar campos se evento for nulo
        }

        convertView.setOnClickListener(v -> {
            if (evento != null) {
                Intent intent = new Intent(mContext, DetalhesEventoActivity.class);
                intent.putExtra("EVENTO", evento);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView tvEventName, tvEventDateTime, tvEventLocation, tvEventType, tvEventOrganizer;
        ImageView imageView;
        Button btnEditEvent, btnDeleteEvent;
    }
}
