package br.ufsm.smpp.config;

import br.ufsm.smpp.model.Usuario;
import br.ufsm.smpp.repository.UsuarioRepository;
import br.ufsm.smpp.model.Atividade;
import br.ufsm.smpp.repository.AtividadeRepository;
import br.ufsm.smpp.model.Vulnerabilidade;
import br.ufsm.smpp.repository.VulnerabilidadeRepository;
import br.ufsm.smpp.model.Cidade;
import br.ufsm.smpp.repository.CidadeRepository;
import br.ufsm.smpp.model.Incidente;
import br.ufsm.smpp.repository.IncidenteRepository;
import br.ufsm.smpp.model.TipoOcorrencia;
import br.ufsm.smpp.repository.TipoOcorrenciaRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

/**
 * Componente responsável por popular o banco de dados com dados iniciais
 * na inicialização da aplicação.
 * Garante que tabelas de domínio (como cidades, atividades, etc.) e
 * um usuário administrador padrão sejam criados se ainda não existirem.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer {

    // Repositórios para interagir com as respectivas tabelas no banco de dados.
    private final AtividadeRepository atividadeRepo;
    private final VulnerabilidadeRepository vulnerabilidadeRepo;
    private final CidadeRepository cidadeRepo;
    private final UsuarioRepository usuarioRepo;
    private final IncidenteRepository incidenteRepo;
    private final TipoOcorrenciaRepository tipoOcorrenciaRepo;

    /**
     * Codificador de senhas para criptografar a senha do usuário administrador.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Método executado após a injeção de dependências do Spring.
     * A anotação {@code @Transactional} garante que todas as operações de banco de dados
     * dentro deste método sejam executadas em uma única transação. Se qualquer
     * uma falhar, todas serão revertidas.
     */
    @PostConstruct
    @Transactional
    public void init() {
        initAtividades();
        initVulnerabilidades();
        initCidades();
        initIncidentes();
        initTiposOcorrencia();
        initAdminUser();
    }

    /**
     * Inicializa a tabela de 'Atividades' com uma lista predefinida.
     * A operação só é executada se a tabela estiver vazia.
     */
    private void initAtividades() {
        if (atividadeRepo.count() == 0) {
            List<Atividade> atividades = Stream.of(
                    "Criação de bovinos", "Criação de búfalos", "Criação de equinos",
                    "Criação de asininos", "Criação de muares", "Criação de suínos",
                    "Criação de caprinos", "Criação de ovinos", "Criação de galinhas e similares",
                    "Criação de codornas", "Criação de outras aves", "Criação de coelhos",
                    "Criação de abelhas", "Criação de peixes, camarões e moluscos",
                    "Criação de rãs", "Criação de bicho-da-seda", "Pesca", "Lavoura Temporária",
                    "Lavoura Permanente", "Horticultura", "Extração Vegetal", "Floricultura",
                    "Silvicultura e seus produtos", "Agroindústria Vegetal", "Agroindústria Animal",
                    "Atividade de turismo rural", "Exploração mineral", "Atividades não agrícolas"
            ).map(nome -> {
                Atividade atividade = new Atividade();
                atividade.setNome(nome);
                return atividade;
            }).toList();
            atividadeRepo.saveAll(atividades);
        }
    }

    /**
     * Inicializa a tabela de 'Vulnerabilidades' com uma lista predefinida.
     * A operação só é executada se a tabela estiver vazia.
     */
    private void initVulnerabilidades() {
        if (vulnerabilidadeRepo.count() == 0) {
            List<Vulnerabilidade> vulnerabilidades = Stream.of(
                    "Área sujeitas a deslizamentos", "Área sujeitas a alagamento",
                    "Área sujeita a secas", "Acesso por pontes sujeitas a inundações",
                    "Acesso por estradas sujeitas a inundações"
            ).map(nome -> {
                Vulnerabilidade vulnerabilidade = new Vulnerabilidade();
                vulnerabilidade.setNome(nome);
                return vulnerabilidade;
            }).toList();
            vulnerabilidadeRepo.saveAll(vulnerabilidades);
        }
    }

    /**
     * Inicializa a tabela de 'Cidades' com uma lista predefinida.
     * A operação só é executada se a tabela estiver vazia.
     */
    private void initCidades() {
        if (cidadeRepo.count() == 0) {
            List<Cidade> cidades = Stream.of(
                    "Agudo", "Cacequi", "Cachoeira do Sul", "Capão do Cipó", "Cerro Branco",
                    "Dilermando de Aguiar", "Dona Francisca", "Faxinal do Soturno", "Itaara",
                    "Ivorá", "Jaguari", "Júlio de Castilhos", "Mata", "Nova Esperança do Sul",
                    "Nova Palma", "Novo Cabrais", "Paraíso do Sul", "Santa Maria", "Santiago",
                    "São Francisco de Assis", "São João do Polêsine", "São Martinho da Serra",
                    "São Sepé", "São Vicente do Sul", "Silveira Martins", "Unistalda"
            ).map(nome -> {
                Cidade cidade = new Cidade();
                cidade.setNome(nome);
                return cidade;
            }).toList();
            cidadeRepo.saveAll(cidades);
        }
    }

    /**
     * Inicializa a tabela de 'Incidentes' com uma lista predefinida.
     * A operação só é executada se a tabela estiver vazia.
     */
    private void initIncidentes() {
        if (incidenteRepo.count() == 0) {
            List<Incidente> incidentes = Stream.of(
                    "Perda de animais", "Perda de equipamentos", "Perda de fertilizantes",
                    "Perda de lavoura", "Dano estrutural"
            ).map(nome -> {
                Incidente incidente = new Incidente();
                incidente.setNome(nome);
                return incidente;
            }).toList();
            incidenteRepo.saveAll(incidentes);
        }
    }

    /**
     * Inicializa a tabela de 'Tipos de Ocorrência' com uma lista predefinida.
     * A operação só é executada se a tabela estiver vazia.
     */
    private void initTiposOcorrencia() {
        if (tipoOcorrenciaRepo.count() == 0) {
            List<TipoOcorrencia> tipos = Stream.of(
                    "Alagamento", "Seca", "Tempestade", "Queimada"
            ).map(nome -> {
                TipoOcorrencia tipo = new TipoOcorrencia();
                tipo.setNome(nome);
                return tipo;
            }).toList();
            tipoOcorrenciaRepo.saveAll(tipos);
        }
    }

    /**
     * Cria um usuário administrador padrão se nenhum usuário existir no banco de dados.
     * A senha é criptografada antes de ser salva.
     */
    private void initAdminUser() {
        if (usuarioRepo.count() == 0) {
            Usuario admin = new Usuario();
            admin.setNome("ADMINISTRADOR");
            admin.setEmail("admin@email.com");
            admin.setTelefone("00000000000");
            admin.setSenha(passwordEncoder.encode("admin060504"));
            admin.setTipoUsuario(Usuario.TipoUsuario.ADMIN);
            usuarioRepo.save(admin);
        }
    }
}