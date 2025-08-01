package com.company;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Locale;

@Controller
public class LanguageController {
    @PostMapping("/set-language")
    public RedirectView setLanguage(@RequestParam("lang") String lang, HttpServletRequest request, HttpServletResponse response) {
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        if (localeResolver != null) {
            Locale locale = Locale.forLanguageTag(lang);
            localeResolver.setLocale(request, response, locale);
        }
        // Torna alla pagina precedente o a "/" se non disponibile
        String referer = request.getHeader("Referer");
        return new RedirectView(referer != null ? referer : "/");
    }
}
