package com.zelory.ugmkepo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by zetbaitsu on 29/01/2015.
 */
public class MahasiswaAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<Mahasiswa> mahasiswaArrayList;
    
    public MahasiswaAdapter(Context context, ArrayList<Mahasiswa> mahasiswaArrayList)
    {
        this.context = context;
        this.mahasiswaArrayList = mahasiswaArrayList;
    }
    @Override
    public int getCount()
    {
        return mahasiswaArrayList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mahasiswaArrayList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View itemView;
        ViewHolder holder;
        Mahasiswa mahasiswa = mahasiswaArrayList.get(position);
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.item_list_layout,parent,false);
            holder = new ViewHolder();
            holder.niu = (TextView) itemView.findViewById(R.id.niu);
            holder.nama = (TextView) itemView.findViewById(R.id.nama);
            holder.prodi = (TextView) itemView.findViewById(R.id.prodi);
            holder.angkatan = (TextView) itemView.findViewById(R.id.angkatan);
            itemView.setTag(holder);
        }
        else
        {
            itemView = convertView;
            holder = (ViewHolder) itemView.getTag();
        }
        
        holder.niu.setText("NIU\t\t\t\t:\t"+mahasiswa.getNiu()+"");
        holder.nama.setText("Nama\t\t\t:\t"+mahasiswa.getNama());
        holder.prodi.setText("Prodi\t\t\t:\t"+mahasiswa.getProdi());
        holder.angkatan.setText("Angkatan\t:\t"+mahasiswa.getAngkatan()+"");
        
        return itemView;
    }
    
    private class ViewHolder
    {
        TextView niu,nama,prodi,angkatan;
    }
}
