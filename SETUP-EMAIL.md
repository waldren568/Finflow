# 📧 Configurazione Email per FinFlow

## Opzioni Provider Email

### 1. Gmail (Consigliato)
```properties
# In application.properties, sostituisci:
spring.mail.username=la-tua-email@gmail.com
spring.mail.password=la-tua-app-password-di-16-caratteri
```

**Come ottenere App Password per Gmail:**
1. Vai su https://myaccount.google.com/security
2. Attiva "Verifica in 2 passaggi" se non è già attiva
3. Cerca "Password per le app" 
4. Genera una nuova password per "Mail"
5. Usa quella password di 16 caratteri (senza spazi)

### 2. Outlook/Hotmail
```properties
spring.mail.host=smtp-mail.outlook.com
spring.mail.port=587
spring.mail.username=la-tua-email@outlook.com
spring.mail.password=la-tua-password
```

### 3. Yahoo Mail
```properties
spring.mail.host=smtp.mail.yahoo.com
spring.mail.port=587
spring.mail.username=la-tua-email@yahoo.com
spring.mail.password=la-tua-app-password
```

### 4. Provider Email Custom
```properties
spring.mail.host=smtp.tuodominio.com
spring.mail.port=587
spring.mail.username=noreply@tuodominio.com
spring.mail.password=la-tua-password
```

## Test Rapido
1. Modifica `application.properties` con i tuoi dati
2. Riavvia l'applicazione
3. Registra un nuovo utente
4. Controlla la tua casella email!

## Fallback
Se l'email non funziona, il sistema mostrerà il link nel terminale come backup.
