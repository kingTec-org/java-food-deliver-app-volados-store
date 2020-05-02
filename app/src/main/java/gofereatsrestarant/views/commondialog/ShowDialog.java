package gofereatsrestarant.views.commondialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.obs.CustomTextView;

import gofereatsrestarant.R;
import gofereatsrestarant.views.main.MainActivity;
import gofereatsrestarant.views.main.OrderHistoryDetails;

public class ShowDialog extends Activity {

    private TextView tvMessage;
    private TextView tvOk;
    private int type;
    private int orderId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show_dialog);

        this.setFinishOnTouchOutside(false);

        tvMessage = (CustomTextView) findViewById(R.id.tvMessage);
        tvOk = (TextView) findViewById(R.id.tvOk);

        tvMessage.setText(getIntent().getStringExtra("message"));
        type = getIntent().getIntExtra("type", 0);

        orderId = getIntent().getIntExtra("orderId", 0);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getType();
            }
        });
    }

    private void getType() {
        if (type == 1 || type == 4) {
            Intent order = new Intent(getApplicationContext(), OrderHistoryDetails.class);
            order.putExtra("orderId", orderId);
            order.putExtra("type", 1);
            startActivity(order);
            finish();
           /* Intent order = new Intent(getApplicationContext(), OrderDetails.class);
            order.putExtra("orderId", orderId);
            startActivity(order);
            finish();*/
        } else if (type == 3) {
            Intent order = new Intent(getApplicationContext(), MainActivity.class);
            order.putExtra("type", "order");
            startActivity(order);
            finish();
        }else {
            finish();
        }
    }


    @Override
    public void onBackPressed() {

    }
}
