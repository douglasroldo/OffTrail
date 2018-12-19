package br.edu.unoesc.webmob.offtrail.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.sql.SQLException;
import java.util.List;

import br.edu.unoesc.webmob.offtrail.R;
import br.edu.unoesc.webmob.offtrail.adapter.TrilheiroAdapter;
import br.edu.unoesc.webmob.offtrail.helper.DatabaseHelper;
import br.edu.unoesc.webmob.offtrail.model.GrupoTrilheiro;
import br.edu.unoesc.webmob.offtrail.model.Trilheiro;

@EViewGroup(R.layout.lista_trilheiros)
public class TrilheiroItemView extends LinearLayout {

    @ViewById
    TextView txtNome;

    @ViewById
    TextView txtMoto;

    @ViewById
    ImageView imvFoto;

    @Bean
    DatabaseHelper dh;

    // variável global
    Trilheiro trilheiro;

    public TrilheiroItemView(Context context) {
        super(context);
    }

    @Click(R.id.imvEditar)
    public void editar() {
        //Toast.makeText(getContext(), "Editar: " + trilheiro.getNome(), Toast.LENGTH_LONG).show();
        //TODO: (2,50) Implementar a edição dos dados do trilheiro.
        Intent itCadastroTrilheiro = new Intent(getContext(), CadastroTrilheirosActivity_.class);
        itCadastroTrilheiro.putExtra("trilheiro", trilheiro);
        getContext().startActivity(itCadastroTrilheiro);
    }

    @Click(R.id.imvExcluir)
    public void excluir() {
        //Toast.makeText(getContext(), "Excluir: " + trilheiro.getNome(), Toast.LENGTH_LONG).show();
        AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
        dialogo.setTitle("Exclusão");
        dialogo.setMessage("Deseja realmente excluir ? - " + trilheiro.getNome());
        dialogo.setCancelable(false);
        dialogo.setNegativeButton("Não", null);
        dialogo.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO: (2,50) Implementar a exclusão do trilheiro e grupo trilheiro.
                try {
                    GrupoTrilheiro gt = dh.selectGrupoTrilheiroByTrilheiro(trilheiro);
                    dh.getGrupoTrilheiroDao().delete(gt);
                    dh.getTrilheiroDao().delete(trilheiro);
                    ((PrincipalActivity_) getContext()).onResume();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        dialogo.show();
    }

    public void bind(Trilheiro t) {
        trilheiro = t;
        txtNome.setText(t.getNome());
        txtMoto.setText(t.getMoto().getModelo() + " - " + t.getMoto().getCilindrada());
        imvFoto.setImageBitmap(BitmapFactory.decodeByteArray(t.getFoto(), 0, t.getFoto().length));
    }
}