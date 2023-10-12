package com.alialtunoglu.aralikkokbulma;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
public class MainActivity extends AppCompatActivity {
    private EditText equationInput;
    private EditText lowerBoundInput;
    private EditText upperBoundInput;
    private EditText epsilonValueInput;
    private Button calculateButton;
    private TextView resultText;
    private TextView stepText;  // Adımları göstermek için yeni bir TextView ekliyoruz
    private int stepCount = 0;  // Adım sayısını tutmak için bir sayaç ekliyoruz
    private String steps1="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Aralığı 2'ye Bölme");

        equationInput = findViewById(R.id.equationInput);
        lowerBoundInput = findViewById(R.id.lowerBoundInput);
        upperBoundInput = findViewById(R.id.upperBoundInput);
        epsilonValueInput = findViewById(R.id.epsilonValueInput);
        calculateButton = findViewById(R.id.calculateButton);
        resultText = findViewById(R.id.resultText);
        stepText = findViewById(R.id.stepText);  // Yeni TextView'ı buluyoruz

        stepText.setMovementMethod(new ScrollingMovementMethod());

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(equationInput.getText().toString().isEmpty()) {
                    equationInput.setError("Bu alanın doldurulması gerekiyor");
                    return;
                }
                if(lowerBoundInput.getText().toString().isEmpty()) {
                    lowerBoundInput.setError("Bu alanın doldurulması gerekiyor");
                    return;
                }
                if(upperBoundInput.getText().toString().isEmpty()) {
                    upperBoundInput.setError("Bu alanın doldurulması gerekiyor");
                    return;
                }
                String equation = equationInput.getText().toString();
                double lowerBound = Double.parseDouble(lowerBoundInput.getText().toString());
                double upperBound = Double.parseDouble(upperBoundInput.getText().toString());
                double  epsilonValue= Double.parseDouble(epsilonValueInput.getText().toString());

                hesapla(equation,lowerBound,upperBound,epsilonValue);
                System.out.println(steps1.toString());
            }
        });
    }
    private void hesapla(String equation,double lowerbound , double upperBound ,double epsilonValue){

        double fxA=evaluateExpression(equation,lowerbound);
        double fxU=evaluateExpression(equation,upperBound);
        double fxY;
        double xY;
        if(fxA*fxU<0){
            System.out.println("Sıfırdan küçük olduğundan bir kök vardır");
            while(true) {
                xY = (lowerbound + upperBound) / 2;
                stepCount++;
                fxY = evaluateExpression(equation, xY);
                fxA=evaluateExpression(equation,lowerbound);
                fxU=evaluateExpression(equation,upperBound);
                if(epsilonValue==0 && fxY==0){
                    steps1+="Adım "+stepCount+": Kök Tespit Edildi -> " + xY +"\n";
                    resultText.setText("Kökünüz "+stepCount+". Adımda Bulundu -> " + String.valueOf(xY));
                    stepText.setText(steps1);  // Tüm adımları gösteriyoruz
                    break;
                }
                if (fxA * fxY < 0) {
                    upperBound = xY;
                } else {
                    lowerbound = xY;
                }
                if ((fxU - fxA) < epsilonValue || (upperBound - lowerbound) < epsilonValue) {
                    steps1+="Adım "+stepCount+": Kök Tespit Edildi -> " + xY +"\n";
                    resultText.setText("Kökünüz "+stepCount+". Adımda Bulundu -> " + String.valueOf(xY));
                    stepText.setText(steps1);  // Tüm adımları gösteriyoruz
                    break;
                }
                steps1+="Adım "+stepCount+": x'in yeni değeri " + xY +"\n";

            }
        }
        else{
            System.out.println("Sıfırdan küçük olmadığından bir kök yoktur vardır");
        }
    }
    private double evaluateExpression(String equation, double xValue) {
        Expression expression = new ExpressionBuilder(equation)
                .variables("x","e")
                .build()
                .setVariable("x", xValue)
                .setVariable("e",Math.E);

        return expression.evaluate();
    }
}
