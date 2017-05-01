package project.sudden.bookinglapang.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.lang.reflect.AccessibleObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import project.sudden.bookinglapang.BaseActivity;
import project.sudden.bookinglapang.R;
import project.sudden.bookinglapang.model.Lapangan;

/**
 * Created by Lotus on 16/04/2017.
 */

public class DialogActivity extends BaseActivity {

    public Spinner spinner;
    public ArrayList<String> subLapangan = new ArrayList<>();
    List<String> statusLapangan = new ArrayList<>();
    List<String> hargaLapangan = new ArrayList<>();
    ArrayList<String> jamLapangan= new ArrayList<>();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();

    TextView data1, data2, data3, data4, data5, data6, data7, data8;
    TextView data9, data10, data11, data12, data13, data14, data15, data16;
    TextView total;

    String item;
    String cabangOlahraga;
    String tempatPilihan;
    TextView desc;
    TextView tanggal;
    Button besok;
    Button processBook;
    String hari;
    Integer totalHarga = 0;
    String subLapanganFix;
    String hariJadwal;
    String namaPemesan;

    int press1 = 0, press2 = 0, press3 = 0, press4 = 0, press5 = 0, press6 = 0, press7 = 0, press8 = 0;
    int press9 = 0, press10 = 0, press11 = 0, press12 = 0, press13 = 0, press14 = 0, press15 = 0, press16 = 0;

    ValueEventListener searchSubLapangan;
    ValueEventListener searchDescription;
    ValueEventListener searchPrice;
    ArrayAdapter<String> spinnerArrayAdapter;

