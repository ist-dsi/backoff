import com.typesafe.config.ConfigFactory
import pt.ulisboa.tecnico.dsi.Backoff


println(ConfigFactory.load().root().render())
//Backoff.constantFunction()(1)