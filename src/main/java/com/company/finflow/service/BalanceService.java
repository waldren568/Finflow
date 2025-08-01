package com.company.finflow.service;

import com.company.finflow.model.Balance;
import com.company.finflow.model.Section;
import com.company.finflow.model.AppUser;
import com.company.finflow.repository.BalanceRepository;
import com.company.finflow.repository.SectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BalanceService {
    public void resetAllSectionsAndBalance(AppUser user) {
        List<Section> sections = sectionRepository.findByUser(user);
        for (Section section : sections) {
            section.setAllocatedAmount(0.0);
            sectionRepository.save(section);
        }
        Balance balance = balanceRepository.findAll().stream()
            .filter(b -> b.getUser() != null && b.getUser().getId().equals(user.getId()))
            .findFirst().orElseGet(() -> {
                Balance b = new Balance();
                b.setUser(user);
                b.setAmount(0.0);
                return b;
            });
        balance.setAmount(0.0);
        balanceRepository.save(balance);
    }
    public void updateBalanceFromSections(AppUser user) {
        List<Section> sections = sectionRepository.findByUser(user);
        double total = sections.stream().mapToDouble(s -> s.getAllocatedAmount() != null ? s.getAllocatedAmount() : 0.0).sum();
        Balance balance = balanceRepository.findAll().stream()
            .filter(b -> b.getUser() != null && b.getUser().getId().equals(user.getId()))
            .findFirst().orElseGet(() -> {
                Balance b = new Balance();
                b.setUser(user);
                b.setAmount(0.0);
                return b;
            });
        balance.setAmount(total);
        balanceRepository.save(balance);
    }
    public void subtractFromBalanceForUser(double amount, AppUser user) {
        Balance balance = balanceRepository.findAll().stream()
            .filter(b -> b.getUser() != null && b.getUser().getId().equals(user.getId()))
            .findFirst().orElseGet(() -> {
                Balance b = new Balance();
                b.setUser(user);
                b.setAmount(0.0);
                return b;
            });
        balance.setAmount(balance.getAmount() - amount);
        balanceRepository.save(balance);
    }
    public void addAmountForUser(double amount, AppUser user) {
        // Aggiorna il saldo dell'utente specifico
        Balance balance = balanceRepository.findAll().stream()
            .filter(b -> b.getUser() != null && b.getUser().getId().equals(user.getId()))
            .findFirst().orElseGet(() -> {
                Balance b = new Balance();
                b.setUser(user);
                b.setAmount(0.0);
                return b;
            });
        balance.setAmount(balance.getAmount() + amount);
        balanceRepository.save(balance);

        // Recupera tutte le sezioni dell'utente
        List<Section> sections = sectionRepository.findByUser(user);

        // Se non esiste la sezione default (filler), la crea
        Section defaultSection = sections.stream()
            .filter(Section::isDefault)
            .findFirst()
            .orElse(null);
        if (defaultSection == null) {
            defaultSection = new Section();
            defaultSection.setName("Filler");
            defaultSection.setDefault(true);
            defaultSection.setPercentage(100);
            defaultSection.setAllocatedAmount(0.0);
            defaultSection.setUser(user);
            sectionRepository.save(defaultSection);
            sections.add(defaultSection);
        }

        // Calcola la somma delle percentuali delle sezioni NON default
        int totalPercentage = sections.stream()
            .filter(s -> !s.isDefault())
            .mapToInt(s -> s.getPercentage() != null ? s.getPercentage() : 0)
            .sum();

        // Distribuisci l'importo nelle sezioni
        for (Section section : sections) {
            double allocated;
            if (section.isDefault()) {
                int defaultPerc = 100 - totalPercentage;
                if (defaultPerc < 0) defaultPerc = 0;
                section.setPercentage(defaultPerc);
                allocated = amount * defaultPerc / 100.0;
            } else {
                allocated = amount * section.getPercentage() / 100.0;
            }
            section.setAllocatedAmount(section.getAllocatedAmount() + allocated);
            sectionRepository.save(section);
        }
    }

    public Balance getBalanceForUser(AppUser user) {
        return balanceRepository.findAll().stream()
            .filter(b -> b.getUser() != null && b.getUser().getId().equals(user.getId()))
            .findFirst().orElse(new Balance());
    }

    private final BalanceRepository balanceRepository;
    private final SectionRepository sectionRepository;

    public BalanceService(BalanceRepository balanceRepository, SectionRepository sectionRepository) {
        this.balanceRepository = balanceRepository;
        this.sectionRepository = sectionRepository;
    }

    public Balance getBalance() {
        return balanceRepository.findAll().stream().findFirst().orElse(new Balance());
    }

    @Transactional
    public void addAmount(double amount) {
        // Aggiorna il saldo totale
        Balance balance = balanceRepository.findAll().stream().findFirst().orElse(new Balance());
        balance.setAmount(balance.getAmount() + amount);
        if (balance.getId() == null) {
            balanceRepository.save(balance);
        } else {
            balanceRepository.save(balance);
        }

        // Recupera tutte le sezioni
        List<Section> sections = sectionRepository.findAll();

        // Se non esiste la sezione default (filler), la crea
        Section defaultSection = sections.stream()
            .filter(Section::isDefault)
            .findFirst()
            .orElse(null);
        if (defaultSection == null) {
            defaultSection = new Section();
            defaultSection.setName("Filler");
            defaultSection.setDefault(true);
            defaultSection.setPercentage(100);
            defaultSection.setAllocatedAmount(0.0);
            sectionRepository.save(defaultSection);
            sections.add(defaultSection);
        }

        // Calcola la somma delle percentuali delle sezioni NON default
        int totalPercentage = sections.stream()
            .filter(s -> !s.isDefault())
            .mapToInt(s -> s.getPercentage() != null ? s.getPercentage() : 0)
            .sum();

        // Distribuisci l'importo nelle sezioni
        for (Section section : sections) {
            double allocated;
            if (section.isDefault()) {
                int defaultPerc = 100 - totalPercentage;
                if (defaultPerc < 0) defaultPerc = 0;
                section.setPercentage(defaultPerc);
                allocated = amount * defaultPerc / 100.0;
            } else {
                allocated = amount * section.getPercentage() / 100.0;
            }
            section.setAllocatedAmount(section.getAllocatedAmount() + allocated);
            sectionRepository.save(section);
        }
    }

    // Metodo per cancellare il saldo dell'utente (usato per delete account)
    @Transactional
    public void deleteBalance(Long userId) {
        List<Balance> balances = balanceRepository.findAll().stream()
            .filter(b -> b.getUser() != null && b.getUser().getId().equals(userId))
            .toList();
        for (Balance b : balances) {
            balanceRepository.delete(b);
        }
    }
}