package br.ufsm.smpp.infra.web_config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Injeta o valor da propriedade 'app.upload.dir' do arquivo
     * application.properties. Se não for definido, usa "uploads" como padrão.
     */
    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Converte o diretório (relativo ou absoluto) em um caminho absoluto
        String uploadPath = Paths.get(uploadDir).toFile().getAbsolutePath();

        // Expõe o diretório de uploads para que ele seja acessível via web.
        // O resource location precisa do prefixo "file:" e terminar com uma barra.
        registry
                .addResourceHandler("/uploads/**")
                .addResourceLocations("file:/" + uploadPath + "/");
    }
}