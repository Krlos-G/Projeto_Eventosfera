package com.example.projeto;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImagemAdapter extends BaseAdapter {
    private Context context;
    private List<String> imageUris;
    private LayoutInflater inflater;
    private static final String TAG = "ImagemAdapter(GridView)";

    public ImagemAdapter(Context context, List<String> imageUris) {
        this.context = context;
        this.imageUris = imageUris;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return imageUris != null ? imageUris.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return imageUris != null ? imageUris.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_image, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.imageViewItem);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final String imageUriString = imageUris.get(position);
        Log.d(TAG, "Posição: " + position + ", Tentando carregar imagem para GridView: " + imageUriString);

        if (imageUriString != null && !imageUriString.isEmpty()) {
            holder.imageView.setImageDrawable(null); // Limpa imagem anterior antes de carregar nova
            Picasso.get()
                    .load(imageUriString)
                    // .placeholder(R.drawable.download) // Removido
                    // .error(R.drawable.download)       // Removido, mas o callback de erro ainda funciona
                    .fit()
                    .centerCrop()
                    .into(holder.imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "Picasso onSuccess: Imagem carregada com sucesso -> " + imageUriString);
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e(TAG, "Picasso onError: Falha ao carregar imagem -> " + imageUriString, e);
                            // O ImageView ficará como estava (provavelmente vazio devido ao setImageDrawable(null) acima)
                            // ou como o Picasso lida com erro sem um .error() drawable.
                        }
                    });
        } else {
            Log.w(TAG, "URI da imagem (Detalhes) é nula ou vazia na posição: " + position);
            holder.imageView.setImageDrawable(null); // Garante que fique vazio
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
    }
}