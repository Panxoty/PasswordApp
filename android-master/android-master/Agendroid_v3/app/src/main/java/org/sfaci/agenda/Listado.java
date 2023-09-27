package org.sfaci.agenda;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.sfaci.agenda.adapters.AmigoAdapter;
import org.sfaci.agenda.base.Amigo;

import java.util.List;


public class Listado extends Activity {

    private AmigoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);

        ListView lvAmigos = (ListView) findViewById(R.id.lvAmigos);
        adapter =
                new AmigoAdapter(this,MainActivity.listaAmigos);
        lvAmigos.setAdapter(adapter);

        registerForContextMenu(lvAmigos);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_listado, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_registrar:
                Intent intent = new Intent(this, Registro.class);
                startActivity(intent);
                break;
            default:
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.menu_context_listado, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterContextMenuInfo info =
                (AdapterContextMenuInfo) item.getMenuInfo();
        final int itemSeleccionado = info.position;
        Amigo amigo = null;

        switch (item.getItemId()) {
            case R.id.action_fijo:
                amigo = MainActivity.listaAmigos.get(itemSeleccionado);
                String telefonoFijo = amigo.getTelefonoFijo();
                Intent intentLlamada = new Intent(Intent.ACTION_CALL);
                intentLlamada.setData(Uri.parse("tel: " + telefonoFijo));
                startActivity(intentLlamada);
                break;
            case R.id.action_movil:
                amigo = MainActivity.listaAmigos.get(itemSeleccionado);
                String telefonoMovil = amigo.getTelefonoMovil();
                Intent intentMovil = new Intent(Intent.ACTION_CALL);
                intentMovil.setData(Uri.parse("tel: " + telefonoMovil));
                startActivity(intentMovil);
                break;
            case R.id.action_editar:
                Intent intent = new Intent(this, Registro.class);
                intent.putExtra("posicion", itemSeleccionado);
                startActivity(intent);
                break;
            case R.id.action_eliminar:
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(this);
                builder.setMessage(R.string.lb_esta_seguro)
                        .setPositiveButton(R.string.lb_si,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        MainActivity.listaAmigos.remove(itemSeleccionado);
                                        adapter.notifyDataSetChanged();
                                        Toast.makeText(Listado.this, R.string.lb_eliminado, Toast.LENGTH_LONG).show();
                                    }
                                })
                        .setNegativeButton(R.string.lb_si,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                builder.create().show();
                break;
            case R.id.action_email:
                amigo = MainActivity.listaAmigos.get(itemSeleccionado);
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, amigo.getEmail());
                emailIntent.setType("message/rfc822");
                startActivity(Intent.createChooser(emailIntent, getString(R.string.selecciona_email)));
                break;
            case R.id.action_detalles:
                Intent intentDetalles = new Intent(this, Detalles.class);
                intentDetalles.putExtra("posicion", itemSeleccionado);
                startActivity(intentDetalles);

                Notification.Builder nBuilder = new Notification.Builder(this)
                        .setSmallIcon(android.R.drawable.ic_notification_overlay)
                        .setContentTitle("Prueba")
                        .setContentText("Esto es una notificacion");
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(0, nBuilder.build());
                break;
            default:
                break;
        }

        return false;
    }
}
