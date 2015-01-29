package com.zelory.ugmkepo;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.nhaarman.listviewanimations.appearance.simple.ScaleInAnimationAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements SearchView.OnQueryTextListener
{
    SearchView searchView;
    ListView listView;
    ArrayList<Mahasiswa> mahasiswaArrayList = new ArrayList<>();
    MahasiswaAdapter mahasiswaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint("Masukan nama atau niu");
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.search)
        {
            searchView.onActionViewExpanded();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        new CariData().execute(searchView.getQuery().toString());
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
        return false;
    }

    private class CariData extends AsyncTask<String, Void, Void>
    {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Mencari data...");
            progressDialog.show();
            mahasiswaArrayList.clear();
        }

        @Override
        protected Void doInBackground(String... keyword)
        {
            Document document = null;
            Elements elements;
            Mahasiswa mahasiswa = null;
            try
            {
                document = Jsoup
                        .connect(
                                "http://akademik.ugm.ac.id/2013/home.php?ma=profil&ms=mhs_profile#")
                        .data("key", keyword[0]).data("ma", "profil")
                        .data("ms", "mhs_profile").data("cari", "cari").post();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            if (document != null)
            {
                elements = document.select("input[type$=hidden]");
                elements.remove(0);
                elements.remove(0);

                for (int i = 0; i < elements.size(); i++)
                {
                    Element element = elements.get(i);

                    if (i % 3 == 0)
                    {
                        mahasiswa = new Mahasiswa();
                        mahasiswa
                                .setNiu(Integer.parseInt(element.attr("value")));
                    }
                    else if (i % 3 == 1)
                    {
                        assert mahasiswa != null;
                        mahasiswa.setNama(element.attr("value"));
                    }
                    else if (i % 3 == 2)
                    {
                        assert mahasiswa != null;
                        mahasiswa.setAngkatan(Integer.parseInt(element
                                .attr("value")));
                        mahasiswaArrayList.add(mahasiswa);
                    }
                }

                elements = document.getElementsByTag("td");
                int j = 0;
                for (int i = 1; i < elements.size(); i += 3)
                {
                    Element e = elements.get(i);
                    mahasiswaArrayList.get(j).setProdi(e.text());
                    j++;
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            mahasiswaAdapter = new MahasiswaAdapter(MainActivity.this, mahasiswaArrayList);
            ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(mahasiswaAdapter);
            scaleInAnimationAdapter.setAbsListView(listView);
            listView.setAdapter(scaleInAnimationAdapter);
            progressDialog.dismiss();
        }
    }
}
