package project.sudden.bookinglapang.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import project.sudden.bookinglapang.BaseActivity;
import project.sudden.bookinglapang.R;
import project.sudden.bookinglapang.model.DatabaseHandler;
import project.sudden.bookinglapang.model.Lapangan;

/**
 * Created by Lotus on 24/04/2017.
 */

public class ConfirmBooking extends BaseActivity {

    TextView namaPemesanTv;
    TextView subLapanganPesananTv;
    TextView jadwalPesananTv;
    TextView lapanganPesananTv;
    TextView totalHargaTv;
    Button finalProcess;

    String namaUserPesan;
    String subLapangan;
    ArrayList<String> jamLapanganPesanan = new ArrayList<>();
    String lapangan;
    String totalHarga;
    String hariDipesan;
    String cabangOlahraga;
    String formattedDate;
    String jamLapanganTotal="";

    FragmentManager fm = getSupportFragmentManager();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();

    Map<String, Object> done;
    ProgressDialog pdialog;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        namaPemesanTv = (TextView) findViewById(R.id.namaPemesan);
        subLapanganPesananTv = (TextView) findViewById(R.id.subLapanganPesanan);
        jadwalPesananTv = (TextView) findViewById(R.id.jadwalPesanan);
        lapanganPesananTv = (TextView) findViewById(R.id.lapanganPesanan);
        totalHargaTv = (TextView) findViewById(R.id.totalHarga);
        finalProcess = (Button) findViewById(R.id.finalProcess);

        Intent intent = getIntent();
        namaUserPesan = intent.getStringExtra("namaPemesan");
        subLapangan = intent.getStringExtra("subLapangan");
        jamLapanganPesanan = intent.getStringArrayListExtra("jadwalPesanan");
        lapangan = intent.getStringExtra("lapanganPesanan");
        totalHarga = intent.getStringExtra("totalHarga");
        hariDipesan = intent.getStringExtra("hariDipesan");
        cabangOlahraga = intent.getStringExtra("cabangOlahraga");

        final String jamLapanganJoined = TextUtils.join(", ", jamLapanganPesanan);

        namaPemesanTv.setText(":        " + namaUserPesan);
        subLapanganPesananTv.setText(":        " + subLapangan);
        jadwalPesananTv.setText(":        " + hariDipesan + " - " + jamLapanganJoined);
        lapanganPesananTv.setText(":        " + lapangan);
        totalHargaTv.setText(":        " + totalHarga);

        db = new DatabaseHandler(getApplicationContext());
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formattedDate = df.format(c.getTime());

        jamLapanganTotal = TextUtils.join(", ", jamLapanganPesanan);

        finalProcess.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                done = new HashMap<String, Object>();
                for (int i = 0; i<jamLapanganPesanan.size(); i++) {
                    if (jamLapanganPesanan.get(i).equals("06.00") || jamLapanganPesanan.get(i).equals("07.00")
                            ||jamLapanganPesanan.get(i).equals("08.00") || jamLapanganPesanan.get(i).equals("09.00")){
                        done.put(jamLapanganPesanan.get(i).replace("0","").replace(".",""), "process");
                    }
                    else
                        done.put(jamLapanganPesanan.get(i).replace(".00",""), "process");
                }

                pdialog = ProgressDialog.show(ConfirmBooking.this, "", "Booking in progress..", true);

                ConfirmFragment dFragment = new ConfirmFragment();
                // Show DialogFragment
                dFragment.show(fm, "Dialog Fragment");

                RetreiveFeedTask task = new RetreiveFeedTask();
                task.execute();
            }
        });
    }

    class RetreiveFeedTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{

                myRef.child("lapangan").child(cabangOlahraga).child(lapangan).child("sublapangan")
                        .child(subLapangan).child(hariDipesan).updateChildren(done);
                db.addLapangan(new Lapangan(hariDipesan +", "+ jamLapanganTotal, formattedDate, lapangan + " - " + subLapangan));

            } catch(Exception e) {
                Log.d("TAGES", e.toString());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            pdialog.dismiss();
            finish();
        }
    }
}