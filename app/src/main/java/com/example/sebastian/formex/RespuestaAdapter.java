package com.example.sebastian.formex;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RespuestaAdapter extends RecyclerView.Adapter {
    private List<Respuesta> respuestas;

    public RespuestaAdapter(List<Respuesta> respuestas){
        this.respuestas = respuestas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.respuesta_layout, parent, false);
        return new RespuestaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RespuestaViewHolder respuestaViewHolder = (RespuestaViewHolder)holder;
        respuestaViewHolder.bindRespuesta(respuestas.get(position));
    }

    public class RespuestaViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPregunta;
        private TextView tvRespuesta;

        public RespuestaViewHolder (View itemView) {
            super(itemView);
            tvPregunta = itemView.findViewById(R.id.rl_tv_pregunta);
            tvRespuesta = itemView.findViewById(R.id.rl_tv_respuesta);
        }

        public void bindRespuesta(Respuesta respuesta){
            tvPregunta.setText(respuesta.getPregunta());
            tvRespuesta.setText(respuesta.getOpcion());
        }
    }

    @Override
    public int getItemCount() {
        if(respuestas != null)
            return respuestas.size();
        return -1;
    }
}


