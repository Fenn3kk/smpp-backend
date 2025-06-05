package br.ufsm.smpp.model.propriedade;

import java.util.List;
import java.util.UUID;

public class PropriedadeDTO {
    public String nome;
    public UUID cidade;                   // Referência à cidade pela UUID
    public String coordenadas;
    public String proprietario;
    public String telefoneProprietario;
    public List<UUID> atividades;           // IDs das atividades
    public List<UUID> vulnerabilidades;    // IDs das vulnerabilidades
}