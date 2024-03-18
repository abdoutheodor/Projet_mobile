package com.example.project1442.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
        import androidx.core.content.ContextCompat;

        import android.content.Intent;
        import android.os.Bundle;
        import android.widget.Toast;

import com.example.project1442.R;

import java.util.concurrent.Executor;

public class AuthenticationActivity extends AppCompatActivity {

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication); // Assurez-vous que vous avez ce layout ou changez-le selon votre cas

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(AuthenticationActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(), "Erreur d'authentification : " + errString, Toast.LENGTH_SHORT).show();
                // Fermez l'application ou retournez à une activité sécurisée selon vos besoins
                finish();
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "Authentification réussie!", Toast.LENGTH_SHORT).show();
                // Rediriger vers LoginActivity après une authentification réussie
                startActivity(new Intent(AuthenticationActivity.this, LoginActivity.class));
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentification échouée.", Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Authentification biométrique requise")
                .setSubtitle("Connectez-vous à l'aide de l'empreinte digitale ou du code d'accès")
                // Cette ligne est modifiée pour utiliser setAllowedAuthenticators au lieu de setNegativeButtonText
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                .build();


        biometricPrompt.authenticate(promptInfo);
    }
}
