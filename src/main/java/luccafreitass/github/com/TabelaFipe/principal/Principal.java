package luccafreitass.github.com.TabelaFipe.principal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import luccafreitass.github.com.TabelaFipe.model.DadosVeiculo;
import luccafreitass.github.com.TabelaFipe.model.Modelo;
import luccafreitass.github.com.TabelaFipe.model.Veiculo;
import luccafreitass.github.com.TabelaFipe.service.ConsumoApi;
import luccafreitass.github.com.TabelaFipe.service.ConverteDados;

public class Principal {

	private Scanner in = new Scanner(System.in);

	private ConverteDados conversor = new ConverteDados();

	private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";
	private ConsumoApi consumo = new ConsumoApi();

	public void exibeMenu() {

		String menu = "**** Opcoes ****" + "\n  Carro" + "\n  Moto" + "\n  Caminhao" + "\n\n  Digite uma das opcoes: ";

		System.out.println(menu);
		String opcao = in.next();
		in.nextLine();

		String endereco;

		if (opcao.toLowerCase().contains("carr")) {
			endereco = URL_BASE + "carros/marcas";
		} else if (opcao.toLowerCase().contains("mot")) {
			endereco = URL_BASE + "motos/marcas";
		} else {
			endereco = URL_BASE + "caminhoes/marcas";
		}

		String json = consumo.obterDados(endereco);
		
		var marcas = conversor.obterLista(json, DadosVeiculo.class);
		marcas.stream().sorted(Comparator.comparing(DadosVeiculo :: codigo))
		.forEach(System.out::println);

		System.out.println("Informe o código da marca para consulta: ");
		int codMarca = in.nextInt();
		in.nextLine();

		endereco = endereco + "/" + codMarca + "/modelos";
		json = consumo.obterDados(endereco);
		
		var modeloLista = conversor.obterDados(json, Modelo.class);
		System.out.println("\nModelos dessa marca: ");
		modeloLista.modelos().stream()
		.sorted(Comparator.comparing(DadosVeiculo :: codigo))
		.forEach(System.out::println);

		

		System.out.println("Digite um trecho do nome do veículo para consulta: ");
		String nomeVeiculo = in.next();
		in.nextLine();
		
		List <DadosVeiculo> modelosFiltrados = modeloLista.modelos().stream()
				.filter(m -> m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
				.collect(Collectors.toList()); 
		
		System.out.println("\nModelos Filtrados: ");
		modelosFiltrados.forEach(System.out::println);
		
		System.out.println("\nDigite o codigo do modelo: ");
		var codModelo = in.nextInt();
		
		endereco += "/" + codModelo + "/anos";
		json = consumo.obterDados(endereco);
		
		List <DadosVeiculo> anos = conversor.obterLista(json, DadosVeiculo.class);
		List <Veiculo> veiculos = new ArrayList<>();
		
		for (int i = 0; i < anos.size(); i++) {
			String enderecoAnos = endereco + "/" + anos.get(i).codigo();
			json = consumo.obterDados(enderecoAnos);
			Veiculo veiculo = conversor.obterDados(json, Veiculo.class);
			veiculos.add(veiculo);
		}
		
		System.out.println("Todos os veiculos filtrados com avaliacoes por ano: ");
		veiculos.forEach(System.out::println);

	}

}
