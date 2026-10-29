package banco.bdd;
import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features="classpath:features", glue="banco.bdd",
    plugin={"pretty","html:target/cucumber/relatorio.html","json:target/cucumber/relatorio.json","junit:target/cucumber/relatorio.xml"})
public class RunCucumberTest { }
