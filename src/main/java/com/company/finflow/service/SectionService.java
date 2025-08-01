package com.company.finflow.service;

import com.company.finflow.model.Section;
import com.company.finflow.repository.SectionRepository;
import com.company.finflow.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectionService {
    public List<Section> findSectionsByUser(AppUser user) {
        return sectionRepository.findByUser(user);
    }
    public BalanceService getBalanceService() {
        return balanceService;
    }
    @Autowired
    private CurrentUserUtil currentUserUtil;
    private final SectionRepository sectionRepository;
    private final BalanceService balanceService;

    public SectionService(SectionRepository sectionRepository, BalanceService balanceService) {
        this.sectionRepository = sectionRepository;
        this.balanceService = balanceService;
    }

    public List<Section> getAllSections() {
        // Recupera l'utente corrente
        AppUser user = currentUserUtil.getCurrentUser();
        return sectionRepository.findByUser(user);
    }

    public Section saveSection(Section section) {
        return sectionRepository.save(section);
    }

    public void addSection(Section newSection) {
        AppUser user = currentUserUtil.getCurrentUser();
        newSection.setUser(user);
        List<Section> sections = sectionRepository.findByUser(user);
        int totalPercentage = sections.stream()
            .filter(s -> !s.isDefault())
            .mapToInt(s -> s.getPercentage() != null ? s.getPercentage() : 0)
            .sum();

        Section defaultSection = sections.stream()
            .filter(Section::isDefault)
            .findFirst()
            .orElse(null);

        // Se la sezione default non esiste, la crea
        if (defaultSection == null) {
            defaultSection = new Section();
            defaultSection.setName("Filler");
            defaultSection.setDefault(true);
            defaultSection.setUser(user);
            defaultSection.setAllocatedAmount(0.0);
        }

        // Aggiorna la percentuale della default
        defaultSection.setPercentage(100 - (totalPercentage + newSection.getPercentage()));
        sectionRepository.save(defaultSection);

        sectionRepository.save(newSection);
    }

    public void deleteSection(Long sectionId) {
        Section section = sectionRepository.findById(sectionId)
            .orElseThrow(() -> new RuntimeException("Sezione non trovata"));
        if (section.isDefault()) {
            // Non eliminare la sezione Filler
            return;
        }
        sectionRepository.deleteById(sectionId);
    }

    public void renameSection(Long sectionId, String newName) {
        Section section = sectionRepository.findById(sectionId)
            .orElseThrow(() -> new RuntimeException("Sezione non trovata"));
        section.setName(newName);
        sectionRepository.save(section);
    }

    public boolean withdrawFromSection(Long sectionId, Double amount) {
        Section section = sectionRepository.findById(sectionId)
            .orElseThrow(() -> new RuntimeException("Sezione non trovata"));
        AppUser user = section.getUser();
        if (amount != null && amount > 0 && section.getAllocatedAmount() >= amount) {
            section.setAllocatedAmount(section.getAllocatedAmount() - amount);
            sectionRepository.save(section);
            // Aggiorna il saldo totale come somma delle sezioni
            balanceService.updateBalanceFromSections(user);
            return true;
        }
        return false;
    }
    public boolean transferBetweenSections(Long fromSectionId, Long toSectionId, Double amount) {
        if (amount == null || amount <= 0) return false;
        Section from = sectionRepository.findById(fromSectionId).orElse(null);
        Section to = sectionRepository.findById(toSectionId).orElse(null);
        if (from == null || to == null) return false;
        if (from.getAllocatedAmount() < amount) return false;
        from.setAllocatedAmount(from.getAllocatedAmount() - amount);
        to.setAllocatedAmount(to.getAllocatedAmount() + amount);
        sectionRepository.save(from);
        sectionRepository.save(to);
        // Aggiorna il saldo totale come somma delle sezioni
        balanceService.updateBalanceFromSections(from.getUser());
        return true;
    }
}