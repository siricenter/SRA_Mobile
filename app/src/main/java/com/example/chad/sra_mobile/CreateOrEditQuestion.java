package com.example.chad.sra_mobile;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class CreateOrEditQuestion extends Activity {

    private TableLayout dataPointTable;
    private Dialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_edit_question);

        dataPointTable = (TableLayout) findViewById(R.id.data_point_table);
        dataPointTable.setStretchAllColumns(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_or_edit_question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addDataPoint(View view) {
        addDataPoint();
    }

    public void addDataPoint() {
        alert = new Dialog(this);
        alert.setContentView(R.layout.new_data_point);
        alert.setCancelable(false);
        alert.setTitle("Data point");

        Button save = (Button) alert.findViewById(R.id.save_button);
        Button cancel = (Button) alert.findViewById(R.id.cancel_button);

        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText label = (EditText) alert.findViewById(R.id.label_field);
                addDataPointRow(label.getText().toString(), "");
                alert.dismiss();
            }
        });
        alert.show();
    }

    public void addDataPointRow(String label, String type) {
        final TableRow row = new TableRow(this);

        TextView tv = new TextView(this);
        tv.setText(label);

        Button edit = new Button(this);
        edit.setText("Edit");

        Button delete = new Button(this);
        delete.setText("Delete");

        row.addView(tv);
        row.addView(edit);
        row.addView(delete);

        dataPointTable.addView(row);
    }
}
