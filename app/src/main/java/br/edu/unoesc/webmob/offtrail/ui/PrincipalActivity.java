package br.edu.unoesc.webmob.offtrail.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.edu.unoesc.webmob.offtrail.R;
import br.edu.unoesc.webmob.offtrail.adapter.TrilheiroAdapter;
import br.edu.unoesc.webmob.offtrail.helper.DatabaseHelper;
import br.edu.unoesc.webmob.offtrail.model.Cidade;
import br.edu.unoesc.webmob.offtrail.model.Trilheiro;
import br.edu.unoesc.webmob.offtrail.model.Usuario;
import br.edu.unoesc.webmob.offtrail.rest.CidadeClient;
import br.edu.unoesc.webmob.offtrail.rest.CidadeClient_;
import br.edu.unoesc.webmob.offtrail.rest.Endereco;

@EActivity(R.layout.activity_principal)
@Fullscreen
@WindowFeature(Window.FEATURE_NO_TITLE)
public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @ViewById
    ListView lstTrilheiros;
    @Bean
    TrilheiroAdapter trilheiroAdapter;
    // injeção das preferências
    @Pref
    static Configuracao_ configuracao;
    // injeção do cliente rest
    @RestService
    CidadeClient cidadeClient;
    @Bean
    DatabaseHelper dh;

    ProgressDialog pd;
    View viewRoot;

    @AfterViews
    public void inicializar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabCadastroTrilheiro);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itCadastroTrilheiros = new Intent(PrincipalActivity.this, CadastroTrilheirosActivity_.class);
                startActivity(itCadastroTrilheiros);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // recuperar dados do usuário
        Usuario u = (Usuario) getIntent().getSerializableExtra("usuario");
        Toast.makeText(this, "Seja bem-vindo, " + u.getLogin() + "!", Toast.LENGTH_LONG).show();

        // mudando a cor de fundo da view principal
        viewRoot = toolbar.getRootView();
        // setando a cor de fundo da activity
        viewRoot.setBackgroundColor(configuracao.cor().get());

//        Toast.makeText(this, configuracao.parametro().get(), Toast.LENGTH_LONG).show();
//        // escrevendo parâmetros
//        configuracao.edit().cor().put(Color.BLUE).apply();

        atualizarListaTrilheiros();
    }

    @Override
    public void onResume() {
        super.onResume();
        //TODO: (1,00) Implementar atualização automática da lista de trilheiros ao sair da tela de cadastro do trilheiro.
        atualizarListaTrilheiros();

        // setando a cor de fundo da activity através da preferência escolhida
        viewRoot.setBackgroundColor(configuracao.cor().get());
    }

    public void atualizarListaTrilheiros() {
        //TODO: (1,00) Implementar a ordenação pelo nome do trilheiro de forma descendente.
        // tarefa resolvida no método initAdapter na classe TrilheiroAdapter usando Collections.sort
        trilheiroAdapter.attTrilheiros();
        lstTrilheiros.setAdapter(trilheiroAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sobre) {
            //TODO: (0,75) Implementar uma tela de sobre o sistema com informações gerais.
            Intent itSobre = new Intent(PrincipalActivity.this, SobreActivity_.class);
            startActivity(itSobre);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_sincronizar) {
            pd = new ProgressDialog(this);
            pd.setCancelable(false);
            pd.setTitle("Aguarde, consultando ...");
            pd.setIndeterminate(true);
            pd.show();
            consultarCidadePorNome();
        } else if (id == R.id.nav_preferencias) {
            //TODO: (0,75) Implementar tela para salvar preferências.
            Intent itPreferencias = new Intent(PrincipalActivity.this, PreferenciasActivity_.class);
            startActivity(itPreferencias);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    @UiThread
//    public void mostrarResultado(String resultado) {
//        pd.dismiss();
//        Toast.makeText(this, resultado, Toast.LENGTH_LONG).show();
//    }

    @UiThread
    public void cadastrarCidades(List<Endereco> e) {
        pd.dismiss();
        Cidade c = null;
        try {
            for (Endereco endereco : e) {
                c = dh.selectCidadeByNome(endereco.getLocalidade());
                // só cadastrar se não existir na base
                if (c == null) {
                    c = new Cidade();
                    c.setNome(endereco.getLocalidade());
                    dh.getCidadeDao().create(c);
                }
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        // mostrar as cidades cadastradas no Logcat
        try {
            List<Cidade> cidades = dh.getCidadeDao().queryForAll();
            for (Cidade cidade : cidades) {
                Log.e("Cidade", cidade.getNome());
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    @Background(delay = 2000)
    public void consultarCidadePorNome() {
        //TODO: (1,50) Implementar a busca de todas as cidades que começam com "São" e gravar na tabela cidade.
        // aciona a busca
        List<Endereco> e = cidadeClient.getEndereco("São");
        if (e != null && e.size() > 0) {
            //mostrarResultado(e.get(0).toString());
            cadastrarCidades(e);
        }
    }
}
