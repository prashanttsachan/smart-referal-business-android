package com.example.ylifebsb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class detail_transaction extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaction);

        Intent i = getIntent();
        ImageView img = (ImageView) findViewById(R.id.backButtonDetail);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        transaction_Data td = (transaction_Data) i.getSerializableExtra("transaction_data");
        TextView amount = (TextView) findViewById(R.id.AmountdetailTransaction);
        TextView type = (TextView) findViewById(R.id.typeDetailTransaction);
        TextView created = (TextView) findViewById(R.id.createdTimeDetailTransaction);
        TextView updated = (TextView) findViewById(R.id.UpdatedDetailTransaction);
        TextView member = (TextView) findViewById(R.id.memberDetailTransaction);
        TextView id = (TextView) findViewById(R.id.idDetailTransaction);
        TextView remarks = (TextView) findViewById(R.id.RemarksDetailTransaction);
        ImageView statusimg = (ImageView) findViewById(R.id.statusImageViewdetailTransaction);
        if(td.type.equals("CREDIT")){
            type.setText("CREDITED");
            amount.setText(td.amount+" INR");
        }else{
            type.setText("DEBITED");
            amount.setText(td.amount+" INR");
        }

        if((td.status).equals("SUCCESS")){
            statusimg.setBackgroundResource(R.drawable.right2);
        }
        else if((td.status).equals("DECLINED")) {
            statusimg.setBackgroundResource(R.drawable.wrong2);
        }
        else {
            statusimg.setBackgroundResource(R.drawable.exclamation);
        }
        created.setText(td.createdAt);
        updated.setText(td.updatedAt);
        member.setText(td.member);
        id.setText(td.id);
        remarks.setText(td.remarks);


    }
}