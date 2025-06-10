package ntp.example.e123.admin.tracnghiem;

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

import java.util.ArrayList;

import ntp.example.e123.R;
import ntp.example.e123.tracnghiem.TracNghiem;


public class QLTracNghiemAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<TracNghiem> list;
    private DatabaseReference tracNghiemRef;

    public QLTracNghiemAdapter(Context context, ArrayList<TracNghiem> list) {
        this.context = context;
        this.list = list;
        tracNghiemRef = FirebaseDatabase.getInstance().getReference("TracNghiem");
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
        View view = inflater.inflate(R.layout.row_tracnghiem, parent, false);

        TextView txtTenTracNghiem = view.findViewById(R.id.tvTenTracNghiem);
        ImageView imgEdit = view.findViewById(R.id.imgEditTN);
        ImageView imgDelete = view.findViewById(R.id.imgDeleteTN);

        TracNghiem tn = list.get(position);
        txtTenTracNghiem.setText(tn.getNoiDung());

        imgEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditTracNghiemActivity.class);
            intent.putExtra("ID_TN", tn.getIdCau());
            intent.putExtra("idBoHocTap", tn.getIdBoHocTap()); // Truyền ID bộ học tập
            context.startActivity(intent);
        });

        imgDelete.setOnClickListener(v -> showDeleteDialog(tn.getIdCau()));

        return view;
    }

    private void showDeleteDialog(String idCau) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa")
                .setMessage("Bạn chắc chắn muốn xóa câu trắc nghiệm này?")
                .setPositiveButton("Có", (dialog, which) -> deleteTracNghiem(idCau))
                .setNegativeButton("Không", null)
                .show();
    }

    private void deleteTracNghiem(String idCau) {
        tracNghiemRef.child(idCau).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                Toast.makeText(context, "Xóa thành công!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
