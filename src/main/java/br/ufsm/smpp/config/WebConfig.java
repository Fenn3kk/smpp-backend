package br.ufsm.smpp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Classe de configuração para o Spring Web MVC.
 * Implementa a interface {@link WebMvcConfigurer} para personalizar
 * a configuração padrão do Spring MVC.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Diretório onde os arquivos de upload são armazenados.
     * O valor é injetado a partir da propriedade 'app.upload.dir' no arquivo
     * de configuração (application.properties). Se a propriedade não for definida,
     * o valor padrão "uploads" será usado.
     */
    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    /**
     * Configura manipuladores de recursos para servir arquivos estáticos.
     * Este método mapeia uma URL pública para um diretório físico no servidor,
     * permitindo que o navegador acesse arquivos como imagens, CSS, etc.
     *
     * @param registry O registro onde os manipuladores de recursos são adicionados.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Converte o nome do diretório (relativo ou absoluto) em um caminho absoluto no sistema de arquivos.
        // Usar Path em vez de File é uma prática mais moderna em Java.
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath();

        // Mapeia as requisições que começam com "/uploads/**"
        // para o diretório físico de uploads no servidor.
        // O prefixo "file:" indica que o recurso está no sistema de arquivos local.
        // O "/" no final é importante para que o mapeamento funcione corretamente.
        registry
                .addResourceHandler("/uploads/**")
                .addResourceLocations("file:/" + uploadPath + "/");
    }
}