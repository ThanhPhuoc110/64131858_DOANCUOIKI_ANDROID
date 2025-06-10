package ntp.example.e123.admin.dienkhuyet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import ntp.example.e123.R;
import ntp.example.e123.dienkhuyet.CauDienKhuyet;

public class QLDienKhuyetAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CauDienKhuyet> list;
    private DatabaseReference dienKhuyetRef;
    public QLDienKhuyetAdapter(Context context, ArrayList<CauDienKhuyet> list){
        this.context=context;
        this.list=list;
        dienKhuyetRef = FirebaseDatabase.getInstance().getReference("DienKhuyet");
    }
    @Override
    public int getCount() {
        return list.size();
    }
    public Object getItem(int position) {
        return list.get(position);
    }
    public long getItemId(int position) {
        return position;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_dienkhuyet, null);

        TextView txtTenDienKhuyet = view.findViewById(R.id.tvTenDienKhuyet);
        ImageView imgEdit = view.findViewById(R.id.imgEditDK);
        ImageView imgDelete = view.findViewById(R.id.imgDeleteDK);

        CauDienKhuyet dk = list.get(position);
        txtTenDienKhuyet.setText(dk.getNoiDung());

        imgEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditDienKhuyetActivity.class);
            intent.putExtra("ID_DK", dk.getIdCau());
            intent.putExtra("ID_Bo", dk.getIdBo());
            context.startActivity(intent);
        });

        imgDelete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Xác nhận xóa");
            builder.setMessage("Bạn chắc chắn muốn xóa câu điền khuyết này?");
            builder.setPositiveButton("Có", (dialog, which) -> deleteDienKhuyet(dk.getIdCau()));
            builder.setNegativeButton("Không", (dialog, which) -> {});
            builder.create().show();
        });

        return view;
    }
    private void deleteDienKhuyet(String idCau) {
        dienKhuyetRef.child(idCau).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                list.removeIf(dk -> dk.getIdCau().equals(idCau)); // Cập nhật danh sách
                notifyDataSetChanged();
            } else {
                Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
