package ntp.example.e123.hoctuvung;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import ntp.example.e123.R;

public class DSTuVungAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<TuVung> dstuvungs;

    public DSTuVungAdapter(Context context, int layout, List<TuVung> dstuvungs) {
        this.context = context;
        this.layout = layout;
        this.dstuvungs = dstuvungs;
    }


    @Override
    public int getCount() {
        return dstuvungs.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        ImageView imgHinh;
        TextView twDichNghia;
        TextView twTuVung;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            holder.imgHinh = (ImageView) convertView.findViewById(R.id.imgHinh);
            holder.twDichNghia = (TextView) convertView.findViewById(R.id.twDichNghia);
            holder.twTuVung = (TextView) convertView.findViewById(R.id.twTuVung);



            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        TuVung tuVung = dstuvungs.get(position);
        holder.twDichNghia.setText(tuVung.getDichnghia());
        holder.twTuVung.setText(tuVung.getTu() +"("+tuVung.getLoaitu()+"):");
        Glide.with(context)
                .load(tuVung.getAnh())
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.client)
                .into(holder.imgHinh);
        return convertView;


    }
}

