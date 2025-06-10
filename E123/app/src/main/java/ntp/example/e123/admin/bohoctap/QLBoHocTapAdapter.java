package ntp.example.e123.admin.bohoctap;

import android.app.AlertDialog;
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

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

import ntp.example.e123.R;
import ntp.example.e123.bohoctap.BoHocTap;

public class QLBoHocTapAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<BoHocTap> list;
    private DatabaseReference databaseRef;

    public QLBoHocTapAdapter(Context context, ArrayList<BoHocTap> list) {
        this.context = context;
        this.list = list;
        databaseRef = FirebaseDatabase.getInstance().getReference();
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
        View view = inflater.inflate(R.layout.row_bohoctap, parent, false);

        TextView txtTenBo = view.findViewById(R.id.tvTenBo);
        ImageView imgEdit = view.findViewById(R.id.imgEditBHT);
        ImageView imgDelete = view.findViewById(R.id.imgDeleteBHT);

        BoHocTap bht = list.get(position);
        txtTenBo.setText(bht.getTenBo());

        imgEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditBoHocTapActivity.class);
            intent.putExtra("ID_BHT", bht.getIdBo());
            context.startActivity(intent);
        });

        imgDelete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Xác nhận xóa");
            builder.setMessage("Bạn chắc chắn muốn xóa bộ học tập này?");
            builder.setPositiveButton("Có", (dialog, which) -> {
                deleteBoHocTap(bht.getIdBo());
            });
            builder.setNegativeButton("Không", (dialog, which) -> {});
            builder.create().show();
        });

        return view;
    }

    private void deleteBoHocTap(String idBo) {
        DatabaseReference boHocTapRef = databaseRef.child("BoHocTap").child(idBo);

        // Xóa dữ liệu liên quan
        databaseRef.child("DienKhuyet").orderByChild("idBo").equalTo(idBo).addListenerForSingleValueEvent(deleteDataListener());

        databaseRef.child("TracNghiem").orderByChild("idBo").equalTo(idBo).addListenerForSingleValueEvent(deleteDataListener());
        databaseRef.child("TuVung").orderByChild("idBo").equalTo(idBo).addListenerForSingleValueEvent(deleteDataListener());

        // Xóa bộ học tập
        boHocTapRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ValueEventListener deleteDataListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    data.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };
    }
}