    Calendar calendar;
    Date today;
    Date tomorrow;
    DateFormat dateFormat;
    DateFormat dateFormat2;
    int tambahHari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_fragment);

        spinner = (Spinner) findViewById(R.id.spinner);

        TableLayout layout = (TableLayout) findViewById(R.id.tableLayout);
        desc = (TextView) findViewById(R.id.descText);
        tanggal = (TextView) findViewById(R.id.tanggal);
        data1 = (TextView) findViewById(R.id.data1);
        data2 = (TextView) findViewById(R.id.data2);
        data3 = (TextView) findViewById(R.id.data3);
        data4 = (TextView) findViewById(R.id.data4);
        data5 = (TextView) findViewById(R.id.data5);
        data6 = (TextView) findViewById(R.id.data6);
        data7 = (TextView) findViewById(R.id.data7);
        data8 = (TextView) findViewById(R.id.data8);
        data9 = (TextView) findViewById(R.id.data9);
        data10 = (TextView) findViewById(R.id.data10);
        data11 = (TextView) findViewById(R.id.data11);
        data12 = (TextView) findViewById(R.id.data12);
        data13 = (TextView) findViewById(R.id.data13);
        data14 = (TextView) findViewById(R.id.data14);
        data15 = (TextView) findViewById(R.id.data15);
        data16 = (TextView) findViewById(R.id.data16);
        total = (TextView) findViewById(R.id.total);
        besok = (Button) findViewById(R.id.besok);
        processBook = (Button) findViewById(R.id.processbook);
        processBook.setEnabled(false);

        subLapangan = this.getIntent().getStringArrayListExtra("subLapangan");
        cabangOlahraga = this.getIntent().getStringExtra("cabangOlahraga");
        tempatPilihan = this.getIntent().getStringExtra("tempatPilihan");
        namaPemesan = this.getIntent().getStringExtra("namaPemesan");

        spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subLapangan);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinnerArrayAdapter.notifyDataSetChanged();

        tambahHari = 0;

        calendar = Calendar.getInstance();
        today = calendar.getTime();
        dateFormat = new SimpleDateFormat("EEEE");
        dateFormat2 = new SimpleDateFormat("dd-MMM-yyyy");
        hari = dateFormat.format(today);
        tanggal.setText(hari + " " + dateFormat2.format(today));

        checkHarga();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                item = parentView.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();
                statusLapangan.clear();
                hargaLapangan.clear();
                totalHarga = 0;
                total.setText("jumlah: " + totalHarga);
                press1 = 0; press2 = 0; press3 = 0; press4 = 0; press5 = 0; press6 = 0; press7 = 0; press8 = 0;
                press9 = 0; press10 = 0; press11 = 0; press12 = 0; press13 = 0; press14 = 0; press15 = 0; press16 = 0;

                besok.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View arg0) {

                         totalHarga = 0;
                         total.setText("jumlah: " + totalHarga);
                         press1 = 0; press2 = 0; press3 = 0; press4 = 0; press5 = 0; press6 = 0; press7 = 0; press8 = 0;
                         press9 = 0; press10 = 0; press11 = 0; press12 = 0; press13 = 0; press14 = 0; press15 = 0; press16 = 0;
                         jamLapangan.clear();
                         calendar.add(Calendar.DAY_OF_YEAR, 1);
                         tomorrow = calendar.getTime();
                         hari = dateFormat.format(tomorrow);
                         Log.d("TAGES", hari + " " + dateFormat2.format(tomorrow));
                         tanggal.setText(hari + " " + dateFormat2.format(tomorrow));

                         checkDatabase(item, hari);

                     }
                 });

                checkDatabase(item, hari);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        processBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Intent intent =new Intent(DialogActivity.this, ConfirmBooking.class);
                intent.putExtra("namaPemesan", namaPemesan);
                intent.putExtra("subLapangan", subLapanganFix);
                intent.putStringArrayListExtra("jadwalPesanan", jamLapangan);
                intent.putExtra("lapanganPesanan", tempatPilihan);
                intent.putExtra("totalHarga", String.valueOf(totalHarga));
                intent.putExtra("hariDipesan", hari);
                intent.putExtra("cabangOlahraga", cabangOlahraga);
                startActivity(intent);
            }
        });
    }

    public void checkDatabase(String sublapangan, String hari) {

        searchDescription = new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    //Log.d("TAGES", "GetChildren: "+ eventSnapshot.getKey() +" "+ eventSnapshot.getValue());
                    desc.setText(eventSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Handle possible errors.
            }
        };

        searchPrice = new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                hargaLapangan.clear();
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    //Log.d("TAGES", eventSnapshot.getValue().toString());
                    hargaLapangan.add(eventSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Handle possible errors.
            }
        };

        subLapanganFix = sublapangan;
        hariJadwal = hari;

        searchSubLapangan = new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                statusLapangan.clear();
                Log.d("TAGES", snapshot.toString());
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    //Log.d("TAGES", "GetChildren: "+ eventSnapshot.getKey() +" "+ eventSnapshot.getValue());
                    if (eventSnapshot.getValue() instanceof String) {
                        statusLapangan.add(eventSnapshot.getValue().toString());
                    }
                }

                for (int i = 0; i < statusLapangan.size(); i++)
                    Log.d("TAGES", "status: " + i + " " + item + " " + statusLapangan.get(i));


                for (int j = 0; j <= 15; j++) {
                    if (j == 0)
                        checkStatus(data1, statusLapangan.get(j).toString());
                    else if (j == 1)
                        checkStatus(data2, statusLapangan.get(j).toString());
                    else if (j == 2)
                        checkStatus(data3, statusLapangan.get(j).toString());
                    else if (j == 3)
                        checkStatus(data4, statusLapangan.get(j).toString());
                    else if (j == 4)
                        checkStatus(data5, statusLapangan.get(j).toString());
                    else if (j == 5)
                        checkStatus(data6, statusLapangan.get(j).toString());
                    else if (j == 6)
                        checkStatus(data7, statusLapangan.get(j).toString());
                    else if (j == 7)
                        checkStatus(data8, statusLapangan.get(j).toString());
                    else if (j == 8)
                        checkStatus(data9, statusLapangan.get(j).toString());
                    else if (j == 9)
                        checkStatus(data10, statusLapangan.get(j).toString());
                    else if (j == 10)
                        checkStatus(data11, statusLapangan.get(j).toString());
                    else if (j == 11)
                        checkStatus(data12, statusLapangan.get(j).toString());
                    else if (j == 12)
                        checkStatus(data13, statusLapangan.get(j).toString());
                    else if (j == 13)
                        checkStatus(data14, statusLapangan.get(j).toString());
                    else if (j == 14)
                        checkStatus(data15, statusLapangan.get(j).toString());
                    else
                        checkStatus(data16, statusLapangan.get(j).toString());

                    myRef.child("lapangan").child(cabangOlahraga).child(tempatPilihan).child("sublapangan").child(subLapanganFix).child(hariJadwal).removeEventListener(searchSubLapangan);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        };
        myRef.child("lapangan").child(cabangOlahraga).child(tempatPilihan).child("sublapangan").child(sublapangan).child(hariJadwal).addValueEventListener(searchSubLapangan);
        myRef.child("lapangan").child(cabangOlahraga).child(tempatPilihan).child("sublapangan").child(sublapangan).orderByKey().
                startAt("description").endAt("description").limitToLast(1).addValueEventListener(searchDescription);
        myRef.child("lapangan").child(cabangOlahraga).child(tempatPilihan).child("sublapangan").child(sublapangan).orderByKey().
                startAt("6").endAt("21").limitToLast(16).addValueEventListener(searchPrice);
        //myRef.removeEventListener(aaa);
    }

    public void checkStatus(TextView tv, String status){
        if (status.equals("open")) {
            tv.setBackgroundColor(Color.parseColor("#FFFFFF"));
            tv.setClickable(true);
        }
        else if (status.equals("process")) {
            tv.setBackgroundColor(Color.parseColor("#FFFF00"));
            tv.setClickable(false);
        }
        else if (status.equals("booked")) {
            tv.setBackgroundColor(Color.parseColor("#FF0000"));
            tv.setClickable(false);
        }
        else {
            tv.setBackgroundColor(Color.parseColor("#000000"));
            tv.setClickable(false);
        }
    }

    public void hitungHarga(int press, String harga, String jam, TextView tv){
        if (press%2 == 1){
            totalHarga += Integer.parseInt(harga);
            jamLapangan.add(jam);
            tv.setBackgroundColor(Color.parseColor("#E3DAC9"));
        }
        else {
            totalHarga -= Integer.parseInt(harga);
            jamLapangan.remove(jam);
            tv.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        total.setText("jumlah: " + totalHarga);
        if (totalHarga > 0) {
            processBook.setBackgroundColor(Color.parseColor("#FF33B5E5"));
            processBook.setEnabled(true);
        }
        else {
            processBook.setBackgroundColor(Color.parseColor("#D3D3D3"));
            processBook.setEnabled(false);
        }
    }

    public void checkHarga(){
        data1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                press1++;
                hitungHarga (press1, hargaLapangan.get(0), "06.00", data1);
            }});
        data2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                press2++;
                hitungHarga (press2, hargaLapangan.get(1), "07.00", data2);
            }});
        data3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                press3++;
                hitungHarga (press3, hargaLapangan.get(2), "08.00", data3);
            }});
        data4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                press4++;
                hitungHarga (press4, hargaLapangan.get(3), "09.00", data4);
            }});
        data5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                press5++;
                hitungHarga (press5, hargaLapangan.get(4), "10.00", data5);
            }});
        data6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                press6++;
                hitungHarga (press6, hargaLapangan.get(5), "11.00", data6);
            }});
        data7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                press7++;
                hitungHarga (press7, hargaLapangan.get(6), "12.00", data7);
            }});
        data8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                press8++;
                hitungHarga (press8, hargaLapangan.get(7), "13.00", data8);
            }});
        data9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                press9++;
                hitungHarga (press9, hargaLapangan.get(8), "14.00", data9);
            }});
        data10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                press10++;
                hitungHarga (press10, hargaLapangan.get(9), "15.00", data10);
            }});
        data11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                press11++;
                hitungHarga (press11, hargaLapangan.get(10), "16.00", data11);
            }});
        data12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                press12++;
                hitungHarga (press12, hargaLapangan.get(11), "17.00", data12);
            }});
        data13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                press13++;
                hitungHarga (press13, hargaLapangan.get(12), "18.00", data13);
            }});
        data14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                press14++;
                hitungHarga (press14, hargaLapangan.get(13), "19.00", data14);
            }});
        data15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                press15++;
                hitungHarga (press15, hargaLapangan.get(14), "20.00", data15);
            }});
        data16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                press16++;
                hitungHarga (press16, hargaLapangan.get(15), "21.00", data16);
            }});
    }
}