package banco.e2e;

import banco.BancoApplication;
import org.springframework.boot.SpringApplication;

/** Entrada exclusiva do classpath de testes. Nunca e empacotada no JAR de producao. */
public class E2eApplication {
    public static void main(String[] args) {
        new SpringApplication(BancoApplication.class, E2eConfiguration.class).run(args);
    }
}
