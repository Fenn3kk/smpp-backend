package br.ufsm.smpp.infra.initializer;
import br.ufsm.smpp.model.atividade.Atividade;
import br.ufsm.smpp.model.atividade.AtividadeRepository;
import br.ufsm.smpp.model.vulnerabilidade.Vulnerabilidade;
import br.ufsm.smpp.model.vulnerabilidade.VulnerabilidadeRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final AtividadeRepository atividadeRepo;
    private final VulnerabilidadeRepository vulnerabilidadeRepo;

    @PostConstruct
    public void init() {
        if (atividadeRepo.count() == 0) {
            atividadeRepo.saveAll(List.of(
                    new Atividade("Pecuária"),
                    new Atividade("Agricultura"),
                    new Atividade("Silvicultura")
            ));
        }

        if (vulnerabilidadeRepo.count() == 0) {
            vulnerabilidadeRepo.saveAll(List.of(
                    new Vulnerabilidade("Incêndio"),
                    new Vulnerabilidade("Inundação"),
                    new Vulnerabilidade("Erosão")
            ));
        }
    }
}
