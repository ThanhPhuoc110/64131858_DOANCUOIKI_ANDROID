package ntp.example.e123.admin.tuvung;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import ntp.example.e123.R;
import ntp.example.e123.hoctuvung.TuVung;

public class QLTuVungAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<TuVung> list;
    private DatabaseReference tuvungRef;

    public QLTuVungAdapter(Context context, ArrayList<TuVung> list) {
        this.context = context;
        this.list = list;
        tuvungRef = FirebaseDatabase.getInstance().getReference("TuVung");
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_tuvung, parent, false);

        TextView txtTenTuVung = view.findViewById(R.id.tvTenTuVung);
        ImageView imgEdit = view.findViewById(R.id.imgEditTV);
        ImageView imgDelete = view.findViewById(R.id.imgDeleteTV);

        TuVung tv = list.get(position);
        txtTenTuVung.setText(tv.getTu());

        imgEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditTuVungActivity.class);
            intent.putExtra("ID_TV", tv.getIdTu());
            intent.putExtra("idBoTuVung", tv.getIdBoHocTap());
            context.startActivity(intent);
        });
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn chắc chắn muốn xóa từ vựng này?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteTuVung(tv.getIdTu());
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });



        return view;
    }

    private void deleteTuVung(String idTu) {
        tuvungRef.child(idTu).removeValue();
        tuvungRef.child(idTu).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
