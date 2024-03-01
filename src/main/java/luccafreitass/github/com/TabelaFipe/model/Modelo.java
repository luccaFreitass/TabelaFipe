package luccafreitass.github.com.TabelaFipe.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Modelo(@JsonAlias("modelos") List <DadosVeiculo> modelos) {

}
