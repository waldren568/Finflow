document.addEventListener('DOMContentLoaded', function() {
    // Gestione degli eventi lato client
    console.log('FinFlow app loaded');
    
    // Esempio: Gestione della modalità dark/light
    const themeToggle = document.getElementById('theme-toggle');
    if (themeToggle) {
        themeToggle.addEventListener('click', function() {
            document.body.classList.toggle('dark');
        });
    }
    
    // Aggiungi qui altre funzionalità JavaScript
});