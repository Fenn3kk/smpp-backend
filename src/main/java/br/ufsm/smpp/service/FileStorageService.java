package br.ufsm.smpp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Serviço para gerenciar o armazenamento, recuperação e exclusão de arquivos
 * no sistema de arquivos local.
 */
@Service
public class FileStorageService {

    /**
     * O caminho raiz onde os arquivos de upload são armazenados.
     * SUGESTÃO: Este valor é ideal para ser configurado no application.properties
     * e injetado com @Value para maior flexibilidade.
     */
    private final Path rootLocation = Paths.get("uploads");

    /**
     * Carrega um arquivo do sistema de arquivos como um recurso (Resource) para que possa ser servido,
     * por exemplo, em um endpoint de download.
     *
     * @param filename O nome do arquivo a ser carregado (ex: "imagem.jpg").
     * @return O arquivo como um objeto {@link Resource}, pronto para ser transmitido.
     * @throws RuntimeException se o arquivo não for encontrado, não puder ser lido ou se a URL for malformada.
     */
    public Resource loadAsResource(String filename) {
        try {
            Path file = rootLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Não foi possível ler o arquivo: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Não foi possível ler o arquivo: " + filename, e);
        }
    }

    /**
     * Deleta um arquivo específico do diretório de uploads.
     *
     * @param filename O nome do arquivo a ser deletado.
     * @throws RuntimeException se ocorrer um erro de I/O durante a exclusão do arquivo.
     */
    public void delete(String filename) {
        try {
            Path file = rootLocation.resolve(filename).normalize();
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new RuntimeException("Falha ao deletar o arquivo: " + filename, e);
        }
    }
}