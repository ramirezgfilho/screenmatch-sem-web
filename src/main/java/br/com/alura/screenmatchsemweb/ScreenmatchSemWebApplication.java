package br.com.alura.screenmatchsemweb;

import br.com.alura.screenmatchsemweb.principal.Principale;
import br.com.alura.screenmatchsemweb.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchSemWebApplication implements CommandLineRunner {

	@Autowired
	private SerieRepository repository;

	public static void main(String[] args) {

		SpringApplication.run(ScreenmatchSemWebApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principale principal = new Principale(repository);

		principal.exibemenu();





	}
}
