package com.example.tomek.stanowiskopracy.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;


/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Methods below is called when the order button is clicked.
     */
    public void increment(View view) {
        if (quantity < 100) {
            display(++quantity);
        } else {
            Toast.makeText(this, "You cannot have more than 100 coffee", Toast.LENGTH_LONG).show();
            return;
        }
    }

    public void decrement(View view) {
        if (quantity > 1) {
            display(--quantity);
        } else {
            Toast.makeText(this, "You cannot have less than 1 coffee", Toast.LENGTH_LONG).show();
            return;
        }
    }

    public void submitOrder(View view) {
        // Find the user's name
        EditText customerName = (EditText) findViewById(R.id.name_edit_text);
        String name = customerName.getText().toString();
        // Figure out if the user wants whipped cream topping
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();
        //Figure out if the user wants chocolate topping
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckBox.isChecked();

        int price = calculatePrice(hasWhippedCream, hasChocolate);
        String priceMessage = createOrderSummary(name, price, hasWhippedCream, hasChocolate);

        // Sent the intent to the email app
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_summary_email_subject, name));
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }


    /**
     * Calculates the price of the order based on the current quantity.
     *
     * @param addWhippedCream is whether or not the user wants whipped cream topping
     * @param addChocolate    is whether or not the user wants chocolate topping
     * @return the price
     */
    private int calculatePrice(boolean addWhippedCream, boolean addChocolate) {
        // Price of 1 cup of coffee
        int basePrice = 5;
        // Add $1 if the user wants whipped cream
        if (addWhippedCream) {
            basePrice += 1;
        }
        // Add $2 if the user wants chocolate
        if (addChocolate) {
            basePrice += 2;
        }
        return basePrice * quantity;
    }


    /**
     * Create summary of the order.
     *
     * @param addWhippedCream is whether or not the user wants whipped cream topping
     * @param addChocolate    is whether or not the user wants chocolate topping
     * @param price           of the order
     * @return text summary
     */
    private String createOrderSummary(String name, int price, boolean addWhippedCream, boolean addChocolate) {
        return  "\n"+getString(R.string.order_summary_name, name) +
                "\n"+getString(R.string.order_summary_whipped_cream, addWhippedCream) +
                "\n"+getString(R.string.order_summary_chocolate, addChocolate) +
                "\n"+getString(R.string.order_summary_quantity, quantity) +
                "\n"+getString(R.string.order_summary_price, NumberFormat.getCurrencyInstance().format(price)) +
                "\n"+getString(R.string.thank_you);
    }
}