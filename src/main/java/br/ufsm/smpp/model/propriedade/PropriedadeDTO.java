package br.ufsm.smpp.model.propriedade;

import java.util.List;
import java.util.UUID;

public class PropriedadeDTO {
    public String nome;
    public String cidade;
    public String coordenadas;
    public String proprietario;
    public String telefoneProprietario;
    public List<UUID> atividades;
    public List<UUID> vulnerabilidades;
}
