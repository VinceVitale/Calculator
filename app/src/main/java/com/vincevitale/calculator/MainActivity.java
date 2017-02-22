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

    // Buttons (Decimal, Back, Clear, Equal)
    private Button buttonDecimal;
    private Button buttonDelete;
    private Button buttonClear;
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

        // Strings for the rest of the Buttons (Decimal, Back, Clear, Equal)
        buttonDecimal = (Button) findViewById(R.id.buttonDecimal);
        buttonDelete = (Button) findViewById(R.id.buttonDelete);
        buttonClear = (Button) findViewById(R.id.buttonClear);
        buttonEquals = (Button) findViewById(R.id.buttonEquals);

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
                disableAll();
            }else{
                clearCalc();
                textView.setText(".");
                buttonDecimal.setEnabled(false);
                disableAll();
            }
            }
        });

        // Button Delete Event Handler
        buttonDelete.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        disableAll();
                    } else {
                        // Disables Operator Buttons if Last Character of textView is a Decimal
                        tempString = textView.getText().toString();
                        tempString = tempString.substring((tempString.length() - 1), tempString.length());
                        if (tempString.equals(".")) {
                            disableAll();
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

            if(operatorPicked.equals("+")){
                sumNumber = firstNumber + secondNumber;
            }else if(operatorPicked.equals("-")){
                sumNumber = firstNumber - secondNumber;
            }else if(operatorPicked.equals("*")){
                sumNumber = firstNumber * secondNumber;
            }else if(operatorPicked.equals("/")){
                // Prevents Dividing by Zero
                if(secondNumber == 0){
                    sumNumber = 0;
                }else {
                    sumNumber = firstNumber / secondNumber;
                }
            }else {
                sumNumber = 0;
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

            if(operatorPicked.equals("+")){
                sumNumber = firstNumber + secondNumber;
            }else if(operatorPicked.equals("-")){
                sumNumber = firstNumber - secondNumber;
            }else if(operatorPicked.equals("*")){
                sumNumber = firstNumber * secondNumber;
            }else if(operatorPicked.equals("/")){
                // Prevents Dividing by Zero
                if(secondNumber == 0){
                    sumNumber = 0;
                }else {
                    sumNumber = firstNumber / secondNumber;
                }
            }else {
                sumNumber = 0;
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
    }

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
    }

    public void clearCalc() {
        textView.setText(R.string.underscore);
        textViewNumberOne.setText("");
        textViewOperator.setText("");
        textViewNumberTwo.setText("");
        textViewSum.setText("");

        buttonDelete.setEnabled(true);
        buttonDecimal.setEnabled(true);
        disableAll();
    }

    public void disableAll() {
        buttonAdd.setEnabled(false);
        buttonSubtract.setEnabled(false);
        buttonMultiply.setEnabled(false);
        buttonDivide.setEnabled(false);
        buttonEquals.setEnabled(false);
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
        buttonAdd.setEnabled(false);
        buttonSubtract.setEnabled(false);
        buttonMultiply.setEnabled(false);
        buttonDivide.setEnabled(false);

        buttonDecimal.setEnabled(false);
        buttonDelete.setEnabled(false);
        buttonEquals.setEnabled(false);
    }

} // MainActivity End



/*
    // set a listener
    buttonAdd.setOnClickListener(this);
    buttonSubtract.setOnClickListener(this);
    buttonMultiply.setOnClickListener(this);
    buttonDivide.setOnClickListener(this);

    @Override
    public void onClick(View v) {

        float num1, num2;
        float result = 0;

        // check if the fields are empty
        if (TextUtils.isEmpty(textViewNumberOne.getText().toString())
                || TextUtils.isEmpty(textViewNumberTwo.getText().toString())) {
            return;
        }

        // read EditText and fill variables with numbers
        num1 = Float.parseFloat(textViewNumberOne.getText().toString());
        num2 = Float.parseFloat(textViewNumberTwo.getText().toString());

        // defines the button that has been clicked and performs the corresponding operation
        switch (v.getId()) {
            case R.id.buttonAdd:
                result = num1 + num2;
                break;
            case R.id.buttonSubtract:
                result = num1 - num2;
                break;
            case R.id.buttonMultiply:
                result = num1 * num2;
                break;
            case R.id.buttonDivide:
                result = num1 / num2;
                break;
            default:
                break;
        }

        // form the output line
        textViewSum.setText(" = " + result);
    }
*/

/*
    // Button One Event Handler
    buttonOne.setOnClickListener(new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            textView.append("1");

            if(textViewOperator.toString().equals("")){
                buttonAdd.setEnabled(true);
                buttonSubtract.setEnabled(true);
                buttonMultiply.setEnabled(true);
                buttonDivide.setEnabled(true);
                buttonEquals.setEnabled(false);
            }else {
                buttonAdd.setEnabled(false);
                buttonSubtract.setEnabled(false);
                buttonMultiply.setEnabled(false);
                buttonDivide.setEnabled(false);
                buttonEquals.setEnabled(true);
            }
        }
    });

    // Buttons 1 through 9 and 0
    Button buttonOne = (Button) findViewById(R.id.buttonOne);
    Button buttonTwo = (Button) findViewById(R.id.buttonTwo);
    Button buttonThree = (Button) findViewById(R.id.buttonThree);
    Button buttonFour = (Button) findViewById(R.id.buttonFour);
    Button buttonFive = (Button) findViewById(R.id.buttonFive);
    Button buttonSix = (Button) findViewById(R.id.buttonSix);
    Button buttonSeven = (Button) findViewById(R.id.buttonSeven);
    Button buttonEight = (Button) findViewById(R.id.buttonEight);
    Button buttonNine = (Button) findViewById(R.id.buttonNine);
    Button buttonZero = (Button) findViewById(R.id.buttonZero);
*/

/*

        // Button Add Event Handler
        buttonAdd.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
            // Checks if New Calculation or else its Continued
            if(textViewSum.getText().toString().equals("")) {
                // New Calculation
                textViewNumberOne.setText(textView.getText().toString());

                buttonDecimal.setEnabled(true);
                disableOperators();
            }else {
                // Continued Calculation
                textViewNumberOne.setText(textViewSum.getText().toString());
                textViewNumberTwo.setText("");
                textViewSum.setText("");
            }
            textViewOperator.setText(R.string.add);
            textView.setText("");
            }
        });

        // Button Subtract Event Handler
        buttonSubtract.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
            // Checks if New Calculation or else its Continued
            if(textViewSum.getText().toString().equals("")) {
                // New Calculation
                textViewNumberOne.setText(textView.getText().toString());

                buttonDecimal.setEnabled(true);
                disableOperators();
            }else {
                // Continued Calculation
                textViewNumberOne.setText(textViewSum.getText().toString());
                textViewNumberTwo.setText("");
                textViewSum.setText("");
            }
            textViewOperator.setText(R.string.subtract);
            textView.setText("");
            }
        });

        // Button Multiply Event Handler
        buttonMultiply.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
            // Checks if New Calculation or else its Continued
            if(textViewSum.getText().toString().equals("")) {
                // New Calculation
                textViewNumberOne.setText(textView.getText().toString());
            }else {
                // Continued Calculation
                textViewNumberOne.setText(textViewSum.getText().toString());
            }
            textViewOperator.setText(R.string.multiply);
            buttonDecimal.setEnabled(true);
            textViewNumberTwo.setText("");
            textViewSum.setText("");
            textView.setText("");
            disableOperators();
            }
        });

        // Button Divide Event Handler
        buttonDivide.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
            // Checks if New Calculation or else its Continued
            if(textViewSum.getText().toString().equals("")) {
                // New Calculation
                textViewNumberOne.setText(textView.getText().toString());
            }else {
                // Continued Calculation
                textViewNumberOne.setText(textViewSum.getText().toString());
            }
            textViewOperator.setText(R.string.divide);
            buttonDecimal.setEnabled(true);
            textViewNumberTwo.setText("");
            textViewSum.setText("");
            textView.setText("");
            disableOperators();
            }
        });
*/
