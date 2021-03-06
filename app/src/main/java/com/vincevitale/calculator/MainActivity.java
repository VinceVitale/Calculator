package com.vincevitale.calculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // TextViews
    private TextView textView;
    private TextView textViewNumberOne;
    private TextView textViewOperator;
    private TextView textViewNumberTwo;
    private TextView textViewSum;

    // Operation Buttons
    private Button buttonAdd;
    private Button buttonSubtract;
    private Button buttonMultiply;
    private Button buttonDivide;

    // Buttons (Decimal, Back, Equal)
    private Button buttonDecimal;
    private Button buttonDelete;
    private Button buttonEquals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TextViews
        textView = (TextView) findViewById(R.id.editText);
        textViewNumberOne = (TextView) findViewById(R.id.textViewNumberOne);
        textViewOperator = (TextView) findViewById(R.id.textViewOperator);
        textViewNumberTwo = (TextView) findViewById(R.id.textViewNumberTwo);
        textViewSum = (TextView) findViewById(R.id.textViewSum);

        // Strings for Operation Buttons
        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonSubtract = (Button) findViewById(R.id.buttonSubtract);
        buttonMultiply = (Button) findViewById(R.id.buttonMultiply);
        buttonDivide = (Button) findViewById(R.id.buttonDivide);

        // Strings for the rest of the Buttons (Decimal, Back, Equal)
        buttonDecimal = (Button) findViewById(R.id.buttonDecimal);
        buttonDelete = (Button) findViewById(R.id.buttonDelete);
        buttonEquals = (Button) findViewById(R.id.buttonEquals);

        // String for Clear Button
        Button buttonClear = (Button) findViewById(R.id.buttonClear);

        // Clears Calculator Variables
        clearCalc();

        // Button Decimal Event Handler
        buttonDecimal.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textView.getText().toString().equals("_")){
                    textView.setText("");
                }

                if (textViewSum.getText().toString().equals("")) {
                    textView.append(".");
                    buttonDecimal.setEnabled(false);
                    buttonEquals.setEnabled(false);
                    disableOperators();
                }else{
                    clearCalc();
                    textView.setText(".");
                    buttonDecimal.setEnabled(false);
                    disableOperators();
                }
            }
        });

        // Button Delete Event Handler
        buttonDelete.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gets a copy of textView Text as a String
                String tempString = textView.getText().toString();
                if(!tempString.equals("_")){
                    // Checks if textView is Empty
                    if (tempString.length() > 0) {
                        // Enables Decimal Button if the Deleted Character was a Decimal
                        if (tempString.substring((tempString.length() - 1), tempString.length()).equals(".")) {
                            buttonDecimal.setEnabled(true);
                        }
                        textView.setText(tempString.substring(0, tempString.length() - 1));
                        // Disables Operator Buttons if textView is Empty
                        if (textView.getText().toString().length() <= 0) {
                            disableOperators();
                            buttonEquals.setEnabled(false);
                        } else {
                            // Disables Operator Buttons if Last Character of textView is a Decimal
                            tempString = textView.getText().toString();
                            tempString = tempString.substring((tempString.length() - 1), tempString.length());
                            if (tempString.equals(".")) {
                                disableOperators();
                                buttonEquals.setEnabled(false);
                                buttonDecimal.setEnabled(false);
                            } else if (textViewOperator.getText().toString().equals("")) {
                                enableOperators();
                            } else {
                                buttonEquals.setEnabled(true);
                            }
                        }
                    }
                }
                // Checks if textView is Empty After Delete
                if(
                    (textView.getText().toString().length() == 0) &&
                    (textViewOperator.getText().toString().equals("")) &&
                    (textViewNumberOne.getText().toString().equals(""))
                    ){

                    textView.setText("_");
                }
            }
        });

        // Button Clear Event Handler
        buttonClear.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCalc();
            }
        });

        // Button Equals
        buttonEquals.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                double textViewDouble = Double.parseDouble(textView.getText().toString());
                textViewNumberTwo.setText(String.format(Locale.US, "%1$.2f", textViewDouble));
                String operatorPicked = textViewOperator.getText().toString();
                double firstNumber = Double.parseDouble(textViewNumberOne.getText().toString());
                double secondNumber = Double.parseDouble(textViewNumberTwo.getText().toString());
                double sumNumber;

                switch (operatorPicked){
                    case ("+"):
                        sumNumber = firstNumber + secondNumber;
                        break;
                    case ("-"):
                        sumNumber = firstNumber - secondNumber;
                        break;
                    case ("*"):
                        sumNumber = firstNumber * secondNumber;
                        break;
                    case ("/"):
                        // Prevents Dividing by Zero
                        if(secondNumber == 0){
                            sumNumber = 0;
                        }else {
                            sumNumber = firstNumber / secondNumber;
                        }
                        break;
                    default:
                        sumNumber = 0;
                        break;
                }

                // Check for Dividing by Zero
                if(operatorPicked.equals("/") && secondNumber == 0){
                    textView.setText(R.string.error);
                    clearOnly();
                }else {
                    textViewSum.setText(String.format(Locale.US, "%1$.2f", sumNumber));
                    textView.setText(String.format(Locale.US, "%1$.2f", sumNumber));
                    enableOperators();
                    buttonDelete.setEnabled(false);
                    buttonDecimal.setEnabled(true);
                }
            }
        });
    } // onCreate End

    // OnClick Event Handler for all Operators
    public void onClickOperator(View v) {
        Button b = (Button) v;

        if(!textViewSum.getText().toString().equals("")) { // Continued Calculation
            double textViewDouble = Double.parseDouble(textView.getText().toString());
            textViewNumberOne.setText(String.format(Locale.US, "%1$.2f", textViewDouble));
            textViewNumberTwo.setText("");
            textViewSum.setText("");
        } else if(
                (!textViewOperator.getText().toString().equals("")) &&
                (!textViewNumberOne.getText().toString().equals(""))
                ) { // New or Continued Calculation with no Equals Button

            // Modified Equals onClick
            String operatorPicked = textViewOperator.getText().toString();
            double firstNumber = Double.parseDouble(textViewNumberOne.getText().toString());
            double secondNumber = Double.parseDouble(textView.getText().toString());
            double sumNumber;

            switch (operatorPicked){
                case ("+"):
                    sumNumber = firstNumber + secondNumber;
                    break;
                case ("-"):
                    sumNumber = firstNumber - secondNumber;
                    break;
                case ("*"):
                    sumNumber = firstNumber * secondNumber;
                    break;
                case ("/"):
                    // Prevents Dividing by Zero
                    if(secondNumber == 0){
                        sumNumber = 0;
                    }else {
                        sumNumber = firstNumber / secondNumber;
                    }
                    break;
                default:
                    sumNumber = 0;
                    break;
            }

            // Check for Dividing by Zero
            if(operatorPicked.equals("/") && secondNumber == 0){
                textView.setText(R.string.error);
                clearOnly();
            }else {
                textViewNumberOne.setText(String.format(Locale.US, "%1$.2f", sumNumber));
                textViewNumberTwo.setText("");
                textViewSum.setText("");
            }
        } else if(textViewSum.getText().toString().equals("") &&
                textViewNumberTwo.getText().toString().equals("")
                ) { // New Calculation

            // Allows String to hold Negative Numbers
            double textViewDouble = Double.parseDouble(textView.getText().toString());
            textViewNumberOne.setText(String.format(Locale.US, "%1$.2f", textViewDouble));
        }

        textView.setText("");
        disableOperators();
        textViewOperator.setText(b.getText().toString());
        buttonDecimal.setEnabled(true);
        buttonEquals.setEnabled(false);
    } // onClickOperator End

    // OnClick Event Handler for all Number Buttons
    public void onClickNumber(View v){
        Button b = (Button) v;

        buttonDelete.setEnabled(true);

        // Empties textView to Input Numbers
        if(textView.getText().toString().equals("_")){
            textView.setText("");
        }

        // Resets Underscore
        if(!textViewSum.getText().toString().equals("")){
            clearCalc();
            textView.setText("");
        }

        // Prevents Starting with Zero and Having Multiple Zeros
        if((textView.getText().toString().equals("0")) &&
                (!b.getText().toString().equals("."))){

            textView.setText("");
        }

        // Appends Number Button to end of textView
        textView.append(b.getText().toString());
        if (textViewOperator.getText().toString().equals("")) {
            enableOperators();
        }else if (
                (!textViewNumberOne.getText().toString().equals("")) &&
                (!textViewOperator.getText().toString().equals(""))
                ) {
            enableOperators();
            buttonEquals.setEnabled(true);
        } else {
            disableOperators();
            buttonEquals.setEnabled(true);
        }
    } // onClickNumber End

    public void clearCalc() {
        textView.setText(R.string.underscore);
        textViewNumberOne.setText("");
        textViewOperator.setText("");
        textViewNumberTwo.setText("");
        textViewSum.setText("");

        buttonDelete.setEnabled(true);
        buttonDecimal.setEnabled(true);
        buttonEquals.setEnabled(false);
        disableOperators();
    }

    public void enableOperators() {
        buttonAdd.setEnabled(true);
        buttonSubtract.setEnabled(true);
        buttonMultiply.setEnabled(true);
        buttonDivide.setEnabled(true);
        buttonEquals.setEnabled(false);
    }

    public void disableOperators() {
        buttonAdd.setEnabled(false);
        buttonSubtract.setEnabled(false);
        buttonMultiply.setEnabled(false);
        buttonDivide.setEnabled(false);
    }

    public void clearOnly() {
        disableOperators();

        buttonDecimal.setEnabled(false);
        buttonDelete.setEnabled(false);
        buttonEquals.setEnabled(false);
    }

} // MainActivity End
