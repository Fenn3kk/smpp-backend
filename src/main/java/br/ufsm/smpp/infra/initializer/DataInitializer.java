package br.ufsm.smpp.infra.initializer;

import br.ufsm.smpp.model.usuario.Usuario;
import br.ufsm.smpp.model.usuario.UsuarioRepository;
import br.ufsm.smpp.model.atividade.Atividade;
import br.ufsm.smpp.model.atividade.AtividadeRepository;
import br.ufsm.smpp.model.vulnerabilidade.Vulnerabilidade;
import br.ufsm.smpp.model.vulnerabilidade.VulnerabilidadeRepository;
import br.ufsm.smpp.model.cidade.Cidade;
import br.ufsm.smpp.model.cidade.CidadeRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final AtividadeRepository atividadeRepo;
    private final VulnerabilidadeRepository vulnerabilidadeRepo;
    private final CidadeRepository cidadeRepo;
    private final UsuarioRepository usuarioRepo;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if (atividadeRepo.count() == 0) {
            atividadeRepo.saveAll(List.of(
                    new Atividade("Criação de bovinos"),
                    new Atividade("Criação de búfalos"),
                    new Atividade("Criação de equinos"),
                    new Atividade("Criação de asininos"),
                    new Atividade("Criação de muares"),
                    new Atividade("Criação de suínos"),
                    new Atividade("Criação de caprinos"),
                    new Atividade("Criação de ovinos"),
                    new Atividade("Criação de galinhas e similares"),
                    new Atividade("Criação de codornas"),
                    new Atividade("Criação de outras aves"),
                    new Atividade("Criação de coelhos"),
                    new Atividade("Criação de abelhas"),
                    new Atividade("Criação de peixes, camarões e moluscos"),
                    new Atividade("Criação de rãs"),
                    new Atividade("Criação de bicho-da-seda"),
                    new Atividade("Pesca"),
                    new Atividade("Lavoura Temporária"),
                    new Atividade("Lavoura Permanente"),
                    new Atividade("Horticultura"),
                    new Atividade("Extração Vegetal"),
                    new Atividade("Floricultura"),
                    new Atividade("Silvicultura e seus produtos"),
                    new Atividade("Agroindústria Vegetal"),
                    new Atividade("Agroindústria Animal"),
                    new Atividade("Atividade de turismo rural"),
                    new Atividade("Exploração mineral"),
                    new Atividade("Atividades não agrícolas")
            ));
        }

        if (vulnerabilidadeRepo.count() == 0) {
            vulnerabilidadeRepo.saveAll(List.of(
                    new Vulnerabilidade("Área sujeitas a deslizamentos"),
                    new Vulnerabilidade("Área sujeitas a alagamento"),
                    new Vulnerabilidade("Área sujeita a secas"),
                    new Vulnerabilidade("Acesso por pontes sujeitas a inundações"),
                    new Vulnerabilidade("Acesso por estradas sujeitas a inundações")
            ));
        }

        if (cidadeRepo.count() == 0) {
            cidadeRepo.saveAll(List.of(
                    new Cidade("Agudo"),
                    new Cidade("Cacequi"),
                    new Cidade("Cachoeira do Sul"),
                    new Cidade("Capão do Cipó"),
                    new Cidade("Cerro Branco"),
                    new Cidade("Dilermando de Aguiar"),
                    new Cidade("Dona Francisca"),
                    new Cidade("Faxinal do Soturno"),
                    new Cidade("Itaara"),
                    new Cidade("Ivorá"),
                    new Cidade("Jaguari"),
                    new Cidade("Júlio de Castilhos"),
                    new Cidade("Mata"),
                    new Cidade("Nova Esperança do Sul"),
                    new Cidade("Nova Palma"),
                    new Cidade("Novo Cabrais"),
                    new Cidade("Paraíso do Sul"),
                    new Cidade("Santa Maria"),
                    new Cidade("Santiago"),
                    new Cidade("São Francisco de Assis"),
                    new Cidade("São João do Polêsine"),
                    new Cidade("São Martinho da Serra"),
                    new Cidade("São Sepé"),
                    new Cidade("São Vicente do Sul"),
                    new Cidade("Silveira Martins"),
                    new Cidade("Unistalda")
            ));
        }

        if (usuarioRepo.count() == 0) {
            Usuario admin = new Usuario();
            admin.setNome("ADMINISTRADOR");
            admin.setEmail("admin@email.com");
            admin.setTelefone("00000000000");
            admin.setSenha(passwordEncoder.encode("admin060504")); // senha já codificada
            admin.setTipoUsuario(Usuario.TipoUsuario.ADMIN);
            usuarioRepo.save(admin);
        }
    }
}
